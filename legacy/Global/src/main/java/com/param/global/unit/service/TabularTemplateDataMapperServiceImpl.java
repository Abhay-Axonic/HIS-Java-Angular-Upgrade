package com.param.global.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.IPatientTabularTemplateDataMapperdDao;
import com.param.global.unit.dao.ITabularTemplateDataMapperDao;
import com.param.global.unit.dao.ITabularTemplateMasterDao;
import com.param.global.unit.dto.PatientTabularTemplateDataMapperDto;
import com.param.global.unit.dto.TabularTemplateDataMapperDto;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class TabularTemplateDataMapperServiceImpl implements ITabularTemplateDataMapperService, ICommonConstants{
	final static Logger logger = Logger.getLogger(TabularTemplateDataMapperServiceImpl.class);


	@Autowired
	ITabularTemplateDataMapperDao iTabularTemplateDataMapperDao;
	
	@Autowired
	IPatientTabularTemplateDataMapperdDao iPatientTabularTemplateDataMapperdDao;
	
	@Override
	public Response saveTabularTemplateDataMapper(TabularTemplateDataMapperDto tabularTemplateDataMapperDto)
			throws ApplicationException {
		try {
				return iTabularTemplateDataMapperDao.saveTabularTemplateDataMapper(tabularTemplateDataMapperDto);

		} catch (Exception e) {
		logger.error("saveTabularTemplateDataMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTabularTemplateDataMapper(TabularTemplateDataMapperDto tabularTemplateDataMapperDto)
			throws ApplicationException {
		try {
	
				return iTabularTemplateDataMapperDao.updateTabularTemplateDataMapper(tabularTemplateDataMapperDto);
			

		} catch (Exception e) {
		logger.error("updateTabularTemplateDataMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTabularTemplateDataMapperById(Integer tabularTemplateId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			
			return iTabularTemplateDataMapperDao.getTabularTemplateDataMapperById(tabularTemplateId, organizationId, unitId);
	
	} catch (Exception e) {
	logger.error("getTabularTemplateDataMapperById failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response savePatientTabularTemplateDataMapper(PatientTabularTemplateDataMapperDto patientTabularTemplateDataMapperDto) throws ApplicationException {
		try {
			return iPatientTabularTemplateDataMapperdDao.savePatientTabularTemplateDataMapper(patientTabularTemplateDataMapperDto);

	} catch (Exception e) {
	logger.error("savePatientTabularTemplateDataMapper failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getPatientTabularTemplateDataMapperById(Integer tabularTemplateId, Integer organizationId,
			Integer unitId , Integer patientId ,Integer templateTypeId) throws ApplicationException {
		try {
			
			return iPatientTabularTemplateDataMapperdDao.getPatientTabularTemplateDataMapperById(tabularTemplateId, organizationId, unitId ,patientId,templateTypeId);
	
	} catch (Exception e) {
	logger.error("getPatientTabularTemplateDataMapperById failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

}
