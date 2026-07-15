package com.param.pharmacy.op.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.ErrorResponse;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.global.common.ServiceException;
import com.param.pharmacy.ip.dto.assignPicker.CancelRequest;
import com.param.pharmacy.op.dto.salesWorkList.ResponseWorkListSearch;
import com.param.pharmacy.op.dto.salesWorkList.WorkListSearch;
import com.param.pharmacy.op.service.salesWorkList.ISalesWorkListService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/pharmacy/op/sale/workList")
public class SalesWorkListController {

	private static final Logger logger = Logger.getLogger(SalesWorkListController.class);

	@Autowired
	private ISalesWorkListService salesWorkListService;

	@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	@ApiOperation(value = "Get Dropdown list", response = PrescriptionDetail.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList() {
		try {
			return new ResponseEntity<Object>(salesWorkListService.getDropdownList(), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get work list ", response = ResponseWorkListSearch.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> worklistSearch(@RequestBody WorkListSearch WorkListSearch) {
		try {
			return new ResponseEntity<Object>(salesWorkListService.worklistSearch(WorkListSearch), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{storeId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of item Details By Id", response = PrescriptionDetail.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> prescriptionDetailsById(@PathVariable("id") Long id,@PathVariable Integer unitId,@PathVariable Integer storeId) {
		try {
			return new ResponseEntity<Object>(salesWorkListService.prescriptionDetailsByIdBP(id,unitId,storeId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/substitute/{id}/{itemId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get substitute By Id", response = PrescriptionDetail.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getSubstitute(@PathVariable("id") Integer id,
			@PathVariable("itemId") Integer itemId) {
		try {
			return new ResponseEntity<Object>(salesWorkListService.getSubstitute(id, itemId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	
	@RequestMapping(value = "/substitute/{id}/{itemId}/{encounterId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get substitute By Id", response = PrescriptionDetail.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getSubstitute(@PathVariable("id") Integer id,
			@PathVariable("itemId") Integer itemId, @PathVariable("encounterId") Integer encounterId) {
		try {
			return new ResponseEntity<Object>(salesWorkListService.getSubstitute(id, itemId, encounterId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/cancel", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel sales worklist", response = PrescriptionDetail.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelAssignPicker(@RequestBody CancelRequest cancelRequest) {
		try {
			salesWorkListService.cancelSalesWorklist(cancelRequest);
			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}
}
