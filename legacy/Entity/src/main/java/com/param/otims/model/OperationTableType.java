package com.param.otims.model;

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
	@NamedQuery(name="GET_OPERATION_TABLE_TYPE_LIST",
			     query=" SELECT opTblTyp.operationTableTypeId AS operationTableTypeId ,opTblTyp.tableCode AS tableCode ,"
			     		+ " opTblTyp.tableName AS tableName,opTblTyp.tableDesc AS tableDesc, opTblTyp.status AS status  "
			     		+ " FROM  OperationTableType opTblTyp "
			     		+ " WHERE opTblTyp.orgId =:orgId "
			     		+ " AND   opTblTyp.unitId =:unitId "),
	@NamedQuery(name="GET_OPERATION_TABLE_TYPE_LIST_BY_ID",
				 query=" SELECT opTblTyp.operationTableTypeId AS operationTableTypeId ,opTblTyp.tableCode AS tableCode ,"
			    		+ " opTblTyp.tableName AS tableName,opTblTyp.tableDesc AS tableDesc, opTblTyp.status AS status  "
			    		+ " FROM  OperationTableType opTblTyp "
			    		+ " WHERE opTblTyp.operationTableTypeId =:operationTableTypeId  "),
	@NamedQuery(name="GET_ACTIVE_OPERATION_TABLE_TYPE_LIST",
				query=" SELECT opTblTyp.operationTableTypeId AS operationTableTypeId ,opTblTyp.tableCode AS tableCode ,"
			     		+ " opTblTyp.tableName AS tableName,opTblTyp.tableDesc AS tableDesc, opTblTyp.status AS status  "
			     		+ " FROM   OperationTableType opTblTyp "
			     		+ " WHERE  opTblTyp.orgId =:orgId "
			     		+ " AND    opTblTyp.unitId =:unitId "
			     		+ " AND    opTblTyp.status='A' "
			     		+ " ORDER BY opTblTyp.tableDesc ASC "),
	@NamedQuery(name="GET_OPERATION_TABLE_TYPE_LIST_BY_NAME",
				query=" SELECT opTblTyp.operationTableTypeId AS operationTableTypeId ,opTblTyp.tableCode AS tableCode "
			     		+ " FROM   OperationTableType opTblTyp "
			     		+ " WHERE  opTblTyp.orgId =:orgId "
			     		+ " AND    opTblTyp.unitId =:unitId "
			     		+ " AND    opTblTyp.tableName=:tableName "),
	@NamedQuery(name="GET_OPERATION_TABLE_TYPE_LIST_BY_NAME_NOT_BY_ID",
				query=" SELECT opTblTyp.operationTableTypeId AS operationTableTypeId ,opTblTyp.tableCode AS tableCode "
			     		+ " FROM   OperationTableType opTblTyp "
			     		+ " WHERE  opTblTyp.orgId =:orgId "
			     		+ " AND    opTblTyp.unitId =:unitId "
			     		+ " AND    opTblTyp.tableName=:tableName "
			     		+ " AND    opTblTyp.operationTableTypeId !=:operationTableId")
})

@Entity
@Table(name = "m_operation_table_type", schema = "otims")
@SequenceGenerator(name = "m_operation_table_type_seq", sequenceName = "otims.m_operation_table_type_seq", allocationSize = 1)
public class OperationTableType {

	@Id
	@Column(name = "operation_table_type_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_operation_table_type_seq")
	private Integer operationTableTypeId;
	
	@Column(name="table_code")
	private String tableCode;
	
	@Column(name="table_name")
	private String tableName;
	
	@Column(name="table_desc")
	private String tableDesc;
	
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

	public Integer getOperationTableTypeId() {
		return operationTableTypeId;
	}

	public void setOperationTableTypeId(Integer operationTableTypeId) {
		this.operationTableTypeId = operationTableTypeId;
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
	
	

}
