package com.param.global.unit.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class TabularTemplateMasterDto {
	private Integer tabularTemplateId;
	
	private String tabularTemplateCode;
	
	private String tabularTemplateDesc;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;

	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	public Integer getTabularTemplateId() {
		return tabularTemplateId;
	}

	public void setTabularTemplateId(Integer tabularTemplateId) {
		this.tabularTemplateId = tabularTemplateId;
	}


	public String getTabularTemplateCode() {
		return tabularTemplateCode;
	}

	public void setTabularTemplateCode(String tabularTemplateCode) {
		this.tabularTemplateCode = tabularTemplateCode;
	}

	public String getTabularTemplateDesc() {
		return tabularTemplateDesc;
	}

	public void setTabularTemplateDesc(String tabularTemplateDesc) {
		this.tabularTemplateDesc = tabularTemplateDesc;
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

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	
}
