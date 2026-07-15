package com.param.procurement.quotation.service.enqSendToVendor;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestAccessoriesMapper;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.QuotationVendor;
import com.param.entity.model.procurement.QuotationVendorDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.procurement.quotation.dao.enqSendToVendor.IEnqSendToVendorDao;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToMultiVendorReqDto;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToMultipleVendorReqDto;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToVendorDetailRespDto;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToVendorReqDto;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToVendorRespDto;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToVendorSearchDto;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToVentorReqDetailDto;
import com.param.procurement.quotation.dto.enqSendToVendor.MultipleVendors;
import com.param.procurement.quotation.dto.enqSendToVendor.QuotationDetailSearchResponse;
import com.param.procurement.quotation.dto.enqSendToVendor.QuotationDetailsResponse;
import com.param.procurement.quotation.dto.enqSendToVendor.QuotationSearchResponse;
import com.param.procurement.quotation.dto.quotationRequest.QRAccessoriesMapper;
import com.param.procurement.quotation.dto.quotationRequest.QuotationReqDropDownDto;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class EnqSendToVendorService implements IEnqSendToVendorService{
	
	private Integer screenId = Screen.EnquirySendtoVendor.getScreen();
	
	@Autowired
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService GlobalService;
	
	@Autowired
	IEnqSendToVendorDao enqSendToVendorDao;

	@Autowired
	private SessionService sessionService;
	
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

		for (AssetType assetType : GlobalDao.getAssetTypeList(0,0)) {
			quotationDropDownDto
					.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}
		return quotationDropDownDto;
	}
	
	@Override
	@Transactional
	public Object getEnquirySendToVendorList(EnqSendToVendorReqDto enqSendToVendorReqDto) {
		enqSendToVendorReqDto.setToDate((new DateTime(enqSendToVendorReqDto.getToDate())).plusDays(1).toDate());

		List<EnqSendToVendorSearchDto> enqSendToVendorSearchList = new ArrayList<EnqSendToVendorSearchDto>();

		for (QuotationVendor quotationVendor : enqSendToVendorDao.getQuotationVendorSearchList(enqSendToVendorReqDto, screenId)) {
			
			EnqSendToVendorSearchDto enqSendToVendor = new EnqSendToVendorSearchDto(quotationVendor.getId(), quotationVendor.getQuotationRequest().getQrNo(), 
					quotationVendor.getQuotationRequest().getAddedDate(),
					quotationVendor.getVqNumber(), quotationVendor.getAddedDate(), quotationVendor.getStore().getStoreName(), quotationVendor.getVendor().getName(),
					quotationVendor.getDueDate(), quotationVendor.getAddedBy().getUserName(), quotationVendor.getStatus().getStatus(), quotationVendor.getApprovalStatus().getStatus());
			
			enqSendToVendorSearchList.add(enqSendToVendor);
		}
		return enqSendToVendorSearchList;
	}

	@Override
	@Transactional
	public Object saveEnquirySendToVendor(EnqSendToMultiVendorReqDto enqSendToVendorReqDto) {

		/*Random rand = new Random();
		int num = rand.nextInt(1000);
		String vqNumber = "VQ-" + num;
		*/
		/*Integer storeId = enqSendToVendorReqDto.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		
				
		for(MultipleVendors mv : enqSendToVendorReqDto.getVenders()) {
			String enquiryVendorNo = GlobalService.getBillNoByGeneratedCode(enqSendToVendorReqDto.getStoreId(), enqSendToVendorReqDto.getUnitId()!=null ? enqSendToVendorReqDto.getUnitId(): sessionService.getUnitId(), screenId);
				
			
			QuotationVendor quotationVendor = new QuotationVendor( enqSendToVendorReqDto.getDueDate(), new QuotationRequest(enqSendToVendorReqDto.getQrId()),
					new Status(enqSendToVendorReqDto.getStatusId()), new Store(enqSendToVendorReqDto.getStoreId()), 
					new Vendor(mv.getVendorId()), enquiryVendorNo, new Status(enqSendToVendorReqDto.getApprovalStatusId()));
			quotationVendor.setCreateDefaults(enqSendToVendorReqDto.getOrganizationId()!=null ? enqSendToVendorReqDto.getOrganizationId() : sessionService.getOrganizationId(),enqSendToVendorReqDto.getUnitId() !=null ? enqSendToVendorReqDto.getUnitId() :sessionService.getUnitId() ,enqSendToVendorReqDto.getUserId()!=null ? enqSendToVendorReqDto.getUserId() : sessionService.getUserId());
			
			for(EnqSendToVentorReqDetailDto item : enqSendToVendorReqDto.getItems()) {
				if(mv.getVendorId().equals(item.getVendorId())) {
					QuotationVendorDetail quotationVendorDetail=new QuotationVendorDetail(new QuotationRequestDetail(item.getQrDetailId()),new Status(enqSendToVendorReqDto.getStatusId()));
					quotationVendorDetail.setCreateDefaults(enqSendToVendorReqDto.getOrganizationId()!=null ? enqSendToVendorReqDto.getOrganizationId() : sessionService.getOrganizationId(),enqSendToVendorReqDto.getUnitId() !=null ? enqSendToVendorReqDto.getUnitId() :sessionService.getUnitId() ,enqSendToVendorReqDto.getUserId()!=null ? enqSendToVendorReqDto.getUserId() : sessionService.getUserId());
					
					QuotationVendorDetail detail = quotationVendor.addQuotationVendorDetail(quotationVendorDetail);
					
					
				    enqSendToVendorDao.saveQuotationVendor(quotationVendor);	
				}
				
			}
				
			//response.setId(quotationVendor.getId().longValue());
		}
			
		CommonResponse response = new CommonResponse();
		response.setMessage("Enquiry Send To Vendor saved successfully");
		return response;

	}

		@Override
	@Transactional
	public Object updateEnquirySendToVendor(EnqSendToVendorReqDto enqSendToVendorReqDto) {

			
		QuotationVendor quotationVendor = enqSendToVendorDao.getQuotationVendor(enqSendToVendorReqDto.getId());
		
		quotationVendor.updateQuotationVendor(enqSendToVendorReqDto.getDueDate()!=null ? enqSendToVendorReqDto.getDueDate() : quotationVendor.getDueDate());
		
		for(QuotationVendorDetail detail : quotationVendor.getQuotationVendorDetailsList()) {
			Integer id = null;
			for(EnqSendToVentorReqDetailDto item : enqSendToVendorReqDto.getItems()) {
				if(item.getId() !=null && item.getId().equals(detail.getId())) {
					id = item.getId();
					//no field for update()
				}
			}
			
			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationVendor.getUnitId()));
			detail.setUpdatedByUser(enqSendToVendorReqDto.getUserId());

		}
		for(EnqSendToVentorReqDetailDto item : enqSendToVendorReqDto.getItems()) {
			if (item.getId() == null) {
				QuotationVendorDetail qvd=new QuotationVendorDetail(new QuotationRequestDetail(item.getQrDetailId()),
						new Status(item.getStatusId()));
				qvd.setCreateDefaults(enqSendToVendorReqDto.getOrganizationId(), enqSendToVendorReqDto.getUnitId(), enqSendToVendorReqDto.getUserId());
				quotationVendor.addQuotationVendorDetail(qvd);
			}
		}
		
		quotationVendor.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationVendor.getUnitId()));
		quotationVendor.setUpdatedByUser(enqSendToVendorReqDto.getUserId());

		enqSendToVendorDao.updateQuotationVendor(quotationVendor);

		CommonResponse response = new CommonResponse();
		response.setMessage("Enquiry Send To Vendor updated successfully");

		return response;
	}
		
		

	@Override
	@Transactional
	public Object getQuotationVendor(Integer id,Integer orgId) {

		QuotationVendor quotationVendor = enqSendToVendorDao.getQuotationVendor(id);
		
		EnqSendToVendorRespDto qrVendorResp = new EnqSendToVendorRespDto(quotationVendor.getId(), quotationVendor.getQuotationRequest().getId(),quotationVendor.getQuotationRequest().getQrNo(),
				quotationVendor.getQuotationRequest().getAddedDate(), quotationVendor.getVqNumber(), quotationVendor.getAddedDate(), 
				quotationVendor.getStore().getStoreName(), quotationVendor.getStore().getStoreId(), quotationVendor.getQuotationRequest().getAssetType().getId(),quotationVendor.getQuotationRequest().getAssetType().getType(),
				quotationVendor.getQuotationRequest().getRequestType().getId(),quotationVendor.getQuotationRequest().getRequestType().getType(),
				quotationVendor.getVendor().getName(), quotationVendor.getVendor().getId(), quotationVendor.getDueDate(), 
				quotationVendor.getAddedBy().getUserName(), quotationVendor.getStatus().getStatus(), quotationVendor.getStatus().getId(), 
				quotationVendor.getApprovalStatus().getStatus(), quotationVendor.getApprovalStatus().getId()); 
		
		for(QuotationVendorDetail detail : quotationVendor.getQuotationVendorDetailsList())	{
			if(detail.getIsActive()!=false && detail.getIsDeleted()!=true) {
			qrVendorResp.addItems(new EnqSendToVendorDetailRespDto(detail.getId(), detail.getQrDetail().getItem().getId(), detail.getQrDetail().getItem().getCode(),
					detail.getQrDetail().getItem().getName(), detail.getQrDetail().getUomType()!=null ?detail.getQrDetail().getUomType().getId() : null, detail.getQrDetail().getUomType()!=null ? detail.getQrDetail().getUomType().getType() : null, 
							detail.getQrDetail().getUomUnit()!=null ? detail.getQrDetail().getUomUnit().getId() : null, detail.getQrDetail().getUomUnit()!=null ? detail.getQrDetail().getUomUnit().getUnits() : null, 
					detail.getQrDetail().getQuantity(), detail.getQrDetail().getWarrantyPeriod(), detail.getQrDetail().getExpectedSupportTerm(),
					detail.getQrDetail().getExpectedPayment(),detail.getQrDetail().getExpectedTechnicalSpecification(), 
					quotationVendor.getQuotationRequest().getAssetType().getId().equals(Constants.getConsumableId()) ? GlobalDao.getUnitOfMeasurementListByItem(detail.getQrDetail().getItem().getId(),orgId) : null, 
					enqSendToVendorDao.getPurchaseRequestDetail(detail.getQrDetail().getId()), getExpectedAccessories(detail.getQrDetail())));
			}
		}
	
		return qrVendorResp;				
	}

	
	@Transactional
	private List<QRAccessoriesMapper> getExpectedAccessories(QuotationRequestDetail detail) {
		List<QuotationRequestAccessoriesMapper> qrAccessMapperList = detail.getQuotationRequestAccessoriesMapperList();
		List<QRAccessoriesMapper> qrAccesoriesMapper = new ArrayList<QRAccessoriesMapper>();
		for(QuotationRequestAccessoriesMapper qrAccess : qrAccessMapperList) { 
			QRAccessoriesMapper qrAccessMapper = new QRAccessoriesMapper(qrAccess.getId(), qrAccess.getAccessoriesDetail(),
			qrAccess.getName());
			qrAccesoriesMapper.add(qrAccessMapper);
		}		
		return qrAccesoriesMapper;
		
	}
	
	
	
	@Override
	@Transactional
	public Object approveQuotationVendor(EnqSendToVendorReqDto enqSendToVendorReqDto) {

		for (Integer id : enqSendToVendorReqDto.getVqrId()) {
					
			QuotationVendor quotationVendor = enqSendToVendorDao.getQuotationVendor(id);

			quotationVendor.setStatus(new Status(enqSendToVendorReqDto.getStatusId()));
			quotationVendor.setApprovalStatus(new Status(enqSendToVendorReqDto.getApprovalStatusId()));

			for(QuotationVendorDetail detail : quotationVendor.getQuotationVendorDetailsList()) {
				detail.setStatus(new Status(enqSendToVendorReqDto.getStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationVendor.getUnitId()));
				detail.setUpdatedByUser(enqSendToVendorReqDto.getUserId());
			}

			quotationVendor.setApprovedDate(TimeZoneComponent.getDateByzone(quotationVendor.getUnitId()));
			quotationVendor.setApprovedBy(new User(enqSendToVendorReqDto.getUserId()!=null ? enqSendToVendorReqDto.getUserId() : sessionService.getUserId()));

			quotationVendor.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationVendor.getUnitId()));
			quotationVendor.setUpdatedByUser(enqSendToVendorReqDto.getUserId());

			enqSendToVendorDao.updateQuotationVendor(quotationVendor);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Enquiry Send To Vendor approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelQuotationVendor(EnqSendToVendorReqDto enqSendToVendorReqDto) {

		QuotationVendor quotationVendor = enqSendToVendorDao.getQuotationVendor(enqSendToVendorReqDto.getId());
		
		for(QuotationVendorDetail detail : quotationVendor.getQuotationVendorDetailsList()) {
			detail.setStatus(new Status(enqSendToVendorReqDto.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationVendor.getUnitId()));
			detail.setUpdatedByUser(enqSendToVendorReqDto.getUserId());
		}
		quotationVendor.setCancelReason(new CancelReason(enqSendToVendorReqDto.getCancelReasonId()));
		quotationVendor.setCancelNote(enqSendToVendorReqDto.getCancelNote());

		quotationVendor.setStatus(new Status(enqSendToVendorReqDto.getStatusId()));
		quotationVendor.setApprovalStatus(new Status(enqSendToVendorReqDto.getApprovalStatusId()));

		quotationVendor.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationVendor.getUnitId()));
		quotationVendor.setUpdatedByUser(enqSendToVendorReqDto.getUserId());

		enqSendToVendorDao.updateQuotationVendor(quotationVendor);

		CommonResponse response = new CommonResponse();
		response.setMessage("Enquiry Send To Vendor cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getQuotationRequestSearchList(EnqSendToVendorReqDto enqSendToVendorReqDto) {
		List<QuotationSearchResponse> quotationSearchResponseList = new ArrayList<QuotationSearchResponse>();
		boolean getQr =  enqSendToVendorReqDto.getGetQr()==null ? true : false;
		if(getQr) {
		enqSendToVendorReqDto.setToDate((new DateTime(enqSendToVendorReqDto.getToDate())).plusDays(1).toDate());
		
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
		statusIds.add(com.param.common.constants.Status.PendingApproval.getStatus());

		List<QuotationRequest> qrList = enqSendToVendorDao.getQuotationRequestSearchList(enqSendToVendorReqDto,statusIds);
		
		for (QuotationRequest quotationRequest : qrList) {
			
		QuotationSearchResponse quotationReqSearch = new QuotationSearchResponse(quotationRequest.getId(), quotationRequest.getAddedDate(),
		quotationRequest.getQrNo(),quotationRequest.getStore().getStoreName(), quotationRequest.getAddedBy().getUserName(),
			quotationRequest.getApprovedBy()!=null ? quotationRequest.getApprovedBy().getUserName() : null);
		
		for(QuotationRequestDetail detail : quotationRequest.getQuotationRequestDetailsList()) {
			if(detail.getIsActive()!=false && detail.getIsDeleted()!=true) {
			
			quotationReqSearch.addItems(new QuotationDetailSearchResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(), detail.getItem().getName(), 
					detail.getUomType()!=null ? detail.getUomType().getId() : null, 
					detail.getUomType()!=null ? detail.getUomType().getType() : null, 
					detail.getUomUnit()!=null ? detail.getUomUnit().getId() : null, 
					detail.getUomUnit()!=null ? detail.getUomUnit().getUnits() : null,
					detail.getQuantity()));
			}
			
		}
			quotationSearchResponseList.add(quotationReqSearch);

			}
		}
		else if(!getQr) {
			QuotationRequest quotationRequest = enqSendToVendorDao.getQuotationRequestList(enqSendToVendorReqDto.getQrId());
			QuotationSearchResponse quotationReqSearch = new QuotationSearchResponse(quotationRequest.getId(), quotationRequest.getAddedDate(),
					quotationRequest.getQrNo(),quotationRequest.getStore().getStoreName(), quotationRequest.getAddedBy().getUserName(),
						quotationRequest.getApprovedBy()!=null ? quotationRequest.getApprovedBy().getUserName() : null);
			List<QuotationRequestDetail> qrDetailList = quotationRequest.getQuotationRequestDetailsList();
			
				 for(QuotationVendorDetail qvDetail : enqSendToVendorDao.getQuotationVendorDetail(enqSendToVendorReqDto.getId())) {
						 qrDetailList.remove(qvDetail.getQrDetail());
			}
			
			for(QuotationRequestDetail detail : qrDetailList) {
				if(detail.getIsActive()!=false && detail.getIsDeleted()!=true) {
				quotationReqSearch.addItems(new QuotationDetailSearchResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(), detail.getItem().getName(), 
						detail.getUomType()!=null ? detail.getUomType().getId() : null, 
								detail.getUomType()!=null ? detail.getUomType().getType() : null, 
								detail.getUomUnit()!=null ? detail.getUomUnit().getId() : null, 
								detail.getUomUnit()!=null ? detail.getUomUnit().getUnits() : null,
								detail.getQuantity()));
				}
			}
			quotationSearchResponseList.add(quotationReqSearch);
		}
		return quotationSearchResponseList;
	}
	
	@Override
	@Transactional
	public Object getQuotationRequestDetails(List<EnqSendToVendorReqDto> enqSendToVendorReqDtoList) {
		
		List<QuotationDetailsResponse> response = new ArrayList<QuotationDetailsResponse>();
		for (EnqSendToVendorReqDto enqSendToVendorReqDto : enqSendToVendorReqDtoList) {

			QuotationRequest quotationRequest = enqSendToVendorDao.getQuotationRequestList(enqSendToVendorReqDto.getQrId());
			
			QuotationDetailsResponse quotationDetailsResponse = new QuotationDetailsResponse(quotationRequest.getId(),quotationRequest.getQrNo(),quotationRequest.getAddedDate(),quotationRequest.getAssetType().getId(),
					quotationRequest.getAssetType().getType(),quotationRequest.getRequestType().getId(),quotationRequest.getRequestType().getType()); 
			
			for (Integer itemId : enqSendToVendorReqDto.getItemIds()) {
			
			  QuotationRequestDetail detail = enqSendToVendorDao.getQuotationRequestDetailsList(itemId,quotationRequest.getId());
				if(detail!=null && detail.getQuotationRequest().getId().equals(quotationRequest.getId())) {
				  quotationDetailsResponse.addItems(new EnqSendToVendorDetailRespDto(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(), 
						  detail.getItem().getName(), 
						  detail.getUomType()!=null ? detail.getUomType().getId() : null, 
						  detail.getUomType()!=null ? detail.getUomType().getType() : null, 
					      detail.getUomUnit()!=null ? detail.getUomUnit().getId() : null, 
						  detail.getUomUnit()!=null ? detail.getUomUnit().getUnits() : null, detail.getQuantity(), detail.getWarrantyPeriod(), 
						  detail.getExpectedSupportTerm(), detail.getExpectedPayment(), detail.getExpectedTechnicalSpecification(), 
						  enqSendToVendorDao.getPurchaseRequestDetail(detail.getId()), getExpectedAccessories(detail)));
					}
				response.add(quotationDetailsResponse);
			    }
			
		}
		
		return response;
	}

	@Override
	@Transactional
	public Object getQuotationRequesMultipleSearchtList(EnqSendToMultipleVendorReqDto enqSendToVendorReqDto) {

		List<QuotationSearchResponse> quotationSearchResponseList = new ArrayList<QuotationSearchResponse>();
		boolean getQr =  enqSendToVendorReqDto.getGetQr()==null ? true : false;
		if(getQr) {
		enqSendToVendorReqDto.setToDate((new DateTime(enqSendToVendorReqDto.getToDate())).plusDays(1).toDate());
		
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
		statusIds.add(com.param.common.constants.Status.PendingApproval.getStatus());

		List<QuotationRequest> qrList = enqSendToVendorDao.getQuotationRequestMultipleSearchList(enqSendToVendorReqDto,statusIds);
		
		for (QuotationRequest quotationRequest : qrList) {
			
		QuotationSearchResponse quotationReqSearch = new QuotationSearchResponse(quotationRequest.getId(), quotationRequest.getAddedDate(),
		quotationRequest.getQrNo(),quotationRequest.getStore().getStoreName(), quotationRequest.getAddedBy().getUserName(),
			quotationRequest.getApprovedBy()!=null ? quotationRequest.getApprovedBy().getUserName() : null);
		
		for(QuotationRequestDetail detail : quotationRequest.getQuotationRequestDetailsList()) {
			if(detail.getIsActive()!=false && detail.getIsDeleted()!=true) {
			
			quotationReqSearch.addItems(new QuotationDetailSearchResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(), detail.getItem().getName(), 
					detail.getUomType()!=null ? detail.getUomType().getId() : null, 
					detail.getUomType()!=null ? detail.getUomType().getType() : null, 
					detail.getUomUnit()!=null ? detail.getUomUnit().getId() : null, 
					detail.getUomUnit()!=null ? detail.getUomUnit().getUnits() : null,
					detail.getQuantity()));
			}
			
		}
			quotationSearchResponseList.add(quotationReqSearch);

			}
		}
		else if(!getQr) {
			QuotationRequest quotationRequest = enqSendToVendorDao.getQuotationRequestList(enqSendToVendorReqDto.getQrId());
			QuotationSearchResponse quotationReqSearch = new QuotationSearchResponse(quotationRequest.getId(), quotationRequest.getAddedDate(),
					quotationRequest.getQrNo(),quotationRequest.getStore().getStoreName(), quotationRequest.getAddedBy().getUserName(),
						quotationRequest.getApprovedBy()!=null ? quotationRequest.getApprovedBy().getUserName() : null);
			List<QuotationRequestDetail> qrDetailList = quotationRequest.getQuotationRequestDetailsList();
			
				 for(QuotationVendorDetail qvDetail : enqSendToVendorDao.getQuotationVendorDetail(enqSendToVendorReqDto.getId())) {
						 qrDetailList.remove(qvDetail.getQrDetail());
			}
			
			for(QuotationRequestDetail detail : qrDetailList) {
				if(detail.getIsActive()!=false && detail.getIsDeleted()!=true) {
				quotationReqSearch.addItems(new QuotationDetailSearchResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(), detail.getItem().getName(), 
						detail.getUomType()!=null ? detail.getUomType().getId() : null, 
								detail.getUomType()!=null ? detail.getUomType().getType() : null, 
								detail.getUomUnit()!=null ? detail.getUomUnit().getId() : null, 
								detail.getUomUnit()!=null ? detail.getUomUnit().getUnits() : null,
								detail.getQuantity()));
				}
			}
			quotationSearchResponseList.add(quotationReqSearch);
		}
		return quotationSearchResponseList;
	
	}
}
