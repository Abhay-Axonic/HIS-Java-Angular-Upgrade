package com.param.billing.global.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_doctor_slab_transaction", schema="billing")
public class DoctorSlabTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id",unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "doctor_slab_master_id")
	private Integer doctorSlabMasterId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "bill_detail_id")
	private Integer billDetailId;
	
	@Column(name = "doctor_revenue")
	private BigDecimal doctorRevenue;
	
	@Column(name = "split_revenue_against")
	private Integer splitRevenueAgainst;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "revenue_percentage")
	private BigDecimal revenuePercentage;
	
	@Column(name = "process_amount")
	private BigDecimal processAmount;
	
	

	public BigDecimal getProcessAmount() {
		return processAmount;
	}

	public void setProcessAmount(BigDecimal processAmount) {
		this.processAmount = processAmount;
	}

	public BigDecimal getRevenuePercentage() {
		return revenuePercentage;
	}

	public void setRevenuePercentage(BigDecimal revenuePercentage) {
		this.revenuePercentage = revenuePercentage;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public BigDecimal getDoctorRevenue() {
		return doctorRevenue;
	}

	public void setDoctorRevenue(BigDecimal doctorRevenue) {
		this.doctorRevenue = doctorRevenue;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDoctorSlabMasterId() {
		return doctorSlabMasterId;
	}

	public void setDoctorSlabMasterId(Integer doctorSlabMasterId) {
		this.doctorSlabMasterId = doctorSlabMasterId;
	}

	public Integer getBillDetailId() {
		return billDetailId;
	}

	public void setBillDetailId(Integer billDetailId) {
		this.billDetailId = billDetailId;
	}

	public Integer getSplitRevenueAgainst() {
		return splitRevenueAgainst;
	}

	public void setSplitRevenueAgainst(Integer splitRevenueAgainst) {
		this.splitRevenueAgainst = splitRevenueAgainst;
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
	
}


