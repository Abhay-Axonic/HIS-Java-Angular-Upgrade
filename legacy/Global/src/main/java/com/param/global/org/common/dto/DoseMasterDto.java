package com.param.global.org.common.dto;

public class DoseMasterDto {
	private Integer doseId;

	private String doseCode;

	private String doseDesc;

	private Character status;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Integer organizationId;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;

	public Integer getDoseId() {
		return doseId;
	}

	public void setDoseId(Integer doseId) {
		this.doseId = doseId;
	}

	public String getDoseCode() {
		return doseCode;
	}

	public void setDoseCode(String doseCode) {
		this.doseCode = doseCode;
	}

	public String getDoseDesc() {
		return doseDesc;
	}

	public void setDoseDesc(String doseDesc) {
		this.doseDesc = doseDesc;
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
	
}
