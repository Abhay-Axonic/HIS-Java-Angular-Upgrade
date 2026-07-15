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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import org.apache.log4j.Logger;

import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.inventory.stockAdjustments.dto.StockAdjustmentDropdownResponse;
import com.param.inventory.stockAdjustments.dto.confirmStock.ConfirmStockRequestDto;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;
import com.param.inventory.stockAdjustments.service.confirmStock.IConfirmStockService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/inventory/stock/adjusments/confirm/stock")
public class ConfirmStockController {

	private static final Logger logger = Logger.getLogger(ConfirmStockController.class);

	@Autowired
	IConfirmStockService ConfirmStockService;
	
	@RequestMapping(value = "/dropdown/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of dropdown", response = StockAdjustmentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(ConfirmStockService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while getting Confirm Stock dropdown", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Confirm Stock list", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConfirmStockList(@RequestBody UploadStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ConfirmStockService.getConfirmStockList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching Confirm Stock list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Confirm Stock details", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConfirmStockDetails(@PathVariable Integer id , @PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(ConfirmStockService.getConfirmStock(id,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching Confirm Stock details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Confirm Stock ", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> approveConfirmStock(@RequestBody ConfirmStockRequestDto confirmStockRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(ConfirmStockService.approveConfirmStock(confirmStockRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Approve Confirm Stock ", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancelled Confirm Stock details", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> cancelledConfirmStock(@RequestBody ConfirmStockRequestDto confirmStockRequest,@PathVariable Integer id) throws Exception {
		try {
			confirmStockRequest.setId(id);
			return new ResponseEntity<Object>(ConfirmStockService.cancelConfirmStock(confirmStockRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Cancelled Confirm Stock details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Details Approved Confirm Stock details", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> detailApprovedConfirmStock(@RequestBody ConfirmStockRequestDto confirmStockRequest,@PathVariable Integer id) throws Exception {
		try {
			confirmStockRequest.setId(id);
			return new ResponseEntity<Object>(ConfirmStockService.detailApproveConfirmStock(confirmStockRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Details Approved Confirm Stock ", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/rejectconfirmstock/{id}/{unitId}/{statusId}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Reject Confirm Stock", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> RejectConfirmStock(@PathVariable Integer id, @PathVariable Integer unitId ,@PathVariable Integer statusId) throws Exception {
		try {
			return new ResponseEntity<Object>(ConfirmStockService.RejectConfirmStock(id, unitId,statusId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while rejecting Confirm Stock", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
