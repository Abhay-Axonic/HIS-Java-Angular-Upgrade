package com.param.global.controller;

import com.param.global.dto.PrefixMasterDto;
import com.param.global.utility.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AreaMasterDto;
import com.param.global.dto.CityMasterDto;
import com.param.global.service.IAreaMasterService;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class AreaMasterController implements ICommonConstants
{
	final static Logger logger = Logger.getLogger(AreaMasterController.class);

	@Autowired
	private IAreaMasterService iAreaMaster;
	
	@RequestMapping(value="/saveAreaMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveAreaMaster(@RequestBody AreaMasterDto areaMasterDto){
		try{
			
			return iAreaMaster.addAreaMaster(areaMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAreaMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAreaMasterList(@RequestBody AreaMasterDto areaMasterDto){
		try{
			return iAreaMaster.getAreaMasterList(areaMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	 
	@RequestMapping(value="/updateAreaMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateAreaMaster(@RequestBody AreaMasterDto areaMasterDto){
		try{
			return iAreaMaster.updateAreaMaster(areaMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAreaById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAreaById(@RequestBody AreaMasterDto areaMasterDto){
		try{
			
			return iAreaMaster.getAreaById(areaMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateStatusForArea" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForArea(@RequestBody AreaMasterDto areaMasterDto){
		try{
			
			return iAreaMaster.updateStatusForArea(areaMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getActiveAreaList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveCityList(){
		try{
			return iAreaMaster.getActiveAreaList();
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getActiveCityListByDistrictId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveCityListByDistrictId(@RequestBody CityMasterDto cityMasterDto){
		try{
				return iAreaMaster.getActiveCityListByDistrictId(cityMasterDto);

		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getAreaCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getAreaCount(@RequestBody AreaMasterDto areaMasterDto){
		try{
			
			return iAreaMaster.getAreaCount(areaMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	/*@RequestMapping(value="/getDataByPinCode/{code}/{orgId}" , method = RequestMethod.GET)
	public @ResponseBody Response getDataByPincode(@PathVariable("code") String code,@PathVariable("orgId") Integer orgId){
		try{
			return iAreaMaster.getDataByPincode(code,orgId);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}*/

	@RequestMapping(value="/getDataByPinCode/{code}/{orgId}", method=RequestMethod.GET)
	public @ResponseBody Response getDataByPincode(@PathVariable("code") String code,@PathVariable("orgId") Integer orgId) {
		try {
			return iAreaMaster.getDataByPincode(code,orgId);
		}catch (Exception e) {
		logger.error("method failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
}
