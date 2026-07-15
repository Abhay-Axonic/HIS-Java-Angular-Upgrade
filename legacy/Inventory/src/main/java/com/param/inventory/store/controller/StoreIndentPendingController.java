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
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.dto.storeIndent.StoreIndentRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentResponse;
import com.param.inventory.store.dto.storeIndentPending.StoreIndentPendingRequest;
import com.param.inventory.store.service.storeIndentPending.IStoreIndentPendingService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stores/indents/pending")
public class StoreIndentPendingController {

	private static final Logger logger = Logger.getLogger(StoreIndentPendingController.class);

	@Autowired
	IStoreIndentPendingService StoreIndentPendingService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = StoreIndentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(StoreIndentPendingService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of store indent pending by search", response = StoreIndentResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStoreIndentList(@RequestBody StoreIndentRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StoreIndentPendingService.getStoreIndentList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the pending store indent list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get store indent pending details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getStoreIndent(@PathVariable Integer id,@PathVariable Integer orgId,@PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(StoreIndentPendingService.getStoreIndent(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the pending store indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/close/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Close pending store indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> closeStoreIndent(@PathVariable Integer id,
			@RequestBody StoreIndentPendingRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(StoreIndentPendingService.closeStoreIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while closing the store indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
