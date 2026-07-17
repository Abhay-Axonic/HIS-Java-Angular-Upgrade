package com.param.pharmacy.ip.service.pendingIssues.acceptance.medication;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import com.param.entity.model.adt.Priority;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.MedicationHandover;
import com.param.entity.model.pharmacy.MedicationHandoverDetail;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.RejectDetail;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoules;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoulesDetail;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.Floor;
import com.param.global.dto.global.Ward;
import com.param.global.dto.items.UomResponse;
import com.param.global.pharmacy.dto.ReceiverResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.pendingIssues.acceptance.medication.IPendingIssuesAcceptanceMedicationDao;
import com.param.pharmacy.ip.dao.pharmacology.IPharmacologyValidationDao;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.IssueAcceptanceMadicationDropdown;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.MedicationHandoverSaveRequest;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceDetails;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceItem;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSaveItems;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearch;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearchResult;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PrescriptionDetailsList;
import com.param.pharmacy.master.global.dao.unitOfMeasurement.UnitOfMeasurementDao;
import com.param.pharmacy.nursing.discharge.dto.BatchDetail;
import com.param.pharmacy.nursing.discharge.dto.MedicationHandoverResponse;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationItem;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchDto;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchRequest;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDto;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("pendingIssuesAcceptanceMedicationService")
@Transactional
public class PendingIssuesAcceptanceMedicationService implements ICommonConstants,IPendingIssuesAcceptanceMedicationService {
	@Autowired
	private IPendingIssuesAcceptanceMedicationDao pendingIssuesAcceptanceMedicationDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private IGlobalDao GlobalDao;
	@Autowired
	private IPharmacologyValidationDao pharmacologyValidationDao;
	@Autowired
	IGlobalService GlobalService;
	private Integer rejectScreenId = Screen.PatientIssueRejection.getScreen();
	private Integer screenId = Screen.IssueAcceptance.getScreen();

	private com.param.entity.model.master.Screen screen;
	@Autowired
	UnitOfMeasurementDao unitOfMeasurementDao;
	@Override
	@Transactional
	public IssueAcceptanceMadicationDropdown getDropdownList(Integer unitId) {
		IssueAcceptanceMadicationDropdown response = new IssueAcceptanceMadicationDropdown();

		for (WardMasters ward : GlobalDao.getWardList(unitId)) {
			response.addWard(new Ward(ward.getWardId(), ward.getWardName()));
		}

		for (FloorMasters floor : GlobalDao.getFloorList(unitId)) {
			response.addFloor(new Floor(floor.getFloorId(), floor.getFloorName()));
		}
		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<PendingIssuesAcceptanceSearchResult> pendingIssuesAcceptanceMedicationSearch(
			PendingIssuesAcceptanceSearch pendingIssuesAcceptanceSearch) {
		pendingIssuesAcceptanceSearch
				.setToDate((new DateTime(pendingIssuesAcceptanceSearch.getToDate())).plusDays(1).toDate());
		List<PendingIssuesAcceptanceSearchResult> pendingIssuesAcceptanceSearchResult = new ArrayList<PendingIssuesAcceptanceSearchResult>();
		for (Issue issue : pendingIssuesAcceptanceMedicationDao
				.pendingIssuesAcceptanceMedicationSearch(pendingIssuesAcceptanceSearch, screenId)) {
			if (issue.getPatientIndent() == null) {

				/*
				 * if(issue.getPrescription().getDoctor()!=null) { doctorDeatil =
				 * issue.getDoctor().getFirstName() + " " + issue.getDoctor().getMiddleName() +
				 * " " + issue.getDoctor().getLastName() +
				 * " | "+issue.getSpecialityMaster().getSpecialityName(); }
				 */
				String doctorDeatil = "";
				if (issue.getPrescription().getSpecialityMaster() != null
						&& issue.getPrescription().getDoctor() != null) {
					doctorDeatil = issue.getPrescription().getDoctor().getFirstName() + " "
							+ issue.getPrescription().getDoctor().getMiddleName() + " "
							+ issue.getPrescription().getDoctor().getLastName() + " | "
							+ issue.getPrescription().getSpecialityMaster().getSpecialityName();
				}

				if (issue.getAdmission() != null) {
					String wardName = "";
					String bedNumber = "";
					AdmissionDetail admissionDetail = GlobalDao
							.getPatientWardBedDetails(issue.getAdmission().getAdmissionId());
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
					Integer age = Util.getAgeByBirthDate(
							issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
					/*
					 * String patientDetail =
					 * issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " " +
					 * issue.getAdmission().getPatient().getFirstName() + " " +
					 * issue.getAdmission().getPatient().getMiddleName() + " " +
					 * issue.getAdmission().getPatient().getLastName() + " | " + age + " | " +
					 * issue.getAdmission().getPatient().getGender().getGenderCode();
					 */
					String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " "
							+ issue.getAdmission().getPatient().getFirstName() + " "
							+ issue.getAdmission().getPatient().getMiddleName() + " "
							+ issue.getAdmission().getPatient().getLastName();
					PendingIssuesAcceptanceSearchResult pendingIssuesAcceptance = new PendingIssuesAcceptanceSearchResult(
							issue.getId(), issue.getIssueNo(), issue.getAddedDate(),
							issue.getAdmission().getPatient().getUhidNumber(), patientDetail, age,
							issue.getAdmission().getPatient().getGender().getGenderCode(),
							issue.getAdmission().getPatient().getImagePath(), doctorDeatil,
							issue.getVisitType().getVisitTypeCode(), issue.getStore().getStoreName());
					pendingIssuesAcceptance.setPrescriptionDate(issue.getPrescription().getPrescriptionDate()); 
					pendingIssuesAcceptance.setWardName(!wardName.equals("") ? wardName : null);
					pendingIssuesAcceptance.setBedNo(!bedNumber.equals("") ? bedNumber : null);
					pendingIssuesAcceptance.setStatus(issue.getStatus().getStatus());
					pendingIssuesAcceptance.setStatusId(issue.getStatus().getId());
					pendingIssuesAcceptance.setMedicationType(issue.getPrescription().getMedicationType());
					pendingIssuesAcceptance.setPriorityId(issue.getPrescription().getPriority().getPriorityId());
					pendingIssuesAcceptanceSearchResult.add(pendingIssuesAcceptance);
					
				} else if (issue.getEncounter() != null) {
					Integer age = Util.getAgeByBirthDate(
							issue.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
					/*
					 * String patientDetail =
					 * issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " " +
					 * issue.getEncounter().getPatient().getFirstName() + " " +
					 * issue.getEncounter().getPatient().getMiddleName() + " " +
					 * issue.getEncounter().getPatient().getLastName() + " | " + age + " | " +
					 * issue.getEncounter().getPatient().getGender().getGenderCode();
					 */
					String patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " "
							+ issue.getEncounter().getPatient().getFirstName() + " "
							+ issue.getEncounter().getPatient().getMiddleName() + " "
							+ issue.getEncounter().getPatient().getLastName();
					PendingIssuesAcceptanceSearchResult pendingIssuesAcceptance = new PendingIssuesAcceptanceSearchResult(
							issue.getId(), issue.getIssueNo(), issue.getAddedDate(),
							issue.getEncounter().getPatient().getUhidNumber(), patientDetail, age,
							issue.getEncounter().getPatient().getGender().getGenderCode(),
							issue.getEncounter().getPatient().getImagePath(), doctorDeatil,
							issue.getVisitType().getVisitTypeCode(), issue.getStore().getStoreName());
					pendingIssuesAcceptance.setPrescriptionDate(issue.getPrescription().getPrescriptionDate()); 
					pendingIssuesAcceptance.setStatus(issue.getStatus().getStatus());
					pendingIssuesAcceptance.setStatusId(issue.getStatus().getId());
					pendingIssuesAcceptance.setMedicationType(issue.getPrescription().getMedicationType());
					pendingIssuesAcceptanceSearchResult.add(pendingIssuesAcceptance);
				}
			}
		}
		return pendingIssuesAcceptanceSearchResult;
	}

	@Override
	@Transactional
	public PendingIssuesAcceptanceDetails pendingIssuesAcceptanceMedicationDetails(Integer id) {
		Issue issue = pendingIssuesAcceptanceMedicationDao.pendingIssuesAcceptanceMedicationById(id);
		PendingIssuesAcceptanceDetails pendingIssuesAcceptanceDetail = new PendingIssuesAcceptanceDetails();
		String carrier = pendingIssuesAcceptanceMedicationDao.getRecievedBy(id);
	
		String drSpeciality = "";
		if (issue.getPrescription().getSpecialityMaster() != null) {
			drSpeciality = issue.getPrescription().getSpecialityMaster().getSpecialityName();
		}

		String doctorDeatil = null;
		if (issue.getPrescription().getDoctor() != null && issue.getPrescription().getSpecialityMaster() != null) {
			doctorDeatil = issue.getPrescription().getDoctor().getFirstName() + " "
					+ issue.getPrescription().getDoctor().getMiddleName() + " "
					+ issue.getPrescription().getDoctor().getLastName() + " | " + drSpeciality;
		}

		if (issue.getAdmission() != null) {
			String wardName = "";
			String bedNumber = "";
			Date doa = null;
			Date pdd = null;
			AdmissionDetail admissionDetail = GlobalDao.getPatientWardBedDetails(issue.getAdmission().getAdmissionId());
			if (admissionDetail != null) {
				doa = admissionDetail.getDoa();
				pdd = admissionDetail.getPdd();
				WardMasters wardMaster = admissionDetail.getWardMaster();
				if (wardMaster != null) {
					wardName = wardMaster.getWardName();
				}
				BedMaster bedMaster = admissionDetail.getBedMaster();
				if (bedMaster != null) {
					bedNumber = bedMaster.getBedNumber();
				}
			}
			Integer age = Util
					.getAgeByBirthDate(issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " "
					+ issue.getAdmission().getPatient().getFirstName() + " "
					+ issue.getAdmission().getPatient().getMiddleName() + " "
					+ issue.getAdmission().getPatient().getLastName() + " | " + age + " | "
					+ issue.getAdmission().getPatient().getGender().getGenderCode();
			pendingIssuesAcceptanceDetail = new PendingIssuesAcceptanceDetails(issue.getId(),
					issue.getAdmission().getPatient().getPatientId(), patientDetail,
					issue.getAdmission().getPatient().getDob(),
					doctorDeatil == null ? null : ((issue.getPrescription() != null && issue.getPrescription().getDoctor() != null ) ? issue.getPrescription().getDoctor().getDoctorId():0), 					//doctorDeatil == null ? null : (issue.getDoctor() != null ? (issue.getDoctor().getDoctorId() != null ? issue.getDoctor().getDoctorId() : 0) : issue.getPrescription().getDoctor().getDoctorId()),
					doctorDeatil,
					issue.getAdmission().getPatient().getUhidNumber(), issue.getIssueNo(), issue.getAddedDate(),
					issue.getPrescription().getPrescriptionNo(), issue.getStore().getStoreName());
			pendingIssuesAcceptanceDetail.setWardName(!wardName.equals("") ? wardName : null);
			pendingIssuesAcceptanceDetail.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			pendingIssuesAcceptanceDetail.setDoa(doa != null ? doa : null);
			pendingIssuesAcceptanceDetail.setPdd(pdd != null ? pdd : null);
			pendingIssuesAcceptanceDetail.setStatus(issue.getStatus().getStatus());
			pendingIssuesAcceptanceDetail.setStatusId(issue.getStatus().getId());
			pendingIssuesAcceptanceDetail.setImagePath(issue.getAdmission().getPatient().getImagePath());
			pendingIssuesAcceptanceDetail.setGenderId(issue.getAdmission().getPatient().getGender().getGenderId());
		} else if (issue.getEncounter() != null) {
			Integer age = Util
					.getAgeByBirthDate(issue.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " "
					+ issue.getEncounter().getPatient().getFirstName() + " "
					+ issue.getEncounter().getPatient().getMiddleName() + " "
					+ issue.getEncounter().getPatient().getLastName() + " | " + age + " | "
					+ issue.getEncounter().getPatient().getGender().getGenderCode();
			pendingIssuesAcceptanceDetail = new PendingIssuesAcceptanceDetails(issue.getId(),
					issue.getEncounter().getPatient().getPatientId(), patientDetail,
					issue.getEncounter().getPatient().getDob(),
					doctorDeatil == null ? null : issue.getDoctor().getDoctorId(), doctorDeatil,
					issue.getEncounter().getPatient().getUhidNumber(), issue.getIssueNo(), issue.getAddedDate(),
					issue.getPrescription().getPrescriptionNo(), issue.getStore().getStoreName());
			pendingIssuesAcceptanceDetail.setStatus(issue.getStatus().getStatus());
			pendingIssuesAcceptanceDetail.setStatusId(issue.getStatus().getId());
			pendingIssuesAcceptanceDetail.setImagePath(issue.getEncounter().getPatient().getImagePath());
			pendingIssuesAcceptanceDetail.setGenderId(issue.getEncounter().getPatient().getGender().getGenderId());
		}

		List<PendingIssuesAcceptanceItem> list=pendingIssuesAcceptanceMedicationDao.pendingIssuesAcceptanceMedicationDetails(id);
	
		for(PendingIssuesAcceptanceItem i : list)
		{
			List<Integer> itemIds =new ArrayList<>();
			itemIds.add(i.getItemId());
		 
			List<UomResponse> uoms = null;
			if (itemIds.size() > 0) {
				uoms = GlobalDao.getUnitOfMeasurementListByItemBettoerPerformance(itemIds,issue.getOrganizationId());
			}
			i.setUomResponse(uoms);
			//Integer issueQuantity = Util.GetPendingIndentQuantityLeaseQuantityStoreIndent(i.getQuantityIssued(),unitOfMeasurementDao.getAllByItemId(i.getItemId(),issue.getOrganizationId()), i.getUomTypeId());
			//i.setQuantityIssued(issueQuantity);
		}
		pendingIssuesAcceptanceDetail.setCarrier(carrier!= null ? carrier : "");
		pendingIssuesAcceptanceDetail .setItems(list);
		return pendingIssuesAcceptanceDetail;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object pendingIssuesAcceptanceMedicationSave(PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave) {
		
	    
	    Boolean flagStatus=pendingIssuesAcceptanceMedicationDao.getMedicationFlag(pendingIssuesAcceptanceSave);
		screen = GlobalDao.getScreen(screenId);
		Issue issue = pendingIssuesAcceptanceMedicationDao
				.pendingIssuesAcceptanceMedicationById(pendingIssuesAcceptanceSave.getId());

		ReturnOfEmptyAmpoules ampoules = new ReturnOfEmptyAmpoules();
		List<ReturnOfEmptyAmpoulesDetail> listNew = new ArrayList<>();
		int narFlag = -1;
		for (IssueDetail issueDetail : GlobalDao.getIssueDeatilByIssueId(issue.getId())) {
			for (IssueDetailBatchMapper batchMapper : issueDetail.getIssueDetailBatchMapper()) {
				for (PendingIssuesAcceptanceSaveItems issueDetailBatchMapper : pendingIssuesAcceptanceSave.getItems()) {
					if (issueDetailBatchMapper.getBatchMapperId() != null
							&& (batchMapper.getId().equals(issueDetailBatchMapper.getBatchMapperId())))
					// if (issueDetailBatchMapper.getBatchMapperId() != null && batchMapper.getId()
					// == issueDetailBatchMapper.getBatchMapperId())
					{
						if (issueDetailBatchMapper.getAcceptQuantity() >= 0) {
							/*
							 * BatchMapperStoreStock(issue.getStore().getStoreId(), batchMapper.getBatch(),
							 * batchMapper.getIssueQuantity(), batchMapper.getUomType(),
							 * issueDetailBatchMapper.getAcceptQuantity(), pendingIssuesAcceptanceSave,
							 * batchMapper,issueDetailBatchMapper,issue);
							 */

							batchMapper.updateIssueDetailBatchMapper(issueDetailBatchMapper.getBatchMapperId(),
									issueDetailBatchMapper.getAcceptQuantity(),
									(issueDetailBatchMapper.getNote() != null) ? issueDetailBatchMapper.getNote()
											: null,
									issueDetailBatchMapper.getRejectQuantity(),
									(issueDetailBatchMapper.getReasonId() != null)
											? new RejectReason(issueDetailBatchMapper.getReasonId())
											: null);
							batchMapper.setReturnAcceptedQuantity(0);
							batchMapper.setReturnRejectedQuantity(0);
						}
						// if (issue.getStore().getStoreType().getId() == 6) {
						Boolean isNarcotic = true;
						if(flagStatus==true)
						{
							narFlag = Boolean.valueOf(issue.getIsNarcotics())
									.compareTo(Boolean.valueOf(isNarcotic));
						}
						else {
						narFlag = Boolean.valueOf(issue.getStore().getIsNarcotic())
								.compareTo(Boolean.valueOf(isNarcotic));
						}
						if (narFlag == 0) {

							if (issue.getVisitType().getVisitTypeId().equals(ER)) {
								ampoules = new ReturnOfEmptyAmpoules(new Issue(issue.getId()),
										new PatientRegistration(
												issue.getPrescription().getAdmission().getPatient().getPatientId()),
										new WardMasters(issue.getAdmission().getAdmissionDetail().getFirst().getWardMaster()
												.getWardId()),
										new BedMaster(issue.getAdmission().getAdmissionDetail().getFirst().getBedMaster()
												.getBedId()),
										new Priority(issue.getPrescription().getPriority().getPriorityId()),
										new Status(com.param.common.constants.Status.New.getStatus()));

								// sessionService.setEntityDefaults(ampoules);
								ampoules.setCreateDefaults(
										pendingIssuesAcceptanceSave.getOrganizationId() != null
												? pendingIssuesAcceptanceSave.getOrganizationId()
												: sessionService.getOrganizationId(),
										pendingIssuesAcceptanceSave.getUnitId() != null
												? pendingIssuesAcceptanceSave.getUnitId()
												: sessionService.getUnitId(),
										pendingIssuesAcceptanceSave.getUserId() != null
												? pendingIssuesAcceptanceSave.getUserId()
												: sessionService.getUserId());
								ReturnOfEmptyAmpoulesDetail returnOfEmptyAmpoulesDetail = new ReturnOfEmptyAmpoulesDetail(
										new ReturnOfEmptyAmpoules(ampoules.getId()),
										issueDetailBatchMapper.getAcceptQuantity(),
										new Status(com.param.common.constants.Status.New.getStatus()),
										new IssueDetailBatchMapper(batchMapper.getId()));

								returnOfEmptyAmpoulesDetail.setCreateDefaults(
										pendingIssuesAcceptanceSave.getOrganizationId() != null
												? pendingIssuesAcceptanceSave.getOrganizationId()
												: sessionService.getOrganizationId(),
										pendingIssuesAcceptanceSave.getUnitId() != null
												? pendingIssuesAcceptanceSave.getUnitId()
												: sessionService.getUnitId(),
										pendingIssuesAcceptanceSave.getUserId() != null
												? pendingIssuesAcceptanceSave.getUserId()
												: sessionService.getUserId());
								listNew.add(returnOfEmptyAmpoulesDetail);
							} else {
								if (!issue.getVisitType().getVisitTypeId().equals(IP)) {
									Doctor doctor = new Doctor();
									doctor.setDoctorId(0);
									issue.setDoctor(doctor);
								}
								ampoules = new ReturnOfEmptyAmpoules(new Issue(issue.getId()),
										new PatientRegistration(
												issue.getPrescription().getAdmission().getPatient().getPatientId()),

										new WardMasters(issue.getAdmission().getAdmissionDetail().getFirst().getWardMaster()
												.getWardId()),
										new BedMaster(issue.getAdmission().getAdmissionDetail().getFirst().getBedMaster()
												.getBedId()),
										new Priority(issue.getPrescription().getPriority().getPriorityId()),
										new Status(com.param.common.constants.Status.New.getStatus()));

								// sessionService.setEntityDefaults(ampoules);
								ampoules.setCreateDefaults(
										pendingIssuesAcceptanceSave.getOrganizationId() != null
												? pendingIssuesAcceptanceSave.getOrganizationId()
												: sessionService.getOrganizationId(),
										pendingIssuesAcceptanceSave.getUnitId() != null
												? pendingIssuesAcceptanceSave.getUnitId()
												: sessionService.getUnitId(),
										pendingIssuesAcceptanceSave.getUserId() != null
												? pendingIssuesAcceptanceSave.getUserId()
												: sessionService.getUserId());

								ReturnOfEmptyAmpoulesDetail returnOfEmptyAmpoulesDetail = new ReturnOfEmptyAmpoulesDetail(
										new ReturnOfEmptyAmpoules(ampoules.getId()),
										issueDetailBatchMapper.getAcceptQuantity(),
										new Status(com.param.common.constants.Status.New.getStatus()),
										new IssueDetailBatchMapper(batchMapper.getId()));

								returnOfEmptyAmpoulesDetail.setCreateDefaults(
										pendingIssuesAcceptanceSave.getOrganizationId() != null
												? pendingIssuesAcceptanceSave.getOrganizationId()
												: sessionService.getOrganizationId(),
										pendingIssuesAcceptanceSave.getUnitId() != null
												? pendingIssuesAcceptanceSave.getUnitId()
												: sessionService.getUnitId(),
										pendingIssuesAcceptanceSave.getUserId() != null
												? pendingIssuesAcceptanceSave.getUserId()
												: sessionService.getUserId());
								listNew.add(returnOfEmptyAmpoulesDetail);
							}

						}

					}

					// saveStoreStock(issueDetail.getIssue().getStore().getStoreId(),issueDetail.getBatch().getId(),issueDetailBatchMapper.getAcceptQuantity());

				}
			}

		}
		ampoules.setCreateDefaults(
				pendingIssuesAcceptanceSave.getOrganizationId() != null
						? pendingIssuesAcceptanceSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				pendingIssuesAcceptanceSave.getUnitId() != null ? pendingIssuesAcceptanceSave.getUnitId()
						: sessionService.getUnitId(),
				pendingIssuesAcceptanceSave.getUserId() != null ? pendingIssuesAcceptanceSave.getUserId()
						: sessionService.getUserId());

		ampoules.addReturnDetailList1(listNew);
	//	issue.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
		issue.setUpdatedDate(TimeZoneComponent.getDateByzone(pendingIssuesAcceptanceSave.getUnitId()));
		issue.setUpdatedByUser(pendingIssuesAcceptanceSave.getUserId() != null ? pendingIssuesAcceptanceSave.getUserId()
				: sessionService.getUserId());
		Response res = pendingIssuesAcceptanceMedicationDao.getIssueStatus(issue.getId());
		Integer statusId = 4;
		Integer partially  = 18;
		if (res.getObject() != null) {
			ItemRequestDto dto = (ItemRequestDto) res.getObject();

			if (dto.getStatusId().equals(statusId)) {
                //changed by nitin
				
				issue.setStatus(new Status(pendingIssuesAcceptanceSave.getStatusId()));
				
				/*if (pendingIssuesAcceptanceSave.getStatusId().equals(statusId)) {
					issue.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
				} else if (pendingIssuesAcceptanceSave.getStatusId().equals(partially)) {
					issue.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
				}else {
					issue.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
				}*/

			} else {
				issue.setStatus(new Status(dto.getStatusId()));
				//issue.setStatus(new Status(pendingIssuesAcceptanceSave.getStatusId()));
			}
		}
		pendingIssuesAcceptanceMedicationDao.pendingIssuesAcceptanceMedicationSave(issue);
		if (ampoules != null && narFlag == 0) {
			// ampoules.setIssue(issue);

			pendingIssuesAcceptanceMedicationDao.saveAmpoules(ampoules);
		}

		saveRejectDetails(pendingIssuesAcceptanceSave);
		CommonResponse response = new CommonResponse();
		response.setId(issue.getId().longValue());
		response.setMessage("Issue Acceptance Medication saved successfully");

		return response;
	}

	private void BatchMapperStoreStock(Integer storeId, Batch batch, Integer issueQuantity, UomType uomType,
			Integer acceptQty, PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave, IssueDetailBatchMapper ib, PendingIssuesAcceptanceSaveItems issueDetailBatchMapper, Issue issue) {

		boolean hasStock = false;

		for (StoreStock storeStock : batch.getStoreStockList()) {
			if (storeStock.getStore().getStoreId().equals(storeId)) {

				hasStock = true;
				Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId());

				Integer leaseQuantity = Util.GetLeaseQuantity(acceptQty, batch.getBatchUomList(), uomType);

				// Integer balanceQuantity = avaialableStoreStock + leaseQuantity; //comment
				// code for resolved HIS-9152

				// storeStock.setAvailableQty(balanceQuantity); //comment code for resolved
				// HIS-9152

				Integer qty = 0;
				if (storeStock.gettQty() != null) {
					qty = storeStock.gettQty();
				 	storeStock.settQty((qty-leaseQuantity)<0 ? storeStock.gettQty() : (qty-leaseQuantity));
				}
				// storeStock.settQty(qty - acceptQty);

				// start code for resolved HIS-9152 Store Stock Dashboard-The Qty which is
				// accepted in Patient Issue Acceptance is getting added to AvailableQty in
				// StoreStockDashboard.
				Integer rejectleaseQty = Util.GetLeaseQuantity(issueDetailBatchMapper.getRejectQuantity()!=null ? issueDetailBatchMapper.getRejectQuantity() : 0, batch.getBatchUomList(), uomType);
				//Integer 
				//// storeStock.settQty(qty - acceptleaseQty);
				//storeStock.setAvailableQty(avaialableStoreStock);

				StockTransaction newStockTrans = new StockTransaction();
				newStockTrans.setCurrentBal(avaialableStoreStock);
				newStockTrans.setDoc(screen);
				newStockTrans.setDocType(screen.getCode());
				newStockTrans.setIsTransit(true);
				newStockTrans.setQuantity(rejectleaseQty);
				newStockTrans.setStore(new Store(storeId));
				newStockTrans.setItem(batch.getItem());
				newStockTrans.setTransitNo(issue.getIssueNo());
				newStockTrans.setCreateDefaults(
						pendingIssuesAcceptanceSave.getOrganizationId() != null
								? pendingIssuesAcceptanceSave.getOrganizationId()
								: sessionService.getOrganizationId(),
						pendingIssuesAcceptanceSave.getUnitId() != null ? pendingIssuesAcceptanceSave.getUnitId()
								: sessionService.getUnitId(),
						pendingIssuesAcceptanceSave.getUserId() != null ? pendingIssuesAcceptanceSave.getUserId()
								: sessionService.getUserId());
				newStockTrans.setTransactionDetialsId(ib.getId());
				// batch.addStockTransaction(new StockTransaction(avaialableStoreStock, screen,
				// screen.getCode(), acceptleaseQty,
				// batch.getStoreStockList().get(0).getStore()));
				batch.addStockTransaction(newStockTrans);

				// end code for resolved HIS-9152 Store Stock Dashboard-The Qty which is
				// accepted in Patient Issue Acceptance is getting added to AvailableQty in
				// StoreStockDashboard.

				// batch.addStockTransaction(new StockTransaction(balanceQuantity, screen,
				// screen.getCode(), leaseQuantity,
				// batch.getStoreStockList().get(0).getStore()));
			}
		}
		/*if (!hasStock) {

			new StoreStock(issueQuantity, new Store(storeId), new Batch(batch.getId()));

			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batch.getId());

			Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity, batch.getBatchUomList(), uomType);

			StockTransaction newStockTrans = new StockTransaction();
			newStockTrans.setCurrentBal(avaialableStoreStock);
			newStockTrans.setDoc(screen);
			newStockTrans.setDocType(screen.getCode());
			newStockTrans.setQuantity(leaseQuantity);
			newStockTrans.setStore(batch.getStoreStockList().get(0).getStore());
			newStockTrans.setCreateDefaults(
					pendingIssuesAcceptanceSave.getOrganizationId() != null
							? pendingIssuesAcceptanceSave.getOrganizationId()
							: sessionService.getOrganizationId(),
					pendingIssuesAcceptanceSave.getUnitId() != null ? pendingIssuesAcceptanceSave.getUnitId()
							: sessionService.getUnitId(),
					pendingIssuesAcceptanceSave.getUserId() != null ? pendingIssuesAcceptanceSave.getUserId()
							: sessionService.getUserId());

			batch.addStockTransaction(newStockTrans);
			// batch.addStockTransaction(new StockTransaction(avaialableStoreStock, screen,
			// screen.getCode(),
			// leaseQuantity, batch.getStoreStockList().get(0).getStore()));
		}*/
	}

	private void saveRejectDetails(PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave) {
		Timestamp now = new Timestamp((new DateTime()).getMillis());
		boolean flag = false;

		/*
		 * Integer storeId = sessionService.getStoreId();
		 * 
		 * Store storeNew = GlobalDao.getStore(storeId);
		 * 
		 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		 */

		String rejectBillNumber = GlobalService.getBillNoByGeneratedCode(pendingIssuesAcceptanceSave.getStoreId(),
				pendingIssuesAcceptanceSave.getUnitId(), rejectScreenId);

		Reject reject = new Reject(rejectBillNumber, new Issue(pendingIssuesAcceptanceSave.getId()),
				new User(sessionService.getUserId()), now,
				new Status(com.param.common.constants.Status.Pending.getStatus()));

		for (PendingIssuesAcceptanceSaveItems issueDetailItem : pendingIssuesAcceptanceSave.getItems()) {
			if (issueDetailItem.getRejectQuantity() > 0) {
				flag = true;
				RejectDetail rejectDetail = new RejectDetail(
						new IssueDetailBatchMapper(issueDetailItem.getBatchMapperId()),
						issueDetailItem.getRejectQuantity(),
						new Status(com.param.common.constants.Status.Pending.getStatus()),
						(issueDetailItem.getReasonId() != null) ? new RejectReason(issueDetailItem.getReasonId())
								: null,
						issueDetailItem.getNote());
				rejectDetail.setCreateDefaults(
						pendingIssuesAcceptanceSave.getOrganizationId() != null
								? pendingIssuesAcceptanceSave.getOrganizationId()
								: sessionService.getOrganizationId(),
						pendingIssuesAcceptanceSave.getUnitId() != null ? pendingIssuesAcceptanceSave.getUnitId()
								: sessionService.getUnitId(),
						pendingIssuesAcceptanceSave.getUserId() != null ? pendingIssuesAcceptanceSave.getUserId()
								: sessionService.getUserId());

				reject.addRejectDetail(rejectDetail);

			}
		}
		if (flag == true) {
			reject.setCreateDefaults(pendingIssuesAcceptanceSave.getOrganizationId(),
					pendingIssuesAcceptanceSave.getUnitId(), pendingIssuesAcceptanceSave.getUserId());
			pendingIssuesAcceptanceMedicationDao.pendingIssuesAcceptanceMedicationRejectSave(reject);
		}
	}

	@Override
	@Transactional
	public List<MedicationReconciliationSearchDto> medicationHandoverSearch(
			MedicationReconciliationSearchRequest request, Integer admissionId) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		List<MedicationReconciliationSearchDto> medicationReconciliationSearchs = new ArrayList<MedicationReconciliationSearchDto>();
		for (PrescriptionDetail prescriptionDetail : pendingIssuesAcceptanceMedicationDao
				.medicationHandoverSearch(request, admissionId, admissionId)) {
			/*
			 * List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
			 * .doctorSearchByIdValue(prescriptionDetail.getPrescription().getDoctor().
			 * getDoctorId());
			 */
			
			MedicationReconciliationSearchDto res=pendingIssuesAcceptanceMedicationDao.getIssueAcceptedQuantity(prescriptionDetail.getId());
			if(res.getAcceptedQuantity()!=null) {
			String doctorDeatil = prescriptionDetail.getPrescription().getDoctor().getFirstName() + " "
					+ prescriptionDetail.getPrescription().getDoctor().getMiddleName() + " "
					+ prescriptionDetail.getPrescription().getDoctor().getLastName() + " | "
					+ prescriptionDetail.getPrescription().getSpecialityMaster().getSpecialityName();

			if (prescriptionDetail.getPrescription().getAdmission() != null) {
				String wardName = "";
				String bedNumber = "";
				AdmissionDetail admissionDetail = GlobalDao
						.getPatientWardBedDetails(prescriptionDetail.getPrescription().getAdmission().getAdmissionId());
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
				Integer age = Util.getAgeByBirthDate(prescriptionDetail.getPrescription().getAdmission().getPatient()
						.getDob().toLocalDateTime().toLocalDate());
				String patientDetail = prescriptionDetail.getPrescription().getAdmission().getPatient().getPrefix()
						.getPrefixCode() + " "
						+ prescriptionDetail.getPrescription().getAdmission().getPatient().getFirstName() + " "
						+ prescriptionDetail.getPrescription().getAdmission().getPatient().getMiddleName() + " "
						+ prescriptionDetail.getPrescription().getAdmission().getPatient().getLastName() + " | " + age
						+ " | "
						+ prescriptionDetail.getPrescription().getAdmission().getPatient().getGender().getGenderCode();
				Integer sId = 0;
				String rStatus = "";
				if (prescriptionDetail.getPrescription().getReconciliationStatus() != null) {
					sId = prescriptionDetail.getPrescription().getReconciliationStatus().getId();
				}
				if (prescriptionDetail.getPrescription().getReconciliationStatus() != null) {
					rStatus = prescriptionDetail.getPrescription().getReconciliationStatus().getStatus();
				}
				MedicationReconciliationSearchDto disDate = pendingIssuesAcceptanceMedicationDao
						.getDischargeDate(admissionId);
				MedicationReconciliationSearchDto medicationReconciliationSearchDto = new MedicationReconciliationSearchDto(
						prescriptionDetail.getPrescription().getId(),
						prescriptionDetail.getPrescription().getAdmission().getPatient().getUhidNumber(), patientDetail,
						doctorDeatil, rStatus, sId,disDate!=null ? (disDate.getDischargedate()!=null ? disDate.getDischargedate():null):null,disDate!=null ?  (disDate.getDischargestatusid()!=null ? disDate.getDischargestatusid():null):null,
								disDate!=null ? (disDate.getDischargedesc()!=null ? disDate.getDischargedesc():null):null);
				medicationReconciliationSearchDto.setWardName(!wardName.equals("") ? wardName : null);
				medicationReconciliationSearchDto.setBedNo(!bedNumber.equals("") ? bedNumber : null);
				medicationReconciliationSearchs.add(medicationReconciliationSearchDto);
			}
		}
		}
		return medicationReconciliationSearchs;

	}

	@Override
	@Transactional
	public MedicationHandoverResponse medicationHandoverDetail(Integer admissionId) {

		Prescription prescription = pharmacologyValidationDao.getPrescriptionByAdmissionId(admissionId);

		MedicationHandoverResponse medicationReconciliationResponse = new MedicationHandoverResponse();

		String doctorDeatil = "";
		if (prescription.getDoctor() != null && prescription.getSpecialityMaster() != null) {
			doctorDeatil = prescription.getDoctor().getFirstName() + " " + prescription.getDoctor().getMiddleName()
					+ " " + prescription.getDoctor().getLastName() + " | "
					+ prescription.getSpecialityMaster().getSpecialityName();
		}

		if (prescription.getAdmission() != null) {

			String wardName = "";
			String bedNumber = "";
			Integer wardId = 0;
			Integer bedId = 0;
			Date doa = null;
			Date pdd = null;
			AdmissionDetail admissionDetail = GlobalDao
					.getPatientWardBedDetails(prescription.getAdmission().getAdmissionId());
			if (admissionDetail != null) {
				doa = admissionDetail.getDoa();
				pdd = admissionDetail.getPdd();
				WardMasters wardMaster = admissionDetail.getWardMaster();
				if (wardMaster != null) {
					wardName = wardMaster.getWardName();
					wardId = wardMaster.getWardId();
				}
				BedMaster bedMaster = admissionDetail.getBedMaster();
				if (bedMaster != null) {
					bedNumber = bedMaster.getBedNumber();
					bedId = bedMaster.getBedId();
				}
			}

			Integer age = Util.getAgeByBirthDate(
					prescription.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = prescription.getAdmission().getPatient().getPrefix().getPrefixCode() + " "
					+ prescription.getAdmission().getPatient().getFirstName() + " "
					+ prescription.getAdmission().getPatient().getMiddleName() + " "
					+ prescription.getAdmission().getPatient().getLastName() + " | " + age + " | "
					+ prescription.getAdmission().getPatient().getGender().getGenderCode();

			medicationReconciliationResponse = new MedicationHandoverResponse(prescription.getId(),
					prescription.getAdmission().getPatient().getUhidNumber(),
					prescription.getAdmission().getPatient().getPatientId(), patientDetail,
					prescription.getAdmission().getPatient().getDob(), prescription.getDoctor().getDoctorId(),
					doctorDeatil);
			medicationReconciliationResponse.setDoa(doa != null ? doa : null);
			medicationReconciliationResponse.setPdd(pdd != null ? pdd : null);
			medicationReconciliationResponse.setWardName(!wardName.equals("") ? wardName : null);
			medicationReconciliationResponse.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			medicationReconciliationResponse.setWardId(wardId != null ? wardId : null);
			medicationReconciliationResponse.setBedId(bedId != null ? bedId : null);
			medicationReconciliationResponse.setImagePath(prescription.getAdmission().getPatient().getImagePath());
			medicationReconciliationResponse
					.setGenderId(prescription.getAdmission().getPatient().getGender().getGenderId());
		}

		List<MedicationReconciliationItem> items = new ArrayList<MedicationReconciliationItem>();
		for (PrescriptionDetail prescriptionDetail : pharmacologyValidationDao
				.pharmacologyValidationDetailsByAdmissionId(prescription.getId())) {
			for (PrescriptionFrequencyDetail prescriptionFrequencyDetail : prescriptionDetail
					.getPrescriptionFrequencyDetails()) {
				Integer patientId = prescription.getAdmission().getPatient().getPatientId();
				// Integer doctorId
				BigInteger totalIssuedQty = GlobalDao.getTotalIssuedQty(patientId,
						prescriptionDetail.getItem().getId());
				BatchDetail bthDetail = pharmacologyValidationDao.getBatchDetail(prescriptionDetail.getId());
				BigInteger consumedQty =BigInteger.valueOf(0);
				if(bthDetail!=null) { 
					consumedQty = GlobalDao.getConsumedQty(patientId, prescriptionDetail.getItem().getId(), Long.valueOf(bthDetail.getBatchId()), 0);
				}
				
				// prescriptionDetail.getItem().get

				String bthNo = "";
				Date expDate = null;
				Integer issueQty = 0;
				Integer acceptedqty = 0;
				if (bthDetail != null) {
					bthNo = bthDetail.getBatcnno();
					expDate = bthDetail.getExpdate();
					issueQty = bthDetail.getIssuequantity();
					if (bthDetail.getAcceptedqty() != null) {
						acceptedqty = bthDetail.getAcceptedqty();
					}
				}
				MedicationReconciliationItem item = new MedicationReconciliationItem(prescriptionDetail.getId(),
						prescriptionDetail.getItem().getId(), prescriptionDetail.getItem().getName(),
						prescriptionDetail.getItem().getGeneric().getId(),
						prescriptionDetail.getItem().getGeneric().getName(),
						prescriptionFrequencyDetail.getRouteOfAdministration()!=null ? 
								prescriptionFrequencyDetail.getRouteOfAdministration().getDescription():"",
						prescriptionFrequencyDetail.getDose(),
						prescriptionFrequencyDetail.getFrequency().getDescription(),
						prescriptionFrequencyDetail.getStartDate(), prescriptionFrequencyDetail.getStopDate(),
						prescriptionDetail.getOrderQty(),
						GlobalDao.getStoreStockByItem(prescriptionDetail.getStore().getStoreId(),
								prescriptionDetail.getItem().getId(), prescription.getUnit().getUnitId()),
						prescriptionDetail.getRevisedOrderedQuantity(), totalIssuedQty, consumedQty, bthNo, expDate,
						issueQty, acceptedqty);
				items.add(item);
			}
		}
		ReceiverResponse receiverResponse = GlobalDao.getReceiverDetails(prescription.getId(),
				prescription.getUnitId());
		if (receiverResponse != null) {
			medicationReconciliationResponse.setReceiverName(receiverResponse.getReceivername());
			medicationReconciliationResponse.setReceiverContactDetails(receiverResponse.getContactdetail());
			medicationReconciliationResponse.setRelationshipId(receiverResponse.getRelationid());
			medicationReconciliationResponse.setRelationship(receiverResponse.getRelationname());

		}
		medicationReconciliationResponse.setItems(items);
		return medicationReconciliationResponse;

	}

	@Override
	@Transactional
	public Object medicationHandoverDetail(MedicationHandoverSaveRequest request) {
		Prescription p = pendingIssuesAcceptanceMedicationDao.getPrescription(request.getPrescriptionId());
		CommonResponse response = new CommonResponse();
		MedicationHandover mh = new MedicationHandover();
		mh.setPrescriptionId(request.getPrescriptionId());
		mh.setReceiverName(request.getReceiverName());
		mh.setReceiverContactDetails(request.getReceiverContactDetails());
		mh.setRelationshipId(request.getRelationshipId());
		mh.setVisitTypeId(request.getVisitTypeId());
		// sessionService.setEntityDefaults(mh);
		mh.setCreateDefaults(
				request.getOrganizationId() != null ? request.getOrganizationId() : sessionService.getOrganizationId(),
				request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
				request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

		for (PrescriptionDetailsList list : request.getPrescriptionDetailsList()) {
			MedicationHandoverDetail mhDetail = new MedicationHandoverDetail();
			mhDetail.setPrescriptionDetailId(list.getPrescriptionDetailId());
			mhDetail.setItemId(list.getItemId());
			mhDetail.setHandOverQty(list.getHandOverQty());
			// sessionService.setEntityDefaults(mhDetail);
			mhDetail.setCreateDefaults(
					request.getOrganizationId() != null ? request.getOrganizationId()
							: sessionService.getOrganizationId(),
					request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

			mh.addMedicationHandoverDetail(mhDetail);
			pendingIssuesAcceptanceMedicationDao.medicationHandoverSave(mh);
			response.setMessage("Medication Handover saved successfully");
		}
		p.setReconciliationStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
		pendingIssuesAcceptanceMedicationDao.update(p);
		return response;

	}

}
