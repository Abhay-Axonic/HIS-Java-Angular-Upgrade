package com.param.global.otims.service;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dao.IResourceChargeConfigurationDao;
import com.param.global.otims.dto.ResourceChargeConfigurationDto;

import in.param.exception.ApplicationException;

@Service
@Transactional
@SuppressWarnings({"rawtypes","unchecked"})
public class ResourceChargeConfigurationServiceImpl implements IResourceChargeConfigurationService,ICommonConstants{

	final static Logger logger = Logger.getLogger(ResourceChargeConfigurationServiceImpl.class);

	@Autowired
	IResourceChargeConfigurationDao iResourceChargeConfigurationDao;
	
	@Override
	public Response saveResourceChargeConfiguration(ResourceChargeConfigurationDto resourceChargeConfigurationDto)
			throws ApplicationException {
		try{
			Character res = iResourceChargeConfigurationDao.isResourceChargeConfigurationAlreadyExist(resourceChargeConfigurationDto);
			if(res.equals("Y")){
				return new Response(ERROR,ERROR_CODE,"Already Exist !!",null,null);
			}else{
				return iResourceChargeConfigurationDao.saveResourceChargeConfiguration(resourceChargeConfigurationDto);
			}
		}catch(Exception e){
			logger.error("saveResourceChargeConfiguration failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateResourceChargeConfiguration(ResourceChargeConfigurationDto resourceChargeConfigurationDto)
			throws ApplicationException {
		try{
			Character res = iResourceChargeConfigurationDao.isResourceChargeConfigurationAlreadyExistNotById(resourceChargeConfigurationDto);
			if(res.equals("Y")){
				return new Response(ERROR,ERROR_CODE,"Already Exist !!",null,null);
			}else{
				return iResourceChargeConfigurationDao.updateResourceChargeConfiguration(resourceChargeConfigurationDto);
			}
		}catch(Exception e){
			logger.error("updateResourceChargeConfiguration failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getResourceChargeConfigurationById(Integer resourceChargeConfigurationId)
			throws ApplicationException {
		try{
			return iResourceChargeConfigurationDao.getResourceChargeConfigurationById(resourceChargeConfigurationId);
		}catch(Exception e){
			logger.error("getResourceChargeConfigurationById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getResourceChargeConfigurationList(Integer orgId, Integer unitId) throws ApplicationException {
		try{
			return iResourceChargeConfigurationDao.getResourceChargeConfigurationList(orgId,unitId);
		}catch(Exception e){
			logger.error("getResourceChargeConfigurationList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateResourceChargeConfigurationStatus(
			ResourceChargeConfigurationDto resourceChargeConfigurationDto) throws ApplicationException {
		try{
			return iResourceChargeConfigurationDao.updateResourceChargeConfigurationStatus(resourceChargeConfigurationDto);
		}catch(Exception e){
			logger.error("updateResourceChargeConfigurationStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getResourceChareges(ResourceChargeConfigurationDto resourceChargeConfigurationDto)
			throws ApplicationException {
		try{
			return iResourceChargeConfigurationDao.getResourceChareges(resourceChargeConfigurationDto);
		}catch(Exception e){
			logger.error("getResourceChareges failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	/*@Override
	public Response getActiveResourceChargeConfigurationList(Integer orgId, Integer unitId)
			throws ApplicationException {
		try{
			
		}catch(Exception e){
			
		}
		return new Response(ERROR,ERROR_CODE,COMMON_ERROR,null,null);
	}*/

}
