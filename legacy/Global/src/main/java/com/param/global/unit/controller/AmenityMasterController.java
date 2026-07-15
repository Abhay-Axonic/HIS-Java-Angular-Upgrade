package com.param.global.unit.controller;

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
import com.param.global.unit.dto.AmenityMasterDto;
import com.param.global.unit.service.IAmenityMasterService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/adt")
@SuppressWarnings("rawtypes")
public class AmenityMasterController implements ICommonConstants{
	final static Logger logger = Logger.getLogger(AmenityMasterController.class);

	
	@Autowired
	IAmenityMasterService iAmenityMasterService;
	
	@RequestMapping(value="saveAmenityMaster",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody Response saveAmenityMaster(@RequestBody AmenityMasterDto amenityMasterDto){
		try{
			return iAmenityMasterService.saveAmenityMaster(amenityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="updateAmenityMaster",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody Response updateAmenityMaster(@RequestBody AmenityMasterDto amenityMasterDto){
		try{
			return iAmenityMasterService.updateAmenityMaster(amenityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="updateAmenityMasterStatus",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody Response updateAmenityMasterStatus(@RequestBody AmenityMasterDto amenityMasterDto){
		try{
			return iAmenityMasterService.updateAmenityMasterStatus(amenityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="getAmenityMasterList",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody Response getAmenityMasterList(@RequestBody AmenityMasterDto amenityMasterDto){
		try{
			return iAmenityMasterService.getAmenityMasterList(amenityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="getActiveAmenityMasterList",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody Response getActiveAmenityMasterList(@RequestBody AmenityMasterDto amenityMasterDto){
		try{
			return iAmenityMasterService.getActiveAmenityMasterList(amenityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="getAmenityMasterListCount",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody Response getAmenityMasterListCount(@RequestBody AmenityMasterDto amenityMasterDto){
		try{
			return iAmenityMasterService.getAmenityMasterListCount(amenityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="getAmenityMasterListById/{amenityId}",method=RequestMethod.GET)
	@ResponseBody Response getAmenityMasterListById(@PathVariable("amenityId") Integer amenityId){
		try{
			return iAmenityMasterService.getAmenityMasterListById(amenityId);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
