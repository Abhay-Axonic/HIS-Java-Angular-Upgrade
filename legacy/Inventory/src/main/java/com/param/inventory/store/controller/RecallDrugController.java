package com.param.inventory.store.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.inventory.store.dto.RecallDrug.RecallDrugRequest;
import com.param.inventory.store.dto.RecallDrug.RecallDrugResponse;
import com.param.inventory.store.service.recallDrug.IRecallDrugService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/inventory/stores/recall/drug")
public class RecallDrugController {

	private static final Logger logger = Logger.getLogger(RecallDrugController.class);

	@Autowired
	IRecallDrugService RecallDrugService;

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of recall drug by search", response = RecallDrugResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getRecallDrugList(@RequestBody RecallDrugRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(RecallDrugService.getRecallDrugList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getRecallDrugList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
