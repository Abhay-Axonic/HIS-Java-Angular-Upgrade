package com.param.adt.discharge.dto;

import java.util.List;

import javax.persistence.Column;


public class DischargeSummaryFieldDto {
	
	private Integer dischargeFieldId;
	
	private String dischargeFieldCode;

	private String dischargeFieldDesc;
	
	private String summaryFieldDesc;
	
	private String dischargeFieldTemplateObj;
	
	private Integer createdBy;

	private String createdDate;
	
	private Character status;

	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	private String dischargeFieldColumnJson;
	private Integer offset;
	private String columnName;
	private Integer dischSumTabColMstId;

	private String searchKeyword;

	private List<DischargeSummaryFieldDto> listOfColumns;
	private List<DischargeSumTableColumnsMasterDto> columnsListData;

	private boolean isColumnRequired;
	private Character defaultField;

	public Integer getDischargeFieldId() {
		return dischargeFieldId;
	}

	public void setDischargeFieldId(Integer dischargeFieldId) {
		this.dischargeFieldId = dischargeFieldId;
	}

	public String getDischargeFieldCode() {
		return dischargeFieldCode;
	}

	public void setDischargeFieldCode(String dischargeFieldCode) {
		this.dischargeFieldCode = dischargeFieldCode;
	}

	public String getDischargeFieldDesc() {
		return dischargeFieldDesc;
	}

	public void setDischargeFieldDesc(String dischargeFieldDesc) {
		this.dischargeFieldDesc = dischargeFieldDesc;
	}

	public String getSummaryFieldDesc() {
		return summaryFieldDesc;
	}

	public void setSummaryFieldDesc(String summaryFieldDesc) {
		this.summaryFieldDesc = summaryFieldDesc;
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

	public String getDischargeFieldTemplateObj() {
		return dischargeFieldTemplateObj;
	}

	public void setDischargeFieldTemplateObj(String dischargeFieldTemplateObj) {
		this.dischargeFieldTemplateObj = dischargeFieldTemplateObj;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getDischSumTabColMstId() {
		return dischSumTabColMstId;
	}

	public void setDischSumTabColMstId(Integer dischSumTabColMstId) {
		this.dischSumTabColMstId = dischSumTabColMstId;
	}

	public List<DischargeSummaryFieldDto> getListOfColumns() {
		return listOfColumns;
	}

	public void setListOfColumns(List<DischargeSummaryFieldDto> listOfColumns) {
		this.listOfColumns = listOfColumns;
	}

	public String getDischargeFieldColumnJson() {
		return dischargeFieldColumnJson;
	}

	public void setDischargeFieldColumnJson(String dischargeFieldColumnJson) {
		this.dischargeFieldColumnJson = dischargeFieldColumnJson;
	}

	public boolean getIsColumnRequired() {
		return isColumnRequired;
	}

	public void setIsColumnRequired(boolean isColumnRequired) {
		this.isColumnRequired = isColumnRequired;
	}

	public List<DischargeSumTableColumnsMasterDto> getColumnsListData() {
		return columnsListData;
	}

	public void setColumnsListData(List<DischargeSumTableColumnsMasterDto> columnsListData) {
		this.columnsListData = columnsListData;
	}

	public Character getDefaultField() {
		return defaultField;
	}

	public void setDefaultField(Character defaultField) {
		this.defaultField = defaultField;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
}
