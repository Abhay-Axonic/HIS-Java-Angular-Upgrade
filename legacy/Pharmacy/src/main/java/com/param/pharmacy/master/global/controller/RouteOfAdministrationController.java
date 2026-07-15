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
import com.param.pharmacy.master.global.dto.routeOfAdministration.RouteOfAdministrationDetailsResponse;
import com.param.pharmacy.master.global.dto.routeOfAdministration.RouteOfAdministrationListResponse;
import com.param.pharmacy.master.global.dto.routeOfAdministration.RouteOfAdministrationRequest;
import com.param.pharmacy.master.global.service.routeOfAdministration.IRouteOfAdministrationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "api/global/route/of/administration")
public class RouteOfAdministrationController {

	private static final Logger logger = Logger.getLogger(RouteOfAdministrationController.class);

	@Autowired
	IRouteOfAdministrationService RouteOfAdministrationService;

	@RequestMapping(value = "/{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Route Of Administrations", response = RouteOfAdministrationListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<RouteOfAdministrationListResponse>> GetAll(@PathVariable Integer orgId) {
		ResponseEntity<List<RouteOfAdministrationListResponse>> response = new ResponseEntity<List<RouteOfAdministrationListResponse>>(
				RouteOfAdministrationService.getAll(orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Route Of Administration By Id", response = RouteOfAdministrationDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<RouteOfAdministrationDetailsResponse> GetById(@PathVariable("id") Integer id,@PathVariable Integer orgId) {

		ResponseEntity<RouteOfAdministrationDetailsResponse> response = new ResponseEntity<RouteOfAdministrationDetailsResponse>(
				RouteOfAdministrationService.getById(id,orgId), HttpStatus.OK);

		return response;
	}

	@RequestMapping(value = "/{orgId}/{userId}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Create RouteOfAdministration", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "RouteOfAdministration Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to RouteOfAdministration"),
			@ApiResponse(code = 403, message = "Create RouteOfAdministration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Create(
			@RequestBody RouteOfAdministrationRequest routeOfAdministrationRequest,@PathVariable Integer userId,@PathVariable Integer orgId) {
		routeOfAdministrationRequest.setOrganizationId(orgId);
		routeOfAdministrationRequest.setUserId(userId);
		return new ResponseEntity<CommonResponse>(RouteOfAdministrationService.create(routeOfAdministrationRequest),
				HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update RouteOfAdministration", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "RouteOfAdministration Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to RouteOfAdministration"),
			@ApiResponse(code = 403, message = "Update RouteOfAdministration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id,
			@RequestBody RouteOfAdministrationRequest routeOfAdministrationRequest,@PathVariable Integer orgId) {
		routeOfAdministrationRequest.setId(id);
		routeOfAdministrationRequest.setOrganizationId(orgId);
		try {
			return new ResponseEntity<Object>(RouteOfAdministrationService.update(routeOfAdministrationRequest),
					HttpStatus.CREATED);
		} catch (CustomeException CustomeRespone) {
			logger.error(CustomeRespone.getMessage(), CustomeRespone);
			throw new ServiceException(CustomeRespone.getMessage());
		}

	}

	/*
	 * @RequestMapping(value = "/dropdown", method = RequestMethod.GET)
	 * 
	 * @ApiOperation(value = "Get list for populating dropdown values", response =
	 * FormulationTypeAndROAList.class)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
	 * 
	 * @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
	 * 403, message = "forbidden!!!"),
	 * 
	 * @ApiResponse(code = 404, message = "not found!!!") }) public @ResponseBody
	 * ResponseEntity<Object> getFormulationTypeAndROA() throws JSONException {
	 * return new
	 * ResponseEntity<Object>(RouteOfAdministrationService.getDropdownList(),
	 * HttpStatus.OK); }
	 */
}
