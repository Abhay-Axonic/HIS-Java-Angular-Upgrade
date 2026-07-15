package com.param.billing.global.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.global.dto.BillListReqDto;
import com.param.billing.global.transaction.service.IBillPayeeDetailsService;
import com.param.billing.global.transaction.service.IBillingMasterService;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.IBillingStatusMasterConstant;
import com.param.global.common.Response;

@RestController
@RequestMapping("api/billing")
@SuppressWarnings({"rawtypes","unchecked"})
public class DebitNoteController implements ICommonConstants,IBillingStatusMasterConstant {
	
	
	@Autowired
	private IBillingMasterService iBillingMasterService;

	@Autowired
	private IBillPayeeDetailsService iBillPayeeDetailsService;
	
	
	@RequestMapping(value = "/debitNote/filter" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDebitNoteFilterList(@RequestBody BillListReqDto reqDto){
		try{
			return iBillingMasterService.getDebitNoteFilterList(reqDto);
		}catch(BillingException be){
			throw be;
		}
		catch (Exception e){
				
				throw e;
				//throw new BillingException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/debitNoteDetails/{billId}" , method = RequestMethod.GET)
	public Response getDebitNoteDetailsByBillMasterId(@PathVariable("billId")int billId){
		try{
			return iBillPayeeDetailsService.getDebitNoteDetailsByBillMasterId(billId);
		}catch(BillingException be){
			throw be;
		}
		catch (Exception e){
				
				throw e;
				//throw new BillingException(e.getMessage());
		}
	}

}
