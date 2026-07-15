package com.param.billing.global.transaction.dto;

import java.math.BigDecimal;

public class DoctorSlabTransactionDto {

	private Integer id;
	
	private Integer doctorSlabMasterId;
	
	private Integer doctorId;
	
	private Integer billDetailId;
	
	private Integer splitRevenueAgainst;

	private Integer organizationId;
	
	private Integer unitId;

	private Character status;
	
	private Integer createdBy;

	private Integer updatedBy;

	private BigDecimal doctorRevenue;
	
	private BigDecimal revenuePercentage;
	
	private BigDecimal processAmount;
	
	private Double slabFrom;
	
	private Double slabTo;
	
	private Double totalDocRevenue;
	
	private BigDecimal revenueGeneratedAgainst;
	
	private Double slabPercentage;
	
	public Double getSlabPercentage() {
		return slabPercentage;
	}

	public void setSlabPercentage(Double slabPercentage) {
		this.slabPercentage = slabPercentage;
	}

	public BigDecimal getRevenueGeneratedAgainst() {
		return revenueGeneratedAgainst;
	}

	public void setRevenueGeneratedAgainst(BigDecimal revenueGeneratedAgainst) {
		this.revenueGeneratedAgainst = revenueGeneratedAgainst;
	}

	public Double getTotalDocRevenue() {
		return totalDocRevenue;
	}

	public void setTotalDocRevenue(Double totalDocRevenue) {
		this.totalDocRevenue = totalDocRevenue;
	}

	public Double getSlabFrom() {
		return slabFrom;
	}

	public void setSlabFrom(Double slabFrom) {
		this.slabFrom = slabFrom;
	}

	public Double getSlabTo() {
		return slabTo;
	}

	public void setSlabTo(Double slabTo) {
		this.slabTo = slabTo;
	}

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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	
}
