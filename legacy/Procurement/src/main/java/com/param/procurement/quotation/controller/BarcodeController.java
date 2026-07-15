package com.param.procurement.quotation.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.global.dto.items.ItemsRequest;
import com.param.procurement.quotation.dto.barcode.BarcodeBatchListResponse;
import com.param.procurement.quotation.service.barcode.IBarcodeService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/quotation/barcode")
public class BarcodeController {

	final static Logger logger = Logger.getLogger(BarcodeController.class);

	@Autowired
	IBarcodeService BarcodeService;

	@RequestMapping(value = "/batches", method = RequestMethod.POST)
	@ApiOperation(value = "Get batch list for barcode", response = BarcodeBatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getBatchList(@RequestBody ItemsRequest itemRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(BarcodeService.getBatchList(itemRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getBatchList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/batches/prePrintedBarcode/save", method = RequestMethod.POST)
	@ApiOperation(value = "Get batch list for barcode", response = BarcodeBatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> savePrePrintedBarcode(@RequestBody ItemsRequest itemRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(BarcodeService.savePrePrintedBarcode(itemRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("savePrePrintedBarcode failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/batches/prePrintedBarcode/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get batch list for barcode", response = BarcodeBatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> searchPrePrintedBarcode(@RequestBody ItemsRequest itemRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(BarcodeService.searchPrePrintedBarcode(itemRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("searchPrePrintedBarcode failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/batches/prePrintedBarcode/getBarcodes", method = RequestMethod.POST)
	@ApiOperation(value = "Get batch list for barcode", response = BarcodeBatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPrePrintedBarcode(@RequestBody ItemsRequest itemRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(BarcodeService.getPrePrintedBarcode(itemRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPrePrintedBarcode failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/batches/updatePrintedCount", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "update list of store indent approval by status", response = BarcodeBatchListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> updateStoreIndentForForceClose(@RequestBody List <ItemsRequest> itemRequestList)
			throws Exception {
		try {

			return new ResponseEntity<Object>(BarcodeService.updatePrintedCount(itemRequestList), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateStoreIndentForForceClose failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
