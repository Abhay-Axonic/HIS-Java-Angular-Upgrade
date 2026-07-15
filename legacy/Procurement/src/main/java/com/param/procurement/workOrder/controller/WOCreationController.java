package com.param.procurement.workOrder.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.param.procurement.workOrder.dto.workOrderCreation.WOCreationRequest;
import com.param.procurement.workOrder.dto.workOrderCreation.WOCreationResponse;
import com.param.procurement.workOrder.service.workOrderCreation.IWOCreationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



@RestController
@RequestMapping(value = "/api/procurements/WOCreation")
public class WOCreationController {

	final static Logger logger = Logger.getLogger(WOCreationController.class);

	@Autowired
	IWOCreationService wOCreationService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getWOVendorSelectionDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(wOCreationService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getWOVendorSelectionDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  WorkOrderCreation by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getWorkOrderCreationList(@RequestBody WOCreationRequest woCreationRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(wOCreationService.getWorkOrderCreationList(woCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWorkOrderCreationList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchByQR", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  WorkOrderCreation by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getWorkOrderCreationListByQR(@RequestBody WOCreationRequest woCreationRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(wOCreationService.getWorkOrderCreationListByQR(woCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWorkOrderCreationListByQR failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchByQRById",method = RequestMethod.POST, consumes = "application/json" )
	@ApiOperation(value = "Get list of  WorkOrderCreation by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getWorkOrderCreationQRById(@RequestBody WOCreationRequest woCreationRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(wOCreationService.getWorkOrderCreationQRById(woCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWorkOrderCreationQRById failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchByWR", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  WorkOrderCreation by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getWorkOrderCreationListByWR(@RequestBody WOCreationRequest woCreationRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(wOCreationService.getWorkOrderCreationListByWR(woCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWorkOrderCreationListByWR failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchByWRById", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  WorkOrderCreation by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getWorkOrderCreationWRById(@RequestBody WOCreationRequest woCreationRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(wOCreationService.getWorkOrderCreationWRById(woCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWorkOrderCreationWRById failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save WorkOrderRequest By QR", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveWorkOrderRequest(@RequestBody WOCreationRequest woCreationRequest) throws Exception {
		try {
				return new ResponseEntity<Object>(wOCreationService.saveWorkOrderByQuotationRequest(woCreationRequest), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error("saveWorkOrderRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update WorkOrderCreationRequest", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateWorkOrderCreation(@PathVariable Integer id, @RequestBody WOCreationRequest woCreationRequest)
			throws Exception {
		try {
			woCreationRequest.setId(id);
			    return new ResponseEntity<Object>(wOCreationService.updateWorkOrderCreation(woCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateWorkOrderCreation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get WorkOrderCreationRequest details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getworkOrderCreation(@PathVariable Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId)throws Exception {
		try {
			     return new ResponseEntity<Object>(wOCreationService.getworkOrderCreation(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getworkOrderCreation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve WorkOrderCreationRequest", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveWorkOrderCreation(@RequestBody WOCreationRequest woCreationRequest)
			throws Exception {
		try {
			    return new ResponseEntity<Object>(wOCreationService.approveWorkOrderCreation(woCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveWorkOrderCreation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel WorkOrderCreationRequest", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelWorkOrderCreation(@PathVariable Integer id, @RequestBody WOCreationRequest woCreationRequest)
			throws Exception {
		try {
			woCreationRequest.setId(id);
			return new ResponseEntity<Object>(wOCreationService.cancelWorkOrderCreation(woCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelWorkOrderCreation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	

}
