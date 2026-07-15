package com.param.billing.global.transaction.dao;

import com.param.billing.global.transaction.dto.PaymentRefundDetailsDto;
import com.param.billing.global.transaction.model.PaymentRefundDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IPaymentRefundDetailsDao extends IGenericDao<PaymentRefundDetails, Integer>
{
	public Response savePaymentRefundDetails(PaymentRefundDetailsDto dto)throws BillingException;
}

