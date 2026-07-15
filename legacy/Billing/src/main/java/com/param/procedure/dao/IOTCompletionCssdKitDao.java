package com.param.procedure.dao;

import com.param.global.common.Response;
import com.param.procedure.dto.OTCompletionCssdKitDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOTCompletionCssdKitDao {

	public Response saveOtCompletionCssdKit(OTCompletionCssdKitDto dto)throws ApplicationException;

	public Response getOtCSSDKit(int proReqId)throws ApplicationException;

	public Response updateIsUsedFlag(OTCompletionCssdKitDto otCompletionCssdKitDto, char c);
}
