package com.param.global.dto;

import java.util.Date;


public class AllergyMasterDto {

	private Integer allergyId;
	private String allergyName;
	private String allergyDesc;
	private Integer allergyTypeId;
	private char status;
	private Integer createdBy;
	private Integer updatedBy;
	private String createdDate;
	private String updatedDate;
	private String marathiAllergyName;
	private Integer unitId;
	private Integer organizationId;
	private String allergyTypeDesc;

	private Integer offset;

	private Integer noOfRecordsPerPage;
	private String cimsCode;
	private String cimsType;
	
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
	public Integer getAllergyId() {
		return allergyId;
	}
	public void setAllergyId(Integer allergyId) {
		this.allergyId = allergyId;
	}
	public String getAllergyName() {
		return allergyName;
	}
	public void setAllergyName(String allergyName) {
		this.allergyName = allergyName;
	}
	public String getAllergyDesc() {
		return allergyDesc;
	}
	public void setAllergyDesc(String allergyDesc) {
		this.allergyDesc = allergyDesc;
	}
	public Integer getAllergyTypeId() {
		return allergyTypeId;
	}
	public void setAllergyTypeId(Integer allergyTypeId) {
		this.allergyTypeId = allergyTypeId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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
	public String getMarathiAllergyName() {
		return marathiAllergyName;
	}
	public void setMarathiAllergyName(String marathiAllergyName) {
		this.marathiAllergyName = marathiAllergyName;
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
	public String getAllergyTypeDesc() {
		return allergyTypeDesc;
	}
	public void setAllergyTypeDesc(String allergyTypeDesc) {
		this.allergyTypeDesc = allergyTypeDesc;
	}
	public String getCimsCode() {
		return cimsCode;
	}
	public void setCimsCode(String cimsCode) {
		this.cimsCode = cimsCode;
	}
	public String getCimsType() {
		return cimsType;
	}
	public void setCimsType(String cimsType) {
		this.cimsType = cimsType;
	}
	
	
}
