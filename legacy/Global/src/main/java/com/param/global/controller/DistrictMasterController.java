package com.param.global.controller;

import com.param.global.dto.PrefixMasterDto;
import com.param.global.utility.UserComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.DistrictMasterDto;
import com.param.global.dto.StateMasterDto;
import com.param.global.service.IDistrictMasterService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class DistrictMasterController implements ICommonConstants
{
	private static final Logger logger = Logger.getLogger(DistrictMasterController.class);

	@Autowired
	private IDistrictMasterService iDistrictSercive;
	
	@RequestMapping(value="/saveDistrictMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveDistrictMaster(@RequestBody DistrictMasterDto districtMasterDto){
		try{

			return iDistrictSercive.addDistrictMaster(districtMasterDto);
		}catch(Exception e){
			logger.error("Error while saving the district master", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getDistrictMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDistrictMasterList(@RequestBody DistrictMasterDto districtMasterDto){
		try{
			return iDistrictSercive.getDistrictMasterList(districtMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the district master list", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	 
	@RequestMapping(value="/updateDistrictMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateDistrictMaster(@RequestBody DistrictMasterDto districtMasterDto){
		try{
			return iDistrictSercive.updateDistrictMaster(districtMasterDto);
		}catch(Exception e){
			logger.error("Error while updating the district master", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getDistrictById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDistrictById(@RequestBody DistrictMasterDto districtMasterDto){
		try{

			return iDistrictSercive.getDistrictById(districtMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the district by id", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateStatusForDistrict" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForDistrict(@RequestBody DistrictMasterDto districtMasterDto){
		try{

			return iDistrictSercive.updateStatusForDistrict(districtMasterDto);
		}catch(Exception e){
			logger.error("Error while updating the status for district", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getActiveDistrictList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveDistrictList(@RequestBody DistrictMasterDto districtMasterDto){
		try{
			return iDistrictSercive.getActiveDistrictList(districtMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the active district list", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getActiveStateListByCountryId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveStateListByCountryId(@RequestBody StateMasterDto stateMasterDto){
		try{

			return  iDistrictSercive.getActiveStateListByCountryId(stateMasterDto);

		}catch(Exception e){
			logger.error("Error while fetching the active state list by country id", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getDistrictCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDistrictCount(@RequestBody DistrictMasterDto districtMasterDto){
		try{

			return iDistrictSercive.getDistrictCount(districtMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the district count", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getDistrictMasterListByCountryId", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getDistrictMasterListByCountryId(@RequestBody DistrictMasterDto districtMasterDto){
		try{
			return iDistrictSercive.getDistrictMasterListByCountryId(districtMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the district master list by country id", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	
}
