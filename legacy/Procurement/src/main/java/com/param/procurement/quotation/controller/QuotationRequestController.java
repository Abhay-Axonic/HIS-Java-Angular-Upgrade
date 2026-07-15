package com.param.procurement.quotation.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import com.param.procurement.quotation.dto.quotationRequest.QuotationRequestDto;
import com.param.procurement.quotation.service.quotationRequest.IQuotationRequestService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping(value = "/api/procurements/quotation/enquiry")
public class QuotationRequestController {

	final static Logger logger = Logger.getLogger(QuotationRequestController.class);

	@Autowired
	IQuotationRequestService quotationRequestService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getQuotationRequestDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(quotationRequestService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getQuotationRequestDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Quotation Request by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getQuotationRequestList(@RequestBody QuotationRequestDto quotationRequestDto)
			throws Exception {
		try {
			return new ResponseEntity<Object>(quotationRequestService.getQuotationRequestList(quotationRequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getQuotationRequestList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Quotation Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveQuotationRequest(@RequestBody QuotationRequestDto quotationRequestDto) throws Exception {
		try {
			return new ResponseEntity<Object>(quotationRequestService.saveQuotationRequest(quotationRequestDto), HttpStatus.OK);
		}
		catch (DataIntegrityViolationException ex) {
			logger.error("saveQuotationRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
		catch (Exception ex) {
			logger.error("saveQuotationRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Quotation Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateQuotationRequest(@PathVariable Integer id, @RequestBody QuotationRequestDto quotationRequestDto)
			throws Exception {
		try {
			quotationRequestDto.setId(id);
			    return new ResponseEntity<Object>(quotationRequestService.updateQuotationRequest(quotationRequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateQuotationRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Quotation Request details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getQuotationRequest(@PathVariable Integer id)throws Exception {
		try {
			     return new ResponseEntity<Object>(quotationRequestService.getQuotationRequest(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getQuotationRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}


	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Quotation Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveQuotationRequest(@RequestBody QuotationRequestDto quotationRequestDto)
			throws Exception {
		try {
			    return new ResponseEntity<Object>(quotationRequestService.approveQuotationRequest(quotationRequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveQuotationRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Quotation Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelQuotationRequest(@PathVariable Integer id, @RequestBody QuotationRequestDto quotationRequestDto)
			throws Exception {
		try {
			quotationRequestDto.setId(id);
			return new ResponseEntity<Object>(quotationRequestService.cancelQuotationRequest(quotationRequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelQuotationRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
