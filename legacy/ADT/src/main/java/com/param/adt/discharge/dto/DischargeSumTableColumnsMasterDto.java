package com.param.adt.discharge.dto;

import java.util.Date;
import java.util.List;


public class DischargeSumTableColumnsMasterDto {
	private Integer dischSumTabColMstId;
	
	private Integer dischargeFieldId;
	
	private String columnName;
	
	private Integer tableId;
	
	private String tableName;
	
	private boolean isColumnRequired;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;
	private String dischargeFieldDesc;
	private String dischargeFieldCode;
	private String summaryFieldDesc;
	private String dischargeFieldColumnJson;

	private List<DischargeSumTableColumnsMasterDto> listOfColumns;
	private Integer organizationId;
	private Integer dischSumTabColumnId;
	public Integer getDischSumTabColMstId() {
		return dischSumTabColMstId;
	}

	public void setDischSumTabColMstId(Integer dischSumTabColMstId) {
		this.dischSumTabColMstId = dischSumTabColMstId;
	}

	public Integer getDischargeFieldId() {
		return dischargeFieldId;
	}

	public void setDischargeFieldId(Integer dischargeFieldId) {
		this.dischargeFieldId = dischargeFieldId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getTableId() {
		return tableId;
	}

	public void setTableId(Integer tableId) {
		this.tableId = tableId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public boolean getIsColumnRequired() {
		return isColumnRequired;
	}

	public void setIsColumnRequired(boolean isColumnRequired) {
		this.isColumnRequired = isColumnRequired;
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

	public String getDischargeFieldDesc() {
		return dischargeFieldDesc;
	}

	public void setDischargeFieldDesc(String dischargeFieldDesc) {
		this.dischargeFieldDesc = dischargeFieldDesc;
	}

	public List<DischargeSumTableColumnsMasterDto> getListOfColumns() {
		return listOfColumns;
	}

	public void setListOfColumns(List<DischargeSumTableColumnsMasterDto> listOfColumns) {
		this.listOfColumns = listOfColumns;
	}

	public String getDischargeFieldCode() {
		return dischargeFieldCode;
	}

	public void setDischargeFieldCode(String dischargeFieldCode) {
		this.dischargeFieldCode = dischargeFieldCode;
	}

	public String getSummaryFieldDesc() {
		return summaryFieldDesc;
	}

	public void setSummaryFieldDesc(String summaryFieldDesc) {
		this.summaryFieldDesc = summaryFieldDesc;
	}

	public void setColumnRequired(boolean isColumnRequired) {
		this.isColumnRequired = isColumnRequired;
	}

	public String getDischargeFieldColumnJson() {
		return dischargeFieldColumnJson;
	}

	public void setDischargeFieldColumnJson(String dischargeFieldColumnJson) {
		this.dischargeFieldColumnJson = dischargeFieldColumnJson;
	}
	
	public Integer getDischSumTabColumnId() {
		return dischSumTabColumnId;
	}

	public void setDischSumTabColumnId(Integer dischSumTabColumnId) {
		this.dischSumTabColumnId = dischSumTabColumnId;
	}
}
