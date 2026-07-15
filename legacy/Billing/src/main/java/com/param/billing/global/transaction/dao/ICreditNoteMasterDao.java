package com.param.billing.global.transaction.dao;

import java.math.BigInteger;
import java.util.List;

import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.transaction.dto.CreditNoteDetailsDto;
import com.param.billing.global.transaction.dto.CreditNoteMasterDto;
import com.param.billing.global.transaction.model.BillPayeeDetails;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.billing.global.transaction.model.CreditNoteMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ICreditNoteMasterDao extends IGenericDao<CreditNoteMaster, Integer> {
	public Response saveCreditNote(CreditNoteMasterDto creditNoteMasterDto)throws ApplicationException;
	public BigInteger generateCreditNoteNumber()throws BillingException;
	public Response getCreditNoteListForRefund(RefundCnSearchDto reqDto)throws ApplicationException;
	public Response updateDueAmountByCreditNoteId(int creditNoteId,double crDeuAmount, String receivedFrom);
	public Response getCreditNotesByBillForAddendum(int billId)throws ApplicationException;
	public Response checkCreditNotePeningAgainstBill(Integer billId) throws ApplicationException;
	public Response checkFinalBillAgaistAdmission(Integer admissionId) throws ApplicationException;
	public Response cancelCreditNote(Integer creditNoteId,Integer billId)throws ApplicationException;
	public Response checkPatientOutstanding(Integer patientId)throws ApplicationException;
	public List<CreditNoteDetailsDto> getCreditNoteDetailsByCreditNoteId(Integer creditNoteId);
	BillPayeeDetails getBillDetailsObjByBillPayeeDetailsId(Integer billPayeeDetailsId) throws ApplicationException;
	public Double getRoundOffByBillId(Integer billId)throws ApplicationException;
	Response updateCrPayeeDetails(Integer billId, Integer billPayeeDetailsId,
			Double amountToBeSubtractedFromBillOutstanding, boolean addSubtract, boolean isSelfDueClear, CreditNoteMaster master) throws ApplicationException;
	BillingMaster getBillByBillIdId(Integer billId) throws ApplicationException;
	CreditNoteMaster getCreditNoteById(Integer creditNoteId) throws ApplicationException;
	public Response getCreditNoteDetailsByCreditNoteId(CreditNoteDetailsDto creditNoteDetailsDto)throws ApplicationException;

    void inActiveOrderDetails(List<Integer> orderIds);

    void inActiveOrderDetailsForWeb(Integer orderIds);
}
