package com.param.global.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureOrderComponentDetailsDto;
import com.param.otims.model.ProcedureOrderComponentDetails;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IProcedureOrderComponentDetailsDao extends IGenericDao<ProcedureOrderComponentDetails, Integer>{
	public Response saveProcedureOrderComponentDetails(ProcedureOrderComponentDetailsDto dto)throws ApplicationException;
}
