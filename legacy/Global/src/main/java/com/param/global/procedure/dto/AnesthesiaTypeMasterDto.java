package com.param.global.procedure.dto;

public class AnesthesiaTypeMasterDto {
	private int anesthesiaTypeId;
	private String anesthesiaTypeDesc;
	private String anesthesiaTypeName;
	private String anesthesiaTypeCode;
	private char status;
	private Integer orgId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private Integer offset;
	private Integer noOfRecordsPerPage;
	
	public int getAnesthesiaTypeId() {
		return anesthesiaTypeId;
	}
	public void setAnesthesiaTypeId(int anesthesiaTypeId) {
		this.anesthesiaTypeId = anesthesiaTypeId;
	}
	public String getAnesthesiaTypeDesc() {
		return anesthesiaTypeDesc;
	}
	public void setAnesthesiaTypeDesc(String anesthesiaTypeDesc) {
		this.anesthesiaTypeDesc = anesthesiaTypeDesc;
	}
	public String getAnesthesiaTypeName() {
		return anesthesiaTypeName;
	}
	public void setAnesthesiaTypeName(String anesthesiaTypeName) {
		this.anesthesiaTypeName = anesthesiaTypeName;
	}
	public String getAnesthesiaTypeCode() {
		return anesthesiaTypeCode;
	}
	public void setAnesthesiaTypeCode(String anesthesiaTypeCode) {
		this.anesthesiaTypeCode = anesthesiaTypeCode;
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
