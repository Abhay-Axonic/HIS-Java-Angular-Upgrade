package com.param.bloodBank.dao;

import com.param.bloodBank.dto.ScreeningTestMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IScreeningTestMasterDao {
	public Response saveScreeningTestMaster(ScreeningTestMasterDto screeningTestMasterDto) throws ApplicationException;
	
	public Response updateScreeningTestMaster(ScreeningTestMasterDto screeningTestMasterDto) throws ApplicationException;
	
	public Response updateScreeningTestMasterStatus(ScreeningTestMasterDto screeningTestMasterDto) throws ApplicationException;
	
	public Response getScreeningTestMasterList(ScreeningTestMasterDto screeningTestMasterDto) throws ApplicationException;
	
	public Response getScreeningTestMasterById(Integer screeningTestId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveScreeningTestMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getScreeningTestMasterCount(ScreeningTestMasterDto screeningTestMasterDto) throws ApplicationException;
	
	public Response getScreeningTestMasterByName(ScreeningTestMasterDto screeningTestMasterDto) throws ApplicationException;
	
	public Response getScreeningTestMasterByNameNotId(ScreeningTestMasterDto screeningTestMasterDto) throws ApplicationException;
}
