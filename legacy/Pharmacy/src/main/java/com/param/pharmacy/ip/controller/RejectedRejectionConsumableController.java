package com.param.pharmacy.ip.controller;


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

import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.rejectedRejection.RejectedRejectionDetails;
import com.param.pharmacy.ip.dto.rejectedRejectionConsumable.RejectedRejectionConsumableSearch;
import com.param.pharmacy.ip.service.rejectedRejectionConsumable.IRejectedRejectionConsumableService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/ip/pending/rejected/rejection/consumable")
public class RejectedRejectionConsumableController {

	private static final Logger logger = Logger.getLogger(RejectedRejectionConsumableController.class);

	@Autowired
	private IRejectedRejectionConsumableService rejectedRejectionConsumableService;
	
	@RequestMapping(value = "/dropdowns/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = CommonDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(rejectedRejectionConsumableService.getDropdownList(unitId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get rejected rejection consumable list ", response = RejectedRejectionDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> rejectedRejectionConsumableSearch(@RequestBody RejectedRejectionConsumableSearch rejectedRejectionSearch) {
		try
		{
			return new ResponseEntity<Object>(rejectedRejectionConsumableService.rejectedRejectionConsumableSearch(rejectedRejectionSearch), HttpStatus.OK);

		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get rejected rejection medication details ", response = RejectedRejectionDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> rejectedRejectionMedicationDetails(@PathVariable("id") Integer id,@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(rejectedRejectionConsumableService.rejectedRejectionMedicationDetails(id,unitId), HttpStatus.OK);

		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.PATCH)
	@ApiOperation(value = "save rejected rejection medicationdetails ", response = RejectedRejectionDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> rejectedRejectionMedicationSave(@RequestBody PendingIssuesAcceptanceSave rejectedRejectionSave) {
		try
		{
			return new ResponseEntity<Object>(rejectedRejectionConsumableService.rejectedRejectionMedicationSave(rejectedRejectionSave), HttpStatus.OK);

		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
}