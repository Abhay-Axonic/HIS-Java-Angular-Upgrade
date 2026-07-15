package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.unit.model.BillingBedCategoryMaster;

@Entity
@Table(name = "t_tariff_billing_bed_category_mapper", schema = "service")
@SequenceGenerator(name = "tariff_bed_category_mapper_seq", sequenceName = "service.tariff_bed_category_mapper_seq", allocationSize = 1)
public class TariffBillingBedCategoryMapper {
	@Id	
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tariff_bed_category_mapper_seq")
	@Column(name = "tariff_billing_bed_category_mapper_id")
	private Integer tariffBillingBedCategoryMapperId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "tariff_id")
	private Integer tariffId;

	@Column(name = "billing_bed_category_id")
	private Integer billingBedCategoryId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "status")
	private Character status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "billing_bed_category_id", insertable = false, nullable = false, updatable = false)
	private BillingBedCategoryMaster billingBedCategoryMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tariff_id", insertable = false, nullable = false, updatable = false)
	private TariffMaster tariffMaster;

	public Integer getTariffBillingBedCategoryMapperId() {
		return tariffBillingBedCategoryMapperId;
	}

	public void setTariffBillingBedCategoryMapperId(Integer tariffBillingBedCategoryMapperId) {
		this.tariffBillingBedCategoryMapperId = tariffBillingBedCategoryMapperId;
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

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public BillingBedCategoryMaster getBillingBedCategoryMaster() {
		return billingBedCategoryMaster;
	}

	public void setBillingBedCategoryMaster(BillingBedCategoryMaster billingBedCategoryMaster) {
		this.billingBedCategoryMaster = billingBedCategoryMaster;
	}

	public TariffMaster getTariffMaster() {
		return tariffMaster;
	}

	public void setTariffMaster(TariffMaster tariffMaster) {
		this.tariffMaster = tariffMaster;
	}
	
	
}
