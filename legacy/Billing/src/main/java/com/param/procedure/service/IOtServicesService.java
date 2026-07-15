package com.param.procedure.service;

import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IOtServicesService {
	public Response getOtServicesByProReqId(int id)throws ApplicationException;
}
