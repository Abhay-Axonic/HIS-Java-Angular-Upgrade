package com.param.procedure.dao;

import com.param.global.common.Response;
import com.param.procedure.dto.OTCompletionDetailsDto;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IOTCompletionDetailsDao {

	public Response saveOtCompletionDetails(OTCompletionDetailsDto dto)throws ApplicationException;

	public Response getOtCompletionDetials(int proReqId)throws ApplicationException;
}
