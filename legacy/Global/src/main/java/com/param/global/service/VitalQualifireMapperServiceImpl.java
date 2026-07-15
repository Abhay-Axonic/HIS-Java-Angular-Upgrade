package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IVitalQualifireMapperDao;
import com.param.global.dto.VitalQualifireMapperDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class VitalQualifireMapperServiceImpl implements IVitalQualifireMapperService, ICommonConstants{
	final static Logger logger = Logger.getLogger(VitalQualifireMapperServiceImpl.class);


	@Autowired
	IVitalQualifireMapperDao iVitalQualifireMapperDao;
	
	@Override
	public Response saveVitalQualifireMapper(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException {
		Response res=iVitalQualifireMapperDao.getVitalQualifireMapperByNameNotId(vitalQualifireMapperDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iVitalQualifireMapperDao.saveVitalQualifireMapper(vitalQualifireMapperDto);
			}

		} catch (Exception e) {
		logger.error("saveVitalQualifireMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVitalQualifireMapper(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException{
//		Response res=iVitalQualifireMapperDao.getVitalQualifireMapperByNameNotId(vitalQualifireMapperDto);
		try {
//			if(res.getListObject()!=null && res.getListObject().size()>0){
//				
//				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
//			}else{
				return iVitalQualifireMapperDao.updateVitalQualifireMapper(vitalQualifireMapperDto);
//			}

		} catch (Exception e) {
		logger.error("updateVitalQualifireMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVitalQualifireMapperStatus(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException{
		try {
			
				return iVitalQualifireMapperDao.updateVitalQualifireMapperStatus(vitalQualifireMapperDto);
		
		} catch (Exception e) {
		logger.error("updateVitalQualifireMapperStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	/*@Override
	public Response getVitalQualifireMapperById(Integer cabinId, Integer organizationId) throws ApplicationException {
			try {
				
				return iVitalQualifireMapperDao.getVitalQualifireMapperById(cabinId, organizationId);
		
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}*/

	@Override
	public Response getVitalQualifireMapperList(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException{
			try {
				
				return iVitalQualifireMapperDao.getVitalQualifireMapperList(vitalQualifireMapperDto);
		
		} catch (Exception e) {
		logger.error("getVitalQualifireMapperList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireMapperCount(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException{
			try {
				
				return iVitalQualifireMapperDao.getVitalQualifireMapperCount(vitalQualifireMapperDto);
		
		} catch (Exception e) {
		logger.error("getVitalQualifireMapperCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveVitalQualifireMapper(Integer organizationId) throws ApplicationException{
			try {
				
				return iVitalQualifireMapperDao.getActiveVitalQualifireMapper(organizationId);
		
		} catch (Exception e) {
		logger.error("getActiveVitalQualifireMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getVitalQualifireMapperByNameNotId(VitalQualifireMapperDto vitalQualifireMapperDto) throws ApplicationException{
			try {
				
				return iVitalQualifireMapperDao.getVitalQualifireMapperByNameNotId(vitalQualifireMapperDto);
		
		} catch (Exception e) {
		logger.error("getVitalQualifireMapperByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireListByCategoryId(VitalQualifireMapperDto vitalQualifireMapperDto)
			throws ApplicationException {
		try {
			
			return iVitalQualifireMapperDao.getVitalQualifireListByCategoryId(vitalQualifireMapperDto);
	
	} catch (Exception e) {
	logger.error("getVitalQualifireListByCategoryId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getVitalQualifireListByVitalId(VitalQualifireMapperDto vitalQualifireMapperDto)
			throws ApplicationException {
		try {
			
			return iVitalQualifireMapperDao.getVitalQualifireListByVitalId(vitalQualifireMapperDto);
	
	} catch (Exception e) {
	logger.error("getVitalQualifireListByVitalId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireMapperListById(VitalQualifireMapperDto vitalQualifireMapperDto)
			throws ApplicationException {
	try {
				
				return iVitalQualifireMapperDao.getVitalQualifireMapperListById(vitalQualifireMapperDto);
		
		} catch (Exception e) {
		logger.error("getVitalQualifireMapperListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
