package com.param.global.dto;

import org.springframework.web.multipart.MultipartFile;

public class PatientConsentVideoDetailsDto {
	
	private Integer videoDetailsId;
	
	private Integer consentTransactionId;

	private String videoPath;
	
	private String videoName;
	
	private Character status;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private MultipartFile file;

	public Integer getVideoDetailsId() {
		return videoDetailsId;
	}

	public void setVideoDetailsId(Integer videoDetailsId) {
		this.videoDetailsId = videoDetailsId;
	}

	public Integer getConsentTransactionId() {
		return consentTransactionId;
	}

	public void setConsentTransactionId(Integer consentTransactionId) {
		this.consentTransactionId = consentTransactionId;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public String getVideoName() {
		return videoName;
	}

	public void setVideoName(String videoName) {
		this.videoName = videoName;
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

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

}
