package com.param.procurement.workOrder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.procurement.workOrder.dto.workOrderSelection.WOSelectionRequest;
import com.param.procurement.workOrder.service.workOrderSelection.IWOSelectionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(value = "/api/procurements/workOrder/quotationSelection")
public class WorkOrderSelectionController {

	final static Logger logger = Logger.getLogger(WorkOrderSelectionController.class);

	@Autowired
	IWOSelectionService wovendorSelection;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getWOVendorSelectionDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(wovendorSelection.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getWOVendorSelectionDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
		
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Work Order Vendor Selection by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getWOVendorSelectionList(@RequestBody WOSelectionRequest woSelectionRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(wovendorSelection.getWoQuotationSelectionList(woSelectionRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWOVendorSelectionList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	/*@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save WorkOrder Vendor Negotiation", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveWOVendorNegotiation(@RequestBody WONegotiationRequest woNegotiationRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(wovendorNegotiation.saveWoQuotationNegotiation(woNegotiationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<Object>(new ErrorResponse("HIS-1483", "Error while saving the WOVendorNegotiation", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Work Order Vendor Selection", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateWOVendorSelection(@PathVariable Integer id, @RequestBody WOSelectionRequest woSelectionRequest)
			throws Exception {
		try {
			woSelectionRequest.setId(id);
			Object response = wovendorSelection.updateWoQuotationSelection(woSelectionRequest);
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateWOVendorSelection failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
		
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Work Order Vendor Selection details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getVendorSelection(@PathVariable Integer id)throws Exception {
		try {
			     return new ResponseEntity<Object>(wovendorSelection.getWoQuotationSelection(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getVendorSelection failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/*@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Quotation Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelVendorNegotiation(@PathVariable Integer id, @RequestBody VendorNegotiationRequest vendorNegotiationRequest)
			throws Exception {
		try {
			vendorNegotiationRequest.setId(id);
			return new ResponseEntity<Object>(wovendorNegotiation.cancelVendorNegotiation(vendorNegotiationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<Object>(new ErrorResponse("HIS-1885", "Error while fetching the Quotation Request", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/


}
