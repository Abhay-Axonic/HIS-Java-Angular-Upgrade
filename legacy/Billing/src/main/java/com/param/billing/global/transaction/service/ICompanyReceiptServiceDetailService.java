package com.param.billing.global.transaction.service;

import com.param.billing.global.transaction.dto.CompanyReceiptServiceDetailsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICompanyReceiptServiceDetailService {
	public Response saveCompanyReceiptServiceDetail(CompanyReceiptServiceDetailsDto dto)throws ApplicationException;
}
