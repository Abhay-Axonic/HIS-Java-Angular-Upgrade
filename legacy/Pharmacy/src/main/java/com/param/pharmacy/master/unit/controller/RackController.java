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
import com.param.pharmacy.master.unit.dto.rack.RackDetailsResponse;
import com.param.pharmacy.master.unit.dto.rack.RackListResponse;
import com.param.pharmacy.master.unit.dto.rack.RackRequest;
import com.param.pharmacy.master.unit.service.rack.IRackService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/units/rack")
public class RackController {

	@Autowired
	IRackService RackService;

	@RequestMapping(value = "/{unitId}/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Racks", response = RackListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<RackListResponse>> GetAll(@PathVariable Integer unitId,@PathVariable Integer orgId) {
		ResponseEntity<List<RackListResponse>> response = new ResponseEntity<List<RackListResponse>>(
				RackService.getAll(unitId,orgId), HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Rack By Id", response = RackDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
		public ResponseEntity<RackDetailsResponse> GetById(@PathVariable("id") Integer id) {
			ResponseEntity<RackDetailsResponse> response = new ResponseEntity<RackDetailsResponse>(
					RackService.getById(id), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Rack", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Rack Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Rack"),
			@ApiResponse(code = 403, message = "Create Rack is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody RackRequest rackRequest) {
		return new ResponseEntity<CommonResponse>(RackService.create(rackRequest), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Rack", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Rack Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Rack"),
			@ApiResponse(code = 403, message = "Update Rack is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id, @RequestBody RackRequest rackRequest) {
		rackRequest.setId(id);
		return new ResponseEntity<Object>(RackService.updateRack(rackRequest), HttpStatus.CREATED);
	}

}