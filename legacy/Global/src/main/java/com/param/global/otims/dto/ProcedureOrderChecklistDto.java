package com.param.global.otims.dto;

import java.util.List;

public class ProcedureOrderChecklistDto {

	private Integer procedureOrderChecklistId;
	
	private Integer procedureRequestId;
	
	private Integer checklistId;
	
	private Integer procedureOrderId ;
	
	private char status;
			
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer stageId;
	
	private String roleName;
	
	private String description;
	
	private Integer procedureConfigChecklistId;
	
	private Integer procedureConfigId;
	
	private List<ProcedureOrderChecklistDto> otCheckList;
	
	private List<ProcedureOrderChecklistDto> checklistItemList;

	private Integer checklistStatusId; 
	
	private Integer otChecklistDetailsId;
	
	private Integer checklistItemMasterId;
	
	private Integer checklistMasterDetailsId;
	
	private String remark;
	
	private boolean checkStatus;
	
	private Integer roleId;
	
	private boolean userMappedChecklist;
	
	public boolean isCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(boolean checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Integer getChecklistStatusId() {
		return checklistStatusId;
	}

	public void setChecklistStatusId(Integer checklistStatusId) {
		this.checklistStatusId = checklistStatusId;
	}

	public Integer getProcedureOrderChecklistId() {
		return procedureOrderChecklistId;
	}

	public void setProcedureOrderChecklistId(Integer procedureOrderChecklistId) {
		this.procedureOrderChecklistId = procedureOrderChecklistId;
	}

	public Integer getProcedureRequestId() {
		return procedureRequestId;
	}

	public void setProcedureRequestId(Integer procedureRequestId) {
		this.procedureRequestId = procedureRequestId;
	}

	public Integer getChecklistId() {
		return checklistId;
	}

	public void setChecklistId(Integer checklistId) {
		this.checklistId = checklistId;
	}

	public Integer getProcedureOrderId() {
		return procedureOrderId;
	}

	public void setProcedureOrderId(Integer procedureOrderId) {
		this.procedureOrderId = procedureOrderId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
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

	public Integer getOtChecklistDetailsId() {
		return otChecklistDetailsId;
	}

	public void setOtChecklistDetailsId(Integer otChecklistDetailsId) {
		this.otChecklistDetailsId = otChecklistDetailsId;
	}

	public Integer getChecklistItemMasterId() {
		return checklistItemMasterId;
	}

	public void setChecklistItemMasterId(Integer checklistItemMasterId) {
		this.checklistItemMasterId = checklistItemMasterId;
	}

	public Integer getChecklistMasterDetailsId() {
		return checklistMasterDetailsId;
	}

	public void setChecklistMasterDetailsId(Integer checklistMasterDetailsId) {
		this.checklistMasterDetailsId = checklistMasterDetailsId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getStageId() {
		return stageId;
	}

	public void setStageId(Integer stageId) {
		this.stageId = stageId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getProcedureConfigChecklistId() {
		return procedureConfigChecklistId;
	}

	public void setProcedureConfigChecklistId(Integer procedureConfigChecklistId) {
		this.procedureConfigChecklistId = procedureConfigChecklistId;
	}

	public Integer getProcedureConfigId() {
		return procedureConfigId;
	}

	public void setProcedureConfigId(Integer procedureConfigId) {
		this.procedureConfigId = procedureConfigId;
	}
	
	public List<ProcedureOrderChecklistDto> getOtCheckList() {
		return otCheckList;
	}

	public void setOtCheckList(List<ProcedureOrderChecklistDto> otCheckList) {
		this.otCheckList = otCheckList;
	}

	public List<ProcedureOrderChecklistDto> getChecklistItemList() {
		return checklistItemList;
	}

	public void setChecklistItemList(List<ProcedureOrderChecklistDto> checklistItemList) {
		this.checklistItemList = checklistItemList;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public boolean isUserMappedChecklist() {
		return userMappedChecklist;
	}

	public void setUserMappedChecklist(boolean userMappedChecklist) {
		this.userMappedChecklist = userMappedChecklist;
	}



}
