package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.PatientSearchDto;
import com.param.global.model.UnknownPatientRegistration;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IUnknownPatientRegistrationDao extends IGenericDao<UnknownPatientRegistration, Integer>
{

	
	Response saveUnknownPatient(AdmissionDto unAdmissionDto) throws ApplicationException;

	Response getPriorityMasterList(Integer orgId, Integer unitId) throws ApplicationException;

	Response saveERPatientAdmission(AdmissionDto unAdmissionDto)throws ApplicationException;

	Response saveERPatientDetails(AdmissionDto unAdmissionDto)throws ApplicationException;

	Response patientSearchByMultipleCriteria(PatientSearchDto patientSearchDto)throws ApplicationException;

	Response saveMedicoLegalDetails(AdmissionDto unAdmissionDto)throws ApplicationException;
	
	Long getNextValAdmissionId()throws ApplicationException;
	
//	Response getListOfEncounterIdByPatientId(Integer patientId, Integer organizationId, Integer unitId, String fromDate, String toDate) throws ApplicationException;

	Response getListOfDocumentForBookView(PatientSearchDto patientSearchDto) throws ApplicationException;

	Response searchListOfPatientsByWardId(PatientSearchDto patientSearchDto)throws ApplicationException;

	Response patientSearchByMultipleCriteriaForIPD(PatientSearchDto patientSearchDto) throws ApplicationException;

	Response patientSearchByForTransferICU(PatientSearchDto patientSearchDto)throws ApplicationException;

	Response patientSearchByMultipleCriteriaForOPBilling(PatientSearchDto patientSearchDto) throws ApplicationException;

	Response getListOfEncounterIdByPatientId(PatientSearchDto patientSearchDto) throws ApplicationException;

	Response getMedicolegalDetailsForIcon(AdmissionDto admissionDto) throws ApplicationException;

	Response getPatientListMedicolegalDetailsMLCWorklist(AdmissionDto admissionDto) throws ApplicationException;

	Response getMedicolegalDetailsMLCWorklistCount(AdmissionDto admissionDto)throws ApplicationException;

	Response searchListOfPatientsByDoctorIdForIOChart(PatientSearchDto patientSearchDto)throws ApplicationException;

	Response patientSearchByMultipleCriteriaForWalkInSales(PatientSearchDto patientSearchDto)throws ApplicationException;

	Response patientSearchByMultipleCriteriaWithDeathPatient(PatientSearchDto patientSearchDto)throws ApplicationException;

	Response patientSearchByMultipleCriteriaGlobal(PatientSearchDto patientSearchDto);

	Response getIpRenderedDateChange(PatientSearchDto patientSearchDto);

	Response patientSearchByMultipleCriteriaForICUAndER(PatientSearchDto patientSearchDto) throws ApplicationException;

	Response patientSearchByMultipleCriteriaNabh(PatientSearchDto patientSearchDto);

	Response patientSearchForIPDNutritionalAssessment(PatientSearchDto patientSearchDto) throws ApplicationException;

	Response patientSearchByICUAndER(PatientSearchDto patientSearchDto) throws ApplicationException;

    Response patientSearchByMultipleCriteriaGrowthChart(PatientSearchDto patientSearchDto)throws ApplicationException;

    Response getAppointmentsByPatientId(PatientSearchDto patientSearchDto) throws ApplicationException;

    Response updateMLCFLag(AdmissionDto admissionDto) throws ApplicationException;

	Integer getMLCcountForAdmission(AdmissionDto admissionDto) throws ApplicationException;
}
