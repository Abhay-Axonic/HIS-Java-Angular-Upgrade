package com.param.global.service;

import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.PaymentEntitlementMasterDto;

@SuppressWarnings("rawtypes")
@Transactional
public interface IPaymentEntitlementMasterService {


	Response getPaymentEntitlementMaster(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;
	
	Response getPaymentEntitlementListByOrgId(int orgId) throws ApplicationException;
	
	Response getPaymententitlementById(int orgId, int paymentEntId)throws ApplicationException;
	
	Response getPaymententitlementList(PaymentEntitlementMasterDto paymentEntitlementMasterDto)throws ApplicationException;
	
	Response savePaymententitlement(PaymentEntitlementMasterDto paymentEntitlementMasterDto)throws ApplicationException;
	
	Response updatePaymententitlement(PaymentEntitlementMasterDto paymentEntitlementMasterDto)throws ApplicationException;
	
	Response updatePaymententitlementStatus(PaymentEntitlementMasterDto paymentEntitlementMasterDto)throws ApplicationException;
	
	Response getPaymententitlementCount(PaymentEntitlementMasterDto paymentEntitlementMasterDto)throws ApplicationException;
}
