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
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.global.dto.generic.GenericDetailsResponse;
import com.param.pharmacy.master.global.dto.generic.GenericListResponse;
import com.param.pharmacy.master.unit.dto.configuration.PrefixConfigurationDropdown;
import com.param.pharmacy.master.unit.dto.configuration.PrefixConfigurationDto;
import com.param.pharmacy.master.unit.service.configuration.IPrefixConfigurationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import org.apache.log4j.Logger;

@RestController
@RequestMapping(value = "/api/units/configuration/prefixconfiguration")
public class PrefixConfigurationController {

	private static final Logger logger = Logger.getLogger(PrefixConfigurationController.class);

	@Autowired
	IPrefixConfigurationService prefixConfigurationService;
	
	@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = PrefixConfigurationDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList() {
		try
		{
			return new ResponseEntity<Object>(prefixConfigurationService.getDropdownList(), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save Prefix Configuration", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> savePrefixConfiguration(@RequestBody PrefixConfigurationDto request) throws Exception {
		try {
			return new ResponseEntity<Object>(prefixConfigurationService.savePrefixConfiguration(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Prefix Configuration", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Prefix Configuration Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Prefix Configuration"),
			@ApiResponse(code = 403, message = "Update Prefix Configuration is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> update(@PathVariable("id") Integer id, @RequestBody PrefixConfigurationDto prefixConfigurationDto) {
		try {
			prefixConfigurationDto.setId(id);
			return new ResponseEntity<Object>(prefixConfigurationService.update(prefixConfigurationDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Prefix Configuration By Id", response = GenericDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<PrefixConfigurationDto> getById(@PathVariable("id") Integer id) {

		ResponseEntity<PrefixConfigurationDto> response = new ResponseEntity<PrefixConfigurationDto>(
				prefixConfigurationService.getById(id), HttpStatus.OK);
		return response;
	}
	
	@RequestMapping(value = "/orgUnitWiseList/{unitId}/{orgId}/{flag}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Prefix Configuration", response = GenericListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<PrefixConfigurationDto>> getAll(@PathVariable("flag") Boolean flag,@PathVariable Integer unitId,@PathVariable Integer orgId) {
		ResponseEntity<List<PrefixConfigurationDto>> response = new ResponseEntity<List<PrefixConfigurationDto>>(
				prefixConfigurationService.getAll(flag,orgId,unitId), HttpStatus.OK);

		return response;
	}
	
}
