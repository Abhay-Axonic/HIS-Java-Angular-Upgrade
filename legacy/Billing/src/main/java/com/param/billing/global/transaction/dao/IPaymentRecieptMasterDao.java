package com.param.billing.global.transaction.dao;


import com.param.billing.global.transaction.model.PaymentRecieptMaster;
import com.param.global.billing.dto.PaymentRecieptMasterDto;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPaymentRecieptMasterDao extends IGenericDao<PaymentRecieptMaster, Integer>{
	
	public Response savePaymentRecieptMaster(PaymentRecieptMasterDto paymentRecieptMasterDto)throws ApplicationException;
	public Response getPaymentRecieptsByBillId(int billId)throws ApplicationException;
	public Response updatePaymentReceiptAsCancelById(PaymentRecieptMasterDto dto)throws ApplicationException;
	public Long getNextValRecieptMasterId()throws ApplicationException;
}

