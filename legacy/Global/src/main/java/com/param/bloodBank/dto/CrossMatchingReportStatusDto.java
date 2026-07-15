package com.param.bloodBank.dto;


public class CrossMatchingReportStatusDto {

	private Integer crossMatchingReportId;
	
	private Integer crossMatchingReportName;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;

	public Integer getCrossMatchingReportId() {
		return crossMatchingReportId;
	}

	public void setCrossMatchingReportId(Integer crossMatchingReportId) {
		this.crossMatchingReportId = crossMatchingReportId;
	}

	public Integer getCrossMatchingReportName() {
		return crossMatchingReportName;
	}

	public void setCrossMatchingReportName(Integer crossMatchingReportName) {
		this.crossMatchingReportName = crossMatchingReportName;
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
