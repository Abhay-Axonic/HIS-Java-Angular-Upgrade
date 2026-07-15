package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.ScreeningTestMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IScreeningTestMasterService {
	
	public Response saveScreeningTestMaster(ScreeningTestMasterDto bloodTestMasterDto) throws ApplicationException;
	
	public Response updateScreeningTestMaster(ScreeningTestMasterDto bloodTestMasterDto) throws ApplicationException;
	
	public Response updateScreeningTestMasterStatus(ScreeningTestMasterDto bloodTestMasterDto) throws ApplicationException;
	
	public Response getScreeningTestMasterList(ScreeningTestMasterDto bloodTestMasterDto) throws ApplicationException;
	
	public Response getScreeningTestMasterById(Integer bloodTestId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveScreeningTestMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getScreeningTestMasterCount(ScreeningTestMasterDto bloodTestMasterDto) throws ApplicationException;
	
	public Response getScreeningTestMasterByName(ScreeningTestMasterDto bloodTestMasterDto) throws ApplicationException;
	
	public Response getScreeningTestMasterByNameNotId(ScreeningTestMasterDto bloodTestMasterDto) throws ApplicationException;
}
