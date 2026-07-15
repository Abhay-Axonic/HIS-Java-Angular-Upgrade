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

import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionApprovalRequest;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionDropdown;
import com.param.assetManagement.auctionRequisition.dto.AuctionRequisitionRequest;
import com.param.assetManagement.auctionRequisition.service.IAuctionRequisiApprovalService;
import com.param.assetManagement.fixedAssetOpeningBalance.dto.FixedAssetOpeningBalanceResponse;
import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/asset/management/auction/requisition/approval")
public class AuctionRequisiApprovalController {

	private static final Logger logger = Logger.getLogger(AuctionRequisiApprovalController.class);

	@Autowired
	IAuctionRequisiApprovalService auctionRequisiApprovalService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get dropdown list", response = AuctionRequisitionDropdown.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) {
		try {
			return new ResponseEntity<Object>(auctionRequisiApprovalService.getDropdownList(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while getting dropdowns by screenId", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Auction Requisition Approval by search", response = FixedAssetOpeningBalanceResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getAuctionRequisitionApprovalList(
			@RequestBody AuctionRequisitionRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(auctionRequisiApprovalService.getAuctionRequisitionApprovalList(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the auction requisition list", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Auction Requisition Approval by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getAuctionRequisition(@PathVariable Integer id) throws Exception {
		try {
			return new ResponseEntity<Object>(auctionRequisiApprovalService.getAuctionRequisitionApproval(id),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the auction requisition approval", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/save", method = RequestMethod.PATCH)
	@ApiOperation(value = "Save Auction Requisition Approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveAuctionRequisitionApproval(@RequestBody AuctionRequisitionApprovalRequest request)
			throws Exception {
		try {

			return new ResponseEntity<Object>(auctionRequisiApprovalService.saveAuctionRequisitionApproval(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while saving the Auction Requisition Approval", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Auction Requisition Approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveAuctionRequisitionApproval(
			@RequestBody AuctionRequisitionApprovalRequest request) throws Exception {
		try {
			return new ResponseEntity<Object>(auctionRequisiApprovalService.approveAuctionRequisitionApproval(request),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("Error while fetching the Auction Requisition Approval", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

}
