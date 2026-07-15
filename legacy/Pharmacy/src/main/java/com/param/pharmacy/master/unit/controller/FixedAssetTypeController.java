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
import com.param.pharmacy.master.unit.dto.fixedAssetType.FixedAssetTypeDetailsResponse;
import com.param.pharmacy.master.unit.dto.fixedAssetType.FixedAssetTypeListResponse;
import com.param.pharmacy.master.unit.dto.fixedAssetType.FixedAssetTypeRequest;
import com.param.pharmacy.master.unit.service.fixedAssetType.IFixedAssetTypeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/unit/fixed/asset/type")
public class FixedAssetTypeController {

	@Autowired
	IFixedAssetTypeService FixedAssetTypeService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Fixed Asset Type", response = FixedAssetTypeListResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<FixedAssetTypeListResponse>> GetAll() {
		ResponseEntity<List<FixedAssetTypeListResponse>> response = new ResponseEntity<List<FixedAssetTypeListResponse>>(
				FixedAssetTypeService.getAll(), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Fixed Asset Type By Id", response = FixedAssetTypeDetailsResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<FixedAssetTypeDetailsResponse> GetById(@PathVariable("id") Integer id) {

		ResponseEntity<FixedAssetTypeDetailsResponse> response = new ResponseEntity<FixedAssetTypeDetailsResponse>(
				FixedAssetTypeService.getById(id), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Fixed Asset Type", response = CommonResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Fixed Asset Type Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Fixed Asset Type"),
			@ApiResponse(code = 403, message = "Create Fixed Asset Type is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody FixedAssetTypeRequest fixedAssetTypeRequest) {
		return new ResponseEntity<CommonResponse>(FixedAssetTypeService.create(fixedAssetTypeRequest),
				HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Fixed Asset Type", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Fixed Asset Type Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Fixed Asset Type"),
			@ApiResponse(code = 403, message = "Update Fixed Asset Type is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("id") Integer id,
			@RequestBody FixedAssetTypeRequest fixedAssetTypeRequest) {
		fixedAssetTypeRequest.setId(id);
		return new ResponseEntity<CommonResponse>(FixedAssetTypeService.update(fixedAssetTypeRequest),
				HttpStatus.CREATED);
	}

}
