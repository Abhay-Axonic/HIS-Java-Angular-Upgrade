package com.param.global.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.nabhprocess.dto.ReportedNabhMapperDto;
import com.param.global.unit.dto.ReportedIncidentMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes"})
@Transactional
public interface IReportedIncidentMapperService {

	Response saveReportedIncidentMapper(ReportedIncidentMapperDto reportedIncidentMapperDto) throws ApplicationException;

	Response getReportedIncidentMapperList(ReportedIncidentMapperDto reportedIncidentMapperDto) throws ApplicationException;

	Response getReportedIncidentMapperById(ReportedIncidentMapperDto reportedIncidentMapperDto)throws ApplicationException;

	Response getTemplateByReportedIncidentId(ReportedIncidentMapperDto reportedIncidentMapperDto)throws ApplicationException;
	
	Response submitReportedIncidentMapper(ReportedIncidentMapperDto reportedIncidentMapperDto)throws ApplicationException;

	Response getTemplateByReportedNabhId(ReportedNabhMapperDto reportedNabhMapperDto)throws ApplicationException;

	Response getReportedNabhMapperById(ReportedNabhMapperDto reportedNabhMapperDto)throws ApplicationException;

}
