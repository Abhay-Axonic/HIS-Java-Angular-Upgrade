package com.param.global.dto;

public class NewBornVitalsDto {

	private int vitalIds;

	private int vitalId;	
	
	private String vitalName;
	
	private String vitalValue;
	
	private char status;

	private int createdBy;

	private String createdDate;

	private int updatedBy;

	private String updatedDate;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;
	
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

	public int getVitalIds() {
		return vitalIds;
	}

	public void setVitalIds(int vitalIds) {
		this.vitalIds = vitalIds;
	}

	public int getVitalId() {
		return vitalId;
	}

	public void setVitalId(int vitalId) {
		this.vitalId = vitalId;
	}

	public String getVitalValue() {
		return vitalValue;
	}

	public void setVitalValue(String vitalValue) {
		this.vitalValue = vitalValue;
	}

	public String getVitalName() {
		return vitalName;
	}

	public void setVitalName(String vitalName) {
		this.vitalName = vitalName;
	}

	

}
