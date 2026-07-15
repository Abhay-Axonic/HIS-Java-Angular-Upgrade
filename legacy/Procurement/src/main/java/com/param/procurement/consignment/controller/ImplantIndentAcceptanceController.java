package com.param.procurement.consignment.controller;

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
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentRequest;
import com.param.procurement.consignment.dto.implantIndent.ImplantIndentResponse;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionDropdown;
import com.param.procurement.consignment.service.implantIndentAcceptance.IImplantIndentAcceptanceService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/procurements/consignments/implant/indent/acceptance")
public class ImplantIndentAcceptanceController {

	final static Logger logger = Logger.getLogger(ImplantIndentAcceptanceController.class);

	@Autowired
	IImplantIndentAcceptanceService ImplantIndentAcceptanceService;

	
	@RequestMapping(value = "/dropdowns/{orgId}/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = PatientConsumtionDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId) {
		try {
			return new ResponseEntity<Object>(ImplantIndentAcceptanceService.getDropdownList(unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Implant Indent Acceptance List by search", response = ImplantIndentResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getImplantIndentAcceptanceList(
			@RequestBody ImplantIndentRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(ImplantIndentAcceptanceService.getImplantIndentAcceptanceList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getImplantIndentAcceptanceList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Implant Indent Acceptance List details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getImplantIndentAcceptanceDetails(@PathVariable Integer id,@PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(ImplantIndentAcceptanceService.getImplantIndentAcceptanceDetails(id,unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getImplantIndentAcceptanceDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Implant Indent Acceptance", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateImplantIndentAcceptance(@PathVariable Integer id,
			@RequestBody ImplantIndentRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ImplantIndentAcceptanceService.updateImplantIndentAcceptance(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateImplantIndentAcceptance failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
