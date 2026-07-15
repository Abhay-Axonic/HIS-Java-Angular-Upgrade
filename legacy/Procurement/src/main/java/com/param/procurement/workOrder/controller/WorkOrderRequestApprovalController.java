package com.param.procurement.workOrder.controller;

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
import com.param.procurement.workOrder.dto.workOrderRequest.WorkOrderRequest;
import com.param.procurement.workOrder.service.workOrderRequestApproval.IWorkOrderRequestApprovalService;
import com.param.global.common.ServiceException;
import org.apache.log4j.Logger;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/workOrderRequest/approval")
public class WorkOrderRequestApprovalController {

	final static Logger logger = Logger.getLogger(WorkOrderRequestApprovalController.class);
	
	
	@Autowired
	IWorkOrderRequestApprovalService WorkOrderRequestApprovalService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getWorkOrderRequestDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(WorkOrderRequestApprovalService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch (Exception ex) {
			logger.error("getWorkOrderRequestDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  WorkOrder Request by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getWorkOrderRequestList(@RequestBody WorkOrderRequest workOrderRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(WorkOrderRequestApprovalService.getWorkOrderList(workOrderRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWorkOrderRequestList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get WorkOrderRequest details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getWorkOrderRequest(@PathVariable Integer id,@PathVariable Integer unitId)throws Exception {
		try {
			     return new ResponseEntity<Object>(WorkOrderRequestApprovalService.getWorkOrderRequest(id,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWorkOrderRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	

	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Quotation Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveWorkOrderRequest(@PathVariable Integer id, @RequestBody WorkOrderRequest workOrderRequest)
			throws Exception {
		try {
			workOrderRequest.setId(id);
			    return new ResponseEntity<Object>(WorkOrderRequestApprovalService.approveWorkOrderRequest(workOrderRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveWorkOrderRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Quotation Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelworkOrderRequest(@PathVariable Integer id, @RequestBody WorkOrderRequest workOrderRequest)
			throws Exception {
		try {
			workOrderRequest.setId(id);
			return new ResponseEntity<Object>(WorkOrderRequestApprovalService.cancelworkOrderRequest(workOrderRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelworkOrderRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
