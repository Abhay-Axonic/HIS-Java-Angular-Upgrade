package com.param.assetManagement.nonReturnableGatepass.controller;

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

import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssRequest;
import com.param.assetManagement.nonReturnableGatepass.dto.NonReturnableGatepssResponse;
import com.param.assetManagement.nonReturnableGatepass.service.INonReturnableGatepssService;
import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.CommonDropdown;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/asset/non/returnable/gatepass/")
public class NonReturnableGatepssController {

	private static final Logger logger = Logger.getLogger(NonReturnableGatepssController.class);

	@Autowired
	INonReturnableGatepssService nonReturnableGatepssService;

	@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = CommonDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList() {
		try {
			return new ResponseEntity<Object>(nonReturnableGatepssService.getDropdownList(), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while getting dropdowns by screen id", ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Non Returnable Gatepss", response = NonReturnableGatepssResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getNonReturnableGatepssList(
			@RequestBody NonReturnableGatepssRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(nonReturnableGatepssService.getNonReturnableGatepssList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Non Returnable Gatepss list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Non Returnable Gatepss by id", response = NonReturnableGatepssResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getAuctionVendorApprovalById(@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(nonReturnableGatepssService.getNonReturnableGatepssById(id),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Non Returnable Gatepss", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save Non Returnable Gatepss", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveNonReturnableGatepass(@RequestBody NonReturnableGatepssRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(nonReturnableGatepssService.saveNonReturnableGatepass(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the Non Returnable Gatepss", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/get/request/{unitId}/{orgId}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get Non Returnable Gatepss", response = NonReturnableGatepssResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getRequestNonReturnableGatepss(
			@RequestBody NonReturnableGatepssRequest request,@PathVariable Integer unitId,@PathVariable Integer orgId) throws Exception {
		try {
			return new ResponseEntity<Object>(nonReturnableGatepssService.getRequestNonReturnableGatepss(request,unitId,orgId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Non Returnable Gatepss", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Non Returnable Gatepss", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelNonReturnableGatepass(@PathVariable Integer id,
			@RequestBody NonReturnableGatepssRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(nonReturnableGatepssService.cancelNonReturnableGatepass(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling the Non Returnable Gatepss", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Non Returnable Gatepss", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveNonReturnableGatepass(@RequestBody NonReturnableGatepssRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(nonReturnableGatepssService.approveNonReturnableGatepass(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the Non Returnable Gatepss", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/update", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Non Returnable Gatepss", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateNonReturnableGatepass(@RequestBody NonReturnableGatepssRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(nonReturnableGatepssService.updateNonReturnableGatepass(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating the Non Returnable Gatepss", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}