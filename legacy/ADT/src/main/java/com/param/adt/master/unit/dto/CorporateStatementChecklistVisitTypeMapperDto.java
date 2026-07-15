package com.param.adt.master.unit.dto;

import javax.persistence.Column;

public class CorporateStatementChecklistVisitTypeMapperDto {

	private Integer corporateStatementChecklistVisitTypeMapperId;
	
	private Integer corporateStatementChecklistId;
	
	private Integer visitTypeId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;

	
	public Integer getCorporateStatementChecklistVisitTypeMapperId() {
		return corporateStatementChecklistVisitTypeMapperId;
	}

	public void setCorporateStatementChecklistVisitTypeMapperId(Integer corporateStatementChecklistVisitTypeMapperId) {
		this.corporateStatementChecklistVisitTypeMapperId = corporateStatementChecklistVisitTypeMapperId;
	}

	public Integer getCorporateStatementChecklistId() {
		return corporateStatementChecklistId;
	}

	public void setCorporateStatementChecklistId(Integer corporateStatementChecklistId) {
		this.corporateStatementChecklistId = corporateStatementChecklistId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
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
	
}
