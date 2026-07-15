package com.param.adt.master.unit.model;

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

import com.param.adt.master.global.model.OrganizationMaster;
import com.param.adt.master.global.model.UnitMaster;
@NamedQueries({
	@NamedQuery(name="GET_AMENITY_MASTER_LIST",
			query="SELECT am.amenityId AS amenityId,"
					+ "am.amenityCode AS amenityCode,"
					+ "am.amenityDesc AS amenityDesc,"
					+ "am.status AS status "
					+ "FROM AmenityMaster am "
					+ "WHERE am.organizationId =:orgId "
					+ "AND am.unitId =:unitId "
					+ "ORDER  BY am.amenityId DESC"),
	@NamedQuery(name="GET_ACTIVE_AMENITY_MASTER_LIST",
	query="SELECT am.amenityId AS amenityId,"
			+ "am.amenityCode AS amenityCode,"
			+ "am.amenityDesc AS amenityDesc,"
			+ "am.status AS status "
			+ "FROM AmenityMaster am "
			+ "WHERE am.organizationId =:orgId "
			+ "AND am.unitId =:unitId "
			+ "AND am.status='A'"),
	@NamedQuery(name="GET_AMENITY_MASTER_LIST_BY_ID",
	query="SELECT am.amenityId AS amenityId,"
			+ "am.amenityCode AS amenityCode,"
			+ "am.amenityDesc AS amenityDesc,"
			+ "am.status AS status "
			+ "FROM AmenityMaster am "
			+ "WHERE am.amenityId =:amenityId "),
	@NamedQuery(name="GET_AMENITY_MASTER_LIST_BY_NAME",
	query="SELECT am.amenityId AS amenityId "
			+ "FROM AmenityMaster am "
			+ "WHERE am.organizationId =:orgId " 
			+ "AND am.unitId=:unitId "
			+ "AND LOWER(am.amenityDesc)=:name "),
	@NamedQuery(name="GET_AMENITY_MASTER_LIST_BY_NAME_NOT_BY_ID",
	query="SELECT am.amenityId AS amenityId "
			+ "FROM AmenityMaster am "
			+ "WHERE am.organizationId =:orgId " 
			+ "AND am.unitId=:unitId "
			+ "AND LOWER(am.amenityDesc)=:name "
			+ "AND am.amenityId !=:amenityId")
})
@Entity
@Table(name="m_amenity_master",schema="adt")
@SequenceGenerator(name="amenity_master_seq",sequenceName="adt.amenity_master_seq",allocationSize=1)
public class AmenityMaster 
{
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="amenity_master_seq")
	@Column(name="amenity_id")
	private Integer amenityId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name = "amenity_code")
	private String amenityCode;
	
	@Column(name = "amenity_desc")
	private String amenityDesc;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, updatable = false)
	private OrganizationMaster organizationMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, updatable = false)
	private UnitMaster unitMaster;

	public Integer getAmenityId() {
		return amenityId;
	}

	public void setAmenityId(Integer amenityId) {
		this.amenityId = amenityId;
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

	public String getAmenityCode() {
		return amenityCode;
	}

	public void setAmenityCode(String amenityCode) {
		this.amenityCode = amenityCode;
	}

	public String getAmenityDesc() {
		return amenityDesc;
	}

	public void setAmenityDesc(String amenityDesc) {
		this.amenityDesc = amenityDesc;
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
