package com.param.pharmacy.op.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.global.common.Response;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyUpdate;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyUpdateItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.master.global.dao.equipment.IDepartmentStoreMapperDao;
import com.param.pharmacy.master.global.dto.equipment.DepartmentStoreMapperDto;
import com.param.pharmacy.master.unit.dao.applicationConfiguration.IApplicationConfigurationDao;
import com.param.pharmacy.op.dao.IPharmacologyValidationOPDao;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service
public class IPharmacologyValidationServiceOPImpl implements IPharmacologyValidationServiceOP {
	
	@Autowired
	private IGlobalDao GlobalDao;
	
	@Autowired
	IPharmacologyValidationOPDao pharmacologyValidationOPDao;
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	IEmrService EmrService;
	
	@Autowired
	IApplicationConfigurationDao iApplicationConfigurationDao;
	
	@Autowired
	private IDepartmentStoreMapperDao iDepartmentStoreMapperDao;
	
	private Integer screenId = Screen.PharmacologyValidation.getScreen();
	

	@Override
	@Transactional
	public CommonDropdown getDropdownList() {
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}
		
		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}
		
		return response;
	}

	@Override
	@Transactional
	public List<PharmacologyValidationSearchResult> pharmacologyValidationSearch(
			PharmacologyValidationSearch pharmacologyValidationSearch) {
		List<PharmacologyValidationSearchResult> pharmacologyValidationSearchList = new ArrayList<PharmacologyValidationSearchResult>();
		pharmacologyValidationSearch.setToDate((new DateTime(pharmacologyValidationSearch.getToDate())).plusDays(1).toDate());
		List<Prescription> prescriptions = null;
		
		
		ApplicationConfiguration applicationConfiguration = iApplicationConfigurationDao.getApplicationConfigurationByUnitId(pharmacologyValidationSearch.getUnitId());
		if(applicationConfiguration != null) {
			if(!applicationConfiguration.getIsPharmValidationOPRequired())
				return pharmacologyValidationSearchList;
			else
			{
				if(applicationConfiguration.getStoreId().equals(pharmacologyValidationSearch.getStoreId())) 
					prescriptions = pharmacologyValidationOPDao.pharmacologyValidationSearchOP(pharmacologyValidationSearch, screenId);
				else {
					Response resp = iDepartmentStoreMapperDao.getDeptStoremappings(pharmacologyValidationSearch.getUnitId(), pharmacologyValidationSearch.getStoreId());
					List<DepartmentStoreMapperDto> deptList = resp.getListObject();
					if(deptList == null || deptList.isEmpty())
						return pharmacologyValidationSearchList;
					else {
						List<Integer> deptIds = deptList.stream().map(x -> x.getDeptId()).collect(Collectors.toList());
						prescriptions = pharmacologyValidationOPDao.pharmacologyValidationSearchOP(pharmacologyValidationSearch, screenId, deptIds);
					}
				}
			}
		}
		
		for(Prescription prescription : prescriptions)
		{
			if(prescription.getIsValidate() == false)
			{
				if(prescription.getEncounter() != null)
				{
					String doctorDetail ="";
					if(prescription.getDoctor() != null && prescription.getSpecialityMaster()!=null)
					{
						doctorDetail = prescription.getDoctor().getFirstName() + " " + prescription.getDoctor().getMiddleName() + " " +
								prescription.getDoctor().getLastName() + " | " + prescription.getSpecialityMaster().getSpecialityName();
					}
						
					Integer age = Util.getAgeByBirthDate(prescription.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = prescription.getEncounter().getPatient().getPrefix().getPrefixCode() + " " + prescription.getEncounter().getPatient().getFirstName() + " " + prescription.getEncounter().getPatient().getMiddleName() + " " +
						prescription.getEncounter().getPatient().getLastName() + " | " + age + " | " + prescription.getEncounter().getPatient().getGender().getGenderCode();
					
					String patientName = prescription.getEncounter().getPatient().getPrefix().getPrefixCode() + " " + prescription.getEncounter().getPatient().getFirstName() + " " + prescription.getEncounter().getPatient().getMiddleName() + " " +
							prescription.getEncounter().getPatient().getLastName();
						
					PharmacologyValidationSearchResult pharmacologyValidation = new PharmacologyValidationSearchResult(prescription.getId(),
							prescription.getVisitType().getVisitTypeName(), prescription.getEncounter().getCreatedDate(), 
							prescription.getPrescriptionDate(), prescription.getEncounter().getPatient().getUhidNumber(), patientDetail, doctorDetail, 
							prescription.getPriority().getPriorityDesc(),
							prescription.getStatus().getStatus(), patientName, age);
					
					pharmacologyValidation.setStatusId(prescription.getStatus().getId());
					pharmacologyValidation.setEncounterNumber(prescription.getEncounter().getEncounterNumber());
					pharmacologyValidation.setPrescriptionNo(prescription.getPrescriptionNo());
					pharmacologyValidation.setImagePath(prescription.getEncounter().getPatient().getImagePath());
					pharmacologyValidation.setGenderCode(prescription.getEncounter().getPatient().getGender().getGenderCode());
					pharmacologyValidation.setGenderId(prescription.getEncounter().getPatient().getGender().getGenderId());
					pharmacologyValidation.setPrescriptionNo(prescription.getPrescriptionNo());
					
					pharmacologyValidationSearchList.add(pharmacologyValidation);
				}
				else if(prescription.getTempPatient() != null)
				{
					
					String patientDetail = prescription.getTempPatient().getPrefix().getPrefixCode() + " " + prescription.getTempPatient().getPatientName() + " " +
							  " | " + prescription.getTempPatient().getAge() + " | " + prescription.getTempPatient().getGender().getGenderCode();
					
					PharmacologyValidationSearchResult pharmacologyValidation = new PharmacologyValidationSearchResult(
							prescription.getId(),
							prescription.getVisitType().getVisitTypeName(), 
							prescription.getPrescriptionDate(), 
							prescription.getPrescriptionDate(), 
							prescription.getTempPatient().getTempUhid(), 
							patientDetail, 
							prescription.getTempDoctor() != null ? prescription.getTempDoctor().getDoctorName() : "", 
							prescription.getPriority().getPriorityDesc(),
							prescription.getStatus().getStatus());
					
					pharmacologyValidation.setStatusId(prescription.getStatus().getId());
					pharmacologyValidation.setEncounterNumber("WALK-IN");
					pharmacologyValidation.setGenderCode(prescription.getTempPatient().getGender().getGenderCode());
					pharmacologyValidation.setGenderId(prescription.getTempPatient().getGender().getGenderId());
					pharmacologyValidationSearchList.add(pharmacologyValidation);
				}
			}
			
		}
		return pharmacologyValidationSearchList;
	}
	
	@Override
	@Transactional
	public PharmacologyValidationDetails pharmacologyValidationDetails(Long id) {
		PharmacologyValidationDetails pharmacologyValidationDetails = new PharmacologyValidationDetails();
		List<PharmacologyItem> pharmacologyItemList = new ArrayList<PharmacologyItem>();
		Prescription prescription = pharmacologyValidationOPDao.getPrescriptionById(id);
		
		if(prescription.getEncounter() != null)
		{
			String doctorDetail = "";
			if(prescription.getDoctor() != null && prescription.getSpecialityMaster()!=null)
			{
				doctorDetail = prescription.getDoctor().getFirstName() + " | " + prescription.getDoctor().getMiddleName() + " " +
						prescription.getDoctor().getLastName() + " | " + prescription.getSpecialityMaster().getSpecialityName();
			}
				
			Integer age = Util.getAgeByBirthDate(prescription.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = prescription.getEncounter().getPatient().getPrefix().getPrefixCode() + " " + prescription.getEncounter().getPatient().getFirstName() + " " + prescription.getEncounter().getPatient().getMiddleName() + " " +
				prescription.getEncounter().getPatient().getLastName() + " | " + age + " | " + prescription.getEncounter().getPatient().getGender().getGenderCode();
	
			pharmacologyValidationDetails = new PharmacologyValidationDetails(prescription.getId(),
			prescription.getEncounter().getPatient().getPatientId(), patientDetail, prescription.getEncounter().getPatient().getDob(),
			prescription.getDoctor().getDoctorId(), doctorDetail, TimeZoneComponent.getDateByzone(prescription.getUnitId()), TimeZoneComponent.getDateByzone(prescription.getUnitId()),prescription.getEncounter().getPatient().getUhidNumber());
			
			pharmacologyValidationDetails.setEncounterId(prescription.getEncounter().getEncounterId());
			pharmacologyValidationDetails.setVisitType(prescription.getVisitType().getVisitTypeId());
			pharmacologyValidationDetails.setStatusId(prescription.getStatus().getId());
			pharmacologyValidationDetails.setStatus(prescription.getStatus().getStatus());
			pharmacologyValidationDetails.setPrescriptionNo(prescription.getPrescriptionNo());
			for(PrescriptionDetail prescriptionDetail : pharmacologyValidationOPDao.pharmacologyValidationDetails(id)){
				for(PrescriptionFrequencyDetail prescriptionFrequencyDetail : pharmacologyValidationOPDao.getPrescriptionFrequencyDetails(prescriptionDetail.getId())){
					PharmacologyItem pharmacologyItem = new PharmacologyItem(prescriptionDetail.getId(), prescriptionDetail.getItem().getId(), prescriptionDetail.getItem().getName(),
							prescriptionDetail.getItem().getGeneric().getName(),
							prescriptionFrequencyDetail.getFrequency().getDescription(),
							prescriptionFrequencyDetail.getDose(), 
							prescriptionFrequencyDetail.getStartDate(),prescriptionFrequencyDetail.getStopDate(),
							prescriptionDetail.getPrescription().getIsIntervention());
					pharmacologyItem.setInterventionRemark(prescriptionDetail.getRemark());
					pharmacologyItem.setStatusId(prescriptionDetail.getStatus().getId());
					pharmacologyItem.setStatus(prescriptionDetail.getStatus().getStatus());
					pharmacologyItemList.add(pharmacologyItem);
				}
			}
		}
		else if(prescription.getTempPatient() != null){
			String patientDetail = prescription.getTempPatient().getPrefix().getPrefixCode() + " " + prescription.getTempPatient().getPatientName() + " " +
					  " | " + prescription.getTempPatient().getAge() + " | " + prescription.getTempPatient().getGender().getGenderCode();
			
			pharmacologyValidationDetails = new PharmacologyValidationDetails(prescription.getId(),
					prescription.getTempPatient().getPatientId(), 
					patientDetail, prescription.getTempDoctor() != null ? prescription.getTempDoctor().getDoctorName() : null, TimeZoneComponent.getDateByzone(prescription.getUnitId()), TimeZoneComponent.getDateByzone(prescription.getUnitId()));
			
			pharmacologyValidationDetails.setStatusId(prescription.getStatus().getId());
			pharmacologyValidationDetails.setStatus(prescription.getStatus().getStatus());
			
			for(PrescriptionDetail prescriptionDetail : pharmacologyValidationOPDao.pharmacologyValidationDetails(id)){
				for(PrescriptionFrequencyDetail prescriptionFrequencyDetail : pharmacologyValidationOPDao.getPrescriptionFrequencyDetails(prescriptionDetail.getId())){
					PharmacologyItem pharmacologyItem = new PharmacologyItem(prescriptionDetail.getId(), prescriptionDetail.getItem().getId(), prescriptionDetail.getItem().getName(),
							prescriptionDetail.getItem().getGeneric().getName(),
							prescriptionFrequencyDetail.getFrequency().getDescription(),
							prescriptionFrequencyDetail.getDose(), 
							prescriptionFrequencyDetail.getStartDate(),prescriptionFrequencyDetail.getStopDate(),
							prescriptionDetail.getPrescription().getIsIntervention());
					pharmacologyItem.setInterventionRemark(prescriptionDetail.getRemark());
					pharmacologyItem.setStatusId(prescriptionDetail.getStatus().getId());
					pharmacologyItem.setStatus(prescriptionDetail.getStatus().getStatus());
					pharmacologyItemList.add(pharmacologyItem);
				}
			}
		}
		
		pharmacologyValidationDetails.setItems(pharmacologyItemList);
		return pharmacologyValidationDetails;
	}

	@Override
	@Transactional
	public Object pharmacologyValidationSave(PharmacologyUpdate pharmacologyUpdate) {
		Prescription prescription = pharmacologyValidationOPDao.getPrescriptionById(pharmacologyUpdate.getId());
		
		for (PrescriptionDetail prescriptionDeatil : prescription.getPrescriptionDetailsList()){
			for(PharmacologyUpdateItem pharmacologyItem : pharmacologyUpdate.getItems()){
				if(pharmacologyItem.getId().equals(prescriptionDeatil.getId())){
					prescriptionDeatil.setIsIntervention(pharmacologyItem.isIntervention());
					prescriptionDeatil.setStatus(new Status(pharmacologyItem.getItemStatusId()));
					prescriptionDeatil.setRemark(pharmacologyItem.getRemark());
					prescriptionDeatil.setUpdatedDate(TimeZoneComponent.getDateByzone(prescription.getUnitId()));
					prescriptionDeatil.setUpdatedByUser(pharmacologyUpdate.getUserId()!=null ? pharmacologyUpdate.getUserId() : sessionService.getUserId());
				}
			}
		}
		
		prescription.setIsValidate(pharmacologyUpdate.isValidate());
		prescription.setStatus(new Status(pharmacologyUpdate.getStatusId()));
		prescription.setUpdatedDate(TimeZoneComponent.getDateByzone(prescription.getUnitId()));
		prescription.setUpdatedByUser(pharmacologyUpdate.getUserId()!=null ? pharmacologyUpdate.getUserId() : sessionService.getUserId());
		pharmacologyValidationOPDao.updatePrescription(prescription);
		
		CommonResponse response = new CommonResponse();
		response.setId((prescription.getId().longValue()));
		response.setMessage("Pharmacology Validation saved successfully");
		
		return response;
	}

	

}
