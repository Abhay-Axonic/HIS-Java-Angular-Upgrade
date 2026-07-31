package com.param.pharmacy.master.unit.controller;

import java.util.List;

import com.param.pharmacy.master.unit.dto.mainStore.MainStoreRequestNew;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.dto.productCategory.ProductCategoryDetailResponse;
import com.param.pharmacy.master.unit.dto.mainStore.MainStoreDetailsResponse;
import com.param.pharmacy.master.unit.dto.mainStore.MainStoreListResponse;
import com.param.pharmacy.master.unit.dto.mainStore.MainStoreRequest;
import com.param.pharmacy.master.unit.service.mainStore.IMainStoreService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/unit/main/stores")
public class MainStoreController {

	@Autowired
	IMainStoreService MainStoreService;
	
	@RequestMapping(value = "/{unitId}/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Main Stores", response = MainStoreListResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<MainStoreListResponse>> GetAll(@PathVariable Integer unitId , @PathVariable Integer orgId) {
		ResponseEntity<List<MainStoreListResponse>> response = new ResponseEntity<List<MainStoreListResponse>>(
				MainStoreService.getAll(unitId, orgId), HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Main Stores By Id", response = ProductCategoryDetailResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"),
			@ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<MainStoreDetailsResponse> GetById(@PathVariable("id") Integer id) {
		ResponseEntity<MainStoreDetailsResponse> response = new ResponseEntity<MainStoreDetailsResponse>(
				MainStoreService.getById(id), HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Store", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Store Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Store"),
			@ApiResponse(code = 403, message = "Create Store is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(@RequestBody MainStoreRequest mainStoreRequest) {
		return new ResponseEntity<CommonResponse>(MainStoreService.create(mainStoreRequest),
				HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Store", response = CommonResponse.class)
	@ApiResponses(value = { 
			@ApiResponse(code = 200, message = "Store Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Store"),
			@ApiResponse(code = 403, message = "Update Store is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("id") Integer id,
			@RequestBody MainStoreRequest mainStoreRequest) {
		mainStoreRequest.setId(id);
		return new ResponseEntity<CommonResponse>(MainStoreService.update(mainStoreRequest),
				HttpStatus.CREATED);
	}
	@RequestMapping(value = "/{id}/{status}/{orgId}/{unitId}/{userId}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Store", response = CommonResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Store Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Store"),
			@ApiResponse(code = 403, message = "Update Store is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Update(@PathVariable("id") Integer id,
												 @PathVariable("status") Boolean status ,
												 @PathVariable("orgId") Integer orgId,
												 @PathVariable("unitId") Integer unitId,
												 @PathVariable("userId") Integer userId) {
		return new ResponseEntity<CommonResponse>(MainStoreService.updateStatus(id,status, orgId,unitId,userId),
				HttpStatus.CREATED);
		/*if (MainStoreService.updateStatus(id,status).getStatus().equals(HttpStatus.OK.toString()))
		{
			return new ResponseEntity<CommonResponse>(MainStoreService.updateStatus(id,status),
					HttpStatus.CREATED);
		}
		else
		{
			return new ResponseEntity<CommonResponse>(MainStoreService.updateStatus(id,status),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}*/

	}

	/**
	 * New method created for updating/adding store with multiple visitTypes
	 * we can use above function for updating/adding store with single visitType
	 */
	@RequestMapping(value = "/new/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Store", response = CommonResponse.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Store Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Store"),
			@ApiResponse(code = 403, message = "Update Store is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> updateNew(@PathVariable("id") Integer id,
												 @RequestBody MainStoreRequestNew mainStoreRequest) {
		mainStoreRequest.setId(id);
		return new ResponseEntity<CommonResponse>(MainStoreService.updateNew(mainStoreRequest),
				HttpStatus.CREATED);
	}

}
