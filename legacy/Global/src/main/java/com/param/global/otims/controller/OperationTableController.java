package com.param.global.otims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.OperationTableTypeDto;
import com.param.global.otims.service.IOperationTableService;
import in.param.exception.ApplicationException;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value = "/api/otims")
@SuppressWarnings("rawtypes")

public class OperationTableController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(OperationTableController.class);


	@Autowired
	IOperationTableService iOperationTableService;
	
	@RequestMapping(value = "/saveOperationTable", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response saveOperationTable(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
				return iOperationTableService.saveOperationTable(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value = "/updateOperationTable", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response updateOperationTable(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableService.updateOperationTable(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/updateOperationTableStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response updateOperationTableStatus(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableService.updateOperationTableStatus(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getOperationTableList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response getOperationTableList(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableService.getOperationTableList(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getOperationTableListById/{operationTableId}", method = RequestMethod.GET)
	Response getOperationTableListById(@PathVariable("operationTableId") Integer operationTableId)
			throws ApplicationException {
		try {	
			return iOperationTableService.getOperationTableListById(operationTableId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getOperationTableListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response getOperationTableListCount(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableService.getOperationTableListCount(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getActiveOperationTableList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response getActiveOperationTableList(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableService.getActiveOperationTableList(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

	@RequestMapping(value = "/getActiveOperationTableListByTheatreId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response getActiveOperationTableListByTheatreId(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableService.getActiveOperationTableListByTheatreId(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
