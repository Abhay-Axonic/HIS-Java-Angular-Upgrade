package com.param.billing.unit.service;

import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ITCompanyInvoiceDetailsService {
	public Response getCompanyInvoiceDetailsListByInvoiceId(int invoiceId)throws ApplicationException;
}
