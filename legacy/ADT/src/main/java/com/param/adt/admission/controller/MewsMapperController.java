package com.param.adt.admission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.adt.admission.dto.MewsMapperDto;
import com.param.adt.admission.service.IMewsMapperService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.service.ICabinMasterService;

@RestController
@RequestMapping(value="/adt")
@SuppressWarnings({"rawtypes"})
public class MewsMapperController implements ICommonConstants{
	@Autowired
	private IMewsMapperService iMewsMapperService;
	
	@RequestMapping(value = "/saveMewsMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveMewsMapper(@RequestBody MewsMapperDto mewsMapperDto){
		
		try {
			return iMewsMapperService.saveMewsMapper(mewsMapperDto);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getMewsMapperList/{patientId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getMewsMapperList(@PathVariable ("patientId") Integer patientId,
																  @PathVariable ("organizationId") Integer organizationId,
																  @PathVariable ("unitId") Integer unitId){
		
		try {
			return iMewsMapperService.getMewsMapperList(patientId, organizationId, unitId);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getCountOnMewsMapper/{patientId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getCountOnMewsMapper(@PathVariable ("patientId") Integer patientId,
																  @PathVariable ("organizationId") Integer organizationId,
																  @PathVariable ("unitId") Integer unitId){
		
		try {
			return iMewsMapperService.getCountOnMewsMapper(patientId, organizationId, unitId);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getListOfAllergies/{patientId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getListOfAllergies(@PathVariable ("patientId") Integer patientId,
																  @PathVariable ("organizationId") Integer organizationId,
																  @PathVariable ("unitId") Integer unitId){
		
		try {
			return iMewsMapperService.getListOfAllergies(patientId, organizationId, unitId);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getListOfOrderedProcedure/{patientId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	public Response getListOfOrderedProcedure(@PathVariable ("patientId") Integer patientId,
																  @PathVariable ("organizationId") Integer organizationId,
																  @PathVariable ("unitId") Integer unitId){
		
		try {
			return iMewsMapperService.getListOfOrderedProcedure(patientId, organizationId, unitId);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
