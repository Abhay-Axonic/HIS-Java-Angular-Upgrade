package com.param.global.nabhprocess.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.nabhprocess.dto.ReportedNabhMapperDto;
import com.param.global.unit.dto.ReportedIncidentMapperDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes"})
@Transactional
public interface IReportedNabhMapperService {

	Response saveReportedNabhMapper(ReportedNabhMapperDto reportedNabhMapperDto) throws ApplicationException;

	Response getReportedNabhMapperList(ReportedNabhMapperDto reportedNabhMapperDto) throws ApplicationException;

//	Response getReportedIncidentMapperById(ReportedIncidentMapperDto reportedIncidentMapperDto)throws ApplicationException;
//
//	Response getTemplateByReportedIncidentId(ReportedIncidentMapperDto reportedIncidentMapperDto)throws ApplicationException;
//	
    Response submitReportedNabhMapper(ReportedNabhMapperDto reportedNabhMapperDto)throws ApplicationException;
}
