package com.param.billing.global.transaction.service;

import com.param.billing.global.dto.BillPaymentCollectionReqDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;
import com.param.global.dto.BillPayeeDetailsDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBillPayeeDetailsService {
	public Response saveBillPayeeDetails(BillPayeeDetailsDto billPayeeDetailsDto)throws ApplicationException;
	public Response getBillPayeeDetailsByBillId(int billId)throws BillingException;
	public Response getBillPayeeDetailsByBillIdCr(int billId,int visitTypeId,int admissionId)throws BillingException;
	public Response updateCrAmtByPayeeAndBillId(int billId, int payeeId, double crAmount)throws BillingException;
	public Response getDebitNoteDetailsByBillMasterId(int billId)throws BillingException;
	public Response getBillPharmacyPayeeByBillIdCreditNote(int saleId)throws BillingException;
	public Response cancelPharmacyReceipt(BillPaymentCollectionReqDto dto)throws BillingException;
}

