package com.param.adt.admission.service;

import javax.transaction.Transactional;

import com.param.adt.common.Response;
import com.param.adt.master.unit.dto.WardMasterDto;
import com.param.global.dto.AdmissionDto;

import in.param.exception.ApplicationException;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface IAdmittedPatientListService 
{
	
	@Transactional
	Response getAdmittedPatientList(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response getActiveWardListByFloorId(WardMasterDto wardMasterDto)throws ApplicationException;

	@Transactional
	Response getKinDetailsByAdmissionId(AdmissionDto admissionDto)throws ApplicationException;
	
	@Transactional
	Response getAdmittedPatientByFloorWard(AdmissionDto admissionDto) throws ApplicationException;

	@Transactional
	Response serachPatientForMapOfBed(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response getIpPatientListByDocId(AdmissionDto admissionDto)throws ApplicationException;
	
	@Transactional
	Response ipPatientListByDocIdForCancelDischarge(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response getAdmittedPatientListForEMR(AdmissionDto admissionDto)throws ApplicationException;
	
	@Transactional
	Response getAdmittedPatientListForIPDNursingWorklist(AdmissionDto admissionDto)throws ApplicationException;
	
	@Transactional
	Response getListOfERPatients(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response getListOfDayCarePatientsForNursing(AdmissionDto admissionDto) throws ApplicationException;
	
	@Transactional
	Response getListOfDayCareNursingAccepted(AdmissionDto admissionDto)throws ApplicationException;
	
	@Transactional
	Response getHemodialysisByDoctorList(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response getAdmittedPatientListForIPDNursingWorklistForNursingPage(AdmissionDto admissionDto)throws ApplicationException;
	
	@Transactional
	Response getAdmittedPatientListForIPDNursingWorklistForNursingPageCount(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response getLoginDoctorSpecialityId(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response getAdmittedPatientListPagination(AdmissionDto admissionDto)throws ApplicationException;
	
	@Transactional
	Response getAdmittedPatientListPaginationCount(AdmissionDto admissionDto);

	@Transactional
	Response admittedPatientListUniversalSearch(AdmissionDto admissionDto) throws ApplicationException;

	@Transactional
	Response getAdmittedPatientListForVitalUploader(AdmissionDto admissionDto);

	@Transactional
	Response getIpPatientListForDoctorApp(AdmissionDto admissionDto)throws ApplicationException;
	
	@Transactional
	Response getAdmittedPatientListForNewBorn(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response editAdmissionDetailsGetById(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response admissionCancellation(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response getPatientDetailsForWorklistForWebPage(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response getAdmittedPatientListForDietOrder(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response admittedPatientListUniversalSearchForDietOrder(AdmissionDto admissionDto);

	@Transactional
	Response ipPatientListByDocIdBySearch(AdmissionDto admissionDto);

	@Transactional
	Response savePatientOrdersWorkbench(List<AdmissionDto> admissionDto)throws ApplicationException;

	@Transactional
	Response saveTriageCurrentDetails(AdmissionDto admissionDto) throws ApplicationException;
}
