package com.param.inventory.store.service.materialReturn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.MaterialIssueBatchMapper;
import com.param.entity.model.inventory.MaterialIssueDetail;
import com.param.entity.model.inventory.Reject;
import com.param.entity.model.inventory.RejectDetail;
import com.param.entity.model.inventory.Return;
import com.param.entity.model.inventory.ReturnDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.global.common.Response;
import com.param.global.dao.IEmployeeRegistrationDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.org.common.dao.IReasonMasterDao;
import com.param.global.org.common.dto.ReasonMasterDto;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.inventory.store.dao.materialReturn.IMaterialReturnDao;
import com.param.inventory.store.dao.storeStock.IStoreStockDao;
import com.param.inventory.store.dto.materialIssueList.MaterialIssueListDropdownResponse;
import com.param.inventory.store.dto.materialReturn.ItemRespone;
import com.param.inventory.store.dto.materialReturn.MaterialResponse;
import com.param.inventory.store.dto.materialReturn.MaterialReturnIssue;
import com.param.inventory.store.dto.materialReturn.MaterialReurnRequest;
import com.param.inventory.store.dto.materialReturn.ReturnReason;
import com.param.inventory.store.dto.materialReturn.ReturnReasonDropdownResponse;
import com.param.inventory.store.dto.storeIndent.ItemRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.pharmacy.nursing.workstation.dao.patientIssueReturn.IPatientIssueReturnDao;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;
import com.param.procurement.quotation.dao.physicalInspection.IConsignmentStockConversionDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("MaterialReturnService")
public class MaterialReturnService implements IMaterialReturnService {

	private Integer screenId = Screen.MaterialReturn.getScreen();
	private Integer rejectScreenId = Screen.MaterialRejectAcceptance.getScreen();

	private com.param.entity.model.master.Screen screen;

	@Autowired
	IEmployeeRegistrationDao iEmployeeRegistrationDao;
	
	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IMaterialReturnDao MaterialReturnDao;
	@Autowired
	private IPatientIssueReturnDao patientIssueReturnDao;

	@Autowired
	IStoreStockDao StoreStockDao;
	@Autowired
	IConsignmentStockConversionDao consignmentStockConversionDao;
	@Autowired
	IReasonMasterDao iReasonMasterDao;

	@Override
	@Transactional
	public StoreIndentDropdownResponse getDropdowns(Integer unitId, Integer orgId, Integer userId) {

		StoreIndentDropdownResponse response = new StoreIndentDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName(),
					store.getStoreType().getCode().equals("MS")));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}
		return response;
	}

	@Override
	@Transactional
	public ReturnReasonDropdownResponse getDropdownForReturnReason(Integer unitId) {
		ReturnReasonDropdownResponse response = new ReturnReasonDropdownResponse();

		/*
		 * for (com.param.entity.model.master.ReturnReason returnReason :
		 * GlobalDao.getReturnReason(unitId)) { response.addReason(new
		 * ReturnReason(returnReason.getId(), returnReason.getReason())); }
		 */
		ReasonMasterDto reasonMaster = new ReasonMasterDto();
		reasonMaster.setOrganizationId(sessionService.getOrganizationId());
		List<ReasonMaster> rData = GlobalDao.getreasonDropdownListByTypeId(sessionService.getOrganizationId(), 18);
		// com.param.global.common.Response reasonMasterDto =
		// iReasonMasterDao.getReasonMasterList(reasonMaster);
		// List<ReasonMasterDto> reasonMasterDtoList =reasonMasterDto.getListObject();
		for (ReasonMaster l : rData) {
			response.addReason(new ReturnReason(l.getReasonMasterId(), l.getReasonDesc()));
		}

		return response;
	}

	@Override
	@Transactional
	public MaterialIssueListDropdownResponse dropdownMaterialReturnCarrierList(Integer unitId, Integer orgId,
			Integer userId) {

		MaterialIssueListDropdownResponse response = new MaterialIssueListDropdownResponse();

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (User user : GlobalDao.getUser(orgId, unitId)) {
			response.addToCarrier(new com.param.global.dto.global.User(user.getUserId(), user.getUserName()));
		}
		return response;

	}

	@Override
	@Transactional
	public Object getMaterialReturnList(MaterialReurnRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		return MaterialReturnDao.searchMaterialReturnList(request, screenId);
	}

	@Override
	@Transactional
	public Object saveMaterialReturn(MaterialReurnRequest request) {

		Integer storeId = request.getFromStoreId();
		/*
		 * // Integer storeId = request.getToStoreId(); Store storeNew =
		 * GlobalDao.getStore(storeId);
		 * 
		 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		 */

		String returnNumber = GlobalService.getBillNoByGeneratedCode(storeId, request.getUnitId(), screenId);

		Return returnData = new Return(new Status(request.getApprovalStatusId()), new User(request.getCarrierId()),
				returnNumber, request.getIsConsignment(), request.getRemark(), new Status(request.getStatusId()),
				new Store(request.getFromStoreId()), new Store(request.getToStoreId()), request.getNearByExpiry());
		// sessionService.setEntityDefaults(returnData);
		returnData.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
		for (com.param.inventory.store.dto.storeIndent.ItemRequest items : request.getItems()) {
			/*
			 * ReturnDetail returnDetail=new ReturnDetail(new Batch(items.getBatchId()), new
			 * com.param.entity.model.master.ReturnReason(items.getReturnReasonId()),
			 * items.getReturnQuantity(), new Status(items.getStatusId()), new
			 * UomType(items.getUomTypeId()), new UomUnit(items.getUomUnitId()));
			 * sessionService.setEntityDefaults(returnDetail);
			 * returnData.addReturnDetail(returnDetail);
			 */

			/* new code start for clubbatch and consignmentlogic by minesh */
			// List<StoreStock> StoreStockList =
			// GlobalDao.getStoreStockListByBatchIdAndStoreId(items.getBatchId(),storeId,request.getUnitId());
			Integer getMaterialAcceptedQty=GlobalDao.getMaterialAcceptedQty(items.getIssueDetailId());
			Integer getMaterialReturnedQty=GlobalDao.getMaterialReturnedQty(items.getIssueDetailId());
			if(getMaterialAcceptedQty<(getMaterialReturnedQty+(items.getReturnQuantity()+(items.getComsumedQty()!=null ? items.getComsumedQty() :0))))
					{
                CommonResponse response = new CommonResponse();
                response.setExceptionMessage("This transaction already processed by other user");
                return response;
					}
			List<StoreStock> StoreStockList = GlobalDao.getStoreStockByBatchAndStoreByItem(items.getBatchId(), storeId,
					items.getItemId(), request.getUnitId());
			int returnQtyFromRequest = items.getReturnQuantity();
			for (StoreStock storeStockObj : StoreStockList) {
				if (returnQtyFromRequest != 0 && storeStockObj.getStore().getStoreId().equals(storeId)
						&& storeStockObj.getBatch().getId().equals(items.getBatchId())) {
					Long storeAvaliableQuantity = Util.ConvertQuantity(Long.valueOf(storeStockObj.getAvailableQty()),
							storeStockObj.getBatch().getBatchUomList(), new UomType(items.getUomTypeId()));
					Long batchId = storeStockObj.getBatch().getId();
					if (returnQtyFromRequest <= storeAvaliableQuantity) {
						ReturnDetail returnDetail = new ReturnDetail(new Batch(batchId),
								new com.param.adt.master.global.model.ReasonMaster(items.getReturnReasonId()),
								returnQtyFromRequest, new Status(items.getStatusId()),
								new UomType(items.getUomTypeId()), new UomUnit(items.getUomUnitId()));
						returnDetail.setIssueQty(items.getIssueQty());
						returnDetail.setComsumedQty(items.getComsumedQty());
						returnDetail.setItemId(items.getItemId());
						returnDetail.setMaterialIssueDetailId(items.getIssueDetailId());
						// sessionService.setEntityDefaults(returnDetail);
						returnDetail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
								request.getUserId());
						returnData.addReturnDetail(returnDetail);

						returnQtyFromRequest = 0;
					} else {
						int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();
						ReturnDetail returnDetail = new ReturnDetail(new Batch(batchId),
								new com.param.adt.master.global.model.ReasonMaster(items.getReturnReasonId()),
								storeAvaliableQuantityINT, new Status(items.getStatusId()),
								new UomType(items.getUomTypeId()), new UomUnit(items.getUomUnitId()));
						returnDetail.setIssueQty(items.getIssueQty());
						returnDetail.setComsumedQty(items.getComsumedQty());
						returnDetail.setItemId(items.getItemId());
						returnDetail.setMaterialIssueDetailId(items.getIssueDetailId());
						// sessionService.setEntityDefaults(returnDetail);
						returnDetail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
								request.getUserId());
						returnData.addReturnDetail(returnDetail);

						returnQtyFromRequest = returnQtyFromRequest - storeAvaliableQuantityINT;
					}
				}
			}
			/* new code end for clubbatch and consignmentlogic by minesh */

		}
		MaterialReturnDao.saveMaterialReturn(returnData);

		CommonResponse response = new CommonResponse();
		response.setId(returnData.getId().longValue());
		response.setMessage("Material Return saved successfully");

		return response;

	}

	/*
	 * private void BatchMapperStoreStock(Integer storeId, Batch batch, Integer
	 * issueQuantity, UomType uomType) {
	 * 
	 * boolean hasStock = false;
	 * 
	 * for (StoreStock storeStock : batch.getStoreStockList()) { if
	 * (storeStock.getStore().getStoreId().equals(storeId)) {
	 * 
	 * hasStock = true; Integer avaialableStoreStock =
	 * GlobalDao.getStoreStockByBatch(storeId, batch.getId());
	 * 
	 * Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity,
	 * batch.getBatchUomList(), uomType);
	 * 
	 * Integer balanceQuantity = avaialableStoreStock + leaseQuantity;
	 * 
	 * storeStock.setAvailableQty(balanceQuantity);
	 * 
	 * Integer currentQuantity = batch.getCurrentQuantity() + leaseQuantity;
	 * batch.setCurrentQuantity(currentQuantity);
	 * 
	 * batch.addStockTransaction(new StockTransaction(balanceQuantity, screen,
	 * screen.getCode(), leaseQuantity,
	 * batch.getStoreStockList().get(0).getStore())); } } if (!hasStock) {
	 * 
	 * StoreStock StoreStock = new StoreStock(issueQuantity, new Store(storeId), new
	 * Batch(batch.getId()));
	 * 
	 * sessionService.setEntityDefaults(StoreStock);
	 * 
	 * Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId,
	 * batch.getId());
	 * 
	 * Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity,
	 * batch.getBatchUomList(), uomType);
	 * 
	 * Integer currentQuantity = batch.getCurrentQuantity() + leaseQuantity;
	 * batch.setCurrentQuantity(currentQuantity); StockTransaction stockTransaction
	 * = new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
	 * leaseQuantity, batch.getStoreStockList().get(0).getStore());
	 * batch.addStockTransaction(stockTransaction); } }
	 */
	@Override
	@Transactional
	public Object getMaterialReturnDetails(Integer id, Integer unitId) {
		Return returnData = MaterialReturnDao.getMaterialReturn(id);

		String userName = "";
		if(returnData.getCarrier() != null) {
			userName = iEmployeeRegistrationDao.getUserNamwByUserId( returnData.getCarrier().getUserId());
		}
		
		
		MaterialResponse response = new MaterialResponse(returnData.getId(), returnData.getAddedDate(),
				returnData.getReturnNo(), returnData.getFromStore().getStoreId(),
				returnData.getFromStore().getStoreName(), returnData.getToStore().getStoreId(),
				returnData.getToStore().getStoreName(), returnData.getAddedBy().getUserName(),
				returnData.getIsConsignment(), returnData.getRemark(), returnData.getStatus().getId(),
				returnData.getStatus().getStatus(), returnData.getCarrier().getUserId(),
				userName +" - "+returnData.getCarrier().getUserName(), returnData.getNearByExpiry());

		for (ReturnDetail detail : MaterialReturnDao.getMaterialReturndetail(id)) {

			Long availableQuantity = GlobalDao.getStoreStockByItem(returnData.getFromStore().getStoreId(),
					detail.getBatch().getItem().getId(), unitId);
			Long availableStock = Util.ConvertQuantity(availableQuantity, detail.getBatch().getBatchUomList(),
					detail.getUomType());
			List<MaterialReturnIssue> materialReturnIssue = MaterialReturnDao
					.getMaterialReturnIssueData(detail.getId());
			String issueNo = "";
			Date issueDate = null;
			if (materialReturnIssue.size() > 0) {
				issueNo = materialReturnIssue.get(0).getIssueno();
				issueDate = materialReturnIssue.get(0).getIsuedate();
			}
			// detail.getMateria
			/*
			 * response.addItems(new ItemRespone(detail.getBatch().getItem().getId(),
			 * detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
			 * detail.getBatch().getId(), detail.getUomType().getId(),
			 * detail.getUomType().getType(), detail.getUomUnit().getId(),
			 * detail.getUomUnit().getUnits(), detail.getBatch().getExpiryDate(),
			 * availableStock, detail.getId(), detail.getReturnReason().getId(),
			 * detail.getReturnQty(), detail.getReturnReason().getReason(),
			 * detail.getBatch().getBatchNo(),
			 * detail.getBatch().getItem().getGeneric().getName(), detail.getIssueQty(),
			 * detail.getComsumedQty(), detail.getItemId(), issueNo, issueDate));
			 */

			/*
			 * start code for resolved HIS-9214 Return billable consumption to have issues,
			 * consumed and returned columns
			 */
			BigInteger totalReturnedQty = MaterialReturnDao.getTotalReturnedQty(detail.getMaterialIssueDetailId(),detail.getBatch().getId().intValue(),detail.getId());
			BigInteger totalComsumedQty = MaterialReturnDao.getTotalComsumedQty(detail.getMaterialIssueDetailId(),detail.getBatch().getId().intValue());
			BigInteger totalTransitQty = MaterialReturnDao.getTotalTransiQty(detail.getMaterialIssueDetailId(),detail.getBatch().getId().intValue());
			Integer getRejectedQty = MaterialReturnDao.getRejectedQty(detail.getMaterialIssueDetailId());
			Integer acceptedQty = MaterialReturnDao.getAcceptedQty(detail.getMaterialIssueDetailId(), detail.getBatch().getId().intValue(),detail.getId());
			ItemRespone itemRespone = new ItemRespone(detail.getBatch().getItem().getId(),
					detail.getBatch().getItem().getCode(), detail.getBatch().getItem().getName(),
					detail.getBatch().getId(), detail.getUomType().getId(), detail.getUomType().getType(),
					detail.getUomUnit().getId(), detail.getUomUnit().getUnits(), detail.getBatch().getExpiryDate(),
					availableStock, detail.getId(), detail.getReturnReason().getReasonMasterId(), detail.getReturnQty(),
					detail.getReturnReason().getReasonDesc(), detail.getBatch().getBatchNo(),
					detail.getBatch().getItem().getGeneric().getName(), detail.getIssueQty(), detail.getComsumedQty(),
					detail.getItemId(), issueNo, issueDate, detail.getBatch().getItem().getIsThreeUOMApplicable(), GlobalDao.getUnitOfMeasurementListByItem(detail.getBatch().getItem().getId(),returnData.getOrganizationId()));
			itemRespone.setTotalReturnedQty(totalReturnedQty.intValue());
			itemRespone.setTotalComsumedQty(totalComsumedQty.intValue());
			itemRespone.setTotalTransitInQty(totalTransitQty.intValue());
			itemRespone.setRejectQuantity(getRejectedQty.intValue());
			itemRespone.setAcceptQuantity(acceptedQty);
			response.addItems(itemRespone);
			/*
			 * end code for resolved HIS-9214 Return billable consumption to have issues,
			 * consumed and returned columns
			 */
		}
		return response;
	}

	@Override
	@Transactional
	public Object approveMaterialReturn(MaterialReurnRequest request) {

		screen = GlobalDao.getScreen(screenId);

		for (Integer id : request.getReturnId()) {

			Return returnData = MaterialReturnDao.getMaterialReturn(id);

			returnData.setStatus(new Status(request.getStatusId()));
			returnData.setApprovalStatus(new Status(request.getApprovalStatusId()));

			for (ReturnDetail detail : returnData.getReturnDetail()) {
				if (!detail.getIsDeleted()) {
					updateStoreStock(returnData.getFromStore().getStoreId(), detail.getBatch().getId(),
							detail.getReturnQty(), detail.getUomType(), returnData.getUnit().getUnitId(), request,
							detail.getItemId(), returnData.getReturnNo(), returnData, detail);

					detail.setStatus(new Status(request.getApprovalStatusId()));
					detail.setUpdatedDate(TimeZoneComponent.getDateByzone(returnData.getUnitId()));
					// detail.setUpdatedBy(detail.getUser());
					detail.setUpdatedByUser(request.getUserId());
				}
			}

			returnData.setApprovedDate(TimeZoneComponent.getDateByzone(returnData.getUnitId()));
			returnData.setApprovedBy(
					new User(request.getUserId() != null ? request.getUserId() : sessionService.getUserId()));

			returnData.setUpdatedDate(TimeZoneComponent.getDateByzone(returnData.getUnitId()));
			// returnData.setUpdatedBy(returnData.getUser());
			returnData.setUpdatedByUser(request.getUserId());

			MaterialReturnDao.updateMaterialReturn(returnData);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Material Return approved successfully");

		return response;
	}

	private Batch updateStoreStock(Integer storeId, Long batchId, Integer quantity, UomType uomType, Integer unitId,
			MaterialReurnRequest request, Integer item, String prefixNo, Return returnData, ReturnDetail d) {
		{
			// get batch needs to be issued
			Batch batch = GlobalDao.getBatch(storeId, batchId, unitId);

			// Get existing batch store stock
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId);

			// Get lease quantity
			Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);

			Integer balanceQuantity = avaialableStoreStock - leaseQuantity;
			batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity < 0 ? 0 : balanceQuantity);

			// Deduct issued quantity from the batch store stock
			Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(storeId, batch.getId());
			Integer tQty = inTransitQuantity + leaseQuantity;
			batch.getStoreStockList().get(0).settQty(tQty);

			// Add stock transaction
			StockTransaction stockTransaction = new StockTransaction(balanceQuantity, screen, screen.getCode(),
					leaseQuantity, batch.getStoreStockList().get(0).getStore(), d.getId());
			// sessionService.setEntityDefaults(stockTransaction);
			stockTransaction.setIsTransit(true);
			stockTransaction.setItem(new Item(item));
			stockTransaction.setOutQty(leaseQuantity);
			stockTransaction.setTransactionId(returnData.getId());
			stockTransaction.setTransitNo(prefixNo);
			stockTransaction.setCreateDefaults(returnData.getOrganizationId(), returnData.getUnitId(),
					returnData.getAddedByUser());
			batch.addStockTransaction(stockTransaction);

			return batch;
		}
	}

	@Override
	@Transactional
	public Object updateMaterialReturn(MaterialReurnRequest request) {

		Return returnData = MaterialReturnDao.getMaterialReturn(request.getId());

		returnData.updateMaterialReturn(new User(request.getCarrierId()), request.getRemark());

		for (ReturnDetail detail : returnData.getReturnDetail()) {
			Integer id = null;

			for (ItemRequest item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					detail.updateMaterialReturn(
							new com.param.adt.master.global.model.ReasonMaster(item.getReturnReasonId()),
							item.getReturnQuantity(), item.getIssueQty(), item.getComsumedQty());
					break;
				}
			}

			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(returnData.getUnitId()));
			// detail.setUpdatedBy(detail.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		for (ItemRequest item : request.getItems()) {
			if (item.getId() == null) {
				ReturnDetail returnDetail = new ReturnDetail(new Batch(item.getBatchId()),
						new com.param.adt.master.global.model.ReasonMaster(item.getReturnReasonId()),
						item.getReturnQuantity(), new Status(item.getStatusId()), new UomType(item.getUomTypeId()),
						new UomUnit(item.getUomUnitId()));
				returnDetail.setIssueQty(item.getIssueQty());
				returnDetail.setComsumedQty(item.getComsumedQty());
				returnDetail.setMaterialIssueDetailId(item.getIssueDetailId());
				returnDetail.setItemId(item.getItemId());
				// sessionService.setEntityDefaults(returnDetail);
				returnDetail.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
				returnData.addReturnDetail(returnDetail);

			}
		}

		returnData.setUpdatedDate(TimeZoneComponent.getDateByzone(returnData.getUnitId()));
		// returnData.setUpdatedBy(returnData.getUser());
		returnData.setUpdatedByUser(request.getUserId());

		MaterialReturnDao.updateMaterialReturn(returnData);

		CommonResponse response = new CommonResponse();
		response.setMessage("Material Return updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelMaterialReturn(MaterialReurnRequest request) {

		Return returnData = MaterialReturnDao.getMaterialReturn(request.getId());

		for (ReturnDetail detail : returnData.getReturnDetail()) {
			detail.setStatus(new Status(request.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(returnData.getUnitId()));
			// detail.setUpdatedBy(returnData.getUser());
			detail.setUpdatedByUser(request.getUserId());
		}

		returnData.setCancelReason(new CancelReason(request.getCancelReasonId()));
		returnData.setCancelNote(request.getCancelNote());

		returnData.setStatus(new Status(request.getStatusId()));

		returnData.setApprovalStatus(new Status(request.getApprovalStatusId()));

		returnData.setUpdatedDate(TimeZoneComponent.getDateByzone(returnData.getUnitId()));
		// returnData.setUpdatedBy(returnData.getUser());
		returnData.setUpdatedByUser(request.getUserId());

		MaterialReturnDao.updateMaterialReturn(returnData);

		CommonResponse response = new CommonResponse();
		response.setMessage("Material Return cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public List<MaterialReturnIssue> patientAgainstIssueSearch(PatientIssueReturnRequest patientIssueReturnRequest) {
		patientIssueReturnRequest.setToDate((new DateTime(patientIssueReturnRequest.getToDate())).plusDays(1).toDate());
		List<MaterialReturnIssue> materialReturnIssue = MaterialReturnDao
				.getMaterialReturnIssue(patientIssueReturnRequest);
        List<Integer> issueIds = materialReturnIssue.stream().map(MaterialReturnIssue::getIssueid).collect(Collectors.toList());
        List<MaterialIssueDetail> detailAll=MaterialReturnDao.getMaterialIssuedetailAll(issueIds);
        List<UomResponse> uomResponsesAll = GlobalDao.getAllUomListAll(patientIssueReturnRequest.getOrganizationId());
		for (MaterialReturnIssue m : materialReturnIssue) {
            Stream<MaterialIssueDetail> allLotNosStream = detailAll.stream();
            List<MaterialIssueDetail> detailList= allLotNosStream
            .filter(str -> str.getMaterialIssue().getId().equals(m.getIssueid()))
            .collect(Collectors.toList());
			for (MaterialIssueDetail detail : detailList) {
				List<MaterialIssueBatchMapper> materialIssueBatchMappers = detail.getMaterialIssueBatchMappers();

				for (MaterialIssueBatchMapper mData : materialIssueBatchMappers) {
					ItemRespone r = new ItemRespone();
					r.setIssuedetailsid(detail.getId());
					r.setItemId(detail.getItem().getId());
					r.setItemcode(detail.getItem().getCode());
					r.setItemName(detail.getItem().getName());
					r.setIsThreeUOMApplicable(detail.getItem().getIsThreeUOMApplicable());
					r.setGenericName(detail.getItem().getGeneric().getName());
					r.setBatchNo(mData.getBatch().getBatchNo());
					r.setExpiry(mData.getBatch().getExpiryDate());
					r.setUomType(mData.getUomType().getType());
					r.setUomTypeId(mData.getUomType().getId());
					r.setUomUnit(mData.getUomUnit().getUnits());
					r.setUomUnitId(mData.getUomUnit().getId());
					
		            Stream<UomResponse> uomListStream = uomResponsesAll.stream();
		            List<UomResponse> uomResponses=uomListStream
		            .filter(str -> str.getItemId()!=null && str.getItemId().equals(detail.getItem().getId()))
		            .collect(Collectors.toList());
					r.setUom(uomResponses);


					// Long storeStock = (long)
					// (materialIssueBatchMappers.get(0).getBatch().getQuantity()-
					// materialIssueBatchMappers.get(0).getBatch().getCurrentQuantity());

					// Long storeStock1 =
					// GlobalDao.getStoreStockByItem(detail.getMaterialIssue().getFromStore().getStoreId(),detail.getItem().getId());
					// Long availableStock = Util.ConvertQuantity(
					// materialIssueBatchMappers.get(0).getIssueQuantity().longValue(),
					// materialIssueBatchMappers.get(0).getBatch().getBatchUomList(),
					// materialIssueBatchMappers.get(0).getUomType());

					List<com.param.global.dto.items.BatchResponse1> data = StoreStockDao.getItemBatchDetails(
							patientIssueReturnRequest.getStoreId(), detail.getItem().getId(), mData.getBatch().getId(),
							mData.getUomType().getId());
					BigInteger availableQty = BigInteger.valueOf(0);
					Integer consumedQty = 0;
					Long issueQty = 0l;
					issueQty = Util.ConvertQuantity(mData.getIssueQuantity().longValue(),
							mData.getBatch().getBatchUomList(), mData.getUomType());
					if (data.size() > 0) {
						com.param.global.dto.items.BatchResponse1 rData = data.get(0);

						if (rData.getLotno() != null) {
							// Integer sCstock =
							// consignmentStockConversionDao.getTotalConsignmentStockFromBatch(r.getBatchno(),r.getLotno());
							availableQty = consignmentStockConversionDao.getAvailableStock(rData.getBatchno(),
									rData.getLotno(), rData.getIsconsignment(), patientIssueReturnRequest.getStoreId(),
									detail.getItem().getId(), patientIssueReturnRequest.getUnitId());
							// r.setAvailablestockquantity(availableQty);
							// r.setTotalstock(sCstock);
						} else if (rData.getBatchno() != null) {
							// Integer sCstock =
							// consignmentStockConversionDao.getTotalStock(r.getBatchno());
							// r.setTotalstock(sCstock);
							boolean isConsignmentFlag = true;
							if (r.getIsconsignment() != null) {
								isConsignmentFlag = r.getIsconsignment();
							}

							availableQty = consignmentStockConversionDao.getAvailableWithoutLotStock(rData.getBatchno(),
									isConsignmentFlag, patientIssueReturnRequest.getStoreId(), detail.getItem().getId(),
									patientIssueReturnRequest.getUnitId());
							// r.setAvailablestockquantity(availableQty);
						} else if (rData.getLotno() == null && rData.getBatchno() == null) {

							// Integer sCstock =
							// consignmentStockConversionDao.getTotalStock(r.getBatchno());
							// r.setTotalstock(sCstock);
							boolean isConsignmentFlag = true;
							if (r.getIsconsignment() != null) {
								isConsignmentFlag = r.getIsconsignment();
							}

							availableQty = consignmentStockConversionDao.getAvailableWithBatchIdStock(
									mData.getBatch().getId(), isConsignmentFlag, patientIssueReturnRequest.getStoreId(),
									detail.getItem().getId(), patientIssueReturnRequest.getUnitId());
							// r.setAvailablestockquantity(availableQty);

						}

						Long convertedAvailableStock = Util.ConvertQuantity(availableQty.longValue(),
								mData.getBatch().getBatchUomList(), mData.getUomType());

						if (convertedAvailableStock >= issueQty) {
							consumedQty = 0;
							// consumedQty = (int) (convertedAvailableStock - issueQty);
						}

						/*
						 * else if (convertedAvailableStock == issueQty) { consumedQty =
						 * convertedAvailableStock.intValue(); }
						 */

						else if (convertedAvailableStock < issueQty) {
							consumedQty = (int) (issueQty - convertedAvailableStock);
						}

					} else {
						consumedQty = issueQty.intValue();
					}

					r.setComsumedQty(consumedQty);
					r.setBatchId(mData.getBatch().getId());
					r.setIssueQty(mData.getIssueQuantity());
					r.setAcceptQuantity(mData.getAcceptedQuantity()); // HIS-11092
					BigInteger returnQty = MaterialReturnDao.getReturnQty(detail.getId());
					r.setReturnQty(returnQty.intValue());

					/*
					 * start code for resolved HIS-9214 Return billable consumption to have issues,
					 * consumed and returned columns
					 */
					BigInteger totalReturnedQty = MaterialReturnDao.getTotalReturnedQty(detail.getId(),r.getBatchId().intValue(),detail.getId());
					BigInteger totalTransitQty = MaterialReturnDao.getTotalTransiQty(detail.getId(),r.getBatchId().intValue());
					BigInteger totalComsumedQty = MaterialReturnDao.getTotalComsumedQty(detail.getId(),r.getBatchId().intValue());
					r.setTotalReturnedQty(totalReturnedQty.intValue());
					r.setTotalComsumedQty(totalComsumedQty.intValue());
					r.setTotalTransitQty(totalTransitQty.intValue());
					r.setComsumedQty(r.getTotalComsumedQty());
					/*
					 * end code for resolved HIS-9214 Return billable consumption to have issues,
					 * consumed and returned columns
					 */
					r.setRejectQuantity(mData.getRejectQuantity());
					if (mData.getAcceptedQuantity() > 0) {
						if(r.getIssueQty()!=(totalReturnedQty.intValue()+totalTransitQty.intValue()+totalComsumedQty.intValue()))
						{
							m.addItems(r);
						}
					}

				}

		}
		}
		List<MaterialReturnIssue> newList=new ArrayList<MaterialReturnIssue>();
		for (MaterialReturnIssue m : materialReturnIssue) {
			if(m.getItems()!=null)
			{
				newList.add(m);
			}
		}
		materialReturnIssue=newList;
		return materialReturnIssue;

	}

	@Override
	@Transactional
	public List<MaterialReturnIssue> againstExpireItemSearch(PatientIssueReturnRequest patientIssueReturnRequest) {
		patientIssueReturnRequest.setExpireDate(patientIssueReturnRequest.getFromDate());
		patientIssueReturnRequest.setToDate((new DateTime(patientIssueReturnRequest.getToDate())).plusDays(1).toDate());
		List<MaterialReturnIssue> materialReturnIssue = MaterialReturnDao.getMaterialReturnIssue(patientIssueReturnRequest);

		for (MaterialReturnIssue m : materialReturnIssue) {
			for (MaterialIssueDetail detail : MaterialReturnDao.getMaterialIssuedetail(m.getIssueid())) {
				 		//List<MaterialIssueBatchMapper> materialIssueBatchMappers = detail.getMaterialIssueBatchMappers();
				 		List<MaterialIssueBatchMapper> materialIssueBatchMappers = MaterialReturnDao.getMaterialIssueExpireBatchMapper(detail.getId() ,patientIssueReturnRequest);
				for (MaterialIssueBatchMapper mData : materialIssueBatchMappers) {
					
					/*if(patientIssueReturnRequest.getExpireDate().before(mData.getBatch().getExpiryDate())
							&& patientIssueReturnRequest.getFromDate().after(mData.getBatch().getExpiryDate()))
					{*/
					 
					ItemRespone r = new ItemRespone();
					r.setIssuedetailsid(detail.getId());
					r.setItemId(detail.getItem().getId());
					r.setItemcode(detail.getItem().getCode());
					r.setItemName(detail.getItem().getName());
					r.setIsThreeUOMApplicable(detail.getItem().getIsThreeUOMApplicable());
					r.setGenericName(detail.getItem().getGeneric().getName());
					r.setBatchNo(mData.getBatch().getBatchNo());
					r.setExpiry(mData.getBatch().getExpiryDate());
					r.setUomType(mData.getUomType().getType());
					r.setUomTypeId(mData.getUomType().getId());
					r.setUomUnit(mData.getUomUnit().getUnits());
					r.setUomUnitId(mData.getUomUnit().getId());
					List<UomResponse> uomResponses = GlobalDao.getUnitOfMeasurementListByItem(detail.getItem().getId(), patientIssueReturnRequest.getOrganizationId());
					r.setUom(uomResponses);
 
					List<com.param.global.dto.items.BatchResponse1> data = StoreStockDao.getItemBatchDetails(
							patientIssueReturnRequest.getStoreId(), detail.getItem().getId(), mData.getBatch().getId(),
							mData.getUomType().getId());
					BigInteger availableQty = BigInteger.valueOf(0);
					Integer consumedQty = 0;
					Long issueQty =0l;
					 issueQty = Util.ConvertQuantity(mData.getIssueQuantity().longValue(),
								mData.getBatch().getBatchUomList(), mData.getUomType());
					if (data.size() > 0) {
						com.param.global.dto.items.BatchResponse1 rData = data.get(0);

						if (rData.getLotno() != null) {
							// Integer sCstock =
							// consignmentStockConversionDao.getTotalConsignmentStockFromBatch(r.getBatchno(),r.getLotno());
							availableQty = consignmentStockConversionDao.getAvailableStock(rData.getBatchno(),
									rData.getLotno(), rData.getIsconsignment(), patientIssueReturnRequest.getStoreId(),
									detail.getItem().getId(),patientIssueReturnRequest.getUnitId());
							// r.setAvailablestockquantity(availableQty);
							// r.setTotalstock(sCstock);
						} else if (rData.getBatchno() != null){
							// Integer sCstock =
							// consignmentStockConversionDao.getTotalStock(r.getBatchno());
							// r.setTotalstock(sCstock);
							boolean isConsignmentFlag = true;
							if (r.getIsconsignment() != null) {
								isConsignmentFlag = r.getIsconsignment();
							}

							availableQty = consignmentStockConversionDao.getAvailableWithoutLotStock(rData.getBatchno(),
									isConsignmentFlag, patientIssueReturnRequest.getStoreId(),
									detail.getItem().getId(),patientIssueReturnRequest.getUnitId());
							// r.setAvailablestockquantity(availableQty);
						}
						else if(rData.getLotno()==null && rData.getBatchno() ==null)
						{

							// Integer sCstock =
							// consignmentStockConversionDao.getTotalStock(r.getBatchno());
							// r.setTotalstock(sCstock);
							boolean isConsignmentFlag = true;
							if (r.getIsconsignment() != null) {
								isConsignmentFlag = r.getIsconsignment();
							}

							availableQty = consignmentStockConversionDao.getAvailableWithBatchIdStock (mData.getBatch().getId(),
									isConsignmentFlag, patientIssueReturnRequest.getStoreId(),
									detail.getItem().getId(),patientIssueReturnRequest.getUnitId());
							// r.setAvailablestockquantity(availableQty);
						
						}
						
						Long convertedAvailableStock = Util.ConvertQuantity(availableQty.longValue(),
								mData.getBatch().getBatchUomList(), mData.getUomType());

						if (convertedAvailableStock >= issueQty) {
							consumedQty = 0;
							// consumedQty = (int) (convertedAvailableStock - issueQty);
						}

						/*else if (convertedAvailableStock == issueQty) {
							consumedQty = convertedAvailableStock.intValue();
						}*/

						else if (convertedAvailableStock < issueQty) {
							consumedQty = (int) (issueQty - convertedAvailableStock);
						}

					}
					else
					{
						consumedQty = issueQty.intValue();
					}

					r.setComsumedQty(consumedQty);
					r.setBatchId(mData.getBatch().getId());
					r.setIssueQty(mData.getIssueQuantity());
					r.setAcceptQuantity(mData.getAcceptedQuantity()); //HIS-11092
					BigInteger returnQty = MaterialReturnDao.getReturnQty(detail.getId());
					r.setReturnQty(returnQty.intValue());
					
					/* start code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns */
					BigInteger totalReturnedQty = MaterialReturnDao.getTotalReturnedQty(detail.getId(),r.getBatchId().intValue(),detail.getId());
					BigInteger totalComsumedQty = MaterialReturnDao.getTotalComsumedQty(detail.getId(),r.getBatchId().intValue());
					BigInteger totalTransitQty = MaterialReturnDao.getTotalTransiQty(detail.getId(),r.getBatchId().intValue());
					r.setTotalReturnedQty(totalReturnedQty.intValue());					
					r.setTotalComsumedQty(totalComsumedQty.intValue());
					r.setComsumedQty(r.getTotalComsumedQty());
					r.setTotalTransitQty(totalTransitQty.intValue());
					/* end code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns */
					r.setRejectQuantity(mData.getRejectQuantity());
					if(mData.getAcceptedQuantity()> 0)
					{
						m.addItems(r);
					}
				//}
				}

			}

		}

		return materialReturnIssue;

	
	}
	

	private void updateStoreStock(Return returnData, Batch batch, Integer quantity, UomType uomType , MaterialReurnRequest request, ReturnDetail d, Integer rejectQty) {
		
        Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(returnData.getToStore().getStoreId(), batch.getId());
        //add by minesh
        Integer avaialableStoreStockOfFromStore = GlobalDao.getStoreStockByBatch(returnData.getFromStore().getStoreId(), batch.getId());
		
		Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
		
		Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(returnData.getFromStore().getStoreId(), batch.getId());
		
		Integer balanceQuantity = avaialableStoreStock + leaseQuantity;
				
		Integer tQty = inTransitQuantity - leaseQuantity;
		Integer rejectQty1=Util.GetLeaseQuantity(rejectQty, batch.getBatchUomList(), uomType);
		
		List<StoreStock> fromStoreStock = batch.getStoreStockList().stream().filter(i -> i.getStore().getStoreId() == returnData.getFromStore().getStoreId()).collect(Collectors.toList());
		if(fromStoreStock!=null &&  fromStoreStock.size()>0) {
			fromStoreStock.get(0).settQty(tQty);
			saveStockTransaction(true,rejectQty1, fromStoreStock.get(0),request,d.getId(), d.getMreturn().getReturnNo());

		}
		
		List<StoreStock> toStoreStock = batch.getStoreStockList().stream().filter(i -> i.getStore().getStoreId() == returnData.getToStore().getStoreId()).collect(Collectors.toList());
		if(toStoreStock!=null &&  toStoreStock.size()>0) {
			toStoreStock.get(0).setAvailableQty(balanceQuantity);
	    	
			StockTransaction stockTransaction=	new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					leaseQuantity, batch.getStoreStockList().get(0).getStore(),d.getId());
			stockTransaction.setTransitNo(returnData.getReturnNo());
		//	sessionService.setEntityDefaults(stockTransaction);	
			stockTransaction.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),
					request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
	    	batch.addStockTransaction(stockTransaction);
		}
		else {
			StoreStock storeStockNew = new StoreStock(leaseQuantity, new Store(returnData.getToStore().getStoreId()), new Batch(batch.getId()));
		//	sessionService.setEntityDefaults(storeStockNew);
			storeStockNew.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),
					request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
			//storeStockNew.setIsConsignment(isConsignment);
			storeStockNew.setTotalQty(leaseQuantity);			
			MaterialReturnDao.saveStoreStock(storeStockNew);
			
			StockTransaction stockTransaction=new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					leaseQuantity, batch.getStoreStockList().get(0).getStore(), d.getId());
			stockTransaction.setTransitNo(returnData.getReturnNo());
		//	sessionService.setEntityDefaults(stockTransaction);
			stockTransaction.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),
					request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId()!=null ?request.getUserId() : sessionService.getUserId());
			batch.addStockTransaction(stockTransaction);
		}
	}
	private void saveStockTransaction(Boolean flag,Integer leaseQuantity, StoreStock storeStock, MaterialReurnRequest request, Integer integer, String returnBillNumber) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), storeStock.getStore(),
				new Batch(storeStock.getBatch().getId()),integer);
		stockTransaction.setTransitNo(returnBillNumber);
		stockTransaction.setItem(storeStock.getBatch().getItem());
		stockTransaction.setIsTransit(flag);
		//sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);
	}

    @Override
    @Transactional
    public Response getMaterialReturnDetails1(List<Integer> ids, Integer fromStoreId, Integer toStoreId)
            throws Exception {
        return MaterialReturnDao.getMaterialReturnDetails1(ids,fromStoreId,toStoreId);
    
    }

	@Override
	@Transactional
	public Response materialReturnStatus(Integer id) {
		Integer issueSaveStatus = MaterialReturnDao.materialReturnStatus(id);
        return new Response<>(null, null, null, null, issueSaveStatus); 
	}


}
