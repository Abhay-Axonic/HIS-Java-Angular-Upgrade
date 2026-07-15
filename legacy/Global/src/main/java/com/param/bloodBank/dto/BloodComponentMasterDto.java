package com.param.bloodBank.dto;

public class BloodComponentMasterDto {
	
	private Integer bloodComponentId;
	
	private String bloodComponentCode;
	
	private String bloodComponentDesc;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer expiry;

	private String bloodComponentShortDesc;

	private Integer quantity;
	
	private Character byCrossChecking;
	
	private Integer unitId;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;

	public Integer getBloodComponentId() {
		return bloodComponentId;
	}

	public void setBloodComponentId(Integer bloodComponentId) {
		this.bloodComponentId = bloodComponentId;
	}

	public String getBloodComponentCode() {
		return bloodComponentCode;
	}

	public void setBloodComponentCode(String bloodComponentCode) {
		this.bloodComponentCode = bloodComponentCode;
	}

	public String getBloodComponentDesc() {
		return bloodComponentDesc;
	}

	public void setBloodComponentDesc(String bloodComponentDesc) {
		this.bloodComponentDesc = bloodComponentDesc;
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

	public Integer getExpiry() {
		return expiry;
	}

	public void setExpiry(Integer expiry) {
		this.expiry = expiry;
	}

	public String getBloodComponentShortDesc() {
		return bloodComponentShortDesc;
	}

	public void setBloodComponentShortDesc(String bloodComponentShortDesc) {
		this.bloodComponentShortDesc = bloodComponentShortDesc;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Character getByCrossChecking() {
		return byCrossChecking;
	}

	public void setByCrossChecking(Character byCrossChecking) {
		this.byCrossChecking = byCrossChecking;
	}
	
	
}
