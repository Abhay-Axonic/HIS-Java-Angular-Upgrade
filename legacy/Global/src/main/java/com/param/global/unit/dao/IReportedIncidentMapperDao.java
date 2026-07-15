package com.param.global.unit.dao;


import com.param.global.common.Response;
import com.param.global.unit.dto.ReportedIncidentMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({ "rawtypes"})
public interface IReportedIncidentMapperDao {

	Response saveReportedIncidentMapper(ReportedIncidentMapperDto dto);

	Response submitReportedIncidentMapper(ReportedIncidentMapperDto dto);
}
