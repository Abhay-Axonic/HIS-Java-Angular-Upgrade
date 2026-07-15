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
import com.param.procurement.workOrder.dto.workOrderNegotiation.WONegotiationDetailRequest;
import com.param.procurement.workOrder.dto.workOrderNegotiation.WONegotiationRequest;
import com.param.procurement.workOrder.dto.workOrderNegotiation.WoNegotiationMapperRequest;
import com.param.procurement.workOrder.service.workOrderNegotiation.IWorkOrderNegotiationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(value = "/api/procurements/workOrder/quotationNegotiation")
public class WorkOrderNegotiationController {

	final static Logger logger = Logger.getLogger(WorkOrderNegotiationController.class);

	@Autowired
	IWorkOrderNegotiationService wovendorNegotiation;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getWOVendorNegotiationDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(wovendorNegotiation.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getWOVendorNegotiationDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
		
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Work Order Vendor Negotiation by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getWOVendorNegotiationList(@RequestBody WONegotiationRequest woNegotiationRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(wovendorNegotiation.getWoQuotationNegotiationList(woNegotiationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWOVendorNegotiationList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save WorkOrder Vendor Negotiation", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveWOVendorNegotiation(@RequestBody WONegotiationRequest woNegotiationRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(wovendorNegotiation.saveWoQuotationNegotiation(woNegotiationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveWOVendorNegotiation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Work Order Vendor Negotiation", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateWOVendorNegotiation(@PathVariable Integer id, @RequestBody WONegotiationRequest woNegotiationRequest)
			throws Exception {
		try {
			woNegotiationRequest.setId(id);
			Object response = wovendorNegotiation.updateWoQuotationNegotiation(woNegotiationRequest);
			Boolean accept = true;
			for (WONegotiationDetailRequest item : woNegotiationRequest.getItems()) {
				for(WoNegotiationMapperRequest itemsMapper : item.getMapperList()) {
				if(itemsMapper.getOnHold()==true || itemsMapper.getReject()==true) {
					accept = false;
				}
			}}
			if(accept) {
				response = wovendorNegotiation.saveVendorSelection(woNegotiationRequest);
			}
			    return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateWOVendorNegotiation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
		
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Work Order Vendor Negotiation details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getVendorNegotiation(@PathVariable Integer id)throws Exception {
		try {
			     return new ResponseEntity<Object>(wovendorNegotiation.getWoQuotationNegotiation(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getVendorNegotiation failed", ex);
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
