package com.param.global.pharmacy.dto;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WorkOrderCreationRequestDocumentDto {
	
	  private Integer workorderRequestId;
	
     private Integer workorderRequestdocumentDetailsId;

	private Integer  documentTypeId;
	
	private String  documentPath;
	
	private String  documentTypeName;
	
	private char status;
	
    private String createdDate;
	
	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
    private Integer unitId;
	
	private Integer orgId;
	
	private String requestDocumentDate;
	
	private Integer bucketFlag;
	
	@JsonIgnore
	private MultipartFile file;

	public Integer getWorkorderRequestId() {
		return workorderRequestId;
	}

	public void setWorkorderRequestId(Integer workorderRequestId) {
		this.workorderRequestId = workorderRequestId;
	}

	public Integer getWorkorderRequestdocumentDetailsId() {
		return workorderRequestdocumentDetailsId;
	}

	public void setWorkorderRequestdocumentDetailsId(Integer workorderRequestdocumentDetailsId) {
		this.workorderRequestdocumentDetailsId = workorderRequestdocumentDetailsId;
	}

	public Integer getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Integer documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public String getDocumentTypeName() {
		return documentTypeName;
	}

	public void setDocumentTypeName(String documentTypeName) {
		this.documentTypeName = documentTypeName;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getRequestDocumentDate() {
		return requestDocumentDate;
	}

	public void setRequestDocumentDate(String requestDocumentDate) {
		this.requestDocumentDate = requestDocumentDate;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public Integer getBucketFlag() {
		return bucketFlag;
	}

	public void setBucketFlag(Integer bucketFlag) {
		this.bucketFlag = bucketFlag;
	} 
	
	

}
