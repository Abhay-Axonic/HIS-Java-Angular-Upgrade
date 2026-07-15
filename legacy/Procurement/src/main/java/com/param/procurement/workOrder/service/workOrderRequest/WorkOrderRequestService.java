package com.param.procurement.workOrder.service.workOrderRequest;

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
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.ContractType;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.service.global.IGlobalService;
import com.param.procurement.workOrder.dao.workOrderRequest.IWorkOrderRequestDao;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderRequest;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderRequestDetail;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderResponse;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderResponseDetail;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class WorkOrderRequestService implements IWorkOrderRequestService{
	
	private Integer screenId = Screen.WorkorderRequest.getScreen();

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;
	
	@Autowired
	IItemsDao ItemsDao;
	
	@Autowired
	IWorkOrderRequestDao workOrderRequestDao;

	@Autowired
	private SessionService sessionService;
	
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
			workOrderDropDown.addContractType(new com.param.global.dto.global.ContractType(contractType.getId(), 
					contractType.getCode(),contractType.getDescription()));
			
		}

		return workOrderDropDown;
	}

	@Override
	@Transactional
	public Object getWorkOrderList(WorkOrderRequest workOrderRequest) {
		workOrderRequest.setToDate((new DateTime(workOrderRequest.getToDate())).plusDays(1).toDate());

		List<WorkOrderResponse> responseList = new ArrayList<WorkOrderResponse>();

		for (com.param.entity.model.procurement.WorkOrderRequest workOrder : workOrderRequestDao.searchWorkOrderList(workOrderRequest, screenId)) {
			
			WorkOrderResponse response = new WorkOrderResponse(workOrder.getId(), workOrder.getRequestNo(), workOrder.getAddedDate(), 
					workOrder.getRemark(), workOrder.getAddedBy().getUserName(), workOrder.getAddedDate(), workOrder.getStore().getStoreName(), 
					workOrder.getContractType().getDescription(), workOrder.getStatus().getStatus(), workOrder.getApprovalStatus().getStatus());

			responseList.add(response);

		}
		return responseList;
	}

	@Override
	@Transactional
	public Object saveWorkOrderRequest(WorkOrderRequest workOrderRequest) {

		/*
		Random rand = new Random();
		int num = rand.nextInt(1000);
		String reqNo = "REQ-" + num;
		*/
		/*Integer storeId = workOrderRequest.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String workOrderReqNo = GlobalService.getBillNoByGeneratedCode(workOrderRequest.getStoreId(), sessionService.getUnitId(), screenId);
		
		com.param.entity.model.procurement.WorkOrderRequest workOrder = new com.param.entity.model.procurement.WorkOrderRequest(new Status(workOrderRequest.getApprovalStatusId()), 
				workOrderRequest.getRemark(), workOrderReqNo, new Status(workOrderRequest.getStatusId()), new Store(workOrderRequest.getStoreId()), 
				new ContractType(workOrderRequest.getContractTypeId()));
//		sessionService.setEntityDefaults(workOrder);
		workOrder.setCreateDefaults(workOrderRequest.getOrganizationId(),workOrderRequest.getUnitId(),
				workOrderRequest.getUserId());
		for(WorkOrderRequestDetail item : workOrderRequest.getItems())
		{
			com.param.entity.model.procurement.WorkOrderRequestDetail workOrderRequestDetail= 	new com.param.entity.model.procurement.WorkOrderRequestDetail(item.getCurrentVendorId()!=null ? new Vendor(item.getCurrentVendorId()) : null,
					new Item(item.getItemId()), item.getQuantity(), item.getRemark(), 
					item.getSpecification(), new Status(item.getStatusId()), new Vendor(item.getVendorId()),
					item.getBatchId()!=null ? new Batch((long) item.getBatchId()) : null);
//			sessionService.setEntityDefaults(workOrderRequestDetail);	
			workOrderRequestDetail.setCreateDefaults(workOrderRequest.getOrganizationId(),workOrderRequest.getUnitId(),
					workOrderRequest.getUserId());
			workOrder.addWorkOrderDetailList(workOrderRequestDetail);
		}
		
		workOrderRequestDao.saveWorkOrderRequest(workOrder);
		CommonResponse response = new CommonResponse();
		response.setId(workOrder.getId().longValue());
		response.setMessage("Work Order Request saved successfully");
		return response;
	}

	@Override
	@Transactional
	public Object updateWorkOrderRequest(WorkOrderRequest workOrderRequest) {

			
		com.param.entity.model.procurement.WorkOrderRequest workOrder = workOrderRequestDao.getWorkOrderRequest(workOrderRequest.getId());
		
		workOrder.updateWorkOrderRequest(workOrderRequest.getRemark()!=null ? workOrderRequest.getRemark() : workOrder.getRemark());
		workOrder.setContractType(new ContractType(workOrderRequest.getContractTypeId()));
		for(com.param.entity.model.procurement.WorkOrderRequestDetail detail : workOrder.getWorkOrderDetailList()) {
			Integer id = null;
			
			for(WorkOrderRequestDetail item : workOrderRequest.getItems()) {
				if(item.getId() !=null && item.getId().equals(detail.getId())) {
					id = item.getId();
					
					detail.updateWorkOrderRequestDetail(item.getQuantity()!=null ? item.getQuantity() : detail.getQuantity(), item.getRemark()!=null ? item.getRemark() : detail.getRemark(),
							item.getSpecification()!=null ? item.getSpecification() :detail.getSpecification(),
					        item.getCurrentVendorId()!=null ? new Vendor(item.getCurrentVendorId()) : detail.getCurrentVendor(),		
							item.getBatchId()!=null ? new Batch((long) item.getBatchId()) : detail.getBatch());
					
					}
			}
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrder.getUnitId()));
			detail.setUpdatedByUser(workOrderRequest.getUserId());

		}
		
		for(WorkOrderRequestDetail item : workOrderRequest.getItems()) {
		      if (item.getId() == null)
		      {
		    	  com.param.entity.model.procurement.WorkOrderRequestDetail  workOrderRequestDetail= new com.param.entity.model.procurement.WorkOrderRequestDetail(item.getCurrentVendorId()!=null ? new Vendor(item.getCurrentVendorId()) : null, 
		    			  new Item(item.getItemId()), item.getQuantity(), item.getRemark(), 
							item.getSpecification(), new Status(item.getStatusId()), new Vendor(item.getVendorId()),
							item.getBatchId()!=null ? new Batch((long) item.getBatchId()) : null);
//		    	  sessionService.setEntityDefaults(workOrderRequestDetail);
		    	  workOrderRequestDetail.setCreateDefaults(workOrderRequest.getOrganizationId(),workOrderRequest.getUnitId(),
							workOrderRequest.getUserId());
		    	  workOrder.addWorkOrderDetailList(workOrderRequestDetail);
					
			}	 
	  }
		workOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrder.getUnitId()));
		workOrder.setUpdatedByUser(workOrderRequest.getUserId());

		workOrderRequestDao.updateWorkOrderRequest(workOrder);

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order Request updated successfully");

		return response;
	}
		
		

	@Override
	@Transactional
	public Object getWorkOrderRequest(Integer id,Integer unitId) {

		com.param.entity.model.procurement.WorkOrderRequest workOrder = workOrderRequestDao.getWorkOrderRequest(id);
		
		
		WorkOrderResponse response = new WorkOrderResponse(workOrder.getId(), workOrder.getRequestNo(), workOrder.getAddedDate(), workOrder.getRemark(), 
				workOrder.getAddedBy().getUserName(), workOrder.getAddedDate(), 
				workOrder.getStore().getStoreId(), workOrder.getStore().getStoreName(), workOrder.getContractType().getDescription(), 
				workOrder.getStatus().getId(), workOrder.getStatus().getStatus(), workOrder.getApprovalStatus().getId(), workOrder.getApprovalStatus().getStatus());
				
		for(com.param.entity.model.procurement.WorkOrderRequestDetail detail : workOrder.getWorkOrderDetailList()) {
			if(detail.getIsDeleted()!=true && detail.getIsActive()!=false) {
				
				response.addItems(new WorkOrderResponseDetail(detail.getId(), detail.getApprovedQuantity(), detail.getRejectQuantity(),
						detail.getCurrentVendor()!=null ? detail.getCurrentVendor().getId() : null, detail.getItem().getId(), detail.getQuantity(), detail.getRemark(), detail.getSpecification(), 
						detail.getStatus().getId(), detail.getVendor().getId(), detail.getCurrentVendor()!=null ? detail.getCurrentVendor().getName() : null, detail.getItem().getCode(), 
						detail.getItem().getName(), detail.getVendor().getName(),detail.getStatus().getStatus(),
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

		for (Integer id : workOrderRequest.getWorkOrderIds()) {
			com.param.entity.model.procurement.WorkOrderRequest workOrder = workOrderRequestDao.getWorkOrderRequest(id);

			workOrder.setStatus(new Status(workOrderRequest.getStatusId()));
			workOrder.setApprovalStatus(new Status(workOrderRequest.getApprovalStatusId()));

			for(com.param.entity.model.procurement.WorkOrderRequestDetail detail : workOrder.getWorkOrderDetailList()) {
				detail.setStatus(new Status(workOrderRequest.getStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrder.getUnitId()));
				detail.setUpdatedByUser(workOrderRequest.getUserId());
			}

			workOrder.setApprovedOn(TimeZoneComponent.getDateByzone(workOrder.getUnitId()));
			workOrder.setApprovedBy(new User(workOrderRequest.getUserId()));

			workOrder.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrder.getUnitId()));
			workOrder.setUpdatedByUser(workOrderRequest.getUserId());

			workOrderRequestDao.updateWorkOrderRequest(workOrder);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelworkOrderRequest(WorkOrderRequest workOrderRequest) {

		com.param.entity.model.procurement.WorkOrderRequest workOrder = workOrderRequestDao.getWorkOrderRequest(workOrderRequest.getId());
		
		for(com.param.entity.model.procurement.WorkOrderRequestDetail detail : workOrder.getWorkOrderDetailList()) {
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
		response.setMessage("Work Order cancelled successfully");

		return response;
	}


}
