package com.param.assetManagement.maintenance.dao;


import com.param.assetManagement.maintenance.dto.AssetScheduleChecklistDto;
import com.param.assetManagement.maintenance.dto.PreventiveMaintenanceDto;
import com.param.assetManagement.maintenance.dto.SpareConsumptionDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPreventiveMaintenanceDao {

	int saveAssetSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;

	Response getActiveAssestList(PreventiveMaintenanceDto preventiveMaintenanceDto)  throws ApplicationException;

	Response saveChecklists(AssetScheduleChecklistDto assetScheduleDto)  throws ApplicationException;
	
	Response updateAssetSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto)  throws ApplicationException;
	
	Response fetchAssetSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto)  throws ApplicationException;
	
	Response fetchCheckListByAssetScheduleId(PreventiveMaintenanceDto preventiveMaintenanceDto)  throws ApplicationException;
	
	Response addAndUpdateChecklist(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;
	
	Response deleteChecklist(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;

	Response getAssetDataToView(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;

	Response getAssetDetailsByCriteria(PreventiveMaintenanceDto preventiveMaintenanceDto)throws ApplicationException;

	Response saveChangedSchedule(PreventiveMaintenanceDto preventiveMaintenanceDto)throws ApplicationException;
	
	Response saveCompletionDetails(PreventiveMaintenanceDto preventiveMaintenanceDto)throws ApplicationException;

	Response fetchScheduledAndCompletionData(PreventiveMaintenanceDto preventiveMaintenanceDto)throws ApplicationException;

	Response saveAssignDetails(PreventiveMaintenanceDto preventiveMaintenanceDto)throws ApplicationException;
	
//	int saveOrUpdatePMSDetails(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;
	Response saveOrUpdatePMSDetails(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;
	
	Response fetchPMSByPMSId(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;
	
	Response saveOrUpdateSpareConsumption(SpareConsumptionDto dto)throws ApplicationException;
	
	Response deleteSpareById(PreventiveMaintenanceDto preventiveMaintenanceDto) throws ApplicationException;
	
}
