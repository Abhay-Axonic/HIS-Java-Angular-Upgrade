package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_doctor_slab_master", schema = "billing")
@SequenceGenerator(name = "m_doctor_slab_master_id_seq", sequenceName = "billing.m_doctor_slab_master_id_seq", allocationSize = 1)
public class DoctorSlabMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_doctor_slab_master_id_seq")
	@Column(name = "doctor_slab_master_id")
	private Integer doctorSlabMasterId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "slab_from")
	private double slabFrom;

	@Column(name = "slab_to")
	private double slabTo;
	
	@Column(name = "percentage")
	private double percentage;
	
	@Column(name = "amount")
	private double amount;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "status")
	private char status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	public Integer getDoctorSlabMasterId() {
		return doctorSlabMasterId;
	}

	public void setDoctorSlabMasterId(Integer doctorSlabMasterId) {
		this.doctorSlabMasterId = doctorSlabMasterId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public double getSlabFrom() {
		return slabFrom;
	}

	public void setSlabFrom(double slabFrom) {
		this.slabFrom = slabFrom;
	}

	public double getSlabTo() {
		return slabTo;
	}

	public void setSlabTo(double slabTo) {
		this.slabTo = slabTo;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}
