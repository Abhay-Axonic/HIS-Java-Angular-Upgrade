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

import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.inventory.store.dto.storeStock.ExpiredStoreStockRequest;
import com.param.inventory.store.dto.storeStock.StoreStockRequest;
import com.param.inventory.store.dto.storeStock.StoreStockRequestDate;
import com.param.inventory.store.dto.storeStock.StoreStockResponse;
import com.param.inventory.store.dto.storeStock.SubstituteStoreStockDashboardDto;
import com.param.inventory.store.service.storeStock.IStoreStockService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stores/stock/dashboard")
public class StoreStockController {

	private static final Logger logger = Logger.getLogger(StoreStockController.class);

	@Autowired
	IStoreStockService StoreStockService;

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of store stock of items", response = StoreStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStoreIndentList(@RequestBody StoreStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.getStoreStock(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the store stock list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/batch/{unitId}/{storeId}/{itemId}/{showDetails}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of batches", response = BatchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getBatchDetails(@PathVariable("storeId") int storeId,@PathVariable int unitId,
			@PathVariable("itemId") int itemId,@PathVariable("showDetails") boolean showDetails) throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.getBatchDetails(storeId, itemId,showDetails,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/batch/zerostock/{unitId}/{storeId}/{itemId}/{showDetails}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of batches", response = BatchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getZeroStockBatchDetails(@PathVariable("storeId") int storeId,
			@PathVariable("itemId") int itemId,@PathVariable("showDetails") boolean showDetails,@PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.getZeroStockBatchDetails(storeId, itemId,showDetails,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/expiredbatch/{unitId}/{storeId}/{itemId}/{showDetails}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of expired batches", response = BatchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getExpiredBatchDetails(@PathVariable Integer unitId,@PathVariable("storeId") int storeId,
			@PathVariable("itemId") int itemId,@PathVariable("showDetails") boolean showDetails) throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.getExpiredBatchDetails(storeId, itemId,showDetails,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/nextexpiredbatch/{unitId}/{storeId}/{itemId}/{month}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of expired batches", response = BatchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getNextExpiredBatchDetails(@PathVariable Integer unitId,@PathVariable("storeId") int storeId,
			@PathVariable("itemId") int itemId,@PathVariable("month") Integer month) throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.getNextExpiredBatchDetails(storeId, itemId,month,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/searchdatewise", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of store stock of items", response = StoreStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStoreIndentDateWiseList(@RequestBody StoreStockRequestDate request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.getStoreIndentDateWiseList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the store stock list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/searchdatewisezerostock", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of store stock of items", response = StoreStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStoreIndentDateWiseZeroStockList(@RequestBody StoreStockRequestDate request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.getStoreIndentDateWiseZeroStockList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the store stock list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchdatewisezerowithstock", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of store stock of items", response = StoreStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStoreIndentDateWiseZeroWikthStockList(@RequestBody StoreStockRequestDate request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.getStoreIndentDateWiseZeroWikthStockList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the store stock list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/item/batch/{storeId}/{itemId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of batches", response = BatchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getItemWiseBatchDetails(@PathVariable("storeId") int storeId,
			@PathVariable("itemId") int itemId) throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.getItemWiseBatchDetails(storeId, itemId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/item/batch/{unitId}/{itemId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of batches", response = BatchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getItemIdWiseBatchDetails(@PathVariable("itemId") int itemId,@PathVariable("unitId") Integer unitId)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.getItemWiseBatchDetailsWithUnit(itemId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/item/batch/batch/{unitId}/{batchId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of batches", response = BatchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getBatchWiseBatchDetails(@PathVariable("batchId") int batchId,@PathVariable("unitId") Integer unitId)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.getBatchWiseBatchDetails(batchId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/expiredstorestockdashboard", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of batches", response = BatchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> expiredStoreStockDashboard(@RequestBody ExpiredStoreStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.expiredStoreStockDashboard(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/nextexpiredstorestockdashboard", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of batches", response = BatchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> nextExpiredStoreStockDashboard(@RequestBody ExpiredStoreStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.nextExpiredStoreStockDashboard(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/stockintransit", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of batches", response = BatchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> stockInTransit(@RequestBody ExpiredStoreStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StoreStockService.stockInTransit(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the batch list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/getSubstituteDetailsForStoreStockDashboard/{genericId}/{itemId}/{isNarcotic}/{storeId}/{unitId}/{dateString}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of substitutes", response = BatchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getSubstituteDetailsForStoreStockDashboard(@PathVariable("genericId") Integer genericId,
            @PathVariable("itemId") Integer itemId, 
            @PathVariable("isNarcotic") Boolean isNarcotic, 
            @PathVariable("storeId") Integer storeId,
			@PathVariable("unitId") Integer unitId,
			@PathVariable("dateString") String dateString
			)
			{
			try {
					return new ResponseEntity<Object>(StoreStockService.getSubstituteDetailsForStoreStockDashboard(genericId, itemId, isNarcotic,storeId,unitId, dateString), HttpStatus.OK);
			} catch (Exception ex) {
			logger.error("Error while getting substitute by id", ex);
			throw new ServiceException(ex.getMessage());
		}
			}
}