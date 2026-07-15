package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.IncidentCategoryMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})

public interface IIncidentCategoryMasterDaoImpl {

	public Response getIncidentCategoryByName(IncidentCategoryMasterDto incidentCategoryMasterDto)throws ApplicationException;

	public Response saveIncidentCategoryMaster(IncidentCategoryMasterDto incidentCategoryMasterDto)throws ApplicationException;

	public Response getIncidentCategoryMasterByNameNotId(IncidentCategoryMasterDto incidentCategoryMasterDto)throws ApplicationException;

	public Response updateIncidentCategoryMasterStatus(IncidentCategoryMasterDto incidentCategoryMasterDto)throws ApplicationException;

	public Response getIncidentCategoryMasterById(Integer incidentCategoryId, Integer organizationId, Integer unitId)throws ApplicationException;

	public Response getIncidentCategoryMasterList(IncidentCategoryMasterDto incidentCategoryMasterDto)throws ApplicationException;

	public Response getIncidentCategoryMasterCount(IncidentCategoryMasterDto incidentCategoryMasterDto)throws ApplicationException;

	public Response getActiveIncidentCategoryMaster(Integer organizationId, Integer unitId)throws ApplicationException;

	public Response updateIncidentCategoryMaster(IncidentCategoryMasterDto incidentCategoryMasterDto)throws ApplicationException;
}
