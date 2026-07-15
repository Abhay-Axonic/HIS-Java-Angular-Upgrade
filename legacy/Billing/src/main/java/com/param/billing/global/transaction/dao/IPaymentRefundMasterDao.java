package com.param.billing.global.transaction.dao;

import java.math.BigInteger;

import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.dto.RefundCollectionReqDto;
import com.param.billing.global.transaction.dto.PaymentRefundMasterDto;
import com.param.billing.global.transaction.model.PaymentRefundMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IPaymentRefundMasterDao extends IGenericDao<PaymentRefundMaster, Integer>{

	public Response savePaymentRefundMasterDto(PaymentRefundMasterDto dto)throws BillingException;
	public BigInteger generateRefundVoucherNo()throws BillingException;
	public Response searchRefundMaster(RefundCnSearchDto reqDto)throws BillingException; 
	public Response getRefundDetailsByRefundId(int orgId,int unitId, int paymentRefundVoucherId)throws BillingException;
	public Response searchRefundedCreditNote(RefundCnSearchDto reqDto)throws BillingException; 
	public Response searchRefundedCreditNoteDetails(int orgId,int unitId, int paymentRefundVoucherId)throws BillingException; 
	public Response getRefundBankDetails(int orgId,int unitId, int tRefundCnId)throws BillingException;
	public Response checkIsDoctroSharePosted(Integer creditNoteId);

	public Response checkExistingRefundApproval(RefundCollectionReqDto refundCollectionReqDto, int depositId, int tRefundCnId) throws BillingException;
}
