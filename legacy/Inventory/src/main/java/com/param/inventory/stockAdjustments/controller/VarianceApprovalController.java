package com.param.inventory.stockAdjustments.controller;

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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.inventory.stockAdjustments.dto.StockAdjustmentDropdownResponse;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockRequest;
import com.param.inventory.stockAdjustments.dto.uploadStock.UploadStockResponse;
import com.param.inventory.stockAdjustments.service.varianceApproval.IVarianceApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/inventory/stock/adjusments/variance/approval")
public class VarianceApprovalController {

	private static final Logger logger = Logger.getLogger(VarianceApprovalController.class);

	@Autowired
	IVarianceApprovalService VarianceApprovalService;

	@RequestMapping(value = "/dropdown/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of dropdown", response = StockAdjustmentDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(VarianceApprovalService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while getting Variance Approval dropdown", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Variance Approval list", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getVarianceApprovalList(@RequestBody UploadStockRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(VarianceApprovalService.getVarianceApprovalList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching Variance Approval list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Variance Approval details", response = UploadStockResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getVarianceApprovalDetails(@PathVariable Integer id , @PathVariable Integer unitId ) throws Exception {
		try {
			return new ResponseEntity<Object>(VarianceApprovalService.getVarianceApprovalDetails(id , unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching Variance Approval details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Variance Approval quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveVarianceApproval(@PathVariable Integer id,
			@RequestBody UploadStockRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(VarianceApprovalService.approveVarianceApproval(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the Variance Approval", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Variance Approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelVarianceApproval(@PathVariable Integer id,
			@RequestBody UploadStockRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(VarianceApprovalService.cancelVarianceApproval(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling the Variance Approval", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
