package com.param.global.dao;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.dto.PaymentEntitlementMasterDto;
import com.param.global.model.PaymentEntitlementMaster;

@SuppressWarnings("rawtypes")
public interface IPaymentEntitlementMasterDao extends IGenericDao<PaymentEntitlementMaster, Integer>{

	public Response getPaymentEntitlementMaster(PaymentEntitlementMasterDto paymentEntitlementMasterDto) throws ApplicationException;
	
	public Response getPaymentEntitlementListByOrgId(int orgId)throws ApplicationException;
	
    Response getPaymententitlementById(int orgId, int paymentEntId)throws ApplicationException;
	
	Response getPaymententitlementList(PaymentEntitlementMasterDto paymentEntitlementMasterDto)throws ApplicationException;
	
	Response savePaymententitlement(PaymentEntitlementMasterDto paymentEntitlementMasterDto)throws ApplicationException;
	
	Response updatePaymententitlement(PaymentEntitlementMasterDto paymentEntitlementMasterDto)throws ApplicationException;
	
	Response updatePaymententitlementStatus(PaymentEntitlementMasterDto paymentEntitlementMasterDto)throws ApplicationException;
	
	Response getPaymententitlementCount(PaymentEntitlementMasterDto paymentEntitlementMasterDto)throws ApplicationException;
	
	Response getPaymententitlementByName(PaymentEntitlementMasterDto paymentEntitlementMasterDto)throws ApplicationException;
	
	Response getPaymententitlementByNameNotById(PaymentEntitlementMasterDto paymentEntitlementMasterDto)throws ApplicationException;
}
