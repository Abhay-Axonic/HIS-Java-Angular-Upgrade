package com.param.global.dto;

import java.math.BigDecimal;

public class OrdeDetailsPackageMapperLogDto {

	private Integer orderDetailsPackageMapperLogId;
	
	private Integer orderDetailsId;
	
	private Integer packageConsumptionMasterId;
	
	private Integer packageId;
	
	private BigDecimal packageAmount;
	
	private Integer createdBy;
	
	private Integer unitId;

	private Integer organizationId;
	
	private Integer packageConsumptionCapDetailsId;

	public Integer getOrderDetailsPackageMapperLogId() {
		return orderDetailsPackageMapperLogId;
	}

	public void setOrderDetailsPackageMapperLogId(Integer orderDetailsPackageMapperLogId) {
		this.orderDetailsPackageMapperLogId = orderDetailsPackageMapperLogId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}

	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public BigDecimal getPackageAmount() {
		return packageAmount;
	}

	public void setPackageAmount(BigDecimal packageAmount) {
		this.packageAmount = packageAmount;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Integer getPackageConsumptionCapDetailsId() {
		return packageConsumptionCapDetailsId;
	}

	public void setPackageConsumptionCapDetailsId(Integer packageConsumptionCapDetailsId) {
		this.packageConsumptionCapDetailsId = packageConsumptionCapDetailsId;
	}
	
}
