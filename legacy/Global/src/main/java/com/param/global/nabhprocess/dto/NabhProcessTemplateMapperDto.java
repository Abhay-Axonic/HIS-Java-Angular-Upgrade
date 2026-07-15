package com.param.global.nabhprocess.dto;

import java.util.List;

import com.param.global.nabhprocess.dto.NabhProcessTemplateMapperDto;

public class NabhProcessTemplateMapperDto {


	private Integer incidentReportTemplateId;

	private Integer templateId;

	private Integer incidentReportId;

	private Integer roleId;

	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;

	private String updatedDate;

	private Character status;

	private Integer organizationId;
	private Integer incidentReportRoleId;

	private Integer unitId;
	private String title;
	private String templateObject;
	private Integer templateFormatId;
	private Integer templateTypeId;
	
	private String formTemplateId;

	private List<NabhProcessTemplateMapperDto> incidentReportTemplateMapperList;

	public Integer getIncidentReportTemplateId() {
		return incidentReportTemplateId;
	}

	public void setIncidentReportTemplateId(Integer incidentReportTemplateId) {
		this.incidentReportTemplateId = incidentReportTemplateId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTemplateObject() {
		return templateObject;
	}

	public void setTemplateObject(String templateObject) {
		this.templateObject = templateObject;
	}

	public Integer getTemplateFormatId() {
		return templateFormatId;
	}

	public void setTemplateFormatId(Integer templateFormatId) {
		this.templateFormatId = templateFormatId;
	}

	public List<NabhProcessTemplateMapperDto> getIncidentReportTemplateMapperList() {
		return incidentReportTemplateMapperList;
	}

	public void setIncidentReportTemplateMapperList(
			List<NabhProcessTemplateMapperDto> incidentReportTemplateMapperList) {
		this.incidentReportTemplateMapperList = incidentReportTemplateMapperList;
	}

	public Integer getIncidentReportRoleId() {
		return incidentReportRoleId;
	}

	public void setIncidentReportRoleId(Integer incidentReportRoleId) {
		this.incidentReportRoleId = incidentReportRoleId;
	}

	public String getFormTemplateId() {
		return formTemplateId;
	}

	public void setFormTemplateId(String formTemplateId) {
		this.formTemplateId = formTemplateId;
	}

	public synchronized Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public synchronized void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	
	// public List<IncidentReportTemplateMapperDto>
	// getIncidentReportTemplateMapperList() {
	// return incidentReportTemplateMapperList;
	// }
	//
	// public void setIncidentReportTemplateMapperList(
	// List<IncidentReportTemplateMapperDto> incidentReportTemplateMapperList) {
	// this.incidentReportTemplateMapperList = incidentReportTemplateMapperList;
	// }



}
