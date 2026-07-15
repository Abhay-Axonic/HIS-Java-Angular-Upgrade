package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="t_cost_estimate_details",schema="billing")
public class CostEstimateDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id",unique = true, nullable = false)
	private Integer id;
	
	@Column(name="cost_estimation_id")
	private Integer costEstimationId;
	
	@Column(name="service_speciality_id")
	private Integer serviceSpecialityId;
	
	@Column(name="service_sub_speciality_id")
	private Integer serviceSubSpecialityId;
	
	@Column(name="service_master_id")
	private Integer serviceMasterId;
	
	@Column(name="quantity")
	private Double quantity;
	
	@Column(name="total_amt")
	private Double totalAmt;
	
	@Column(name="self_payable")
	private Double selfPayable;
	
	@Column(name="credit_payable")
	private Double creditPayable;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="is_package_service")
	private Character isPackageService;
	
	@Column(name="is_cap_service")
	private Character isCapService;
	
	@Column(name="package_payable")
	private Double packagePayable;
	
	@Column(name="package_id")
	private Integer packageId;
	
	@Column(name="package_type_id")
	private Integer packageTypeId;
	
	@Column(name="package_master_id")
	private Integer packageMasterId;

	@Column(name="status")
	private Character status;
	
	@Column(name="billing_bed_category_id")
	private Integer billingBedCategoryId;
	
	@Column(name="tariff_id")
	private Integer tariffId;
	
	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCostEstimationId() {
		return costEstimationId;
	}

	public void setCostEstimationId(Integer costEstimationId) {
		this.costEstimationId = costEstimationId;
	}

	public Integer getServiceSpecialityId() {
		return serviceSpecialityId;
	}

	public void setServiceSpecialityId(Integer serviceSpecialityId) {
		this.serviceSpecialityId = serviceSpecialityId;
	}

	public Integer getServiceSubSpecialityId() {
		return serviceSubSpecialityId;
	}

	public void setServiceSubSpecialityId(Integer serviceSubSpecialityId) {
		this.serviceSubSpecialityId = serviceSubSpecialityId;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public Double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(Double totalAmt) {
		this.totalAmt = totalAmt;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
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

	public Character getIsPackageService() {
		return isPackageService;
	}

	public void setIsPackageService(Character isPackageService) {
		this.isPackageService = isPackageService;
	}

	public Character getIsCapService() {
		return isCapService;
	}

	public void setIsCapService(Character isCapService) {
		this.isCapService = isCapService;
	}

	public Double getPackagePayable() {
		return packagePayable;
	}

	public void setPackagePayable(Double packagePayable) {
		this.packagePayable = packagePayable;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public Integer getPackageMasterId() {
		return packageMasterId;
	}

	public void setPackageMasterId(Integer packageMasterId) {
		this.packageMasterId = packageMasterId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}
	
	
	
}
