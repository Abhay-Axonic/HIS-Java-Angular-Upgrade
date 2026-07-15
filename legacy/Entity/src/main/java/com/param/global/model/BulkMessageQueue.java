package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_bulk_message_queue", schema = "public")
@SequenceGenerator(name = "bulk_message_queue_seq", sequenceName = "public.bulk_message_queue_seq", allocationSize = 1)
public class BulkMessageQueue {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bulk_message_queue_seq")
	@Column(name = "bulk_message_queue_id")
	private Integer bulkMessageQueueId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "mail_content")
	private String mailContent;
	
	@Column(name = "mail_subject")
	private String mailSubject;
	
	@Column(name = "mail_file_name")
	private String mailFileName;
	
	@Column(name = "report_string")
	private String reportString;
	
	@Column(name = "message_queue_id")
	private Integer messageQueueId;
	
	@Column(name = "message_log_id")
	private Integer messageLogId;
	
	@Column(name = "unique_id")
	private String uniqueId;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "error_message")
	private String errorMessage;
	
	@Column(name = "retry_attemps")
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
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
