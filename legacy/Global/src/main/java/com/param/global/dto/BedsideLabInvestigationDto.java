package com.param.global.dto;

import java.util.Date;

import javax.persistence.Column;

public class BedsideLabInvestigationDto {
	
	private Integer bedsideLabId;
	
	private String bedsideLabCode;
	
	private String bedsideLabDesc;
	
	private Integer maxReferenceRange;
	
	private Integer minReferenceRange;
	
	private Integer bedsideUnitId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String unitName;

	public Integer getBedsideLabId() {
		return bedsideLabId;
	}

	public void setBedsideLabId(Integer bedsideLabId) {
		this.bedsideLabId = bedsideLabId;
	}

	public String getBedsideLabCode() {
		return bedsideLabCode;
	}

	public void setBedsideLabCode(String bedsideLabCode) {
		this.bedsideLabCode = bedsideLabCode;
	}

	public String getBedsideLabDesc() {
		return bedsideLabDesc;
	}

	public void setBedsideLabDesc(String bedsideLabDesc) {
		this.bedsideLabDesc = bedsideLabDesc;
	}

	public Integer getMaxReferenceRange() {
		return maxReferenceRange;
	}

	public void setMaxReferenceRange(Integer maxReferenceRange) {
		this.maxReferenceRange = maxReferenceRange;
	}

	public Integer getMinReferenceRange() {
		return minReferenceRange;
	}

	public void setMinReferenceRange(Integer minReferenceRange) {
		this.minReferenceRange = minReferenceRange;
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

	public Integer getBedsideUnitId() {
		return bedsideUnitId;
	}

	public void setBedsideUnitId(Integer bedsideUnitId) {
		this.bedsideUnitId = bedsideUnitId;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	
	

}
