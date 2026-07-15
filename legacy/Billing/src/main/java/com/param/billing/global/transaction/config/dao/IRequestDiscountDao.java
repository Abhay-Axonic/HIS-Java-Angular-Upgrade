package com.param.billing.global.transaction.config.dao;

import java.util.List;

import com.param.billing.global.transaction.model.RequestDiscount;
import com.param.global.billing.dto.RequestDiscountDetailsDto;
import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IRequestDiscountDao{

	Response requestDiscount(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response getRequestedDiscount(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response requestDiscountDetails(RequestDiscountDetailsDto requestDiscountDetailsDto) throws ApplicationException;

	Response getRequestDetails(Integer reqDiscId)throws ApplicationException;

	Response approveRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response approveRequestDetails(RequestDiscountDetailsDto detailsDto)throws ApplicationException;

	Response cancelRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response getDiscountRequestForOPBilling(RequestDiscountDto requestDiscountDto) throws ApplicationException;

	Response getRequestedDiscountsForDashboard(RequestDiscountDto requestDiscountDto) throws ApplicationException;

	Response updateBillIdToRequestDiscountDetails(Integer billMasterId, Integer reqDiscId)throws ApplicationException;

	Response approveRequestForFinancialClearance(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response updateDischargeStatusAfterFinancialClearance(RequestDiscountDto requestDiscountDto)
			throws ApplicationException;

	Response updateAdmissionsIsFinancialClearance(Integer admissionId) throws ApplicationException;
	
	Response approveCnRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;
	
	Response updateCnRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response approveAndApply(Integer ReqDicId) throws ApplicationException;
	
	Response updateRefundRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;
	
	Response completeCreditNoteDiscountRequest(Integer reqDiscId)throws ApplicationException;
	
	Response approvedWithoutBill(Integer requestDiscountId)throws ApplicationException;

	Response getBadDebitRequestObject(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response getCancelDiscountReqByBillId(Integer billId) throws ApplicationException;

	Response updateEncounterIsFinancialClearance(Integer encounterId) throws ApplicationException;

	Response getUHIDMergeReuestApprovalData(RequestDiscountDto requestDiscountDto);

	Response cancelDiscountRequestOnCreditNoteCancellation(Integer creditNoteId) throws ApplicationException;

	Response updateExpenseRemarkRequest(RequestDiscountDto requestDiscountDto);

	Response getOrderDetailsIdByCreditNoteId(RequestDiscountDto requestDiscountDto) throws ApplicationException;

	Response cancelRequestFinancialRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response requestDiscountBillList(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response updateEncounterMasterStatus(RequestDiscountDto requestDiscountDto)throws ApplicationException;

}
