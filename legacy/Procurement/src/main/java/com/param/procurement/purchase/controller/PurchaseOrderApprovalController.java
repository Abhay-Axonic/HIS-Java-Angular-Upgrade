package com.param.procurement.purchase.controller;

import java.util.List;

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
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderDropDownDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderMasterDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderSearchDto;
import com.param.procurement.purchase.purchaseOrder.dto.PurchaseOrderStatusDto;
import com.param.procurement.purchase.service.purchaseOrderApproval.IPurchaseOrderApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/purchase/order/approval")
public class PurchaseOrderApprovalController {

	final static Logger logger = Logger.getLogger(PurchaseOrderApprovalController.class);

	@Autowired
	private IPurchaseOrderApprovalService purchaseOrderApprovalService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = PurchaseOrderDropDownDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getPurchaseOrderDropdown(@PathVariable Integer unitId, @PathVariable Integer orgId,
			@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(purchaseOrderApprovalService.getDropdowns(unitId, orgId, userId),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPurchaseOrderDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}

	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Purchase Order by search", response = PurchaseOrderSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPurchaseOrderList(@RequestBody PurchaseOrderMasterDto purchaseOrdDto)
			throws Exception {
		try {
			return new ResponseEntity<Object>(purchaseOrderApprovalService.getPuchaseOrderList(purchaseOrdDto),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPurchaseOrderList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/finalsearch", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Purchase Order by search", response = PurchaseOrderSearchDto.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getPurchaseOrderFinalList(
			@RequestBody PurchaseOrderMasterDto purchaseOrdDto) throws Exception {
		try {
			return new ResponseEntity<Object>(purchaseOrderApprovalService.getPurchaseOrderFinalList(purchaseOrdDto),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPurchaseOrderFinalList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Purchase Order details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getPurchaseOrder(@PathVariable Integer id, @PathVariable Integer unitId,
			@PathVariable Integer orgId) throws Exception {
		try {
			return new ResponseEntity<Object>(purchaseOrderApprovalService.getPucrhaseOrder(id, unitId, orgId),
					HttpStatus.OK);
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
	public ResponseEntity<Object> approvePurchaseOrder(
			@RequestBody List<PurchaseOrderStatusDto> purchaseOrderStatusDtoList) throws Exception {
		try {
			return new ResponseEntity<Object>(
					purchaseOrderApprovalService.approvePurchaseOrder(purchaseOrderStatusDtoList), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approvePurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve purchase Order quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> approvePurchaseOrderDetail(@PathVariable Integer id,
			@RequestBody PurchaseOrderMasterDto purchaseOrdDto) throws Exception {
		try {
			purchaseOrdDto.setId(id);
			return new ResponseEntity<Object>(purchaseOrderApprovalService.approvePurchaseOrderDetail(purchaseOrdDto),
					HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approvePurchaseOrderDetail failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "secondlevel/approval/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve purchase Order quantity", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> secondLevelApprovePurchaseOrderDetail(@PathVariable Integer id,
			@RequestBody PurchaseOrderMasterDto purchaseOrdDto) throws Exception {
		try {
			purchaseOrdDto.setId(id);
			return new ResponseEntity<Object>(
					purchaseOrderApprovalService.secondLevelApprovePurchaseOrderDetail(purchaseOrdDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("secondLevelApprovePurchaseOrderDetail failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	/*
	 * @RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	 * 
	 * @ApiOperation(value = "Approve local purchase request", response =
	 * CommonResponse.class)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
	 * 
	 * @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
	 * 403, message = "forbidden!!!"),
	 * 
	 * @ApiResponse(code = 404, message = "not found!!!") }) public
	 * ResponseEntity<Object> approveLocalPurchaseRequest(@PathVariable Integer
	 * id,@RequestBody LocPurchaseRequestDto locPurchaseReqDto) throws Exception {
	 * try { locPurchaseReqDto.setId(id); return new
	 * ResponseEntity<Object>(locPurchaseReqApprovalService.approvePurchaseRequest(
	 * locPurchaseReqDto), HttpStatus.OK); } catch (Exception ex) { return new
	 * ResponseEntity<Object>(new ErrorResponse("HIS-1084",
	 * "Error while fetching the local purchase request", ex),
	 * HttpStatus.INTERNAL_SERVER_ERROR); } }
	 * 
	 * @RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	 * 
	 * @ApiOperation(value = "Cancel local purchase request", response =
	 * CommonResponse.class)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
	 * 
	 * @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
	 * 403, message = "forbidden!!!"),
	 * 
	 * @ApiResponse(code = 404, message = "not found!!!") }) public
	 * ResponseEntity<Object> cancelStoreIndent(@PathVariable BigInteger
	 * id, @RequestBody LocPurchaseRequestDto locPurchaseReqDto) throws Exception {
	 * try { locPurchaseReqDto.setId(id);; return new
	 * ResponseEntity<Object>(locPurchaseReqApprovalService.
	 * cancelLocalPurchaseRequest(locPurchaseReqDto), HttpStatus.OK); } catch
	 * (Exception ex) { return new ResponseEntity<Object>(new
	 * ErrorResponse("HIS-1085", "Error while fetching the Local Purchase Request",
	 * ex), HttpStatus.INTERNAL_SERVER_ERROR); } }
	 */
	/*
	 * @Autowired IPurchaseOrderService purchaseOrdService;
	 */

	/*
	 * @RequestMapping(value = "/dropdowns/{screenId}", method = RequestMethod.GET)
	 * 
	 * @ApiOperation(value = "Get list of DropDowns of PurchaseOrder", notes =
	 * "Get list of DropDowns of PurchaseOrder", response =
	 * PurchaseOrderDropDownDto.class)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
	 * 
	 * @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
	 * 403, message = "forbidden!!!"),
	 * 
	 * @ApiResponse(code = 404, message = "not found!!!") }) public
	 * ResponseEntity<PurchaseOrderDropDownDto>
	 * getPurchaseOrderDropdown(@PathVariable("screenId") Integer screenId) { return
	 * new ResponseEntity<PurchaseOrderDropDownDto>(purchaseOrdService.
	 * getPurchaseOrderDropDown(screenId), HttpStatus.OK);
	 * 
	 * }
	 * 
	 * @RequestMapping(value = "/search", method = RequestMethod.POST, consumes =
	 * MediaType.APPLICATION_JSON_VALUE, produces =
	 * MediaType.APPLICATION_JSON_VALUE)
	 * 
	 * @ApiOperation(value = "Get list of Purchase Order", notes =
	 * "Get list of  Purchase Order", response = PurchaseOrderSearchDto.class)
	 * 
	 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
	 * 
	 * @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
	 * 403, message = "forbidden!!!"),
	 * 
	 * @ApiResponse(code = 404, message = "not found!!!") }) public
	 * ResponseEntity<List<PurchaseOrderSearchDto>>
	 * getPurchaseOrderListBySearchInfo(@RequestBody PurchaseOrderMasterDto
	 * purchaseOrdDto) { return new
	 * ResponseEntity<List<PurchaseOrderSearchDto>>(purchaseOrdService.
	 * retrievePuchaseOrderSearchList(purchaseOrdDto), HttpStatus.OK);
	 * 
	 * }
	 *//**
		 * 
		 * 
		 * @param purchaseOrderStatusDto
		 * @return
		 *//*
			 * 
			 * @RequestMapping(value = "", method = RequestMethod.PATCH, consumes =
			 * MediaType.APPLICATION_JSON_VALUE, produces =
			 * MediaType.APPLICATION_JSON_VALUE)
			 * 
			 * @ApiOperation(value = "Purchase Order Approval Status update", response =
			 * LocPurchaseRequestDto.class)
			 * 
			 * @ApiResponses(value = { @ApiResponse(code = 200, message =
			 * " Purchase Order Approval updated successfully"),
			 * 
			 * @ApiResponse(code = 401, message =
			 * "You are Not authorized to  Purchase Order Approval"),
			 * 
			 * @ApiResponse(code = 403, message =
			 * "Create Purchase Order Approval is forbidden"),
			 * 
			 * @ApiResponse(code = 404, message = "Resource Not found"),
			 * 
			 * @ApiResponse(code = 405, message = "Method Not Allowed") }) public Response
			 * updateMultipleStatusOfPurchaseOrderApproval(@RequestBody
			 * PurchaseOrderStatusDto purchaseOrderStatusDto) { //status set to
			 * approvalStatus and changes the details staus return
			 * purchaseOrdService.updateApprovalStatus(purchaseOrderStatusDto); }
			 * 
			 * 
			 * @RequestMapping(value = "/approved", method = RequestMethod.PATCH, consumes =
			 * MediaType.APPLICATION_JSON_VALUE, produces =
			 * MediaType.APPLICATION_JSON_VALUE)
			 * 
			 * @ApiOperation(value = "Purchase Order Approval Status update", response =
			 * LocPurchaseRequestDto.class)
			 * 
			 * @ApiResponses(value = { @ApiResponse(code = 200, message =
			 * " Purchase Order Approval updated successfully"),
			 * 
			 * @ApiResponse(code = 401, message =
			 * "You are Not authorized to  Purchase Order Approval"),
			 * 
			 * @ApiResponse(code = 403, message =
			 * "Create Purchase Order Approval is forbidden"),
			 * 
			 * @ApiResponse(code = 404, message = "Resource Not found"),
			 * 
			 * @ApiResponse(code = 405, message = "Method Not Allowed") }) public Response
			 * updateMultipleStatusOfPurchaseOrderApproval(@RequestBody
			 * List<PurchaseOrderDetailDto> purchaseOrderDetailsDtoList) { //status set to
			 * approvalStatus and changes the details staus return
			 * purchaseOrdService.updatePurchaseOrderDetailsApproval(
			 * purchaseOrderDetailsDtoList); }
			 * 
			 * @RequestMapping(value = "/{id}", method = RequestMethod.GET)
			 * 
			 * @ApiOperation(value = "Get Details of PurchaseOrder", notes =
			 * "Get Details of PurchaseOrder", response = PurchaseRequestDropDownDto.class)
			 * 
			 * @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			 * 
			 * @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code =
			 * 403, message = "forbidden!!!"),
			 * 
			 * @ApiResponse(code = 404, message = "not found!!!") }) public
			 * ResponseEntity<PurchaseOrderMasterDetailsDto>
			 * getPurchaseOrderDetails(@PathVariable("id") Integer Id) { return new
			 * ResponseEntity<PurchaseOrderMasterDetailsDto>(purchaseOrdService.
			 * retrievePuchaseOrderDetails(Id), HttpStatus.OK); }
			 */

}
