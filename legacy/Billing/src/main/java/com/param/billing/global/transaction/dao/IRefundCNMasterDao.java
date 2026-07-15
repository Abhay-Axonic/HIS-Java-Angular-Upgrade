package com.param.billing.global.transaction.dao;

import java.math.BigInteger;

import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.transaction.dto.RefundCNMasterDto;
import com.param.billing.global.transaction.model.RefundCNMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IRefundCNMasterDao extends IGenericDao<RefundCNMaster, Integer>{

	public Response saveRefundCreditNote(RefundCNMasterDto refundCNMasterDto)throws BillingException;
	public Response updateRefundCreditNote(RefundCNMasterDto refundCNMasterDto)throws BillingException;
	public BigInteger generateRefundCreditNoteNo()throws BillingException;
	public Response searchRefundCn(RefundCnSearchDto reqDto)throws BillingException;
	

}
