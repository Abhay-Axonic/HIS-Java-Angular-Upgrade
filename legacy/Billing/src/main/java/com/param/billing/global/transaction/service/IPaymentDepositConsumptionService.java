package com.param.billing.global.transaction.service;

import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPaymentDepositConsumptionService {

	Response consumeDeposit(BillPaymentCollectionReqDto reqDto)throws ApplicationException;

}
