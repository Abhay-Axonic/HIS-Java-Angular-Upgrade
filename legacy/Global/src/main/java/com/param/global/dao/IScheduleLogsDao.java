package com.param.global.dao;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.dto.ScheduleLogsDto;
import com.param.global.dto.sync.ServiceappoServiceMpprSyncDto;
import com.param.global.model.ScheduleLogs;

@SuppressWarnings("rawtypes")
public interface IScheduleLogsDao extends IGenericDao<ScheduleLogs, Integer>{

	
	public Response saveScheduleLogs(ScheduleLogsDto scheduleLogsDto)throws ApplicationException;
	public Response getPendingScheduleLogs()throws ApplicationException;
	public Response markScheduleLogsCompleted(Integer scheduleLogsId)throws ApplicationException;
	public Response markScheduleLogsError(Integer scheduleLogsId)throws ApplicationException;
	public Response getPendingScheduleLogsForAppointmenDetails();
	public Response updatePathalogyLabReportStatus(List<ServiceappoServiceMpprSyncDto> serviceAppoMapperList);
	public Response getConnectionStringForMyLife()throws ApplicationException;
	public Response isRecordIdAndTableNameExist(ScheduleLogsDto scheduleLogsDto)throws ApplicationException;
	public Response getScheduleDetailByIdForGlobalScheduling(Integer recordId)throws ApplicationException;
	public Response getMyLifeDetail(Integer id)throws ApplicationException;
	public Integer getchannelIdForSync(Integer organizationId)throws ApplicationException;
	public Response markScheduleLogsCompletedUsingPatientId(Integer organizationId)throws ApplicationException;
	
	
}
