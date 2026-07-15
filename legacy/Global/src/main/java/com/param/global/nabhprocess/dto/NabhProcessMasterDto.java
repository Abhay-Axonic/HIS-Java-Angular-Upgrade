package com.param.global.nabhprocess.dto;

import java.util.List;

import javax.persistence.Column;
import com.param.global.unit.dto.IncidentReportRoleMapperDto;
import com.param.global.unit.dto.IncidentReportTemplateMapperDto;

public class NabhProcessMasterDto {

private Integer incidentReportRoleId;
	
private Integer nabhProcessId;

private String nabhProcessCode;

private String nabhProcessDesc;

private String createdDate;

private Integer createdBy;

private Integer updatedBy;

private String updatedDate;
private String title;
private String roleName;
private String templateObject;

private Character status;

private Character isCapaOff;

private Integer organizationId;

private Integer unitId;

private Integer roleId;

private Integer templateId;
private Integer offset;
private Integer noOfRecordsPerPage;

private List<IncidentReportRoleMapperDto> incidentReportRoleMapperList;
private List<IncidentReportTemplateMapperDto> incidentReportTemplateMapperList;
public Integer getIncidentReportRoleId() {
	return incidentReportRoleId;
}
public void setIncidentReportRoleId(Integer incidentReportRoleId) {
	this.incidentReportRoleId = incidentReportRoleId;
}
public Integer getNabhProcessId() {
	return nabhProcessId;
}
public void setNabhProcessId(Integer nabhProcessId) {
	this.nabhProcessId = nabhProcessId;
}
public String getNabhProcessCode() {
	return nabhProcessCode;
}
public void setNabhProcessCode(String nabhProcessCode) {
	this.nabhProcessCode = nabhProcessCode;
}
public String getNabhProcessDesc() {
	return nabhProcessDesc;
}
public void setNabhProcessDesc(String nabhProcessDesc) {
	this.nabhProcessDesc = nabhProcessDesc;
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
public String getTitle() {
	return title;
}
public void setTitle(String title) {
	this.title = title;
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
public Character getIsCapaOff() {
	return isCapaOff;
}
public void setIsCapaOff(Character isCapaOff) {
	this.isCapaOff = isCapaOff;
}



}
