package com.param.global.otims.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.service.IProcedureOrderStatusService;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/otims")
@SuppressWarnings("rawtypes")
public class ProcedureOrderStatusController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(ProcedureOrderStatusController.class);


	@Autowired
	IProcedureOrderStatusService iProcedureOrderStatusService;
	
	
	@RequestMapping(value="/getActiveProcedureOrderStatus",method=RequestMethod.GET)
	Response getActiveProcedureOrderStatus() throws ApplicationException{
		
		try{
			return iProcedureOrderStatusService.getActiveProcedureOrderStatus();
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
