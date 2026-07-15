package com.param.adt.master.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_bed_log_status", schema = "adt")
@SequenceGenerator(name="bed_log_status_seq", sequenceName="adt.bed_log_status_seq", allocationSize=1)
public class BedLogStatus {
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="bed_log_status_seq")
	@Column(name = "bed_log_status_id")
	private Integer bedLogStatusId;
	
	@Column(name = "bed_id")
	private Integer bedId;
	
	@Column(name = "bed_status_id")
	private Integer bedStatusId;
	
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
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "bed_id", insertable = false, updatable = false)
	private BedMaster bedMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "bed_status_id", insertable = false, updatable = false)
	private BedStatusMaster bedStatusMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "organization_id", insertable = false, updatable = false)
	private OrganizationMaster organizationMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, updatable = false)
	private UnitMaster unitMaster;
	

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

	public OrganizationMaster getOrganizationMaster() {
		return organizationMaster;
	}

	public void setOrganizationMaster(OrganizationMaster organizationMaster) {
		this.organizationMaster = organizationMaster;
	}

	public UnitMaster getUnitMaster() {
		return unitMaster;
	}

	public void setUnitMaster(UnitMaster unitMaster) {
		this.unitMaster = unitMaster;
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

	public BedMaster getBedMaster() {
		return bedMaster;
	}

	public void setBedMaster(BedMaster bedMaster) {
		this.bedMaster = bedMaster;
	}

	public BedStatusMaster getBedStatusMaster() {
		return bedStatusMaster;
	}

	public void setBedStatusMaster(BedStatusMaster bedStatusMaster) {
		this.bedStatusMaster = bedStatusMaster;
	}

	public Integer getBedLogStatusId() {
		return bedLogStatusId;
	}

	public void setBedLogStatusId(Integer bedLogStatusId) {
		this.bedLogStatusId = bedLogStatusId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getBedStatusId() {
		return bedStatusId;
	}

	public void setBedStatusId(Integer bedStatusId) {
		this.bedStatusId = bedStatusId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = (status == null) ? 'A' : status;
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
	
	

}
