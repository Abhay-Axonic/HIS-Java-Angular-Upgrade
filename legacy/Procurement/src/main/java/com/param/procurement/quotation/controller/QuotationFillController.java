package com.param.procurement.quotation.controller;

import java.util.List;

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
import com.param.procurement.quotation.dto.quotationFill.QuotationFillRequest;
import com.param.procurement.quotation.service.quotationFill.IQuotationFillService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/quotation/fill")
public class QuotationFillController {

	final static Logger logger = Logger.getLogger(QuotationFillController.class);

	@Autowired
	IQuotationFillService quotationFillService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getQuotationFillInDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(quotationFillService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getQuotationFillInDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Quotation Fill In by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getQuotationFillInList(@RequestBody QuotationFillRequest quotationFillRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(quotationFillService.getQuotationFillList(quotationFillRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getQuotationFillInList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Quotation Fill In", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveQuotationFillIn(@RequestBody QuotationFillRequest quotationFillRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(quotationFillService.saveQuotationFill(quotationFillRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveQuotationFillIn failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Quotation Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateQuotationFillIn(@PathVariable Integer id, @RequestBody QuotationFillRequest quotationFillRequest)
			throws Exception {
		try {
			quotationFillRequest.setId(id);
			    return new ResponseEntity<Object>(quotationFillService.updateQuotationFill( quotationFillRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateQuotationFillIn failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Quotation Request details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getQuotationFillIn(@PathVariable Integer id)throws Exception {
		try {
			     return new ResponseEntity<Object>(quotationFillService.getQuotationFill(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getQuotationFillIn failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}


	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve QuotationFillIn", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveQuotationFillIn(@RequestBody QuotationFillRequest quotationFillRequest)
			throws Exception {
		try {
			    return new ResponseEntity<Object>(quotationFillService.approveQuotationFill(quotationFillRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveQuotationFillIn failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel QuotationFillIn", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelQuotationFillIn(@PathVariable Integer id, @RequestBody QuotationFillRequest quotationFillRequest)
			throws Exception {
		try {
			quotationFillRequest.setId(id);
			return new ResponseEntity<Object>(quotationFillService.cancelQuotationFill(quotationFillRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelQuotationFillIn failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/vendor/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  QuotationFillIn by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getQuotationVendorSearchtList(@RequestBody QuotationFillRequest quotationFillRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(quotationFillService.getQuotationVendorSearchList(quotationFillRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getQuotationVendorSearchtList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/vendor/details", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get Details of  Quotation Vendor ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getQuotationVendorsDetails(@RequestBody List<QuotationFillRequest> quotationFillRequestList)
			throws Exception {
		try {
			return new ResponseEntity<Object>(quotationFillService.getQuotationVendorDetails(quotationFillRequestList), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getQuotationVendorsDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}



}
