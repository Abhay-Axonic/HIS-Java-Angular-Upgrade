package com.param.global.otims.dto;


public class ProcedureRoleMasterDto {
	
	private Integer procedureRoleId;
	
	private String procedureRoleCode;
	
	private String procedureRoleName;

	private String procedureRoleDesc;

	private Character status;

	private Integer orgId;

	private Integer unitId;
	
	private Integer createdBy;

	private String createdDate;
	
	private Integer updatedBy;

	private String updatedDate;
	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;

	public Integer getProcedureRoleId() {
		return procedureRoleId;
	}

	public void setProcedureRoleId(Integer procedureRoleId) {
		this.procedureRoleId = procedureRoleId;
	}

	public String getProcedureRoleCode() {
		return procedureRoleCode;
	}

	public void setProcedureRoleCode(String procedureRoleCode) {
		this.procedureRoleCode = procedureRoleCode;
	}

	public String getProcedureRoleName() {
		return procedureRoleName;
	}

	public void setProcedureRoleName(String procedureRoleName) {
		this.procedureRoleName = procedureRoleName;
	}

	

	public String getProcedureRoleDesc() {
		return procedureRoleDesc;
	}

	public void setProcedureRoleDesc(String procedureRoleDesc) {
		this.procedureRoleDesc = procedureRoleDesc;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
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
