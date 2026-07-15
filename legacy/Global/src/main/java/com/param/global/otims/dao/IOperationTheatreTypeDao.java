package com.param.global.otims.dao;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.otims.dto.OperationTheatreTypeDto;

@SuppressWarnings({"rawtypes"})
public interface IOperationTheatreTypeDao {
	
	public Response saveOperationTheatreType(OperationTheatreTypeDto operationTheatreTypeDto) throws ApplicationException;
	
	public Response updateOperationTheatreType(OperationTheatreTypeDto operationTheatreTypeDto) throws ApplicationException;
	
	public Response updateOperationTheatreTypeStatus(OperationTheatreTypeDto operationTheatreTypeDto) throws ApplicationException;
	
	public Response getOperationTheatreTypeById(Integer operationTheatreTypeId, Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getOperationTheatreTypeList(OperationTheatreTypeDto operationTheatreTypeDto) throws ApplicationException;
	
	public Response getActiveOperationTheatreTypeList(OperationTheatreTypeDto operationTheatreTypeDto) throws ApplicationException;
	
	public Response getOperationTheatreTypeCount(OperationTheatreTypeDto operationTheatreTypeDto) throws ApplicationException;
	
	public Response getOperationTheatreTypeByName(OperationTheatreTypeDto operationTheatreTypeDto) throws ApplicationException;
	
	public Response getOperationTheatreTypeByNameNotId(OperationTheatreTypeDto operationTheatreTypeDto) throws ApplicationException;
}
