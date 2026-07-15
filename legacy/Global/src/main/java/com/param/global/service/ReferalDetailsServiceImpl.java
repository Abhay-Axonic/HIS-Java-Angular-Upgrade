package com.param.global.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IReferalDetailsDao;
import com.param.global.dto.ReferalDetailsDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class ReferalDetailsServiceImpl implements IReferalDetailsService,ICommonConstants{

	private static final Logger logger = Logger.getLogger(ReferalDetailsServiceImpl.class);

	@Autowired
	IReferalDetailsDao iReferalDetailsDao;

	@Override
	public Response saveReferalDetails(ReferalDetailsDto referalDetailsDto) throws ApplicationException {
		try {
			return iReferalDetailsDao.saveReferalDetails(referalDetailsDto);
		}catch (Exception e) {
			logger.error("saveReferalDetails failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
