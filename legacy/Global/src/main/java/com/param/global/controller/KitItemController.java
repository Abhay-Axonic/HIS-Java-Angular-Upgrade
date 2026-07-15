package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.common.dto.ErrorResponse;

import com.param.global.common.ServiceException;
import com.param.global.dto.kit.Items.KitItemsDetailsListResponse;
import com.param.global.dto.kit.Items.KitItemsRequest;
import com.param.global.service.kitItem.IKitItemService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/global/items")
public class KitItemController {

	private static final Logger logger = Logger.getLogger(KitItemController.class);

	@Autowired
	IKitItemService kitItemsService;

	//As per UI Requerment  create Item Search Details
	
	@RequestMapping(value = "/kit/details", method = RequestMethod.POST)
	@ApiOperation(value = "Get Kit items details by itemIds", response = KitItemsDetailsListResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> kitItemsDetails(@RequestBody KitItemsRequest kitItemsRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(kitItemsService.kitItemsDetails(kitItemsRequest), HttpStatus.OK);
		}

		catch (Exception ex) {
			logger.error("Error while fetching the Kit items details", ex);
			return new ResponseEntity<Object>(
					new ErrorResponse("HIS-0104", "Error while fetching the Kit items details", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
