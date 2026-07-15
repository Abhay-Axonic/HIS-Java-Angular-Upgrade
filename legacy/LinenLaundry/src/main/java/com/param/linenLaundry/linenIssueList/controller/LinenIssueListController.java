package com.param.linenLaundry.linenIssueList.controller;

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
import com.param.linenLaundry.linenIssueList.service.ILinenIssueListService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/linenLaundry/linen/issue/list")
public class LinenIssueListController {

	private static final Logger logger = Logger.getLogger(LinenIssueListController.class);

	@Autowired
	ILinenIssueListService linenIssueListService;

	@RequestMapping(value = "/dropdowns/{unitId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = LinenLaundryDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(linenIssueListService.getDropdowns(unitId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of linen issue list by search", response = LinenResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLinenIssueList(@RequestBody LinenRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(linenIssueListService.getLinenIssueList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the linen issue list list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/direct/issue", method = RequestMethod.POST)
	@ApiOperation(value = "Save linen issue list", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveLinenIssueList(@RequestBody LinenRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(linenIssueListService.saveLinenIssueList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the linen issue list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get linen issue list details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getLinenIssueList(@PathVariable Integer id)
			throws Exception {
		try {
			return new ResponseEntity<Object>(linenIssueListService.getLinenIssueList(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the linen issue list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
