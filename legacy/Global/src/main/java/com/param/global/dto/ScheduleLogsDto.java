package com.param.global.dto;

import java.io.Serial;


import org.springframework.context.ApplicationEvent;

public class ScheduleLogsDto extends ApplicationEvent{

	@Serial
	private static final long serialVersionUID = -4215334842370518697L;
	public ScheduleLogsDto(Object source) {
		super(source);
	}
	public ScheduleLogsDto(){
		super(null);
	}
	private Integer scheduleLogsId;
	private String tableName;
	private String status;
	private String dateTime;
	private Integer recordId;
	private String priority;
	private Integer errorCount;
	private Integer addedBy;
	private String addedDate;
	private String action;
	private String application;
	private Integer orgId;
	private Integer appointmentStatusId;
	private boolean isSyncScheduling;

	
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getScheduleLogsId() {
		return scheduleLogsId;
	}
	public void setScheduleLogsId(Integer scheduleLogsId) {
		this.scheduleLogsId = scheduleLogsId;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public Integer getRecordId() {
		return recordId;
	}
	public void setRecordId(Integer recordId) {
		this.recordId = recordId;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public Integer getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}
	public Integer getAddedBy() {
		return addedBy;
	}
	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}
	public String getAddedDate() {
		return addedDate;
	}
	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getApplication() {
		return application;
	}
	public void setApplication(String application) {
		this.application = application;
	}
	
	
	public boolean getIsSyncScheduling() {
		return isSyncScheduling;
	}
	public void setIsSyncScheduling(boolean isSyncScheduling) {
		this.isSyncScheduling = isSyncScheduling;
	}

	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}

	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}

	@Override
	public String toString() {
		return "ScheduleLogsDto [scheduleLogsId=" + scheduleLogsId + ", tableName=" + tableName + ", status=" + status
				+ ", dateTime=" + dateTime + ", recordId=" + recordId + ", priority=" + priority + ", errorCount="
				+ errorCount + ", addedBy=" + addedBy + ", addedDate=" + addedDate + ", action=" + action
				+ ", application=" + application + "]";
	}
	
}
