package com.param.procurement.workOrder.service.workOrderCreation;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.ContractType;
import com.param.entity.model.procurement.WorkOrderCreation;
import com.param.entity.model.procurement.WorkOrderCreationDetail;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.entity.model.procurement.WorkOrderQuotationRequestDetail;
import com.param.entity.model.procurement.WorkOrderRequest;
import com.param.entity.model.procurement.WorkOrderRequestDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.service.global.IGlobalService;
import com.param.procurement.workOrder.dao.workOrderCreation.IWOCreationDao;
import com.param.procurement.workOrder.dto.workOrderCreation.WOCreationDetailRequest;
import com.param.procurement.workOrder.dto.workOrderCreation.WOCreationDetailResponse;
import com.param.procurement.workOrder.dto.workOrderCreation.WOCreationRequest;
import com.param.procurement.workOrder.dto.workOrderCreation.WOCreationResponse;
import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationDropDown;
import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class WOCreationService implements IWOCreationService {

	private Integer screenId = Screen.WorkorderQuotationCreation.getScreen();

	private Integer woAgainstQR = com.param.common.constants.RequestType.AgainstWorkOrderQuotationRequest
			.getRequestType();

	private Integer woAgainstWR = com.param.common.constants.RequestType.AgainstWorkOrderRequest.getRequestType();
	private Integer directRequest = com.param.common.constants.RequestType.DirectRequest.getRequestType();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	IWOCreationDao wOCreationDao;
	
	@Autowired
	IItemsDao ItemsDao;


	@Override
	@Transactional
	public WorkOrderCreationDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		WorkOrderCreationDropDown workOrderCreationDropDown = new WorkOrderCreationDropDown();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			workOrderCreationDropDown
					.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			workOrderCreationDropDown.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}
		for (ContractType contractType : GlobalDao.getContractType()) {
			workOrderCreationDropDown.addContractType(
					new com.param.global.dto.global.ContractType(contractType.getId(), contractType.getCode()));
		}
		for (RequestType requestType : GlobalDao.getRequestType()) {
			if(com.param.common.constants.RequestType.AgainstWorkOrderQuotationRequest.getRequestType().equals(requestType.getId())) {
			workOrderCreationDropDown.addRequestType(new com.param.global.dto.global.RequestType(requestType.getId(), 
					requestType.getCode(),requestType.getType()));
			}
			else if(com.param.common.constants.RequestType.AgainstWorkOrderRequest.getRequestType().equals(requestType.getId())) {
			workOrderCreationDropDown.addRequestType(new com.param.global.dto.global.RequestType(requestType.getId(), 
					requestType.getCode(),requestType.getType()));
			}
			else if(com.param.common.constants.RequestType.DirectRequest.getRequestType().equals(requestType.getId())) {
				workOrderCreationDropDown.addRequestType(new com.param.global.dto.global.RequestType(requestType.getId(), 
						requestType.getCode(),requestType.getType()));
				}
			
		}
		return workOrderCreationDropDown;
	}

	@Override
	@Transactional
	public Object getWorkOrderCreationList(WOCreationRequest woCreationRequest) {
		woCreationRequest.setToDate((new DateTime(woCreationRequest.getToDate())).plusDays(1).toDate());

		List<WOCreationResponse> responseList = new ArrayList<WOCreationResponse>();

		for (WorkOrderCreation workOrderCreation : wOCreationDao.searchWorkOrderCreationList(woCreationRequest,
				screenId)) {

			WOCreationResponse response = new WOCreationResponse(workOrderCreation.getId(),
					workOrderCreation.getWorkOrderNo(),
					workOrderCreation.getAddedDate(), workOrderCreation.getStore().getStoreId(),
					workOrderCreation.getStore().getStoreName(), workOrderCreation.getContractType().getCode(),
					workOrderCreation.getRequestType().getType(), workOrderCreation.getRequestType().getId(),
					workOrderCreation.getStatus().getStatus(),
					workOrderCreation.getApprovalStatus() != null ? workOrderCreation.getApprovalStatus().getId()
							: null,
					workOrderCreation.getAddedBy().getUserName(), workOrderCreation.getRemark(),
					workOrderCreation.getVendor() != null ? workOrderCreation.getVendor().getName() : null);

			responseList.add(response);

		}
		return responseList;
	}

	@Override
	@Transactional
	public Object getWorkOrderCreationListByQR(WOCreationRequest woCreationRequest) {
		woCreationRequest.setToDate((new DateTime(woCreationRequest.getToDate())).plusDays(1).toDate());

		List<WOCreationResponse> responseList = new ArrayList<WOCreationResponse>();

		for (WorkOrderQuotationRequest workOrderQuotationRequest : wOCreationDao.searchWorkOrderCreationListByQR(woCreationRequest,
				screenId, woAgainstQR)) {

			WOCreationResponse response = new WOCreationResponse(workOrderQuotationRequest.getId(),
					workOrderQuotationRequest.getWorkOrderQRNo(), workOrderQuotationRequest.getAddedDate(),
					workOrderQuotationRequest.getStore().getStoreId(), workOrderQuotationRequest.getStore().getStoreName(),
					workOrderQuotationRequest.getContractType().getId(), workOrderQuotationRequest.getContractType().getCode(),
					workOrderQuotationRequest.getApprovalStatus() != null ? workOrderQuotationRequest.getApprovalStatus().getId()
							: null,
					workOrderQuotationRequest.getApprovedBy() != null ? workOrderQuotationRequest.getApprovedBy().getUserId() : null,
					workOrderQuotationRequest.getRemark(), workOrderQuotationRequest.getRequestType().getId(),
					workOrderQuotationRequest.getSendVendorSelection(),
					workOrderQuotationRequest.getStatus().getId(),
					workOrderQuotationRequest.getId(),
					workOrderQuotationRequest.getApprovalStatus() != null ? workOrderQuotationRequest.getApprovalStatus().getStatus()
							: null,
					workOrderQuotationRequest.getApprovedBy() != null ? workOrderQuotationRequest.getApprovedBy().getUserName() : null,
					workOrderQuotationRequest.getApprovedDate(), workOrderQuotationRequest.getRequestType().getType(),
					workOrderQuotationRequest.getStatus().getStatus(), workOrderQuotationRequest.getAddedBy().getUserName(),
					workOrderQuotationRequest.getRequestForNegotiation(),workOrderQuotationRequest.getWoQuotationVendor().getFirst().getQvNo());

			for (WorkOrderQuotationRequestDetail detail : workOrderQuotationRequest.getWorkOrderQRDetailsList()) {

				response.addItems(new WOCreationDetailResponse(detail.getId(),
						detail.getItem() != null ? detail.getItem().getId() : null,
						detail.getItem() != null ? detail.getItem().getCode() : null,
						detail.getItem() != null ? detail.getItem().getName() : null,
						detail.getBatch() != null ? detail.getBatch().getId() : null,
						detail.getBatch() != null ? detail.getBatch().getBatchNo() : null, 
						detail.getCurrentVendor() != null ? detail.getCurrentVendor().getId(): null,
						detail.getCurrentVendor() != null ? detail.getCurrentVendor().getName(): null,
						detail.getStatus().getId(), detail.getBatch().getExpiryDate(),
						detail.getId(),
						detail.getSpecification(),detail.getRemark(),detail.getWoQuotationVendorDetail().getFirst().getWoQuotationEntryDetailList().getFirst().getVendorCop(),
						detail.getQuantity()));
				

			}

			responseList.add(response);

		}
		return responseList;
	}
	
	@Override
	@Transactional
	public Object getWorkOrderCreationQRById(WOCreationRequest woCreationRequest) {
		
		List<WOCreationResponse> responseList = new ArrayList<WOCreationResponse>();

		WorkOrderQuotationRequest workOrderQuotationRequest = wOCreationDao.searchWorkOrderCreationQRById(woCreationRequest);

			WOCreationResponse response = new WOCreationResponse(workOrderQuotationRequest.getId(),
					workOrderQuotationRequest.getWorkOrderQRNo(), workOrderQuotationRequest.getAddedDate(),
					workOrderQuotationRequest.getStore().getStoreId(), workOrderQuotationRequest.getStore().getStoreName(),
					workOrderQuotationRequest.getContractType().getId(), workOrderQuotationRequest.getContractType().getCode(),
					workOrderQuotationRequest.getApprovalStatus() != null ? workOrderQuotationRequest.getApprovalStatus().getId()
							: null,
					workOrderQuotationRequest.getApprovedBy() != null ? workOrderQuotationRequest.getApprovedBy().getUserId() : null,
					workOrderQuotationRequest.getRemark(), workOrderQuotationRequest.getRequestType().getId(),
					workOrderQuotationRequest.getSendVendorSelection(),
					workOrderQuotationRequest.getStatus().getId(),
					workOrderQuotationRequest.getId(),
					workOrderQuotationRequest.getApprovalStatus() != null ? workOrderQuotationRequest.getApprovalStatus().getStatus()
							: null,
					workOrderQuotationRequest.getApprovedBy() != null ? workOrderQuotationRequest.getApprovedBy().getUserName() : null,
					workOrderQuotationRequest.getApprovedDate(), workOrderQuotationRequest.getRequestType().getType(),
					workOrderQuotationRequest.getStatus().getStatus(), workOrderQuotationRequest.getAddedBy().getUserName(),
					workOrderQuotationRequest.getRequestForNegotiation(),workOrderQuotationRequest.getWoQuotationVendor().getFirst().getQvNo());

			for (WorkOrderQuotationRequestDetail detail : workOrderQuotationRequest.getWorkOrderQRDetailsList()) {

				response.addItems(new WOCreationDetailResponse(detail.getId(),
						detail.getItem() != null ? detail.getItem().getId() : null,
						detail.getItem() != null ? detail.getItem().getCode() : null,
						detail.getItem() != null ? detail.getItem().getName() : null,
						detail.getBatch() != null ? detail.getBatch().getId() : null,
						detail.getBatch() != null ? detail.getBatch().getBatchNo() : null, 
						detail.getCurrentVendor() != null ? detail.getCurrentVendor().getId(): null,
						detail.getCurrentVendor() != null ? detail.getCurrentVendor().getName(): null,
						detail.getStatus().getId(), detail.getBatch().getExpiryDate(),
						detail.getId(),
						detail.getSpecification(),detail.getRemark(),detail.getBatch().getTax() != null ? detail.getBatch().getTax().getId() : null,
						detail.getBatch().getTax() != null ? detail.getBatch().getTax().getName() : null,detail.getBatch().getTaxPercentage(),
						detail.getWoQuotationVendorDetail().getFirst().getWoQuotationEntryDetailList().getFirst().getVendorCop(),
						detail.getQuantity()));

			}

			responseList.add(response);
			WorkOrderCreationResponse workOrderCreationResponse= new WorkOrderCreationResponse();
			
			workOrderCreationResponse.setPOCreationDetailList(responseList);
			workOrderCreationResponse.setTaxList(GlobalDao.getTaxListBP(woCreationRequest.getOrganizationId()));
		
		return workOrderCreationResponse;
	}

	@Override
	@Transactional
	public Object getWorkOrderCreationListByWR(WOCreationRequest woCreationRequest) {
		woCreationRequest.setToDate((new DateTime(woCreationRequest.getToDate())).plusDays(1).toDate());

		List<WOCreationResponse> responseList = new ArrayList<WOCreationResponse>();

		for (WorkOrderRequest workOrderRequest : wOCreationDao.searchWorkOrderCreationListByWR(woCreationRequest,
				screenId, woAgainstWR)) {

			if (workOrderRequest.getWorkOrderDetailList().getFirst().getSendWorkOrderCreation().equals(true)) {

				WOCreationResponse response = new WOCreationResponse(workOrderRequest.getId(),
						workOrderRequest.getStore().getStoreId(), workOrderRequest.getStore().getStoreName(),
						workOrderRequest.getContractType().getId(), workOrderRequest.getContractType().getCode(),
						workOrderRequest.getApprovalStatus() != null ? workOrderRequest.getApprovalStatus().getId() : null,
						workOrderRequest.getApprovedBy() != null ? workOrderRequest.getApprovedBy().getUserId() : null,
						workOrderRequest.getRequestNo(),
						workOrderRequest.getAddedDate(),
						workOrderRequest.getApprovalStatus() != null ? workOrderRequest.getApprovalStatus().getStatus() : null,
						workOrderRequest.getApprovedBy() != null ? workOrderRequest.getApprovedBy().getUserName()
								: null,
						workOrderRequest.getApprovedOn(), com.param.common.constants.RequestType.AgainstWorkOrderRequest.getRequestType(),
						workOrderRequest.getStatus().getStatus(), workOrderRequest.getAddedBy().getUserName(),
						workOrderRequest.getId());

				for (WorkOrderRequestDetail detail : workOrderRequest.getWorkOrderDetailList()) {

					response.addItems(new WOCreationDetailResponse(detail.getId(), detail.getItem().getId(),
							detail.getItem().getCode(), detail.getItem().getName(), detail.getBatch().getId(),
							detail.getBatch().getBatchNo(), 
							detail.getVendor() != null ? detail.getVendor().getId() : null,
							detail.getVendor() != null ? detail.getVendor().getName(): null,
							detail.getStatus().getId(), detail.getBatch().getExpiryDate(), detail.getSpecification(),
							detail.getRemark(), detail.getId(),detail.getApprovedQuantity()));

				}

				responseList.add(response);
			}

		}
		return responseList;
	}
	
	@Override
	@Transactional
	public Object getWorkOrderCreationWRById(WOCreationRequest woCreationRequest) {
		
		List<WOCreationResponse> responseList = new ArrayList<WOCreationResponse>();

		WorkOrderRequest workOrderRequest = wOCreationDao.searchWorkOrderCreationWRId(woCreationRequest);

			if (workOrderRequest.getWorkOrderDetailList().getFirst().getSendWorkOrderCreation().equals(true)) {

				WOCreationResponse response = new WOCreationResponse(workOrderRequest.getId(),
						workOrderRequest.getStore().getStoreId(), workOrderRequest.getStore().getStoreName(),
						workOrderRequest.getContractType().getId(), workOrderRequest.getContractType().getCode(),
						workOrderRequest.getApprovalStatus() != null ? workOrderRequest.getApprovalStatus().getId() : null,
						workOrderRequest.getApprovedBy() != null ? workOrderRequest.getApprovedBy().getUserId() : null,
						workOrderRequest.getRequestNo(),
						workOrderRequest.getAddedDate(),
						workOrderRequest.getApprovalStatus() != null ? workOrderRequest.getApprovalStatus().getStatus() : null,
						workOrderRequest.getApprovedBy() != null ? workOrderRequest.getApprovedBy().getUserName()
								: null,
						workOrderRequest.getApprovedOn(), com.param.common.constants.RequestType.AgainstWorkOrderRequest.getRequestType(),
						workOrderRequest.getStatus().getStatus(), workOrderRequest.getAddedBy().getUserName(),
						workOrderRequest.getId());

				for (WorkOrderRequestDetail detail : workOrderRequest.getWorkOrderDetailList()) {

					response.addItems(new WOCreationDetailResponse(detail.getId(), detail.getItem().getId(),
							detail.getItem().getCode(), detail.getItem().getName(), detail.getBatch().getId(),
							detail.getBatch().getBatchNo(), 
							detail.getVendor() != null ? detail.getVendor().getId() : null,
							detail.getVendor() != null ? detail.getVendor().getName(): null,
							detail.getStatus().getId(), detail.getBatch().getExpiryDate(), detail.getSpecification(),
							detail.getRemark(), detail.getId(),detail.getBatch().getTax() != null ? detail.getBatch().getTax().getId() :null ,
							detail.getBatch().getTax() != null ? detail.getBatch().getTax().getName() : null,
							detail.getBatch().getTaxPercentage(),detail.getApprovedQuantity()));

				}

				responseList.add(response);
			}
			WorkOrderCreationResponse workOrderCreationResponse= new WorkOrderCreationResponse();
			
			workOrderCreationResponse.setPOCreationDetailList(responseList);
			workOrderCreationResponse.setTaxList(GlobalDao.getTaxListBP(woCreationRequest.getOrganizationId()));
		
		return workOrderCreationResponse;
	}

	@Override
	@Transactional
	public Object saveWorkOrderByQuotationRequest(WOCreationRequest woCreationRequest) {

		
		/*Store storeNew = GlobalDao.getStore(woCreationRequest.getStoreId());
		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String WOCreationNo = GlobalService.getBillNoByGeneratedCode(woCreationRequest.getStoreId(), woCreationRequest.getUnitId(), screenId);
		CommonResponse response = new CommonResponse();
		if (woCreationRequest.getWoAgainstTypeId().equals(woAgainstQR)) {
			WorkOrderCreation workOrdercreationQR = new WorkOrderCreation(new Store(woCreationRequest.getStoreId()),
					new ContractType(woCreationRequest.getContractTypeId()),
					new Vendor(woCreationRequest.getVendorId()),
					new WorkOrderQuotationRequest(woCreationRequest.getWoReqOrWoQuotId()),
					new RequestType(woCreationRequest.getWoAgainstTypeId()), woCreationRequest.getTotal_amount(),
					woCreationRequest.getTotalDiscount(), woCreationRequest.getTotalTax(),
					woCreationRequest.getNetAmount(), new Status(woCreationRequest.getStatusId()),
					new Status(woCreationRequest.getApprovalStatusId()), woCreationRequest.getRemark(),
					WOCreationNo);

			for (WOCreationDetailRequest item : woCreationRequest.getItems()) {
				WorkOrderCreationDetail workOrderCreationDetail = new WorkOrderCreationDetail(
						new Item(item.getItemId()), new Batch(item.getBatchId()),
						new WorkOrderQuotationRequestDetail(item.getWoOrQrId()), item.getWoQty(), item.getBonusQty(),
						item.getAmount(), item.getDisPercentage(), item.getDisAmount(), new Tax(item.getTaxId()),
						item.getTaxPercentage(), item.getTaxAmount(),item.getTotalAmount(), item.getNetAmount(), item.getInclusionDetails(),
						item.getExclusionDetails(), item.getPaymentTerms(), item.getRemark(),
						new Status(item.getStatusId()));
//				sessionService.setEntityDefaults(workOrderCreationDetail);
				workOrderCreationDetail.setCreateDefaults(woCreationRequest.getOrganizationId(),woCreationRequest.getUnitId(),
						woCreationRequest.getUserId());
				workOrdercreationQR.addWorkOrderCreationDetailList(workOrderCreationDetail);

			}
//			sessionService.setEntityDefaults(workOrdercreationQR);
			workOrdercreationQR.setCreateDefaults(woCreationRequest.getOrganizationId(),woCreationRequest.getUnitId(),
					woCreationRequest.getUserId());
			wOCreationDao.saveWorkOrderCreation(workOrdercreationQR);
			response.setId(workOrdercreationQR.getId().longValue());
			response.setMessage("Work Order Creation saved By QR successfully");
		}else if (woCreationRequest.getWoAgainstTypeId().equals(woAgainstWR)) {
			WorkOrderCreation workOrdercreationQR = new WorkOrderCreation(new Store(woCreationRequest.getStoreId()),
					new ContractType(woCreationRequest.getContractTypeId()),
					new Vendor(woCreationRequest.getVendorId()),
					new WorkOrderRequest(woCreationRequest.getWoReqOrWoQuotId()),
					new RequestType(woCreationRequest.getWoAgainstTypeId()), woCreationRequest.getTotal_amount(),
					woCreationRequest.getTotalDiscount(), woCreationRequest.getTotalTax(),
					woCreationRequest.getNetAmount(), new Status(woCreationRequest.getStatusId()),
					new Status(woCreationRequest.getApprovalStatusId()), woCreationRequest.getRemark(),
					WOCreationNo);

			for (WOCreationDetailRequest item : woCreationRequest.getItems()) {
				WorkOrderCreationDetail workOrderCreationDetail = new WorkOrderCreationDetail(
						new Item(item.getItemId()), new Batch(item.getBatchId()),
						new WorkOrderRequestDetail(item.getWoOrQrId()), item.getWoQty(), item.getBonusQty(),
						item.getAmount(), item.getDisPercentage(), item.getDisAmount(), new Tax(item.getTaxId()),
						item.getTaxPercentage(), item.getTaxAmount(),item.getTotalAmount(), item.getNetAmount(), item.getInclusionDetails(),
						item.getExclusionDetails(), item.getPaymentTerms(), item.getRemark(),
						new Status(item.getStatusId()));
//				sessionService.setEntityDefaults(workOrderCreationDetail);
				workOrderCreationDetail.setCreateDefaults(woCreationRequest.getOrganizationId(),woCreationRequest.getUnitId(),
						woCreationRequest.getUserId());
				workOrdercreationQR.addWorkOrderCreationDetailList(workOrderCreationDetail);

			}
			
//			sessionService.setEntityDefaults(workOrdercreationQR);
			workOrdercreationQR.setCreateDefaults(woCreationRequest.getOrganizationId(),woCreationRequest.getUnitId(),
					woCreationRequest.getUserId());
			wOCreationDao.saveWorkOrderCreation(workOrdercreationQR);
			response.setId(workOrdercreationQR.getId().longValue());
			response.setMessage("Work Order Creation saved By WR successfully");
		}
		else  if (woCreationRequest.getWoAgainstTypeId().equals(directRequest))
		 {
			WorkOrderCreation workOrdercreationQR = new WorkOrderCreation(new Store(woCreationRequest.getStoreId()),
					new ContractType(woCreationRequest.getContractTypeId()),
					new Vendor(woCreationRequest.getVendorId()),
					new RequestType(woCreationRequest.getWoAgainstTypeId()), woCreationRequest.getTotal_amount(),
					woCreationRequest.getTotalDiscount(), woCreationRequest.getTotalTax(),
					woCreationRequest.getNetAmount(), new Status(woCreationRequest.getStatusId()),
					new Status(woCreationRequest.getApprovalStatusId()), woCreationRequest.getRemark(),WOCreationNo
					);

			for (WOCreationDetailRequest item : woCreationRequest.getItems()) {
				WorkOrderCreationDetail workOrderCreationDetail = new WorkOrderCreationDetail(
						new Item(item.getItemId()), new Batch(item.getBatchId()),
						 item.getWoQty(), item.getBonusQty(),
						item.getAmount(), item.getDisPercentage(), item.getDisAmount(), new Tax(item.getTaxId()),
						item.getTaxPercentage(), item.getTaxAmount(),item.getTotalAmount(), item.getNetAmount(), item.getInclusionDetails(),
						item.getExclusionDetails(), item.getPaymentTerms(), item.getRemark(),
						new Status(item.getStatusId()));
//				sessionService.setEntityDefaults(workOrderCreationDetail);
				workOrderCreationDetail.setCreateDefaults(woCreationRequest.getOrganizationId(),woCreationRequest.getUnitId(),
						woCreationRequest.getUserId());
				workOrdercreationQR.addWorkOrderCreationDetailList(workOrderCreationDetail);

			}
//			sessionService.setEntityDefaults(workOrdercreationQR);
			workOrdercreationQR.setCreateDefaults(woCreationRequest.getOrganizationId(),woCreationRequest.getUnitId(),
					woCreationRequest.getUserId());
			wOCreationDao.saveWorkOrderCreation(workOrdercreationQR);
			response.setId(workOrdercreationQR.getId().longValue());
			response.setMessage("Work Order Creation saved By WR successfully");
		}

		
		return response;

	}

	@Override
	@Transactional
	public Object updateWorkOrderCreation(WOCreationRequest woCreationRequest) {

		WorkOrderCreation workOrderCreation = wOCreationDao.getWorkOrderCreation(woCreationRequest.getId());

		if (woCreationRequest.getTotal_amount() != null) {
			workOrderCreation.setTotalAmount(woCreationRequest.getTotal_amount());
		}
		if (woCreationRequest.getTotalDiscount() != null) {
			workOrderCreation.setTotalDiscount(woCreationRequest.getTotalDiscount());
		}
		if (woCreationRequest.getTotalTax() != null) {
			workOrderCreation.setTotalTax(woCreationRequest.getTotalTax());
		}
		if (woCreationRequest.getNetAmount() != null) {
			workOrderCreation.setNetAmount(woCreationRequest.getNetAmount());
		}
		if (woCreationRequest.getRemark() != null) {
			workOrderCreation.setRemark(woCreationRequest.getRemark());
		}
		

		for (WorkOrderCreationDetail detail : workOrderCreation.getWorkOrderCreationDetail()) {
			Integer id = null;

			for (WOCreationDetailRequest item : woCreationRequest.getItems()) {

				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					
					if (item.getItemId() != null) {
						detail.setItem(new Item(item.getItemId()));
					}
					if (item.getBatchId() != null) {
						detail.setBatch(new Batch(item.getBatchId()));
					}
					if (item.getWoQty() != null) {
						detail.setWoQuantity(item.getWoQty());
					}
					if (item.getBonusQty() != null) {
						detail.setQoBonusQuantity(item.getBonusQty());
					}
					if (item.getAmount() != null) {
						detail.setAmount(item.getAmount());
					}
					if (item.getDisPercentage() != null) {
						detail.setDiscountPercentage(item.getDisPercentage());
					}
					
					if (item.getDisAmount() != null) {
						detail.setDiscountAmount(item.getDisAmount());
					}
					if (item.getTaxId() != null) {
						detail.setTax(new Tax(item.getTaxId()));
					}
					if (item.getTaxPercentage() != null) {
						detail.setTaxPercentage(item.getTaxPercentage());
					}
					if (item.getTaxAmount() != null) {
						detail.setTaxAmount(item.getTaxAmount());
					}
					if (item.getTotalAmount() != null) {
						detail.setTotalAmount(item.getTotalAmount());
					}
					if (item.getNetAmount() != null) {
						detail.setNetAmount(item.getNetAmount());
					}
					if (item.getInclusionDetails() != null) {
						detail.setInclusionDetails(item.getInclusionDetails());
					}
					if (item.getExclusionDetails() != null) {
						detail.setExclusionDetails(item.getExclusionDetails());
					}
					if (item.getPaymentTerms() != null) {
						detail.setPaymentTerm(item.getPaymentTerms());
					}
					if (item.getRemark() != null) {
						detail.setRemark(item.getRemark());
					}
					

				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderCreation.getUnitId()));
			detail.setUpdatedByUser(woCreationRequest.getUserId());

		}

		for (WOCreationDetailRequest item : woCreationRequest.getItems()) {
			if (item.getId() == null) {
				WorkOrderCreationDetail workOrderCreationDetail = new WorkOrderCreationDetail(
						new Item(item.getItemId()), new Batch(item.getBatchId()),
						new WorkOrderRequestDetail(item.getWoOrQrId()), item.getWoQty(), item.getBonusQty(),
						item.getAmount(), item.getDisPercentage(), item.getDisAmount(), new Tax(item.getTaxId()),
						item.getTaxPercentage(), item.getTaxAmount(),item.getTotalAmount(), item.getNetAmount(), item.getInclusionDetails(),
						item.getExclusionDetails(), item.getPaymentTerms(), item.getRemark(),
						new Status(item.getStatusId()));
//				sessionService.setEntityDefaults(workOrderCreationDetail);
				workOrderCreationDetail.setCreateDefaults(woCreationRequest.getOrganizationId(),woCreationRequest.getUnitId(),
						woCreationRequest.getUserId());
				workOrderCreation.addWorkOrderCreationDetailList(workOrderCreationDetail);
			}

		}
		workOrderCreation.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderCreation.getUnitId()));
		workOrderCreation.setUpdatedByUser(woCreationRequest.getUserId());

		wOCreationDao.updateWorkOrderCreation(workOrderCreation);

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order Creation updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getworkOrderCreation(Integer id,Integer unitId,Integer orgId) {

		WorkOrderCreation workOrderCreation = wOCreationDao.getWorkOrderCreation(id);

		WOCreationResponse response = null;
		if (workOrderCreation.getRequestType().getId().equals(woAgainstWR)) {
			response =new WOCreationResponse(workOrderCreation.getId(), workOrderCreation.getWorkOrderNo(),
					workOrderCreation.getAddedDate(), workOrderCreation.getStore().getStoreId(),
					workOrderCreation.getStore().getStoreName(), workOrderCreation.getContractType().getId(),
					workOrderCreation.getContractType().getCode(), workOrderCreation.getRequestType().getId(),
					workOrderCreation.getRequestType().getType(), workOrderCreation.getStatus().getId(),
					workOrderCreation.getStatus().getStatus(), workOrderCreation.getApprovalStatus().getStatus(),
					workOrderCreation.getApprovalStatus().getId(), workOrderCreation.getAddedBy().getUserId(),
					workOrderCreation.getAddedBy().getUserName(),
					workOrderCreation.getApprovedBy() != null ? workOrderCreation.getApprovedBy().getUserName() : null,
					workOrderCreation.getApprovedOn(),
					workOrderCreation.getApprovedBy() != null ? workOrderCreation.getApprovedBy().getUserId() : null,
					workOrderCreation.getWorkOrder() != null ? workOrderCreation.getWorkOrder().getId() : null,
					workOrderCreation.getVendor().getId(),workOrderCreation.getVendor().getName(),
					workOrderCreation.getTotalAmount(),workOrderCreation.getNetAmount(),workOrderCreation.getTotalTax(),
					workOrderCreation.getTotalDiscount(),workOrderCreation.getWorkOrder().getRequestNo(),workOrderCreation.getRemark()

			);
			

			for (WorkOrderCreationDetail detail : workOrderCreation.getWorkOrderCreationDetail()) {
				if (detail.getIsDeleted() != true && detail.getIsActive() != false) {

					response.addItems(new WOCreationDetailResponse(detail.getId(), detail.getItem().getId(),
							detail.getItem().getCode(), detail.getItem().getName(), detail.getBatch().getId(),
							detail.getBatch().getBatchNo(), detail.getAmount(),
							detail.getWorkOrderCreation().getVendor() != null
									? detail.getWorkOrderCreation().getVendor().getId()
									: null,
							detail.getWorkOrderCreation().getVendor() != null
									? detail.getWorkOrderCreation().getVendor().getName()
									: null,
							detail.getStatus().getId(), detail.getBatch().getExpiryDate(), detail.getSpecification(),
							detail.getInclusionDetails(), detail.getExclusionDetails(), detail.getPaymentTerm(),
							detail.getRemark(), detail.getWorkOrderDetail().getId(), detail.getTax().getId(),
							detail.getTax().getName(), detail.getTax().getTaxPercentage(), detail.getTotalAmount(),
							detail.getDiscountAmount(), detail.getNetAmount(), detail.getTaxAmount(),
							detail.getWoQuantity(),detail.getWorkOrderDetail().getQuantity(),detail.getDiscountPercentage()));
					

				}
			}

		}
		if (workOrderCreation.getRequestType().getId().equals(woAgainstQR)) {
			response = new WOCreationResponse(workOrderCreation.getId(), workOrderCreation.getWorkOrderNo(),
					workOrderCreation.getAddedDate(), workOrderCreation.getStore().getStoreId(),
					workOrderCreation.getStore().getStoreName(), workOrderCreation.getContractType().getId(),
					workOrderCreation.getContractType().getCode(), workOrderCreation.getRequestType().getId(),
					workOrderCreation.getRequestType().getType(), workOrderCreation.getStatus().getId(),
					workOrderCreation.getStatus().getStatus(), workOrderCreation.getApprovalStatus().getStatus(),
					workOrderCreation.getApprovalStatus().getId(), workOrderCreation.getAddedBy().getUserId(),
					workOrderCreation.getAddedBy().getUserName(),
					workOrderCreation.getApprovedBy() != null ? workOrderCreation.getApprovedBy().getUserName() : null,
					workOrderCreation.getApprovedOn(),
					workOrderCreation.getApprovedBy() != null ? workOrderCreation.getApprovedBy().getUserId() : null,
					workOrderCreation.getWorkOrderQuotation() != null
							? workOrderCreation.getWorkOrderQuotation().getId()
							: null,
					workOrderCreation.getWorkOrderQuotation() != null
							? workOrderCreation.getWorkOrderQuotation().getSendVendorSelection()
							: null,
					workOrderCreation.getWorkOrderQuotation() != null
							? workOrderCreation.getWorkOrderQuotation().getRequestForNegotiation()
							: null,
							workOrderCreation.getVendor().getId(),workOrderCreation.getVendor().getName(),
							workOrderCreation.getTotalAmount(),workOrderCreation.getNetAmount(),workOrderCreation.getTotalTax(),
							workOrderCreation.getTotalDiscount(),workOrderCreation.getWorkOrderQuotation().getWorkOrderQRNo(),workOrderCreation.getRemark());

			for (WorkOrderCreationDetail detail : workOrderCreation.getWorkOrderCreationDetail()) {
				if (detail.getIsDeleted() != true && detail.getIsActive() != false) {

					response.addItems(new WOCreationDetailResponse(detail.getId(),
							detail.getItem() != null ? detail.getItem().getId() : null,
							detail.getItem() != null ? detail.getItem().getCode() : null,
							detail.getItem() != null ? detail.getItem().getName() : null,
							detail.getBatch() != null ? detail.getBatch().getId() : null,
							detail.getBatch() != null ? detail.getBatch().getBatchNo() : null, detail.getAmount(),
							detail.getWorkOrderCreation().getVendor() != null
									? detail.getWorkOrderCreation().getVendor().getId()
									: null,
							detail.getWorkOrderCreation().getVendor() != null
									? detail.getWorkOrderCreation().getVendor().getName()
									: null,
							detail.getStatus().getId(), detail.getBatch().getExpiryDate(),
							detail.getWorkOrderQuotationDetail() != null ? detail.getWorkOrderQuotationDetail().getId()
									: null,
							detail.getSpecification(), detail.getInclusionDetails(), detail.getExclusionDetails(),
							detail.getPaymentTerm(), detail.getRemark(), detail.getTax().getId(),
							detail.getTax().getName(), detail.getTax().getTaxPercentage(), detail.getTotalAmount(),
							detail.getDiscountAmount(), detail.getNetAmount(), detail.getTaxAmount(),
							detail.getWorkOrderQuotationDetail().getQuantity(),
							detail.getWorkOrderQuotationDetail().getWoQuotationVendorDetail().getFirst().getWoQuotationEntryDetailList().getFirst().getVendorCop(),
							detail.getWoQuantity(),detail.getDiscountPercentage()));
				
				}
			}
		}
		if (workOrderCreation.getRequestType().getId().equals(directRequest)) {
			response = new WOCreationResponse(workOrderCreation.getId(),
					workOrderCreation.getAddedDate(), workOrderCreation.getStore().getStoreId(),
					workOrderCreation.getStore().getStoreName(), workOrderCreation.getContractType().getId(),
					workOrderCreation.getContractType().getCode(), workOrderCreation.getRequestType().getId(),
					workOrderCreation.getRequestType().getType(), workOrderCreation.getStatus().getId(),
					workOrderCreation.getStatus().getStatus(), workOrderCreation.getApprovalStatus().getStatus(),
					workOrderCreation.getApprovalStatus().getId(), workOrderCreation.getAddedBy().getUserId(),
					workOrderCreation.getAddedBy().getUserName(),
					workOrderCreation.getApprovedBy() != null ? workOrderCreation.getApprovedBy().getUserName() : null,
					workOrderCreation.getApprovedOn(),
					workOrderCreation.getApprovedBy() != null ? workOrderCreation.getApprovedBy().getUserId() : null,
					workOrderCreation.getVendor().getId(),workOrderCreation.getVendor().getName(),
					workOrderCreation.getTotalAmount(),workOrderCreation.getNetAmount(),workOrderCreation.getTotalTax(),
					workOrderCreation.getTotalDiscount(),workOrderCreation.getWorkOrderNo(),workOrderCreation.getRemark()
					);

			for (WorkOrderCreationDetail detail : workOrderCreation.getWorkOrderCreationDetail()) {
				if (detail.getIsDeleted() != true && detail.getIsActive() != false) {

					response.addItems(new WOCreationDetailResponse(detail.getId(),
							detail.getItem() != null ? detail.getItem().getId() : null,
							detail.getItem() != null ? detail.getItem().getCode() : null,
							detail.getItem() != null ? detail.getItem().getName() : null,
							detail.getBatch() != null ? detail.getBatch().getId() : null,
							detail.getBatch() != null ? detail.getBatch().getBatchNo() : null, detail.getAmount(),
							detail.getWorkOrderCreation().getVendor() != null
									? detail.getWorkOrderCreation().getVendor().getId()
									: null,
							detail.getWorkOrderCreation().getVendor() != null
									? detail.getWorkOrderCreation().getVendor().getName()
									: null,
							detail.getStatus().getId(), detail.getBatch().getExpiryDate(),
							
							detail.getSpecification(), detail.getInclusionDetails(), detail.getExclusionDetails(),
							detail.getPaymentTerm(), detail.getRemark(), detail.getTax().getId(),
							detail.getTax().getName(), detail.getTax().getTaxPercentage(), detail.getTotalAmount(),
							detail.getDiscountAmount(), detail.getNetAmount(), detail.getTaxAmount(),detail.getDiscountPercentage(),
							ItemsDao.batchListByItemId(detail.getItem().getId(), workOrderCreation.getStore().getStoreId(),unitId),detail.getWoQuantity()));
					
					
				}
			}
		}
		
		response.setTaxList(GlobalDao.getTaxListBP(orgId));

		return response;
	}

	@Override
	@Transactional
	public Object approveWorkOrderCreation(WOCreationRequest woCreationRequest) {

		for (Integer id : woCreationRequest.getWoCreationIds()) {
			WorkOrderCreation workOrderCreation = wOCreationDao.getWorkOrderCreation(id);

			workOrderCreation.setStatus(new Status(woCreationRequest.getStatusId()));
			workOrderCreation.setApprovalStatus(new Status(woCreationRequest.getApprovalStatusId()));

			for (WorkOrderCreationDetail detail : workOrderCreation.getWorkOrderCreationDetail()) {
				detail.setStatus(new Status(woCreationRequest.getStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderCreation.getUnitId()));
				detail.setUpdatedByUser(woCreationRequest.getUserId());
			}

			workOrderCreation.setApprovedOn(TimeZoneComponent.getDateByzone(workOrderCreation.getUnitId()));
			workOrderCreation.setApprovedBy(new User(woCreationRequest.getUserId()));

			workOrderCreation.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderCreation.getUnitId()));
			workOrderCreation.setUpdatedByUser(woCreationRequest.getUserId());

			wOCreationDao.updateWorkOrderCreation(workOrderCreation);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelWorkOrderCreation(WOCreationRequest woCreationRequest) {

		WorkOrderCreation workOrderCreation = wOCreationDao.getWorkOrderCreation(woCreationRequest.getId());

		for (WorkOrderCreationDetail detail : workOrderCreation.getWorkOrderCreationDetail()) {
			detail.setStatus(new Status(woCreationRequest.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderCreation.getUnitId()));
			detail.setUpdatedByUser(woCreationRequest.getUserId());
		}
		workOrderCreation.setCancelReason(new CancelReason(woCreationRequest.getCancelReasonId()));
		workOrderCreation.setCancelNote(woCreationRequest.getCancelNote());

		workOrderCreation.setStatus(new Status(woCreationRequest.getStatusId()));
		workOrderCreation.setApprovalStatus(new Status(woCreationRequest.getApprovalStatusId()));

		workOrderCreation.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderCreation.getUnitId()));
		workOrderCreation.setUpdatedByUser(woCreationRequest.getUserId());

		wOCreationDao.updateWorkOrderCreation(workOrderCreation);

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order Creation cancelled successfully");

		return response;
	}

}
