package com.param.billing.global.transaction.dao;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDocShareVoucherDto;
import com.param.billing.global.transaction.model.TDocShareVoucher;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IDocShareVoucherDao extends IGenericDao<TDocShareVoucher, Integer>{
	public Response saveDocShareVoucher(TDocShareVoucherDto masterDto)throws ServiceException;
	public Response updateDocShareVoucher(TDocShareVoucherDto masterDto)throws ServiceException;
	
}
