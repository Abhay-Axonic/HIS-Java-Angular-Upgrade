package com.param.billing.global.transaction.dao;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

import com.param.billing.global.transaction.config.dto.TCompanyInvoiceServiceDetailsDto;
import com.param.billing.global.transaction.model.TCompanyInvoiceServiceDetails;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ITCompanyInvoiceServiceDetailsDao extends IGenericDao<TCompanyInvoiceServiceDetails, Integer>{
	
	public Response saveCompanyInvoiceServiceDetails(TCompanyInvoiceServiceDetailsDto companyInvoiceServiceDetailsDto )throws ApplicationException;
	public Response getCompInvServDetailsByInvDtls(TCompanyInvoiceServiceDetailsDto companyInvoiceServiceDetailsDto )throws ApplicationException;
	public Response updateRecvAndDisallAmtByInvDtls(TCompanyInvoiceServiceDetailsDto companyInvoiceServiceDetailsDto )throws ApplicationException;
	public Response getAmountDtlsByCompServInvId(int compInvSerDetlsId) throws ApplicationException;
	
}
