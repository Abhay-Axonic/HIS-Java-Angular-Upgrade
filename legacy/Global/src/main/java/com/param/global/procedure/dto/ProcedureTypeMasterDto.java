package com.param.global.procedure.dto;

public class ProcedureTypeMasterDto {
	
	private int procedureTypeId;
	
	private String procedureTypeName;
	
	private String procedureTypeDesc;
	
	private String procedureTypeCode;
	
	private char status;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	public int getProcedureTypeId() {
		return procedureTypeId;
	}
	public void setProcedureTypeId(int procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}
	public String getProcedureTypeName() {
		return procedureTypeName;
	}
	public void setProcedureTypeName(String procedureTypeName) {
		this.procedureTypeName = procedureTypeName;
	}
	public String getProcedureTypeDesc() {
		return procedureTypeDesc;
	}
	public void setProcedureTypeDesc(String procedureTypeDesc) {
		this.procedureTypeDesc = procedureTypeDesc;
	}
	public String getProcedureTypeCode() {
		return procedureTypeCode;
	}
	public void setProcedureTypeCode(String procedureTypeCode) {
		this.procedureTypeCode = procedureTypeCode;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
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
