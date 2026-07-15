package com.param.opd.coversheet.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.DiagnosisMapperDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({ "rawtypes"})
public interface IDiagnosisMapperService {

	Response saveDiagnosisMapper(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	Response searchDiagnosisByCodeOrDescription(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	Response getListOfDiagnosisMapper(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	Response updateDiagnosisMapperStatus(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	Response getGenderByDiagnosisId(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	Response getDiagnosisByGenderId(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	Response updateDiagnosisMapper(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	Response getDiagnosisMapperById(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	Response getDiagnosisMapperCount(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

}
