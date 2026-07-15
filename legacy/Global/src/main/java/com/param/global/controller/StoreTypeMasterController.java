package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.StoreTypeMasterDto;
import com.param.global.service.IStoreTypeMasterService;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings("rawtypes")
public class StoreTypeMasterController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(StoreTypeMasterController.class);

	
	@Autowired
	IStoreTypeMasterService iStoreTypeMasterService;
	
	@RequestMapping(value="/saveStoreType",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveStoreType(@RequestBody StoreTypeMasterDto dto){
		try{
			return iStoreTypeMasterService.saveStoreType(dto);
		}catch(Exception e){
		logger.error("saveStoreType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/updateStoreType",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStoreType(@RequestBody StoreTypeMasterDto dto){
		try{
			return iStoreTypeMasterService.updateStoreType(dto);
		}catch(Exception e){
		logger.error("updateStoreType failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/updateStoreTypeStatus",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStoreTypeStatus(@RequestBody StoreTypeMasterDto dto){
		try{
			return iStoreTypeMasterService.updateStoreTypeStatus(dto);
		}catch(Exception e){
		logger.error("updateStoreTypeStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/getStoreTypeList",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getStoreTypeList(@RequestBody StoreTypeMasterDto dto){
		try{
			return iStoreTypeMasterService.getStoreTypeList(dto);
		}catch(Exception e){
		logger.error("getStoreTypeList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	
	@RequestMapping(value="/getStoreTypeCount",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getStoreTypeCount(@RequestBody StoreTypeMasterDto dto){
		try{
			return iStoreTypeMasterService.getStoreTypeCount(dto);
		}catch(Exception e){
		logger.error("getStoreTypeCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	

	
	@RequestMapping(value="/getStoreTypeById",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getStoreTypeById(@RequestBody StoreTypeMasterDto dto){
		try{
			return iStoreTypeMasterService.getStoreTypeById(dto);
		}catch(Exception e){
		logger.error("getStoreTypeById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}
	

}
