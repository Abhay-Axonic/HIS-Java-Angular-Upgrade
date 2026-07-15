package com.param.global.unit.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;
import com.param.global.unit.dto.VisitorPassMapperDto;
import com.param.global.unit.service.IVisitorPassMapperService;
import com.param.global.common.Response;
@RestController
@RequestMapping(value="/api/unit/")
@SuppressWarnings("rawtypes")
public class VisitorPassMapperController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(VisitorPassMapperController.class);

	@Autowired
	IVisitorPassMapperService iVisitorPassMapperService;
	
	
	@RequestMapping(value="/saveVisitorPassMapper",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveVisitorPassMapper(@RequestBody VisitorPassMapperDto dto){
		try{
			return iVisitorPassMapperService.saveVisitorPassMapper(dto);
		}catch(Exception e){
			logger.error("saveVisitorPassMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/updateVisitorPassMapper",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateVisitorPassMapper(@RequestBody VisitorPassMapperDto dto){
		try{
			return iVisitorPassMapperService.updateVisitorPassMapper(dto);
		}catch(Exception e){
			logger.error("updateVisitorPassMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/updateVisitorPassMapperStatus",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateVisitorPassMapperStatus(@RequestBody VisitorPassMapperDto dto){
		try{
			return iVisitorPassMapperService.updateVisitorPassMapperStatus(dto);
		}catch(Exception e){
			logger.error("updateVisitorPassMapperStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/getVisitorPassMapperList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVisitorPassMapperList(@RequestBody VisitorPassMapperDto dto){
		try{
			return iVisitorPassMapperService.getVisitorPassMapperList(dto);
		}catch(Exception e){
			logger.error("getVisitorPassMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/getVisitorPassMapperCount",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVisitorPassMapperCount(@RequestBody VisitorPassMapperDto dto){
		try{
			return iVisitorPassMapperService.getVisitorPassMapperCount(dto);
		}catch(Exception e){
			logger.error("getVisitorPassMapperCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/getVisitorPassMapperById",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVisitorPassMapperById(@RequestBody VisitorPassMapperDto dto){
		try{
			return iVisitorPassMapperService.getVisitorPassMapperById(dto);
		}catch(Exception e){
			logger.error("getVisitorPassMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	@RequestMapping(value="/getVisitorPassCountByBedCategoryByVisitorPassIdByPassId",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVisitorPassCountByBedCategoryByVisitorPassIdByPassId(@RequestBody VisitorPassMapperDto dto){
		try{
			return iVisitorPassMapperService.getVisitorPassCountByBedCategoryByVisitorPassIdByPassId(dto);
		}catch(Exception e){
			logger.error("getVisitorPassCountByBedCategoryByVisitorPassIdByPassId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/getVisitorPassMapperListByBedCategoryNVisitType",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVisitorPassMapperListByBedCategoryNVisitType(@RequestBody VisitorPassMapperDto dto){
		try{
			return iVisitorPassMapperService.getVisitorPassMapperListByBedCategoryNVisitType(dto);
		}catch(Exception e){
			logger.error("getVisitorPassMapperListByBedCategoryNVisitType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
}
