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

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.inventory.store.dto.batchChange.BatchChangeRequest;
import com.param.inventory.store.dto.batchChange.BatchChangeResponse;
import com.param.inventory.store.service.batchChangeApproval.IBatchChangeApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/inventory/stores/batch/change/approval")
public class BatchChangeApprovalController {

	private static final Logger logger = Logger.getLogger(BatchChangeApprovalController.class);

	@Autowired
	IBatchChangeApprovalService BatchChangeApprovalService;
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of batch expiry change request", response = BatchChangeResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getBatchChangeList(@RequestBody BatchChangeRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(BatchChangeApprovalService.getBatchChangeList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch expiry change list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of batch expiry change request", response = BatchChangeResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getBatchChangeDetails(@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(BatchChangeApprovalService.getBatchChangeDetails(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch expiry change detail", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve batch change details", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveBatchChange(@RequestBody BatchChangeRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(BatchChangeApprovalService.approveBatchChange(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving batch change details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve batch change details", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveBatchChangeDetails(@PathVariable Integer id, @RequestBody BatchChangeRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(BatchChangeApprovalService.approveBatchChangeDetails(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving batch change details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel batch change", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelBatchChange(@PathVariable Integer id, @RequestBody BatchChangeRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(BatchChangeApprovalService.cancelBatchChange(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling batch change request", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
