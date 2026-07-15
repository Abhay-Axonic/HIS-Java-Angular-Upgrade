package com.param.pharmacy.master.unit.controller;

import java.math.BigInteger;
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
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionItemResDetailsResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionItemRestrictionRequest;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionItemRestrictionResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionPCListResponse;
import com.param.pharmacy.master.unit.dto.productRestriction.ProductRestrictionRequest;
import com.param.pharmacy.master.unit.service.productRestriction.IProductRestrictionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/unit/product/restrictions")
public class ProductRestrictionController {

	private static final Logger logger = Logger.getLogger(ProductRestrictionController.class);

	@Autowired
	IProductRestrictionService ProductRestrictionService;

	@RequestMapping(value = "/product/category/mapper/{orgId}/{storeId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Product Restrictions of Product Categoey Mapping", response = ProductRestrictionPCListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ProductRestrictionPCListResponse>> GetAll(@PathVariable Integer orgId,@PathVariable("storeId") Integer storeId) {
		ResponseEntity<List<ProductRestrictionPCListResponse>> response = new ResponseEntity<List<ProductRestrictionPCListResponse>>(
				ProductRestrictionService.getAll(storeId,orgId), HttpStatus.OK);

		return response;
	}

	/*
	 * @RequestMapping(value = "/product/category/mapper/{id}", method =
	 * RequestMethod.GET)
	 * 
	 * @ApiOperation(value =
	 * "Get Product Restriction of Product Categoey Mapping By Id", response =
	 * ProductRestrictionDetailsResponse.class)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
	 * 
	 * @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
	 * 403, message = "forbidden!!!"),
	 * 
	 * @ApiResponse(code = 404, message = "not found!!!") }) public
	 * ResponseEntity<ProductRestrictionDetailsResponse> GetById(@PathVariable("id")
	 * BigInteger id) {
	 * 
	 * ResponseEntity<ProductRestrictionDetailsResponse> response = new
	 * ResponseEntity<ProductRestrictionDetailsResponse>(
	 * ProductRestrictionService.getById(id), HttpStatus.OK);
	 * 
	 * return response; }
	 */

	@RequestMapping(value = "/{storeId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Product Restriction of Product Categoey Mapping By storeId", response = ProductRestrictionPCListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ProductRestrictionPCListResponse>> GetBystoreId(
			@PathVariable("storeId") Integer storeId) {
		ResponseEntity<List<ProductRestrictionPCListResponse>> response = new ResponseEntity<List<ProductRestrictionPCListResponse>>(
				ProductRestrictionService.getBystoreId(storeId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/pcMapper", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Product Restriction of Product Categoey Mapping", response = CommonResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Product Restriction of Product Categoey Mapping Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Product Restriction of Product Categoey Mapping"),
			@ApiResponse(code = 403, message = "Create Product Restriction of Product Categoey Mapping is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody ProductRestrictionRequest productRestrictionRequest) {
		return new ResponseEntity<CommonResponse>(ProductRestrictionService.create(productRestrictionRequest),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/pcMapper/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Product Restriction of Product Categoey Mapping", response = CommonResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Product Restriction of Product Categoey Mapping Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Product Restriction of Product Categoey Mapping"),
			@ApiResponse(code = 403, message = "Update Product Restriction of Product Categoey Mapping is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") BigInteger id,
			@RequestBody ProductRestrictionRequest productRestrictionRequest) {
		productRestrictionRequest.setId(id);
		try {
			return new ResponseEntity<Object>(ProductRestrictionService.update(productRestrictionRequest),
					HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}

	}

	@RequestMapping(value = "/item/restrictions", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Product Restrictions of Item Restrictions", response = ProductRestrictionItemRestrictionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ProductRestrictionItemRestrictionResponse>> GetAllByItemRestriction() {
		ResponseEntity<List<ProductRestrictionItemRestrictionResponse>> response = new ResponseEntity<List<ProductRestrictionItemRestrictionResponse>>(
				ProductRestrictionService.getAllItems(), HttpStatus.OK);
		return response;
	}

	/* items of item restrictions by store id */
	@RequestMapping(value = "/itemRestrictions/{storeId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Product Restriction of Item Restrictions By storeId", response = ProductRestrictionPCListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ProductRestrictionItemRestrictionResponse>> GetItemRestrictionsBystoreId(
			@PathVariable("storeId") Integer storeId) {
		ResponseEntity<List<ProductRestrictionItemRestrictionResponse>> response = new ResponseEntity<List<ProductRestrictionItemRestrictionResponse>>(
				ProductRestrictionService.getItemRestrictionsBystoreId(storeId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/item/restrictions/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Product Restriction of Item Restrictions By Id", response = ProductRestrictionItemResDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<ProductRestrictionItemResDetailsResponse> GetItemRestrictionById(
			@PathVariable("id") BigInteger id) {

		ResponseEntity<ProductRestrictionItemResDetailsResponse> response = new ResponseEntity<ProductRestrictionItemResDetailsResponse>(
				ProductRestrictionService.getItemRestrictionById(id), HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/item/restrictions", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Product Restriction of Item Restrictions", response = CommonResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Product Restriction of Product Categoey Mapping Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Product Restriction of Product Categoey Mapping"),
			@ApiResponse(code = 403, message = "Create Product Restriction of Product Categoey Mapping is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(
			@RequestBody ProductRestrictionItemRestrictionRequest productRestrictionItemRestrictionRequest) {
		return new ResponseEntity<CommonResponse>(
				ProductRestrictionService.createItemRestriction(productRestrictionItemRestrictionRequest),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/item/restrictions/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Product Restriction of Item Restrictions", response = CommonResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Product Restriction of Product Categoey Mapping Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Product Restriction of Product Categoey Mapping"),
			@ApiResponse(code = 403, message = "Update Product Restriction of Product Categoey Mapping is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("id") BigInteger id,
			@RequestBody ProductRestrictionItemRestrictionRequest productRestrictionItemRestrictionRequest) {
		productRestrictionItemRestrictionRequest.setId(id);
		return new ResponseEntity<CommonResponse>(
				ProductRestrictionService.updateItemRestriction(productRestrictionItemRestrictionRequest),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/product/category/mapper/{id}/{status}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Get all Product Restrictions of Product Categoey Mapping", response = ProductRestrictionPCListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<CommonResponse> DeleteProductRestrictionMapper(@PathVariable("id") BigInteger id,
			@PathVariable("status") Boolean status) {
		ResponseEntity<CommonResponse> response = new ResponseEntity<CommonResponse>(
				ProductRestrictionService.updateProductRestrictionMapper(id, status), HttpStatus.OK);
		return response;
	}
}
