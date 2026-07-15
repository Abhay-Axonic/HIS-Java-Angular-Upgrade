package com.param.billing.global.transaction.dao;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDocSharePaymentVoucherLoggerDto;
import com.param.billing.global.transaction.dto.TDoctorShareLoggerReqDto;
import com.param.billing.global.transaction.model.TDocSharePaymentVoucherLogger;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IDocShareVoucherLoggerDao extends IGenericDao<TDocSharePaymentVoucherLogger, Integer>{
	
	public Response saveDoctorSharePaymentVoucherList(TDocSharePaymentVoucherLoggerDto reqDto) throws ApplicationException;

	Response saveDoctorSharePaymentVoucher(TDoctorShareLoggerReqDto masterDto) throws ServiceException;
}
