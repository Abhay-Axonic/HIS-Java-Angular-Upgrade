package com.param.billing.global.transaction.dao;

import java.util.List;

import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseDto;
import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseRecieptDetailsDto;
import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseRecieptDto;
import com.param.global.common.Response;
@SuppressWarnings("rawtypes")
public interface IMiscellaneousExpenseDao {

	Response saveExpense(MiscellaneousExpenseDto miscellaneousExpenseDto);

	Response saveExpenseReciept(MiscellaneousExpenseDto miscellaneousExpenseDto);

	Response saveExpenseRecieptDetails(List<MiscellaneousExpenseRecieptDetailsDto> miscellaneousExpenseRecieptDetailsDtosList,Integer expenseRecieptId);

	Response getListOfExpenses(MiscellaneousExpenseDto miscellaneousExpenseDto);

	Response getExpensesDetailsById(Integer expenseId);

	Response updateExpense(MiscellaneousExpenseDto miscellaneousExpenseDto);

	Response cancelExpenseById(MiscellaneousExpenseDto miscellaneousExpenseDto);

	Response cancelExpenseRecieptById(MiscellaneousExpenseRecieptDto miscellaneousExpenseRecieptDto);

}
