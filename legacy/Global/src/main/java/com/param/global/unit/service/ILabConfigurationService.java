package com.param.global.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.global.unit.dto.MedallRequestResponseDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({"rawtypes"})
public interface ILabConfigurationService {

	Response saveLabConfiguration(LabConfigurationDto dto)throws ApplicationException;
	Response updateLabConfiguration(LabConfigurationDto dto)throws ApplicationException;
	Response getLabConfigurationById(int tLabConfigId)throws ApplicationException;
	Response updateConfigStatus(int tLabConfigId,char status)throws ApplicationException;
	Response getLabConfigurationList(int orgId,int unitId)throws ApplicationException;
	Response checkConfigurationExistOrNot(LabConfigurationDto dto)throws ApplicationException;
	Response getLabConfigurationByOrgUnit(int orgId,int unitId)throws ApplicationException;
	Response checkPinExistOrNot(int orgId, int unitId, String pin, int userId)throws ApplicationException;
	Response getUserByPin(int orgId, int unitId, String pin)throws ApplicationException;
	Response saveMedallRequestResponse(MedallRequestResponseDto dto)throws ApplicationException;;
	Response getMedallRequestResponse(MedallRequestResponseDto med)throws ApplicationException;;
	
}
