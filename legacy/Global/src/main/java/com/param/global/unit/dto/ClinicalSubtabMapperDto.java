package com.param.global.unit.dto;

import java.util.Date;

import javax.persistence.Column;

public class ClinicalSubtabMapperDto {
	
	private Integer subTabId;
	
	private String subTabName;
	
	private Integer headerId;
	
	private char opdCheck;
	
	private char ipdCheck;
	
	private char nurseCheck;
	
	private char status;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private Date createdDate;
	
	private Date updatedDate;
	
	private Integer unitId;
	
	private Integer organizationId;

	public Integer getSubTabId() {
		return subTabId;
	}

	public void setSubTabId(Integer subTabId) {
		this.subTabId = subTabId;
	}

	public String getSubTabName() {
		return subTabName;
	}

	public void setSubTabName(String subTabName) {
		this.subTabName = subTabName;
	}

	public Integer getHeaderId() {
		return headerId;
	}

	public void setHeaderId(Integer headerId) {
		this.headerId = headerId;
	}

	public char getOpdCheck() {
		return opdCheck;
	}

	public void setOpdCheck(char opdCheck) {
		this.opdCheck = opdCheck;
	}

	public char getIpdCheck() {
		return ipdCheck;
	}

	public void setIpdCheck(char ipdCheck) {
		this.ipdCheck = ipdCheck;
	}

	public char getNurseCheck() {
		return nurseCheck;
	}

	public void setNurseCheck(char nurseCheck) {
		this.nurseCheck = nurseCheck;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
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
	
	
}
