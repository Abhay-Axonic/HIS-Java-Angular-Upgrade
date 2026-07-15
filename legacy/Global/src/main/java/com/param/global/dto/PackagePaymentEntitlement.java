package com.param.global.dto;

import java.util.List;

public class PackagePaymentEntitlement {

	private Integer paymentEntitlementId;
	private String paymentEntitlement;
	List<PackagePatientCategory> patientCategoryList;

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public String getPaymentEntitlement() {
		return paymentEntitlement;
	}

	public void setPaymentEntitlement(String paymentEntitlement) {
		this.paymentEntitlement = paymentEntitlement;
	}

	public List<PackagePatientCategory> getPatientCategoryList() {
		return patientCategoryList;
	}

	public void setPatientCategoryList(List<PackagePatientCategory> patientCategoryList) {
		this.patientCategoryList = patientCategoryList;
	}

}
