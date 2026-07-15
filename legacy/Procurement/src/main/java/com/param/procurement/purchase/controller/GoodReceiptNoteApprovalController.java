package com.param.procurement.purchase.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.param.entity.model.procurement.PhysicalInspection;
import com.param.global.common.ServiceException;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNRequest;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNStatusDto;
import com.param.procurement.purchase.service.goodReceiptNoteApproval.IGoodReceiptNoteApprovalService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping(value = "/api/procurements/quotation/grn/approval")
public class GoodReceiptNoteApprovalController {

	final static Logger logger = Logger.getLogger(GoodReceiptNoteApprovalController.class);

	@Autowired
	IGoodReceiptNoteApprovalService goodReceiptNoteApproval;
	
	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getGoodReceiptNoteApprovalDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId)throws Exception {
		try
		{
		    return new ResponseEntity<Object>(goodReceiptNoteApproval.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		}
		catch(Exception ex)
		{
			logger.error("getGoodReceiptNoteApprovalDropdown failed", ex);
			throw new ServiceException(ex.getMessage());
		}

	}
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Good Receipt Note by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getGoodReceiptNoteList(@RequestBody GRNRequest grnRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(goodReceiptNoteApproval.getGRNList(grnRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getGoodReceiptNoteList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/settlementsearch", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Good Receipt Note by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized @ResponseBody ResponseEntity<Object> getGoodReceiptNoteSettlementList(@RequestBody GRNRequest grnRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(goodReceiptNoteApproval.getGRNSettlementList(grnRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getGoodReceiptNoteSettlementList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/settlementdetails/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Good Receipt Note details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getGoodReceiptNoteSettlementDetails(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer id)throws Exception {
		try {
			     return new ResponseEntity<Object>(goodReceiptNoteApproval.getGoodReceiptNote(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getGoodReceiptNoteSettlementDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/settledsearch", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Good Receipt Note by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getGoodReceiptNoteSettledList(@RequestBody GRNRequest grnRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(goodReceiptNoteApproval.getGoodReceiptNoteSettledList(grnRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getGoodReceiptNoteSettledList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/settleddetails/{grnid}/{unitId}", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Good Receipt Note by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getGoodReceiptNoteSettledDetails(@PathVariable Integer grnid , @PathVariable Integer unitId)
			throws Exception {
		try {
			return new ResponseEntity<Object>(goodReceiptNoteApproval.getGoodReceiptNoteSettledDetails(grnid,unitId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getGoodReceiptNoteSettledDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Good Receipt Note details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getGoodReceiptNote(@PathVariable Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId)throws Exception {
		try {
			     return new ResponseEntity<Object>(goodReceiptNoteApproval.getGoodReceiptNote(id,unitId,orgId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getGoodReceiptNote failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	

	@RequestMapping(value = "/approve", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Good Receipt Note", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<Object> approveGoodReceiptNote(@RequestBody List<GRNStatusDto> grnStatusDtoList)
			throws Exception {
		try {
			
			List<Integer> grnIds = new ArrayList<Integer>();
			
			for (GRNStatusDto dto : grnStatusDtoList) {
				grnIds.add(dto.getGrnId());
			}
			
			BigInteger res1 = goodReceiptNoteApproval.alreadyGoodReceiptNoteExits(grnIds);
			
			if(res1.intValue() > 0 ){
				CommonResponse response = new CommonResponse();
				response.setMessage("Good receipt note already approved by other user ");
				return new ResponseEntity<Object>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			
			CommonResponse res = goodReceiptNoteApproval.approveGoodReceiptNote(grnStatusDtoList);
			HashMap<Integer, Long> map=(HashMap<Integer, Long>) res.getObject();
			boolean result=true;
			if(map!=null)
			{
			for (Map.Entry<Integer,Long> entry : map.entrySet()) //using map.entrySet() for iteration  
			{  
				 result=goodReceiptNoteApproval.updateBatchIdForBonusGrn(entry.getKey(),entry.getValue());
			}
		    }
			if(result)
			{
			if (res.getListObject()!=null) {
				List<PhysicalInspection> physicalInspectionList = res.getListObject();

				for (PhysicalInspection p : physicalInspectionList) {
					res = goodReceiptNoteApproval.saveAutoPhysicalInspection(p, p.getDeliveryStoreId());

				}
			}
		}

			return new ResponseEntity<Object>(res, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveGoodReceiptNote failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/approve/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Approve Good Receipt Note Approval", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<Object> approveGoodReceiptNoteApprovalDetail(@PathVariable Integer id,
			@RequestBody GRNRequest grnRequest) throws Exception {
		try {
			
			List<Integer> grnIds = new ArrayList<Integer>();
			grnIds.add(id);
			
			BigInteger res1 = goodReceiptNoteApproval.alreadyGoodReceiptNoteExits(grnIds);
			
			if(res1.intValue() > 0 ){
				CommonResponse response = new CommonResponse();
				response.setMessage("Good receipt note already approved by other user ");
				return new ResponseEntity<Object>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
			grnRequest.setId(id);
			CommonResponse ob = null;
			ob = goodReceiptNoteApproval.approveGoodReceiptNoteDetail(grnRequest);
			HashMap<Integer, Long> map=(HashMap<Integer, Long>) ob.getObject1();
			boolean result=true;
			if(map!=null)
			{
			for (Map.Entry<Integer,Long> entry : map.entrySet()) //using map.entrySet() for iteration  
			{  
				 result=goodReceiptNoteApproval.updateBatchIdForBonusGrn(entry.getKey(),entry.getValue());
			}
		    }
			if(result)
			{
			if (ob.getObject() != null) {
				PhysicalInspection phy = (PhysicalInspection) ob.getObject();
				ob = goodReceiptNoteApproval.saveAutoPhysicalInspection(phy, grnRequest.getDeliveryStoreId());
			}
		}

			// RestTemplate restTemplate = new RestTemplate();
			// ResponseEntity<Boolean> response =
			// restTemplate.postForEntity("http://localhost:8090/availability",
			// userModel.getUsername(), Boolean.class);

			return new ResponseEntity<Object>(ob, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveGoodReceiptNoteApprovalDetail failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	

}
