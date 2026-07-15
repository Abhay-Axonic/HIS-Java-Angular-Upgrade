package com.param.adt.discharge.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_discharge_sum_tab_columns_master", schema = "emr")
@SequenceGenerator(name = "m_discharge_sum_tab_columns_master_seq", sequenceName = "emr.m_discharge_sum_tab_columns_master_seq", allocationSize = 1)
public class DischargeSumTableColumnsMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_discharge_sum_tab_columns_master_seq")
	@Column(name = "disch_sum_tab_col_mst_id")
	private Integer dischSumTabColMstId;
	
	@Column(name = "discharge_field_id")
	private Integer dischargeFieldId;
	
	@Column(name = "column_name")
	private String columnName;
	
	@Column(name = "table_id")
	private Integer tableId;
	
	@Column(name = "table_name")
	private String tableName;
	
	@Column(name = "is_column_required")
	private boolean isColumnRequired;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "disch_sum_tab_column_id")
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

	public boolean isColumnRequired() {
		return isColumnRequired;
	}

	public void setColumnRequired(boolean isColumnRequired) {
		this.isColumnRequired = isColumnRequired;
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

	public Integer getDischSumTabColumnId() {
		return dischSumTabColumnId;
	}

	public void setDischSumTabColumnId(Integer dischSumTabColumnId) {
		this.dischSumTabColumnId = dischSumTabColumnId;
	}

	
}
