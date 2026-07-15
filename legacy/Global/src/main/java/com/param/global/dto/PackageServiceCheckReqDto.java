package com.param.global.dto;

import java.util.List;

public class PackageServiceCheckReqDto {

	private Integer patientId;
	private Integer encounterId;
	private Integer visitType;
	private Integer serviceId;
	private List<Integer> listServices;
	private Integer groupId;
	private Integer subGroupId;
	private Integer organisationId;
	private Integer unitId;
	private Integer tariffId;
	private Integer paymentEntitlementId;
	private Integer patientTypeId;
	private Integer billingBedCategoryId;
	
	private float serviceAmt;
	private double creditPayable;
	private double selfPayable;
	private double packageConsumptionAmt;
	
	
	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}
	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}
	public Integer getPatientTypeId() {
		return patientTypeId;
	}
	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public Integer getTariffId() {
		return tariffId;
	}
	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public Integer getVisitType() {
		return visitType;
	}
	public void setVisitType(Integer visitType) {
		this.visitType = visitType;
	}
	public Integer getServiceId() {
		return serviceId;
	}
	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}
	public List<Integer> getListServices() {
		return listServices;
	}
	public void setListServices(List<Integer> listServices) {
		this.listServices = listServices;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getSubGroupId() {
		return subGroupId;
	}
	public void setSubGroupId(Integer subGroupId) {
		this.subGroupId = subGroupId;
	}
	public float getServiceAmt() {
		return serviceAmt;
	}
	public void setServiceAmt(float serviceAmt) {
		this.serviceAmt = serviceAmt;
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
	public double getCreditPayable() {
		return creditPayable;
	}
	public void setCreditPayable(double creditPayable) {
		this.creditPayable = creditPayable;
	}
	public double getSelfPayable() {
		return selfPayable;
	}
	public void setSelfPayable(double selfPayable) {
		this.selfPayable = selfPayable;
	}
	public double getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}
	public void setPackageConsumptionAmt(double packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}
	
	
}
