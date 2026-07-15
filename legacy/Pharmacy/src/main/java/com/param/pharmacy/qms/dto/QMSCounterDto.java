package com.param.pharmacy.qms.dto;

public class QMSCounterDto {

	private Integer qmsCounterId;
	
	private String qmsCounterCode;
	
	private String qmsCounterDesc;
	
	private Character status ;
	
	private Integer unitId ;
	
	private Integer organizationId;
	
	private Integer createdBy ;
	
	private Integer updatedBy ;
	
	private String createdDate ;
	
	private String updatedDate;

	public Integer getQmsCounterId() {
		return qmsCounterId;
	}

	public void setQmsCounterId(Integer qmsCounterId) {
		this.qmsCounterId = qmsCounterId;
	}

	public String getQmsCounterCode() {
		return qmsCounterCode;
	}

	public void setQmsCounterCode(String qmsCounterCode) {
		this.qmsCounterCode = qmsCounterCode;
	}

	public String getQmsCounterDesc() {
		return qmsCounterDesc;
	}

	public void setQmsCounterDesc(String qmsCounterDesc) {
		this.qmsCounterDesc = qmsCounterDesc;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
}
