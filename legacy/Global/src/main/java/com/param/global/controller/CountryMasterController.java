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
import com.param.global.dto.CountryMasterDto;
import com.param.global.service.ICountryMasterService;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class CountryMasterController implements ICommonConstants
{
  private static final Logger logger = Logger.getLogger(CountryMasterController.class);

  @Autowired
  private ICountryMasterService iCountryService;


	@RequestMapping(value="/saveCountryMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCountryMaster(@RequestBody CountryMasterDto countryMasterDto){
		try{

			return iCountryService.addCountryMaster(countryMasterDto);
		}catch(Exception e){
			logger.error("Error while saving the country master", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getCountryMasterList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCountryMasterList(@RequestBody CountryMasterDto countryMasterDto){
		try{
			return iCountryService.getCountryMasterList(countryMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the country master list", e);
		}
		return new Response(ERROR, null, null, null,null);
	}


	@RequestMapping(value="/updateCountryMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateCountryMaster(@RequestBody CountryMasterDto countryMasterDto){
		try{
			return iCountryService.updateCountryMaster(countryMasterDto);
		}catch(Exception e){
			logger.error("Error while updating the country master", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getCountryById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCountryById(@RequestBody CountryMasterDto countryMasterDto){
		try{

			return iCountryService.getCountryById(countryMasterDto);

		}catch(Exception e){
			logger.error("Error while fetching the country by id", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/updateStatusForCountry" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForCountry(@RequestBody CountryMasterDto countryMasterDto){
		try{

			return iCountryService.updateStatusForCountry(countryMasterDto);
		}catch(Exception e){
			logger.error("Error while updating the status for country", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getActiveCountryList/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getActiveCountryList(@PathVariable("orgId") Integer orgId){
		try{

			return  iCountryService.getActiveCountryList(orgId);

		}catch(Exception e){
			logger.error("Error while fetching the active country list", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getCountryCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getCountryCount(@RequestBody CountryMasterDto countryMasterDto){
		try{

			return iCountryService.getCountryCount(countryMasterDto);
		}catch(Exception e){
			logger.error("Error while fetching the country count", e);
		}
		return new Response(ERROR, null, null, null,null);
	}


	@RequestMapping(value="/getAllCountryMasterList", method = RequestMethod.GET)
	public @ResponseBody Response getAllCountryList(){
		try{
			return iCountryService.getCountryMasterList();
		}catch(Exception e){
			logger.error("Error while fetching all country master list", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
}
