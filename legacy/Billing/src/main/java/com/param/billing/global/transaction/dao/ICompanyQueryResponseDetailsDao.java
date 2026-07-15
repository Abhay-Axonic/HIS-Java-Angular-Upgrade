package com.param.billing.global.transaction.dao;

import com.param.billing.global.transaction.config.dto.TCompanyInvoiceQueryDetailDto;
import com.param.billing.global.transaction.model.TCompanyInvoiceQueryDetail;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICompanyQueryResponseDetailsDao extends IGenericDao<TCompanyInvoiceQueryDetail, Integer>{
	
	public Response saveCompanyQueryResponseDetails(TCompanyInvoiceQueryDetailDto reqDto) throws ApplicationException;
	public Response updateCompanyQueryResponseDetailsByCompanyInvoiceReminderId(TCompanyInvoiceQueryDetailDto reqDto) throws ServiceException;
	public Response getPendingCompanyQueryResponseDetails(TCompanyInvoiceQueryDetailDto reqDto) throws ApplicationException;
}
