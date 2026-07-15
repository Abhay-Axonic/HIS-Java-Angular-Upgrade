package com.param.pharmacy.nursing.pharmacy.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.param.global.pharmacy.dto.BillableConsumptionSave;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionDetailItem;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionDetails;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionRequest;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionResponse;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.ConsumptionIds;
import com.param.pharmacy.nursing.pharmacy.service.returnBillableConsumption.IReturnBillableConsumptionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/nursing/pharmacy/return/billable/consumption")
public class ReturnBillableConsumptionController {

	@Autowired
	private IReturnBillableConsumptionService returnBillableConsumptionService;

	private static final Logger logger = Logger.getLogger(ReturnBillableConsumptionController.class);

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = CommonDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try
		{
			return new ResponseEntity<Object>(returnBillableConsumptionService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get return billable consumption list ", response = BillableConsumptionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> returnBillableConsumptionSearch(@RequestBody BillableConsumptionRequest request) {
		try
		{
			return new ResponseEntity<Object>(returnBillableConsumptionService.returnBillableConsumptionSearch(request), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get return billable consumption details ", response = BillableConsumptionDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> returnBillableConsumptionDetails(@PathVariable("id") Integer id,@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(returnBillableConsumptionService.returnBillableConsumptionDetails(id,unitId), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save return billable consumption", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> returnBillableConsumptionSave(@RequestBody BillableConsumptionSave request) {
		try
		{
			return new ResponseEntity<Object>(returnBillableConsumptionService.returnBillableConsumptionSave(request), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}

	@RequestMapping(value = "/search/new", method = RequestMethod.POST)
	@ApiOperation(value = "Search return billable consumption issue list ", response = BillableConsumptionDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> returnBillableConsumptionSearchIssue(@RequestBody BillableConsumptionRequest request) {
		try
		{
			return new ResponseEntity<Object>(returnBillableConsumptionService.returnBillableConsumptionSearchIssue(request), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/add/new", method = RequestMethod.POST)
	@ApiOperation(value = "Add return billable consumption list ", response = BillableConsumptionDetailItem.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> returnBillableConsumptionAdd(@RequestBody ConsumptionIds ids) {
		// before multiple @PathVariable("id") Integer id
		try
		{
			return new ResponseEntity<Object>(returnBillableConsumptionService.returnBillableConsumptionAdd(ids), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}