package com.param.pharmacy.master.global.controller;

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
import com.param.pharmacy.master.global.dto.equipment.EquipmentRequest;
import com.param.pharmacy.master.global.dto.equipment.EquipmentResponse;
import com.param.pharmacy.master.global.service.equipment.IEquipmentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/globals/equipment/")
public class EquipmentController {
	
	@Autowired
	IEquipmentService EquipmentService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Equipment")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<EquipmentResponse>> GetAll() {
		ResponseEntity<List<EquipmentResponse>> response = new ResponseEntity<List<EquipmentResponse>>(
				EquipmentService.getAll(), HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Equipment", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Equipment Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Equipment"),
			@ApiResponse(code = 403, message = "Update Equipment is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("id") Integer id,
			@RequestBody EquipmentRequest equipmentRequest) {
		equipmentRequest.setId(id);
		return new ResponseEntity<CommonResponse>(EquipmentService.update(equipmentRequest),
				HttpStatus.CREATED);
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Save Equipment", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Equipment Saved successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Equipment"),
			@ApiResponse(code = 403, message = "Update Equipment is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Save(
			@RequestBody EquipmentRequest equipmentRequest) {
		return new ResponseEntity<CommonResponse>(EquipmentService.saveEquipment(equipmentRequest),
				HttpStatus.CREATED);
	}
	
}
