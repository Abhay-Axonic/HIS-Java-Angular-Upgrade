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

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceRequest;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceResponse;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceSave;
import com.param.pharmacy.ip.service.returnAcceptance.medication.IReturnAcceptanceMedicationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/ip/patient/return/acceptance/medication")
public class ReturnAcceptanceMedicationController {

	@Autowired
	private IReturnAcceptanceMedicationService returnAcceptanceMedicationService;

	private static final Logger logger = Logger.getLogger(ReturnAcceptanceMedicationController.class);

	@RequestMapping(value = "/dropdowns/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = CommonDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(returnAcceptanceMedicationService.getDropdownList(unitId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get return acceptance list ", response = ReturnAcceptanceResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> returnAcceptanceSearch(@RequestBody ReturnAcceptanceRequest returnAcceptanceRequest) {
		try
		{
			return new ResponseEntity<Object>(returnAcceptanceMedicationService.returnAcceptanceSearch(returnAcceptanceRequest), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get return acceptance details ", response = ReturnAcceptanceResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> returnAcceptanceDetails(@PathVariable("id") Integer id,@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(returnAcceptanceMedicationService.returnAcceptanceDetails(id,unitId), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save Patient Return Acceptance", response = PatientIssueReturn.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<Object> patientReturnAcceptanceSave(@RequestBody ReturnAcceptanceSave returnAcceptanceSave) {
		try
		{
            Response statausres = returnAcceptanceMedicationService.patientReturnAcceptanceStatus(returnAcceptanceSave);
			
			if (statausres.getObject() == com.param.common.constants.Status.Completed.getStatus()
					|| statausres.getObject() == com.param.common.constants.Status.Closed.getStatus()) {
				CommonResponse response = new CommonResponse();
				response.setMessage("This transaction already processed by other user");
				return new ResponseEntity<Object>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return new ResponseEntity<Object>(returnAcceptanceMedicationService.patientReturnAcceptanceSave(returnAcceptanceSave), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
}