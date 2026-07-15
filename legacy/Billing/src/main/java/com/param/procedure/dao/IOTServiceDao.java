package com.param.procedure.dao;

import com.param.global.common.Response;
import com.param.otims.model.OperationTheaterService;
import com.param.procedure.dto.OTServiceDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IOTServiceDao extends IGenericDao<OperationTheaterService, Integer>{
	public Response saveOTService(OTServiceDto dto)throws ApplicationException;
	public Response getOtServicesByProReqId(int id)throws ApplicationException;
}
