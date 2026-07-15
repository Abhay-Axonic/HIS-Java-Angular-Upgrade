package com.param.global.unit.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.unit.dto.DiscountCategoryTypeMasterDto;
import com.param.global.unit.service.IDiscountTypeMasterService;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

@RestController
@RequestMapping(value="/api/global/unit")
public class DiscountTypeMasterController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(DiscountTypeMasterController.class);

	@Autowired
	IDiscountTypeMasterService iDiscountTypeMasterService;
	
	@RequestMapping(value="/saveDiscountCategory",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDiscountCategory(@RequestBody DiscountCategoryTypeMasterDto dto){
		try{
			return iDiscountTypeMasterService.saveDiscountCategory(dto);
		}catch(Exception e){
			logger.error("saveDiscountCategory failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/updateDiscountCategory",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDiscountCategory(@RequestBody DiscountCategoryTypeMasterDto dto){
		try{
			return iDiscountTypeMasterService.updateDiscountCategory(dto);
		}catch(Exception e){
			logger.error("updateDiscountCategory failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/updateDiscountCategoryStatus",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDiscountCategoryStatus(@RequestBody DiscountCategoryTypeMasterDto dto){
		try{
			return iDiscountTypeMasterService.updateDiscountCategoryStatus(dto);
		}catch(Exception e){
			logger.error("updateDiscountCategoryStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getDiscountCategoryList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDiscountCategoryList(@RequestBody DiscountCategoryTypeMasterDto dto){
		try{
			return iDiscountTypeMasterService.getDiscountCategoryList(dto);
		}catch(Exception e){
			logger.error("getDiscountCategoryList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getDiscountCategoryCount",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDiscountCategoryCount(@RequestBody DiscountCategoryTypeMasterDto dto){
		try{
			return iDiscountTypeMasterService.getDiscountCategoryCount(dto);
		}catch(Exception e){
			logger.error("getDiscountCategoryCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/getDiscountCategoryById",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDiscountCategoryById(@RequestBody DiscountCategoryTypeMasterDto dto){
		try{
			return iDiscountTypeMasterService.getDiscountCategoryById(dto);
		}catch(Exception e){
			logger.error("getDiscountCategoryById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
