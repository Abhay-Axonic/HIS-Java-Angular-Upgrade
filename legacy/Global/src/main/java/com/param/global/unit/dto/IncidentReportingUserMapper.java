package com.param.global.unit.dto;

import javax.persistence.Column;

public class IncidentReportingUserMapper {
	private Integer inc_report_user_mppr_id;
	
	private Integer incidentReportingMapperId;
	
	private Integer userId;
	
	private Integer roleId;
	
	private Integer reportingTemplateId;
	
	private String reportingTemplateObj;
	
	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;

	private String updatedDate;

	private Character status;

	private Integer organizationId;

	private Integer unitId;

	public Integer getInc_report_user_mppr_id() {
		return inc_report_user_mppr_id;
	}

	public void setInc_report_user_mppr_id(Integer inc_report_user_mppr_id) {
		this.inc_report_user_mppr_id = inc_report_user_mppr_id;
	}

	public Integer getIncidentReportingMapperId() {
		return incidentReportingMapperId;
	}

	public void setIncidentReportingMapperId(Integer incidentReportingMapperId) {
		this.incidentReportingMapperId = incidentReportingMapperId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getReportingTemplateId() {
		return reportingTemplateId;
	}

	public void setReportingTemplateId(Integer reportingTemplateId) {
		this.reportingTemplateId = reportingTemplateId;
	}

	public String getReportingTemplateObj() {
		return reportingTemplateObj;
	}

	public void setReportingTemplateObj(String reportingTemplateObj) {
		this.reportingTemplateObj = reportingTemplateObj;
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
	
	
}
