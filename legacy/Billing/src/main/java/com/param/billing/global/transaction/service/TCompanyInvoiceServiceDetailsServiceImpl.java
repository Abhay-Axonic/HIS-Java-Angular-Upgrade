package com.param.billing.global.transaction.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.param.exception.ApplicationException;

import com.param.billing.global.transaction.config.dto.TCompanyInvoiceServiceDetailsDto;
import com.param.billing.global.transaction.dao.ITCompanyInvoiceServiceDetailsDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@Service
public class TCompanyInvoiceServiceDetailsServiceImpl implements ITCompanyInvoiceServiceDetailsService , ICommonConstants {

	@Autowired
	ITCompanyInvoiceServiceDetailsDao iTCompanyInvoiceServiceDetailsDao;
	
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public Response getCompInvServDetailsByInvDtls(TCompanyInvoiceServiceDetailsDto companyInvoiceServiceDetailsDto)
			throws ApplicationException {
		try{
			return iTCompanyInvoiceServiceDetailsDao.getCompInvServDetailsByInvDtls(companyInvoiceServiceDetailsDto);
		}catch(Exception e){
			
			throw e;
		}
	}

}
