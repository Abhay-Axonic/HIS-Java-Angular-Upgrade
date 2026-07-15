package com.param.pharmacy.prescription.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.pharmacy.master.global.dto.storageUnit.StorageUnitListResponse;
import com.param.pharmacy.prescription.dto.IsInterventionDto;
import com.param.pharmacy.prescription.service.IPrescriptionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/prescription/")
public class PrescriptionController {

	@Autowired
	IPrescriptionService PrescriptionService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value = "Get Intervention status", response = StorageUnitListResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<IsInterventionDto> getPrescriptionIsIntervention(String prescriptionMongoId) {
		ResponseEntity<IsInterventionDto> response = new ResponseEntity<IsInterventionDto>(
				PrescriptionService.getPrescriptionIsIntervention(), HttpStatus.OK);

		return response;
	} 
}
