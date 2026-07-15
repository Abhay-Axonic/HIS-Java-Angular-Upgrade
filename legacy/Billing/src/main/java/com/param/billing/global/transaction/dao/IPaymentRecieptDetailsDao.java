package com.param.billing.global.transaction.dao;

import java.util.List;

import com.param.billing.global.transaction.dto.PaymentInvoicesDetailsDto;
import com.param.billing.global.transaction.model.PaymentReceiptDetails;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPaymentRecieptDetailsDao extends IGenericDao<PaymentReceiptDetails, Integer>{
	
	public Response savePaymentRecieptDetails(PaymentReceiptDetailsDto paymentReceiptDetailsDto)throws ApplicationException;
	
	public Response paymentRecieptByBillId(PaymentInvoicesDetailsDto paymentInvoicesDetailsDto)throws ApplicationException;
	
	public Response getPaymentReceieptDetailsForAddendumByBill(int billId)throws ApplicationException;

	List<PaymentReceiptDetails> getPaymentReceieptDetailsForBillCancellation(int payemntRecieptId) throws ApplicationException;

	public Response inactiveRecieptDetails(PaymentReceiptDetails receiptDetails);
}
