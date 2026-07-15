package com.param.procurement.quotation.service.technicalApproval;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.QuotationFill;
import com.param.entity.model.procurement.QuotationFillDetail;
import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.TechnicalApproval;
import com.param.entity.model.procurement.TechnicalApprovalDetail;
import com.param.entity.model.procurement.TechnicalApprovalDetailMapper;
import com.param.entity.model.procurement.VendorNegotiation;
import com.param.entity.model.procurement.VendorNegotiationDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.procurement.quotation.dao.technicalApproval.ITechnicalApprovalDao;
import com.param.procurement.quotation.dto.quotationRequest.QuotationReqDropDownDto;
import com.param.procurement.quotation.dto.technicalApproval.TechApprovalMapperDetails;
import com.param.procurement.quotation.dto.technicalApproval.TechnicalApprovalDetailsResponse;
import com.param.procurement.quotation.dto.technicalApproval.TechnicalApprovalRequestDto;
import com.param.procurement.quotation.dto.technicalApproval.TechnicalApprovalResponse;
import com.param.procurement.quotation.dto.technicalApproval.TechnicalApprovalSearchResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class TechnicalApprovalService implements ITechnicalApprovalService{
	
	private Integer screenId = Screen.TechnicalApproval.getScreen();
	
	@Autowired
	private SessionService sessionService;

	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	ITechnicalApprovalDao technicalApprovalDao;
	
	@Override
	@Transactional
	public QuotationReqDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		QuotationReqDropDownDto quotationDropDownDto = new QuotationReqDropDownDto();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			quotationDropDownDto.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			quotationDropDownDto.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		return quotationDropDownDto;
	}

	@Override
	@Transactional
	public Object getTechnicalApprovalList(TechnicalApprovalRequestDto techApprovalRequestDto) {
		techApprovalRequestDto.setToDate((new DateTime(techApprovalRequestDto.getToDate())).plusDays(1).toDate());

		List<TechnicalApprovalSearchResponse> techApprovalSearchList = new ArrayList<TechnicalApprovalSearchResponse>();

		for (TechnicalApproval technicalApproval : technicalApprovalDao.searchTechnicalApprovalList(techApprovalRequestDto, screenId)) {
			
			TechnicalApprovalSearchResponse response = new TechnicalApprovalSearchResponse(technicalApproval.getId(), technicalApproval.getQuotationRequest().getAddedDate(), 
					technicalApproval.getQuotationRequest().getQrNo(), technicalApproval.getQuotationRequest().getStore().getStoreName(), technicalApproval.getQuotationRequest().getRequestType().getType(), technicalApproval.getAssetType().getType(),
					technicalApproval.getAddedBy().getUserName(), technicalApproval.getAddedDate(), technicalApproval.getStatus().getStatus());
			
			techApprovalSearchList.add(response);

		}
		return techApprovalSearchList;
	}
	
	@Override
	@Transactional
	public Object saveTechnicalApproval(TechnicalApprovalRequestDto techApprovalRequestDto) {
		QuotationRequest quotationRequest = technicalApprovalDao.getQuotationRequest(techApprovalRequestDto.getQrId());
		TechnicalApproval technicalApproval = new TechnicalApproval(new AssetType(quotationRequest.getAssetType().getId()), 
				new Status(2) , new Store(quotationRequest.getStore().getStoreId()), new QuotationRequest(quotationRequest.getId()), quotationRequest.getRemark());
		technicalApproval.setCreateDefaults(techApprovalRequestDto.getOrganizationId()!=null ? techApprovalRequestDto.getOrganizationId() : sessionService.getOrganizationId(),techApprovalRequestDto.getUnitId() !=null ? techApprovalRequestDto.getUnitId() :sessionService.getUnitId() ,techApprovalRequestDto.getUserId()!=null ? techApprovalRequestDto.getUserId() : sessionService.getUserId());
		List<QuotationRequestDetail> quotationFillDetailList = technicalApprovalDao.getQuotationRequestDetail(techApprovalRequestDto.getQrId());
		
		List<QuotationFill> quotationFillList = technicalApprovalDao.getQuotationFill(techApprovalRequestDto.getQrId());
	
		for(QuotationRequestDetail qfDetail : quotationFillDetailList)
		{
			TechnicalApprovalDetail technicalApprovalDetail=new TechnicalApprovalDetail(new QuotationRequestDetail(qfDetail.getId()), new Status(2));
			technicalApprovalDetail.setCreateDefaults(techApprovalRequestDto.getOrganizationId()!=null ? techApprovalRequestDto.getOrganizationId() : sessionService.getOrganizationId(),techApprovalRequestDto.getUnitId() !=null ? techApprovalRequestDto.getUnitId() :sessionService.getUnitId() ,techApprovalRequestDto.getUserId()!=null ? techApprovalRequestDto.getUserId() : sessionService.getUserId());
			
			TechnicalApprovalDetail techApprovalDetail  = technicalApproval.addTechnicalApprovalDetailList(technicalApprovalDetail);
			techApprovalDetail.setCreateDefaults(techApprovalRequestDto.getOrganizationId()!=null ? techApprovalRequestDto.getOrganizationId() : sessionService.getOrganizationId(),techApprovalRequestDto.getUnitId() !=null ? techApprovalRequestDto.getUnitId() :sessionService.getUnitId() ,techApprovalRequestDto.getUserId()!=null ? techApprovalRequestDto.getUserId() : sessionService.getUserId());
			
			for(QuotationFill quotationFill : quotationFillList)
			{
				TechnicalApprovalDetailMapper technicalApprovalDetailMapper=new TechnicalApprovalDetailMapper("remark", false, false, 
						false, false, new QuotationFill(quotationFill.getId()), new Status(2), new Vendor(quotationFill.getVendor().getId()));
				technicalApprovalDetailMapper.setCreateDefaults(techApprovalRequestDto.getOrganizationId()!=null ? techApprovalRequestDto.getOrganizationId() : sessionService.getOrganizationId(),techApprovalRequestDto.getUnitId() !=null ? techApprovalRequestDto.getUnitId() :sessionService.getUnitId() ,techApprovalRequestDto.getUserId()!=null ? techApprovalRequestDto.getUserId() : sessionService.getUserId());
				
				techApprovalDetail.addTechnicalApprovalDetailMapperList(technicalApprovalDetailMapper);
			}
			
		}	
		technicalApprovalDao.saveTechnicalApproval(technicalApproval);		
		quotationRequest.setSendTechnicalApproval(true);		
		technicalApprovalDao.updateQuotationRequest(quotationRequest);
		CommonResponse response = new CommonResponse();
		response.setId(technicalApproval.getId().longValue());
		response.setMessage("Technical Approval saved successfully");
		return response;
		
		
	}
	
	@Override
	@Transactional
	public Object updateTechnicalApproval(TechnicalApprovalRequestDto techApprovalRequestDto)
	{

		TechnicalApproval technicalApproval = technicalApprovalDao.getTechnicalApproval(techApprovalRequestDto.getId());
		
		technicalApproval.setStatus(new Status(techApprovalRequestDto.getStatusId()));
		CommonResponse response = new CommonResponse();
				
		List<Integer> qfIdList = new ArrayList<Integer>();
		
		for(TechnicalApprovalDetail detail : technicalApproval.getTechnicalApprovalDetailsList()) 
		{
			Integer id = null;
			Boolean onHold = true;
			
			for(TechnicalApprovalDetailsResponse item : techApprovalRequestDto.getItems()) 
			{
				if(item.getId() !=null && item.getId().equals(detail.getId()))
				{
					id = item.getId();
					detail.setStatus(new Status(techApprovalRequestDto.getStatusId()));
					for(TechnicalApprovalDetailMapper mapper : detail.getTechnicalApprovalDetailMapperList())
					{
						for(TechApprovalMapperDetails itemsMapper : item.getVendorDetails())
						{
							if(itemsMapper.getAccept().equals(true) && mapper.getId().equals(itemsMapper.getId()))
							{
								qfIdList.add(mapper.getQuotationFill().getId());
							}
							if(itemsMapper.getId()!=null && itemsMapper.getId().equals(mapper.getId()))
							{
							if(itemsMapper.getOnHold()== true)
							{
								onHold = false;		
								response.setMessage("Technical Approval updated successfully");
							}
							mapper.updateTechnicalApprovalDetailMapper(itemsMapper.getAccept()!=null ? itemsMapper.getAccept() : mapper.getAccept(), itemsMapper.getReject(), 
									itemsMapper.getRejectNote(), itemsMapper.getOnHold(), 
									itemsMapper.getRemark(), itemsMapper.getPaymentFlag(), itemsMapper.getSupportFlag(), itemsMapper.getTechnicalSpecFlag(), 
									itemsMapper.getWarrantyFlag(), itemsMapper.getPaymentRejectNote()!=null ? itemsMapper.getPaymentRejectNote() : mapper.getExpectedPaymentRejectNote() , 
									itemsMapper.getSupportRejectNote()!=null ? itemsMapper.getSupportRejectNote() : mapper.getSupportTermRejectNote(),
									itemsMapper.getTechnicalSpecRejectNote()!=null ? itemsMapper.getTechnicalSpecRejectNote() : mapper.getTechnicalSpecificationRejectNote(),
									itemsMapper.getWarrantyRejectNote()!=null ? itemsMapper.getWarrantyRejectNote() : mapper.getwarrantyPeriodRejectNote(),new Status(techApprovalRequestDto.getStatusId()));
						}
					   }
					}
					
					if(onHold)
					{
						//save data into vendorNegotiation
						saveVendorNegociation(qfIdList, techApprovalRequestDto);
						response.setMessage("Send For Vendor Negotiation");
					}
					
				}
			}
			
			
			
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(technicalApproval.getUnitId()));
			detail.setUpdatedByUser(techApprovalRequestDto.getUserId());
		}
		
		technicalApproval.setUpdatedDate(TimeZoneComponent.getDateByzone(technicalApproval.getUnitId()));
		technicalApproval.setUpdatedByUser(techApprovalRequestDto.getUserId());

		technicalApprovalDao.updateTechnicalApproval(technicalApproval);
		return response;
			
			}
	
	private void saveVendorNegociation(List<Integer> qfIdList, TechnicalApprovalRequestDto techApprovalRequestDto)
	{
		
		for(Integer qfId : qfIdList) {
		
		QuotationFill quotationFil = technicalApprovalDao.getQuotationFillById(qfId);
		
		VendorNegotiation vendorNegotiation = new VendorNegotiation(new AssetType(quotationFil.getAssetType().getId()), 
				new QuotationFill(quotationFil.getId()), new Status(2), new Store(quotationFil.getStore().getStoreId()), new Vendor(quotationFil.getVendor().getId()));
		vendorNegotiation.setCreateDefaults(techApprovalRequestDto.getOrganizationId()!=null ? techApprovalRequestDto.getOrganizationId() : sessionService.getOrganizationId(),techApprovalRequestDto.getUnitId() !=null ? techApprovalRequestDto.getUnitId() :sessionService.getUnitId() ,techApprovalRequestDto.getUserId()!=null ? techApprovalRequestDto.getUserId() : sessionService.getUserId());
		for(QuotationFillDetail detail : quotationFil.getQuotationFillDetailList())
		{
			VendorNegotiationDetail vendorNegotiationDetail=new VendorNegotiationDetail(new QuotationFillDetail(detail.getId()));
			vendorNegotiationDetail.setCreateDefaults(techApprovalRequestDto.getOrganizationId()!=null ? techApprovalRequestDto.getOrganizationId() : sessionService.getOrganizationId(),techApprovalRequestDto.getUnitId() !=null ? techApprovalRequestDto.getUnitId() :sessionService.getUnitId() ,techApprovalRequestDto.getUserId()!=null ? techApprovalRequestDto.getUserId() : sessionService.getUserId());
			
			vendorNegotiation.addVendorNegociationDetailList(vendorNegotiationDetail);
		}
		technicalApprovalDao.saveVendorNegotiation(vendorNegotiation);
		}
	}
	
	@Override
	@Transactional
	public Object getTechnicalApproval(Integer id) {

		TechnicalApproval technicalApproval = technicalApprovalDao.getTechnicalApproval(id);
		
		TechnicalApprovalResponse response = new TechnicalApprovalResponse(technicalApproval.getId(), technicalApproval.getQuotationRequest().getQrNo());
		
		for(TechnicalApprovalDetail detail : technicalApproval.getTechnicalApprovalDetailsList()) {
			if(detail.getIsDeleted()!= true && detail.getIsActive()!=false) {
			List<TechApprovalMapperDetails> vendorDetails = getvendorDetails(detail);
			
			response.addItems(new TechnicalApprovalDetailsResponse(detail.getId(), detail.getQuotationRequestDetail().getItem().getId(),
					detail.getQuotationRequestDetail().getItem().getName(), detail.getQuotationRequestDetail().getQuantity(), 
					detail.getQuotationRequestDetail().getWarrantyPeriod(), detail.getQuotationRequestDetail().getExpectedSupportTerm(),
					detail.getQuotationRequestDetail().getExpectedTechnicalSpecification(), detail.getQuotationRequestDetail().getExpectedPayment(),vendorDetails));
			}
		}
		
		return response;				
	}

	private List<TechApprovalMapperDetails> getvendorDetails(TechnicalApprovalDetail detail){
		
		List<TechApprovalMapperDetails> vendorsDetailslist = new ArrayList<TechApprovalMapperDetails>();
		for(TechnicalApprovalDetailMapper vendorDetails : detail.getTechnicalApprovalDetailMapperList()) {
		
			//add if condation for resolved HIS-9541 Quotation fill in cancelled still visible on the technical approval screen
			if (!vendorDetails.getQuotationFill().getApprovalStatus().getId().equals(com.param.common.constants.Status.Cancelled.getStatus())) {
			
			for(QuotationFillDetail qfDetail : vendorDetails.getQuotationFill().getQuotationFillDetailList()) {			
			
				if(detail.getQuotationRequestDetail().getId().equals(qfDetail.getQuotationRequestDetail().getId()) && vendorDetails.getVendor().getId().equals(vendorDetails.getQuotationFill().getVendor().getId())) {
					if(vendorDetails.getIsDeleted()!=true && vendorDetails.getIsActive()!=false) {
					TechApprovalMapperDetails mapper = new TechApprovalMapperDetails(vendorDetails.getId(),vendorDetails.getVendor().getId(), qfDetail.getQuotationFill().getId(), qfDetail.getVendorWarrantyPeriod(), qfDetail.getExpectedSupportTerm(), 
							qfDetail.getExpectedTechnicalSpecification(), qfDetail.getExpectedPayment(), vendorDetails.getwarrantyPeriod(), vendorDetails.getSupportTerm(), vendorDetails.getTechnicalSpecification(), 
							vendorDetails.getExpectedPayment(), vendorDetails.getwarrantyPeriodRejectNote(), vendorDetails.getSupportTermRejectNote(), vendorDetails.getTechnicalSpecificationRejectNote(),
							vendorDetails.getExpectedPaymentRejectNote(), qfDetail.getRemark(), vendorDetails.getRejectNote(), vendorDetails.getAccept(), vendorDetails.getReject(), vendorDetails.getOnHold(),vendorDetails.getVendor().getName(),vendorDetails.getQuotationFill().getQuotationVendor().getVqNumber()) ;
					vendorsDetailslist.add(mapper);
					}
			    }
			}
			
			}
			//end if condation for resolved HIS-9541 Quotation fill in cancelled still visible on the technical approval screen
		}
		
		return vendorsDetailslist;
	}
	
	@Override
	@Transactional
	public Object cancelTechnicalApproval(TechnicalApprovalRequestDto techApprovalRequestDto) {

		TechnicalApproval technicalApproval = technicalApprovalDao.getTechnicalApproval(techApprovalRequestDto.getId());
		
		for(TechnicalApprovalDetail detail : technicalApproval.getTechnicalApprovalDetailsList()) {
			detail.setStatus(new Status(techApprovalRequestDto.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(technicalApproval.getUnitId()));
			detail.setUpdatedByUser(techApprovalRequestDto.getUserId());
		}
		technicalApproval.setCancelReason(new CancelReason(techApprovalRequestDto.getCancelReasonId()));
		technicalApproval.setCancelNote(techApprovalRequestDto.getCancelNote());

		technicalApproval.setStatus(new Status(techApprovalRequestDto.getStatusId()));
		//technicalApproval.setApprovalStatus(new Status(techApprovalRequestDto.getApprovalStatusId()));

		technicalApproval.setUpdatedDate(TimeZoneComponent.getDateByzone(technicalApproval.getUnitId()));
		technicalApproval.setUpdatedByUser(techApprovalRequestDto.getUserId());

		technicalApprovalDao.updateTechnicalApproval(technicalApproval);

		CommonResponse response = new CommonResponse();
		response.setMessage("Technical Approval cancelled successfully");

		return response;
	}

}
