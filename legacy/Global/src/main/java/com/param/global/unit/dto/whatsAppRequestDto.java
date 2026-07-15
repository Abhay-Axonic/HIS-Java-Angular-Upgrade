package com.param.global.unit.dto;

import java.util.Date;

import javax.persistence.Column;

public class whatsAppRequestDto {
	private String to_number;
	private String type ;
	private String message;
	private String text;
	private String mediaUrl;
	private String name;
	private String apiKey;
	private String responseJson;
	
	private Integer whatsAppCredentialId;
	
	private String accountSid;
	
	private String authToken;
	
	private String fromNumber;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer typeOf;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	  
	  private String from;
	  
	  private String to;
	  
	  private String message_type;
	  
	  private String channel;
	  private Integer whatsAppTemplateMasterId;
	  private String whatsAppTemplateName;
	  private String whatsAppMessageType;

	  public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	private String templateId;
	
	public String getTo_number() {
		return to_number;
	}
	public void setTo_number(String to_number) {
		this.to_number = to_number;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getMediaUrl() {
		return mediaUrl;
	}
	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getWhatsAppCredentialId() {
		return whatsAppCredentialId;
	}
	public void setWhatsAppCredentialId(Integer whatsAppCredentialId) {
		this.whatsAppCredentialId = whatsAppCredentialId;
	}
	public String getAccountSid() {
		return accountSid;
	}
	public void setAccountSid(String accountSid) {
		this.accountSid = accountSid;
	}
	public String getAuthToken() {
		return authToken;
	}
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	public String getFromNumber() {
		return fromNumber;
	}
	public void setFromNumber(String fromNumber) {
		this.fromNumber = fromNumber;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMessage_type() {
		return message_type;
	}
	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public Integer getTypeOf() {
		return typeOf;
	}
	public void setTypeOf(Integer typeOf) {
		this.typeOf = typeOf;
	}
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public String getResponseJson() {
		return responseJson;
	}
	public void setResponseJson(Object object) {
		this.responseJson = (String) object;
	}

	public Integer getWhatsAppTemplateMasterId() {
		return whatsAppTemplateMasterId;
	}

	public void setWhatsAppTemplateMasterId(Integer whatsAppTemplateMasterId) {
		this.whatsAppTemplateMasterId = whatsAppTemplateMasterId;
	}

	public String getWhatsAppTemplateName() {
		return whatsAppTemplateName;
	}

	public void setWhatsAppTemplateName(String whatsAppTemplateName) {
		this.whatsAppTemplateName = whatsAppTemplateName;
	}

	public String getWhatsAppMessageType() {
		return whatsAppMessageType;
	}

	public void setWhatsAppMessageType(String whatsAppMessageType) {
		this.whatsAppMessageType = whatsAppMessageType;
	}
}
