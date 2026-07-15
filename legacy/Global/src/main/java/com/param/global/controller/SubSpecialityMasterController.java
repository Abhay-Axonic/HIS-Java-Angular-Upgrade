package com.param.global.controller;

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
import com.param.global.dto.SubSpecialityMasterDto;
import com.param.global.service.ISpecialityService;
import com.param.global.service.ISubSpecialityService;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@SuppressWarnings({"unchecked","rawtypes"})
@RequestMapping("/adt")
public class SubSpecialityMasterController implements ICommonConstants
{
	final static Logger logger = Logger.getLogger(SubSpecialityMasterController.class);

	@Autowired
    private ISubSpecialityService iSubSpecialityService;
	
	@Autowired
    private ISpecialityService iSpecialityService;
	
	@RequestMapping(value="/saveMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveSpecialityMaster(@RequestBody SpecialityMasterDto specialityMasterDto){
		try{
			
			return iSpecialityService.addSpecialityMaster(specialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	@RequestMapping(value="/saveSubSpecialtiyMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveSubSpecialityMaster(@RequestBody SubSpecialityMasterDto subSpecialityMasterDto){
		try{
			return iSubSpecialityService.addSubSpecialityMaster(subSpecialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value="/getSubSpecialityList",  method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSpecialityList(@RequestBody SubSpecialityMasterDto subSpecialityMasterDto){
		try{
			return iSubSpecialityService.getSubSpecialityMasterList(subSpecialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSubSpecialityById" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSubSpecialityById(@RequestBody SubSpecialityMasterDto subSpecialityMasterDto){
		try{
			
			return iSubSpecialityService.getSubSpecialityById(subSpecialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSubSpecialityBySpecialityList" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSubSpecialityBySpecialityList(@RequestBody SubSpecialityMasterDto subSpecialityMasterDto){
		try{
			
			return iSubSpecialityService.getSubSpecialityBySpecialityList(subSpecialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateStatusForSubSpeciality" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForSubSpeciality(@RequestBody SubSpecialityMasterDto subSpecialityMasterDto){
		try{
			
			return iSubSpecialityService.updateStatusForSubSpeciality(subSpecialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateSubSpecialityMaster" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateSubSpecialityMaster(@RequestBody SubSpecialityMasterDto subspecialityMasterDto){
		try{
			
			return iSubSpecialityService.updateSubSpecialityMaster(subspecialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSubSpecialityCount" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSubSpecialityCount(@RequestBody SubSpecialityMasterDto subspecialityMasterDto){
		try{
			
			return iSubSpecialityService.getSubSpecialityCount(subspecialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value = "/subspeciality/orgId/{orgId}/specialityId/{specialityId}" , method = RequestMethod.GET)
	public @ResponseBody Response getSubSpecialityBySpecialityId(@PathVariable("orgId") int orgId,@PathVariable("specialityId") int specialityId){
		try{
			SubSpecialityMasterDto masterDto = new SubSpecialityMasterDto();
			masterDto.setSpecialityId(specialityId);
			masterDto.setOrganizationId(orgId);
			Response res = iSubSpecialityService.getSubSpecialityBySpecialityId(masterDto);
			masterDto = null;
			return res;
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	@RequestMapping(value = "/getSubspecialityNotInUnit/orgId/{orgId}/unitId/{unitId}/specialityId/{specialityId}" , method = RequestMethod.GET)
	public @ResponseBody Response getSubspecialityNotInUnit(@PathVariable("orgId") int orgId,@PathVariable("unitId") int unitId,@PathVariable("specialityId") int specialityId){
		try{
			
			Response res = iSubSpecialityService.getSubspecialityNotInUnit(orgId,unitId,specialityId);
			return res;
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value = "/subspeciality/orgId/{orgId}/unitId/{unitId}/specialityId/{specialityId}" , method = RequestMethod.GET)
	public @ResponseBody Response getSubSpecialityBySpecialityId(@PathVariable("orgId") int orgId,@PathVariable("unitId") int unitId,@PathVariable("specialityId") int specialityId){
		try{
			SubSpecialityMasterDto masterDto = new SubSpecialityMasterDto();
			masterDto.setSpecialityId(specialityId);
			masterDto.setOrganizationId(orgId);
			masterDto.setUnitId(unitId);
			Response res = iSubSpecialityService.getUnitSubSpecialityBySpecialityId(masterDto);
			masterDto = null;
			return res;
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	/*@RequestMapping(value="/getSubSpecialityBySpecialityId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSubSpecialityBySpecialityId(@RequestBody SubSpecialityMasterDto subspecialityMasterDto){
		try{
			return iSubSpecialityService.getSubSpecialityBySpecialityId(subspecialityMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}*/
	
	@RequestMapping(value="/subSpeciality/{orgId}/{unitId}" , method = RequestMethod.GET)
	public @ResponseBody Response getSubSpecialityList(@PathVariable("orgId") int orgId,@PathVariable("unitId") int unitId){
		try{
			
			return iSubSpecialityService.getSubSpecialityList(orgId,unitId);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/subSpecialityMasterUniversalSearch" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response subSpecialityMasterUniversalSearch(@RequestBody SubSpecialityMasterDto subSpecialityMasterDto){
		try{
			
			return iSubSpecialityService.subSpecialityMasterUniversalSearch(subSpecialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value="/getSubSpecialityMRIandCT" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSubSpecialityMRIandCT(@RequestBody SubSpecialityMasterDto subSpecialityMasterDto){
		try{
			
			return iSubSpecialityService.getSubSpecialityMRIandCT(subSpecialityMasterDto);
		}catch(Exception e){
		logger.error("method failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}

}
