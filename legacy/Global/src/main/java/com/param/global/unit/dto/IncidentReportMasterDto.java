package com.param.global.unit.dto;


import java.util.List;


public class IncidentReportMasterDto {
private Integer incidentReportRoleId;
	
private Integer incidentReportId;

private String incidentReportCode;

private String incidentReportDesc;

private String createdDate;

private Integer createdBy;

private Integer updatedBy;

private String updatedDate;
private String title;
private String roleName;
private String templateObject;

private Character status;

private Integer organizationId;

private Integer unitId;

private Integer roleId;

private Integer templateId;
private Integer offset;
private Integer noOfRecordsPerPage;

private String searchKeyword;

private List<IncidentReportRoleMapperDto> incidentReportRoleMapperList;
private List<IncidentReportTemplateMapperDto> incidentReportTemplateMapperList;

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

public String getIncidentReportCode() {
	return incidentReportCode;
}

public void setIncidentReportCode(String incidentReportCode) {
	this.incidentReportCode = incidentReportCode;
}

public String getIncidentReportDesc() {
	return incidentReportDesc;
}

public void setIncidentReportDesc(String incidentReportDesc) {
	this.incidentReportDesc = incidentReportDesc;
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

public Integer getRoleId() {
	return roleId;
}

public void setRoleId(Integer roleId) {
	this.roleId = roleId;
}

public Integer getTemplateId() {
	return templateId;
}

public void setTemplateId(Integer templateId) {
	this.templateId = templateId;
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

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
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

public String getRoleName() {
	return roleName;
}

public void setRoleName(String roleName) {
	this.roleName = roleName;
}

public String getTemplateObject() {
	return templateObject;
}

public void setTemplateObject(String templateObject) {
	this.templateObject = templateObject;
}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
}
