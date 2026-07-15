package com.param.billing.cashDesk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.cashDesk.dto.DenominationMasterDto;
import com.param.billing.cashDesk.dto.InterimHandoverMasterDto;
import com.param.billing.cashDesk.service.IInterimHandoverMasterService;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DenominationDetailsMasterDto;


@SuppressWarnings({"rawtypes","unchecked"})
@RestController
@RequestMapping("/adt")
public class InterimHandoverMasterController implements ICommonConstants  {
	
	@Autowired
	private IInterimHandoverMasterService iInterimHandoverMasterService;
	
	
	@RequestMapping(value= "/getListOfDenominations" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody  Response getListOfDenominations(@RequestBody DenominationMasterDto denominationMasterDto){
		try{
			return iInterimHandoverMasterService.getListOfDenominations(denominationMasterDto);
		}catch(Exception e){
			
			throw e;
		}
//		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value= "/saveInterimHandover" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody  Response saveInterimHandover(@RequestBody InterimHandoverMasterDto interimHandoverMasterDto){
		try{
			return iInterimHandoverMasterService.saveInterimHandover(interimHandoverMasterDto);
		}catch(Exception e){
			
			throw e;
		}
//		return new Response(ERROR, null, null, null,null);
	}
	
	@RequestMapping(value="/saveDenominations", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveDenominations(@RequestBody List<DenominationDetailsMasterDto> denominationDetailsMasterDto) {
		try
		{
			return iInterimHandoverMasterService.saveDenominations(denominationDetailsMasterDto);
		}catch(Exception e)
		{
			
			throw e;
		}
//		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value="/getInterimTransactionList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getInterimHandoverList(@RequestBody InterimHandoverMasterDto interimHandoverMasterDto) {
		try
		{
			return iInterimHandoverMasterService.getInterimHandoverList(interimHandoverMasterDto);
		}catch(Exception e)
		{
			
			throw e;
		}
//		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/*@RequestMapping(value="/validateUser1", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response validateUser1(@RequestBody InterimHandoverMasterDto interimHandoverMasterDto) {
		try
		{
			return iInterimHandoverMasterService.validateUser1(interimHandoverMasterDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/
	
	/*@RequestMapping(value="/getICHNumberList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getICHNumberList(@RequestBody InterimHandoverMasterDto interimHandoverMasterDto) {
		try
		{
			return iInterimHandoverMasterService.getICHNumberList(interimHandoverMasterDto);
		}catch(Exception e)
		{
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	*/
	@RequestMapping(value="/getChequeList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response getChequeList(@RequestBody PaymentReceiptDetailsDto paymentDepositDetailsDto) {
		try
		{
			return iInterimHandoverMasterService.getChequeList(paymentDepositDetailsDto);
		}catch(Exception e)
		{
			
			throw e;
		}
//		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getDenominationDetailsList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDenominationDetailsList(@RequestBody DenominationDetailsMasterDto denominationDetailsMasterDto){

		try {
			
			return iInterimHandoverMasterService.getDenominationDetailsList(denominationDetailsMasterDto);

		} catch (Exception e) {
			
			throw e;
//			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
	
	@RequestMapping(value = "/getCardTransactionList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCardTransactionList(@RequestBody PaymentReceiptDetailsDto paymentDepositDetailsDto){

		try {
			
			return iInterimHandoverMasterService.getCardTransactionList(paymentDepositDetailsDto);

		} catch (Exception e) {
			
			throw e;
//			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
		
		@RequestMapping(value = "/saveChequeInterimTransaction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response saveChequeInterimTransaction(@RequestBody InterimHandoverMasterDto interimHandoverMasterDto){

			try {
				
				return iInterimHandoverMasterService.saveChequeInterimTransaction(interimHandoverMasterDto);

			} catch (Exception e) {
				
				throw e;
//				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

	}
		@RequestMapping(value = "/saveCardInterimTransaction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response saveCardInterimTransaction(@RequestBody InterimHandoverMasterDto interimHandoverMasterDto){

			try {
				
				return iInterimHandoverMasterService.saveCardInterimTransaction(interimHandoverMasterDto);

			} catch (Exception e) {
				
				throw e;
//				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}

	}
		
		/*@RequestMapping(value = "/saveCardInterimTransaction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response addSample(@RequestBody String sampleMasterDto) {


		Response<InterimHandoverMasterDto, Integer> response = null;
		try {

		JsonCheck.mapJsonToObject(sampleMasterDto, InterimHandoverMasterDto.class);
		
		* response = iSampleMasterService.addSampleMaster(sampleMasterDto); return
		* response;
		
		return null;
		} catch (Exception e) {
		
		return new Response(ERROR, null, null, null, null);
		}
		}*/
		
		@RequestMapping(value = "/updateInterimChequeStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response updateInterimChequeStatus(@RequestBody List<PaymentReceiptDetailsDto> paymentDepositDetailsDto){

			try {
				
				return iInterimHandoverMasterService.updateInterimChequeStatus(paymentDepositDetailsDto);

			} catch (Exception e) {
				
				throw e;
//				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
		
		@RequestMapping(value = "/updateInterimCardStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response updateInterimCardStatus(@RequestBody List<PaymentReceiptDetailsDto> paymentDepositDetailsDto){

			try {
				
				return iInterimHandoverMasterService.updateInterimCardStatus(paymentDepositDetailsDto);

			} catch (Exception e) {
				
				throw e;
//				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
		
		@RequestMapping(value = "/updateInterimStatusToComplete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		public Response updateInterimStatusToComplete(@RequestBody InterimHandoverMasterDto interimHandoverMasterDto) {
			try {
				return iInterimHandoverMasterService.updateInterimStatusToComplete(interimHandoverMasterDto);

			} catch (Exception e) {
				
				throw e;
//				return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}

	}
