package com.param.billing.global.transaction.dao;

import com.param.billing.global.transaction.dto.CreditNoteDiscountDetailsDto;
import com.param.billing.global.transaction.model.CreditNoteDiscoutDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICreditNoteDiscountDetailsDao extends IGenericDao<CreditNoteDiscoutDetails, Integer> {

	public Response saveCreditNoteDiscountDetails(CreditNoteDiscountDetailsDto creditNoteDiscountDetailsDto)throws ApplicationException;
	public Response creditNoteDiscountDetails(int billingDetailsId)throws BillingException;
}
