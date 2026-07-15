package com.param.service.controller;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.Response;

import com.param.global.common.ICommonConstants;
import com.param.service.dto.ContractGradeMasterDto;
import com.param.service.services.IContractGradeMasterService;

@RestController
@RequestMapping(value="/api/billing")
@SuppressWarnings({"rawtypes"})
public class ContractGradeMasterController implements ICommonConstants{
	@Autowired
	private IContractGradeMasterService iContractGradeMasterService;
	
	@RequestMapping(value = "/saveContractGradeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveContractGradeMaster(@RequestBody ContractGradeMasterDto contractGradeMasterDto){
		
		try {
			return iContractGradeMasterService.saveContractGradeMaster(contractGradeMasterDto);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateContractGradeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateContractGradeMaster(@RequestBody ContractGradeMasterDto contractGradeMasterDto) {
		
		try {
			return iContractGradeMasterService.updateContractGradeMaster(contractGradeMasterDto);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateContractGradeMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

	public  Response updateContractGradeMasterStatus(@RequestBody ContractGradeMasterDto contractGradeMasterDto) {
		try {
			return iContractGradeMasterService.updateContractGradeMasterStatus(contractGradeMasterDto);

		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/getContractGradeMasterById/{contractGradeMasterId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public  Response getContractGradeMasterById(@PathVariable ("contractGradeMasterId") Integer contractGradeMasterId,

															 @PathVariable ("orgId") Integer orgId,
															 @PathVariable ("unitId") Integer unitId) {
	
		try {
			return iContractGradeMasterService.getContractGradeMasterById(contractGradeMasterId, orgId, unitId);
		
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getContractGradeMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)

	public  Response getContractGradeMasterList(@RequestBody ContractGradeMasterDto contractGradeMasterDto) {
		try {
			return iContractGradeMasterService.getContractGradeMasterList(contractGradeMasterDto);

		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getContractGradeMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getContractGradeMasterCount(@RequestBody ContractGradeMasterDto contractGradeMasterDto) {
	
		try {
			return iContractGradeMasterService.getContractGradeMasterCount(contractGradeMasterDto);
			 
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/getActiveContractGradeMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveContractGradeMasterList(@RequestBody ContractGradeMasterDto contractGradeMasterDto) {
		
		try {
			return iContractGradeMasterService.getActiveContractGradeMasterList(contractGradeMasterDto);
		
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}

	@RequestMapping(value = "/getContractGradeMasterNotMappedInMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getContractGradeMasterNotMappedInMapper(@RequestBody ContractGradeMasterDto contractGradeMasterDto) {
	
		try {
			return iContractGradeMasterService.getContractGradeMasterNotMappedInMapper(contractGradeMasterDto);
		
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
}
