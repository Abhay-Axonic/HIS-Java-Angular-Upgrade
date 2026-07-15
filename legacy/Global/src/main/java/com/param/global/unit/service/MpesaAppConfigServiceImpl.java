package com.param.global.unit.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.IMpesaAppConfigDao;
import com.param.global.unit.dto.MpesaApplicationConfigurationDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})
public class MpesaAppConfigServiceImpl implements IMpesaAppConfigService, ICommonConstants{

	final static Logger logger = Logger.getLogger(MpesaAppConfigServiceImpl.class);

	@Autowired
	IMpesaAppConfigDao iMpesaAppConfigDao;

	@Override
	public Response saveMpesaAppConfig(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto)
			throws ApplicationException {
		try {
			return iMpesaAppConfigDao.saveMpesaAppConfig(mpesaApplicationConfigurationDto);

		} catch (Exception e) {
			logger.error("saveMpesaAppConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateMpesaAppConfigStatus(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto)
			throws ApplicationException {
		try {
			return iMpesaAppConfigDao.updateMpesaAppConfigStatus(mpesaApplicationConfigurationDto);

		} catch (Exception e) {
			logger.error("updateMpesaAppConfigStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getMpesaAppConfig(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto)
			throws ApplicationException {
		try {
			return iMpesaAppConfigDao.getMpesaAppConfig(mpesaApplicationConfigurationDto);

		} catch (Exception e) {
			logger.error("getMpesaAppConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getMpesaAppConfigList(MpesaApplicationConfigurationDto mpesaApplicationConfigurationDto)
			throws ApplicationException {
		try {
			return iMpesaAppConfigDao.getMpesaAppConfigList(mpesaApplicationConfigurationDto);

		} catch (Exception e) {
			logger.error("getMpesaAppConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}



}
