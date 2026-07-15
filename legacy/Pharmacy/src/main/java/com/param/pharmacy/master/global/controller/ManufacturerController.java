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
import com.param.pharmacy.master.global.dto.manufacturer.ManufacturerDetailsResponse;
import com.param.pharmacy.master.global.dto.manufacturer.ManufacturerListResponse;
import com.param.pharmacy.master.global.dto.manufacturer.ManufacturerRequset;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryDetailResponse;
import com.param.pharmacy.master.global.service.manufacturer.IManufacturerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/globals/manufacturers/")
public class ManufacturerController {

	private static final Logger logger = Logger.getLogger(ManufacturerController.class);

	@Autowired
	IManufacturerService ManufacturerService;

	@RequestMapping(value = "{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Manufacturer", response = ManufacturerListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ManufacturerListResponse>> getAll() {
		ResponseEntity<List<ManufacturerListResponse>> response = new ResponseEntity<List<ManufacturerListResponse>>(
				ManufacturerService.getAll(), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Manufacturer By Id", response = ProductCategoryDetailResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<ManufacturerDetailsResponse> GetById(@PathVariable("id") Integer id,@PathVariable Integer orgId) {

		ResponseEntity<ManufacturerDetailsResponse> response = new ResponseEntity<ManufacturerDetailsResponse>(
				ManufacturerService.getById(id,orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "{orgId}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Manufacturer", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Manufacturer Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Manufacturer"),
			@ApiResponse(code = 403, message = "Create Manufacturer is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody ManufacturerRequset manufacturerRequset,@PathVariable Integer orgId) {
		manufacturerRequset.setOrganizationId(orgId);
		return new ResponseEntity<CommonResponse>(ManufacturerService.create(manufacturerRequset), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Manufacturer", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Manufacturer Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Manufacturer"),
			@ApiResponse(code = 403, message = "Update Manufacturer is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id,
			@RequestBody ManufacturerRequset manufacturerRequset,@PathVariable Integer orgId) {
			manufacturerRequset.setId(id);
			manufacturerRequset.setOrganizationId(orgId);
		try {
			return new ResponseEntity<Object>(ManufacturerService.update(manufacturerRequset), HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}
	}

	@RequestMapping(value = "/prefix/{orgId}/{screenId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Manufacturer prefix By screen Id", response = ProductCategoryDetailResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> GetPrefixById(@PathVariable("orgId") Integer orgId,@PathVariable Integer screenId) {

		ResponseEntity<Object> response = new ResponseEntity<Object>(
				ManufacturerService.GetPrefixById(orgId,screenId), HttpStatus.OK);

		return response;
	}

}
