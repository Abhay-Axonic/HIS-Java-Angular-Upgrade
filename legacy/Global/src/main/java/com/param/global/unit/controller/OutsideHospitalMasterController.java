package com.param.global.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.OutsideHospitalDoctorMapperDto;
import com.param.global.unit.dto.OutsideHospitalMasterDto;
import com.param.global.unit.dto.OutsideHospitalMasterDto;
import com.param.global.unit.service.IOutsideHospitalMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings({"rawtypes"})

public class OutsideHospitalMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(OutsideHospitalMasterController.class);

	
	@Autowired
	private IOutsideHospitalMasterService iOutsideHospitalMasterService;
	
	@RequestMapping(value = "/saveOutsideHospitalMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveOutsideHospitalMaster(@RequestBody OutsideHospitalMasterDto outsideHospitalMasterDto){
		
		try {
			return iOutsideHospitalMasterService.saveOutsideHospitalMaster(outsideHospitalMasterDto);
			
		} catch (Exception e) {
		logger.error("saveOutsideHospitalMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateOutsideHospitalMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateOutsideHospitalMaster(@RequestBody OutsideHospitalMasterDto outsideHospitalMasterDto){
		
		try {
			return iOutsideHospitalMasterService.updateOutsideHospitalMaster(outsideHospitalMasterDto);
			
		} catch (Exception e) {
		logger.error("updateOutsideHospitalMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateOutsideHospitalMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateOutsideHospitalMasterStatus(@RequestBody OutsideHospitalMasterDto outsideHospitalMasterDto){
		
		try {
			return iOutsideHospitalMasterService.updateOutsideHospitalMasterStatus(outsideHospitalMasterDto);
			
		} catch (Exception e) {
		logger.error("updateOutsideHospitalMasterStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getOutsideHospitalMasterById/{outsideHospitalId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getOutsideHospitalMasterById(@PathVariable ("outsideHospitalId") Integer outsideHospitalId ,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		
		try {
			return iOutsideHospitalMasterService.getOutsideHospitalMasterById(outsideHospitalId, organizationId, unitId);
			
		} catch (Exception e) {
		logger.error("getOutsideHospitalMasterById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getOutsideHospitalMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOutsideHospitalMasterList(@RequestBody OutsideHospitalMasterDto outsideHospitalMasterDto){

		try {
			return iOutsideHospitalMasterService.getOutsideHospitalMasterList(outsideHospitalMasterDto);

		} catch (Exception e) {
		logger.error("getOutsideHospitalMasterList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getOutsideHospitalMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOutsideHospitalMasterCount(@RequestBody OutsideHospitalMasterDto outsideHospitalMasterDto){
		
		try {
			return iOutsideHospitalMasterService.getOutsideHospitalMasterCount(outsideHospitalMasterDto);
			
		} catch (Exception e) {
		logger.error("getOutsideHospitalMasterCount failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveOutsideHospitalMaster/{organizationId}", method = RequestMethod.GET)
	public Response getActiveOutsideHospitalMaster(@PathVariable ("organizationId") Integer organizationId){
		
		try {
			return iOutsideHospitalMasterService.getActiveOutsideHospitalMaster(organizationId);
			
		} catch (Exception e) {
		logger.error("getActiveOutsideHospitalMaster failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/saveOutsideHospitalDoctorMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveOutsideHospitalDoctorMapper(@RequestBody OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto){
		
		try {
			return iOutsideHospitalMasterService.saveOutsideHospitalDoctorMapper(outsideHospitalDoctorMapperDto);
			
		} catch (Exception e) {
		logger.error("saveOutsideHospitalDoctorMapper failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getOutsideHospitalDoctorMapperList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOutsideHospitalDoctorMapperList(@RequestBody OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto){
		
		try {
			return iOutsideHospitalMasterService.getOutsideHospitalDoctorMapperList(outsideHospitalDoctorMapperDto);
			
		} catch (Exception e) {
		logger.error("getOutsideHospitalDoctorMapperList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateOutsideHospitalDoctorMapperStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateOutsideHospitalDoctorMapperStatus(@RequestBody OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto){
		
		try {
			return iOutsideHospitalMasterService.updateOutsideHospitalDoctorMapperStatus(outsideHospitalDoctorMapperDto);
			
		} catch (Exception e) {
		logger.error("updateOutsideHospitalDoctorMapperStatus failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getOutsideHospitalDoctorMapperById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getOutsideHospitalDoctorMapperById(@RequestBody OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto){
		
		try {
			return iOutsideHospitalMasterService.getOutsideHospitalDoctorMapperById(outsideHospitalDoctorMapperDto);
			
		} catch (Exception e) {
		logger.error("getOutsideHospitalDoctorMapperById failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/searchOutsideHospitalDoctorList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchOutsideHospitalDoctorList(@RequestBody OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto){
		
		try {
			return iOutsideHospitalMasterService.searchOutsideHospitalDoctorList(outsideHospitalDoctorMapperDto);
			
		} catch (Exception e) {
		logger.error("searchOutsideHospitalDoctorList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getAllOutsideHospitalDoctorList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllOutsideHospitalDoctorList(@RequestBody OutsideHospitalDoctorMapperDto outsideHospitalDoctorMapperDto){
		
		try {
			return iOutsideHospitalMasterService.getAllOutsideHospitalDoctorList(outsideHospitalDoctorMapperDto);
			
		} catch (Exception e) {
		logger.error("getAllOutsideHospitalDoctorList failed", e);
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
