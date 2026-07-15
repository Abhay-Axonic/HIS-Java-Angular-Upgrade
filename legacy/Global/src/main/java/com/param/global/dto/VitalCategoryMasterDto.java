package com.param.global.dto;

import java.util.Date;


public class VitalCategoryMasterDto {
	
	private Integer vitalCategoryId;
	
	private String vitalCategoryCode;
	
	private String vitalCategoryDesc;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;

	public Integer getVitalCategoryId() {
		return vitalCategoryId;
	}

	public void setVitalCategoryId(Integer vitalCategoryId) {
		this.vitalCategoryId = vitalCategoryId;
	}

	public String getVitalCategoryCode() {
		return vitalCategoryCode;
	}

	public void setVitalCategoryCode(String vitalCategoryCode) {
		this.vitalCategoryCode = vitalCategoryCode;
	}

	public String getVitalCategoryDesc() {
		return vitalCategoryDesc;
	}

	public void setVitalCategoryDesc(String vitalCategoryDesc) {
		this.vitalCategoryDesc = vitalCategoryDesc;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
