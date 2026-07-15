package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.IncidentReportingMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes" })
public interface IIncidentReportingMapperDao {
	Response saveIncidentReportingMapper(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException;

	Response updateIncidentReportingMapper(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException;

	Response getIncidentReportingMapperById(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException;

	Response getIncidentReportingMapperList(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException;

	Response updateIncidentReportingMapperStatus(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException;

	Response getActiveIncidentReportingMapperList(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException;

	Response getIncidentReportingMapperCount(IncidentReportingMapperDto incidentReportingMapperDto)
			throws ApplicationException;

	Response getRoleWiseUserList(IncidentReportingMapperDto incidentReportingMapperDto) throws ApplicationException;
}
