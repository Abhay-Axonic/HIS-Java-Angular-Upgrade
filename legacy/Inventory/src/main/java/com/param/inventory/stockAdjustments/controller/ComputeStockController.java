package com.param.inventory.stockAdjustments.controller;

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
import com.param.inventory.stockAdjustments.dto.ComputeStockDropdownResponse;
import com.param.inventory.stockAdjustments.dto.computeStock.ComputeStockRequest;
import com.param.inventory.stockAdjustments.dto.computeStock.ComputeStockResponse;
import com.param.inventory.stockAdjustments.service.computeStock.IComputeStockService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/inventory/stock/adjustment/compute/stock")
public class ComputeStockController {

	private static final Logger logger = Logger.getLogger(ComputeStockController.class);

	@Autowired
	IComputeStockService ComputeStockService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ComputeStockDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(ComputeStockService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of compute stock by search", response = ComputeStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getComputeStockList(@RequestBody ComputeStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ComputeStockService.getComputeStockList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the compute stock list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Compute Stock", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelComputeStock(@PathVariable Integer id, @RequestBody ComputeStockRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ComputeStockService.cancelComputeStock(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Cancelling the Compute Stock", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get compute stock details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getComputeStock(@PathVariable Integer id)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ComputeStockService.getComputeStock(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the compute stock", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Save Compute Stock", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveComputeStock(@PathVariable Integer id, @RequestBody ComputeStockRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ComputeStockService.saveComputeStock(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the Compute Stock", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Compute Stock", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveComputeStock(@RequestBody ComputeStockRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(ComputeStockService.approveComputeStock(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the Compute Stock", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
}
