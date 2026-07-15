package com.param.bloodBank.dto;


public class BloodGroupMatchingMasterDto{

	
	private Integer bloodGroupMatchingId;
	
	private Integer recipientBloodGroupId;
	
	private Integer donorBloodGroupId;
	
	private Integer availability;
	
	private Integer componentId;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Integer unitId;
	
	private Integer organizationId;

	private Character status;
	
	private String createdDate;

	private String updatedDate;
	
	private Integer checkValue;
	
	private String bloodGroupDesc;

	public Integer getBloodGroupMatchingId() {
		return bloodGroupMatchingId;
	}

	public void setBloodGroupMatchingId(Integer bloodGroupMatchingId) {
		this.bloodGroupMatchingId = bloodGroupMatchingId;
	}

	public Integer getRecipientBloodGroupId() {
		return recipientBloodGroupId;
	}

	public void setRecipientBloodGroupId(Integer recipientBloodGroupId) {
		this.recipientBloodGroupId = recipientBloodGroupId;
	}

	public Integer getDonorBloodGroupId() {
		return donorBloodGroupId;
	}

	public void setDonorBloodGroupId(Integer donorBloodGroupId) {
		this.donorBloodGroupId = donorBloodGroupId;
	}

	public Integer getAvailability() {
		return availability;
	}

	public void setAvailability(Integer availability) {
		this.availability = availability;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getCheckValue() {
		return checkValue;
	}

	public void setCheckValue(Integer checkValue) {
		this.checkValue = checkValue;
	}

	public String getBloodGroupDesc() {
		return bloodGroupDesc;
	}

	public void setBloodGroupDesc(String bloodGroupDesc) {
		this.bloodGroupDesc = bloodGroupDesc;
	}
	
	

}
