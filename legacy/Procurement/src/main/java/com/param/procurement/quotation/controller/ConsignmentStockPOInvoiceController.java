package com.param.procurement.quotation.controller;

import java.util.List;

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
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.quotation.dto.consignmentPOInvoice.ConsignmentStockPORequestDto;
import com.param.procurement.quotation.dto.consignmentPOInvoice.ConsignmentStockPOResponseDto;
import com.param.procurement.quotation.service.consignmentPOInvoice.IConsignmentStockPOInvoiceService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/quotation/consignmentstockpoinvoice")
public class ConsignmentStockPOInvoiceController {

	final static Logger logger = Logger.getLogger(ConsignmentStockPOInvoiceController.class);

	@Autowired
	IConsignmentStockPOInvoiceService iConsignmentStockPOInvoice;
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Consignment Stock PO Invoice List", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmenStockPOInvoiceList(@RequestBody ConsignmentStockPORequestDto consignmentStockPORequestDto) throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentStockPOInvoice.getConsignmentStockPOInvoiceList(consignmentStockPORequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmenStockPOInvoiceList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/getconsignmentpolist", method = RequestMethod.POST)
	@ApiOperation(value = "Get Consignment PO List", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmentPOList(@RequestBody ConsignmentStockPORequestDto consignmentStockPORequestDto) throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentStockPOInvoice.getConsignmentStockPOList(consignmentStockPORequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmentPOList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save Consignment PO Invoice List", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveConsignmentPOInvoice(@RequestBody List<ConsignmentStockPOResponseDto> consignmentStockPOResponse) throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentStockPOInvoice.saveConsignmentStockPOInvoice(consignmentStockPOResponse), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveConsignmentPOInvoice failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/details", method = RequestMethod.POST)
	@ApiOperation(value = "Get consignment stock po invoice details", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmentStockPOInvoiceDetails(@RequestBody ConsignmentStockPOResponseDto consignmentStockPOResponseDto) throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentStockPOInvoice.getConsignmentStockPOInvoiceDetail(consignmentStockPOResponseDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmentStockPOInvoiceDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/updatedetails", method = RequestMethod.PATCH)
	@ApiOperation(value = "Save Consignment PO Invoice List", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> updateConsignmentPOInvoiceDetail(@RequestBody ConsignmentStockPOResponseDto consignmentStockPOResponseDto) throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentStockPOInvoice.updateConsignmentStockPOInvoiceDetail(consignmentStockPOResponseDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateConsignmentPOInvoiceDetail failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Consignment PO Invoice List", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> updateonsignmentPOInvoice(@RequestBody List<ConsignmentStockPOResponseDto> consignmentStockPOResponseDto) throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentStockPOInvoice.updateConsignmentStockPOInvoice(consignmentStockPOResponseDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateonsignmentPOInvoice failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/taxList/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "taxList", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getTaxList(Integer orgId) throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentStockPOInvoice.getTaxList(orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getTaxList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel purchase Order invoice ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelPurchaseOrder(@PathVariable Integer id, @RequestBody PurchaseOrderMasterDto purchaseOrderDto)
			throws Exception {
		try {
			purchaseOrderDto.setId(id);
			return new ResponseEntity<Object>(iConsignmentStockPOInvoice.cancelPurchaseOrder(purchaseOrderDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelPurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
