package com.param.global.model;

import java.util.List;

public class AppointmentTemplateMasterMapperDto {
	
	private Integer appointmentTemplateMasterMapperId;

	private Integer appointmentTemplateMasterId;

	private Integer srNo;
	
	private Integer days;

	private Integer unitId;

	private Integer organizationId;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Character status;
	
	private List<AppointmentTemplateServiceDto> appointmentTemplateServiceList;

	public Integer getAppointmentTemplateMasterMapperId() {
		return appointmentTemplateMasterMapperId;
	}

	public void setAppointmentTemplateMasterMapperId(Integer appointmentTemplateMasterMapperId) {
		this.appointmentTemplateMasterMapperId = appointmentTemplateMasterMapperId;
	}

	public Integer getAppointmentTemplateMasterId() {
		return appointmentTemplateMasterId;
	}

	public void setAppointmentTemplateMasterId(Integer appointmentTemplateMasterId) {
		this.appointmentTemplateMasterId = appointmentTemplateMasterId;
	}

	public Integer getSrNo() {
		return srNo;
	}

	public void setSrNo(Integer srNo) {
		this.srNo = srNo;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public List<AppointmentTemplateServiceDto> getAppointmentTemplateServiceList() {
		return appointmentTemplateServiceList;
	}

	public void setAppointmentTemplateServiceList(List<AppointmentTemplateServiceDto> appointmentTemplateServiceList) {
		this.appointmentTemplateServiceList = appointmentTemplateServiceList;
	}

	
}
