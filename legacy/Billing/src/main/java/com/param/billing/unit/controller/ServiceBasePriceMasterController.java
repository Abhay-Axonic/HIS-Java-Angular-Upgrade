package com.param.billing.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.unit.service.IServiceBasePriceMasterService;
import com.param.global.billing.dto.ServiceBasePriceMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@SuppressWarnings({ "rawtypes", "unchecked" })
@RequestMapping(value="api/unit/billing")
public class ServiceBasePriceMasterController implements ICommonConstants{

	@Autowired
	IServiceBasePriceMasterService iServiceBasePriceMasterService; 
	
	@RequestMapping(value="/getServiceBasePriceList", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getServiceBasePriceList(@RequestBody ServiceBasePriceMasterDto serviceBasePriceMasterDto ){
		try{
			return iServiceBasePriceMasterService.getServiceBasePriceList(serviceBasePriceMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/getServiceBasePriceListById/{serviceBasePriceId}", method=RequestMethod.GET)
	public @ResponseBody Response getServiceBasePriceListById(@PathVariable("serviceBasePriceId") Integer serviceBasePriceId ){
		try{
			return iServiceBasePriceMasterService.getServiceBasePriceListById(serviceBasePriceId);
		}catch(Exception e){
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/updateServiceBasePriceMaster", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateServiceBasePriceMaster(@RequestBody ServiceBasePriceMasterDto serviceBasePriceMasterDto ){
		try{
			return iServiceBasePriceMasterService.updateServiceBasePriceMaster(serviceBasePriceMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/updateServiceBasePriceMasterStatus", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateServiceBasePriceMasterStatus(@RequestBody ServiceBasePriceMasterDto serviceBasePriceMasterDto ){
		try{
			return iServiceBasePriceMasterService.updateServiceBasePriceMasterStatus(serviceBasePriceMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value="/countServiceBasePriceMaster", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response countServiceBasePriceMaster(@RequestBody ServiceBasePriceMasterDto serviceBasePriceMasterDto ){
		try{
			return iServiceBasePriceMasterService.countServiceBasePriceMaster(serviceBasePriceMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value="/getServiceBasePriceListSearch", method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getServiceBasePriceListSearch(@RequestBody ServiceBasePriceMasterDto serviceBasePriceMasterDto ){
		try{
			return iServiceBasePriceMasterService.getServiceBasePriceListSearch(serviceBasePriceMasterDto);
		}catch(Exception e){
			
		}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
	}
}
