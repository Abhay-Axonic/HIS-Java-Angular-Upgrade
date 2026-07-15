package com.param.assetManagement.auctionRequisition.controller;

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

import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionDropdown;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionListResponse;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionRequest;
import com.param.assetManagement.auctionRequisition.service.IAuctionRequisitionService;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceResponse;
import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/asset/management/auction/requisition")
public class AuctionRequisitionController {

	private static final Logger logger = Logger.getLogger(AuctionRequisitionController.class);

	@Autowired
	IAuctionRequisitionService auctionRequisitionService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = AuctionRequisitionDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer userId) {
		try
		{
			return new ResponseEntity<Object>(auctionRequisitionService.getDropdownList(unitId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("Error while getting dropdowns by screenId", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of auction requisition by search", response = FixedAssetOpeningBalanceResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAuctionRequisitionList(@RequestBody AuctionRequisitionRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(auctionRequisitionService.getAuctionRequisitionList(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the auction requisition list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{orgId}/{userId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get auction requisition details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getAuctionRequisition(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId,@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(auctionRequisitionService.getAuctionRequisition(id,unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the auction requisition", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ApiOperation(value = "Save Auction Requisition", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveAuctionRequisition(@RequestBody AuctionRequisitionListResponse request) throws Exception {
		try {
			return new ResponseEntity<Object>(auctionRequisitionService.saveAuctionRequisition(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the Auction Requisition", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Auction Requisition", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveAuctionRequisition(@RequestBody AuctionRequisitionListResponse request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(auctionRequisitionService.approveAuctionRequisition(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while approving the Auction Requisition", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Auction Requisition", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelAuctionRequisition(@PathVariable Integer id, @RequestBody AuctionRequisitionListResponse request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(auctionRequisitionService.cancelAuctionRequisition(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while cancelling the Auction Requisition", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "update Auction Requisition", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateAuctionRequisition(@PathVariable Integer id,
			@RequestBody AuctionRequisitionListResponse request) throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(auctionRequisitionService.updateAuctionRequisition(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while updating the Auction Requisition", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
