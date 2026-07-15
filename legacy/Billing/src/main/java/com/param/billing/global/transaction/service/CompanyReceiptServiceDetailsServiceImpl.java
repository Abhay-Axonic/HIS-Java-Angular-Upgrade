package com.param.billing.global.transaction.service;


import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.common.ICommonConstants;
import com.param.billing.global.transaction.dao.ICompanyReceiptServiceDetailDao;
import com.param.billing.global.transaction.dto.CompanyReceiptServiceDetailsDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class CompanyReceiptServiceDetailsServiceImpl implements ICompanyReceiptServiceDetailService, ICommonConstants{

	@Autowired
	private ICompanyReceiptServiceDetailDao  iCompanyReceiptServiceDetailDao;
	
	

	@Override
	@Transactional
	public com.param.global.common.Response saveCompanyReceiptServiceDetail(CompanyReceiptServiceDetailsDto reqDto) throws ApplicationException {
		try {
			return iCompanyReceiptServiceDetailDao.saveCompanyReceiptServiceDetail(reqDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new com.param.global.common.Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
}
