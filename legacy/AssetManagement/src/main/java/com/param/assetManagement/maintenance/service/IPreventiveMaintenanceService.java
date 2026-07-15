package com.param.assetManagement.maintenance.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.assetManagement.maintenance.dto.PreventiveMaintenanceDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface IPreventiveMaintenanceService {
	
	Response saveAssetSchedule(List<PreventiveMaintenanceDto> preventiveMaintenanceDto) throws ApplicationException;

	Response getActiveAssestList(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;
	
	Response updateAssetSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;
	
	Response fetchAssetSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;
	
	Response addAndUpdateChecklist(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;
	
	Response deleteChecklist(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;

	Response fetchCheckListByAssetScheduleId(PreventiveMaintenanceDto preventiveMaintenanceDto);

	Response getAssetDataToView(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;

	Response getAssetDetailsByCriteria(PreventiveMaintenanceDto preventiveMaintenanceDto)throws ApplicationException;

	Response saveChangedSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto)throws ApplicationException;
	
	Response saveCompletionDetails(PreventiveMaintenanceDto preventiveMaintenanceDto)throws ApplicationException;

	Response fetchScheduledAndCompletionData(PreventiveMaintenanceDto preventiveMaintenanceDto)throws ApplicationException;

	Response saveAssignDetails(PreventiveMaintenanceDto preventiveMaintenanceDto)throws ApplicationException;
	
	Response saveOrUpdatePMSDetails(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;

	Response fetchPMSByPMSId(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;
	
	Response deleteSpareById(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;
	
}
