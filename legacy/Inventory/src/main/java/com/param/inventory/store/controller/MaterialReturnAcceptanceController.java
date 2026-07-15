package com.param.inventory.store.controller;

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
import com.param.inventory.store.dto.materialIssueAcceptance.MaterialsDropdownResponse;
import com.param.inventory.store.dto.materialReturn.MaterialReurnRequest;
import com.param.inventory.store.dto.storeIndent.StoreIndentDropdownResponse;
import com.param.inventory.store.service.materialReturnAcceptance.IMaterialReturnAcceptanceService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/inventory/stores/material/return/acceptance")
public class MaterialReturnAcceptanceController {

 private static final Logger logger = Logger.getLogger(MaterialReturnAcceptanceController.class);

 @Autowired
 IMaterialReturnAcceptanceService MaterialReturnAcceptanceService;
 
 @RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = MaterialsDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialReturnAcceptanceService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
 
    @RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Material Return Acceptance by search", response = StoreIndentDropdownResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getMaterialReturnAcceptanceList(@RequestBody MaterialReurnRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialReturnAcceptanceService.getMaterialReturnAcceptanceList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Material Return Acceptance list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Material Return Acceptance", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateMaterialReturnAcceptance(@PathVariable Integer id, @RequestBody MaterialReurnRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(MaterialReturnAcceptanceService.updateMaterialReturnAcceptance(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while Updating the Material Return Acceptanc", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
    
	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Material Return Acceptance details by id", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getMaterialReturnAcceptanceDetails(@PathVariable Integer unitId,@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialReturnAcceptanceService.getMaterialReturnAcceptanceDetails(id,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the material Return details", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
    
}
