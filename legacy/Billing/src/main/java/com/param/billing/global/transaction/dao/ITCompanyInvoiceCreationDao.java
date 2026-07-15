package com.param.billing.global.transaction.dao;

import com.param.billing.global.transaction.config.dto.TCompanyInvoiceDetailsDto;
import com.param.billing.global.transaction.model.TCompanyInvoiceDetails;
import com.param.billing.global.transaction.model.TCompanyInvoiceServiceDetails;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ITCompanyInvoiceCreationDao extends IGenericDao<TCompanyInvoiceDetails, Integer>{
	
	public Response saveCompanyInvoiceCreationMasterList(TCompanyInvoiceDetailsDto reqDto) throws ApplicationException;
	public Response getCompanyInvoiceDetailsListByInvoiceId(int invoiceId)throws ApplicationException;
	public Response updateDueAmount(int id,double amount)throws ApplicationException;
	public Response updateInvoiceDueAmountByInvoiceDtlsId(int id, double amount) throws ApplicationException;
	Response getCompanyInvoiceDetailsByBillDetails(Integer billId, Integer billPayeeDetailsId)throws ApplicationException;
	public void updateTCompanyInvoiceDetails(TCompanyInvoiceDetails companyInvoiceDetails);
	public void updateTCompanyInvoiceServiceDetails(TCompanyInvoiceServiceDetails details);
	
}
