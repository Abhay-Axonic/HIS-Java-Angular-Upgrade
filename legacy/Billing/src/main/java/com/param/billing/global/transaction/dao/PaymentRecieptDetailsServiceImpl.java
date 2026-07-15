package com.param.billing.global.transaction.dao;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.transaction.dto.PaymentInvoicesDetailsDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class PaymentRecieptDetailsServiceImpl implements IPaymentRecieptDetailsService,ICommonConstants{

	@Autowired
	IPaymentRecieptDetailsDao iPaymentRecieptDetailsDao; 
	
	@Override
	public Response savePaymentRecieptDetails(PaymentReceiptDetailsDto paymentReceiptDetailsDto)
			throws ApplicationException {
		try{
			return iPaymentRecieptDetailsDao.savePaymentRecieptDetails(paymentReceiptDetailsDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(SUCCESS,SUCCESS_CODE,SUCCESS_MESSAGE , null, null);
	}

	@Override
	@Transactional
	public Response paymentRecieptByBillId(PaymentInvoicesDetailsDto paymentInvoicesDetailsDto)
			throws ApplicationException {
		try{
			return iPaymentRecieptDetailsDao.paymentRecieptByBillId(paymentInvoicesDetailsDto);
		}catch(Exception e){
			
			throw e;
		}
		//return new Response<>(SUCCESS,SUCCESS_CODE,SUCCESS , null, null);
	}

}
