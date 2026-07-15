package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.WeekendMasterDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({"rawtypes"})
public interface IWeekendMasterService {

	Response saveWeekendMaster(WeekendMasterDto weekendMasterDto)throws ApplicationException;

	Response getWeekendMasterList(WeekendMasterDto weekendMasterDto)throws ApplicationException;
	
	Response updateWeekendMaster(WeekendMasterDto weekendMasterDto)throws ApplicationException;
	
	Response updateWeekendMasterStatus(WeekendMasterDto weekendMasterDto)throws ApplicationException;
	
	Response getListOfWeekendMaster(WeekendMasterDto weekendMasterDto)throws ApplicationException;
	
	Response getWeekendMasterCount(WeekendMasterDto weekendMasterDto)throws ApplicationException;
	
	Response getWeekendMasterById(Integer weekendId, Integer organizationId, Integer unitId)throws ApplicationException;
	
	Response getWeekendMasterByName(WeekendMasterDto weekendMasterDto)throws ApplicationException;
	
	Response getWeekendMasterByNameNotId(WeekendMasterDto weekendMasterDto)throws ApplicationException;

	

}
