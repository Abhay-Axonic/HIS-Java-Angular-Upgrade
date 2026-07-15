package com.param.pharmacy.ip.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.IssueAcceptanceDropdown;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceConsumableSearch;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceDetails;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearchResult;
import com.param.pharmacy.ip.service.pendingIssues.acceptance.consumable.IPendingIssuesAcceptanceConsumableService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/ip/pending/issues/acceptance/consumable")
public class PendingIssuesAcceptanceConsumableController {

	@Autowired
	private IPendingIssuesAcceptanceConsumableService pendingIssuesAcceptanceConsumableService;

	private static final Logger logger = Logger.getLogger(PendingIssuesAcceptanceConsumableController.class);

	@RequestMapping(value = "/dropdowns/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = IssueAcceptanceDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId) {
		try {
			return new ResponseEntity<Object>(pendingIssuesAcceptanceConsumableService.getDropdownList(unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get pending issues acceptance consumable list ", response = PendingIssuesAcceptanceSearchResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pendingIssuesAcceptanceConsumableSearch(
			@RequestBody PendingIssuesAcceptanceConsumableSearch pendingIssuesAcceptanceSearch) {
		try {
			return new ResponseEntity<Object>(pendingIssuesAcceptanceConsumableService
					.pendingIssuesAcceptanceConsumableSearch(pendingIssuesAcceptanceSearch), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get pending issues acceptance consumable details ", response = PendingIssuesAcceptanceDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pendingIssuesAcceptanceConsumableDetails(@PathVariable("id") Integer id) {
		try {
			return new ResponseEntity<Object>(
					pendingIssuesAcceptanceConsumableService.pendingIssuesAcceptanceConsumableDetails(id),
					HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.PATCH)
	@ApiOperation(value = "save pending issues acceptance consumable details ", response = PendingIssuesAcceptanceDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pendingIssuesAcceptanceConsumableSave(
			@RequestBody PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave) {
		try {
			return new ResponseEntity<Object>(pendingIssuesAcceptanceConsumableService
					.pendingIssuesAcceptanceConsumableSave(pendingIssuesAcceptanceSave), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}