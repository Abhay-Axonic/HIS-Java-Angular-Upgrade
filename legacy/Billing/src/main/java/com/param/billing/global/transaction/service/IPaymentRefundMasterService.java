package com.param.billing.global.transaction.service;

import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.dto.RefundCollectionReqDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IPaymentRefundMasterService {

	public Response saveRefundAgainstDeposit(RefundCollectionReqDto refundCollectionReqDto,int depositId,int tRefundCnId)throws BillingException; 
	public Response saveRefundAgainstCreditNote(RefundCollectionReqDto refundCollectionReqDto,int billMasterId,int billPayeeId)throws BillingException; 
	public Response saveRefundAgainstPharamacy(RefundCollectionReqDto refundCollectionReqDto)throws BillingException; 
	
	public Response searchRefundMaster(RefundCnSearchDto reqDto)throws BillingException; 
	public Response getRefundDetailsByRefundId(int orgId,int unitId, int paymentRefundVoucherId)throws BillingException; 
	
	public Response searchRefundedCreditNote(RefundCnSearchDto reqDto)throws BillingException; 
	public Response searchRefundedCreditNoteDetails(int orgId,int unitId, int paymentRefundVoucherId)throws BillingException; 
	
	public Response getRefundBankDetails(int orgId,int unitId, int tRefundCnId)throws BillingException; 
}
