package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedNativeQueries({

    @NamedNativeQuery(name = "GET_MESSAGE_FROM_QUEQE", query = " select " + 
    		"	mass.message_queue_id as \"messageQueueId\","+
    		"	mass.message_type as \"messageType\","+
    		"	mass.mail_content as \"mailContent\","+
    		"	mass.to_mail_id as \"toMailId\","+
    		"	mail_subject as \"mailSubject\","+
    		"	mail_file_name as \"mailFileName\","+
    		"	mass.report_string as \"reportString\","+
    		"	mass.to_whatsapp_number as \"toWhatsappNumber\","+
    		"	mass.text_message_content as \"textMessageContent\","+
    		"	mass.mobile_number as \"mobileNumber\","+
    		"	mass.retry_attemps as \"retryAttemps\","+
    		"	mass.unique_id as \"uniqueId\","+
    		"	mass.patient_id_list as \"patientIdList\","+
    		"	mass.trigger_id as \"triggerId\","+
    		"	mass.user_id as \"userId\","+
    		"	mass.category_id as \"categoryId\","+
    		"	mass.category_name as \"categoryName\","+
    		"	mass.unit_id as \"unitId\","+
    		"	mass.organization_id as \"organizationId\","+
    		"	mass.whatsapp_template_master_id as \"whatsAppTemplateMasterId\","+
    		"	mass.whatsapp_document_url as \"whatsappDocumentUrl\","+
    		"	mwtm.template_desc as \"whatsAppTemplateName\","+
    		"	mwtm.message_type as \"whatsAppMessageType\","+
    		"	tuabc.smtp_host as \"smtpHost\","+
    		"	tuabc.smtp_port as \"smtPort\","+
    		"	tuabc.smtp_user_id as \"smtpUserId\","+
    		"	tuabc.smtp_user_pwd as \"smtpUserPwd\","+
    		"	tuabc.sender_id as \"senderId\","+
    		"	tuabc.pdf_generation_url  as \"pdfGenerationUrl\" ,"
    		+ " mum.unit_folder as \"unitFolder\","
    		+ " tuabc.mail_provider_id as \"mailProviderId\","
    		+ " tuabc.mail_credential as \"mailCredential\","
    		+ " tuabc.bcc_email_id as \"bccEmailId\" "
    		+"	from public.t_message_queue mass "+
    		"	inner join t_unit_application_billing_configuration tuabc on mass.unit_id = tuabc.unit_id "
    		+ " inner join m_unit_master mum  on mum.unit_id = mass.unit_id   "
			+ " left join public.m_whatsapp_template_master mwtm on mwtm.whatsapp_template_master_id = mass.whatsapp_template_master_id "
      )
})


@Entity
@Table(name = "t_message_queue", schema = "public")
@SequenceGenerator(name = "message_queue_seq", sequenceName = "public.message_queue_seq", allocationSize = 1)
public class MessageQueue {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_queue_seq")
	@Column(name = "message_queue_id")
	private Integer id;
	
	@Column(name = "message_type")
	private Integer messageType;
	
	@Column(name = "mail_content")
	private String mailContent;
	
	@Column(name = "to_mail_id")
	private String toMailId;
	
	@Column(name = "mail_subject")
	private String mailSubject;
	
	@Column(name = "mail_file_name")
	private String mailFileName;
	
	@Column(name = "report_string")
	private String reportString;
	
	@Column(name = "to_whatsapp_number")
	private String toWhatsappNumber;
	
	@Column(name = "text_message_content")
	private String textMessageContent;
	
	@Column(name = "mobile_number")
	private String mobileNumber;
	
	@Column(name = "retry_attemps")
	private Integer retryAttemps;
	
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
	
	@Column(name = "selected")
	private Boolean selected;

	@Column(name = "whatsapp_template_master_id")
	private Integer whatsAppTemplateMasterId;

	@Column(name = "whatsapp_document_url")
	private String whatsappDocumentUrl;

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

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public String getToMailId() {
		return toMailId;
	}

	public void setToMailId(String toMailId) {
		this.toMailId = toMailId;
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

	public String getToWhatsappNumber() {
		return toWhatsappNumber;
	}

	public void setToWhatsappNumber(String toWhatsappNumber) {
		this.toWhatsappNumber = toWhatsappNumber;
	}

	public String getTextMessageContent() {
		return textMessageContent;
	}

	public void setTextMessageContent(String textMessageContent) {
		this.textMessageContent = textMessageContent;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public Integer getRetryAttemps() {
		return retryAttemps;
	}

	public void setRetryAttemps(Integer retryAttemps) {
		this.retryAttemps = retryAttemps;
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

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public Integer getWhatsAppTemplateMasterId() {
		return whatsAppTemplateMasterId;
	}

	public void setWhatsAppTemplateMasterId(Integer whatsAppTemplateMasterId) {
		this.whatsAppTemplateMasterId = whatsAppTemplateMasterId;
	}

	public String getWhatsappDocumentUrl() {
		return whatsappDocumentUrl;
	}

	public void setWhatsappDocumentUrl(String whatsappDocumentUrl) {
		this.whatsappDocumentUrl = whatsappDocumentUrl;
	}
}
