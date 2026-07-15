package com.param.global.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.ICabinMasterDao;
import com.param.global.unit.dao.ITabularTemplateMasterDao;
import com.param.global.unit.dto.TabularTemplateMasterDto;
import com.param.global.unit.dto.TabularTemplateMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class TabularTemplateMasterServiceImpl implements ITabularTemplateMasterService, ICommonConstants{
	final static Logger logger = Logger.getLogger(TabularTemplateMasterServiceImpl.class);

	
	@Autowired
	ITabularTemplateMasterDao iTabularTemplateMasterDao;
	
	@Override
	public Response saveTabularTemplateMaster(TabularTemplateMasterDto tabularTemplateMasterDto)
			throws ApplicationException {
		
		Response res=iTabularTemplateMasterDao.getTabularTemplateMasterByName(tabularTemplateMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iTabularTemplateMasterDao.saveTabularTemplateMaster(tabularTemplateMasterDto);
			}

		} catch (Exception e) {
		logger.error("saveTabularTemplateMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTabularTemplateMaster(TabularTemplateMasterDto tabularTemplateMasterDto) {
		Response res=iTabularTemplateMasterDao.getTabularTemplateMasterByNameNotId(tabularTemplateMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iTabularTemplateMasterDao.updateTabularTemplateMaster(tabularTemplateMasterDto);
			}

		} catch (Exception e) {
		logger.error("updateTabularTemplateMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTabularTemplateMasterStatus(TabularTemplateMasterDto tabularTemplateMasterDto) {
		try {
			
			return iTabularTemplateMasterDao.updateTabularTemplateMasterStatus(tabularTemplateMasterDto);
	
	} catch (Exception e) {
	logger.error("updateTabularTemplateMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getTabularTemplateMasterById(Integer tabularTemplateId, Integer organizationId, Integer unitId) {
		try {
			
			return iTabularTemplateMasterDao.getTabularTemplateMasterById(tabularTemplateId, organizationId, unitId);
	
	} catch (Exception e) {
	logger.error("getTabularTemplateMasterById failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getTabularTemplateMasterList(TabularTemplateMasterDto tabularTemplateMasterDto) {
		try {
			
			return iTabularTemplateMasterDao.getTabularTemplateMasterList(tabularTemplateMasterDto);
	
	} catch (Exception e) {
	logger.error("getTabularTemplateMasterList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getTabularTemplateMasterCount(TabularTemplateMasterDto tabularTemplateMasterDto) {
		try {
			
			return iTabularTemplateMasterDao.getTabularTemplateMasterCount(tabularTemplateMasterDto);
	
	} catch (Exception e) {
	logger.error("getTabularTemplateMasterCount failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getActiveTabularTemplateMaster(Integer organizationId, Integer unitId) {
		try {
			
			return iTabularTemplateMasterDao.getActiveTabularTemplateMaster(organizationId, unitId);
	
	} catch (Exception e) {
	logger.error("getActiveTabularTemplateMaster failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
  }

	@Override
	public Response getTabularTemplateMasterByName(TabularTemplateMasterDto tabularTemplateMasterDto) throws ApplicationException{
			try {
				
				return iTabularTemplateMasterDao.getTabularTemplateMasterByName(tabularTemplateMasterDto);
		
		} catch (Exception e) {
		logger.error("getTabularTemplateMasterByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTabularTemplateMasterByNameNotId(TabularTemplateMasterDto tabularTemplateMasterDto) throws ApplicationException{
			try {
				
				return iTabularTemplateMasterDao.getTabularTemplateMasterByNameNotId(tabularTemplateMasterDto);
		
		} catch (Exception e) {
		logger.error("getTabularTemplateMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
