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
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionRequest;
import com.param.inventory.nursing.dto.nonBillableConsumption.ConsumptionResponse;
import com.param.inventory.nursing.service.nonBillableConsumption.IConsumptionService;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/nursing/non/billable/consumption")
public class NonBillableConsumptionController implements ICommonConstants{
	private static final Logger logger = Logger.getLogger(NonBillableConsumptionController.class);

	@Autowired
	IConsumptionService ConsumptionService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = StoreIndentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(ConsumptionService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Save department consumption", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> IConsumptionService(@RequestBody ConsumptionRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ConsumptionService.saveConsumption(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving department consumption", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of non billable consumption", response = ConsumptionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsumptionList(@RequestBody ConsumptionRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ConsumptionService.getConsumptionList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching non billable consumption list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get non billable consumption", response = ConsumptionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsumptionDetail(@PathVariable Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId) throws Exception {
		try {
			return new ResponseEntity<Object>(ConsumptionService.getConsumptionDetail(id,orgId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching non billable consumption detail", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "update non billable consumption", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> updateConsumption(@PathVariable Integer id,
			@RequestBody ConsumptionRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ConsumptionService.updateConsumption(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating non billable consumption", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/submit", method = RequestMethod.PATCH)
	@ApiOperation(value = "Submit non billable consumption", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> submitConsumption(@RequestBody ConsumptionRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(ConsumptionService.submitConsumption(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while submitting non billable consumption", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel non billable consumption", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelConsumption(@PathVariable Integer id, @RequestBody ConsumptionRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ConsumptionService.cancelConsumption(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling non billable consumption", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/radiology/getConsumptionDetailsByTemplateTransactionId", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of non billable consumption", response = ConsumptionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody Response getConsumptionDetailsByTemplateTransactionId(@RequestBody ConsumptionRequest request)
			throws Exception {
		try {
			return ConsumptionService.getConsumptionDetailsByTemplateTransactionId(request);
		} catch (Exception ex) {
			logger.error("Error while fetching non billable consumption list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
