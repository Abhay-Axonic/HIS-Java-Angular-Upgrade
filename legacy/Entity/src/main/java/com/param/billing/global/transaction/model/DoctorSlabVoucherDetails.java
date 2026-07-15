package com.param.billing.global.transaction.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name="t_doctor_slab_voucher_details", schema="billing")
public class DoctorSlabVoucherDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id",unique = true, nullable = false)
	private Integer id;
	
	@Column(name = "doctor_slab_master_id")
	private Integer doctorSlabMasterId;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "revenue_processed")
	private BigDecimal revenueProcessed;
	
	@Column(name = "revenue_generated_against")
	private BigDecimal revenueGeneratedAgainst;
	
	@Column(name = "voucher_id")
	private Integer voucherId;
	
	@Column(name = "status")
	private Character status;

	public BigDecimal getRevenueGeneratedAgainst() {
		return revenueGeneratedAgainst;
	}

	public void setRevenueGeneratedAgainst(BigDecimal revenueGeneratedAgainst) {
		this.revenueGeneratedAgainst = revenueGeneratedAgainst;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public BigDecimal getRevenueProcessed() {
		return revenueProcessed;
	}

	public void setRevenueProcessed(BigDecimal revenueProcessed) {
		this.revenueProcessed = revenueProcessed;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getVoucherId() {
		return voucherId;
	}

	public void setVoucherId(Integer voucherId) {
		this.voucherId = voucherId;
	}

}
