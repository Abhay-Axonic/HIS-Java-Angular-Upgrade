package com.param.pharmacy.master.global.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.taxComponent.TaxComponentDetailsResponse;
import com.param.pharmacy.master.global.dto.taxComponent.TaxComponentListResponse;
import com.param.pharmacy.master.global.dto.taxComponent.TaxComponentRequest;
import com.param.pharmacy.master.global.service.taxComponent.ITaxComponentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/globals/tax/components/")
public class TaxComponentController {

	@Autowired
	ITaxComponentService TaxComponentService;

	private static final Logger logger = Logger.getLogger(TaxComponentController.class);

	@RequestMapping(value = "{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Tax Component", response = TaxComponentListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<TaxComponentListResponse>> GetAll(@PathVariable Integer orgId) {
		ResponseEntity<List<TaxComponentListResponse>> response = new ResponseEntity<List<TaxComponentListResponse>>(
				TaxComponentService.getAll(orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Tax Component By Id", response = TaxComponentDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<TaxComponentDetailsResponse> GetById(@PathVariable("id") Integer id,@PathVariable Integer orgId) {

		ResponseEntity<TaxComponentDetailsResponse> response = new ResponseEntity<TaxComponentDetailsResponse>(
				TaxComponentService.getById(id,orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "{orgId}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Product Category", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Tax Component Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Tax Component"),
			@ApiResponse(code = 403, message = "Create Tax Component is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody TaxComponentRequest taxComponentRequest,@PathVariable Integer orgId) {
		taxComponentRequest.setOrganizationId(orgId);
		return new ResponseEntity<CommonResponse>(TaxComponentService.create(taxComponentRequest), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Product Category", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Tax Component Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Tax Component"),
			@ApiResponse(code = 403, message = "Update Tax Component is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id,
			@RequestBody TaxComponentRequest taxComponentRequest,@PathVariable Integer orgId) {
		taxComponentRequest.setId(id);
		taxComponentRequest.setOrganizationId(orgId);
		try {
			return new ResponseEntity<Object>(TaxComponentService.update(taxComponentRequest), HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}
	}

	@RequestMapping(value = "/taxcomponentlist", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = com.param.pharmacy.master.global.dto.taxComponent.TaxComponentListDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getTaxComponentList() throws JSONException {
		return new ResponseEntity<Object>(TaxComponentService.getTaxComponentList(), HttpStatus.OK);
	}
}
