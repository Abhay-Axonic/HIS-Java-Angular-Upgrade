package com.param.billing.global.transaction.service;

import com.param.billing.global.dto.RefundCnSearchDto;
import com.param.billing.global.transaction.dto.RefundCNMasterDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IRefundCNMasterService {
	
	public Response saveRefundCreditNote(RefundCNMasterDto refundCNMasterDto)throws BillingException;
	
	public Response searchRefundCn(RefundCnSearchDto reqDto)throws BillingException;
	

}
