package com.param.opd.encounter.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.param.global.dto.global.VisitType;

public class FundSanctionRequestDocumentDto {

	private Integer fundRequestdocumentDetailsId;
	
	private Integer fundsanctionrequestDetailsId;	

	private Integer patientId;
	
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
	
	@JsonIgnore
	private MultipartFile file;
	
	List<FundSanctionRequestDocumentDto> fundSanctionRequestDocument;


	public Integer getFundsanctionrequestDetailsId() {
		return fundsanctionrequestDetailsId;
	}

	public void setFundsanctionrequestDetailsId(Integer fundsanctionrequestDetailsId) {
		this.fundsanctionrequestDetailsId = fundsanctionrequestDetailsId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public List<FundSanctionRequestDocumentDto> getFundSanctionRequestDocument() {
		return fundSanctionRequestDocument;
	}

	public void setFundSanctionRequestDocument(List<FundSanctionRequestDocumentDto> fundSanctionRequestDocument) {
		this.fundSanctionRequestDocument = fundSanctionRequestDocument;
	}

	public Integer getFundRequestdocumentDetailsId() {
		return fundRequestdocumentDetailsId;
	}

	public void setFundRequestdocumentDetailsId(Integer fundRequestdocumentDetailsId) {
		this.fundRequestdocumentDetailsId = fundRequestdocumentDetailsId;
	}

	public String getRequestDocumentDate() {
		return requestDocumentDate;
	}

	public void setRequestDocumentDate(String requestDocumentDate) {
		this.requestDocumentDate = requestDocumentDate;
	}
	
	
}
