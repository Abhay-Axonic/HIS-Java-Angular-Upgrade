package com.param.global.billing.dao;

import com.param.global.common.Response;
import com.param.global.dto.PaymentEntitlementMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPaymentModeMasterDao {
	
	
	Response savePaymentModeMaster(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;

	Response getPaymentModeMasterById(int payModeId, int orgId) throws ApplicationException;

	Response getPaymentModeMasterList(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException; 

	Response updatePaymentModeMaster(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;

	Response updatePaymentModeMasterStatus(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;

	Response getPaymentModeMasterCount(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;

	Response getActivePaymentModeMasterList(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;
	
	Response getPaymentModeMasterListByName(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;
	
	Response getPaymentModeMasterListByNameNotById(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException; 

}
