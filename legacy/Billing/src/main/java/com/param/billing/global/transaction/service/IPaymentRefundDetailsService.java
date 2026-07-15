package com.param.billing.global.transaction.service;

import com.param.billing.global.transaction.dto.PaymentRefundDetailsDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IPaymentRefundDetailsService {
	public Response savePaymentRefundDetails(PaymentRefundDetailsDto dto)throws BillingException;
	
}
