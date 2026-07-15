package com.param.procurement.purchase.controller;

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
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.service.grnAgainstPo.IGRNAgainstPoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/purchase/grn/against/po/")
public class GRNAgainstPoController {

	final static Logger logger = Logger.getLogger(GRNAgainstPoController.class);

	@Autowired
	IGRNAgainstPoService grnAgainstPOService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getPurchaseOrderDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(grnAgainstPOService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getPurchaseOrderDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  GRN Against PO by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getGoodReceiptNoteList(@RequestBody PurchaseOrderMasterDto PurchaseOrdDto)
			throws Exception {
		try {
			return new ResponseEntity<Object>(grnAgainstPOService.getPuchaseOrderList(PurchaseOrdDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getGoodReceiptNoteList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get GRN Against PO by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getGoodReceiptNote(@PathVariable Integer id)throws Exception {
		try {
			     return new ResponseEntity<Object>(grnAgainstPOService.getGoodReceiptNoteList(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getGoodReceiptNote failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel GRN Against PO", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelPurchaseOrder(@PathVariable Integer id, @RequestBody PurchaseOrderMasterDto purchaseOrderDto)
			throws Exception {
		try {
			purchaseOrderDto.setId(id);
			return new ResponseEntity<Object>(grnAgainstPOService.cancelPurchaseOrder(purchaseOrderDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelPurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
