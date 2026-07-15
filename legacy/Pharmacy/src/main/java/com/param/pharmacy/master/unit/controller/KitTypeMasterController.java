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
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterRequest;
import com.param.pharmacy.master.unit.dto.kit.KitTypeMasterResponse;
import com.param.pharmacy.master.unit.service.kit.IKitTypeMasterService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@RestController
@RequestMapping(value = "/api/units/kit/")
public class KitTypeMasterController {
	
	
	@Autowired
	IKitTypeMasterService IKitTypeMasterService;

	@RequestMapping(value = "/save", method = RequestMethod.POST ,consumes = "application/json")
	@ApiOperation(value = "Kit type Master Saved", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = " Saved successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Kit type"),
			@ApiResponse(code = 403, message = "Kit type is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> KitMasterSave(@RequestBody KitTypeMasterRequest KitTypeMasterRequest) {
		return new ResponseEntity<CommonResponse>(IKitTypeMasterService.saveKitMasterType(KitTypeMasterRequest), HttpStatus.CREATED);
	}

	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Equipment")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<KitTypeMasterResponse>> GetAll() {
		ResponseEntity<List<KitTypeMasterResponse>> response = new ResponseEntity<List<KitTypeMasterResponse>>(
				IKitTypeMasterService.getAllKitType(), HttpStatus.OK);
		return response;
	}
	
	
	
	
	  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	  
	  @ApiOperation(value = "Get Kit Type By Id", response =
			  KitTypeMasterResponse.class)
	  
	  @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
	  
	  @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
	  403, message = "forbidden!!!"),
	  
	  @ApiResponse(code = 404, message = "not found!!!") }) public
	  ResponseEntity<Object> GetById(@PathVariable("id") Integer id)
	  {
	  
	  ResponseEntity<Object> response = new ResponseEntity<Object>(IKitTypeMasterService.getById(id),
	  HttpStatus.OK); return response;
	  }
	  

		@RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH, consumes = "application/json")
		@ApiOperation(value = "Update Kit Type", response = CommonResponse.class)
		@ApiResponses(value = { @ApiResponse(code = 200, message = "Equipment Updated successfully"),
				@ApiResponse(code = 401, message = "You are Not authorized to Equipment"),
				@ApiResponse(code = 403, message = "Update Equipment is forbidden"),
				@ApiResponse(code = 404, message = "Resource Not found"),
				@ApiResponse(code = 405, message = "Method Not Allowed") })
		public ResponseEntity<CommonResponse> Update(@PathVariable("id") Integer id,
				@RequestBody KitTypeMasterRequest KitTypeMasterRequest) {
			KitTypeMasterRequest.setId(id);
			return new ResponseEntity<CommonResponse>(IKitTypeMasterService.update(KitTypeMasterRequest),
					HttpStatus.CREATED);
		}
	 
}
