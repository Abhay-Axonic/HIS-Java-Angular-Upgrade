package com.param.pharmacy.ip.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.global.dto.global.CommonDropdown;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionSearchResult;
import com.param.pharmacy.ip.dto.patientIssueRejectionConsumable.PatientIssueRejectionConsumableSearch;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.service.patientIssueRejectionConsumable.IPatientPendingIssuesRejectionConsumableService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/ip/patient/issues/rejection/consumable")
public class PatientPendingIssuesRejectionConsumableController {

	@Autowired
	private IPatientPendingIssuesRejectionConsumableService patientPendingIssuesRejectionConsumableService;

	private static final Logger logger = Logger.getLogger(PatientPendingIssuesRejectionConsumableController.class);

	@RequestMapping(value = "/dropdowns/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = CommonDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable("unitId") Integer unitId) {
		try {
			return new ResponseEntity<Object>(patientPendingIssuesRejectionConsumableService.getDropdownList(unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get patient issues rejection consumable list ", response = PatientIssueRejectionSearchResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIssuesRejectionConsumableSearch(
			@RequestBody PatientIssueRejectionConsumableSearch patientIssueRejectionSearch) {
		try {
			return new ResponseEntity<Object>(patientPendingIssuesRejectionConsumableService
					.patientIssuesRejectionConsumableSearch(patientIssueRejectionSearch), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get patient issues rejection consumable details ", response = PatientIssueRejectionSearchResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIssuesRejectionConsumableDetails(@PathVariable("id") Integer id) {
		try {
			return new ResponseEntity<Object>(
					patientPendingIssuesRejectionConsumableService.patientIssuesRejectionConsumableDetails(id),
					HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.PATCH)
	@ApiOperation(value = "save patient issues rejection consumable ", response = PatientIssueRejectionSearchResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientIssuesRejectionMedicationSave(
			@RequestBody PendingIssuesAcceptanceSave patientIssueRejectionSave) {
		try {
			return new ResponseEntity<Object>(patientPendingIssuesRejectionConsumableService
					.patientIssuesRejectionMedicationSave(patientIssueRejectionSave), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}