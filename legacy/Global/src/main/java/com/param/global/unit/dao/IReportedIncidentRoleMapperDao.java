package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.model.ReportedIncidentRoleMapper;
import com.param.global.nabhprocess.dto.ReportedNabhMapperDto;
import com.param.global.unit.dto.ReportedIncidentMapperDto;

import in.param.exception.ApplicationException;

public interface IReportedIncidentRoleMapperDao {

	Response saveReportedIncidentRoleMapper(ReportedIncidentMapperDto dtoRole)throws ApplicationException;	
	
	Response saveReportedNabhRoleMapper(ReportedIncidentMapperDto dtoRole)throws ApplicationException;

    void updateReportedNabhRoleMapper(ReportedIncidentMapperDto dtoRole);

    Integer updateReportedIncidentRoleMapper(Integer reportedIncMpprId, Integer unitId, Integer organizationId);
}
