package com.param.procurement.quotation.service.vendorNegotiation;

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
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.VendorNegotiation;
import com.param.entity.model.procurement.VendorNegotiationDetail;
import com.param.entity.model.procurement.VendorNegotiationMapper;
import com.param.entity.model.procurement.VendorSelection;
import com.param.entity.model.procurement.VendorSelectionDetail;
import com.param.entity.model.procurement.VendorSelectionMapper;
import com.param.global.dao.global.IGlobalDao;
import com.param.procurement.quotation.dao.quotationRequest.IQuotationRequestDao;
import com.param.procurement.quotation.dao.vendorNegotiation.IVendorNegotiationDao;
import com.param.procurement.quotation.dao.vendorSelection.IVendorSelectionDao;
import com.param.procurement.quotation.dto.VendorNegotiationMapperDto;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationDetailsRequest;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationDetailsResponse;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationDropDown;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationMapperResp;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationRequest;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationResponse;
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationSearchResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class VendorNegotiationService implements IVendorNegotiationService {

	private Integer screenId = Screen.VendorNegociation.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IVendorNegotiationDao vendorNegotiationDao;
	
	@Autowired
	IVendorSelectionDao vendorSelectionDao;
	
	@Autowired
	IQuotationRequestDao quotationRequestDao;
	

@Autowired
private SessionService sessionService;


	@Override
	@Transactional
	public VendorNegotiationDropDown getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		VendorNegotiationDropDown vNegotiationDropDown = new VendorNegotiationDropDown();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			vNegotiationDropDown
					.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			vNegotiationDropDown.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		return vNegotiationDropDown;
	}

	@Override
	@Transactional
	public Object getVendorNegotiationList(VendorNegotiationRequest vendorNegotiationRequest) 
	{
		vendorNegotiationRequest.setToDate((new DateTime(vendorNegotiationRequest.getToDate())).plusDays(1).toDate());

		List<VendorNegotiationSearchResponse> vendorNegotiationSearchList = new ArrayList<VendorNegotiationSearchResponse>();

		for (VendorNegotiation vendorNegotiation : vendorNegotiationDao.searchVendorNegotiationList(vendorNegotiationRequest, screenId)) {

						
			
			VendorNegotiationSearchResponse response = new VendorNegotiationSearchResponse(vendorNegotiation.getId(),
					vendorNegotiation.getQuotationFil().getQuotationRequest().getAddedDate(),
					vendorNegotiation.getQuotationFil().getQuotationRequest().getQrNo(),
					vendorNegotiation.getQuotationFil().getQuotationVendor().getAddedDate(),
					vendorNegotiation.getQuotationFil().getQuotationVendor().getVqNumber(),
					vendorNegotiation.getStore().getStoreName(), vendorNegotiation.getAssetType().getType(),
					vendorNegotiation.getQuotationFil().getQuotationRequest().getRequestType().getType(),
					vendorNegotiation.getVendor().getName(), vendorNegotiation.getAddedBy().getUserName(),
					vendorNegotiation.getStatus().getStatus());

			vendorNegotiationSearchList.add(response);

		}
		return vendorNegotiationSearchList;
	}
	

	/*
	 * @Override
	 * 
	 * @Transactional public Object updateVendorNegotiation(VendorNegotiationRequest
	 * vendorNegotiationRequest) { VendorSelection
	 * vendorSelection=vendorSelectionDao.getVendorSelectionFornegotiation(
	 * vendorNegotiationRequest.getQrId()); for(VendorSelectionDetail details :
	 * vendorSelection.getVendorSelectionDetailList()) { for
	 * (VendorNegotiationDetailsRequest item : vendorNegotiationRequest.getItems())
	 * {
	 * if(vendorNegotiationRequest.getQrId()==vendorSelection.getQuotationRequest().
	 * getId()||item.getQrDetailId()==details.getId()) { VendorNegotiation
	 * vendorNegotiation =
	 * vendorNegotiationDao.getVendorNegotiation(vendorNegotiationRequest.getId());
	 * Boolean accept = true; vendorNegotiation.setStatus(new
	 * Status(vendorNegotiationRequest.getStatusId()));
	 * 
	 * for (VendorNegotiationDetail detail :
	 * vendorNegotiation.getVendorNegociationDetailsList()) { Integer id = null; for
	 * (VendorNegotiationDetailsRequest items : vendorNegotiationRequest.getItems())
	 * { if (items.getId() != null && items.getId().equals(detail.getId())) { id =
	 * items.getId(); VendorNegotiationMapperResp itemsMapper =
	 * items.getNegotiationDetails(); VendorNegotiationMapper
	 * vendorNegotiationMapper = new VendorNegotiationMapper(
	 * itemsMapper.getNegotiationCop(), itemsMapper.getNegotiationRemark(),
	 * itemsMapper.getAccept(), itemsMapper.getReject(),
	 * itemsMapper.getRejectNote(), itemsMapper.getOnHold(), new
	 * Status(vendorNegotiationRequest.getStatusId()),
	 * itemsMapper.getNegotiationPaymentTerms(),
	 * itemsMapper.getNegotiationSupportTerms(),
	 * itemsMapper.getNegotiationTechSpec(),
	 * itemsMapper.getNegotiationWarrantyPeriod());
	 * sessionService.setEntityDefaults(vendorNegotiationMapper);
	 * detail.addVendorNegociationMapperList(vendorNegotiationMapper); break; } }
	 * detail.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId)); detail.setUpdatedBy(detail.getUser()); }
	 * vendorNegotiation.setUpdatedDate(TimeZoneComponent.getDateByzone(unitId));
	 * vendorNegotiation.setUpdatedBy(vendorNegotiation.getUser());
	 * VendorNegotiation vendorNegotiationResponse =
	 * vendorNegotiationDao.updateVendorNegotiation(vendorNegotiation); } else
	 * if(vendorNegotiationRequest.getQrId()!=vendorSelection.getQuotationRequest().
	 * getId()||item.getQrDetailId()!=details.getId()) {
	 * saveVendorSelection(vendorNegotiationRequest);
	 * 
	 * } } } CommonResponse response = new CommonResponse();
	 * response.setMessage("Vendor Negotiation updated successfully"); return
	 * response;
	 * 
	 * }
	 */
	
	@Override
	@Transactional
	public Object updateVendorNegotiation(VendorNegotiationRequest vendorNegotiationRequest)
	{
		
		
		
		QuotationRequest quotation = quotationRequestDao.getQuotationRequest(vendorNegotiationRequest.getQrId());

		VendorSelection  vendorSelection=vendorSelectionDao.getVendorSelectionFornegotiation(vendorNegotiationRequest.getQrId());

		for (QuotationRequestDetail qrDetail : quotation.getQuotationRequestDetailsList()) {
			
			for (VendorNegotiationDetailsRequest item : vendorNegotiationRequest.getItems())
			{
				if(vendorNegotiationRequest.getQrId().equals(quotation.getId())||item.getQrDetailId().equals(qrDetail.getId()))
				{
					VendorNegotiation vendorNegotiation = vendorNegotiationDao.getVendorNegotiation(vendorNegotiationRequest.getId());
					Boolean accept = true;
					vendorNegotiation.setStatus(new Status(vendorNegotiationRequest.getStatusId()));
				
					for (VendorNegotiationDetail detail : vendorNegotiation.getVendorNegociationDetailsList())
					{
						Integer id = null;
						for (VendorNegotiationDetailsRequest items : vendorNegotiationRequest.getItems())
						{
							if (items.getId() != null && items.getId().equals(detail.getId()))
							{
								id = items.getId();
								VendorNegotiationMapperResp itemsMapper = items.getNegotiationDetails();
								VendorNegotiationMapper vendorNegotiationMapper = new VendorNegotiationMapper(
									itemsMapper.getNegotiationCop(), itemsMapper.getNegotiationRemark(),
									itemsMapper.getAccept(), itemsMapper.getReject(), itemsMapper.getRejectNote(),
									itemsMapper.getOnHold(), new Status(vendorNegotiationRequest.getStatusId()), itemsMapper.getNegotiationPaymentTerms(),
									itemsMapper.getNegotiationSupportTerms(), itemsMapper.getNegotiationTechSpec(),
									itemsMapper.getNegotiationWarrantyPeriod());
//								sessionService.setEntityDefaults(vendorNegotiationMapper);
								vendorNegotiationMapper.setCreateDefaults(vendorNegotiationRequest.getOrganizationId(),vendorNegotiationRequest.getUnitId(),
										vendorNegotiationRequest.getUserId());
								detail.addVendorNegociationMapperList(vendorNegotiationMapper);
								break;
							}
						//	break;
						}
						detail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotation.getUnitId()));
						detail.setUpdatedByUser(vendorNegotiationRequest.getUserId());
					//	break;
					}
					vendorNegotiation.setUpdatedDate(TimeZoneComponent.getDateByzone(quotation.getUnitId()));
					vendorNegotiation.setUpdatedByUser(vendorNegotiationRequest.getUserId());
					VendorNegotiation vendorNegotiationResponse = vendorNegotiationDao.updateVendorNegotiation(vendorNegotiation);
				}
				
				//break; 
			}
			//break;
		}
		if(vendorSelection == null)
		{
			saveVendorSelection(vendorNegotiationRequest);
			
		}else
		{
			saveVendorSelectionForNewVendor(vendorNegotiationRequest,vendorSelection);

		}
		
		CommonResponse response = new CommonResponse();
		response.setMessage("Vendor Negotiation updated successfully");
		return response;
		
	}
	
	@Override
	@Transactional
	public Object saveVendorSelection(VendorNegotiationRequest vendorNegotiationRequest) {
	  
	VendorNegotiation vendorNegotiation = vendorNegotiationDao.getVendorNegotiation(vendorNegotiationRequest.getId());
	  VendorSelection vendorSelection = new VendorSelection(new QuotationRequest(vendorNegotiationRequest.getQrId()) , new Status(2));
	  for(VendorNegotiationDetail detail : vendorNegotiation.getVendorNegociationDetailsList()) { 
		 Integer id = null;
	     for(VendorNegotiationDetailsRequest item : vendorNegotiationRequest.getItems()) { 
	    	 if(!item.getId().equals(null) &&	 item.getId().equals(detail.getId()) && item.getQrDetailId().equals(detail.getQuotationFillDetail().getQuotationRequestDetail().getId())) 
	    	 { 
	    		 VendorSelectionDetail vendorSelectionDetails= new VendorSelectionDetail(new QuotationRequestDetail(item.getQrDetailId()), new Status(2));
//	    		 sessionService.setEntityDefaults(vendorSelectionDetails);
	    		 
	    		 vendorSelectionDetails.setCreateDefaults(vendorNegotiationRequest.getOrganizationId(),vendorNegotiationRequest.getUnitId(),
	    				 vendorNegotiationRequest.getUserId());
	    		 VendorSelectionDetail 	vendorSelectionDetail = vendorSelection.addVendorSelectionDetail(vendorSelectionDetails);
	  
	    		 for(VendorNegotiationMapper mapper : detail.getVendorNegociationMappersList()) {
	    			 if(mapper.getAccept().equals(true)) 
	    			 {
	    				 VendorSelectionMapper vendorSelectionMapper= new VendorSelectionMapper(new VendorNegotiationMapper(mapper.getId()), 
	    						 new Vendor(vendorNegotiation.getVendor().getId()),false,false,false,false,false);
//	    				 sessionService.setEntityDefaults(vendorSelectionMapper);
	    				 vendorSelectionMapper.setCreateDefaults(vendorNegotiationRequest.getOrganizationId(),vendorNegotiationRequest.getUnitId(),
	    	    				 vendorNegotiationRequest.getUserId());
	    			      vendorSelectionDetail.addVendorSelectionMapper(vendorSelectionMapper);
	    			 }
	    			 } 
//	    		 sessionService.setEntityDefaults(vendorSelectionDetail);
	    		 vendorSelectionDetail.setCreateDefaults(vendorNegotiationRequest.getOrganizationId(),vendorNegotiationRequest.getUnitId(),
	    				 vendorNegotiationRequest.getUserId());
	    		 } 
	    	 }
	   }
//	  sessionService.setEntityDefaults(vendorSelection);
	  vendorSelection.setCreateDefaults(vendorNegotiationRequest.getOrganizationId(),vendorNegotiationRequest.getUnitId(),
				 vendorNegotiationRequest.getUserId());
	  vendorNegotiationDao.saveVendorSelection(vendorSelection); 
	  CommonResponse response = new CommonResponse();
	  response.setMessage("Send For Vendor Selection");

	  return response;
	}
	
	@Override
	@Transactional
	public Object saveVendorSelectionForNewVendor(VendorNegotiationRequest vendorNegotiationRequest,VendorSelection vendorSelectionreuest) {
	  
	VendorNegotiation vendorNegotiation = vendorNegotiationDao.getVendorNegotiation(vendorNegotiationRequest.getId());
	  for(VendorNegotiationDetail detail : vendorNegotiation.getVendorNegociationDetailsList()) { 
		 Integer id = null;
	     for(VendorNegotiationDetailsRequest item : vendorNegotiationRequest.getItems()) { 
	    	 if(!item.getId().equals(null) &&	 item.getId().equals(detail.getId()) && item.getQrDetailId().equals(detail.getQuotationFillDetail().getQuotationRequestDetail().getId())) 
	    	 { 
	  
	    		 for(VendorSelectionDetail vendorSelectionDetail : vendorSelectionreuest.getVendorSelectionDetailList()) {
	    			 if(vendorSelectionDetail.getQuotationRequestDetail().getId().equals(item.getQrDetailId()))
	    			 {
    					 for(VendorNegotiationMapper mapper : detail.getVendorNegociationMappersList()) {

    	    				 for(VendorSelectionMapper vendorSelectionMapper : vendorSelectionDetail.getVendorSelectionMapperList()) {
	    		    			 if(mapper.getAccept().equals(true) && !vendorSelectionMapper.getVendor().getId().equals(vendorNegotiation.getVendor().getId())) 
	    		    			 {
	    		    				 VendorSelectionMapper vendorSelectionMappernew= new VendorSelectionMapper(new VendorNegotiationMapper(mapper.getId()), 
	    		    						 new Vendor(vendorNegotiation.getVendor().getId()),mapper.getAccept(),false,false,false,false, 
	    		    						 new VendorSelectionDetail(vendorSelectionDetail.getId()));
//	    		    				 sessionService.setEntityDefaults(vendorSelectionMappernew);
	    		    				 vendorSelectionMappernew.setCreateDefaults(vendorNegotiationRequest.getOrganizationId(),vendorNegotiationRequest.getUnitId(),
	    		    						 vendorNegotiationRequest.getUserId());
	    		    				  vendorNegotiationDao.saveVendorSelectionForNew(vendorSelectionMappernew); 

	    		    			 }
	    		    		} 
	    					 
	    				 }
	    				 
	    			 }
	    			 
	    		 }
		    		
	    		 } 
	    	 }
	   }
	  
	  CommonResponse response = new CommonResponse();
	  response.setMessage("Send For Vendor Selection");

	  return response;
	}
	 
	@Override
	@Transactional
	public Object getVendorNegotiation(Integer id) {

		VendorNegotiation vendorNegotiation = vendorNegotiationDao.getVendorNegotiation(id);

		VendorNegotiationResponse response = new VendorNegotiationResponse(vendorNegotiation.getId(),vendorNegotiation.getQuotationFil().getQuotationRequest().getId(),
				vendorNegotiation.getQuotationFil().getQuotationRequest().getQrNo(),
				vendorNegotiation.getQuotationFil().getQuotationRequest().getAddedDate(),
				vendorNegotiation.getQuotationFil().getQuotationVendor().getVqNumber(),
				vendorNegotiation.getQuotationFil().getQuotationVendor().getAddedDate(),
				vendorNegotiation.getStore().getStoreId(), vendorNegotiation.getStore().getStoreName(),
				vendorNegotiation.getVendor().getName(), vendorNegotiation.getQuotationFil().getRefNumber(),
				vendorNegotiation.getQuotationFil().getRefDate(), vendorNegotiation.getStatus().getId(),
				vendorNegotiation.getStatus().getStatus());

		for (VendorNegotiationDetail detail : vendorNegotiation.getVendorNegociationDetailsList()) {

			List<VendorNegotiationMapperResp> mapperList = getNegotiationDetails(detail);

			response.addItems(new VendorNegotiationDetailsResponse(detail.getId(),
					detail.getQuotationFillDetail().getQuotationRequestDetail().getId(),
					detail.getQuotationFillDetail().getQuotationRequestDetail().getItem().getId(),
					detail.getQuotationFillDetail().getQuotationRequestDetail().getItem().getName(),
					detail.getQuotationFillDetail().getQuotationRequestDetail().getQuantity(),
					detail.getQuotationFillDetail().getQuotationRequestDetail().getWarrantyPeriod(),
					detail.getQuotationFillDetail().getQuotationRequestDetail().getExpectedSupportTerm(),
					detail.getQuotationFillDetail().getQuotationRequestDetail().getExpectedTechnicalSpecification(),
					detail.getQuotationFillDetail().getQuotationRequestDetail().getExpectedPayment(), null, null,
					detail.getQuotationFillDetail().getVendorWarrantyPeriod(),
					detail.getQuotationFillDetail().getExpectedSupportTerm(),
					detail.getQuotationFillDetail().getExpectedPayment(),
					detail.getQuotationFillDetail().getExpectedTechnicalSpecification(),
					detail.getQuotationFillDetail().getVendorCop(), detail.getQuotationFillDetail().getRemark(),
					mapperList));
		}

		return response;
	}

	private List<VendorNegotiationMapperResp> getNegotiationDetails(VendorNegotiationDetail detail) {

		List<VendorNegotiationMapperResp> vendorsDetailslist = new ArrayList<VendorNegotiationMapperResp>();
		List<Integer> ids = vendorNegotiationDao.getMapperIds(detail.getId());
		List<VendorNegotiationMapperDto> mapperData= new ArrayList<>();
		//if(ids.size() > 1)
		//{
			 mapperData=vendorNegotiationDao.getVendorNegotiationMapperList(detail.getId(),ids );
		
			
		//}
		//else
		//{
		//	mapperData=detail.getVendorNegociationMappersList();
		//}
	
		if(mapperData.size() > 0) {
				for (VendorNegotiationMapperDto vendorDetails :mapperData) {
					/*VendorNegotiationMapperResp mapper = new VendorNegotiationMapperResp(vendorDetails.getId(),
							vendorDetails.getAddedDate(), vendorDetails.getCop(), vendorDetails.getRemarks(),
							vendorDetails.getVendorwarrentyPeriod(), vendorDetails.getVendorSupportTerms(),
							vendorDetails.getVendorTechnicalSpecification(), vendorDetails.getVendorExpectedPayment(),
							vendorDetails.getAccept(), vendorDetails.getReject(), vendorDetails.getOnHold(),
							vendorDetails.getRejectNote());
					vendorsDetailslist.add(mapper);*/
					VendorNegotiationMapperResp dto=new VendorNegotiationMapperResp();
					dto.setId(vendorDetails.getId());
					dto.setNegotiationDate(vendorDetails.getAddedDate());
					dto.setNegotiationCop(vendorDetails.getCop());
					dto.setNegotiationRemark(vendorDetails.getRemarks());
					dto.setNegotiationWarrantyPeriod(vendorDetails.getVendorwarrentyPeriod());
					dto.setNegotiationSupportTerms(vendorDetails.getVendorSupportTerms());
					dto.setNegotiationTechSpec(vendorDetails.getVendorTechnicalSpecification());
					dto.setNegotiationPaymentTerms(vendorDetails.getVendorExpectedPayment());
					dto.setAccept(vendorDetails.getAccept());
					dto.setReject(vendorDetails.getReject());
					dto.setOnHold(vendorDetails.getOnHold());
					dto.setRejectNote(vendorDetails.getRejectNote());
					vendorsDetailslist.add(dto);
				 
			} 
		}else {
			for (VendorNegotiationMapperDto vendorDetails :mapperData) {
			VendorNegotiationMapperResp mapper = new VendorNegotiationMapperResp(vendorDetails.getId(),
					vendorDetails.getAddedDate(), vendorDetails.getCop(), vendorDetails.getRemarks(),
					vendorDetails.getVendorwarrentyPeriod(), vendorDetails.getVendorSupportTerms(),
					vendorDetails.getVendorTechnicalSpecification(), vendorDetails.getVendorExpectedPayment(),
					vendorDetails.getAccept(), vendorDetails.getReject(), vendorDetails.getOnHold(),
					vendorDetails.getRejectNote());
			vendorsDetailslist.add(mapper);
			}
		}
		
		
		return vendorsDetailslist;
	}

	@Override
	@Transactional
	public Object cancelVendorNegotiation(VendorNegotiationRequest vendorNegotiationRequest) {

		VendorNegotiation vendorNegotiation = vendorNegotiationDao
				.getVendorNegotiation(vendorNegotiationRequest.getId());

		for (VendorNegotiationDetail detail : vendorNegotiation.getVendorNegociationDetailsList()) {

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(vendorNegotiation.getUnitId()));
			detail.setUpdatedByUser(vendorNegotiationRequest.getUserId());
		}
		vendorNegotiation.setCancelReason(new CancelReason(vendorNegotiationRequest.getCancelReasonId()));
		vendorNegotiation.setCancelNote(vendorNegotiationRequest.getCancelNote());

		vendorNegotiation.setStatus(new Status(vendorNegotiationRequest.getStatusId()));
		

		vendorNegotiation.setUpdatedDate(TimeZoneComponent.getDateByzone(vendorNegotiation.getUnitId()));
		vendorNegotiation.setUpdatedByUser(vendorNegotiationRequest.getUserId());

		vendorNegotiationDao.updateVendorNegotiation(vendorNegotiation);

		CommonResponse response = new CommonResponse();
		response.setMessage("Vendor Negotiation cancelled successfully");

		return response;
	}
}
