package com.param.pharmacy.master.global.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.dto.schedule.ScheduleDetailsResponse;
import com.param.pharmacy.master.global.dto.schedule.ScheduleListResponse;
import com.param.pharmacy.master.global.dto.schedule.ScheduleRequest;
import com.param.pharmacy.master.global.service.schedule.IScheduleService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping(value = "/api/globals/schedules")
public class ScheduleController {
	
	@Autowired
	IScheduleService ScheduleService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Schedules", response = ScheduleListResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), 
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ScheduleListResponse>> GetAll() {
		ResponseEntity<List<ScheduleListResponse>> response = new ResponseEntity<List<ScheduleListResponse>>(
				ScheduleService.getAll(), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Schedule By Id", response = ScheduleDetailsResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<ScheduleDetailsResponse> GetById(@PathVariable("id") Integer id) {
		ResponseEntity<ScheduleDetailsResponse> response = new ResponseEntity<ScheduleDetailsResponse>(
				ScheduleService.getById(id), HttpStatus.OK);

		return response;
	}

	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Schedule" , response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Schedule Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Schedule"),
			@ApiResponse(code = 403, message = "Create Schedule is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody ScheduleRequest scheduleRequest) {
		return new ResponseEntity<CommonResponse>(ScheduleService.create(scheduleRequest),
				HttpStatus.CREATED);
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Schedule", response = CommonResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Schedule Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Schedule"),
			@ApiResponse(code = 403, message = "Update Schedule is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("id") Integer id,
			@RequestBody ScheduleRequest scheduleRequest) {
		scheduleRequest.setId(id);
		return new ResponseEntity<CommonResponse>(ScheduleService.update(scheduleRequest),
				HttpStatus.CREATED);
	}

}
