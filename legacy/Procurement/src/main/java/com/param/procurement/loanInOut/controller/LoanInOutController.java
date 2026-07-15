package com.param.procurement.loanInOut.controller;

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
import com.param.procurement.loanInOut.dto.LoanInOutDropdown;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;
import com.param.procurement.loanInOut.service.loanInOut.ILoanInOutService;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionDetails;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/loan/in/out")
public class LoanInOutController {

	final static Logger logger = Logger.getLogger(LoanInOutController.class);

	@Autowired
	ILoanInOutService loanInOutService;

	@RequestMapping(value = "/dropdown/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of dropdown", response = LoanInOutDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(loanInOutService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get loan in out list", response = LoanInOutResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLoanInOutList(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(loanInOutService.getLoanInOutList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLoanInOutList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get loan in out details", response = LoanInOutResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLoanInOutDetails(@PathVariable Integer id,@PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(loanInOutService.getLoanInOutDetails(id,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLoanInOutDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save loan in out", response = PhysicalInspectionDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveLoanInOut(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(loanInOutService.saveLoanInOut(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveLoanInOut failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel loan in out", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelLoanInOut(@PathVariable Integer id,
			@RequestBody LoanInOutRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(loanInOutService.cancelLoanInOut(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelLoanInOut failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve loan in out", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveLoanInOut(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(loanInOutService.approveLoanInOut(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveLoanInOut failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update loan in out", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateLoanInOut(@PathVariable Integer id,
			@RequestBody LoanInOutRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(loanInOutService.updateLoanInOut(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateLoanInOut failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/get/item", method = RequestMethod.POST)
	@ApiOperation(value = "Get loan in out item list", response = LoanInOutResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLoanInOutItemList(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(loanInOutService.getLoanInOutItemList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLoanInOutItemList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/add/item", method = RequestMethod.POST)
	@ApiOperation(value = "Get loan in out item list", response = LoanInOutResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLoanInOutAddItemList(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(loanInOutService.getLoanInOutAddItemList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLoanInOutAddItemList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
