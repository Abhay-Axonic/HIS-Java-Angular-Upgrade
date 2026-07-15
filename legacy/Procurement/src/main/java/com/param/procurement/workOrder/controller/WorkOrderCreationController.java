package com.param.procurement.workOrder.controller;

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
import com.param.procurement.workOrder.dto.workOrderCreation.WorkOrderCreationRequest;
import com.param.procurement.workOrder.service.workOrderCreation.IWorkOrderCreationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/workOrder/creation")
public class WorkOrderCreationController {

	final static Logger logger = Logger.getLogger(WorkOrderCreationController.class);

	@Autowired
	IWorkOrderCreationService workOrderCreation;

	@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getWorkOrderCreationDropdown()throws Exception {
		try
		{
		    return new ResponseEntity<Object>(workOrderCreation.getDropdowns(), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getWorkOrderCreationDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  WorkOrderCreation by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getWorkOrderCreationList(@RequestBody WorkOrderCreationRequest woCreationRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(workOrderCreation.getWorkOrderCreationList(woCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWorkOrderCreationList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save WorkOrderCreationRequest", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveWorkOrderCreation(@RequestBody WorkOrderCreationRequest woCreationRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(workOrderCreation.saveWorkOrderCreation(woCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveWorkOrderCreation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update WorkOrderCreationRequest", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateWorkOrderCreation(@PathVariable Integer id, @RequestBody WorkOrderCreationRequest woCreationRequest)
			throws Exception {
		try {
			woCreationRequest.setId(id);
			    return new ResponseEntity<Object>(workOrderCreation.updateWorkOrderCreation(woCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateWorkOrderCreation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get WorkOrderCreationRequest details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getworkOrderCreation(@PathVariable Integer id)throws Exception {
		try {
			     return new ResponseEntity<Object>(workOrderCreation.getworkOrderCreation(id), HttpStatus.OK);
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
	public ResponseEntity<Object> approveWorkOrderCreation(@RequestBody WorkOrderCreationRequest woCreationRequest)
			throws Exception {
		try {
			    return new ResponseEntity<Object>(workOrderCreation.approveWorkOrderCreation(woCreationRequest), HttpStatus.OK);
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
	public ResponseEntity<Object> cancelWorkOrderCreation(@PathVariable Integer id, @RequestBody WorkOrderCreationRequest woCreationRequest)
			throws Exception {
		try {
			woCreationRequest.setId(id);
			return new ResponseEntity<Object>(workOrderCreation.cancelWorkOrderCreation(woCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelWorkOrderCreation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
