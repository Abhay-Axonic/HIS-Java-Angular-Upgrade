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
import com.param.global.unit.dto.PassTypeMasterDto;
import com.param.global.unit.service.IPassTypeMasterService;
import com.param.global.common.Response;

@RestController
@RequestMapping(value="/api/unit/visitor")
@SuppressWarnings("rawtypes")
public class VisitorPassTypeMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(VisitorPassTypeMasterController.class);

	@Autowired
	IPassTypeMasterService iPassTypeMasterService;
	

	@RequestMapping(value="/savePassTypeMaster",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePassTypeMaster(@RequestBody PassTypeMasterDto dto){
		try{
			return iPassTypeMasterService.savePassTypeMaster(dto);
		}catch(Exception e){
			logger.error("savePassTypeMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/updatePassTypeMaster",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDiscountCategory(@RequestBody PassTypeMasterDto dto){
		try{
			return iPassTypeMasterService.updateDiscountCategory(dto);
		}catch(Exception e){
			logger.error("updateDiscountCategory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/updatePassTypeMasterStatus",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updatePassTypeMasterStatus(@RequestBody PassTypeMasterDto dto){
		try{
			return iPassTypeMasterService.updatePassTypeMasterStatus(dto);
		}catch(Exception e){
			logger.error("updatePassTypeMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/getPassTypeMasterList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPassTypeMasterList(@RequestBody PassTypeMasterDto dto){
		try{
			return iPassTypeMasterService.getPassTypeMasterList(dto);
		}catch(Exception e){
			logger.error("getPassTypeMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/getPassTypeMasterCount",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPassTypeMasterCount(@RequestBody PassTypeMasterDto dto){
		try{
			return iPassTypeMasterService.getPassTypeMasterCount(dto);
		}catch(Exception e){
			logger.error("getPassTypeMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/getPassTypeMasterById",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDiscountCategoryById(@RequestBody PassTypeMasterDto dto){
		try{
			return iPassTypeMasterService.getPassTypeMasterById(dto);
		}catch(Exception e){
			logger.error("getDiscountCategoryById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	@RequestMapping(value="/getActivePassTypeMasterList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getActivePassTypeMasterList(@RequestBody PassTypeMasterDto dto){
		try{
			return iPassTypeMasterService.getActivePassTypeMasterList(dto);
		}catch(Exception e){
			logger.error("getActivePassTypeMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

}
