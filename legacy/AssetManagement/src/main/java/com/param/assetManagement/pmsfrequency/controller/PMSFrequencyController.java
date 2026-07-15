package com.param.assetManagement.pmsfrequency.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.param.assetManagement.pmsfrequency.dto.PMSFrequencyDto;
import com.param.assetManagement.pmsfrequency.service.IPMSFrequencyService;
import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/asset/management/pms/frequency")
public class PMSFrequencyController {

	private static final Logger logger = Logger.getLogger(PMSFrequencyController.class);

	@Autowired
	IPMSFrequencyService pmsFrequencyService;
	
	 
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save pms frequency", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> save(@RequestBody PMSFrequencyDto request) throws Exception {
		try {
			return new ResponseEntity<Object>(pmsFrequencyService.save(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the pms frequency", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.PATCH)
	@ApiOperation(value = "update pms frequency", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> update(@RequestBody PMSFrequencyDto request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(pmsFrequencyService.update(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating the pms frequency", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/list/{unitId}", method = RequestMethod.GET)
	@ApiOperation(value = "Save pms frequency", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> list(@PathVariable Integer unitId) throws Exception {
		try {
			return new ResponseEntity<Object>(pmsFrequencyService.list(unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the pms frequency list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
}
