package com.param.linenLaundry.linenIndent.controller;

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
import com.param.linenLaundry.linenIndent.service.ILinenIndentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/linenLaundry/linen/indent")
public class LinenIndentController {

	private static final Logger logger = Logger.getLogger(LinenIndentController.class);

	@Autowired
	ILinenIndentService linenIndentService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = LinenLaundryDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(linenIndentService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of linen indent by search", response = LinenResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLinenIndentList(@RequestBody LinenRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(linenIndentService.getLinenIndentList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the linen indent list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save linen indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveLinenIndent(@RequestBody LinenRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(linenIndentService.saveLinenIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the linen indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get linen indent details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getLinenIndent(@PathVariable Integer id)
			throws Exception {
		try {
			return new ResponseEntity<Object>(linenIndentService.getLinenIndent(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the linen indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve linen indents", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveLinenIndent(@RequestBody LinenRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(linenIndentService.approveLinenIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the linen indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel linen indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelLinenIndent(@PathVariable Integer id, @RequestBody LinenRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(linenIndentService.cancelLinenIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the linen indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update linen indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateLinenIndent(@PathVariable Integer id, @RequestBody LinenRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(linenIndentService.updateLinenIndent(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the linen indent", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
}
