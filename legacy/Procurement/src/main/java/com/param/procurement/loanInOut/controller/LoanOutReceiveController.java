package com.param.procurement.loanInOut.controller;

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
import com.param.procurement.loanInOut.dto.LoanInOutDropdown;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;
import com.param.procurement.loanInOut.service.loanOutReceive.ILoanOutReceiveService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/procurements/loanInOut/loan/out/receive")

public class LoanOutReceiveController {

	final static Logger logger = Logger.getLogger(LoanOutReceiveController.class);

	@Autowired
	ILoanOutReceiveService LoanOutReceiveService;
	
	@RequestMapping(value = "/dropdown/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of dropdown", response = LoanInOutDropdown.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(LoanOutReceiveService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get loan out receive list", response = LoanInOutResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLoanOutReceiveList(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(LoanOutReceiveService.getLoanOutReceiveList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLoanOutReceiveList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get loan out receive details", response = LoanInOutResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLoanOutReceiveDetails(@PathVariable Integer id,@PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(LoanOutReceiveService.getLoanOutReceiveDetails(id,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLoanOutReceiveDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save loan out receive", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveLoanOutReceive(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(LoanOutReceiveService.saveLoanOutReceive(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveLoanOutReceive failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/get/item", method = RequestMethod.POST)
	@ApiOperation(value = "Get loan out receive item list", response = LoanInOutResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLoanOutReceiveItemList(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(LoanOutReceiveService.getLoanOutReceiveItemList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLoanOutReceiveItemList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/add/item", method = RequestMethod.POST)
	@ApiOperation(value = "Get loan out receive item list", response = LoanInOutResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLoanOutReceiveAddItemList(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(LoanOutReceiveService.getLoanOutReceiveAddItemList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLoanOutReceiveAddItemList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Loan Out Receive", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelLoanOutReceive(@PathVariable Integer id, @RequestBody LoanInOutRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(LoanOutReceiveService.cancelLoanOutReceive(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelLoanOutReceive failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve loan out Receive", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveLoanOutReceive(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(LoanOutReceiveService.approveLoanOutReceive(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveLoanOutReceive failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Loan Out Receive", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateLoanOutReceive(@PathVariable Integer id, @RequestBody LoanInOutRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(LoanOutReceiveService.updateLoanOutReceive(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateLoanOutReceive failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
