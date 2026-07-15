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

import org.apache.log4j.Logger;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.inventory.store.dto.materialInward.MaterialInwardDropdownResponse;
import com.param.inventory.store.dto.materialInward.MaterialInwardRequest;
import com.param.inventory.store.dto.materialInward.MaterialInwardResponse;
import com.param.inventory.store.service.materialInwardApproval.IMaterialInwardApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stores/material/inward/approval")
public class MaterialInwardApprovalController {

	private static final Logger logger = Logger.getLogger(MaterialInwardApprovalController.class);

	@Autowired
	IMaterialInwardApprovalService MaterialInwardApprovalService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = MaterialInwardDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {

		try {
			return new ResponseEntity<Object>(MaterialInwardApprovalService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of material inwards approval by search", response = MaterialInwardResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getMaterialInwardApprovalList(
			@RequestBody MaterialInwardRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialInwardApprovalService.getMaterialInwardApprovalList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the material inwards list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get material inward details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getMaterialInwardApproval(@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(MaterialInwardApprovalService.getMaterialInwardApproval(id),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the material inward", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Material Inward Approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<Object> saveMaterialInwardApproval(@PathVariable Integer id,
			@RequestBody MaterialInwardRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(MaterialInwardApprovalService.approveMaterialInwardApproval(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the material inward approval", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
