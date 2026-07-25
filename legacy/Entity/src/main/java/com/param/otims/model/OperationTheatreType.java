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
				@NamedQuery(name="GET_OPERATION_THEATRE_TYPE_BY_ID",
						query=" SELECT "
								+ " operationTheatreType.operationTheatreTypeId AS operationTheatreTypeId, operationTheatreType.theatreTypeCode AS theatreTypeCode, "
								+ " operationTheatreType.theatreTypeName AS theatreTypeName, operationTheatreType.theatreTypeDesc AS theatreTypeDesc, operationTheatreType.status AS status, "
								+ " operationTheatreType.orgId AS orgId, operationTheatreType.unitId AS unitId"
								+ " FROM OperationTheatreType operationTheatreType WHERE operationTheatreType.orgId= :orgId AND operationTheatreType.unitId= :unitId "
								+ " AND operationTheatreType.operationTheatreTypeId= :operationTheatreTypeId ORDER BY operationTheatreType.operationTheatreTypeId asc"
								),
				
				@NamedQuery(name="GET_OPERATION_THEATRE_TYPE_LIST",
						query=" SELECT "
								+ " operationTheatreType.operationTheatreTypeId AS operationTheatreTypeId, operationTheatreType.theatreTypeCode AS theatreTypeCode, "
								+ " operationTheatreType.theatreTypeName AS theatreTypeName, operationTheatreType.theatreTypeDesc AS theatreTypeDesc, operationTheatreType.status AS status, "
								+ " operationTheatreType.orgId AS orgId, operationTheatreType.unitId AS unitId"
								+ " FROM OperationTheatreType operationTheatreType WHERE operationTheatreType.orgId= :orgId AND operationTheatreType.unitId= :unitId "
								+ " ORDER BY operationTheatreType.operationTheatreTypeId desc"
								),
				
				@NamedQuery(name="GET_ACTIVE_OPERATION_THEATRE_TYPE_LIST",
						query=" SELECT "
								+ " operationTheatreType.operationTheatreTypeId AS operationTheatreTypeId, operationTheatreType.theatreTypeCode AS theatreTypeCode, "
								+ " operationTheatreType.theatreTypeName AS theatreTypeName, operationTheatreType.theatreTypeDesc AS theatreTypeDesc, operationTheatreType.status AS status, "
								+ " operationTheatreType.orgId AS orgId, operationTheatreType.unitId AS unitId"
								+ " FROM OperationTheatreType operationTheatreType WHERE operationTheatreType.orgId= :orgId AND operationTheatreType.unitId= :unitId "
								+ " AND operationTheatreType.status = 'A' ORDER BY operationTheatreType.operationTheatreTypeId asc"
								),
				
				
				@NamedQuery(name="GET_OPERATION_THEATRE_TYPE_BY_NAME",
						query=" SELECT "
								+ " operationTheatreType.operationTheatreTypeId AS operationTheatreTypeId, operationTheatreType.theatreTypeDesc AS theatreTypeDesc, operationTheatreType.theatreTypeName AS theatreTypeName"
								+ " FROM OperationTheatreType operationTheatreType WHERE operationTheatreType.orgId= :orgId AND operationTheatreType.unitId= :unitId "
								+ " AND (LOWER(operationTheatreType.theatreTypeDesc)= :theatreTypeDesc OR LOWER(operationTheatreType.theatreTypeName)= :theatreTypeName) "
								),

				@NamedQuery(name="GET_OPERATION_THEATRE_TYPE_BY_NAME_NOT_ID",
						query=" SELECT "
								+ " operationTheatreType.operationTheatreTypeId AS operationTheatreTypeId, operationTheatreType.theatreTypeDesc AS theatreTypeDesc, operationTheatreType.theatreTypeName AS theatreTypeName"
								+ " FROM OperationTheatreType operationTheatreType WHERE operationTheatreType.orgId= :orgId AND operationTheatreType.unitId= :unitId  AND operationTheatreType.operationTheatreTypeId != :operationTheatreTypeId "
								+ " AND (LOWER(operationTheatreType.theatreTypeDesc)= :theatreTypeDesc OR LOWER(operationTheatreType.theatreTypeName)= :theatreTypeName) "
								)
})
@Entity
@Table(name = "m_operation_theatre_type", schema = "otims")
@SequenceGenerator(name = "m_operation_theatre_type_seq", sequenceName = "otims.m_operation_theatre_type_seq", allocationSize = 1)
public class OperationTheatreType {
	@Id
	@Column(name = "operation_theatre_type_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_operation_theatre_type_seq")
	private Integer operationTheatreTypeId;
	
	@Column(name="theatre_type_code")
	private String theatreTypeCode;
	
	@Column(name="theatre_type_name")
	private String theatreTypeName;
	
	@Column(name="theatreType_desc")
	private String theatreTypeDesc;
	
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

	public Integer getOperationTheatreTypeId() {
		return operationTheatreTypeId;
	}

	public void setOperationTheatreTypeId(Integer operationTheatreTypeId) {
		this.operationTheatreTypeId = operationTheatreTypeId;
	}

	public String getTheatreTypeCode() {
		return theatreTypeCode;
	}

	public void setTheatreTypeCode(String theatreTypeCode) {
		this.theatreTypeCode = theatreTypeCode;
	}

	public String getTheatreTypeName() {
		return theatreTypeName;
	}

	public void setTheatreTypeName(String theatreTypeName) {
		this.theatreTypeName = theatreTypeName;
	}

	public String getTheatreTypeDesc() {
		return theatreTypeDesc;
	}

	public void setTheatreTypeDesc(String theatreTypeDesc) {
		this.theatreTypeDesc = theatreTypeDesc;
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
