package com.param.pharmacy.nursing.workstation.controller;


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
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstIssueResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstItemResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnDropdown;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnResponse;
import com.param.pharmacy.nursing.workstation.service.patientIssueReturnConsumable.IPatientIssueReturnConsumableService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/nursing/workstation/patient/issue/return/consumable")
public class PatientIssueReturnConsumableController {

	@Autowired
	private IPatientIssueReturnConsumableService patientIssueReturnConsumableService;

	private static final Logger logger = Logger.getLogger(PatientIssueReturnConsumableController.class);

	@RequestMapping(value = "/dropdown/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = PatientIssueReturnDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(patientIssueReturnConsumableService.getDropdownList(unitId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient Issue Return list ", response = PatientIssueReturnResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIssueReturnSearch(@RequestBody PatientIssueReturnRequest patientIssueReturnRequest) {
		try
		{
			return new ResponseEntity<Object>(patientIssueReturnConsumableService.patientIssueReturnSearch(patientIssueReturnRequest), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	

	@RequestMapping(value = "/againstIssue/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient Against Issue list ", response = PatientAgainstIssueResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientAgainstIssueSearch(@RequestBody PatientIssueReturnRequest patientIssueReturnRequest) {
		try
		{
			return new ResponseEntity<Object>(patientIssueReturnConsumableService.patientAgainstIssueSearch(patientIssueReturnRequest), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/againstItem/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient Against Item list ", response = PatientAgainstItemResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientAgainstItemSearch(@RequestBody PatientIssueReturnRequest patientIssueReturnRequest) {
		try
		{
			return new ResponseEntity<Object>(patientIssueReturnConsumableService.patientAgainstItemSearch(patientIssueReturnRequest), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
}