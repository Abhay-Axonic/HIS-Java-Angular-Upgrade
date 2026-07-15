package com.param.pharmacy.master.global.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.global.common.Response;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementDetailsResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementListResponse;
import com.param.pharmacy.master.global.dto.unitOfMeasurement.UnitOfMeasurementRequest;
import com.param.pharmacy.master.global.service.unitOfMeasurement.IUnitOfMeasurementService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/global/unitOfMeasurement")
public class UnitOfMeasurementController {

	@Autowired
	IUnitOfMeasurementService UnitOfMeasurementService;

	@RequestMapping(value = "/{orgId}/{offset}/{noOfRecordsPerPage}/{itemName}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Unit Of Measurement", response = UnitOfMeasurementListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public Response GetAll(@PathVariable Integer orgId,@PathVariable Integer offset,@PathVariable Integer noOfRecordsPerPage , @PathVariable String itemName) {
		return UnitOfMeasurementService.getAll(orgId,offset,noOfRecordsPerPage,itemName);

		 
	}
	

	/*@RequestMapping(value = "/uom/{itemId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all unit Of Measurement Data", response = UnitOfMeasurementListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<UnitOfMeasurementListResponse> GetAll(@PathVariable("itemId") Integer itemId) {
		ResponseEntity<UnitOfMeasurementListResponse> response = new ResponseEntity<UnitOfMeasurementListResponse>(
				UnitOfMeasurementService.getAll(itemId), HttpStatus.OK);

		return response;
	}*/


	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Unit Of Measurement By Id", response = UnitOfMeasurementDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<UnitOfMeasurementDetailsResponse> GetById(@PathVariable("id") Integer id,@PathVariable Integer orgId) {

		ResponseEntity<UnitOfMeasurementDetailsResponse> response = new ResponseEntity<UnitOfMeasurementDetailsResponse>(
				UnitOfMeasurementService.getById(id,orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{orgId}/{userId}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Unit Of Measurement", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Unit Of Measurement Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Unit Of Measurement"),
			@ApiResponse(code = 403, message = "Create Unit Of Measurement is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })

	public ResponseEntity<CommonResponse> Create(@RequestBody List<UnitOfMeasurementRequest> unitOfMeasurementRequest,@PathVariable Integer userId,@PathVariable Integer orgId) {
		return new ResponseEntity<CommonResponse>(UnitOfMeasurementService.create(unitOfMeasurementRequest,userId,orgId),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{orgId}/{itemId}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Unit Of Measurement", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Unit Of Measurement Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Unit Of Measurement"),
			@ApiResponse(code = 403, message = "Update Unit Of Measurement is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("itemId") Integer itemId,@PathVariable Integer orgId,
			@RequestBody UnitOfMeasurementRequest unitOfMeasurementRequest ) {
		unitOfMeasurementRequest.setItemId(itemId);
		unitOfMeasurementRequest.setOrganizationId(orgId);
		return new ResponseEntity<CommonResponse>(UnitOfMeasurementService.update(unitOfMeasurementRequest),
				HttpStatus.CREATED);
	}

}
