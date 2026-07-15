package com.param.inventory.stockAdjustments.controller;

import java.util.ArrayList;

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
import com.param.inventory.stockAdjustments.dto.stopTransaction.ProductCategoryResponse;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionRequest;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionResponse;
import com.param.inventory.stockAdjustments.dto.stopTransaction.StopTransactionSearchResponse;
import com.param.inventory.stockAdjustments.service.stopTransaction.IStopTransactionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stock/adjustment/stop/transaction")
public class StopTransactionController {

	private static final Logger logger = Logger.getLogger(StopTransactionController.class);

	@Autowired
	IStopTransactionService StopTransactionService;

	@RequestMapping(value = "/add/new", method = RequestMethod.POST)
	@ApiOperation(value = "save stop transaction", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveAddNewStopTransaction(@RequestBody StopTransactionRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StopTransactionService.saveAddNewStopTransaction(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the stop transaction", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "save stop transaction", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveStopTransaction(@PathVariable Integer id,
			@RequestBody StopTransactionRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(StopTransactionService.saveStopTransaction(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the stop transaction", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of stop transaction by search", response = StopTransactionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStopTransactionList(@RequestBody StopTransactionRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StopTransactionService.getStopTransactionList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the stop transaction list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/add/new/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of stop transaction by search", response = StopTransactionSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStopTransactionNewList(@RequestBody StopTransactionRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StopTransactionService.getStopTransactionNewList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the stop transaction list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/add/new/nonzerosearch", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of stop transaction by search", response = StopTransactionSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStopTransactionNonZeroNewList(@RequestBody StopTransactionRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StopTransactionService.getStopTransactionNonZeroNewList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the stop transaction list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/productCategory/search/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of product category", notes = "Get list of product category", response = ProductCategoryResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getproductCategoryAutoPopulateList(@PathVariable("name") String name)
			throws Exception {
		return new ResponseEntity<Object>(StopTransactionService.getProductCategoryList(name), HttpStatus.OK);

	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel stop transaction", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelStopTransaction(@PathVariable Integer id,
			@RequestBody StopTransactionRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(StopTransactionService.cancelStopTransaction(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the stop transaction", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Stop Transaction details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getStopTransaction(@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(StopTransactionService.getStopTransaction(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Stop Transaction", ex);
			throw new ServiceException(ex.getMessage());
		}
	}	
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Stop Transaction ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveStopTransaction(@RequestBody StopTransactionRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(StopTransactionService.approveStopTransaction(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the Stop Transaction ", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
		
	@RequestMapping(value = "/stockTransit/{storeId}", method = RequestMethod.POST)
	@ApiOperation(value = "Stock in Transit Check", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> ChecStockInTransit(@PathVariable Integer storeId) throws Exception {
		try {		
			return new ResponseEntity<Object>(StopTransactionService.ChecStockInTransit(storeId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Checking Stock in Transit", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/CheckStopTransaction", method = RequestMethod.PATCH)
	@ApiOperation(value = "Stop Transaction Check Item Wise", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> CheckStopTransactionItemWise(@RequestBody StopTransactionRequest request) throws Exception {
		try {
				return new ResponseEntity<Object>(StopTransactionService.CheckStopTransactionItemWise(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Checking Stop Transaction Item Wise", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/CheckStopTransactionBatchWise", method = RequestMethod.PATCH)
	@ApiOperation(value = "Stop Transaction Check BatchWise", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> CheckStopTransactionBatchWise(@RequestBody StopTransactionRequest request) throws Exception {
		try {
				return new ResponseEntity<Object>(StopTransactionService.CheckStopTransactionBatchWise(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Checking Stop Transaction Batch Wise", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/sendAlert/{storeId}/{unitId}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Send Stop Transaction Alert", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> sendStopTransactionAlert(@PathVariable Integer storeId , @PathVariable Integer  unitId) throws Exception {
		try {		
			return new ResponseEntity<Object>(StopTransactionService.sendStopTransactionAlert(storeId, unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while sending Stop Transaction Alert", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/removeItembyProduct/{productId}/{stopTransactionId}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Remove Item By Product Category", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> removeItemsByProductId(@PathVariable Integer productId,@PathVariable Integer stopTransactionId,@RequestBody StopTransactionRequest request) throws Exception {
		try {		
			return new ResponseEntity<Object>(StopTransactionService.removeItemsByProductId(productId,stopTransactionId,request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Remove Item By Product Category", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/removeItembyItemId", method = RequestMethod.PATCH)
	@ApiOperation(value = "Remove Item By ItemId", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> removeItembyItemId(@RequestBody StopTransactionRequest request) throws Exception {
		try {
				return new ResponseEntity<Object>(StopTransactionService.removeItembyItemId(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Remove Item By ItemId", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/CheckStopTransactionDuration/{storeId}/{organizationId}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Stop Transaction Check for Store", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> CheckStopTransactionDuration(@PathVariable Integer storeId , @PathVariable Integer organizationId , @PathVariable Integer unitId ) throws Exception {
		try {
				return new ResponseEntity<Object>(StopTransactionService.CheckStopTransactionDuration(storeId,organizationId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Checking Stop Transaction for Store", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	//start code for resolved HIS-10092 While selecting start date time and stop date time then wrong validation showing.
	@RequestMapping(value = "/CheckStopTransactionDuration", method = RequestMethod.POST)
	@ApiOperation(value = "Stop Transaction Check for Store", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> CheckStopTransactionDuration(@RequestBody StopTransactionRequest request) throws Exception {
		try {
				return new ResponseEntity<Object>(StopTransactionService.CheckStopTransactionDuration(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Checking Stop Transaction for Store", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	//end code for resolved HIS-10092 While selecting start date time and stop date time then wrong validation showing.
	
	/* start code for resolved HIS-10094 Store / Inventory - Without add stop transaction details able to save request for stop transaction */
	@RequestMapping(value = "/CheckStopTransactionDetail", method = RequestMethod.POST)
	@ApiOperation(value = "Check Stop Transaction Detail", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> CheckStopTransactionDetail(@RequestBody StopTransactionRequest request) throws Exception {
		try {
				return new ResponseEntity<Object>(StopTransactionService.CheckStopTransactionDetail(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the Stop Transaction ", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	/* end code for resolved HIS-10094 Store / Inventory - Without add stop transaction details able to save request for stop transaction */

}