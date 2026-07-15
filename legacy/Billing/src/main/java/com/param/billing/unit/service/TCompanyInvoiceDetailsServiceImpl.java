package com.param.billing.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.global.transaction.dao.ITCompanyInvoiceCreationDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})
public class TCompanyInvoiceDetailsServiceImpl implements ITCompanyInvoiceDetailsService, ICommonConstants{

	@Autowired
	private ITCompanyInvoiceCreationDao iTCompanyInvoiceCreationDao;
	
	@Override
	@Transactional
	public Response getCompanyInvoiceDetailsListByInvoiceId(int invoiceId) throws ApplicationException {
		try{
			return iTCompanyInvoiceCreationDao.getCompanyInvoiceDetailsListByInvoiceId(invoiceId);
		}catch(Exception e){
			
			throw e;
		}
	}

}
