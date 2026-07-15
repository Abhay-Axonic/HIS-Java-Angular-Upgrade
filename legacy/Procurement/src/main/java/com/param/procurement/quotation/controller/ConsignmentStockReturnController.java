package com.param.procurement.quotation.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.global.service.items.CustomException;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailHistory;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailResponse;
import com.param.procurement.quotation.dto.consignmentStockReturn.ConsignmentStockReturnDetailSaveRequest;
import com.param.procurement.quotation.dto.physicalInspection.ConsignmentStockReturnRequest;
import com.param.procurement.quotation.service.physicalInspection.IConsignmentStockReturnService;
import com.param.procurement.quotation.service.physicalInspection.IPhysicalInspectionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/quotation/consignmentstockreturn")
public class ConsignmentStockReturnController {

	final static Logger logger = Logger.getLogger(ConsignmentStockReturnController.class);

	@Autowired
	IPhysicalInspectionService PhysicalInspectionService;
	
	@Autowired
	IConsignmentStockReturnService consignmentStockReturnService;
	
	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get consignment stock return list", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmenStocktList(@RequestBody ConsignmentStockReturnRequest consignmentStockReturnRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(consignmentStockReturnService.getConsignmenStocktList(consignmentStockReturnRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmenStocktList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/addNewConsignmentReturn/getallgrn", method = RequestMethod.POST)
	@ApiOperation(value = "Get grn details for consignment", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAllGrnDetailsForConsignmentStockReturn(@RequestBody ConsignmentStockReturnRequest consignmentStockReturnRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(consignmentStockReturnService.getAllCRGrnDetails(consignmentStockReturnRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getAllGrnDetailsForConsignmentStockReturn failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/addNewConsignmentReturn/getgrn", method = RequestMethod.POST)
	@ApiOperation(value = "Get grn details for consignment", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getGrnDetailsForConsignmentStockReturn(@RequestBody ConsignmentStockReturnRequest consignmentStockReturnRequest) throws Exception {
		try {
			//return new ResponseEntity<Object>(consignmentStockReturnService.getGrnDetails(consignmentStockReturnRequest), HttpStatus.OK);
			return new ResponseEntity<Object>(consignmentStockReturnService.getCRGrnDetails(consignmentStockReturnRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getGrnDetailsForConsignmentStockReturn failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	/*@RequestMapping(value = "/save/returndetail", method = RequestMethod.POST)
	@ApiOperation(value = "save consignment stock return details ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveConsignmentStockReturnDetails(@RequestBody ConsignmentStockReturnDetailSaveRequest consignmentStockReturnDetailSaveRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(consignmentStockReturnService.saveConsignmentStockReturnDetails(consignmentStockReturnDetailSaveRequest), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<Object>(new ErrorResponse("HIS-1243", "Error while saving Consignment Stock Return information", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	@RequestMapping(value = "/save/returndetail", method = RequestMethod.POST)
	@ApiOperation(value = "save consignment stock return details ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveConsignmentStockReturnDetails(@RequestBody List<ConsignmentStockReturnDetailSaveRequest> consignmentStockReturnDetailSaveRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(consignmentStockReturnService.saveConsignmentStockReturnDetails(consignmentStockReturnDetailSaveRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveConsignmentStockReturnDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/detailsConsignmentStockReturn", method = RequestMethod.POST)
	@ApiOperation(value = "Get consignment stock conversion details", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmentStockReturnDetails(@RequestBody ConsignmentStockReturnRequest consignmentStockReturnRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(consignmentStockReturnService.getConsignmentStockreturnDetail(consignmentStockReturnRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmentStockReturnDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/update/returndetail", method = RequestMethod.POST)
	@ApiOperation(value = "update consignment stock return details ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> updateConsignmentStockReturnDetails(@RequestBody List<ConsignmentStockReturnDetailResponse> ConsignmentStockReturnDetailResponse) throws Exception {
		try {
			return new ResponseEntity<Object>(consignmentStockReturnService.updateConsignmentStockReturnDetails(ConsignmentStockReturnDetailResponse), HttpStatus.OK);
		}  catch (CustomException e) {
			logger.error("updateConsignmentStockReturnDetails failed", e);
			throw new ServiceException(e.getMessage());
		} catch (Exception ex) {
			logger.error("updateConsignmentStockReturnDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save consignment stock return", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveConsignmentStockReturn(@RequestBody List<ConsignmentStockReturnRequest> consignmentStockReturnRequests) throws Exception {
		try {
			return new ResponseEntity<Object>(consignmentStockReturnService.saveConsignmentStockReturn(consignmentStockReturnRequests), HttpStatus.OK);
		} catch (CustomException e) {
			logger.error("saveConsignmentStockReturn failed", e);
			throw new ServiceException(e.getMessage());
		} catch (Exception ex) {
			logger.error("saveConsignmentStockReturn failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/history", method = RequestMethod.POST)
	@ApiOperation(value = "Get consignment stock return details history", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmentStockReturnDetailsHistory(@RequestBody ConsignmentStockReturnDetailHistory consignmentStockReturnHistory) throws Exception {
		try {
			return new ResponseEntity<Object>(consignmentStockReturnService.getConsignmentStockreturnDetailHistory(consignmentStockReturnHistory), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmentStockReturnDetailsHistory failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
