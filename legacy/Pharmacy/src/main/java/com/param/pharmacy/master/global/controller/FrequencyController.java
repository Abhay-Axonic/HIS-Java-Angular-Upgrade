package com.param.pharmacy.master.global.controller;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.dto.frequency.FrequencyDetailsResponse;
import com.param.pharmacy.master.global.dto.frequency.FrequencyRequest;
import com.param.pharmacy.master.global.dto.frequency.GetFrequencyResponse;
import com.param.pharmacy.master.global.service.frequency.IFrequencyService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static com.param.global.common.ICommonConstants.*;

@RestController
@RequestMapping(value = "/api/globals/frequency/")
public class FrequencyController {
	
	@Autowired
	IFrequencyService FrequencyService;

	private static final Logger logger = Logger.getLogger(FrequencyController.class);


	@RequestMapping(value = "{orgId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Frequencies", response = GetFrequencyResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<GetFrequencyResponse>> GetAll(@PathVariable Integer orgId) {
		ResponseEntity<List<GetFrequencyResponse>> response = new ResponseEntity<List<GetFrequencyResponse>>(
				FrequencyService.getAll(orgId), HttpStatus.OK);
		return response;
	}

	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.PATCH, consumes = "application/json")
	@ApiOperation(value = "Update Frequency", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Frequency Updated successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Frequency"),
			@ApiResponse(code = 403, message = "Update Frequency is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<Object> Update(@PathVariable("id") Integer id, @RequestBody FrequencyRequest frequencyRequest,@PathVariable Integer orgId) {
		frequencyRequest.setId(id);
		frequencyRequest.setOrganizationId(orgId);
		return new ResponseEntity<Object>(FrequencyService.updateFrequency(frequencyRequest), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Save Frequency", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Frequency Saved successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Tax"),
			@ApiResponse(code = 403, message = "Update Frequency is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public ResponseEntity<CommonResponse> Save(
			@RequestBody FrequencyRequest frequencyRequest) {
		return new ResponseEntity<CommonResponse>(FrequencyService.saveFrequency(frequencyRequest),
				HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Frequency By Id", response = FrequencyDetailsResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<FrequencyDetailsResponse> GetById(@PathVariable("id") Integer id) {
		ResponseEntity<FrequencyDetailsResponse>response = new ResponseEntity<FrequencyDetailsResponse>(FrequencyService.getById(id),
				HttpStatus.OK);
		return response;
	}
	
	/*@RequestMapping(value = "/dropdown", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = FrequencyResponseList.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAdministationTimes() throws JSONException {
		return new ResponseEntity<Object>(FrequencyService.getDropdownList(), HttpStatus.OK);
	}*/
	@RequestMapping(value = "/getConvertedLanguageListById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getConvertedLanguageListById(@RequestBody FrequencyRequest frequencyRequest) {
		try {
			return FrequencyService.getConvertedLanguageListById(frequencyRequest);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
