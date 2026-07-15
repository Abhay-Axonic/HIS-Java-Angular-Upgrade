package com.param.pharmacy.nursing.crashCart.controller;

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
import com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest;
import com.param.pharmacy.nursing.crashCart.dto.CrashCartResponse;
import com.param.pharmacy.nursing.crashCart.service.crashCartApproval.ICrashCartApprovalService;
import com.param.global.common.ServiceException;
import com.param.global.service.items.CustomException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/pharmacy/nursing/crashCart/crash/cart/request/approval")
public class CrashCartRequestApproval {

	private static final Logger logger = Logger.getLogger(CrashCartRequestApproval.class);

	@Autowired
	ICrashCartApprovalService CrashCartApprovalService;

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Crash Cart Request Approval by search", response = CrashCartResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getCrashCartApprovalList(@RequestBody CrashCartRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(CrashCartApprovalService.getCrashCartApprovalList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Crash Cart Request Approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveCrashCartApproval(@RequestBody CrashCartRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(CrashCartApprovalService.saveCrashCartApproval(request), HttpStatus.OK);
		} catch (CustomException ex) { //added CustomException code for resolved HIS-10354 Crash Cart Request Approval -
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Crash Cart Request Approval details ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> crashCartApprovalDetails(@PathVariable("id") Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId) {
		try {
			return new ResponseEntity<Object>(CrashCartApprovalService.crashCartApprovalDetails(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
