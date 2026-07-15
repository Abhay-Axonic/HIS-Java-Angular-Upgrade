package com.param.pharmacy.inPatient.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.PatientIndent;
import com.param.global.common.ServiceException;
import com.param.global.pharmacy.dto.PatientConsumableIssueSave;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueDetails;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueRequest;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueResponse;
import com.param.pharmacy.inPatient.service.patientConsumableIssue.IPatientConsumableIssueService;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentDropdown;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/pharmacy/in/patient/consumable/issue")
public class PatientConsumableIssueController {

	private static final Logger logger = Logger.getLogger(PatientConsumableIssueController.class);

	@Autowired
	private IPatientConsumableIssueService patientConsumableIssueService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = PatientIndentDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(patientConsumableIssueService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient Indent list ", response = PatientConsumableIssueResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumableIssueSearch(@RequestBody PatientConsumableIssueRequest request) {
		try {
			return new ResponseEntity<Object>(patientConsumableIssueService.patientConsumableIssueSearch(request),
					HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search/issue/acceptance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient Indent list ", response = PatientConsumableIssueResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumableIssueSearchForPatientIssueAcceptance(@RequestBody PatientConsumableIssueRequest request) {
		try {
			return new ResponseEntity<Object>(patientConsumableIssueService.patientConsumableIssueSearchForPatientIssueAcceptance(request),
					HttpStatus.OK);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "acceptance/{unitId}/{storeId}/{id}/{screenFlag}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Patient Consumable issue details ", response = PatientConsumableIssueDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumableIssueAcceptanceDetails(@PathVariable Integer unitId,@PathVariable Integer storeId,@PathVariable("id") Integer id,@PathVariable("screenFlag") Integer screenFlag) {
		try {
			return new ResponseEntity<Object>(patientConsumableIssueService.patientConsumableIssueAcceptanceDetails(id,screenFlag,storeId,unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{storeId}/{id}/{screenFlag}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Patient Consumable issue details ", response = PatientConsumableIssueDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumableIssueDetails(@PathVariable Integer unitId,@PathVariable Integer storeId,@PathVariable("id") Integer id,@PathVariable("screenFlag") Integer screenFlag) {
		try {
			return new ResponseEntity<Object>(patientConsumableIssueService.patientConsumableIssueDetails(id,screenFlag,storeId,unitId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "save Patient Consumable Issue", response = PatientIndent.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumableIssueSave(
			@RequestBody PatientConsumableIssueSave patientConsumableIssueSave) {
		try {
			
             if(patientConsumableIssueSave.getId() != null) {
				
				PatientConsumableIssueDetails test = patientConsumableIssueService.patientConsumableIssueDetails(patientConsumableIssueSave.getId(),1,patientConsumableIssueSave.getStoreId(),patientConsumableIssueSave.getUnitId()) ;
				Integer Ids = test.getStatusId().intValue();
				if(Ids == com.param.common.constants.Status.Completed.getStatus() || Ids == com.param.common.constants.Status.Closed.getStatus()) {
					CommonResponse response = new CommonResponse();
					response.setMessage("This transaction already processed by other user");
					return new ResponseEntity<Object>(response,HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			
			
			CommonResponse response =(CommonResponse) patientConsumableIssueService.patientConsumableIssueSave(patientConsumableIssueSave);
		/*	if(response.getObject()!=null && response.getObject1()!=null)
			{
				Issue issue=(Issue) response.getObject();
				PatientConsumableIssueSave idata= (PatientConsumableIssueSave) response.getObject1();
				patientConsumableIssueService.savePatientIssueAcceptance(issue, idata);
			}*/
			
			return new ResponseEntity<Object>(response,
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}

	}

}