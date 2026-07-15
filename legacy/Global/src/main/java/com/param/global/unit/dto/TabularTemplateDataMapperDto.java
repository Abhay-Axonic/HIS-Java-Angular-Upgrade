package com.param.global.unit.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.param.global.dto.BillPayeeDetailsDto;

public class TabularTemplateDataMapperDto {
	
	private Integer tabularTemplateDataMapperId;
	
	private Integer tabularTemplateId;
	
	private Integer templateId;
	
	private String columnName;
	
	private String tableName;
	
	private Integer coulmnId;
	
	private Integer rowId;
	
	private String rowName;
	
	private String inputValue;
	
	private Integer numberOfCoulmn;
	
	private Integer numberOfRow;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private List<TabularTemplateDataMapperDto> toSaveList;

	public Integer getTabularTemplateDataMapperId() {
		return tabularTemplateDataMapperId;
	}

	public void setTabularTemplateDataMapperId(Integer tabularTemplateDataMapperId) {
		this.tabularTemplateDataMapperId = tabularTemplateDataMapperId;
	}

	public Integer getTabularTemplateId() {
		return tabularTemplateId;
	}

	public void setTabularTemplateId(Integer tabularTemplateId) {
		this.tabularTemplateId = tabularTemplateId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Integer getCoulmnId() {
		return coulmnId;
	}

	public void setCoulmnId(Integer coulmnId) {
		this.coulmnId = coulmnId;
	}

	public Integer getRowId() {
		return rowId;
	}

	public void setRowId(Integer rowId) {
		this.rowId = rowId;
	}

	public String getRowName() {
		return rowName;
	}

	public void setRowName(String rowName) {
		this.rowName = rowName;
	}

	public String getInputValue() {
		return inputValue;
	}

	public void setInputValue(String inputValue) {
		this.inputValue = inputValue;
	}

	public Integer getNumberOfCoulmn() {
		return numberOfCoulmn;
	}

	public void setNumberOfCoulmn(Integer numberOfCoulmn) {
		this.numberOfCoulmn = numberOfCoulmn;
	}

	public Integer getNumberOfRow() {
		return numberOfRow;
	}

	public void setNumberOfRow(Integer numberOfRow) {
		this.numberOfRow = numberOfRow;
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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<TabularTemplateDataMapperDto> getToSaveList() {
		return toSaveList;
	}

	public void setToSaveList(List<TabularTemplateDataMapperDto> toSaveList) {
		this.toSaveList = toSaveList;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	
}
