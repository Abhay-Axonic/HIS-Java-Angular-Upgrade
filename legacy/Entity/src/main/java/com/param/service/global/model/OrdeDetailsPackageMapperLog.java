package com.param.service.global.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_order_details_package_mapper_log", schema = "public")
@SequenceGenerator(name = "t_order_details_package_mapper_log_seq", sequenceName = "public.t_order_details_package_mapper_log_seq", allocationSize = 1)
public class OrdeDetailsPackageMapperLog {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_order_details_package_mapper_log_seq")
	@Column(name = "order_details_package_mapper_log_id")
	private Integer orderDetailsPackageMapperLogId;
	
	@Column(name = "order_details_id")
	private Integer orderDetailsId;
	
	@Column(name = "package_consumption_master_id")
	private Integer packageConsumptionMasterId;
	
	@Column(name = "package_id")
	private Integer packageId;
	
	@Column(name = "package_amount")
	private BigDecimal packageAmount;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "package_consumption_cap_details_id")
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
