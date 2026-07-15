package com.param.billing.patientType.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.patientType.dto.PatientTypeDto;
import com.param.billing.patientType.dto.PatientTypeGroupWiseDetailsDto;
import com.param.billing.patientType.dto.PatientTypeServiceWiseDetailsDto;
import com.param.billing.patientType.service.IPatientTypeServiceWiseDetailsService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.JsonCheck;
import com.param.global.common.Response;

@RestController
@RequestMapping(value="/api/billing")
@SuppressWarnings({"rawtypes"})
public class PatientTypeServiceWiseDetailsController implements ICommonConstants{

	@Autowired
	IPatientTypeServiceWiseDetailsService iPatientTypeServiceWiseDetailsService;
	
	/*@RequestMapping(value = "/savePatientTypeWiseDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {


	Response<PatientTypeDto, Integer> response = null;
	try {

	JsonCheck.mapJsonToObject(sampleMasterDto, PatientTypeDto.class);
	
	* response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
	* response;
	
	return null;
	} catch (Exception e) {
	
	return new Response(ERROR, null, null, null, null);
	}
	}*/
	
	@RequestMapping(value = "/savePatientTypeWiseDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePatientTypeServiceWiseDetails(@RequestBody PatientTypeDto patientTypeDto){
		
		try {
			return iPatientTypeServiceWiseDetailsService.savePatientTypeServiceWiseDetails(patientTypeDto);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getPatientTypeServiceWiseDetails/{PatientTypeId}/{organizationId}/{unitId}")
	public Response getPatientTypeServiceWiseDetails(@PathVariable("PatientTypeId") int PatientTypeId,
			@PathVariable("organizationId") int organizationId,
			@PathVariable("unitId") int unitId){
		
		try {
			return iPatientTypeServiceWiseDetailsService.getPatientTypeServiceWiseDetails(PatientTypeId,organizationId,unitId);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/savePatientType", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePatientType(@RequestBody PatientTypeDto patientTypeDto){
		
		try {
			return iPatientTypeServiceWiseDetailsService.savePatientType(patientTypeDto);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getPatientType/{organizationId}/{unitId}" , method = RequestMethod.GET)
	public Response getPatientType(@PathVariable("organizationId") int organizationId,
			@PathVariable("unitId") int unitId){
		
		try {

			return iPatientTypeServiceWiseDetailsService.getPatientType(organizationId,unitId);

		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updatePatientTypeStatus" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updatePatientTypeStatus(@RequestBody PatientTypeDto patientTypeDto){
		
		try {
			return iPatientTypeServiceWiseDetailsService.updatePatientTypeStatus(patientTypeDto);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updatePatientType" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updatePatientType(@RequestBody PatientTypeDto patientTypeDto){
		
		try {
			return iPatientTypeServiceWiseDetailsService.updatePatientType(patientTypeDto);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updatePatientTypeGroupStatus" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updatePatientTypeGroupStatus(@RequestBody PatientTypeGroupWiseDetailsDto patientTypeGroupWiseDetailsDto){
		
		try {
			return iPatientTypeServiceWiseDetailsService.updatePatientTypeGroupStatus(patientTypeGroupWiseDetailsDto);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updatePatientTypeServiceStatus" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updatePatientTypeServiceStatus(@RequestBody PatientTypeServiceWiseDetailsDto patientTypeServiceWiseDetailsDto){
		
		try {
			return iPatientTypeServiceWiseDetailsService.updatePatientTypeServiceStatus(patientTypeServiceWiseDetailsDto);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
