package com.param.procurement.workOrder.service.workOrderRequestApproval;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.procurement.ContractType;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.procurement.workOrder.dao.workOrderRequest.IWorkOrderRequestDao;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderRequest;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderRequestDetail;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderResponse;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderResponseDetail;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class WorkOrderRequestApprovalService implements IWorkOrderRequestApprovalService {

	private Integer screenId = Screen.WorkorderRequestApproval.getScreen();

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IItemsDao ItemsDao;

	@Autowired
	IWorkOrderRequestDao workOrderRequestDao;

	@Autowired
	SessionService sessionService;

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
	public Object getWorkOrderList(WorkOrderRequest workOrderRequest) {
		workOrderRequest.setToDate((new DateTime(workOrderRequest.getToDate())).plusDays(1).toDate());

		List<WorkOrderResponse> responseList = new ArrayList<WorkOrderResponse>();

		for (com.param.entity.model.procurement.WorkOrderRequest workOrder : workOrderRequestDao
				.searchWorkOrderList(workOrderRequest, screenId)) {

			WorkOrderResponse response = new WorkOrderResponse(workOrder.getId(), workOrder.getRequestNo(),
					workOrder.getAddedDate(), workOrder.getRemark(), workOrder.getAddedBy().getUserName(),
					workOrder.getAddedDate(), workOrder.getStore().getStoreName(),
					workOrder.getContractType().getCode(), workOrder.getStatus().getStatus(),
					workOrder.getApprovalStatus().getStatus());

			responseList.add(response);

		}
		return responseList;
	}

	@Override
	@Transactional
	public Object getWorkOrderRequest(Integer id,Integer unitId) {

		com.param.entity.model.procurement.WorkOrderRequest workOrder = workOrderRequestDao.getWorkOrderRequest(id);

		WorkOrderResponse response = new WorkOrderResponse(workOrder.getId(), workOrder.getRequestNo(),
				workOrder.getAddedDate(), workOrder.getRemark(), workOrder.getAddedBy().getUserName(),
				workOrder.getAddedDate(), workOrder.getStore().getStoreId(), workOrder.getStore().getStoreName(),
				workOrder.getContractType().getCode(), workOrder.getStatus().getId(), workOrder.getStatus().getStatus(),
				workOrder.getApprovalStatus().getId(), workOrder.getApprovalStatus().getStatus());

		for (com.param.entity.model.procurement.WorkOrderRequestDetail detail : workOrder.getWorkOrderDetailList()) {
			if (detail.getIsDeleted() != true && detail.getIsActive() != false) {
				response.setIsCreation(detail.getSendWorkOrderCreation());
				response.setIsQuotation(detail.getSendQuotationRequest());
				response.addItems(new WorkOrderResponseDetail(detail.getId(), detail.getApprovedQuantity(),
						detail.getRejectQuantity(), 
						detail.getCurrentVendor() != null ? detail.getCurrentVendor().getId() : null,
						detail.getItem().getId(),
						detail.getQuantity(), detail.getRemark(), detail.getSpecification(), detail.getStatus().getId(),
						detail.getVendor() != null ? detail.getVendor().getId() : null,
						detail.getCurrentVendor() != null ? detail.getCurrentVendor().getName() : null,
						detail.getItem().getCode(),detail.getItem().getName(),
						detail.getVendor() != null ? detail.getVendor().getName() : null ,
						detail.getStatus().getStatus(),
						detail.getBatch()!=null ? detail.getBatch().getId() : null,
						detail.getBatch()!=null ? detail.getBatch().getBatchNo() : null,
						ItemsDao.batchListByItemId(detail.getItem().getId(),workOrder.getStore().getStoreId(),unitId)));
			}
		}
		

		return response;
	}

	@Override
	@Transactional
	public Object approveWorkOrderRequest(WorkOrderRequest workOrderRequest) {

		com.param.entity.model.procurement.WorkOrderRequest workOrder = workOrderRequestDao.getWorkOrderRequest(workOrderRequest.getId());

		for (com.param.entity.model.procurement.WorkOrderRequestDetail detail : workOrder.getWorkOrderDetailList()) {

			for (WorkOrderRequestDetail item : workOrderRequest.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					detail.approveWorkOrderRequestDetail(item.getApprovedQuantity(), (new User(workOrderRequest.getUserId())),
							TimeZoneComponent.getDateByzone(workOrder.getUnitId()), item.getRejectReason(), item.getRejectQuantity(),
							new RejectReason(item.getRejectReasonId()), new Status(item.getStatusId()), TimeZoneComponent.getDateByzone(workOrder.getUnitId()),
							(new User(workOrderRequest.getUserId())),workOrderRequest.getSendWorkOrderCreation(), workOrderRequest.getSendQuotationRequest());
					break;
				}
			}
		}
		workOrder.setStatus(new Status(workOrderRequest.getStatusId()));
		workOrder.setApprovalStatus(new Status(workOrderRequest.getApprovalStatusId()));

		workOrder.setApprovedOn(TimeZoneComponent.getDateByzone(workOrder.getUnitId()));
		workOrder.setApprovedBy(new User(workOrderRequest.getUserId()));

		workOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrder.getUnitId()));
		workOrder.setUpdatedByUser(workOrderRequest.getUserId());

		workOrderRequestDao.updateWorkOrderRequest(workOrder);

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order Approval approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelworkOrderRequest(WorkOrderRequest workOrderRequest) {

		com.param.entity.model.procurement.WorkOrderRequest workOrder = workOrderRequestDao
				.getWorkOrderRequest(workOrderRequest.getId());

		for (com.param.entity.model.procurement.WorkOrderRequestDetail detail : workOrder.getWorkOrderDetailList()) {
			detail.setStatus(new Status(workOrderRequest.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrder.getUnitId()));
			detail.setUpdatedByUser(workOrderRequest.getUserId());
		}
		workOrder.setCancelReason(new CancelReason(workOrderRequest.getCancelReasonId()));
		workOrder.setCancelNote(workOrderRequest.getCancelNote());

		workOrder.setStatus(new Status(workOrderRequest.getStatusId()));
		workOrder.setApprovalStatus(new Status(workOrderRequest.getApprovalStatusId()));

		workOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrder.getUnitId()));
		workOrder.setUpdatedByUser(workOrderRequest.getUserId());

		workOrderRequestDao.updateWorkOrderRequest(workOrder);

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order Approval cancelled successfully");

		return response;
	}

}
