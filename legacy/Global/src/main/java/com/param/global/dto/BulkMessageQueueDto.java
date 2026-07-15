package com.param.global.dto;

public class BulkMessageQueueDto {

	private Integer bulkMessageQueueId;
	
	private Integer patientId;
	
	private String mailContent;
	
	private String mailSubject;
	
	private String mailFileName;
	
	private String reportString;
	
	private Integer messageQueueId;
	
	private Integer messageLogId;
	
	private String uniqueId;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private String errorMessage;
	
	private Integer retryAttemps; 

	public Integer getBulkMessageQueueId() {
		return bulkMessageQueueId;
	}

	public void setBulkMessageQueueId(Integer bulkMessageQueueId) {
		this.bulkMessageQueueId = bulkMessageQueueId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getMailFileName() {
		return mailFileName;
	}

	public void setMailFileName(String mailFileName) {
		this.mailFileName = mailFileName;
	}

	public String getReportString() {
		return reportString;
	}

	public void setReportString(String reportString) {
		this.reportString = reportString;
	}

	public Integer getMessageQueueId() {
		return messageQueueId;
	}

	public void setMessageQueueId(Integer messageQueueId) {
		this.messageQueueId = messageQueueId;
	}

	public Integer getMessageLogId() {
		return messageLogId;
	}

	public void setMessageLogId(Integer messageLogId) {
		this.messageLogId = messageLogId;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Integer getRetryAttemps() {
		return retryAttemps;
	}

	public void setRetryAttemps(Integer retryAttemps) {
		this.retryAttemps = retryAttemps;
	}

	
}
