package com.param.billing.global.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseDto;
import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseRecieptDto;
import com.param.billing.global.transaction.service.IMiscellaneousExpenseService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@Controller
@RequestMapping("/api/billing/expense")
@SuppressWarnings("rawtypes")
public class MiscellaneousExpenseController implements ICommonConstants{

	@Autowired
	IMiscellaneousExpenseService iMiscellaneousExpenseService; 
	
	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveExpense(@RequestBody MiscellaneousExpenseDto miscellaneousExpenseDto) {
		try {

			return iMiscellaneousExpenseService.saveExpense(miscellaneousExpenseDto);
			
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfExpenses(@RequestBody MiscellaneousExpenseDto miscellaneousExpenseDto) {
		try {
		
			return iMiscellaneousExpenseService.getListOfExpenses(miscellaneousExpenseDto);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/details/{expenseId}", method = RequestMethod.GET)
	public @ResponseBody Response getExpensesDetailsById(@PathVariable("expenseId") Integer expenseId) {
		try {
		
			return iMiscellaneousExpenseService.getExpensesDetailsById(expenseId);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/cancel", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelExpenseById(@RequestBody MiscellaneousExpenseDto miscellaneousExpenseDto) {
		try {
		
			return iMiscellaneousExpenseService.cancelExpenseById(miscellaneousExpenseDto);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/receipt/cancel", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelReceiptById(@RequestBody MiscellaneousExpenseRecieptDto expenseRecieptDto) {
		try {
		
			return iMiscellaneousExpenseService.cancelReceiptById(expenseRecieptDto);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value = "/requet/approval", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requetForApproval(@RequestBody MiscellaneousExpenseDto expenseRecieptDto) {
		try {
		
			return iMiscellaneousExpenseService.requetForApproval(expenseRecieptDto);
		
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
