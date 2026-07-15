package com.param.entity.model.pharmacy;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "m_generic_schedule_service_code", schema = "pharmacy")
@SequenceGenerator(name = "m_generic_schedule_service_code_seq", sequenceName = "pharmacy.m_generic_schedule_service_code_seq", allocationSize = 1)
public class GenericScheduleServiceCode {
	@Id
	@Column(name = "generic_schedule_service_code_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_generic_schedule_service_code_seq")
	private Integer genericScheduleServiceCodeiId;
	
	@Column(name = "generic_schedule_id")
	private Integer genericScheduleId;
	
	@Column(name = "service_master_id")
	private Integer serviceMasterId;

	@Column(name = "status")
	private Character status;

	@Column(name = "crated_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "unit_id")
	private Integer unitId;

	public Integer getGenericScheduleServiceCodeiId() {
		return genericScheduleServiceCodeiId;
	}

	public void setGenericScheduleServiceCodeiId(Integer genericScheduleServiceCodeiId) {
		this.genericScheduleServiceCodeiId = genericScheduleServiceCodeiId;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
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

	public Integer getGenericScheduleId() {
		return genericScheduleId;
	}

	public void setGenericScheduleId(Integer genericScheduleId) {
		this.genericScheduleId = genericScheduleId;
	}

	
	
}
