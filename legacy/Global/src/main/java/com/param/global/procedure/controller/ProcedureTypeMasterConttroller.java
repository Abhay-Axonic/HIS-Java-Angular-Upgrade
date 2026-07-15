package com.param.global.procedure.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.procedure.dto.ProcedureTypeMasterDto;
import com.param.global.procedure.service.IProcedureTypeMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="api/otims")
@SuppressWarnings({"rawtypes"})
public class ProcedureTypeMasterConttroller implements ICommonConstants{
	final static Logger logger = Logger.getLogger(ProcedureTypeMasterConttroller.class);

	
	@Autowired
	IProcedureTypeMasterService iProcedureTypeMasterService;
	
	@RequestMapping(value = "/saveProcedureTypeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveProcedureTypeMaster(@RequestBody ProcedureTypeMasterDto procedureTypeMasterDto){
		
		try {
			return iProcedureTypeMasterService.saveProcedureTypeMaster(procedureTypeMasterDto);
			
		} catch (Exception e) {
		logger.error("saveProcedureTypeMaster failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/updateProcedureTypeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateProcedureTypeMaster(@RequestBody ProcedureTypeMasterDto procedureTypeMasterDto){
		
		try {
			return iProcedureTypeMasterService.updateProcedureTypeMaster(procedureTypeMasterDto);
			
		} catch (Exception e) {
		logger.error("updateProcedureTypeMaster failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/updateProcedureTypeMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateProcedureTypeMasterStatus(@RequestBody ProcedureTypeMasterDto procedureTypeMasterDto){
		
		try {
			return iProcedureTypeMasterService.updateProcedureTypeMasterStatus(procedureTypeMasterDto);
			
		} catch (Exception e) {
		logger.error("updateProcedureTypeMasterStatus failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getListProcedureTypeMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getListProcedureTypeMaster(@RequestBody ProcedureTypeMasterDto procedureTypeMasterDto){
		
		try {
			return iProcedureTypeMasterService.getListProcedureTypeMaster(procedureTypeMasterDto);
			
		} catch (Exception e) {
		logger.error("getListProcedureTypeMaster failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getProcedureTypeMasterById/{procedureTypeId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public Response getProcedureTypeMasterById(@PathVariable ("procedureTypeId") Integer procedureTypeId,
												@PathVariable ("orgId") Integer orgId,
												@PathVariable ("unitId") Integer unitId){
		try {
			return iProcedureTypeMasterService.getProcedureTypeMasterById(procedureTypeId, orgId, unitId);
			
		} catch (Exception e) {
		logger.error("getProcedureTypeMasterById failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getProcedureTypeMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getProcedureTypeMasterCount(@RequestBody ProcedureTypeMasterDto procedureTypeMasterDto){
		
		try {
			return iProcedureTypeMasterService.getProcedureTypeMasterCount(procedureTypeMasterDto);
			
		} catch (Exception e) {
		logger.error("getProcedureTypeMasterCount failed", e);
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}



}
