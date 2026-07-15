package com.param.adt.master.global.dao;

import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.PatientConsentVideoDetailsDto;
import com.param.adt.common.Response;
import com.param.adt.master.global.model.PatientConsentVideoDetails;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPatientConsentVideoDetailsDao {

	Response uploadpatientConsentVideo(PatientConsentVideoDetailsDto dto)throws ApplicationException;

	Response getConsentDocsListByMapperId(PatientConsentMapperDto patientConsentMapperDto)throws ApplicationException;

	Response deactiveConsentVideo(PatientConsentVideoDetails dto)throws ApplicationException ;

}
