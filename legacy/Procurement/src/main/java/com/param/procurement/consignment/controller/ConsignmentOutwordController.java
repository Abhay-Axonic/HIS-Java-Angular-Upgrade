package com.param.procurement.consignment.controller;

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
import com.param.procurement.consignment.dto.consignment.ConsignmentDropdown;
import com.param.procurement.consignment.dto.consignment.ConsignmentDto;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequest;
import com.param.procurement.consignment.dto.consignment.ConsignmentResponse;
import com.param.procurement.consignment.service.consignment.IConsignmentOutwordService;
import com.param.global.common.ServiceException;
import org.apache.log4j.Logger;
import com.wordnik.swagger.annotations.ApiOperation;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/consignment/consignment/outward")
public class ConsignmentOutwordController {

	final static Logger logger = Logger.getLogger(ConsignmentOutwordController.class);
	
	@Autowired
	IConsignmentOutwordService ConsignmentOutwordService;

	@RequestMapping(value = "/dropdown/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ConsignmentDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(ConsignmentOutwordService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Consignment Outward by search", response = ConsignmentDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getStoreIndentList(@RequestBody ConsignmentRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ConsignmentOutwordService.getConsignmentOutwardList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getStoreIndentList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save Consignment Outword", response = ConsignmentRequest.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveConsignmentOutward(@RequestBody ConsignmentRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(ConsignmentOutwordService.saveConsignmentOutward(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveConsignmentOutward failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Consignment Outward details", response = ConsignmentResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmentOutwardDetails(@PathVariable Integer unitId,@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(ConsignmentOutwordService.getConsignmentOutwardDetails(id,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmentOutwardDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Consignment Outword", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateConsignmentOutword(@PathVariable Integer id,
			@RequestBody ConsignmentRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ConsignmentOutwordService.updateConsignmentOutword(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateConsignmentOutword failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Consignment Outword", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveConsignmentOutword(@RequestBody ConsignmentRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(ConsignmentOutwordService.approveConsignmentOutword(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveConsignmentOutword failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Consignment Outword", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelConsignmentOutword(@PathVariable Integer id,
			@RequestBody ConsignmentRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ConsignmentOutwordService.cancelConsignmentOutword(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelConsignmentOutword failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}