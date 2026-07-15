package com.param.global.dto;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class PackaageResDto {


	Integer orgId;
	Integer unitId;
	ParentPackageMasterDto parentPackageMasterDto;
	List<PackagePaymentEntitlement> paymentEntitlement;

	Set<MultiSelectObj> selectedBedBillingCategoryList;
	Set<MultiSelectObj>		selectedVisitTypeList;
	Set<MultiSelectObj> selectedPatientTypeList;
	Set<MultiSelectObj>		selectedPaymentEntitlementList;

	public List<PackagePaymentEntitlement> getPaymentEntitlement() {
		return paymentEntitlement;
	}

	public void setPaymentEntitlement(List<PackagePaymentEntitlement> paymentEntitlement) {
		this.paymentEntitlement = paymentEntitlement;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public ParentPackageMasterDto getParentPackageMasterDto() {
		return parentPackageMasterDto;
	}

	public void setParentPackageMasterDto(ParentPackageMasterDto parentPackageMasterDto) {
		this.parentPackageMasterDto = parentPackageMasterDto;
	}

	public Set<MultiSelectObj> getSelectedBedBillingCategoryList() {
		return selectedBedBillingCategoryList;
	}

	public void setSelectedBedBillingCategoryList(Set<MultiSelectObj> selectedBedBillingCategoryList) {
		this.selectedBedBillingCategoryList = selectedBedBillingCategoryList;
	}

	public Set<MultiSelectObj> getSelectedVisitTypeList() {
		return selectedVisitTypeList;
	}

	public void setSelectedVisitTypeList(Set<MultiSelectObj> selectedVisitTypeList) {
		this.selectedVisitTypeList = selectedVisitTypeList;
	}

	public Set<MultiSelectObj> getSelectedPatientTypeList() {
		return selectedPatientTypeList;
	}

	public void setSelectedPatientTypeList(Set<MultiSelectObj> selectedPatientTypeList) {
		this.selectedPatientTypeList = selectedPatientTypeList;
	}

	public Set<MultiSelectObj> getSelectedPaymentEntitlementList() {
		return selectedPaymentEntitlementList;
	}

	public void setSelectedPaymentEntitlementList(Set<MultiSelectObj> selectedPaymentEntitlementList) {
		this.selectedPaymentEntitlementList = selectedPaymentEntitlementList;
	}



}
