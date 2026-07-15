package com.param.global.dto;

import javax.persistence.Column;

public class OrgWiseVitalMapperDto {
	private Integer orgWiseVitalMpprId;
	
	private Integer vitalId;
	
	private Integer vitalOrderOpd;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;
	
	private Integer organizationId;

	public Integer getOrgWiseVitalMpprId() {
		return orgWiseVitalMpprId;
	}

	public void setOrgWiseVitalMpprId(Integer orgWiseVitalMpprId) {
		this.orgWiseVitalMpprId = orgWiseVitalMpprId;
	}

	public Integer getVitalId() {
		return vitalId;
	}

	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}

	public Integer getVitalOrderOpd() {
		return vitalOrderOpd;
	}

	public void setVitalOrderOpd(Integer vitalOrderOpd) {
		this.vitalOrderOpd = vitalOrderOpd;
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
	
	
}
