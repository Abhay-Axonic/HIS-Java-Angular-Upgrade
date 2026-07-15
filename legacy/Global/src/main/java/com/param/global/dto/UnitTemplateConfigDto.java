 
package com.param.global.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UnitTemplateConfigDto {
	private Integer masterId;
	private Integer orgId;
	private Integer unitId;
	@JsonIgnore
	private String apiKey;
	private String templateUrl;
	private String templateForm;
	private Character status;
	private String createdDate;
	private Integer createdBy;
	private Integer updatedBy;
	private String updatedDate;
	public Integer getMasterId() {
		return masterId;
	}
	public void setMasterId(Integer masterId) {
		this.masterId = masterId;
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
	public String getApiKey() {
		return apiKey;
	}
	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
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
	public String getTemplateUrl() {
		return templateUrl;
	}
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}
	public String getTemplateForm() {
		return templateForm;
	}
	public void setTemplateForm(String templateForm) {
		this.templateForm = templateForm;
	}
}
