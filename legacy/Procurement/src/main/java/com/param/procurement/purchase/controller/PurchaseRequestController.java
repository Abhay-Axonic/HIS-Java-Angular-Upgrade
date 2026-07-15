package com.param.procurement.purchase.controller;

import java.util.List;

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
import com.param.procurement.common.exception.CustomException;
import com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequest;
import com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequestDetailResponse;
import com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequestDropdownResponse;
import com.param.procurement.purchase.dto.purchaseRequest.PurchaseRequestSearchResponse;
import com.param.procurement.purchase.service.purchaseRequest.IPurchaseRequestService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/purchase/requests")
public class PurchaseRequestController {

	final static Logger logger = Logger.getLogger(PurchaseRequestController.class);

	@Autowired
	IPurchaseRequestService PurchaseRequestService;

	@RequestMapping(value = "/dropdowns/{unitId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values", response = PurchaseRequestDropdownResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(PurchaseRequestService.getDropdowns(unitId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getDropdownList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ApiOperation(value = "Get list of purchase requests search ", response = PurchaseRequestSearchResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> purchaseRequestSearch(@RequestBody PurchaseRequest search) {
		try {
			List<PurchaseRequestSearchResponse> purchaseRequestSearchResponse = PurchaseRequestService.purchaseRequestSearch(search);
			return new ResponseEntity<Object>(purchaseRequestSearchResponse, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("purchaseRequestSearch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id} ", method = RequestMethod.GET)
	@ApiOperation(value = "Get purchase request details for particular id ", response = PurchaseRequestDetailResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> purchaseRequestSearch(@PathVariable("id") Long purchaseRequestId,@PathVariable Integer unitId,@PathVariable Integer orgId) {
		try {
			PurchaseRequestDetailResponse purchaseRequestSearchResponse = PurchaseRequestService.purchaseRequestDetails(purchaseRequestId,unitId,orgId);
			return new ResponseEntity<Object>(purchaseRequestSearchResponse, HttpStatus.OK);
		} catch(Exception e) {
			logger.error("purchaseRequestSearch failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save store indent", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> savePurchaseRequest(@RequestBody PurchaseRequest request)throws Exception {
		try {
			//if(request.ValidateSave()) {
			return new ResponseEntity<Object>(PurchaseRequestService.savePurchaseRequest(request), HttpStatus.OK);
			//}
			/*else {
				return new ResponseEntity<Object>(new ErrorResponse("HIS-1044", request.getvalidationMessage(), new CustomException(request.getvalidationMessage())),
						HttpStatus.BAD_REQUEST);
				
			}*/
		} 
		catch(CustomException cutom) {
			logger.error("savePurchaseRequest failed", cutom);
			throw new ServiceException(cutom.getMessage());
		}
		catch (IllegalArgumentException valid) {
			logger.error("savePurchaseRequest failed", valid);
			throw new ServiceException(valid.getMessage());
		}

		catch (DataIntegrityViolationException div) {
			logger.error("savePurchaseRequest failed", div);
			throw new ServiceException(div.getMessage());
		}
		catch (Exception ex) {
			logger.error("savePurchaseRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update purchase request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updatePurchaseRequest(@PathVariable Integer id, @RequestBody PurchaseRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(PurchaseRequestService.updatePurchaseRequest(request), HttpStatus.OK);
		}catch (DataIntegrityViolationException div) {
			logger.error("updatePurchaseRequest failed", div);
			throw new ServiceException(div.getMessage());
		}catch (Exception ex) {
			logger.error("updatePurchaseRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve purchase requests", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approvePurchaseRequest(@RequestBody PurchaseRequest request)
			throws Exception {
		try {
			return new ResponseEntity<Object>(PurchaseRequestService.approvePurchaseRequest(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approvePurchaseRequest failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel purchase request", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelStoreIndent(@PathVariable Integer id, @RequestBody PurchaseRequest request)
			throws Exception {
		try {
			request.setId(id);
			return new ResponseEntity<Object>(PurchaseRequestService.cancelPurchaseRequest(request), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelStoreIndent failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/*@RequestMapping(value = "/purchase", method = RequestMethod.GET)
	public ResponseEntity<List<com.param.procurement.purchase.model.PurchaseRequest>> retrivePurchaseData(){
		return new ResponseEntity<>(PurchaseRequestService.retrivePurchaseRequestData(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/purchase", method = RequestMethod.POST)
	public ResponseEntity<Boolean> createPurchaseRequest(@RequestBody PurchaseRequestDto purchaseRequestDto){
		return new ResponseEntity<>(PurchaseRequestService.createPurchaseRequest(purchaseRequestDto), HttpStatus.CREATED);
	}
	
	@RequestMapping(value ="/purchase/approve", method = RequestMethod.PATCH)
	public ResponseEntity<Boolean> purchaseRequestApproval(@RequestBody PurchaseRequestDto purchaseRequestDto){
		boolean result = PurchaseRequestService.purchaseRequestApproval(purchaseRequestDto);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}*/
	
	/*@RequestMapping(value = "/test", method = RequestMethod.GET)
	public PurchaseRequestDto test() {
		PurchaseRequestDto request =  new PurchaseRequestDto();
		PurchaseRequest purchaseRequest = new PurchaseRequest();
		ItemRequest itemRequest = new ItemRequest();
		ItemRequest itemRequest2 = new ItemRequest();
		request.setPurchaseRequest(purchaseRequest);
		List<ItemRequest> itemRequests = new ArrayList<>();
		itemRequests.add(itemRequest);
		itemRequests.add(itemRequest2);
		request.setItemRequests(itemRequests);
		return request;
	} */
	
}
