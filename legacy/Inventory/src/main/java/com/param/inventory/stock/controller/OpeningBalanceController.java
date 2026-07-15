package com.param.inventory.stock.controller;

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
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceRequest;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceResponse;
import com.param.inventory.stock.service.openingBalance.IOpeningBalanceService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stock/opening/balances")
public class OpeningBalanceController {

	private static final Logger logger = Logger.getLogger(OpeningBalanceController.class);

	@Autowired
	IOpeningBalanceService OpeningBalanceService;

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of opening balance by search", response = OpeningBalanceResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getOpeningBalanceList(@RequestBody OpeningBalanceRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(OpeningBalanceService.getOpeningBalanceList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the opening balance list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save opening balance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveOpeningBalance(@RequestBody OpeningBalanceRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(OpeningBalanceService.saveOpeningBalance(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the opening balance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Opening Balance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateOpeningBalance(@PathVariable Integer id,
			@RequestBody OpeningBalanceRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(OpeningBalanceService.updateOpeningBalance(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating the opening balance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get opening balance by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getOpeningBalanceByid(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(OpeningBalanceService.getOpeningBalancePT(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the opening balance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Opening Balance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveOpeningBalance(@PathVariable Integer id,
			@RequestBody OpeningBalanceRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(OpeningBalanceService.approveOpeningBalanceDetail(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the opening balance detail", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Opening Balance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<Object> approveOpeningBalance(@RequestBody OpeningBalanceRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(OpeningBalanceService.approveOpeningBalance(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the opening balance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Opening Balances", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelStoreIndent(@PathVariable Integer id,
			@RequestBody OpeningBalanceRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(OpeningBalanceService.cancelOpeningBalance(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling the opening balance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
