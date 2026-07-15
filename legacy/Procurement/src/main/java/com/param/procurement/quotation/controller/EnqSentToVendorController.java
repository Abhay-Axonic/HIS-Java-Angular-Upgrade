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
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToMultiVendorReqDto;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToMultipleVendorReqDto;
import com.param.procurement.quotation.dto.enqSendToVendor.EnqSendToVendorReqDto;
import com.param.procurement.quotation.service.enqSendToVendor.IEnqSendToVendorService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/quotation/enquiry/sentToVendor")
public class EnqSentToVendorController {

	final static Logger logger = Logger.getLogger(EnqSentToVendorController.class);

	@Autowired
	IEnqSendToVendorService enqSendToVendorService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getQuotationVendorDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(enqSendToVendorService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getQuotationVendorDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Quotation Vendor by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getQuotationVendorList(@RequestBody EnqSendToVendorReqDto enqSendToVendorReqDto)
			throws Exception {
		try {
			return new ResponseEntity<Object>(enqSendToVendorService.getEnquirySendToVendorList(enqSendToVendorReqDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getQuotationVendorList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Quotation Vendor", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveQuotationVendor(@RequestBody EnqSendToMultiVendorReqDto enqSendToVendorReqDto) throws Exception {
		try {
			return new ResponseEntity<Object>(enqSendToVendorService.saveEnquirySendToVendor(enqSendToVendorReqDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveQuotationVendor failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Quotation Vendor", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateQuotationVendor(@PathVariable Integer id, @RequestBody EnqSendToVendorReqDto enqSendToVendorReqDto)
			throws Exception {
		try {
			enqSendToVendorReqDto.setId(id);
			    return new ResponseEntity<Object>(enqSendToVendorService.updateEnquirySendToVendor(enqSendToVendorReqDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateQuotationVendor failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Quotation Vendor details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getQuotationVendor(@PathVariable Integer id,@PathVariable Integer orgId)throws Exception {
		try {
			     return new ResponseEntity<Object>(enqSendToVendorService.getQuotationVendor(id,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getQuotationVendor failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Quotation Vendor", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveQuotationVendor(@RequestBody EnqSendToVendorReqDto enqSendToVendorReqDto)
			throws Exception {
		try {
			    return new ResponseEntity<Object>(enqSendToVendorService.approveQuotationVendor(enqSendToVendorReqDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveQuotationVendor failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Quotation Vendor", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelQuotationVendor(@PathVariable Integer id, @RequestBody EnqSendToVendorReqDto enqSendToVendorReqDto)
			throws Exception {
		try {
			enqSendToVendorReqDto.setId(id);
			return new ResponseEntity<Object>(enqSendToVendorService.cancelQuotationVendor(enqSendToVendorReqDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelQuotationVendor failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/qr/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Quotation Request by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getQuotationRequesSearchtList(@RequestBody EnqSendToVendorReqDto enqSendToVendorReqDto)
			throws Exception {
		try {
			return new ResponseEntity<Object>(enqSendToVendorService.getQuotationRequestSearchList(enqSendToVendorReqDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getQuotationRequesSearchtList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/qr/details", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get Details of  Quotation Request ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getQuotationRequestDetails(@RequestBody List<EnqSendToVendorReqDto> enqSendToVendorReqDtoList)
			throws Exception {
		try {
			return new ResponseEntity<Object>(enqSendToVendorService.getQuotationRequestDetails(enqSendToVendorReqDtoList), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getQuotationRequestDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/qr/multiplesearch", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Quotation Request by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getQuotationRequesMultipleSearchtList(@RequestBody EnqSendToMultipleVendorReqDto enqSendToVendorReqDto)
			throws Exception {
		try {
			return new ResponseEntity<Object>(enqSendToVendorService.getQuotationRequesMultipleSearchtList(enqSendToVendorReqDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getQuotationRequesMultipleSearchtList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
