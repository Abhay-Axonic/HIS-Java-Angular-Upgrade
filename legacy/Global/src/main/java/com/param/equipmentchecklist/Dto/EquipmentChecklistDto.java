package com.param.equipmentchecklist.Dto;

public class EquipmentChecklistDto {

	private Integer checklistEquipmentMasterId;

	private String checklistCode;

	private String checklistName;
	
	private Character status;

	private Integer organizationId;

	private Integer unitId;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;
	
	
	 private Integer offset;
		
	private Integer noOfRecordsPerPage;


	public Integer getChecklistEquipmentMasterId() {
		return checklistEquipmentMasterId;
	}

	public void setChecklistEquipmentMasterId(Integer checklistEquipmentMasterId) {
		this.checklistEquipmentMasterId = checklistEquipmentMasterId;
	}

	public String getChecklistCode() {
		return checklistCode;
	}

	public void setChecklistCode(String checklistCode) {
		this.checklistCode = checklistCode;
	}

	public String getChecklistName() {
		return checklistName;
	}

	public void setChecklistName(String checklistName) {
		this.checklistName = checklistName;
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
