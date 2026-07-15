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
import com.param.procurement.purchase.service.itemRequestApproval.IItemRequestApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/purchase/item/request/approval")
public class ItemRequestApprovalController {

	final static Logger logger = Logger.getLogger(ItemRequestApprovalController.class);

	@Autowired
	private IItemRequestApprovalService itemRequestApprovalService;
	
	@RequestMapping(value = "/dropdowns", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = ItemRequestDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList() throws Exception {
		try {
			return new ResponseEntity<Object>(itemRequestApprovalService.getDropdowns(), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Item Request by search", response = LocPurchaseRequestSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getItemRequestList(@RequestBody ItemRequestDto request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(itemRequestApprovalService.getItemRequestList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getItemRequestList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Item Request details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemRequest(@PathVariable Integer id)
			throws Exception {
		try {
			     return new ResponseEntity<Object>(itemRequestApprovalService.getItemRequest(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getItemRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Item Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveItemRequestDetail(@PathVariable Integer id,@RequestBody ItemRequestDto itemRequestDto)
			throws Exception {
		try {
			    itemRequestDto.setId(id);
			    return new ResponseEntity<Object>(itemRequestApprovalService.approveItemDetailRequest(itemRequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveItemRequestDetail failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Item Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveItemRequest(@RequestBody ItemRequestDto itemRequestDto)
			throws Exception {
		try {
			    return new ResponseEntity<Object>(itemRequestApprovalService.approveItemRequest(itemRequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveItemRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Item request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelItemRequest(@PathVariable Integer id, @RequestBody ItemRequestDto itemRequestDto)
			throws Exception {
		try {
			itemRequestDto.setId(id);
			return new ResponseEntity<Object>(itemRequestApprovalService.cancelItemRequest(itemRequestDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelItemRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
