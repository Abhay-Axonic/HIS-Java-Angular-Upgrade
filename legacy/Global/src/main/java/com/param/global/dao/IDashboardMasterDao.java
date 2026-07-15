package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.DashboardMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes","unused"})
public interface IDashboardMasterDao {
	
	public Response getDashboardMasterList(DashboardMasterDto dashboardMasterDto)throws ApplicationException;

	public Response getDashboardMasterListByRoleId(DashboardMasterDto dashboardMasterDto)throws ApplicationException;
}
