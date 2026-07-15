package com.param.billing.unit.dto;

import java.util.List;

public class GlobalDocShareServiceRateDto {
	private Integer organizationId;
	private Integer unitId;
	private Double amount;
	private Integer bedBillingCategoryId;
	private String bedBillingCategoryName;
	private Integer departmentId;
	private String departmentName;
	private String effectiveDate; 
	private String groupDesc;
	private String patientName;
	private Integer patientTypeId;
	private String serviceName;
	private Double serviceShare;
	private Integer servicesMstId;
	private Integer subDepartmentId;
	private String subGroupDesc;
	private Integer tariffId;
	private String tariffName;
	private Integer visitTypeId;
	private String visitTypeName;
	private Double rate;
	private List<GlobalDocShareServiceRateDto> globalDocShareServiceRateDtosList;
	private Integer doctorId;
	private String doctorName;
	
	private String paymentEntitlementName;
	private Integer paymentEntitlementId;
	
	private Double amountOld;
	
	
	public Double getAmountOld() {
		return amountOld;
	}
	public void setAmountOld(Double amountOld) {
		this.amountOld = amountOld;
	}
	public String getPaymentEntitlementName() {
		return paymentEntitlementName;
	}
	public void setPaymentEntitlementName(String paymentEntitlementName) {
		this.paymentEntitlementName = paymentEntitlementName;
	}
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public Double getRate() {
		return rate;
	}
	public void setRate(Double rate) {
		this.rate = rate;
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
	public List<GlobalDocShareServiceRateDto> getGlobalDocShareServiceRateDtosList() {
		return globalDocShareServiceRateDtosList;
	}
	public void setGlobalDocShareServiceRateDtosList(List<GlobalDocShareServiceRateDto> globalDocShareServiceRateDtosList) {
		this.globalDocShareServiceRateDtosList = globalDocShareServiceRateDtosList;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Integer getBedBillingCategoryId() {
		return bedBillingCategoryId;
	}
	public void setBedBillingCategoryId(Integer bedBillingCategoryId) {
		this.bedBillingCategoryId = bedBillingCategoryId;
	}
	public String getBedBillingCategoryName() {
		return bedBillingCategoryName;
	}
	public void setBedBillingCategoryName(String bedBillingCategoryName) {
		this.bedBillingCategoryName = bedBillingCategoryName;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}
	public String getGroupDesc() {
		return groupDesc;
	}
	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Integer getPatientTypeId() {
		return patientTypeId;
	}
	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public Double getServiceShare() {
		return serviceShare;
	}
	public void setServiceShare(Double serviceShare) {
		this.serviceShare = serviceShare;
	}
	public Integer getServicesMstId() {
		return servicesMstId;
	}
	public void setServicesMstId(Integer servicesMstId) {
		this.servicesMstId = servicesMstId;
	}
	public Integer getSubDepartmentId() {
		return subDepartmentId;
	}
	public void setSubDepartmentId(Integer subDepartmentId) {
		this.subDepartmentId = subDepartmentId;
	}
	public String getSubGroupDesc() {
		return subGroupDesc;
	}
	public void setSubGroupDesc(String subGroupDesc) {
		this.subGroupDesc = subGroupDesc;
	}
	public Integer getTariffId() {
		return tariffId;
	}
	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}
	public String getTariffName() {
		return tariffName;
	}
	public void setTariffName(String tariffName) {
		this.tariffName = tariffName;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public String getVisitTypeName() {
		return visitTypeName;
	}
	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}
	
}
