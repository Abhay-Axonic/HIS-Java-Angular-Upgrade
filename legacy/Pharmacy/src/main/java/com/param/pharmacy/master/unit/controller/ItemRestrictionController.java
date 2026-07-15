package com.param.pharmacy.master.unit.controller;

import java.math.BigInteger;
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
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.unit.dto.itemRestriction.ItemRestrictionRequest;
import com.param.pharmacy.master.unit.dto.itemRestriction.ItemRestrictionResponse;
import com.param.pharmacy.master.unit.service.ItemRestriction.IItemRestrictionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/unit/item/restrictions/")
public class ItemRestrictionController {

	@Autowired
	IItemRestrictionService ItemRestrictionService;

	private static final Logger logger = Logger.getLogger(ItemRestrictionController.class);

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value = "Get all Item Restriction")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<ItemRestrictionResponse>> GetAll() {
		ResponseEntity<List<ItemRestrictionResponse>> response = new ResponseEntity<List<ItemRestrictionResponse>>(
				ItemRestrictionService.getAll(), HttpStatus.OK);
		return response;
	}


	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update store item restriction", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateStoreItemRestriction(@PathVariable BigInteger id, @RequestBody ItemRestrictionRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(ItemRestrictionService.updateStoreItemRestriction(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Item Restriction", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveStoreItemRestriction(@RequestBody ItemRestrictionRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(ItemRestrictionService.saveStoreItemRestriction(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
}
