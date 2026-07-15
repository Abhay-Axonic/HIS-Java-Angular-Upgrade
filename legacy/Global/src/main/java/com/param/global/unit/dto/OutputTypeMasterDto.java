package com.param.global.unit.dto;

import java.util.Date;

import javax.persistence.Column;

public class OutputTypeMasterDto {
	
	private Integer outputTypeId;
	
	private String outputTypeCode;
	
	private String outputTypeDesc;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private Date createdDate;
	
	private Integer updatedBy;
	
	private Date updatedDate;

	public Integer getOutputTypeId() {
		return outputTypeId;
	}

	public void setOutputTypeId(Integer outputTypeId) {
		this.outputTypeId = outputTypeId;
	}

	public String getOutputTypeCode() {
		return outputTypeCode;
	}

	public void setOutputTypeCode(String outputTypeCode) {
		this.outputTypeCode = outputTypeCode;
	}

	public String getOutputTypeDesc() {
		return outputTypeDesc;
	}

	public void setOutputTypeDesc(String outputTypeDesc) {
		this.outputTypeDesc = outputTypeDesc;
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
