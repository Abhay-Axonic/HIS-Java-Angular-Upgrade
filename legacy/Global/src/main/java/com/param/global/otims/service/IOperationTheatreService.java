package com.param.global.otims.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.otims.dto.OperationTheatreDto;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IOperationTheatreService {
	
	public Response saveOperationTheatre(OperationTheatreDto operationTheatreDto) throws ApplicationException;
	
	public Response updateOperationTheatre(OperationTheatreDto operationTheatreDto) throws ApplicationException;
	
	public Response updateOperationTheatreStatus(OperationTheatreDto operationTheatreDto) throws ApplicationException;
	
	public Response getOperationTheatreList(OperationTheatreDto operationTheatreDto) throws ApplicationException;
	
	public Response getOperationTheatreById(Integer operationTheatreId, Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getActiveOperationTheatreList(OperationTheatreDto operationTheatreDto) throws ApplicationException;
	
	public Response getOperationTheatreCount(OperationTheatreDto operationTheatreDto) throws ApplicationException;
	
	public Response getOperationTheatreByName(OperationTheatreDto operationTheatreDto) throws ApplicationException;
	
	public Response getOperationTheatreByNameNotId(OperationTheatreDto operationTheatreDto) throws ApplicationException;

	public Response getOperationTheatreByTypeId(Integer operationTheatreTypeId, Integer orgId, Integer unitId) throws ApplicationException;
	
}
