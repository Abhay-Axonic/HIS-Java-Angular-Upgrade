package com.param.pharmacy.master.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.dto.instruction.InstructionRequest;
import com.param.pharmacy.master.global.service.instruction.IInstructionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/globals/instruction/")
public class InstructionController {

	@Autowired
	IInstructionService InstructionService;
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Instruction", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Instruction Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized Create to Instruction"),
			@ApiResponse(code = 403, message = "Create Instruction is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> create(@RequestBody InstructionRequest instructionRequest) {
		return new ResponseEntity<CommonResponse>(InstructionService.create(instructionRequest), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Instruction", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Instruction Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Instruction"),
			@ApiResponse(code = 403, message = "Update Instruction is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> update(@PathVariable("id") Integer id,
			@RequestBody InstructionRequest instructionRequest) {
		instructionRequest.setInstructionId(id);
		return new ResponseEntity<CommonResponse>(InstructionService.update(instructionRequest), HttpStatus.CREATED);
	}
}
