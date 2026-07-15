package com.param.billing.global.transaction.dao;

import com.param.billing.global.transaction.config.dto.CompanyInvoiceCreationReqDto;
import com.param.billing.global.transaction.config.dto.MCompanyInvoiceMasterDto;
import com.param.billing.global.transaction.dto.CompanyInvoiceSettlementSearchReqDto;
import com.param.billing.global.transaction.model.MCompanyInvoiceMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICompanyInvoiceCreationMasterDao extends IGenericDao<MCompanyInvoiceMaster, Integer>{
	
	public Response saveCompanyInvoiceCreationMasterList(MCompanyInvoiceMasterDto reqDto) throws ApplicationException;
	public Response getcompanyRemainderList(CompanyInvoiceCreationReqDto companyInvoiceCreationReqDto)throws BillingException;
	public Response getCompInvoiceForSettlement(CompanyInvoiceSettlementSearchReqDto reqDto)throws BillingException;
	public Response updateCompanyInvoiceDueAmount(int id, double amount)throws BillingException;
	public Response updateCompanyInvoiceDueAmount(Double availableDeposit, Integer companyReceiptsExpenseId)throws BillingException;
	public Response updateCompanyInvoiceSettlementDate(int id, double amount)throws BillingException;
	
}
