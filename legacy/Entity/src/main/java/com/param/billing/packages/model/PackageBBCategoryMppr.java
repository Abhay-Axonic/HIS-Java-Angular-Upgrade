package com.param.billing.packages.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "package_billing_bed_category", schema = "billing")
@SequenceGenerator(name = "t_seq_package_bb_category", sequenceName = "billing.t_seq_package_bb_category", allocationSize = 1)
public class PackageBBCategoryMppr {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_package_bb_category")
	@Column(name = "package_bbmppr_id ")
	private int packageBbmpprId;

	@Column(name = "package_consumption_master_id")
	private Integer packageConsumptionMasterId;

	@Column(name = "package_id")
	private Integer packageId;

	@Column(name = "billing_bed_category_id")
	private Integer billingBedCategoryId;

	public int getPackageBbmpprId() {
		return packageBbmpprId;
	}

	public void setPackageBbmpprId(int packageBbmpprId) {
		this.packageBbmpprId = packageBbmpprId;
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

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

}
