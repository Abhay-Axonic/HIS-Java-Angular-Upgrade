package com.param.adt.master.global.dao;

import java.io.ByteArrayOutputStream;

import com.param.adt.common.Response;
import com.param.adt.master.global.dto.ConsentMasterDto;
import com.param.adt.master.global.model.ConsentMaster;
import com.param.global.dto.PatientConsentMapperDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IConsentMasterDao extends IGenericDao<ConsentMaster, Integer>
{

	
	Response addConsentMaster(ConsentMasterDto consentMasterDto) throws ApplicationException;

	Response updateConsentMaster(ConsentMasterDto consentMasterDto);
	
	Response getConsentByName(ConsentMasterDto consentMasterDto)throws ApplicationException;

	Response getConsentByNameNotId(ConsentMasterDto consentMasterDto) throws ApplicationException;

	Response getConsentById(ConsentMasterDto consentMasterDto) throws ApplicationException;

	Response updateConsentStatus(ConsentMasterDto consentMasterDto) throws ApplicationException;

	Response getConsentMasterList(ConsentMasterDto consentMasterDto) throws ApplicationException;

	Response getCount(ConsentMasterDto consentMasterDto) throws ApplicationException;

	Response getActiveConsentList(Integer orgId, Integer unitId) throws ApplicationException;

	Response getConsentListByConsentTypeId(ConsentMasterDto consentMasterDto) throws ApplicationException;

	Response updateConsentMasterForFileUploading(ConsentMasterDto consentMasterDto)throws ApplicationException;
	
	Response getPatientConsent(PatientConsentMapperDto dto)throws ApplicationException;
	
	Response savePatientConsent(PatientConsentMapperDto dto)throws ApplicationException;

	Response getConsentListByAdmissionAndEncounterId(PatientConsentMapperDto dto)throws ApplicationException;

	Response cancelConsentPrint(Integer consentTransactionId)throws ApplicationException;

	ByteArrayOutputStream createConsentPrintPDF(PatientConsentMapperDto dto)throws ApplicationException;

	Response getConsentHtmlString(Integer consentTransactionId)throws ApplicationException;
}
