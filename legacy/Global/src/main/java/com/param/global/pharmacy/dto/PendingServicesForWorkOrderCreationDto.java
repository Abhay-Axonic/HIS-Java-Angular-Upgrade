package com.param.global.pharmacy.dto;

import java.util.List;

public class PendingServicesForWorkOrderCreationDto extends WorkOrderCreationRequestDetailsDto {

	private Integer patientId;
	
	private String billNumber;
	
	private String visitNumber;
	
	private String patientDetails;
	
	private String visitDetails;
	
	private String payementEntitlement;
	
	private String visitType;
	
	private Integer billId;
	
	private Integer billDetailsId;
	
	private Integer orderDetailsId;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private String fromDate;
	
	private String toDate;

	private Integer unitId;

	private Integer selfPayeeId;

	private String billDetails;
	
	private String billDate;
	
	private Double totalNetAmount;
	
	private Integer serviceId;
	
	private String serviceName;
	
	private Integer patientCategoryId;
	
	private Integer paymentEntitlemnetId;
	
	private Integer visitTypeId;
	
	private Integer billingBedCategoryId;
	
	private Integer opBillingBedCategoryId;
	
	private List<VendorRateDto> vendorList;
	
	private String uhidNumber;
	
	private String imagePath;
	
	private String genderCode;
	
	private Integer packageId;
	
	private String worOrderStatus;
	
	private long pendingServicesCount;
	
	public long getPendingServicesCount() {
		return pendingServicesCount;
	}

	public void setPendingServicesCount(long pendingServicesCount) {
		this.pendingServicesCount = pendingServicesCount;
	}

	public String getWorOrderStatus() {
		return worOrderStatus;
	}

	public void setWorOrderStatus(String worOrderStatus) {
		this.worOrderStatus = worOrderStatus;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Integer getOpBillingBedCategoryId() {
		return opBillingBedCategoryId;
	}

	public void setOpBillingBedCategoryId(Integer opBillingBedCategoryId) {
		this.opBillingBedCategoryId = opBillingBedCategoryId;
	}

	public List<VendorRateDto> getVendorList() {
		return vendorList;
	}

	public void setVendorList(List<VendorRateDto> vendorList) {
		this.vendorList = vendorList;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getPaymentEntitlemnetId() {
		return paymentEntitlemnetId;
	}

	public void setPaymentEntitlemnetId(Integer paymentEntitlemnetId) {
		this.paymentEntitlemnetId = paymentEntitlemnetId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getBillDetailsId() {
		return billDetailsId;
	}

	public void setBillDetailsId(Integer billDetailsId) {
		this.billDetailsId = billDetailsId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Double getTotalNetAmount() {
		return totalNetAmount;
	}

	public void setTotalNetAmount(Double totalNetAmount) {
		this.totalNetAmount = totalNetAmount;
	}

	public String getBillDetails() {
		return billDetails;
	}

	public void setBillDetails(String billDetails) {
		this.billDetails = billDetails;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getVisitNumber() {
		return visitNumber;
	}

	public void setVisitNumber(String visitNumber) {
		this.visitNumber = visitNumber;
	}

	public String getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(String patientDetails) {
		this.patientDetails = patientDetails;
	}

	public String getVisitDetails() {
		return visitDetails;
	}

	public void setVisitDetails(String visitDetails) {
		this.visitDetails = visitDetails;
	}

	public String getPayementEntitlement() {
		return payementEntitlement;
	}

	public void setPayementEntitlement(String payementEntitlement) {
		this.payementEntitlement = payementEntitlement;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
}
