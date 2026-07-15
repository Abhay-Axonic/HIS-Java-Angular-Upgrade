package com.param.procurement.workOrder.service.workOrderSelection;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.procurement.ContractType;
import com.param.entity.model.procurement.WoQuotationSelection;
import com.param.entity.model.procurement.WoQuotationSelectionDetail;
import com.param.entity.model.procurement.WoQuotationSelectionVendorMapper;
import com.param.global.dao.global.IGlobalDao;
import com.param.procurement.workOrder.dao.workOrderSelection.IWOSelectionDao;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;
import com.param.procurement.workOrder.dto.workOrderSelection.WOSelectionDetailRequest;
import com.param.procurement.workOrder.dto.workOrderSelection.WOSelectionDetailsResponse;
import com.param.procurement.workOrder.dto.workOrderSelection.WOSelectionMapperRequest;
import com.param.procurement.workOrder.dto.workOrderSelection.WOSelectionMapperResponse;
import com.param.procurement.workOrder.dto.workOrderSelection.WOSelectionRequest;
import com.param.procurement.workOrder.dto.workOrderSelection.WOSelectionResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class WOSelectionService implements IWOSelectionService{
	
	@Autowired
	private SessionService sessionService;
	
	private Integer screenId = Screen.WorkorderQuotationSelection.getScreen();

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IWOSelectionDao workOrderSelectionDao;
	
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
	public Object getWoQuotationSelectionList(WOSelectionRequest woSelectionRequest) {
		woSelectionRequest.setToDate((new DateTime(woSelectionRequest.getToDate())).plusDays(1).toDate());

		List<WOSelectionResponse> responseList = new ArrayList<WOSelectionResponse>();

		for (WoQuotationSelection  woSelection : workOrderSelectionDao.searchWoQuotationSelectionList(woSelectionRequest, screenId)) {
			
			WOSelectionResponse response = new WOSelectionResponse(woSelection.getId(), woSelection.getStatus().getId(), woSelection.getStatus().getStatus(), 
					woSelection.getQuotationRequest().getWorkOrderQRNo(), woSelection.getQuotationRequest().getAddedDate(), 
					woSelection.getQuotationRequest().getStore().getStoreName(), woSelection.getQuotationRequest().getContractType().getDescription(), 
					woSelection.getQuotationRequest().getRequestType().getType(), woSelection.getAddedBy().getUserName());					
			responseList.add(response);

		}
		return responseList;
	}

	/*@Override
	@Transactional
	public Object saveWoQuotationSelection(WOSelectionRequest woSelectionRequest) {
		WoQuotationSelection  woSelection = workOrderSelectionDao.getWoQuotationSelection(woSelectionRequest.getId());
		WoQuotationSelection  woSelection = new WoQuotationSelection(new WorkOrderQuotationRequest(woSelectionRequest.getQuotationRequestId()), 
				new Status(woSelectionRequest.getStatusId()));
		
		for(WOSelectionDetailRequest item : woSelectionRequest.getItems()) {
			
			WoQuotationSelectionDetail woSelectionDetail =  woSelection.addWoQuotationSelectionDetailList(new WoQuotationSelectionDetail(new WorkOrderQuotationRequestDetail(item.getQuotationRequestDetailId()),
					new Status(item.getStatusId())));
			
			for(WOSelectionMapperRequest mapper : item.getMapper()) {
				woSelectionDetail.addWoQuotationSelectionVendorMapperList(new WoQuotationSelectionVendorMapper(mapper.getIsAccepted(), 
				mapper.getQuantity(), new WoQuotationNegotiation(mapper.getQuotationNegotiationId()),mapper.getRejectNote()));
			}
		}
			
		workOrderSelectionDao.saveWoQuotationSelection(woSelection);
		CommonResponse response = new CommonResponse();
		response.setId(woSelection.getId().longValue());
		response.setMessage("Work Order Selection saved successfully");
		return response;

	}*/

	@Override
	@Transactional
	public Object updateWoQuotationSelection(WOSelectionRequest woSelectionRequest) {

		WoQuotationSelection  woSelection = workOrderSelectionDao.getWoQuotationSelection(woSelectionRequest.getId());	
		
		for(WoQuotationSelectionDetail detail : woSelection.getWoQuotationSelectionDetail()) {			
			Integer id = null;
		
			for(WOSelectionDetailRequest item : woSelectionRequest.getItems()) {
				if(item.getId() !=null && item.getId().equals(detail.getId())) {
					id = item.getId();
			
					for(WoQuotationSelectionVendorMapper map :detail.getWoQuotationSelectionVendorMappers())			
					for(WOSelectionMapperRequest mapper : item.getMapper()) {
						if(mapper.getId() !=null && mapper.getId().equals(map.getId())) {
							
							 map.updateWoQuotationSelectionVendorMapper(mapper.getIsAccepted(), mapper.getRejectNote(),mapper.getQuantity(),mapper.getRemark(),
	                                    mapper.getInclusionDetails(),mapper.getExclusionDetails(),mapper.getPaymentTerms());
						}
					/*.....*/	
						/*detail.addWoQuotationSelectionVendorMapperList(new WoQuotationSelectionVendorMapper(mapper.getIsAccepted(), 
						mapper.getQuantity(), new WoQuotationNegotiationMapper(mapper.getQuotationNegotiationId()),mapper.getRejectNote()));*/
						}
					}
			}
			
			if (id == null) {
				detail.setIsDeleted(true);
			}
			
		detail.setUpdatedDate(TimeZoneComponent.getDateByzone(woSelection.getUnitId()));
		detail.setUpdatedByUser(woSelectionRequest.getUserId());
	    }
		
		woSelection.getQuotationRequest().getWorkOrderRequest().setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
		woSelection.getQuotationRequest().getWorkOrderRequest().setUpdatedByUser(woSelectionRequest.getUserId());
		woSelection.getQuotationRequest().getWorkOrderRequest().setUpdatedDate(TimeZoneComponent.getDateByzone(woSelection.getUnitId()));
		
		woSelection.getQuotationRequest().setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
		woSelection.getQuotationRequest().setUpdatedByUser(woSelectionRequest.getUserId());
		woSelection.getQuotationRequest().setUpdatedDate(TimeZoneComponent.getDateByzone(woSelection.getUnitId()));
		
		woSelection.setUpdatedDate(TimeZoneComponent.getDateByzone(woSelection.getUnitId()));
		woSelection.setUpdatedByUser(woSelectionRequest.getUserId());

		workOrderSelectionDao.updateWoQuotationSelection(woSelection);

		CommonResponse response = new CommonResponse();
		response.setId(woSelection.getId().longValue());
		response.setMessage("Work Order Selection updated successfully");

		return response;
	}
		
		

	@Override
	@Transactional
	public Object getWoQuotationSelection(Integer id) {

		WoQuotationSelection  woSelection = workOrderSelectionDao.getWoQuotationSelection(id);
		
		WOSelectionResponse response = new WOSelectionResponse(woSelection.getId(), woSelection.getStatus().getId(), woSelection.getStatus().getStatus(), 
				woSelection.getQuotationRequest().getWorkOrderQRNo(), woSelection.getQuotationRequest().getAddedDate(), 
				woSelection.getQuotationRequest().getStore().getStoreName(), woSelection.getQuotationRequest().getContractType().getDescription(), 
				woSelection.getQuotationRequest().getRequestType().getType(), woSelection.getAddedBy().getUserName());
		
		for(WoQuotationSelectionDetail detail : woSelection.getWoQuotationSelectionDetail()) {
			
			if(detail.getIsDeleted()!=true && detail.getIsActive()!=false) {
				
				response.addItems(new WOSelectionDetailsResponse(detail.getId(), detail.getQuotationRequestDetail().getItem().getId(), 
						detail.getQuotationRequestDetail().getItem().getCode(), 
						detail.getQuotationRequestDetail().getItem().getName(),detail.getQuotationRequestDetail().getSpecification(),
						detail.getStatus().getId(), detail.getStatus().getStatus(), getWOSelectionMapperResponse(detail),
						detail.getQuotationRequestDetail().getBatch().getId(),detail.getQuotationRequestDetail().getBatch().getBatchNo(),
						detail.getQuotationRequestDetail().getWorkOrderQuotationRequest().getWoQuotationVendor().get(0).getWoQuotationEntry().get(0).getWoQuotationEntryDetailList().get(0).getExpectedExclusion(),
						detail.getQuotationRequestDetail().getWorkOrderQuotationRequest().getWoQuotationVendor().get(0).getWoQuotationEntry().get(0).getWoQuotationEntryDetailList().get(0).getExpectedInclusion(),
						detail.getQuotationRequestDetail().getWorkOrderQuotationRequest().getWoQuotationVendor().get(0).getWoQuotationEntry().get(0).getWoQuotationEntryDetailList().get(0).getPaymentTerm()));
						
						
					}
			}
		
		return response;				
	}
	
	private List<WOSelectionMapperResponse> getWOSelectionMapperResponse(WoQuotationSelectionDetail detail){
		List<WOSelectionMapperResponse> mapperList = new ArrayList<WOSelectionMapperResponse>();
		for(WoQuotationSelectionVendorMapper mapper : detail.getWoQuotationSelectionVendorMappers()) {
			if(mapper.getIsActive()!=false && mapper.getIsDeleted()!=true) {
				WOSelectionMapperResponse mapperResponse = new WOSelectionMapperResponse(mapper.getId(), mapper.getIsAccepted(), mapper.getQuantity(),
						mapper.getRejectNote(),mapper.getExclusionDetails(),mapper.getInclusionDetails(),mapper.getPaymentTerms(),mapper.getQuotationNegotiationMapper().getAmount(),
						 mapper.getQuotationNegotiationMapper().getExclusionDetail(),mapper.getQuotationNegotiationMapper().getInclusionDetail(),
						 mapper.getQuotationNegotiationMapper().getPaymentTerm(),
							mapper.getQuotationNegotiationMapper().getWoQuotationNegotiationDetail().getWoQuotationNegotiation().getVendorQuotation().getVendor().getName(),
							mapper.getQuotationNegotiationMapper().getWoQuotationNegotiationDetail().getWoQuotationNegotiation().getVendorQuotation().getQvNo());
				
				//WoQuotationNegotiationMapper negotiationMapper = mapper.getQuotationNegotiationMapper();
				//mapperResponse.addItems(new WONegotiationDetailResponse(negotiationMapper.getExclusionDetail(), negotiationMapper.getInclusionDetail(), negotiationMapper.getPaymentTerm()));
				//sessionService.setEntityDefaults(negotiationMapper);
			mapperList.add(mapperResponse);
			}
		}
		return mapperList;
	}

	
	/*@Override
	@Transactional
	public Object approveWoQuotationNegotiation(WONegotiationRequest woNegotiationRequest) {

		for (Integer id : woNegotiationRequest.getNegoIds()) {
			WoQuotationNegotiation  workOrderNego = workOrderNegotiationDao.getWoQuotationNegotiation(id);

			workOrderNego.setStatus(new Status(woNegotiationRequest.getStatusId()));
		//	workOrderNego.setApprovalStatus(new Status(woNegotiationRequest.getApprovalStatusId()));

			for(WoQuotationNegotiationDetail detail : workOrderNego.getWoQuotationNegotiationDetailList()) {
				detail.setStatus(new Status(woNegotiationRequest.getStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
				detail.setUpdatedBy(detail.getUser());
			}

			//workOrderNego.setApprovedDate(TimeZoneComponent.getDateByzone(unitId));
			//workOrderNego.setApprovedBy(new User(sessionService.getUserId()));

			workOrderNego.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
			workOrderNego.setUpdatedBy(workOrderNego.getUser());

			workOrderNegotiationDao.updateWoQuotationNegotiation(workOrderNego);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelWoQuotationNegotiation(WoQuoatationEntryRequest woQEntryRequest) {

		WoQuotationEntry  workOrderQEntry = workOrderQEntryDao.getWoQuotationEntry(woQEntryRequest.getId());
		
		for(WoQuotationEntryDetail detail : workOrderQEntry.getWoQuotationEntryDetailList()) {
			detail.setStatus(new Status(woQEntryRequest.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
			detail.setUpdatedBy(workOrderQEntry.getUser());
		}
		workOrderQEntry.setCancelReason(new CancelReason(woQEntryRequest.getCancelReasonId()));
		workOrderQEntry.setCancelNote(woQEntryRequest.getCancelNote());

		workOrderQEntry.setStatus(new Status(woQEntryRequest.getStatusId()));
		workOrderQEntry.setApprovalStatus(new Status(woQEntryRequest.getApprovalStatusId()));

		workOrderQEntry.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
		workOrderQEntry.setUpdatedBy(workOrderQEntry.getUser());

		workOrderQEntryDao.updateWoQuotationEntry(workOrderQEntry);

		CommonResponse response = new CommonResponse();
		response.setMessage("Work Order cancelled successfully");

		return response;
	}

*/

}
