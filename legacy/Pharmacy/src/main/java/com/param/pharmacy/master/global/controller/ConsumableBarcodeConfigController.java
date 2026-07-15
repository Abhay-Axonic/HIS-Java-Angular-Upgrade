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
import com.param.pharmacy.master.global.dto.consumableBarcodeConfig.ConsumableBarcodeConfigRequest;
import com.param.pharmacy.master.global.dto.consumableBarcodeConfig.ConsumableBarcodeConfigResponse;
import com.param.pharmacy.master.global.service.consumableBarcodeConfig.IConsumableBarcodeConfigService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/globals/consumableBarcodeConfig/")
public class ConsumableBarcodeConfigController {

	@Autowired
	IConsumableBarcodeConfigService ConsumableBarcodeConfigService;

	@RequestMapping(value = "/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all ConsumableBarcodeConfig")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ConsumableBarcodeConfigResponse>> GetAll(@PathVariable Integer unitId) {
		ResponseEntity<List<ConsumableBarcodeConfigResponse>> response = new ResponseEntity<List<ConsumableBarcodeConfigResponse>>(
				ConsumableBarcodeConfigService.getAll(unitId), HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update ConsumableBarcodeConfig", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Consumable Barcode Config Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Consumable Barcode Config"),
			@ApiResponse(code = 403, message = "Update Consumable Barcode Config is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("id") Integer id,
			@RequestBody ConsumableBarcodeConfigRequest consumableBarcodeConfigRequest) {
		consumableBarcodeConfigRequest.setId(id);
		return new ResponseEntity<CommonResponse>(ConsumableBarcodeConfigService.update(consumableBarcodeConfigRequest),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Save Equipment", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "ConsumableBarcodeConfig Saved successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to ConsumableBarcodeConfig"),
			@ApiResponse(code = 403, message = "Update ConsumableBarcodeConfig is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Save(
			@RequestBody ConsumableBarcodeConfigRequest consumableBarcodeConfigRequest) {
		return new ResponseEntity<CommonResponse>(
				ConsumableBarcodeConfigService.saveConsumableBarcodeConfig(consumableBarcodeConfigRequest),
				HttpStatus.CREATED);
	}

}
