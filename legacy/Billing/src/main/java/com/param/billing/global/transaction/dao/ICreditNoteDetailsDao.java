package com.param.billing.global.transaction.dao;

import java.util.List;

import com.param.billing.global.transaction.dto.CreditNoteDetailsDto;
import com.param.billing.global.transaction.dto.TDoctorShareRespDto;
import com.param.billing.global.transaction.model.CreditNoteDetails;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ICreditNoteDetailsDao extends IGenericDao<CreditNoteDetails, Integer>{
	public Response saveCreditNoteDetails(CreditNoteDetailsDto creditNoteDetailsDto)throws BillingException;
	List<TDoctorShareRespDto> getCrDetailsOrderDetailsId(Integer orderDetailsId);
}
