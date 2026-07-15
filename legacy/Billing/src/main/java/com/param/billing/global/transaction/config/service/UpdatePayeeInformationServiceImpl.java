package com.param.billing.global.transaction.config.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.IError;
import com.param.billing.common.Response;
import com.param.billing.global.transaction.config.dao.IUpdatePayeeInformationDao;
import com.param.billing.global.transaction.config.dto.UpdatePayeeInformationReqDto;

import in.param.exception.ApplicationException;

@Service
@Transactional
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UpdatePayeeInformationServiceImpl implements IUpdatePayeeInformationService, ICommonConstants, IError{

	@Autowired
	IUpdatePayeeInformationDao iUpdatePayeeInformationDao;
	
	@Override
	@Transactional
	public Response getPayeeInfoByVisitTypeId(UpdatePayeeInformationReqDto reqDto) throws ApplicationException {
		try {
			return iUpdatePayeeInformationDao.getPayeeInfoByVisitTypeId(reqDto);
		}catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

}
