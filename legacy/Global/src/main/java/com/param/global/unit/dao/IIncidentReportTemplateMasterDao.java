package com.param.global.unit.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.unit.dto.IncidentReportMasterDto;
import com.param.global.unit.dto.IncidentReportTemplateMapperDto;

public interface IIncidentReportTemplateMasterDao {

	Response updateIncidentReportTemplateMapper(IncidentReportMasterDto incidentReportMasterDto);

}
