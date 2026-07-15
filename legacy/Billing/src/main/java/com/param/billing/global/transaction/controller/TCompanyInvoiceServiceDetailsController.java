package com.param.billing.global.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.global.transaction.config.dto.TCompanyInvoiceServiceDetailsDto;
import com.param.billing.global.transaction.service.ITCompanyInvoiceServiceDetailsService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping(value="/api/billing")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class TCompanyInvoiceServiceDetailsController implements ICommonConstants{

	@Autowired
	private ITCompanyInvoiceServiceDetailsService iTCompanyInvoiceServiceDetailsService;
	
	
	
	@RequestMapping(value="/getCompanyInvServDetails",method = RequestMethod.GET)
	public Response getCompInvServDetailsByInvDtls(@RequestParam("invoiceDetailId") Integer invoiceDetailId){
		try{
			TCompanyInvoiceServiceDetailsDto companyInvoiceServiceDetailsDto = new TCompanyInvoiceServiceDetailsDto();
			companyInvoiceServiceDetailsDto.setInvoiceDetailId(invoiceDetailId);
			return iTCompanyInvoiceServiceDetailsService.getCompInvServDetailsByInvDtls(companyInvoiceServiceDetailsDto);
			
		}catch(Exception e){
			
			throw e;
		}
		//return new Response(ERROR, ERROR_CODE, null, null, null);
	}
}
