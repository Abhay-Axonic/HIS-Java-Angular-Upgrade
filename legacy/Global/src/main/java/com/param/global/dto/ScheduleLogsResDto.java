package com.param.global.dto;

public class ScheduleLogsResDto {
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
	private Integer appointmentStatusId;
	private Integer globalOrgId;
	private Integer globalDocId;
	private String uuid;
	private Integer channelId;
	
	public Integer getGlobalOrgId() {
		return globalOrgId;
	}
	public void setGlobalOrgId(Integer globalOrgId) {
		this.globalOrgId = globalOrgId;
	}
	public Integer getGlobalDocId() {
		return globalDocId;
	}
	public void setGlobalDocId(Integer globalDocId) {
		this.globalDocId = globalDocId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
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
	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}
	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	
	
}
