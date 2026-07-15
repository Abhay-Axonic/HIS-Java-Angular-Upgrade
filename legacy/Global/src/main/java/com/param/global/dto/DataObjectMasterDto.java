package com.param.global.dto;

public class DataObjectMasterDto {
	private int dataObjectId;
	private String title;
	private String dataObject;
	private Integer orgId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private char status;
	public int getDataObjectId() {
		return dataObjectId;
	}
	public void setDataObjectId(int dataObjectId) {
		this.dataObjectId = dataObjectId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDataObject() {
		return dataObject;
	}
	public void setDataObject(String dataObject) {
		this.dataObject = dataObject;
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
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
}
