package com.param.billing.global.transaction.dao;

import com.param.billing.global.model.CompanyReceiptDetail;
import com.param.billing.global.transaction.dto.CompanyReceiptDetailDto;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICompanyReceiptDetailDao extends IGenericDao<CompanyReceiptDetail, Integer> {
	public Response saveCompanyReceiptDetail(CompanyReceiptDetailDto dto)throws ApplicationException;
}
