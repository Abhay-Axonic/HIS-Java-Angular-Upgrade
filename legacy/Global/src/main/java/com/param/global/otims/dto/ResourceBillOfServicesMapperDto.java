package com.param.global.otims.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ResourceBillOfServicesMapperDto {


	private Integer resourceBillMapperId;
	
	private Integer surgeryResourceConfigId;
	
	private Integer componenetServicesId;
	
	private Integer dependentOn;
	
	private double dependentOnPercentage;
	
	private Character status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String componenetServicesDesc;
	
	private String name;

	public Integer getResourceBillMapperId() {
		return resourceBillMapperId;
	}

	public void setResourceBillMapperId(Integer resourceBillMapperId) {
		this.resourceBillMapperId = resourceBillMapperId;
	}

	
	public Integer getSurgeryResourceConfigId() {
		return surgeryResourceConfigId;
	}

	public void setSurgeryResourceConfigId(Integer surgeryResourceConfigId) {
		this.surgeryResourceConfigId = surgeryResourceConfigId;
	}

	public Integer getComponenetServicesId() {
		return componenetServicesId;
	}

	public void setComponenetServicesId(Integer componenetServicesId) {
		this.componenetServicesId = componenetServicesId;
	}

	public Integer getDependentOn() {
		return dependentOn;
	}

	public void setDependentOn(Integer dependentOn) {
		this.dependentOn = dependentOn;
	}

	public double getDependentOnPercentage() {
		return dependentOnPercentage;
	}

	public void setDependentOnPercentage(double dependentOnPercentage) {
		this.dependentOnPercentage = dependentOnPercentage;
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

	public String getComponenetServicesDesc() {
		return componenetServicesDesc;
	}

	public void setComponenetServicesDesc(String componenetServicesDesc) {
		this.componenetServicesDesc = componenetServicesDesc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
}
