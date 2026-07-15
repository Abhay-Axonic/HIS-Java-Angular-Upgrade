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
	@NamedQuery(name="GET_STORE_TYPE_MASTER_LIST",
		query="SELECT mstr.storeTypeId as storeTypeId,mstr.code as code,mstr.type as type,mstr.isActive as isActive"
					+ " FROM StoreTypeMaster mstr"
					+ " WHERE mstr.organizationId=:orgId "),
	@NamedQuery(name="GET_STORE_TYPE_BY_ID",
	  query="SELECT mstr.storeTypeId as storeTypeId,mstr.code as code,mstr.type as type,mstr.isActive as isActive"
			+ " FROM StoreTypeMaster mstr"
			+ " WHERE mstr.organizationId=:orgId"
			//+ " AND mstr.unitId=:unitId"
			+ " AND mstr.storeTypeId=:id"),
	@NamedQuery(name="GET_STORE_TYPE_ALREADY_EXIST_OR_NOT",
	  query="SELECT mstr.storeTypeId as storeTypeId,mstr.code as code,mstr.type as type,mstr.isActive as isActive"
			+ " FROM StoreTypeMaster mstr"
			+ " WHERE mstr.organizationId=:orgId"
			//+ " AND mstr.unitId=:unitId"
			+ " AND LOWER(mstr.code)=:code AND LOWER(mstr.type)=:type"),
	@NamedQuery(name="GET_STORE_TYPE_ALREADY_EXIST_OR_NOT_BY_ID",
	  query="SELECT mstr.storeTypeId as storeTypeId,mstr.code as code,mstr.type as type,mstr.isActive as isActive"
			+ " FROM StoreTypeMaster mstr"
			+ " WHERE mstr.organizationId=:orgId"
			//+ " AND mstr.unitId=:unitId"
			+ " AND LOWER(mstr.code)=:code AND LOWER(mstr.type)=:type AND mstr.storeTypeId!=:id ")
})

@Entity
@Table(name="m_store_type",schema="public")
@SequenceGenerator(name="m_store_type_id_seq",sequenceName="public.m_store_type_id_seq",allocationSize=1)
public class StoreTypeMaster {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="m_store_type_id_seq")
	private Integer storeTypeId;
	
	@Column(name="code")
	private String code;
	
	@Column(name="type")
	private String type;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="is_active")
	private Boolean isActive;
	
	@Column(name="is_deleted")
	private Boolean isDeleted;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="added_by")
	private Integer addedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="added_date")
	private Date addedDate;

	public Integer getStoreTypeId() {
		return storeTypeId;
	}

	public void setStoreTypeId(Integer storeTypeId) {
		this.storeTypeId = storeTypeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(Integer addedBy) {
		this.addedBy = addedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Date getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}	
	
}


