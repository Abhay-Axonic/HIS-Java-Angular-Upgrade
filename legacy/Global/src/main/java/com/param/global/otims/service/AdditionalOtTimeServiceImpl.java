package com.param.global.otims.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dao.IAdditionalOtTimeDao;
import com.param.global.otims.dto.AdditionalOtTimeDto;
import com.param.global.otims.dto.ResourceChargeConfigurationDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@Transactional
public class AdditionalOtTimeServiceImpl implements ICommonConstants,IAdditionalOtTimeService{
	final static Logger logger = Logger.getLogger(AdditionalOtTimeServiceImpl.class);


	@Autowired
	IAdditionalOtTimeDao iAdditionalOtTimeDao; 
	
	@Override
	public Response saveAdditionalOtTime(AdditionalOtTimeDto additionalOtTimeDto)
			throws ApplicationException {
		try{
			/*Character res = iAdditionalOtTimeDao.isAdditionalOtTimeExist(additionalOtTimeDto);
			if(res.equals('Y')){
				return new Response<>(ERROR,ERROR_CODE,"Already Exist !!",null,null);
			}else{*/
				return iAdditionalOtTimeDao.saveAdditionalOtTime(additionalOtTimeDto);
			//}
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	public Response updateAdditionalOtTime(AdditionalOtTimeDto additionalOtTimeDto)
			throws ApplicationException {
		try{
				Character res = iAdditionalOtTimeDao.isAdditionalOtTimeExistNotById(additionalOtTimeDto);
				if(res.equals('Y')){
					return new Response<>(ERROR,ERROR_CODE,"Already Exist !!",null,null);
				}else{
					return iAdditionalOtTimeDao.updateAdditionalOtTime(additionalOtTimeDto);
				}
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	public Response getAdditionalOtTimeList(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			return iAdditionalOtTimeDao.getAdditionalOtTimeList(orgId,unitId);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	public Response updateStatusAdditionalOtTime(AdditionalOtTimeDto additionalOtTimeDto) throws ApplicationException {
		try{
			return iAdditionalOtTimeDao.updateStatusAdditionalOtTime(additionalOtTimeDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	public Response getAddtionalRate(AdditionalOtTimeDto additionalOtTimeDto) throws ApplicationException {
		try{
			return iAdditionalOtTimeDao.getAddtionalRate(additionalOtTimeDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

}
