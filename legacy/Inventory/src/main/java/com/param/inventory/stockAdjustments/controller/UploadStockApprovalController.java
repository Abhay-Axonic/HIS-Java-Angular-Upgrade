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

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.inventory.stockAdjustments.dto.StockAdjustmentDropdownResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;
import com.param.inventory.stockAdjustments.service.uploadStockApproval.IUploadStockApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/inventory/stock/adjusments/upload/stock/approval")
public class UploadStockApprovalController {

	private static final Logger logger = Logger.getLogger(UploadStockApprovalController.class);

	@Autowired
	IUploadStockApprovalService UploadStockApprovalService;

	@RequestMapping(value = "/dropdown/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of dropdown", response = StockAdjustmentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(UploadStockApprovalService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while getting upload stock approval dropdown", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Upload Stock Approval list", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getUploadStockApprovalList(@RequestBody UploadStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(UploadStockApprovalService.getUploadStockApprovalList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching Upload Stock Approval list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Upload Stock Approval details", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getUploadStockApprovalDetails(@PathVariable Integer id)
			throws Exception {
		try {
			return new ResponseEntity<Object>(UploadStockApprovalService.getUploadStockApprovalDetails(id),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching Upload Stock Approval details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve upload stock", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> uploadStockApproval(@RequestBody UploadStockRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(UploadStockApprovalService.uploadStockApproval(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving upload stock", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Upload Stock Approval quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveUploadStockApproval(@PathVariable Integer id,
			@RequestBody UploadStockRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(UploadStockApprovalService.approveUploadStockApproval(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Upload Stock Approval", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/newapprove/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Upload Stock Approval quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveUploadStockNewApproval(@PathVariable Integer id,
			@RequestBody UploadStockRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(UploadStockApprovalService.approveUploadStockNewApproval(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Upload Stock Approval", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Upload Stock Approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelUploadStockApproval(@PathVariable Integer id, @RequestBody UploadStockRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(UploadStockApprovalService.cancelUploadStockApproval(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Upload Stock Approval", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
