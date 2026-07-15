package com.param.adt.master.global.dto;

import org.springframework.web.multipart.MultipartFile;

public class ConsentMasterDto {
	private int consentMasterId;

	private Integer consentTypeMasterId;

	private String consentTypeMasterName;

	private String consentMasterName;

	private String consentMasterCode;

	private char status;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;

	private Integer organizationId;

	private Integer offset;
	
	private Integer orgId;

	private Integer noOfRecordsPerPage;
	
	private String documentName;
	
	private String filePath;
	
	private MultipartFile file;
	
	private String template;
	
	private Integer templateType;
	
	private Integer unitId;
	
	private String formTemplateId;
	
	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public int getConsentMasterId() {
		return consentMasterId;
	}

	public void setConsentMasterId(int consentMasterId) {
		this.consentMasterId = consentMasterId;
	}

	public Integer getConsentTypeMasterId() {
		return consentTypeMasterId;
	}

	public void setConsentTypeMasterId(Integer consentTypeMasterId) {
		this.consentTypeMasterId = consentTypeMasterId;
	}

	public String getConsentTypeMasterName() {
		return consentTypeMasterName;
	}

	public void setConsentTypeMasterName(String consentTypeMasterName) {
		this.consentTypeMasterName = consentTypeMasterName;
	}

	public String getConsentMasterName() {
		return consentMasterName;
	}

	public void setConsentMasterName(String consentMasterName) {
		this.consentMasterName = consentMasterName;
	}

	public String getConsentMasterCode() {
		return consentMasterCode;
	}

	public void setConsentMasterCode(String consentMasterCode) {
		this.consentMasterCode = consentMasterCode;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public String getFormTemplateId() {
		return formTemplateId;
	}

	public void setFormTemplateId(String formTemplateId) {
		this.formTemplateId = formTemplateId;
	}
	
	
	
}
