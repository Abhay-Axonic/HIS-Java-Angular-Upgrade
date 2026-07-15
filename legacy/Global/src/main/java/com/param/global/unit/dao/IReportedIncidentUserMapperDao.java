package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.nabhprocess.dto.ReportedNabhMapperDto;
import com.param.global.unit.dto.ReportedIncidentMapperDto;

import in.param.exception.ApplicationException;

public interface IReportedIncidentUserMapperDao {

	public Response saveReportedIncidentUserMapper(ReportedIncidentMapperDto dtoUser) throws ApplicationException;

	public Response getReportedIncidentMapperList(ReportedIncidentMapperDto reportedIncidentMapperDto) throws ApplicationException;

	public Response getReportedIncidentMapperById(ReportedIncidentMapperDto reportedIncidentMapperDto) throws ApplicationException;

	public Response getTemplateByReportedIncidentId(ReportedIncidentMapperDto reportedIncidentMapperDto) throws ApplicationException;
	
	public Response submitReportedIncidentUserMapper(ReportedIncidentMapperDto dtoUser) throws ApplicationException;

	public Response getReportedNabhMapperList(ReportedNabhMapperDto ReportedNabhMapperDto);
	
	public Response saveReportedNabhUserMapper(ReportedIncidentMapperDto dtoRole)throws ApplicationException;
	
	public Response getReportedNabhMapperList(ReportedIncidentMapperDto reportedIncidentMapperDto) throws ApplicationException;
	
	public Response submitReportedNabhUserMapper(ReportedIncidentMapperDto dtoUser) throws ApplicationException;

	public Response getTemplateByReportedNabhId(ReportedNabhMapperDto reportedNabhMapperDto) throws ApplicationException;

	public Response getReportedNabhMapperById(ReportedNabhMapperDto reportedNabhMapperDto)throws ApplicationException;
	
	public void updateReportedNabhUserMapper(ReportedIncidentMapperDto dtoUser1);


	public Integer updateReportedIncidentUserMapper(Integer reportedIncMpprId, Integer unitId, Integer organizationId);

}
