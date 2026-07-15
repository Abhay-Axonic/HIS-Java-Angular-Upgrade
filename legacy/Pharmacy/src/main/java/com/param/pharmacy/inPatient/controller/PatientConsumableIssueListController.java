package com.param.pharmacy.inPatient.controller;

import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientDetailsRequestForDrugRecall;
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
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueListResponse;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientConsumableIssueListDetails;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientConsumableIssueListRequest;
import com.param.pharmacy.inPatient.service.patientConsumableIssueList.IPatientConsumableIssueListService;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentDropdown;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/pharmacy/in/patient/consumable/issue/list")
public class PatientConsumableIssueListController {

	@Autowired
	private IPatientConsumableIssueListService patientConsumableIssueListService;

	private static final Logger logger = Logger.getLogger(PatientConsumableIssueListController.class);

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = PatientIndentDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(patientConsumableIssueListService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient Issue list ", response = PatientConsumableIssueListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumableIssueSearch(@RequestBody PatientConsumableIssueListRequest request) {
		try {
			return new ResponseEntity<Object>(patientConsumableIssueListService.patientConsumableIssueSearch(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search/drug/recall", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient Issue list ", response = PatientConsumableIssueListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumableIssueSearchForDrugRecall(@RequestBody PatientDetailsRequestForDrugRecall request) {
		try {
			return new ResponseEntity<Object>(patientConsumableIssueListService.patientConsumableIssueSearchForDrugRecall(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Patient Consumable issue details ", response = PatientConsumableIssueListDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumableIssueDetails(@PathVariable("id") Integer id,@PathVariable Integer unitId) {
		try {
			return new ResponseEntity<Object>(patientConsumableIssueListService.patientConsumableIssueDetails(id,unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}