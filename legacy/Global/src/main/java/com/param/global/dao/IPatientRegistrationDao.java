package com.param.global.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.CustomPatientDetailsDto;
import com.param.global.dto.PatientDetailsDto;
import com.param.global.dto.PatientRegistrationDocDetailsDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.PatientVisitsDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.global.model.FingerPrintInterFace;
import com.param.global.model.PatientRegistration;
import com.param.opd.encounter.dto.EncounterDocDetailsDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPatientRegistrationDao extends IGenericDao<PatientRegistration, Integer> {
	public Response getPatientDetailsById(Integer patientId) throws ApplicationException;

	public Response savePatientRegistration(PatientRegistrationDto patientRegistrationDto) throws ApplicationException;

	public Response savePatientDetails(PatientRegistrationDto patientRegistrationDto) throws ApplicationException;

	public Response getPatientDetais(PatientRegistrationDto patientRegistrationDto) throws ApplicationException;

	public Response checkUniquePatient(PatientRegistrationDto patientRegistrationDto) throws ApplicationException;

	public Long getNextValPatientId() throws ApplicationException;

	public Response patientSearchOTC(PatientRegistrationDto patientRegistrationDto) throws ApplicationException;

	public Response patientSearchER(PatientRegistrationDto patientRegistrationDto) throws ApplicationException;

	public Response patientSearchNormal(PatientRegistrationDto patientRegistrationDto) throws ApplicationException;

	public Response updatePatient(PatientRegistrationDto patientRegistrationDto) throws ApplicationException;

	public Response savePatientRegistrationLog(PatientRegistrationDto patientRegistrationDto);

	public Response patientSearchNormalByPatientId(Integer patientId,Integer orgId,Integer unitId)
			throws ApplicationException;

	public Response patientDetailsByIdForBilling(int patientId) throws ApplicationException;

	// data sync
	public Response getPatientByIdForSync(Integer recordId) throws ApplicationException;

	public Response patientSearchPre(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;
	
	public Response updatePatientImagePath(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response patientSearchByRegistrationTypeIdList(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response updateIsPrimaryFlag(Integer patientId)throws ApplicationException;

	public Response updateUuidByPatient(int patientId, String uuid)throws ApplicationException;

	public Response getPatientDetaisForHealthCard(int patientId)throws ApplicationException;

	public Response getPatientAllergies(int patientId, int unitId, int organizationId)throws ApplicationException;

	public Response patientAutofillSearch(SlotMasterDto slotMasterDto)throws ApplicationException;

	public Response patientTypeHeadSearchForSelfRegistration(SlotMasterDto slotMasterDto)throws ApplicationException;
	
	public Response savePatientThumb(FingerPrintInterFace fingerInterface)throws ApplicationException;

	public Response getPatientByuuid(List<Integer> uuids,int orgId,int unitId)throws ApplicationException;

	public Response getPatientDetailsByuuid(String uuid)throws ApplicationException;

	List<PatientDetailsDto> getPatientDataForBirthdaySMS(Integer unitId) throws ApplicationException;

	public Response PatientDemography(int patientId)throws ApplicationException;

	public Response updatePatientAsAbscound(PatientRegistrationDto dto)throws ApplicationException;
	
	public Response commonPatientSearch(PatientRegistrationDto patientRegistrationDto);

	public Response checkEmailDetails(Integer orgId, String emailId)throws ApplicationException;

	public Response patientDetailsByIdForCyclic(int patientId)throws ApplicationException;

	public Response patientAutofillSearchPlain(SlotMasterDto slotMasterDto) throws ApplicationException;

	public Response getAllPatientVisits(PatientVisitsDto patientVisitsDto)throws ApplicationException;

	public Response getPatientCategoryIdByPatientId(Integer patientId)throws ApplicationException;

	public Response getAllPastVisitsDataForInfo(PatientVisitsDto patientVisitsDto)throws ApplicationException;

	public Response deletePatientImage(int patientId, int organizationId)throws ApplicationException;

	public Response changeStatus(PatientRegistrationDto patientRegistrationDto);

	public Integer getPatientIdByUuidAndHospitalId(ScheduleAppointmentReqDto reqDto)throws ApplicationException;

	public String getUUIDByPatientId(Integer patientId);

	public Response getPatientDocuments(EncounterDocDetailsDto encounterDocDetailsDto)throws ApplicationException;

	public Response getPatientDocumentsByEncounterDocId(Integer recordId) throws ApplicationException;

	public Response getPatientKinDetails(Integer recordId)throws ApplicationException;

	public Response deletePatientDocument(EncounterDocDetailsDto encounterDocDetailsDto)throws ApplicationException;

	public Response patientSearchPatientList(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response getPatientDetailsByUhid(PatientRegistrationDto dto)throws ApplicationException;

	public Response saveCustomPatientDetails(CustomPatientDetailsDto customPatientDetailsObj)throws ApplicationException;

	public Response getPatientInfoForHospitalVisit(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response updateCustomDetails(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;
	
    public Response savePatientRegistrationDocDetails(PatientRegistrationDocDetailsDto patientRegistrationDocDetailsDto)throws ApplicationException;

    public Response getPatientDocumentDetailList(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response sendPatientSmsFromCoversheet(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response patientTypeHeadSearchForAppointmentUnique(SlotMasterDto slotMasterDto)throws ApplicationException;

	public Response getPatientDetailsListForNewEMR(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response savePatientRemarksForNewEMR(PatientRegistrationDto patientRegistrationDto)throws ApplicationException;

	public Response checkSqlConnection(PatientRegistrationDto patientRegistrationDto)throws ApplicationException, Exception;

	public Response checkSqlConnectionForUpdate(PatientRegistrationDto patientRegistrationDto)throws ApplicationException, Exception;

	public Response checkSqlConnectionForUpdatePatientSource(PatientRegistrationDto patientRegistrationDto)throws ApplicationException, Exception;

	public Response getPatientMLCDocumentDetailList(PatientRegistrationDto patientRegistrationDto)throws ApplicationException, Exception;

	public Long getNextValPatientIdInPoc() throws ApplicationException;

	public Response getDatabaseLink(PatientRegistrationDto patientRegistrationDto)throws ApplicationException, Exception;

	public Response getcontryIdFromCountryName(ScheduleAppointmentReqDto reqDto)throws ApplicationException, Exception;

	public Response getVideoConsultationLinkForAppFromHIS(String uuid)throws ApplicationException, Exception;

	public Response updatePatientAsDeath(PatientRegistrationDto patientRegistrationDto)throws ApplicationException, Exception;

	public Response patientSearchPatientListForABDM(PatientRegistrationDto patientRegistrationDto)throws ApplicationException, Exception;

	public Response patientSearchPatientListForABDMForDeepLinking(PatientRegistrationDto patientRegistrationDto)throws ApplicationException, Exception;


	public Response patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn(SlotMasterDto slotMasterDto) throws ApplicationException, Exception;

	public Response getInitalAssesmentData(SlotMasterDto slotMasterDto) throws ApplicationException, Exception;
	public  Response  updateLatestMedicalSummaryReport(Integer patientId, String newComprehensiveClinicalReport);

}
