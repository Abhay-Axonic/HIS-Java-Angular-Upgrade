package com.param.billing.cashDesk.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.cashDesk.dto.CashHandoverDetailsDto;
import com.param.billing.cashDesk.dto.DenominationMasterDto;
import com.param.billing.cashDesk.service.ICashHandoverMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DenominationDetailsMasterDto;
@RestController
@RequestMapping(value="/api/global")
@SuppressWarnings({"rawtypes"})
public class CashHandoverMasterController implements ICommonConstants{
	
	@Autowired
	private ICashHandoverMasterService iCashHandoverMasterService;
	
	@RequestMapping(value = "/saveCashHandoverMaster", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCashHandoverMaster(@RequestBody CashHandoverDetailsDto cashHandoverDetailsDto){
		
		try {
			return iCashHandoverMasterService.saveCashHandoverMaster(cashHandoverDetailsDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	
	/*@SuppressWarnings("unchecked")
	@RequestMapping(value="/saveDenominationmaster", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveDenominationmaster(@RequestBody List<DenominationDetailsMasterDto> denominationDetailsMasterDto) {
		try
		{
			return iCashHandoverMasterService.saveDenominationmaster(denominationDetailsMasterDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
	
	
	@RequestMapping(value = "/getListOfDenominations", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getListOfDenominations(@RequestBody DenominationMasterDto denominationMasterDto){

		try {
			
			return iCashHandoverMasterService.getListOfDenominations(denominationMasterDto);

		} catch (Exception e) {
			
			throw e;
		}
	}
	
	/*@RequestMapping(value = "/validateUser", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response validateUser(@RequestBody CashHandoverDetailsDto cashHandoverDetailsDto){


		try {
			return iCashHandoverMasterService.validateUser(cashHandoverDetailsDto);

		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
*/
	
	@RequestMapping(value = "/getListOfDenominationDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getListOfDenominationDetails(@RequestBody DenominationDetailsMasterDto denominationDetailsMasterDto){

		try {
			
			return iCashHandoverMasterService.getListOfDenominationDetails(denominationDetailsMasterDto);

		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value="/getcashOpeningTransactionList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getcashOpeningTransactionList(@RequestBody CashHandoverDetailsDto cashHandoverDetailsDto) {
		try
		{
			return iCashHandoverMasterService.getcashOpeningTransactionList(cashHandoverDetailsDto);
		}catch(Exception e)
		{
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/updateStatusToComplete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response updateStatusToComplete(@RequestBody CashHandoverDetailsDto cashHandoverDetailsDto) {
		try {
			return iCashHandoverMasterService.updateStatusToComplete(cashHandoverDetailsDto);

		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/saveCashInOut", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveCashInOut(@RequestBody CashHandoverDetailsDto cashHandoverDetailsDto){
		
		try {
			return iCashHandoverMasterService.saveCashInOut(cashHandoverDetailsDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/inActiveAndActiveEntry", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response inActiveEntry(@RequestBody CashHandoverDetailsDto cashHandoverDetailsDto){
		
		try {
			return iCashHandoverMasterService.inActiveAndActiveEntry(cashHandoverDetailsDto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value="/getcashInOutList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getcashInOutList(@RequestBody CashHandoverDetailsDto cashHandoverDetailsDto) {
		try
		{
			return iCashHandoverMasterService.getcashInOutList(cashHandoverDetailsDto);
		}catch(Exception e)
		{
			
			throw e;
		}
	}
	
	
	@RequestMapping(value="/getUserInhandCash", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getUserInhandCash(@RequestBody CashHandoverDetailsDto cashHandoverDetailsDto) {
		try
		{
			return iCashHandoverMasterService.getUserInhandCash(cashHandoverDetailsDto);
		}catch(Exception e)
		{
			
			throw e;
		}
	}
	
	
}
