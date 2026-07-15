package com.param.global.billing.dto;

import java.math.BigDecimal;

public class RequestDiscountDetailsDto {

	private Integer reqDiscDetailsId;
	
	private Integer reqDiscId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer orderId;
	
	private Integer orderDetailsId;
	
	private Integer serviceId;
	
	private BigDecimal netAmount;
	
	private BigDecimal reqDisAmt;
	
	private BigDecimal approvedDiscAmt;
	
	private Integer createdBy;
	
	private Integer updatedBy;
	
	private String createdDate;
	
	private String updatedDate;
	
	private String serviceName;

	public Integer getReqDiscDetailsId() {
		return reqDiscDetailsId;
	}
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setReqDiscDetailsId(Integer reqDiscDetailsId) {
		this.reqDiscDetailsId = reqDiscDetailsId;
	}

	public Integer getReqDiscId() {
		return reqDiscId;
	}

	public void setReqDiscId(Integer reqDiscId) {
		this.reqDiscId = reqDiscId;
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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	
	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public BigDecimal getReqDisAmt() {
		return reqDisAmt;
	}

	public void setReqDisAmt(BigDecimal reqDisAmt) {
		this.reqDisAmt = reqDisAmt;
	}

	public BigDecimal getApprovedDiscAmt() {
		return approvedDiscAmt;
	}

	public void setApprovedDiscAmt(BigDecimal approvedDiscAmt) {
		this.approvedDiscAmt = approvedDiscAmt;
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

}
