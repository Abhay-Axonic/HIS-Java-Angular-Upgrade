package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.TransactionTypeMasterDto;
import com.param.global.service.ITransactionTypeMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings("rawtypes")
@RequestMapping(value = "/api/billing")
public class TransactionTypeController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(TransactionTypeController.class);


	@Autowired
	private ITransactionTypeMasterService iTransactionTypeMasterService;
	
	@RequestMapping(value = "/transactiontypes/{orgId}/{unitId}")
	public Response getTransactionTypes(@PathVariable("orgId")int orgId,@PathVariable("unitId")int unitId){
		try{

			return iTransactionTypeMasterService.getTransactionTypes(orgId, unitId);

		}catch(Exception e){
		logger.error("getTransactionTypes failed", e);
			return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value="/getTransactionTypeMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTransactionTypeMasterList(@RequestBody TransactionTypeMasterDto transactionTypeMasterDto){
		try{
			return iTransactionTypeMasterService.getTransactionTypeMasterList(transactionTypeMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/saveTransactionTypeMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveTransactionTypeMaster(@RequestBody TransactionTypeMasterDto transactionTypeMasterDto){
		try{
			
			return iTransactionTypeMasterService.saveTransactionTypeMaster(transactionTypeMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateTransactionTypeMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateTransactionTypeMaster(@RequestBody TransactionTypeMasterDto transactionTypeMasterDto){
		try{
			return iTransactionTypeMasterService.updateTransactionTypeMaster(transactionTypeMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	
	@RequestMapping(value="/updateStatusForTransactionType" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForTransactionType(@RequestBody TransactionTypeMasterDto transactionTypeMasterDto){
		try{
			
			return iTransactionTypeMasterService.updateStatusForTransactionType(transactionTypeMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
}
