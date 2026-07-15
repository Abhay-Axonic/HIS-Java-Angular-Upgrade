package com.param.global.billing.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.PaymentEntitlementMasterDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings("rawtypes")
public interface IPaymentModeMasterService {

	
	Response savePaymentModeMaster(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;

	Response getPaymentModeMasterById(int payModeId, int orgId) throws ApplicationException;

	Response getPaymentModeMasterList(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException; 

	Response updatePaymentModeMaster(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;

	Response updatePaymentModeMasterStatus(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;

	Response getPaymentModeMasterCount(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;

	Response getActivePaymentModeMasterList(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;

}
