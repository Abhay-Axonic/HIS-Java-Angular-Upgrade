package com.param.linenLaundry.linenIssueAcceptance.controller;

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
import com.param.linenLaundry.linenIndent.dto.LinenLaundryDropdown;
import com.param.linenLaundry.linenIndent.dto.LinenRequest;
import com.param.linenLaundry.linenIndent.dto.LinenResponse;
import com.param.linenLaundry.linenIssueAcceptance.service.ILinenIssueAcceptanceService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/linenLaundry/linen/issue/acceptance")
public class LinenIssueAcceptanceController {

	private static final Logger logger = Logger.getLogger(LinenIssueAcceptanceController.class);

	@Autowired
	ILinenIssueAcceptanceService linenIssueAcceptanceService;

	@RequestMapping(value = "/dropdowns/{unitId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = LinenLaundryDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(linenIssueAcceptanceService.getDropdowns(unitId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of linen issue acceptance by search", response = LinenResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLinenIssueAcceptance(@RequestBody LinenRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(linenIssueAcceptanceService.getLinenIssueAcceptance(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the linen issue acceptance list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Save linen issue acceptance", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveLinenIssueAcceptance(@PathVariable Integer id, @RequestBody LinenRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(linenIssueAcceptanceService.saveLinenIssueAcceptance(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the linen issue acceptance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get linen issue acceptance details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getLinenIssueAcceptance(@PathVariable Integer id)
			throws Exception {
		try {
			return new ResponseEntity<Object>(linenIssueAcceptanceService.getLinenIssueAcceptance(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the linen issue acceptance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
