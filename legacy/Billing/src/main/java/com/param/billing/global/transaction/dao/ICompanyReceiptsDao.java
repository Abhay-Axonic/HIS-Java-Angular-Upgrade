package com.param.billing.global.transaction.dao;

import java.util.List;

import com.param.billing.global.model.CompanyReceiptsExpense;
import com.param.billing.global.transaction.config.dto.CompanyReceiptsDto;
import com.param.billing.global.transaction.config.dto.CompanyReceiptsExpenseRecieptDto;
import com.param.billing.global.transaction.config.dto.CompanyRecieptsReceiptDetailsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICompanyReceiptsDao {

	Response saveExpense(CompanyReceiptsDto companyReceiptsDto);

	Response updateExpense(CompanyReceiptsDto companyReceiptsDto);

	Response saveExpenseReciept(CompanyReceiptsDto companyReceiptsDto);

	Response saveExpenseRecieptDetails(List<CompanyRecieptsReceiptDetailsDto> companyRecieptsReceiptDetailsDto,
			Integer expenseRecieptId);

	Response getListOfExpenses(CompanyReceiptsDto companyReceiptsDto);

	Response getExpensesDetailsById(Integer companyReceiptsExpenseId);

	Response cancelExpenseRecieptById(CompanyReceiptsExpenseRecieptDto companyReceiptsExpenseRecieptDto);

	Response cancelExpenseById(CompanyReceiptsDto companyReceiptsDto);

	Response companyReceiptsByCompany(CompanyReceiptsDto companyReceiptsDto);

	Response companyReceiptsConsumeptionDtls(CompanyReceiptsDto companyReceiptsDto);

	List<CompanyReceiptsExpense> getCompanyReceiptsExpenseByCreditNoteId(Integer creditNoteId)throws ApplicationException;

	void inactiveComapnyReciept(Integer companyReceiptsExpenseId);


}
