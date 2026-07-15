package com.param.adt.master.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.unit.dto.UnitSpecialtyMapperDto;
import com.param.adt.master.unit.service.IUnitSpecialityService;
import com.param.global.dto.SpecialityMasterDto;

@Controller
@RequestMapping("/adt")
@SuppressWarnings({"rawtypes","unchecked"})
public class UnitSpecialityController implements ICommonConstants
{
	
	@Autowired
	IUnitSpecialityService iUnitSpecialityService;
	
	
	@RequestMapping(value="/saveUnitSpecialtiy" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveUnitSpecialtiy(@RequestBody UnitSpecialtyMapperDto unitSpecialtyMapperDto){
		try{
			
			return iUnitSpecialityService.saveUnitSpecialtiy(unitSpecialtyMapperDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value="/getUnitSpecialityList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getUnitSpecialityList(@RequestBody UnitSpecialtyMapperDto unitSpecialtyMapperDto){
		try{
			return iUnitSpecialityService.getUnitSpecialityList(unitSpecialtyMapperDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getUnitSpecialityCount", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getUnitSpecialityCount(@RequestBody UnitSpecialtyMapperDto unitSpecialtyMapperDto){
		try{
			return iUnitSpecialityService.getUnitSpecialityCount(unitSpecialtyMapperDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/updateStatusForUnitSpecialityList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateStatusForUnitSpecialityList(@RequestBody UnitSpecialtyMapperDto unitSpecialtyMapperDto){
		try{
			
			return iUnitSpecialityService.updateStatusForUnitSpecialityList(unitSpecialtyMapperDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSpecialityListByDoctorId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSpecialityListByDoctorId(@RequestBody UnitSpecialtyMapperDto unitSpecialtyMapperDto){
		try{
			
			return iUnitSpecialityService.getSpecialityListByDoctorId(unitSpecialtyMapperDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getSpecialityListByDoctorIdByOrg", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSpecialityListByDoctorIdByOrg(@RequestBody UnitSpecialtyMapperDto unitSpecialtyMapperDto){
		try{
			
			return iUnitSpecialityService.getSpecialityListByDoctorIdByOrg(unitSpecialtyMapperDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/getUnitSpecialityById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getUnitSpecialityById(@RequestBody UnitSpecialtyMapperDto unitSpecialtyMapperDto){
		try{
			
			return iUnitSpecialityService.getUnitSpecialityById(unitSpecialtyMapperDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	@RequestMapping(value="/getActiveUnitSpecialityList", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveUnitSpecialityList(@RequestBody UnitSpecialtyMapperDto unitSpecialtyMapperDto){
		try{
			return iUnitSpecialityService.getActiveUnitSpecialityList(unitSpecialtyMapperDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/unitDepartmentMasterUniversalSearch", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response unitDepartmentMasterUniversalSearch(@RequestBody UnitSpecialtyMapperDto unitSpecialtyMapperDto){
		try{
			return iUnitSpecialityService.unitDepartmentMasterUniversalSearch(unitSpecialtyMapperDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value="/updateUnitSpecialtyMapperDetails" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateUnitSpecialtyMapperDetails(@RequestBody UnitSpecialtyMapperDto unitSpecialtyMapperDto){
		try{
			return iUnitSpecialityService.updateUnitSpecialtyMapperDetails(unitSpecialtyMapperDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
	
	
	@RequestMapping(value="/getSpecialityListByUserIdByUnitId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getSpecialityListByUserIdByUnitId(@RequestBody UnitSpecialtyMapperDto unitSpecialtyMapperDto){
		try{
			
			return iUnitSpecialityService.getSpecialityListByUserIdByUnitId(unitSpecialtyMapperDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, null, null, null,null);
	}
}
