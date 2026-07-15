package com.param.global.unit.dto;

import java.util.Date;

import javax.persistence.Column;

public class IntakeFluidMasterDto {

	
	private Integer intakeFluidId;
	
	private String intakeFluidCode;
	
	private String intakeFluiddesc;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private Date createdDate;
	
	private Integer updatedBy;
	
	private Date updatedDate;

	public Integer getIntakeFluidId() {
		return intakeFluidId;
	}

	public void setIntakeFluidId(Integer intakeFluidId) {
		this.intakeFluidId = intakeFluidId;
	}

	public String getIntakeFluidCode() {
		return intakeFluidCode;
	}

	public void setIntakeFluidCode(String intakeFluidCode) {
		this.intakeFluidCode = intakeFluidCode;
	}

	public String getIntakeFluiddesc() {
		return intakeFluiddesc;
	}

	public void setIntakeFluiddesc(String intakeFluiddesc) {
		this.intakeFluiddesc = intakeFluiddesc;
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
