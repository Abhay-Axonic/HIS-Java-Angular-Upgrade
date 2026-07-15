package com.param.opd.coversheet.dao;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.DiagnosisMapperDto;

import in.param.exception.ApplicationException;

public interface IDiagnosisMapperDao {

	public Response saveDiagnosisMapper(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	public Response searchDiagnosisByCodeOrDescription(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	public Response getListOfDiagnosisMapper(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	public Response updateDiagnosisMapperStatus(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	public Response getGenderByDiagnosisId(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	public Response getDiagnosisByGenderId(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	public Response updateDiagnosisMapper(DiagnosisMapperDto dto)throws ApplicationException;

	public Response checkDiagnosisMapperAvailable(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	public Response getDiagnosisMapperById(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

	public Response getDiagnosisMapperCount(DiagnosisMapperDto diagnosisMapperDto)throws ApplicationException;

}
