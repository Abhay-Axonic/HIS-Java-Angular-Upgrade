package com.param.billing.global.transaction.service;

import in.param.exception.ApplicationException;

import com.param.billing.global.transaction.config.dto.TCompanyInvoiceServiceDetailsDto;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ITCompanyInvoiceServiceDetailsService {

	//public Response saveCompanyInvoiceServiceDetails(TCompanyInvoiceServiceDetailsDto companyInvoiceServiceDetailsDto )throws ApplicationException;
	public Response getCompInvServDetailsByInvDtls(TCompanyInvoiceServiceDetailsDto companyInvoiceServiceDetailsDto)throws ApplicationException; 
}
