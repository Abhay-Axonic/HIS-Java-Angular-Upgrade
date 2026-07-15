package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.PreOperativeDetailsDto;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPreOperativeDetailsDao {

	Response savePreOperativeDetails(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;

	Response getPreOperativeDetailsByProReqId(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;

	Response getActiveDoctorList(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;

	Response getAnaesthesiaDoctorList(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;

	Response getProcedureOrderIdByProReqId(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;

	Response getAiTranscriptionCreds(PreOperativeDetailsDto preOperativeDetailsDto) throws ApplicationException;
}
