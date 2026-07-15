package com.param.global.unit.dao;

import org.springframework.web.bind.annotation.RequestBody;

import com.param.global.common.Response;
import com.param.global.unit.dto.ReportDetailsDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
public interface IReportDetailsDao {
	public Response  getReportDetailsList(@RequestBody ReportDetailsDto reportDetailsDto) throws ApplicationException;
	public Response  getReportDetailsByReportId(@RequestBody ReportDetailsDto reportDetailsDto) throws ApplicationException;
	public Response getReportGroupHeaderMaster(ReportDetailsDto reportDetailsDto) throws ApplicationException;
	public Response getReportGroupHeaderForUserRoleManagement(ReportDetailsDto reportDetailsDto)throws ApplicationException;
	public Response getMappedMISReportIdsForUser(ReportDetailsDto reportDetailsDto)throws ApplicationException;
	public Response getMISReportFormattesMasterforShowReports(ReportDetailsDto reportDetailsDto)throws ApplicationException;
	public Response searchMISReport(ReportDetailsDto reportDetailsDto)throws ApplicationException;
}
