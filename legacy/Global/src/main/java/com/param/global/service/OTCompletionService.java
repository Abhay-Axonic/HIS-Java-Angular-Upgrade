package com.param.global.service;

import in.param.exception.ApplicationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.Unit;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.OperationsCompletionInventory;
import com.param.entity.model.pharmacy.OperationsCompletionInventoryDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.SaleDetailPayeeMapper;
import com.param.entity.model.pharmacy.SaleType;
import com.param.global.billing.dao.IContractConsumptionDao;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IOTCompletionDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.ItemsSearchResponse;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.procedure.dto.OTCompletionItemRequest;
import com.param.global.procedure.dto.OTCompletionRequest;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.ot.dao.IImplantIndentAcceptanceOTDao;
import com.param.service.global.SessionService;
import com.param.service.global.model.SaleDetailPackageMapper;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
public class OTCompletionService implements IOTCompletionService,ICommonConstants {
	final static Logger logger = Logger.getLogger(OTCompletionService.class);


	private Integer screenId = Screen.OTCompletion.getScreen();

	private com.param.entity.model.master.Screen screen;
	
	private Integer issueScreenId = Screen.PendingIssues.getScreen();

    @Autowired
    private SessionService sessionService;

	@Autowired
	IOTCompletionDao OTCompletionDao;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IOrderMasterService iOrderMasterService;
	
	@Autowired
	IImplantIndentAcceptanceOTDao iImplantIndentAcceptanceDao;
	
	@Autowired
	IContractConsumptionDao iContractConsumptionDao;
	
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	
	@Autowired
	IWalkInSalesService iWalkInSalesService;

	@Override
	@Transactional
	public List<ItemsSearchResponse> itemsSearch(boolean isImplant, String search, Integer storeId, Integer unitId) {
		return OTCompletionDao.itemsSearch(isImplant, search, storeId, unitId);
	}

	@Override
	@Transactional
	public CommonResponse saveOTCompletion(OTCompletionRequest request)
	{
		try {
			    /*Integer patientId = request.getPatientId();
				Store storeNew = GlobalDao.getStore(request.getStoreId());
				Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/
				
				String OTNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId() != null ? request.getUnitId() :sessionService.getUnitId(), screenId);
				CommonResponse commonResponse = new CommonResponse();
				
				OperationsCompletionInventory completionInventory = new OperationsCompletionInventory(
						(request.getAdmissionId()!=null && request.getAdmissionId()>0)?new Admission(request.getAdmissionId()):null, new Store(request.getStoreId()), request.getIsMedication(),
				request.getIsImplant(), request.getIsConsumable());
				completionInventory.setCreateDefaults( request.getOrganizationId() != null ? request.getOrganizationId() :sessionService.getOrganizationId(),  request.getUnitId() != null ? request.getUnitId() :sessionService.getUnitId(), sessionService.getUserId());
				//sessionService.setEntityDefaults(completionInventory);
				
				VisitType visitType = null;
				SaleType saleType = null;
				if (request.getVisitTypeId() != null)
					visitType = GlobalDao.getVisitType(request.getVisitTypeId());
				if (visitType != null) {
					if (visitType.getVisitTypeCode().equals("OP"))
						saleType = new SaleType(2);
					else if (visitType.getVisitTypeCode().equals("IP"))
						saleType = new SaleType(1);
					else if (visitType.getVisitTypeCode().equals("DC"))
						saleType = new SaleType(4);
					else if (visitType.getVisitTypeCode().equals("ER"))
						saleType = new SaleType(1);
					else
						throw new RuntimeException("Invalid visit type");
				}
				
				List itemList =  request.getItems();
				request.setItemList(itemList);
				Response otContractRes =iMPackageMasterService.consumeItemFromPackageContract(request,0);
				OTCompletionRequest contractReq = (OTCompletionRequest) otContractRes.getObject();

				  Double totalSelfPayable = contractReq.getItems().stream() .filter(i
				  -> i.getSelfPayable()!=null) .mapToDouble(i -> i.getSelfPayable()).sum();
				  
				  Double totalCreditPayable = contractReq.getItems().stream()
				  .filter(i -> i.getCreditPayable()!=null) .mapToDouble(i ->
				  i.getCreditPayable()).sum();	
				
				  
				  Sale sale = new Sale(null,
							 request.getDiscount(),
							 null, null,
							 null,
							(request.getNetAmount() != null) ? request.getNetAmount() : null,
							(request.getNetAmount() != null) ? request.getNetAmount() : null, 0.00,
							(request.getPatientId() != null) ? new PatientRegistration(request.getPatientId()) : null,
							 request.getRoundOff(),
							new Store(request.getStoreId()),
							(request.getTaxAmount() != null) ? request.getTaxAmount() : null,
							(request.getTotalAmount() != null) ? request.getTotalAmount() : null,
							 request.getTotalDiscountAmount(),
							(request.getVisitTypeId() != null) ? new VisitType(request.getVisitTypeId()) : null, saleType, null,
							totalSelfPayable, totalCreditPayable, Screen.OTCompletion.getScreen());
				     sale.setBillNo(OTNumber);
					if (request.getAdmissionId() != null && request.getAdmissionId()>0)
						sale.setAdmission(new Admission(request.getAdmissionId()));
					else if (request.getEncounterId() != null && request.getEncounterId()>0)
						sale.setEncounter(new Encounter(request.getEncounterId()));
				
					sale.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
//				sessionService.setEntityDefaults(sale);
					sale.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
			    SaleDetail details = null;
			    Double netAmount = 0.00;
		for (CommonBillingItemsDto items :  contractReq.getItemList())
		{
			OperationsCompletionInventoryDetail operationsCompletionInventoryDetail=	new OperationsCompletionInventoryDetail(new Batch(items.getBatchId()), items.getRate(), items.getTotalAmount(),
					items.getUomTypeId() != null ? new UomType(items.getUomTypeId()) : null,items.getUomUnitId() != null ? new UomUnit(items.getUomUnitId()) : null, items.getQuantity());
			operationsCompletionInventoryDetail.setCreateDefaults(request.getOrganizationId() != null ? request.getOrganizationId() :sessionService.getOrganizationId(), request.getUnitId() != null ? request.getUnitId() :sessionService.getUnitId(), sessionService.getUserId());  	
			//sessionService.setEntityDefaults(operationsCompletionInventoryDetail);
					completionInventory.addOperationsCompletionInventoryDetail(operationsCompletionInventoryDetail);
					
					Batch batch = GlobalDao.getBatch(request.getStoreId(), items.getBatchId(),null);
				/*
				 * Integer leaseQuantity = (batch!=null && batch.getBatchUomList()!=null&&
				 * batch.getBatchUomList().size()>0)?Util.GetLeaseQuantity(items.getQuantity(),
				 * batch.getBatchUomList(), items.getUomTypeId() != null ? new
				 * UomType(items.getUomTypeId()) : null):items.getQuantity();
				 */
					
					SaleDetail saleDetail = new SaleDetail(batch,items.getDiscountAmount(),
							items.getLeaseQuantity(), items.getNetAmount(), null,
						  null, items.getQuantity(), items.getRate(), items.getTaxAmount(), items.getTaxId()!=null?new Tax(items.getTaxId()):null,
							items.getTaxPercentage(), items.getTotalAmount(),
							items.getUnitDiscountAmount(), 
							items.getUomTypeId() != null ? new UomType(items.getUomTypeId()) : null,
							items.getUomUnitId() != null ? new UomUnit(items.getUomUnitId()) : null,
							new Status(com.param.common.constants.Status.Pending.getStatus()), items.getSelfPayable(),
							items.getCreditPayable(), items.getPayeeId(), items.getProductCategoryId(),items.getPackageConsumptionAmt());
					saleDetail.setConsessionAmount(items.getConsessionAmount());
					saleDetail.setConsessionPercentage(items.getConsessionPercentage());
					saleDetail.setUnitConsessionAmount(items.getUnitConsessionAmount());
					saleDetail.setItemId(items.getItemId());
					netAmount = netAmount + items.getNetAmount();
					//For package
					List<SaleDetailPackageMapperDto> packageMppr = items.getSaleDetailPackageMapperDtoList();
					if (packageMppr != null && packageMppr.size() > 0) 
					{
						List<SaleDetailPackageMapper> pkgMpprList = new ArrayList<SaleDetailPackageMapper>();
						for (SaleDetailPackageMapperDto dto : packageMppr) 
						{
							SaleDetailPackageMapper mpper = new SaleDetailPackageMapper();
							mpper.setOrganizationId(request.getOrganizationId() != null ? request.getOrganizationId() :sessionService.getOrganizationId());
							mpper.setUnitId( request.getUnitId() != null ? request.getUnitId() :sessionService.getUnitId());
							mpper.setItemId(items.getItemId());
							mpper.setStatus('A');
							mpper.setIsPackageItem(dto.getIsPackageItem());
							mpper.setPackageId(dto.getPackageId());
							mpper.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
							mpper.setPacakgeConsumptionServiceDetailId(dto.getPacakgeConsumptionServiceDetailId());
							mpper.setPackageConsumptionCapDetailsId(dto.getPackageConsumptionCapDetailsId());
							mpper.setPackageConsumptionEODetailId(dto.getPackageConsumptionEODetailId());
							mpper.setPackageAmount(BigDecimal.valueOf(dto.getPackageAmount()));
							mpper.setPackageQty(dto.getPackageQty());
							mpper.setCapOrQty(dto.getCapOrQty());
							mpper.setRate(BigDecimal.valueOf(dto.getRate()));
							mpper.setSaleDetail(saleDetail);
							pkgMpprList.add(mpper);
						}
						saleDetail.setSaleDetailPackageMapperList(pkgMpprList);
					} 
					
					//For Contract
					List<SaleDetailPayeeMapperDto> payeeMapper = items.getSaleDetailPayeeMapperDtoList();
					if (payeeMapper != null && payeeMapper.size() > 0) 
					{
						List<SaleDetailPayeeMapper> payeeList = new ArrayList<SaleDetailPayeeMapper>();
						for (SaleDetailPayeeMapperDto dto : payeeMapper) {
							SaleDetailPayeeMapper mpper = new SaleDetailPayeeMapper();
							mpper.setOrganizationId(request.getOrganizationId() != null ? request.getOrganizationId() :sessionService.getOrganizationId());
							mpper.setUnitId( request.getUnitId() != null ? request.getUnitId() :sessionService.getUnitId());
							mpper.setItemId(items.getItemId());
							mpper.setStatus('A');
							mpper.setContractId(dto.getContractId());
							mpper.setPayeeId(dto.getPayeeId());
							mpper.setPayeeAmount(dto.getPayeeAmount());
							mpper.setQuantity(dto.getQuantity());
							saleDetail.setPayeeId(dto.getPayeeId());
							mpper.setSaleDetail(saleDetail);
							payeeList.add(mpper);
						}
						saleDetail.setSaleDetailPayeeMapperList(payeeList);
					}
					saleDetail.setCreateDefaults(request.getOrganizationId() != null ? request.getOrganizationId() :sessionService.getOrganizationId(),  request.getUnitId() != null ? request.getUnitId() :sessionService.getUnitId(), sessionService.getUserId());
					//sessionService.setEntityDefaults(saleDetail);
					sale.addSaleDetailsList(saleDetail);
				if(items.getIssueId()!=null) {
					OTCompletionDao.updateIssue(sale,items.getIssueId());
				}
		     }
		     sale.setOtCompletion(true);
		     sale.setNetAmount(netAmount);
		     sale.setFromBillableConsumption(true);
		     sale.setCreateDefaults(request.getOrganizationId() != null ? request.getOrganizationId() :sessionService.getOrganizationId(),  request.getUnitId() != null ? request.getUnitId() :sessionService.getUnitId(), sessionService.getUserId());
		     
		     iWalkInSalesService.saveGlobalSale(sale);	
			return commonResponse;	
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	/*private CommonResponse updateStoreStock(Integer storeId, Long batchId, Integer usedQuantity, Integer uomType,
			OperationsCompletionInventory completionInventory, Sale sale, OTCompletionRequest request)
	{
		CommonResponse response = new CommonResponse();
		screen = GlobalDao.getScreen(screenId);
		//System.out.println("storeIDInside UpdateStock:-"+storeId);
		Batch batch = GlobalDao.getBatch(storeId, batchId);
		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId,0);//0 itemId
		// Get lease quantity
		Integer leaseQuantity = Util.GetLeaseQuantity(usedQuantity, batch.getBatchUomList(), uomType);
		if (leaseQuantity < avaialableStoreStock)
		{
			Integer balanceQuantity = avaialableStoreStock - leaseQuantity;
			// Deduct issued quantity from the batch store stock
			batch.getStoreStockList().get(0).settQty(leaseQuantity);
			batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);
			// Add stock transaction
			StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
					batch.getStoreStockList().get(0).getStore());
			sessionService.setEntityDefaults(stockTransaction);
			batch.addStockTransaction(stockTransaction);
			for (OTCompletionItemRequest items : request.getItems())
			{
				IssueDetailBatchMapper issueDetailBatchMapper=	new IssueDetailBatchMapper(new IssueDetail(issueDetail.getId()), new Batch(items.getBatchId()),
						items.getUomTypeId() != null ? new UomType(items.getUomTypeId()) : null,items.getUomUnitId() != null ? new UomUnit(items.getUomUnitId()) : null,
						new UomType(items.getUomTypeId()), new UomUnit(items.getUomUnitId()),
						items.getUsedQuantity(), items.getUsedQuantity(), items.getNetAmount());
				issueDetail.addBatch(issueDetailBatchMapper);
			}
			response.setId(completionInventory.getId().longValue());
			response.setMessage("Operations Completion saved successfully");
		} else {
			response.setMessage("Stock Available is less");
		}
		return response;
	}*/




	/*@Override
	@Transactional
	public Response getIssueAcceptanceData() {
		try{
			return OTCompletionDao.getIssueAcceptanceData();
			
		} catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>("error", null, null, null,null);
	}*/

	@Override
	@Transactional
	public Response getIssueAcceptanceData(OtcCompletionInvMedicationDto otcCompletionInvMedicationDto) {
		try {
			return OTCompletionDao.getIssueAcceptanceData(otcCompletionInvMedicationDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response chageOfPayeeOt(List<OrderDetailsMaster> list, Integer admissionId) throws Exception 
	{
		try 
		{
			
		} catch (Exception e) {
		logger.error("chageOfPayeeOt failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, "Failed To Validate Items.",null , null);
	}
}
