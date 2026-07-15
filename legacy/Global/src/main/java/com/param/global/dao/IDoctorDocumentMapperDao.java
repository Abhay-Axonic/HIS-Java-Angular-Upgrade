package com.param.global.dao;

import com.param.global.common.Response;

import com.param.global.dto.DoctorDocumentMapperDto;
import com.param.global.dto.DoctorMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IDoctorDocumentMapperDao {

	public Response saveDoctorQualificationDetails(DoctorDocumentMapperDto dto)throws ApplicationException;
	public Response getListOfDoctorDegreeDoccument(DoctorDocumentMapperDto doctorDocumentMapperDto)throws ApplicationException;
	public Response updateDoctorEqucationDetails(DoctorDocumentMapperDto dto)throws ApplicationException;


}


