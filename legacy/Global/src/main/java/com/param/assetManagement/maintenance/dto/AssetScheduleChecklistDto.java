package com.param.assetManagement.maintenance.dto;

public class AssetScheduleChecklistDto {

	private Integer assetScheduleChecklistId;
	
	private Integer assetScheduleId;
	
	private Integer checklistNameId;
	
	private String checklistName;
	
	private String checklistCode;
	
	private Character status;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Integer unitId;
	
	private Integer organizationId;

	public Integer getAssetScheduleChecklistId() {
		return assetScheduleChecklistId;
	}

	public void setAssetScheduleChecklistId(Integer assetScheduleChecklistId) {
		this.assetScheduleChecklistId = assetScheduleChecklistId;
	}

	public Integer getAssetScheduleId() {
		return assetScheduleId;
	}

	public void setAssetScheduleId(Integer assetScheduleId) {
		this.assetScheduleId = assetScheduleId;
	}

	public Integer getChecklistNameId() {
		return checklistNameId;
	}

	public void setChecklistNameId(Integer checklistNameId) {
		this.checklistNameId = checklistNameId;
	}

	public String getChecklistName() {
		return checklistName;
	}

	public void setChecklistName(String checklistName) {
		this.checklistName = checklistName;
	}

	public String getChecklistCode() {
		return checklistCode;
	}

	public void setChecklistCode(String checklistCode) {
		this.checklistCode = checklistCode;
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
	
	
}
