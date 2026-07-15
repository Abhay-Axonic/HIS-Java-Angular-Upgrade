package com.param.pharmacy.nursing.discharge.controller;

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
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationDto;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSave;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchDto;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchRequest;
import com.param.pharmacy.nursing.discharge.service.IMedicationReconciliationService;
import com.param.pharmacy.nursing.narcoticsStore.dto.EmptyAmpoulesResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/nursing/discharge/medication/reconciliation")
public class MedicationReconciliationController {

	private static final Logger logger = Logger.getLogger(MedicationReconciliationController.class);

	@Autowired
	private IMedicationReconciliationService medicationReconciliationService;
	
	@RequestMapping(value = "/dropdowns/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = MedicationReconciliationDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(medicationReconciliationService.getDropdownList(unitId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Medication Reconciliation list ", response = MedicationReconciliationSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> medicationReconciliationSearch(@RequestBody MedicationReconciliationSearchRequest request) {
		try
		{
			return new ResponseEntity<Object>(medicationReconciliationService.medicationReconciliationSearch(request), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.PATCH)
	@ApiOperation(value = "save medication reconciliation ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> medicationReconciliationSave(@RequestBody MedicationReconciliationSave request) {
		try
		{
			return new ResponseEntity<Object>(medicationReconciliationService.medicationReconciliationSave(request), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get medication reconciliation details ", response = EmptyAmpoulesResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> medicationReconciliationDetails(@PathVariable("id") Long id,@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(medicationReconciliationService.medicationReconciliationDetails(id,unitId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
