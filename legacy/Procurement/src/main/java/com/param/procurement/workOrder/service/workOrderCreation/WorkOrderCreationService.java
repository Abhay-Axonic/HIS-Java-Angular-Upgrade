package com.param.procurement.workOrder.service.workOrderCreation;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
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
import com.param.global.service.global.IGlobalService;
import com.param.procurement.workOrder.dao.workOrderCreation.IWorkOrderCreationDao;
import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationDetailRequest;
import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationDetailResponse;
import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationDropDown;
import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationRequest;
import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;


@Service
public class WorkOrderCreationService implements IWorkOrderCreationService{
	
	private Integer screenId = Screen.WorkorderQuotationCreation.getScreen();

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;
	
	@Autowired
	IWorkOrderCreationDao workOrderCreationDao;

	@Autowired
	private SessionService sessionService;
	
	@Override
	@Transactional
	public WorkOrderCreationDropDown getDropdowns() {
		WorkOrderCreationDropDown workOrderCreationDropDown = new WorkOrderCreationDropDown();

		for (Store store : GlobalDao.getUserStoreList(null,null,null)) {
			workOrderCreationDropDown.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			workOrderCreationDropDown.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}
		
		for (ContractType contractType : GlobalDao.getContractType()) {
			workOrderCreationDropDown.addContractType(new com.param.global.dto.global.ContractType(contractType.getId(), 
					contractType.getCode()));
			
		}
		
		for (RequestType requestType : GlobalDao.getRequestType()) {
			if(com.param.common.constants.RequestType.AgainstWorkOrderQuotationRequest.getRequestType().equals(12) || 
					com.param.common.constants.RequestType.AgainstWorkOrderRequest.getRequestType().equals(13)) {
			workOrderCreationDropDown.addRequestType(new com.param.global.dto.global.RequestType(requestType.getId(), 
					requestType.getCode()));
			}
			
		}

		return workOrderCreationDropDown;
	}

	@Override
	@Transactional
	public Object getWorkOrderCreationList(WorkOrderCreationRequest woCreationRequest) {
		woCreationRequest.setToDate((new DateTime(woCreationRequest.getToDate())).plusDays(1).toDate());

		List<WorkOrderCreationResponse> responseList = new ArrayList<WorkOrderCreationResponse>();

		for (WorkOrderCreation  workOrderCreation : workOrderCreationDao.searchWorkOrderCreationList(woCreationRequest, screenId)) {
			
			WorkOrderCreationResponse response = new WorkOrderCreationResponse(workOrderCreation.getId(), workOrderCreation.getApprovalStatus().getId(),

					workOrderCreation.getNetAmount(), workOrderCreation.getRemark(), workOrderCreation.getContractType().getId(), workOrderCreation.getRequestType().getId(), 
					workOrderCreation.getStatus().getId(), workOrderCreation.getStore().getStoreId(), workOrderCreation.getTotalAmount(), workOrderCreation.getTotalDiscount(), workOrderCreation.getTotalTax(),
					workOrderCreation.getVendor().getId(), 
					workOrderCreation.getWorkOrder()!=null ? workOrderCreation.getWorkOrder().getId() : null, 
					workOrderCreation.getWorkOrder()!=null ? workOrderCreation.getWorkOrder().getRequestNo() : workOrderCreation.getWorkOrderQuotation().getWorkOrderQRNo(),
					workOrderCreation.getWorkOrderQuotation()!=null ? workOrderCreation.getWorkOrderQuotation().getId() : null, 
					workOrderCreation.getApprovedBy()!=null ?workOrderCreation.getApprovedBy().getUserName() : null, 
					workOrderCreation.getApprovedBy()!=null ? workOrderCreation.getAddedDate() : null, 
					workOrderCreation.getApprovalStatus().getStatus(), workOrderCreation.getContractType().getCode(), 
					workOrderCreation.getRequestType().getCode(),
					workOrderCreation.getStatus().getStatus(), workOrderCreation.getStore().getStoreName(), workOrderCreation.getVendor().getName(),
					workOrderCreation.getWorkOrderNo(),workOrderCreation.getAddedDate(),workOrderCreation.getAddedBy().getUserName());

			responseList.add(response);

		}
		return responseList;
	}

	@Override
	@Transactional
	public Object saveWorkOrderCreation(WorkOrderCreationRequest woCreationRequest) {

		/*Random rand = new Random();
		int num = rand.nextInt(1000);
		String workOrderQRNo = "WOCR-" + num;
		*/
		/*Integer storeId = woCreationRequest.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String workOrdeRCreationNo = GlobalService.getBillNoByGeneratedCode(woCreationRequest.getStoreId(), sessionService.getUnitId(), screenId);
		WorkOrderCreation workOrderCreation = new WorkOrderCreation(new Status(woCreationRequest.getApprovalStatusId()), woCreationRequest.getNetAmount(), woCreationRequest.getRemark(), new RequestType(woCreationRequest.getRequestTypeId()), 
				new Status(woCreationRequest.getStatusId()), new Store(woCreationRequest.getStoreId()), new ContractType(woCreationRequest.getContractTypeId()), 
				woCreationRequest.getTotalAmount(), woCreationRequest.getTotalDiscount(), woCreationRequest.getTotalTax(), 
				new Vendor(woCreationRequest.getVendorId()), new WorkOrderRequest(woCreationRequest.getWorkOrderRequestId()), workOrdeRCreationNo, new WorkOrderQuotationRequest(woCreationRequest.getWorkOrderQuotationId()));
		
		for(WorkOrderCreationDetailRequest item : woCreationRequest.getItems()) 
		{
			//WorkOrderCreationDetail
			WorkOrderCreationDetail workOrderCreationDetail =new WorkOrderCreationDetail(item.getAmount(), item.getDiscountAmount(), 
					item.getDiscountPercentage(),
					item.getExclusionDetails(), item.getInclusionDetails(), new Item(item.getItemId()), item.getNetAmount(), 
					item.getPaymentTerm(), item.getQoBonusQuantity(), item.getRemark(), item.getSpecification(), new Status(item.getStatusId()), 
					item.getTaxAmount(), new Tax(item.getTaxId()), item.getTaxPercentage(), item.getTotalAmount(), item.getWoQuantity(),
					new WorkOrderRequestDetail(item.getWoRequestDetail()), new WorkOrderQuotationRequestDetail(item.getWorkOrderQuotationDetail()));
//			sessionService.setEntityDefaults(workOrderCreationDetail);
			workOrderCreationDetail.setCreateDefaults(item.getOrganizationId(),item.getUnitId(),
					item.getUserId());
			workOrderCreation.addWorkOrderCreationDetailList(workOrderCreationDetail);
			
		}
//		sessionService.setEntityDefaults(workOrderCreation);
		workOrderCreation.setCreateDefaults(woCreationRequest.getOrganizationId(),woCreationRequest.getUnitId(),
				woCreationRequest.getUserId());
		workOrderCreationDao.saveWorkOrderCreation(workOrderCreation);
		CommonResponse response = new CommonResponse();
		response.setId(workOrderCreation.getId().longValue());
		response.setMessage("Work Order Creation saved successfully");
		return response;

	}

	@Override
	@Transactional
	public Object updateWorkOrderCreation(WorkOrderCreationRequest woCreationRequest) {

		WorkOrderCreation workOrderCreation = workOrderCreationDao.getWorkOrderCreation(woCreationRequest.getId());
			
		workOrderCreation.updateWorkOrderCreation(woCreationRequest.getNetAmount()!=null ? woCreationRequest.getNetAmount() : workOrderCreation.getNetAmount(),
				woCreationRequest.getRemark()!=null ? woCreationRequest.getRemark() : workOrderCreation.getRemark(), 
				woCreationRequest.getTotalAmount()!=null ? woCreationRequest.getTotalAmount() : workOrderCreation.getTotalAmount(), 
				woCreationRequest.getTotalDiscount()!=null ? woCreationRequest.getTotalDiscount() : workOrderCreation.getTotalDiscount(), 
				woCreationRequest.getTotalTax()!=null ? woCreationRequest.getTotalTax() : workOrderCreation.getTotalTax());
		
		for(WorkOrderCreationDetail detail : workOrderCreation.getWorkOrderCreationDetail()) {
			Integer id = null;
		
			for(WorkOrderCreationDetailRequest item : woCreationRequest.getItems()) {
				if(item.getId() !=null && item.getId().equals(detail.getId())) {
					id = item.getId();
			
					detail.updateWorkOrderCreationDetail(item.getAmount(), item.getDiscountAmount(), item.getDiscountPercentage(), item.getExclusionDetails(), 
							item.getInclusionDetails(), item.getNetAmount(), item.getPaymentTerm(), 
							item.getQoBonusQuantity(), item.getRemark(), item.getSpecification(), item.getTaxAmount(), item.getTaxPercentage(), item.getTotalAmount(), item.getWoQuantity());
				}
			}
		if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderCreation.getUnitId()));
			detail.setUpdatedByUser(woCreationRequest.getUserId());

		}
		
		for(WorkOrderCreationDetailRequest item : woCreationRequest.getItems()) {
		      if (item.getId() == null) 
		      {
		    	  WorkOrderCreationDetail workOrderCreationDetail=  new WorkOrderCreationDetail(item.getAmount(), item.getDiscountAmount(), 
							item.getDiscountPercentage(),
							item.getExclusionDetails(), item.getInclusionDetails(), new Item(item.getItemId()), item.getNetAmount(), 
							item.getPaymentTerm(), item.getQoBonusQuantity(), item.getRemark(), item.getSpecification(), new Status(item.getStatusId()), 
							item.getTaxAmount(), new Tax(item.getTaxId()), item.getTaxPercentage(), item.getTotalAmount(), item.getWoQuantity(),
							new WorkOrderRequestDetail(item.getWoRequestDetail()), new WorkOrderQuotationRequestDetail(item.getWorkOrderQuotationDetail()));
//		    	  sessionService.setEntityDefaults(workOrderCreationDetail);	
		    	  workOrderCreationDetail.setCreateDefaults(woCreationRequest.getOrganizationId(),woCreationRequest.getUnitId(),
		    			  woCreationRequest.getUserId());
		    	  workOrderCreation.addWorkOrderCreationDetailList(workOrderCreationDetail);
					
			}	 
	  }
		workOrderCreation.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderCreation.getUnitId()));
		workOrderCreation.setUpdatedByUser(woCreationRequest.getUserId());

		workOrderCreationDao.updateWorkOrderCreation(workOrderCreation);

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order Creation updated successfully");

		return response;
	}
		
		

	@Override
	@Transactional
	public Object getworkOrderCreation(Integer id) {

		WorkOrderCreation workOrderCreation = workOrderCreationDao.getWorkOrderCreation(id);
		
		WorkOrderCreationResponse response = new WorkOrderCreationResponse(workOrderCreation.getId(), workOrderCreation.getApprovalStatus().getId(),
				workOrderCreation.getNetAmount(), workOrderCreation.getRemark(), workOrderCreation.getContractType().getId(), workOrderCreation.getRequestType().getId(), 
				workOrderCreation.getStatus().getId(), workOrderCreation.getStore().getStoreId(), workOrderCreation.getTotalAmount(), workOrderCreation.getTotalDiscount(), workOrderCreation.getTotalTax(),
				workOrderCreation.getVendor().getId(), 
				workOrderCreation.getWorkOrder()!=null ? workOrderCreation.getWorkOrder().getId() : null, 
				workOrderCreation.getWorkOrder()!=null ? workOrderCreation.getWorkOrder().getRequestNo() : workOrderCreation.getWorkOrderQuotation().getWorkOrderQRNo(),
				workOrderCreation.getWorkOrderQuotation()!=null ? workOrderCreation.getWorkOrderQuotation().getId() : null, 
				workOrderCreation.getApprovedBy()!=null ?workOrderCreation.getApprovedBy().getLoginName() : null, 
				workOrderCreation.getApprovedBy()!=null ? workOrderCreation.getAddedDate() : null, 
				workOrderCreation.getApprovalStatus().getStatus(), workOrderCreation.getContractType().getCode(), 
				workOrderCreation.getRequestType().getCode(),
				workOrderCreation.getStatus().getStatus(), workOrderCreation.getStore().getStoreName(), workOrderCreation.getVendor().getName(),
				workOrderCreation.getWorkOrderNo(),workOrderCreation.getAddedDate(),workOrderCreation.getAddedBy().getLoginName());
		
		for(WorkOrderCreationDetail detail : workOrderCreation.getWorkOrderCreationDetail()) {
			if(detail.getIsDeleted()!=true && detail.getIsActive()!=false) {
				
				response.addItems(new WorkOrderCreationDetailResponse(detail.getId(), detail.getAmount(), detail.getDiscountAmount(), 
						detail.getDiscountPercentage(), detail.getExclusionDetails(), detail.getInclusionDetails(), detail.getItem().getId(), 
						detail.getNetAmount(), detail.getPaymentTerm(), detail.getQoBonusQuantity(), detail.getRemark(), detail.getSpecification(),
						detail.getStatus().getId(), 
						detail.getTaxAmount(), detail.getTax().getId(), detail.getTaxPercentage(), 
						detail.getTotalAmount(), detail.getWoQuantity(), detail.getItem().getName(), detail.getItem().getCode(), 
						detail.getStatus().getStatus(), detail.getTax().getName()));
						
						
						
						
				}
			}
		
		return response;				
	}

	
	@Override
	@Transactional
	public Object approveWorkOrderCreation(WorkOrderCreationRequest woCreationRequest) {

		for (Integer id : woCreationRequest.getWoCreationIds()) {
			WorkOrderCreation workOrderCreation = workOrderCreationDao.getWorkOrderCreation(id);

			workOrderCreation.setStatus(new Status(woCreationRequest.getStatusId()));
			workOrderCreation.setApprovalStatus(new Status(woCreationRequest.getApprovalStatusId()));

			for(WorkOrderCreationDetail detail : workOrderCreation.getWorkOrderCreationDetail()) {
				detail.setStatus(new Status(woCreationRequest.getStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderCreation.getUnitId()));
				detail.setUpdatedByUser(woCreationRequest.getUserId());
			}

			workOrderCreation.setApprovedOn(TimeZoneComponent.getDateByzone(workOrderCreation.getUnitId()));
			workOrderCreation.setApprovedBy(new User(sessionService.getUserId()));

			workOrderCreation.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderCreation.getUnitId()));
			workOrderCreation.setUpdatedByUser(woCreationRequest.getUserId());

			workOrderCreationDao.updateWorkOrderCreation(workOrderCreation);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelWorkOrderCreation(WorkOrderCreationRequest woCreationRequest) {

		WorkOrderCreation workOrderCreation = workOrderCreationDao.getWorkOrderCreation(woCreationRequest.getId());
		
		for(WorkOrderCreationDetail detail : workOrderCreation.getWorkOrderCreationDetail()) {
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

		workOrderCreationDao.updateWorkOrderCreation(workOrderCreation);

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order Creation cancelled successfully");

		return response;
	}	

}
