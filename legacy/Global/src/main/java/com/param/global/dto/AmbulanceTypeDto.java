package com.param.global.dto;

public class AmbulanceTypeDto {

	private int ambulanceTypeMasterId;
	
	private int ambulanceTypeId;

	private String AmbulanceType;

	private char status;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
	
	public int getAmbulanceTypeMasterId() {
		return ambulanceTypeMasterId;
	}

	public void setAmbulanceTypeMasterId(int ambulanceTypeMasterId) {
		this.ambulanceTypeMasterId = ambulanceTypeMasterId;
	}

	
	public int getAmbulanceTypeId() {
		return ambulanceTypeId;
	}

	public void setAmbulanceTypeId(int ambulanceTypeId) {
		this.ambulanceTypeId = ambulanceTypeId;
	}

	public String getAmbulanceType() {
		return AmbulanceType;
	}

	public void setAmbulanceType(String ambulanceType) {
		AmbulanceType = ambulanceType;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
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
