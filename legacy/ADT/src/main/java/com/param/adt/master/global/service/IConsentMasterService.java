package com.param.adt.master.global.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.transaction.Transactional;

import com.param.adt.common.Response;
import com.param.adt.master.global.dto.ConsentMasterDto;
import com.param.adt.master.global.model.PatientConsentVideoDetails;
import com.param.global.dto.PatientConsentDocumentDetailsDto;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.PatientConsentVideoDetailsDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IConsentMasterService {

	@Transactional
	Response addConsentMaster(ConsentMasterDto consentMasterDto) throws ApplicationException;

	@Transactional
	Response updateConsentMaster(ConsentMasterDto consentMasterDto) throws ApplicationException;

	@Transactional
	Response updateStatusForConsent(ConsentMasterDto consentMasterDto)throws ApplicationException;

	@Transactional
	Response getConsentMasterList(ConsentMasterDto consentMasterDto) throws ApplicationException;

	@Transactional
	Response getConsentById(ConsentMasterDto consentMasterDto) throws ApplicationException;

	@Transactional
	Response getActiveConsentList(Integer orgId,Integer unitId) throws ApplicationException;

	@Transactional
	Response getConsentCount(ConsentMasterDto consentMasterDto) throws ApplicationException;
	
	@Transactional
	Response getConsentListByConsentTypeId(ConsentMasterDto consentMasterDto) throws ApplicationException;

	@Transactional
	Response saveDocumentConsentMaster(ConsentMasterDto consentMasterDto)throws ApplicationException;

	@Transactional
	Response getPatientConsent(PatientConsentMapperDto dto)throws ApplicationException;
	
	@Transactional
	Response savePatientConsent(List<PatientConsentMapperDto> list)throws ApplicationException;

	@Transactional
	Response uploadpatientConsentDoc(PatientConsentDocumentDetailsDto dto)throws ApplicationException;

	@Transactional
	Response uploadpatientConsentVideo(PatientConsentVideoDetailsDto dto);

	@Transactional
	Response getConsentListByAdmissionAndEncounterId(PatientConsentMapperDto consentMasterDto)throws ApplicationException;

	@Transactional
	Response cancelConsentPrint(Integer consentTransactionId)throws ApplicationException;

	@Transactional
	ByteArrayOutputStream getPatientConsentPrintPdfFile(PatientConsentMapperDto dto)throws ApplicationException;

	@Transactional
	Response getConsentDocsListByMapperId(PatientConsentMapperDto patientConsentMapperDto)throws ApplicationException;

	@Transactional
	Response getConsentVideoListByMapperId(PatientConsentMapperDto patientConsentMapperDto)throws ApplicationException;

	@Transactional
	Response deactiveConsentVideo(PatientConsentVideoDetails dto)throws ApplicationException;

	@Transactional
	Response deactiveConsentDocs(PatientConsentDocumentDetailsDto dto)throws ApplicationException;

	@Transactional
	Response getConsentHtmlString(Integer consentTransactionId)throws ApplicationException;

	@Transactional
	Response replaceConsentAnotations(PatientConsentMapperDto dto)throws ApplicationException;


}
