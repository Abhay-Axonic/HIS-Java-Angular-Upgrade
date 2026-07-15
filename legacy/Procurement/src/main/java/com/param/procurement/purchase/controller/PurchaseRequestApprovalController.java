package com.param.procurement.purchase.controller;

import java.util.List;

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

import org.apache.log4j.Logger;

import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.global.common.ServiceException;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApproval;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApprovalDetail;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApprovalDropDown;
import com.param.procurement.purchase.dto.purchaseRequestApproval.PurchaseRequestApprovalResponse;
import com.param.procurement.purchase.service.purchaseRequestApproval.IPurchaseRequestApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/purchase/requests/approval")
public class PurchaseRequestApprovalController {

    final static Logger logger = Logger.getLogger(PurchaseRequestApprovalController.class);

    @Autowired
    IPurchaseRequestApprovalService PurchaseRequestApprovalService;
    IPurchaseRequestApprovalService PurchaseRequestApprovalServisearce;

    @RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
    @ApiOperation(value = "Get list for populating dropdown values", response = PurchaseRequestApprovalDropDown.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    public @ResponseBody
    ResponseEntity<Object> getDropdownList(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
        try {
            return new ResponseEntity<Object>(PurchaseRequestApprovalService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("getDropdownList failed", ex);
            throw new ServiceException(ex.getMessage());
        }
    }


    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ApiOperation(value = "Get list of purchase requests search ", response = PurchaseRequestApprovalResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    public @ResponseBody ResponseEntity<Object> purchaseRequestApprovalSearch(@RequestBody PurchaseRequestApproval search) {
        try {
            List<PurchaseRequestApprovalResponse> purchaseRequestSearchResponse = PurchaseRequestApprovalService.purchaseRequestApprovalSearch(search);
            return new ResponseEntity<Object>(purchaseRequestSearchResponse, HttpStatus.OK);
        } catch(Exception e) {
            logger.error("purchaseRequestApprovalSearch failed", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "Get purchase request approval details for particular id ", response = PurchaseRequestApprovalDetail.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    public @ResponseBody ResponseEntity<Object> purchaseRequestApprovalDetails(@PathVariable("id") Long purchaseRequestId,@PathVariable Integer unitId,@PathVariable Integer orgId) {
        try {
            PurchaseRequestApprovalDetail purchaseRequestSearchResponse = PurchaseRequestApprovalService.purchaseRequestDetails(purchaseRequestId,unitId,orgId);
            return new ResponseEntity<Object>(purchaseRequestSearchResponse, HttpStatus.OK);
        } catch(Exception e) {
            logger.error("purchaseRequestApprovalDetails failed", e);
            throw new ServiceException(e.getMessage());
        }
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Update purchase request approval", response = CommonResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    public ResponseEntity<Object> updatePurchaseRequestApproval(@PathVariable Integer id, @RequestBody PurchaseRequestApproval request)
            throws Exception {
        try {
            request.setId(id);
            return new ResponseEntity<Object>(PurchaseRequestApprovalService.updatePurchaseRequestApproval(request), HttpStatus.OK);
        }
        catch (DataIntegrityViolationException div) {
			logger.error("updatePurchaseRequestApproval failed", div);
			throw new ServiceException(div.getMessage());
		}catch (Exception ex) {
            logger.error("updatePurchaseRequestApproval failed", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
    @ApiOperation(value = "Cancel purchase request", response = CommonResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    public ResponseEntity<Object> cancelStoreIndent(@PathVariable Integer id, @RequestBody PurchaseRequestApproval request)
            throws Exception {
        try {
            request.setId(id);
            return new ResponseEntity<Object>(PurchaseRequestApprovalService.cancelPurchaseRequestApproval(request), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("cancelStoreIndent failed", ex);
            throw new ServiceException(ex.getMessage());
        }
    }

    @RequestMapping(value = "/approve", method = RequestMethod.PATCH)
    @ApiOperation(value = "Approve purchase request approvals", response = CommonResponse.class)
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
            @ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    public ResponseEntity<Object> approvePurchaseRequestApproval(@RequestBody PurchaseRequestApproval request)
            throws Exception {
        try {
            return new ResponseEntity<Object>(PurchaseRequestApprovalService.approvePurchaseRequestApproval(request), HttpStatus.OK);
        } catch (Exception ex) {
            logger.error("approvePurchaseRequestApproval failed", ex);
            throw new ServiceException(ex.getMessage());
        }
    }
}
