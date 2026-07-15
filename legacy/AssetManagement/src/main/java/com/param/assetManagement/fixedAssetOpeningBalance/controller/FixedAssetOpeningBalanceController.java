package com.param.assetManagement.fixedAssetOpeningBalance.controller;

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

import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceDetailsResponse;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceDropdown;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceResponse;
import com.param.assetManagement.fixedAssetOpeningBalance.service.IFixedAssetOpeningBalanceService;
import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/asset/fixedAsset/addFixedAsset")
public class FixedAssetOpeningBalanceController {

	private static final Logger logger = Logger.getLogger(FixedAssetOpeningBalanceController.class);

	@Autowired
	IFixedAssetOpeningBalanceService fixedAssetOpeningBalanceService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = FixedAssetOpeningBalanceDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(fixedAssetOpeningBalanceService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while getting dropdowns by screen id", ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of fixed asset opring balance by search", response = FixedAssetOpeningBalanceResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getOpeningBalanceList(
			@RequestBody FixedAssetOpeningBalanceRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(fixedAssetOpeningBalanceService.getOpeningBalanceList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the fixed asset opening balance list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get fixed asset opring balance by id", response = FixedAssetOpeningBalanceDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getOpeningBalanceById(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(fixedAssetOpeningBalanceService.getOpeningBalance(unitId,orgId,id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the fixed asset opening balance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save fixed asset opring balance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveOpeningBalance(@RequestBody FixedAssetOpeningBalanceRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(fixedAssetOpeningBalanceService.saveOpeningBalance(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the fixed asset opening balance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve fixed asset opring balance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveOpeningBalance(@PathVariable Integer id,
			@RequestBody FixedAssetOpeningBalanceRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(fixedAssetOpeningBalanceService.approveOpeningBalanceDetail(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the fixed asset opening balance detail", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel fixed asset opring balance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelOpeningBalance(@PathVariable Integer id,
			@RequestBody FixedAssetOpeningBalanceRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(fixedAssetOpeningBalanceService.cancelOpeningBalance(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling the fixed asset opening balance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve fixed asset opring balance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveOpeningBalance(@RequestBody FixedAssetOpeningBalanceRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(fixedAssetOpeningBalanceService.approveOpeningBalance(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the fixed asset opening balance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update fixed asset opring balance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateOpeningBalance(@PathVariable Integer id,
			@RequestBody FixedAssetOpeningBalanceRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(fixedAssetOpeningBalanceService.updateOpeningBalance(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating the fixed asset opening balance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}