package com.param.global.unit.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class UserIndetificationDetailsDto {
	
	private Integer userIndetificationDetailsId;
	
	private Integer identificationTypeId;
	
	private String identificationNumber;
	
	private String identificationExpiryDate;
	
	private String doctorIndetificationFile;
	
	private Character status;
	
	private Integer organizationId;
	
	private Integer unitId;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;
	
	private Integer userId;
	
	private Integer doctorId;
	
	private String documentFilePath;
	
	private byte[] file;
	
	private String fileName;
	
	private Integer employeeId;

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getUserIndetificationDetailsId() {
		return userIndetificationDetailsId;
	}

	public void setUserIndetificationDetailsId(Integer userIndetificationDetailsId) {
		this.userIndetificationDetailsId = userIndetificationDetailsId;
	}

	public Integer getIdentificationTypeId() {
		return identificationTypeId;
	}

	public void setIdentificationTypeId(Integer identificationTypeId) {
		this.identificationTypeId = identificationTypeId;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	

	public String getIdentificationExpiryDate() {
		return identificationExpiryDate;
	}

	public void setIdentificationExpiryDate(String identificationExpiryDate) {
		this.identificationExpiryDate = identificationExpiryDate;
	}

	public String getDoctorIndetificationFile() {
		return doctorIndetificationFile;
	}

	public void setDoctorIndetificationFile(String doctorIndetificationFile) {
		this.doctorIndetificationFile = doctorIndetificationFile;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}


	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getDocumentFilePath() {
		return documentFilePath;
	}

	public void setDocumentFilePath(String documentFilePath) {
		this.documentFilePath = documentFilePath;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	

}
