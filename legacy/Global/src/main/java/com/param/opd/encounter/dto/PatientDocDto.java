package com.param.opd.encounter.dto;

import java.util.Date;

public class PatientDocDto {

	
	private String documentName;

	private String documentPath;
	
	private Integer documentTypeId;

	private String createdDate;
	
	private String uploadDoccumentDate;

	private String fileName;
	
    private String remark;
    
    private Integer bucketFlag;
    
    private Integer docTypeId;
    
    private Integer encounterDocId;
    
    private Integer nabhReportingMapperId;

	private Integer incidentReportingMapperId;

	private String  videolink;
	private Date analyzedDocDate;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public Integer getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Integer documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUploadDoccumentDate() {
		return uploadDoccumentDate;
	}

	public void setUploadDoccumentDate(String uploadDoccumentDate) {
		this.uploadDoccumentDate = uploadDoccumentDate;
	}

	public Integer getBucketFlag() {
		return bucketFlag;
	}

	public void setBucketFlag(Integer bucketFlag) {
		this.bucketFlag = bucketFlag;
	}

	public Integer getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(Integer docTypeId) {
		this.docTypeId = docTypeId;
	}

	public Integer getEncounterDocId() {
		return encounterDocId;
	}

	public void setEncounterDocId(Integer encounterDocId) {
		this.encounterDocId = encounterDocId;
	}

	public Integer getNabhReportingMapperId() {
		return nabhReportingMapperId;
	}

	public void setNabhReportingMapperId(Integer nabhReportingMapperId) {
		this.nabhReportingMapperId = nabhReportingMapperId;
	}

	public Integer getIncidentReportingMapperId() {
		return incidentReportingMapperId;
	}

	public void setIncidentReportingMapperId(Integer incidentReportingMapperId) {
		this.incidentReportingMapperId = incidentReportingMapperId;
	}

	public String getVideolink() {
		return videolink;
	}

	public void setVideolink(String videolink) {
		this.videolink = videolink;
	}

	public Date getAnalyzedDocDate() {
		return analyzedDocDate;
	}

	public void setAnalyzedDocDate(Date analyzedDocDate) {
		this.analyzedDocDate = analyzedDocDate;
	}
}
