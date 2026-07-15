package com.param.pharmacy.master.unit.controller;

import java.util.List;

import io.swagger.models.auth.In;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.GenericResponseDto;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.StorewiseGenericMapperDetailsResponse;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.StorewiseGenericMapperListResponse;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.StorewiseGenericMapperRequest;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.VedResponseList;
import com.param.pharmacy.master.unit.service.storewiseGenericMapper.IStorewiseGenericMapperService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/unit/storewise/generic/mapper")
public class StorewiseGenericMapperController {

	@Autowired
	IStorewiseGenericMapperService StorewiseGenericMapperService;

	private static final Logger logger = Logger.getLogger(StorewiseGenericMapperController.class);

	@RequestMapping(value = "/dropdown", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = VedResponseList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getVed() throws JSONException {
		return new ResponseEntity<Object>(StorewiseGenericMapperService.getDropdownList(), HttpStatus.OK);
	}

	@RequestMapping(value = "/storeWiseGenericMapper/search/{unitId}/{orgId}/{name}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list of Generic", notes = "Get list of Generic", response = GenericResponseDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getStorewiseGenericMapperGenericAutoPopulateList(@PathVariable("name") String name,
																					@PathVariable("unitId") Integer unitId,
																				   @PathVariable ("orgId") Integer orgId)
			throws Exception {
		return new ResponseEntity<Object>(StorewiseGenericMapperService.getGenericList(name, unitId, orgId), HttpStatus.OK);

	}

	@RequestMapping(value = "unit/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Storewise Generic Mapper", response = StorewiseGenericMapperListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<StorewiseGenericMapperListResponse>> GetAll(@PathVariable Integer unitId) {
		ResponseEntity<List<StorewiseGenericMapperListResponse>> response = new ResponseEntity<List<StorewiseGenericMapperListResponse>>(
				StorewiseGenericMapperService.getAll(unitId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Storewise Generic Mapper By Id", response = StorewiseGenericMapperDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<StorewiseGenericMapperDetailsResponse> GetById(@PathVariable("id") Integer id) {
		ResponseEntity<StorewiseGenericMapperDetailsResponse> response = new ResponseEntity<StorewiseGenericMapperDetailsResponse>(
				StorewiseGenericMapperService.getById(id), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create Storewise Generic Mapper", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Storewise Generic Mapper Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Storewise Generic Mapper"),
			@ApiResponse(code = 403, message = "Create Storewise Generic Mapper is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(
			@RequestBody StorewiseGenericMapperRequest storewiseGenericMapperRequest) {
		return new ResponseEntity<CommonResponse>(StorewiseGenericMapperService.create(storewiseGenericMapperRequest),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Storewise Generic Mapper", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Storewise Generic Mapper Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Storewise Generic Mapper"),
			@ApiResponse(code = 403, message = "Update Storewise Generic Mapper is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id,
			@RequestBody StorewiseGenericMapperRequest storewiseGenericMapperRequest) {
		storewiseGenericMapperRequest.setId(id);
		try {
			return new ResponseEntity<Object>(StorewiseGenericMapperService.update(storewiseGenericMapperRequest),
					HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}
	}

}
