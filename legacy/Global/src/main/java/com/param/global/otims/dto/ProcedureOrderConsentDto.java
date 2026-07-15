package com.param.global.otims.dto;


public class ProcedureOrderConsentDto {

	private Integer procedureOrderConsentId;
	
	private Integer procedureRequestId;
	
	private Integer consentId ;
	
	private Integer procedureOrderId ;
	
	private char status;
			
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String consentMasterName ;
	
	private String consentTypeMasterName;
		
	private String filePath;
	
	private String documentName;
	
	private Integer templateType;
	
	private String template;
	
	private Integer procedureConfigConsentId;
	
	private Integer procedureConfigId;

	public Integer getProcedureOrderConsentId() {
		return procedureOrderConsentId;
	}

	public void setProcedureOrderConsentId(Integer procedureOrderConsentId) {
		this.procedureOrderConsentId = procedureOrderConsentId;
	}

	public Integer getProcedureRequestId() {
		return procedureRequestId;
	}

	public void setProcedureRequestId(Integer procedureRequestId) {
		this.procedureRequestId = procedureRequestId;
	}

	public Integer getConsentId() {
		return consentId;
	}

	public void setConsentId(Integer consentId) {
		this.consentId = consentId;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public String getConsentMasterName() {
		return consentMasterName;
	}

	public void setConsentMasterName(String consentMasterName) {
		this.consentMasterName = consentMasterName;
	}

	public String getConsentTypeMasterName() {
		return consentTypeMasterName;
	}

	public void setConsentTypeMasterName(String consentTypeMasterName) {
		this.consentTypeMasterName = consentTypeMasterName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Integer getProcedureConfigConsentId() {
		return procedureConfigConsentId;
	}

	public void setProcedureConfigConsentId(Integer procedureConfigConsentId) {
		this.procedureConfigConsentId = procedureConfigConsentId;
	}

	public Integer getProcedureConfigId() {
		return procedureConfigId;
	}

	public void setProcedureConfigId(Integer procedureConfigId) {
		this.procedureConfigId = procedureConfigId;
	}
}
