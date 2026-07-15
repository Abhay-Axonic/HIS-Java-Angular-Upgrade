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
import com.param.procurement.quotation.dto.physicalInspection.DetailsConsignmentStockConversionSaveRequest;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionDetails;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionRequest;
import com.param.procurement.quotation.dto.physicalInspection.PhysicalInspectionResponse;
import com.param.procurement.quotation.service.physicalInspection.IConsignmentStockConversionService;
import com.param.procurement.quotation.service.physicalInspection.IPhysicalInspectionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/quotation/consignmentstockconversion")
public class ConsignmentStockConversionController {

	final static Logger logger = Logger.getLogger(ConsignmentStockConversionController.class);

	@Autowired
	IPhysicalInspectionService PhysicalInspectionService;
	
	@Autowired
	IConsignmentStockConversionService consignmentStockConversionService;
	
	 /* not in use */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	@ApiOperation(value = "Get consignment stock conversion ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmentStockConversion(@RequestBody PhysicalInspectionRequest physicalInspectionRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(PhysicalInspectionService.getPhysicalnspectionDetails(physicalInspectionRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmentStockConversion failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	/* not in use */
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get physical inspection list", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPhysicalnspectionList(@RequestBody PhysicalInspectionRequest physicalInspectionRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(consignmentStockConversionService.getPhysicalnspectionList(physicalInspectionRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPhysicalnspectionList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/detailsConsignmentStockConversion", method = RequestMethod.POST)
	@ApiOperation(value = "Get consignment stock conversion details", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmentStockConversionDetails(@RequestBody PhysicalInspectionRequest physicalInspectionRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(consignmentStockConversionService.getConsignmentStockConversionDetails(physicalInspectionRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmentStockConversionDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save consignment stock conversion", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveConsignmentStockConversion(@RequestBody DetailsConsignmentStockConversionSaveRequest detailsConsignmentStockConversionSaveRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(consignmentStockConversionService.saveConsignmentStockConversion(detailsConsignmentStockConversionSaveRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveConsignmentStockConversion failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = { "/storestocktransfereddata/{storeId}/{batchId}/{batchNo}/{lotNo}" }, method = RequestMethod.GET)
	@ApiOperation(value = "Delete allocated item quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> allDataList( @PathVariable Integer storeId,  @PathVariable Integer batchId , @PathVariable String batchNo , @PathVariable String lotNo) throws Exception {
		try {
			return new ResponseEntity<Object>(consignmentStockConversionService.allDataList( storeId , batchId, batchNo, lotNo), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("allDataList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
}
