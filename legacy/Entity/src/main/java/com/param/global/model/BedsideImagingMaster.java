package com.param.global.model;

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
	
	@NamedQuery(name="GET_BEDSIDE_IMAGING_MASTER_LIST",
		query="SELECT bedsideImagingMaster.bedsideImagingId AS bedsideImagingId,"
					+ " bedsideImagingMaster.bedsideImagingCode AS bedsideImagingCode, "
					+ " bedsideImagingMaster.subSpecialityId AS subSpecialityId, "
					+ " bedsideImagingMaster.bedsideImagingDesc AS bedsideImagingDesc,"
					+ " bedsideImagingMaster.organizationId AS organizationId,"
					+ " subSpecialityMaster.subSpecialityMasterName as subSpecialityMasterName, "
					+ " bedsideImagingMaster.unitId AS unitId, bedsideImagingMaster.status AS status"
					+ " FROM BedsideImagingMaster bedsideImagingMaster "
					+ "inner join bedsideImagingMaster.subSpecialityMaster subSpecialityMaster "
					+ " WHERE  bedsideImagingMaster.organizationId= :organizationId "
					+ " AND bedsideImagingMaster.unitId= :unitId"
					+ " ORDER BY bedsideImagingMaster.bedsideImagingId asc"
					),
	
	@NamedQuery(name="GET_ACTIVE_BEDSIDE_IMAGING_MASTER",
		query="SELECT bedsideImagingMaster.bedsideImagingId AS bedsideImagingId, "
				 	+ " bedsideImagingMaster.bedsideImagingCode AS bedsideImagingCode, "
				 	+ " bedsideImagingMaster.bedsideImagingDesc AS bedsideImagingDesc,"
					+ " bedsideImagingMaster.subSpecialityId AS subSpecialityId, "
					+ " bedsideImagingMaster.organizationId AS organizationId, "
					+ " bedsideImagingMaster.unitId AS unitId, "
					+ " bedsideImagingMaster.status AS status"
					+ " FROM BedsideImagingMaster bedsideImagingMaster"
					+ " WHERE bedsideImagingMaster.status= 'A'"
					+ " AND bedsideImagingMaster.organizationId= :organizationId"
					+ " AND bedsideImagingMaster.unitId= :unitId"
					+ " ORDER BY bedsideImagingMaster.bedsideImagingId asc"
					),
	
	@NamedQuery(name="GET_BEDSIDE_IMAGING_MASTER_BY_NAME",
		query="SELECT bedsideImagingMaster.bedsideImagingId AS bedsideImagingId "
					+ "	FROM BedsideImagingMaster bedsideImagingMaster"
					+ " WHERE LOWER(bedsideImagingMaster.bedsideImagingDesc)= :bedsideImagingDesc"
					),
	
	@NamedQuery(name="GET_BEDSIDE_IMAGING_MASTER_BY_NAME_NOT_ID",
		query="SELECT bedsideImagingMaster.bedsideImagingId AS bedsideImagingId"
					+ " FROM BedsideImagingMaster bedsideImagingMaster"
					+ " WHERE LOWER(bedsideImagingMaster.bedsideImagingDesc)= :bedsideImagingDesc"
					+ " AND bedsideImagingMaster.bedsideImagingId != :bedsideImagingId"
					),
	})

@Entity
@Table(name = "m_bedside_imaging_master", schema = "public")
@SequenceGenerator(name = "m_bedside_imaging_master_seq", sequenceName = "public.m_bedside_imaging_master_seq", allocationSize = 1)
public class BedsideImagingMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_bedside_imaging_master_seq")
	
	@Column(name = "bedside_imaging_id")
	private Integer bedsideImagingId;
	
	@Column(name = "bedside_imaging_code")
	private String bedsideImagingCode;
	
	@Column(name = "bedside_imaging_desc")
	private String bedsideImagingDesc;
	
	@Column(name = "sub_speciality_id")
	private Integer subSpecialityId;
	
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
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sub_speciality_id" , insertable = false , updatable = false , nullable = false)
	private SubSpecialityMaster subSpecialityMaster;

	public Integer getBedsideImagingId() {
		return bedsideImagingId;
	}

	public void setBedsideImagingId(Integer bedsideImagingId) {
		this.bedsideImagingId = bedsideImagingId;
	}

	public String getBedsideImagingCode() {
		return bedsideImagingCode;
	}

	public void setBedsideImagingCode(String bedsideImagingCode) {
		this.bedsideImagingCode = bedsideImagingCode;
	}

	public String getBedsideImagingDesc() {
		return bedsideImagingDesc;
	}

	public void setBedsideImagingDesc(String bedsideImagingDesc) {
		this.bedsideImagingDesc = bedsideImagingDesc;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
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
	
	
}
