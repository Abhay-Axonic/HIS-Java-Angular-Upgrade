package com.param.billing.global.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.InsuranceMasterDto;
import com.param.billing.global.transaction.service.IInsuranceMasterService;

@RestController
@RequestMapping(value="api/unit/billing")
@SuppressWarnings({ "rawtypes"})
public class InsuranceMasterController implements ICommonConstants{

	@Autowired
	private IInsuranceMasterService iInsuranceMasterService;
	
	@RequestMapping(value = "/saveInsuranceMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  Response saveInsuranceMaster(@RequestBody InsuranceMasterDto insuranceMasterDto) {
		try {
			return iInsuranceMasterService.saveInsuranceMaster(insuranceMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateInsuranceMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  Response updateInsuranceMaster(@RequestBody InsuranceMasterDto insuranceMasterDto) {
		try {
			 return iInsuranceMasterService.updateInsuranceMaster(insuranceMasterDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/updateInsuranceMasterStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  Response updateInsuranceMasterStatus(@RequestBody InsuranceMasterDto insuranceMasterDto) {
		try {
			return iInsuranceMasterService.updateInsuranceMasterStatus(insuranceMasterDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getInsuranceMasterById/{insuranceId}/{orgId}/{unitId}", method = RequestMethod.GET)
	public @ResponseBody Response getInsuranceMasterById(@PathVariable ("insuranceId") Integer insuranceId,
														  @PathVariable ("orgId") Integer orgId,
														  @PathVariable ("unitId") Integer unitId) {
		try {
			 return iInsuranceMasterService.getInsuranceMasterById(insuranceId, orgId, unitId);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getInsuranceMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public  Response getInsuranceMasterList(@RequestBody InsuranceMasterDto insuranceMasterDto) {
		try {
			return iInsuranceMasterService.getInsuranceMasterList(insuranceMasterDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getInsuranceMasterCount", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getInsuranceMasterCount(@RequestBody InsuranceMasterDto insuranceMasterDto) {
		try {
			return iInsuranceMasterService.getInsuranceMasterCount(insuranceMasterDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getActiveInsuranceMasterList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getActiveInsuranceMasterList(@RequestBody InsuranceMasterDto insuranceMasterDto) {
		try {
			return iInsuranceMasterService.getActiveInsuranceMasterList(insuranceMasterDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getMappedGradeToInsuerance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMappedGradeToInsuerance(@RequestBody InsuranceMasterDto insuranceMasterDto) {
		try {
			return iInsuranceMasterService.getMappedGradeToInsuerance(insuranceMasterDto);
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	 @RequestMapping(value = "/insurance/image/{orgId}/{unitId}/{insuranceId}", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public @ResponseBody Response savePatientImage(@PathVariable("orgId") int orgId,
                           @PathVariable("unitId") int unitId,
                           @PathVariable("insuranceId") int insuranceId,
                           @RequestParam("file") MultipartFile inputFile)
         {
	      try
	      {
	         return iInsuranceMasterService.saveInsuranceImage(orgId,unitId,insuranceId,inputFile);
	       }catch(Exception e)
	      {
	           
	        	throw e;
	      }
	     // return new Response<>(ERROR, null, null, null, null);
       }
}
