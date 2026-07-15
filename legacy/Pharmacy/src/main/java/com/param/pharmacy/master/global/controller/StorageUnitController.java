package com.param.pharmacy.master.global.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.storageUnit.StorageUnitDetailsResponse;
import com.param.pharmacy.master.global.dto.storageUnit.StorageUnitListResponse;
import com.param.pharmacy.master.global.dto.storageUnit.StorageUnitRequest;
import com.param.pharmacy.master.global.service.storageUnit.IStorageUnitService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/globals/storage/units/")
public class StorageUnitController {

	@Autowired
	IStorageUnitService StorageUnitService;

	private static final Logger logger = Logger.getLogger(StorageUnitController.class);

	@RequestMapping(value = "{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Storage Units", response = StorageUnitListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<StorageUnitListResponse>> GetAll(@PathVariable Integer orgId) {
		ResponseEntity<List<StorageUnitListResponse>> response = new ResponseEntity<List<StorageUnitListResponse>>(
				StorageUnitService.getAll(orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Storage Units By Id", response = StorageUnitDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<StorageUnitDetailsResponse> GetById(@PathVariable("id") Integer id,@PathVariable Integer orgId) {

		ResponseEntity<StorageUnitDetailsResponse> response = new ResponseEntity<StorageUnitDetailsResponse>(
				StorageUnitService.getById(id,orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "{orgId}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Storage Units", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Storage Units Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Storage Units"),
			@ApiResponse(code = 403, message = "Create Storage Units is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody StorageUnitRequest storageUnitRequest,@PathVariable Integer orgId) 
	{
		storageUnitRequest.setOrganizationId(orgId);
		return new ResponseEntity<CommonResponse>(StorageUnitService.create(storageUnitRequest), HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Storage Units", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Storage Units Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Storage Units"),
			@ApiResponse(code = 403, message = "Update Storage Units is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id,@PathVariable Integer orgId,
			@RequestBody StorageUnitRequest storageUnitRequest) {
		
		try {
			storageUnitRequest.setOrganizationId(orgId);
			storageUnitRequest.setId(id);
			return new ResponseEntity<Object>(StorageUnitService.update(storageUnitRequest), HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}

	}

}
