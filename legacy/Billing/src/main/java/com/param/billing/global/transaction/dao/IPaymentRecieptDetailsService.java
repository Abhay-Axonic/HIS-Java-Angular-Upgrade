package com.param.billing.global.transaction.dao;

import com.param.billing.global.transaction.dto.PaymentInvoicesDetailsDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPaymentRecieptDetailsService {

	Response savePaymentRecieptDetails(PaymentReceiptDetailsDto paymentReceiptDetailsDto)throws ApplicationException;
	public Response paymentRecieptByBillId(PaymentInvoicesDetailsDto paymentInvoicesDetailsDto)throws ApplicationException;
	
}
