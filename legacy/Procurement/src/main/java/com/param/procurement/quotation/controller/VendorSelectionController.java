package com.param.procurement.quotation.controller;

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
import com.param.procurement.quotation.dto.vendorSelection.VendorSelectionRequest;
import com.param.procurement.quotation.service.vendorSelection.IVendorSelectionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/quotation/vendor/selection")
public class VendorSelectionController {

	final static Logger logger = Logger.getLogger(VendorSelectionController.class);

	@Autowired
	IVendorSelectionService vendorSelectionService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getVendorSelectionDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(vendorSelectionService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getVendorSelectionDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
		
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Vendor Selection by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getVendorSelectionList(@RequestBody VendorSelectionRequest vendorSelectionRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(vendorSelectionService.getVendorSelectionList(vendorSelectionRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getVendorSelectionList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Vendor Selection", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateVendorSelection(@PathVariable Integer id, @RequestBody VendorSelectionRequest vendorSelectionRequest)
			throws Exception {
		try {
			vendorSelectionRequest.setId(id);
			return new ResponseEntity<Object>(vendorSelectionService.updateVendorSelection(vendorSelectionRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateVendorSelection failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
		
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Vendor Selection details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getVendorSelection(@PathVariable Integer id)throws Exception {
		try {
			     return new ResponseEntity<Object>(vendorSelectionService.getVendorSelection(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getVendorSelection failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Quotation Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelVendorSelection(@PathVariable Integer id, @RequestBody VendorSelectionRequest vendorSelectionRequest)
			throws Exception {
		try {
			vendorSelectionRequest.setId(id);
			return new ResponseEntity<Object>(vendorSelectionService.cancelVendorSelection(vendorSelectionRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelVendorSelection failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}


}
