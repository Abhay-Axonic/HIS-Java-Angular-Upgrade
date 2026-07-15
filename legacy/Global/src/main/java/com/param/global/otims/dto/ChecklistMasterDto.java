package com.param.global.otims.dto;

import java.util.List;

public class ChecklistMasterDto {

	private Integer checklistMasterId;

	private String code;

	private String description;
	
	private Integer roleId;

	private Integer stageId;
	
	private boolean isWho;

	private Character status;

	private Integer organizationId;

	private Integer unitId;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;
	
	private String roleName;
	
	private List<ChecklistMasterDetailsDto> checklistMasterDetails;
	
    private Integer offset;

    private Integer noOfRecordsPerPage;
    
    private Integer procedureOrderId;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public List<ChecklistMasterDetailsDto> getChecklistMasterDetails() {
		return checklistMasterDetails;
	}

	public void setChecklistMasterDetails(List<ChecklistMasterDetailsDto> checklistMasterDetails) {
		this.checklistMasterDetails = checklistMasterDetails;
	}

	public Integer getChecklistMasterId() {
		return checklistMasterId;
	}

	public void setChecklistMasterId(Integer checklistMasterId) {
		this.checklistMasterId = checklistMasterId;
	}

	public boolean getIsWho() {
		return isWho;
	}

	public void setIsWho(boolean isWho) {
		this.isWho = isWho;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public void setWho(boolean isWho) {
		this.isWho = isWho;
	}

	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
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

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

}
