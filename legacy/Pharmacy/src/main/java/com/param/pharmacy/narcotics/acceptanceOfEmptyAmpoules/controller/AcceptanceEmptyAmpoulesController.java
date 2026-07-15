package com.param.pharmacy.narcotics.acceptanceOfEmptyAmpoules.controller;


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
import com.param.global.dto.global.CommonSearchRequest;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearchResult;
import com.param.pharmacy.narcotics.acceptanceOfEmptyAmpoules.service.IAcceptanceEmptyAmpoulesService;
import com.param.pharmacy.nursing.narcoticsStore.dto.EmptyAmpoulesResponse;
import com.param.pharmacy.nursing.narcoticsStore.dto.EmptyAmpoulesSave;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/narcotics/acceptanceEmptyAmpoules")
public class AcceptanceEmptyAmpoulesController {

	@Autowired
	private IAcceptanceEmptyAmpoulesService acceptanceEmptyAmpoulesService;

	private static final Logger logger = Logger.getLogger(AcceptanceEmptyAmpoulesController.class);

	@RequestMapping(value = "/dropdowns/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = CommonDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId) {
		try
		{
			return new ResponseEntity<Object>(acceptanceEmptyAmpoulesService.getDropdownList(unitId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get acceptance empty ampoules list ", response = PendingIssuesAcceptanceSearchResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> acceptanceEmptyAmpoulesSearch(@RequestBody CommonSearchRequest request) {
		try
		{
			return new ResponseEntity<Object>(acceptanceEmptyAmpoulesService.acceptanceEmptyAmpoulesSearch(request), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get acceptance empty ampoules details ", response = EmptyAmpoulesResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> acceptanceEmptyAmpoulesDetails(@PathVariable("id") Integer id) {
		try
		{
			return new ResponseEntity<Object>(acceptanceEmptyAmpoulesService.acceptanceEmptyAmpoulesDetails(id), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save acceptance empty ampoules ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> acceptanceEmptyAmpoulesSave(@RequestBody EmptyAmpoulesSave request) {
		try
		{
			return new ResponseEntity<Object>(acceptanceEmptyAmpoulesService.acceptanceEmptyAmpoulesSave(request), HttpStatus.OK);
			
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
}