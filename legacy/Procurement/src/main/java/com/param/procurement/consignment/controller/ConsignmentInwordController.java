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
import com.param.global.common.ServiceException;
import com.param.procurement.consignment.dto.consignment.ConsignmentDropdown;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequest;
import com.param.procurement.consignment.dto.consignment.ConsignmentResponse;
import com.param.procurement.consignment.service.ConsignmentInword.IConsignmentInwordService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/consignments/consignment/inword")
public class ConsignmentInwordController {

	final static Logger logger = Logger.getLogger(ConsignmentInwordController.class);

	@Autowired
	IConsignmentInwordService ConsignmentInwordService;

	@RequestMapping(value = "/dropdown/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of dropdown", response = ConsignmentDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(ConsignmentInwordService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get Consignment Inward list", response = ConsignmentResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmentInwardList(@RequestBody ConsignmentRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ConsignmentInwordService.getConsignmentInwardList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmentInwardList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Consignment Inward details", response = ConsignmentResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getConsignmentInwardDetails(@PathVariable Integer id,@PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(ConsignmentInwordService.getConsignmentInwardDetails(id,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getConsignmentInwardDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save Consignment Inword", response = ConsignmentRequest.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> saveConsignmentInward(@RequestBody ConsignmentRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ConsignmentInwordService.saveConsignmentInward(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveConsignmentInward failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Consignment Inword", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateConsignmentInword(@PathVariable Integer id,
			@RequestBody ConsignmentRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ConsignmentInwordService.updateConsignmentInword(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updateConsignmentInword failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Consignment Inword", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveConsignmentInword(@RequestBody ConsignmentRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(ConsignmentInwordService.approveConsignmentInword(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveConsignmentInword failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Consignment Inword", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelConsignmentInword(@PathVariable Integer id,
			@RequestBody ConsignmentRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ConsignmentInwordService.cancelConsignmentInword(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelConsignmentInword failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
