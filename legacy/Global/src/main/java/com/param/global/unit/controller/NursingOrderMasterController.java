package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.NursingOrderMasterDto;
import com.param.global.unit.service.INursingOrderMasterService;

@RestController
@RequestMapping(value="/api/global/unit")
@SuppressWarnings({"rawtypes"})
public class NursingOrderMasterController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(NursingOrderMasterController.class);

	@Autowired
	private INursingOrderMasterService iNursingOrderMasterService;
	
	@RequestMapping(value = "/saveNursingOrderMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveNursingOrderMaster(@RequestBody NursingOrderMasterDto nursingOrderMasterDto){
		
		try {
			return iNursingOrderMasterService.saveNursingOrderMaster(nursingOrderMasterDto);

		} catch (Exception e) {
			logger.error("saveNursingOrderMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateNursingOrderMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNursingOrderMaster(@RequestBody NursingOrderMasterDto nursingOrderMasterDto){
		
		try {
			return iNursingOrderMasterService.updateNursingOrderMaster(nursingOrderMasterDto);

		} catch (Exception e) {
			logger.error("updateNursingOrderMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateNursingOrderMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateNursingOrderMasterStatus(@RequestBody NursingOrderMasterDto nursingOrderMasterDto){
		
		try {
			return iNursingOrderMasterService.updateNursingOrderMasterStatus(nursingOrderMasterDto);

		} catch (Exception e) {
			logger.error("updateNursingOrderMasterStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getNursingOrderMasterById/{nursingOrderId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getNursingOrderMasterById(@PathVariable ("nursingOrderId") Integer nursingOrderId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		
		try {
			return iNursingOrderMasterService.getNursingOrderMasterById(nursingOrderId, organizationId, unitId);

		} catch (Exception e) {
			logger.error("getNursingOrderMasterById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getNursingOrderMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNursingOrderMasterList(@RequestBody NursingOrderMasterDto nursingOrderMasterDto){

		try {
			return iNursingOrderMasterService.getNursingOrderMasterList(nursingOrderMasterDto);

		} catch (Exception e) {
			logger.error("getNursingOrderMasterList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getNursingOrderMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNursingOrderMasterCount(@RequestBody NursingOrderMasterDto nursingOrderMasterDto){
		
		try {
			return iNursingOrderMasterService.getNursingOrderMasterCount(nursingOrderMasterDto);

		} catch (Exception e) {
			logger.error("getNursingOrderMasterCount failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveNursingOrderMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveNursingOrderMaster(@PathVariable ("organizationId") Integer organizationId,
										 @PathVariable ("unitId") Integer unitId){
		
		try {
			return iNursingOrderMasterService.getActiveNursingOrderMaster(organizationId, unitId);

		} catch (Exception e) {
			logger.error("getActiveNursingOrderMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getNursingOrderMasterListSearch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getNursingOrderMasterListSearch(@RequestBody NursingOrderMasterDto nursingOrderMasterDto){

		try {
			return iNursingOrderMasterService.getNursingOrderMasterListSearch(nursingOrderMasterDto);

		} catch (Exception e) {
			logger.error("getNursingOrderMasterListSearch failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}


}
