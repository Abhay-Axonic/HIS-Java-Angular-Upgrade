package com.param.billing.cashDesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.cashDesk.dto.ShiftClosingMasterDto;
import com.param.billing.cashDesk.service.IShiftClosingMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings({"rawtypes"})
public class ShiftClosingMasterController implements ICommonConstants{
	
	@Autowired
	private IShiftClosingMasterService iShiftClosingMasterService;
	
	
	@RequestMapping(value = "/getCashReceipts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCashReceipts(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.getCashReceipts(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getBankReceipts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBankReceipts(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.getBankReceipts(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getChequeInHand", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getChequeInHand(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.getChequeInHand(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getCardReceipts", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCardReceipts(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.getCardReceipts(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getCardInHand", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCardInHand(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.getCardInHand(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	
	@RequestMapping(value = "/getIntrimCash", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIntrimCash(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.getIntrimCash(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	

	@RequestMapping(value = "/getIntrimCheque", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIntrimCheque(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.getIntrimCheque(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/getIntrimCard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getIntrimCard(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.getIntrimCard(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getCashHandoverDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCashHandoverDetails(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.getCashHandoverDetails(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/saveShiftCloseBalance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveShiftCloseBalance(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.saveShiftCloseBalance(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	/*@RequestMapping(value = "/saveShiftCloseBalance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {


	Response<ShiftClosingMasterDto, Integer> response = null;
	try {

	JsonCheck.mapJsonToObject(sampleMasterDto, ShiftClosingMasterDto.class);
	
	* response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
	* response;
	
	return null;
	} catch (Exception e) {
	
	return new Response(ERROR, null, null, null, null);
	}
	}
	*/
	@RequestMapping(value = "/getShiftClosingBalanceSheet", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getShiftClosingBalanceSheet(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.getShiftClosingBalanceSheet(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	
	@RequestMapping(value = "/saveShiftClosedetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveShiftClosedetails(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.saveShiftClosedetails(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getRefundList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getRefundList(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.getRefundList(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getshiftClosingRecords", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getshiftClosingRecords(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.getshiftClosingRecords(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/validateInterimByShiftClosing", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response validateInterimByShiftClosing(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		
		try {
			return iShiftClosingMasterService.validateInterimByShiftClosing(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/validateShiftClosingBalance", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response validateShiftClosingBalance(@RequestBody ShiftClosingMasterDto closingMasterDto ){
		try {
			
				return iShiftClosingMasterService.validateShiftClosingBalance(closingMasterDto);
			
		} catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
