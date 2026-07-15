package com.param.procurement.consignment.controller;

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
import com.param.entity.model.procurement.ImplantIndent;
import com.param.global.common.ServiceException;
import com.param.global.pharmacy.dto.PatientConsumtionIssueSave;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumptionRequest;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumptionResponse;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionDetail;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionDropdown;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionIssueListDetails;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionIssueListResponse;
import com.param.procurement.consignment.service.patientConsumption.IPatientConsumptionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/consignments/patient/consumption")
public class PatientConsumptionController {

	final static Logger logger = Logger.getLogger(PatientConsumptionController.class);

	@Autowired
	IPatientConsumptionService PatientConsumptionService;
   //Dropdown for Both Implant Indend Patient Consumtion Screen
	//Pending Indend And ISsue List 
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = PatientConsumtionDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(PatientConsumptionService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	//Main Search for Implant Indend Patient Consumtion Screen
		//Pending Indend Search 
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient Consumption for Pending Indend list ", response = PatientConsumptionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumptionSearch(@RequestBody PatientConsumptionRequest request) {
		try {
			return new ResponseEntity<Object>(PatientConsumptionService.patientConsumptionSearch(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("patientConsumptionSearch failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	//Main Search for Implant Indend Patient Consumtion Screen
			//Issue List Search 
	
	@RequestMapping(value = "/list/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient Issue list ", response = PatientConsumtionIssueListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumableIssueSearch(@RequestBody PatientConsumptionRequest request) {
		try {
			return new ResponseEntity<Object>(PatientConsumptionService.patientConsumtionListSearch(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("patientConsumableIssueSearch failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/issue/search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get Patient Issue list ", response = PatientConsumtionIssueListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumableIssueSearchForImplant(@RequestBody PatientConsumptionRequest request) {
		try {
			return new ResponseEntity<Object>(PatientConsumptionService.patientConsumtionIssueListSearch(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("patientConsumableIssueSearchForImplant failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	//Get Details for Implant Indend Patient Consumtion Screen
	//Pending Indend Get Details
	@RequestMapping(value = "/{orgId}/{unitId}/{storeId}/{id}/{cookieStoreId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Patient Consumption details ", response = PatientConsumtionDetail.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumptionDetails(@PathVariable("id") Integer id,@PathVariable Integer unitId,@PathVariable Integer storeId,@PathVariable Integer orgId, @PathVariable Integer cookieStoreId) {
		try {
			return new ResponseEntity<Object>(PatientConsumptionService.patientConsumptionDetails(id,unitId,storeId,orgId, cookieStoreId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("patientConsumptionDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	//Get Details for Implant Indend Patient Consumtion Screen
		//Issue List Get Details
	@RequestMapping(value = "/list/{orgId}/{unitId}/{storeId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Patient Consumption issue List details ", response = PatientConsumtionIssueListDetails.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumableIssueDetails(@PathVariable("id") Integer id,@PathVariable Integer orgId, @PathVariable Integer unitId,@PathVariable Integer storeId) {
		try {
			return new ResponseEntity<Object>(PatientConsumptionService.patientConsumtionIssueListDetails(id,unitId,storeId,orgId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("patientConsumableIssueDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Patient Consumption", response = ImplantIndent.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> patientConsumptionSave(@PathVariable Integer id,
			@RequestBody PatientConsumtionIssueSave request) {
		try {
			return new ResponseEntity<Object>(PatientConsumptionService.patientConsumptionSave(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("patientConsumptionSave failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}

}
