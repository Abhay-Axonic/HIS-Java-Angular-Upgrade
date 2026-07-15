package com.param.global.unit.dto;

import java.util.Date;

import javax.persistence.Column;

public class IntakeFluidTypeMasterDto {
	
private Integer intakeTypeFluidId;

private String intakeTypeFluidCode;

private String intakeTypeFluidDesc;

private Integer intakeFluidId;

private Integer organizationId;

private Integer unitId;

private Character status;

private Integer createdBy;

private Date createdDate;

private Integer updatedBy;

private Date updatedDate;

public Integer getIntakeTypeFluidId() {
	return intakeTypeFluidId;
}

public void setIntakeTypeFluidId(Integer intakeTypeFluidId) {
	this.intakeTypeFluidId = intakeTypeFluidId;
}

public String getIntakeTypeFluidCode() {
	return intakeTypeFluidCode;
}

public void setIntakeTypeFluidCode(String intakeTypeFluidCode) {
	this.intakeTypeFluidCode = intakeTypeFluidCode;
}

public String getIntakeTypeFluidDesc() {
	return intakeTypeFluidDesc;
}

public void setIntakeTypeFluidDesc(String intakeTypeFluidDesc) {
	this.intakeTypeFluidDesc = intakeTypeFluidDesc;
}

public Integer getIntakeFluidId() {
	return intakeFluidId;
}

public void setIntakeFluidId(Integer intakeFluidId) {
	this.intakeFluidId = intakeFluidId;
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

public Date getCreatedDate() {
	return createdDate;
}

public void setCreatedDate(Date createdDate) {
	this.createdDate = createdDate;
}

public Integer getUpdatedBy() {
	return updatedBy;
}

public void setUpdatedBy(Integer updatedBy) {
	this.updatedBy = updatedBy;
}

public Date getUpdatedDate() {
	return updatedDate;
}

public void setUpdatedDate(Date updatedDate) {
	this.updatedDate = updatedDate;
}





}
