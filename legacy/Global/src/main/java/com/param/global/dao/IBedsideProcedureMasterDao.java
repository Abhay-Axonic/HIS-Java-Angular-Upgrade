package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.BedsideProcedureMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
public interface IBedsideProcedureMasterDao {
	
	public Response saveBedsideProcedureMaster(BedsideProcedureMasterDto bedsideProcedureMasterDto) throws ApplicationException;

	public Response updateBedsideProcedureMaster(BedsideProcedureMasterDto bedsideProcedureMasterDto) throws ApplicationException;

	public Response updateBedsideProcedureMasterStatus(BedsideProcedureMasterDto bedsideProcedureMasterDto) throws ApplicationException;

	public Response getBedsideProcedureMasterList(BedsideProcedureMasterDto bedsideProcedureMasterDto) throws ApplicationException;

	public Response getActiveBedsideProcedureMasterList(BedsideProcedureMasterDto bedsideProcedureMasterDto) throws ApplicationException;

	public Response getBedsideProcedureMasterByName(BedsideProcedureMasterDto bedsideProcedureMasterDto) throws ApplicationException;

	public Response getBedsideProcedureMasterByNameNotId(BedsideProcedureMasterDto bedsideProcedureMasterDto) throws ApplicationException;

}
