package com.param.billing.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.global.dto.VaccinationMasterDto;
import com.param.billing.global.service.VaccinationMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping(value="/api/billing")
@SuppressWarnings({"rawtypes"})
public class VaccinationMasterController implements ICommonConstants{
	@Autowired
	private VaccinationMasterService vaccinationMasterService;
	
	@RequestMapping(value = "/saveVaccinationMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveVaccinationMaster(@RequestBody VaccinationMasterDto vaccinationMasterDto){
		
		try {
			return vaccinationMasterService.saveVaccinationMaster(vaccinationMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateVaccinationMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateVaccinationMaster(@RequestBody VaccinationMasterDto vaccinationMasterDto){
		
		try {
			return vaccinationMasterService.updateVaccinationMaster(vaccinationMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateVaccinationMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateVaccinationMasterStatus(@RequestBody VaccinationMasterDto vaccinationMasterDto){
		
		try {
			return vaccinationMasterService.updateVaccinationMasterStatus(vaccinationMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getVaccinationMasterById/{vaccinationId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getVaccinationMasterById(@PathVariable ("vaccinationId") Integer vaccinationId,
									@PathVariable ("organizationId") Integer organizationId,
									@PathVariable ("unitId") Integer unitId){
		
		try {
			return vaccinationMasterService.getVaccinationMasterById(vaccinationId, organizationId, unitId);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getVaccinationMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVaccinationMasterList(@RequestBody VaccinationMasterDto vaccinationMasterDto){
		
		try {
			return vaccinationMasterService.getVaccinationMasterList(vaccinationMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getVaccinationMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getVaccinationMasterCount(@RequestBody VaccinationMasterDto vaccinationMasterDto){
		
		try {
			return vaccinationMasterService.getVaccinationMasterCount(vaccinationMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getActiveVaccinationMaster/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getActiveVaccinationMaster(@PathVariable ("organizationId") Integer organizationId,
										 @PathVariable ("unitId") Integer unitId){
		
		try {
			return vaccinationMasterService.getActiveVaccinationMaster(organizationId, unitId);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	

	@RequestMapping(value = "/getActiveVaccinationMasterByGenderId/{organizationId}/{genderId}", method = RequestMethod.GET)
	public Response getActiveVaccinationMasterByGenderId(@PathVariable ("organizationId") Integer organizationId,  @PathVariable ("genderId") Integer genderId){
		
		try {
			return vaccinationMasterService.getActiveVaccinationMasterByGenderId(organizationId,genderId);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	@RequestMapping(value = "/getRouteOfAdministrationList/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getRouteOfAdministrationList(@PathVariable ("organizationId") Integer organizationId,
										 @PathVariable ("unitId") Integer unitId){
		
		try {
			return vaccinationMasterService.getRouteOfAdministrationList(organizationId, unitId);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
}
