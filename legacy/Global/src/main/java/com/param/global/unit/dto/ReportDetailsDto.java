package com.param.global.unit.dto;

import java.util.List;

public class ReportDetailsDto {
	private Integer reportDetailId;
	
	private Integer reportId;
	
	private String parameterName;
	
	private Integer parameterId;
	
	private String controlName;
	
	private String viewName;
	
	private String paramType;

	private String action;

	private Integer isCompulsory;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;

	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer organizationId;
	
	private Integer unitId;
	private Integer userId;
	private String reportName;
	private String paramValue;
	private String lable;
	private String reportGroupHeaderName;
	private String misReportIds;
	private String searchString;

	private Integer reportGroupHeaderId;
	
	private Integer id;
	private String description;
	private String format;
	private String fileExtension;

	private List<MetaDataDetailsDto> metaDataDetailsDtosList;
	private List<ReportDetailsDto> listReportDetails;
	private List<ReportDetailsDto> selectedReprtId;
	private List<ReportDetailsDto> selectedAllHeaders;
	public List<MetaDataDetailsDto> getMetaDataDetailsDtosList() {
		return metaDataDetailsDtosList;
	}

	public void setMetaDataDetailsDtosList(List<MetaDataDetailsDto> metaDataDetailsDtosList) {
		this.metaDataDetailsDtosList = metaDataDetailsDtosList;
	}

	public Integer getReportDetailId() {
		return reportDetailId;
	}

	public void setReportDetailId(Integer reportDetailId) {
		this.reportDetailId = reportDetailId;
	}

	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public Integer getParameterId() {
		return parameterId;
	}

	public void setParameterId(Integer parameterId) {
		this.parameterId = parameterId;
	}

	public String getControlName() {
		return controlName;
	}

	public void setControlName(String controlName) {
		this.controlName = controlName;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public Integer getIsCompulsory() {
		return isCompulsory;
	}

	public void setIsCompulsory(Integer isCompulsory) {
		this.isCompulsory = isCompulsory;
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

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public Integer getReportGroupHeaderId() {
		return reportGroupHeaderId;
	}

	public void setReportGroupHeaderId(Integer reportGroupHeaderId) {
		this.reportGroupHeaderId = reportGroupHeaderId;
	}

	public String getReportGroupHeaderName() {
		return reportGroupHeaderName;
	}

	public void setReportGroupHeaderName(String reportGroupHeaderName) {
		this.reportGroupHeaderName = reportGroupHeaderName;
	}

	public List<ReportDetailsDto> getListReportDetails() {
		return listReportDetails;
	}

	public void setListReportDetails(List<ReportDetailsDto> listReportDetails) {
		this.listReportDetails = listReportDetails;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getMisReportIds() {
		return misReportIds;
	}

	public void setMisReportIds(String misReportIds) {
		this.misReportIds = misReportIds;
	}

	public List<ReportDetailsDto> getSelectedReprtId() {
		return selectedReprtId;
	}

	public void setSelectedReprtId(List<ReportDetailsDto> selectedReprtId) {
		this.selectedReprtId = selectedReprtId;
	}

	public List<ReportDetailsDto> getSelectedAllHeaders() {
		return selectedAllHeaders;
	}

	public void setSelectedAllHeaders(List<ReportDetailsDto> selectedAllHeaders) {
		this.selectedAllHeaders = selectedAllHeaders;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}


	
	
}
