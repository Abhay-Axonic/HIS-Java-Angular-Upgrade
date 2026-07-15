package com.param.global.billing.dto;

public class UrlConfigurationDto {

	private Integer urlConfigId;
	
	private String name;
	
	private String rootUrl;
	
	private String baseUrl;
	
	private char status;
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private String createDate;
	
	private String updateDate;

	public Integer getUrlConfigId() {
		return urlConfigId;
	}

	public void setUrlConfigId(Integer urlConfigId) {
		this.urlConfigId = urlConfigId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRootUrl() {
		return rootUrl;
	}

	public void setRootUrl(String rootUrl) {
		this.rootUrl = rootUrl;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	
	
	
}
