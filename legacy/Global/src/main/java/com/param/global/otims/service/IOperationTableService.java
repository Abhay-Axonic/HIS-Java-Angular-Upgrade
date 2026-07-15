package com.param.global.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.OperationTableTypeDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Transactional
public interface IOperationTableService {

	
	Response saveOperationTable(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;

	Response updateOperationTable(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;

	Response updateOperationTableStatus(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;

	Response getOperationTableList(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;

	Response getOperationTableListById(Integer operationTableId) throws ApplicationException;

	Response getOperationTableListCount(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;

	Response getActiveOperationTableList(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;
	
	Response getOperationTableListByName(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;
	
	Response getOperationTableListByNameNotById(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;

	Response getActiveOperationTableListByTheatreId(OperationTableTypeDto operationTableTypeDto)throws ApplicationException;

}
