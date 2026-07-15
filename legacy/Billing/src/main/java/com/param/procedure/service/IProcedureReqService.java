package com.param.procedure.service;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.ProcedureReqMasterDto;

@SuppressWarnings("rawtypes")
public interface IProcedureReqService {
	public Response saveProcedureReq(ProcedureReqMasterDto procedureReqMasterDto)throws ServiceException;
}
