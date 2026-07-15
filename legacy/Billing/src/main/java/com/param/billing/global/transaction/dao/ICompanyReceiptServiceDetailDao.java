package com.param.billing.global.transaction.dao;

import com.param.billing.global.transaction.dto.CompanyReceiptServiceDetailsDto;
import com.param.billing.global.transaction.model.CompanyReceiptServiceDetails;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICompanyReceiptServiceDetailDao extends IGenericDao<CompanyReceiptServiceDetails, Integer> {
	public Response saveCompanyReceiptServiceDetail(CompanyReceiptServiceDetailsDto dto)throws ApplicationException;
}
