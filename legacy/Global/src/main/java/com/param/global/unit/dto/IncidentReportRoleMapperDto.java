package com.param.global.unit.dto;

import java.util.List;

import javax.persistence.Column;

public class IncidentReportRoleMapperDto {
	private Integer incidentReportRoleId;
	
	private Integer incidentReportId;
	
	private Integer roleId;
	
	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;

	private String updatedDate;
	private String roleName;
	private Character status;

	private Integer organizationId;
	private Integer incidentReportTemplateId;

	private Integer unitId;
	
	private Integer nabhProcessId;
	private List<IncidentReportTemplateMapperDto> incidentReportTemplateMapperList;
	private List<IncidentReportRoleMapperDto> incidentReportRoleMapperList;

	public Integer getIncidentReportRoleId() {
		return incidentReportRoleId;
	}

	public void setIncidentReportRoleId(Integer incidentReportRoleId) {
		this.incidentReportRoleId = incidentReportRoleId;
	}

	public Integer getIncidentReportId() {
		return incidentReportId;
	}

	public void setIncidentReportId(Integer incidentReportId) {
		this.incidentReportId = incidentReportId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public List<IncidentReportRoleMapperDto> getIncidentReportRoleMapperList() {
		return incidentReportRoleMapperList;
	}

	public void setIncidentReportRoleMapperList(List<IncidentReportRoleMapperDto> incidentReportRoleMapperList) {
		this.incidentReportRoleMapperList = incidentReportRoleMapperList;
	}

	public List<IncidentReportTemplateMapperDto> getIncidentReportTemplateMapperList() {
		return incidentReportTemplateMapperList;
	}

	public void setIncidentReportTemplateMapperList(
			List<IncidentReportTemplateMapperDto> incidentReportTemplateMapperList) {
		this.incidentReportTemplateMapperList = incidentReportTemplateMapperList;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Integer getIncidentReportTemplateId() {
		return incidentReportTemplateId;
	}

	public void setIncidentReportTemplateId(Integer incidentReportTemplateId) {
		this.incidentReportTemplateId = incidentReportTemplateId;
	}

	public Integer getNabhProcessId() {
		return nabhProcessId;
	}

	public void setNabhProcessId(Integer nabhProcessId) {
		this.nabhProcessId = nabhProcessId;
	}
	
	
	
}
