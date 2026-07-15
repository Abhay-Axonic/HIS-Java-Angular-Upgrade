package com.param.billing.global.transaction.service;

import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseDto;
import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseRecieptDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
@SuppressWarnings("rawtypes")
public interface IMiscellaneousExpenseService extends ICommonConstants{

	Response saveExpense(MiscellaneousExpenseDto miscellaneousExpenseDto);

	Response getListOfExpenses(MiscellaneousExpenseDto miscellaneousExpenseDto);

	Response getExpensesDetailsById(Integer expenseId);

	Response cancelExpenseById(MiscellaneousExpenseDto miscellaneousExpenseDto);

	Response cancelReceiptById(MiscellaneousExpenseRecieptDto expenseRecieptDto);

	Response requetForApproval(MiscellaneousExpenseDto expenseRecieptDto);

}
