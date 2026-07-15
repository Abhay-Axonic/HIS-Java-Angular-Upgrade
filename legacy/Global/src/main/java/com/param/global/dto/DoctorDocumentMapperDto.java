package com.param.global.dto;

import org.springframework.web.multipart.MultipartFile;

public class DoctorDocumentMapperDto {
	
	private Integer doctorDocId;
	
	private Integer doctorId;
	
	private String registrationNumber;
	
	private Integer qualificationId;
	
	private String qualificationDesc;
	
	private Integer degreeId;
	
	private String documentFilePath;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status; 

	private Integer createdBy;

	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;

	private byte[] file;
	private String organizationFolder;
	
	private String degreeDesc;

	private String fileName;
	
	public Integer getDoctorDocId() {
		return doctorDocId;
	}

	public void setDoctorDocId(Integer doctorDocId) {
		this.doctorDocId = doctorDocId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Integer getQualificationId() {
		return qualificationId;
	}

	public void setQualificationId(Integer qualificationId) {
		this.qualificationId = qualificationId;
	}

	public String getQualificationDesc() {
		return qualificationDesc;
	}

	public void setQualificationDesc(String qualificationDesc) {
		this.qualificationDesc = qualificationDesc;
	}

	public Integer getDegreeId() {
		return degreeId;
	}

	public void setDegreeId(Integer degreeId) {
		this.degreeId = degreeId;
	}

	public String getDocumentFilePath() {
		return documentFilePath;
	}

	public void setDocumentFilePath(String documentFilePath) {
		this.documentFilePath = documentFilePath;
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

	

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getOrganizationFolder() {
		return organizationFolder;
	}

	public void setOrganizationFolder(String organizationFolder) {
		this.organizationFolder = organizationFolder;
	}

	public String getDegreeDesc() {
		return degreeDesc;
	}

	public void setDegreeDesc(String degreeDesc) {
		this.degreeDesc = degreeDesc;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
