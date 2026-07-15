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

import org.apache.log4j.Logger;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dto.vendor.VendorDetailsResponse;
import com.param.pharmacy.master.unit.dto.vendor.VendorRequest;
import com.param.pharmacy.master.unit.generalinsuranceservice.IGeneralInsuranceMasterService;
import com.param.pharmacy.master.unit.genralinsurancedto.GeneralInsuranceDetailsResponse;
import com.param.pharmacy.master.unit.genralinsurancedto.GeneralInsuranceRequest;
import com.param.pharmacy.master.unit.genralinsurancedto.GenralInsuranceListResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/unit/generalinsurance")
public class GeneralInsuranceMasterController 
{
	@Autowired
	IGeneralInsuranceMasterService generalInsuranceService;

	private static final Logger logger = Logger.getLogger(GeneralInsuranceMasterController.class);

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Genral Insurance List", response = GenralInsuranceListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<GenralInsuranceListResponse>> GetAll() {
		ResponseEntity<List<GenralInsuranceListResponse>> response = new ResponseEntity<List<GenralInsuranceListResponse>>(
				generalInsuranceService.getAll(), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "save Insuran Company Master", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Insuran Company saved successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Vendors"),
			@ApiResponse(code = 403, message = "Create Insuran Company is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Save(@RequestBody GeneralInsuranceRequest generalInsuranceRequest) {
		return new ResponseEntity<CommonResponse>(generalInsuranceService.save(generalInsuranceRequest), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Insurance Company By Id", response = GeneralInsuranceDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<GeneralInsuranceDetailsResponse> GetById(@PathVariable("id") Integer id) {
		ResponseEntity<GeneralInsuranceDetailsResponse> response = new ResponseEntity<GeneralInsuranceDetailsResponse>(
				generalInsuranceService.getById(id), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Insurance Company", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Insurance Company Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Insurance Company"),
			@ApiResponse(code = 403, message = "Update Insurance Company is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id, @RequestBody GeneralInsuranceRequest generalInsuranceRequest) {
		generalInsuranceRequest.setId(id);
		try {
			return new ResponseEntity<Object>(generalInsuranceService.update(generalInsuranceRequest), HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}
	}

}
