package com.param.pharmacy.nursing.workstation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.apache.log4j.Logger;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.pharmacy.dto.AntibioticTestDoseDto;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationDropdown;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationRequest;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationResponse;
import com.param.pharmacy.nursing.workstation.service.drugAdministration.IDrugAdministrationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/nursing/workstation/drug/administration")
public class DrugAdministrationController implements ICommonConstants{

	private static final Logger logger = Logger.getLogger(DrugAdministrationController.class);

	@Autowired
	private IDrugAdministrationService drugAdministrationService;
	
	@RequestMapping(value = "/dropdown", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = DrugAdministrationDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList() {
		try
		{
			return new ResponseEntity<Object>(drugAdministrationService.drugAdministrationDropdown(), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	
	@RequestMapping(value = "/reasondropdown/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = DrugAdministrationDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getreasonDropdownList(@PathVariable Integer orgId) {
		try
		{
			return new ResponseEntity<Object>(drugAdministrationService.getreasonDropdownList(orgId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get drug administration list ", response = DrugAdministrationResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> drugAdministrationSearch(@RequestBody DrugAdministrationRequest request) {
		try
		{
			return new ResponseEntity<Object>(drugAdministrationService.drugAdministrationSearch(request), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save drug administration", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> drugAdministrationSave(@RequestBody DrugAdministrationRequest request) {
		try
		{
			return new ResponseEntity<Object>(drugAdministrationService.drugAdministrationSave(request), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.PATCH)
	@ApiOperation(value = "update drug administration", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> drugAdministrationUpdate(@RequestBody DrugAdministrationRequest request) {
		try
		{
			return new ResponseEntity<Object>(drugAdministrationService.drugAdministrationUpdate(request), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	
	@RequestMapping(value = "/getBatchDetailsByPresDetailsId/{id}", method = RequestMethod.GET)
	public Response getActiveCabinMaster(@PathVariable ("id") Integer id){
		
		try {
			return drugAdministrationService.getBatchDetailsByPresDetailsId(id);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/saveAntibioticTestDose",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveAntibioticTestDose(@RequestBody AntibioticTestDoseDto dto) {
		try {
			return drugAdministrationService.saveAntibioticTestDose(dto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value="/getAntibioticTestDose",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getAntibioticTestDose(@RequestBody AntibioticTestDoseDto dto) {
		try {
			return drugAdministrationService.getAntibioticTestDose(dto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/checkObservation",method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response checkObservation(@RequestBody AntibioticTestDoseDto dto) {
		try {
			return drugAdministrationService.checkObservation(dto);
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}