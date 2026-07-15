package com.param.global.billing.dto;

import java.math.BigDecimal;

public class CouponTemplateConsumeDetailsDto {

	private Integer couponTemplateConsumeDetailsId;
	
	private Integer patientCouponTemplateMapperId;
	
	private Integer orderDetailsId;
	
	private Integer billId; 
	
	private Integer serviceId;
	
	private Double consumeAmount;
	
	private String createdDate;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private Integer createdBy;
	
	private char status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private BigDecimal ordDiscount;

	public Integer getCouponTemplateConsumeDetailsId() {
		return couponTemplateConsumeDetailsId;
	}

	public void setCouponTemplateConsumeDetailsId(Integer couponTemplateConsumeDetailsId) {
		this.couponTemplateConsumeDetailsId = couponTemplateConsumeDetailsId;
	}

	public Integer getPatientCouponTemplateMapperId() {
		return patientCouponTemplateMapperId;
	}

	public void setPatientCouponTemplateMapperId(Integer patientCouponTemplateMapperId) {
		this.patientCouponTemplateMapperId = patientCouponTemplateMapperId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Double getConsumeAmount() {
		return consumeAmount;
	}

	public void setConsumeAmount(Double consumeAmount) {
		this.consumeAmount = consumeAmount;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public BigDecimal getOrdDiscount() {
		return ordDiscount;
	}

	public void setOrdDiscount(BigDecimal ordDiscount) {
		this.ordDiscount = ordDiscount;
	}
}
