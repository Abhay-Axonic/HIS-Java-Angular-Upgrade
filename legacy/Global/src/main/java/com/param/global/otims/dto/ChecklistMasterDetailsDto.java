package com.param.global.otims.dto;

public class ChecklistMasterDetailsDto {

	private Integer checklistMasterDetailsId;

	private Integer checklistMasterId;

	private Integer checklistItemMasterId;

	private Character status;

	private Integer organizationId;

	private Integer unitId;

	private Integer createdBy;

	private String createdDate;
	
	private String description;

	private Integer updatedBy;

	private String updatedDate;
	
	private Integer checklistStatusId;
	
	private String remark;
	
	private boolean checkStatus;
	
	private Integer otChecklistDetailsId;
	
	private Integer stageId;

	public Integer getChecklistMasterDetailsId() {
		return checklistMasterDetailsId;
	}

	public void setChecklistMasterDetailsId(Integer checklistMasterDetailsId) {
		this.checklistMasterDetailsId = checklistMasterDetailsId;
	}

	public Integer getChecklistMasterId() {
		return checklistMasterId;
	}

	public void setChecklistMasterId(Integer checklistMasterId) {
		this.checklistMasterId = checklistMasterId;
	}

	public Integer getChecklistItemMasterId() {
		return checklistItemMasterId;
	}

	public void setChecklistItemMasterId(Integer checklistItemMasterId) {
		this.checklistItemMasterId = checklistItemMasterId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getChecklistStatusId() {
		return checklistStatusId;
	}

	public void setChecklistStatusId(Integer checklistStatusId) {
		this.checklistStatusId = checklistStatusId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(boolean checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Integer getOtChecklistDetailsId() {
		return otChecklistDetailsId;
	}

	public void setOtChecklistDetailsId(Integer otChecklistDetailsId) {
		this.otChecklistDetailsId = otChecklistDetailsId;
	}

	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}
	
}
