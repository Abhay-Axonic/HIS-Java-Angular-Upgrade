package com.param.pharmacy.master.global.controller;

import java.util.List;

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
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.strengthUnit.StrengthUnitDetailsResponse;
import com.param.pharmacy.master.global.dto.strengthUnit.StrengthUnitListResponse;
import com.param.pharmacy.master.global.dto.strengthUnit.StrengthUnitRequest;
import com.param.pharmacy.master.global.service.strengthUnit.IStrengthUnitService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/globals/strength/units")
public class StrengthUnitController {

	private static final Logger logger = Logger.getLogger(StrengthUnitController.class);

	@Autowired
	IStrengthUnitService StrengthUnitService;

	@RequestMapping(value = "/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Strength Units", response = StrengthUnitListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<StrengthUnitListResponse>> GetAll(@PathVariable Integer orgId) {
		ResponseEntity<List<StrengthUnitListResponse>> response = new ResponseEntity<List<StrengthUnitListResponse>>(
				StrengthUnitService.getAll(orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Strength Unit By Id", response = StrengthUnitDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<StrengthUnitDetailsResponse> GetById(@PathVariable("id") Integer id,@PathVariable Integer orgId) {

		ResponseEntity<StrengthUnitDetailsResponse> response = new ResponseEntity<StrengthUnitDetailsResponse>(
				StrengthUnitService.getById(id), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{orgId}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Strength Unit", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Strength Unit Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Strength Unit"),
			@ApiResponse(code = 403, message = "Create Strength Unit is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody StrengthUnitRequest strengthUnitRequest,@PathVariable Integer orgId) {
		strengthUnitRequest.setOrganizationId(orgId);
		return new ResponseEntity<CommonResponse>(StrengthUnitService.create(strengthUnitRequest), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Strength Unit", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Strength Unit Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Strength Unit"),
			@ApiResponse(code = 403, message = "Update Strength Unit is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id,
			@RequestBody StrengthUnitRequest strengthUnitRequest,@PathVariable Integer orgId) {
		strengthUnitRequest.setId(id);
		strengthUnitRequest.setOrganizationId(orgId);
		try {
			return new ResponseEntity<Object>(StrengthUnitService.update(strengthUnitRequest), HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}
	}
}
