package com.param.inventory.nursing.controller;

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
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionDropdownResponse;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionRequest;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionResponse;
import com.param.inventory.nursing.service.consumableAcceptance.IConsumableAcceptanceService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/nursing/consumable/acceptance")
public class ConsumableAcceptanceController {

	private static final Logger logger = Logger.getLogger(ConsumableAcceptanceController.class);

	@Autowired
	IConsumableAcceptanceService ConsumableAcceptanceService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ConsumptionDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(ConsumableAcceptanceService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of consumable acceptance", response = ConsumptionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsumableAcceptanceList(@RequestBody ConsumptionRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ConsumableAcceptanceService.getConsumableAcceptanceList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching consumable acceptance list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get consumable acceptance detail", response = ConsumptionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsumableAcceptanceDetail(@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(ConsumableAcceptanceService.getConsumableAcceptanceDetail(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching consumable acceptance detail", ex);
			throw new ServiceException(ex.getMessage());
		}
	}	
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve consumable acceptance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveBatchChange(@RequestBody ConsumptionRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ConsumableAcceptanceService.approveConsumableAcceptance(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving batch change details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve consumable acceptance details", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveBatchChangeDetails(@PathVariable Integer id, @RequestBody ConsumptionRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ConsumableAcceptanceService.approveConsumableAcceptanceDetails(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving batch change details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel consumable acceptance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelBatchChange(@PathVariable Integer id, @RequestBody ConsumptionRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ConsumableAcceptanceService.cancelConsumableAcceptance(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling batch change request", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
