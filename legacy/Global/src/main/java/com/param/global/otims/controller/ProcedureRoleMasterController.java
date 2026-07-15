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
import com.param.global.otims.dto.ProcedureRoleMasterDto;
import com.param.global.otims.service.IProcedureRoleMasterService;



import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="api/otims")
@SuppressWarnings({"rawtypes"})

public class ProcedureRoleMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureRoleMasterController.class);

	@Autowired
	private IProcedureRoleMasterService iProcedureRoleMasterService;
	
	@RequestMapping(value = "/saveProcedureRoleMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveProcedureRoleMaster(@RequestBody ProcedureRoleMasterDto procedureRoleMasterDto){
		
		try {
			return iProcedureRoleMasterService.saveProcedureRoleMaster(procedureRoleMasterDto);
			
		} catch (Exception e) {
		logger.error("saveProcedureRoleMaster failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	

	@RequestMapping(value = "/updateProcedureRoleMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateProcedureRoleMaster(@RequestBody ProcedureRoleMasterDto procedureRoleMasterDto){
		
		try {
			return iProcedureRoleMasterService.updateProcedureRoleMaster(procedureRoleMasterDto);
			
		} catch (Exception e) {
		logger.error("updateProcedureRoleMaster failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	

	@RequestMapping(value = "/updateProcedureRoleMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateProcedureRoleMasterStatus(@RequestBody ProcedureRoleMasterDto procedureRoleMasterDto){
		
		try {
			return iProcedureRoleMasterService.updateProcedureRoleMasterStatus(procedureRoleMasterDto);
			
		} catch (Exception e) {
		logger.error("updateProcedureRoleMasterStatus failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	

	@RequestMapping(value = "/getProcedureRoleMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureRoleMasterList(@RequestBody ProcedureRoleMasterDto procedureRoleMasterDto){
		
		try {
			return iProcedureRoleMasterService.getProcedureRoleMasterList(procedureRoleMasterDto);
			
		} catch (Exception e) {
		logger.error("getProcedureRoleMasterList failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	

	@RequestMapping(value = "/getProcedureRoleMasterById/{procedureRoleId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getProcedureRoleMasterById(@PathVariable("procedureRoleId") Integer procedureRoleId, 
											   @PathVariable("orgId") Integer orgId, 
											   @PathVariable("unitId") Integer unitId){
		
		try {
			return iProcedureRoleMasterService.getProcedureRoleMasterById(procedureRoleId, orgId, unitId);
			
		} catch (Exception e) {
		logger.error("getProcedureRoleMasterById failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	

	@RequestMapping(value = "/getProcedureRoleMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureRoleMasterCount(@RequestBody ProcedureRoleMasterDto procedureRoleMasterDto){
		
		try {
			return iProcedureRoleMasterService.getProcedureRoleMasterCount(procedureRoleMasterDto);
			
		} catch (Exception e) {
		logger.error("getProcedureRoleMasterCount failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	

	@RequestMapping(value = "/getActiveProcedureRoleMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActiveProcedureRoleMasterList(@RequestBody ProcedureRoleMasterDto procedureRoleMasterDto){
		
		try {
			return iProcedureRoleMasterService.getActiveProcedureRoleMasterList(procedureRoleMasterDto);
			
		} catch (Exception e) {
		logger.error("getActiveProcedureRoleMasterList failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	

	
}
