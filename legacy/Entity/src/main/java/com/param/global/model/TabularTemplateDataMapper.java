package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name="GET_TABULAR_TEMPLATE_DATA_MAPPER_BY_ID",
		query="SELECT tabularTemplateDataMapper.tabularTemplateId AS tabularTemplateId, "
				    + " tabularTemplateDataMapper.tableName AS tableName, "
				    + " tabularTemplateDataMapper.tabularTemplateDataMapperId AS tabularTemplateDataMapperId, "
				    + " tabularTemplateDataMapper.columnName AS columnName, "
				    + " tabularTemplateDataMapper.coulmnId AS coulmnId, "
				    + " tabularTemplateDataMapper.rowId AS rowId, "
				    + " tabularTemplateDataMapper.rowName AS rowName, "
				    + " tabularTemplateDataMapper.inputValue AS inputValue, "
				    + " tabularTemplateDataMapper.numberOfCoulmn AS numberOfCoulmn, "
				    + " tabularTemplateDataMapper.numberOfRow AS numberOfRow, "
					+ " tabularTemplateDataMapper.organizationId AS organizationId, "
					+ " tabularTemplateDataMapper.unitId AS unitId, "
					+ " tabularTemplateDataMapper.status AS status "
					+ " FROM TabularTemplateDataMapper tabularTemplateDataMapper "
					+ " WHERE tabularTemplateDataMapper.tabularTemplateId= :tabularTemplateId "
					+ " AND tabularTemplateDataMapper.organizationId= :organizationId "
					+ " AND tabularTemplateDataMapper.unitId= :unitId "
					+ " AND tabularTemplateDataMapper.status= 'A' "
					),
	
	})

@Entity
@Table(name = "m_tabular_template_data_mapper", schema = "public")
@SequenceGenerator(name = "seq_m_tabular_template_data_mapper", sequenceName = "public.seq_m_tabular_template_data_mapper", allocationSize = 1)
public class TabularTemplateDataMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_tabular_template_data_mapper")
	
	@Column(name = "tabular_template_data_mapper_id")
	private Integer tabularTemplateDataMapperId;
	
	@Column(name = "tabular_template_id")
	private Integer tabularTemplateId;
	
	@Column(name = "column_name")
	private String columnName;
	
	@Column(name = "table_name")
	private String tableName;
	
	@Column(name = "coulmn_id")
	private Integer coulmnId;
	
	@Column(name = "row_id")
	private Integer rowId;
	
	@Column(name = "row_name")
	private String rowName;
	
	@Column(name = "input_value")
	private String inputValue;
	
	@Column(name = "number_of_coulmn")
	private Integer numberOfCoulmn;
	
	@Column(name = "number_of_row")
	private Integer numberOfRow;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

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

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	
}
