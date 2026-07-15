package com.param.procurement.purchase.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderStatusDto;
import com.param.procurement.purchase.service.purchaseOrder.IPurchaseOrderService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/purchase/order/creation")
public class PurchaseOrderController {

	final static Logger logger = Logger.getLogger(PurchaseOrderController.class);

	@Autowired
	IPurchaseOrderService purchaseOrdService;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getPurchaseOrderDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(purchaseOrdService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getPurchaseOrderDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Purchase Order by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPurchaseOrderList(@RequestBody PurchaseOrderMasterDto purchaseOrdDto)
			throws Exception {
		try {
			return new ResponseEntity<Object>(purchaseOrdService.getPuchaseOrderList(purchaseOrdDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPurchaseOrderList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Purchase Order", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> savePurchaseOrder(@RequestBody PurchaseOrderMasterDto purchaseOrderDto) throws Exception {
		try {
			return new ResponseEntity<Object>(purchaseOrdService.savePurchaseOrder(purchaseOrderDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("savePurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Purchase Order", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updatePurchaseOrder(@PathVariable Integer id, @RequestBody PurchaseOrderMasterDto purchaseOrderDto)
			throws Exception {
		try {
			purchaseOrderDto.setId(id);
			    return new ResponseEntity<Object>(purchaseOrdService.updatePurchaseOrder(purchaseOrderDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updatePurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Purchase Order details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getPurchaseOrder(@PathVariable Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId)
			throws Exception {
		try {
			     return new ResponseEntity<Object>(purchaseOrdService.getPucrhaseOrder(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve purchase Order", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approvePurchaseOrder(@RequestBody PurchaseOrderStatusDto purchaseOrderStatusDto)
			throws Exception {
		try {
			    return new ResponseEntity<Object>(purchaseOrdService.approvePurchaseOrder(purchaseOrderStatusDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approvePurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel purchase Order", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelPurchaseOrder(@PathVariable Integer id, @RequestBody PurchaseOrderMasterDto purchaseOrderDto)
			throws Exception {
		try {
			purchaseOrderDto.setId(id);
			return new ResponseEntity<Object>(purchaseOrdService.cancelPurchaseOrder(purchaseOrderDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelPurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/{itemid}/{vendorid}/last/{poType}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Purchase Order details", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getLastPurchaseOrder(@PathVariable Integer itemid ,@PathVariable Integer vendorid,@PathVariable Integer poType )
			throws Exception {
		try {
			     return new ResponseEntity<Object>(purchaseOrdService.getLastPurchaseOrder(itemid , vendorid,poType), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getLastPurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/forcefulyclosed/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel purchase Order", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> forcefulyClosed(@PathVariable Integer id, @RequestBody PurchaseOrderMasterDto purchaseOrderDto)
			throws Exception {
		try {
			purchaseOrderDto.setId(id);
			return new ResponseEntity<Object>(purchaseOrdService.forcefulyClosed(purchaseOrderDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("forcefulyClosed failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	/*@RequestMapping(value = "/{search}", method = RequestMethod.GET, consumes = "application/json")
	@ApiOperation(value = "Get list of Vendor of  Purchase Order ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPurchaseOrderVendorAutoPopulateList(@PathVariable("search") String search)
			throws Exception {
		try {
			return new ResponseEntity<Object>(purchaseOrdService.getVendorList(search), HttpStatus.OK);
		} catch (Exception ex) {
			return new ResponseEntity<Object>(
					new ErrorResponse("HIS-1092", "Error while fetching the Vendor of Purchase Order list", ex),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}*/
	
	/*
	@RequestMapping(value = "/{search}", method = RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Get list of Purchase Order", notes = "Get list of  Purchase Order", response = VendorResponseDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<List<VendorResponseDto>> getPurchaseOrderVendorAutoPopulateList(@PathVariable("search") String search) {
       return new ResponseEntity<List<VendorResponseDto>>(purchaseOrdService.retrieveVendorAutoPopulate(search), HttpStatus.OK);

	}
	
	@RequestMapping(value = " ", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Add New Purchase Order", response = PurchaseOrderMasterDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Local Purchase Order Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Local Purchase Order"),
			@ApiResponse(code = 403, message = "Create Local Purchase Order is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public Response savePurchaseOrder(@RequestBody PurchaseOrderMasterDto PurchaseOrderDto) throws Exception {
		return purchaseOrdService.saveMultipleLocPurchaseOrder(PurchaseOrderDto);
	}
	
	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "cancel  purchase Order", response = LocPurchaseRequestDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = " Purchase Order Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to cancel  Purchase Order"),
			@ApiResponse(code = 403, message = "Create  Purchase Order is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public Response updateCancelStatus(@PathVariable("id") Integer id, @RequestBody PurchaseOrderMasterDto PurchaseOrderDto) {
		PurchaseOrderDto.setId(id);
		return purchaseOrdService.updateCancelReason(PurchaseOrderDto);
		
	}
	
	@RequestMapping(value = "/status", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update Local Purchase request", response = LocPurchaseRequestDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Local Purchase request Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to Local Purchase request"),
			@ApiResponse(code = 403, message = "Create Local Purchase request is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public Response updateMultipleOpeningBalanceByOPBId(
			@RequestBody PurchaseOrderStatusDto purchaseOrderStatusDto) {
		return purchaseOrdService.updateApproveStatus(purchaseOrderStatusDto);
		
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Update  Purchase Order Master", response = PurchaseOrderMasterDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = " Purchase Order Created successfully"),
			@ApiResponse(code = 401, message = "You are Not authorized to  Purchase Order"),
			@ApiResponse(code = 403, message = "Create  Purchase Order is forbidden"),
			@ApiResponse(code = 404, message = "Resource Not found"),
			@ApiResponse(code = 405, message = "Method Not Allowed") })
	public Response updateMultiplePurchaseOrder(@PathVariable("id") Integer id,@RequestBody PurchaseOrderMasterDto PurchaseOrderDto) {
		PurchaseOrderDto.setId(id);
		return purchaseOrdService.updatePurchaseOrder(PurchaseOrderDto);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Details of PurchaseOrder", notes = "Get Details of PurchaseOrder", response = PurchaseRequestDropDownDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<PurchaseOrderMasterDetailsDto> getPurchaseOrderDetails(@PathVariable("id") Integer Id) {
		return new ResponseEntity<PurchaseOrderMasterDetailsDto>(purchaseOrdService.retrievePuchaseOrderDetails(Id), HttpStatus.OK);
 }
*/


}
