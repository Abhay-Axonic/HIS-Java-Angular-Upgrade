package com.param.billing.global.transaction.service;

import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.transaction.dto.CreditNoteDetailsDto;
import com.param.billing.global.transaction.dto.CreditNoteMasterDto;
import com.param.billing.global.transaction.model.CreditNoteMaster;
import com.param.global.common.Response;
import com.param.global.dto.OrderMasterDto;

import in.param.exception.ApplicationException;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface ICreditNoteMasterService {
	
	public Response saveCreditNoteAgainstService(CreditNoteMasterDto creditNoteMasterDto)throws ApplicationException;
	public Response saveCreditNoteAgainstDeposit(CreditNoteMasterDto creditNoteMasterDto)throws ApplicationException;
	public Response saveCreditNote(CreditNoteMasterDto dto)throws ApplicationException;

	public Response creditNoteDiscountDetails(int billingDetailsId)throws ApplicationException;
	
	public Response getCreditNoteListForRefund(RefundCnSearchDto reqDto)throws ApplicationException;
	public Response updateDueAmountByCreditNoteId(int creditNoteId,double crDeuAmount, String receivedFrom);
	public Response getCreditNotesByBillForAddendum(int billId)throws ApplicationException;
	public Response cancelCreditNote(Integer creditNoteId)throws ApplicationException;
	public Response checkPatientOutstanding(Integer patientId)throws ApplicationException;
	public Response updateCrPayeeDetails(CreditNoteMaster master, boolean addSubtract)throws ApplicationException;
	void applyCreditNoteOnBill(CreditNoteMaster creditNoteMaster, Integer selfPayeeId) throws ApplicationException;
	public Response getEncounterAndAdmissionDetails(OrderMasterDto reqDto)throws ApplicationException;
	public Response getCreditNoteDetailsByCreditNoteId(CreditNoteDetailsDto creditNoteDetailsDto)throws ApplicationException;

	void inActiveOrderDetails(List<Integer> orderIds)throws ApplicationException;

	void inActiveOrderDetailsForWeb(Integer orderIds)throws ApplicationException;
}
