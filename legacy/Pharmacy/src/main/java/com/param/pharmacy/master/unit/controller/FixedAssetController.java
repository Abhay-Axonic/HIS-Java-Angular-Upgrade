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
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryDetailResponse;
import com.param.pharmacy.master.unit.dto.fixedAsset.FixedAssetListResponse;
import com.param.pharmacy.master.unit.dto.fixedAsset.FixedAssetRequest;
import com.param.pharmacy.master.unit.dto.fixedAsset.FixedAssetResponse;
import com.param.pharmacy.master.unit.service.fixedAsset.IFixedAssetService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/unit/fixed/asset")
public class FixedAssetController {
	
	@Autowired
	IFixedAssetService FixedAssetService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Fixed Assets", response = FixedAssetListResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<FixedAssetListResponse>> GetAll() {
		ResponseEntity<List<FixedAssetListResponse>> response = new ResponseEntity<List<FixedAssetListResponse>>(
				FixedAssetService.getAll(), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Fixed Asset By Id", response = ProductCategoryDetailResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<FixedAssetResponse>GetById(@PathVariable("id") Integer id) {

		ResponseEntity<FixedAssetResponse> response = new ResponseEntity<FixedAssetResponse>(
				FixedAssetService.getById(id), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Fixed Asset", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Fixed Asset Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Fixed Asset"),
			@ApiResponse(code = 403, message = "Create Fixed Asset is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody com.param.pharmacy.master.unit.dto.fixedAsset.FixedAssetRequest fixedAssetRequest) {
		return new ResponseEntity<CommonResponse>(FixedAssetService.create(fixedAssetRequest),
				HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Product Category", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Fixed Asset Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Fixed Asset"),
			@ApiResponse(code = 403, message = "Update Fixed Asset is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("id") Integer id,
			@RequestBody FixedAssetRequest fixedAssetRequest) {
		fixedAssetRequest.setId(id);
		return new ResponseEntity<CommonResponse>(FixedAssetService.update(fixedAssetRequest),
				HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/getAssetNameListByAssetCategoryId/{assetCategoryId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Fixed Assets Name By Asset Category Id", response = FixedAssetListResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<FixedAssetListResponse>> getAssetNameListByAssetCategoryId(@PathVariable("assetCategoryId") Integer assetCategoryId) {
		ResponseEntity<List<FixedAssetListResponse>> response = new ResponseEntity<List<FixedAssetListResponse>>(
				FixedAssetService.getAssetNameListByAssetCategoryId(assetCategoryId), HttpStatus.OK);

		return response;
	}
}
