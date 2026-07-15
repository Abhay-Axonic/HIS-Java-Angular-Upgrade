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
@Table(name = "t_message_log", schema = "public")
@SequenceGenerator(name = "message_log_seq", sequenceName = "public.message_log_seq", allocationSize = 1)
public class MessageLog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_log_seq")
	@Column(name = "message_log_id")
	private Integer id;
	
	@Column(name = "message_type")
	private Integer messageType;
	
	@Column(name = "to_mail_id")
	private String toMailId;
	
	@Column(name = "to_whatsapp_number")
	private String toWhatsappNumber;
	
	@Column(name = "mobile_number")
	private String mobileNumber;
	
	@Column(name = "message_status")
	private boolean messageStatus;
	
	@Column(name = "unique_id")
	private String uniqueId;
	
	@Column(name = "patient_id_list")
	private String patientIdList;
	
	@Column(name = "trigger_id")
	private Integer triggerId;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "category_name")
	private String categoryName;
	
	@Column(name = "category_id")
	private Integer categoryId;
		
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "error_message")
	private String errorMessage;
	
	@Column(name = "message_sent_status")
	private boolean messageSentStatus;
	
	@Column(name = "message_queue_id")
	private Integer messageQueueId;
	
	@Column(name = "mail_content")
	private String mailContent;
	
	@Column(name = "mail_subject")
	private String mailSubject;
	
	@Column(name = "report_string")
	private String reportString;
	
	@Column(name = "text_message_content")
	private String textMessageContent;
	
	@Column(name = "from_email")
	private String fromEmail;

	@Column(name = "whatsapp_template_master_id")
	private Integer whatsAppTemplateMasterId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMessageType() {
		return messageType;
	}

	public void setMessageType(Integer messageType) {
		this.messageType = messageType;
	}

	public String getToMailId() {
		return toMailId;
	}

	public void setToMailId(String toMailId) {
		this.toMailId = toMailId;
	}

	public String getToWhatsappNumber() {
		return toWhatsappNumber;
	}

	public void setToWhatsappNumber(String toWhatsappNumber) {
		this.toWhatsappNumber = toWhatsappNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public boolean isMessageStatus() {
		return messageStatus;
	}

	public void setMessageStatus(boolean messageStatus) {
		this.messageStatus = messageStatus;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getPatientIdList() {
		return patientIdList;
	}

	public void setPatientIdList(String patientIdList) {
		this.patientIdList = patientIdList;
	}

	public Integer getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(Integer triggerId) {
		this.triggerId = triggerId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public boolean isMessageSentStatus() {
		return messageSentStatus;
	}

	public void setMessageSentStatus(boolean messageSentStatus) {
		this.messageSentStatus = messageSentStatus;
	}

	public Integer getMessageQueueId() {
		return messageQueueId;
	}

	public void setMessageQueueId(Integer messageQueueId) {
		this.messageQueueId = messageQueueId;
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

	public String getReportString() {
		return reportString;
	}

	public void setReportString(String reportString) {
		this.reportString = reportString;
	}

	public String getTextMessageContent() {
		return textMessageContent;
	}

	public void setTextMessageContent(String textMessageContent) {
		this.textMessageContent = textMessageContent;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public Integer getWhatsAppTemplateMasterId() {
		return whatsAppTemplateMasterId;
	}

	public void setWhatsAppTemplateMasterId(Integer whatsAppTemplateMasterId) {
		this.whatsAppTemplateMasterId = whatsAppTemplateMasterId;
	}
}
