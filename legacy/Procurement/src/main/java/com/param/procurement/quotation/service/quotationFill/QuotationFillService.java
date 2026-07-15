package com.param.procurement.quotation.service.quotationFill;

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
import com.param.entity.model.procurement.QuotationFill;
import com.param.entity.model.procurement.QuotationFillAccessory;
import com.param.entity.model.procurement.QuotationFillDetail;
import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestAccessoriesMapper;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.QuotationVendor;
import com.param.entity.model.procurement.QuotationVendorDetail;
import com.param.entity.model.procurement.TechnicalApproval;
import com.param.entity.model.procurement.TechnicalApprovalDetail;
import com.param.entity.model.procurement.TechnicalApprovalDetailMapper;
import com.param.entity.model.procurement.VendorNegotiation;
import com.param.entity.model.procurement.VendorNegotiationDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.service.global.IGlobalService;
import com.param.procurement.quotation.dao.quotationFill.IQuotationFillDao;
import com.param.procurement.quotation.dao.technicalApproval.ITechnicalApprovalDao;
import com.param.procurement.quotation.dto.quotationFill.QuotationFillAccessRequest;
import com.param.procurement.quotation.dto.quotationFill.QuotationFillDetailRequestDto;
import com.param.procurement.quotation.dto.quotationFill.QuotationFillDetailResponse;
import com.param.procurement.quotation.dto.quotationFill.QuotationFillDropDownDto;
import com.param.procurement.quotation.dto.quotationFill.QuotationFillRequest;
import com.param.procurement.quotation.dto.quotationFill.QuotationFillResponse;
import com.param.procurement.quotation.dto.quotationFill.QuotationFillSearchResponse;
import com.param.procurement.quotation.dto.quotationFill.QuotationVendorSearchDetailResponse;
import com.param.procurement.quotation.dto.quotationFill.QuotationVendorSearchResponse;
import com.param.procurement.quotation.dto.quotationFill.VendorDetails;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class QuotationFillService implements IQuotationFillService {

	private Integer screenId = Screen.QuotationFillIn.getScreen();

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IQuotationFillDao quotationFillDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	ITechnicalApprovalDao technicalApprovalDao;

	@Override
	@Transactional
	public QuotationFillDropDownDto getDropdowns(Integer unitId, Integer orgId, Integer userId) {
		QuotationFillDropDownDto quotationFillDropDownDto = new QuotationFillDropDownDto();

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			quotationFillDropDownDto
					.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			quotationFillDropDownDto.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
					screenStatus.getStatus().getStatus()));
		}

		for (AssetType assetType : GlobalDao.getAssetTypeList(0,0)) {
			quotationFillDropDownDto
					.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}
		return quotationFillDropDownDto;
	}

	@Override
	@Transactional
	public Object getQuotationFillList(QuotationFillRequest quotationFillRequest) {
		quotationFillRequest.setToDate((new DateTime(quotationFillRequest.getToDate())).plusDays(1).toDate());

		List<QuotationFillSearchResponse> quotationFillSearchList = new ArrayList<QuotationFillSearchResponse>();

		for (QuotationFill quotationFill : quotationFillDao.searchQuotationFillList(quotationFillRequest, screenId)) {

			QuotationFillSearchResponse quotationFillSearch = new QuotationFillSearchResponse(quotationFill.getId(),
					quotationFill.getQuotationRequest().getQrNo(), quotationFill.getQuotationRequest().getAddedDate(),
					quotationFill.getQuotationVendor().getVqNumber(), quotationFill.getQuotationVendor().getAddedDate(),
					quotationFill.getStore().getStoreName(), quotationFill.getVendor().getName(),
					quotationFill.getAddedBy().getUserName(), quotationFill.getStatus().getStatus(),
					quotationFill.getApprovalStatus().getStatus(),
					new VendorDetails(quotationFill.getVendor().getContactPersonName(),
							quotationFill.getVendor().getAddress(),
							quotationFill.getVendor().getCountry().getCountryName(),
							quotationFill.getVendor().getState() !=null ?quotationFill.getVendor().getState().getStateName() :null,
							quotationFill.getVendor().getCity() !=null ?quotationFill.getVendor().getCity().getCityName():null,
							quotationFill.getVendor().getArea()!=null ?quotationFill.getVendor().getArea().getAreaName() :null, 
							quotationFill.getVendor().getZipcode()));

			quotationFillSearchList.add(quotationFillSearch);
		}
		return quotationFillSearchList;
	}

	@Override
	@Transactional
	public Object saveQuotationFill(QuotationFillRequest quotationFillRequest) {

		/*
		 * Integer storeId = quotationFillRequest.getStoreId();
		 * 
		 * Store storeNew = GlobalDao.getStore(storeId);
		 * 
		 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		 */

		QuotationFill quotationFill = new com.param.entity.model.procurement.QuotationFill(
				new AssetType(quotationFillRequest.getAssetTypeId()), new Status(quotationFillRequest.getStatusId()),
				new Store(quotationFillRequest.getStoreId()), new Status(quotationFillRequest.getApprovalStatusId()),
				new QuotationRequest(quotationFillRequest.getQrId()),
				new QuotationVendor(quotationFillRequest.getVqrId()), quotationFillRequest.getRemark(),
				quotationFillRequest.getRefNumber(), quotationFillRequest.getRefDate(),
				new Vendor(quotationFillRequest.getVendorId()));
		quotationFill.setCreateDefaults(quotationFillRequest.getOrganizationId()!=null ? quotationFillRequest.getOrganizationId() : sessionService.getOrganizationId(),quotationFillRequest.getUnitId() !=null ? quotationFillRequest.getUnitId() :sessionService.getUnitId() ,quotationFillRequest.getUserId()!=null ? quotationFillRequest.getUserId() : sessionService.getUserId());
		for (QuotationFillDetailRequestDto item : quotationFillRequest.getItems()) {

			QuotationFillDetail detail = quotationFill.addQuotationFillDetailList(new QuotationFillDetail(
					new Status(item.getStatusId()), item.getVendorCop(), item.getVendorExpectedPayment(),
					item.getVendorExpectedSupport(), item.getVendorTechnicalSpecification(),
					item.getVendorWarrantyPeriod(), item.getRemark(), new QuotationRequestDetail(item.getQrDetailId()),
					new QuotationVendorDetail(item.getVqrDetailId())));

			if (item.getAccessoriesDetails() != null) {
				for (QuotationFillAccessRequest qrAccess : item.getAccessoriesDetails())
					if (qrAccess.getQrAccessMapperId() != null) {
						QuotationFillAccessory quotationFillAccessory = new QuotationFillAccessory(qrAccess.getName(),
								new QuotationRequestAccessoriesMapper(qrAccess.getQrAccessMapperId()),
								qrAccess.getVendorDetails());
						quotationFillAccessory.setCreateDefaults(quotationFillRequest.getOrganizationId()!=null ? quotationFillRequest.getOrganizationId() : sessionService.getOrganizationId(),quotationFillRequest.getUnitId() !=null ? quotationFillRequest.getUnitId() :sessionService.getUnitId() ,quotationFillRequest.getUserId()!=null ? quotationFillRequest.getUserId() : sessionService.getUserId());
						
						detail.addQuotationFillAccessoryList(quotationFillAccessory);
					} else {
						/*
						 * QuotationFillAccessory quotationFillAccessory=new
						 * QuotationFillAccessory(qrAccess.getName(), qrAccess.getVendorDetails());
						 * sessionService.setEntityDefaults(quotationFillAccessory);
						 * detail.addQuotationFillAccessoryList(quotationFillAccessory);
						 */

						// start code for resolved HIS-9538 Change mode of the quotation fill in clears
						// the second line accessories details
						QuotationRequestAccessoriesMapper quotationRequestAccessoriesMapper = new QuotationRequestAccessoriesMapper(
								qrAccess.getDetails(), qrAccess.getName());
						quotationRequestAccessoriesMapper
								.setQuotationRequestDetail(new QuotationRequestDetail(item.getQrDetailId()));
						quotationRequestAccessoriesMapper.setCreateDefaults(quotationFillRequest.getOrganizationId()!=null ? quotationFillRequest.getOrganizationId() : sessionService.getOrganizationId(),quotationFillRequest.getUnitId() !=null ? quotationFillRequest.getUnitId() :sessionService.getUnitId() ,quotationFillRequest.getUserId()!=null ? quotationFillRequest.getUserId() : sessionService.getUserId());
						
						quotationFillDao.saveQuotationRequestAccessoriesMapper(quotationRequestAccessoriesMapper);

						QuotationFillAccessory quotationFillAccessory = new QuotationFillAccessory(qrAccess.getName(),
								new QuotationRequestAccessoriesMapper(quotationRequestAccessoriesMapper.getId()),
								qrAccess.getVendorDetails());
						quotationFillAccessory.setCreateDefaults(quotationFillRequest.getOrganizationId()!=null ? quotationFillRequest.getOrganizationId() : sessionService.getOrganizationId(),quotationFillRequest.getUnitId() !=null ? quotationFillRequest.getUnitId() :sessionService.getUnitId() ,quotationFillRequest.getUserId()!=null ? quotationFillRequest.getUserId() : sessionService.getUserId());
						
						detail.addQuotationFillAccessoryList(quotationFillAccessory);
						// end code for resolved HIS-9538 Change mode of the quotation fill in clears
						// the second line accessories details
					}
			}
			detail.setCreateDefaults(quotationFillRequest.getOrganizationId()!=null ? quotationFillRequest.getOrganizationId() : sessionService.getOrganizationId(),quotationFillRequest.getUnitId() !=null ? quotationFillRequest.getUnitId() :sessionService.getUnitId() ,quotationFillRequest.getUserId()!=null ? quotationFillRequest.getUserId() : sessionService.getUserId());
			

		}

		List<Integer> qrDetailId = new ArrayList();
		List<QuotationRequestDetail> quotationRequestDetails = new ArrayList();
		for (QuotationFillDetailRequestDto item : quotationFillRequest.getItems()) {

			qrDetailId.add(item.getQrDetailId());
		}
		if (qrDetailId.size() > 0) {
			quotationRequestDetails = quotationFillDao.getQuotationRequestDetail(quotationFillRequest.getQrId(),
					qrDetailId);
		}
		for (QuotationRequestDetail r : quotationRequestDetails) {
			r.setIsQuotationFillCancel(true);
			quotationFillDao.updateQuotationReqDetail(r);
		}

		quotationFillDao.saveQuotationFill(quotationFill);
		CommonResponse response = new CommonResponse();
		response.setId(quotationFill.getId().longValue());
		response.setMessage("Quotation Fill saved successfully");
		return response;

	}

	@Override
	@Transactional
	public Object updateQuotationFill(QuotationFillRequest quotationFillRequest) {

		QuotationFill quotationFill = quotationFillDao.getQuotationFill(quotationFillRequest.getId());

		quotationFill.updateQuotationFill(
				quotationFillRequest.getRemark() != null ? quotationFillRequest.getRemark() : quotationFill.getRemark(),
				quotationFillRequest.getRefNumber() != null ? quotationFillRequest.getRefNumber()
						: quotationFill.getRefNumber(),
				quotationFillRequest.getRefDate() != null ? quotationFillRequest.getRefDate()
						: quotationFill.getRefDate());

		for (QuotationFillDetail detail : quotationFill.getQuotationFillDetailList()) {
			Integer id = null;
			for (QuotationFillDetailRequestDto item : quotationFillRequest.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					detail.updateQuotationFillDetail(
							item.getVendorCop() != null ? item.getVendorCop() : detail.getVendorCop(),
							item.getVendorExpectedPayment() != null ? item.getVendorExpectedPayment()
									: detail.getExpectedPayment(),
							item.getVendorExpectedSupport() != null ? item.getVendorExpectedSupport()
									: detail.getExpectedSupportTerm(),
							item.getVendorTechnicalSpecification() != null ? item.getVendorTechnicalSpecification()
									: detail.getExpectedTechnicalSpecification(),
							item.getVendorWarrantyPeriod() != null ? item.getVendorWarrantyPeriod()
									: detail.getVendorWarrantyPeriod(),
							item.getRemark() != null ? item.getRemark() : detail.getRemark());

					for (QuotationFillAccessory qrAccess : detail.getQuotationFillAccessoryList()) {
						Integer qrid = null;
						for (QuotationFillAccessRequest qrAccessDto : item.getAccessoriesDetails()) {
							
							Integer mapperId= quotationFillDao.getMapperId(qrAccessDto.getId());
							quotationFillDao.updateDetailscolumn(qrAccessDto.getDetails(),mapperId);
							
							if (qrAccessDto.getId() != null && qrAccessDto.getId().equals(qrAccess.getId())) {
								qrid = qrAccessDto.getId();
								qrAccess.updateQuotationFillAccessory(
										qrAccessDto.getName() != null ? qrAccessDto.getName() : qrAccess.getName(),
										qrAccessDto.getVendorDetails() != null ? qrAccessDto.getVendorDetails()
												: qrAccess.getVendorDetail());

							}
						}
						if (qrid == null) {
							qrAccess.setIsDeleted(true);
						}

						qrAccess.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
						qrAccess.setUpdatedByUser(quotationFillRequest.getUserId());
					}

					for (QuotationFillAccessRequest qrAccessDto : item.getAccessoriesDetails()) {
						if (qrAccessDto.getId() == null) {
							QuotationFillAccessory quotationFillAccessory = new QuotationFillAccessory(
									qrAccessDto.getName(), qrAccessDto.getVendorDetails());
							detail.addQuotationFillAccessoryList(quotationFillAccessory);
						}
					}
				}
			}

			if (id == null) {
				detail.setIsDeleted(true);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
			detail.setUpdatedByUser(quotationFillRequest.getUserId());

		}
		for (QuotationFillDetailRequestDto item : quotationFillRequest.getItems()) {
			if (item.getId() == null) {

				QuotationFillDetail quotationFillDetail = new QuotationFillDetail(new Status(item.getStatusId()),
						item.getVendorCop(), item.getVendorExpectedPayment(), item.getVendorExpectedSupport(),
						item.getVendorTechnicalSpecification(), item.getVendorWarrantyPeriod(), item.getRemark(),
						new QuotationRequestDetail(item.getQrDetailId()),
						new QuotationVendorDetail(item.getVqrDetailId()));
				quotationFillDetail.setCreateDefaults(quotationFillRequest.getOrganizationId()!=null ? quotationFillRequest.getOrganizationId() : sessionService.getOrganizationId(),quotationFillRequest.getUnitId() !=null ? quotationFillRequest.getUnitId() :sessionService.getUnitId() ,quotationFillRequest.getUserId()!=null ? quotationFillRequest.getUserId() : sessionService.getUserId());
				QuotationFillDetail detail = quotationFill.addQuotationFillDetailList(quotationFillDetail);

				if (item.getAccessoriesDetails() != null) {
					for (QuotationFillAccessRequest qrAccess : item.getAccessoriesDetails())
						if (qrAccess.getQrAccessMapperId() != null) {
							QuotationFillAccessory qfa=new QuotationFillAccessory(qrAccess.getName(),
									new QuotationRequestAccessoriesMapper(qrAccess.getQrAccessMapperId()),
									qrAccess.getVendorDetails());
							qfa.setCreateDefaults(quotationFillRequest.getOrganizationId()!=null ? quotationFillRequest.getOrganizationId() : sessionService.getOrganizationId(),quotationFillRequest.getUnitId() !=null ? quotationFillRequest.getUnitId() :sessionService.getUnitId() ,quotationFillRequest.getUserId()!=null ? quotationFillRequest.getUserId() : sessionService.getUserId());
							
							detail.addQuotationFillAccessoryList(qfa);
						} else {

							QuotationFillAccessory qu=new QuotationFillAccessory(qrAccess.getName(), qrAccess.getVendorDetails());
							qu.setCreateDefaults(quotationFillRequest.getOrganizationId()!=null ? quotationFillRequest.getOrganizationId() : sessionService.getOrganizationId(),quotationFillRequest.getUnitId() !=null ? quotationFillRequest.getUnitId() :sessionService.getUnitId() ,quotationFillRequest.getUserId()!=null ? quotationFillRequest.getUserId() : sessionService.getUserId());
							
							detail.addQuotationFillAccessoryList(qu);
						}
				}
				detail.setCreateDefaults(quotationFillRequest.getOrganizationId()!=null ? quotationFillRequest.getOrganizationId() : sessionService.getOrganizationId(),quotationFillRequest.getUnitId() !=null ? quotationFillRequest.getUnitId() :sessionService.getUnitId() ,quotationFillRequest.getUserId()!=null ? quotationFillRequest.getUserId() : sessionService.getUserId());
				
			}
		}

		quotationFill.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
		quotationFill.setUpdatedByUser(quotationFillRequest.getUserId());

		quotationFillDao.updateQuotationFill(quotationFill);

		CommonResponse response = new CommonResponse();
		response.setMessage("Quotation Fill updated successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getQuotationFill(Integer id) {

		QuotationFill quotationfill = quotationFillDao.getQuotationFill(id);

		QuotationFillResponse qFillResponse = new QuotationFillResponse(quotationfill.getId(),
				quotationfill.getStatus().getId(), quotationfill.getStatus().getStatus(),
				quotationfill.getAssetType().getId(), quotationfill.getAssetType().getType(),
				quotationfill.getStore().getStoreId(), quotationfill.getStore().getStoreName(),
				quotationfill.getApprovalStatus().getId(), quotationfill.getApprovalStatus().getStatus(),
				quotationfill.getAddedBy().getUserName(), quotationfill.getQuotationRequest().getId(),
				quotationfill.getQuotationRequest().getQrNo(), quotationfill.getQuotationRequest().getAddedDate(),
				quotationfill.getQuotationVendor().getId(), quotationfill.getQuotationVendor().getVqNumber(),
				quotationfill.getQuotationVendor().getAddedDate(), quotationfill.getRefNumber(),
				quotationfill.getRefDate(), quotationfill.getRemark(), quotationfill.getVendor().getId(),
				quotationfill.getVendor().getName());

		for (QuotationFillDetail detail : quotationfill.getQuotationFillDetailList()) {
			if (detail.getIsActive() != false && detail.getIsDeleted() != true) {
				qFillResponse.addItems(new QuotationFillDetailResponse(detail.getId(),
						detail.getQuotationRequestDetail().getItem().getId(),
						detail.getQuotationRequestDetail().getItem().getCode(),
						detail.getQuotationRequestDetail().getItem().getName(),
						detail.getQuotationRequestDetail().getUomType() != null
								? detail.getQuotationRequestDetail().getUomType().getId()
								: null,
						detail.getQuotationRequestDetail().getUomType() != null
								? detail.getQuotationRequestDetail().getUomType().getType()
								: null,
						detail.getQuotationRequestDetail().getUomUnit() != null
								? detail.getQuotationRequestDetail().getUomUnit().getId()
								: null,
						detail.getQuotationRequestDetail().getUomUnit() != null
								? detail.getQuotationRequestDetail().getUomUnit().getUnits()
								: null,
						detail.getQuotationRequestDetail().getQuantity(), detail.getStatus().getId(),
						detail.getVendorCop(), detail.getQuotationRequestDetail().getExpectedPayment(),
						detail.getQuotationRequestDetail().getExpectedSupportTerm(),
						detail.getQuotationRequestDetail().getExpectedTechnicalSpecification(),
						detail.getQuotationRequestDetail().getWarrantyPeriod(), detail.getExpectedPayment(),
						detail.getExpectedSupportTerm(), detail.getExpectedTechnicalSpecification(),
						detail.getVendorWarrantyPeriod(), detail.getRemark(), getExpectedAccessories(detail)));
			}
		}

		return qFillResponse;
	}

	@Transactional
	private List<QuotationFillAccessRequest> getExpectedAccessories(QuotationFillDetail detail) {
		List<QuotationFillAccessory> qFillAccessoryList = detail.getQuotationFillAccessoryList();
		List<QuotationFillAccessRequest> qFillAccesoriesRequestDtoList = new ArrayList<QuotationFillAccessRequest>();
		for (QuotationFillAccessory qFillAccess : qFillAccessoryList) {
			if (qFillAccess.getIsActive() != false && qFillAccess.getIsDeleted() != true) {
				QuotationFillAccessRequest qrAccessMapperDto = new QuotationFillAccessRequest(qFillAccess.getId(),
						qFillAccess.getName() != null ? qFillAccess.getName(): (qFillAccess.getQrAccessories() != null ? qFillAccess.getQrAccessories().getName()
								: qFillAccess.getName()),
						qFillAccess.getQrAccessories() != null ? qFillAccess.getQrAccessories().getAccessoriesDetail()
								: null,
						qFillAccess.getVendorDetail());
				qFillAccesoriesRequestDtoList.add(qrAccessMapperDto);
			}
		}
		return qFillAccesoriesRequestDtoList;

	}

	@Override
	@Transactional
	public Object approveQuotationFill(QuotationFillRequest quotationFillRequest) {

		for (Integer id : quotationFillRequest.getQuotationFillId()) {

			QuotationFill quotationFill = quotationFillDao.getQuotationFill(id);

			quotationFill.setStatus(new Status(quotationFillRequest.getStatusId()));
			quotationFill.setApprovalStatus(new Status(quotationFillRequest.getApprovalStatusId()));

			for (QuotationFillDetail detail : quotationFill.getQuotationFillDetailList()) {
				detail.setStatus(new Status(quotationFillRequest.getStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
				detail.setUpdatedByUser(quotationFillRequest.getUserId());
			}

			// update requestforTechnicalApproval flag = true
			QuotationRequest quotationRequest = quotationFill.getQuotationRequest();
			if (quotationRequest.getAssetType().getId().equals(Constants.fixedAssetId)) {
				if ((quotationRequest.getSendTechnicalApproval().equals(true))
						&& (quotationRequest.getAssetType().getId().equals(Constants.fixedAssetId))) {
					TechnicalApproval technicalApproval = new TechnicalApproval(
							new AssetType(quotationRequest.getAssetType().getId()), new Status(2),
							new Store(quotationRequest.getStore().getStoreId()),
							new QuotationRequest(quotationRequest.getId()), quotationRequest.getRemark());
					//sessionService.setEntityDefaults(technicalApproval);
					technicalApproval.setAddedByUser(quotationFillRequest.getUserId());
					technicalApproval.setUpdatedByUser(quotationFillRequest.getUserId());
					technicalApproval.setAddedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
					technicalApproval.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
					technicalApproval.setOrganizationId(quotationFillRequest.getOrganizationId());
					technicalApproval.setUnitId(quotationFillRequest.getUnitId());


					for (QuotationFillDetail qfDetail : quotationFill.getQuotationFillDetailList()) {
						TechnicalApprovalDetail technicalApprovalDetail = new TechnicalApprovalDetail(
								new QuotationRequestDetail(qfDetail.getQuotationRequestDetail().getId()),
								new Status(2));
						//sessionService.setEntityDefaults(technicalApprovalDetail);
						technicalApprovalDetail.setAddedByUser(quotationFillRequest.getUserId());
						technicalApprovalDetail.setUpdatedByUser(quotationFillRequest.getUserId());
						technicalApprovalDetail.setAddedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
						technicalApprovalDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
						technicalApprovalDetail.setOrganizationId(quotationFillRequest.getOrganizationId());
						technicalApprovalDetail.setUnitId(quotationFillRequest.getUnitId());
						TechnicalApprovalDetail techApprovalDetail = technicalApproval
								.addTechnicalApprovalDetailList(technicalApprovalDetail);
					//	sessionService.setEntityDefaults(techApprovalDetail);
						techApprovalDetail.setAddedByUser(quotationFillRequest.getUserId());
						techApprovalDetail.setUpdatedByUser(quotationFillRequest.getUserId());
						techApprovalDetail.setAddedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
						techApprovalDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
						techApprovalDetail.setOrganizationId(quotationFillRequest.getOrganizationId());
						techApprovalDetail.setUnitId(quotationFillRequest.getUnitId());
						TechnicalApprovalDetailMapper technicalApprovalDetailMapper = new TechnicalApprovalDetailMapper(
								"remark", false, false, false, false, new QuotationFill(quotationFill.getId()),
								new Status(2), new Vendor(quotationFill.getVendor().getId()));
						//sessionService.setEntityDefaults(technicalApprovalDetailMapper);
						technicalApprovalDetailMapper.setAddedByUser(quotationFillRequest.getUserId());
						technicalApprovalDetailMapper.setUpdatedByUser(quotationFillRequest.getUserId());
						technicalApprovalDetailMapper.setAddedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
						technicalApprovalDetailMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
						technicalApprovalDetailMapper.setOrganizationId(quotationFillRequest.getOrganizationId());
						technicalApprovalDetailMapper.setUnitId(quotationFillRequest.getUnitId());
							techApprovalDetail.addTechnicalApprovalDetailMapperList(technicalApprovalDetailMapper);

					}
					technicalApprovalDao.saveTechnicalApproval(technicalApproval);
				} else if ((quotationRequest.getSendTechnicalApproval().equals(false))
						&& (quotationRequest.getAssetType().getId().equals(Constants.fixedAssetId))) {
					quotationRequest.setRequestForTechnicalApproval(true);

				}
			} else if (quotationRequest.getAssetType().getId().equals(Constants.consumableId)) {
				/*
				 * VendorNegotiation vendorNegotiation = new VendorNegotiation();
				 * vendorNegotiation.setAssetType(new
				 * AssetType(quotationRequest.getAssetType().getId()));
				 * vendorNegotiation.setQuotationFil(quotationFill);
				 * vendorNegotiation.setStatus(new Status(2)); vendorNegotiation.setStore(new
				 * Store(quotationRequest.getStore().getStoreId()));
				 * vendorNegotiation.setVendor(new Vendor(quotationFill.getVendor().getId()));
				 */
				VendorNegotiation vendorNegotiation = new VendorNegotiation(
						new AssetType(quotationFill.getAssetType().getId()), new QuotationFill(quotationFill.getId()),
						new Status(2), new Store(quotationFill.getStore().getStoreId()),
						new Vendor(quotationFill.getVendor().getId()));
				//sessionService.setEntityDefaults(vendorNegotiation);
				vendorNegotiation.setAddedByUser(quotationFillRequest.getUserId());
				vendorNegotiation.setUpdatedByUser(quotationFillRequest.getUserId());
				vendorNegotiation.setAddedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
				vendorNegotiation.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
				vendorNegotiation.setOrganizationId(quotationFillRequest.getOrganizationId());
				vendorNegotiation.setUnitId(quotationFillRequest.getUnitId());
				


				List<VendorNegotiationDetail> vendorNegotiationDetails = new ArrayList<VendorNegotiationDetail>();

				for (QuotationFillDetail detail : quotationFill.getQuotationFillDetailList()) {
					VendorNegotiationDetail vendorNegotiationDetail = new VendorNegotiationDetail(
							new QuotationFillDetail(detail.getId()));
					vendorNegotiationDetail.setAddedByUser(quotationFillRequest.getUserId());
					vendorNegotiationDetail.setUpdatedByUser(quotationFillRequest.getUserId());
					vendorNegotiationDetail.setAddedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
					vendorNegotiationDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
					vendorNegotiationDetail.setOrganizationId(quotationFillRequest.getOrganizationId());
					vendorNegotiationDetail.setUnitId(quotationFillRequest.getUnitId());
					//sessionService.setEntityDefaults(vendorNegotiationDetail);
					vendorNegotiation.addVendorNegociationDetailList(vendorNegotiationDetail);
				}
				technicalApprovalDao.saveVendorNegotiation(vendorNegotiation);

				/*
				 * for(QuotationFillDetail qfDetail :
				 * quotationFill.getQuotationFillDetailList()) { VendorNegotiationDetail
				 * vendorNegotiationDetail = new VendorNegotiationDetail();
				 * vendorNegotiationDetail.setQuotationFillDetail(qfDetail);
				 * vendorNegotiationDetail.setVendorNegociation(vendorNegotiation);
				 * sessionService.setEntityDefaults(vendorNegotiationDetail);
				 * 
				 * VendorNegotiationMapper detailMapper=new VendorNegotiationMapper();
				 * detailMapper.setVendorNegociationDetail(vendorNegotiationDetail);
				 * detailMapper.setStatus(new Status(2));
				 * detailMapper.setCop(qfDetail.getVendorCop());
				 * detailMapper.setRemarks(qfDetail.getRemark());
				 * detailMapper.setVendorwarrentyPeriod(qfDetail.getVendorWarrantyPeriod());
				 * detailMapper.setVendorSupportTerms(qfDetail.getExpectedSupportTerm());
				 * detailMapper.setVendorTechnicalSpecification(qfDetail.
				 * getExpectedTechnicalSpecification());
				 * detailMapper.setVendorExpectedPayment(qfDetail.getExpectedPayment());
				 * vendorNegotiationDetail.addVendorNegociationMapperList(detailMapper);
				 * sessionService.setEntityDefaults(detailMapper);
				 * 
				 * vendorNegotiationDetails.add(vendorNegotiationDetail); }
				 * vendorNegotiation.setVendorNegociationDetailsList(vendorNegotiationDetails);
				 * IVendorNegotiationDao.saveVendorNegotiation(vendorNegotiation);
				 */
			}

			quotationFill.setQuotationRequest(quotationRequest);

			quotationFill.setApprovedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
			quotationFill.setApprovedBy(new User(quotationFillRequest.getUserId()!=null ? quotationFillRequest.getUserId() : sessionService.getUserId()));

			quotationFill.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
			quotationFill.setUpdatedByUser(quotationFillRequest.getUserId());
			quotationFillDao.updateQuotationFill(quotationFill);

		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Quotation Fill approved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object cancelQuotationFill(QuotationFillRequest quotationFillRequest) {

		QuotationFill quotationFill = quotationFillDao.getQuotationFill(quotationFillRequest.getId());

		for (QuotationFillDetail detail : quotationFill.getQuotationFillDetailList()) {
			detail.setStatus(new Status(quotationFillRequest.getStatusId()));

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
			detail.setUpdatedByUser(quotationFillRequest.getUserId());
		}
		quotationFill.setCancelReason(new CancelReason(quotationFillRequest.getCancelReasonId()));
		quotationFill.setCancelNote(quotationFillRequest.getCancelNote());

		quotationFill.setStatus(new Status(quotationFillRequest.getStatusId()));
		quotationFill.setApprovalStatus(new Status(quotationFillRequest.getApprovalStatusId()));

		quotationFill.setUpdatedDate(TimeZoneComponent.getDateByzone(quotationFill.getUnitId()));
		quotationFill.setUpdatedByUser(quotationFillRequest.getUserId());

		quotationFillDao.updateQuotationFill(quotationFill);

		CommonResponse response = new CommonResponse();
		response.setMessage("Quotation Fill cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public Object getQuotationVendorSearchList(QuotationFillRequest quotationFillRequest) {

		List<QuotationVendorSearchResponse> quotationVendorSearchResponseList = new ArrayList<QuotationVendorSearchResponse>();

		boolean getVqr = quotationFillRequest.getGetVqr() == null ? true : false;
		if (getVqr) {
			quotationFillRequest.setToDate((new DateTime(quotationFillRequest.getToDate())).plusDays(1).toDate());
			List<Integer> statusIds = new ArrayList<Integer>();
			statusIds.add(com.param.common.constants.Status.Approved.getStatus());
			statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
			statusIds.add(com.param.common.constants.Status.PendingApproval.getStatus());

			for (QuotationVendor quotationVendor : quotationFillDao.getQuotationVendorSearchList(quotationFillRequest,
					statusIds)) {

				QuotationVendorSearchResponse quotationVendorSearch = new QuotationVendorSearchResponse(
						quotationVendor.getId(), quotationVendor.getAddedDate(), quotationVendor.getVqNumber(),
						quotationVendor.getQuotationRequest().getId(),
						quotationVendor.getQuotationRequest().getAddedDate(),
						quotationVendor.getQuotationRequest().getQrNo(), quotationVendor.getStore().getStoreName(),
						quotationVendor.getAddedBy().getUserName(),
						quotationVendor.getApprovedBy() != null ? quotationVendor.getApprovedBy().getUserName() : null);

				for (QuotationVendorDetail detail : quotationVendor.getQuotationVendorDetailsList()) {
					// detail.getQrDetail()
					if (detail.getIsActive() != false && detail.getIsDeleted() != true) {

						quotationVendorSearch.addItems(new QuotationVendorSearchDetailResponse(detail.getId(),
								detail.getQrDetail().getItem().getId(), detail.getQrDetail().getItem().getCode(),
								detail.getQrDetail().getItem().getName(),
								detail.getQrDetail().getUomType() != null ? detail.getQrDetail().getUomType().getId()
										: null,
								detail.getQrDetail().getUomType() != null ? detail.getQrDetail().getUomType().getType()
										: null,
								detail.getQrDetail().getUomUnit() != null ? detail.getQrDetail().getUomUnit().getId()
										: null,
								detail.getQrDetail().getUomUnit() != null ? detail.getQrDetail().getUomUnit().getUnits()
										: null,
								detail.getQrDetail().getQuantity()));
					}
				}
				quotationVendorSearchResponseList.add(quotationVendorSearch);
			}
		}

		else if (!getVqr) {
			QuotationVendor quotationVendor = quotationFillDao.getQuotationVendor(quotationFillRequest.getVqrId());
			QuotationVendorSearchResponse quotationVendorSearch = new QuotationVendorSearchResponse(
					quotationVendor.getId(), quotationVendor.getAddedDate(), quotationVendor.getVqNumber(),
					quotationVendor.getQuotationRequest().getId(), quotationVendor.getQuotationRequest().getAddedDate(),
					quotationVendor.getQuotationRequest().getQrNo(), quotationVendor.getStore().getStoreName(),
					quotationVendor.getAddedBy().getUserName(),
					quotationVendor.getApprovedBy() != null ? quotationVendor.getApprovedBy().getUserName() : null);
			List<QuotationVendorDetail> qvDetailList = quotationVendor.getQuotationVendorDetailsList();

			for (QuotationFillDetail qfDetail : quotationFillDao.getQuotationFillDetail(quotationFillRequest.getId())) {
				//qvDetailList.remove(qfDetail.getQuotationVendorDetail());
			}

			for (QuotationVendorDetail detail : quotationVendor.getQuotationVendorDetailsList()) {
				if (detail.getIsActive() != false && detail.getIsDeleted() != true) {
					quotationVendorSearch.addItems(new QuotationVendorSearchDetailResponse(detail.getId(),
							detail.getQrDetail().getItem().getId(), detail.getQrDetail().getItem().getCode(),
							detail.getQrDetail().getItem().getName(),
							detail.getQrDetail().getUomType() != null ? detail.getQrDetail().getUomType().getId()
									: null,
							detail.getQrDetail().getUomType() != null ? detail.getQrDetail().getUomType().getType()
									: null,
							detail.getQrDetail().getUomUnit() != null ? detail.getQrDetail().getUomUnit().getId()
									: null,
							detail.getQrDetail().getUomUnit() != null ? detail.getQrDetail().getUomUnit().getUnits()
									: null,
							detail.getQrDetail().getQuantity()));
				}
			}
			quotationVendorSearchResponseList.add(quotationVendorSearch);
		}

		return quotationVendorSearchResponseList;
	}

	@Override
	@Transactional
	public Object getQuotationVendorDetails(List<QuotationFillRequest> quotationFillRequestList) {

		List<QuotationVendorSearchResponse> response = new ArrayList<QuotationVendorSearchResponse>();
		for (QuotationFillRequest quotationFillRequest : quotationFillRequestList) {

			QuotationVendor quotationVendor = quotationFillDao.getQuotationVendor(quotationFillRequest.getVqrId());

			QuotationVendorSearchResponse quotationVendorSearchResponse = new QuotationVendorSearchResponse(
					quotationVendor.getId(), quotationVendor.getAddedDate(), quotationVendor.getVqNumber(),
					quotationVendor.getQuotationRequest().getId(), quotationVendor.getQuotationRequest().getAddedDate(),
					quotationVendor.getQuotationRequest().getQrNo(), quotationVendor.getStore().getStoreName(),
					quotationVendor.getAddedBy().getUserName(),
					quotationVendor.getApprovedBy() != null ? quotationVendor.getApprovedBy().getUserName() : null);

			for (Integer itemId : quotationFillRequest.getItemIds()) {

				QuotationVendorDetail detail = quotationFillDao.getQuotationVendorDetail(itemId,
						quotationVendor.getId());
				// detail.getQrDetail().getId();
				List<QuotationFillAccessRequest> accessoriesDetails = getAccessoriesDetails(detail);

				quotationVendorSearchResponse.addItems(new QuotationVendorSearchDetailResponse(detail.getId(),
						detail.getQrDetail() != null ? detail.getQrDetail().getId() : null,
						detail.getQrDetail() != null ? detail.getQrDetail().getItem().getId() : null,
						detail.getQrDetail() != null ? detail.getQrDetail().getItem().getCode() : null,
						detail.getQrDetail() != null ? detail.getQrDetail().getItem().getName() : null,
						detail.getQrDetail().getUomType() != null ? detail.getQrDetail().getUomType().getId() : null,
						detail.getQrDetail().getUomType() != null ? detail.getQrDetail().getUomType().getType() : null,
						detail.getQrDetail().getUomUnit() != null ? detail.getQrDetail().getUomUnit().getId() : null,
						detail.getQrDetail().getUomUnit() != null ? detail.getQrDetail().getUomUnit().getUnits() : null,
						detail.getQrDetail() != null ? detail.getQrDetail().getQuantity() : null,
						detail.getQrDetail() != null ? detail.getQrDetail().getWarrantyPeriod() : null,
						detail.getQrDetail() != null ? detail.getQrDetail().getExpectedSupportTerm() : null,
						detail.getQrDetail() != null ? detail.getQrDetail().getExpectedPayment() : null,
						detail.getQrDetail() != null ? detail.getQrDetail().getExpectedTechnicalSpecification() : null,
						accessoriesDetails));
			}
			response.add(quotationVendorSearchResponse);
		}

		return response;
	}

	private List<QuotationFillAccessRequest> getAccessoriesDetails(QuotationVendorDetail detail) {
		Integer qrDetailId = detail.getQrDetail().getId();
		List<QuotationFillAccessRequest> quotationFillAccessList = new ArrayList<QuotationFillAccessRequest>();
		for (QuotationRequestAccessoriesMapper qrAccess : quotationFillDao
				.getQuotationRequestAccessoriesMapperList(qrDetailId)) {
			QuotationFillAccessRequest response = new QuotationFillAccessRequest(qrAccess.getId(), qrAccess.getName(),
					qrAccess.getAccessoriesDetail());
			quotationFillAccessList.add(response);
		}
		return quotationFillAccessList;
	}

}
