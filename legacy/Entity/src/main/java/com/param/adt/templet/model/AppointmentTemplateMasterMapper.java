package com.param.adt.templet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="m_appointment_template_master_mapper",schema="public")
@SequenceGenerator(name="m_appointment_template_master_mapper_seq",sequenceName="public.m_appointment_template_master_mapper_seq",allocationSize=1)
public class AppointmentTemplateMasterMapper {

	@Id
	@Column(name = "appointment_template_master_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_appointment_template_master_mapper_seq")
	private Integer appointmentTemplateMasterMapperId;

	@Column(name = "appointment_template_master_id")
	private Integer appointmentTemplateMasterId;

	@Column(name = "sr_no")
	private Integer srNo;
	
	@Column(name = "days")
	private Integer days;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "status")
	private Character status;

	public Integer getAppointmentTemplateMasterMapperId() {
		return appointmentTemplateMasterMapperId;
	}

	public void setAppointmentTemplateMasterMapperId(Integer appointmentTemplateMasterMapperId) {
		this.appointmentTemplateMasterMapperId = appointmentTemplateMasterMapperId;
	}

	public Integer getAppointmentTemplateMasterId() {
		return appointmentTemplateMasterId;
	}

	public void setAppointmentTemplateMasterId(Integer appointmentTemplateMasterId) {
		this.appointmentTemplateMasterId = appointmentTemplateMasterId;
	}

	public Integer getSrNo() {
		return srNo;
	}

	public void setSrNo(Integer srNo) {
		this.srNo = srNo;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	
}
