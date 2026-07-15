package com.param.pharmacy.nursing.workstation.controller;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.ErrorResponse;
import com.param.entity.model.pharmacy.PatientIndent;
import com.param.global.common.ServiceException;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstIssueResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstItemResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnAddItem;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnAddItemResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnDetails;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnDropdown;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnSave;
import com.param.pharmacy.nursing.workstation.service.patientIssueReturn.IPatientIssueReturnService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/nursing/workstation/patient/issue/return/medication")
public class PatientIssueReturnController {

	@Autowired
	private IPatientIssueReturnService patientIssueReturnService;

	private static final Logger logger = Logger.getLogger(PatientIssueReturnController.class);

	@RequestMapping(value = "/dropdown/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = PatientIssueReturnDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(patientIssueReturnService.getDropdownList(unitId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Patient Issue Return list ", response = PatientIssueReturnResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIssueReturnSearch(@RequestBody PatientIssueReturnRequest patientIssueReturnRequest) {
		try
		{
			return new ResponseEntity<Object>(patientIssueReturnService.patientIssueReturnSearch(patientIssueReturnRequest), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Patient Issue Return list ", response = PatientIssueReturnDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIssueReturnById(@PathVariable Integer id,@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(patientIssueReturnService.patientIssueReturnById(id,unitId), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	} 
	
	@RequestMapping(value = "/againstIssue/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Patient Against Issue list ", response = PatientAgainstIssueResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientAgainstIssueSearch(@RequestBody PatientIssueReturnRequest patientIssueReturnRequest) {
		try
		{
			return new ResponseEntity<Object>(patientIssueReturnService.patientAgainstIssueSearch(patientIssueReturnRequest), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/againstItem/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Patient Against Item list ", response = PatientAgainstItemResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientAgainstItemSearch(@RequestBody PatientIssueReturnRequest patientIssueReturnRequest) {
		try
		{
			return new ResponseEntity<Object>(patientIssueReturnService.patientAgainstItemSearch(patientIssueReturnRequest), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/add/new", method = RequestMethod.POST)
	@ApiOperation(value = "Add issue to patient issue retur", response = PatientIssueReturnAddItemResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIssueReturnAddNew(@RequestBody PatientIssueReturnAddItem patientIssueReturnAddItem) {
		try
		{
			return new ResponseEntity<Object>(patientIssueReturnService.patientIssueReturnAddNew(patientIssueReturnAddItem), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save Patient Issue Return", response = PatientIndent.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIssueReturnSave(@RequestBody PatientIssueReturnSave patientIssueReturnSave) {
		try
		{
			return new ResponseEntity<Object>(patientIssueReturnService.patientIssueReturnSave(patientIssueReturnSave), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}

}