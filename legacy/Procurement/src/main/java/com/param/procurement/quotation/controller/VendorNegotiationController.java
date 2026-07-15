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
import com.param.procurement.quotation.dto.vendorNegotiation.VendorNegotiationRequest;
import com.param.procurement.quotation.service.vendorNegotiation.IVendorNegotiationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(value = "/api/procurements/quotation/vendor/negotiation")
public class VendorNegotiationController {

	final static Logger logger = Logger.getLogger(VendorNegotiationController.class);

	@Autowired
	IVendorNegotiationService vendorNegotiationService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getVendorNegotiationDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(vendorNegotiationService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getVendorNegotiationDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
		
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Vendor Negotiation by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getVendorNegotiationList(@RequestBody VendorNegotiationRequest vendorNegotiationRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(vendorNegotiationService.getVendorNegotiationList(vendorNegotiationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getVendorNegotiationList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Vendor Negotiation", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateVendorNegotiation(@PathVariable Integer id, @RequestBody VendorNegotiationRequest vendorNegotiationRequest)
			throws Exception {
		try {
			vendorNegotiationRequest.setId(id);
			    return new ResponseEntity<Object>(vendorNegotiationService.updateVendorNegotiation( vendorNegotiationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateVendorNegotiation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Vendor Negotiation details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getVendorNegotiation(@PathVariable Integer id)throws Exception {
		try {
			     return new ResponseEntity<Object>(vendorNegotiationService.getVendorNegotiation(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getVendorNegotiation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Quotation Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelVendorNegotiation(@PathVariable Integer id, @RequestBody VendorNegotiationRequest vendorNegotiationRequest)
			throws Exception {
		try {
			vendorNegotiationRequest.setId(id);
			return new ResponseEntity<Object>(vendorNegotiationService.cancelVendorNegotiation(vendorNegotiationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelVendorNegotiation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
