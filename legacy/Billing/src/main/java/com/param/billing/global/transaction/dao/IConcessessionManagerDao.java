package com.param.billing.global.transaction.dao;

import java.util.List;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.model.BillPayeeDetails;
import com.param.billing.global.transaction.model.BillingDetails;
import com.param.billing.global.transaction.model.BillingMaster;
import com.param.global.billing.dto.BillingDetailsDto;

import in.param.exception.ApplicationException;

public interface IConcessessionManagerDao {

	public Response saveConcessessionManagerLog(BillingMaster billObj, List<BillingDetails> billingDetailsList,List<BillPayeeDetails> billPayeeDetailsList);
	
}
