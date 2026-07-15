package com.param.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.PreOperativeDetailsDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IPreOperativeDetailsService {

	Response savePreOperativeDetails(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;

	Response getPreOperativeDetailsByProReqId(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;

	Response getActiveDoctorList(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;

	Response sendNotificationToDoctor(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;

	Response getAnaesthesiaDoctorList(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;

	Response getProcedureOrderIdByProReqId(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;

    Response getAiTranscriptionCreds(PreOperativeDetailsDto preOperativeDetailsDto) throws ApplicationException;

}
