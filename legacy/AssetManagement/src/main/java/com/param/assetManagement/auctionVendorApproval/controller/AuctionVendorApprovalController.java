package com.param.assetManagement.auctionVendorApproval.controller;

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

import com.param.assetManagement.auctionVendorApproval.service.IAuctionVendorApprovalService;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceDropdown;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceRequest;
import com.param.assetManagement.itemForAuction.dto.ItemAuctionResponse;
import com.param.assetManagement.itemForAuction.dto.ItemForAuctionRequest;
import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/asset/auction/vendor/approval")
public class AuctionVendorApprovalController {

	private static final Logger logger = Logger.getLogger(AuctionVendorApprovalController.class);

	@Autowired
	IAuctionVendorApprovalService auctionVendorApprovalService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = FixedAssetOpeningBalanceDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try
		{
			return new ResponseEntity<Object>(auctionVendorApprovalService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("Error while getting dropdowns by screen id", ex);
			throw new ServiceException(ex.getMessage());
		}
		
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of auction vendor approval by search", response = ItemAuctionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAuctionVendorApprovalList(@RequestBody FixedAssetOpeningBalanceRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(auctionVendorApprovalService.getAuctionVendorApprovalList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the auction vendor approval list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get auction vendor approval by id", response = ItemAuctionResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getAuctionVendorApprovalById(@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(auctionVendorApprovalService.getAuctionVendorApprovalById(id), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the auction vendor approval", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Save auction vendor approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveAuctionVendorApproval(@PathVariable Integer id, @RequestBody ItemForAuctionRequest request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(auctionVendorApprovalService.saveAuctionVendorApproval(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the auction vendor approval", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
}