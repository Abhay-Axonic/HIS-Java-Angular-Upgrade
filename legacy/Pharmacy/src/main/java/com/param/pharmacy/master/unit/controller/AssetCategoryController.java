package com.param.pharmacy.master.unit.controller;

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
import com.param.pharmacy.master.unit.dto.assetCategory.AssetCategoryDetailsResponse;
import com.param.pharmacy.master.unit.dto.assetCategory.AssetCategoryListResponse;
import com.param.pharmacy.master.unit.dto.assetCategory.AssetCategoryRequest;
import com.param.pharmacy.master.unit.service.assetCategory.IAssetCategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/units/assets/categories/")
public class AssetCategoryController {

	private static final Logger logger = Logger.getLogger(AssetCategoryController.class);

	@Autowired
	IAssetCategoryService AssetCategoryService;

	@RequestMapping(value = "/org/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Asset Category", response = AssetCategoryListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<AssetCategoryListResponse>> GetAll(@PathVariable("orgId") Integer orgId) {
		ResponseEntity<List<AssetCategoryListResponse>> response = new ResponseEntity<List<AssetCategoryListResponse>>(
				AssetCategoryService.getAll(orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Asset Category By Id", response = AssetCategoryDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<AssetCategoryDetailsResponse> GetById(@PathVariable("id") Integer id) {

		ResponseEntity<AssetCategoryDetailsResponse> response = new ResponseEntity<AssetCategoryDetailsResponse>(
				AssetCategoryService.getById(id), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Asset Category", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Asset Category Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Asset Category"),
			@ApiResponse(code = 403, message = "Create Asset Category is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody AssetCategoryRequest assetCategoryRequest) {
		return new ResponseEntity<CommonResponse>(AssetCategoryService.create(assetCategoryRequest),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Asset Category", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Product Category Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Product Category"),
			@ApiResponse(code = 403, message = "Update Product Category is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id,
			@RequestBody AssetCategoryRequest assetCategoryRequest) {
		assetCategoryRequest.setId(id);
		try {
			return new ResponseEntity<Object>(AssetCategoryService.update(assetCategoryRequest), HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}

	}

}
