package com.param.global.service.global;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.common.constants.Constants;
import com.param.common.constants.Screen;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.FloorMasters;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.base.UomEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.PMSFrequency;
import com.param.entity.model.inventory.ReturnableGatepassDetail;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.master.Company;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.PharmacologicalClassification;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.StorageUnit;
import com.param.entity.model.master.StrengthUnit;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.TaxComponent;
import com.param.entity.model.master.Unit;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.Ved;
import com.param.entity.model.master.Vendor;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.PharamcyBillPayeeDetails;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.SaleDetailPayeeMapper;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.entity.model.procurement.PurchaseOrderDetail;
import com.param.entity.model.procurement.PurchaseOrderOtherChargesMapper;
import com.param.entity.model.procurement.PurchaseRequest;
import com.param.entity.model.procurement.PurchaseRequestDetail;
import com.param.entity.model.procurement.QuotationRequest;
import com.param.entity.model.procurement.QuotationRequestDetail;
import com.param.entity.model.procurement.QuotationVendor;
import com.param.entity.model.procurement.QuotationVendorDetail;
import com.param.entity.model.procurement.WorkOrderCreation;
import com.param.entity.model.procurement.WorkOrderCreationDetail;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.billing.dto.PharmacyBillCommonDto;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.common.VisitTypeConstants;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dao.items.IItemsDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.DoctorSpecialityMapperDto;
import com.param.global.dto.GRNQuantity;
import com.param.global.dto.GoodReceiptNoteDetailDto;
import com.param.global.dto.StoreStockRequestDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.global.AgainstGrnDetailResponse;
import com.param.global.dto.global.AgainstGrnRequest;
import com.param.global.dto.global.AgainstGrnResponse;
import com.param.global.dto.global.AgainstPOSearchResponse;
import com.param.global.dto.global.AgainstPurchaseOrder;
import com.param.global.dto.global.AgainstPurchaseRequest;
import com.param.global.dto.global.AgainstPurchaseRequestResponse;
import com.param.global.dto.global.AgainstQuotationRequest;
import com.param.global.dto.global.AgainstQuotationRequestResponse;
import com.param.global.dto.global.AgainstWORequest;
import com.param.global.dto.global.AssetCategory;
import com.param.global.dto.global.AssetType;
import com.param.global.dto.global.BatchProperty;
import com.param.global.dto.global.CancelReason;
import com.param.global.dto.global.Currency;
import com.param.global.dto.global.DateRequest;
import com.param.global.dto.global.DirectPurchaseRequestResponse;
import com.param.global.dto.global.Doctor;
import com.param.global.dto.global.FixedAssetType;
import com.param.global.dto.global.Floor;
import com.param.global.dto.global.FormulationType;
import com.param.global.dto.global.FormulationTypeAndROAList;
import com.param.global.dto.global.GRNAgainstApprovedPOQuantity;
import com.param.global.dto.global.InsuranceCompanyDetails;
import com.param.global.dto.global.ItemDropdownResponse;
import com.param.global.dto.global.ItemResponse;
import com.param.global.dto.global.ManufacturerResponseDto;
import com.param.global.dto.global.OtherChargesResponse;
import com.param.global.dto.global.POOtherCharges;
import com.param.global.dto.global.Patient;
import com.param.global.dto.global.PatientDetailRequest;
import com.param.global.dto.global.PatientDetailResponse;
import com.param.global.dto.global.PatientIssueDropdown;
import com.param.global.dto.global.PatientSearchDetails;
import com.param.global.dto.global.PoAccessoriesMapperGlobal;
import com.param.global.dto.global.ProductCategory;
import com.param.global.dto.global.PurchaseOrderDetailResponse;
import com.param.global.dto.global.PurchaseOrderHistory;
import com.param.global.dto.global.PurchaseOrderResponse;
import com.param.global.dto.global.PurchaseOrderStaged;
import com.param.global.dto.global.PurchaseOrderStagedDcDate;
import com.param.global.dto.global.PurchaseRequestDetailResponse;
import com.param.global.dto.global.PurchaseRequestSearchDto;
import com.param.global.dto.global.QuotationRequestDetailResponse;
import com.param.global.dto.global.QuotationRequestSearchDto;
import com.param.global.dto.global.RejectReason;
import com.param.global.dto.global.SaleType;
import com.param.global.dto.global.ScrapReason;
import com.param.global.dto.global.Status;
import com.param.global.dto.global.Store;
import com.param.global.dto.global.StoreIndentResponse;
import com.param.global.dto.global.StoreType;
import com.param.global.dto.global.StorewiseGenericMapperDropdown;
import com.param.global.dto.global.TariffQueryData;
import com.param.global.dto.global.TariffRequest;
import com.param.global.dto.global.TariffResponse;
import com.param.global.dto.global.TaxComponentDropdown;
import com.param.global.dto.global.TaxDropDown;
import com.param.global.dto.global.UomUnit;
import com.param.global.dto.global.User;
import com.param.global.dto.global.VedResponseList;
import com.param.global.dto.global.VendorResponseDto;
import com.param.global.dto.global.VisitDetails;
import com.param.global.dto.global.Ward;
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.dto.items.Quantities;
import com.param.global.dto.items.UomResponse;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.service.global.SessionService;
import com.param.service.global.model.SaleDetailPackageMapper;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
@Service("GlobalService")
public class GlobalService implements IGlobalService {
	final static Logger logger = Logger.getLogger(GlobalService.class);


	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IItemsDao ItemsDao;
	
	private Integer screenId = Screen.PatientIssueReturn.getScreen();
	
	
	@Autowired
	SessionService sessionService;

	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;

	@Override
	@Transactional
	public List<AssetType> getAssetTypeList(Integer unitId, Integer orgId) {
		List<AssetType> response = new ArrayList<AssetType>();

		for (com.param.entity.model.master.AssetType assetType : GlobalDao.getAssetTypeList(unitId, orgId)) {
			response.add(new AssetType(assetType.getId(), assetType.getType()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<AssetCategory> getAssetCategoryList(Integer assetTypeId, Integer unitId, Integer orgId) {
		List<AssetCategory> response = new ArrayList<AssetCategory>();

		for (com.param.entity.model.master.AssetCategory assetCategory : GlobalDao.getAssetCategoryList(assetTypeId,
				unitId, orgId)) {
			response.add(new AssetCategory(assetCategory.getId(), assetCategory.getCategory()));

		}
		return response;

	}

	@Override
	@Transactional
	public List<ProductCategory> getProductCategoryList(Integer assetCategoryId, Integer orgId) {
		List<ProductCategory> response = new ArrayList<ProductCategory>();

		for (com.param.entity.model.master.ProductCategory productCategory : GlobalDao
				.getProductCategoryList(assetCategoryId, orgId)) {
			response.add(new ProductCategory(productCategory.getId(), productCategory.getCategory()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<FormulationType> getFormulationTypeList(Integer orgId) {
		List<FormulationType> response = new ArrayList<FormulationType>();
		for (com.param.entity.model.master.FormulationType formulationType : GlobalDao.getFormulationTypeList(orgId)) {
			response.add(new FormulationType(formulationType.getId(), formulationType.getType()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<com.param.global.dto.global.Manufacturer> getManufacturersList(Integer orgId) {
		List<com.param.global.dto.global.Manufacturer> response = new ArrayList<com.param.global.dto.global.Manufacturer>();
		for (Manufacturer manufacturer : GlobalDao.getManufacturersList(orgId)) {
			response.add(new com.param.global.dto.global.Manufacturer(manufacturer.getId(), manufacturer.getName()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<UomUnit> getUomUnitsList(Integer orgId) {
		List<com.param.global.dto.global.UomUnit> response = new ArrayList<com.param.global.dto.global.UomUnit>();
		for (com.param.entity.model.master.UomUnit uomUnit : GlobalDao.getUomUnitsList(orgId)) {
			response.add(new com.param.global.dto.global.UomUnit(uomUnit.getId(), uomUnit.getUnits()));
		}
		return response;
	}

	@Override
	@Transactional
	public Currency getCurrency(Integer unitId) {

		com.param.entity.model.master.Currency currency = GlobalDao.getCurrency(unitId);

		if (currency != null) {
			Currency response = new Currency(currency.getCurrencyId(), currency.getCurrencyName(),
					currency.getCurrencySymbol());

			return response;
		} else {
			Currency response = new Currency(0, "Rupees", "₹");
			return response;
		}

	}

	@Override
	@Transactional
	public List<Status> getStatusList() {
		List<Status> response = new ArrayList<Status>();

		for (com.param.entity.model.master.Status status : GlobalDao.getStatusList()) {
			response.add(new Status(status.getId(), status.getStatus()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<VisitType> getVisitTypeList() {
		List<VisitType> response = new ArrayList<VisitType>();

		for (com.param.entity.model.master.VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.add(new VisitType(visitType.getVisitTypeId(), visitType.getVisitTypeName()));
		}
		return response;
	}

	@Override
	@Transactional
	public PatientIssueDropdown getPatientIssueDropdown(Integer orgId, Integer unitId, Integer userId) {
		PatientIssueDropdown response = new PatientIssueDropdown();

		for (com.param.entity.model.master.VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (WardMasters ward : GlobalDao.getWardList(unitId)) {
			response.addWard(new Ward(ward.getWardId(), ward.getWardName()));
		}

		for (FloorMasters floor : GlobalDao.getFloorList(unitId)) {
			response.addFloor(new Floor(floor.getFloorId(), floor.getFloorName()));
		}

		for (com.param.entity.model.master.Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		
		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
			screenStatus.getStatus().getStatus()));
		}

		return response;

	}

	@Override
	@Transactional
	public List<SaleType> getSaleTypeList() {
		List<SaleType> response = new ArrayList<SaleType>();

		for (com.param.entity.model.pharmacy.SaleType saleType : GlobalDao.getSaleTypeList()) {
			response.add(new SaleType(saleType.getId(), saleType.getType()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<RejectReason> getRejectReasonList(Integer unitId) {
		List<RejectReason> response = new ArrayList<RejectReason>();

		for (com.param.entity.model.master.RejectReason rejectReason : GlobalDao.getRejectReasonList(unitId)) {
			response.add(new RejectReason(rejectReason.getId(), rejectReason.getCode(), rejectReason.getReason()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<CancelReason> getCancelReasonList() {
		List<CancelReason> response = new ArrayList<CancelReason>();
		// List<ReasonMaster> list=GlobalDao.getreasonDropdownList(1);
		for (com.param.entity.model.master.CancelReason cancelReason : GlobalDao.getCancelReasonList()) {
			response.add(new CancelReason(cancelReason.getId(), cancelReason.getCode(), cancelReason.getReason()));
		}

		/*
		 * for (ReasonMaster cancelReason :
		 * GlobalDao.getCancelReasonDropdownList(orgId)) { response.add(new
		 * CancelReason(cancelReason.getReasonMasterId(), cancelReason.getReasonCode(),
		 * cancelReason.getReasonDesc())); }
		 */
		return response;
	}

	@Override
	@Transactional
	public List<Store> getUserStoreList(Integer orgId, Integer unitId, Integer userId) {
		List<Store> response = new ArrayList<Store>();

		for (com.param.entity.model.master.Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.add(new Store(store.getStoreId(), store.getStoreName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<StoreType> getStoreTypeList(Integer orgId) {
		List<StoreType> response = new ArrayList<StoreType>();

		for (com.param.entity.model.master.StoreType storeType : GlobalDao.getStoreTypeList(orgId)) {
			response.add(new StoreType(storeType.getId(), storeType.getType()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<StoreIndentResponse> getStoreIndentList(Integer fromStoreId, Integer toStoreId,
			DateRequest dateRequest) {
		List<StoreIndentResponse> response = new ArrayList<StoreIndentResponse>();
		dateRequest.setToDate((new DateTime(dateRequest.getToDate())).plusDays(1).toDate());

		if (dateRequest.getFromDate() == null) {
			dateRequest.setFromDate(TimeZoneComponent.getDateByzone(dateRequest.getUnitId()));
		}

		if (dateRequest.getAssetTypeId() == null || dateRequest.getAssetTypeId().equals(1)) {
			for (com.param.entity.model.inventory.StoreIndent indent : GlobalDao.getStoreIndentListWithDate(fromStoreId,
					toStoreId, dateRequest)) {
				StoreIndentResponse result = new StoreIndentResponse(indent.getId(), indent.getAddedDate(),
						indent.getIndentNo(), indent.getFromStore().getStoreName(), indent.getAddedBy().getUserName(),
						indent.getApprovedBy().getUserName(), indent.getApprovedDate());

				for (com.param.entity.model.inventory.StoreIndentDetail detail : GlobalDao
						.getStoreIndentDetailsList(indent.getId())) {

					if (detail.getPurchaseRequestDetail() == null) {
						Long storeStock = GlobalDao.getStoreStockByItem(indent.getFromStore().getStoreId(),
								detail.getItem().getId(), dateRequest.getUnitId());
						Long pendingQuantity = Util.ConvertQuantityByUom(storeStock,
								detail.getItem().getUnitOfMeasurementList(), detail.getUomType());

						result.addItems(
								new ItemResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(),
										detail.getItem().getName(), detail.getAuthorizedQuantity(), pendingQuantity));
					} else if (detail.getPurchaseRequestDetail() != null
							&& (detail.getPurchaseRequestDetail().getPurchaseRequest().getApprovalStatus().getId()
									.equals(com.param.common.constants.Status.Cancelled.getStatus())
									|| detail.getPurchaseRequestDetail().getPurchaseRequest().getApprovalStatus()
											.getId().equals(com.param.common.constants.Status.Rejected.getStatus()))) {
						Long storeStock = GlobalDao.getStoreStockByItem(indent.getFromStore().getStoreId(),
								detail.getItem().getId(), dateRequest.getUnitId());
						Long pendingQuantity = Util.ConvertQuantityByUom(storeStock,
								detail.getItem().getUnitOfMeasurementList(), detail.getUomType());

						result.addItems(
								new ItemResponse(detail.getId(), detail.getItem().getId(), detail.getItem().getCode(),
										detail.getItem().getName(), detail.getAuthorizedQuantity(), pendingQuantity));
					}

				}

				if (result.getItems() != null && !result.getItems().isEmpty()) {
					response.add(result);
				}
			}
		} else {
			for (com.param.entity.model.asset.AssetIndent indent : GlobalDao.getAssetIndentListWithDate(fromStoreId,
					toStoreId, dateRequest)) {
				StoreIndentResponse result = new StoreIndentResponse(indent.getId(), indent.getAddedDate(),
						indent.getIndentNo(), indent.getFromStore().getStoreName(), indent.getAddedBy().getUserName(),
						indent.getApprovedBy().getUserName(), indent.getAppprovedOn());

				for (com.param.entity.model.asset.AssetIndentDetail detail : GlobalDao
						.getAssetIndentDetailsList(indent.getId())) {

					if (detail.getPurchaseRequestDetail() == null) {
						Long storeStock = GlobalDao.getStoreStockByItem(indent.getFromStore().getStoreId(),
								detail.getItem().getId(), dateRequest.getUnitId());
						/*
						 * Long pendingQuantity = Util.ConvertQuantityByUom(storeStock,
						 * detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
						 */
						Long pendingQuantity = detail.getPendingQuantity() != null ? detail.getPendingQuantity() : 0l;
						result.addItems(new ItemResponse(detail.getId(), detail.getItem().getId(),
								detail.getItem().getCode(), detail.getItem().getName(),
								detail.getApprovedIndentQuantity(), pendingQuantity));
					} else if (detail.getPurchaseRequestDetail() != null) {
						Long storeStock = GlobalDao.getStoreStockByItem(indent.getFromStore().getStoreId(),
								detail.getItem().getId(), dateRequest.getUnitId());
						/*
						 * Long pendingQuantity = Util.ConvertQuantityByUom(storeStock,
						 * detail.getItem().getUnitOfMeasurementList(), detail.getUomType());
						 */
						Long pendingQuantity = detail.getPendingQuantity() != null ? detail.getPendingQuantity() : 0l;
						result.addItems(new ItemResponse(detail.getId(), detail.getItem().getId(),
								detail.getItem().getCode(), detail.getItem().getName(),
								detail.getApprovedIndentQuantity(), pendingQuantity));
					}

				}

				if (result.getItems() != null && !result.getItems().isEmpty()) {
					response.add(result);
				}
			}
		}

		return response;
	}

	@Override
	@Transactional
	public PatientDetailResponse patientIndentPatientSearch(PatientDetailRequest patientDetailsSearch) {
		PatientDetailResponse patientDetails = new PatientDetailResponse();
		PatientRegistration patient = GlobalDao.getPatient(patientDetailsSearch.getPatientId());
		String patientName = patient.getPrefix().getPrefixCode() + " " + patient.getFirstName() + " "
				+ patient.getMiddleName() + " " + patient.getLastName();
		Integer age = Util.getAgeByBirthDate(patient.getDob().toLocalDateTime().toLocalDate());
		String patientDetail = patientName + " | " + age + " | " + patient.getGender().getGenderCode();
		patientDetails = new PatientDetailResponse(patient.getPatientId(), patientName, patientDetail,
				patient.getUhidNumber());
		VisitType visitType = GlobalDao.getVisitType(patientDetailsSearch.getVisitType());
		if (visitType != null) {
			patientDetails.setVisitTypeId(visitType.getVisitTypeId());
			patientDetails.setVisitType(visitType.getVisitTypeCode());
		}
		List<VisitDetails> visitDetails = new ArrayList<VisitDetails>();

		if (patientDetailsSearch.getVisitType() == 1) {
			for (Encounter encounter : GlobalDao.encounterPatient(patientDetailsSearch,
					patientDetailsSearch.getUnitId())) {
				VisitDetails visitDetail = new VisitDetails(
						encounter.getDoctor() != null ? encounter.getDoctor().getDoctorId() : 0,
						encounter.getEncounterNumber());
				visitDetail.setEncounterId(encounter.getEncounterId());
				visitDetail.setAddedDate(encounter.getUpdatedDate());
				visitDetails.add(visitDetail);
			}
		}

		if (patientDetailsSearch.getVisitType() == 2) {
			Admission admission = GlobalDao.admissionPatient(patientDetailsSearch, patientDetailsSearch.getUnitId());

			if (admission != null) {
				VisitDetails visitDetail = new VisitDetails(admission.getDoctor().getDoctorId(),
						admission.getAdmissionNumber());
				visitDetail.setAdmissionId(admission.getAdmissionId());
				visitDetail.setAddedDate(admission.getUpdatedDate());
				visitDetails.add(visitDetail);
				if (admission.getAdmissionDetail() != null) {
					patientDetails.setWardId(admission.getAdmissionDetail().get(0).getWardMaster().getWardId());
					patientDetails.setWardName(admission.getAdmissionDetail().get(0).getWardMaster().getWardName());
					patientDetails.setBedId(admission.getAdmissionDetail().get(0).getBedMaster().getBedId());
					patientDetails.setBedNo(admission.getAdmissionDetail().get(0).getBedMaster().getBedNumber());
				}
			}
		}

		patientDetails.setVisitDetails(visitDetails);

		return patientDetails;

	}

	@Override
	@Transactional
	public PatientDetailResponse patientIndentPatientSearchIndent(PatientDetailRequest patientDetailsSearch,
			Integer unitId, Integer orgId) throws Exception {
		PatientDetailResponse patientDetails = new PatientDetailResponse();
		PatientRegistration patient = GlobalDao.getPatient(patientDetailsSearch.getPatientId());
		String patientName = patient.getPrefix().getPrefixCode() + " " + patient.getFirstName() + " "
				+ patient.getMiddleName() + " " + patient.getLastName();
		Integer age = Util.getAgeByBirthDate(patient.getDob().toLocalDateTime().toLocalDate());
		String patientDetail = patientName + " | " + age + " | " + patient.getGender().getGenderCode();
		patientDetails = new PatientDetailResponse(patient.getPatientId(), patientName, patientDetail,
				patient.getUhidNumber());
		VisitType visitType = GlobalDao.getVisitType(patientDetailsSearch.getVisitType());
		if (visitType != null) {
			patientDetails.setVisitTypeId(visitType.getVisitTypeId());
			patientDetails.setVisitType(visitType.getVisitTypeCode());
		}
		List<VisitDetails> visitDetails = new ArrayList<VisitDetails>();

		if (patientDetailsSearch.getVisitType() == 1) {
			List<Encounter> encounters = GlobalDao.encounterPatient(patientDetailsSearch, unitId);
			if (encounters != null && !encounters.isEmpty()) {
				for (Encounter encounter : encounters) {
					VisitDetails visitDetail = new VisitDetails(
							encounter.getDoctor() != null ? encounter.getDoctor().getDoctorId() : null,
							encounter.getEncounterNumber(), encounter.getEncounterDate());
					visitDetail.setEncounterId(encounter.getEncounterId());
					visitDetail.setAddedDate(encounter.getUpdatedDate());
					visitDetails.add(visitDetail);
				}
			} else
				throw new CustomException("Encounters not found for the patient");
		}

		if (patientDetailsSearch.getVisitType() == 2 || patientDetailsSearch.getVisitType() == 4
				|| patientDetailsSearch.getVisitType() == 3) {
			Admission admission = GlobalDao.admissionPatient(patientDetailsSearch, unitId);
			// patientDetailsS
			if (admission != null) {

				Integer drId = 0;
				if (patientDetailsSearch.getVisitType() == 4) {

				} else {
					if (admission.getDoctor() != null) {
						if (admission.getDoctor().getDoctorId() == null) {

						} else {
							drId = admission.getDoctor().getDoctorId();
						}

					}
				}

				VisitDetails visitDetail = new VisitDetails(drId, admission.getAdmissionNumber());
				visitDetail.setAdmissionId(admission.getAdmissionId());
				visitDetail.setAddedDate(admission.getUpdatedDate());
				visitDetails.add(visitDetail);
				if (admission.getAdmissionDetail() != null) {
					
					AdmissionDto admission1 = GlobalDao.admissionPatient1(admission.getAdmissionId());
					patientDetails.setWardId(admission1.getWard());
					patientDetails.setWardName(admission1.getWard1());
					patientDetails.setBedId(admission1.getBed());
					patientDetails.setBedNo(admission1.getBednumber1());
				}
			} else
				throw new CustomException("Admission not found for the patient");
		}

		patientDetails.setVisitDetails(visitDetails);
		return patientDetails;
	}

	@Override
	@Transactional
	public User userSearch(Integer id) {
		com.param.entity.model.master.User user = GlobalDao.userSearch(id);
		User userDetails = new User();
		userDetails.setId(user.getUserId());
		userDetails.setName(user.getUserName());

		return userDetails;
	}

	@Override
	@Transactional
	public List<Patient> patientSearch(String name, Integer visitType, Integer unitId) {

		List<Patient> patientDetailsList = new ArrayList<Patient>();

		List<PatientSearchDetails> patients = new ArrayList<PatientSearchDetails>();
		List<PatientSearchDetails> patients11 = new ArrayList<PatientSearchDetails>();
		if (visitType.equals(VisitTypeConstants.OP)) {
			patients = GlobalDao.getOPPatients(name, unitId);
		}
		if (visitType.equals(VisitTypeConstants.IP) || visitType.equals(VisitTypeConstants.DC)
				|| visitType.equals(VisitTypeConstants.ER)) {
			patients = GlobalDao.getIPDCERPatients(name, visitType, unitId);
		}
		
		if (visitType == 0 ) {
//			patients = GlobalDao.getOPPatients(name, unitId);
			patients11 = GlobalDao.getIPDCERPatientsAll(name, unitId);
			patients.addAll(patients11);
		}
		for (PatientSearchDetails patient : patients/* GlobalDao.patientSearchByName(name) */) {
			String patientName = patient.getPrefixCode() + " " + patient.getFirstName() + " " + patient.getMiddleName()
					+ " " + patient.getLastName();
			String uhid = patient.getUhidNumber();
			Integer age = null;
			if (patient.getDob() != null) {
				age = Util.getAgeByBirthDate(patient.getDob().toLocalDateTime().toLocalDate());
			}

			String patientDetails = uhid + " | " + patientName + " | " + age + " | " + patient.getGenderCode() + " | "
					+ patient.getMobileNumber();
			Patient patientDetail = new Patient(patient.getPatientId(), patientName, uhid, patientDetails);
			patientDetailsList.add(patientDetail);
		}
		return patientDetailsList;
	}

	@Override
	@Transactional
	public List<Patient> patientSearchByUhid(String uhid, Integer orgId) {

		List<Patient> patientDetailsList = new ArrayList<Patient>();
		for (PatientRegistration patient : GlobalDao.patientSearchByUhid(uhid, orgId)) {
			String preCode = patient.getPrefix().getPrefixCode();
			String firstName = patient.getFirstName() != null ?patient.getFirstName() : "";
			String middleName = patient.getMiddleName() != null ? patient.getMiddleName():"";
			String lastName = patient.getLastName() != null ?patient.getLastName() :"";
			String patientName = firstName +" "+ middleName +" "+ lastName;
			String uhidNumber = patient.getUhidNumber();
			Integer age = 0;
			if (patient.getDob() != null) {
				age = Util.getAgeByBirthDate(patient.getDob().toLocalDateTime().toLocalDate());
			}
			
			String mobno =patient.getMobileNumber() != null ?patient.getMobileNumber() : "";
			String patientDetails = uhidNumber + " | " + preCode + " " + firstName +" "+ middleName +" "+ lastName + " | " + age + " | "
					+ patient.getGender().getGenderCode() + " | " + mobno;
			Patient patientDetail = new Patient(patient.getPatientId(), patientName, uhidNumber, patientDetails);
			patientDetailsList.add(patientDetail);
		}
		return patientDetailsList;
	}

	@Override
	@Transactional
	public List<Doctor> doctorSearch(Integer orgId, Integer unitId, String name) {

		List<Doctor> doctorDetailsList = new ArrayList<Doctor>();
		for (DoctorSpecialityMapperDto doctorSpeciality : GlobalDao.doctorSearchByName(orgId, unitId, name)) {

			Doctor doctorDetail = new Doctor(doctorSpeciality.getDoctorId(), doctorSpeciality.getDoctorName(),
					doctorSpeciality.getDoctorName());

			doctorDetailsList.add(doctorDetail);
		}
		return doctorDetailsList;
	}

	@Override
	@Transactional
	public List<Ward> getWardList() {
		List<Ward> wards = new ArrayList<Ward>();
		for (WardMasters ward : GlobalDao.getWardList(null)) {
			Ward wardObj = new Ward(ward.getWardId(), ward.getWardName());
			wards.add(wardObj);
		}
		return wards;
	}

	@Override
	@Transactional
	public List<Floor> getFloorList() {
		List<Floor> floors = new ArrayList<Floor>();
		for (FloorMasters floor : GlobalDao.getFloorList(null)) {
			Floor floorObj = new Floor(floor.getFloorId(), floor.getFloorName());
			floors.add(floorObj);
		}
		return floors;
	}

	@Override
	public AdmissionDetail getPatientWardBedDetails(Integer admissionId) throws ApplicationException {
		try {
			return GlobalDao.getPatientWardBedDetails(admissionId);
		} catch (Exception e) {
		logger.error("getPatientWardBedDetails failed", e);
		}
		return null;
	}

	@Override
	@Transactional
	public Object getVendorList(String name, Integer unitId) {
		List<VendorResponseDto> vendorList = new ArrayList<VendorResponseDto>();
		for (Vendor vendor : GlobalDao.getVendorList(name, unitId)) {
			if (vendor.getIsActive() == true && vendor.getIsDeleted() == false) {
				VendorResponseDto vendorResponse = new VendorResponseDto(vendor.getId(),
						vendor.getCode() + "|" + vendor.getName() + "|" + vendor.getAddress(), vendor.getCode(),
						vendor.getName(), vendor.getAddress());
				vendorList.add(vendorResponse);
			}
		}
		return vendorList;
	}

	@Override
	@Transactional
	public Object getServiceProviderList(String name, Integer orgId) {
		List<VendorResponseDto> vendorList = new ArrayList<VendorResponseDto>();
		for (Vendor vendor : GlobalDao.getServiceProviderList(name, orgId)) {

			VendorResponseDto vendorResponse = new VendorResponseDto(vendor.getId(),
					vendor.getCode() + "|" + vendor.getName() + "|" + vendor.getAddress(), vendor.getCode(),
					vendor.getName(), vendor.getAddress());
			vendorList.add(vendorResponse);
		}
		return vendorList;
	}

	@Override
	@Transactional
	public Object getInsuranceCompanyList(String name, Integer unitId) {
		List<InsuranceCompanyDetails> insuranceCompanyList = new ArrayList<InsuranceCompanyDetails>();
		for (Company company : GlobalDao.getInsuranceCompanyList(name, unitId)) {

			InsuranceCompanyDetails insuranceCompany = new InsuranceCompanyDetails(company.getCompanyId(),
					company.getCompanyDesc());
			insuranceCompanyList.add(insuranceCompany);
		}
		return insuranceCompanyList;
	}

	@Override
	@Transactional
	public Object getManufacturerList(String name, Integer orgId) {
		List<ManufacturerResponseDto> manufacturerList = new ArrayList<ManufacturerResponseDto>();
		for (Manufacturer manufacture : GlobalDao.getManufacturerList(name, orgId)) {

			if (manufacture.getIsActive() == true && manufacture.getIsDeleted() == false) {
				ManufacturerResponseDto manufacturerResponse = new ManufacturerResponseDto(manufacture.getId(),
						manufacture.getCode() + "|" + manufacture.getName() + "|" + manufacture.getAddress(),
						manufacture.getCode(), manufacture.getName(), manufacture.getAddress());
				manufacturerList.add(manufacturerResponse);
			}
		}
		return manufacturerList;
	}

	@Override
	@Transactional
	public Object getAgainstPurchseRequestSearch(AgainstPurchaseRequest againstPurchaseRequest) {

		againstPurchaseRequest.setToDate((new DateTime(againstPurchaseRequest.getToDate())).plusDays(1).toDate());

		List<PurchaseRequestSearchDto> purchaseRequestResponseList = new ArrayList<PurchaseRequestSearchDto>();
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
		// statusIds.add(com.param.common.constants.Status.PendingApproval.getStatus());
		List<PurchaseRequest> prData = GlobalDao.searchLocalPuchaseRequest(againstPurchaseRequest, statusIds);
		for (PurchaseRequest purchaseRequest : prData) {

			PurchaseRequestSearchDto purchaseReqResponseDto = new PurchaseRequestSearchDto(purchaseRequest.getId(),
					purchaseRequest.getPurchaseNo(), purchaseRequest.getAddedDate(),
					purchaseRequest.getStore().getStoreId(), purchaseRequest.getStore().getStoreName(),
					purchaseRequest.getAddedBy().getUserName(), purchaseRequest.getApprovedBy().getUserName(),
					purchaseRequest.getRequestingStore().getStoreName());

			List<PurchaseRequestDetailResponse> purchaseRequestDeatailResponseList = new ArrayList<PurchaseRequestDetailResponse>();

			for (PurchaseRequestDetail purchaseReqDetails : purchaseRequest.getPurchaseRequestDetailList()) {
				if (purchaseReqDetails.getIsDeleted().equals(false)
						&& purchaseReqDetails.getApprovedQuantity() != null) {
					PurchaseRequestDetailResponse pucrhaseRequestDeatilResp = new PurchaseRequestDetailResponse(
							purchaseReqDetails.getId(), purchaseReqDetails.getItem().getId(),
							purchaseReqDetails.getItem().getCode(), purchaseReqDetails.getItem().getName(),
							purchaseReqDetails.getUomType() != null ? purchaseReqDetails.getUomType().getId() : null,
							purchaseReqDetails.getUomType() != null ? purchaseReqDetails.getUomType().getType() : null,
							purchaseReqDetails.getUomUnit() != null ? purchaseReqDetails.getUomUnit().getId() : null,
							purchaseReqDetails.getUomUnit() != null ? purchaseReqDetails.getUomUnit().getUnits() : null,
							purchaseReqDetails.getApprovedQuantity() != null ? purchaseReqDetails.getApprovedQuantity()
									: 0);

					if (againstPurchaseRequest.getScreenId() != null
							&& (againstPurchaseRequest.getScreenId().equals(Screen.QuotationEnquiry.getScreen()))) {
						if (purchaseReqDetails.getQuotationRequestDetail() == null
								&& GlobalDao.getQuotationRequestLeaseQunatity(purchaseRequest.getStore().getStoreId(),
										purchaseReqDetails.getItem().getId(),
										againstPurchaseRequest.getUnitId()) < 99999) {
							purchaseRequestDeatailResponseList.add(pucrhaseRequestDeatilResp);
						} else if (purchaseReqDetails.getQuotationRequestDetail() != null
								&& purchaseReqDetails.getQuotationRequestDetail().getQuotationRequest()
										.getApprovalStatus().getId()
										.equals(com.param.common.constants.Status.Cancelled.getStatus())
								&& GlobalDao.getCancelQuotationRequestLeaseQunatity(
										purchaseRequest.getStore().getStoreId(), purchaseReqDetails.getItem().getId(),
										againstPurchaseRequest.getUnitId()) < 99999) {
							purchaseRequestDeatailResponseList.add(pucrhaseRequestDeatilResp);

						}
					} else if (againstPurchaseRequest.getScreenId() == null) {

						int poQuantity = 0;
						int poApprovedQuantity = 0;

						List<PurchaseOrderDetail> purchaseOrderDetails = GlobalDao
								.getPurchaseOrdersWithCanelByRequestDetailId(purchaseReqDetails.getId());
						for (PurchaseOrderDetail pod : purchaseOrderDetails) {
							if (pod.getPoApprovedQuantity() == null || pod.getPoApprovedQuantity().equals(0)) {
								poQuantity = poQuantity + pod.getPoQuantity();
							}

							else if (pod.getPoApprovedQuantity() != null || pod.getPoApprovedQuantity() > 0) {
								poApprovedQuantity = poApprovedQuantity + pod.getPoApprovedQuantity();
							}

						}
						if ((poQuantity + poApprovedQuantity) < purchaseReqDetails.getApprovedQuantity()) {
							purchaseRequestDeatailResponseList.add(pucrhaseRequestDeatilResp);
						}
					}
				}

			}
			purchaseReqResponseDto.setItems(purchaseRequestDeatailResponseList);
			if (purchaseReqResponseDto.getItems() != null && !purchaseReqResponseDto.getItems().isEmpty()) {
				purchaseRequestResponseList.add(purchaseReqResponseDto);
			}
		}

		return purchaseRequestResponseList;
	}

	@Override
	@Transactional
	public Object getAgainstQuotationRequestSearch(AgainstPurchaseRequest againstPurchaseRequest) {

		againstPurchaseRequest.setToDate((new DateTime(againstPurchaseRequest.getToDate())).plusDays(1).toDate());

		List<QuotationRequestSearchDto> purchaseRequestResponseList = new ArrayList<QuotationRequestSearchDto>();
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
		// statusIds.add(com.param.common.constants.Status.PendingApproval.getStatus());

		for (QuotationRequest quotationRequest : GlobalDao.searchLocalQuotationRequest(againstPurchaseRequest,
				statusIds)) {

			String vqrNo = "";

			QuotationRequestSearchDto quotationRequestSearchDto = new QuotationRequestSearchDto(
					quotationRequest.getId(), quotationRequest.getQrNo(), quotationRequest.getAddedDate(),
					quotationRequest.getStore().getStoreId(), quotationRequest.getStore().getStoreName(),
					quotationRequest.getAddedBy().getUserName(), quotationRequest.getApprovedBy().getUserName());

			for (QuotationRequestDetail qrd : quotationRequest.getQuotationRequestDetailsList()) {

				for (QuotationVendorDetail q : qrd.getQuotationVendorDetail()) {
					if (q.getQuotationVendor().getVendor().getId().equals(againstPurchaseRequest.getVendorId())) {
						vqrNo = q.getQuotationVendor().getVqNumber();
					}

				}

			}
			quotationRequestSearchDto.setVqrNo(vqrNo);

			List<QuotationRequestDetailResponse> quotationRequestDetailResponseList = new ArrayList<QuotationRequestDetailResponse>();
			List<QuotationVendor> qvData = GlobalDao.getQuotationVendorSearchList(againstPurchaseRequest, statusIds,
					againstPurchaseRequest.getUnitId());

			if (qvData.size() > 0) {
				for (QuotationVendor qrd : qvData) {

					List<Integer> quotationRequestDetailIds = GlobalDao.getQuotationRequestDetailIds(
							quotationRequest.getId(), againstPurchaseRequest.getVendorId());
					List<QuotationRequestDetail> quotationRequestDetailList = GlobalDao
							.getQuotationRequestDetailListByIds(quotationRequestDetailIds);

					/*
					 * for (QuotationVendorDetail qvd : qrd.getQuotationVendorDetailsList()) {
					 * QuotationRequestDetail quotationRequestDetail = qvd.getQrDetail();
					 */

					for (QuotationRequestDetail quotationRequestDetail : quotationRequestDetailList) {

						// for (QuotationRequestDetail quotationRequestDetail : qvd.getQrDetail()) {
						// quotationRequestDetail.getQuotationVendorDetail().
						if (quotationRequestDetail.getIsDeleted().equals(false)) {
							/*
							 * QuotationRequestDetailResponse quotationRequestDetailResp = new
							 * QuotationRequestDetailResponse( quotationRequestDetail.getId(),
							 * quotationRequestDetail.getItem().getId(),
							 * quotationRequestDetail.getItem().getCode(),
							 * quotationRequestDetail.getItem().getName(),
							 * quotationRequestDetail.getUomType() != null ?
							 * quotationRequestDetail.getUomType().getId() : null,
							 * quotationRequestDetail.getUomType() != null ?
							 * quotationRequestDetail.getUomType().getType() : null,
							 * quotationRequestDetail.getUomUnit() != null ?
							 * quotationRequestDetail.getUomUnit().getId() : null,
							 * quotationRequestDetail.getUomUnit() != null ?
							 * quotationRequestDetail.getUomUnit().getUnits() : null,
							 * quotationRequestDetail.getQuantity() != null ?
							 * quotationRequestDetail.getQuantity() : 0);
							 * quotationRequestDetailResponseList.add(quotationRequestDetailResp);
							 */

							if (quotationRequestDetail.getPurchaseOrderDetails().size() == 0) {

								QuotationRequestDetailResponse quotationRequestDetailResp = new QuotationRequestDetailResponse(
										quotationRequestDetail.getId(), quotationRequestDetail.getItem().getId(),
										quotationRequestDetail.getItem().getCode(),
										quotationRequestDetail.getItem().getName(),
										quotationRequestDetail.getUomType() != null
												? quotationRequestDetail.getUomType().getId()
												: null,
										quotationRequestDetail.getUomType() != null
												? quotationRequestDetail.getUomType().getType()
												: null,
										quotationRequestDetail.getUomUnit() != null
												? quotationRequestDetail.getUomUnit().getId()
												: null,
										quotationRequestDetail.getUomUnit() != null
												? quotationRequestDetail.getUomUnit().getUnits()
												: null,
										quotationRequestDetail.getQuantity() != null
												? quotationRequestDetail.getQuantity()
												: 0);
								quotationRequestDetailResponseList.add(quotationRequestDetailResp);
							} else {

								for (PurchaseOrderDetail pod : quotationRequestDetail.getPurchaseOrderDetails()) {
									if (pod.getPurchaseOrder().getApprovalStatus().getId()
											.equals(com.param.common.constants.Status.Cancelled.getStatus())) {
										QuotationRequestDetailResponse quotationRequestDetailResp = new QuotationRequestDetailResponse(
												quotationRequestDetail.getId(),
												quotationRequestDetail.getItem().getId(),
												quotationRequestDetail.getItem().getCode(),
												quotationRequestDetail.getItem().getName(),
												quotationRequestDetail.getUomType() != null
														? quotationRequestDetail.getUomType().getId()
														: null,
												quotationRequestDetail.getUomType() != null
														? quotationRequestDetail.getUomType().getType()
														: null,
												quotationRequestDetail.getUomUnit() != null
														? quotationRequestDetail.getUomUnit().getId()
														: null,
												quotationRequestDetail.getUomUnit() != null
														? quotationRequestDetail.getUomUnit().getUnits()
														: null,
												quotationRequestDetail.getQuantity() != null
														? quotationRequestDetail.getQuantity()
														: 0);
										quotationRequestDetailResponseList.add(quotationRequestDetailResp);
									} else if (!pod.getPurchaseOrder().getVendor().getId()
											.equals(againstPurchaseRequest.getVendorId())) {

										QuotationRequestDetailResponse quotationRequestDetailResp = new QuotationRequestDetailResponse(
												quotationRequestDetail.getId(),
												quotationRequestDetail.getItem().getId(),
												quotationRequestDetail.getItem().getCode(),
												quotationRequestDetail.getItem().getName(),
												quotationRequestDetail.getUomType() != null
														? quotationRequestDetail.getUomType().getId()
														: null,
												quotationRequestDetail.getUomType() != null
														? quotationRequestDetail.getUomType().getType()
														: null,
												quotationRequestDetail.getUomUnit() != null
														? quotationRequestDetail.getUomUnit().getId()
														: null,
												quotationRequestDetail.getUomUnit() != null
														? quotationRequestDetail.getUomUnit().getUnits()
														: null,
												quotationRequestDetail.getQuantity() != null
														? quotationRequestDetail.getQuantity()
														: 0);
										quotationRequestDetailResponseList.add(quotationRequestDetailResp);

									}

								}

							}

						}

						// }

					}

				}
			} else {
				// quotationRequest.get
				// List<QuotationRequestDetail> quotationRequestDetailList=
				// GlobalDao.getQuotationRequestDetailList(quotationRequest.getId(),
				// againstPurchaseRequest.getVendorId());
				// for (QuotationRequestDetail quotationRequestDetail : quotationRequest
				// .getQuotationRequestDetailsList()) {

				List<Integer> quotationRequestDetailIds = GlobalDao
						.getQuotationRequestDetailIds(quotationRequest.getId(), againstPurchaseRequest.getVendorId());
				List<QuotationRequestDetail> quotationRequestDetailList = GlobalDao
						.getQuotationRequestDetailListByIds(quotationRequestDetailIds);
				// for (QuotationRequestDetail quotationRequestDetail : quotationRequest
				// .getQuotationRequestDetailsList()) {
				for (QuotationRequestDetail quotationRequestDetail : quotationRequestDetailList) {

					// quotationRequestDetail.getQuotationVendorDetail().get(0).getQrDetail().getQuotationRequest().getQuotationRequestDetailsList().get(0).getq
					// if (quotationRequestDetail.getIsDeleted().equals(false) &&
					// quotationRequestDetail.getIsQuotationFillCancel().equals(false)) {
					if (quotationRequestDetail.getIsDeleted().equals(false)) {
						if (quotationRequestDetail.getPurchaseOrderDetails().size() == 0) {
							// quotationRequestDetail.getq
							QuotationRequestDetailResponse quotationRequestDetailResp = new QuotationRequestDetailResponse(
									quotationRequestDetail.getId(), quotationRequestDetail.getItem().getId(),
									quotationRequestDetail.getItem().getCode(),
									quotationRequestDetail.getItem().getName(),
									quotationRequestDetail.getUomType() != null
											? quotationRequestDetail.getUomType().getId()
											: null,
									quotationRequestDetail.getUomType() != null
											? quotationRequestDetail.getUomType().getType()
											: null,
									quotationRequestDetail.getUomUnit() != null
											? quotationRequestDetail.getUomUnit().getId()
											: null,
									quotationRequestDetail.getUomUnit() != null
											? quotationRequestDetail.getUomUnit().getUnits()
											: null,
									quotationRequestDetail.getQuantity() != null ? quotationRequestDetail.getQuantity()
											: 0);

							Boolean vMapper = GlobalDao.getVendorSelectionMapper(quotationRequestDetail.getId(),
									againstPurchaseRequest.getVendorId());
							if (vMapper != null && vMapper.equals(true)) {
								quotationRequestDetailResponseList.add(quotationRequestDetailResp);
							}

						} else {

							for (PurchaseOrderDetail pod : quotationRequestDetail.getPurchaseOrderDetails()) {
								if (pod.getPurchaseOrder().getApprovalStatus().getId()
										.equals(com.param.common.constants.Status.Cancelled.getStatus())) {
									QuotationRequestDetailResponse quotationRequestDetailResp = new QuotationRequestDetailResponse(
											quotationRequestDetail.getId(), quotationRequestDetail.getItem().getId(),
											quotationRequestDetail.getItem().getCode(),
											quotationRequestDetail.getItem().getName(),
											quotationRequestDetail.getUomType() != null
													? quotationRequestDetail.getUomType().getId()
													: null,
											quotationRequestDetail.getUomType() != null
													? quotationRequestDetail.getUomType().getType()
													: null,
											quotationRequestDetail.getUomUnit() != null
													? quotationRequestDetail.getUomUnit().getId()
													: null,
											quotationRequestDetail.getUomUnit() != null
													? quotationRequestDetail.getUomUnit().getUnits()
													: null,
											quotationRequestDetail.getQuantity() != null
													? quotationRequestDetail.getQuantity()
													: 0);
									quotationRequestDetailResponseList.add(quotationRequestDetailResp);
								} else if (!pod.getPurchaseOrder().getVendor().getId()
										.equals(againstPurchaseRequest.getVendorId())) {

									QuotationRequestDetailResponse quotationRequestDetailResp = new QuotationRequestDetailResponse(
											quotationRequestDetail.getId(), quotationRequestDetail.getItem().getId(),
											quotationRequestDetail.getItem().getCode(),
											quotationRequestDetail.getItem().getName(),
											quotationRequestDetail.getUomType() != null
													? quotationRequestDetail.getUomType().getId()
													: null,
											quotationRequestDetail.getUomType() != null
													? quotationRequestDetail.getUomType().getType()
													: null,
											quotationRequestDetail.getUomUnit() != null
													? quotationRequestDetail.getUomUnit().getId()
													: null,
											quotationRequestDetail.getUomUnit() != null
													? quotationRequestDetail.getUomUnit().getUnits()
													: null,
											quotationRequestDetail.getQuantity() != null
													? quotationRequestDetail.getQuantity()
													: 0);
									quotationRequestDetailResponseList.add(quotationRequestDetailResp);

								}

							}

						}

					}

				}
			}

			quotationRequestSearchDto.setItems(quotationRequestDetailResponseList);
			if (quotationRequestSearchDto.getItems() != null && !quotationRequestSearchDto.getItems().isEmpty()) {
				purchaseRequestResponseList.add(quotationRequestSearchDto);
			}
		}

		return purchaseRequestResponseList;
	}

	@Override
	@Transactional
	public Object getAgainstWOQuotationRequestSearch(AgainstWORequest againstWORequest) {

		againstWORequest.setToDate((new DateTime(againstWORequest.getToDate())).plusDays(1).toDate());

		List<QuotationRequestSearchDto> quotationRequestResponseList = new ArrayList<QuotationRequestSearchDto>();
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
		// statusIds.add(com.param.common.constants.Status.PendingApproval.getStatus());

		for (WorkOrderCreation workOrderCreation : GlobalDao.searchLocalWOQuotationRequest(againstWORequest, statusIds,
				null, null)) {

			QuotationRequestSearchDto quotationRequestSearchDto = new QuotationRequestSearchDto(
					workOrderCreation.getId(), workOrderCreation.getWorkOrderNo(), workOrderCreation.getAddedDate(),
					workOrderCreation.getStore().getStoreId(), workOrderCreation.getStore().getStoreName(),
					workOrderCreation.getAddedBy().getUserName(), workOrderCreation.getApprovedBy().getUserName());

			List<QuotationRequestDetailResponse> quotationRequestDetailResponseList = new ArrayList<QuotationRequestDetailResponse>();

			for (WorkOrderCreationDetail workOrderCreationDetail : workOrderCreation.getWorkOrderCreationDetail()) {

				// if
				// ((workOrderCreationDetail.getStatus().getId().equals(com.param.common.constants.Status.Approved.getStatus())
				// && !(workOrderCreationDetail.getWorkOrderDetail() != null))
				// ||
				// (workOrderCreationDetail.getStatus().getId().equals(com.param.common.constants.Status.PartiallyApproved.getStatus())
				// && !(workOrderCreationDetail.getWorkOrderDetail() != null))) {
				if (!(workOrderCreationDetail.getWorkOrderDetail() != null)
						&& workOrderCreationDetail.getIsDeleted().equals(false)) {
					QuotationRequestDetailResponse quotationRequestDeatilResp = new QuotationRequestDetailResponse(
							workOrderCreationDetail.getId(), workOrderCreationDetail.getItem().getId(),
							workOrderCreationDetail.getItem().getCode(), workOrderCreationDetail.getItem().getName(),
							workOrderCreationDetail.getWoQuantity() != null ? workOrderCreationDetail.getWoQuantity()
									: 0);

					if (againstWORequest.getScreenId() != null
							&& (againstWORequest.getScreenId().equals(Screen.QuotationEnquiry.getScreen()))) {
						// Quotation Enquiry Search
						if (workOrderCreationDetail.getWorkOrderQuotationDetail() == null
								&& GlobalDao.getQuotationRequestLeaseQunatity(workOrderCreation.getStore().getStoreId(),
										workOrderCreationDetail.getItem().getId(), null) < 99999) {
							quotationRequestDetailResponseList.add(quotationRequestDeatilResp);
						}
					} else if (againstWORequest.getScreenId() == null) {
						// Purchase Order Search
						quotationRequestDetailResponseList.add(quotationRequestDeatilResp);

					}
				}
				// }

			}
			quotationRequestSearchDto.setItems(quotationRequestDetailResponseList);
			if (quotationRequestSearchDto.getItems() != null && !quotationRequestSearchDto.getItems().isEmpty()) {
				quotationRequestResponseList.add(quotationRequestSearchDto);
			}
		}

		return quotationRequestResponseList;
	}

	@Override
	@Transactional
	public Object getItemsDetailsAgainstPurchseRequest(List<AgainstPurchaseRequest> againstPurchaseRequestList,
			Integer orgId, Integer unitId) {
		AgainstPurchaseRequestResponse againstPurchaseReq = new AgainstPurchaseRequestResponse();
		List<PurchaseRequestSearchDto> response = new ArrayList<PurchaseRequestSearchDto>();
		for (AgainstPurchaseRequest againstPurchaseRequest : againstPurchaseRequestList) {

			PurchaseRequest purchaseRequest = GlobalDao.getPurchaseRequestList((long) againstPurchaseRequest.getPrId());

			PurchaseRequestSearchDto purchaseRequestSearchDto = new PurchaseRequestSearchDto(purchaseRequest.getId(),
					purchaseRequest.getPurchaseNo(), purchaseRequest.getAddedDate());

			List<PurchaseRequestDetailResponse> prDeatailList = new ArrayList<PurchaseRequestDetailResponse>();

			for (Integer Id : againstPurchaseRequest.getItemIds()) {
				if (purchaseRequest.getAssetType().getId().equals(Constants.getConsumableId())) {
					for (PurchaseRequestDetailResponse purchasedReqDetail : GlobalDao
							.getItemsDetailsAgainstPr(purchaseRequest.getId(), Id)) {

						int poQuantity = 0;
						int poApprovedQuantity = 0;

						List<PurchaseOrderDetail> purchaseOrderDetails = GlobalDao
								.getPurchaseOrdersWithCanelByRequestDetailId(purchasedReqDetail.getId());

						for (PurchaseOrderDetail pod : purchaseOrderDetails) {

							if (pod.getPoApprovedQuantity() == null || pod.getPoApprovedQuantity().equals(0))
								poQuantity = poQuantity + pod.getPoQuantity();
							else if (pod.getPoApprovedQuantity() != null || pod.getPoApprovedQuantity() > 0)
								poApprovedQuantity = poApprovedQuantity + pod.getPoApprovedQuantity();

						}

						Integer pendingQty = purchasedReqDetail.getPrQuantity() - (poQuantity + poApprovedQuantity);
						purchasedReqDetail.setPendingPOqty(pendingQty);

						Long storeStock = GlobalDao.getStoreStockByItem(purchasedReqDetail.getStoreId(),
								purchasedReqDetail.getItemId(), unitId);
						Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(purchasedReqDetail.getStoreId(),
								purchasedReqDetail.getItemId());
						Long maxStoreQuantity = (long) GlobalDao.getStoreMaxQuantityByItem(
								purchasedReqDetail.getStoreId(), purchasedReqDetail.getItemId(), orgId);
						Long prLeaseQuantity = GlobalDao.getPurchaseRequestLeaseQunatity(
								purchasedReqDetail.getStoreId(), purchasedReqDetail.getItemId(), unitId);
						/*
						 * Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit +
						 * prLeaseQuantity);
						 */
						Long approvedPRAndPOQuantity = ItemsDao.getApprovedPRandPOQuantity(
								purchasedReqDetail.getStoreId(), purchasedReqDetail.getItemId(), unitId);
						Long approvedPOQuantity = ItemsDao.getApprovedPurchaseOrderQuantity(
								purchasedReqDetail.getStoreId(), purchasedReqDetail.getItemId(), unitId);

						Long maxOrderQuantity = maxStoreQuantity
								- (storeStock + stockInTransit + approvedPRAndPOQuantity + approvedPOQuantity);
						purchasedReqDetail.setPrQuantity(
								purchasedReqDetail.getPrQuantity() != null ? purchasedReqDetail.getPrQuantity() : 0);
						purchasedReqDetail.setUom(
								GlobalDao.getUnitOfMeasurementListByItem(purchasedReqDetail.getItemId(), orgId));
						purchasedReqDetail.setMaxStoreQuantity(maxStoreQuantity);
						purchasedReqDetail.setMaxOrderQuantity(maxOrderQuantity);
						purchasedReqDetail
								.setStockAvailable(GlobalDao.getStoreStockByItem(purchasedReqDetail.getStoreId(),
										purchasedReqDetail.getItemId(), unitId).intValue());

						GoodReceiptNoteDetailDto d = GlobalDao.getLastPouomandMrp(purchaseRequest.getOrganizationId(),
								purchaseRequest.getUnitId(), purchasedReqDetail.getItemId());
						purchasedReqDetail.setGoodReceiptNoteDetailDto(d);
						purchasedReqDetail.setPendingQuantity(maxOrderQuantity.intValue());
						purchasedReqDetail.setStockAvailable(storeStock.intValue());
						prDeatailList.add(purchasedReqDetail);
					}
				}
				if (purchaseRequest.getAssetType().getId().equals(Constants.getFixedAssetId())) {
					for (PurchaseRequestDetailResponse purchasedReqDetail : GlobalDao
							.getFixedItemsDetailsAgainstPr(purchaseRequest.getId(), Id)) {

						int poQuantity = 0;
						int poApprovedQuantity = 0;

						List<PurchaseOrderDetail> purchaseOrderDetails = GlobalDao
								.getPurchaseOrdersWithCanelByRequestDetailId(purchasedReqDetail.getId());

						for (PurchaseOrderDetail pod : purchaseOrderDetails) {

							if (pod.getPoApprovedQuantity() == null || pod.getPoApprovedQuantity().equals(0))
								poQuantity = poQuantity + pod.getPoQuantity();
							else if (pod.getPoApprovedQuantity() != null || pod.getPoApprovedQuantity() > 0)
								poApprovedQuantity = poApprovedQuantity + pod.getPoApprovedQuantity();

						}

						Integer pendingQty = purchasedReqDetail.getPrQuantity() - (poQuantity + poApprovedQuantity);
						purchasedReqDetail.setPendingPOqty(pendingQty);

						Long storeStock = GlobalDao.getStoreStockByItem(purchasedReqDetail.getStoreId(),
								purchasedReqDetail.getItemId(), unitId);
						Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(purchasedReqDetail.getStoreId(),
								purchasedReqDetail.getItemId());
						Long maxStoreQuantity = (long) GlobalDao.getStoreMaxQuantityByItem(
								purchasedReqDetail.getStoreId(), purchasedReqDetail.getItemId(), orgId);
						Long prLeaseQuantity = GlobalDao.getPurchaseRequestLeaseQunatity(
								purchasedReqDetail.getStoreId(), purchasedReqDetail.getItemId(), unitId);
						Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit + prLeaseQuantity);

						purchasedReqDetail.setPrQuantity(
								purchasedReqDetail.getPrQuantity() != null ? purchasedReqDetail.getPrQuantity() : 0);
						purchasedReqDetail.setMaxStoreQuantity(maxStoreQuantity);
						purchasedReqDetail.setMaxOrderQuantity(maxOrderQuantity);
						purchasedReqDetail.setPendingPOqty(purchasedReqDetail.getPendingQuantity());
						purchasedReqDetail.setPendingQuantity(maxOrderQuantity.intValue());
						purchasedReqDetail.setStockAvailable(storeStock.intValue());
						prDeatailList.add(purchasedReqDetail);
					}
				}

				purchaseRequestSearchDto.setItems(prDeatailList);
			}
			if (purchaseRequestSearchDto.getItems() != null && !purchaseRequestSearchDto.getItems().isEmpty()) {
				response.add(purchaseRequestSearchDto);
			}
		}
		againstPurchaseReq.setPurchaseRequestList(response);
		againstPurchaseReq.setOtherChargesList(getOtherCharges(unitId));
		againstPurchaseReq.setTaxList(getTaxList(orgId));
		return againstPurchaseReq;
	}

	private List<TaxDropDown> getTaxList(Integer orgId) {
		List<TaxDropDown> taxList = new ArrayList<TaxDropDown>();
		for (Tax tax : GlobalDao.getTaxList(orgId)) {

			TaxDropDown taxDto = new TaxDropDown(tax.getId(), tax.getName(), tax.getTaxPercentage());
			taxList.add(taxDto);
		}
		return taxList;
	}

	@Override
	@Transactional
	public List<OtherChargesResponse> getOtherCharges(Integer unitId) {

		/*
		 * List<OtherChargesResponse> otherChargesList = new
		 * ArrayList<OtherChargesResponse>(); for (OtherCharge otherCharges :
		 * GlobalDao.getOtherChargesBP(unitId)) {
		 * 
		 * OtherChargesResponse response = new
		 * OtherChargesResponse(otherCharges.getId(), otherCharges.getDescription());
		 * otherChargesList.add(response); }
		 */

		return GlobalDao.getOtherChargesBP(unitId);
	}

	@Override
	@Transactional
	public Object getItemsDetailsAgainstQuotationRequest(List<AgainstQuotationRequest> againstQuotationRequestList,
			Integer orgId, Integer unitId) {

		AgainstQuotationRequestResponse againstPurchaseReq = new AgainstQuotationRequestResponse();
		List<QuotationRequestSearchDto> response = new ArrayList<QuotationRequestSearchDto>();
		for (AgainstQuotationRequest againstPurchaseRequest : againstQuotationRequestList) {
			QuotationRequest quotationRequest = GlobalDao.getQuotationRequestList(againstPurchaseRequest.getQrId());

			QuotationRequestSearchDto quotationRequestSearchDto = new QuotationRequestSearchDto(
					quotationRequest.getId(), quotationRequest.getQrNo(), quotationRequest.getAddedDate());

			List<QuotationRequestDetailResponse> qrDeatailList = new ArrayList<QuotationRequestDetailResponse>();

			for (Integer Id : againstPurchaseRequest.getItemIds()) {
				if (quotationRequest.getAssetType().getId().equals(Constants.getConsumableId())) {
					for (QuotationRequestDetailResponse quotationReqDetail : GlobalDao
							.getItemsDetailsAgainstQr(quotationRequest.getId(), Id)) {
						Long storeStock = GlobalDao.getStoreStockByItem(quotationReqDetail.getStoreId(),
								quotationReqDetail.getItemId(), unitId);

						Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(quotationReqDetail.getStoreId(),
								quotationReqDetail.getItemId());

						Long maxStoreQuantity = (long) GlobalDao.getStoreMaxQuantityByItem(
								quotationReqDetail.getStoreId(), quotationReqDetail.getItemId(), orgId);

						Long prLeaseQuantity = GlobalDao.getPurchaseRequestLeaseQunatity(
								quotationReqDetail.getStoreId(), quotationReqDetail.getItemId(), unitId);

						Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit + prLeaseQuantity);

						quotationReqDetail.setPendingPOqty(
								quotationReqDetail.getPendingPOqty() != null ? quotationReqDetail.getPendingPOqty()
										: 0);

						quotationReqDetail.setQrQuantity(
								quotationReqDetail.getQrQuantity() != null ? quotationReqDetail.getQrQuantity() : 0);
						quotationReqDetail.setUom(
								GlobalDao.getUnitOfMeasurementListByItem(quotationReqDetail.getItemId(), orgId));
						quotationReqDetail.setMaxStoreQuantity(maxStoreQuantity);
						quotationReqDetail.setMaxOrderQuantity(maxOrderQuantity);
						quotationReqDetail.setStockAvailable(storeStock);
						// quotationReqDetail.get

						// quotationReqDetail.setVendorCop(GlobalDao.getVendorCop(quotationReqDetail.getId()));
						qrDeatailList.add(quotationReqDetail);
					}
				}
				if (quotationRequest.getAssetType().getId().equals(Constants.getFixedAssetId())) {
					for (QuotationRequestDetailResponse quotationedReqDetail : GlobalDao
							.getFixedItemsDetailsAgainstQr(quotationRequest.getId(), Id)) {

						Long storeStock = GlobalDao.getStoreStockByItem(quotationedReqDetail.getStoreId(),
								quotationedReqDetail.getItemId(), unitId);

						Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(quotationedReqDetail.getStoreId(),
								quotationedReqDetail.getItemId());

						Long maxStoreQuantity = (long) GlobalDao.getStoreMaxQuantityByItem(
								quotationedReqDetail.getStoreId(), quotationedReqDetail.getItemId(), orgId);

						Long prLeaseQuantity = GlobalDao.getPurchaseRequestLeaseQunatity(
								quotationedReqDetail.getStoreId(), quotationedReqDetail.getItemId(), unitId);

						Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit + prLeaseQuantity);

						quotationedReqDetail.setQrQuantity(
								quotationedReqDetail.getQrQuantity() != null ? quotationedReqDetail.getQrQuantity()
										: 0);
						quotationedReqDetail.setMaxStoreQuantity(maxStoreQuantity);
						quotationedReqDetail.setMaxOrderQuantity(maxOrderQuantity);
						quotationedReqDetail.setStockAvailable(storeStock);
						// quotationedReqDetail.setVendorCop(GlobalDao.getVendorCop(quotationedReqDetail.getId()));
						qrDeatailList.add(quotationedReqDetail);
					}
				}

				quotationRequestSearchDto.setItems(qrDeatailList);
			}
			if (quotationRequestSearchDto.getItems() != null && !quotationRequestSearchDto.getItems().isEmpty()) {
				response.add(quotationRequestSearchDto);
			}
		}
		againstPurchaseReq.setQuotationRequestList(response);
		againstPurchaseReq.setOtherChargesList(getOtherCharges(unitId));
		againstPurchaseReq.setTaxList(getTaxList(orgId));
		// System.out.println(againstPurchaseReq.toString());
		return againstPurchaseReq;

	}

	@Override
	@Transactional
	public DirectPurchaseRequestResponse getItemDetailsDirectPurchaseRequest(
			AgainstPurchaseRequest againstPurchaseRequest) {

		DirectPurchaseRequestResponse directPoResponse = new DirectPurchaseRequestResponse();

		long count = ItemsDao.productCategoryCount(againstPurchaseRequest.getItemIds());
		List<ItemsDetailsListResponse> response = null;
		if (count == 0) {
			response = ItemsDao.itemsDetails(againstPurchaseRequest.getItemIds(), againstPurchaseRequest.getStoreId(),
					againstPurchaseRequest.getUnitId());
		} else if (count != 0 && count > 0) {
			response = ItemsDao.itemsDetailsFixedAsset(againstPurchaseRequest.getItemIds(),
					againstPurchaseRequest.getStoreId());
		}

		List<Integer> itemIds = response.stream().map(ItemsDetailsListResponse::getItemId).collect(Collectors.toList());
		// List<ItemQuantitiesByStoreId> quantities =
		// GlobalDao.getQuantities(againstPurchaseRequest.getStoreId(), itemIds);
		List<UomResponse> uoms = GlobalDao.getUnitOfMeasurementListByItemBettoerPerformance(itemIds,
				againstPurchaseRequest.getOrganizationId());

		List<Quantities> storeStocks = GlobalDao.getStoreStockByItemBP(againstPurchaseRequest.getStoreId(), itemIds,
				againstPurchaseRequest.getUnitId());
		List<Quantities> stockInTransits = GlobalDao.getStoreStockInTransitByItemBP(againstPurchaseRequest.getStoreId(),
				itemIds, againstPurchaseRequest.getUnitId());
		List<Quantities> lastMonthSales = GlobalDao.getLastMonthSaleBP(againstPurchaseRequest.getStoreId(), itemIds,
				againstPurchaseRequest.getUnitId());
		List<Quantities> maxStoreQuantitys = GlobalDao.getStoreMaxQuantityByItemBP(againstPurchaseRequest.getStoreId(),
				itemIds, againstPurchaseRequest.getOrganizationId());
		List<Quantities> currentMonthSales = GlobalDao.getCurrentMonthSaleBP(againstPurchaseRequest.getStoreId(),
				itemIds, againstPurchaseRequest.getUnitId());

		for (ItemsDetailsListResponse item : response) {
			Long storeStock = 0l;
			Long stockInTransit = 0l;
			Long lastMonthSale = 0l;
			Long currentMonthSale = 0l;
			Integer maxStoreQuantity = 0;

			if (storeStocks.stream().filter(x -> x.getItemId().equals(item.getItemId()))
					.collect(Collectors.toList()) != null
					&& !storeStocks.stream().filter(x -> x.getItemId().equals(item.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {

				storeStock = storeStocks.stream().filter(x -> x.getItemId().equals(item.getItemId()))
						.collect(Collectors.toList()).get(0).getQuantity();
			}
			if (stockInTransits.stream().filter(x -> x.getItemId().equals(item.getItemId()))
					.collect(Collectors.toList()) != null
					&& !stockInTransits.stream().filter(x -> x.getItemId().equals(item.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				stockInTransit = stockInTransits.stream().filter(x -> x.getItemId().equals(item.getItemId()))
						.collect(Collectors.toList()).get(0).getQuantity();
			}
			if (lastMonthSales.stream().filter(x -> x.getItemId().equals(item.getItemId()))
					.collect(Collectors.toList()) != null
					&& !lastMonthSales.stream().filter(x -> x.getItemId().equals(item.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				lastMonthSale = lastMonthSales.stream().filter(x -> x.getItemId().equals(item.getItemId()))
						.collect(Collectors.toList()).get(0).getQuantity();
			}
			if (currentMonthSales.stream().filter(x -> x.getItemId().equals(item.getItemId()))
					.collect(Collectors.toList()) != null
					&& !currentMonthSales.stream().filter(x -> x.getItemId().equals(item.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				currentMonthSale = currentMonthSales.stream().filter(x -> x.getItemId().equals(item.getItemId()))
						.collect(Collectors.toList()).get(0).getQuantity();
			}
			if (maxStoreQuantitys.stream().filter(x -> x.getItemId().equals(item.getItemId()))
					.collect(Collectors.toList()) != null
					&& !maxStoreQuantitys.stream().filter(x -> x.getItemId().equals(item.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				maxStoreQuantity = maxStoreQuantitys.stream().filter(x -> x.getItemId().equals(item.getItemId()))
						.collect(Collectors.toList()).get(0).getIntQuantity();
			}

			Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit);
			if (maxOrderQuantity < 0) {
				maxOrderQuantity = 0L;
			}

			item.setStockAvailable(storeStock);
			item.setStockInTransit(stockInTransit);
			item.setLastMonthSale(lastMonthSale);
			item.setCurrentMonthSale(currentMonthSale);
			item.setMaxStoreQuantity(maxStoreQuantity);
			item.setMaxOrderQuantity(maxOrderQuantity);

			item.setUom(uoms.stream().filter(x -> x.getItemId().equals(item.getItemId())).collect(Collectors.toList()));
		}

		directPoResponse.setItemDetailsList(response);
		// directPoResponse.setOtherChargesList(getOtherCharges());
		// directPoResponse.setTaxList(getTaxList());
		directPoResponse.setOtherChargesList(GlobalDao.getOtherChargesBP(againstPurchaseRequest.getUnitId()));
		directPoResponse.setTaxList(GlobalDao.getTaxListBP(againstPurchaseRequest.getOrganizationId()));
		return directPoResponse;
	}

	@Override
	@Transactional
	public Object getAgainstPurchseOrderSearch(AgainstPurchaseOrder againstPurchaseOrder) {

		againstPurchaseOrder.setToDate((new DateTime(againstPurchaseOrder.getToDate())).plusDays(1).toDate());
		List<AgainstPOSearchResponse> againstPOSearchResponseList = new ArrayList<AgainstPOSearchResponse>();
		List<Integer> statusIds = new ArrayList<Integer>();
		statusIds.add(com.param.common.constants.Status.Approved.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyApproved.getStatus());
		statusIds.add(com.param.common.constants.Status.PartiallyCompleted.getStatus());

		List<AgainstPOSearchResponse> againstPOSearchResponses = GlobalDao.searchPurchaseOrderBP(againstPurchaseOrder,
				statusIds);

		for (AgainstPOSearchResponse r : againstPOSearchResponses) {
			PurchaseOrderDetailResponse res = GlobalDao.getPOReqStore(r.getId());
			r.setFromStoreId(res.getFromstoreid());
			r.setFromStoreName(res.getFromstorename());
		}
		List<Integer> poIds = againstPOSearchResponses.stream().map(AgainstPOSearchResponse::getId)
				.collect(Collectors.toList());
		List<PurchaseOrderDetailResponse> poDetailsResp = new ArrayList<PurchaseOrderDetailResponse>();
		if (poIds != null && !poIds.isEmpty())
			poDetailsResp = GlobalDao.getPoDetailsResponse(poIds);

		for (AgainstPOSearchResponse againstPOSearchResponse : againstPOSearchResponses) {
			List<PurchaseOrderDetailResponse> purchaseOrderDetailResponseList = new ArrayList<PurchaseOrderDetailResponse>();

			List<PurchaseOrderDetailResponse> specificPoDetailsResp = poDetailsResp.stream()
					.filter(x -> x.getPurchaseOrderId().equals(againstPOSearchResponse.getId()))
					.collect(Collectors.toList());

			for (PurchaseOrderDetailResponse purchaseOrderDetailResponse : specificPoDetailsResp) {
				if (purchaseOrderDetailResponse.getGrnApprovedAndReceivedSum() < purchaseOrderDetailResponse
						.getPoQuantity())

					purchaseOrderDetailResponseList.add(purchaseOrderDetailResponse);
			}

			if (purchaseOrderDetailResponseList != null && purchaseOrderDetailResponseList.size() > 0) {
				againstPOSearchResponse.setItems(purchaseOrderDetailResponseList);
				againstPOSearchResponseList.add(againstPOSearchResponse);
			}
		}

		return againstPOSearchResponseList;
	}

	@Override
	@Transactional
	public Object getItemDetailsAgainstPurchaseOrder(List<AgainstPurchaseOrder> againstPurchaseOrderList, Integer orgId,
			Integer unitId) {
		PurchaseOrderResponse purchaseOrderResponseList = new PurchaseOrderResponse();
		List<AgainstPOSearchResponse> response = new ArrayList<AgainstPOSearchResponse>();

		List<Integer> poIds = againstPurchaseOrderList.stream().map(AgainstPurchaseOrder::getPoId)
				.collect(Collectors.toList());
		List<AgainstPOSearchResponse> againstPOSearchResponses = null;
		if (!poIds.isEmpty()) {
			againstPOSearchResponses = GlobalDao.getPurchaseOrderListBP(poIds);
		}

		List<Integer> consumablePoIds = againstPOSearchResponses.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getConsumableId()))
				.map(AgainstPOSearchResponse::getPoId).collect(Collectors.toList());
		List<Integer> fixedPoIds = againstPOSearchResponses.stream()
				.filter(x -> x.getAssetTypeId().equals(Constants.getFixedAssetId()))
				.map(AgainstPOSearchResponse::getPoId).collect(Collectors.toList());
		List<Integer> storeIds = againstPOSearchResponses.stream().map(AgainstPOSearchResponse::getStoreId)
				.collect(Collectors.toList());

		List<Integer> consumableItemIds = new ArrayList<Integer>();
		List<Integer> fixedleItemIds = new ArrayList<Integer>();
		againstPurchaseOrderList.stream().filter(x -> consumablePoIds.contains(x.getPoId()))
				.forEach(a -> consumableItemIds.addAll(a.getItemIds()));
		againstPurchaseOrderList.stream().filter(x -> fixedPoIds.contains(x.getPoId()))
				.forEach(a -> fixedleItemIds.addAll(a.getItemIds()));

		List<PurchaseOrderDetailResponse> poDetailListAll = new ArrayList<PurchaseOrderDetailResponse>();
		if (consumableItemIds != null && !consumableItemIds.isEmpty())
			poDetailListAll = GlobalDao.getItemsDetailsAgainstPOBP(consumablePoIds, consumableItemIds);
		if (fixedleItemIds != null && !fixedleItemIds.isEmpty())
			poDetailListAll.addAll(GlobalDao.getFixedItemsDetailsAgainstPOBP(fixedPoIds, fixedleItemIds));

		List<Integer> poDetailsItemIds = poDetailListAll.stream().map(PurchaseOrderDetailResponse::getItemId)
				.collect(Collectors.toList());

		List<UomResponse> uoms = null;
		if (!poDetailsItemIds.isEmpty())
			uoms = GlobalDao.getUnitOfMeasurementListByItemBP(poDetailsItemIds, orgId);

		List<Integer> poDetailIds = poDetailListAll.stream().map(PurchaseOrderDetailResponse::getId)
				.collect(Collectors.toList());

		if (poDetailIds.size() == 0) {
			poDetailIds.add(0);
		}
		List<PurchaseOrderStaged> poStagedList = GlobalDao.getPurchaseOrderStagedListBP(poDetailIds);
		List<PurchaseOrderStagedDcDate> grnStagedLists = GlobalDao.getPurchaseOrderStagedListDcDateBP(poDetailIds);
		/*
		 * for(PurchaseOrderStagedDcDate sqty : grnStagedLists) {
		 * 
		 * }
		 */

		List<GRNQuantity> grnQuantities = GlobalDao.getGrnReceivedAndApprovedQtySum(poDetailIds);

		// List<POOtherCharges> totalAppliedOtherCharges =
		// GlobalDao.getAppliedOtherChargesForPO(poDetailIds);
		List<GRNAgainstApprovedPOQuantity> storeStockQtys = null;
		List<GRNAgainstApprovedPOQuantity> storeStockTransitQtys = null;
		List<GRNAgainstApprovedPOQuantity> storeMaxQtys = null;
		List<GRNAgainstApprovedPOQuantity> prLeaseQtys = null;

		if (poDetailsItemIds.size() > 0) {
			storeStockQtys = GlobalDao.getStoreStockByItemBP(storeIds, poDetailsItemIds, unitId);
			storeStockTransitQtys = GlobalDao.getStoreStockInTransitByItemBP(storeIds, poDetailsItemIds);
			storeMaxQtys = GlobalDao.getStoreMaxQuantityByItemBP(storeIds, poDetailsItemIds);
			prLeaseQtys = GlobalDao.getPurchaseRequestLeaseQunatity(storeIds, poDetailsItemIds, unitId);

		}

		// start code for HIS-9505 GRN form asset against PO issues - Approve quantity
		// is showing 0
		List<PoAccessoriesMapperGlobal> accessoriesmapperBP = new ArrayList<PoAccessoriesMapperGlobal>();
		if (fixedleItemIds != null && !fixedleItemIds.isEmpty()) {
			accessoriesmapperBP = GlobalDao.getPoAccessoriesMapperBP(poDetailIds);
		}
		// end code for HIS-9505 GRN form asset against PO issues - Approve quantity is
		// showing 0

		for (AgainstPurchaseOrder againstPurchaseOrder : againstPurchaseOrderList) {
			// List<POOtherCharges> poOtherCharges =
			// GlobalDao.getPurchaseOrderOtherChargesMapperBP(againstPurchaseOrder.getPoId());
			// purchaseOrderResponseList.setOtherChargesList(poOtherCharges);
			purchaseOrderResponseList.setOtherChargesList(GlobalDao.getOtherChargesBP(unitId));
			AgainstPOSearchResponse purchaseOrderSearchDto = againstPOSearchResponses.stream()
					.filter(x -> x.getPoId().equals(againstPurchaseOrder.getPoId())).collect(Collectors.toList())
					.get(0);

			List<PurchaseOrderDetailResponse> poDeatailList = new ArrayList<PurchaseOrderDetailResponse>();

			for (Integer itemId : againstPurchaseOrder.getItemIds()) {
				List<PurchaseOrderDetailResponse> poDetailList = poDetailListAll.stream().filter(
						x -> x.getPoId().equals(purchaseOrderSearchDto.getPoId()) && x.getItemId().equals(itemId))
						.collect(Collectors.toList());

				for (PurchaseOrderDetailResponse poDetail : poDetailList) {
					long storeStock = 0;
					long stockInTransit = 0;
					long maxStoreQuantity = 0;
					long prLeaseQty = 0;
					int poGRNQuantity = 0;

					List<GRNQuantity> specificGRNQuantitiy = grnQuantities.stream()
							.filter(x -> x.getPoDetailId().equals(poDetail.getId())).collect(Collectors.toList());
					if (specificGRNQuantitiy != null && !specificGRNQuantitiy.isEmpty())
						poGRNQuantity = specificGRNQuantitiy.get(0).getReceivedAndApprovedQtySum();
					poDetail.setPoGrnQuantity(poGRNQuantity);
					poDetail.setPendingQuantity(poDetail.getPendingQuantity());// HIS-14302 // /HIS-14302
					poDetail.setPoPendingQuantity(poDetail.getPoQuantity() - poDetail.getPoGrnQuantity());
					poDetail.setPoQuantity(poDetail.getPoQuantity() != null ? poDetail.getPoQuantity() : 0);
					poDetail.setBonusQuantity(poDetail.getBonusQuantity() != null ? poDetail.getBonusQuantity() : 0);
					poDetail.setPoLeaseQuantity(
							poDetail.getPoLeaseQuantity() != null ? poDetail.getPoLeaseQuantity() : 0);
					poDetail.setBonusLeaseQuantity(
							poDetail.getBonusLeaseQuantity() != null ? poDetail.getBonusLeaseQuantity() : 0);

					if (uoms != null)
						poDetail.setUom(uoms.stream().filter(x -> x.getItemId().equals(poDetail.getItemId()))
								.collect(Collectors.toList()));

					poDetail.setPoStagedList(poStagedList.stream()
							.filter(x -> x.getPoDetailId().equals(poDetail.getId())).collect(Collectors.toList()));
					poDetail.setGrnStagedLists(grnStagedLists.stream()
							.filter(x -> x.getPoDetailId().equals(poDetail.getId())).collect(Collectors.toList()));

					List<GRNAgainstApprovedPOQuantity> storeStock1 = storeStockQtys.stream()
							.filter(x -> x.getItemId().equals(poDetail.getItemId())
									&& x.getStoreId().equals(purchaseOrderSearchDto.getStoreId()))
							.collect(Collectors.toList());
					List<GRNAgainstApprovedPOQuantity> stockInTransit1 = storeStockTransitQtys.stream()
							.filter(x -> x.getItemId().equals(poDetail.getItemId())
									&& x.getStoreId().equals(purchaseOrderSearchDto.getStoreId()))
							.collect(Collectors.toList());
					List<GRNAgainstApprovedPOQuantity> maxStoreQuantity1 = storeMaxQtys.stream()
							.filter(x -> x.getItemId().equals(poDetail.getItemId())
									&& x.getStoreId().equals(purchaseOrderSearchDto.getStoreId()))
							.collect(Collectors.toList());
					List<GRNAgainstApprovedPOQuantity> prLeaseQuantity = prLeaseQtys.stream()
							.filter(x -> x.getItemId().equals(poDetail.getItemId())
									&& x.getStoreId().equals(purchaseOrderSearchDto.getStoreId()))
							.collect(Collectors.toList());

					if (storeStock1 != null && !storeStock1.isEmpty())
						storeStock = storeStock1.get(0).getQuantity();
					if (stockInTransit1 != null && !stockInTransit1.isEmpty())
						stockInTransit = stockInTransit1.get(0).getQuantity();
					if (maxStoreQuantity1 != null && !maxStoreQuantity1.isEmpty())
						maxStoreQuantity = maxStoreQuantity1.get(0).getIntQuantity();
					if (prLeaseQuantity != null && !prLeaseQuantity.isEmpty())
						prLeaseQty = prLeaseQuantity.get(0).getQuantity();
					long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit + prLeaseQty);

					if (maxOrderQuantity < 0) {
						maxOrderQuantity = 0l;
					}
					poDetail.setMaxStoreQuantity(maxStoreQuantity);
					poDetail.setMaxOrderQuantity(maxOrderQuantity);

					// start code for HIS-9505 GRN form asset against PO issues - Approve quantity
					// is showing 0
					if (fixedleItemIds != null && !fixedleItemIds.isEmpty()) {
						poDetail.setPoAccessoriesMapper(accessoriesmapperBP.stream()
								.filter(x -> x.getPoDetailId().equals(poDetail.getId())).collect(Collectors.toList()));
					}
					// end code for HIS-9505 GRN form asset against PO issues - Approve quantity is
					// showing 0

					poDeatailList.add(poDetail);
				}
				purchaseOrderSearchDto.setItems(poDeatailList);
			}
			if (purchaseOrderSearchDto.getItems() != null && !purchaseOrderSearchDto.getItems().isEmpty()) {
				// start code for HIS-9505 GRN form asset against PO issues - Approve quantity
				// is showing 0
				List<POOtherCharges> otherChargesBP = GlobalDao
						.getPurchaseOrderOtherChargesMapperBP(purchaseOrderSearchDto.getPoId());
				purchaseOrderSearchDto.setOtherCharges(otherChargesBP);
				Double getNetAmount=GlobalDao.getNetAmount(poIds);
				Double getRoundOffAmount=GlobalDao.getRountOffAmount(poIds);
				purchaseOrderSearchDto.setRoundOffAmount(getRoundOffAmount);
				purchaseOrderSearchDto.setNetAmount(getNetAmount);
				// end code for HIS-9505 GRN form asset against PO issues - Approve quantity is
				// showing 0
				response.add(purchaseOrderSearchDto);
			}
		}

		purchaseOrderResponseList.setPurchaseOrderList(response);
		// purchaseOrderResponseList.setOtherChargesList(GlobalDao.getOtherChargesBP());
		purchaseOrderResponseList.setTaxList(GlobalDao.getTaxListBP(orgId));
		return purchaseOrderResponseList;
	}

	private List<POOtherCharges> getOtherChargesListByPoDetailId(Integer id) {
		List<OtherChargesResponse> otherChargesList = new ArrayList<OtherChargesResponse>();
		List<POOtherCharges> poOtherChrgesList = new ArrayList<POOtherCharges>();

		for (PurchaseOrderOtherChargesMapper poOtherChargeMapper : GlobalDao.getPurchaseOrderOtherChargesMapper(id)) {

			if (poOtherChargeMapper.getOtherCharge() != null) {

				POOtherCharges poOtherCharges = new POOtherCharges(poOtherChargeMapper.getId(),
						poOtherChargeMapper.getOtherCharge().getId(),
						poOtherChargeMapper.getOtherCharge().getDescription(), poOtherChargeMapper.getAmount());
				poOtherChrgesList.add(poOtherCharges);
			}

		}
		return poOtherChrgesList;
	}

	private List<PurchaseOrderStaged> getPoStagedList(Integer id) {
		return GlobalDao.getPurchaseOrderStagedList(id);
	}

	@Override
	@Transactional
	public List<FixedAssetType> getFixedAssetTypes() {
		List<FixedAssetType> response = new ArrayList<FixedAssetType>();

		for (com.param.entity.model.master.FixedAssetType fixedAssetType : GlobalDao.getFixedAssetTypes()) {
			response.add(new FixedAssetType(fixedAssetType.getId(), fixedAssetType.getType()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getRacks(Integer storeId, Integer orgId) {
		return GlobalDao.getRacks(storeId, orgId);
	}

	@Override
	@Transactional
	public Object getShelves(Integer rackId) {
		return GlobalDao.getShelves(rackId);
	}

	@Override
	@Transactional
	public Object getAgainstGrn(AgainstGrnRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<AgainstGrnResponse> response = new ArrayList<AgainstGrnResponse>();

		List<AgainstGrnResponse> grnList = GlobalDao.getGrn(request);

		if (grnList != null && grnList.size() > 0) {
			List<AgainstGrnDetailResponse> grnDetails = GlobalDao.getGrnDetails(
					(grnList.stream().map(AgainstGrnResponse::getId).collect(Collectors.toList())),
					request.getAssetTypeID());
			for (AgainstGrnResponse againstGrnResponse : grnList) {
				List<AgainstGrnDetailResponse> details = grnDetails.stream()
						.filter(grn -> grn.getGrnId().equals(againstGrnResponse.getId())).collect(Collectors.toList());

				if (details != null && !details.isEmpty()) {
					for (AgainstGrnDetailResponse detail : details) {
						// detail.setCop(GoodReceiptNoteDetail.get);
						Character isPendingStatus='N';
						Integer isPending =GlobalDao.getIsPendingStatus(detail.getGrnDetailId());
						if(isPending == null || isPending != 1){
							isPendingStatus = 'N';
						}
						else
						{
							isPendingStatus='Y';
						}
						
						detail.setIsApprovalPending(isPendingStatus);
                        Double actualDiscountPercentage=(detail.getTotalDiscount()*100)/detail.getTotalAmount();
                        detail.setActualDiscountPercentage(actualDiscountPercentage);
						List<ReturnableGatepassDetail> rgpDetails = GlobalDao
								.getReturnableGatepassDetails(detail.getGrnDetailId());

						Integer returnQuantity = rgpDetails.stream()
								.filter(rgp -> rgp.getApprovedQty() == null || rgp.getApprovedQty().equals(0))
								.mapToInt(qty -> qty.getReturnQty()).sum();

						Integer approvedQuantity = rgpDetails.stream()
								.filter(rgp -> rgp.getApprovedQty() != null || rgp.getApprovedQty() > 0)
								.mapToInt(qty -> qty.getApprovedQty()).sum();
						detail.setApprovedQuantity(approvedQuantity);
						Long avaQty = 0l;
						if (detail.getAvailableQuantity() != null) {
							Long phyRejQty = 0l;

							if (detail.getPhyRejectQuantity() != null) {
								phyRejQty = detail.getPhyRejectQuantity();
							}
							detail.setAvailableQuantity(phyRejQty);
							avaQty = detail.getAvailableQuantity();
						}

						/*
						 * start code to resolved HIS-9736 Returnable Gatepass - Edit mode shows the
						 * accepted quantity at physical inspection as available quantity
						 */
						if (request.getAssetTypeID().equals(com.param.common.constants.Constants.consumableId)) {
							List<UomResponse> uomList = GlobalDao.getBatchUomList(detail.getBatchId());
							detail.setUomList(uomList);
						}
						/*
						 * end code to resolved HIS-9736 Returnable Gatepass - Edit mode shows the
						 * accepted quantity at physical inspection as available quantity
						 */
                         List<ReturnableGatepassDetail> getAvailQty = GlobalDao.getAvailQty(detail.getGrnDetailId());
                         Integer approvedQuantity1 = getAvailQty.stream()
 								.filter(rgp -> rgp.getApprovedQty() != null || rgp.getApprovedQty() > 0)
 								.mapToInt(qty -> qty.getApprovedQty()).sum();
                         detail.setAvailableQuantity((detail.getPhyRejectQuantity()!=null ? detail.getPhyRejectQuantity() :0)-approvedQuantity1);
						if ((returnQuantity + approvedQuantity) < detail.getApprovedQuantity()+ (detail.getGrnRejectQuantity()!=null ?
							detail.getGrnRejectQuantity(): (detail.getPhyRejectQuantity()!=null ? detail.getPhyRejectQuantity() : 0.00))) {
//							detail.setTotalGRNQuantity(detail.getPhyRejectQuantity()+detail.getPendingQuantity());
							if(detail.getAvailableQuantity()!=0)
							{
								againstGrnResponse.addItem(detail);
							}
						} else if (avaQty > (approvedQuantity)) {
							if(detail.getAvailableQuantity()!=0)
							{
//								detail.setTotalGRNQuantity(detail.getPhyRejectQuantity()+detail.getPendingQuantity());
								againstGrnResponse.addItem(detail);
							}
						}
					}

					if (againstGrnResponse.getItems() != null && againstGrnResponse.getItems().size() > 0) {
						response.add(againstGrnResponse);
					}
				}
			}
		}

		return response;
	}

	// for Barcode Printing get approval qty

	@Override

	@Transactional
	public Object getBarcodeAgainstGrn(AgainstGrnRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<AgainstGrnResponse> response = new ArrayList<AgainstGrnResponse>();
		
		List<AgainstGrnDetailResponse> bonusBatchExpiry = new ArrayList<AgainstGrnDetailResponse>(); 
		List<AgainstGrnDetailResponse> uniqueBonusBatch = new ArrayList<AgainstGrnDetailResponse>();
		List<AgainstGrnResponse> grnList = GlobalDao.getGrn(request);
		boolean isPRNRequired=GlobalDao.isPRNRequired(request.getUnitId());
		String pharmacyPRNFormat=GlobalDao.pharmacyPRNFormat(request.getUnitId());
		boolean isManualBarcodeRequired = GlobalDao.isManualBarcodeRequired(request.getUnitId());
		
		if (grnList != null && !grnList.isEmpty()) {
			List<AgainstGrnDetailResponse> grnDetails = GlobalDao.getBarcodeGrnDetails(
					(grnList.stream().map(AgainstGrnResponse::getId).collect(Collectors.toList())),
					request.getAssetTypeID());
			
			for(AgainstGrnResponse detailsRes1 : grnList){
				
				bonusBatchExpiry = GlobalDao.getBonusBatchDetails(detailsRes1.getId() != null ? detailsRes1.getId():0);
				int prevGrnDetailId = 0;
				for(AgainstGrnDetailResponse detailsRes : grnDetails){
					
					if(bonusBatchExpiry.size()>0 &&  prevGrnDetailId != detailsRes.getGrnDetailId()){
						prevGrnDetailId = detailsRes.getGrnDetailId();
						for(AgainstGrnDetailResponse batchRes2 : uniqueBonusBatch){
							if(uniqueBonusBatch.size()>0){
								bonusBatchExpiry.remove(batchRes2);
							}
						}
					for(AgainstGrnDetailResponse batchRes : bonusBatchExpiry){
						if((detailsRes.getBatchNo() != null && detailsRes.getBatchNo().trim() != "")  && (batchRes.getBatchNo() != null && batchRes.getBatchNo().trim() != "")){
							if(!(batchRes.getBatchNo().equalsIgnoreCase(detailsRes.getBatchNo()))){
								
								batchRes.setBatchId(batchRes.getBatchIdForBarcode().longValue());
								batchRes.setPendingQuantity(batchRes.getPendingQuan().longValue());
								batchRes.setAvailableQuantity(batchRes.getAvailQuan().longValue());

									uniqueBonusBatch.add(batchRes);

						}
					  }
					}
				  }
				}
				bonusBatchExpiry.clear();
			  }
			
			
			
			for(AgainstGrnDetailResponse batchRes1 : uniqueBonusBatch){
				if(uniqueBonusBatch.size()>0){
					grnDetails.add(batchRes1);
				}
			}
			
			for (AgainstGrnResponse againstGrnResponse : grnList) {
				againstGrnResponse.setIsPRNRequired(isPRNRequired);
				againstGrnResponse.setPharmacyPRNFormat(pharmacyPRNFormat);
				againstGrnResponse.setIsManualBarcodeRequired(isManualBarcodeRequired);

				List<AgainstGrnDetailResponse> details = grnDetails.stream()
						.filter(grn -> grn.getGrnId().equals(againstGrnResponse.getId())).collect(Collectors.toList());
				
				if (details != null && !details.isEmpty()) {
					againstGrnResponse.setItems(details);
					response.add(againstGrnResponse);
				}
			}
		}
		return response;
	}

	@Override
	@Transactional
	public List<ScrapReason> getScrapReasonList(Integer orgId) {
		List<ScrapReason> response = new ArrayList<ScrapReason>();

		List<ReasonMaster> reasonList = GlobalDao.getreasonDropdownListByTypeId(orgId, 19);

		for (ReasonMaster scrapReason : reasonList) {
			response.add(new ScrapReason(scrapReason.getReasonMasterId(), scrapReason.getReasonCode(),
					scrapReason.getReasonDesc()));

		}
		return response;
	}

	@Override
	@Transactional
	public List<User> inWardBySearch(String name, Integer unitId, Integer orgId) {
		List<User> response = new ArrayList<User>();

		for (UserMasterDto userObj : GlobalDao.getUserByName(name, unitId, orgId)) {
			response.add(new User(userObj.getUserId(), userObj.getUserName()));
		}
		return response;
	}

	public static <T extends UomEntity> Integer GetLeaseQuantity(Integer quantity, List<T> uomList, UomType uomType) {
		int conversion = 1;
		boolean calculate = false;

		uomList.sort(new Comparator<T>() {
			@Override
			public int compare(T uom1, T uom2) {
				if (uom1.getUomType().getId().equals(uom2.getUomType().getId())) {
					return 0;
				}

				return uom1.getUomType().getId() < uom2.getUomType().getId() ? -1 : 1;
			}
		});

		for (T uom : uomList) {
			if (uom.getUomType().getId().equals(uomType.getId()) || calculate) {
				calculate = true;
				conversion = conversion * uom.getConversions();
			}
		}

		return quantity * conversion;
	}

	@Override
	@Transactional
	public ItemDropdownResponse getItemDropdowns(Integer unitId, Integer orgId) {

		ItemDropdownResponse response = new ItemDropdownResponse();

		for (com.param.entity.model.master.AssetType assetType : GlobalDao.getAssetTypeList(0, 0)) {
			response.addAssetType(new com.param.global.dto.global.AssetType(assetType.getId(), assetType.getType()));
		}

		for (PharmacologicalClassification pharmacologicalClassification : GlobalDao
				.getPharmacologicalClassificationList(unitId)) {
			response.addPharmacologicalClassification(new com.param.global.dto.generic.PharmacologicalClassification(
					pharmacologicalClassification.getId(), pharmacologicalClassification.getClassification()));
		}

		for (Manufacturer manufacturer : GlobalDao.getManufacturerList(orgId)) {
			response.addManufacturer(
					new com.param.global.dto.generic.Manufacturer(manufacturer.getId(), manufacturer.getName()));
		}

		for (StrengthUnit strengthUnit : GlobalDao.getStrengthUnitList(orgId)) {
			response.addStrengthUnit(
					new com.param.global.dto.generic.StrengthUnit(strengthUnit.getId(), strengthUnit.getUnits()));
		}

		for (StorageUnit storageUnit : GlobalDao.getStorageUnitList(orgId)) {
			response.addStorageUnit(
					new com.param.global.dto.generic.StorageUnit(storageUnit.getId(), storageUnit.getUnitName()));
		}

		for (Tax tax : GlobalDao.getTaxList(orgId)) {
			response.addTax(new com.param.global.dto.generic.Tax(tax.getId(), tax.getName()));
		}
		for (com.param.entity.model.master.FormulationType formulationType : GlobalDao.getFormulationTypeList(orgId)) {
			response.addFormulationType(new com.param.global.dto.global.FormulationType(formulationType.getId(),
					formulationType.getType()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<TaxComponentDropdown> getTaxComponentList() {
		List<TaxComponentDropdown> response = new ArrayList<TaxComponentDropdown>();

		for (TaxComponent taxComponent : GlobalDao.getTaxComponentList()) {
			if (!taxComponent.getIsDeleted()) {
				response.add(new TaxComponentDropdown(taxComponent.getId(), taxComponent.getComponent()));
			}
		}
		return response;
	}

	@Override
	@Transactional
	public List<FormulationTypeAndROAList> getFormulationTypeAndROADropdown() {
		List<FormulationTypeAndROAList> response = new ArrayList<FormulationTypeAndROAList>();

		for (RouteOfAdministration Route : GlobalDao.getFormulationTypeAndROADropdown()) {
			response.add(new FormulationTypeAndROAList(Route.getId(), Route.getDescription()));
		}
		return response;
	}

	@Override
	@Transactional
	public StorewiseGenericMapperDropdown getStorewiseGenericDropdown(Integer unitId, Integer orgId) {
		StorewiseGenericMapperDropdown response = new StorewiseGenericMapperDropdown();

		for (com.param.entity.model.master.AssetCategory assetCategory : GlobalDao.getAssetCategory(orgId)) {
			response.addAssetCategory(new AssetCategory(assetCategory.getId(), assetCategory.getCategory()));
		}
		for (com.param.entity.model.master.Store store : GlobalDao.getStoreList(unitId)) {
			response.addStore(new Store(store.getStoreId(), store.getStoreName()));
		}

		for (Ved ved : GlobalDao.getVedList()) {
			response.addVedResponseList(new VedResponseList(ved.getId(), ved.getName(), ved.getCode()));
		}
		return response;
	}

	@Override
	// @Transactional
	public String getBillNoByGeneratedCode(int storeId, Integer unitId, Integer screenId) {

		return iGlobalUnitMasterDao.getGeneratedNo(storeId, unitId, screenId);
	}

	@Override
	@Transactional
	public List<PurchaseOrderHistory> getPurchaseOrderHistoryByItemId(Integer itemId, Integer unitId, Integer poId) {
		List<PurchaseOrderHistory> poHistoryList = new ArrayList<PurchaseOrderHistory>();
		for (PurchaseOrderDetail poDetail : GlobalDao.getPurchaseOrderHistory(itemId, unitId)) {
//			if (poDetail.getPurchaseOrder().getId().equals(poId)) {
				PurchaseOrderHistory poHistory = new PurchaseOrderHistory(poDetail.getPurchaseOrder().getPoNumber(),
						poDetail.getPurchaseOrder().getAddedDate(),
						poDetail.getGrnDetail() != null ? poDetail.getGrnDetail().getGoodReceiptNote().getGrnNumber()
								: null,
						poDetail.getGrnDetail() != null ? poDetail.getGrnDetail().getGoodReceiptNote().getAddedDate()
								: null,
						poDetail.getGrnDetail() != null
								? poDetail.getGrnDetail().getGoodReceiptNote().getInvoiceNumber()
								: null,
						poDetail.getGrnDetail() != null ? poDetail.getGrnDetail().getBatchNo() : null,
						poDetail.getItem().getName(), poDetail.getPurchaseOrder().getVendor().getName(),
						poDetail.getGrnDetail() != null ? poDetail.getPoQuantity() : null,
						poDetail.getGrnDetail() != null ? poDetail.getGrnDetail().getGrnApprovedQuantity() : null,
						poDetail.getBonusQuantity(), poDetail.getBonusQuantity(), poDetail.getCop());
				poHistory.setMrp(poDetail.getMrp() != null ? poDetail.getMrp() : null);
				if(poHistoryList.size()<=10 && poDetail.getGrnDetail() != null && poDetail.getGrnDetail().getGoodReceiptNote().getGrnNumber()!=null)
				{
					poHistoryList.add(poHistory);
				}
//			}

		}

		return poHistoryList;
	}

	@Override
	public Integer getGrnQuantity(Integer poDetailId) {
		PurchaseOrderDetail poDetails = GlobalDao.getPurchaseOrderDetailsById(poDetailId);

		List<GoodReceiptNoteDetail> grnDetailsList = poDetails.getGrnDetailsList();
		Integer grnReceivedQuantity = grnDetailsList.stream()
				.filter(grnDetails -> grnDetails.getGrnApprovedQuantity() == null)
				.mapToInt(grnQty -> grnQty.getGrnReceivedQuantity()).sum();

		Integer grnApprovedQuantity = grnDetailsList.stream()
				.filter(grnDetails -> grnDetails.getGrnApprovedQuantity() != null)
				.mapToInt(grnQty -> grnQty.getGrnApprovedQuantity()).sum();

		return grnReceivedQuantity + grnApprovedQuantity;
	}

	@Override
	public PurchaseOrderDetail getPurchaseOrderDetail(Integer poDetailId) {
		return GlobalDao.getPurchaseOrderDetailsById(poDetailId);
	}

	@Override
	@Transactional
	public String manageStockConsumption(StoreStockRequestDto store) {
		try {
			// com.param.entity.model.master.User user = new
			// com.param.entity.model.master.User(store.getUserId());

			// get batch needs to be issued
			Batch batch = GlobalDao.getBatchForStockConsumption(store.getStoreeId(), store.getBatchId(),
					store.getUnitId()/* storeId, batchId */);
			batch.setUpdatedByUser(store.getUserId());
			batch.setUpdatedDate(TimeZoneComponent.getDateByzone(store.getUnitId()));

			// Get existing batch store stock
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(store.getStoreeId(), batch.getId(), 0);// 0
																													// itemId

			// Get lease quantity
			Integer leaseQuantity = Util.GetLeaseQuantity(store.getConsumedQuantity(), batch.getBatchUomList(),
					new UomType(store.getUomTypeId()));

			if (avaialableStoreStock < leaseQuantity)
				throw new ServiceException("Consumed quantity is greater than available quantity");

			// Calculate store stock balance quantity
			Integer balanceQuantity = avaialableStoreStock - leaseQuantity;

			// Deduct issued quantity from the batch store stock
			batch.getStoreStockList().get(0).setAvailableQty(balanceQuantity);
			batch.getStoreStockList().get(0).setUpdatedByUser(store.getUserId());
			batch.getStoreStockList().get(0).setUpdatedDate(TimeZoneComponent.getDateByzone(store.getUnitId()));
			// Calculate current batch quantity

			// Set current batch quantity
			batch.setCurrentQuantity(batch.getCurrentQuantity() - leaseQuantity);

			com.param.entity.model.master.Screen screen = GlobalDao.getScreen(store.getScreenId());
			screen.setUpdatedByUser(store.getUserId());

			StockTransaction stockTransaction = new StockTransaction(balanceQuantity, screen, screen.getCode(),
					leaseQuantity, batch.getStoreStockList().get(0).getStore(), 0);

			stockTransaction.setUpdatedByUser(store.getUserId());
			stockTransaction.setAddedDate(TimeZoneComponent.getDateByzone(store.getUnitId()));
			stockTransaction.setUpdatedByUser(store.getUserId());
			stockTransaction.setUpdatedDate(TimeZoneComponent.getDateByzone(store.getUnitId()));
			stockTransaction.setUnit(new Unit(store.getUnitId()));
			stockTransaction.setOrganization(batch.getOrganization());

			// Add stock transaction
			batch.addStockTransaction(stockTransaction);

			return "Stock and batch is updated successfully";
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Integer updatePharamcyDetailsPaidAmount(Integer saleId, Double amount, Integer status, Integer entitlementId, Double amt)
			throws BillingException {
		return GlobalDao.updatePharamcyDetailsPaidAmount(saleId, amount, status,entitlementId, amt);
	}

	@Override
	@Transactional
	public List<PMSFrequency> getFrequencyList(Integer unitId) {
		List<com.param.entity.model.inventory.PMSFrequency> response = new ArrayList<com.param.entity.model.inventory.PMSFrequency>();
		for (PMSFrequency frequency : GlobalDao.getPMSFrequency(unitId)) {
			response.add(new com.param.entity.model.inventory.PMSFrequency(frequency.getId(), frequency.getCode(),
					frequency.getDescription()));
		}
		return response;

	}

	@Override
	@Transactional
	public DirectPurchaseRequestResponse getItemDetailsDirectPurchaseRequestWithPendingQty(
			AgainstPurchaseRequest againstPurchaseRequest) {

		DirectPurchaseRequestResponse directPoResponse = new DirectPurchaseRequestResponse();

		long count = ItemsDao.productCategoryCount(againstPurchaseRequest.getItemIds());
		List<ItemsDetailsListResponse> response = null;
		if (count == 0) {
			response = ItemsDao.itemsDetails(againstPurchaseRequest.getItemIds(), againstPurchaseRequest.getStoreId(),
					againstPurchaseRequest.getUnitId());
		} else if (count != 0 && count > 0) {
			response = ItemsDao.itemsDetailsFixedAsset(againstPurchaseRequest.getItemIds(),
					againstPurchaseRequest.getStoreId());
		}

		List<Integer> itemIds = response.stream().map(ItemsDetailsListResponse::getItemId).collect(Collectors.toList());
		List<UomResponse> uoms = null;
		if (itemIds.size() > 0) {
			uoms = GlobalDao.getUnitOfMeasurementListByItemBettoerPerformance(itemIds,
					againstPurchaseRequest.getOrganizationId());
		}
		// List<ItemQuantitiesByStoreId> quantities =
		// GlobalDao.getQuantities(againstPurchaseRequest.getStoreId(), itemIds);

		List<Quantities> storeStocks = GlobalDao.getStoreStockByItemBP(againstPurchaseRequest.getStoreId(), itemIds,
				againstPurchaseRequest.getUnitId());
		List<Quantities> stockInTransits = GlobalDao.getStoreStockInTransitByItemBP(againstPurchaseRequest.getStoreId(),
				itemIds, againstPurchaseRequest.getUnitId());
		List<Quantities> lastMonthSales = GlobalDao.getLastMonthSaleBP(againstPurchaseRequest.getStoreId(), itemIds,
				againstPurchaseRequest.getUnitId());
		List<Quantities> maxStoreQuantitys = GlobalDao.getStoreMaxQuantityByItemBP(againstPurchaseRequest.getStoreId(),
				itemIds, againstPurchaseRequest.getOrganizationId());
		List<Quantities> currentMonthSales = GlobalDao.getCurrentMonthSaleBP(againstPurchaseRequest.getStoreId(),
				itemIds, againstPurchaseRequest.getUnitId());

		List<Quantities> approvedPRAndPOQuantities = ItemsDao.getApprovedPRandPOQuantityList(
				againstPurchaseRequest.getStoreId(), againstPurchaseRequest.getItemIds(),
				againstPurchaseRequest.getUnitId());
		List<Quantities> approvedPOQuantities = ItemsDao.getApprovedPurchaseOrderQuantityList(
				againstPurchaseRequest.getStoreId(), againstPurchaseRequest.getItemIds(),
				againstPurchaseRequest.getUnitId());

		for (ItemsDetailsListResponse item : response) {
			Long storeStock = 0l;
			Long stockInTransit = 0l;
			Long lastMonthSale = 0l;
			Long currentMonthSale = 0l;
			Integer maxStoreQuantity = 0;
			Long approvedPRAndPOQuantity = 0l;
			Long approvedPOQuantity = 0l;

			if (storeStocks.stream().filter(x -> x.getItemId().equals(item.getItemId()))
					.collect(Collectors.toList()) != null
					&& !storeStocks.stream().filter(x -> x.getItemId().equals(item.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {

				storeStock = storeStocks.stream().filter(x -> x.getItemId().equals(item.getItemId()))
						.collect(Collectors.toList()).get(0).getQuantity();
			}
			if (stockInTransits.stream().filter(x -> x.getItemId().equals(item.getItemId()))
					.collect(Collectors.toList()) != null
					&& !stockInTransits.stream().filter(x -> x.getItemId().equals(item.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				stockInTransit = stockInTransits.stream().filter(x -> x.getItemId().equals(item.getItemId()))
						.collect(Collectors.toList()).get(0).getQuantity();
			}
			if (lastMonthSales.stream().filter(x -> x.getItemId().equals(item.getItemId()))
					.collect(Collectors.toList()) != null
					&& !lastMonthSales.stream().filter(x -> x.getItemId().equals(item.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				lastMonthSale = lastMonthSales.stream().filter(x -> x.getItemId().equals(item.getItemId()))
						.collect(Collectors.toList()).get(0).getQuantity();
			}
			if (currentMonthSales.stream().filter(x -> x.getItemId().equals(item.getItemId()))
					.collect(Collectors.toList()) != null
					&& !currentMonthSales.stream().filter(x -> x.getItemId().equals(item.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				currentMonthSale = currentMonthSales.stream().filter(x -> x.getItemId().equals(item.getItemId()))
						.collect(Collectors.toList()).get(0).getQuantity();
			}
			if (maxStoreQuantitys.stream().filter(x -> x.getItemId().equals(item.getItemId()))
					.collect(Collectors.toList()) != null
					&& !maxStoreQuantitys.stream().filter(x -> x.getItemId().equals(item.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				maxStoreQuantity = maxStoreQuantitys.stream().filter(x -> x.getItemId().equals(item.getItemId()))
						.collect(Collectors.toList()).get(0).getIntQuantity();
			}
			if (approvedPRAndPOQuantities.stream().filter(x -> x.getItemId().equals(item.getItemId()))
					.collect(Collectors.toList()) != null
					&& !approvedPRAndPOQuantities.stream().filter(x -> x.getItemId().equals(item.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				approvedPRAndPOQuantity = approvedPRAndPOQuantities.stream()
						.filter(x -> x.getItemId().equals(item.getItemId())).collect(Collectors.toList()).get(0)
						.getQuantity();
			}
			if (approvedPOQuantities.stream().filter(x -> x.getItemId().equals(item.getItemId()))
					.collect(Collectors.toList()) != null
					&& !approvedPOQuantities.stream().filter(x -> x.getItemId().equals(item.getItemId()))
							.collect(Collectors.toList()).isEmpty()) {
				approvedPOQuantity = approvedPOQuantities.stream().filter(x -> x.getItemId().equals(item.getItemId()))
						.collect(Collectors.toList()).get(0).getQuantity();
			}

			// Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit +
			// approvedPRAndPOQuantity + approvedPOQuantity);
			Long maxOrderQuantity = GlobalDao.getMaxOrderQuantity(maxStoreQuantity, storeStock, stockInTransit,
					approvedPRAndPOQuantity, approvedPOQuantity);
			if (maxOrderQuantity < 0) {
				maxOrderQuantity = 0L;
			}

			item.setStockAvailable(storeStock);
			item.setStockInTransit(stockInTransit);
			item.setLastMonthSale(lastMonthSale);
			item.setCurrentMonthSale(currentMonthSale);
			item.setMaxStoreQuantity(maxStoreQuantity);
			item.setMaxOrderQuantity(maxOrderQuantity);

			if (againstPurchaseRequest.getIsPoOrGrn() == 1) {
				// Ganesh get last mrp and last uom
				GoodReceiptNoteDetailDto goodReceiptNoteDetailDto = GlobalDao.getLastGrnuomandMrp(
						againstPurchaseRequest.getOrganizationId(), againstPurchaseRequest.getUnitId(),
						item.getItemId());
				item.setGoodReceiptNoteDetailDto(goodReceiptNoteDetailDto);
			} else if (againstPurchaseRequest.getIsPoOrGrn() == 2) {
				GoodReceiptNoteDetailDto goodReceiptNoteDetailDto = GlobalDao.getLastPouomandMrp(
						againstPurchaseRequest.getOrganizationId(), againstPurchaseRequest.getUnitId(),
						item.getItemId());
				item.setGoodReceiptNoteDetailDto(goodReceiptNoteDetailDto);
			}
			item.setUom(uoms.stream().filter(x -> x.getItemId().equals(item.getItemId())).collect(Collectors.toList()));

		}

		directPoResponse.setItemDetailsList(response);
		// directPoResponse.setOtherChargesList(getOtherCharges());
		// directPoResponse.setTaxList(getTaxList());
		directPoResponse.setOtherChargesList(GlobalDao.getOtherChargesBP(againstPurchaseRequest.getUnitId()));
		directPoResponse.setTaxList(GlobalDao.getTaxListBP(againstPurchaseRequest.getOrganizationId()));
		return directPoResponse;
	}

	@Override
	@Transactional
	public Object getAllVendorList(Integer unitId,String vendorName) {

		List<VendorResponseDto> vendorList = new ArrayList<VendorResponseDto>();
		for (Vendor vendor : GlobalDao.getAllVendorList(unitId, vendorName)) {
			if (vendor.getIsActive() == true && vendor.getIsDeleted() == false) {
				VendorResponseDto vendorResponse = new VendorResponseDto(vendor.getId(),
						vendor.getCode() + "|" + vendor.getName() + "|" + vendor.getAddress(), vendor.getCode(),
						vendor.getName(), vendor.getAddress());
				vendorList.add(vendorResponse);
			}
		}
		return vendorList;

	}

	@Override
	@Transactional
	public void cancelPrescription(Integer prescriptionId) throws CustomException {
		Prescription prescription = GlobalDao.getPrescription(prescriptionId);

		if (prescription == null)
			throw new CustomException("Prescription not found");

		if (!prescription.getIsValidate()
				&& prescription.getStatus().getId().equals(com.param.common.constants.Status.New.getStatus()))
			prescription.setStatus(
					new com.param.entity.model.master.Status(com.param.common.constants.Status.Cancelled.getStatus()));
	}

	@Override
	@Transactional
	public List<Patient> billListPatientSearch(String name, Integer unitId) {

		List<Patient> patientDetailsList = new ArrayList<Patient>();

		List<PatientSearchDetails> patients = GlobalDao.billListPatientSearch(name, unitId);
		for (PatientSearchDetails patient : patients) {
			String patientName = patient.getPrefixCode() + " " + patient.getFirstName() + " " + patient.getMiddleName()
					+ " " + patient.getLastName();
			String uhid = patient.getUhidNumber();
			Integer age = null;
			if (patient.getDob() != null) {
				age = Util.getAgeByBirthDate(patient.getDob().toLocalDateTime().toLocalDate());
			}

			String patientDetails = uhid + " | " + patientName + " | " + age + " | " + patient.getGenderCode() + " | "
					+ patient.getMobileNumber();
			Patient patientDetail = new Patient(patient.getPatientId(), patientName, uhid, patientDetails);
			patientDetailsList.add(patientDetail);
		}
		return patientDetailsList;
	}

	@Override
	@Transactional
	public List<Patient> patientSearchIssueReturn(Integer unitId, String name, Integer visitType) {

		List<Patient> patientDetailsList = new ArrayList<Patient>();

		List<PatientSearchDetails> patients = new ArrayList<PatientSearchDetails>();
		if (visitType.equals(VisitTypeConstants.OP)) {
			patients = GlobalDao.getOPPatients(name, unitId);
		}
		if (visitType.equals(VisitTypeConstants.IP) || visitType.equals(VisitTypeConstants.DC)
				|| visitType.equals(VisitTypeConstants.ER)) {
			patients = GlobalDao.getIPDCERPatientsIssueReturn(name, visitType, unitId);
		}
		for (PatientSearchDetails patient : patients/* GlobalDao.patientSearchByName(name) */) {
			String patientName = patient.getPrefixCode() + " " + patient.getFirstName() + " " + patient.getMiddleName()
					+ " " + patient.getLastName();
			String uhid = patient.getUhidNumber();
			Integer age = null;
			if (patient.getDob() != null) {
				age = Util.getAgeByBirthDate(patient.getDob().toLocalDateTime().toLocalDate());
			}

			String patientDetails = uhid + " | " + patientName + " | " + age + " | " + patient.getGenderCode() + " | "
					+ patient.getMobileNumber();
			Patient patientDetail = new Patient(patient.getPatientId(), patientName, uhid, patientDetails);
			patientDetailsList.add(patientDetail);
		}
		return patientDetailsList;
	}

	@Override
	@Transactional
	public List<Patient> patientSearchPatientIndent(Integer unitId, String name, Integer visitType) {

		List<Patient> patientDetailsList = new ArrayList<Patient>();

		List<PatientSearchDetails> patients = new ArrayList<PatientSearchDetails>();
		if (visitType.equals(VisitTypeConstants.OP)) {
			patients = GlobalDao.getOPPatients(name, unitId);
		}
		if (visitType.equals(VisitTypeConstants.IP) || visitType.equals(VisitTypeConstants.DC)
				|| visitType.equals(VisitTypeConstants.ER)) {
			patients = GlobalDao.getIPDCERPatientsPatientIndent(name, visitType, unitId);
		}
		for (PatientSearchDetails patient : patients/* GlobalDao.patientSearchByName(name) */) {
			String patientName = patient.getPrefixCode() + " " + patient.getFirstName() + " " + patient.getMiddleName()
					+ " " + patient.getLastName();
			String uhid = patient.getUhidNumber();
			Integer age = null;
			if (patient.getDob() != null) {
				age = Util.getAgeByBirthDate(patient.getDob().toLocalDateTime().toLocalDate());
			}

			String patientDetails = uhid + " | " + patientName + " | " + age + " | " + patient.getGenderCode() + " | "
					+ patient.getMobileNumber();
			Patient patientDetail = new Patient(patient.getPatientId(), patientName, uhid, patientDetails);
			patientDetailsList.add(patientDetail);
		}
		return patientDetailsList;
	}

	@Override
	@Transactional
	public List<Patient> salesReturnPatientSearchByUhid(String uhid, Integer visitType, Integer orgId) {

		List<Patient> patientDetailsList = new ArrayList<Patient>();
		for (PatientSearchDetails patient : GlobalDao.salesReturnPatientSearchByUhid(uhid, orgId)) {
			String patientName = patient.getPrefixCode() + " " + patient.getFirstName() + " " + patient.getMiddleName()
					+ " " + patient.getLastName();
			String uhidNumber = patient.getUhidNumber();
			Integer age = Util.getAgeByBirthDate(patient.getDob().toLocalDateTime().toLocalDate());
			String patientDetails = uhidNumber + " | " + patientName + " | " + age + " | " + patient.getGenderCode()
					+ " | " + patient.getMobileNumber();
			Patient patientDetail = new Patient(patient.getPatientId(), patientName, uhidNumber, patientDetails);
			patientDetailsList.add(patientDetail);
		}
		return patientDetailsList;
	}

	@Override
	@Transactional
	public List<TariffResponse> getTariffData(TariffRequest tariffReq) {
		List<TariffResponse> tariffResp = new ArrayList<TariffResponse>();
		if (tariffReq != null) {
			/* prodCatIds = GlobalDao.getItemProdCategories(tariffReq.getItemIds()); */
			for (TariffQueryData tariff : tariffReq.getItems()) {
				List<Integer> prodCatId = new ArrayList<Integer>();
				prodCatId.add(tariff.getProdCategoryId());
				List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatId, tariffReq.getEntitlementId());
				if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
					if (pharmacyTariffs.size() > 1) {
						// throw exception?
						// Don't throw - get first object and set data.
					}

					TariffResponse resp = new TariffResponse();
					PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
					resp.setMarkUpDown(pharmacyTariff.getMarkUpDown());
					resp.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
					resp.setItemId(tariff.getItemId());
					resp.setProdCatId(tariff.getProdCategoryId());
					tariffResp.add(resp);
				}
			}
		}
		return tariffResp;
	}

	@Override
	//@Transactional
	public PharmacyBillCommonDto updateSale(PharmacyBillCommonDto dto) {
		Map<Integer, BigDecimal> sum = new HashMap<>();
		Sale sale = GlobalDao.getSaleById(dto.getId().longValue());
		List<PharamcyBillPayeeDetails> pharamcyBillPayeeDetails = sale.getBillPayeeList();
		Double discount = 0.00;
		Double concession = 0.00;
		Double totalAmt = 0.00;
		Double netAmt = 0.00;
		Double taxAmt = 0.00;
		Double totalSelf = 0.00;
		Double totalCredit = 0.00;
		Double totalPackageConsumedAmt = 0.00;
		Map<Integer, BigDecimal> packageSum = new HashMap<>();
		List<SaleDetail> saleDetailsList = new ArrayList<SaleDetail>();
		Map<Integer, BigDecimal> contractSum = new HashMap<>();
		Map<Map<Integer, Integer>, BigDecimal> contractSumAssociate = new HashMap<>();

		for (CommonBillingItemsDto item : dto.getItemList()) {
			SaleDetail saleDetail = GlobalDao.getSaleDetailsById(item.getId().longValue());

			saleDetail.setRate(item.getRate());
			saleDetail.setTotalAmount(item.getTotalAmount());
			saleDetail.setDiscountPercentage(item.getDiscountPercentage());
			saleDetail.setDiscountAmount(item.getDiscountAmount());
			saleDetail.setUnitDiscountAmount(item.getUnitDiscountAmount());
			saleDetail.setSelfPayable(item.getSelfPayable());
			saleDetail.setCreditPayable(item.getCreditPayable());
			saleDetail.setPackageConsumptionAmt(item.getPackageConsumptionAmt());
			saleDetail.setConsessionAmount(item.getConsessionAmount());
			saleDetail.setConsessionPercentage(item.getConsessionPercentage());
			saleDetail.setUnitConsessionAmount(item.getUnitConsessionAmount());
			saleDetail.setPayeeId(item.getPayeeId());
			saleDetail.setNetAmount(item.getNetAmount());
			saleDetail.setTaxAmount(item.getTaxAmount());
			saleDetail.setSaleDetailPackageMapperList(null);
			saleDetail.setSaleDetailPayeeMapperList(null);
			List<SaleDetailPayeeMapperDto> payeeMapper = item.getSaleDetailPayeeMapperDtoList();
			List<SaleDetailPackageMapperDto> packageMpprDto = item.getSaleDetailPackageMapperDtoList();
			if (payeeMapper != null && payeeMapper.size() > 0) {
				for (SaleDetailPayeeMapperDto sdto : payeeMapper) {
					SaleDetailPayeeMapper mpper = new SaleDetailPayeeMapper();
					mpper.setOrganizationId(dto.getOrganizationId() != null ? dto.getOrganizationId()
							: sessionService.getOrganizationId());
					mpper.setUnitId(dto.getUnitId() != null ? dto.getUnitId() : sessionService.getUnitId());
					mpper.setItemId(item.getItemId());
					mpper.setStatus('A');
					mpper.setContractId(sdto.getContractId());
					mpper.setPayeeId(sdto.getPayeeId());
					mpper.setAssociateCompanyId(sdto.getAssociateCompanyId());
					mpper.setPayeeAmount(sdto.getPayeeAmount());
					mpper.setQuantity(sdto.getQuantity());
					mpper.setSaleDetail(saleDetail);
					saleDetail.addSaleDetailPayeeMapper(mpper);

					if (mpper.getAssociateCompanyId() != null) {
						Map<Integer, Integer> newAssociatePayee = new HashMap<>();
						newAssociatePayee.put(dto.getPayeeId(), sdto.getAssociateCompanyId());
						if (contractSumAssociate.containsKey(newAssociatePayee)) {
							contractSumAssociate.put(newAssociatePayee,
									contractSumAssociate.get(newAssociatePayee).add(mpper.getPayeeAmount()));
						} else {
							contractSumAssociate.put(newAssociatePayee, mpper.getPayeeAmount());
						}
					} else {
						if (contractSum.containsKey(mpper.getPayeeId())) {
							contractSum.put(mpper.getPayeeId(),
									contractSum.get(mpper.getPayeeId()).add(mpper.getPayeeAmount()));
						} else {
							contractSum.put(mpper.getPayeeId(), mpper.getPayeeAmount());
						}
					}

				}
			}
			if (packageMpprDto != null && packageMpprDto.size() > 0) {
				for (SaleDetailPackageMapperDto pkgDto : packageMpprDto) {
					SaleDetailPackageMapper mpper = new SaleDetailPackageMapper();
					mpper.setOrganizationId(dto.getOrganizationId() != null ? dto.getOrganizationId()
							: sessionService.getOrganizationId());
					mpper.setUnitId(dto.getUnitId() != null ? dto.getUnitId() : sessionService.getUnitId());
					mpper.setItemId(item.getItemId());
					mpper.setStatus('A');
					mpper.setIsPackageItem(pkgDto.getIsPackageItem());
					mpper.setPackageId(pkgDto.getPackageId());
					mpper.setPackageConsumptionMasterId(pkgDto.getPackageConsumptionMasterId());
					mpper.setPacakgeConsumptionServiceDetailId(pkgDto.getPacakgeConsumptionServiceDetailId());
					mpper.setPackageConsumptionCapDetailsId(pkgDto.getPackageConsumptionCapDetailsId());
					mpper.setPackageConsumptionEODetailId(pkgDto.getPackageConsumptionEODetailId());
					mpper.setPackageAmount(BigDecimal.valueOf(pkgDto.getPackageAmount()));
					mpper.setPackageQty(pkgDto.getPackageQty());
					mpper.setCapOrQty(pkgDto.getCapOrQty());
					mpper.setSaleDetail(saleDetail);
					mpper.setRate(BigDecimal.valueOf(pkgDto.getRate()));
					saleDetail.addSaleDetailPackageMapper(mpper);

					if (packageSum.containsKey(mpper.getPackageConsumptionMasterId())) {
						packageSum.put(mpper.getPackageConsumptionMasterId(),
								packageSum.get(mpper.getPackageConsumptionMasterId()).add(mpper.getPackageAmount()));
					} else {
						packageSum.put(mpper.getPackageConsumptionMasterId(), mpper.getPackageAmount());
					}

				}
			}
			discount = discount + saleDetail.getDiscountAmount();
			concession = concession + saleDetail.getConsessionAmount();
			totalAmt = totalAmt + saleDetail.getTotalAmount();
			netAmt = netAmt + saleDetail.getNetAmount();
			taxAmt = taxAmt + saleDetail.getTaxAmount();
			totalSelf = totalSelf + saleDetail.getSelfPayable();
			totalCredit = totalCredit + saleDetail.getCreditPayable();
			totalPackageConsumedAmt = totalPackageConsumedAmt + saleDetail.getPackageConsumptionAmt();
			saleDetailsList.add(saleDetail);
		}

		sale.setTotalDiscountAmount(discount);
		sale.setDiscountCategory(dto.getDiscountCategoryId() != null
				? new com.param.entity.model.master.DiscountCategory(dto.getDiscountCategoryId())
				: null);
		sale.setDoctor(dto.getDoctorId() != null ? new com.param.entity.model.doctor.Doctor(dto.getDoctorId()) : null);
		sale.setDiscountType(dto.getDiscountTypeId() != null ? new DiscountType(dto.getDiscountTypeId()) : null);
		sale.setNetAmount(netAmt);
		sale.setOutstandingAmount(netAmt);
		sale.setPaidAmount(0.00);
		sale.setPatient(new PatientRegistration(dto.getPatientId()));
		sale.setTaxAmount(taxAmt);
		sale.setTotalAmount(totalAmt);
		sale.setVisitType(new VisitType(dto.getVisitTypeId()));
		sale.setTotalSelfPayable(totalSelf);
		sale.setTotalCreditPayable(totalCredit);
		sale.setCounterId(sessionService.getCounterId());
		sale.setConcession(concession);
		if(sale.getTotalSelfPayable()>0)
		{
			sale.setStatus(new com.param.entity.model.master.Status(com.param.common.constants.Status.Pending.getStatus()));
		}
		if(sale.getTotalSelfPayable()==0 &&sale.getTotalCreditPayable()>0)
		{
			sale.setStatus(new com.param.entity.model.master.Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
		}
		List<PharamcyBillPayeeDetails> listToBeUpdated = new LinkedList<>();
		if (contractSum != null && contractSum.size() > 0) {
			for (PharamcyBillPayeeDetails payeeDetails : pharamcyBillPayeeDetails) {
				if (contractSum.containsKey(payeeDetails.getPayeeId())) {
					if (!(payeeDetails.getDueAmount()
							.equals(contractSum.get(payeeDetails.getPayeeId()).doubleValue()))) {
						payeeDetails.setDueAmount(totalSelf);
						payeeDetails.setBillAmount(totalSelf);

						listToBeUpdated.add(payeeDetails);
					}
				}
			}
		}
		if (contractSumAssociate != null && contractSumAssociate.size() > 0) {
			for (PharamcyBillPayeeDetails payeeDetails : pharamcyBillPayeeDetails) {
				Map<Integer, Integer> newMap = new HashMap<>();
				newMap.put(payeeDetails.getPayeeId(), payeeDetails.getAssociateCompanyId());

				if (contractSumAssociate.containsKey(newMap)) {
					if (!(payeeDetails.getDueAmount().equals(contractSumAssociate.get(newMap).doubleValue()))) {
						payeeDetails.setDueAmount(contractSum.get(payeeDetails.getPayeeId()).doubleValue());
						payeeDetails.setBillAmount(contractSum.get(payeeDetails.getPayeeId()).doubleValue());

						listToBeUpdated.add(payeeDetails);
					}
				}
			}
		}
		sale.setSaleDetailsList(saleDetailsList);
		GlobalDao.updateSale(sale);
		dto.setSale(sale);
		// GlobalDao.updateSaleBatchUpdate(sale);
		return dto;
	}

	@Override
	@Transactional
	public Response getPatientNameSearchByName(PatientDetailRequest patientDetailsSearch) {
		try {
			return GlobalDao.getPatientNameSearchByName(patientDetailsSearch);
		} catch (Exception ex) {
		throw new ApplicationException(ex.getMessage(), ex);
	}
		
	}

	@Override
	public List<Batch> getBatchDetailsGlobal(Integer id, Integer unitId, BatchProperty batchProperty, int storeId) {

		try {

			List<Batch> batchListModified = new ArrayList<>();
			Map<String, Batch> batchMap = new HashMap<>();
			List<Batch> batchList = GlobalDao.getBatchDetails(id, unitId, storeId, batchProperty);
			if (batchList != null && batchList.size() > 0) {
				Integer count = 0;
				for (Batch batch : batchList) {

					if (batch.getBatchNo() == null) {
						if (batchMap.containsKey(batch.getBatchNo())) {
							// Batch batchObj = batchMap.get(batch.getBatchNo());
							Batch batchObj = batchMap.get(batch.getBatchNo());
							Batch bBatchClone = (Batch) batchObj.clone();
							bBatchClone.setCurrentQuantity(batchObj.getCurrentQuantity() + batch.getCurrentQuantity());
							bBatchClone.setQuantity(batchObj.getQuantity() + batch.getQuantity());
							bBatchClone.setSellingPrice(batchObj.getSellingPrice() + batch.getSellingPrice());
							bBatchClone
									.setRejectedQuantity(batchObj.getRejectedQuantity() + batch.getRejectedQuantity());
							bBatchClone
									.setReturnedQuantity(batchObj.getReturnedQuantity() + batch.getReturnedQuantity());
							bBatchClone.setRate(batchObj.getRate() + batch.getRate());
							count = count + 1;
							batchMap.put(batch.getBatchNo(), bBatchClone);
						} else {
							batchMap.put(batch.getBatchNo(), batch);
							count = count + 1;
						}
					} else {
						batchListModified.add(batch);
					}
					// for get batchwise stock
					if (batch.getStoreStockList() != null && batch.getStoreStockList().size() > 0) {
						batch.setCurrentQuantity(batch.getStoreStockList().get(0).getAvailableQty() != null
								? batch.getStoreStockList().get(0).getAvailableQty()
								: 0);
					}
				}
				if (batchMap != null && !batchMap.isEmpty()) {
					Batch batchObj = batchMap.get(null);
					batchObj.setSellingPrice(GlobalCommonDateUtils.getAmount(batchObj.getSellingPrice() / count));
					batchObj.setRate(GlobalCommonDateUtils.getAmount(batchObj.getRate() / count));
					batchObj.setBatchNo(ICommonConstants.NOT_APPLICABLE);
					// batchObj.setId(new Long(0));
					batchListModified.add(batchObj);
				}
			}
			// return batchListModified;
			return batchList;
		} catch (Exception ex) {
		logger.error("getBatchDetailsGlobal failed", ex);
		return null;
		}
	}

	@Override
	@Transactional
	public Object getItemDetailsAgainstQuotationRequestByVendorId(
			List<AgainstQuotationRequest> againstQuotationRequestList, Integer vendorId, Integer orgId,
			Integer unitId) {

		AgainstQuotationRequestResponse againstPurchaseReq = new AgainstQuotationRequestResponse();
		List<QuotationRequestSearchDto> response = new ArrayList<QuotationRequestSearchDto>();
		for (AgainstQuotationRequest againstPurchaseRequest : againstQuotationRequestList) {
			QuotationRequest quotationRequest = GlobalDao.getQuotationRequestList(againstPurchaseRequest.getQrId());

			QuotationRequestSearchDto quotationRequestSearchDto = new QuotationRequestSearchDto(
					quotationRequest.getId(), quotationRequest.getQrNo(), quotationRequest.getAddedDate());

			List<QuotationRequestDetailResponse> qrDeatailList = new ArrayList<QuotationRequestDetailResponse>();

			for (Integer Id : againstPurchaseRequest.getItemIds()) {
				if (quotationRequest.getAssetType().getId().equals(Constants.getConsumableId())) {
					for (QuotationRequestDetailResponse quotationReqDetail : GlobalDao
							.getItemsDetailsAgainstQr(quotationRequest.getId(), Id)) {
						Long storeStock = GlobalDao.getStoreStockByItem(quotationReqDetail.getStoreId(),
								quotationReqDetail.getItemId(), unitId);

						Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(quotationReqDetail.getStoreId(),
								quotationReqDetail.getItemId());

						Long maxStoreQuantity = (long) GlobalDao.getStoreMaxQuantityByItem(
								quotationReqDetail.getStoreId(), quotationReqDetail.getItemId(), orgId);

						Long prLeaseQuantity = GlobalDao.getPurchaseRequestLeaseQunatity(
								quotationReqDetail.getStoreId(), quotationReqDetail.getItemId(), unitId);

						Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit + prLeaseQuantity);

						quotationReqDetail.setPendingPOqty(
								quotationReqDetail.getPendingPOqty() != null ? quotationReqDetail.getPendingPOqty()
										: 0);

						quotationReqDetail.setQrQuantity(
								quotationReqDetail.getQrQuantity() != null ? quotationReqDetail.getQrQuantity() : 0);
						quotationReqDetail.setUom(
								GlobalDao.getUnitOfMeasurementListByItem(quotationReqDetail.getItemId(), orgId));
						quotationReqDetail.setMaxStoreQuantity(maxStoreQuantity);
						quotationReqDetail.setMaxOrderQuantity(maxOrderQuantity);
						quotationReqDetail.setStockAvailable(storeStock);
						// quotationReqDetail.get

						quotationReqDetail.setVendorCop(GlobalDao.getVendorCop(vendorId, quotationReqDetail.getId()));
						qrDeatailList.add(quotationReqDetail);
					}
				}
				if (quotationRequest.getAssetType().getId().equals(Constants.getFixedAssetId())) {
					for (QuotationRequestDetailResponse quotationedReqDetail : GlobalDao
							.getFixedItemsDetailsAgainstQr(quotationRequest.getId(), Id)) {

						Long storeStock = GlobalDao.getStoreStockByItem(quotationedReqDetail.getStoreId(),
								quotationedReqDetail.getItemId(), unitId);

						Long stockInTransit = GlobalDao.getStoreStockInTransitByItem(quotationedReqDetail.getStoreId(),
								quotationedReqDetail.getItemId());

						Long maxStoreQuantity = (long) GlobalDao.getStoreMaxQuantityByItem(
								quotationedReqDetail.getStoreId(), quotationedReqDetail.getItemId(), orgId);

						Long prLeaseQuantity = GlobalDao.getPurchaseRequestLeaseQunatity(
								quotationedReqDetail.getStoreId(), quotationedReqDetail.getItemId(), unitId);

						Long maxOrderQuantity = maxStoreQuantity - (storeStock + stockInTransit + prLeaseQuantity);

						quotationedReqDetail.setQrQuantity(
								quotationedReqDetail.getQrQuantity() != null ? quotationedReqDetail.getQrQuantity()
										: 0);
						quotationedReqDetail.setMaxStoreQuantity(maxStoreQuantity);
						quotationedReqDetail.setMaxOrderQuantity(maxOrderQuantity);
						quotationedReqDetail.setStockAvailable(storeStock);
						quotationedReqDetail
								.setVendorCop(GlobalDao.getVendorCop(vendorId, quotationedReqDetail.getId()));
						qrDeatailList.add(quotationedReqDetail);
					}
				}

				quotationRequestSearchDto.setItems(qrDeatailList);
			}
			if (quotationRequestSearchDto.getItems() != null && !quotationRequestSearchDto.getItems().isEmpty()) {
				response.add(quotationRequestSearchDto);
			}
		}
		againstPurchaseReq.setQuotationRequestList(response);
		againstPurchaseReq.setOtherChargesList(getOtherCharges(unitId));
		againstPurchaseReq.setTaxList(getTaxList(orgId));
		// System.out.println(againstPurchaseReq.toString());
		return againstPurchaseReq;

	}

	@Override
	@Transactional
	public Object getAgainstrjectedGrn(AgainstGrnRequest request) {

		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<AgainstGrnResponse> response = new ArrayList<AgainstGrnResponse>();

		List<AgainstGrnResponse> grnList = GlobalDao.getGrn(request);

		if (grnList != null && grnList.size() > 0) {
			List<AgainstGrnDetailResponse> grnDetails = GlobalDao.getGrnDetails(
					(grnList.stream().map(AgainstGrnResponse::getId).collect(Collectors.toList())),
					request.getAssetTypeID());
			for (AgainstGrnResponse againstGrnResponse : grnList) {
				List<AgainstGrnDetailResponse> details = grnDetails.stream()
						.filter(grn -> grn.getGrnId().equals(againstGrnResponse.getId())).collect(Collectors.toList());

				if (details != null && !details.isEmpty()) {
					for (AgainstGrnDetailResponse detail : details) {

						List<ReturnableGatepassDetail> rgpDetails = GlobalDao
								.getReturnableGatepassDetails(detail.getGrnDetailId());

						Integer returnQuantity = rgpDetails.stream()
								.filter(rgp -> rgp.getApprovedQty() == null || rgp.getApprovedQty().equals(0))
								.mapToInt(qty -> qty.getReturnQty()).sum();

						Integer approvedQuantity = rgpDetails.stream()
								.filter(rgp -> rgp.getApprovedQty() != null || rgp.getApprovedQty() > 0)
								.mapToInt(qty -> qty.getApprovedQty()).sum();
						Long avaQty = 0l;
						if (detail.getAvailableQuantity() != null) {
							avaQty = detail.getAvailableQuantity();
						}

						if ((returnQuantity + approvedQuantity) < avaQty) {
							againstGrnResponse.addItem(detail);
						} else if ((returnQuantity + approvedQuantity) == avaQty) {
							againstGrnResponse.addItem(detail);
						}
					}

					if (againstGrnResponse.getItems() != null && againstGrnResponse.getItems().size() > 0) {
						response.add(againstGrnResponse);
					}
				}
			}
		}

		return response;

	}

	@Override
	@Transactional
	public PatientDetailResponse patientIndentPatientSearchIndentForNursing(PatientDetailRequest patientDetailsSearch)
			throws Exception {

		PatientDetailResponse patientDetails = new PatientDetailResponse();
		PatientRegistration patient = GlobalDao.getPatient(patientDetailsSearch.getPatientId());
		String patientName = patient.getPrefix().getPrefixCode() + " " + patient.getFirstName() + " "
				+ patient.getMiddleName() + " " + patient.getLastName();
		Integer age = Util.getAgeByBirthDate(patient.getDob().toLocalDateTime().toLocalDate());
		String patientDetail = patientName + " | " + age + " | " + patient.getGender().getGenderCode();
		patientDetails = new PatientDetailResponse(patient.getPatientId(), patientName, patientDetail,
				patient.getUhidNumber());
		VisitType visitType = GlobalDao.getVisitType(patientDetailsSearch.getVisitType());
		
		if (visitType != null) {
			patientDetails.setVisitTypeId(visitType.getVisitTypeId());
			patientDetails.setVisitType(visitType.getVisitTypeCode());
		}
		List<VisitDetails> visitDetails = new ArrayList<VisitDetails>();

		if (patientDetailsSearch.getVisitType() == 1) {
			List<Encounter> encounters = GlobalDao.encounterPatient(patientDetailsSearch,
					patientDetailsSearch.getUnitId());
			if (encounters != null && !encounters.isEmpty()) {
				for (Encounter encounter : encounters) {
					VisitDetails visitDetail = new VisitDetails(
							encounter.getDoctor() != null ? encounter.getDoctor().getDoctorId() : null,
							encounter.getEncounterNumber(), encounter.getEncounterDate());
					visitDetail.setEncounterId(encounter.getEncounterId());
					visitDetail.setAddedDate(encounter.getUpdatedDate());
					visitDetails.add(visitDetail);
					
					Integer isFinalBill=GlobalDao.getIsFinalBill(encounter.getEncounterId(),null);
					if(isFinalBill!=null && isFinalBill==2)
					{
						patientDetails.setIsFinalBill('Y');
					}
					else
					{
						patientDetails.setIsFinalBill('N');
					}
				}
			} else
				throw new CustomException("Encounters not found for the patient");
		}

		if (patientDetailsSearch.getVisitType() == 2 || patientDetailsSearch.getVisitType() == 4
				|| patientDetailsSearch.getVisitType() == 3) {
			Admission admission = GlobalDao.admissionPatientForNursing(patientDetailsSearch,
					patientDetailsSearch.getUnitId());
			// patientDetailsS
			if (admission != null) {

				Integer drId = 0;
				if (patientDetailsSearch.getVisitType() == 4) {

				} else {
					if (admission.getDoctor() != null) {
						if (admission.getDoctor().getDoctorId() == null) {

						} else {
							drId = admission.getDoctor().getDoctorId();
						}

					}
				}

				VisitDetails visitDetail = new VisitDetails(drId, admission.getAdmissionNumber());
				visitDetail.setAdmissionId(admission.getAdmissionId());
				visitDetail.setAddedDate(admission.getUpdatedDate());
				visitDetails.add(visitDetail);
				if (admission.getAdmissionDetail() != null) {
					patientDetails.setWardId(admission.getAdmissionDetail().get(0).getWardMaster().getWardId());
					patientDetails.setWardName(admission.getAdmissionDetail().get(0).getWardMaster().getWardName());
					patientDetails.setBedId(admission.getAdmissionDetail().get(0).getBedMaster().getBedId());
					patientDetails.setBedNo(admission.getAdmissionDetail().get(0).getBedMaster().getBedNumber());
				}
				
				Integer isFinalBill=GlobalDao.getIsFinalBill(null,admission.getAdmissionId());
				if(isFinalBill!=null && isFinalBill==2)
				{
					patientDetails.setIsFinalBill('Y');
				}
				else
				{
					patientDetails.setIsFinalBill('N');
				}
				
			} else
				throw new CustomException("Admission not found for the patient");
		}

		patientDetails.setVisitDetails(visitDetails);
		return patientDetails;

	}

	@Override
	@Transactional
	public List<User> inWardBySearchUser(Integer organizationId, Integer unitId, String name)
			throws ApplicationException {
		List<User> response = new ArrayList<User>();
		for (UserMasterDto userObj : GlobalDao.inWardBySearchUser(organizationId, unitId, name)) {
			response.add(new User(userObj.getUserId(), userObj.getUserName()));
		}
		return response;
	}

	@Override
	@Transactional
	public Object getAgainstGrnReturn(AgainstGrnRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<AgainstGrnResponse> response = new ArrayList<AgainstGrnResponse>();

		List<AgainstGrnResponse> grnList = GlobalDao.getGrn(request);

		if (grnList != null && grnList.size() > 0) {
			List<AgainstGrnDetailResponse> grnDetails = GlobalDao.getApprovedGrnDetails(
					(grnList.stream().map(AgainstGrnResponse::getId).collect(Collectors.toList())),
					request.getAssetTypeID());
			for (AgainstGrnResponse againstGrnResponse : grnList) {
				List<AgainstGrnDetailResponse> details = grnDetails.stream()
						.filter(grn -> grn.getGrnId().equals(againstGrnResponse.getId())).collect(Collectors.toList());

				if (details != null && !details.isEmpty()) {
					for (AgainstGrnDetailResponse detail : details) {
						// detail.setCop(GoodReceiptNoteDetail.get);
						Character isPendingStatus='N';
						Integer isPending =GlobalDao.getIsPendingStatus(detail.getGrnDetailId());
						if(isPending == null || isPending != 1){
							isPendingStatus = 'N';
						}
						else
						{
							isPendingStatus='Y';
						}
						
						detail.setIsApprovalPending(isPendingStatus);
						Double actualDiscountPercentage=(detail.getTotalDiscount()*100)/detail.getTotalAmount();
						detail.setActualDiscountPercentage(actualDiscountPercentage);
						List<ReturnableGatepassDetail> rgpDetails = GlobalDao
								.getReturnableGatepassDetails(detail.getGrnDetailId());

						Integer returnQuantity = rgpDetails.stream()
								.filter(rgp -> rgp.getApprovedQty() == null || rgp.getApprovedQty().equals(0))
								.mapToInt(qty -> qty.getReturnQty()).sum();

						Integer approvedQuantity = rgpDetails.stream()
								.filter(rgp -> rgp.getApprovedQty() != null || rgp.getApprovedQty() > 0)
								.mapToInt(qty -> qty.getApprovedQty()).sum();
						detail.setApprovedQuantity(approvedQuantity);
						Long avaQty = 0l;
						if (detail.getAvailableQuantity() != null) {
							Long phyRejQty = 0l;

							if (detail.getPhyRejectQuantity() != null) {
								phyRejQty = detail.getPhyRejectQuantity();
							}
							// detail.setAvailableQuantity(detail.getAvailableQuantity() + phyRejQty);
							avaQty = detail.getAvailableQuantity();
						}

						/*
						 * start code to resolved HIS-9736 Returnable Gatepass - Edit mode shows the
						 * accepted quantity at physical inspection as available quantity
						 */
						if (request.getAssetTypeID().equals(com.param.common.constants.Constants.consumableId)) {
							List<UomResponse> uomList = GlobalDao.getBatchUomList(detail.getBatchId());
							detail.setUomList(uomList);
		                    List<GrnBonusBatchExpiry> getList= GlobalDao.getGrnBonusBatchExpiryByGrnDetailId(detail.getGrnDetailId());
		                    List<GrnBonusBatchExpiry> getList3=new ArrayList<GrnBonusBatchExpiry>();
		                    for (GrnBonusBatchExpiry getList1 : getList) {
		                        GrnBonusBatchExpiry getList2=new GrnBonusBatchExpiry();
		                        getList2.setBatchNo(getList1.getBatchNo());
		                        getList2.setBonusQuantity(getList1.getBonusQuantity());
		                        getList2.setExpiryDate(getList1.getExpiryDate());
		                        getList2.setGrnBonusBatchExpiryId(getList1.getGrnBonusBatchExpiryId());
		                        getList2.setBatchId(getList1.getBatchId());
		                        Integer returnedQty=GlobalDao.getSumOfReturnedQty(getList2.getBatchId());
		                        getList2.setReturnedQuantity(returnedQty);
		                        getList3.add(getList2);
		                    }
		                    detail.setGrnBonusBatchExpiry(getList3);
		                    if(detail.getGrnBonusBatchExpiry().size()>0)
		                    {
		                        for (GrnBonusBatchExpiry bonusBatches : detail.getGrnBonusBatchExpiry()) {
		                        	if(bonusBatches.getBonusQuantity()!=null && bonusBatches.getBonusQuantity()>0)
		                        	{
		                        	if(detail.getBonusUomTypeId().equals(1))
									{
		                        		bonusBatches.setBonusQuantity(bonusBatches.getBonusQuantity()*(detail.getUomList().get(0).getConversion()*detail.getUomList().get(1).getConversion()));
									}
									if(detail.getBonusUomTypeId().equals(2))
									{
										bonusBatches.setBonusQuantity(bonusBatches.getBonusQuantity()*(detail.getUomList().get(1).getConversion()));
									}
//									if(detail.getBatchId().equals(bonusBatches.getBatchId()))
//									{
//										detail.setTotalGRNQuantity(detail.getTotalGRNQuantity()+(bonusBatches.getBonusQuantity()!=null ? bonusBatches.getBonusQuantity():0));
//									}
		                        	}
		                        }
		                    }
		                    else
		                    {
		                    	if( detail.getBonusUomTypeId() != null && detail.getBonusUomTypeId().equals(1))
								{
	                        		detail.setBonusApprovedQuantity(detail.getBonusApprovedQuantity()*(detail.getUomList().get(0).getConversion()*detail.getUomList().get(1).getConversion()));
								}
								if( detail.getBonusUomTypeId() != null && detail.getBonusUomTypeId().equals(2))
								{
									detail.setBonusApprovedQuantity(detail.getBonusApprovedQuantity()*(detail.getUomList().get(1).getConversion()));
								}
//								detail.setTotalGRNQuantity(detail.getTotalGRNQuantity()+(detail.getBonusApprovedQuantity()!=null ? detail.getBonusApprovedQuantity():0));
		                    }
							
						}
						if (request.getAssetTypeID().equals(com.param.common.constants.Constants.fixedAssetId)) {
                            List<GrnBonusBatchExpiry> getList= GlobalDao.getGrnBonusBatchExpiryByGrnDetailId(detail.getGrnDetailId());
                            List<GrnBonusBatchExpiry> getList3=new ArrayList<GrnBonusBatchExpiry>();
                            for (GrnBonusBatchExpiry getList1 : getList) {
                                GrnBonusBatchExpiry getList2=new GrnBonusBatchExpiry();
                                getList2.setBatchNo(getList1.getBatchNo());
                                getList2.setBonusQuantity(getList1.getBonusQuantity());
                                getList2.setExpiryDate(getList1.getExpiryDate());
                                getList2.setGrnBonusBatchExpiryId(getList1.getGrnBonusBatchExpiryId());
                                getList2.setBatchId(getList1.getBatchId());
                                Integer returnedQty=GlobalDao.getSumOfReturnedQty(getList2.getBatchId());
                                getList2.setReturnedQuantity(returnedQty);
                                getList3.add(getList2);
                            }
                            detail.setGrnBonusBatchExpiry(getList3);
						}
						/*
						 * end code to resolved HIS-9736 Returnable Gatepass - Edit mode shows the
						 * accepted quantity at physical inspection as available quantity
						 */
						if(detail.getAvailableQuantity()!=0)
						{
							againstGrnResponse.addItem(detail);
						}
					}

					if (againstGrnResponse.getItems() != null && againstGrnResponse.getItems().size() > 0) {
						response.add(againstGrnResponse);
					}
				}
			}
		}

		return response;
	}

	@Override
	@Transactional
	public List<Batch> getBatchDetailsGlobalWalkInPrescription(Integer id, Integer unitId, BatchProperty batchProperty,
			Integer storeId) {
		try {

			List<Batch> batchListModified = new ArrayList<>();
			Map<String, Batch> batchMap = new HashMap<>();
			List<Batch> batchList = GlobalDao.getBatchDetailsWalkInPrescription(id, unitId, storeId, batchProperty);
			if (batchList != null && batchList.size() > 0) {
				Integer count = 0;
				for (Batch batch : batchList) {

					if (batch.getBatchNo() == null) {
						if (batchMap.containsKey(batch.getBatchNo())) {
							// Batch batchObj = batchMap.get(batch.getBatchNo());
							Batch batchObj = batchMap.get(batch.getBatchNo());
							Batch bBatchClone = (Batch) batchObj.clone();
							bBatchClone.setCurrentQuantity(batchObj.getCurrentQuantity() + batch.getCurrentQuantity());
							bBatchClone.setQuantity(batchObj.getQuantity() + batch.getQuantity());
							bBatchClone.setSellingPrice(batchObj.getSellingPrice() + batch.getSellingPrice());
							bBatchClone
									.setRejectedQuantity(batchObj.getRejectedQuantity() + batch.getRejectedQuantity());
							bBatchClone
									.setReturnedQuantity(batchObj.getReturnedQuantity() + batch.getReturnedQuantity());
							bBatchClone.setRate(batchObj.getRate() + batch.getRate());
							count = count + 1;
							batchMap.put(batch.getBatchNo(), bBatchClone);
						} else {
							batchMap.put(batch.getBatchNo(), batch);
							count = count + 1;
						}
					} else {
						batchListModified.add(batch);
					}
					// for get batchwise stock
					if (batch.getStoreStockList() != null && batch.getStoreStockList().size() > 0) {
						batch.setCurrentQuantity(batch.getStoreStockList().get(0).getAvailableQty() != null
								? batch.getStoreStockList().get(0).getAvailableQty()
								: 0);
					}
				}
				if (batchMap != null && !batchMap.isEmpty()) {
					Batch batchObj = batchMap.get(null);
					batchObj.setSellingPrice(GlobalCommonDateUtils.getAmount(batchObj.getSellingPrice() / count));
					batchObj.setRate(GlobalCommonDateUtils.getAmount(batchObj.getRate() / count));
					batchObj.setBatchNo(ICommonConstants.NOT_APPLICABLE);
					// batchObj.setId(new Long(0));
					batchListModified.add(batchObj);
				}
			}
			// return batchListModified;
			return batchList;
		} catch (Exception ex) {
		throw new ApplicationException(ex.getMessage(), ex);
	}
	}

}
