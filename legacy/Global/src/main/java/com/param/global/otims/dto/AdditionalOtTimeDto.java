package com.param.global.otims.dto;

import java.math.BigDecimal;

public class AdditionalOtTimeDto {

	private Integer additionalOtTimeId;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer procedureTypeId;
	
	private BigDecimal timeMin;
	
	private BigDecimal rate;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private String procedureTypeName;
	

	public String getProcedureTypeName() {
		return procedureTypeName;
	}

	public void setProcedureTypeName(String procedureTypeName) {
		this.procedureTypeName = procedureTypeName;
	}

	public Integer getAdditionalOtTimeId() {
		return additionalOtTimeId;
	}

	public void setAdditionalOtTimeId(Integer additionalOtTimeId) {
		this.additionalOtTimeId = additionalOtTimeId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}

	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}

	public BigDecimal getTimeMin() {
		return timeMin;
	}

	public void setTimeMin(BigDecimal timeMin) {
		this.timeMin = timeMin;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
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
	
}
