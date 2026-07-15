package com.param.billing.unit.dto;

import java.math.BigDecimal;

public class DoctorSlabVoucherDetailsDto 
{
	private Integer doctorSlabMasterId;
	
	private Integer doctorId;
	
	private BigDecimal revenueProcessed;
	
	private BigDecimal revenueGeneratedAgainst;
	
	private Integer vouncherId;
	
	public BigDecimal getRevenueGeneratedAgainst() {
		return revenueGeneratedAgainst;
	}

	public void setRevenueGeneratedAgainst(BigDecimal revenueGeneratedAgainst) {
		this.revenueGeneratedAgainst = revenueGeneratedAgainst;
	}

	public Integer getVouncherId() {
		return vouncherId;
	}

	public void setVouncherId(Integer vouncherId) {
		this.vouncherId = vouncherId;
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
	

	

}
