package com.param.procurement.quotation.controller;

import org.apache.log4j.Logger;
import org.json.JSONException;
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
import com.param.global.dto.global.PurchaseOrderHistory;
import com.param.procurement.purchase.consignmentstockpo.dto.ConsignmentStockPOMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockReturnRequest;
import com.param.procurement.quotation.service.consignmentPOCreation.IConsignmentStockPOCreationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/quotation/consignmentstockpocreation")
public class ConsignmentStockPOCreationController {

	final static Logger logger = Logger.getLogger(ConsignmentStockPOCreationController.class);

	@Autowired
	IConsignmentStockPOCreationService iConsignmentStockPOCreationService;
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Purchase Order by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPurchaseOrderList(@RequestBody PurchaseOrderMasterDto purchaseOrdDto)
			throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentStockPOCreationService.getConsignmentStockPuchaseOrderList(purchaseOrdDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPurchaseOrderList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/getgrn", method = RequestMethod.POST)
	@ApiOperation(value = "Get grn details for consignment", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAllGrnDetailsForConsignmentStockReturn(@RequestBody ConsignmentStockReturnRequest consignmentStockReturnRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentStockPOCreationService.getAllCRGrnDetails(consignmentStockReturnRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getAllGrnDetailsForConsignmentStockReturn failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save Purchase Order", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> savePurchaseOrder(@RequestBody ConsignmentStockPOMasterDto purchaseOrderDto) throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentStockPOCreationService.savePurchaseOrder(purchaseOrderDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("savePurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Purchase Order", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updatePurchaseOrder(@PathVariable Integer id, @RequestBody ConsignmentStockPOMasterDto purchaseOrderDto)
			throws Exception {
		try {
			purchaseOrderDto.setId(id);
			    return new ResponseEntity<Object>(iConsignmentStockPOCreationService.updatePurchaseOrder(purchaseOrderDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updatePurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Purchase Order details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getPurchaseOrder(@PathVariable Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId)
			throws Exception {
		try {
			     return new ResponseEntity<Object>(iConsignmentStockPOCreationService.getPucrhaseOrder(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "consignmentpohistory/{grnDetailId}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for Purchase Order History by GRN Detail id", response = PurchaseOrderHistory.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPurchaseOrderHistoryByItemId(@PathVariable Integer grnDetailId , @PathVariable Integer unitId)
			throws JSONException {
		return new ResponseEntity<Object>(iConsignmentStockPOCreationService.getConsignmentPOHistoryByGRNDetailId(grnDetailId , unitId), HttpStatus.OK);
	}

}
