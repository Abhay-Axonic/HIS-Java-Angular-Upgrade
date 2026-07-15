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
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeListResponse;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationRoaRequest;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationTypeAndROAList;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationTypeAndROARequest;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationTypeAndROAResponse;
import com.param.pharmacy.master.global.dto.formulationTypeAndROA.FormulationTypeAndROAResponseByID;
import com.param.pharmacy.master.global.service.formulationTypeAndROA.IFormulationTypeAndROAService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/globals/formulationTypeAndROA")
public class FormulationTypeAndROAController {

	private static final Logger logger = Logger.getLogger(FormulationTypeAndROAController.class);

	@Autowired
	IFormulationTypeAndROAService FormulationTypeAndROAService;

	@RequestMapping(value = "/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all FormulationTypeAndROA", response = FormulationTypeListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<FormulationTypeAndROAResponse>> GetAll(@PathVariable Integer orgId) {
		ResponseEntity<List<FormulationTypeAndROAResponse>> response = new ResponseEntity<List<FormulationTypeAndROAResponse>>(
				FormulationTypeAndROAService.getAll(orgId), HttpStatus.OK);

		return response;
	}

	/*
	 * @RequestMapping(value = "/save", method = RequestMethod.POST, consumes =
	 * "application/json")
	 * 
	 * @ApiOperation(value = "Save FormulationTypeAndROA", response =
	 * CommonResponse.class)
	 * 
	 * @ApiResponses(value = {
	 * 
	 * @ApiResponse(code = 200, message =
	 * "FormulationTypeAndROA Saved successfully"),
	 * 
	 * @ApiResponse(code = 401, message =
	 * "You are Not authorized to FormulationTypeAndROA"),
	 * 
	 * @ApiResponse(code = 403, message =
	 * "Update FormulationTypeAndROA is forbidden"),
	 * 
	 * @ApiResponse(code = 404, message = "Resource Not found"),
	 * 
	 * @ApiResponse(code = 405, message = "Method Not Allowed") }) public
	 * ResponseEntity<CommonResponse> create(
	 * 
	 * @RequestBody FormulationTypeAndROARequest formulationTypeAndROARequest) {
	 * return new ResponseEntity<CommonResponse>(
	 * FormulationTypeAndROAService.create(formulationTypeAndROARequest),
	 * HttpStatus.CREATED); }
	 */

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save FormulationType and ROA ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveFormulationTypeAndROA(@RequestBody FormulationRoaRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(FormulationTypeAndROAService.saveFormulationTypeAndROA(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update FormulationTypeAndROA", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "FormulationTypeAndROA Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Update FormulationTypeAndROA"),
			@ApiResponse(code = 403, message = "Update FormulationTypeAndROA is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> update(@PathVariable("id") Integer id,
			@RequestBody FormulationTypeAndROARequest formulationTypeAndROARequest) {
		formulationTypeAndROARequest.setId(id);
		try {
			return new ResponseEntity<Object>(FormulationTypeAndROAService.update(formulationTypeAndROARequest),
					HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}
	}

	@RequestMapping(value = "/dropdown/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = FormulationTypeAndROAList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getFormulationTypeAndROA(@PathVariable Integer orgId) throws JSONException {
		return new ResponseEntity<Object>(FormulationTypeAndROAService.getDropdownList(orgId), HttpStatus.OK);
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Tax By Id", response = FormulationTypeAndROAResponseByID.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<FormulationTypeAndROAResponseByID> GetById(@PathVariable("id") Integer id,@PathVariable Integer orgId) {
		ResponseEntity<FormulationTypeAndROAResponseByID> response = new ResponseEntity<FormulationTypeAndROAResponseByID>(
				FormulationTypeAndROAService.getById(id,orgId), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/getCount/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get count By unitid and OrgId ", response = FormulationTypeAndROAResponseByID.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public Long getCount(@PathVariable Integer orgId)
			throws Exception {
		try {
			return FormulationTypeAndROAService.getCount(orgId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/get/formulationTypeAndROA/{orgId}{offset}/{noOfRecordsPerPage}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all FormulationTypeAndROA", response = FormulationTypeListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<FormulationTypeAndROAResponse>> getFormulationTypeAndROA(@PathVariable Integer orgId,@PathVariable("offset") Integer offset,@PathVariable("noOfRecordsPerPage") Integer noOfRecordsPerPage) {
		ResponseEntity<List<FormulationTypeAndROAResponse>> response = new ResponseEntity<List<FormulationTypeAndROAResponse>>(
				FormulationTypeAndROAService.getFormulationTypeAndROA(offset,noOfRecordsPerPage,orgId), HttpStatus.OK);

		return response;
	}
}