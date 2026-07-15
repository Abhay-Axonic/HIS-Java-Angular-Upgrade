package com.param.bloodBank.dto;

public class BloodTestResultMasterDto {

	private Integer bloodTestResultId;
	
	private String bloodTestResultCode;
	
	private String bloodTestResultDesc;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;

	public Integer getBloodTestResultId() {
		return bloodTestResultId;
	}

	public void setBloodTestResultId(Integer bloodTestResultId) {
		this.bloodTestResultId = bloodTestResultId;
	}

	public String getBloodTestResultCode() {
		return bloodTestResultCode;
	}

	public void setBloodTestResultCode(String bloodTestResultCode) {
		this.bloodTestResultCode = bloodTestResultCode;
	}

	public String getBloodTestResultDesc() {
		return bloodTestResultDesc;
	}

	public void setBloodTestResultDesc(String bloodTestResultDesc) {
		this.bloodTestResultDesc = bloodTestResultDesc;
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

