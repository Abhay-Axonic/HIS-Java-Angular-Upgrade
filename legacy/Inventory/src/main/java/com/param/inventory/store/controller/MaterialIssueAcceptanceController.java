package com.param.inventory.store.controller;

import org.apache.log4j.Logger;
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
import com.param.inventory.store.dto.materialIssueAcceptance.MaterialIssueRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.service.materialIssueAcceptance.IMaterialIssueAcceptanceService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stores/material/issue/acceptance")
public class MaterialIssueAcceptanceController {

	private static final Logger logger = Logger.getLogger(MaterialIssueAcceptanceController.class);

	@Autowired
	IMaterialIssueAcceptanceService MaterialIssueAcceptanceService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = StoreIndentDropdownResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialIssueAcceptanceService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Material Issue Acceptance List by search", response = MaterialPickerResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getMaterialIssueAcceptanceList(@RequestBody MaterialPickerRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialIssueAcceptanceService.getMaterialIssueAcceptanceList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Material Issue Acceptance list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Material Issue Acceptance", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveMaterialIssueAcceptance(@PathVariable Integer id, @RequestBody MaterialIssueRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(MaterialIssueAcceptanceService.saveMaterialIssueAcceptance(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Updating the Material Issue Acceptance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Material Issue Acceptance List details by id", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getMaterialIssueAcceptanceDetails(@PathVariable Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialIssueAcceptanceService.getMaterialIssueAcceptanceDetails(id,orgId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Material Issue Acceptance Details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
