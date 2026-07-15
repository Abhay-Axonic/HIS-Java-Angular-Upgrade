package com.param.inventory.stock.controller;

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
import com.param.global.dto.items.ItemsDetailsListResponse;
import com.param.global.dto.items.ItemsRequest;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceRequest;
import com.param.inventory.stock.dto.openingBalance.OpeningBalanceResponse;
import com.param.inventory.stock.service.openingBalance.IOpeningBalanceImplantService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stock/opening/implant/balances")
public class OpeningBalanceImplantController {

	private static final Logger logger = Logger.getLogger(OpeningBalanceImplantController.class);

	@Autowired
	IOpeningBalanceImplantService iOpeningBalanceImplantService;
	
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	@ApiOperation(value = "Get Implant items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getImplantItemsList(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(iOpeningBalanceImplantService.implantItemsList(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the implant items list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/consignment/list", method = RequestMethod.POST)
	@ApiOperation(value = "Get Implant items list", response = ItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getConsignmentImplantItemsList(@RequestBody ItemsRequest itemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(iOpeningBalanceImplantService.consignmentImplantItemsList(itemsRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the consignment implant items list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of opening balance Implant by search", response = OpeningBalanceResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getOpeningBalanceImplantList(@RequestBody OpeningBalanceRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(iOpeningBalanceImplantService.getOpeningBalanceImplantList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the opening balance implant list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save Opening Balance Implant", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveOpeningBalanceImplant(@RequestBody OpeningBalanceRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(iOpeningBalanceImplantService.saveOpeningBalanceImplant(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the opening balance implant", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get opening balance implant by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getOpeningBalanceImplantByid(@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(iOpeningBalanceImplantService.getOpeningBalanceImplant(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the opening balance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Opening Balance Implant", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateOpeningBalanceImplant(@PathVariable Integer id,
			@RequestBody OpeningBalanceRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(iOpeningBalanceImplantService.updateOpeningBalanceImplant(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating the opening balance implant", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Opening Balance Implant", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveOpeningBalanceImplant(@RequestBody OpeningBalanceRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(iOpeningBalanceImplantService.approveOpeningBalanceImplant(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the opening balance Implant", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	/*
	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Opening Balance Implant", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveOpeningBalanceImplant(@PathVariable Integer id,
			@RequestBody OpeningBalanceRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(iOpeningBalanceImplantService.approveOpeningBalanceImplantDetail(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<Object>(
					new ErrorResponse("HIS-1055", "Error while approving the opening balance implant detail", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Opening Balance Implant", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelStoreIndent(@PathVariable Integer id,
			@RequestBody OpeningBalanceRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(iOpeningBalanceImplantService.cancelOpeningBalanceImplant(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Cancelling the opening balance", ex);
			throw new ServiceException(ex.getMessage());
		}
	}


}
