package com.param.adt.discharge.model;

import java.util.Date;

public class DischargeFieldUnitWiseDTO {

	private Integer dischargeFieldUnitWiseId;

	private Integer dischargeFieldId;

	private String dischargeChangedName;
	
	private String dischargeFieldDesc;

	private Integer createdBy;

	private String createdDate;

	private Character status;

	private Integer updatedBy;

	private String updatedDate;

	private Integer organizationId;

	private Integer unitId;

	private Integer offset;

	private Integer noOfRecordsPerPage;

	private String searchKeyword;

	public Integer getDischargeFieldUnitWiseId() {
		return dischargeFieldUnitWiseId;
	}

	public void setDischargeFieldUnitWiseId(Integer dischargeFieldUnitWiseId) {
		this.dischargeFieldUnitWiseId = dischargeFieldUnitWiseId;
	}

	public Integer getDischargeFieldId() {
		return dischargeFieldId;
	}

	public void setDischargeFieldId(Integer dischargeFieldId) {
		this.dischargeFieldId = dischargeFieldId;
	}

	public String getDischargeChangedName() {
		return dischargeChangedName;
	}

	public void setDischargeChangedName(String dischargeChangedName) {
		this.dischargeChangedName = dischargeChangedName;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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

	public String getDischargeFieldDesc() {
		return dischargeFieldDesc;
	}

	public void setDischargeFieldDesc(String dischargeFieldDesc) {
		this.dischargeFieldDesc = dischargeFieldDesc;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
}
