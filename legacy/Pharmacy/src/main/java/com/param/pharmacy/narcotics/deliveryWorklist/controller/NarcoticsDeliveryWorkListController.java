package com.param.pharmacy.narcotics.deliveryWorklist.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.narcotics.deliveryWorklist.service.INarcoticsDeliveryWorklistService;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsDetails;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsRequest;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsResponse;
import com.param.pharmacy.narcotics.issueWorklist.dto.UpdateCarrier;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/narcotics/delivery/worklist")
public class NarcoticsDeliveryWorkListController {

	@Autowired
	private INarcoticsDeliveryWorklistService narcoticsDeliveryWorklistService;

	private static final Logger logger = Logger.getLogger(NarcoticsDeliveryWorkListController.class);

	@RequestMapping(value = "/dropdowns/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = CommonDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(narcoticsDeliveryWorklistService.getDropdownList(unitId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get delivery worklist ", response = NarcoticsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> narcoticsDeliveryWorklistSearch(@RequestBody NarcoticsRequest narcoticsRequest) {
		try
		{
			return new ResponseEntity<Object>(narcoticsDeliveryWorklistService.narcoticsDeliveryWorklistSearch(narcoticsRequest), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{orgId}/{unitId}/{storeId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get delivery worklist details ", response = NarcoticsDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> narcoticsDeliveryWorklistDetails(@PathVariable("id") Long id,@PathVariable Integer storeId,@PathVariable Integer unitId,@PathVariable Integer orgId) {
		try
		{
			return new ResponseEntity<Object>(narcoticsDeliveryWorklistService.narcoticsDeliveryWorklistDetails(id,unitId,storeId,orgId), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save delivery worklist", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> narcoticsDeliveryWorklistSave(@RequestBody UpdateCarrier updateCarrier) {
		try
		{
			return new ResponseEntity<Object>(narcoticsDeliveryWorklistService.narcoticsDeliveryWorklistSave(updateCarrier), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
}