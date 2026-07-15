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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.procurement.loanInOut.dto.LoanInOutDropdown;
import com.param.procurement.loanInOut.dto.LoanInOutRequest;
import com.param.procurement.loanInOut.dto.LoanInOutResponse;
import com.param.procurement.loanInOut.service.loanOut.ILoanOutService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/procurements/loanInOut/loan/out")
public class LoanOutController {

	final static Logger logger = Logger.getLogger(LoanOutController.class);

	@Autowired
	ILoanOutService LoanOutService;
	
	@RequestMapping(value = "/dropdown/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of dropdown", response = LoanInOutDropdown.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(LoanOutService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get loan out list", response = LoanInOutResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLoanOutList(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(LoanOutService.getLoanOutList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLoanOutList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get loan out details", response = LoanInOutResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLoanOutDetails(@PathVariable Integer unitId,@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(LoanOutService.getLoanOutDetails(id,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLoanOutDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save loan out", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveLoanOut(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(LoanOutService.saveLoanOut(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveLoanOut failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Loan Out", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelLoanIn(@PathVariable Integer id, @RequestBody LoanInOutRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(LoanOutService.cancelLoanOut(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelLoanIn failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Loan Out", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveLoanIn(@RequestBody LoanInOutRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(LoanOutService.approveLoanOut(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveLoanIn failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Loan Out", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateLoanOut(@PathVariable Integer id, @RequestBody LoanInOutRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(LoanOutService.updateLoanOut(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateLoanOut failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
