package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_TOTAL_wardSTORE_RECORD", query = "select count(*) FROM pharmacy.t_ward_store_mapper where "
			+ "org_id = :orgId and unit_id = :unitId ")
})

	/*@NamedQueries({
		@NamedQuery(name = "GET_STORE_BY_SPECIALTY_ID",
	   query = "SELECT	dsm.storeId as storeId "
			+ "FROM WardStoreMapper dsm "
			+ "WHERE dsm.wardId = :specialityId "
			+ "AND dsm.orgId= :orgId "
			+ "AND dsm.unitId= :unitId "
			)
	})*/



@Entity
@Table(name = "t_ward_store_mapper", schema = "pharmacy")
//@SequenceGenerator(name = "t_ward_store_mapper_seq", sequenceName = "pharmacy.t_ward_store_mapper_seq", allocationSize = 1)
public class WardStoreMapper {

	@Id
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_ward_store_mapper_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ward_store_mapper_id")
	private Integer wardStoreMapperId;
	
	@Column(name = "store_id")
	private Integer storeId;
	
	@Column(name="ward_id")
	private Integer wardId;
	
	
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

	@Column(name = "org_id")
	private Integer orgId;

	public Integer getwardStoreMapperId() {
		return wardStoreMapperId;
	}

	public void setwardStoreMapperId(Integer wardStoreMapperId) {
		this.wardStoreMapperId = wardStoreMapperId;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Integer getwardId() {
		return wardId;
	}

	public void setwardId(Integer wardId) {
		this.wardId = wardId;
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

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
}
