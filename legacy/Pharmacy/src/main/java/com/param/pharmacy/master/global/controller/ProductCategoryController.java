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

import org.apache.log4j.Logger;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryDetailResponse;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryListResponse;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryRequest;
import com.param.pharmacy.master.global.service.productCategory.IProductCategoryService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/globals/products/categories")
public class ProductCategoryController {
	@Autowired
	IProductCategoryService ProductCategoryService;

	private static final Logger logger = Logger.getLogger(ProductCategoryController.class);

	@RequestMapping(value = "/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Product Categories", response = ProductCategoryListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ProductCategoryListResponse>> GetAll(@PathVariable Integer orgId) {
		ResponseEntity<List<ProductCategoryListResponse>> response = new ResponseEntity<List<ProductCategoryListResponse>>(
				ProductCategoryService.getAll(orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Product Category By Id", response = ProductCategoryDetailResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<ProductCategoryDetailResponse> GetById(@PathVariable("id") Integer id,@PathVariable Integer orgId) {

		ResponseEntity<ProductCategoryDetailResponse> response = new ResponseEntity<ProductCategoryDetailResponse>(
				ProductCategoryService.getById(id,orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Product Category", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Product Category Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Product Category"),
			@ApiResponse(code = 403, message = "Create Product Category is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody ProductCategoryRequest productCategoryRequest) {
		return new ResponseEntity<CommonResponse>(ProductCategoryService.create(productCategoryRequest),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Product Category", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Product Category Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Product Category"),
			@ApiResponse(code = 403, message = "Update Product Category is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id,@PathVariable Integer orgId,
			@RequestBody ProductCategoryRequest productCategoryRequest) {
		productCategoryRequest.setId(id);
		try {
			return new ResponseEntity<Object>(ProductCategoryService.update(productCategoryRequest),
					HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}

	}

}
