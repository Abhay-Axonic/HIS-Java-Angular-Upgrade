package com.param.billing.global.transaction.service;

import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

public interface IPaymentRecieptMasterService {

	public Response savePaymentReciept(PaymentRecieptMasterDto paymentRecieptMasterDto, Double totalDueAmount, double depositCounsumtionAmount)throws ApplicationException; 
	
}
