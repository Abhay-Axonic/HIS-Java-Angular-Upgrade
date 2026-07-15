package com.param.procurement.purchase.controller;

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

import com.param.common.constants.Constants;
import com.param.common.dto.CommonResponse;
import com.param.common.dto.ErrorResponse;
import com.param.entity.model.procurement.GoodReceiptNote;
import com.param.entity.model.procurement.GoodReceiptNoteDetail;
import com.param.entity.model.procurement.GrnBonusBatchExpiry;
import com.param.global.common.ServiceException;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNDetailsRequest;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNRequest;
import com.param.procurement.purchase.dto.goodReceiptNote.GRNStatusDto;
import com.param.procurement.purchase.dto.goodReceiptNote.GrnSettledRequest;
import com.param.procurement.purchase.service.goodReceiptNote.IGoodReceiptNoteService;
import com.param.utility.TimeZoneComponent;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/procurements/quotation/grn")
public class GoodReceiptNoteController {

	final static Logger logger = Logger.getLogger(GoodReceiptNoteController.class);

	@Autowired
	IGoodReceiptNoteService grnService;

	@RequestMapping(value = "/dropdowns/{unitId}/{orgId}/{userId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get list for populating dropdown values ", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getPurchaseOrderDropdown(@PathVariable Integer unitId,@PathVariable Integer orgId,@PathVariable Integer userId) throws Exception {
		try {
			return new ResponseEntity<Object>(grnService.getDropdowns(unitId,orgId,userId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getPurchaseOrderDropdown failed", ex);
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
			return new ResponseEntity<Object>(grnService.getGRNList(grnRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getGoodReceiptNoteList failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	@RequestMapping(value = "/search/invoiceNumber", method = RequestMethod.POST, consumes = "application/json")
	@ApiOperation(value = "Get list of  Good Receipt Note by search", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public @ResponseBody ResponseEntity<Object> getInvoiceNumber(@RequestBody GRNRequest grnRequest)
			throws Exception {
		try {
			return new ResponseEntity<Object>(grnService.getInvoiceNumber(grnRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("getInvoiceNumber failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	

	@RequestMapping(value = "", method = RequestMethod.POST)
	@ApiOperation(value = "Save Good Receipt Note", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public synchronized ResponseEntity<Object> saveGoodReceiptNote(@RequestBody GRNRequest grnRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(grnService.saveGoodReceiptNote(grnRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("saveGoodReceiptNote failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/grnsettled", method = RequestMethod.POST)
	@ApiOperation(value = "Settled Good Receipt Note", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> settledGoodReceiptNote(@RequestBody GrnSettledRequest grnSettledRequest) throws Exception {
		try {
			return new ResponseEntity<Object>(grnService.settledGoodReceiptNote(grnSettledRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("settledGoodReceiptNote failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Update Good Receipt Note", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> updatePurchaseOrder(@PathVariable Integer id, @RequestBody GRNRequest grnRequest)
			throws Exception {
		try {
			grnRequest.setId(id);
			CommonResponse goodReceiptNoteResponse=(CommonResponse) grnService.updateGoodReceiptNote(grnRequest);
			Integer index=0;
			Integer index1=0;
			for(Object grnDetail:goodReceiptNoteResponse.getListObject())
			{
				for (GRNDetailsRequest item : grnRequest.getItems()) {
					if(item.getItemId().equals(((GoodReceiptNoteDetail) grnDetail).getItemId()))
					{
					if (goodReceiptNoteResponse.getObject().equals(Constants.getConsumableId())) {
						grnService.updateBonusBatchExpiry(((GoodReceiptNoteDetail) grnDetail).getId());
						for (GrnBonusBatchExpiry grnBatchExpiry : item.getGrnBonusBatchExpiry()) {
		                    GrnBonusBatchExpiry grnBatchExpiry1 = new GrnBonusBatchExpiry();
		                            grnBatchExpiry1.setBatchNo(grnBatchExpiry.getBatchNo());
		                            grnBatchExpiry1.setExpiryDate(grnBatchExpiry.getExpiryDate());
		                            grnBatchExpiry1.setBonusQuantity(grnBatchExpiry.getBonusQuantity());
		                            grnBatchExpiry1.setGrnDetailId(((GoodReceiptNoteDetail) grnDetail).getId());
		                            grnBatchExpiry1.setOrganizationId(grnRequest.getOrganizationId());
		                            grnBatchExpiry1.setUnitId(grnRequest.getUnitId());
		                            grnBatchExpiry1.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
		                            grnBatchExpiry1.setUpdatedByUser(grnRequest.getUserId());
		                            grnBatchExpiry1.setAddedByUser(grnRequest.getUserId());
		                            grnBatchExpiry1.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
		                            grnBatchExpiry1.setGrnBonusBatchExpiryId(grnBatchExpiry.getGrnBonusBatchExpiryId());
//		                            if(grnBatchExpiry1.getGrnBonusBatchExpiryId()!=null)
//		                            {
//		                                BonusBatchExpiryDao.updateBonusBatchExpiry(grnBatchExpiry1);
//		                            }
//		                            else
//		                            {
		                            grnService.saveBonusBatchExpiry(grnBatchExpiry1);
//		                            }
		                }
					}
					index++;
					
					}
				}
			}
			for(Object grnDetail:goodReceiptNoteResponse.getListObject())
			{
				for (GRNDetailsRequest item : grnRequest.getItems()) {
					if(item.getItemId().equals(((GoodReceiptNoteDetail) grnDetail).getItemId()))
					{
					if (goodReceiptNoteResponse.getObject().equals(Constants.getFixedAssetId())) {
						grnService.updateBonusBatchExpiry(((GoodReceiptNoteDetail) grnDetail).getId());
						for (GrnBonusBatchExpiry grnBatchExpiry : item.getGrnBonusBatchExpiry()) {
		                    GrnBonusBatchExpiry grnBatchExpiry1 = new GrnBonusBatchExpiry();
		                            grnBatchExpiry1.setBatchNo(grnBatchExpiry.getBatchNo());
		                            grnBatchExpiry1.setExpiryDate(null);
		                            grnBatchExpiry1.setBonusQuantity(grnBatchExpiry.getBonusQuantity());
		                            grnBatchExpiry1.setGrnDetailId( ((GoodReceiptNoteDetail) grnDetail).getId());
		                            grnBatchExpiry1.setOrganizationId(grnRequest.getOrganizationId());
		                            grnBatchExpiry1.setUnitId(grnRequest.getUnitId());
		                            grnBatchExpiry1.setUpdatedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
		                            grnBatchExpiry1.setUpdatedByUser(grnRequest.getUserId());
		                            grnBatchExpiry1.setAddedByUser(grnRequest.getUserId());
		                            grnBatchExpiry1.setAddedDate(TimeZoneComponent.getDateByzone(grnRequest.getUnitId()));
		                            grnBatchExpiry1.setGrnBonusBatchExpiryId(grnBatchExpiry.getGrnBonusBatchExpiryId());
//		                            if(grnBatchExpiry1.getGrnBonusBatchExpiryId()!=null)
//		                            {
//		                                BonusBatchExpiryDao.updateBonusBatchExpiry(grnBatchExpiry1);
//		                            }
//		                            else
//		                            {
		                            grnService.saveBonusBatchExpiry(grnBatchExpiry1);
//		                            }
		                }
					}
					index1++;
					}
				}
			}
			CommonResponse response = new CommonResponse();
			response.setMessage(goodReceiptNoteResponse.getMessage());
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("updatePurchaseOrder failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/{unitId}/{orgId}/{id}", method = RequestMethod.GET)
	@ApiOperation(value = "Get Good Receipt Note details by id", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> getGoodReceiptNote(@PathVariable Integer id,@PathVariable Integer unitId,@PathVariable Integer orgId) throws Exception {
		try {
			return new ResponseEntity<Object>(grnService.getGoodReceiptNote(id,unitId,orgId), HttpStatus.OK);
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
	public ResponseEntity<Object> approveGoodReceiptNote(@RequestBody GRNStatusDto grnStatusDto) throws Exception {
		try {
			return new ResponseEntity<Object>(grnService.approveGoodReceiptNote(grnStatusDto), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("approveGoodReceiptNote failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}

	@RequestMapping(value = "/cancel/{id}", method = RequestMethod.PATCH)
	@ApiOperation(value = "Cancel Good Receipt Note", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> cancelGoodReceiptNote(@PathVariable Integer id, @RequestBody GRNRequest grnRequest)
			throws Exception {
		try {
			grnRequest.setId(id);
			return new ResponseEntity<Object>(grnService.cancelGoodReceiptNote(grnRequest), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("cancelGoodReceiptNote failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	
	@RequestMapping(value = "/previousgrndetails/{vendorId}/{itemid}/{grnId}", method = RequestMethod.GET)
	@ApiOperation(value = "Cancel Good Receipt Note", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> previousGrnDetails(@PathVariable Integer vendorId , @PathVariable Integer itemid, @PathVariable Integer grnId)
			throws Exception {
		try {
			return new ResponseEntity<Object>(grnService.previousGrnDetails(vendorId , itemid, grnId), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("previousGrnDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
	
	@RequestMapping(value = "/newpreviousgrndetails/{vendorId}/{itemid}", method = RequestMethod.GET)
	@ApiOperation(value = "Cancel Good Receipt Note", response = CommonResponse.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Suceess|OK"),
			@ApiResponse(code = 401, message = "not authorized!"), @ApiResponse(code = 403, message = "forbidden!!!"),
			@ApiResponse(code = 404, message = "not found!!!") })
	public ResponseEntity<Object> previousNewGrnDetails(@PathVariable Integer vendorId , @PathVariable Integer itemid)
			throws Exception {
		try {
			return new ResponseEntity<Object>(grnService.previousNewGrnDetails(vendorId , itemid), HttpStatus.OK);
		} catch (Exception ex) {
			logger.error("previousNewGrnDetails failed", ex);
			throw new ServiceException(ex.getMessage());
		}
	}
}
