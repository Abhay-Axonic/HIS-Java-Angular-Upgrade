package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.dto.sync.PatientMasterDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.Response;
import com.param.global.dto.CustomPatientDetailsDto;
import com.param.global.dto.FingerPrintSearchDto;
import com.param.global.dto.NewONPRegistrationDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.PatientVisitsDto;
import com.param.global.dto.SlotMasterDto;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPatientRegistrationService {
	public Response getPatientDetailsById(Integer patientId) throws ApplicationException;

	public Response savePatientRegistration(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;
	
	public Response registerPatient(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response getPatientDetais(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response patientSearch(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response updatepatientRegistration(PatientRegistrationDto patientRegistrationDto)throws ApplicationException, Exception;
	
	public Response patientDetailsByIdForBilling(int patientId)throws ApplicationException;

	public Response convertPatient(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;
	
	public Response savePatientImage(int orgId,int unitId,int patientId,MultipartFile inputFile)throws ApplicationException;

	public Response getPatientDetaisForHealthCard(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response patientAutofillSearch(SlotMasterDto slotMasterDto)throws ApplicationException;

	public Response patientTypeHeadSearchForSelfRegistration(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	public Response savePatientFingerPrint(PatientRegistrationDto dto)throws ApplicationException;
	
	public Response searchPatientByFingerPrint(FingerPrintSearchDto dto)throws ApplicationException;
	
	@Transactional
	public Response PatientDemography(int patientId)throws ApplicationException;

	public Response updatePatientAsAbscound(PatientRegistrationDto patientRegistrationDto);

	public Response checkEmailDetails(Integer orgId, String emailId)throws ApplicationException;
	
	public void saveBabyRegistration(PatientRegistrationDto patientRegistrationDto) throws ApplicationException;

	Response checkUniquePatient(PatientRegistrationDto patientRegistrationDto);

	public Response patientDetailsByIdForCyclic(int patientId)throws ApplicationException;

	Response patientAutofillSearchPlain(SlotMasterDto slotMasterDto) throws ApplicationException;

	public Response getAllPatientVisits(PatientVisitsDto patientVisitsDto)throws ApplicationException;

	public Response getAllPastVisitsDataForInfo(PatientVisitsDto patientVisitsDto)throws ApplicationException;

	public Response deletePatientImage(int patientId, int organizationId)throws ApplicationException;

	public Response changeStatus(PatientRegistrationDto patientRegistrationDto);

	public String getUUIDByPatientId(Integer patientId);

	public Response getPatientDocuments(EncounterDocDetailsDto encounterDocDetailsDto)throws ApplicationException;

	public Response deletePatientDocument(EncounterDocDetailsDto encounterDocDetailsDto)throws ApplicationException;

	public Response patientSearchPatientList(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response getPatientDetailsByUhid(PatientRegistrationDto dto)throws ApplicationException;

	public Response saveCustomPatientDetails(CustomPatientDetailsDto customPatientDetailsObj)throws ApplicationException;

	public Response getPatientInfoForHospitalVisit(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response updateCustomDetails(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response getPatientDocumentDetailList(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response sendPatientSmsFromCoversheet(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response patientTypeHeadSearchForAppointmentUnique(SlotMasterDto slotMasterDto)throws ApplicationException;

	public Response getPatientDetailsListForNewEMR(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response savePatientRemarksForNewEMR(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response checkSqlConnection(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response checkSqlConnectionForUpdate(PatientRegistrationDto patientRegistrationDto) throws ApplicationException;

	public Response checkSqlConnectionForUpdatePatientSource(PatientRegistrationDto patientRegistrationDto) throws ApplicationException;

	public Response getPatientDetailsListForNewEMROnlyFavoritesList(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response getPatientMLCDocumentDetailList(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response getDatabaseLink(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response getVideoConsultationLinkForAppFromHIS(String uuid)throws ApplicationException;

	public Response updatePatientAsDeath(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response patientSearchPatientListForABDM(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;
	public Response updatePatientRegistrationFromMyLife(PatientMasterDto patientMasterDto)throws ApplicationException, Exception;
	
    public Response sendRealTimeSyncCallToMyLife(Integer patientId);

	public Response patientSearchPatientListForABDMForDeepLinking(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;


	public Response patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn(SlotMasterDto slotMasterDto) throws ApplicationException;

	public Response getInitalAssesmentData(SlotMasterDto slotMasterDto) throws ApplicationException;
}
