package com.param.global.otims.service;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.OtcCompletionInvImplantsDto;

@SuppressWarnings("rawtypes")
@Transactional
public interface IOtcCompletionInvImplantsService {

public Response saveInvImplants(OtcCompletionInvImplantsDto otcCompletionInvImplantsDto) throws ApplicationException;

public Response getImplantIssueAcceptanceData(OtcCompletionInvImplantsDto otcCompletionInvImplantsDto)throws ApplicationException;

}
