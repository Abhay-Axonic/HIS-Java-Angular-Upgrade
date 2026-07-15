package com.param.assetManagement.maintenance.service;

import javax.transaction.Transactional;

import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceWorklistDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface ICorrectiveMaintenanceWorklistService {

	Response saveCorrectiveMaintenanceWorklist(CorrectiveMaintenanceWorklistDto correctiveMaintenanceWorklistDto) throws ApplicationException;

	Response getCorrectiveMaintenanceWorklistListByMultipleCriteria(CorrectiveMaintenanceWorklistDto correctiveMaintenanceWorklistDto)throws ApplicationException;
}
