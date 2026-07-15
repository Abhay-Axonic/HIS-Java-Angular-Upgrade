package com.param.billing.global.transaction.config.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.global.transaction.config.dto.CompanyInvoiceCreationReqDto;
import com.param.billing.global.transaction.config.dto.CompanyInvoiceReminderMasterDto;
import com.param.billing.global.transaction.config.dto.TCompanyInvoiceCreationReqDto;
import com.param.billing.global.transaction.config.service.ICompanyInvoiceCreationService;
import com.param.billing.global.transaction.dao.IPaymentRecieptDetailsService;
import com.param.billing.global.transaction.dto.CompanyInvoiceSettlementReqDto;
import com.param.billing.global.transaction.dto.CompanyInvoiceSettlementSearchReqDto;
import com.param.billing.global.transaction.dto.PaymentInvoicesDetailsDto;
import com.param.global.common.Response;

/**
 * CLASS TO HANDLE BACK OFFICE FUCTIONALITIES
 * @author User
 *
 */
@RestController
@RequestMapping(value="api/billing")
@SuppressWarnings("rawtypes")
public class CompanyInvoiceCreationController implements ICommonConstants{
	
	@Autowired
	private ICompanyInvoiceCreationService iCompanyInvoiceCreationService;
	@Autowired
	private IPaymentRecieptDetailsService iPaymentRecieptDetailsService;
	
	/**
	 * METHOD TO GET INVOICE LIST BY COMPANY ID
	 * @param companyInvoiceCreationReqDto
	 * @return
	 */
	@RequestMapping(value="/companyInvoiceCreationList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public com.param.global.common.Response getCompanyInvoiceCreationList(@RequestBody CompanyInvoiceCreationReqDto companyInvoiceCreationReqDto) {
		try {
			return iCompanyInvoiceCreationService.getCompanyInvoiceCreationList(companyInvoiceCreationReqDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * METHOD IS TO GET INVOICE DETAILS WITH SERVICE LIST BY BILL
	 * @param companyInvoiceCreationReqDto
	 * @return
	 */
	@RequestMapping(value="/detailsCompanyInvoiceCreationList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public com.param.global.common.Response getDetailsCompanyInvoiceCreationList(@RequestBody CompanyInvoiceCreationReqDto companyInvoiceCreationReqDto) {
		try {
			return iCompanyInvoiceCreationService.getDetailsCompanyInvoiceCreationList(companyInvoiceCreationReqDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * METHOS IS TO SAVE INVOICE CREATED WITH MULTIPLE BILLS
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value="/saveCompanyInvoiceCreationList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public synchronized com.param.global.common.Response saveCompanyInvoiceCreationList(@RequestBody TCompanyInvoiceCreationReqDto reqDto) {
		try {
			return iCompanyInvoiceCreationService.saveCompanyInvoiceCreationList(reqDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * TO GET COMPANY REMINDER FOR SEND TO COMPANY
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value="/getcompanyRemainderList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public com.param.global.common.Response getcompanyRemainderList(@RequestBody CompanyInvoiceCreationReqDto reqDto) {
		try {
			return iCompanyInvoiceCreationService.getcompanyRemainderList(reqDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * TO CREATE COMPANY REMINDER FOR SEND TO COMPANY
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value="/saveCompanyReminder", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public com.param.global.common.Response saveCompanyReminder(@RequestBody CompanyInvoiceReminderMasterDto reqDto) {
		try {
			
			return iCompanyInvoiceCreationService.saveCompanyReminder(reqDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * TO UPDATE REMARK AGAINST REMINDER SET FOR COMPANY
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value="/updateCompanyReminderRemark", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public com.param.global.common.Response updateCompanyReminderRemark(@RequestBody CompanyInvoiceReminderMasterDto reqDto) {
		try {
			
			return iCompanyInvoiceCreationService.updateCompanyReminderRemark(reqDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	/**
	 * METHOD IS GET PENDING INVOICES BY DATE RANGE & COMPANY TYPE
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/companyinvoices/pending" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCompanyInvoiceSettlementList(@RequestBody CompanyInvoiceSettlementSearchReqDto reqDto){
		try {
			return iCompanyInvoiceCreationService.getCompInvoiceForSettlement(reqDto);
		}catch (Exception e) {
			
			throw e;
		//	return new Response<>(ERROR, COMMON_ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 * METHOD IS GET SETTLED INVOICES BY DATE RANGE & COMPANY TYPE
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/companyinvoices/settle" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public synchronized Response settleCompanyInvoices(@RequestBody CompanyInvoiceSettlementReqDto reqDto){
		try {
			Response response =  iCompanyInvoiceCreationService.settleCompanyInvoice(reqDto);
			if(response.getCode().equals(SUCCESS_CODE)){
				iCompanyInvoiceCreationService.updateBillStatus(reqDto);
			}
			return response;
		}catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	/**
	 * TO GET INVOICE LIST BY BILL ID FOR ADDENDUM BILL
	 * @param reqDto
	 * @return
	 */
	@RequestMapping(value = "/companyinvoices/paymentRecieptByBillId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response paymentRecieptByBillId(@RequestBody PaymentInvoicesDetailsDto reqDto){
		try {
			return iPaymentRecieptDetailsService.paymentRecieptByBillId(reqDto);
		}catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, e.getMessage(), null, null);
		}
	}
	/**
	 * TO get settle invoice details by company invoice id  
	 */
	@RequestMapping(value = "/companyinvoices/settledetails/byInvoiceId" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCompanyInvoiceSettlementDetailsByCompanyInvoiceId(@RequestBody CompanyInvoiceSettlementSearchReqDto reqDto){
		try {
			return iCompanyInvoiceCreationService.getCompanyInvoiceSettlementDetailsByCompanyInvoiceId(reqDto);
		}catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, e.getMessage(), null, null);
		}
	}
	@RequestMapping(value = "/companyinvoices/settleDetails" , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getCompanyInvoiceSettlementDetailsList(@RequestBody CompanyInvoiceSettlementSearchReqDto reqDto){
		try {
			return iCompanyInvoiceCreationService.getCompInvoiceForSettlementDetails(reqDto);
		}catch (Exception e) {
			
			throw e;
			//return new Response<>(ERROR, COMMON_ERROR_CODE, e.getMessage(), null, null);
		}
	}
	
	@RequestMapping(value="/companyInvoiceCreationListByBillId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public com.param.global.common.Response companyInvoiceCreationListByBillId(@RequestBody CompanyInvoiceCreationReqDto companyInvoiceCreationReqDto) {
		try {
			return iCompanyInvoiceCreationService.companyInvoiceCreationListByBillId(companyInvoiceCreationReqDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
