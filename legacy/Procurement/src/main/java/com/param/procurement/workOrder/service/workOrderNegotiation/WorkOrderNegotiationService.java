package com.param.procurement.workOrder.service.workOrderNegotiation;

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
import com.param.entity.model.procurement.WoQuotationNegotiation;
import com.param.entity.model.procurement.WoQuotationNegotiationDetail;
import com.param.entity.model.procurement.WoQuotationNegotiationMapper;
import com.param.entity.model.procurement.WoQuotationSelection;
import com.param.entity.model.procurement.WoQuotationSelectionDetail;
import com.param.entity.model.procurement.WoQuotationSelectionVendorMapper;
import com.param.entity.model.procurement.WoQuotationVendor;
import com.param.entity.model.procurement.WoQuotationVendorDetail;
import com.param.entity.model.procurement.WorkOrderQuotationRequest;
import com.param.entity.model.procurement.WorkOrderQuotationRequestDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.procurement.workOrder.dao.workOrderNegotiation.IWorkOrderNegotiationDao;
import com.param.procurement.workOrder.dao.workOrderQuotationEntry.WorkOrderQEntryDao;
import com.param.procurement.workOrder.dto.workOrderNegotiation.WONegotiationDetailRequest;
import com.param.procurement.workOrder.dto.workOrderNegotiation.WONegotiationDetailResponse;
import com.param.procurement.workOrder.dto.workOrderNegotiation.WONegotiationRequest;
import com.param.procurement.workOrder.dto.workOrderNegotiation.WONegotiationResponse;
import com.param.procurement.workOrder.dto.workOrderNegotiation.WoNegotiationMapperRequest;
import com.param.procurement.workOrder.dto.workOrderQuotationEntry.WoQuotationEntryDetailResponse;
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderDropDown;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class WorkOrderNegotiationService implements IWorkOrderNegotiationService{
	
	private Integer screenId = Screen.WorkorderQuotationNegotiation.getScreen();

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IWorkOrderNegotiationDao workOrderNegotiationDao;
	
	@Autowired
	WorkOrderQEntryDao workOrderQEntryDao;
	

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
	public Object getWoQuotationNegotiationList(WONegotiationRequest woNegotiationRequest) {
		woNegotiationRequest.setToDate((new DateTime(woNegotiationRequest.getToDate())).plusDays(1).toDate());

		List<WONegotiationResponse> responseList = new ArrayList<WONegotiationResponse>();

		for (WoQuotationNegotiation  workOrderNego : workOrderNegotiationDao.searchWorkOrderNegotiationList(woNegotiationRequest, screenId)) {
			
			WONegotiationResponse response = new WONegotiationResponse(workOrderNego.getId(),workOrderNego.getStatus().getId(),workOrderNego.getRemark()!=null ? workOrderNego.getRemark() :workOrderNego.getRemark(),
					workOrderNego.getVendorQuotation().getWorkOrderQR().getWorkOrderQRNo(), workOrderNego.getVendorQuotation().getWorkOrderQR().getAddedDate(), 
					workOrderNego.getVendorQuotation().getQvNo(), workOrderNego.getVendorQuotation().getAddedDate(), workOrderNego.getVendorQuotation().getWorkOrderQR().getStore().getStoreName(), workOrderNego.getVendorQuotation().getVendor().getName(), 
					workOrderNego.getVendorQuotation().getWorkOrderQR().getContractType().getDescription(), workOrderNego.getVendorQuotation().getWorkOrderQR().getContractType().getId(), 
					workOrderNego.getVendorQuotation().getWorkOrderQR().getRequestType().getType(), workOrderNego.getAddedBy().getUserName(), workOrderNego.getStatus().getStatus());
										
			
			
			
			responseList.add(response);

		}
		return responseList;
	}

	@Override
	@Transactional
	public Object saveWoQuotationNegotiation(WONegotiationRequest woNegotiationRequest) {
		CommonResponse response = new CommonResponse();
		
		for(WoQuotationVendor woQuotationVendor : workOrderNegotiationDao.getWoQuotationVendor(woNegotiationRequest.getWoQuotationId())) {
						
			WoQuotationNegotiation  workOrderNego = new WoQuotationNegotiation(new Status(2), 
					new WoQuotationVendor(woQuotationVendor.getId()));
							
			for(WoQuotationVendorDetail item : woQuotationVendor.getWoQuotationVendorDetail())
			{
				WoQuotationNegotiationDetail woQuotationNegotiationDetail=	new WoQuotationNegotiationDetail(new WoQuotationVendorDetail(item.getId()));
//				sessionService.setEntityDefaults(woQuotationNegotiationDetail);
				
				WoQuotationNegotiationDetail woNegoDetail = workOrderNego.addWoQuotationNegotiationDetailList(woQuotationNegotiationDetail);
//				sessionService.setEntityDefaults(woNegoDetail);
				woNegoDetail.setCreateDefaults(woNegotiationRequest.getOrganizationId(),woNegotiationRequest.getUnitId(),
						woNegotiationRequest.getUserId());
				/*for(WoNegotiationMapperRequest mapper : item.getMapperList()) {
				woNegoDetail.addWoQuotationNegotiationMapperList(new WoQuotationNegotiationMapper(mapper.getNegotiationExclusion(), mapper.getNegotiationInclusion(), mapper.getNegotiationPaymentTerms(), mapper.getNegotiationRemark(), 
						mapper.getAccept(), mapper.getReject(), mapper.getOnHold(), mapper.getRejectNote(), mapper.getNegotiationAmount(), mapper.getNegotiationQuantity()));
				}*/
			}
//			sessionService.setEntityDefaults(workOrderNego);
			workOrderNego.setCreateDefaults(woNegotiationRequest.getOrganizationId(),woNegotiationRequest.getUnitId(),
					woNegotiationRequest.getUserId());
		workOrderNegotiationDao.saveWoQuotationNegotiation(workOrderNego);				
		response.setId(workOrderNego.getId().longValue());
		}
		response.setMessage("Work Order Negotiation saved successfully");
		return response;

	}

	@Override
	@Transactional
	public Object updateWoQuotationNegotiation(WONegotiationRequest woNegotiationRequest) {

		CommonResponse response = new CommonResponse();
		WoQuotationNegotiation  workOrderNego = workOrderNegotiationDao.getWoQuotationNegotiation(woNegotiationRequest.getId());
		Boolean onHold = true;
		//workOrderNego.updateWoQuotationNegotiation(woNegotiationRequest.getRemark()!=null ? woNegotiationRequest.getRemark() : workOrderNego.getRemark());
	///..............................
		
		if(woNegotiationRequest.getStatusId() != null)
		{
		workOrderNego.setStatus(new Status(woNegotiationRequest.getStatusId()));
		}
		for(WoQuotationNegotiationDetail detail : workOrderNego.getWoQuotationNegotiationDetailList()) {			
			Integer id = null;
			
			
			List<Integer> woQRList = new ArrayList<Integer>();
		
			for(WONegotiationDetailRequest item : woNegotiationRequest.getItems()) {
				if(item.getId() !=null && item.getId().equals(detail.getId())) {
					id = item.getId();
			
					/*detail.updateWoQuotationNegotiationDetail(item.getQuantity()!=null ? item.getQuantity() : detail.getQuantity(), 
							item.getRemark()!=null ? item.getRemark() : detail.getRemark());*/
					
					/*for(WoQuotationNegotiationMapper mapper : detail.getWoQuotationNegotiationMapperList()) {
						Integer mapperId = null;*/
						
						for(WoNegotiationMapperRequest mapper : item.getMapperList()) {
							
							//Send For Vendor Selection
							if(mapper.getAccept()== true) {
								woQRList.add(detail.getVendorQuotationDetail().getWorkOrderQRDetail().getId());
							}
							
							if(mapper.getOnHold()== true) {
								onHold = false;		
								response.setMessage("Vendor Negotiation save successfully");
							}
							WoQuotationNegotiationMapper woQuotationNegotiationMapper=new WoQuotationNegotiationMapper(mapper.getNegotiationExclusion(), mapper.getNegotiationInclusion(), mapper.getNegotiationPaymentTerms(), mapper.getNegotiationRemark(), 
									mapper.getAccept(), mapper.getReject(), mapper.getOnHold(), mapper.getRejectNote(), 
									mapper.getNegotiationAmount(), mapper.getNegotiationQuantity());
//							sessionService.setEntityDefaults(woQuotationNegotiationMapper);
							woQuotationNegotiationMapper.setCreateDefaults(woNegotiationRequest.getOrganizationId(),woNegotiationRequest.getUnitId(),
									woNegotiationRequest.getUserId());
							detail.addWoQuotationNegotiationMapperList(woQuotationNegotiationMapper);
							
							/*if(mapperDto.getId() !=null && mapperDto.getId().equals(mapper.getId())) {
								mapperId = mapperDto.getId();
								mapper.updateWoQuotationNegotiationMapper(mapperDto.getNegotiationExclusion()!=null ? mapperDto.getNegotiationExclusion() : mapper.getExclusionDetail(), 
										mapperDto.getNegotiationInclusion()!=null ? mapperDto.getNegotiationInclusion() : mapper.getInclusionDetail(), 
										mapperDto.getNegotiationPaymentTerms()!=null ? mapperDto.getNegotiationPaymentTerms() : mapper.getPaymentTerm(), 
										mapperDto.getNegotiationRemark()!=null ? mapperDto.getNegotiationRemark() : mapper.getRemark());
						}*/
					}
						
						
						// insert into Selection table
						/*if(onHold) {
							//save data into vendorNegotiation
							//saveVendorSelection(woQRList);
							response.setMessage("Send For Vendor Selection");
						}*/
						
						
				}
		      }
			//}		

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderNego.getUnitId()));
			detail.setUpdatedByUser(woNegotiationRequest.getUserId());

		}
		workOrderNego.setUpdatedDate(TimeZoneComponent.getDateByzone(workOrderNego.getUnitId()));
		workOrderNego.setUpdatedByUser(woNegotiationRequest.getUserId());

		workOrderNegotiationDao.updateWoQuotationNegotiation(workOrderNego);
		
     //   response.setMessage("Vendor Negotiation updated successfully");
		return response;
	}
	
	@Override
	@Transactional
	public Object saveVendorSelection(WONegotiationRequest woNegotiationRequest) {
		
		WoQuotationNegotiation  workOrderNego = workOrderNegotiationDao.getWoQuotationNegotiation(woNegotiationRequest.getId());
		
		WoQuotationSelection  woSelection = new WoQuotationSelection(new WorkOrderQuotationRequest(workOrderNego.getVendorQuotation().getWorkOrderQR().getId()), 
				new Status(2));
		for(WoQuotationNegotiationDetail detail : workOrderNego.getWoQuotationNegotiationDetailList())
		{
			Integer id =null ;
			for(WONegotiationDetailRequest item : woNegotiationRequest.getItems()) 
			{
				if(item.getId() !=null && item.getId().equals(detail.getId())) {
		
		//WorkOrderQuotationRequest  woQR = workOrderNegotiationDao.getWoWorkOrderQuotationRequest(woNegotiationRequest.getId());		
		//for(WorkOrderQuotationRequestDetail item : woQR.getWorkOrderQRDetailsList()) {
					WoQuotationSelectionDetail woQuotationSelectionDetail=new WoQuotationSelectionDetail(new WorkOrderQuotationRequestDetail(detail.getVendorQuotationDetail().getWorkOrderQRDetail().getId()),
							new Status(2));
//					sessionService.setEntityDefaults(woQuotationSelectionDetail);
					woQuotationSelectionDetail.setCreateDefaults(woNegotiationRequest.getOrganizationId(),woNegotiationRequest.getUnitId(),
							woNegotiationRequest.getUserId());
			WoQuotationSelectionDetail  woSelectionDetail = woSelection.addWoQuotationSelectionDetailList(woQuotationSelectionDetail);
			for(WoQuotationNegotiationMapper mapper : detail.getWoQuotationNegotiationMapperList()) 
			{
				if(mapper.getAccept() == true)
				{
					WoQuotationSelectionVendorMapper woQuotationSelectionVendorMapper=	new WoQuotationSelectionVendorMapper (mapper.getAccept(), 
							mapper.getQuantity(), new WoQuotationNegotiationMapper(mapper.getId()),mapper.getRejectNote());
//					sessionService.setEntityDefaults(woQuotationSelectionVendorMapper);
					woQuotationSelectionVendorMapper.setCreateDefaults(woNegotiationRequest.getOrganizationId(),woNegotiationRequest.getUnitId(),
							woNegotiationRequest.getUserId());
					woSelectionDetail.addWoQuotationSelectionVendorMapperList(woQuotationSelectionVendorMapper);
					
				}
			}
			woSelectionDetail.setCreateDefaults(woNegotiationRequest.getOrganizationId(),woNegotiationRequest.getUnitId(),
					woNegotiationRequest.getUserId());
//			sessionService.setEntityDefaults(woSelectionDetail);
		}
	}
}
//		sessionService.setEntityDefaults(woSelection);
		woSelection.setCreateDefaults(woNegotiationRequest.getOrganizationId(),woNegotiationRequest.getUnitId(),
				woNegotiationRequest.getUserId());
		workOrderNegotiationDao.saveWoQuotationSelection(woSelection);
		
		CommonResponse response = new CommonResponse();
		response.setMessage("Send For Vendor Selection");
		return response;
	}
		
		

	@Override
	@Transactional
	public Object getWoQuotationNegotiation(Integer id) {

		WoQuotationNegotiation  workOrderNego = workOrderNegotiationDao.getWoQuotationNegotiation(id);
		
		WONegotiationResponse response = new WONegotiationResponse(workOrderNego.getId(), workOrderNego.getStatus().getId(), 
				workOrderNego.getVendorQuotation().getWorkOrderQR().getWorkOrderQRNo(), workOrderNego.getVendorQuotation().getWorkOrderQR().getAddedDate(), 
				workOrderNego.getVendorQuotation().getQvNo(), workOrderNego.getVendorQuotation().getAddedDate(), 
				workOrderNego.getVendorQuotation().getWorkOrderQR().getStore().getStoreId(),
				workOrderNego.getVendorQuotation().getWorkOrderQR().getStore().getStoreName(), 
				workOrderNego.getVendorQuotation().getVendor().getName(), workOrderNego.getVendorQuotation().getWorkOrderQR().getContractType().getDescription(),
				workOrderNego.getVendorQuotation().getWorkOrderQR().getContractType().getId(), 
				workOrderNego.getVendorQuotation().getWorkOrderQR().getRequestType().getType(), workOrderNego.getAddedBy().getUserName(), workOrderNego.getStatus().getStatus(),
				"null",workOrderNego.getVendorQuotation().getWorkOrderQR().getAddedDate(),workOrderNego.getVendorQuotation().getVendor().getId());
		WoQuotationEntryDetailResponse woQuotationEntryVenAmt=null;
		for(WoQuotationNegotiationDetail detail : workOrderNego.getWoQuotationNegotiationDetailList()) {
			//if(detail.getIsDeleted()!=true && detail.getIsActive()!=false) {
			 woQuotationEntryVenAmt=workOrderQEntryDao.getWouotationNegoAmount(workOrderNego.getId(),workOrderNego.getVendorQuotation().getId(),detail.getVendorQuotationDetail().getWorkOrderQRDetail().getId());
			//System.out.println(woQuotationEntryVenAmt.getVendorcopalias());
				response.addItems(new WONegotiationDetailResponse(detail.getId(), 
						detail.getVendorQuotationDetail().getWorkOrderQRDetail().getItem().getId(), 
						detail.getVendorQuotationDetail().getWorkOrderQRDetail().getItem().getCode(), 
						detail.getVendorQuotationDetail().getWorkOrderQRDetail().getItem().getName(), 
						detail.getVendorQuotationDetail().getWorkOrderQRDetail().getQuantity(),
						detail.getVendorQuotationDetail().getWorkOrderQRDetail().getSpecification(),
						detail.getVendorQuotationDetail().getWorkOrderQRDetail().getBatch().getBatchNo(),
						detail.getVendorQuotationDetail().getWorkOrderQRDetail().getExpectedInclusion(),
						woQuotationEntryVenAmt != null ? woQuotationEntryVenAmt.getExpectedexclusionalias() : null,
						woQuotationEntryVenAmt != null ? woQuotationEntryVenAmt.getExpectedexclusionalias() : null,
						woQuotationEntryVenAmt != null ? woQuotationEntryVenAmt.getPaymenttermalias() : null,
						woQuotationEntryVenAmt != null ? woQuotationEntryVenAmt.getRemark() : null,
						woQuotationEntryVenAmt != null ? woQuotationEntryVenAmt.getVendorcopalias() : null,
						detail.getVendorQuotationDetail().getRemark(),						
						getWONegotiationMapperResponse(detail)));				
				
					}
		//	}
		////System.out.println(workOrderNego.getVendorQuotation().getWoQuotationEntry().get(0).getWoQuotationEntryDetailList().get(0).getVendorCop());
		return response;				
	}
	
	private List<WoNegotiationMapperRequest> getWONegotiationMapperResponse(WoQuotationNegotiationDetail detail){
		List<WoNegotiationMapperRequest> mapperList = new ArrayList<WoNegotiationMapperRequest>();
		for(WoQuotationNegotiationMapper mapper : detail.getWoQuotationNegotiationMapperList()) {
			if(mapper.getIsActive()!=false && mapper.getIsDeleted()!=true) {
				WoNegotiationMapperRequest mapperResponse = new WoNegotiationMapperRequest(mapper.getId(), mapper.getQuantity(), mapper.getAmount(), mapper.getInclusionDetail(),
						mapper.getExclusionDetail(), mapper.getPaymentTerm(), mapper.getRemark(), mapper.getAccept(), mapper.getReject(), mapper.getOnHold(), mapper.getRejectNote(),mapper.getAddedDate());
						
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
