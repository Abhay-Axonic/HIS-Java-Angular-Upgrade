package com.param.procurement.workOrder.service.workOrderQuotationVendor;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.ContractType;
import com.param.entity.model.procurement.WoQuotationVendor;
import com.param.entity.model.procurement.WoQuotationVendorDetail;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.entity.model.procurement.WorkOrderQuotationRequestDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.procurement.quotation.dto.enqSendToVendor.MultipleVendors;
import com.param.procurement.workOrder.dao.workOrderQuotationVendor.IWorkOrderQuotationVendorDao;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQR;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQRDetailResponse;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQrResponse;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQRDto;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVDetailRequest;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVDetailResponse;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVRequest;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVResponse;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class WorkOrderQuotationVendorService implements IWorkOrderQuotationVendorService {

	private Integer screenId = Screen.WorkorderQuotationSendtoVendor.getScreen();

	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IWorkOrderQuotationVendorDao workOrderQuotationVendorDao;

	@Override
	@Transactional
	public WorkOrderDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		WorkOrderDropDown workOrderDropDown = new WorkOrderDropDown();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			workOrderDropDown.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			workOrderDropDown.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (ContractType contractType : GlobalDao.getContractType()) {
			workOrderDropDown.addContractType(
					new com.param.global.dto.global.ContractType(contractType.getId(), contractType.getCode()));

		}
		return workOrderDropDown;
	}

	@Override
	@Transactional
	public Object getWorkOrderQuotationVendorList(WorkOrderQVRequest workOrderQVRequest) {
		workOrderQVRequest.setToDate((new DateTime(workOrderQVRequest.getToDate())).plusDays(1).toDate());

		List<WorkOrderQVResponse> responseList = new ArrayList<WorkOrderQVResponse>();

		for (WoQuotationVendor workOrderQV : workOrderQuotationVendorDao.searchWorkOrderQVList(workOrderQVRequest,
				screenId)) {

			WorkOrderQVResponse response = new WorkOrderQVResponse(workOrderQV.getId(),
					workOrderQV.getWorkOrderQR().getWorkOrderQRNo(), workOrderQV.getWorkOrderQR().getAddedDate(),
					workOrderQV.getQvNo(), workOrderQV.getAddedDate(),
					workOrderQV.getWorkOrderQR().getStore().getStoreName(), workOrderQV.getVendor().getName(),
					workOrderQV.getWorkOrderQR().getContractType().getCode(),
					workOrderQV.getWorkOrderQR().getRequestType().getType(), workOrderQV.getDueDate(),
					workOrderQV.getRemark(), workOrderQV.getAddedBy().getUserName(),
					workOrderQV.getStatus().getStatus(), workOrderQV.getApprovalStatus().getStatus(),
					workOrderQV.getApprovedBy() != null ? workOrderQV.getApprovedBy().getUserName() : null);

			responseList.add(response);

		}
		return responseList;
	}

	@Override
	@Transactional
	public Object getWorkOrderQuotationVendorListNew(WorkOrderQVRequest workOrderQVRequest) {
		workOrderQVRequest.setToDate((new DateTime(workOrderQVRequest.getToDate())).plusDays(1).toDate());

		List<WorkOrderQVResponse> responseList = new ArrayList<WorkOrderQVResponse>();

		for (WoQuotationVendor workOrderQV : workOrderQuotationVendorDao.searchWorkOrderQVListNew(workOrderQVRequest,
				screenId)) {

			WorkOrderQVResponse response = new WorkOrderQVResponse(workOrderQV.getId(),
					workOrderQV.getWorkOrderQR().getWorkOrderQRNo(), workOrderQV.getWorkOrderQR().getAddedDate(),
					workOrderQV.getQvNo(), workOrderQV.getAddedDate(),
					workOrderQV.getWorkOrderQR().getStore().getStoreName(), workOrderQV.getVendor().getName(),
					workOrderQV.getWorkOrderQR().getContractType().getCode(),
					workOrderQV.getWorkOrderQR().getRequestType().getType(), workOrderQV.getDueDate(),
					workOrderQV.getRemark(), workOrderQV.getAddedBy().getUserName(),
					workOrderQV.getStatus().getStatus(), workOrderQV.getApprovalStatus().getStatus(),
					workOrderQV.getApprovedBy() != null ? workOrderQV.getApprovedBy().getUserName() : null);

			responseList.add(response);

		}
		return responseList;
	}



	@Override
	@Transactional
	public Object saveWoQuotationVendor(WorkOrderQVRequest workOrderQVRequest) {

		/*
		 * Random rand = new Random(); int num = rand.nextInt(1000); String
		 * workOrderQVNo = "WQV-" + num;
		 */
		Integer storeId = workOrderQVRequest.getStoreId();

		//Store storeNew = GlobalDao.getStore(storeId);
		CommonResponse response = new CommonResponse();
		//Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		for (MultipleVendors mv : workOrderQVRequest.getVenders()) {

			String workOrderQVNo = GlobalService.getBillNoByGeneratedCode(storeId, sessionService.getUnitId(), screenId);
			WoQuotationVendor workOrderQV = new WoQuotationVendor(new Status(workOrderQVRequest.getApprovalStatusId()),
					workOrderQVNo, workOrderQVRequest.getDueDate(),
					workOrderQVRequest.getWoQuotationRequestId() != null
							? new WorkOrderQuotationRequest(workOrderQVRequest.getWoQuotationRequestId())
							: null,
					workOrderQVRequest.getRemark(), new Status(workOrderQVRequest.getStatusId()),
					new Vendor(mv.getVendorId()));

			for (WorkOrderQVDetailRequest item : workOrderQVRequest.getItems()) {
				if (mv.getVendorId() == item.getVendorId()) {
					WoQuotationVendorDetail woQuotationVendorDetail = new WoQuotationVendorDetail(
							item.getWorkOrderQRDetailId() != null
									? new WorkOrderQuotationRequestDetail(item.getWorkOrderQRDetailId())
									: null,
							item.getRemark(), new Status(item.getStatusId()));
//					sessionService.setEntityDefaults(woQuotationVendorDetail);
					woQuotationVendorDetail.setCreateDefaults(workOrderQVRequest.getOrganizationId(),workOrderQVRequest.getUnitId(),
							workOrderQVRequest.getUserId());
					workOrderQV.addWoQuotationSendToVendorDetailList(woQuotationVendorDetail);
//					sessionService.setEntityDefaults(workOrderQV);
					workOrderQV.setCreateDefaults(workOrderQVRequest.getOrganizationId(),workOrderQVRequest.getUnitId(),
							workOrderQVRequest.getUserId());
					workOrderQuotationVendorDao.saveWoQuotationVendor(workOrderQV);
					response.setId(workOrderQV.getId().longValue());
				}

			}

			response.setMessage("Quotation Send To Vendor saved successfully");

		}

		return response;

	}

	@Override
	@Transactional
	public Object saveWoQuotationVendorNew(WorkOrderQVRequest workOrderQVRequest) {

		/*
		 * Random rand = new Random(); int num = rand.nextInt(1000); String
		 * workOrderQVNo = "WQV-" + num;
		 */
		Integer storeId = workOrderQVRequest.getStoreId();

		/*Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String workOrderQVNo = GlobalService.getBillNoByGeneratedCode(storeId, workOrderQVRequest.getUnitId(), screenId);
		WoQuotationVendor workOrderQV = new WoQuotationVendor(new Status(workOrderQVRequest.getApprovalStatusId()), workOrderQVNo,
				workOrderQVRequest.getDueDate(),workOrderQVRequest.getWoQuotationRequestId()!=null ? new WorkOrderQuotationRequest(workOrderQVRequest.getWoQuotationRequestId()) : null ,
				workOrderQVRequest.getRemark(), new Status(workOrderQVRequest.getStatusId()), new Vendor(workOrderQVRequest.getVendorId()));

		for(WorkOrderQVDetailRequest item : workOrderQVRequest.getItems())
		{
			WoQuotationVendorDetail woQuotationVendorDetail=new WoQuotationVendorDetail(item.getWorkOrderQRDetailId()!=null ? new WorkOrderQuotationRequestDetail(item.getWorkOrderQRDetailId()) : null,
					item.getRemark(), new Status(item.getStatusId()));
//			sessionService.setEntityDefaults(woQuotationVendorDetail);
			woQuotationVendorDetail.setCreateDefaults(workOrderQVRequest.getOrganizationId(),workOrderQVRequest.getUnitId(),
					workOrderQVRequest.getUserId());
			workOrderQV.addWoQuotationSendToVendorDetailList(woQuotationVendorDetail);
		}
//		sessionService.setEntityDefaults(workOrderQV);
		workOrderQV.setCreateDefaults(workOrderQVRequest.getOrganizationId(),workOrderQVRequest.getUnitId(),
				workOrderQVRequest.getUserId());
		workOrderQuotationVendorDao.saveWoQuotationVendor(workOrderQV);
		CommonResponse response = new CommonResponse();
		response.setId(workOrderQV.getId().longValue());


		return response;

	}

	@Override
	@Transactional
	public Object updateWoQuotationVendor(WorkOrderQVRequest workOrderQVRequest) {

		WoQuotationVendor workOrderQV = workOrderQuotationVendorDao.getWoQuotationVendor(workOrderQVRequest.getId());

		workOrderQV.updateWoQuotationVendor(
				workOrderQVRequest.getRemark() != null ? workOrderQVRequest.getRemark() : workOrderQV.getRemark());

		for (WoQuotationVendorDetail detail : workOrderQV.getWoQuotationVendorDetail()) {

			Integer id = null;

			for (WorkOrderQVDetailRequest item : workOrderQVRequest.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();

					detail.updateWoQuotationVendorDetail(
							item.getRemark() != null ? item.getRemark() : detail.getRemark());
				}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQV.getUnitId()));
			detail.setUpdatedByUser(workOrderQVRequest.getUserId());

		}

		for (WorkOrderQVDetailRequest item : workOrderQVRequest.getItems()) {
			if (item.getId() == null) {

				workOrderQV.addWoQuotationSendToVendorDetailList(
						new WoQuotationVendorDetail(item.getWorkOrderQRDetailId() != null
								? new WorkOrderQuotationRequestDetail(item.getWorkOrderQRDetailId())
								: null, item.getRemark(), new Status(item.getStatusId())));

			}
		}
		workOrderQV.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQV.getUnitId()));
		workOrderQV.setUpdatedByUser(workOrderQVRequest.getUserId());

		workOrderQuotationVendorDao.updateWoQuotationVendor(workOrderQV);

		CommonResponse response = new CommonResponse();
		response.setMessage("Quotation Send To Vendor updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getWoQuotationVendor(Integer id) {

		WoQuotationVendor workOrderQV = workOrderQuotationVendorDao.getWoQuotationVendor(id);

		WorkOrderQVResponse response = new WorkOrderQVResponse(workOrderQV.getId(),
				workOrderQV.getWorkOrderQR() != null ? workOrderQV.getWorkOrderQR().getWorkOrderQRNo() : null,
				workOrderQV.getWorkOrderQR() != null ? workOrderQV.getWorkOrderQR().getAddedDate() : null,
				workOrderQV.getQvNo(), workOrderQV.getAddedDate(),
				workOrderQV.getWorkOrderQR() != null ? workOrderQV.getWorkOrderQR().getStore().getStoreName() : null,
				workOrderQV.getWorkOrderQR() != null ? workOrderQV.getWorkOrderQR().getStore().getStoreId() : null,
				workOrderQV.getVendor().getName(), workOrderQV.getVendor().getId(),
				workOrderQV.getWorkOrderQR() != null ? workOrderQV.getWorkOrderQR().getContractType().getCode() : null,
				workOrderQV.getWorkOrderQR() != null ? workOrderQV.getWorkOrderQR().getContractType().getId() : null,
				workOrderQV.getWorkOrderQR() != null ? workOrderQV.getWorkOrderQR().getRequestType().getType() : null,
				workOrderQV.getWorkOrderQR() != null ? workOrderQV.getWorkOrderQR().getRequestType().getId() : null,
				workOrderQV.getDueDate(), workOrderQV.getRemark(), workOrderQV.getStatus().getStatus(),
				workOrderQV.getStatus().getId(), workOrderQV.getApprovalStatus().getStatus(),
				workOrderQV.getApprovalStatus().getId(), workOrderQV.getAddedBy().getUserName());

		for (WoQuotationVendorDetail detail : workOrderQV.getWoQuotationVendorDetail()) {
			if (detail.getIsDeleted() != true && detail.getIsActive() != false) {

				response.addItems(new WorkOrderQVDetailResponse(detail.getId(),
						detail.getWorkOrderQRDetail() != null ? detail.getWorkOrderQRDetail().getItem().getId() : null,
						detail.getWorkOrderQRDetail() != null ? detail.getWorkOrderQRDetail().getItem().getCode()
								: null,
						detail.getWorkOrderQRDetail() != null ? detail.getWorkOrderQRDetail().getItem().getName()
								: null,
						detail.getWorkOrderQRDetail() != null ? detail.getWorkOrderQRDetail().getQuantity() : 0,
						detail.getWorkOrderQRDetail().getCurrentVendor() != null
								? detail.getWorkOrderQRDetail().getCurrentVendor().getId()
								: null,
						detail.getWorkOrderQRDetail().getCurrentVendor() != null
								? detail.getWorkOrderQRDetail().getCurrentVendor().getName()
								: null,
						detail.getWorkOrderQRDetail() != null ? detail.getWorkOrderQRDetail().getSpecification() : null,
						detail.getWorkOrderQRDetail() != null ? detail.getWorkOrderQRDetail().getExpectedInclusion()
								: null,
						detail.getRemark(),
						detail.getWorkOrderQRDetail().getBatch() != null
								? detail.getWorkOrderQRDetail().getBatch().getBatchNo()
								: null,
						detail.getWorkOrderQRDetail() != null ? getWoQuotationReq(detail) : null));
			}
		}

		return response;
	}

	private List<WorkOrderQRDto> getWoQuotationReq(WoQuotationVendorDetail detail) {
		List<WorkOrderQRDto> requestDetails = new ArrayList<WorkOrderQRDto>();
		if (detail.getWorkOrderQRDetail().getId() != null) {
			WorkOrderQuotationRequest workOrderQR = workOrderQuotationVendorDao
					.getWorkOrderQuotationRequest(detail.getWorkOrderQRDetail().getWorkOrderQuotationRequest().getId());
			if (workOrderQR != null) {
				WorkOrderQRDto qr = new WorkOrderQRDto(workOrderQR.getId(), workOrderQR.getWorkOrderQRNo(),
						workOrderQR.getAddedDate());
				requestDetails.add(qr);
				// return requestDetails;
			}
		}
		/*HIS-6975
		 * else { return null; }
		 */
		// return null;
		return requestDetails;
	}

	private List<WorkOrderQRDto> getWoQuotationReqForDetails(WorkOrderQuotationRequestDetail detail) {
		List<WorkOrderQRDto> requestDetails = new ArrayList<WorkOrderQRDto>();

		WorkOrderQuotationRequest workOrderQR = workOrderQuotationVendorDao
				.getWorkOrderQuotationRequest(detail.getWorkOrderQuotationRequest().getId());
		if (workOrderQR != null) {
			WorkOrderQRDto qr = new WorkOrderQRDto(workOrderQR.getId(), workOrderQR.getWorkOrderQRNo(),
					workOrderQR.getAddedDate());
			requestDetails.add(qr);
			// return requestDetails;
		}
		/*
		 * else { return null; }
		 */
		// return null;
		return requestDetails;
	}

	@Override
	@Transactional
	public Object approveWoQuotationVendor(WorkOrderQVRequest workOrderQVRequest) {

		for (Integer id : workOrderQVRequest.getQvIds()) {
			WoQuotationVendor workOrderQV = workOrderQuotationVendorDao.getWoQuotationVendor(id);

			workOrderQV.setStatus(new Status(workOrderQVRequest.getStatusId()));
			workOrderQV.setApprovalStatus(new Status(workOrderQVRequest.getApprovalStatusId()));

			for (WoQuotationVendorDetail detail : workOrderQV.getWoQuotationVendorDetail()) {
				detail.setStatus(new Status(workOrderQVRequest.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQV.getUnitId()));
				detail.setUpdatedByUser(workOrderQVRequest.getUserId());
			}

			workOrderQV.setApprovedDate(TimeZoneComponent.getDateByzone(workOrderQV.getUnitId()));
			workOrderQV.setApprovedBy(new User(workOrderQVRequest.getUserId()));

			workOrderQV.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQV.getUnitId()));
			workOrderQV.setUpdatedByUser(workOrderQVRequest.getUserId());

			workOrderQuotationVendorDao.updateWoQuotationVendor(workOrderQV);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Quotation Send To Vendor approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelWoQuotationVendor(WorkOrderQVRequest workOrderQVRequest) {

		WoQuotationVendor workOrderQV = workOrderQuotationVendorDao.getWoQuotationVendor(workOrderQVRequest.getId());

		for (WoQuotationVendorDetail detail : workOrderQV.getWoQuotationVendorDetail()) {
			detail.setStatus(new Status(workOrderQVRequest.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQV.getUnitId()));
			detail.setUpdatedByUser(workOrderQVRequest.getUserId());
		}
		workOrderQV.setCancelReason(new CancelReason(workOrderQVRequest.getCancelReasonId()));
		workOrderQV.setCancelNote(workOrderQVRequest.getCancelNote());

		workOrderQV.setStatus(new Status(workOrderQVRequest.getStatusId()));
		workOrderQV.setApprovalStatus(new Status(workOrderQVRequest.getApprovalStatusId()));

		workOrderQV.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQV.getUnitId()));
		workOrderQV.setUpdatedByUser(workOrderQVRequest.getUserId());

		workOrderQuotationVendorDao.updateWoQuotationVendor(workOrderQV);

		CommonResponse response = new CommonResponse();
		response.setMessage("Quotation Send To Vendor cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getWorkOrderQRPopSearchList(WorkOrderQR workOrderQuotationReq) {
		workOrderQuotationReq.setToDate((new DateTime(workOrderQuotationReq.getToDate())).plusDays(1).toDate());

		List<WorkOrderQrResponse> responseList = new ArrayList<WorkOrderQrResponse>();

		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
		statusIds.add(com.param.common.constants.Status.PendingApproval.getStatus());

		for (WorkOrderQuotationRequest workOrderQR : workOrderQuotationVendorDao
				.getWorkOrderQuotationRequestSearchList(workOrderQuotationReq, statusIds)) {

			List<WorkOrderQuotationRequest> wos = workOrderQuotationVendorDao
					.getByVendorAndId(workOrderQuotationReq.getVendorId(), workOrderQR.getId());

			if (wos != null && wos.isEmpty()) {
				WorkOrderQrResponse response = new WorkOrderQrResponse(workOrderQR.getId(),
						workOrderQR.getApprovalStatus().getId(),
						workOrderQR.getApprovedBy() != null ? workOrderQR.getApprovedBy().getUserId() : null,
						workOrderQR.getContractType().getId(), workOrderQR.getRemark(),
						workOrderQR.getRequestType().getId(), workOrderQR.getSendVendorSelection(),
						workOrderQR.getStatus().getId(), workOrderQR.getStore().getStoreId(),
						workOrderQR.getWorkOrderRequest() != null ? workOrderQR.getWorkOrderRequest().getId() : null,
						workOrderQR.getApprovalStatus().getStatus(),
						workOrderQR.getApprovedBy() != null ? workOrderQR.getApprovedBy().getLoginName() : null,
						workOrderQR.getApprovedDate(), workOrderQR.getContractType().getCode(),
						workOrderQR.getRequestType().getType(), workOrderQR.getStatus().getStatus(),
						workOrderQR.getStore().getStoreName(), workOrderQR.getWorkOrderQRNo(),
						workOrderQR.getAddedDate(), workOrderQR.getAddedBy().getUserName(),
						workOrderQR.getRequestForNegotiation());

				for (WorkOrderQuotationRequestDetail detail : workOrderQR.getWorkOrderQRDetailsList()) {
					if (detail.getIsDeleted() != true && detail.getIsActive() != false) {
						response.addItems(new WorkOrderQRDetailResponse(detail.getId(),
								detail.getCurrentVendor() != null ? detail.getCurrentVendor().getId() : null,
								detail.getSpecification(), detail.getStatus().getId(),
								detail.getWorkOrderQuotationRequest().getId(), detail.getRemark(), detail.getQuantity(),
								detail.getExpectedInclusion(), detail.getItem().getId(),
								detail.getCurrentVendor() != null ? detail.getCurrentVendor().getName() : null,
								detail.getStatus().getStatus(), detail.getItem().getName(), detail.getItem().getCode(),
								detail.getBatch() != null ? detail.getBatch().getId() : null,
								detail.getBatch() != null ? detail.getBatch().getBatchNo() : null));
					}
				}
				responseList.add(response);
			}
		}
		return responseList;
	}

	@Override
	@Transactional
	public Object getWoQuotationRequestDetails(List<WorkOrderQR> workOrderQuotationReqList) {

		List<WorkOrderQrResponse> responseList = new ArrayList<WorkOrderQrResponse>();
		for (WorkOrderQR workOrderQuotationReq : workOrderQuotationReqList) {

			WorkOrderQuotationRequest workOrderQR = workOrderQuotationVendorDao
					.getWorkOrderQuotationRequest(workOrderQuotationReq.getId());

			WorkOrderQrResponse response = new WorkOrderQrResponse(workOrderQR.getId(),
					workOrderQR.getApprovalStatus().getId(),
					workOrderQR.getApprovedBy() != null ? workOrderQR.getApprovedBy().getUserId() : null,
					workOrderQR.getContractType().getId(), workOrderQR.getRemark(),
					workOrderQR.getRequestType().getId(), workOrderQR.getSendVendorSelection(),
					workOrderQR.getStatus().getId(), workOrderQR.getStore().getStoreId(),
					workOrderQR.getWorkOrderRequest() != null ? workOrderQR.getWorkOrderRequest().getId() : null,
					workOrderQR.getApprovalStatus().getStatus(),
					workOrderQR.getApprovedBy() != null ? workOrderQR.getApprovedBy().getLoginName() : null,
					workOrderQR.getApprovedDate(), workOrderQR.getContractType().getCode(),
					workOrderQR.getRequestType().getType(), workOrderQR.getStatus().getStatus(),
					workOrderQR.getStore().getStoreName(), workOrderQR.getWorkOrderQRNo(), workOrderQR.getAddedDate(),
					workOrderQR.getAddedBy().getUserName(), workOrderQR.getRequestForNegotiation());

			for (Integer itemId : workOrderQuotationReq.getQrIds()) {
				for (WorkOrderQuotationRequestDetail detail : workOrderQR.getWorkOrderQRDetailsList()) {
					List<WorkOrderQRDto> requestDetails = new ArrayList<WorkOrderQRDto>();
					WorkOrderQRDto qr = new WorkOrderQRDto(workOrderQR.getId(), workOrderQR.getWorkOrderQRNo(),
							workOrderQR.getAddedDate());
					requestDetails.add(qr);

					if (itemId.equals(detail.getId()) && detail.getIsDeleted() != true
							&& detail.getIsActive() != false) {
						response.addItems(new WorkOrderQRDetailResponse(detail.getId(),
								detail.getCurrentVendor() != null ? detail.getCurrentVendor().getId() : null,
								detail.getSpecification(), detail.getStatus().getId(),
								detail.getWorkOrderQuotationRequest().getId(), detail.getRemark(), detail.getQuantity(),
								detail.getExpectedInclusion(), detail.getItem().getId(),
								detail.getCurrentVendor() != null ? detail.getCurrentVendor().getName() : null,
								detail.getStatus().getStatus(), detail.getItem().getName(), detail.getItem().getCode(),
								detail.getBatch() != null ? detail.getBatch().getId() : null, requestDetails,
								detail.getBatch() != null ? detail.getBatch().getBatchNo() : null));

					}
				}
			}
			responseList.add(response);
		}
		return responseList;
	}

}
