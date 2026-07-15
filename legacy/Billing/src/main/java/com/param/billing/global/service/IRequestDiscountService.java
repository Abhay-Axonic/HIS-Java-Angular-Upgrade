package com.param.billing.global.service;

import com.param.global.billing.dto.RequestDiscountDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
public interface IRequestDiscountService {

	Response requestDiscount(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response getRequestedDiscount(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response approveRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response cancelRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response getDiscountRequestDetailsForOPBilling(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response getRequestedDiscountsForDashboard(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response approveRequestForFinancialClearance(RequestDiscountDto requestDiscountDto)throws ApplicationException;
	
	Response approveCnRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;
	
	Response approveExpenseRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;
	
	Response completeCreditNoteDiscountRequest(Integer reqDiscId)throws ApplicationException;

	Response getBadDebitRequestObject(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response approveCompanyReceiptsExpenseRequest(RequestDiscountDto requestDiscountDto);

	Response getUHIDMergeReuestApprovalData(RequestDiscountDto requestDiscountDto);

	Response cancelRequestFinancialRequest(RequestDiscountDto requestDiscountDto)throws ApplicationException;

	Response requestDiscountBillList(RequestDiscountDto requestDiscountDto)throws ApplicationException;

}
