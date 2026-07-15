package com.param.global.billing.dao;

import com.param.billing.global.model.PaymentDepositDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dto.PaymentDepositDetailsDto;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IPaymentDepositDetailsDao extends IGenericDao<PaymentDepositDetails, Integer>{
	public Response savePaymentDepositDetails(PaymentDepositDetailsDto dto)throws BillingException;

	public Response updateDepositDetails(PaymentDepositDetailsDto paymentDepositDetailsDto)throws BillingException;
}
