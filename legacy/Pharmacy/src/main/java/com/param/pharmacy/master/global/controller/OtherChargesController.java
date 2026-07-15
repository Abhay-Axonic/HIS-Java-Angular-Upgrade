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
//import com.param.entity.dto.CommonResponse;
import com.param.pharmacy.master.global.dto.otherCharges.OtherChargesDetailsResponse;
import com.param.pharmacy.master.global.dto.otherCharges.OtherChargesListResponse;
import com.param.pharmacy.master.global.dto.otherCharges.OtherChargesRequest;
import com.param.pharmacy.master.global.service.otherCharges.IOtherChargesService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/globals/other/charges/")
public class OtherChargesController {
	@Autowired 
	IOtherChargesService OtherChargesService;

	@RequestMapping(value = "{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Other Charges", response = OtherChargesListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<OtherChargesListResponse>> GetAll(@PathVariable Integer orgId) {
		
		ResponseEntity<List<OtherChargesListResponse>> response = new ResponseEntity<List<OtherChargesListResponse>>(
				OtherChargesService.getAll(orgId), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Other Charges By Id", response = OtherChargesDetailsResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<OtherChargesDetailsResponse> GetById(@PathVariable("id") Integer id,@PathVariable Integer orgId) {
		
		ResponseEntity<OtherChargesDetailsResponse> response = new ResponseEntity<OtherChargesDetailsResponse>(
				OtherChargesService.getById(id), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "{orgId}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Other Charges", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Other Charges Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Other Charges"),
			@ApiResponse(code = 403, message = "Create Other Charges is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody OtherChargesRequest otherChargesRequest,@PathVariable Integer orgId) {
		otherChargesRequest.setOrganizationId(orgId);
		return new ResponseEntity<CommonResponse>(OtherChargesService.create(otherChargesRequest),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Other Charges", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Other Charges Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Other Charges"),
			@ApiResponse(code = 403, message = "Update Other Charges is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("id") Integer id,@PathVariable Integer orgId,
			@RequestBody OtherChargesRequest otherChargesRequest) {
     	otherChargesRequest.setId(id);
     	otherChargesRequest.setOrganizationId(orgId);
		return new ResponseEntity<CommonResponse>(OtherChargesService.update(otherChargesRequest),
				HttpStatus.CREATED);
		
	}
}
