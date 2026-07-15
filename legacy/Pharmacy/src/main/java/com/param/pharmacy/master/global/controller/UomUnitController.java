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
import com.param.pharmacy.master.global.dto.uomUnit.UomUnitDetailsResponse;
import com.param.pharmacy.master.global.dto.uomUnit.UomUnitListResponse;
import com.param.pharmacy.master.global.dto.uomUnit.UomUnitRequest;
import com.param.pharmacy.master.global.service.uomUnit.IUomUnitService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/globals/uom/units/")
public class UomUnitController {

	@Autowired
	IUomUnitService UomUnitService;

	private static final Logger logger = Logger.getLogger(UomUnitController.class);

	@RequestMapping(value = "{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all UOM Unit", response = UomUnitListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<UomUnitListResponse>> GetAll(@PathVariable Integer orgId) {
		ResponseEntity<List<UomUnitListResponse>> response = new ResponseEntity<List<UomUnitListResponse>>(
				UomUnitService.getAll(orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get UOM Unit By Id", response = UomUnitDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<UomUnitDetailsResponse> GetById(@PathVariable("id") Integer id,@PathVariable Integer orgId) {

		ResponseEntity<UomUnitDetailsResponse> response = new ResponseEntity<UomUnitDetailsResponse>(
				UomUnitService.getById(id,orgId), HttpStatus.OK);

		return response;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "{orgId}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create UOM Unit", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "UOM Unit Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to UOM Unit"),
			@ApiResponse(code = 403, message = "Create UOM Unit is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody UomUnitRequest uomUnitRequest,@PathVariable Integer orgId) {
		uomUnitRequest.setOrganizationId(orgId);
		return new ResponseEntity<CommonResponse>(UomUnitService.create(uomUnitRequest), HttpStatus.CREATED);
	}

	@RequestMapping(value = "{orgId}/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update UOM Unit", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "UOM Unit Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to UOM Unit"),
			@ApiResponse(code = 403, message = "Update UOM Unit is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id, @RequestBody UomUnitRequest uomUnitRequest,@PathVariable Integer orgId) {
		uomUnitRequest.setId(id);
		
		try {
			uomUnitRequest.setOrganizationId(orgId);
			return new ResponseEntity<Object>(UomUnitService.update(uomUnitRequest), HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}
	}
}
