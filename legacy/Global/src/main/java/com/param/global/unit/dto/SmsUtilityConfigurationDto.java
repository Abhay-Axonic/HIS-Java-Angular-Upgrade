package com.param.global.unit.dto;

import java.util.List;

public class SmsUtilityConfigurationDto {
	
	private Integer smsUtilityId ;
	private Integer triggerId;
	private String templateDescription;
    private Integer categoryId ;
	private Integer orgId ;
	private Integer unitId ;
	private Character status;
	private Integer createdBy ;
	private String createdDate;
	private Integer updatedBy ;
	private String updatedDate ;
	private String triggerName;
	private String categoryName;
	private String senderId;
	private String data;
	private String dataDesc;
	private Integer categoryType;
	private Integer triggerType;
	private Integer scheduleTypeId;
	private List<String> mobileNoList;
	private Boolean isEmail;
	private Boolean isSms;
	private String mailSubject;
	private Character isWhatsappSend;
	private String userDefinedMail;
	private Character isSMSSend;
	private String type;
	private Character criticalReport;
	private Integer whatsAppTemplateMasterId;
	private String whatsAppTemplateDesc;
	public String getUserDefinedMail() {
		return userDefinedMail;
	}
	public void setUserDefinedMail(String userDefinedMail) {
		this.userDefinedMail = userDefinedMail;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDataDesc() {
		return dataDesc;
	}
	public void setDataDesc(String dataDesc) {
		this.dataDesc = dataDesc;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public Integer getSmsUtilityId() {
		return smsUtilityId;
	}
	public void setSmsUtilityId(Integer smsUtilityId) {
		this.smsUtilityId = smsUtilityId;
	}
	public Integer getTriggerId() {
		return triggerId;
	}
	public void setTriggerId(Integer triggerId) {
		this.triggerId = triggerId;
	}
	public String getTemplateDescription() {
		return templateDescription;
	}
	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(Integer categoryType) {
		this.categoryType = categoryType;
	}
	public Integer getTriggerType() {
		return triggerType;
	}
	public void setTriggerType(Integer triggerType) {
		this.triggerType = triggerType;
	}
	public Integer getScheduleTypeId() {
		return scheduleTypeId;
	}
	public void setScheduleTypeId(Integer scheduleTypeId) {
		this.scheduleTypeId = scheduleTypeId;
	}
	public List<String> getMobileNoList() {
		return mobileNoList;
	}
	public void setMobileNoList(List<String> mobileNoList) {
		this.mobileNoList = mobileNoList;
	}
	public Boolean getIsEmail() {
		return isEmail;
	}
	public void setIsEmail(Boolean isEmail) {
		this.isEmail = isEmail;
	}
	public Boolean getIsSms() {
		return isSms;
	}
	public void setIsSms(Boolean isSms) {
		this.isSms = isSms;
	}
	public String getMailSubject() {
		return mailSubject;
	}
	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}
	public Character getIsWhatsappSend() {
		return isWhatsappSend;
	}
	public void setIsWhatsappSend(Character isWhatsappSend) {
		this.isWhatsappSend = isWhatsappSend;
	}
	public Character getIsSMSSend() {
		return isSMSSend;
	}
	public void setIsSMSSend(Character isSMSSend) {
		this.isSMSSend = isSMSSend;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Character getCriticalReport() {
		return criticalReport;
	}
	public void setCriticalReport(Character criticalReport) {
		this.criticalReport = criticalReport;
	}

	public Integer getWhatsAppTemplateMasterId() {
		return whatsAppTemplateMasterId;
	}

	public void setWhatsAppTemplateMasterId(Integer whatsAppTemplateMasterId) {
		this.whatsAppTemplateMasterId = whatsAppTemplateMasterId;
	}

	public String getWhatsAppTemplateDesc() {
		return whatsAppTemplateDesc;
	}

	public void setWhatsAppTemplateDesc(String whatsAppTemplateDesc) {
		this.whatsAppTemplateDesc = whatsAppTemplateDesc;
	}
}
