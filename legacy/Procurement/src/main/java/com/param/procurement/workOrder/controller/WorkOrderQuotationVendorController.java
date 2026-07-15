package com.param.procurement.workOrder.controller;

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

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.procurement.workOrder.dto.workOrderQuotationRequest.WorkOrderQR;
import com.param.procurement.workOrder.dto.workOrderQuotationVendor.WorkOrderQVRequest;
import com.param.procurement.workOrder.service.workOrderQuotationVendor.IWorkOrderQuotationVendorService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/procurements/workOrder/quotationVendor")
public class WorkOrderQuotationVendorController {

	final static Logger logger = Logger.getLogger(WorkOrderQuotationVendorController.class);

	@Autowired
	IWorkOrderQuotationVendorService workOrderQVService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getWorkOrderWorkOrderQuotationVendorDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(workOrderQVService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getWorkOrderWorkOrderQuotationVendorDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  WorkOrder Request by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getWorkOrderQuotationVendorList(@RequestBody WorkOrderQVRequest workOrderQVRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(workOrderQVService.getWorkOrderQuotationVendorList(workOrderQVRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWorkOrderQuotationVendorList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search/new", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  WorkOrder Request by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getWorkOrderQuotationVendorListNew(@RequestBody WorkOrderQVRequest workOrderQVRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(workOrderQVService.getWorkOrderQuotationVendorListNew(workOrderQVRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWorkOrderQuotationVendorListNew failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save WorkOrderRequest", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveWorkOrderQuotationVendor(@RequestBody WorkOrderQVRequest workOrderQVRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(workOrderQVService.saveWoQuotationVendor(workOrderQVRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveWorkOrderQuotationVendor failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save/new", method = RequestMethod.POST)
	@ApiOperation(value = "Save WorkOrderRequest", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveWorkOrderQuotationVendorNew(@RequestBody WorkOrderQVRequest workOrderQVRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(workOrderQVService.saveWoQuotationVendorNew(workOrderQVRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveWorkOrderQuotationVendorNew failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update WorkOrderRequest", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateWorkOrderQuotationVendor(@PathVariable Integer id, @RequestBody WorkOrderQVRequest workOrderQVRequest)
			throws Exception {
		try {
			workOrderQVRequest.setId(id);
			    return new ResponseEntity<Object>(workOrderQVService.updateWoQuotationVendor(workOrderQVRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateWorkOrderQuotationVendor failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get WorkOrderRequest details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getWorkOrderQuotationVendor(@PathVariable Integer id)throws Exception {
		try {
			     return new ResponseEntity<Object>(workOrderQVService.getWoQuotationVendor(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWorkOrderQuotationVendor failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Quotation Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveWorkOrderQuotationVendor(@RequestBody WorkOrderQVRequest workOrderQVRequest)
			throws Exception {
		try {
			    return new ResponseEntity<Object>(workOrderQVService.approveWoQuotationVendor(workOrderQVRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveWorkOrderQuotationVendor failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Quotation Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelWorkOrderQuotationVendor(@PathVariable Integer id, @RequestBody WorkOrderQVRequest workOrderQVRequest)
			throws Exception {
		try {
			workOrderQVRequest.setId(id);
			return new ResponseEntity<Object>(workOrderQVService.cancelWoQuotationVendor(workOrderQVRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelWorkOrderQuotationVendor failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/quotationRequest/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  WorkOrder Request by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getWorkOrderQuotationRequestList(@RequestBody  WorkOrderQR workOrderQuotationReq)
			throws Exception {
		try {
			return new ResponseEntity<Object>(workOrderQVService.getWorkOrderQRPopSearchList(workOrderQuotationReq), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getWorkOrderQuotationRequestList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/quotationRequest/details", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get Details of  Quotation Vendor ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getQuotationRequestDetails(@RequestBody List<WorkOrderQR> workOrderQuotationReqList)
			throws Exception {
		try {
			return new ResponseEntity<Object>(workOrderQVService.getWoQuotationRequestDetails(workOrderQuotationReqList), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getQuotationRequestDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
