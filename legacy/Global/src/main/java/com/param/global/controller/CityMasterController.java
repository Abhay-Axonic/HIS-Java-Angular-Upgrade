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

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CityMasterDto;
import com.param.global.dto.DistrictMasterDto;
import com.param.global.service.ICityMasterService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class CityMasterController implements ICommonConstants
{
	private static final Logger logger = Logger.getLogger(CityMasterController.class);

	@Autowired
	private ICityMasterService iCityMaser;

	@RequestMapping(value="/saveCityMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCityMaster(@RequestBody CityMasterDto cityMasterDto){
		try{

			return iCityMaser.addCityMaster(cityMasterDto);
		}catch(Exception e){
			logger.error("Error while saving the city master", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getCityMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCityMasterList(@RequestBody CityMasterDto cityMasterDto){
		try{
			return iCityMaser.getCityMasterList(cityMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the city master list", e);
		}
		return new Response(ERROR, null, null, null,null);
	}


	@RequestMapping(value="/updateCityMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCityMaster(@RequestBody CityMasterDto citytMasterDto){
		try{
			return iCityMaser.updateCityMaster(citytMasterDto);
		}catch(Exception e){
			logger.error("Error while updating the city master", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getCityById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCityById(@RequestBody CityMasterDto cityMasterDto){
		try{

			return iCityMaser.getCityById(cityMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the city by id", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/updateStatusForCity" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForCity(@RequestBody CityMasterDto cityMasterDto){
		try{

			return iCityMaser.updateStatusForDistrict(cityMasterDto);
		}catch(Exception e){
			logger.error("Error while updating the status for city", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getActiveCityList", method = RequestMethod.GET)
	public @ResponseBody Response getActiveCityList(){
		try{
			return iCityMaser.getActiveCityList();
		}catch(Exception e){
			logger.error("Error while fetching the active city list", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getActiveDistrictListByStateId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveDistrictListByStateId(@RequestBody DistrictMasterDto districtMasterDto){
		try{

			return  iCityMaser.getActiveDistrictListByStateId(districtMasterDto);

		}catch(Exception e){
			logger.error("Error while fetching the active district list by state id", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getCityCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCityCount(@RequestBody CityMasterDto cityMasterDto){
		try{

			return iCityMaser.getCityCount(cityMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the city count", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getAllCityMasterList/{stateId}", method = RequestMethod.GET)
	public @ResponseBody Response getCityMasterList(@PathVariable("stateId") int stateId){
		try{
			CityMasterDto  cityMasterDto= new CityMasterDto();
			cityMasterDto.setStateId(stateId);
			return iCityMaser.getCityByStateIs(cityMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the city master list by state id", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
}
