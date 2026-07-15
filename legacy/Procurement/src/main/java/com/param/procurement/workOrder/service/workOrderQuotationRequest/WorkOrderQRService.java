package com.param.procurement.workOrder.service.workOrderQuotationRequest;

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
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.ContractType;
import com.param.entity.model.procurement.WoQuotationNegotiation;
import com.param.entity.model.procurement.WoQuotationNegotiationDetail;
import com.param.entity.model.procurement.WoQuotationVendor;
import com.param.entity.model.procurement.WoQuotationVendorDetail;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.entity.model.procurement.WorkOrderQuotationRequestDetail;
import com.param.entity.model.procurement.WorkOrderRequest;
import com.param.entity.model.procurement.WorkOrderRequestDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.service.global.IGlobalService;
import com.param.procurement.workOrder.dao.workOrderNegotiation.IWorkOrderNegotiationDao;
import com.param.procurement.workOrder.dao.workOrderQuotationRequest.IWorkOrderQRDao;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderDetailResponseDto;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQR;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQRDetail;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQRDetailResponse;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQrResponse;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderResponseDto;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class WorkOrderQRService implements IWorkOrderQRService{
	
	private Integer screenId = Screen.WorkorderQuotationRequest.getScreen();

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;
	
	@Autowired
	IItemsDao ItemsDao;
		
	@Autowired
	IWorkOrderQRDao workOrderQRDao;
	
	@Autowired
	IWorkOrderNegotiationDao workOrderNegotiationDao;

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
					contractType.getCode()));
			
		}

		return workOrderDropDown;
	}

	@Override
	@Transactional
	public Object getWorkOrderQuotationRequestList(WorkOrderQR workOrderQuotationReq) {
		workOrderQuotationReq.setToDate((new DateTime(workOrderQuotationReq.getToDate())).plusDays(1).toDate());

		List<WorkOrderQrResponse> responseList = new ArrayList<WorkOrderQrResponse>();

		for (WorkOrderQuotationRequest  workOrderQR : workOrderQRDao.searchWorkOrderQRList(workOrderQuotationReq, screenId)) {
			
			WorkOrderQrResponse response = new WorkOrderQrResponse(workOrderQR.getId(), workOrderQR.getRemark(), workOrderQR.getRequestForNegotiation(), 
					workOrderQR.getApprovalStatus().getStatus(),workOrderQR.getContractType().getCode(), workOrderQR.getRequestType().getType(), 
					workOrderQR.getStatus().getStatus(), workOrderQR.getStore().getStoreName(), workOrderQR.getWorkOrderQRNo(),workOrderQR.getAddedDate(),workOrderQR.getAddedBy().getUserName());

			responseList.add(response);

		}
		return responseList;
	}

	@Override
	@Transactional
	public Object saveWorkOrderQuotationRequest(WorkOrderQR workOrderQuotationReq) {

		/*Random rand = new Random();
		int num = rand.nextInt(1000);
		String workOrderQRNo = "WQR-" + num;
		*/
		/*Integer storeId = workOrderQuotationReq.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String workOrderQRNo = GlobalService.getBillNoByGeneratedCode(workOrderQuotationReq.getStoreId(), workOrderQuotationReq.getUnitId(), screenId);
		
		WorkOrderQuotationRequest workOrderQR = new WorkOrderQuotationRequest(new Status(workOrderQuotationReq.getApprovalStatusId()), new ContractType(workOrderQuotationReq.getContractTypeId()), workOrderQuotationReq.getRemark(), new RequestType(workOrderQuotationReq.getRequestTypeId()), 
				workOrderQuotationReq.getSendVendorSelection(), new Status(workOrderQuotationReq.getStatusId()), new Store(workOrderQuotationReq.getStoreId()), 
				workOrderQuotationReq.getWorkOrderId()!=null ? new WorkOrderRequest(workOrderQuotationReq.getWorkOrderId()) : null , workOrderQRNo);
//		sessionService.setEntityDefaults(workOrderQR);	
		workOrderQR.setCreateDefaults(workOrderQuotationReq.getOrganizationId(),workOrderQuotationReq.getUnitId(),
				workOrderQuotationReq.getUserId());
		for(WorkOrderQRDetail item : workOrderQuotationReq.getItems())
		{
			WorkOrderQuotationRequestDetail workOrderQuotationRequestDetail=	new WorkOrderQuotationRequestDetail(item.getCurrentVendorId()!=null ? new Vendor(item.getCurrentVendorId()) : null, item.getSpecification(), new Status(item.getStatusId()),
					item.getWorkOrderRequestDetailId()!=null ? new WorkOrderRequestDetail(item.getWorkOrderRequestDetailId()) : null, 
					item.getRemark(), item.getQuantity(), item.getExpectedInclusion(), 
					new Item(item.getItemId()),item.getBatchId()!=null ?new Batch(item.getBatchId()) :  null);
//			sessionService.setEntityDefaults(workOrderQuotationRequestDetail);
			workOrderQuotationRequestDetail.setCreateDefaults(workOrderQuotationReq.getOrganizationId(),workOrderQuotationReq.getUnitId(),
					workOrderQuotationReq.getUserId());
			workOrderQR.addWorkOrderQRDetailList(workOrderQuotationRequestDetail);
		}
//		sessionService.setEntityDefaults(workOrderQR);
		workOrderQR.setCreateDefaults(workOrderQuotationReq.getOrganizationId(),workOrderQuotationReq.getUnitId(),
				workOrderQuotationReq.getUserId());
		workOrderQRDao.saveWorkOrderQuotationRequest(workOrderQR);
		CommonResponse response = new CommonResponse();
		response.setId(workOrderQR.getId().longValue());
		response.setMessage("Work Order Request saved successfully");
		return response;

	}

	@Override
	@Transactional
	public Object updateWorkOrderQuotationRequest(WorkOrderQR workOrderQuotationReq) {

			
		WorkOrderQuotationRequest workOrderQR = workOrderQRDao.getWorkOrderQuotationRequest(workOrderQuotationReq.getId());
		
		workOrderQR.updateWorkOrderQuotationRequest(workOrderQuotationReq.getRemark()!=null ? workOrderQuotationReq.getRemark() : workOrderQR.getRemark(),
				workOrderQuotationReq.getSendVendorSelection());
		
		for(WorkOrderQuotationRequestDetail detail : workOrderQR.getWorkOrderQRDetailsList()) {
			Integer id = null;
		
			for(WorkOrderQRDetail item : workOrderQuotationReq.getItems()) {
				if(item.getId() !=null && item.getId().equals(detail.getId())) {
					id = item.getId();
			
					detail.updateWorkOrderQuotationRequestDetail(item.getSpecification()!=null ? item.getSpecification() : detail.getSpecification(),
							item.getRemark()!=null ? item.getRemark() :detail.getRemark(),
									item.getQuantity()!=null ? item.getQuantity() : detail.getQuantity() ,
									item.getExpectedInclusion()!=null ? item.getExpectedInclusion() :detail.getExpectedInclusion(),
									item.getBatchId()!=null ?new Batch((long)item.getBatchId()) :  detail.getBatch(),
									item.getCurrentVendorId()!=null ? new Vendor(item.getCurrentVendorId()) : detail.getCurrentVendor());
		          }
			}
		if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQR.getUnitId()));
			detail.setUpdatedByUser(workOrderQuotationReq.getUserId());

		}
		
		for(WorkOrderQRDetail item : workOrderQuotationReq.getItems()) {
		      if (item.getId() == null)
		      {
		    	  WorkOrderQuotationRequestDetail workOrderQuotationRequestDetail=  new WorkOrderQuotationRequestDetail(
		    			  item.getCurrentVendorId() != null ? new Vendor(item.getCurrentVendorId()) : null, 
		    			  item.getSpecification(), new Status(item.getStatusId()),
		    			  item.getWorkOrderRequestDetailId()!=null ?new WorkOrderRequestDetail(item.getWorkOrderRequestDetailId()):null,
							item.getRemark(), item.getQuantity(), item.getExpectedInclusion(),new Item(item.getItemId()),
							item.getBatchId()!=null ?new Batch(item.getBatchId()) :  null);
//		    	  sessionService.setEntityDefaults(workOrderQuotationRequestDetail);
		    	  workOrderQuotationRequestDetail.setCreateDefaults(workOrderQuotationReq.getOrganizationId(),workOrderQuotationReq.getUnitId(),
		    			  workOrderQuotationReq.getUserId());
		    	  workOrderQR.addWorkOrderQRDetailList(workOrderQuotationRequestDetail);
					
			}	
	  }
		
		workOrderQR.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQR.getUnitId()));
		workOrderQR.setUpdatedByUser(workOrderQuotationReq.getUserId());

		workOrderQRDao.updateWorkOrderQuotationRequest(workOrderQR);

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order Request updated successfully");

		return response;
	}
		
		

	@Override
	@Transactional
	public Object getWorkOrderQuotationRequest(Integer id,Integer unitId) {

		WorkOrderQuotationRequest workOrderQR = workOrderQRDao.getWorkOrderQuotationRequest(id);
		
		WorkOrderQrResponse response = new WorkOrderQrResponse(workOrderQR.getId(), workOrderQR.getApprovalStatus().getId(), 
				workOrderQR.getApprovedBy()!=null ? workOrderQR.getApprovedBy().getUserId() : null,
				workOrderQR.getContractType().getId(), 
				workOrderQR.getRemark(), workOrderQR.getRequestType().getId(), workOrderQR.getSendVendorSelection(), workOrderQR.getStatus().getId(), 
				workOrderQR.getStore().getStoreId(),workOrderQR.getWorkOrderRequest()!=null ? workOrderQR.getWorkOrderRequest().getId():null, 
				workOrderQR.getApprovalStatus().getStatus(), workOrderQR.getApprovedBy()!=null ? workOrderQR.getApprovedBy().getLoginName() : null , 
				workOrderQR.getApprovedDate(), workOrderQR.getContractType().getCode(), workOrderQR.getRequestType().getType(),
				workOrderQR.getStatus().getStatus(), workOrderQR.getStore().getStoreName(), workOrderQR.getWorkOrderQRNo(),workOrderQR.getAddedDate()
				,workOrderQR.getAddedBy().getUserName(),workOrderQR.getRequestForNegotiation());
		
		for(WorkOrderQuotationRequestDetail detail : workOrderQR.getWorkOrderQRDetailsList()) {
			if(detail.getIsDeleted()!=true && detail.getIsActive()!=false) {
				
				response.addItems(new WorkOrderQRDetailResponse(detail.getId(), detail.getCurrentVendor()!=null ? detail.getCurrentVendor().getId() : null , detail.getSpecification(), detail.getStatus().getId(), 
						detail.getWorkOrderQuotationRequest().getId(), 
						detail.getRemark(), detail.getQuantity(), detail.getExpectedInclusion(), detail.getItem().getId(), 
						detail.getCurrentVendor()!=null ? detail.getCurrentVendor().getName() :null, detail.getStatus().getStatus(), detail.getItem().getName(), detail.getItem().getCode(),
						detail.getBatch()!=null ? detail.getBatch().getId() : null ,detail.getBatch()!=null ? detail.getBatch().getBatchNo() : null,
				        ItemsDao.batchListByItemId(detail.getItem().getId(),workOrderQR.getStore().getStoreId(),unitId)));
				}
			}
		
		return response;				
	}

	
	@Override
	@Transactional
	public Object approveWOQuotationRequest(WorkOrderQR workOrderQuotationReq) {

		for (Integer id : workOrderQuotationReq.getQrIds()) {
			WorkOrderQuotationRequest workOrderQR = workOrderQRDao.getWorkOrderQuotationRequest(id);

			workOrderQR.setStatus(new Status(workOrderQuotationReq.getStatusId()));
			workOrderQR.setApprovalStatus(new Status(workOrderQuotationReq.getApprovalStatusId()));

			for(WorkOrderQuotationRequestDetail detail : workOrderQR.getWorkOrderQRDetailsList()) {
				detail.setStatus(new Status(workOrderQuotationReq.getStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQR.getUnitId()));
				detail.setUpdatedByUser(workOrderQuotationReq.getUserId());
			}

			workOrderQR.setApprovedDate(TimeZoneComponent.getDateByzone(workOrderQR.getUnitId()));
			workOrderQR.setApprovedBy(new User(workOrderQuotationReq.getUserId()));

			workOrderQR.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQR.getUnitId()));
			workOrderQR.setUpdatedByUser(workOrderQuotationReq.getUserId());

			workOrderQRDao.updateWorkOrderQuotationRequest(workOrderQR);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order approved successfully");

		return response;
	}

	
	@Override
	@Transactional
	public Object approvalWOQuotationRequest(WorkOrderQR workOrderQuotationReq) {
		CommonResponse response = new CommonResponse();
		WorkOrderQuotationRequest workOrderQR = workOrderQRDao.getWorkOrderQuotationRequest(workOrderQuotationReq.getId());
		
		if(workOrderQR.getRequestForNegotiation().equals(false))
		{
			workOrderQR.setId(workOrderQuotationReq.getId());
			workOrderQR.setRequestForNegotiation(true);
			workOrderQR.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQR.getUnitId()));
			workOrderQR.setUpdatedByUser(workOrderQuotationReq.getUserId());
		}
        if(workOrderQR.getRequestForNegotiation().equals(true))
        {
            workOrderQR.setId(workOrderQuotationReq.getId());
            workOrderQR.setRequestForNegotiation(false);
            workOrderQR.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQR.getUnitId()));
            workOrderQR.setUpdatedByUser(workOrderQuotationReq.getUserId());
        }
		

		workOrderQRDao.updateWorkOrderQuotationRequest(workOrderQR);
		
		for(WoQuotationVendor woQuotationVendor : workOrderNegotiationDao.getWoQuotationVendor(workOrderQuotationReq.getId())) {
			
			WoQuotationNegotiation  workOrderNego = new WoQuotationNegotiation(new Status(2), 
					new WoQuotationVendor(woQuotationVendor.getId()));
            workOrderNego.setOrganizationId(workOrderQR.getOrganizationId());
            workOrderNego.setUnitId(workOrderQR.getUnitId());
            workOrderNego.setUpdatedByUser(workOrderQR.getUpdatedByUser());
            workOrderNego.setAddedByUser(workOrderQR.getAddedByUser());
							
			for(WoQuotationVendorDetail item : woQuotationVendor.getWoQuotationVendorDetail())
			{
				WoQuotationNegotiationDetail woQuotationNegotiationDetail=new WoQuotationNegotiationDetail(new WoQuotationVendorDetail(item.getId()));
//				sessionService.setEntityDefaults(woQuotationNegotiationDetail);
				woQuotationNegotiationDetail.setCreateDefaults(workOrderQuotationReq.getOrganizationId(),workOrderQuotationReq.getUnitId(),
						workOrderQuotationReq.getUserId());
				 workOrderNego.addWoQuotationNegotiationDetailList(woQuotationNegotiationDetail);
				
				
			}
			
		workOrderNegotiationDao.saveWoQuotationNegotiation(workOrderNego);				
		response.setId(workOrderNego.getId().longValue());
		}
		response.setMessage("Work Order Negotiation saved successfully");
		return response;
	}

	
	
	@Override
	@Transactional
	public Object cancelWOQuotationRequest(WorkOrderQR workOrderQuotationReq) {

		WorkOrderQuotationRequest workOrderQR = workOrderQRDao.getWorkOrderQuotationRequest(workOrderQuotationReq.getId());
		
		for(WorkOrderQuotationRequestDetail detail : workOrderQR.getWorkOrderQRDetailsList()) {
			detail.setStatus(new Status(workOrderQuotationReq.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQR.getUnitId()));
			detail.setUpdatedByUser(workOrderQuotationReq.getUserId());
		}
		workOrderQR.setCancelReason(new CancelReason(workOrderQuotationReq.getCancelReasonId()));
		workOrderQR.setCancelNote(workOrderQuotationReq.getCancelNote());

		workOrderQR.setStatus(new Status(workOrderQuotationReq.getStatusId()));
		workOrderQR.setApprovalStatus(new Status(workOrderQuotationReq.getApprovalStatusId()));

		workOrderQR.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQR.getUnitId()));
		workOrderQR.setUpdatedByUser(workOrderQuotationReq.getUserId());

		workOrderQRDao.updateWorkOrderQuotationRequest(workOrderQR);

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order cancelled successfully");

		return response;
	}	
	
	@Override
	@Transactional
	public Object getWorkOrderPopSearchList(WorkOrderQR workOrderQuotationReq) {
		workOrderQuotationReq.setToDate((new DateTime(workOrderQuotationReq.getToDate())).plusDays(1).toDate());

		List<WorkOrderResponseDto> responseList = new ArrayList<WorkOrderResponseDto>();
		
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
		statusIds.add(com.param.common.constants.Status.PendingApproval.getStatus());
		
		for(com.param.entity.model.procurement.WorkOrderRequest workOrder : workOrderQRDao.getWorkOrderList(workOrderQuotationReq,statusIds)) {

			WorkOrderResponseDto response = new WorkOrderResponseDto(workOrder.getId(), workOrder.getRequestNo(), workOrder.getAddedDate(), workOrder.getRemark(), workOrder.getAddedBy().getUserName(), 
					workOrder.getAddedDate(), workOrder.getStore().getStoreId(), workOrder.getStore().getStoreName(), workOrder.getContractType().getCode()); 	
			
			for(com.param.entity.model.procurement.WorkOrderRequestDetail detail : workOrder.getWorkOrderDetailList()) {
				if(detail.getIsDeleted()!=true && detail.getIsActive()!=false) {
					
					response.addItems(new WorkOrderDetailResponseDto(detail.getId(), detail.getItem().getId(), 
							detail.getItem().getCode(), detail.getItem().getName(), detail.getApprovedQuantity(),
							detail.getCurrentVendor()!=null ? detail.getCurrentVendor().getId() : null ,detail.getCurrentVendor()!=null ? detail.getCurrentVendor().getName() : null ,
							detail.getBatch()!=null ? detail.getBatch().getId() : null ,detail.getBatch()!=null ? detail.getBatch().getBatchNo() : null));  
					
					}
				}
		responseList.add(response);
		}
		return responseList;
	}
	
	@Override
	@Transactional
	public Object getWorkOrderDetails(List<WorkOrderQR> workOrderQuotationReqList,Integer unitId) {
		
		List<WorkOrderResponseDto> responseList = new ArrayList<WorkOrderResponseDto>();
		for (WorkOrderQR workOrderQuotationReq : workOrderQuotationReqList) {

			com.param.entity.model.procurement.WorkOrderRequest workOrder = workOrderQRDao.getWorkOrder(workOrderQuotationReq.getId(),unitId);
			WorkOrderResponseDto response = new WorkOrderResponseDto(workOrder.getId(), workOrder.getRequestNo(), workOrder.getAddedDate(), workOrder.getRemark(), workOrder.getAddedBy().getUserName(), 
					workOrder.getAddedDate(), workOrder.getStore().getStoreId(), workOrder.getStore().getStoreName(), workOrder.getContractType().getCode()); 	
			
			for (Integer itemId : workOrderQuotationReq.getWoIds()) {
			for(com.param.entity.model.procurement.WorkOrderRequestDetail detail : workOrder.getWorkOrderDetailList()) {
				if(itemId.equals(detail.getId()) && detail.getIsDeleted()!=true && detail.getIsActive()!=false) {
					
					response.addItems(new WorkOrderDetailResponseDto(detail.getId(), detail.getItem().getId(), 
							detail.getItem().getCode(), detail.getItem().getName(), detail.getApprovedQuantity(),
							detail.getCurrentVendor()!=null ? detail.getCurrentVendor().getId() : null ,detail.getCurrentVendor()!=null ? detail.getCurrentVendor().getName() : null ,
							detail.getBatch()!=null ? detail.getBatch().getId() : null ,detail.getBatch()!=null ? detail.getBatch().getBatchNo() : null));  
					
					}
				}
			}
			responseList.add(response);
		}
		return responseList;
	}
}
