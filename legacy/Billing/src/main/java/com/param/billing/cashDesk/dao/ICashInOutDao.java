package com.param.billing.cashDesk.dao;

import com.param.billing.cashDesk.dto.CashHandoverDetailsDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ICashInOutDao {

	Response saveCashInOut(CashHandoverDetailsDto cashHandoverDetailsDto)throws ApplicationException;

	Response getcashInOutList(CashHandoverDetailsDto cashHandoverDetailsDto)throws ApplicationException;

	Response getUserInhandCash(CashHandoverDetailsDto cashHandoverDetailsDto)throws ApplicationException;

}
