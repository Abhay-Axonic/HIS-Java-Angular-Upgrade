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
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.IssueAcceptanceMadicationDropdown;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.MedicationHandoverSaveRequest;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceDetails;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearch;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearchResult;
import com.param.pharmacy.ip.service.pendingIssues.acceptance.medication.IPendingIssuesAcceptanceMedicationService;
import com.param.pharmacy.nursing.discharge.dto.MedicationReconciliationSearchRequest;
import com.param.global.common.ServiceException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/ip/pending/issues/acceptance/medication")
public class PendingIssuesAcceptanceMedicationController {

	@Autowired
	private IPendingIssuesAcceptanceMedicationService pendingIssuesAcceptanceMedicationService;

	private static final Logger logger = Logger.getLogger(PendingIssuesAcceptanceMedicationController.class);

	@RequestMapping(value = "/dropdowns/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = IssueAcceptanceMadicationDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId) {
		try {
			return new ResponseEntity<Object>(pendingIssuesAcceptanceMedicationService.getDropdownList(unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get pending issues acceptance medication list ", response = PendingIssuesAcceptanceSearchResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pendingIssuesAcceptanceMedicationSearch(
			@RequestBody PendingIssuesAcceptanceSearch pendingIssuesAcceptanceSearch) {
		try {
			return new ResponseEntity<Object>(pendingIssuesAcceptanceMedicationService
					.pendingIssuesAcceptanceMedicationSearch(pendingIssuesAcceptanceSearch), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get pending issues acceptance medication details ", response = PendingIssuesAcceptanceDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pendingIssuesAcceptanceMedicationDetails(@PathVariable("id") Integer id) {
		try {
			return new ResponseEntity<Object>(
					pendingIssuesAcceptanceMedicationService.pendingIssuesAcceptanceMedicationDetails(id),
					HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.PATCH)
	@ApiOperation(value = "save pending issues acceptance medication details ", response = PendingIssuesAcceptanceDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> pendingIssuesAcceptanceMedicationSave(
			@RequestBody PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave) {
		try {
			return new ResponseEntity<Object>(pendingIssuesAcceptanceMedicationService
					.pendingIssuesAcceptanceMedicationSave(pendingIssuesAcceptanceSave), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	
	
	// Medication handover API based on admission ID
	@RequestMapping(value = "/medicationhandoversearch/{admissionId}", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get pending issues acceptance medication list ", response = PendingIssuesAcceptanceSearchResult.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> medicationHandoverSearch(
			@RequestBody MedicationReconciliationSearchRequest pendingIssuesAcceptanceSearch, @PathVariable("admissionId") Integer admissionId) {
		try {
			return new ResponseEntity<Object>(pendingIssuesAcceptanceMedicationService.medicationHandoverSearch(pendingIssuesAcceptanceSearch , admissionId), HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	// Medication handover details API based on admission ID
		@RequestMapping(value = "/medicationhandoverdetail/{unitId}/{admissionId}", method = RequestMethod.GET)
		@ApiOperation(value = "Get pending issues acceptance medication list ", response = PendingIssuesAcceptanceSearchResult.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
				@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
				@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<Object> medicationHandoverDetail( @PathVariable("admissionId") Integer admissionId) {
			try {
				return new ResponseEntity<Object>(pendingIssuesAcceptanceMedicationService.medicationHandoverDetail(admissionId), HttpStatus.OK);

			} catch (Exception ex) {
				logger.error(ex.getMessage(), ex);
				throw new ServiceException(ex.getMessage());
			}
		}
		
		    // Medication handover save API
				@RequestMapping(value = "/medicationhandoversave", method = RequestMethod.POST)
				@ApiOperation(value = "medication handover save ", response = PendingIssuesAcceptanceSearchResult.class)
				@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
						@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
						@ApiResponse(code = 404, message = "not found!!!") })
				public ResponseEntity<Object> medicationHandoverSave( @RequestBody MedicationHandoverSaveRequest medicationHandoverSaveRequest) {
					try {
						return new ResponseEntity<Object>(pendingIssuesAcceptanceMedicationService.medicationHandoverDetail(medicationHandoverSaveRequest), HttpStatus.OK);

					} catch (Exception ex) {
						logger.error(ex.getMessage(), ex);
						throw new ServiceException(ex.getMessage());
					}
				}
}