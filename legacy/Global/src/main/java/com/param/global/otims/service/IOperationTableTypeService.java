package com.param.global.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.OperationTableTypeDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Transactional
public interface IOperationTableTypeService {

	
	Response saveOperationTableType(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;

	Response updateOperationTableType(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;

	Response updateOperationTableTypeStatus(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;

	Response getOperationTableTypeList(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;

	Response getOperationTableTypeListById(Integer operationTableTypeId) throws ApplicationException;

	Response getOperationTableTypeListCount(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;

	Response getActiveOperationTableTypeList(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;
	
	Response getOperationTableTypeListByName(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;
	
	Response getOperationTableTypeListByNameNotById(OperationTableTypeDto operationTableTypeDto) throws ApplicationException;

}
