package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ILoyaltyCardConfigurationDao;
import com.param.global.dto.LoyaltyCardConfigurationDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class LoyaltyCardConfigurationServiceImpl implements ILoyaltyCardConfigurationService, ICommonConstants{
	final static Logger logger = Logger.getLogger(LoyaltyCardConfigurationServiceImpl.class);


	@Autowired
	ILoyaltyCardConfigurationDao iLoyaltyCardConfigurationDao;
	
	@Override
	public Response saveLoyaltyCardConfiguration(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException {
		
		Response res=iLoyaltyCardConfigurationDao.checkLoyaltyCardConfigurationAvailable(loyaltyCardConfigurationDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST_LOYALTY, null, null);
			}else{
				return iLoyaltyCardConfigurationDao.saveLoyaltyCardConfiguration(loyaltyCardConfigurationDto);
			}

		} catch (Exception e) {
		logger.error("saveLoyaltyCardConfiguration failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateLoyaltyCardConfigurationStatus(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException{
		try {
			
				return iLoyaltyCardConfigurationDao.updateLoyaltyCardConfigurationStatus(loyaltyCardConfigurationDto);
		
		} catch (Exception e) {
		logger.error("updateLoyaltyCardConfigurationStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getLoyaltyCardConfigurationById(Integer cabinId, Integer organizationId, Integer unitId) throws ApplicationException {
			try {
				
				return iLoyaltyCardConfigurationDao.getLoyaltyCardConfigurationById(cabinId, organizationId, unitId);
		
		} catch (Exception e) {
		logger.error("getLoyaltyCardConfigurationById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getLoyaltyCardConfigurationList(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException{
			try {
				
				return iLoyaltyCardConfigurationDao.getLoyaltyCardConfigurationList(loyaltyCardConfigurationDto);
		
		} catch (Exception e) {
		logger.error("getLoyaltyCardConfigurationList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getLoyaltyCardConfigurationCount(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException{
			try {
				
				return iLoyaltyCardConfigurationDao.getLoyaltyCardConfigurationCount(loyaltyCardConfigurationDto);
		
		} catch (Exception e) {
		logger.error("getLoyaltyCardConfigurationCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	public Response checkLoyaltyCardConfigurationAvailable(LoyaltyCardConfigurationDto loyaltyCardConfigurationDto) throws ApplicationException{
		try {
			
			return iLoyaltyCardConfigurationDao.checkLoyaltyCardConfigurationAvailable(loyaltyCardConfigurationDto);
	
	} catch (Exception e) {
	logger.error("checkLoyaltyCardConfigurationAvailable failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}


}
