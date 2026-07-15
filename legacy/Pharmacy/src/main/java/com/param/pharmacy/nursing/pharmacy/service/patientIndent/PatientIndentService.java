package com.param.pharmacy.nursing.pharmacy.service.patientIndent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.FloorMasters;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.PatientIndent;
import com.param.entity.model.pharmacy.PatientIndentDetail;
import com.param.global.common.ICommonConstants;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.Floor;
import com.param.global.dto.global.Ward;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.pendingIssues.IPendingIssuesDao;
import com.param.pharmacy.nursing.pharmacy.dao.patientIndent.IPatientIndentDao;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentDetails;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentDropdown;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentItem;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentList;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentRequest;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentSave;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentSaveItem;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("patientIndentService")
@Transactional
public class PatientIndentService implements ICommonConstants,IPatientIndentService {
	@Autowired
	private SessionService sessionService;

	@Autowired
	private IPatientIndentDao patientIndentDao;

	@Autowired
	private IPendingIssuesDao pendingIssuesDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	private Integer screenId = Screen.PatientIndent.getScreen();

	private Integer issueScreenId = Screen.PatientIssueConsumable.getScreen();

	@Override
	@Transactional
	public PatientIndentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId) {
		PatientIndentDropdown response = new PatientIndentDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (WardMasters ward : GlobalDao.getWardList(unitId)) {
			response.addWard(new Ward(ward.getWardId(), ward.getWardName()));
		}

		for (FloorMasters floor : GlobalDao.getFloorList(unitId)) {
			response.addFloor(new Floor(floor.getFloorId(), floor.getFloorName()));
		}
		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<PatientIndentList> patientIndentSearch(PatientIndentRequest patientIndentRequest) {
		List<PatientIndentList> patientIndentList = new ArrayList<PatientIndentList>();
		patientIndentRequest.setToDate((new DateTime(patientIndentRequest.getToDate())).plusDays(1).toDate());
		for (PatientIndent patientIndent : patientIndentDao.patientIndentSearch(patientIndentRequest, screenId)) {
			Integer age = Util.getAgeByBirthDate(patientIndent.getPatient().getDob().toLocalDateTime().toLocalDate());

			String patientDetail = patientIndent.getPatient().getPrefix().getPrefixCode() + " "
					+ patientIndent.getPatient().getFirstName() + " " + patientIndent.getPatient().getMiddleName() + " "
					+ patientIndent.getPatient().getLastName();

			/*
			 * List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
			 * .doctorSearchByIdValue(patientIndent.getDoctor().getDoctorId()); String
			 * drSpeciality = ""; if (doctorSpeciality.size() > 0) { drSpeciality =
			 * doctorSpeciality.get(0).getSpeciality().getSpecialityName(); }
			 */

			// No doctor found for emergency admission
			String doctorDetail = "";
			if (patientIndent.getDoctor() != null) {
				doctorDetail = patientIndent.getDoctor().getFirstName() + " | "
						+ patientIndent.getDoctor().getMiddleName() + " " + patientIndent.getDoctor().getLastName();
			}

			// + " | "+ drSpeciality;

			if (patientIndent.getAdmission() != null) {
				PatientIndentList patientIndentObj = new PatientIndentList(patientIndent.getId(),
						patientIndent.getIndentNo(), patientIndent.getAddedDate(),
						patientIndent.getVisitType().getVisitTypeCode(),
						patientIndent.getAdmission().getAdmissionNumber(), patientIndent.getPatient().getUhidNumber(),
						patientDetail, age, patientIndent.getPatient().getGender().getGenderCode(),
						patientIndent.getPatient().getImagePath(), doctorDetail,
						(patientIndent.getStore()!=null && patientIndent.getStore().getStoreName()!=null ? patientIndent.getStore().getStoreName() :""), (patientIndent.getToStore()!=null && patientIndent.getToStore().getStoreName()!=null ? patientIndent.getToStore().getStoreName():""),
						patientIndent.getStatus() != null ? patientIndent.getStatus().getStatus() : null,
						patientIndent.getStatus() != null ? patientIndent.getStatus().getId() : null);
				patientIndentObj
						.setWardName(patientIndent.getWard() != null ? patientIndent.getWard().getWardName() : null);
				patientIndentObj
						.setBedNo(patientIndent.getBed() != null ? patientIndent.getBed().getBedNumber() : null);
				patientIndentObj.setApprovalStatusId(
						patientIndent.getApprovalStatus() != null ? patientIndent.getApprovalStatus().getId() : null);
				patientIndentObj.setApprovalStatus(
						patientIndent.getApprovalStatus() != null ? patientIndent.getApprovalStatus().getStatus()
								: null);
				patientIndentObj.setAddedBy(patientIndent.getAddedBy().getUserName());
				patientIndentObj.setPatientId(patientIndent.getPatient().getPatientId());
				patientIndentList.add(patientIndentObj);
			} else if (patientIndent.getEncounter() != null) {
				PatientIndentList patientIndentObj = new PatientIndentList(patientIndent.getId(),
						patientIndent.getIndentNo(), patientIndent.getAddedDate(),
						patientIndent.getVisitType().getVisitTypeCode(),
						patientIndent.getEncounter().getEncounterNumber(), patientIndent.getPatient().getUhidNumber(),
						patientDetail, age, patientIndent.getPatient().getGender().getGenderCode(),
						patientIndent.getPatient().getImagePath(), doctorDetail,
						patientIndent.getStore().getStoreName(), patientIndent.getToStore().getStoreName(),
						patientIndent.getStatus().getStatus(), patientIndent.getStatus().getId());
				patientIndentObj.setApprovalStatusId(patientIndent.getApprovalStatus().getId());
				patientIndentObj.setApprovalStatus(patientIndent.getApprovalStatus().getStatus());
				patientIndentObj.setAddedBy(patientIndent.getAddedBy().getUserName());
				patientIndentObj.setPatientId(patientIndent.getPatient().getPatientId());
				patientIndentList.add(patientIndentObj);
			}

		}
		return patientIndentList;
	}

	@Override
	@Transactional
	public PatientIndentDetails patientIndentDetails(Integer id, Integer unitId) {
		PatientIndent patientIndent = patientIndentDao.patientIndentById(id, unitId);
		PatientIndentDetails patientIndentDetails = new PatientIndentDetails();

		Integer doctorId = 0;
		// doctor not found for emergency admission
		if (patientIndent.getDoctor() != null) {
			doctorId = patientIndent.getDoctor().getDoctorId();
		}
		if (patientIndent.getAdmission() != null) {
			patientIndentDetails = new PatientIndentDetails(patientIndent.getId(),
					patientIndent.getVisitType().getVisitTypeId(), patientIndent.getVisitType().getVisitTypeCode(),
					patientIndent.getStore().getStoreId(), patientIndent.getStore().getStoreName(),
					patientIndent.getToStore()!=null ? patientIndent.getToStore().getStoreId():0,
					patientIndent.getToStore()!=null ? patientIndent.getToStore().getStoreName():"",
					patientIndent.isConsignment(), patientIndent.getAdmission().getAdmissionNumber(),
					patientIndent.getPatient().getPatientId(),
					patientIndent.getPatient().getFirstName() + " " + patientIndent.getPatient().getMiddleName() + " "
							+ patientIndent.getPatient().getLastName(),
					patientIndent.getPatient().getUhidNumber(), doctorId);
			patientIndentDetails
					.setWardId(patientIndent.getWard() != null ? patientIndent.getWard().getWardId() : null);
			patientIndentDetails
					.setWardName(patientIndent.getWard() != null ? patientIndent.getWard().getWardName() : null);
			patientIndentDetails.setBedId(patientIndent.getBed() != null ? patientIndent.getBed().getBedId() : null);
			patientIndentDetails
					.setBedNo(patientIndent.getBed() != null ? patientIndent.getBed().getBedNumber() : null);
			patientIndentDetails.setAdmissionId(
					patientIndent.getAdmission() != null ? patientIndent.getAdmission().getAdmissionId() : null);
			patientIndentDetails.setRemark(patientIndent.getRemark());
			patientIndentDetails.setDob(patientIndent.getAdmission().getPatient().getDob());
			patientIndentDetails.setGenderId(patientIndent.getPatient().getGender().getGenderId());
			patientIndentDetails.setImagePath(patientIndent.getPatient().getImagePath());
			Date doa = null;
			Date pdd = null;
			AdmissionDetail admissionDetail = GlobalDao
					.getPatientWardBedDetails(patientIndent.getAdmission().getAdmissionId());
			String doctorDeatil = "";
			if (patientIndent.getDoctor() != null) {
				doctorDeatil = patientIndent.getDoctor().getFirstName() + " "
						+ patientIndent.getDoctor().getMiddleName() + " " + patientIndent.getDoctor().getLastName();
			}

			if (admissionDetail != null) {
				doa = admissionDetail.getDoa();
				pdd = admissionDetail.getPdd();
			}
			patientIndentDetails.setDoa(doa != null ? doa : null);
			patientIndentDetails.setPdd(pdd != null ? pdd : null);
			patientIndentDetails.setDoctorDetail(doctorDeatil);

		} else if (patientIndent.getEncounter() != null) {
			patientIndentDetails = new PatientIndentDetails(patientIndent.getId(),
					patientIndent.getVisitType().getVisitTypeId(), patientIndent.getVisitType().getVisitTypeCode(),
					patientIndent.getStore().getStoreId(), patientIndent.getStore().getStoreName(),
					patientIndent.getToStore().getStoreId(), patientIndent.getToStore().getStoreName(),
					patientIndent.isConsignment(), patientIndent.getEncounter().getEncounterNumber(),
					patientIndent.getPatient().getPatientId(),
					patientIndent.getPatient().getFirstName() + " " + patientIndent.getPatient().getMiddleName() + " "
							+ patientIndent.getPatient().getLastName(),
					patientIndent.getPatient().getUhidNumber(),
					patientIndent.getDoctor() != null ? patientIndent.getDoctor().getDoctorId() : null);
			patientIndentDetails.setEncounterId(
					patientIndent.getEncounter() != null ? patientIndent.getEncounter().getEncounterId() : null);
			patientIndentDetails
					.setDoctorId(patientIndent.getDoctor() != null ? patientIndent.getDoctor().getDoctorId() : null);
			patientIndentDetails.setRemark(patientIndent.getRemark());
			patientIndentDetails
					.setDob(patientIndent.getPatient() != null ? patientIndent.getPatient().getDob() : null);

			Date doa = null;
			Date pdd = null;
			String doctorDeatil = "";
			if (patientIndent.getDoctor() != null) {
				doctorDeatil = patientIndent.getDoctor().getFirstName() + " "
						+ patientIndent.getDoctor().getMiddleName() + " " + patientIndent.getDoctor().getLastName();
			}
			if (patientIndent.getAdmission() != null) {
				AdmissionDetail admissionDetail = GlobalDao
						.getPatientWardBedDetails(patientIndent.getAdmission().getAdmissionId());
				if (admissionDetail != null) {
					doa = admissionDetail.getDoa();
					pdd = admissionDetail.getPdd();
				}
				patientIndentDetails.setDoa(doa != null ? doa : null);
				patientIndentDetails.setPdd(pdd != null ? pdd : null);
			}

			patientIndentDetails.setDoctorDetail(doctorDeatil);
		}

		List<PatientIndentItem> items = new ArrayList<PatientIndentItem>();
		for (PatientIndentDetail patientIndentDetail : patientIndentDao.patientIndentDetailById(id, unitId)) {
			PatientIndentItem item = new PatientIndentItem(patientIndentDetail.getId(),
					patientIndentDetail.getItem().getId(), patientIndentDetail.getItem().getCode(),
					patientIndentDetail.getItem().getName(),
					patientIndentDetail.getItem().getGeneric() != null
							? patientIndentDetail.getItem().getGeneric().getName()
							: null,
					patientIndentDetail.getUomType() != null ? patientIndentDetail.getUomType().getType() : null,
					patientIndentDetail.getUomUnit() != null ? patientIndentDetail.getUomUnit().getUnits() : null,
					patientIndentDetail.getIndentQuantity(), patientIndentDetail.getPendingQuantity(),
					patientIndentDetail.getItem().getGeneric() != null
							? patientIndentDetail.getItem().getGeneric().getId()
							: null, GlobalDao.getUnitOfMeasurementListByItem(patientIndentDetail.getItem().getId(), patientIndent.getOrganizationId()),
					patientIndentDetail.getItem().getIsThreeUOMApplicable());
			Long stock = GlobalDao.getStoreStockByItem(patientIndent.getStore().getStoreId(),
					patientIndentDetail.getItem().getId(), patientIndent.getUnitId());
			item.setStockAvailable(stock.intValue());
			items.add(item);
		}
		patientIndentDetails.setItems(items);
		return patientIndentDetails;

	}

	@Override
	@Transactional
	public Object patientIndentSave(PatientIndentSave patientIndentSave) {
		/*
		 * Store storeNew = GlobalDao.getStore(patientIndentSave.getStoreId());
		 * 
		 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		 */

		String patientIndentNumber = GlobalService.getBillNoByGeneratedCode(patientIndentSave.getStoreId(),
				patientIndentSave.getUnitId(), screenId);

		PatientIndent patientIndent = new PatientIndent();
		if (patientIndentSave.getAdmissionId() != null) {
			patientIndent = new PatientIndent(patientIndentNumber,
					patientIndentSave.getVisitType() != null ? new VisitType(patientIndentSave.getVisitType()) : null,
					patientIndentSave.getPatientId() != null ? new PatientRegistration(patientIndentSave.getPatientId())
							: null,
					patientIndentSave.getWard() != null ? new WardMasters(patientIndentSave.getWard()) : null,
					patientIndentSave.getStoreId() != null ? new Store(patientIndentSave.getStoreId()) : null,
					patientIndentSave.getToStoreId() != null ? new Store(patientIndentSave.getToStoreId()) : null,
					patientIndentSave.isConsignment(), patientIndentSave.getRemark(),
					patientIndentSave.getStatusId() != null ? new Status(patientIndentSave.getStatusId()) : null,
					patientIndentSave.getApprovalStatusId() != null
							? new Status(patientIndentSave.getApprovalStatusId())
							: null,
					patientIndentSave.getBed() != null ? new BedMaster(patientIndentSave.getBed()) : null,
					patientIndentSave.getDoctorId() != null ? new Doctor(patientIndentSave.getDoctorId()) : null,
					patientIndentSave.getAdmissionId() != null ? new Admission(patientIndentSave.getAdmissionId())
							: null);
			// sessionService.setEntityDefaults(patientIndent);
			patientIndent.setCreateDefaults(
					patientIndentSave.getOrganizationId() != null ? patientIndentSave.getOrganizationId()
							: sessionService.getOrganizationId(),
					patientIndentSave.getUnitId() != null ? patientIndentSave.getUnitId() : sessionService.getUnitId(),
					patientIndentSave.getUserId() != null ? patientIndentSave.getUserId() : sessionService.getUserId());
		} else if (patientIndentSave.getEncounterId() != null) {
			patientIndent = new PatientIndent(patientIndentNumber,
					patientIndentSave.getVisitType() != null ? new VisitType(patientIndentSave.getVisitType()) : null,
					patientIndentSave.getPatientId() != null ? new PatientRegistration(patientIndentSave.getPatientId())
							: null,
					patientIndentSave.getStoreId() != null ? new Store(patientIndentSave.getStoreId()) : null,
					patientIndentSave.getToStoreId() != null ? new Store(patientIndentSave.getToStoreId()) : null,
					patientIndentSave.isConsignment(), patientIndentSave.getRemark(),
					patientIndentSave.getStatusId() != null ? new Status(patientIndentSave.getStatusId()) : null,
					patientIndentSave.getApprovalStatusId() != null
							? new Status(patientIndentSave.getApprovalStatusId())
							: null,
					patientIndentSave.getDoctorId() != null ? new Doctor(patientIndentSave.getDoctorId()) : null,
					new Encounter(patientIndentSave.getEncounterId()));
			// sessionService.setEntityDefaults(patientIndent);
			patientIndent.setCreateDefaults(
					patientIndentSave.getOrganizationId() != null ? patientIndentSave.getOrganizationId()
							: sessionService.getOrganizationId(),
					patientIndentSave.getUnitId() != null ? patientIndentSave.getUnitId() : sessionService.getUnitId(),
					patientIndentSave.getUserId() != null ? patientIndentSave.getUserId() : sessionService.getUserId());

		}
		patientIndent.setIndentNo(patientIndentNumber);		
//		patientIndent = new PatientIndent(patientIndentNumber,
//				patientIndentSave.getVisitType() != null ? new VisitType(patientIndentSave.getVisitType()) : null,
//				patientIndentSave.getPatientId() != null ? new PatientRegistration(patientIndentSave.getPatientId())
//						: null,
//				patientIndentSave.getWard() != null ? new WardMasters(patientIndentSave.getWard()) : null,
//				patientIndentSave.getStoreId() != null ? new Store(patientIndentSave.getStoreId()) : null,
//				patientIndentSave.getToStoreId() != null ? new Store(patientIndentSave.getToStoreId()) : null,
//				patientIndentSave.isConsignment(), patientIndentSave.getRemark(),
//				patientIndentSave.getStatusId() != null ? new Status(patientIndentSave.getStatusId()) : null,
//				patientIndentSave.getApprovalStatusId() != null
//						? new Status(patientIndentSave.getApprovalStatusId())
//						: null,
//				patientIndentSave.getBed() != null ? new BedMaster(patientIndentSave.getBed()) : null,
//				patientIndentSave.getDoctorId() != null ? new Doctor(patientIndentSave.getDoctorId()) : null,
//				patientIndentSave.getAdmissionId() != null ? new Admission(patientIndentSave.getAdmissionId())
//						: null);
//		patientIndent.setEncounter(new Encounter(patientIndentSave.getEncounterId()!=null ? patientIndentSave.getEncounterId():null));
		patientIndent.setCreateDefaults(
				patientIndentSave.getOrganizationId() != null ? patientIndentSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				patientIndentSave.getUnitId() != null ? patientIndentSave.getUnitId() : sessionService.getUnitId(),
				patientIndentSave.getUserId() != null ? patientIndentSave.getUserId() : sessionService.getUserId()
						);
		for (PatientIndentSaveItem item : patientIndentSave.getItems()) {
			PatientIndentDetail patientIndentDetail = new PatientIndentDetail(new Item(item.getItemId()),
					item.getUomTypeId() != null ? new UomType(item.getUomTypeId()) : null,
					item.getUomUnitId() != null ? new UomUnit(item.getUomUnitId()) : null, item.getIndentQuantity(),
					new Status(item.getStatusId()), item.getIndentQuantity());
			patientIndent.addPatientIndentDetail(patientIndentDetail);
			// sessionService.setEntityDefaults(patientIndentDetail);
			patientIndentDetail.setCreateDefaults(
					patientIndentSave.getOrganizationId() != null ? patientIndentSave.getOrganizationId()
							: sessionService.getOrganizationId(),
					patientIndentSave.getUnitId() != null ? patientIndentSave.getUnitId() : sessionService.getUnitId(),
					patientIndentSave.getUserId() != null ? patientIndentSave.getUserId() : sessionService.getUserId());

		}

		PatientIndent patientIndentResponse = patientIndentDao.savePatientIndent(patientIndent);

		CommonResponse response = new CommonResponse();
		response.setId(patientIndentResponse.getId().longValue());
		response.setMessage("Patient Indent saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object updatePatientIndent(PatientIndentRequest patientIndentRequest) {
		PatientIndent patientIndent = patientIndentDao.patientIndentById(patientIndentRequest.getId(),
				patientIndentRequest.getUnitId());

		patientIndent.updatePatientIndent(patientIndentRequest.getConsignment(), patientIndentRequest.getRemark());

		for (PatientIndentDetail detail : patientIndent.getPatientIndentDetail()) {
			Integer id = null;

			for (PatientIndentItem item : patientIndentRequest.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();
					detail.updatePatientIndentDetail(new Item(item.getItemId()), item.getIndentQuantity(),
							item.getIndentQuantity());
					break;
				}
			}

			if (id == null) {
				detail.setIsDeleted(true);
				detail.setIsActive(false);
			}

			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(patientIndent.getUnitId()));
			detail.setUpdatedByUser(patientIndentRequest.getUserId() != null ? patientIndentRequest.getUserId()
					: sessionService.getUserId());
		}

		for (PatientIndentItem item : patientIndentRequest.getItems()) {
			if (item.getId() == null) {
				PatientIndentDetail pid = new PatientIndentDetail(item.getIndentQuantity(), new Item(item.getItemId()),
						item.getIndentQuantity(), new Status(item.getStatusId()), new UomType(item.getUomTypeId()),
						new UomUnit(item.getUomUnitId()));
				pid.setCreateDefaults(patientIndentRequest.getOrganizationId(), patientIndentRequest.getUnitId(),
						patientIndentRequest.getUserId());
				patientIndent.addPatientIndentDetail(pid);
			}
		}

		patientIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(patientIndent.getUnitId()));
		patientIndent.setUpdatedByUser(patientIndentRequest.getUserId() != null ? patientIndentRequest.getUserId()
				: sessionService.getUserId());

		patientIndentDao.updatePatientIndent(patientIndent);

		CommonResponse response = new CommonResponse();
		response.setMessage("Patient indent updated successfully");

		return response;

	}

	@Override
	@Transactional
	public Object approvePatientIndent(PatientIndentRequest patientIndentRequest) {
		/*
		 * //System.out.println("Session Store Id : "+sessionService.getStoreId());
		 * ////System.out.println("Request Store Id : "+patientIndentRequest.getStore())
		 * ;
		 * //System.out.println("Request Store Id : "+patientIndentRequest.getStore());
		 * //System.out.println("Session Unit Id : "+sessionService.getUnitId());
		 * //System.out.println("Screen Id : "+issueScreenId);
		 * 
		 * Store storeNew = GlobalDao.getStore(1);
		 */
		// Store storeNew = GlobalDao.getStore(sessionService.getStoreId());
		// System.out.println("Store Id : "+storeNew.getStoreId()+" Store Name :
		// "+storeNew.getStoreName());

		// Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		// System.out.println("Unit Id : "+unit.getUnitId()+" Store Name :
		// "+unit.getUnitName());
		// System.out.println("Screen Id : "+issueScreenId);

		for (Integer id : patientIndentRequest.getIndentId()) {
			PatientIndent patientIndent = patientIndentDao.patientIndentById(id, patientIndentRequest.getUnitId());

			patientIndent.setStatus(new Status(patientIndentRequest.getStatusId()));
			patientIndent.setApprovalStatus(new Status(patientIndentRequest.getApprovalStatusId()));

			String issuetNumber = GlobalService.getBillNoByGeneratedCode(patientIndentRequest.getStoreId(),
					patientIndentRequest.getUnitId(), issueScreenId);
			Issue issue = null;
			// no doctor found for emergency admission
			if (patientIndent.getVisitType().getVisitTypeId().equals(ER)) {
				issue = new Issue(issuetNumber, new VisitType(patientIndent.getVisitType().getVisitTypeId()),
						new Store(patientIndent.getStore().getStoreId()),
						new Store(patientIndent.getToStore().getStoreId()), new PatientIndent(patientIndent.getId()),
						new Status(com.param.common.constants.Status.Pending.getStatus()));
			} else {
	            Integer doctorId = 0;
	            if (patientIndent.getDoctor() != null) {
	                doctorId = patientIndent.getDoctor().getDoctorId();
	            }
				issue = new Issue(issuetNumber, new VisitType(patientIndent.getVisitType().getVisitTypeId()),
						new Doctor(doctorId),
						new Store(patientIndent.getStore().getStoreId()),
						new Store(patientIndent.getToStore().getStoreId()), new PatientIndent(patientIndent.getId()),
						new Status(com.param.common.constants.Status.Pending.getStatus()));
			}

			// sessionService.setEntityDefaults(issue);

			issue.setCreateDefaults(
					patientIndentRequest.getOrganizationId() != null ? patientIndentRequest.getOrganizationId()
							: sessionService.getOrganizationId(),
					patientIndentRequest.getUnitId() != null ? patientIndentRequest.getUnitId()
							: sessionService.getUnitId(),
					patientIndentRequest.getUserId() != null ? patientIndentRequest.getUserId()
							: sessionService.getUserId());

			if (patientIndent.getAdmission() != null) {
				issue.setAdmission(new Admission(patientIndent.getAdmission().getAdmissionId()));
			} else if (patientIndent.getEncounter() != null) {
				issue.setEncounter(new Encounter(patientIndent.getEncounter().getEncounterId()));
			}

			for (PatientIndentDetail detail : patientIndent.getPatientIndentDetail()) {
				detail.setStatus(new Status(patientIndentRequest.getApprovalStatusId()));

				detail.setUpdatedDate(TimeZoneComponent.getDateByzone(patientIndent.getUnitId()));
				detail.setUpdatedByUser(patientIndentRequest.getUserId() != null ? patientIndentRequest.getUserId()
						: sessionService.getUserId());

				IssueDetail issueDetail = new IssueDetail(new PatientIndentDetail(detail.getId()),
						new Status(com.param.common.constants.Status.Pending.getStatus()));
				// sessionService.setEntityDefaults(issueDetail);
				issueDetail.setCreateDefaults(
						patientIndentRequest.getOrganizationId() != null ? patientIndentRequest.getOrganizationId()
								: sessionService.getOrganizationId(),
						patientIndentRequest.getUnitId() != null ? patientIndentRequest.getUnitId()
								: sessionService.getUnitId(),
						patientIndentRequest.getUserId() != null ? patientIndentRequest.getUserId()
								: sessionService.getUserId());

				issue.addIssueDetail(issueDetail);

			}

			patientIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(patientIndent.getUnitId()));
			patientIndent.setUpdatedByUser(patientIndentRequest.getUserId() != null ? patientIndentRequest.getUserId()
					: sessionService.getUserId());
			issue.setIsImplant(false);
			patientIndentDao.updatePatientIndent(patientIndent);
			issue.setIsImplant(false);
			issue.setIsConsignment(false);
			Issue issueResponse = pendingIssuesDao.pendingIssuesSave(issue);
		}

		CommonResponse response = new CommonResponse();
		response.setMessage("Patient indent approved successfully");

		return response;

	}

	@Override
	@Transactional
	public Object cancelPatientIndent(PatientIndentRequest patientIndentRequest) {
		PatientIndent patientIndent = patientIndentDao.patientIndentById(patientIndentRequest.getId(),
				patientIndentRequest.getUnitId());

		for (PatientIndentDetail detail : patientIndent.getPatientIndentDetail()) {
			detail.setStatus(new Status(patientIndentRequest.getStatusId()));
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(patientIndent.getUnitId()));
			detail.setUpdatedByUser(patientIndentRequest.getUserId() != null ? patientIndentRequest.getUserId()
					: sessionService.getUserId());
		}

		patientIndent.setCancelReason(new CancelReason(patientIndentRequest.getCancelReasonId()));
		patientIndent.setCancelNote(patientIndentRequest.getCancelNote());

		patientIndent.setStatus(new Status(patientIndentRequest.getStatusId()));

		patientIndent.setUpdatedDate(TimeZoneComponent.getDateByzone(patientIndent.getUnitId()));
		patientIndent.setUpdatedByUser(patientIndentRequest.getUserId() != null ? patientIndentRequest.getUserId()
				: sessionService.getUserId());

		patientIndentDao.updatePatientIndent(patientIndent);

		CommonResponse response = new CommonResponse();
		response.setMessage("Patient indent cancelled successfully");

		return response;
	}

	@Override
	@Transactional
	public List<PatientIndentDetails> patientPreviousIndentDetails(PatientIndentRequest patientIndentRequest) {
 
		   List<PatientIndentDetails> patientIndentDetail1 = new ArrayList<>();
			List<PatientIndent> patientIndentList =patientIndentDao.patientPreviousIndent(patientIndentRequest);
			PatientIndentDetails patientIndentDetails = new PatientIndentDetails();

			Integer doctorId = 0;
			for (PatientIndent patientIndent : patientIndentList) {
			// doctor not found for emergency admission
			if (patientIndent.getDoctor() != null) {
				doctorId = patientIndent.getDoctor().getDoctorId();
			}
			if (patientIndent.getAdmission() != null) {
				patientIndentDetails = new PatientIndentDetails(patientIndent.getId(),
						patientIndent.getVisitType().getVisitTypeId(), patientIndent.getVisitType().getVisitTypeCode(),
						patientIndent.getStore().getStoreId(), patientIndent.getStore().getStoreName(),
						patientIndent.getToStore().getStoreId(), patientIndent.getToStore().getStoreName(),
						patientIndent.isConsignment(), patientIndent.getAdmission().getAdmissionNumber(),
						patientIndent.getPatient().getPatientId(),
						patientIndent.getPatient().getFirstName() + " " + patientIndent.getPatient().getMiddleName() + " "
								+ patientIndent.getPatient().getLastName(),
						patientIndent.getPatient().getUhidNumber(), doctorId);
				patientIndentDetails .setWardId(patientIndent.getWard() != null ? patientIndent.getWard().getWardId() : null);
				patientIndentDetails .setWardName(patientIndent.getWard() != null ? patientIndent.getWard().getWardName() : null);
				patientIndentDetails.setBedId(patientIndent.getBed() != null ? patientIndent.getBed().getBedId() : null);
				patientIndentDetails .setBedNo(patientIndent.getBed() != null ? patientIndent.getBed().getBedNumber() : null);
				patientIndentDetails.setAdmissionId(patientIndent.getAdmission() != null ? patientIndent.getAdmission().getAdmissionId() : null);
				patientIndentDetails.setRemark(patientIndent.getRemark());
			/*	patientIndentDetails.setDob(patientIndent.getAdmission().getPatient().getDob());
				patientIndentDetails.setGenderId(patientIndent.getPatient().getGender().getGenderId());
				patientIndentDetails.setImagePath(patientIndent.getPatient().getImagePath());*/
			/*	Date doa = null;
				Date pdd = null;
				AdmissionDetail admissionDetail = GlobalDao .getPatientWardBedDetails(patientIndent.getAdmission().getAdmissionId());
		*/		String doctorDeatil = "";
				if (patientIndent.getDoctor() != null) {
					doctorDeatil = patientIndent.getDoctor().getFirstName() + " "
							+ patientIndent.getDoctor().getMiddleName() + " " + patientIndent.getDoctor().getLastName();
				}

				/*if (admissionDetail != null) {
					doa = admissionDetail.getDoa();
					pdd = admissionDetail.getPdd();
				}
				patientIndentDetails.setDoa(doa != null ? doa : null);
				patientIndentDetails.setPdd(pdd != null ? pdd : null);*/
				patientIndentDetails.setDoctorDetail(doctorDeatil);

			} else if (patientIndent.getEncounter() != null) {
				patientIndentDetails = new PatientIndentDetails(patientIndent.getId(),
						patientIndent.getVisitType().getVisitTypeId(), patientIndent.getVisitType().getVisitTypeCode(),
						patientIndent.getStore().getStoreId(), patientIndent.getStore().getStoreName(),
						patientIndent.getToStore().getStoreId(), patientIndent.getToStore().getStoreName(),
						patientIndent.isConsignment(), patientIndent.getEncounter().getEncounterNumber(),
						patientIndent.getPatient().getPatientId(),
						patientIndent.getPatient().getFirstName() + " " + patientIndent.getPatient().getMiddleName() + " "
								+ patientIndent.getPatient().getLastName(),
						patientIndent.getPatient().getUhidNumber(),
						patientIndent.getDoctor() != null ? patientIndent.getDoctor().getDoctorId() : null);
				patientIndentDetails.setEncounterId(
						patientIndent.getEncounter() != null ? patientIndent.getEncounter().getEncounterId() : null);
				patientIndentDetails
						.setDoctorId(patientIndent.getDoctor() != null ? patientIndent.getDoctor().getDoctorId() : null);
			/*	patientIndentDetails.setRemark(patientIndent.getRemark());
				patientIndentDetails .setDob(patientIndent.getPatient() != null ? patientIndent.getPatient().getDob() : null);*/

				/*Date doa = null;
				Date pdd = null;*/
				String doctorDeatil = "";
				if (patientIndent.getDoctor() != null) {
					doctorDeatil = patientIndent.getDoctor().getFirstName() + " "
							+ patientIndent.getDoctor().getMiddleName() + " " + patientIndent.getDoctor().getLastName();
				}
				/*if (patientIndent.getAdmission() != null) {
					AdmissionDetail admissionDetail = GlobalDao
							.getPatientWardBedDetails(patientIndent.getAdmission().getAdmissionId());
					if (admissionDetail != null) {
						doa = admissionDetail.getDoa();
						pdd = admissionDetail.getPdd();
					}
					patientIndentDetails.setDoa(doa != null ? doa : null);
					patientIndentDetails.setPdd(pdd != null ? pdd : null);
				}*/

				patientIndentDetails.setDoctorDetail(doctorDeatil);
			}
			List<PatientIndentItem> items = new ArrayList<PatientIndentItem>();
			for (PatientIndentDetail patientIndentDetail : patientIndent.getPatientIndentDetail()) {
				PatientIndentItem item = new PatientIndentItem(patientIndentDetail.getId(),
						patientIndentDetail.getItem().getId(), patientIndentDetail.getItem().getCode(),
						patientIndentDetail.getItem().getName(),
						patientIndentDetail.getItem().getGeneric() != null
								? patientIndentDetail.getItem().getGeneric().getName()
								: null,
						patientIndentDetail.getUomType() != null ? patientIndentDetail.getUomType().getType() : null,
						patientIndentDetail.getUomUnit() != null ? patientIndentDetail.getUomUnit().getUnits() : null,
						patientIndentDetail.getIndentQuantity(), patientIndentDetail.getPendingQuantity(),
						patientIndentDetail.getItem().getGeneric() != null
								? patientIndentDetail.getItem().getGeneric().getId()
								: null, GlobalDao.getUnitOfMeasurementListByItem(patientIndentDetail.getItem().getId(), patientIndent.getOrganizationId()), 
						patientIndentDetail.getItem().getIsThreeUOMApplicable()); 
				items.add(item);
			}	
			patientIndentDetails.setItems(items);
			patientIndentDetail1.add(patientIndentDetails);
	}
		 
		
	
	
		return patientIndentDetail1;

	
	}
}