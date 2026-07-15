package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IDashboardMasterDao;
import com.param.global.dto.DashboardMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})
public class DashbordMasterServiceImpl implements IDashboardMasterService, ICommonConstants{
	final static Logger logger = Logger.getLogger(DashbordMasterServiceImpl.class);

	
	@Autowired
	private IDashboardMasterDao iDashboardMasterDao;

	@Override
	public Response getDashboardMasterList(DashboardMasterDto dashboardMasterDto) throws ApplicationException{
		try {
			
			return iDashboardMasterDao.getDashboardMasterList(dashboardMasterDto);
	
		} catch (Exception e) {
		logger.error("getDashboardMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDashboardMasterListByRoleId(DashboardMasterDto dashboardMasterDto) throws ApplicationException {
		try {
			
			return iDashboardMasterDao.getDashboardMasterListByRoleId(dashboardMasterDto);
	
		} catch (Exception e) {
		logger.error("getDashboardMasterListByRoleId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
}
