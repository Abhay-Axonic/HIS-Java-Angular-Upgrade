package com.param.procurement.workOrder.controller;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.global.common.Response;
import com.param.global.pharmacy.dto.PendingServicesForWorkOrderCreationDto;
import com.param.global.pharmacy.dto.WorkOrderCreationRequestDto;
import com.param.procurement.workOrder.service.workOrderCreation.IServiceWorkOrderCreationService;

@RestController
@RequestMapping(value = "/api/services/workOrder")
@SuppressWarnings("rawtypes")
public class ServiceWorkOrderCreationController {
	
	@Autowired
	IServiceWorkOrderCreationService iServiceWorkOrderCreationService;
	
	@RequestMapping(value = "/pending/list", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody Response getPendingDataForWorkOrderCreation(@RequestBody PendingServicesForWorkOrderCreationDto requestDto)throws ApplicationException
	{
		return iServiceWorkOrderCreationService.getPendingDataForWorkOrderCreation(requestDto);
	}
	
	@RequestMapping(value = "/patient/details", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody Response getDetails(@RequestBody PendingServicesForWorkOrderCreationDto requestDto)throws ApplicationException
	{
		return iServiceWorkOrderCreationService.getDetails(requestDto);
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody Response saveWorkOrder(@RequestBody WorkOrderCreationRequestDto woCreationRequest)throws ApplicationException
	{
		return iServiceWorkOrderCreationService.saveWorkOrderForServices(woCreationRequest);
	}

}
