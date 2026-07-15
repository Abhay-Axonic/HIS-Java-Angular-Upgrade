package com.param.adt.discharge.dto;

import java.util.List;

public class DischargeSummaryTemplateDto {
	
	private Integer dischargeSummaryTempId;
	
	private String dischargeSummaryTempCode;
	
	private String dischargeSummaryTempDesc;
	
	private Integer departmentId;
	
	private Integer dischargeFieldId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;

	private Integer offset;

	private String specialityName;
	
	private Integer id;

	private String lable;
	
	private String dischargeFieldDesc;

	private String searchKeyword;
	
	private List<DischargeSummaryTemplateMapperDto> selectDischargeSummaryTemplateList;
	private List<DischargeTemplateDepartmentMapperDto> selectDischargeTemplateDepartmentList;

	private List<DischargeSummaryTemplateMapperDto> previousSavedList;
	
	private Character isHtml;

	public Integer getDischargeSummaryTempId() {
		return dischargeSummaryTempId;
	}

	public void setDischargeSummaryTempId(Integer dischargeSummaryTempId) {
		this.dischargeSummaryTempId = dischargeSummaryTempId;
	}


	public String getDischargeSummaryTempCode() {
		return dischargeSummaryTempCode;
	}

	public void setDischargeSummaryTempCode(String dischargeSummaryTempCode) {
		this.dischargeSummaryTempCode = dischargeSummaryTempCode;
	}

	public String getDischargeSummaryTempDesc() {
		return dischargeSummaryTempDesc;
	}

	public void setDischargeSummaryTempDesc(String dischargeSummaryTempDesc) {
		this.dischargeSummaryTempDesc = dischargeSummaryTempDesc;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getDischargeFieldId() {
		return dischargeFieldId;
	}

	public void setDischargeFieldId(Integer dischargeFieldId) {
		this.dischargeFieldId = dischargeFieldId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getDischargeFieldDesc() {
		return dischargeFieldDesc;
	}

	public void setDischargeFieldDesc(String dischargeFieldDesc) {
		this.dischargeFieldDesc = dischargeFieldDesc;
	}

	public List<DischargeSummaryTemplateMapperDto> getSelectDischargeSummaryTemplateList() {
		return selectDischargeSummaryTemplateList;
	}

	public void setSelectDischargeSummaryTemplateList(
			List<DischargeSummaryTemplateMapperDto> selectDischargeSummaryTemplateList) {
		this.selectDischargeSummaryTemplateList = selectDischargeSummaryTemplateList;
	}

	public List<DischargeSummaryTemplateMapperDto> getPreviousSavedList() {
		return previousSavedList;
	}

	public void setPreviousSavedList(List<DischargeSummaryTemplateMapperDto> previousSavedList) {
		this.previousSavedList = previousSavedList;
	}

	public List<DischargeTemplateDepartmentMapperDto> getSelectDischargeTemplateDepartmentList() {
		return selectDischargeTemplateDepartmentList;
	}

	public void setSelectDischargeTemplateDepartmentList(
			List<DischargeTemplateDepartmentMapperDto> selectDischargeTemplateDepartmentList) {
		this.selectDischargeTemplateDepartmentList = selectDischargeTemplateDepartmentList;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public Character getIsHtml() {
		return isHtml;
	}

	public void setIsHtml(Character isHtml) {
		this.isHtml = isHtml;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
}
