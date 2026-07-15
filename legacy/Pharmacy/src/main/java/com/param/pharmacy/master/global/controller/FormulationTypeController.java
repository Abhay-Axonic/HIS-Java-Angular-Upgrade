package com.param.pharmacy.master.global.controller;

import java.util.List;

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
import org.apache.log4j.Logger;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeDetailsResponse;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeListResponse;
import com.param.pharmacy.master.global.dto.formulationType.FormulationTypeRequest;
import com.param.pharmacy.master.global.dto.tax.TaxResponseList;
import com.param.pharmacy.master.global.service.formulationType.IFormulationTypeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/globals/formulationTypes")
public class FormulationTypeController {

	private static final Logger logger = Logger.getLogger(FormulationTypeController.class);

	@Autowired
	IFormulationTypeService FormulationTypeService;

	/*
	 * @RequestMapping(value = "/dropdown", method = RequestMethod.GET)
	 * 
	 * @ApiOperation(value = "Get list for populating dropdown values", response =
	 * TaxResponseList.class)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
	 * 
	 * @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
	 * 403, message = "forbidden!!!"),
	 * 
	 * @ApiResponse(code = 404, message = "not found!!!") }) public @ResponseBody
	 * ResponseEntity<Object> getFormulationType() throws JSONException { return new
	 * ResponseEntity<Object>(FormulationTypeService.getDropdownList(),
	 * HttpStatus.OK); }
	 */

	@RequestMapping(value = "/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all FormulationType", response = FormulationTypeListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<FormulationTypeListResponse>> GetAll(@PathVariable Integer orgId) {
		ResponseEntity<List<FormulationTypeListResponse>> response = new ResponseEntity<List<FormulationTypeListResponse>>(
				FormulationTypeService.getAll(orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get FormulationType By Id", response = FormulationTypeDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<FormulationTypeDetailsResponse> getById(@PathVariable Integer orgId,@PathVariable("id") Integer id) {

		ResponseEntity<FormulationTypeDetailsResponse> response = new ResponseEntity<FormulationTypeDetailsResponse>(
				FormulationTypeService.getById(id,orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = " Create FormulationType ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "FormulationType Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Create FormulationType"),
			@ApiResponse(code = 403, message = "Create FormulationType is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> create(@RequestBody FormulationTypeRequest formulationTypeRequest) {
		return new ResponseEntity<CommonResponse>(FormulationTypeService.create(formulationTypeRequest),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update FormulationType", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "FormulationType Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Update FormulationType"),
			@ApiResponse(code = 403, message = "Update FormulationType is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> update(@PathVariable("id") Integer id,
			@RequestBody FormulationTypeRequest formulationTypeRequest) {
		formulationTypeRequest.setId(id);
		try {
			return new ResponseEntity<Object>(FormulationTypeService.update(formulationTypeRequest),
					HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}
	}

}
