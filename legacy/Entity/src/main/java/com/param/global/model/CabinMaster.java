package com.param.global.model;

import java.util.Date;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
	
	@NamedQuery(name="GET_CABIN_MASTER_LIST",
		query="SELECT cabinMaster.cabinId AS cabinId, cabinMaster.cabinCode AS cabinCode, cabinMaster.cabinDesc AS cabinDesc,"
					+ " cabinMaster.organizationId AS organizationId, cabinMaster.unitId AS unitId, cabinMaster.status AS status"
					+ " FROM CabinMaster cabinMaster"
					+ " WHERE  cabinMaster.organizationId= :organizationId "
					+ " AND cabinMaster.unitId= :unitId"
					+ " ORDER BY cabinMaster.cabinId asc"
					),
	
	@NamedQuery(name="GET_CABIN_MASTER_BY_ID",
		query="SELECT cabinMaster.cabinId AS cabinId, cabinMaster.cabinCode AS cabinCode, cabinMaster.cabinDesc AS cabinDesc,"
					+ " cabinMaster.organizationId AS organizationId, cabinMaster.unitId AS unitId, cabinMaster.status AS status"
					+ " FROM CabinMaster cabinMaster"
					+ " WHERE cabinMaster.cabinId= :cabinId"
					+ " AND cabinMaster.organizationId= :organizationId"
					+ " AND cabinMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_ACTIVE_CABIN_MASTER",
		query="SELECT cabinMaster.cabinId AS cabinId, cabinMaster.cabinCode AS cabinCode, cabinMaster.cabinDesc AS cabinDesc,"
					+ " cabinMaster.organizationId AS organizationId, cabinMaster.unitId AS unitId, cabinMaster.status AS status"
					+ " FROM CabinMaster cabinMaster"
					+ " WHERE cabinMaster.status= 'A'"
					+ " AND cabinMaster.organizationId= :organizationId"
					+ " AND cabinMaster.unitId= :unitId"
					+ " ORDER BY cabinMaster.cabinId asc"
					),
	
	@NamedQuery(name="GET_CABIN_MASTER_BY_NAME",
		query="SELECT cabinMaster.cabinId AS cabinId "
					+ "	FROM CabinMaster cabinMaster"
					+ " WHERE (LOWER(cabinMaster.cabinDesc)=:cabinDesc OR cabinMaster.cabinDesc=:cabinDesc) "
					+ " AND cabinMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_CABIN_MASTER_BY_NAME_NOT_ID",
		query="SELECT cabinMaster.cabinId AS cabinId"
					+ " FROM CabinMaster cabinMaster"
					+ " WHERE (LOWER(cabinMaster.cabinDesc)=:cabinDesc OR cabinMaster.cabinDesc=:cabinDesc) "
					+ " AND cabinMaster.cabinId != :cabinId"
					+ " AND cabinMaster.unitId = :unitId"
					),
	})


@Entity
@Table(name = "m_cabin_master", schema = "public")
@SequenceGenerator(name = "seq_m_cabin_master", sequenceName = "public.seq_m_cabin_master", allocationSize = 1)
public class CabinMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_cabin_master")
	
	@Column(name = "cabin_id")
	private Integer cabinId;
	
	@Column(name = "cabin_code")
	private String cabinCode;
	
	@Column(name = "cabin_desc")
	private String cabinDesc;
	
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "cabinMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DoctorCabinMapper> listCabinMaster;

	public Integer getCabinId() {
		return cabinId;
	}

	public void setCabinId(Integer cabinId) {
		this.cabinId = cabinId;
	}

	public String getCabinCode() {
		return cabinCode;
	}

	public void setCabinCode(String cabinCode) {
		this.cabinCode = cabinCode;
	}

	public String getCabinDesc() {
		return cabinDesc;
	}

	public void setCabinDesc(String cabinDesc) {
		this.cabinDesc = cabinDesc;
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
	

}
