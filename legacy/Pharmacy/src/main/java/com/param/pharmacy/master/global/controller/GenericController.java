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
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.generic.GenericDetailsResponse;
import com.param.pharmacy.master.global.dto.generic.GenericListResponse;
import com.param.pharmacy.master.global.dto.generic.GenericRequest;
import com.param.pharmacy.master.global.dto.generic.GenericRequestItem;
import com.param.pharmacy.master.global.service.generic.IGenericService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/globals/generics/")
public class GenericController {

	private static final Logger logger = Logger.getLogger(GenericController.class);

	@Autowired
	IGenericService GenericService;

	/*
	 * @RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	 * 
	 * @ApiOperation(value = "Get list for populating dropdown values", response =
	 * WalkInSalesDropdownResponse.class)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
	 * 
	 * @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
	 * 403, message = "forbidden!!!"),
	 * 
	 * @ApiResponse(code = 404, message = "not found!!!") }) public @ResponseBody
	 * ResponseEntity<Object> getDropDownValues() throws JSONException { try {
	 * return new ResponseEntity<Object>(GenericService.getDropdowns(),
	 * HttpStatus.OK); } catch (Exception ex) { return new
	 * ResponseEntity<Object>(new ErrorResponse(ex),
	 * HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */

	@RequestMapping(value = "/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Generics", response = GenericListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<GenericListResponse>> getAll(@PathVariable Integer orgId) {
		ResponseEntity<List<GenericListResponse>> response = new ResponseEntity<List<GenericListResponse>>(
				GenericService.getAll(orgId), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/IsCombination/{isCombination}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Generics", response = GenericListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<GenericListResponse>> getAllIsCombinationGeneric(@PathVariable("isCombination") Boolean isCombination) {
		ResponseEntity<List<GenericListResponse>> response = new ResponseEntity<List<GenericListResponse>>(
				GenericService.getAllIsCombinationGeneric(isCombination), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Generic By Id", response = GenericDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<GenericDetailsResponse> getById(@PathVariable("id") Integer id,@PathVariable Integer orgId) {

		ResponseEntity<GenericDetailsResponse> response = new ResponseEntity<GenericDetailsResponse>(
				GenericService.getById(id,orgId), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/getCombinationDetail/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Combinationa of Generic Details By Id", response = GenericDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<GenericRequestItem>> getCombinationDetailsById(@PathVariable("id") Integer id) {

		ResponseEntity<List<GenericRequestItem>> response = new ResponseEntity<List<GenericRequestItem>>(
				GenericService.getCombinationDetailsById(id), HttpStatus.OK);

		return response;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Generic", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Generic Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized Create to Generic"),
			@ApiResponse(code = 403, message = "Create Generic is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> create(@RequestBody GenericRequest genericRequest) {
		return new ResponseEntity<CommonResponse>(GenericService.create(genericRequest), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Generic", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Generic Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Generic"),
			@ApiResponse(code = 403, message = "Update Generic is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> update(@PathVariable("id") Integer id, @RequestBody GenericRequest genericRequest) {
		genericRequest.setId(id);
		try {
			return new ResponseEntity<Object>(GenericService.update(genericRequest), HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}

	}
}
