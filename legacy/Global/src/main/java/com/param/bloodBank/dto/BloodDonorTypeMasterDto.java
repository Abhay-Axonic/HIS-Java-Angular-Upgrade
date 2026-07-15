package com.param.bloodBank.dto;


public class BloodDonorTypeMasterDto {

	private Integer bloodDonorTypeId;
	
	private String bloodDonorTypeCode;
	
	private String bloodDonorTypeDesc;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;

	public Integer getBloodDonorTypeId() {
		return bloodDonorTypeId;
	}

	public void setBloodDonorTypeId(Integer bloodDonorTypeId) {
		this.bloodDonorTypeId = bloodDonorTypeId;
	}

	public String getBloodDonorTypeCode() {
		return bloodDonorTypeCode;
	}

	public void setBloodDonorTypeCode(String bloodDonorTypeCode) {
		this.bloodDonorTypeCode = bloodDonorTypeCode;
	}

	public String getBloodDonorTypeDesc() {
		return bloodDonorTypeDesc;
	}

	public void setBloodDonorTypeDesc(String bloodDonorTypeDesc) {
		this.bloodDonorTypeDesc = bloodDonorTypeDesc;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	
	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
	
}
