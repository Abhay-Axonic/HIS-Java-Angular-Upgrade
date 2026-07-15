package com.param.billing.global.transaction.dao;

import com.param.billing.global.transaction.config.dto.CompanyInvoiceCreationReqDto;
import com.param.billing.global.transaction.dto.CompanyInvoiceSettlementSearchReqDto;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ICompanyInvoiceCreationDao extends IGenericDao<BillingMaster, Integer>{
	public Response getCompanyInvoiceCreationList(CompanyInvoiceCreationReqDto companyInvoiceCreationReqDto)throws BillingException;
	public Response getDetailsCompanyInvoiceCreationList(CompanyInvoiceCreationReqDto companyInvoiceCreationReqDto)throws BillingException;
	public Response getCompanyInvoiceSettlementDetailsByCompanyInvoiceId(CompanyInvoiceSettlementSearchReqDto reqDto) throws BillingException;
	public Response getCompInvoiceForSettlementDetails(CompanyInvoiceSettlementSearchReqDto reqDto)throws BillingException;
	public Response companyInvoiceCreationListByBillId(CompanyInvoiceCreationReqDto companyInvoiceCreationReqDto)throws BillingException;
}
