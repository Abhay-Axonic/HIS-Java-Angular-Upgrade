package com.param.global.unit.dao;

import com.param.entity.lis.global.LabConfigurationMaster;
import com.param.global.common.Response;
import com.param.global.unit.dto.LabConfigurationDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface ILabConfigurationDao extends IGenericDao<LabConfigurationMaster,Integer> {
	Response saveLabConfiguration(LabConfigurationDto dto)throws ApplicationException;
	Response updateLabConfiguration(LabConfigurationDto dto)throws ApplicationException;
	Response getLabConfigurationById(int tLabConfigId)throws ApplicationException;
	Response updateConfigStatus(int tLabConfigId,char status)throws ApplicationException;
	Response getLabConfigurationList(int orgId,int unitId)throws ApplicationException;
	Response checkConfigurationExistOrNot(LabConfigurationDto dto)throws ApplicationException;
	Response getLabConfigurationByOrgUnit(int orgId,int unitId)throws ApplicationException;
	Response checkPinExistOrNot(int orgId, int unitId, String pin, int userId)throws ApplicationException;
	Response getUserByPin(int orgId, int unitId, String pin)throws ApplicationException;
}
