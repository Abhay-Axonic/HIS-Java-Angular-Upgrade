package com.param.billing.global.transaction.service;

import com.param.billing.global.transaction.config.dto.CompanyReceiptsDto;
import com.param.billing.global.transaction.config.dto.CompanyReceiptsExpenseRecieptDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ICompanyReceiptsService extends ICommonConstants{

	Response saveCompanyReceipts(CompanyReceiptsDto companyReceiptsDto);

	Response getListOfExpenses(CompanyReceiptsDto companyReceiptsDto);

	Response getExpensesDetailsById(Integer companyReceiptsExpenseId);

	Response cancelReceiptById(CompanyReceiptsExpenseRecieptDto companyReceiptsExpenseRecieptDto);

	Response cancelExpenseById(CompanyReceiptsDto companyReceiptsDto);

	Response requetForCancel(CompanyReceiptsDto companyReceiptsDto);

	Response companyReceiptsByCompany(CompanyReceiptsDto companyReceiptsDto);

	Response companyReceiptsConsumeptionDtls(CompanyReceiptsDto companyReceiptsDto); 

}
