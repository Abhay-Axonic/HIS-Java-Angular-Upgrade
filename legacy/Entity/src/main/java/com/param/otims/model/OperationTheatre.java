package com.param.otims.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.Store;


@NamedQueries({
	@NamedQuery(name="GET_OPERATION_THEATRE_BY_ID",
			query=" SELECT "
					+ " operationTheatre.operationTheatreId AS operationTheatreId, operationTheatre.theatreCode AS theatreCode, "
					+ " operationTheatre.theatreName AS theatreName, operationTheatre.theatreDesc AS theatreDesc, operationTheatre.status AS status, "
					+ " operationTheatre.orgId AS orgId, operationTheatre.unitId AS unitId,operationTheatre.theatreTypeId AS theatreTypeId,stor.storeId as storeId, stor.storeName  as storeName "
					+ " FROM OperationTheatre operationTheatre "
					+ " left join operationTheatre.store stor "
					+ " WHERE operationTheatre.orgId= :orgId AND operationTheatre.unitId= :unitId "
					+ " AND operationTheatre.operationTheatreId= :operationTheatreId "
					),
	
	@NamedQuery(name="GET_OPERATION_THEATRE_LIST",
			query=" SELECT "
					+ " operationTheatre.operationTheatreId AS operationTheatreId, operationTheatre.theatreCode AS theatreCode, "
					+ " operationTheatre.theatreName AS theatreName, operationTheatre.theatreDesc AS theatreDesc, operationTheatre.status AS status, "
					+ " operationTheatre.orgId AS orgId, operationTheatre.unitId AS unitId,"
					+ " opTarTyp.theatreTypeName AS theatreTypeName ,operationTheatre.theatreTypeId AS theatreTypeId,stor.storeId as storeId, stor.storeName  as storeName  "
					+ " FROM OperationTheatre operationTheatre "
					+ " INNER JOIN operationTheatre.operationTheatreType opTarTyp  	"
					+ " left join operationTheatre.store stor "
					+ "  WHERE operationTheatre.orgId= :orgId"
					+ " AND operationTheatre.unitId= :unitId "
					+ " ORDER BY operationTheatre.operationTheatreId desc"
					),
	
	@NamedQuery(name="GET_ACTIVE_OPERATION_THEATRE_LIST",
			query=" SELECT "
					+ " operationTheatre.operationTheatreId AS operationTheatreId, operationTheatre.theatreCode AS theatreCode, "
					+ " operationTheatre.theatreName AS theatreName, operationTheatre.theatreDesc AS theatreDesc, operationTheatre.status AS status, "
					+ " operationTheatre.orgId AS orgId, operationTheatre.unitId AS unitId"
					+ " FROM OperationTheatre operationTheatre "
					+ " WHERE operationTheatre.orgId= :orgId AND operationTheatre.unitId= :unitId "
					+ " AND operationTheatre.status = 'A' ORDER BY operationTheatre.theatreName asc"
					),
	
	
	@NamedQuery(name="GET_OPERATION_THEATRE_BY_NAME",
			query=" SELECT "
					+ " operationTheatre.operationTheatreId AS operationTheatreId, operationTheatre.theatreName AS theatreName"
					+ " FROM OperationTheatre operationTheatre WHERE LOWER(operationTheatre.theatreName)= :theatreName  AND operationTheatre.orgId= :orgId AND operationTheatre.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_OPERATION_THEATRE_BY_NAME_NOT_ID",
			query=" SELECT "
					+ " operationTheatre.operationTheatreId AS operationTheatreId, operationTheatre.theatreName AS theatreName"
					+ " FROM OperationTheatre operationTheatre WHERE operationTheatre.operationTheatreId != :operationTheatreId "
					+ " AND LOWER(operationTheatre.theatreName)= :theatreName AND operationTheatre.orgId= :orgId AND operationTheatre.unitId= :unitId"
					) , 
	
	@NamedQuery(name="GET_OPERATION_THEATRE_BY_OT_TYPE_ID",
	query=" SELECT "
			+ " operationTheatre.operationTheatreId AS operationTheatreId, operationTheatre.theatreName AS theatreName"
			+ " FROM OperationTheatre operationTheatre "
			+ " WHERE operationTheatre.theatreTypeId != :operationTheatreTypeId "
			+ " AND operationTheatre.orgId= :orgId AND operationTheatre.unitId= :unitId"
			+ " AND operationTheatre.status='A'"
			),
	
	@NamedQuery(name="GET_OPERATION_THEATRE_BY_OT_TYPE_ID_NEW",
	query=" SELECT "
			+ " operationTheatre.operationTheatreId AS operationTheatreId, operationTheatre.theatreName AS theatreName"
			+ " FROM OperationTheatre operationTheatre "
			+ " WHERE operationTheatre.theatreTypeId = :operationTheatreTypeId "
			+ " AND operationTheatre.orgId= :orgId AND operationTheatre.unitId= :unitId"
			+ " AND operationTheatre.status='A'"
			)
})


@Entity
@Table(name = "m_operation_theatre", schema = "otims")
@SequenceGenerator(name = "m_operation_theatre_seq", sequenceName = "otims.m_operation_theatre_seq", allocationSize = 1)
public class OperationTheatre {

	@Id
	@Column(name = "operation_theatre_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_operation_theatre_seq")
	private Integer operationTheatreId;

	@Column(name = "theatre_code")
	private String theatreCode;

	@Column(name = "theatre_name")
	private String theatreName;

	@Column(name = "theatre_desc")
	private String theatreDesc;

	@Column(name = "theatre_type_id")
	private Integer theatreTypeId;

	@Column(name = "status")
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
	
	@Column(name="store_id")
	private Integer storeId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "theatre_type_id", insertable = false, updatable = false)
	OperationTheatreType operationTheatreType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", insertable = false, nullable = false, updatable = false)
	private Store store;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "operationTheatre", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OperationTable> listOperationTable;
	
	

	public Integer getOperationTheatreId() {
		return operationTheatreId;
	}

	public void setOperationTheatreId(Integer operationTheatreId) {
		this.operationTheatreId = operationTheatreId;
	}

	public String getTheatreCode() {
		return theatreCode;
	}

	public void setTheatreCode(String theatreCode) {
		this.theatreCode = theatreCode;
	}

	public String getTheatreName() {
		return theatreName;
	}

	public void setTheatreName(String theatreName) {
		this.theatreName = theatreName;
	}

	public String getTheatreDesc() {
		return theatreDesc;
	}

	public void setTheatreDesc(String theatreDesc) {
		this.theatreDesc = theatreDesc;
	}

	public Integer getTheatreTypeId() {
		return theatreTypeId;
	}

	public void setTheatreTypeId(Integer theatreTypeId) {
		this.theatreTypeId = theatreTypeId;
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

	public OperationTheatreType getOperationTheatreType() {
		return operationTheatreType;
	}

	public void setOperationTheatreType(OperationTheatreType operationTheatreType) {
		this.operationTheatreType = operationTheatreType;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}


}
