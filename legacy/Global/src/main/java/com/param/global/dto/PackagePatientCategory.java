package com.param.global.dto;

import java.util.List;

public class PackagePatientCategory {

	private Integer patientTypeId;
	private String patientType;
	List<PackageBillingBedCategory> billingBedCategoryList;

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public String getPatientType() {
		return patientType;
	}

	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	public List<PackageBillingBedCategory> getBillingBedCategoryList() {
		return billingBedCategoryList;
	}

	public void setBillingBedCategoryList(List<PackageBillingBedCategory> billingBedCategoryList) {
		this.billingBedCategoryList = billingBedCategoryList;
	}

}
