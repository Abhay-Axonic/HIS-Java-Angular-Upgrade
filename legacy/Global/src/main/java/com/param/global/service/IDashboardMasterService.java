package com.param.global.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.param.global.common.Response;
import com.param.global.dto.DashboardMasterDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({"rawtypes","unused"})
public interface IDashboardMasterService {

	public Response getDashboardMasterList(DashboardMasterDto dashboardMasterDto)throws ApplicationException;

	public Response getDashboardMasterListByRoleId(DashboardMasterDto dashboardMasterDto)throws ApplicationException;

}
