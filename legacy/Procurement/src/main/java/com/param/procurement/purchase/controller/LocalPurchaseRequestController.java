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
import com.param.global.service.items.CustomException;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocPurchaseRequestSearchDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocalPurchaseReqStatusDto;
import com.param.procurement.purchase.dto.localPurchaseRequest.LocalPurchaseRequestDropDownResponse;
import com.param.procurement.purchase.service.localPurchaseRequest.ILocalPurchaseRequestService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/local/purchase/requests")
public class LocalPurchaseRequestController {

	final static Logger logger = Logger.getLogger(LocalPurchaseRequestController.class);

	@Autowired
	private ILocalPurchaseRequestService locPurchaseReqService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = LocalPurchaseRequestDropDownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(locPurchaseReqService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
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
			return new ResponseEntity<Object>(locPurchaseReqService.getLocalPuchaseRequestList(locPurchaseReqDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLocalPurchaseRequestList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Local Purchase Request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> saveLocalPurchaseRequest(@RequestBody LocPurchaseRequestDto locPurchaseReqDto) throws Exception {
		try {
			if(locPurchaseReqDto.ValidateSave()) {
				return new ResponseEntity<Object>(locPurchaseReqService.saveLocalPurchaseRequest(locPurchaseReqDto), HttpStatus.OK);
			}
			else {
				return new ResponseEntity<Object>(new ErrorResponse("HIS-1044", locPurchaseReqDto.getValidationMessage(), new CustomException(locPurchaseReqDto.getValidationMessage())),
						HttpStatus.BAD_REQUEST);
				
			}
		}
		catch (DataIntegrityViolationException div) {
			logger.error("saveLocalPurchaseRequest failed", div);
			throw new ServiceException(div.getMessage());
		}catch (Exception ex) {
			logger.error("saveLocalPurchaseRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Local Purchase request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updateLocalPurchaseRequest(@PathVariable Long id, @RequestBody LocPurchaseRequestDto locPurchaseReqDto)
			throws Exception {
		try {
			    locPurchaseReqDto.setId(id);
			    if(locPurchaseReqDto.ValidateUpdate()) {
			    	return new ResponseEntity<Object>(locPurchaseReqService.updateLocalPurchaseReq(locPurchaseReqDto), HttpStatus.OK);
			    }
			    else {
					return new ResponseEntity<Object>(new ErrorResponse("HIS-1044", locPurchaseReqDto.getValidationMessage(), new CustomException(locPurchaseReqDto.getValidationMessage())),
							HttpStatus.BAD_REQUEST);
					
				}
		}
		catch (DataIntegrityViolationException div) {
			logger.error("updateLocalPurchaseRequest failed", div);
			throw new ServiceException(div.getMessage());
		}catch (Exception ex) {
			logger.error("updateLocalPurchaseRequest failed", ex);
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
			     return new ResponseEntity<Object>(locPurchaseReqService.getLocalPurchaseRequest(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLocalPurchaseRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve local purchase request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approveLocalPurchaseRequest(@RequestBody LocalPurchaseReqStatusDto locPurchaseReqStatusDto)
			throws Exception {
		try {
			    return new ResponseEntity<Object>(locPurchaseReqService.approvePurchaseRequest(locPurchaseReqStatusDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveLocalPurchaseRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel local purchase request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelLocalPurchaseRequest(@PathVariable Long id, @RequestBody LocPurchaseRequestDto locPurchaseReqDto)
			throws Exception {
		try {
			locPurchaseReqDto.setId(id);
			return new ResponseEntity<Object>(locPurchaseReqService.cancelLocalPurchaseRequest(locPurchaseReqDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelLocalPurchaseRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	

	

}
