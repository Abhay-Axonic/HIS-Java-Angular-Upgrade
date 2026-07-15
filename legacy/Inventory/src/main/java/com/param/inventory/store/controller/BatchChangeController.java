
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
import com.param.inventory.store.dto.batchChange.BatchChangeRequest;
import com.param.inventory.store.dto.batchChange.BatchChangeResponse;
import com.param.inventory.store.service.batchChange.IBatchChangeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stores/batch/change")
public class BatchChangeController {

	private static final Logger logger = Logger.getLogger(BatchChangeController.class);

	@Autowired
	IBatchChangeService BatchChangeService;

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Save batch expiry change request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveBatchChange(@RequestBody BatchChangeRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(BatchChangeService.saveBatchChange(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving batch expiry change request", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of batch expiry change request", response = BatchChangeResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getBatchChangeList(@RequestBody BatchChangeRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(BatchChangeService.getBatchChangeList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch expiry change list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get batch expiry change request", response = BatchChangeResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getBatchChangeDetail(@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(BatchChangeService.getBatchChangeDetail(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch expiry change detail", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "update batch expiry change request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> updateBatchChange(@PathVariable Integer id,
			@RequestBody BatchChangeRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(BatchChangeService.updateBatchChange(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating batch expiry change request", ex);
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
			return new ResponseEntity<Object>(BatchChangeService.cancelBatchChange(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling batch change request", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/submit", method = RequestMethod.PATCH)
	@ApiOperation(value = "Submit batch change request for approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> submitBatchChange(@RequestBody BatchChangeRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(BatchChangeService.submitBatchChange(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while submitting the batch change request", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
