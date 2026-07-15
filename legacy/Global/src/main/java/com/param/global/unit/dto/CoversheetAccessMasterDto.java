package com.param.global.unit.dto;

import java.util.Date;

import javax.persistence.Column;

public class CoversheetAccessMasterDto {

	private Integer coversheetAccessMasterId;
	
	private Integer coversheetId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private Date createdDate;
	
	private Integer updatedBy;
	
	private Date updatedDate;

	public Integer getCoversheetAccessMasterId() {
		return coversheetAccessMasterId;
	}

	public void setCoversheetAccessMasterId(Integer coversheetAccessMasterId) {
		this.coversheetAccessMasterId = coversheetAccessMasterId;
	}

	

	public Integer getCoversheetId() {
		return coversheetId;
	}

	public void setCoversheetId(Integer coversheetId) {
		this.coversheetId = coversheetId;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	
}
