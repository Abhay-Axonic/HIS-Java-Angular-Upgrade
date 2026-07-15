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
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeDetailsResponse;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeListResponse;
import com.param.pharmacy.master.unit.dto.assetType.AssetTypeRequest;
import com.param.pharmacy.master.unit.service.assetType.IAssetTypeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/units/assets/types/")
public class AssetTypeController {
	
	@Autowired
	IAssetTypeService AssetTypeService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Asset Type", response = AssetTypeListResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<AssetTypeListResponse>> GetAll() {
		ResponseEntity<List<AssetTypeListResponse>> response = new ResponseEntity<List<AssetTypeListResponse>>(
				AssetTypeService.getAll(), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Asset Type By Id", response = AssetTypeDetailsResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<AssetTypeDetailsResponse> GetById(@PathVariable("id") Integer id) {

		ResponseEntity<AssetTypeDetailsResponse> response = new ResponseEntity<AssetTypeDetailsResponse>(
				AssetTypeService.getById(id), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Product Category", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Asset Type Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Asset Type"),
			@ApiResponse(code = 403, message = "Create Product Category is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody AssetTypeRequest assetTypeRequest) {
		return new ResponseEntity<CommonResponse>(AssetTypeService.create(assetTypeRequest),
				HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Product Category", response = CommonResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Asset Type Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Asset Type"),
			@ApiResponse(code = 403, message = "Update Asset Type is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("id") Integer id,
			@RequestBody AssetTypeRequest assetTypeRequest) {
		assetTypeRequest.setId(id);
		return new ResponseEntity<CommonResponse>(AssetTypeService.update(assetTypeRequest),
				HttpStatus.CREATED);
	}
}
