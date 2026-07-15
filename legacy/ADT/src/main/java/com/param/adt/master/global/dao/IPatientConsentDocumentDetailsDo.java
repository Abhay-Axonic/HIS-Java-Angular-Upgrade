package com.param.adt.master.global.dao;

import com.param.adt.common.Response;
import com.param.global.dto.PatientConsentDocumentDetailsDto;
import com.param.global.dto.PatientConsentMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPatientConsentDocumentDetailsDo {

	Response uploadpatientConsentDoc(PatientConsentDocumentDetailsDto dto)throws ApplicationException;

	Response getConsentDocsListByMapperId(PatientConsentMapperDto dto)throws ApplicationException;

	Response deactiveConsentDocs(PatientConsentDocumentDetailsDto dto)throws ApplicationException;

}
