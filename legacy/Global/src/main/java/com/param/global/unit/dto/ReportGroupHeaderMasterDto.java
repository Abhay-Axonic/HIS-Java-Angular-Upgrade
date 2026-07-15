package com.param.global.unit.dto;

import java.util.Date;
import java.util.List;


public class ReportGroupHeaderMasterDto {
	private int reportGroupHeaderId;
	
	private String reportGroupHeaderName;
	
	private Character status;
	
	private Integer createdBy;
	
	private Date createdDate;

	private Integer updatedBy;
	
	private Date updatedDate;
	
	private Integer organizationId;
	
	private Integer unitId;
	private List<ReportDetailsDto> searchedList;
	public int getReportGroupHeaderId() {
		return reportGroupHeaderId;
	}

	public void setReportGroupHeaderId(int reportGroupHeaderId) {
		this.reportGroupHeaderId = reportGroupHeaderId;
	}

	public String getReportGroupHeaderName() {
		return reportGroupHeaderName;
	}

	public void setReportGroupHeaderName(String reportGroupHeaderName) {
		this.reportGroupHeaderName = reportGroupHeaderName;
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

	public List<ReportDetailsDto> getSearchedList() {
		return searchedList;
	}

	public void setSearchedList(List<ReportDetailsDto> searchedList) {
		this.searchedList = searchedList;
	}
	
	
}
