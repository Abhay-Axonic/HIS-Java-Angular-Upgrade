package com.param.procurement.workOrder.service.workOrderQuotationEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
import com.param.entity.model.procurement.ContractType;
import com.param.entity.model.procurement.WoQuotationEntry;
import com.param.entity.model.procurement.WoQuotationEntryDetail;
import com.param.entity.model.procurement.WoQuotationVendor;
import com.param.entity.model.procurement.WoQuotationVendorDetail;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.global.dao.global.IGlobalDao;
import com.param.procurement.workOrder.dao.workOrderQuotationEntry.IWorkOrderQEntryDao;
import com.param.procurement.workOrder.dto.workOrderQuotationEntry.WoQuoatationEntryRequest;
import com.param.procurement.workOrder.dto.workOrderQuotationEntry.WoQuotationEntryDetailRequest;
import com.param.procurement.workOrder.dto.workOrderQuotationEntry.WoQuotationEntryDetailResponse;
import com.param.procurement.workOrder.dto.workOrderQuotationEntry.WoQuotationEntryResponse;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQRDto;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVDetailResponse;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVRequest;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVResponse;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class WorkOrderQEntryService implements IWorkOrderQEntryService{
	
	@Autowired
	private SessionService sessionService;
	
	private Integer screenId = Screen.WorkorderQuotationEntry.getScreen();

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IWorkOrderQEntryDao workOrderQEntryDao;
	
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
	public Object getWoQuotationEntryList(WoQuoatationEntryRequest woQEntryRequest) {
		woQEntryRequest.setToDate((new DateTime(woQEntryRequest.getToDate())).plusDays(1).toDate());

		List<WoQuotationEntryResponse> responseList = new ArrayList<WoQuotationEntryResponse>();

		for (WoQuotationEntry  workOrderQEntry : workOrderQEntryDao.searchWorkOrderQEntryList(woQEntryRequest, screenId)) {
			
			 
			WoQuotationEntryResponse response = new WoQuotationEntryResponse(workOrderQEntry.getId(), workOrderQEntry.getVendorQuotation().getWorkOrderQR().getStore().getStoreId(), 
					workOrderQEntry.getVendorQuotation().getVendor().getId(), workOrderQEntry.getApprovalStatus().getId(), workOrderQEntry.getReferenceDate(), workOrderQEntry.getReferenceNo(), 
					workOrderQEntry.getRemark(), workOrderQEntry.getStatus().getId(), workOrderQEntry.getVendorQuotation().getId(), workOrderQEntry.getVendorQuotation().getWorkOrderQR().getRequestType().getId(), 
					workOrderQEntry.getVendorQuotation().getWorkOrderQR().getWorkOrderQRNo(), workOrderQEntry.getVendorQuotation().getWorkOrderQR().getAddedDate(),
					workOrderQEntry.getVendorQuotation().getWorkOrderQR().getWorkOrderQRNo()!=null ? workOrderQEntry.getVendorQuotation().getWorkOrderQR().getWorkOrderQRNo() :null ,
		            workOrderQEntry.getVendorQuotation().getWorkOrderQR().getAddedDate()!=null ? workOrderQEntry.getVendorQuotation().getWorkOrderQR().getAddedDate() :null,
		            workOrderQEntry.getVendorQuotation().getWorkOrderQR().getStore().getStoreName(), 
					workOrderQEntry.getVendorQuotation().getVendor().getName(), workOrderQEntry.getVendorQuotation().getWorkOrderQR().getContractType().getCode(), 
					workOrderQEntry.getVendorQuotation().getWorkOrderQR().getContractType().getId(), workOrderQEntry.getVendorQuotation().getWorkOrderQR().getRequestType().getType(), 
					workOrderQEntry.getAddedBy().getUserName(), workOrderQEntry.getStatus().getStatus(), workOrderQEntry.getApprovalStatus().getStatus(),
					woQEntryRequest.getFromDate(),woQEntryRequest.getToDate());

			responseList.add(response);

		}
		return responseList;
	}

	@Override
	@Transactional
	public Object saveWoQuotationEntry(WoQuoatationEntryRequest woQEntryRequest) {

		Random rand = new Random();
		int num = rand.nextInt(1000);
		String workOrderQVNo = "WQV-" + num;
		
		WoQuotationEntry  workOrderQEntry = new WoQuotationEntry(woQEntryRequest.getReferenceDate(), woQEntryRequest.getReferenceNo(), woQEntryRequest.getRemark(),
				new Status(com.param.common.constants.Status.New.getStatus()), new Status(woQEntryRequest.getApprovalStatusId()),
				new WoQuotationVendor(woQEntryRequest.getVendorQuotationId()));
		
		for(WoQuotationEntryDetailRequest item : woQEntryRequest.getItems()) 
		{
			WoQuotationEntryDetail woQuotationEntryDetail =new WoQuotationEntryDetail(item.getVendorCop(),item.getExpectedExclusion(), item.getExpectedInclusion(), item.getPaymentTerm(), item.getRemark(),
					new Status(item.getStatusId()), new WoQuotationVendorDetail(item.getVendorQuotationDetailId()));
//			sessionService.setEntityDefaults(woQuotationEntryDetail);
			woQuotationEntryDetail.setCreateDefaults(woQEntryRequest.getOrganizationId(),woQEntryRequest.getUnitId(),
					woQEntryRequest.getUserId());
			workOrderQEntry.addWoQuotationEntryDetailList(woQuotationEntryDetail);
		}
//		sessionService.setEntityDefaults(workOrderQEntry);
		workOrderQEntry.setCreateDefaults(woQEntryRequest.getOrganizationId(),woQEntryRequest.getUnitId(),
				woQEntryRequest.getUserId());
		workOrderQEntryDao.saveWoQuotationEntry(workOrderQEntry);
		CommonResponse response = new CommonResponse();
		response.setId(workOrderQEntry.getId().longValue());
		response.setMessage("Work Order Request saved successfully");
		return response;

	}

	@Override
	@Transactional
	public Object updateWoQuotationEntry(WoQuoatationEntryRequest woQEntryRequest) {

		WoQuotationEntry  workOrderQEntry = workOrderQEntryDao.getWoQuotationEntry(woQEntryRequest.getId());	
		
		workOrderQEntry.updateWoQuotationEntry(woQEntryRequest.getRemark()!=null ? woQEntryRequest.getRemark() : workOrderQEntry.getRemark());
		
		for(WoQuotationEntryDetail detail : workOrderQEntry.getWoQuotationEntryDetailList()) {
			
			Integer id = null;
		
			for(WoQuotationEntryDetailRequest item : woQEntryRequest.getItems()) {
				if(item.getId() !=null && item.getId().equals(detail.getId())) {
					id = item.getId();
			
					detail.updateWoQuotationEntryDetail(
							item.getVendorCop()!=null ? item.getVendorCop() : detail.getVendorCop(),
							item.getExpectedExclusion()!=null ? item.getExpectedExclusion() : detail.getExpectedExclusion(),
							item.getExpectedInclusion()!=null ? item.getExpectedInclusion() : detail.getExpectedInclusion(), 
							item.getPaymentTerm()!=null ? item.getPaymentTerm() : detail.getPaymentTerm(),
							item.getRemark()!=null ? item.getRemark() : detail.getRemark());
		          }
			}
		if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQEntry.getUnitId()));
			detail.setUpdatedByUser(woQEntryRequest.getUserId());

		}
		
		for(WoQuotationEntryDetailRequest item : woQEntryRequest.getItems()) {
		      if (item.getId() == null) {
		    	  WoQuotationEntryDetail woQuotationEntryDetail= new WoQuotationEntryDetail(item.getVendorCop(),item.getExpectedExclusion(), item.getExpectedInclusion(), item.getPaymentTerm(), item.getRemark(),
							new Status(item.getStatusId()), new WoQuotationVendorDetail(item.getVendorQuotationDetailId()));
//		    	  sessionService.setEntityDefaults(woQuotationEntryDetail);
		    	  workOrderQEntry.setCreateDefaults(woQEntryRequest.getOrganizationId(),woQEntryRequest.getUnitId(),
		  				woQEntryRequest.getUserId());
		    	   workOrderQEntry.addWoQuotationEntryDetailList(woQuotationEntryDetail);
					
			}	 
	  }
		workOrderQEntry.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQEntry.getUnitId()));
		workOrderQEntry.setUpdatedByUser(woQEntryRequest.getUserId());

		workOrderQEntryDao.updateWoQuotationEntry(workOrderQEntry);

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order Request updated successfully");

		return response;
	}
		
		

	@Override
	@Transactional
	public Object getWoQuotationEntry(Integer id) {

WoQuotationEntry  workOrderQEntry = workOrderQEntryDao.getWoQuotationEntry(id);
		
		WoQuotationEntryResponse response = new WoQuotationEntryResponse(workOrderQEntry.getId(), workOrderQEntry.getVendorQuotation().getWorkOrderQR().getStore().getStoreId(), 
				workOrderQEntry.getVendorQuotation().getVendor().getId(), workOrderQEntry.getApprovalStatus().getId(), workOrderQEntry.getReferenceDate(), workOrderQEntry.getReferenceNo(), 
				workOrderQEntry.getRemark(), workOrderQEntry.getStatus().getId(), workOrderQEntry.getVendorQuotation().getId(), workOrderQEntry.getVendorQuotation().getWorkOrderQR().getRequestType().getId(), 
				workOrderQEntry.getVendorQuotation().getWorkOrderQR().getWorkOrderQRNo(), workOrderQEntry.getVendorQuotation().getWorkOrderQR().getAddedDate(),
				workOrderQEntry.getVendorQuotation().getQvNo(), workOrderQEntry.getVendorQuotation().getAddedDate(), workOrderQEntry.getVendorQuotation().getWorkOrderQR().getStore().getStoreName(), 
				workOrderQEntry.getVendorQuotation().getVendor().getName(), workOrderQEntry.getVendorQuotation().getWorkOrderQR().getContractType().getCode(), 
				workOrderQEntry.getVendorQuotation().getWorkOrderQR().getContractType().getId(), workOrderQEntry.getVendorQuotation().getWorkOrderQR().getRequestType().getType(), 
				workOrderQEntry.getAddedBy().getUserName(), workOrderQEntry.getStatus().getStatus(), workOrderQEntry.getApprovalStatus().getStatus());

		for(WoQuotationEntryDetail detail : workOrderQEntry.getWoQuotationEntryDetailList()) {
				if(detail.getIsDeleted()!=true && detail.getIsActive()!=false) {
					
					
					response.addItems(new WoQuotationEntryDetailResponse(detail.getId(), detail.getVendorCop()!=null ? detail.getVendorCop() : null,detail.getExpectedExclusion(), detail.getExpectedInclusion(), detail.getPaymentTerm(), detail.getRemark(), 
							detail.getStatus().getId(), detail.getVendorQuotationDetail().getId(), detail.getVendorQuotationDetail().getWorkOrderQRDetail().getItem().getId(), 
							detail.getVendorQuotationDetail().getWorkOrderQRDetail().getItem().getCode(), detail.getVendorQuotationDetail().getWorkOrderQRDetail().getItem().getName(), 
							detail.getVendorQuotationDetail().getWorkOrderQRDetail().getQuantity(), detail.getStatus().getStatus(), detail.getVendorQuotationDetail().getWorkOrderQRDetail().getSpecification(),
							detail.getVendorQuotationDetail().getWorkOrderQRDetail().getBatch()!=null ? detail.getVendorQuotationDetail().getWorkOrderQRDetail().getBatch().getBatchNo() : null));
				}
			}
		
		return response;				
	}
	
	/*private List<WorkOrderQRDto> getWoQuotationReq(WoQuotationVendorDetail detail){
		List<WorkOrderQRDto> requestDetails = new ArrayList<WorkOrderQRDto>();
		WorkOrderQuotationRequest workOrderQR = workOrderQuotationVendorDao.getWorkOrderQuotationRequest(detail.getId());
		WorkOrderQRDto qr = new WorkOrderQRDto(workOrderQR.getId(), workOrderQR.getWorkOrderQRNo(), workOrderQR.getAddedDate());
		requestDetails.add(qr);
		return requestDetails;
	}*/

	
	@Override
	@Transactional
	public Object approveWoQuotationEntry(WoQuoatationEntryRequest woQEntryRequest) {

		for (Integer id : woQEntryRequest.getqEntryIds()) {
			WoQuotationEntry  workOrderQEntry = workOrderQEntryDao.getWoQuotationEntry(id);

			workOrderQEntry.setStatus(new Status(woQEntryRequest.getStatusId()));
			workOrderQEntry.setApprovalStatus(new Status(woQEntryRequest.getApprovalStatusId()));

			for(WoQuotationEntryDetail detail : workOrderQEntry.getWoQuotationEntryDetailList()) {
				detail.setStatus(new Status(woQEntryRequest.getStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQEntry.getUnitId()));
				detail.setUpdatedByUser(woQEntryRequest.getUserId());
			}
			
			//update requestforVendorNegotiation flag = true
			WorkOrderQuotationRequest WorkOrderQR = workOrderQEntry.getVendorQuotation().getWorkOrderQR();
			WorkOrderQR.setRequestForNegotiation(true);
			workOrderQEntryDao.updateWoQuotationRequest(WorkOrderQR);
			
			workOrderQEntry.setApprovedDate(TimeZoneComponent.getDateByzone(workOrderQEntry.getUnitId()));
			workOrderQEntry.setApprovedBy(new User(woQEntryRequest.getUserId()));

			workOrderQEntry.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQEntry.getUnitId()));
			workOrderQEntry.setUpdatedByUser(woQEntryRequest.getUserId());

			workOrderQEntryDao.updateWoQuotationEntry(workOrderQEntry);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelWoQuotationEntry(WoQuoatationEntryRequest woQEntryRequest) {

		WoQuotationEntry  workOrderQEntry = workOrderQEntryDao.getWoQuotationEntry(woQEntryRequest.getId());
		
		for(WoQuotationEntryDetail detail : workOrderQEntry.getWoQuotationEntryDetailList()) {
			detail.setStatus(new Status(woQEntryRequest.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQEntry.getUnitId()));
			detail.setUpdatedByUser(woQEntryRequest.getUserId());
		}
		workOrderQEntry.setCancelReason(new CancelReason(woQEntryRequest.getCancelReasonId()));
		workOrderQEntry.setCancelNote(woQEntryRequest.getCancelNote());

		workOrderQEntry.setStatus(new Status(woQEntryRequest.getStatusId()));
		workOrderQEntry.setApprovalStatus(new Status(woQEntryRequest.getApprovalStatusId()));

		workOrderQEntry.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderQEntry.getUnitId()));
		workOrderQEntry.setUpdatedByUser(woQEntryRequest.getUserId());

		workOrderQEntryDao.updateWoQuotationEntry(workOrderQEntry);

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order cancelled successfully");

		return response;
	}
	/*..................................*/
	@Override
	@Transactional
	public Object getWorkOrderQVendorPopSearchList(WorkOrderQVRequest workOrderQuotationVendor) {
		workOrderQuotationVendor.setToDate((new DateTime(workOrderQuotationVendor.getToDate())).plusDays(1).toDate());

		List<WorkOrderQVResponse> responseList = new ArrayList<WorkOrderQVResponse>();
		try {
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
		statusIds.add(com.param.common.constants.Status.PendingApproval.getStatus());
		
		for(WoQuotationVendor workOrderQV : workOrderQEntryDao.getWoQuotationVendorSearchList(workOrderQuotationVendor,statusIds)) {
		
			
			WorkOrderQVResponse response = new WorkOrderQVResponse(workOrderQV.getId(), workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getWorkOrderQRNo() : null, 
					workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getAddedDate() : null, 
					workOrderQV.getQvNo(), workOrderQV.getAddedDate(), workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getStore().getStoreName() : null , 
					workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getStore().getStoreId() : null , workOrderQV.getVendor().getName(), workOrderQV.getVendor().getId(), 
					workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getContractType().getCode() : null , 
					workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getContractType().getId() : null ,
					workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getRequestType().getType() : null ,
					workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getRequestType().getId() : null,
					workOrderQV.getDueDate(), workOrderQV.getRemark(), workOrderQV.getStatus().getStatus(), workOrderQV.getStatus().getId(), 
					workOrderQV.getApprovalStatus().getStatus(), workOrderQV.getApprovalStatus().getId(), workOrderQV.getAddedBy().getUserName(), workOrderQV.getApprovedBy().getUserName());
			
			for(WoQuotationVendorDetail detail : workOrderQV.getWoQuotationVendorDetail()) {
				if(detail.getIsDeleted()!=true && detail.getIsActive()!=false) {
					
					response.addItems(new WorkOrderQVDetailResponse(detail.getId(), detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getItem().getId() : null, 
							detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getItem().getCode() : null, 
							detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getItem().getName() : null , 
							detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getQuantity()!=null? detail.getWorkOrderQRDetail().getQuantity(): 0 : 0, 
							detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getCurrentVendor()!=null ? detail.getWorkOrderQRDetail().getCurrentVendor().getId() : null : null,
							detail.getWorkOrderQRDetail()!=null ?detail.getWorkOrderQRDetail().getSpecification() : null, 
							detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getExpectedInclusion() : null, 
							detail.getRemark(),
							detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getWorkOrderRequestDetail()!=null ?detail.getWorkOrderQRDetail().getWorkOrderRequestDetail().getBatch().getBatchNo() : null : null, 
							detail.getWorkOrderQRDetail()!=null ? getWoQuotationReq(detail) : null));
					}
				}
		responseList.add(response);
		}
		}catch(Exception e) {
			throw e;
		}
		return responseList;
	}
	
	private List<WorkOrderQRDto> getWoQuotationReq(WoQuotationVendorDetail detail){
		/*List<WorkOrderQRDto> requestDetails = new ArrayList<WorkOrderQRDto>();
		if(detail.getWorkOrderQRDetail().getId()!=null) {
		WorkOrderQuotationRequest workOrderQR = workOrderQuotationVendorDao.getWorkOrderQuotationRequest(detail.getWorkOrderQRDetail().getId());
		if(workOrderQR!=null) {
			WorkOrderQRDto qr = new WorkOrderQRDto(workOrderQR.getId(), workOrderQR.getWorkOrderQRNo(), workOrderQR.getAddedDate());
			requestDetails.add(qr);
			//return requestDetails;
		}}
		else {
			return null;
		}
		//return null;
		return requestDetails;*/
		return null;
	}
	
	@Override
	@Transactional
	public Object getWoQuotationRequestDetails(List<WorkOrderQVRequest> woQuotationVendorList) {
		
		List<WorkOrderQVResponse> responseList = new ArrayList<WorkOrderQVResponse>();
		for (WorkOrderQVRequest woQVRequest : woQuotationVendorList) {

			WoQuotationVendor workOrderQV = workOrderQEntryDao.getWoQuotationVendor(woQVRequest.getId());
			
			WorkOrderQVResponse response = new WorkOrderQVResponse(workOrderQV.getId(), workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getWorkOrderQRNo() : null, 
					workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getAddedDate() : null, 
					workOrderQV.getQvNo(), workOrderQV.getAddedDate(), workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getStore().getStoreName() : null , 
					workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getStore().getStoreId() : null , workOrderQV.getVendor().getName(), 
							workOrderQV.getVendor().getId(), 
					workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getContractType().getCode() : null , 
					workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getContractType().getId() : null ,
					workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getRequestType().getType() : null ,
					workOrderQV.getWorkOrderQR()!=null ? workOrderQV.getWorkOrderQR().getRequestType().getId() : null,
					workOrderQV.getDueDate(), workOrderQV.getRemark(), workOrderQV.getStatus().getStatus(), workOrderQV.getStatus().getId(),
					workOrderQV.getApprovalStatus().getStatus(), workOrderQV.getApprovalStatus().getId());
			
			for(WoQuotationVendorDetail detail : workOrderQEntryDao.getWoQuotationVendorDetails(woQVRequest.getQvDetailIds())) {
				if(detail.getIsDeleted()!=true && detail.getIsActive()!=false) {
					
					response.addItems(new WorkOrderQVDetailResponse(detail.getId(), detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getItem().getId() : null, 
							detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getItem().getCode() : null, 
							detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getItem().getName() : null , 
							detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getQuantity() : 0, 
							detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getCurrentVendor()!=null ? detail.getWorkOrderQRDetail().getCurrentVendor().getId() : null : null, 
							detail.getWorkOrderQRDetail()!=null ?detail.getWorkOrderQRDetail().getSpecification() : null, 
							detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getExpectedInclusion() : null, 
							detail.getRemark(),
							detail.getWorkOrderQRDetail()!=null ? detail.getWorkOrderQRDetail().getWorkOrderRequestDetail()!=null ?detail.getWorkOrderQRDetail().getWorkOrderRequestDetail().getBatch().getBatchNo() : null : null, 
							detail.getWorkOrderQRDetail()!=null ? getWoQuotationReq(detail) : null));
					}
				}
			responseList.add(response);
		}
		return responseList;
	}


}
