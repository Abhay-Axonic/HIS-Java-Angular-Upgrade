package com.param.global.dto;

import java.util.List;

public class CorporateStatementDetailsChecklistDto {
	
	private Integer corporateStatementDetailsChecklistId;
	
	private Integer corporateStatementChecklistVisitTypeMapperId;
	
	private Integer corporateStatementChecklistId;
	
	private Integer corporateStatementId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private List<ListOfDetailsChecklistDto> ListOfDetailsChecklistDto;

	public Integer getCorporateStatementDetailsChecklistId() {
		return corporateStatementDetailsChecklistId;
	}

	public Integer getCorporateStatementChecklistVisitTypeMapperId() {
		return corporateStatementChecklistVisitTypeMapperId;
	}

	public Integer getCorporateStatementChecklistId() {
		return corporateStatementChecklistId;
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

	public List<ListOfDetailsChecklistDto> getListOfDetailsChecklistDto() {
		return ListOfDetailsChecklistDto;
	}

	public void setListOfDetailsChecklistDto(List<ListOfDetailsChecklistDto> listOfDetailsChecklistDto) {
		ListOfDetailsChecklistDto = listOfDetailsChecklistDto;
	}

	public void setCorporateStatementDetailsChecklistId(Integer corporateStatementDetailsChecklistId) {
		this.corporateStatementDetailsChecklistId = corporateStatementDetailsChecklistId;
	}

	public void setCorporateStatementChecklistVisitTypeMapperId(Integer corporateStatementChecklistVisitTypeMapperId) {
		this.corporateStatementChecklistVisitTypeMapperId = corporateStatementChecklistVisitTypeMapperId;
	}

	public void setCorporateStatementChecklistId(Integer corporateStatementChecklistId) {
		this.corporateStatementChecklistId = corporateStatementChecklistId;
	}

	public Integer getCorporateStatementId() {
		return corporateStatementId;
	}

	public void setCorporateStatementId(Integer corporateStatementId) {
		this.corporateStatementId = corporateStatementId;
	}
	
	
	

}
