package com.param.pharmacy.ip.service.rejectedRejectionConsumable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.RejectDetail;
import com.param.entity.model.pharmacy.RejectedRejection;
import com.param.entity.model.pharmacy.RejectedRejectionDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.pendingIssues.acceptance.medication.IPendingIssuesAcceptanceMedicationDao;
import com.param.pharmacy.ip.dao.rejectedRejectionConsumable.IRejectedRejectionConsumableDao;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSaveItems;
import com.param.pharmacy.ip.dto.rejectedRejection.RejectedRejectionDetails;
import com.param.pharmacy.ip.dto.rejectedRejection.RejectedRejectionItem;
import com.param.pharmacy.ip.dto.rejectedRejectionConsumable.RejectedRejectionConsumableSearch;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("rejectedRejectionConsumableService")
@Transactional
public class RejectedRejectionConsumableService implements IRejectedRejectionConsumableService {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private IRejectedRejectionConsumableDao rejectedRejectionConsumableDao;

	@Autowired
	private IPendingIssuesAcceptanceMedicationDao pendingIssuesAcceptanceMedicationDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	private Integer screenId = Screen.RejectedRejection.getScreen();

	@Override
	@Transactional
	public CommonDropdown getDropdownList(Integer unitId) {
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<RejectedRejectionDetails> rejectedRejectionConsumableSearch(
			RejectedRejectionConsumableSearch rejectedRejectionSearch) {
		rejectedRejectionSearch.setToDate((new DateTime(rejectedRejectionSearch.getToDate())).plusDays(1).toDate());
		List<RejectedRejectionDetails> rejectedRejectionDetailList = new ArrayList<RejectedRejectionDetails>();
		for (RejectedRejection rejectedRejection : rejectedRejectionConsumableDao
				.rejectedRejectionConsumableSearch(rejectedRejectionSearch, screenId)) {
			if (rejectedRejection.getIssue().getPatientIndent() != null) {
			if ((rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission() :rejectedRejection.getIssue().getAdmission() )!= null) {
				String wardName = "";
				String bedNumber = "";
				AdmissionDetail admissionDetail = GlobalDao.getPatientWardBedDetails(
						rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission().getAdmissionId(): rejectedRejection.getIssue().getAdmission().getAdmissionId());
				if (admissionDetail != null) {
					WardMasters wardMaster = admissionDetail.getWardMaster();
					if (wardMaster != null) {
						wardName = wardMaster.getWardName();
					}
					BedMaster bedMaster = admissionDetail.getBedMaster();
					if (bedMaster != null) {
						bedNumber = bedMaster.getBedNumber();
					}
				}
				Integer age = Util.getAgeByBirthDate(rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission()
						.getPatient().getDob().toLocalDateTime().toLocalDate() : rejectedRejection.getIssue().getAdmission()
						.getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission().getPatient().getPrefix()
						.getPrefixCode() : rejectedRejection.getIssue().getAdmission().getPatient().getPrefix()
						.getPrefixCode() ;
						patientDetail=patientDetail+ " " + (rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission().getPatient().getFirstName()  : rejectedRejection.getIssue().getAdmission().getPatient().getFirstName())+ " "
						+ (rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission().getPatient().getMiddleName():rejectedRejection.getIssue().getAdmission().getPatient().getMiddleName()) + " "
						+ (rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission().getPatient().getLastName() : rejectedRejection.getIssue().getAdmission().getPatient().getLastName()) + " | "
						+ age + " | "
						+ (rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission().getPatient().getGender()
								.getGenderCode() : rejectedRejection.getIssue().getAdmission().getPatient().getGender()
								.getGenderCode())
						+ " | " +  (rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission().getPatient().getUhidNumber() : rejectedRejection.getIssue().getAdmission().getPatient().getUhidNumber());

				Integer ageStringDet = Util.getAgeByBirthDate(rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission()
						.getPatient().getDob().toLocalDateTime().toLocalDate() : rejectedRejection.getIssue().getAdmission()
						.getPatient().getDob().toLocalDateTime().toLocalDate());

				String patientName = (rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission().getPatient().getPrefix()
						.getPrefixCode() : rejectedRejection.getIssue().getAdmission().getPatient().getPrefix()
						.getPrefixCode() )+ " "
						+( rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission().getPatient().getFirstName() : rejectedRejection.getIssue().getAdmission().getPatient().getFirstName() )+ " "
						+ (rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission().getPatient().getMiddleName() :  rejectedRejection.getIssue().getAdmission().getPatient().getMiddleName() ) + " "
						+( rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission().getPatient().getLastName():rejectedRejection.getIssue().getAdmission().getPatient().getLastName() );

				RejectedRejectionDetails rejectedRejectionDetail = new RejectedRejectionDetails(
						rejectedRejection.getId(), rejectedRejection.getRejectNo(), rejectedRejection.getAddedDate(),
						patientDetail,
						( rejectedRejection.getReject()!=null ?rejectedRejection.getReject().getIssue().getAdmission().getPatient().getUhidNumber() : rejectedRejection.getIssue().getAdmission().getPatient().getUhidNumber()),
						( rejectedRejection.getReject()!=null ?rejectedRejection.getReject().getIssue().getVisitType().getVisitTypeCode() : rejectedRejection.getIssue().getVisitType().getVisitTypeCode()),
						 ( rejectedRejection.getReject()!=null ?rejectedRejection.getReject().getIssue().getAdmission().getAdmissionNumber() : rejectedRejection.getIssue().getAdmission().getAdmissionNumber()), patientName,
						ageStringDet);
				rejectedRejectionDetail.setStore(rejectedRejection.getIssue().getStore().getStoreName());
				rejectedRejectionDetail.setWardName(!wardName.equals("") ? wardName : null);
				rejectedRejectionDetail.setBedNo(!bedNumber.equals("") ? bedNumber : null);
				rejectedRejectionDetail.setStatus(rejectedRejection.getStatus().getStatus());
				rejectedRejectionDetail.setStatusId(rejectedRejection.getStatus().getId());
				rejectedRejectionDetail.setGenderCode(rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission()
						.getPatient().getGender().getGenderCode():rejectedRejection.getIssue().getAdmission()
						.getPatient().getGender().getGenderCode());
				rejectedRejectionDetail.setGenderId(
						rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getAdmission().getPatient().getGender().getGenderId():rejectedRejection.getIssue().getAdmission().getPatient().getGender().getGenderId());
				rejectedRejectionDetail.setImagePath(
						rejectedRejection.getReject()!=null ?	rejectedRejection.getReject().getIssue().getAdmission().getPatient().getImagePath() : rejectedRejection.getIssue().getAdmission().getPatient().getImagePath());
				rejectedRejectionDetail.setStore( rejectedRejection.getReject()!=null ?rejectedRejection.getReject().getIssue().getStore().getStoreName():rejectedRejection.getIssue().getStore().getStoreName());
				rejectedRejectionDetail.setRequestFrom(rejectedRejection.getReject()!=null ? "Rejection" : "Return");
				rejectedRejectionDetailList.add(rejectedRejectionDetail);
			} else if ((rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getEncounter() : rejectedRejection.getIssue().getEncounter()) != null) {
				Integer age = Util.getAgeByBirthDate(rejectedRejection.getReject()!=null ? rejectedRejection.getReject().getIssue().getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate() :rejectedRejection.getIssue().getEncounter()
						.getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = rejectedRejection.getIssue().getEncounter().getPatient()
						.getFirstName() + " "
						+ rejectedRejection.getIssue().getEncounter().getPatient().getMiddleName() + " "
						+ rejectedRejection.getIssue().getEncounter().getPatient().getLastName() + " | "
						+ age + " | "
						+ rejectedRejection.getIssue().getEncounter().getPatient().getGender()
								.getGenderCode()
						+ " | " + rejectedRejection.getIssue().getEncounter().getPatient().getUhidNumber();

				rejectedRejection.getIssue().getEncounter().getPatient().getGender().getGenderId();
				rejectedRejection.getIssue().getEncounter().getPatient().getImagePath();

				Integer ageStringDet = Util.getAgeByBirthDate(rejectedRejection.getIssue().getEncounter()
						.getPatient().getDob().toLocalDateTime().toLocalDate());

				String patientName = rejectedRejection.getIssue().getEncounter().getPatient().getPrefix()
						.getPrefixCode() + " "
						+ rejectedRejection.getIssue().getEncounter().getPatient().getFirstName() + " "
						+ rejectedRejection.getIssue().getEncounter().getPatient().getMiddleName() + " "
						+ rejectedRejection.getIssue().getEncounter().getPatient().getLastName();
				rejectedRejection.getIssue().getEncounter().getPatient().getGender().getGenderCode();

				RejectedRejectionDetails rejectedRejectionDetail = new RejectedRejectionDetails(
						rejectedRejection.getId(), rejectedRejection.getRejectNo(), rejectedRejection.getAddedDate(),
						patientDetail,
						rejectedRejection.getIssue().getEncounter().getPatient().getUhidNumber(),
						rejectedRejection.getIssue().getVisitType().getVisitTypeCode(),
						rejectedRejection.getIssue().getEncounter().getEncounterNumber(), patientName,
						ageStringDet);
				rejectedRejectionDetail.setStore(rejectedRejection.getIssue().getStore().getStoreName());
				rejectedRejectionDetail.setStatus(rejectedRejection.getStatus().getStatus());
				rejectedRejectionDetail.setStatusId(rejectedRejection.getStatus().getId());
				rejectedRejectionDetail.setGenderCode(rejectedRejection.getIssue().getEncounter()
						.getPatient().getGender().getGenderCode());
				rejectedRejectionDetail.setGenderId(
						rejectedRejection.getIssue().getEncounter().getPatient().getGender().getGenderId());
				rejectedRejectionDetail.setImagePath(
						rejectedRejection.getIssue().getEncounter().getPatient().getImagePath());
				rejectedRejectionDetail.setStore(rejectedRejection.getIssue().getStore().getStoreName());
				rejectedRejectionDetail.setRequestFrom(rejectedRejection.getReject()!=null ? "Rejection" : "Return");
				rejectedRejectionDetailList.add(rejectedRejectionDetail);
			}
			
		}
		}
		return rejectedRejectionDetailList;

	}

	@Override
	@Transactional
	public RejectedRejectionDetails rejectedRejectionMedicationDetails(Integer id, Integer unitId) {
		RejectedRejectionDetails rejectedRejectionDetail = new RejectedRejectionDetails();
		RejectedRejection rejectedRejection = rejectedRejectionConsumableDao.rejectedRejectionMedicationById(id,
				unitId);
		Integer admissionId=0;
		Integer encounterId=0;
		if (rejectedRejection.getIssue().getAdmission() != null) {
			String wardName = "";
			Integer wardId = 0;
			String bedNumber = "";
			admissionId=rejectedRejection.getIssue().getAdmission().getAdmissionId();
			AdmissionDetail admissionDetail = GlobalDao
					.getPatientWardBedDetails(rejectedRejection.getIssue().getAdmission().getAdmissionId());
			if (admissionDetail != null) {
				WardMasters wardMaster = admissionDetail.getWardMaster();
				if (wardMaster != null) {
					wardName = wardMaster.getWardName();
					wardId = wardMaster.getWardId();
				}
				BedMaster bedMaster = admissionDetail.getBedMaster();
				if (bedMaster != null) {
					bedNumber = bedMaster.getBedNumber();
				}
			}
			Integer age = Util.getAgeByBirthDate(rejectedRejection.getIssue().getAdmission().getPatient()
					.getDob().toLocalDateTime().toLocalDate());
			String patientDetail = rejectedRejection.getIssue().getAdmission().getPatient().getFirstName()
					+ " " + rejectedRejection.getIssue().getAdmission().getPatient().getMiddleName() + " "
					+ rejectedRejection.getIssue().getAdmission().getPatient().getLastName() + " | " + age
					+ " | "
					+ rejectedRejection.getIssue().getAdmission().getPatient().getGender().getGenderCode();

			rejectedRejectionDetail = new RejectedRejectionDetails(rejectedRejection.getId(),
					rejectedRejection.getRejectNo(), rejectedRejection.getAddedDate(), patientDetail,
					rejectedRejection.getIssue().getAdmission().getPatient().getUhidNumber(),
					rejectedRejection.getIssue().getVisitType().getVisitTypeCode(),
					rejectedRejection.getIssue().getAdmission().getAdmissionNumber());
			rejectedRejectionDetail.setWardId(wardId != null ? wardId : null);
			rejectedRejectionDetail.setWardName(!wardName.equals("") ? wardName : null);
			rejectedRejectionDetail.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			rejectedRejectionDetail.setStatus(rejectedRejection.getStatus().getStatus());
			rejectedRejectionDetail.setStatusId(rejectedRejection.getStatus().getId());
			rejectedRejectionDetail.setIssueId(rejectedRejection.getIssue().getId());
		} else if (rejectedRejection.getIssue().getEncounter() != null) {
			encounterId=rejectedRejection.getIssue().getEncounter().getEncounterId();
			Integer age = Util.getAgeByBirthDate(rejectedRejection.getIssue().getEncounter().getPatient()
					.getDob().toLocalDateTime().toLocalDate());
			String patientDetail = rejectedRejection.getIssue().getEncounter().getPatient().getFirstName()
					+ " " + rejectedRejection.getIssue().getEncounter().getPatient().getMiddleName() + " "
					+ rejectedRejection.getIssue().getEncounter().getPatient().getLastName() + " | " + age
					+ " | "
					+ rejectedRejection.getIssue().getEncounter().getPatient().getGender().getGenderCode();

			rejectedRejectionDetail = new RejectedRejectionDetails(rejectedRejection.getId(),
					rejectedRejection.getRejectNo(), rejectedRejection.getAddedDate(), patientDetail,
					rejectedRejection.getIssue().getEncounter().getPatient().getUhidNumber(),
					rejectedRejection.getIssue().getVisitType().getVisitTypeCode(),
					rejectedRejection.getIssue().getEncounter().getEncounterNumber());
			rejectedRejectionDetail.setStatus(rejectedRejection.getStatus().getStatus());
			rejectedRejectionDetail.setStatusId(rejectedRejection.getStatus().getId());
			rejectedRejectionDetail.setIssueId(rejectedRejection.getIssue().getId());
		}
		List<RejectedRejectionItem> rejectedRejectionDetailList = new ArrayList<RejectedRejectionItem>();
		for (RejectedRejectionDetail rejectedRejectionDetails : rejectedRejectionConsumableDao
				.rejectedRejectionMedicationDetailById(id, unitId)) {

			IssueDetailBatchMapper batchMapper = rejectedRejectionDetails.getIssueDetailBatchMapper();

			RejectedRejectionItem item = new RejectedRejectionItem(rejectedRejectionDetails.getId(),
					rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getId(),
					rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getName(),
					rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getName(),
					rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getIsOtc(),
					batchMapper.getUomType().getType(), batchMapper.getUomUnit().getUnits(),
					batchMapper.getBatch().getBatchNo(), batchMapper.getBatch().getExpiryDate(),
					batchMapper.getBatch().getRate(), rejectedRejectionDetails.getRejectDetail()!=null ? rejectedRejectionDetails.getRejectDetail().getRejectedQuantity() : rejectedRejectionDetails.getRejectedRejectQuantity(),
					(rejectedRejectionDetails.getRejectReason() != null)
							? rejectedRejectionDetails.getRejectReason().getReason()
							: null,
					batchMapper.getId(), rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getIsThreeUOMApplicable(),
					GlobalDao.getUnitOfMeasurementListByItem(rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getId(), batchMapper.getOrganizationId()));
			item.setAcceptQuantity(rejectedRejectionDetails.getAcceptedQuantity()!=null ?rejectedRejectionDetails.getAcceptedQuantity() : 0 );
			item.setGenericId(
					rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getId());
			item.setRejectQuantity(rejectedRejectionDetails.getRejectQuantity());
			item.setRejectNote(rejectedRejectionDetails.getRejectNote());
			item.setRejectReasonId((rejectedRejectionDetails.getRejectReason() != null)
					? rejectedRejectionDetails.getRejectReason().getId()
					: null);
//			item.setQty(rejectedRejectionDetails.getRejectQuantity());
			item.setStatus(rejectedRejectionDetails.getStatus().getStatus());
			item.setStatusId(rejectedRejectionDetails.getStatus().getId());
			List<PatientSponsorDetails> patientSponsorDetails = null;
			if(encounterId>0)
			{
				patientSponsorDetails = rejectedRejectionConsumableDao.findByEncounterId(encounterId);
			}
			else
			{
				patientSponsorDetails = rejectedRejectionConsumableDao.findByAdmissionId(admissionId);
			}
			    Integer productCategoryId = null;
				productCategoryId = rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getProductCategory().getId();
				if(patientSponsorDetails != null && productCategoryId != null) 
				{
					for(PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(productCategoryId);
						
						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds, sponsorDetail.getPaymentEntitlementId());
						if(pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							if(pharmacyTariffs.size() > 1){
								//throw exception?
								//Don't throw - get first object and set data.
							}
							
							PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
							item.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							item.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
							break;
						}
					}
					
				}
			rejectedRejectionDetailList.add(item);

			rejectedRejectionDetail.setItems(rejectedRejectionDetailList);
		}
		return rejectedRejectionDetail;

	}

	@Override
	@Transactional
	public Object rejectedRejectionMedicationSave(PendingIssuesAcceptanceSave rejectedRejectionSave) {
		RejectedRejection rejectedRejection = rejectedRejectionConsumableDao
				.rejectedRejectionMedicationById(rejectedRejectionSave.getId(), rejectedRejectionSave.getUnitId());
		for (PendingIssuesAcceptanceSaveItems rejectDetailItem : rejectedRejectionSave.getItems()) {
			RejectedRejectionDetail rejectedRejectionDetail = rejectedRejectionConsumableDao
					.rejectedRejectionMedicationDetailByIdValue(rejectDetailItem.getId(),
							rejectedRejectionSave.getUnitId());
			if(rejectedRejectionDetail.getPatientIssueReturnDetail()!=null)
			{
				rejectedRejectionConsumableDao.updatePatientIssueReturn(rejectedRejectionDetail.getPatientIssueReturnDetail().getId(),rejectDetailItem.getAcceptQuantity());
			}
			rejectedRejectionDetail.setRejectQuantity(rejectDetailItem.getRejectQuantity());
			rejectedRejectionDetail.setRejectReason(
					(rejectDetailItem.getReasonId() != null) ? new RejectReason(rejectDetailItem.getReasonId()) : null);
			rejectedRejectionDetail.setRejectNote(rejectDetailItem.getNote());
			rejectedRejectionDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(rejectedRejection.getUnitId()));
			rejectedRejectionDetail.setAcceptedQuantity(rejectDetailItem.getAcceptQuantity()!=null ? rejectDetailItem.getAcceptQuantity():0);
			rejectedRejectionDetail
					.setUpdatedByUser(rejectedRejectionSave.getUserId() != null ? rejectedRejectionSave.getUserId()
							: sessionService.getUserId());
			if (rejectDetailItem.getStatusId() != null) {
				rejectedRejectionDetail.setStatus(new Status(rejectDetailItem.getStatusId()));
			}
			rejectedRejection.addRejectedRejectionDetail(rejectedRejectionDetail);
		}
		// rejectedRejection.setStatus((new
		// Status(com.param.common.constants.Status.Completed.getStatus())));
		if (rejectedRejectionSave.getStatusId() != null) {
			rejectedRejection.setStatus(new Status(rejectedRejectionSave.getStatusId()));
		}
		rejectedRejection.setUpdatedDate(TimeZoneComponent.getDateByzone(rejectedRejection.getUnitId()));
		rejectedRejection.setUpdatedByUser(rejectedRejectionSave.getUserId() != null ? rejectedRejectionSave.getUserId()
				: sessionService.getUserId());
		rejectedRejectionConsumableDao.rejectedRejectionMedicationSave(rejectedRejection);
		saveRejectDetails(rejectedRejectionSave);
		saveStoreStockDetails(rejectedRejectionSave);

		CommonResponse response = new CommonResponse();
		response.setId(rejectedRejection.getId().longValue());
		response.setMessage("Rejected Rejection Consumable saved successfully");

		return response;

	}

	private void saveRejectDetails(PendingIssuesAcceptanceSave rejectedRejectionSave) {
		Timestamp now = new Timestamp((new DateTime()).getMillis());
		boolean flag = false;

		/*
		 * Integer storeId = sessionService.getStoreId();
		 * 
		 * Store storeNew = GlobalDao.getStore(storeId);
		 * 
		 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		 */

		String rejectBillNumber = GlobalService.getBillNoByGeneratedCode(rejectedRejectionSave.getStoreId(),
				rejectedRejectionSave.getUnitId(), screenId);

		Reject reject = new Reject(rejectBillNumber, new Issue(rejectedRejectionSave.getIssueId()),
				new User(rejectedRejectionSave.getUserId()), now,
				new Status(com.param.common.constants.Status.Pending.getStatus()));
		// sessionService.setEntityDefaults(reject);
		reject.setConsumable(true);
		reject.setCreateDefaults(
				rejectedRejectionSave.getOrganizationId() != null ? rejectedRejectionSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				rejectedRejectionSave.getUnitId() != null ? rejectedRejectionSave.getUnitId()
						: sessionService.getUnitId(),
				rejectedRejectionSave.getUserId() != null ? rejectedRejectionSave.getUserId()
						: sessionService.getUserId());
		for (PendingIssuesAcceptanceSaveItems rejectedRejectionDetailItem : rejectedRejectionSave.getItems()) {
			if (rejectedRejectionDetailItem.getRejectQuantity() > 0) {
				flag = true;
				RejectDetail rejectDetail = new RejectDetail(
						new IssueDetailBatchMapper(rejectedRejectionDetailItem.getBatchMapperId()),
						rejectedRejectionDetailItem.getRejectQuantity(),
						new Status(com.param.common.constants.Status.Pending.getStatus()),
						(rejectedRejectionDetailItem.getReasonId() != null)
								? new RejectReason(rejectedRejectionDetailItem.getReasonId())
								: null,
						rejectedRejectionDetailItem.getNote());
				reject.addRejectDetail(rejectDetail);
				// sessionService.setEntityDefaults(rejectDetail);
				rejectDetail.setCreateDefaults(
						rejectedRejectionSave.getOrganizationId() != null ? rejectedRejectionSave.getOrganizationId()
								: sessionService.getOrganizationId(),
						rejectedRejectionSave.getUnitId() != null ? rejectedRejectionSave.getUnitId()
								: sessionService.getUnitId(),
						rejectedRejectionSave.getUserId() != null ? rejectedRejectionSave.getUserId()
								: sessionService.getUserId());

			}
		}
		if (flag == true) {
			pendingIssuesAcceptanceMedicationDao.pendingIssuesAcceptanceMedicationRejectSave(reject);
		}
	}

	private void saveStoreStockDetails(PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave) {
		for (PendingIssuesAcceptanceSaveItems rejectDetailItem : pendingIssuesAcceptanceSave.getItems()) {
			if (rejectDetailItem.getAcceptQuantity() > 0) {
				IssueDetailBatchMapper batchMapper = GlobalDao
						.getIssueDetailBatchMapperById(rejectDetailItem.getBatchMapperId());

				/*
				 * batchMapper.updateIssueDetailBatchMapper1(rejectDetailItem.getBatchMapperId()
				 * , rejectDetailItem.getAcceptQuantity(), (rejectDetailItem.getNote() != null)
				 * ? rejectDetailItem.getNote() : null, rejectDetailItem.getRejectQuantity(),
				 * (rejectDetailItem.getReasonId() != null) ? new
				 * RejectReason(rejectDetailItem.getReasonId()) : null);
				 */

				batchMapper.updateIssueDetailBatchMapper1(rejectDetailItem.getBatchMapperId(),
						(batchMapper.getRejectedRejectionAcceptedQuantity() != null
								? batchMapper.getRejectedRejectionAcceptedQuantity()
								: 0) + rejectDetailItem.getAcceptQuantity(),
						(rejectDetailItem.getNote() != null) ? rejectDetailItem.getNote() : null,
						(batchMapper.getRejectedRejectionRejectedQuantity() != null
								? batchMapper.getRejectedRejectionRejectedQuantity()
								: 0) + rejectDetailItem.getRejectQuantity(),
						(rejectDetailItem.getReasonId() != null) ? new RejectReason(rejectDetailItem.getReasonId())
								: null);

				RejectedRejectionDetail rejectedRejectionDetail = rejectedRejectionConsumableDao
						.rejectedRejectionMedicationDetailByIdValue(rejectDetailItem.getId(),
								pendingIssuesAcceptanceSave.getUnitId());
				Integer leaseQuantity = Util.GetLeaseQuantity(rejectDetailItem.getAcceptQuantity(),
						rejectedRejectionDetail.getIssueDetailBatchMapper().getBatch().getBatchUomList(),
						rejectedRejectionDetail.getIssueDetailBatchMapper().getUomType());
				
				Integer leaseRejectQuantity = Util.GetLeaseQuantity((rejectDetailItem.getRejectQuantity()!=null ? rejectDetailItem.getRejectQuantity() :0),
						rejectedRejectionDetail.getIssueDetailBatchMapper().getBatch().getBatchUomList(),
						rejectedRejectionDetail.getIssueDetailBatchMapper().getUomType());

				List<StoreStock> storeStockList1 = GlobalDao.getStoreStockByBatchAndStoreList(
						rejectedRejectionDetail.getIssueDetailBatchMapper().getBatch().getId(),
						pendingIssuesAcceptanceSave.getStoreId(), pendingIssuesAcceptanceSave.getUnitId());

				/*
				 * StoreStock storeStockList =
				 * GlobalDao.getStoreStockByBatchAndStore(batchMapper.getBatch().getId(),
				 * patientIssueRejectionSave.getStoreId(),patientIssueRejectionSave.getUnitId())
				 * ;
				 */

				for (StoreStock s : storeStockList1) {
					if (s.getStore().getStoreId().equals(pendingIssuesAcceptanceSave.getStoreId())) {
						Integer stockInTransit = (s.gettQty() != null ? s.gettQty() : 0) - leaseQuantity;
						s.settQty(stockInTransit < 0 ? 0 : stockInTransit);
						Integer availableStock = (s.getAvailableQty() != null ? s.getAvailableQty() : 0)
								+ leaseQuantity;
						//s.setAvailableQty(availableStock < 0 ? 0 : availableStock);
						s = GlobalDao.updateStoreStockLeaseQuantity(s);
					//	saveStockTransaction(leaseRejectQuantity, s,rejectedRejectionDetail);

					}

				}

				// as discussed with divyesh

				/*
				 * Integer tQuantity = 0; StoreStock storeStockDetail = null; StoreStock
				 * storeStockList = GlobalDao.getStoreStockByBatchAndStore(
				 * rejectedRejectionDetail.getIssueDetailBatchMapper().getBatch().getId(),
				 * pendingIssuesAcceptanceSave.getStoreId(),pendingIssuesAcceptanceSave.
				 * getUnitId()); Batch batch = new
				 * Batch(rejectedRejectionDetail.getIssueDetailBatchMapper().getBatch().getId())
				 * ; if (storeStockList == null) { StoreStock storeStock = new
				 * StoreStock(leaseQuantity, new Store(sessionService.getStoreId()), batch);
				 * storeStock.setCreateDefaults(pendingIssuesAcceptanceSave.getOrganizationId(),
				 * pendingIssuesAcceptanceSave.getUnitId(),
				 * pendingIssuesAcceptanceSave.getUserId()); storeStockDetail =
				 * GlobalDao.saveStoreStockLeaseQuantity(storeStock); } else { tQuantity =
				 * leaseQuantity - storeStockList.gettQty(); if(tQuantity < 0) { tQuantity=0;
				 * 
				 * } storeStockList.settQty(tQuantity);
				 * storeStockList.setCreateDefaults(pendingIssuesAcceptanceSave.
				 * getOrganizationId(), pendingIssuesAcceptanceSave.getUnitId(),
				 * pendingIssuesAcceptanceSave.getUserId()); storeStockDetail =
				 * GlobalDao.updateStoreStockLeaseQuantity(storeStockList); }
				 * storeStockDetail.setCreateDefaults(pendingIssuesAcceptanceSave.
				 * getOrganizationId(), pendingIssuesAcceptanceSave.getUnitId(),
				 * pendingIssuesAcceptanceSave.getUserId()); saveStockTransaction(leaseQuantity,
				 * storeStockDetail);
				 */
			}
		}

	}

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock , RejectedRejectionDetail d) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), storeStock.getStore(),
				new Batch(storeStock.getBatch().getId()),d.getId());
		stockTransaction.setIsTransit(true);
		stockTransaction.setTransitNo(d.getRejectedRejection().getRejectNo());
		stockTransaction.setItem(storeStock.getBatch().getItem());
		stockTransaction.setCreateDefaults(
				storeStock.getOrganizationId() != null ? storeStock.getOrganizationId()
						: sessionService.getOrganizationId(),
				storeStock.getUnitId() != null ? storeStock.getUnitId() : sessionService.getUnitId(),
				storeStock.getUpdatedByUser() != null ? storeStock.getUpdatedByUser() : sessionService.getUserId());
		GlobalDao.saveStockTransaction(stockTransaction);
	}
}