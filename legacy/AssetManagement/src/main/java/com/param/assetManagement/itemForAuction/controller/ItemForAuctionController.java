package com.param.assetManagement.itemForAuction.controller;

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

import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceDropdown;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.assetManagement.itemForAuction.dto.ItemAuctionResponse;
import com.param.assetManagement.itemForAuction.dto.ItemForAuctionRequest;
import com.param.assetManagement.itemForAuction.service.IItemForAuctionService;
import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/asset/item/for/auction")
public class ItemForAuctionController {

	private static final Logger logger = Logger.getLogger(ItemForAuctionController.class);

	@Autowired
	IItemForAuctionService itemForAuctionService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = FixedAssetOpeningBalanceDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try
		{
			return new ResponseEntity<Object>(itemForAuctionService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("Error while getting dropdowns by screen id", ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of item for auction by search", response = ItemAuctionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getItemAuctionList(@RequestBody FixedAssetOpeningBalanceRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(itemForAuctionService.getItemAuctionList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the item for auction list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get item for auction by id", response = ItemAuctionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getItemAuctionById(@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(itemForAuctionService.getItemAuctionById(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the item for auction", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.PATCH)
	@ApiOperation(value = "Save item for auction", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveItemAuction(@RequestBody ItemForAuctionRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(itemForAuctionService.saveItemAuction(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the item for auction", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve item for auction", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveItemAuction(@RequestBody ItemForAuctionRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(itemForAuctionService.approveItemAuction(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the item for auction", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
}