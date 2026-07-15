package com.param.procurement.purchase.controller;

import org.apache.log4j.Logger;
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
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDropdown;
import com.param.procurement.purchase.dto.itemRequest.ItemRequestDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestSearchDto;
import com.param.procurement.purchase.service.itemCreationPending.IItemCreationPendingService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/purchase/item/creation/pending")
public class ItemCreationPendingController {

	final static Logger logger = Logger.getLogger(ItemCreationPendingController.class);

	@Autowired
	private IItemCreationPendingService itemCreationPending;
	
	@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ItemRequestDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList() throws Exception {
		try {
			return new ResponseEntity<Object>(itemCreationPending.getDropdowns(), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Item Creation Pending by search", response = LocPurchaseRequestSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getItemCreationPendingList(@RequestBody ItemRequestDto request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(itemCreationPending.getItemCreationList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getItemCreationPendingList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Item craetion Pending details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemCreationPending(@PathVariable Integer id)
			throws Exception {
		try {
			     return new ResponseEntity<Object>(itemCreationPending.getItemCreationPending(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getItemCreationPending failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Item Creation Pending", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelItemCraetionPending(@PathVariable Integer id, @RequestBody ItemRequestDto itemRequestDto)
			throws Exception {
		try {
			itemRequestDto.setId(id);
			return new ResponseEntity<Object>(itemCreationPending.cancelItemCreationPending(itemRequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelItemCraetionPending failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
}
