package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IVitalQualifireMasterDao;
import com.param.global.dto.VitalQualifireMasterDto;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class VitalQualifireMasterServiceImpl implements IVitalQualifireMasterService, ICommonConstants{
	final static Logger logger = Logger.getLogger(VitalQualifireMasterServiceImpl.class);


	@Autowired
	IVitalQualifireMasterDao iVitalQualifireMasterDao;
	
	@Override
	public Response saveVitalQualifireMaster(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException {
		Response res=iVitalQualifireMasterDao.getVitalQualifireMasterByName(vitalQualifireMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iVitalQualifireMasterDao.saveVitalQualifireMaster(vitalQualifireMasterDto);
			}

		} catch (Exception e) {
		logger.error("saveVitalQualifireMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVitalQualifireMaster(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
		Response res=iVitalQualifireMasterDao.getVitalQualifireMasterByNameNotId(vitalQualifireMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iVitalQualifireMasterDao.updateVitalQualifireMaster(vitalQualifireMasterDto);
			}

		} catch (Exception e) {
		logger.error("updateVitalQualifireMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateVitalQualifireMasterStatus(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
		try {
			
				return iVitalQualifireMasterDao.updateVitalQualifireMasterStatus(vitalQualifireMasterDto);
		
		} catch (Exception e) {
		logger.error("updateVitalQualifireMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireMasterById(Integer cabinId, Integer organizationId) throws ApplicationException {
			try {
				
				return iVitalQualifireMasterDao.getVitalQualifireMasterById(cabinId, organizationId);
		
		} catch (Exception e) {
		logger.error("getVitalQualifireMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireMasterList(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
			try {
				
				return iVitalQualifireMasterDao.getVitalQualifireMasterList(vitalQualifireMasterDto);
		
		} catch (Exception e) {
		logger.error("getVitalQualifireMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireMasterCount(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
			try {
				
				return iVitalQualifireMasterDao.getVitalQualifireMasterCount(vitalQualifireMasterDto);
		
		} catch (Exception e) {
		logger.error("getVitalQualifireMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveVitalQualifireMaster(Integer organizationId) throws ApplicationException{
			try {
				
				return iVitalQualifireMasterDao.getActiveVitalQualifireMaster(organizationId);
		
		} catch (Exception e) {
		logger.error("getActiveVitalQualifireMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireMasterByName(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
			try {
				
				return iVitalQualifireMasterDao.getVitalQualifireMasterByName(vitalQualifireMasterDto);
		
		} catch (Exception e) {
		logger.error("getVitalQualifireMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalQualifireMasterByNameNotId(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException{
			try {
				
				return iVitalQualifireMasterDao.getVitalQualifireMasterByNameNotId(vitalQualifireMasterDto);
		
		} catch (Exception e) {
		logger.error("getVitalQualifireMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getVitalCategoryList(VitalQualifireMasterDto vitalQualifireMasterDto) throws ApplicationException {
		try {
			
			return iVitalQualifireMasterDao.getVitalCategoryList(vitalQualifireMasterDto);
	
	} catch (Exception e) {
	logger.error("getVitalCategoryList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

}
