package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.DepartmentNursingStationMapperDto;
import com.param.global.service.IDepartmentNursingStationMapperService;

@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/api/global")
public class DepartmentNursingStationMapperController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(DepartmentNursingStationMapperController.class);

	@Autowired
	private IDepartmentNursingStationMapperService iDepartmentNursingStationMapperService;
	
	
	@RequestMapping(value = "/saveDepartmentNursingStationMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveDepartmentNursingStationMapper(@RequestBody DepartmentNursingStationMapperDto departmentNursingStationMapperDto){
		try{
			return iDepartmentNursingStationMapperService.saveDepartmentNursingStationMapper(departmentNursingStationMapperDto);

		}catch(Exception e){
			logger.error("Error while saving the department nursing station mapper", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/updateDepartmentNursingStationMapper" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateDepartmentNursingStationMapper(@RequestBody DepartmentNursingStationMapperDto departmentNursingStationMapperDto){
		try{
			
			return iDepartmentNursingStationMapperService.updateDepartmentNursingStationMapper(departmentNursingStationMapperDto);

		}catch(Exception e){
			logger.error("Error while updating the department nursing station mapper", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/updateDepartmentNursingStationMapperStatus/{specialityId}/{organisationId}/{unitId}/{status}" , method = RequestMethod.GET)
	public Response updateDepartmentNursingStationMapperStatus(@PathVariable ("specialityId") Integer specialityId,
														@PathVariable ("organisationId") Integer organisationId,
														@PathVariable ("unitId") Integer unitId,
														@PathVariable ("status") Character status){
		try{
			return iDepartmentNursingStationMapperService.updateDepartmentNursingStationMapperStatus(specialityId, organisationId, unitId, status);
		}catch(Exception e){
			logger.error("Error while updating the department nursing station mapper status", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	
	@RequestMapping(value = "/getDepartmentNursingStationMapperlist" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE )
	public Response getDepartmentNursingStationMapperlist(@RequestBody DepartmentNursingStationMapperDto departmentNursingStationMapperDto){
		try{
			return iDepartmentNursingStationMapperService.getDepartmentNursingStationMapperlist(departmentNursingStationMapperDto);
		}catch(Exception e){
			logger.error("Error while fetching the department nursing station mapper list", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getDepartmentNursingStationMapperCount/{organisationId}/{unitId}" , method = RequestMethod.GET )
	public Response getDepartmentNursingStationMapperCount(@PathVariable ("organisationId") Integer organisationId,
											  @PathVariable ("unitId") Integer unitId){
		try{
			return iDepartmentNursingStationMapperService.getDepartmentNursingStationMapperCount(organisationId, unitId);
		}catch(Exception e){
			logger.error("Error while fetching the department nursing station mapper count", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveDepartmentNursingStationMapper/{organisationId}/{unitId}" , method = RequestMethod.GET )
	public Response getActiveDepartmentNursingStationMapper(@PathVariable ("organisationId") Integer organisationId,
												@PathVariable ("unitId") Integer unitId){
		try{
				return iDepartmentNursingStationMapperService.getActiveDepartmentNursingStationMapper(organisationId, unitId);
		}catch(Exception e){
			logger.error("Error while fetching the active department nursing station mapper", e);
			return new Response<>(ERROR, null, null, null, null);
		}
	}
	
	@RequestMapping(value = "/getDepartmentNursingStationMapperById/{specialityId}/{organisationId}/{unitId}" , method = RequestMethod.GET)
	public Response getDepartmentNursingStationMapperById(@PathVariable ("specialityId") Integer specialityId,
														@PathVariable ("organisationId") Integer organisationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iDepartmentNursingStationMapperService.getDepartmentNursingStationMapperById(specialityId, organisationId, unitId);
		}catch(Exception e){
			logger.error("Error while fetching the department nursing station mapper by id", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDetailedListNursingStation/{specialityId}/{organisationId}/{unitId}", method = RequestMethod.GET)
	public Response getDetailedListNursingStation(@PathVariable ("specialityId") Integer specialityId,
														@PathVariable ("organisationId") Integer organisationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iDepartmentNursingStationMapperService.getDetailedListNursingStation(specialityId, organisationId, unitId);
		}catch(Exception e){
			logger.error("Error while fetching the detailed list of nursing stations", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getDeptListByNursingStationId/{nursingStationId}/{organisationId}/{unitId}", method = RequestMethod.GET)
	public Response getDeptListByNursingStationId(@PathVariable ("nursingStationId") Integer nursingStationId,
														@PathVariable ("organisationId") Integer organisationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iDepartmentNursingStationMapperService.getDeptListByNursingStationId(nursingStationId, organisationId, unitId);
		}catch(Exception e){
			logger.error("Error while fetching the department list by nursing station id", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
	@RequestMapping(value = "/getWardListForER/{organisationId}/{unitId}", method = RequestMethod.GET)
	public Response getWardListForER(@PathVariable ("organisationId") Integer organisationId,
														@PathVariable ("unitId") Integer unitId){
		try{
			return iDepartmentNursingStationMapperService.getWardListForER(organisationId, unitId);
		}catch(Exception e){
			logger.error("Error while fetching the ward list for ER", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}
	
}
	
	

