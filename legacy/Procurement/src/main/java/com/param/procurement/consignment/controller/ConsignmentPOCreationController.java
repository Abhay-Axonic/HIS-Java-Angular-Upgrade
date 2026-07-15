package com.param.procurement.consignment.controller;

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
import com.param.entity.model.procurement.ImplantIndent;
import com.param.global.common.ServiceException;
import com.param.procurement.consignment.dto.ConsignmentPOCreation.ConsignmentPOCreationRequest;
import com.param.procurement.consignment.dto.consignment.ConsignmentResponse;
import com.param.procurement.consignment.service.ConsignmentPOCreation.IConsignmentPOCreationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/consignments/POCreation")
public class ConsignmentPOCreationController {

	final static Logger logger = Logger.getLogger(ConsignmentPOCreationController.class);

	@Autowired
	IConsignmentPOCreationService iConsignmentPOCreationService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getPurchaseOrderDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(iConsignmentPOCreationService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getPurchaseOrderDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Purchase Order by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPurchaseOrderList(@RequestBody ConsignmentPOCreationRequest consignmentPOCreationRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentPOCreationService.getConsignmentPuchaseOrderList(consignmentPOCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPurchaseOrderList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/issue/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Purchase Order by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getissueListForSavePO(@RequestBody ConsignmentPOCreationRequest consignmentPOCreationRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentPOCreationService.getConsignmentgetissueListForSavePO(consignmentPOCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getissueListForSavePO failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save Consignment PO", response = ConsignmentPOCreationRequest.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveConsignmentPOCreation(@RequestBody ConsignmentPOCreationRequest consignmentPOCreationRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(iConsignmentPOCreationService.saveConsignmentPOCreation(consignmentPOCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveConsignmentPOCreation failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Consignment PO details", response = ConsignmentResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmentPOCreationDetails(@PathVariable Integer id,@PathVariable Integer unitId) throws Exception {
		try {
			
			return new ResponseEntity<Object>(iConsignmentPOCreationService.getConsignmentPOCreationDetails(id,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmentPOCreationDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Patient Consumption", response = ImplantIndent.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateConsignmentPOCreationDetails(@PathVariable Integer id,
			@RequestBody ConsignmentPOCreationRequest request) {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(iConsignmentPOCreationService.updateConsignmentPOCreationDetails(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateConsignmentPOCreationDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Consignment purchase Order", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approvePurchaseOrder(@RequestBody ConsignmentPOCreationRequest consignmentPOCreationRequest)
			throws Exception {
		try {
			    return new ResponseEntity<Object>(iConsignmentPOCreationService.approveConsignmentPurchaseOrder(consignmentPOCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approvePurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Consignment purchase Order", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelConsignemntPurchaseOrder(@PathVariable Integer id, @RequestBody ConsignmentPOCreationRequest consignmentPOCreationRequest)
			throws Exception {
		try {
			consignmentPOCreationRequest.setId(id);
			return new ResponseEntity<Object>(iConsignmentPOCreationService.cancelConsignemntPurchaseOrder(consignmentPOCreationRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelConsignemntPurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
