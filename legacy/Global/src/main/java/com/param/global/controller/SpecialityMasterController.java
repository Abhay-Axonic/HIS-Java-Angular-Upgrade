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
import com.param.global.dto.SpecialityMasterDto;
import com.param.global.service.ISpecialityService;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@SuppressWarnings({"rawtypes","unchecked"})
@RequestMapping("/adt")
public class SpecialityMasterController implements ICommonConstants
{
	final static Logger logger = Logger.getLogger(SpecialityMasterController.class);
 
	
	@Autowired
    private ISpecialityService iSpecialityService;
	
	
	@RequestMapping(value="/saveSpecialtiyMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveSpecialityMaster(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			
			return iSpecialityService.addSpecialityMaster(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value="/getSpecialityList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSpecialityList(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			return iSpecialityService.getSpecialityMasterList(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value="/getSpecialityListForSubSpeciality/{orgId}", method = RequestMethod.GET)
	public @ResponseBody Response getSpecialityListForSubSpeciality(@PathVariable("orgId") int orgId){
		try{
			return iSpecialityService.getSpecialityListForSubSpeciality(orgId);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value="/updateSpecialityMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateSpecialityMaster(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			return iSpecialityService.updateSpecialityMaster(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSpecialityById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSpecialityById(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			
			return iSpecialityService.getSpecialityById(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateStatus" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatus(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			
			return iSpecialityService.updateStatus(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getActiveSpecialityList", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveSpecialityList(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			return  iSpecialityService.getActiveSpecialityMasterList(specialityMasterDto);

		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSpecialityCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSpecialityCount(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			return iSpecialityService.getSpecialityCount(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	@RequestMapping(value="/getOrgActiveSpecialityList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getOrgActiveSpecialityList(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			return iSpecialityService.getOrgActiveSpecialityList(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

	@RequestMapping(value="/getSpecialityNameById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSpecialityNameById(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			
			return iSpecialityService.getSpecialityNameById(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getStoreBySpecialtyId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getStoreBySpecialtyId(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			
			return iSpecialityService.getStoreBySpecialtyId(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSpecialityListOfActiveInactive" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSpecialityListOfActiveInactive(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			
			return iSpecialityService.getSpecialityListOfActiveInactive(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/departmentMasterUniversalSearch" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response departmentMasterUniversalSearch(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			
			return iSpecialityService.departmentMasterUniversalSearch(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	@RequestMapping(value="/getActiveSpecialityListForModality", method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveSpecialityListForModality(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			return iSpecialityService.getActiveSpecialityListForModality(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}


}

