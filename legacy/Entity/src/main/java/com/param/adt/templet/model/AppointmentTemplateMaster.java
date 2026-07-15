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
@Table(name="m_appointment_template_master",schema="public")
@SequenceGenerator(name="m_appointment_template_master_seq",sequenceName="public.m_appointment_template_master_seq",allocationSize=1)
public class AppointmentTemplateMaster {

	@Id
	@Column(name = "appointment_template_master_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_appointment_template_master_seq")
	private Integer appointmentTemplateMasterId;

	@Column(name = "template_name")
	private String templateName;

	@Column(name = "morning_mond")
	private Character morningMond;

	@Column(name = "morning_tues")
	private Character morningTues;
	
	@Column(name = "morning_wedn")
	private Character morningWedn;

	@Column(name = "morning_thus")
	private Character morningThus;
	
	@Column(name = "morning_frid")
	private Character morningFrid;

	@Column(name = "morning_satur")
	private Character morningSatur;
	
	@Column(name = "morning_sund")
	private Character morningSund;
	
	@Column(name = "evening_mond")
	private Character eveningMond;

	@Column(name = "evening_tues")
	private Character eveningTues;
	
	@Column(name = "evening_wedn")
	private Character eveningWedn;

	@Column(name = "evening_thus")
	private Character eveningThus;
	
	@Column(name = "evening_frid")
	private Character eveningFrid;

	@Column(name = "evening_satur")
	private Character eveningSatur;
	
	@Column(name = "evening_sund")
	private Character eveningSund;

	@Column(name = "remark")
	private String remark;

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

	public Integer getAppointmentTemplateMasterId() {
		return appointmentTemplateMasterId;
	}

	public void setAppointmentTemplateMasterId(Integer appointmentTemplateMasterId) {
		this.appointmentTemplateMasterId = appointmentTemplateMasterId;
	}

	public Character getMorningMond() {
		return morningMond;
	}

	public void setMorningMond(Character morningMond) {
		this.morningMond = morningMond;
	}

	public Character getMorningTues() {
		return morningTues;
	}

	public void setMorningTues(Character morningTues) {
		this.morningTues = morningTues;
	}

	public Character getMorningWedn() {
		return morningWedn;
	}

	public void setMorningWedn(Character morningWedn) {
		this.morningWedn = morningWedn;
	}

	public Character getMorningThus() {
		return morningThus;
	}

	public void setMorningThus(Character morningThus) {
		this.morningThus = morningThus;
	}

	public Character getMorningFrid() {
		return morningFrid;
	}

	public void setMorningFrid(Character morningFrid) {
		this.morningFrid = morningFrid;
	}

	public Character getMorningSatur() {
		return morningSatur;
	}

	public void setMorningSatur(Character morningSatur) {
		this.morningSatur = morningSatur;
	}

	public Character getMorningSund() {
		return morningSund;
	}

	public void setMorningSund(Character morningSund) {
		this.morningSund = morningSund;
	}

	public Character getEveningMond() {
		return eveningMond;
	}

	public void setEveningMond(Character eveningMond) {
		this.eveningMond = eveningMond;
	}

	public Character getEveningTues() {
		return eveningTues;
	}

	public void setEveningTues(Character eveningTues) {
		this.eveningTues = eveningTues;
	}

	public Character getEveningWedn() {
		return eveningWedn;
	}

	public void setEveningWedn(Character eveningWedn) {
		this.eveningWedn = eveningWedn;
	}

	public Character getEveningThus() {
		return eveningThus;
	}

	public void setEveningThus(Character eveningThus) {
		this.eveningThus = eveningThus;
	}

	public Character getEveningFrid() {
		return eveningFrid;
	}

	public void setEveningFrid(Character eveningFrid) {
		this.eveningFrid = eveningFrid;
	}

	public Character getEveningSatur() {
		return eveningSatur;
	}

	public void setEveningSatur(Character eveningSatur) {
		this.eveningSatur = eveningSatur;
	}

	public Character getEveningSund() {
		return eveningSund;
	}

	public void setEveningSund(Character eveningSund) {
		this.eveningSund = eveningSund;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	
}
