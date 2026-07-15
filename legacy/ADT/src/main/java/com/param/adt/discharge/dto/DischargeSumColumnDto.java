package com.param.adt.discharge.dto;

public class DischargeSumColumnDto {
private Integer dischSumTabColMstId;
	
	private Integer dischargeFieldId;
	
	private String columnName;
	
	private Integer tableId;
	
	private String tableName;
	
	private boolean isColumnRequired;
	private String dischargeFieldDesc;
	private String dischargeFieldCode;
	private String summaryFieldDesc;
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
	public boolean getIsColumnRequired() {
		return isColumnRequired;
	}
	public void setIsColumnRequired(boolean isColumnRequired) {
		this.isColumnRequired = isColumnRequired;
	}
	public String getDischargeFieldDesc() {
		return dischargeFieldDesc;
	}
	public void setDischargeFieldDesc(String dischargeFieldDesc) {
		this.dischargeFieldDesc = dischargeFieldDesc;
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
	
	
}
