package com.param.adt.admission.dao;

import javax.transaction.Transactional;

import com.param.adt.admission.model.Admission;
import com.param.adt.common.Response;
import com.param.adt.master.unit.dto.WardMasterDto;
import com.param.global.dto.AdmissionDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAdmittedPatientListDao extends IGenericDao<Admission, Integer>
{
	Response getAdmittedPatientList(AdmissionDto admissionDto) throws ApplicationException;

	Response getActiveWardListByFloorId(WardMasterDto wardMasterDto) throws ApplicationException;

	Response getKinDetailsByAdmissionId(AdmissionDto admissionDto)throws ApplicationException;

	Response getAdmittedPatientByFloorWard(AdmissionDto admissionDto) throws ApplicationException;

	Response serachPatientForMapOfBed(AdmissionDto admissionDto)throws ApplicationException;

	Response getIpPatientListByDocId(AdmissionDto admissionDto)throws ApplicationException;

	Response getAdmittedPatientListForEMR(AdmissionDto admissionDto)throws ApplicationException;

	Response getAdmittedPatientListForIPDNursingWorklist(AdmissionDto admissionDto)throws ApplicationException;
	
	Response getListOfERPatients(AdmissionDto admissionDto)throws ApplicationException;

	Response getListOfDayCarePatientsForNursing(AdmissionDto admissionDto) throws ApplicationException;

	Response getListOfDayCareNursingAccepted(AdmissionDto admissionDto) throws ApplicationException;
	
	Response ipPatientListByDocIdForCancelDischarge(AdmissionDto admissionDto)throws ApplicationException;

	Response getHemodialysisByDoctorList(AdmissionDto admissionDto)throws ApplicationException;

	Response getAdmittedPatientListForIPDNursingWorklistForNursingPage(AdmissionDto admissionDto) throws ApplicationException;

	Response getAdmittedPatientListForIPDNursingWorklistForNursingPageCount(AdmissionDto admissionDto)throws ApplicationException;

	Response getLoginDoctorSpecialityId(AdmissionDto admissionDto)throws ApplicationException;

	Response getAdmittedPatientListPagination(AdmissionDto admissionDto)throws ApplicationException;

	Response getAdmittedPatientListPaginationCount(AdmissionDto admissionDto)throws ApplicationException;

	Response admittedPatientListUniversalSearch(AdmissionDto admissionDto) throws ApplicationException;

	Response getAdmittedPatientListForVitalUploader(AdmissionDto admissionDto) throws ApplicationException;

	Response getIpPatientListForDoctorApp(AdmissionDto admissionDto)throws ApplicationException;

	Response getAdmittedPatientListForNewBorn(AdmissionDto admissionDto)throws ApplicationException;
	
	Response editAdmissionDetailsGetById(AdmissionDto admissionDto)throws ApplicationException;

	Response admissionCancellation(AdmissionDto admissionDto)throws ApplicationException;

	Response getPatientDetailsForWorklistForWebPage(AdmissionDto admissionDto)throws ApplicationException;

	Response getAdmittedPatientListForDietOrder(AdmissionDto admissionDto)throws ApplicationException;

	Response admittedPatientListUniversalSearchForDietOrder(AdmissionDto admissionDto);


	Response ipPatientListByDocIdBySearch(AdmissionDto admissionDto)throws ApplicationException;
}
