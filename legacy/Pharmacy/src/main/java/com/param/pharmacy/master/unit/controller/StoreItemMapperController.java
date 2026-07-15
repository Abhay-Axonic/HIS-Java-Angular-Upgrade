package com.param.pharmacy.master.unit.controller;

import java.math.BigInteger;
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
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dto.MultipleStoreRequest;
import com.param.pharmacy.master.unit.dto.storeItemMapper.GetStoreItemRequest;
import com.param.pharmacy.master.unit.dto.storeItemMapper.SIResponse;
import com.param.pharmacy.master.unit.dto.storeItemMapper.StoreItemMapperDetailsResponse;
import com.param.pharmacy.master.unit.dto.storeItemMapper.StoreItemMapperListResponse;
import com.param.pharmacy.master.unit.service.storeItemMapper.IStoreItemMapperService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/unit/store/item/mapper")
public class StoreItemMapperController {
	
	@Autowired
	IStoreItemMapperService StoreItemMapperService;
 
	@RequestMapping(value = "store/{storeId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Store Item Mapper", response = StoreItemMapperListResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<StoreItemMapperListResponse>> GetAll(@PathVariable ("storeId") Integer storeId) {
		ResponseEntity<List<StoreItemMapperListResponse>> response = new ResponseEntity<List<StoreItemMapperListResponse>>(
				StoreItemMapperService.getAll(storeId), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Store Item Mapper By Id", response = StoreItemMapperDetailsResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<StoreItemMapperDetailsResponse> GetById(@PathVariable("id") BigInteger id) {

		ResponseEntity<StoreItemMapperDetailsResponse> response = new ResponseEntity<StoreItemMapperDetailsResponse>(
				StoreItemMapperService.getById(id), HttpStatus.OK);

		return response;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Store Item Mapper", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Store Item Mapper Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Store Item Mapper"),
			@ApiResponse(code = 403, message = "Create Store Item Mapper is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody List<GetStoreItemRequest> getStoreItemRequest) {
		return new ResponseEntity<CommonResponse>(StoreItemMapperService.create(getStoreItemRequest),
				HttpStatus.CREATED);
	}


	@RequestMapping(value = "/user/{userId}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Store Item Mapper", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Store Item Mapper Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized toStore Item Mapper"),
			@ApiResponse(code = 403, message = "UpdateStore Item Mapper is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@RequestBody List<SIResponse> response,@PathVariable Integer userId) {
		//ResponseEntity<List<SIResponse>> response = new ResponseEntity<List<SIResponse>>(
		return new ResponseEntity<CommonResponse>(StoreItemMapperService.update(response,userId), HttpStatus.OK);
	}


	@RequestMapping(value = "multiplestore", method = RequestMethod.POST , consumes = "application/json")
	@ApiOperation(value = "Get all Store Item Mapper", response = StoreItemMapperListResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<CommonResponse> GetMultipleStore(@RequestBody StoreItemMapperListResponse request) {
		ResponseEntity<CommonResponse> response = new ResponseEntity<CommonResponse> (
				StoreItemMapperService.GetMultipleStore(request), HttpStatus.OK);
		return response;
	}
	// item mapping removal
	@RequestMapping(value = "/remove", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Store Item Mapper", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Store Item Mapper remove successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized toStore Item Mapper"),
			@ApiResponse(code = 403, message = "UpdateStore Item Mapper is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> removeItemMapping(@RequestBody SIResponse request) throws CustomeException {
		//ResponseEntity<List<SIResponse>> response = new ResponseEntity<List<SIResponse>>(
		return new ResponseEntity<CommonResponse>(StoreItemMapperService.removeItemMapping(request), HttpStatus.OK);
	}
	@RequestMapping(value = "multiplestore/{id}", method = RequestMethod.POST , consumes = "application/json")
	@ApiOperation(value = "Get all Store Item Mapper", response = StoreItemMapperListResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<StoreItemMapperListResponse>> GetTransactionCount(@RequestBody List<MultipleStoreRequest> request,@PathVariable("id") BigInteger itemId) {
		ResponseEntity<List<StoreItemMapperListResponse>> response = new ResponseEntity<List<StoreItemMapperListResponse>>(
				StoreItemMapperService.GetTransactionCount(request,itemId), HttpStatus.OK);
		return response;
	}

}
