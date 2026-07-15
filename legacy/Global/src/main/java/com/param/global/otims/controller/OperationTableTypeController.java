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
import com.param.global.otims.service.IOperationTableTypeService;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value = "/api/otims")
@SuppressWarnings("rawtypes")
public class OperationTableTypeController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(OperationTableTypeController.class);


	@Autowired
	IOperationTableTypeService iOperationTableTypeService;
	
	@RequestMapping(value = "/saveOperationTableType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response saveOperationTableType(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
				return iOperationTableTypeService.saveOperationTableType(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@RequestMapping(value = "/updateOperationTableType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response updateOperationTableType(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableTypeService.updateOperationTableType(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/updateOperationTableTypeStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response updateOperationTableTypeStatus(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableTypeService.updateOperationTableTypeStatus(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getOperationTableTypeList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response getOperationTableTypeList(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableTypeService.getOperationTableTypeList(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getOperationTableTypeListById/{operationTableTypeId}", method = RequestMethod.GET)
	Response getOperationTableTypeListById(@PathVariable("operationTableTypeId") Integer operationTableTypeId)
			throws ApplicationException {
		try {	
			return iOperationTableTypeService.getOperationTableTypeListById(operationTableTypeId);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getOperationTableTypeListCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response getOperationTableTypeListCount(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableTypeService.getOperationTableTypeListCount(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getActiveOperationTableTypeList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	Response getActiveOperationTableTypeList(@RequestBody OperationTableTypeDto operationTableTypeDto)
			throws ApplicationException {
		try {
			return iOperationTableTypeService.getActiveOperationTableTypeList(operationTableTypeDto);
		} catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
