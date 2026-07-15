package com.param.procedure.dao;

import com.param.global.procedure.dto.OtCompletionMedicalGasDto;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
@SuppressWarnings("rawtypes")
public interface IOtCompletionMedicalGasDao {

	
	public Response  saveOtCompletionMedicalGas(OtCompletionMedicalGasDto medicalGas)throws ApplicationException;

	public Response getOTMedicalGas(int proReqId)throws ApplicationException;

}
