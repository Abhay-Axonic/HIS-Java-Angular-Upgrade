package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.OtcCompletionInvImplantsDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IOtcCompletionInvImplantsDao {

	public Response saveInvImplants(OtcCompletionInvImplantsDto otcCompletionInvImplantsDto) throws ApplicationException;

	public Response getImplantIssueAcceptanceData(OtcCompletionInvImplantsDto otcCompletionInvImplantsDto) throws ApplicationException;

	public Response updateIssueStatus(OtcCompletionInvImplantsDto meddto) throws ApplicationException;

	public Response getInvImplants(int proReqId) throws ApplicationException;

}
