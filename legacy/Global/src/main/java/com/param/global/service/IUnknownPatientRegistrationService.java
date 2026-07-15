package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.PatientSearchDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IUnknownPatientRegistrationService {

	@Transactional
	Response getPriorityMasterList() throws ApplicationException;

	@Transactional
	Response saveUnknownPatient(AdmissionDto unAdmissionDto)throws ApplicationException;
			
	@Transactional
	Response saveERPatientAdmisson(AdmissionDto unAdmissionDto) throws ApplicationException;

	@Transactional
	Response saveERsavePatientDetails(AdmissionDto unAdmissionDto)throws ApplicationException;

	@Transactional
	Response patientSearchByMultipleCriteria(PatientSearchDto patientSearchDto)throws ApplicationException;

	@Transactional
	Response saveMedicoLegalDetails(AdmissionDto unAdmissionDto)throws ApplicationException;
	
	String getAdmissionNumber() throws ApplicationException;
	
	@Transactional
//	Response getListOfEncounterIdByPatientId(Integer patientId,  Integer organizationId, Integer unitId, String fromDate, String toDate) throws ApplicationException;
	Response getListOfEncounterIdByPatientId(PatientSearchDto patientSearchDto)throws ApplicationException;

	@Transactional
	Response getListOfDocumentForBookView(PatientSearchDto patientSearchDto) throws ApplicationException;

	@Transactional
	Response searchListOfPatientsByWardId(PatientSearchDto patientSearchDto)throws ApplicationException;

	@Transactional
	Response patientSearchByMultipleCriteriaForIPD(PatientSearchDto patientSearchDto) throws ApplicationException;
	
	@Transactional
	Response patientSearchByForTransferICU(PatientSearchDto patientSearchDto)throws ApplicationException;

	@Transactional
	Response patientSearchByMultipleCriteriaForOPBilling(PatientSearchDto patientSearchDto) throws ApplicationException;

	@Transactional
	Response getMedicolegalDetailsForIcon(AdmissionDto admissionDto) throws ApplicationException;

	@Transactional
	Response updateMedicolegalDetails(AdmissionDto admissionDto) throws ApplicationException;

	@Transactional
	Response getPatientListMedicolegalDetailsMLCWorklist(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response getMedicolegalDetailsMLCWorklistCount(AdmissionDto admissionDto)throws ApplicationException;

	@Transactional
	Response searchListOfPatientsByDoctorIdForIOChart(PatientSearchDto patientSearchDto)throws ApplicationException;

	@Transactional
	Response patientSearchByMultipleCriteriaForWalkInSales(PatientSearchDto patientSearchDto)throws ApplicationException;

	@Transactional
	Response patientSearchByMultipleCriteriaWithDeathPatient(PatientSearchDto patientSearchDto)throws ApplicationException;

	Response patientSearchByMultipleCriteriaGlobal(PatientSearchDto patientSearchDto);

	Response getIpRenderedDateChange(PatientSearchDto patientSearchDto);

	@Transactional
	Response patientSearchByMultipleCriteriaForICUAndER(PatientSearchDto patientSearchDto) throws ApplicationException;

	Response patientSearchByMultipleCriteriaNabh(PatientSearchDto patientSearchDto);

	@Transactional
	Response patientSearchForIPDNutritionalAssessment(PatientSearchDto patientSearchDto) throws ApplicationException;

	@Transactional
	Response patientSearchByICUAndER(PatientSearchDto patientSearchDto) throws ApplicationException;

	@Transactional
    Response patientSearchByMultipleCriteriaGrowthChart(PatientSearchDto patientSearchDto)throws ApplicationException;

	@Transactional
	public void invalidatePriorityMasterCache() throws ApplicationException;

    @Transactional
    public Response getAppointmentsByPatientId(PatientSearchDto patientSearchDto) throws ApplicationException;

	@Transactional
	Response updateMLCFLag(AdmissionDto admissionDto);
}
