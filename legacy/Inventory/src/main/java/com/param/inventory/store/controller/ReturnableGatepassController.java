package com.param.inventory.store.controller;

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
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassDropdownResponse;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassRequest;
import com.param.inventory.store.dto.returnableGatepass.ReturnableGatepassResponse;
import com.param.inventory.store.service.returnableGatepass.IReturnableGatepassService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/inventory/stores/returnable/gatepass")
public class ReturnableGatepassController {

	private static final Logger logger = Logger.getLogger(ReturnableGatepassController.class);

	@Autowired
	IReturnableGatepassService ReturnableGatepassService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ReturnableGatepassDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(ReturnableGatepassService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching dropdowns", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of returnable gatepass by search", response = ReturnableGatepassResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getReturnableGatepassList(
			@RequestBody ReturnableGatepassRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(ReturnableGatepassService.getReturnableGatepassList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the returnable gatepass list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save returnable gatepass", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveReturnableGatepass(@RequestBody ReturnableGatepassRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ReturnableGatepassService.saveReturnableGatepass(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the returnable gatepass", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get returnable gatepass details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getReturnableGatepass(@PathVariable Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId) throws Exception {
		try {
			return new ResponseEntity<Object>(ReturnableGatepassService.getReturnableGatepass(id,orgId,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the returnable gatepass", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update returnable gatepass", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateReturnableGatepass(@PathVariable Integer id,
			@RequestBody ReturnableGatepassRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ReturnableGatepassService.updateReturnableGatepass(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating returnable gatepass", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/submit", method = RequestMethod.PATCH)
	@ApiOperation(value = "Submit returnable gatepass for approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> submitReturnableGatepass(@RequestBody ReturnableGatepassRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ReturnableGatepassService.submitReturnableGatepass(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while submitting the returnable gatepass", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel returnable gatepass", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelReturnableGatepass(@PathVariable Integer id,
			@RequestBody ReturnableGatepassRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ReturnableGatepassService.cancelReturnableGatepass(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling returnable gatepass", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/checkReturnableGatePass", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of returnable gatepass by search", response = ReturnableGatepassResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> checkReturnableGatePass(
			@RequestBody List <ReturnableGatepassRequest> request) throws Exception {
		try {
			return new ResponseEntity<Object>(ReturnableGatepassService.checkReturnableGatePass(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while checking the returnable gatepass", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
