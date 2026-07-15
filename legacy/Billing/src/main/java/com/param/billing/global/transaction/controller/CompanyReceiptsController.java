package com.param.billing.global.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.billing.global.transaction.config.dto.CompanyReceiptsDto;
import com.param.billing.global.transaction.config.dto.CompanyReceiptsExpenseRecieptDto;
import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseDto;
import com.param.billing.global.transaction.config.dto.MiscellaneousExpenseRecieptDto;
import com.param.billing.global.transaction.service.ICompanyReceiptsService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@Controller
@RequestMapping("/api/billing/companyReceipts")
@SuppressWarnings("rawtypes")
public class CompanyReceiptsController implements ICommonConstants{
	
	@Autowired
	ICompanyReceiptsService iCompanyReceiptsService;

	@RequestMapping(value = "/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCompanyReceipts(@RequestBody CompanyReceiptsDto companyReceiptsDto) {
		try {

			return iCompanyReceiptsService.saveCompanyReceipts(companyReceiptsDto);
			
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getListOfExpenses(@RequestBody CompanyReceiptsDto companyReceiptsDto) {
		try {
		
			return iCompanyReceiptsService.getListOfExpenses(companyReceiptsDto);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/details/{companyReceiptsExpenseId}", method = RequestMethod.GET)
	public @ResponseBody Response getExpensesDetailsById(@PathVariable("companyReceiptsExpenseId") Integer companyReceiptsExpenseId) {
		try {
		
			return iCompanyReceiptsService.getExpensesDetailsById(companyReceiptsExpenseId);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/receipt/cancel", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelReceiptById(@RequestBody CompanyReceiptsExpenseRecieptDto companyReceiptsExpenseRecieptDto) {
		try {
		
			return iCompanyReceiptsService.cancelReceiptById(companyReceiptsExpenseRecieptDto);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/cancel", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response cancelExpenseById(@RequestBody CompanyReceiptsDto companyReceiptsDto) {
		try {
		
			return iCompanyReceiptsService.cancelExpenseById(companyReceiptsDto);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/requet/cancel", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response requetForApproval(@RequestBody CompanyReceiptsDto companyReceiptsDto) {
		try {
		
			return iCompanyReceiptsService.requetForCancel(companyReceiptsDto);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/companyReceiptsByCompany", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response companyReceiptsByCompany(@RequestBody CompanyReceiptsDto companyReceiptsDto) {
		try {
		
			return iCompanyReceiptsService.companyReceiptsByCompany(companyReceiptsDto);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@RequestMapping(value = "/companyReceiptsConsumeptionDtls", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response companyReceiptsConsumeptionDtls(@RequestBody CompanyReceiptsDto companyReceiptsDto) {
		try {
		
			return iCompanyReceiptsService.companyReceiptsConsumeptionDtls(companyReceiptsDto);
		
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
