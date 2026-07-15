package com.param.billing.global.transaction.dao;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.TDocShareVoucherDtlsDto;
import com.param.billing.global.transaction.model.TDocShareVoucherDtls;
import com.param.global.common.ServiceException;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IDocShareVoucherDtlsDao extends IGenericDao<TDocShareVoucherDtls, Integer>{
	public Response saveDocShareVoucherDtls(TDocShareVoucherDtlsDto masterDto)throws ServiceException;
	
}
