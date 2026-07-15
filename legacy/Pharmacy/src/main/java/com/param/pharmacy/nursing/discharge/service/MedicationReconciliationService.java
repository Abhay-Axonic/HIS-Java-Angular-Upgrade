package com.param.pharmacy.nursing.discharge.service;

import java.math.BigInteger;
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
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.UserNursingStationMapperDto;
import com.param.global.dto.global.Floor;
import com.param.global.service.IUserNursingStationMapperService;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.pharmacology.IPharmacologyValidationDao;
import com.param.pharmacy.nursing.discharge.dao.IMedicationReconciliationDao;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationDto;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationItem;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationResponse;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSave;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSaveItem;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchDto;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchRequest;
import com.param.service.global.SessionService;

@Service("MedicationReconciliationService")
public class MedicationReconciliationService implements IMedicationReconciliationService {

	private Integer screenId = Screen.DischargeMedicationReconciliation.getScreen();

	@Autowired
	private IMedicationReconciliationDao medicationReconciliationDao;

	@Autowired
	private IPharmacologyValidationDao pharmacologyValidationDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	private IUserNursingStationMapperService iUserNursingStationMapperService;

	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public MedicationReconciliationDto getDropdownList(Integer unitId) {

		MedicationReconciliationDto response = new MedicationReconciliationDto();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (WardMasters wardMaster : GlobalDao.getWardList(unitId)) {
			response.addWard(new com.param.global.dto.global.Ward(wardMaster.getWardId(), wardMaster.getWardName()));
		}

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		for (FloorMasters floor : GlobalDao.getFloorList(unitId)) {
			response.addFloor(new Floor(floor.getFloorId(), floor.getFloorName()));
		}

		UserNursingStationMapperDto userNursingStationMapperDto = new UserNursingStationMapperDto();
		userNursingStationMapperDto.setUserId(sessionService.getUserId());
		userNursingStationMapperDto.setUnitId(sessionService.getUnitId());
		userNursingStationMapperDto.setOrganisationId(sessionService.getOrganizationId());

		// List<UserNursingStationMapperDto> list =
		// iUserNursingStationMapperService.getUserNursingStationMapperlist(userNursingStationMapperDto).getListObject();
		List<UserNursingStationMapperDto> list = iUserNursingStationMapperService
				.getUserNursingStationMapperById(sessionService.getUserId(), sessionService.getOrganizationId(),
						sessionService.getUnitId())
				.getListObject();
		//System.out.println("Nursing Stations : " + list);
		response.setNursingStations(list);

		return response;
	}

	@Override
	@Transactional
	public List<MedicationReconciliationSearchDto> medicationReconciliationSearch(
			MedicationReconciliationSearchRequest request) {

		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		List<MedicationReconciliationSearchDto> medicationReconciliationSearchs = new ArrayList<MedicationReconciliationSearchDto>();
		for (PrescriptionDetail prescriptionDetail : medicationReconciliationDao.medicationReconciliationSearch(request,
				screenId)) {

			/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
					.doctorSearchByIdValue(prescriptionDetail.getPrescription().getDoctor().getDoctorId());*/
			/*String drSpeciality = "";
			if (doctorSpeciality.size() > 0) {
				drSpeciality = doctorSpeciality.get(0).getSpeciality().getSpecialityName();

			}*/
			String doctorDeatil="";
			if(prescriptionDetail.getPrescription().getSpecialityMaster()!=null && prescriptionDetail.getPrescription().getDoctor()!=null)
			{
				 
			
			  doctorDeatil = prescriptionDetail.getPrescription().getDoctor().getFirstName() + " "
						+ prescriptionDetail.getPrescription().getDoctor().getMiddleName() + " "
						+ prescriptionDetail.getPrescription().getDoctor().getLastName() + " | " + prescriptionDetail.getPrescription().getSpecialityMaster().getSpecialityName();
		
			}
			
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
				Integer sId = 2;
				String rStatus = "Pending";
				if (prescriptionDetail.getPrescription().getReconciliationStatus() != null) {
					sId = prescriptionDetail.getPrescription().getReconciliationStatus().getId();
				}
				if (prescriptionDetail.getPrescription().getReconciliationStatus() != null) {
					rStatus = prescriptionDetail.getPrescription().getReconciliationStatus().getStatus();
				}

				MedicationReconciliationSearchDto medicationReconciliationSearchDto = new MedicationReconciliationSearchDto(
						prescriptionDetail.getPrescription().getId(),
						prescriptionDetail.getPrescription().getAdmission().getPatient().getUhidNumber(), patientDetail,
						doctorDeatil, rStatus, sId);
				medicationReconciliationSearchDto.setWardName(!wardName.equals("") ? wardName : null);
				medicationReconciliationSearchDto.setPatientId(prescriptionDetail.getPrescription().getAdmission().getPatient().getPatientId());
				medicationReconciliationSearchDto.setBedNo(!bedNumber.equals("") ? bedNumber : null);
				medicationReconciliationSearchs.add(medicationReconciliationSearchDto);
			}
		}
		return medicationReconciliationSearchs;
	}

	@Override
	@Transactional
	public CommonResponse medicationReconciliationSave(MedicationReconciliationSave request) {
		Prescription prescription = pharmacologyValidationDao.getPrescriptionById(request.getId());
		// prescription.updatePrescription(new Status(request.getStatusId()),true);
		prescription.updatePrescription(new Status(4), true);
		for (PrescriptionDetail prescriptionDeatil : prescription.getPrescriptionDetailsList()) {
			for (MedicationReconciliationSaveItem items : request.getItems()) {
				if (items.getId().equals(prescriptionDeatil.getId())) {
					prescriptionDeatil.updatePrescriptionDetail(items.getQuantityToOrdered());
					/*
					 * prescription.addMedicationReconsiliationDetailList(new
					 * PrescriptionDetail(items.getId(), items.getQuantityToOrdered()));
					 */
				}
			}
		}
		medicationReconciliationDao.medicationReconciliationSave(prescription);
		CommonResponse response = new CommonResponse();
		response.setId(prescription.getId().longValue());
		response.setMessage("Medication Reconciliation saved successfully");
		return response;
	}

	@Override
	@Transactional
	public MedicationReconciliationResponse medicationReconciliationDetails(Long id,Integer unitId) {
		Prescription prescription = pharmacologyValidationDao.getPrescriptionById(id);
		MedicationReconciliationResponse medicationReconciliationResponse = new MedicationReconciliationResponse();
		/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
				.doctorSearchByIdValue(prescription.getDoctor().getDoctorId());*/
		String doctorDeatil="";
		if(prescription.getDoctor()!=null && prescription.getSpecialityMaster()!=null) {
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

			medicationReconciliationResponse = new MedicationReconciliationResponse(prescription.getId(),
					prescription.getAdmission().getPatient().getUhidNumber(),
					prescription.getAdmission().getPatient().getPatientId(), patientDetail,
					prescription.getAdmission().getPatient().getDob(), doctorDeatil!=""? prescription.getDoctor().getDoctorId():null,
					doctorDeatil);
			medicationReconciliationResponse.setDoa(doa != null ? doa : null);
			medicationReconciliationResponse.setPdd(pdd != null ? pdd : null);
			medicationReconciliationResponse.setWardName(!wardName.equals("") ? wardName : null);
			medicationReconciliationResponse.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			medicationReconciliationResponse.setWardId(wardId != null ? wardId : null);
			medicationReconciliationResponse.setBedId(bedId != null ? bedId : null);
			medicationReconciliationResponse.setImagePath(prescription.getAdmission().getPatient().getImagePath());
			medicationReconciliationResponse.setGenderId(prescription.getAdmission().getPatient().getGender().getGenderId());

		}
		
		Integer sId = 2;
		String rStatus = "Pending";
		if (prescription.getReconciliationStatus() != null) {
			sId = prescription.getReconciliationStatus().getId();
		}
		if (prescription.getReconciliationStatus() != null) {
			rStatus = prescription.getReconciliationStatus().getStatus();
		}
		medicationReconciliationResponse.setStatusId(sId);
		
		List<MedicationReconciliationItem> items = new ArrayList<MedicationReconciliationItem>();
		for (PrescriptionDetail prescriptionDetail : pharmacologyValidationDao.pharmacologyValidationDetails(id)) {
			for (PrescriptionFrequencyDetail prescriptionFrequencyDetail : prescriptionDetail
					.getPrescriptionFrequencyDetails()) {
				Integer patientId = prescription.getAdmission().getPatient().getPatientId();
				// Integer doctorId
				BigInteger totalIssuedQty = GlobalDao.getTotalIssuedQty(patientId,
						prescriptionDetail.getItem().getId());
				
				// discusssed with sharad
//				if (totalIssuedQty != null) {

					BigInteger consumedQty = GlobalDao.getConsumedQty(patientId, prescriptionDetail.getItem().getId(), 0L, 0);
					Integer returnQty = GlobalDao.getReturnQty(prescription.getAdmission().getAdmissionId(),
							prescriptionDetail.getId());

					MedicationReconciliationItem item = new MedicationReconciliationItem(prescriptionDetail.getId(),
							prescriptionDetail.getItem().getId(), prescriptionDetail.getItem().getName(),
							prescriptionDetail.getItem().getGeneric().getId(),
							prescriptionDetail.getItem().getGeneric().getName(),
							prescriptionFrequencyDetail.getRouteOfAdministration()!=null ? prescriptionFrequencyDetail.getRouteOfAdministration().getDescription():null,
							prescriptionFrequencyDetail.getDose(),
							prescriptionFrequencyDetail.getFrequency().getDescription(),
							prescriptionFrequencyDetail.getStartDate(), prescriptionFrequencyDetail.getStopDate(),
							prescriptionDetail.getOrderQty(),
							GlobalDao.getStoreStockByItem(prescriptionDetail.getStore().getStoreId(),
									prescriptionDetail.getItem().getId(),unitId),
							prescriptionDetail.getRevisedOrderedQuantity(), totalIssuedQty, consumedQty, returnQty, prescriptionDetail.getItem().getStrengthUnit().getUnits());
					items.add(item);

//				}

			}
		}
		medicationReconciliationResponse.setItems(items);
		return medicationReconciliationResponse;
	}

}
