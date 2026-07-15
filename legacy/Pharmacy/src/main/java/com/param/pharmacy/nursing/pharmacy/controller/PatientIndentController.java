
package com.param.pharmacy.nursing.pharmacy.controller;


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
import com.param.entity.model.pharmacy.PatientIndent;
import com.param.global.common.ServiceException;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentDetails;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentDropdown;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentList;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentRequest;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentSave;
import com.param.pharmacy.nursing.pharmacy.service.patientIndent.IPatientIndentService;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/pharmacy/nursing/pharmacy/patient/indent/consumable")
public class PatientIndentController {

	private static final Logger logger = Logger.getLogger(PatientIndentController.class);

	@Autowired
	private IPatientIndentService patientIndentService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = PatientIndentDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try
		{
			return new ResponseEntity<Object>(patientIndentService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Patient Indent list ", response = PatientIndentList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIndentSearch(@RequestBody PatientIndentRequest patientIndentRequest) {
		try
		{
			return new ResponseEntity<Object>(patientIndentService.patientIndentSearch(patientIndentRequest), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/previousindentdetails", method = RequestMethod.POST)
	@ApiOperation(value = "Get Patient Indent details ", response = PatientIndentDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientPreviousIndentDetails(@RequestBody PatientIndentRequest patientIndentRequest) {
		try
		{
			return new ResponseEntity<Object>(patientIndentService.patientPreviousIndentDetails(patientIndentRequest), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}


	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Patient Indent details ", response = PatientIndentDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIndentDetails(@PathVariable("id") Integer id,@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(patientIndentService.patientIndentDetails(id,unitId), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}


	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save Patient Indent", response = PatientIndent.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIndentSave(@RequestBody PatientIndentSave patientIndentSave) {
		try
		{
			return new ResponseEntity<Object>(patientIndentService.patientIndentSave(patientIndentSave), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update patient indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateStoreIndent(@PathVariable Integer id, @RequestBody PatientIndentRequest patientIndentRequest)
			throws Exception {
		try {
			patientIndentRequest.setId(id);
			return new ResponseEntity<Object>(patientIndentService.updatePatientIndent(patientIndentRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve patient indents", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveStoreIndent(@RequestBody PatientIndentRequest patientIndentRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(patientIndentService.approvePatientIndent(patientIndentRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel patient indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelPatientIndent(@PathVariable Integer id, @RequestBody PatientIndentRequest patientIndentRequest)
			throws Exception {
		try {
			patientIndentRequest.setId(id);
			return new ResponseEntity<Object>(patientIndentService.cancelPatientIndent(patientIndentRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}