package com.param.global.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.IncidentReportMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Transactional
public interface IIncidentReportMasterService {

	public Response saveIncidentReportMaster(IncidentReportMasterDto incidentReportMasterDto) throws ApplicationException;

	public Response updateIncidentReportMaster(IncidentReportMasterDto incidentReportMasterDto) throws ApplicationException;

	public Response getIncidentReportMasterById(IncidentReportMasterDto incidentReportMasterDto) throws ApplicationException;

	public Response getIncidentReportMasterList(IncidentReportMasterDto incidentReportMasterDto) throws ApplicationException;

	public Response getTemplateByIncidentReportId(IncidentReportMasterDto incidentReportMasterDto) throws ApplicationException;

	public Response getRoleByIncidentReportId(IncidentReportMasterDto incidentReportMasterDto) throws ApplicationException;

	public Response updateIncidentReportMasterStatus(IncidentReportMasterDto incidentReportMasterDto) throws ApplicationException;

	public Response getActiveIncidentReportMasterList(IncidentReportMasterDto incidentReportMasterDto) throws ApplicationException;

	public Response getIncidentReportMasterCount(IncidentReportMasterDto incidentReportMasterDto) throws ApplicationException;

}
