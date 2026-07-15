package com.param.billing.packages.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_package_bed_category", schema = "billing")
@SequenceGenerator(name = "seq_package_bed_category", sequenceName = "billing.seq_package_bed_category", allocationSize = 1)
public class TPackageBedCategory {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_package_bed_category")
	@Column(name = "t_package_bed_category_id")
	private int tPackageBedCategoryId;

	@Column(name = "package_consumption_master_id")
	private Integer packageConsumptionMasterId;

	@Column(name = "bed_category_id")
	private Integer bedCategoryId;

	@Column(name = "billing_bed_category_id")
	private Integer billingBedCategoryId;

	@Column(name = "applicable_days")
	private Integer applicableDays;

	@Column(name = "status")
	private char status;

	@Column(name = "organisation_id")
	private Integer organisationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}

	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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

	public int gettPackageBedCategoryId() {
		return tPackageBedCategoryId;
	}

	public void settPackageBedCategoryId(int tPackageBedCategoryId) {
		this.tPackageBedCategoryId = tPackageBedCategoryId;
	}

	public Integer getBedCategoryId() {
		return bedCategoryId;
	}

	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getApplicableDays() {
		return applicableDays;
	}

	public void setApplicableDays(Integer applicableDays) {
		this.applicableDays = applicableDays;
	}

}
