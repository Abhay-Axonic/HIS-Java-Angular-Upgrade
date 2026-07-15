package com.param.billing.global.transaction.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.transaction.service.IBillingDetailsService;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.BillingDetailsDto;

@RestController
@RequestMapping(value="api/billing")
@SuppressWarnings("rawtypes")
public class BillingDetailsController implements ICommonConstants{
	
	@Autowired
	private IBillingDetailsService iBillingDetailsService;
	
	@RequestMapping(value="/billingDetails", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response saveBillingDetails(@RequestBody BillingDetailsDto billingDetailsDto) {
		try {
			return null; //iBillingDetailsService.saveBillingDetails(billingDetailsDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/billDetails/{orgId}/{unitId}/{billId}" , method = RequestMethod.GET)
	public Response getBillDetailsByBillId(@PathVariable Map<String,String> params)
	{
		try{
			int orgId = Integer.parseInt(params.get("orgId").trim());
			int unitId = Integer.parseInt(params.get("unitId").trim());
			int billId = Integer.parseInt(params.get("billId").trim());
			return iBillingDetailsService.getBillDetailsByBillId(orgId,unitId,billId);
		}catch(BillingException be){
			throw be;
		}
		catch (Exception e){
				
				throw e;
				//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/transaction/logs" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getTransactionLogs(@RequestBody BillingDetailsDto billingDetailsDto){
		try{
			return iBillingDetailsService.getTransactionLogs(billingDetailsDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/addendum/services/{billId}" ,  method = RequestMethod.GET)
	public Response getBillDetailsForAddendumBill(@PathVariable("billId") int billId){
		try{
			return iBillingDetailsService.getBillServicesForAddendumBill(billId);
		}catch(Exception e){
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
}
