package com.param.billing.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.unit.dto.InsuranceTPAMapperDto;
import com.param.billing.unit.service.IInsuranceTPAMapperService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@RestController
@RequestMapping(value="api/billing")
@SuppressWarnings({"rawtypes","unchecked"})
public class InsuranceTPAMapperController implements ICommonConstants{
	@Autowired
	IInsuranceTPAMapperService iInsuranceTPAMapperService;
	
	
	@RequestMapping(value="/saveInsuranceTPAMapper",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response  saveInsuranceTPAMapper(@RequestBody InsuranceTPAMapperDto insuranceTPAMapperDto) throws ApplicationException
	 {
		 try{
			return iInsuranceTPAMapperService.saveInsuranceTPAMapper(insuranceTPAMapperDto);
		 }catch(Exception e){
			 
		 }
		 return new Response(ERROR,COMMON_ERROR_CODE,COMMON_ERROR_MESSAGE,null,null);
	 }
	@RequestMapping(value="/getInsuranceTPAMapperList", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getInsuranceTPAMapperList(@RequestBody InsuranceTPAMapperDto insuranceTPAMapperDto) {
		try {
			
			return iInsuranceTPAMapperService.getInsuranceTPAMapperList(insuranceTPAMapperDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getInsuranceTPAMapperListCount", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getInsuranceTPAMapperListCount(@RequestBody InsuranceTPAMapperDto insuranceTPAMapperDto) {
		try {
			
			return iInsuranceTPAMapperService.getInsuranceTPAMapperListCount(insuranceTPAMapperDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/updateInsuranceTPAMapperStatus", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response updateInsuranceTPAMapperStatus(@RequestBody InsuranceTPAMapperDto insuranceTPAMapperDto) {
		try {
			
			return iInsuranceTPAMapperService.updateInsuranceTPAMapperStatus(insuranceTPAMapperDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getInsuranceTPAMapperListById", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getInsuranceTPAMapperListById(@RequestBody InsuranceTPAMapperDto insuranceTPAMapperDto) {
		try {
			
			return iInsuranceTPAMapperService.getInsuranceTPAMapperListById(insuranceTPAMapperDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getInsuranceListNotMappedToMapper", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getInsuranceListNotMappedToMapper(@RequestBody InsuranceTPAMapperDto insuranceTPAMapperDto) {
		try {
			
			return iInsuranceTPAMapperService.getInsuranceListNotMappedToMapper(insuranceTPAMapperDto);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
