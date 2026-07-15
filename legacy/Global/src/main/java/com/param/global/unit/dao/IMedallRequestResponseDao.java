package com.param.global.unit.dao;

import com.param.global.billing.dto.POSMachineResponseMapperDto;
import com.param.global.common.Response;
import com.param.global.unit.dto.MedallRequestResponseDto;

import in.param.exception.ApplicationException;

public interface IMedallRequestResponseDao {

	public Response saveMedallRequestResponse(MedallRequestResponseDto dto) throws ApplicationException;

	public Response getMedallRequestResponse(MedallRequestResponseDto med)throws ApplicationException;

	public Response updateMedallRequestResponse(MedallRequestResponseDto medReqSaveDto)throws ApplicationException;


}
