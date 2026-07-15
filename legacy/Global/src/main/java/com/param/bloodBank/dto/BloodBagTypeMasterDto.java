package com.param.bloodBank.dto;


public class BloodBagTypeMasterDto {
	
	private Integer bloodBagTypeId;
	
	private String bloodBagTypeCode;
	
	private String bloodBagTypeDesc;
	
	private String itemName;

	private Integer capacityMax;

	private Integer capacityMin;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer offset;

	public Integer getBloodBagTypeId() {
		return bloodBagTypeId;
	}

	public void setBloodBagTypeId(Integer bloodBagTypeId) {
		this.bloodBagTypeId = bloodBagTypeId;
	}

	public String getBloodBagTypeCode() {
		return bloodBagTypeCode;
	}

	public void setBloodBagTypeCode(String bloodBagTypeCode) {
		this.bloodBagTypeCode = bloodBagTypeCode;
	}

	public String getBloodBagTypeDesc() {
		return bloodBagTypeDesc;
	}

	public void setBloodBagTypeDesc(String bloodBagTypeDesc) {
		this.bloodBagTypeDesc = bloodBagTypeDesc;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getCapacityMax() {
		return capacityMax;
	}

	public void setCapacityMax(Integer capacityMax) {
		this.capacityMax = capacityMax;
	}

	public Integer getCapacityMin() {
		return capacityMin;
	}

	public void setCapacityMin(Integer capacityMin) {
		this.capacityMin = capacityMin;
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
