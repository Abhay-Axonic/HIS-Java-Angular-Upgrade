package com.param.billing.global.transaction.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.dto.RefundCollectionReqDto;
import com.param.billing.global.transaction.config.dto.UpdatePayeeInformationReqDto;
import com.param.billing.global.transaction.config.service.IUpdatePayeeInformationService;
import com.param.billing.global.transaction.service.IPaymentRefundMasterService;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

@RestController
@RequestMapping("api/billing")
@SuppressWarnings({"rawtypes","unchecked"})
public class UpdatePayeeInformationController {

	@Autowired
	private IUpdatePayeeInformationService iUpdatePayeeInformationService;
	
	@RequestMapping(value = "/getPayeeInfoByVisitType" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public com.param.billing.common.Response getPayeeInfoByVisitTypeId(@RequestBody UpdatePayeeInformationReqDto reqDto){
		try{
			return iUpdatePayeeInformationService.getPayeeInfoByVisitTypeId(reqDto);
		}catch(Exception e){
			
			throw e;
			//throw new BillingException(e.getMessage());
		}
	}
	

}
