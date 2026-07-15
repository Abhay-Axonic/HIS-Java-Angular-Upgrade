package com.param.pharmacy.ip.service.rejectedRejection;

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
import com.param.entity.model.adt.FloorMasters;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.RejectDetail;
import com.param.entity.model.pharmacy.RejectedRejection;
import com.param.entity.model.pharmacy.RejectedRejectionDetail;
import com.param.global.billing.controller.BillingException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.pendingIssues.acceptance.medication.IPendingIssuesAcceptanceMedicationDao;
import com.param.pharmacy.ip.dao.rejectedRejection.IRejectedRejectionMedicationDao;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSaveItems;
import com.param.pharmacy.ip.dto.rejectedRejection.RejectedRejectionDetails;
import com.param.pharmacy.ip.dto.rejectedRejection.RejectedRejectionItem;
import com.param.pharmacy.ip.dto.rejectedRejection.RejectedRejectionSearch;
import com.param.pharmacy.ip.dto.returnAcceptance.PharmacySaleReturnDto;
import com.param.pharmacy.nursing.workstation.dao.patientIssueReturn.IPatientIssueReturnDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("rejectedRejectionMedicationService")
@Transactional
public class RejectedRejectionMedicationService implements IRejectedRejectionMedicationService {
	@Autowired
	private IRejectedRejectionMedicationDao rejectedRejectionMedicationDao;

	@Autowired
	private IPendingIssuesAcceptanceMedicationDao pendingIssuesAcceptanceMedicationDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private IWalkInSalesService iWalkInSalesService;

	@Autowired
	private IPatientIssueReturnDao patientIssueReturnDao;

	private Integer screenId = Screen.RejectedRejection.getScreen();

	@Override
	@Transactional
	public CommonDropdown getDropdownList(Integer unitId) {
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (FloorMasters floor : GlobalDao.getFloorList(unitId)) {
			response.addFloor(new com.param.global.dto.global.Floor(floor.getFloorId(), floor.getFloorName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<RejectedRejectionDetails> rejectedRejectionMedicationSearch(
			RejectedRejectionSearch rejectedRejectionSearch) {
		rejectedRejectionSearch.setToDate((new DateTime(rejectedRejectionSearch.getToDate())).plusDays(1).toDate());
		List<RejectedRejectionDetails> rejectedRejectionDetailList = new ArrayList<RejectedRejectionDetails>();
		for (RejectedRejection rejectedRejection : rejectedRejectionMedicationDao
				.rejectedRejectionMedicationSearch(rejectedRejectionSearch, screenId)) {
			if (rejectedRejection.getIssue().getPatientIndent() == null) {

				String doctorDetail = "";

				if (rejectedRejection.getIssue().getDoctor() != null) {
					doctorDetail = rejectedRejection.getIssue().getDoctor().getFirstName() + " "
							+ rejectedRejection.getIssue().getDoctor().getMiddleName() + " "
							+ rejectedRejection.getIssue().getDoctor().getLastName();
				}

				if (rejectedRejection.getIssue().getSpecialityMaster() != null) {
					doctorDetail = doctorDetail + " | "
							+ rejectedRejection.getIssue().getSpecialityMaster().getSpecialityName();
				}

				if (rejectedRejection.getIssue().getAdmission() != null) {
					String wardName = "";
					String bedNumber = "";
					AdmissionDetail admissionDetail = GlobalDao.getPatientWardBedDetails(
							rejectedRejection.getIssue().getAdmission().getAdmissionId());
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

					Integer age = Util.getAgeByBirthDate(rejectedRejection.getIssue().getAdmission()
							.getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = rejectedRejection.getIssue().getAdmission().getPatient()
							.getPrefix().getPrefixCode() + " "
							+ rejectedRejection.getIssue().getAdmission().getPatient().getFirstName() + " "
							+ rejectedRejection.getIssue().getAdmission().getPatient().getMiddleName() + " "
							+ rejectedRejection.getIssue().getAdmission().getPatient().getLastName() + " | "
							+ age + " | "
							+ rejectedRejection.getIssue().getAdmission().getPatient().getGender()
									.getGenderCode()
							+ " | "
							+ rejectedRejection.getIssue().getAdmission().getPatient().getUhidNumber();

					Integer ageStringDet = Util.getAgeByBirthDate(rejectedRejection.getIssue()
							.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());

					String patientName = rejectedRejection.getIssue().getAdmission().getPatient()
							.getPrefix().getPrefixCode() + " "
							+ rejectedRejection.getIssue().getAdmission().getPatient().getFirstName() + " "
							+ rejectedRejection.getIssue().getAdmission().getPatient().getMiddleName() + " "
							+ rejectedRejection.getIssue().getAdmission().getPatient().getLastName();

					RejectedRejectionDetails rejectedRejectionDetail = new RejectedRejectionDetails(
							rejectedRejection.getId(), rejectedRejection.getRejectNo(),
							rejectedRejection.getAddedDate(), doctorDetail, patientDetail,
							rejectedRejection.getIssue().getAdmission().getPatient().getUhidNumber(),
							rejectedRejection.getIssue().getVisitType().getVisitTypeCode(),
							rejectedRejection.getIssue().getStore().getStoreName(), patientName,
							ageStringDet);
					rejectedRejectionDetail.setMedicationType(rejectedRejection.getIssue().getPrescription()!=null ? rejectedRejection.getIssue().getPrescription().getMedicationType():null);
					rejectedRejectionDetail.setStatus(rejectedRejection.getStatus().getStatus());
					rejectedRejectionDetail.setStatusId(rejectedRejection.getStatus().getId());
					rejectedRejectionDetail.setWardName(!wardName.equals("") ? wardName : null);
					rejectedRejectionDetail.setBedNo(!bedNumber.equals("") ? bedNumber : null);
					rejectedRejectionDetail.setGenderCode(rejectedRejection.getIssue().getAdmission()
							.getPatient().getGender().getGenderCode());
					rejectedRejectionDetail.setGenderId(rejectedRejection.getIssue().getAdmission()
							.getPatient().getGender().getGenderId());
					rejectedRejectionDetail.setImagePath(
							rejectedRejection.getIssue().getAdmission().getPatient().getImagePath());
					rejectedRejectionDetail.setRequestFrom(rejectedRejection.getReject()!=null ? "Rejection" : "Return");
					rejectedRejectionDetail.setPatientId(rejectedRejection.getIssue().getAdmission().getPatient().getPatientId());
					rejectedRejectionDetailList.add(rejectedRejectionDetail);

				} else if (rejectedRejection.getIssue().getEncounter() != null) {

					Integer age = Util.getAgeByBirthDate(rejectedRejection.getIssue().getEncounter()
							.getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = rejectedRejection.getIssue().getEncounter().getPatient()
							.getPrefix().getPrefixCode() + " "
							+ rejectedRejection.getIssue().getEncounter().getPatient().getFirstName() + " "
							+ rejectedRejection.getIssue().getEncounter().getPatient().getMiddleName() + " "
							+ rejectedRejection.getIssue().getEncounter().getPatient().getLastName() + " | "
							+ age + " | "
							+ rejectedRejection.getIssue().getEncounter().getPatient().getGender()
									.getGenderCode()
							+ " | "
							+ rejectedRejection.getIssue().getEncounter().getPatient().getUhidNumber();

					Integer ageStringDet = Util.getAgeByBirthDate(rejectedRejection.getIssue()
							.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());

					String patientName = rejectedRejection.getIssue().getEncounter().getPatient()
							.getPrefix().getPrefixCode() + " "
							+ rejectedRejection.getIssue().getEncounter().getPatient().getFirstName() + " "
							+ rejectedRejection.getIssue().getEncounter().getPatient().getMiddleName() + " "
							+ rejectedRejection.getIssue().getEncounter().getPatient().getLastName();

					RejectedRejectionDetails rejectedRejectionDetail = new RejectedRejectionDetails(
							rejectedRejection.getId(), rejectedRejection.getRejectNo(),
							rejectedRejection.getAddedDate(), doctorDetail, patientDetail,
							rejectedRejection.getIssue().getEncounter().getPatient().getUhidNumber(),
							rejectedRejection.getIssue().getVisitType().getVisitTypeCode(),
							rejectedRejection.getIssue().getStore().getStoreName(), patientName,
							ageStringDet);
					rejectedRejectionDetail.setMedicationType(rejectedRejection.getIssue().getPrescription().getMedicationType());
					rejectedRejectionDetail.setStatus(rejectedRejection.getStatus().getStatus());
					rejectedRejectionDetail.setStatusId(rejectedRejection.getStatus().getId());
					rejectedRejectionDetail.setGenderCode(rejectedRejection.getIssue().getEncounter()
							.getPatient().getGender().getGenderCode());
					rejectedRejectionDetail.setGenderId(rejectedRejection.getIssue().getEncounter()
							.getPatient().getGender().getGenderId());
					rejectedRejectionDetail.setImagePath(
							rejectedRejection.getIssue().getEncounter().getPatient().getImagePath());
					rejectedRejectionDetail.setRequestFrom(rejectedRejection.getReject()!=null ? "Rejection" : "Return");
					rejectedRejectionDetail.setPatientId(rejectedRejection.getIssue().getAdmission().getPatient().getPatientId());
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
		RejectedRejection rejectedRejection = rejectedRejectionMedicationDao.rejectedRejectionMedicationById(id,
				unitId);
		String doctorDetail = "";
		Integer admissionId=0;
		Integer encounterId=0;
		if (rejectedRejection.getIssue().getDoctor() != null) {
			doctorDetail = rejectedRejection.getIssue().getDoctor().getFirstName() + " "
					+ rejectedRejection.getIssue().getDoctor().getMiddleName() + " "
					+ rejectedRejection.getIssue().getDoctor().getLastName();
		}

		if (rejectedRejection.getIssue().getSpecialityMaster() != null) {
			doctorDetail = doctorDetail + " | "
					+ rejectedRejection.getIssue().getSpecialityMaster().getSpecialityName();
		}
		if (rejectedRejection.getIssue().getAdmission() != null) {
			String wardName = "";
			String bedNumber = "";
			admissionId=rejectedRejection.getIssue().getAdmission().getAdmissionId();
			AdmissionDetail admissionDetail = GlobalDao
					.getPatientWardBedDetails(rejectedRejection.getIssue().getAdmission().getAdmissionId());
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
			Integer age = Util.getAgeByBirthDate(rejectedRejection.getIssue().getAdmission().getPatient()
					.getDob().toLocalDateTime().toLocalDate());
			String patientDetail = rejectedRejection.getIssue().getAdmission().getPatient().getPrefix()
					.getPrefixCode() + " "
					+ rejectedRejection.getIssue().getAdmission().getPatient().getFirstName() + " "
					+ rejectedRejection.getIssue().getAdmission().getPatient().getMiddleName() + " "
					+ rejectedRejection.getIssue().getAdmission().getPatient().getLastName() + " | " + age
					+ " | "
					+ rejectedRejection.getIssue().getAdmission().getPatient().getGender().getGenderCode();

			rejectedRejectionDetail = new RejectedRejectionDetails(rejectedRejection.getId(),
					rejectedRejection.getRejectNo(), rejectedRejection.getAddedDate(), doctorDetail, patientDetail,
					rejectedRejection.getIssue().getAdmission().getPatient().getUhidNumber(),
					rejectedRejection.getIssue().getVisitType().getVisitTypeCode(),
					rejectedRejection.getIssue().getStore().getStoreName());
			rejectedRejectionDetail.setWardName(!wardName.equals("") ? wardName : null);
			rejectedRejectionDetail.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			rejectedRejectionDetail.setIssueId(rejectedRejection.getIssue().getId());
			rejectedRejectionDetail.setStatus(rejectedRejection.getStatus().getStatus());
			rejectedRejectionDetail.setStatusId(rejectedRejection.getStatus().getId());
			rejectedRejectionDetail.setStoreId(rejectedRejection.getIssue().getStore().getStoreId());

		} else if (rejectedRejection.getIssue().getEncounter() != null) {
			encounterId=rejectedRejection.getIssue().getEncounter().getEncounterId();
			Integer age = Util.getAgeByBirthDate(rejectedRejection.getIssue().getEncounter().getPatient()
					.getDob().toLocalDateTime().toLocalDate());
			String patientDetail = rejectedRejection.getIssue().getEncounter().getPatient().getPrefix()
					.getPrefixCode() + " "
					+ rejectedRejection.getIssue().getEncounter().getPatient().getFirstName() + " "
					+ rejectedRejection.getIssue().getEncounter().getPatient().getMiddleName() + " "
					+ rejectedRejection.getIssue().getEncounter().getPatient().getLastName() + " | " + age
					+ " | "
					+ rejectedRejection.getIssue().getEncounter().getPatient().getGender().getGenderCode();

			rejectedRejectionDetail = new RejectedRejectionDetails(rejectedRejection.getId(),
					rejectedRejection.getRejectNo(), rejectedRejection.getAddedDate(), doctorDetail, patientDetail,
					rejectedRejection.getIssue().getEncounter().getPatient().getUhidNumber(),
					rejectedRejection.getIssue().getVisitType().getVisitTypeCode(),
					rejectedRejection.getIssue().getStore().getStoreName());
			rejectedRejectionDetail.setIssueId(rejectedRejection.getIssue().getId());
			rejectedRejectionDetail.setStatus(rejectedRejection.getStatus().getStatus());
			rejectedRejectionDetail.setStatusId(rejectedRejection.getStatus().getId());
			rejectedRejectionDetail.setStoreId(rejectedRejection.getIssue().getStore().getStoreId());
		}
		List<RejectedRejectionItem> rejectedRejectionDetailList = new ArrayList<RejectedRejectionItem>();
		for (RejectedRejectionDetail rejectedRejectionDetails : rejectedRejectionMedicationDao
				.rejectedRejectionMedicationDetailById(id, unitId)) {
			IssueDetailBatchMapper batchMapper = rejectedRejectionDetails.getIssueDetailBatchMapper();

			RejectedRejectionItem item = new RejectedRejectionItem(rejectedRejectionDetails.getId(),
					rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getId(),
					batchMapper.getId(),
					rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getName(),
					rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getName(),
					rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getIsOtc(),
					batchMapper.getUomType().getType(), batchMapper.getUomUnit().getUnits(),
					batchMapper.getBatch().getBatchNo(), batchMapper.getBatch().getExpiryDate(),
					batchMapper.getBatch().getRate(), rejectedRejectionDetails.getRejectDetail()!=null ? rejectedRejectionDetails.getRejectDetail().getRejectedQuantity():rejectedRejectionDetails.getRejectedRejectQuantity(),
					(rejectedRejectionDetails.getRejectReason() != null)
							? rejectedRejectionDetails.getRejectReason().getReason()
							: null,
					rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getIsThreeUOMApplicable(), 
					GlobalDao.getUnitOfMeasurementListByItem(rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getId(), rejectedRejection.getOrganizationId()));
			item.setGenericId(
					rejectedRejectionDetails.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getId());
			item.setAcceptQuantity(rejectedRejectionDetails.getAcceptedQuantity());
			item.setRejectQuantity(rejectedRejectionDetails.getRejectQuantity());
			item.setRejectNote(rejectedRejectionDetails.getRejectNote());
			item.setRejectReasonId((rejectedRejectionDetails.getRejectReason() != null)
					? rejectedRejectionDetails.getRejectReason().getId()
					: null);
			// item.setQty(rejectedRejectionDetails.getRejectQuantity());
			item.setStatus(rejectedRejectionDetails.getStatus().getStatus());
			item.setStatusId(rejectedRejectionDetails.getStatus().getId());
			List<PatientSponsorDetails> patientSponsorDetails = null;
			if(encounterId>0)
			{
				patientSponsorDetails = rejectedRejectionMedicationDao.findByEncounterId(encounterId);
			}
			else
			{
				patientSponsorDetails = rejectedRejectionMedicationDao.findByAdmissionId(admissionId);
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
		}
		rejectedRejectionDetail.setItems(rejectedRejectionDetailList);
		return rejectedRejectionDetail;

	}

	/*
	 * private Integer encounterId; private Integer admissionId; private Integer
	 * patientId;
	 */

	@Override
	@Transactional
	public Object rejectedRejectionMedicationSave(PendingIssuesAcceptanceSave rejectedRejectionSave)
			throws BillingException, Exception {

		RejectedRejection rejectedRejection = rejectedRejectionMedicationDao
				.rejectedRejectionMedicationById(rejectedRejectionSave.getId(), rejectedRejectionSave.getUnitId());

		// For return
		BillingMasterDto dto = new BillingMasterDto();
		dto.setOrganisationId(rejectedRejection.getOrganization().getOrganizationId());
		dto.setUnitId(rejectedRejection.getUnit().getUnitId());
		dto.setAdmissionId(rejectedRejection.getIssue().getAdmission() != null
				? rejectedRejection.getIssue().getAdmission().getAdmissionId()
				: null);
		dto.setEncounterId(rejectedRejection.getIssue().getEncounter() != null
				? rejectedRejection.getIssue().getEncounter().getEncounterId()
				: null);

		if (dto.getAdmissionId() != null && dto.getAdmissionId() > 0) {
			dto.setPatientId(rejectedRejection.getIssue().getAdmission().getPatient() != null
					? rejectedRejection.getIssue().getAdmission().getPatient().getPatientId()
					: null);
			dto.setVisitTypeId(rejectedRejection.getIssue().getAdmission().getVisitType().getVisitTypeId());
		} else if (dto.getEncounterId() != null && dto.getEncounterId() > 0) {
			dto.setPatientId(rejectedRejection.getIssue().getEncounter().getPatient() != null
					? rejectedRejection.getIssue().getEncounter().getPatient().getPatientId()
					: null);
			dto.setVisitTypeId(rejectedRejection.getIssue().getVisitType().getVisitTypeId());
		}
		dto.setCreatedBy(rejectedRejectionSave.getUserId() != null ? rejectedRejectionSave.getUserId()
				: sessionService.getUserId());

		List<OrderDetailsMasterDto> orderDetailsList = new ArrayList<OrderDetailsMasterDto>();

		for (PendingIssuesAcceptanceSaveItems rejectDetailItem : rejectedRejectionSave.getItems()) {
			if (rejectDetailItem.getAcceptQuantity() > 0) {
				
				RejectedRejectionDetail rejectedRejectionDetail = rejectedRejectionMedicationDao
						.rejectedRejectionMedicationDetailByIdValue(rejectDetailItem.getId(),
								rejectedRejectionSave.getUnitId());
				if(rejectedRejectionDetail.getPatientIssueReturnDetail()!=null)
				{
				rejectedRejectionMedicationDao.updatePatientIssueReturn(rejectedRejectionDetail.getPatientIssueReturnDetail().getId(),rejectDetailItem.getAcceptQuantity());
				}
				rejectedRejectionDetail.setRejectQuantity(rejectDetailItem.getRejectQuantity());
				rejectedRejectionDetail.setAcceptedQuantity(rejectDetailItem.getAcceptQuantity());
				rejectedRejectionDetail.setRejectReason(
						(rejectDetailItem.getReasonId() != null) ? new RejectReason(rejectDetailItem.getReasonId())
								: null);
				rejectedRejectionDetail.setRejectNote(rejectDetailItem.getNote());
				rejectedRejectionDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(rejectedRejection.getUnitId()));
				rejectedRejectionDetail
						.setUpdatedByUser(rejectedRejectionSave.getUserId() != null ? rejectedRejectionSave.getUserId()
								: sessionService.getUserId());
				if (rejectDetailItem.getStatusId() != null) {
					rejectedRejectionDetail.setStatus(new Status(rejectDetailItem.getStatusId()));
				}
				rejectedRejection.addRejectedRejectionDetail(rejectedRejectionDetail);

				PharmacySaleReturnDto phSRDto = patientIssueReturnDao
						.getSaleDetailIdByIssueDetailMapper(rejectDetailItem.getBatchMapperId());

				if (phSRDto != null) {
					OrderDetailsMasterDto orderDtlsDto = new OrderDetailsMasterDto();
					orderDtlsDto.setSaleDetailId(phSRDto.getSaleDetailId().intValue());
					orderDtlsDto.setSaleId(phSRDto.getSaleId() != null ? phSRDto.getSaleId().intValue() : null);
					orderDtlsDto.setOrgId(dto.getOrganisationId());
					orderDtlsDto.setOrgUnitId(dto.getUnitId());
					orderDtlsDto.setVisitTypeId(dto.getVisitTypeId());
					orderDtlsDto.setOrderQty(rejectDetailItem.getAcceptQuantity());
					orderDtlsDto.setBatchId(phSRDto.getBatchId().intValue());
					orderDtlsDto.setDrugId(phSRDto.getItemId());
					orderDtlsDto.setProductCategoryId(phSRDto.getProductCategoryId());
					orderDetailsList.add(orderDtlsDto);
				}
			}
		}

		if (rejectedRejectionSave.getStatusId() != null) {
			rejectedRejection.setStatus(new Status(rejectedRejectionSave.getStatusId()));
		}

		rejectedRejection.setUpdatedDate(TimeZoneComponent.getDateByzone(rejectedRejection.getUnitId()));
		rejectedRejection.setUpdatedByUser(rejectedRejectionSave.getUserId() != null ? rejectedRejectionSave.getUserId()
				: sessionService.getUserId());
		rejectedRejectionMedicationDao.rejectedRejectionMedicationSave(rejectedRejection);

		// sales return
		// iWalkInSalesService.pharamcyPackageContractReturn(dto, orderDetailsList);
		saveRejectDetails(rejectedRejectionSave);
		saveStoreStockDetails(rejectedRejectionSave);

		CommonResponse response = new CommonResponse();
		response.setId(rejectedRejection.getId().longValue());
		response.setMessage("Rejected Rejection Medication saved successfully");

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

		String rejectBillNumber = GlobalService.getBillNoByGeneratedCode(sessionService.getStoreId(),
				rejectedRejectionSave.getUnitId(), screenId);

		Reject reject = new Reject(rejectBillNumber, new Issue(rejectedRejectionSave.getIssueId()),
				new User(rejectedRejectionSave.getUserId()), now,
				new Status(com.param.common.constants.Status.Pending.getStatus()));
		reject.setCreateDefaults(
				rejectedRejectionSave.getOrganizationId() != null ? rejectedRejectionSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				rejectedRejectionSave.getUnitId() != null ? rejectedRejectionSave.getUnitId()
						: sessionService.getUnitId(),
				rejectedRejectionSave.getUserId() != null ? rejectedRejectionSave.getUserId()
						: sessionService.getUserId());

		for (PendingIssuesAcceptanceSaveItems issueDetailItem : rejectedRejectionSave.getItems()) {
			if (issueDetailItem.getRejectQuantity() > 0) {
				flag = true;
				RejectDetail rejectDetail = new RejectDetail(
						new IssueDetailBatchMapper(issueDetailItem.getBatchMapperId()),
						issueDetailItem.getRejectQuantity(),
						new Status(com.param.common.constants.Status.Pending.getStatus()),
						(issueDetailItem.getReasonId() != null) ? new RejectReason(issueDetailItem.getReasonId())
								: null,
						issueDetailItem.getNote());
				// sessionService.setEntityDefaults(rejectDetail);
				rejectDetail.setCreateDefaults(
						rejectedRejectionSave.getOrganizationId() != null ? rejectedRejectionSave.getOrganizationId()
								: sessionService.getOrganizationId(),
						rejectedRejectionSave.getUnitId() != null ? rejectedRejectionSave.getUnitId()
								: sessionService.getUnitId(),
						rejectedRejectionSave.getUserId() != null ? rejectedRejectionSave.getUserId()
								: sessionService.getUserId());
				reject.addRejectDetail(rejectDetail);
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
				 * batchMapper.updateIssueDetailBatchMapper(rejectDetailItem.getBatchMapperId(),
				 * rejectDetailItem.getAcceptQuantity(), (rejectDetailItem.getNote() != null) ?
				 * rejectDetailItem.getNote() : null, rejectDetailItem.getRejectQuantity(),
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

				RejectedRejectionDetail rejectedRejectionDetail = rejectedRejectionMedicationDao
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

				for (StoreStock s : storeStockList1) {
					if (s.getStore().getStoreId().equals(pendingIssuesAcceptanceSave.getStoreId())) {
						Integer stockInTransit = (s.gettQty() != null ? s.gettQty() : 0) - leaseQuantity;
						s.settQty(stockInTransit < 0 ? 0 : stockInTransit);
						Integer availableStock = (s.getAvailableQty() != null ? s.getAvailableQty() : 0)
								+ leaseQuantity;
						// s.setAvailableQty(availableStock < 0 ? 0 : availableStock);
						s = GlobalDao.updateStoreStockLeaseQuantity(s);
//						saveStockTransaction(leaseRejectQuantity, s, rejectedRejectionDetail);

					}

				}

				/*
				 * StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(
				 * rejectedRejectionDetail.getIssueDetailBatchMapper().getBatch().getId(),
				 * pendingIssuesAcceptanceSave.getStoreId(),pendingIssuesAcceptanceSave.
				 * getUnitId()); Batch batch = new
				 * Batch(rejectedRejectionDetail.getIssueDetailBatchMapper().getBatch().getId())
				 * ; if (storeStockList == null) { StoreStock storeStock = new
				 * StoreStock(leaseQuantity, new
				 * Store(pendingIssuesAcceptanceSave.getStoreId()), batch);
				 * storeStock.settQty(rejectDetailItem.getAcceptQuantity()); //
				 * sessionService.setEntityDefaults(storeStock);
				 * storeStock.setCreateDefaults(pendingIssuesAcceptanceSave.getOrganizationId()!
				 * =null ? pendingIssuesAcceptanceSave.getOrganizationId() :
				 * sessionService.getOrganizationId(),pendingIssuesAcceptanceSave.getUnitId()
				 * !=null ? pendingIssuesAcceptanceSave.getUnitId() :sessionService.getUnitId()
				 * ,pendingIssuesAcceptanceSave.getUserId()!=null ?
				 * pendingIssuesAcceptanceSave.getUserId() : sessionService.getUserId());
				 * 
				 * storeStockDetail = GlobalDao.saveStoreStockLeaseQuantity(storeStock); } else
				 * { tQuantity = leaseQuantity - storeStockList.gettQty(); Integer qty=0;
				 * if(storeStockList.gettQty() != null) { qty=storeStockList.gettQty(); }
				 * storeStockList.settQty(qty-rejectDetailItem.getAcceptQuantity());
				 * //storeStockList.settQty(tQuantity); storeStockDetail =
				 * GlobalDao.updateStoreStockLeaseQuantity(storeStockList); }
				 * saveStockTransaction(leaseQuantity, storeStockDetail,
				 * pendingIssuesAcceptanceSave.getStoreId(), pendingIssuesAcceptanceSave);
				 */
			}
		}

	}

/*	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock, Integer storeId,
			PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), new Store(storeId), new Batch(storeStock.getBatch().getId()));
		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(
				pendingIssuesAcceptanceSave.getOrganizationId() != null
						? pendingIssuesAcceptanceSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				pendingIssuesAcceptanceSave.getUnitId() != null ? pendingIssuesAcceptanceSave.getUnitId()
						: sessionService.getUnitId(),
				pendingIssuesAcceptanceSave.getUserId() != null ? pendingIssuesAcceptanceSave.getUserId()
						: sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);
	}*/

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