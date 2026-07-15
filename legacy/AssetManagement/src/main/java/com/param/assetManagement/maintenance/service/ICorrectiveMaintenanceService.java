package com.param.assetManagement.maintenance.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface ICorrectiveMaintenanceService {
	
	Response saveCorrectiveMaintenance(CorrectiveMaintenanceDto correctiveMaintenanceDto) throws ApplicationException;

	Response getCorrectiveMaintenanceListByMultipleCriteria(CorrectiveMaintenanceDto correctiveMaintenanceDto) throws ApplicationException;

	Response getSerialNoByItemId(Integer itemId, Integer unitId, Integer organizationId) throws ApplicationException;

	Response ExportedToCorrectiveMaintenanceWorklist(CorrectiveMaintenanceDto correctiveMaintenanceDto)throws ApplicationException;

	Response CorrectiveMaintenanceRequestCompleteByRequester(CorrectiveMaintenanceDto correctiveMaintenanceDto)throws ApplicationException;

	Response updateCorrectiveMaintenancePendingToNewRequest(List<CorrectiveMaintenanceDto> correctiveMaintenanceDto)throws ApplicationException;

	Response cancelCorrectiveMaintenanceRequest(CorrectiveMaintenanceDto correctiveMaintenanceDto) throws ApplicationException;

	Response getPMSDetailsByItemId(CorrectiveMaintenanceDto correctiveMaintenanceDto) throws ApplicationException;
}
