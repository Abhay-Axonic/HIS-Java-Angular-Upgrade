package com.param.billing.global.transaction.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.unit.service.ITCompanyInvoiceDetailsService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping(value="/api/billing")
@SuppressWarnings("rawtypes")
public class CompanyInvoiceDetailsController implements ICommonConstants{

	@Autowired
	private ITCompanyInvoiceDetailsService iTCompanyInvoiceDetailsService;
	
	/**
	 * METHOD IS TO GET INVOICE DETAILS BY INVOICE ID
	 * @param invoiceId
	 * @return
	 */
	@RequestMapping(value = "/invoicedtls/invoice/{invoiceId}" , method = RequestMethod.GET )
	public Response getInvoiceDetailsByInvoiceId(@PathVariable("invoiceId")int invoiceId){
		try{
			return iTCompanyInvoiceDetailsService.getCompanyInvoiceDetailsListByInvoiceId(invoiceId);
		}catch(Exception e){
			
			throw e;
			//return new Response<>(ERROR_CODE, COMMON_ERROR, e.getMessage(), null, null);
		}
	}
}
