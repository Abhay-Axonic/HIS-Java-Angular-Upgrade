package com.param.inventory.store.service.returnableGatepassApproval;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.ReturnableGatepass;
import com.param.entity.model.inventory.ReturnableGatepassAccessories;
import com.param.entity.model.inventory.ReturnableGatepassDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.inventory.returnableBonusBatchExpiry;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.TaxComponentDetailsDto;
import com.param.global.dto.items.UomResponse;
import com.param.global.org.dao.ITaxComponentDetailsDao;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.returnableGatepass.IReturnableBonusBatchExpiryDao;
import com.param.inventory.store.dao.returnableGatepass.IReturnableGatepassDao;
import com.param.inventory.store.dto.returnableGatepass.ItemRequest;
import com.param.inventory.store.dto.returnableGatepass.ItemResponse;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassDropdownResponse;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassRequest;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassResponse;
import com.param.inventory.store.dto.returnableGatepass.accessoriesResponse;
import com.param.inventory.store.dto.returnableGatepass.accessoriesReuest;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ReturnableGatepassApprovalService")
public class ReturnableGatepassApprovalService implements IReturnableGatepassApprovalService {

	private static final Logger logger = Logger.getLogger(ReturnableGatepassApprovalService.class);

	@Autowired
	private SessionService sessionService;

	private Integer screenId = Screen.ReturnableGatepassApproval.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IReturnableGatepassDao ReturnableGatepassDao;
	
	
	@Autowired
	ITaxComponentDetailsDao iTaxComponentDetailsDao;
	
	@Autowired
    IReturnableBonusBatchExpiryDao ReturnableBonusBatchExpiryDao;

	@Override
	@Transactional
	public ReturnableGatepassDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		ReturnableGatepassDropdownResponse response = new ReturnableGatepassDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList(0, 0)) {
			response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}

		return response;
	}

	@Override
	@Transactional
	public Object getReturnableGatepassList(ReturnableGatepassRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return ReturnableGatepassDao.getReturnableGatepassList(request, screenId);
	}

	@Override
	@Transactional
	public Object getReturnableGatepass(Integer id, Integer orgId, Integer unitId) {
		/*
		 * ReturnableGatepassResponse response =
		 * ReturnableGatepassDao.getReturnableGatepassResponse(id);
		 * response.setItems(ReturnableGatepassDao.getReturnableGatepassDetails(id));
		 * return response;
		 */

		ReturnableGatepass returnableGatepass = ReturnableGatepassDao.getReturnableGatepass(id);

		ReturnableGatepassResponse response = new ReturnableGatepassResponse(returnableGatepass.getId(),
				returnableGatepass.getRetGpNo(), returnableGatepass.getAddedDate(),
				returnableGatepass.getStore().getStoreId(), returnableGatepass.getStore().getStoreName(),
				returnableGatepass.getVendor().getId(), returnableGatepass.getVendor().getName(),
				returnableGatepass.getAssetType().getId(), returnableGatepass.getAssetType().getType(),
				returnableGatepass.getRequestType().getId(), returnableGatepass.getRequestType().getType(),
				returnableGatepass.getReferenceNo(), returnableGatepass.getApprovalStatus().getId(),
				returnableGatepass.getApprovalStatus().getStatus(), returnableGatepass.getTotalAmt(),
				returnableGatepass.getTotalTaxAmount(), returnableGatepass.getNetAmount(),
				returnableGatepass.getStatus().getId(), returnableGatepass.getStatus().getStatus(),
				returnableGatepass.getRemark(), returnableGatepass.getAddedBy().getUserName());

		if (returnableGatepass.getAddedBy() != null)
			response.setRequestedBy(returnableGatepass.getAddedBy().getUserName());

		for (ReturnableGatepassDetail detail : returnableGatepass.getReturnableGatepassDetails()) {

			if (!detail.getIsDeleted()) {

				if (response.getAssetTypeId() == Constants.consumableId) {

					/*
					 * response.addItems(new
					 * ItemResponse(detail.getId(),detail.getBatch().getId(),detail.getVendor().
					 * getId(), detail.getUomType().getId(),
					 * detail.getUomUnit().getId(),detail.getTax().getId(),detail.getTaxPercentage()
					 * , detail.getReturnQty(), detail.getTotalAmt(),detail.getTotalTaxAmount(),
					 * detail.getNetAmount(), detail.getStatus().getId(), detail.getApprovedQty(),
					 * detail.getRejectQty(),detail.getBatch().getItem().getId(),
					 * detail.getBatch().getItem().getCode(),detail.getBatch().getItem().getName(),
					 * detail.getUomType().getType(), detail.getUomUnit().getUnits(),
					 * detail.getBatch().getBatchNo(), detail.getBatch().getLotNo()!=null ?
					 * detail.getBatch().getLotNo() :
					 * "-",detail.getBatch().getExpiryDate(),detail.getVendor().getName(),
					 * detail.getBatch().getStoreStockList().get(0).getAvailableQty(),
					 * detail.getBatch().getCop(), detail.getTax().getName()));
					 */

					/* start code by minesh for clubbed batch */
					
					int getTotalAvailableQty=0;
					List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(
							detail.getBatch().getId(), returnableGatepass.getStore().getStoreId(), unitId);
					if(response.getRequestTypeId()==15)
					{
						Integer getList = ReturnableGatepassDao.getQuantity(detail.getId());
						getTotalAvailableQty= getList;
					}
					else
					{
						getTotalAvailableQty = StoreStockList.stream().mapToInt(x -> x.getAvailableQty()).sum();
	                    int getleaseQty=Util.GetLeaseQuantity(getTotalAvailableQty, detail.getBatch().getBatchUomList(), detail.getUomType());
						getTotalAvailableQty = Util.ConvertQuantity(Long.valueOf(getleaseQty),
								detail.getBatch().getBatchUomList(), detail.getUomType()).intValue();
					}
					 
					Integer gatepassId = ReturnableGatepassDao.getid(detail.getId());
					Integer detailApprovedQuantity = ReturnableGatepassDao.getDetailApprovedQantity(gatepassId,response.getRequestTypeId());
					if(response.getRequestTypeId()==15)
					{
						getTotalAvailableQty=getTotalAvailableQty-detailApprovedQuantity;
					}
					 Integer grnQty = ReturnableGatepassDao.getDetailApprovedQantity1(gatepassId);
					 Double totalDiscountAmount = 0.00;
                     Double totalAmount = 0.00;
					 if(gatepassId == null){
						 totalDiscountAmount = ReturnableGatepassDao.totalDiscountAmountUsingBatchId(detail.getBatch().getId(),detail.getId()); 
						 totalAmount = ReturnableGatepassDao.totalAmountUsingBatchId(detail.getBatch().getId(),detail.getId()); 
					 }
					 else{
						 totalDiscountAmount = ReturnableGatepassDao.totalDiscountAmount(gatepassId); 
						 totalAmount = ReturnableGatepassDao.totalAmount(gatepassId); 
					 }
					
					 Double actualDiscountPercentage=((totalDiscountAmount!=null ? totalDiscountAmount :0.00)*100)/(totalAmount!=null ? totalAmount:0.00);
					ItemResponse ires =new ItemResponse(detail.getId(), detail.getBatch().getId(),
							detail.getVendor().getId(), detail.getUomType().getId(), detail.getUomUnit().getId(),
							detail.getTax().getId(), detail.getTaxPercentage(), detail.getReturnQty(),detailApprovedQuantity,
							detail.getTotalAmt(), detail.getTotalTaxAmount(), detail.getNetAmount(),
							detail.getStatus().getId(), detail.getApprovedQty(), detail.getRejectQty(),
							detail.getBatch().getItem().getId(), detail.getBatch().getItem().getCode(),
							detail.getBatch().getItem().getName(), detail.getUomType().getType(),
							detail.getUomUnit().getUnits(), detail.getBatch().getBatchNo(),
							detail.getBatch().getLotNo() != null ? detail.getBatch().getLotNo() : "-",
							detail.getBatch().getExpiryDate(), detail.getVendor().getName(), getTotalAvailableQty,
							detail.getBatch().getCop(), detail.getTax().getName(), detail.getBatch().getItem().getIsThreeUOMApplicable());
					 if(detail.getGoodReceiptNoteDetail() != null) {
						 Integer getBonusTypeId=GlobalDao.getBonusTypeId(detail.getGoodReceiptNoteDetail().getId());
						 ires.setBonusUomTypeId(getBonusTypeId);
					 }
					ires.setTotalDiscount(detail.getTotalDiscount());
					ires.setActualDiscountPercentage(actualDiscountPercentage);
					ires.setTotalGRNQuantity(grnQty);
					List<UomResponse> uomList = GlobalDao.getBatchUomList(detail.getBatch().getId());
					ires.setUomList(uomList);
                    List<returnableBonusBatchExpiry> getList= GlobalDao.getReturnableBonusBatchExpiryByGrnDetailId(detail.getId());
                    List<com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry> getList3=new ArrayList<>();
                    for (returnableBonusBatchExpiry getList1 : getList) {
                        com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry getList2=new com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry();
                        getList2.setBatchNo(getList1.getBatchNo());
                        getList2.setBonusQuantity(getList1.getBonusQuantity());
                        getList2.setExpiryDate(getList1.getExpiryDate());
                        getList2.setReturnableBonusBatchExpiryId(getList1.getReturnableBonusBatchExpiryId());
                        getList2.setBatchId(getList1.getBatchId());
                        Integer returnedBonusQty=GlobalDao.getSumOfReturnedQty(getList2.getBatchId());
                        getList2.setReturnedQuantity(returnedBonusQty);
                        getList2.setReturnQuantity(getList1.getReturnQuantity());
                        getList2.setGrnDetailId(getList1.getGrnDetailId());
                        getList2.setReturnableGatepassDetailId(getList1.getReturnableGatepassDetailId());
                        getList3.add(getList2);
                    }
                    ires.setReturnableBonusBatchExpiry(getList3);
					
					response.addItems(ires);
					/* end code by minesh for clubbed batch */

				} else if (response.getAssetTypeId() == Constants.fixedAssetId) {

					/*
					 * response.addItems(new
					 * ItemResponse(detail.getId(),detail.getBatch().getId(),detail.getVendor().
					 * getId(), detail.getTax().getId(),detail.getTaxPercentage(),
					 * detail.getReturnQty(), detail.getTotalAmt(),detail.getTotalTaxAmount(),
					 * detail.getNetAmount(), detail.getStatus().getId(), detail.getApprovedQty(),
					 * detail.getRejectQty(),detail.getBatch().getItem().getId(),
					 * detail.getBatch().getItem().getCode(),detail.getBatch().getItem().getName(),
					 * detail.getBatch().getBatchNo(),
					 * detail.getBatch().getExpiryDate(),detail.getVendor().getName(),
					 * detail.getBatch().getStoreStockList().get(0).getAvailableQty(),
					 * detail.getBatch().getCop(), detail.getTax().getName(),
					 * PopulateAccessories(ReturnableGatepassDao.
					 * getReturnableGatepassAccessoriesList(detail.getId()))));
					 */

					/* start code by minesh for clubbed batch */
					int getTotalAvailableQty=0;
					List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(
							detail.getBatch().getId(), returnableGatepass.getStore().getStoreId(), unitId);
					
					if(response.getRequestTypeId()==15)
					{
						Integer getList = ReturnableGatepassDao.getQuantity(detail.getId());
						getTotalAvailableQty= getList;
					}
					else
					{
						 getTotalAvailableQty = StoreStockList.stream().mapToInt(x -> x.getTotalQty()).sum();
					}
					 
					Integer gatepassId = ReturnableGatepassDao.getid(detail.getId());
					Integer detailApprovedQuantity = ReturnableGatepassDao.getDetailApprovedQantity(gatepassId,response.getRequestTypeId());
					 Integer grnQty = ReturnableGatepassDao.getDetailApprovedQantity1(gatepassId);
					 Double totalDiscountAmount = ReturnableGatepassDao.totalDiscountAmount(gatepassId); 
					 Double totalAmount = ReturnableGatepassDao.totalAmount(gatepassId); 
					 Double actualDiscountPercentage=((totalDiscountAmount!=null ? totalDiscountAmount :0.00)*100)/(totalAmount!=null ? totalAmount:0.00);
					 if(response.getRequestTypeId()==15)
						{
							getTotalAvailableQty=getTotalAvailableQty-detailApprovedQuantity;
						}
				ItemResponse ires=new ItemResponse(detail.getId(), detail.getBatch().getId(),
							detail.getVendor().getId(), detail.getTax()!=null ? detail.getTax().getId():null, detail.getTaxPercentage()!=null ? detail.getTaxPercentage():null,
							detail.getReturnQty(),detailApprovedQuantity, detail.getTotalAmt(), detail.getTotalTaxAmount(),
							detail.getNetAmount(), detail.getStatus().getId(), detail.getApprovedQty(),
							detail.getRejectQty(), detail.getBatch().getItem().getId(),
							detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
							detail.getBatch().getBatchNo(),detail.getBatch().getExpiryDate()!=null ?  detail.getBatch().getExpiryDate():null,
							detail.getVendor().getName(), getTotalAvailableQty, detail.getBatch().getCop(),
							detail.getTax()!=null ? detail.getTax().getName() :null, PopulateAccessories(
									ReturnableGatepassDao.getReturnableGatepassAccessoriesList(detail.getId())),detail.getTotalDiscount(), detail.getBatch().getItem().getIsThreeUOMApplicable());
					ires.setTotalGRNQuantity(grnQty);
					ires.setActualDiscountPercentage(actualDiscountPercentage);
					List<returnableBonusBatchExpiry> getList= GlobalDao.getReturnableBonusBatchExpiryByGrnDetailId(detail.getId());
                    List<com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry> getList3=new ArrayList<>();
                    for (returnableBonusBatchExpiry getList1 : getList) {
                        com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry getList2=new com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry();
                        getList2.setBatchNo(getList1.getBatchNo());
                        getList2.setBonusQuantity(getList1.getBonusQuantity());
                        getList2.setExpiryDate(getList1.getExpiryDate());
                        getList2.setReturnableBonusBatchExpiryId(getList1.getReturnableBonusBatchExpiryId());
                        getList2.setBatchId(getList1.getBatchId());
                        Integer returnedBonusQty=GlobalDao.getSumOfReturnedQty(getList2.getBatchId());
                        getList2.setReturnedQuantity(returnedBonusQty);
                        getList2.setReturnQuantity(getList1.getReturnQuantity());
                        getList2.setGrnDetailId(getList1.getGrnDetailId());
                        getList2.setReturnableGatepassDetailId(getList1.getReturnableGatepassDetailId());
                        getList3.add(getList2);
                    }
                    ires.setReturnableBonusBatchExpiry(getList3);
					response.addItems(ires);
					/* end code by minesh for clubbed batch */

				}
			}
		}

		return response;

	}

	private List<accessoriesResponse> PopulateAccessories(List<ReturnableGatepassAccessories> InwardAccessories) {
		List<accessoriesResponse> response = new ArrayList<accessoriesResponse>();

		for (ReturnableGatepassAccessories accessories : InwardAccessories) {
			response.add(new accessoriesResponse(accessories.getId(), accessories.getBatch().getId(),
					accessories.getBatch().getItem().getId(), accessories.getBatch().getItem().getCode(),
					accessories.getBatch().getItem().getName(), accessories.getQuantity(),
					accessories.getSpecification(), accessories.getApprovedQty()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object approveReturnableGatepass(ReturnableGatepassRequest request) {
		screen = GlobalDao.getScreen(screenId);

		ReturnableGatepass gatepass = ReturnableGatepassDao.getReturnableGatepass(request.getId());

		if (request.getTotalAmount() != null && request.getTotalAmount() > 0) {
			gatepass.setTotalAmt(request.getTotalAmount());
		}

		if (request.getTotalTaxAmount() != null && request.getTotalTaxAmount() > 0) {
			gatepass.setTotalTaxAmount(request.getTotalTaxAmount());
		}

		if (request.getNetAmount() != null && request.getNetAmount() > 0) {
			gatepass.setNetAmount(request.getNetAmount());
		}
		for (ReturnableGatepassDetail detail : gatepass.getReturnableGatepassDetails()) {
			for (ItemRequest item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId()) && detail.getStatus().getId()
						.equals(com.param.common.constants.Status.PendingApproval.getStatus())) {
					Long mainBatchId=detail.getBatch().getId();
					if (request.getAssetTypeId() == Constants.consumableId) {
						request.setIsMultiBonusBatch(false);
						Integer itemApprovedQty=item.getApprovedQuantity();
	                    if(item.getReturnableBonusBatchExpiry().size()>0)
	                    {
	                    	request.setIsMultiBonusBatch(true);
	                        for (com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry bonusBatches : item.getReturnableBonusBatchExpiry()) {
	                        	detail.setBatch(this.batchStoreTransaction(bonusBatches.getBatchId(),
	    								gatepass.getStore().getStoreId(), bonusBatches.getReturnQuantity(), new UomType(item.getBonusUomTypeId()),
	    								gatepass.getRequestType().getId(), request.getUnitId(), request,detail,0));
	                        }
	                    }
	                    if(item.getReturnableBonusBatchExpiry().size()>0)
	                    {
	                    	item.setApprovedQuantity(itemApprovedQty);
	                    }
						detail.setBatch(this.batchStoreTransaction(mainBatchId,
								gatepass.getStore().getStoreId(), item.getApprovedQuantity(), detail.getUomType(),
								gatepass.getRequestType().getId(), request.getUnitId(), request,detail,item.getRejectedQuantity()));

						// change by minesh for clubbed and consignment batch
						/*
						 * detail.setBatch(this.batchStoreTransactionForClubBatch(detail.getBatch().
						 * getId(), gatepass.getStore().getStoreId(), item.getApprovedQuantity(),
						 * detail.getUomType(), gatepass.getRequestType().getId()));
						 */
						// end
					} else if (request.getAssetTypeId() == Constants.fixedAssetId) {
						Integer itemApprovedQty=item.getApprovedQuantity();
	                    if(item.getReturnableBonusBatchExpiry().size()>0)
	                    {
	                        for (com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry bonusBatches : item.getReturnableBonusBatchExpiry()) {
	                        	detail.setBatch(this.batchStoreTransactionForFixed(bonusBatches.getBatchId(),null,
	    								gatepass.getStore().getStoreId(), bonusBatches.getReturnQuantity(),0,
	    								gatepass.getRequestType().getId(), request.getUnitId(), request,detail,0));
	                        }
	                    }
	                    
	                    if(item.getReturnableBonusBatchExpiry().size()>0)
	                    {
	                    	item.setApprovedQuantity(itemApprovedQty);
	                    }
						  detail.setBatch(this.batchStoreTransactionForFixed(mainBatchId,null, gatepass.getStore().getStoreId(),
									item.getApprovedQuantity(),0,
									gatepass.getRequestType().getId(), request.getUnitId(), request,detail,item.getRejectedQuantity()));
							
							if(item.getAccessories()!=null&&item.getAccessories().size()>0)
							{
								for (accessoriesReuest accessories : item.getAccessories()) {
									detail.setBatch(this.batchStoreTransactionForFixed(detail.getBatch().getId(),
											accessories.getId(), gatepass.getStore().getStoreId(),
											item.getApprovedQuantity(), accessories.getApprovedQuantity(),
											gatepass.getRequestType().getId(), request.getUnitId(), request,detail,item.getRejectedQuantity()));
									detail.getReturnableGatepassAccessoriesList().get(0)
											.setApprovedQty(accessories.getApprovedQuantity());
								}	
							}
						 						 
						 
					}
					detail.approve(item.getApprovedQuantity(), item.getRejectedQuantity(), item.getRejectNote(),
							new Status(item.getStatusId()),
							item.getRejectReasonId() != null ? new RejectReason(item.getRejectReasonId()) : null,
							request.getUserId());
					
					Integer fromGrnOrreturn = 1;   // fromGrnOrreturn is used for saparate the API call in which screen 
					List<TaxComponentDetailsDto> taxDto = iTaxComponentDetailsDao.getTaxComponentDetailsById(detail.getId(),fromGrnOrreturn);
					if(taxDto != null && taxDto.size()>0) {
						
						Double netAmount = detail.getTotalAmt();   // total Amount of grnDetails Amount ItemWise
						Double approvedNetAmount = item.getApprovedQuantity() *(netAmount/detail.getReturnQty());  // total Amount of approved  grnDetails Amount ItemWise
						
						for (TaxComponentDetailsDto taxComponentDetailsDto : taxDto) {
							
							Double taxComponentAmount = (approvedNetAmount / 100)*taxComponentDetailsDto.getTaxComponentPercentage();   // calculation of  tax Component Amount
							
							Double taxAmount =(approvedNetAmount / 100)*taxComponentDetailsDto.getTaxPercentage();
							
							taxComponentDetailsDto.setTaxComponentAmount(taxComponentAmount);
							taxComponentDetailsDto.setTaxAmount(taxAmount);
							
							iTaxComponentDetailsDao.updateTaxComponentDetails(taxComponentDetailsDto);
							
						}
					}
					

					
					
					
					break;
				}
			}
		}

		gatepass.setStatus(new Status(request.getStatusId()));
		gatepass.setApprovalStatus(new Status(request.getApprovalStatusId()));

		gatepass.setUpdatedDate(TimeZoneComponent.getDateByzone(gatepass.getUnitId()));
		// gatepass.setUpdatedBy(gatepass.getUser());
		gatepass.setUpdatedByUser(request.getUserId());

		ReturnableGatepassDao.updateReturnableGatepass(gatepass);
			for (ItemRequest item : request.getItems()) {
	                    if(item.getReturnableBonusBatchExpiry().size()>0)
	                    {	
	                        for (com.param.inventory.store.dto.returnableGatepass.returnableBonusBatchExpiry bonusBatches : item.getReturnableBonusBatchExpiry()) {
	                        	
	                        	Batch batch = GlobalDao.getBatch(gatepass.getStore().getStoreId() != null ? gatepass.getStore().getStoreId() :0, 
	                        			bonusBatches.getBatchId() != null ? bonusBatches.getBatchId() :0 , request.getUnitId() != null ? request.getUnitId():0);
	                        	
	                        	Integer leaseQuantity = (bonusBatches.getReturnQuantity() != null ?  bonusBatches.getReturnQuantity() :0);
	                        	UomType	uomType = new UomType(item.getBonusUomTypeId());
	                        	if(batch != null && uomType != null){
	                        		try{
	                        			
	                        			leaseQuantity = Util.GetLeaseQuantity(leaseQuantity, batch.getBatchUomList(), uomType);
	                        			bonusBatches.setReturnQuantity(leaseQuantity);
	                        		}
	                        		catch(Exception e){
	                        			logger.error("Error while computing lease quantity for bonus batch during gatepass approval", e);
	                        			throw new ServiceException(e.getMessage());
	                        		}
		                        	 	
	                        	}
	                        	 
	                        	ReturnableBonusBatchExpiryDao.updateReturnedQty(bonusBatches);
	                        }
	                    }
				}
		CommonResponse response = new CommonResponse();
		response.setMessage("Returnable gatepass details approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelReturnableGatepass(ReturnableGatepassRequest request) {

		ReturnableGatepass gatepass = ReturnableGatepassDao.getReturnableGatepass(request.getId());

		for (ReturnableGatepassDetail detail : gatepass.getReturnableGatepassDetails()) {
			if (detail.getIsActive() && !detail.getIsDeleted() && detail.getStatus().getId()
					.equals(com.param.common.constants.Status.PendingApproval.getStatus())) {
				detail.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(gatepass.getUnitId()));
				// detail.setUpdatedBy(detail.getUser());
				detail.setUpdatedByUser(request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
			}
		}

		gatepass.setCancelReason(new CancelReason(request.getCancelReasonId()));
		gatepass.setCancelNote(request.getCancelNote());

		gatepass.setStatus(new Status(request.getStatusId()));

		gatepass.setUpdatedDate(TimeZoneComponent.getDateByzone(gatepass.getUnitId()));
		// gatepass.setUpdatedBy(gatepass.getUser());
		gatepass.setUpdatedByUser(request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

		ReturnableGatepassDao.updateReturnableGatepass(gatepass);

		CommonResponse response = new CommonResponse();
		response.setMessage("Returnable gatepass cancelled successfully");

		return response;
	}

	private Batch batchStoreTransaction(Long batchId, Integer storeId, Integer quantity, UomType uomType,
			Integer requestTypeId, Integer unitId, ReturnableGatepassRequest request, ReturnableGatepassDetail d, Integer rejectedQty) {

		// get batch needs to be issued
		Batch batch = GlobalDao.getBatch(storeId, batchId, unitId);
	
		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId());

		// Get lease quantity
		Integer leaseQuantity = quantity;
		
		Integer leaseRejectedQty = rejectedQty;
		
		 leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
		
		 leaseRejectedQty = Util.GetLeaseQuantity(rejectedQty, batch.getBatchUomList(), uomType);
		

		// Calculate store stock balance quantity
		Integer balanceQuantity;
		if(requestTypeId==15) {
			 balanceQuantity = avaialableStoreStock;
		}
		else
		{
			balanceQuantity = avaialableStoreStock - leaseQuantity;
		}
		 
		

		if (balanceQuantity < 0) {
			balanceQuantity = 0;
		}
		// Deduct issued quantity from the batch store stock
		batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);

		// Calculate current batch quantity

		// Set current batch rejected quantity
		if (requestTypeId.equals(5)) {
			batch.setRejectedQuantity(batch.getRejectedQuantity() - leaseQuantity);
		}else if(requestTypeId.equals(15))
		{
			batch.setCurrentQuantity(batch.getCurrentQuantity());
		}
		else {
			batch.setCurrentQuantity(batch.getCurrentQuantity() - leaseQuantity);
		}

		if(requestTypeId==15) {
			
			Boolean inTransit = false;
			if(leaseRejectedQty != null && leaseRejectedQty > 0 ) {
				inTransit = true;
			}
			
			ReturnableGatepassDao.updateIntransitQty(d.getGoodReceiptNoteDetail().getId(),leaseRejectedQty,inTransit,screenId, screen.getCode());
			
		}else {
			
			// Add stock transaction
			StockTransaction stoptr = new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
					batch.getStoreStockList().get(0).getStore(),d.getId());
			stoptr.setOutQty(leaseQuantity);
			stoptr.setTransactionId(d.getReturnableGatepass().getId());
			stoptr.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			batch.addStockTransaction(stoptr);
			
		}
		

		return batch;
	}

	// added by minesh
	/*
	 * private Batch batchStoreTransactionForClubBatch(Long batchId, Integer
	 * storeId, Integer quantity, UomType uomType, Integer requestTypeId) {
	 * 
	 * List<StoreStock> StoreStockList =
	 * GlobalDao.getStoreStockListByBatchIdAndStoreId(batchId, storeId); int
	 * returnQtyFromRequest = quantity;
	 * 
	 * for (StoreStock storeStockObj : StoreStockList) { if (returnQtyFromRequest !=
	 * 0) { Long storeAvaliableQuantity =
	 * Util.ConvertQuantity(Long.valueOf(storeStockObj.getAvailableQty()),
	 * storeStockObj.getBatch().getBatchUomList(), uomType); Long batchIdFromStore =
	 * storeStockObj.getBatch().getId(); if (returnQtyFromRequest <=
	 * storeAvaliableQuantity) { batchStoreTransaction(batchIdFromStore, storeId,
	 * returnQtyFromRequest,uomType, requestTypeId); returnQtyFromRequest = 0; }
	 * else { int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();
	 * batchStoreTransaction(batchIdFromStore, storeId,
	 * storeAvaliableQuantityINT,uomType, requestTypeId); returnQtyFromRequest =
	 * returnQtyFromRequest - storeAvaliableQuantityINT; } } } return
	 * GlobalDao.getBatch(storeId, batchId); }
	 */

	@SuppressWarnings("unlikely-arg-type")
	private Batch batchStoreTransactionForFixed(Long detailBatchId, Integer accessoriesBatchId, Integer storeId,
			Integer detailApprovedQty, Integer accessoriesApprovedty, Integer requestTypeId, Integer unitId,
			ReturnableGatepassRequest request, ReturnableGatepassDetail d, Integer leaseRejectedQty) {
		// get batch needs to be issued
		Batch batch = GlobalDao.getBatch(storeId, detailBatchId, unitId);

		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId());

		// Calculate store stock balance quantity
		Integer balanceQuantity;
		if(requestTypeId==15) {
			 balanceQuantity = avaialableStoreStock;
		}
		else
		{
			 balanceQuantity = (avaialableStoreStock - detailApprovedQty) - accessoriesApprovedty;
		}

		// Deduct issued quantity from the batch store stock
		batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);

		// Calculate current batch quantity

		// Set current batch quantity
		if (requestTypeId.equals(5)) {

		} else {
			batch.setCurrentQuantity(balanceQuantity);

		}

		if(requestTypeId==15) {
			
			Boolean inTransit = false;
			if(leaseRejectedQty != null && leaseRejectedQty > 0 ) {
				inTransit = true;
			}
			
			ReturnableGatepassDao.updateIntransitQty(d.getGoodReceiptNoteDetail().getId(),leaseRejectedQty,inTransit,screenId, screen.getCode());
			
		}else {
			
			if (batch.getId().equals(detailBatchId)) {
				// Add stock transaction
				StockTransaction stockTransaction1 = new StockTransaction(balanceQuantity, screen, screen.getCode(),
						detailApprovedQty, batch.getStoreStockList().get(0).getStore(),d.getId());
				// sessionService.setEntityDefaults(stockTransaction1);
				stockTransaction1.setOutQty(accessoriesApprovedty);
				stockTransaction1.setTransactionId(d.getReturnableGatepass().getId());
				stockTransaction1.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				batch.addStockTransaction(stockTransaction1);

			}
			if (batch.getId().equals(accessoriesBatchId)) {
				// Add stock transaction

				StockTransaction stockTransaction2 = new StockTransaction(balanceQuantity, screen, screen.getCode(),
						accessoriesApprovedty, batch.getStoreStockList().get(0).getStore(),d.getId());
				// sessionService.setEntityDefaults(stockTransaction2);
				stockTransaction2.setOutQty(accessoriesApprovedty);
				stockTransaction2.setTransactionId(d.getReturnableGatepass().getId());
				stockTransaction2.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				batch.addStockTransaction(stockTransaction2);
			}
			
		}
		
		

		return batch;
	}

	// addded by minesh
	/*
	 * private Batch batchStoreTransactionForFixedForClubBatch(Long detailBatchId,
	 * Integer accessoriesBatchId, Integer storeId, Integer detailApprovedQty,
	 * Integer accessoriesApprovedty, Integer requestTypeId) {
	 * 
	 * List<StoreStock> StoreStockList =
	 * GlobalDao.getStoreStockListByBatchIdAndStoreId(detailBatchId, storeId); int
	 * qtyFromRequest = detailApprovedQty;
	 * 
	 * for (StoreStock storeStockObj : StoreStockList) { if (qtyFromRequest != 0) {
	 * Integer storeAvaliableQuantity = storeStockObj.getAvailableQty(); Long
	 * batchIdFromStore = storeStockObj.getBatch().getId(); if (qtyFromRequest <=
	 * storeAvaliableQuantity) {
	 * batchStoreTransactionForFixed(batchIdFromStore,accessoriesBatchId, storeId,
	 * qtyFromRequest,accessoriesApprovedty, requestTypeId); qtyFromRequest = 0; }
	 * else {
	 * 
	 * batchStoreTransactionForFixed(batchIdFromStore,accessoriesBatchId, storeId,
	 * storeAvaliableQuantity,accessoriesApprovedty, requestTypeId); qtyFromRequest
	 * = qtyFromRequest - storeAvaliableQuantity; } } } return
	 * GlobalDao.getBatch(storeId, detailBatchId); }
	 */

	@Override
	@Transactional
	public Object approveReturnableGatepassNew(ReturnableGatepassRequest request) {

		screen = GlobalDao.getScreen(screenId);

		ReturnableGatepass gatepass = ReturnableGatepassDao.getReturnableGatepass(request.getId());

		for (ReturnableGatepassDetail detail : gatepass.getReturnableGatepassDetails()) {
			for (ItemRequest item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId()) && detail.getStatus().getId()
						.equals(com.param.common.constants.Status.PendingApproval.getStatus())) {
					if (request.getAssetTypeId() == Constants.consumableId) {
						detail.setBatch(this.batchStoreTransaction(detail.getBatch().getId(),
								gatepass.getStore().getStoreId(), item.getApprovedQuantity(), detail.getUomType(),
								gatepass.getRequestType().getId(), gatepass.getUnit().getUnitId(), request,detail,item.getRejectedQuantity()));

					} else if (request.getAssetTypeId() == Constants.fixedAssetId) {
						if (item.getId() != null && item.getId().equals(detail.getId())) {

							detail.setBatch(this.batchStoreTransactionForFixed(detail.getBatch().getId(), null,
									gatepass.getStore().getStoreId(), item.getApprovedQuantity(), 0,
									gatepass.getRequestType().getId(), request.getUnitId(), request,detail,item.getRejectedQuantity()));

							if (item.getAccessories() != null && item.getAccessories().size() > 0) {
								for (accessoriesReuest accessories : item.getAccessories()) {
									detail.setBatch(this.batchStoreTransactionForFixed(detail.getBatch().getId(),
											accessories.getId(), gatepass.getStore().getStoreId(),
											item.getApprovedQuantity(), accessories.getApprovedQuantity(),
											gatepass.getRequestType().getId(), request.getUnitId(), request,detail,item.getRejectedQuantity()));
									detail.getReturnableGatepassAccessoriesList().get(0)
											.setApprovedQty(accessories.getApprovedQuantity());
								}
							}

						}
					}
					detail.approve(item.getApprovedQuantity(), item.getRejectedQuantity(), item.getRejectNote(),
							new Status(item.getStatusId()),
							item.getRejectReasonId() != null ? new RejectReason(item.getRejectReasonId()) : null,
							request.getUserId());
					break;
				}
			}
		}

		gatepass.setStatus(new Status(request.getStatusId()));
		gatepass.setApprovalStatus(new Status(request.getApprovalStatusId()));

		gatepass.setUpdatedDate(TimeZoneComponent.getDateByzone(gatepass.getUnitId()));
//		gatepass.setUpdatedBy(gatepass.getUser());
		gatepass.setUpdatedByUser(request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

		ReturnableGatepassDao.updateReturnableGatepass(gatepass);

		CommonResponse response = new CommonResponse();
		response.setMessage("Returnable gatepass details approved successfully");

		return response;

	}
}
