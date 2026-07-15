package com.param.otims.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(name="GET_OPERATION_TABLE_LIST",
			query=" SELECT opTab.operationTableId AS operationTableId,opTab.tableCode AS tableCode,"
					+ "opTab.tableName AS tableName ,opTab.tableDesc AS tableDesc,opTab.status AS status,"
					+ " opTab.tableTypeId as tableTypeId ,opTabType.tableName as tableTypeName,"
					+ " opTheatre.theatreName AS theatreName, opTab.operationTheatreId AS operationTheatreId "
					+ " FROM OperationTable opTab "
					+ " INNER JOIN opTab.operationTableType opTabType "
					+ " INNER JOIN opTab.operationTheatre opTheatre "
					+ " WHERE opTab.orgId=:orgId "
					+ " AND opTab.unitId=:unitId "),
	@NamedQuery(name="GET_OPERATION_TABLE_LIST_BY_ID",
	query=" SELECT opTab.operationTableId AS operationTableId,opTab.tableCode AS tableCode,"
			+ " opTab.tableName AS tableName ,opTab.tableDesc AS tableDesc,opTab.status AS status,"
			+ " opTab.tableTypeId as tableTypeId,opTab.operationTheatreId AS operationTheatreId "
			+ " FROM OperationTable opTab "
			+ " WHERE opTab.operationTableId=:operationTableId "),
	@NamedQuery(name="GET_ACTIVE_OPERATION_TABLE_LIST",
	query=" SELECT opTab.operationTableId AS operationTableId,opTab.tableCode AS tableCode,"
			+ "opTab.tableName AS tableName ,opTab.tableDesc AS tableDesc,opTab.status AS status, "
			+ " opTheatre.theatreName AS theatreName, opTab.operationTheatreId AS operationTheatreId "
			+ " FROM OperationTable opTab "
			+ " INNER JOIN opTab.operationTheatre opTheatre "
			+ " WHERE opTab.orgId=:orgId "
			+ " AND  opTab.unitId=:unitId"
			+ " AND  opTab.status='A'"),
	
	@NamedQuery(name="GET_OPERATION_TABLE_LIST_BY_THEATRE_ID",
	query=" SELECT opTab.operationTableId AS operationTableId,opTab.tableCode AS tableCode,"
			+ "opTab.tableName AS tableName ,opTab.tableDesc AS tableDesc,opTab.status AS status "
			+ " FROM OperationTable opTab "
			+ " WHERE opTab.orgId=:orgId "
			+ " AND  opTab.unitId=:unitId"
			+ " AND  opTab.status='A'"
			+ " AND opTab.operationTheatreId=:theatreId"),
	
	
	@NamedQuery(name="GET_OPERATION_TABLE_LIST_BY_NAME",
	query=" SELECT opTab.operationTableId AS operationTableId,opTab.tableCode AS tableCode,"
			+ "opTab.tableName AS tableName ,opTab.tableDesc AS tableDesc,opTab.status AS status "
			+ " FROM OperationTable opTab "
			+ " WHERE opTab.orgId=:orgId "
			+ " AND  opTab.unitId=:unitId"
			+ " AND  opTab.tableName=:tableName"),
	@NamedQuery(name="GET_OPERATION_TABLE_LIST_BY_NAME_NOT_BY_ID",
	query=" SELECT opTab.operationTableId AS operationTableId,opTab.tableCode AS tableCode,"
			+ "opTab.tableName AS tableName ,opTab.tableDesc AS tableDesc,opTab.status AS status "
			+ " FROM OperationTable opTab "
			+ " WHERE opTab.orgId=:orgId "
			+ " AND  opTab.unitId=:unitId "
			+ " AND  opTab.tableName=:tableName AND opTab.operationTableId!=:operationTableId")
	
})

@Entity
@Table(name = "m_operation_table", schema = "otims")
@SequenceGenerator(name = "m_operation_table_seq", sequenceName = "otims.m_operation_table_seq", allocationSize = 1)
public class OperationTable {
	
	@Id
	@Column(name = "operation_table_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_operation_table_seq")
	private Integer operationTableId;
	
	@Column(name="table_code")
	private String tableCode;
	
	@Column(name="table_name")
	private String tableName;
	
	@Column(name="table_desc")
	private String tableDesc;
	
	@Column(name="table_type_id")
	private Integer tableTypeId;
	
	@Column(name="operation_theatre_id")
	private Integer operationTheatreId;
	
	@Column(name="status")
	private Character status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="table_type_id",insertable=false,updatable=false)
	OperationTableType operationTableType;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="operation_theatre_id",insertable=false,updatable=false)
	OperationTheatre operationTheatre;

	public Integer getOperationTableId() {
		return operationTableId;
	}

	public void setOperationTableId(Integer operationTableId) {
		this.operationTableId = operationTableId;
	}

	public String getTableCode() {
		return tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableDesc() {
		return tableDesc;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}

	public Integer getTableTypeId() {
		return tableTypeId;
	}

	public void setTableTypeId(Integer tableTypeId) {
		this.tableTypeId = tableTypeId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public OperationTableType getOperationTableType() {
		return operationTableType;
	}

	public void setOperationTableType(OperationTableType operationTableType) {
		this.operationTableType = operationTableType;
	}

	public Integer getOperationTheatreId() {
		return operationTheatreId;
	}

	public void setOperationTheatreId(Integer operationTheatreId) {
		this.operationTheatreId = operationTheatreId;
	}

	public OperationTheatre getOperationTheatre() {
		return operationTheatre;
	}

	public void setOperationTheatre(OperationTheatre operationTheatre) {
		this.operationTheatre = operationTheatre;
	} 
	
	
}
