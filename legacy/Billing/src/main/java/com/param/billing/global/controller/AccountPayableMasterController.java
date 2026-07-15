package com.param.billing.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.param.billing.global.service.IAccountPayableMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AccountPayableMasterDto;

@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings({"rawtypes"})
public class AccountPayableMasterController implements ICommonConstants{
	 
	@Autowired
	IAccountPayableMasterService iAccountPayableMasterService;
	
	
	@RequestMapping(value = "/saveAccountPayable", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveAccountPayable(@RequestBody AccountPayableMasterDto accountPayableMasterDto) throws Exception{
		
		try {
			return iAccountPayableMasterService.saveAccountPayable(accountPayableMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getAccountPayable", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAccountPayable(@RequestBody AccountPayableMasterDto accountPayableMasterDto) throws Exception{
		
		try {
			return iAccountPayableMasterService.getAccountPayable(accountPayableMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateAccountPayableStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateAccountPayableStatus(@RequestBody AccountPayableMasterDto accountPayableMasterDto) throws Exception{
		
		try {
			return iAccountPayableMasterService.updateAccountPayableStatus(accountPayableMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveAccountPayable", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveAccountPayable(@RequestBody AccountPayableMasterDto accountPayableMasterDto) throws Exception{
		
		try {
			return iAccountPayableMasterService.getActiveAccountPayable(accountPayableMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
