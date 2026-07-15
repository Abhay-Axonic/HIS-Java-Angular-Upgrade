package com.param.opd.coversheet.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.PatientDiagnosisServiceMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPatientDiagnosisServiceMapperDao {

	Response savePatientDaignosisServiceMapper(List<PatientDiagnosisServiceMapperDto> list) throws ApplicationException;

	Response getPatientDaignosisServiceMapper(PatientDiagnosisServiceMapperDto dto) throws ApplicationException;

}
