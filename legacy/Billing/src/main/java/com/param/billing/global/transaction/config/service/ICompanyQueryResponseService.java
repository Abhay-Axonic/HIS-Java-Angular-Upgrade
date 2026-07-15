package com.param.billing.global.transaction.config.service;

import java.util.List;

import com.param.billing.global.transaction.config.dto.CompanyInvoiceQueryReqDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceQueryDetailDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICompanyQueryResponseService {
	public Response saveCompanyQueryResponse(CompanyInvoiceQueryReqDto reqDto) throws ApplicationException;
	public Response updateCompanyQueryResponseDetailsByCompanyInvoiceReminderId(List<TCompanyInvoiceQueryDetailDto> listReqDto) throws ApplicationException;
	public Response getPendingCompanyQueryResponseDetails(TCompanyInvoiceQueryDetailDto reqDto) throws ApplicationException;
}
	