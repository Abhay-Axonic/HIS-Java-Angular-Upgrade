package com.param.global.nabhprocess.dao;

import com.param.global.common.Response;
import com.param.global.nabhprocess.dto.ReportedNabhMapperDto;
import com.param.global.unit.dto.ReportedIncidentMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes"})
public interface IReportedNabhMapperDao {
	
	Response saveReportedNabhMapper(ReportedNabhMapperDto dto);
	
	Response submitReportedNabhMapper(ReportedNabhMapperDto dto);
}


