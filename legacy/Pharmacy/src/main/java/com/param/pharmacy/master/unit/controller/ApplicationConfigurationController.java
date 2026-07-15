package com.param.pharmacy.master.unit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.unit.dto.applicationConfiguration.ApplicationConfigurationRequest;
import com.param.pharmacy.master.unit.dto.applicationConfiguration.ApplicationConfigurationResponse;
import com.param.pharmacy.master.unit.service.applicationConfiguration.IApplicationConfigurationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/units/applications/configurations/")
public class ApplicationConfigurationController {

	@Autowired
	IApplicationConfigurationService ApplicationConfigurationService;
	
//  GET
	@RequestMapping(value = "{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Application_Configuration")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ApplicationConfigurationResponse>> GetAll(@PathVariable Integer unitId) {
		ResponseEntity<List<ApplicationConfigurationResponse>> response = new ResponseEntity<List<ApplicationConfigurationResponse>>(
				ApplicationConfigurationService.getAll(unitId), HttpStatus.OK);
		return response;
	}
	
//  NEW SAVE
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Save Application_Configuration", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Application_Configuration Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Application_Configuration"),
			@ApiResponse(code = 403, message = "Update Application_Configuration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> SaveNew(@RequestBody ApplicationConfigurationRequest applicationConfigurationRequest) {
		return new ResponseEntity<CommonResponse>(ApplicationConfigurationService.saveNew(applicationConfigurationRequest), HttpStatus.CREATED);
	}
	
//	UPDATE
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Pharmacy Application Configuration", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Application_Configuration Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Application_Configuration"),
			@ApiResponse(code = 403, message = "Update Application_Configuration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> UpdatePharmacy(@PathVariable("id") Integer id,
			@RequestBody ApplicationConfigurationRequest applicationConfigurationRequest) {
		applicationConfigurationRequest.setId(id);
		return new ResponseEntity<CommonResponse>(
				ApplicationConfigurationService.updateApplicationConfiguration(applicationConfigurationRequest), HttpStatus.CREATED);
	}

	/*@RequestMapping(value = "/inventory/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Application_Configuration", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Application_Configuration Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Application_Configuration"),
			@ApiResponse(code = 403, message = "Update Application_Configuration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("id") Integer id,
			@RequestBody ApplicationConfigurationRequest applicationConfigurationRequest) {
		applicationConfigurationRequest.setId(id);
		return new ResponseEntity<CommonResponse>(
				ApplicationConfigurationService.update(applicationConfigurationRequest), HttpStatus.CREATED);
	}
	*/

//	SAVE
	/*@RequestMapping(value = "/{unitId}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Application_Configuration", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Application_Configuration Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Application_Configuration"),
			@ApiResponse(code = 403, message = "Update Application_Configuration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("unitId") Integer unitId,
			@RequestBody ApplicationConfigurationRequest applicationConfigurationRequest) {
		applicationConfigurationRequest.setUnitId(unitId);
		return new ResponseEntity<CommonResponse>(
				ApplicationConfigurationService.update(applicationConfigurationRequest), HttpStatus.CREATED);
	}
*/
	@RequestMapping(value = "/inventory/save", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Save Application_Configuration", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Application_Configuration Saved successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Application_Configuration"),
			@ApiResponse(code = 403, message = "Update Application_Configuration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Save(
			@RequestBody ApplicationConfigurationRequest applicationConfigurationRequest) {
		return new ResponseEntity<CommonResponse>(
				ApplicationConfigurationService.saveApplicationConfiguration(applicationConfigurationRequest),
				HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/pharmacy", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Pharmacy Application_Configuration")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ApplicationConfigurationResponse>> GetAllPharmacy() {
		ResponseEntity<List<ApplicationConfigurationResponse>> response = new ResponseEntity<List<ApplicationConfigurationResponse>>(
				ApplicationConfigurationService.getAllPharmacy(), HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/pharmacy/save", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Save Pharmacy Application Configuration", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Application_Configuration Saved successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Application_Configuration"),
			@ApiResponse(code = 403, message = "Update Application_Configuration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> SavePharmacy(@RequestBody ApplicationConfigurationRequest applicationConfigurationRequest) {
		return new ResponseEntity<CommonResponse>(
				ApplicationConfigurationService.savePharmacyApplicationConfiguration(applicationConfigurationRequest),
				HttpStatus.CREATED);
	}
	
}
