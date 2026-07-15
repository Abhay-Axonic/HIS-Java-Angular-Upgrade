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
import com.param.inventory.store.dto.shelfAllocation.ShelfAllocationRequest;
import com.param.inventory.store.dto.shelfAllocation.ShelfAllocationResponse;
import com.param.inventory.store.service.shelfAllocation.IShelfAllocationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stores/shelf/allocation")
public class ShelfAllocationController {

	private static final Logger logger = Logger.getLogger(ShelfAllocationController.class);

	@Autowired
	IShelfAllocationService ShelfAllocationService;

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of allocated items", response = ShelfAllocationResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getItemList(@RequestBody ShelfAllocationRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ShelfAllocationService.getItemList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching allocated items", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Allocate shelf to the item", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> allocateShelf(@RequestBody ShelfAllocationRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ShelfAllocationService.allocateShelf(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while allocating shelf", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{storeId}/{rackId}/{shelfId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get allocated items", response = ShelfAllocationResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getAllocatedItems(@PathVariable Integer unitId, @PathVariable Integer storeId, @PathVariable Integer rackId,
			@PathVariable Integer shelfId) throws Exception {
		try {
			return new ResponseEntity<Object>(ShelfAllocationService.getAllocatedItems(storeId, rackId, shelfId,unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching allocated items", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/status/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update status", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateStatus(@PathVariable Integer id, @RequestBody ShelfAllocationRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ShelfAllocationService.updateStatus(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating status", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Get allocated item", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateAllocatedItem(@PathVariable Integer id, @RequestBody ShelfAllocationRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ShelfAllocationService.updateAllocatedItem(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating allocated item", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
