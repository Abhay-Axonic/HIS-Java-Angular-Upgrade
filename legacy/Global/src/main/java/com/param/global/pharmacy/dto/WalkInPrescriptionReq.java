package com.param.global.pharmacy.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WalkInPrescriptionReq {

	//protected Long tempBillId;

	protected Long prescriptionId;

	protected Integer screenId;

	protected Integer prefixId;

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	protected Integer patientId;

	protected String patientName;

	protected String patientAddress;

	protected Integer patientAge;

	protected Integer patientGenderId;

	protected Integer doctorId;

	protected String doctorName;

	protected String doctorAddress;

	//protected Integer discountTypeId;

	//protected Integer discountCatgeoryId;

	//protected Double discount;

	//protected Double totalAmount;

	//protected Double discountAmount;

	//protected Double netAmount;
	
	//protected Double totalSelfPayable;
	
	//protected Double totalCreditPayable;

	//protected Double roundOffAmount;

	//protected Double taxAmount;

	//protected Integer saleTypeId;

	protected Integer statusId;

	protected Integer admissionId;

	protected Integer encounterId;

	//protected Integer counterId;

	//protected Integer orderId;

	protected Integer visitTypeId;

	//protected ItemRequest item;
	
	protected Integer tempPatientId;
	
	protected List<WalkInPrescriptionItemsReq> items;
	
	//Ganesh 
	//protected Integer paymentEntitlementTypeId;
	//protected boolean walkInSales;
	//protected boolean opSales;
	//protected Character isContractChanged;
	//protected Character isPayeeUpdated;
	//private Character isSmartApplicable;
	
	protected String mobileNumber;
	
	protected String  remark;
	private Integer organizationId;
	private Integer unitId;
	private Integer userId;
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getEntitlementId() {
		return entitlementId;
	}

	public void setEntitlementId(Integer entitlementId) {
		this.entitlementId = entitlementId;
	}

	private String validationMessage;
	
	protected Integer entitlementId;

	public WalkInPrescriptionReq() {
		super();
	}

	public WalkInPrescriptionReq(Integer screenId, Long tempBillId, ItemRequest item) {
		super();
		this.screenId = screenId;
	}

	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getPatientAddress() {
		return patientAddress;
	}

	public void setPatientAddress(String patientAddress) {
		this.patientAddress = patientAddress;
	}

	public Integer getPatientAge() {
		return patientAge;
	}

	public void setPatientAge(Integer patientAge) {
		this.patientAge = patientAge;
	}

	public Integer getPatientGenderId() {
		return patientGenderId;
	}

	public void setPatientGenderId(Integer patientGenderId) {
		this.patientGenderId = patientGenderId;
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

	public String getDoctorAddress() {
		return doctorAddress;
	}

	public void setDoctorAddress(String doctorAddress) {
		this.doctorAddress = doctorAddress;
	}

	public Integer getStatusId() {
		return statusId;
	}

	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public List<WalkInPrescriptionItemsReq> getItems() {
		return items;
	}

	public void setItems(List<WalkInPrescriptionItemsReq> items) {
		this.items = items;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Long getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Long prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public String getValidationMessage() {
		return validationMessage;
	}

	public void setValidationMessage(String validationMessage) {
		this.validationMessage = validationMessage;
	}
	
	public Integer getTempPatientId() {
		return tempPatientId;
	}

	public void setTempPatientId(Integer tempPatientId) {
		this.tempPatientId = tempPatientId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}
}
