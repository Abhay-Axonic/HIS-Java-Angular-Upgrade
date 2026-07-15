package com.param.bloodBank.dto;

public class DonorRejectionMasterDto {

	private Integer donorRejectionId;

	private String donorRejectionCode;
	
	private String donorRejectionDesc;

	private Integer createdBy;
		
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;

	public Integer getDonorRejectionId() {
		return donorRejectionId;
	}

	public void setDonorRejectionId(Integer donorRejectionId) {
		this.donorRejectionId = donorRejectionId;
	}

	public String getDonorRejectionCode() {
		return donorRejectionCode;
	}

	public void setDonorRejectionCode(String donorRejectionCode) {
		this.donorRejectionCode = donorRejectionCode;
	}

	public String getDonorRejectionDesc() {
		return donorRejectionDesc;
	}

	public void setDonorRejectionDesc(String donorRejectionDesc) {
		this.donorRejectionDesc = donorRejectionDesc;
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
