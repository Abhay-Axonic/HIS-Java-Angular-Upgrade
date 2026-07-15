package com.param.billing.unit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.global.transaction.dto.CancelOrderedServicesDto;
import com.param.billing.unit.dto.TPeriodicityServiceOrdersDto;
import com.param.billing.unit.service.ITPeriodicityServiceOrdersService;
import com.param.global.common.Response;

@RestController
@RequestMapping(value = "/api/periodicity")
@SuppressWarnings({"rawtypes"})
public class TPeriodicityServiceOrdersController {

	@Autowired
	private ITPeriodicityServiceOrdersService iTPeriodicityServiceOrdersService;
	
	/**
	 * METHOD TO ORDER PERIODICITY SERVICES
	 * @param listPserv
	 * @return
	 */
	@RequestMapping(value = "/order" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response orderPeriodicityService(@RequestBody List<TPeriodicityServiceOrdersDto> listPserv){
		try{
			return iTPeriodicityServiceOrdersService.orderTPeriodicityServices(listPserv);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	/**
	 * TO GET PERIODICITY ORDERS BY ADMISSION ID
	 * @param admissionId
	 * @return
	 */
	@RequestMapping(value = "/order/{admissionId}" , method = RequestMethod.GET)
	public Response orderPeriodicityService(@PathVariable Integer admissionId){
		try{
			return iTPeriodicityServiceOrdersService.getTPeriodicityServicesByAdmissionId(admissionId);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/order/cancel" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response cancelOrderedPeriodicityService(@RequestBody CancelOrderedServicesDto cancelOrderedServicesDto ){
		try{
			return iTPeriodicityServiceOrdersService.cancelOrderedPeriodicityService(cancelOrderedServicesDto);
		}catch(Exception e){
			
			throw e;
		}
	}
}
