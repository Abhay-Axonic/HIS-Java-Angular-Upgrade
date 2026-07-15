package com.param.inventory.scrap.controller;

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
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.inventory.scrap.dto.scrapRequest.ScrapRequest;
import com.param.inventory.scrap.dto.scrapRequest.ScrapResponse;
import com.param.inventory.scrap.dto.scrapRequest.ScrapsDropdownResponse;
import com.param.inventory.scrap.service.scrapRequestApproval.IScrapRequestApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@EnableWebMvc
@RestController
@RequestMapping(value = "/api/inventory/scraps/scrap/request/approval")
public class ScrapRequestApprovalController {

	private static final Logger logger = Logger.getLogger(ScrapRequestApprovalController.class);

	@Autowired
	IScrapRequestApprovalService ScrapRequestApprovalService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ScrapsDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(ScrapRequestApprovalService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/dropdowns/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ScrapsDropdownResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownListByUserId(@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(ScrapRequestApprovalService.getDropownsByUserId(userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownListByUserId failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Scrap Request Approval by search", response = ScrapResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getScrapRequestApprovalList(@RequestBody ScrapRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(ScrapRequestApprovalService.getScrapRequestApprovalList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getScrapRequestApprovalList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Scrap Request details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getScrapRequestApprovalDetails(@PathVariable Integer unitId,@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(ScrapRequestApprovalService.getScrapRequestApprovalDetails(id,unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getScrapRequestApprovalDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Scrap Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveScrapRequestApproval(@PathVariable Integer id,
			@RequestBody ScrapRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ScrapRequestApprovalService.approveScrapRequestApproval(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveScrapRequestApproval failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Scrap Request Approval", response = CommonResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelScrapRequestApproval(@PathVariable Integer id, @RequestBody ScrapRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ScrapRequestApprovalService.cancelScrapRequestApproval(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelScrapRequestApproval failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
