package com.param.inventory.store.controller;

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
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.inventory.store.dto.materialIssueAcceptance.MaterialIssueRequest;
import com.param.inventory.store.dto.materialIssueAcceptance.MaterialsDropdownResponse;
import com.param.inventory.store.dto.materialPicker.MaterialPickerRequest;
import com.param.inventory.store.dto.materialPicker.MaterialPickerResponse;
import com.param.inventory.store.service.rejectedMaterialAcceptance.IRejectedMaterialAcceptanceService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stores/reject/material/acceptance")
public class RejectedMaterialAcceptanceController {

	private static final Logger logger = Logger.getLogger(RejectedMaterialAcceptanceController.class);

	@Autowired
	IRejectedMaterialAcceptanceService RejectedMaterialAcceptanceService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = MaterialsDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(RejectedMaterialAcceptanceService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Reject Material Acceptance List by search", response = MaterialPickerResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getRejectMaterialAcceptanceList(
			@RequestBody MaterialPickerRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(
					RejectedMaterialAcceptanceService.getRejectMaterialAcceptanceList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the material issue acceptance list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Reject Material Acceptance details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getRejectMaterialAcceptanceDetails(@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(RejectedMaterialAcceptanceService.getRejectMaterialAcceptanceDetails(id),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the material issue acceptance details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Reject Material Acceptance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveRejectMaterialAcceptance(@PathVariable Integer id,
			@RequestBody MaterialIssueRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(RejectedMaterialAcceptanceService.saveRejectMaterialAcceptance(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating the material issue acceptance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
