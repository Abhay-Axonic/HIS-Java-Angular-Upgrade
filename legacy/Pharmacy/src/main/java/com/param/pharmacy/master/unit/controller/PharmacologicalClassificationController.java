package com.param.pharmacy.master.unit.controller;

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
import com.param.pharmacy.master.unit.dto.pharmacologicalClassification.PharmacologicalClassificationDetailsResponse;
import com.param.pharmacy.master.unit.dto.pharmacologicalClassification.PharmacologicalClassificationListResponse;
import com.param.pharmacy.master.unit.dto.pharmacologicalClassification.PharmacologicalClassificationRequest;
import com.param.pharmacy.master.unit.service.pharmacologicalClassification.IPharmacologicalClassificationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/units/pharmacological/classifications/")
public class PharmacologicalClassificationController {
	
	@Autowired
	IPharmacologicalClassificationService PharmacologicalClassificationService;

	@RequestMapping(value = "/org/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Pharmacological Classification", response = PharmacologicalClassificationListResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<PharmacologicalClassificationListResponse>> GetAll(@PathVariable Integer orgId) {
		ResponseEntity<List<PharmacologicalClassificationListResponse>> response = new ResponseEntity<List<PharmacologicalClassificationListResponse>>(
				PharmacologicalClassificationService.getAll(orgId), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Pharmacological Classification By Id", response = PharmacologicalClassificationDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<PharmacologicalClassificationDetailsResponse> GetById(@PathVariable("id") Integer id) {

		ResponseEntity<PharmacologicalClassificationDetailsResponse> response = new ResponseEntity<PharmacologicalClassificationDetailsResponse>(
				PharmacologicalClassificationService.getById(id), HttpStatus.OK);

		return response;
	}
	 
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Pharmacological Classification", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Pharmacological Classification Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Pharmacological Classification"),
			@ApiResponse(code = 403, message = "Create Pharmacological Classification is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody PharmacologicalClassificationRequest pharmacologicalClassificationRequest) {
		return new ResponseEntity<CommonResponse>(PharmacologicalClassificationService.create(pharmacologicalClassificationRequest),
				HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Pharmacological Classification", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Pharmacological Classification Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Pharmacological Classification"),
			@ApiResponse(code = 403, message = "Update Pharmacological Classification is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("id") Integer id,
			@RequestBody PharmacologicalClassificationRequest pharmacologicalClassificationRequest) {
		pharmacologicalClassificationRequest.setId(id);
		return new ResponseEntity<CommonResponse>(PharmacologicalClassificationService.update(pharmacologicalClassificationRequest),
				HttpStatus.CREATED);
	}
}
