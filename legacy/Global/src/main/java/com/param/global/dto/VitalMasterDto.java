package com.param.global.dto;

import javax.persistence.Column;

public class VitalMasterDto {

	private int vitalId;
	private String vitalName;
	private String minValue;
	private String maxValue;
	private char status;
	private String unit;
	private String remark;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private Integer unitId;
	private Integer organizationId;
	private String vitalIconPath;
	private Integer vitalOrderOpd;
    private Boolean isIcu;
	private Character isRequired;

	
	
	
	public Integer getVitalOrderOpd() {
		return vitalOrderOpd;
	}
	public void setVitalOrderOPD(Integer vitalOrderOpd) {
		this.vitalOrderOpd = vitalOrderOpd;
	}
	public String getVitalIconPath() {
		return vitalIconPath;
	}
	public void setVitalIconPath(String vitalIconPath) {
		this.vitalIconPath = vitalIconPath;
	}
	public int getVitalId() {
		return vitalId;
	}
	public void setVitalId(int vitalId) {
		this.vitalId = vitalId;
	}
	public String getVitalName() {
		return vitalName;
	}
	public void setVitalName(String vitalName) {
		this.vitalName = vitalName;
	}
	public String getMinValue() {
		return minValue;
	}
	public void setMinValue(String minValue) {
		this.minValue = minValue;
	}
	public String getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(String maxValue) {
		this.maxValue = maxValue;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Boolean getIsIcu() {
		return isIcu;
	}
	public void setIsIcu(Boolean isIcu) {
		this.isIcu = isIcu;
	}
	public Character getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(Character isRequired) {
		this.isRequired = isRequired;
	}
	
	
}
