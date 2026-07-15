package com.param.procurement.purchase.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestSearchDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocalPurchaseRequestDropDownResponse;
import com.param.procurement.purchase.service.localPurchaseRequestApproval.ILocalPurchaseRequestApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/local/purchase/requests/approval")
public class LocalPurchaseReqApprovalController {

	final static Logger logger = Logger.getLogger(LocalPurchaseReqApprovalController.class);

	@Autowired
	private ILocalPurchaseRequestApprovalService locPurchaseReqApprovalService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = LocalPurchaseRequestDropDownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(locPurchaseReqApprovalService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of Local Purchase Request by search", response = LocPurchaseRequestSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getLocalPurchaseRequestList(@RequestBody LocPurchaseRequestDto locPurchaseReqDto)
			throws Exception {
		try {
			return new ResponseEntity<Object>(locPurchaseReqApprovalService.getLocalPuchaseRequestList(locPurchaseReqDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLocalPurchaseRequestList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get local purchase request details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getLocalPurchaseRequest(@PathVariable Long id,@PathVariable Integer unitId,@PathVariable Integer orgId)
			throws Exception {
		try {
			return new ResponseEntity<Object>(locPurchaseReqApprovalService.getLocalPurchaseRequest(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLocalPurchaseRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve local purchase request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveLocalPurchaseRequest(@PathVariable Long id,@RequestBody LocPurchaseRequestDto locPurchaseReqDto)
			throws Exception {
		try {
			locPurchaseReqDto.setId(id);
			return new ResponseEntity<Object>(locPurchaseReqApprovalService.approvePurchaseRequest(locPurchaseReqDto), HttpStatus.OK);
		}catch (DataIntegrityViolationException div) {
			logger.error("approveLocalPurchaseRequest failed", div);
			throw new ServiceException(div.getMessage());
		}
		catch (Exception ex) {
			logger.error("approveLocalPurchaseRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel local purchase request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelStoreIndent(@PathVariable Long id, @RequestBody LocPurchaseRequestDto locPurchaseReqDto)
			throws Exception {
		try {
			locPurchaseReqDto.setId(id);;
			return new ResponseEntity<Object>(locPurchaseReqApprovalService.cancelLocalPurchaseRequest(locPurchaseReqDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelStoreIndent failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	
	
	/*@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update Local Purchase request Approval", response = LocPurchaseRequestDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Local Purchase request Approval Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Local Purchase request Approval"),
			@ApiResponse(code = 403, message = "Create Local Purchase request Approval is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public Response updateMultipleLocalPurchaseReq(@PathVariable("id") Long id,@RequestBody LocPurchaseRequestDto locPurchaseReqDto) {
		locPurchaseReqDto.setId(id);
		return locPurchaseReqService.updateLocalPurchaseReqApproval(locPurchaseReqDto);
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update cancel Local Purchase request Approval", response = LocPurchaseRequestDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Local Purchase request Approval Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Local Purchase request Approval"),
			@ApiResponse(code = 403, message = "Create Local Purchase request Approval is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public Response updateMultipleOpeningBalanceByOPBId(@PathVariable("id") Long id,@RequestBody LocPurchaseRequestDto locPurchaseReqDto) {
		locPurchaseReqDto.setId(id);
		return locPurchaseReqService.updateCancelReason(locPurchaseReqDto);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Details of LocalPurchaserequest", notes = "Get Details of LocalPurchaserequest", response = PurchaseRequestDropDownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<LocPurchaseApprovalMasterResponseDto> getLocalPurchaseRequestDetails(@PathVariable("id") Integer Id) {
		return new ResponseEntity<LocPurchaseApprovalMasterResponseDto>(locPurchaseReqService.getLocalPurchaseReqApprovalDetails(Id), HttpStatus.OK);

	}
*/
}
