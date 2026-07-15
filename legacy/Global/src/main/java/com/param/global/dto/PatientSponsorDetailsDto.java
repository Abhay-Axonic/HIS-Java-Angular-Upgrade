package com.param.global.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class PatientSponsorDetailsDto {

	private Integer patientSponsorDetailsId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer serviceId;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private Integer paymentEntitlementId;
	
	private String paymentEntitlementDesc;
	
	private Integer payeeId;
	
	private Integer companyId;
	
	private Integer priorityId;
	
	private String createdDate;
	
	private Integer createdBy;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private Character status;
	
	private Integer visitTypeId;
	
	private Integer tariffId;
	
	private Integer contractId;
	
	private String companyDesc;
	
	private String tpa;
	
	private String insuranceDesc;
	
	private String contractName;
	
	private String validityTo;
	
	private String validityFrom;
	
	private String insuranceNumber;
	
	private BigInteger insuredAmount;
	
	private BigDecimal insuredAmt;
	
	private Integer genderId;
	
	private Integer patientTypeId;
	
	private Integer departmentId;
	
	private Integer subDepartmentId;
	
	private Double departmentCapAmount;
	
	private Integer productCategoryId;
	
	private Integer insuranceId;
	
	private Double quantity;
	
	private Double coSharePer;
	
	private Double amount;
	
	private Integer patientId;
	
	private Integer billingBedCategoryId;
	
	private String tariffDesc;
	
	private String corporateEmpNote;
	
	private Integer applicableDays;
	
	private Boolean tillDischarge;
	
	private Character isContractChanged;
	
	private Character isPayeeUpdated;
	
	private Integer visitId;
	
	private Integer patientClassId;
	
	private String date;
	
	private Integer type;
	
	private String employeeNoManual;
	
	private boolean capExist;
	
	private String employeeName;
	
	private String relation;
	
	private String medicalCardNo;
	
	private String applicableDate;
	
	private String bedName;
	
	private String wardName;
	
	private String claimNumber;
	
	public String getApplicableDate() {
		return applicableDate;
	}

	public void setApplicableDate(String applicableDate) {
		this.applicableDate = applicableDate;
	}

	public boolean isCapExist() {
		return capExist;
	}

	public void setCapExist(boolean capExist) {
		this.capExist = capExist;
	}

	public String getEmployeeNoManual() {
		return employeeNoManual;
	}

	public void setEmployeeNoManual(String employeeNoManual) {
		this.employeeNoManual = employeeNoManual;
	}

	public BigDecimal getInsuredAmt() {
		return insuredAmt;
	}

	public void setInsuredAmt(BigDecimal insuredAmt) {
		this.insuredAmt = insuredAmt;
	}

	public Integer getVisitId() {
		return visitId;
	}

	public void setVisitId(Integer visitId) {
		this.visitId = visitId;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Boolean getTillDischarge() {
		return tillDischarge;
	}

	public void setTillDischarge(Boolean tillDischarge) {
		this.tillDischarge = tillDischarge;
	}

	public Integer getApplicableDays() {
		return applicableDays;
	}

	public void setApplicableDays(Integer applicableDays) {
		this.applicableDays = applicableDays;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getTariffDesc() {
		return tariffDesc;
	}

	public void setTariffDesc(String tariffDesc) {
		this.tariffDesc = tariffDesc;
	}

	public String getInsuranceDesc() {
		return insuranceDesc;
	}

	public void setInsuranceDesc(String insuranceDesc) {
		this.insuranceDesc = insuranceDesc;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getSubDepartmentId() {
		return subDepartmentId;
	}

	public void setSubDepartmentId(Integer subDepartmentId) {
		this.subDepartmentId = subDepartmentId;
	}

	public Double getDepartmentCapAmount() {
		return departmentCapAmount;
	}

	public void setDepartmentCapAmount(Double departmentCapAmount) {
		this.departmentCapAmount = departmentCapAmount;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getInsuranceNumber() {
		return insuranceNumber;
	}

	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}

	public BigInteger getInsuredAmount() {
		return insuredAmount;
	}

	public void setInsuredAmount(BigInteger insuredAmount) {
		this.insuredAmount = insuredAmount;
	}

	public String getValidityTo() {
		return validityTo;
	}

	public void setValidityTo(String validityTo) {
		this.validityTo = validityTo;
	}

	public String getValidityFrom() {
		return validityFrom;
	}

	public void setValidityFrom(String validityFrom) {
		this.validityFrom = validityFrom;
	}

	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}

	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientSponsorDetailsId() {
		return patientSponsorDetailsId;
	}

	public void setPatientSponsorDetailsId(Integer patientSponsorDetailsId) {
		this.patientSponsorDetailsId = patientSponsorDetailsId;
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

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getStatus() {
		return status==null?'I':status;
	}

	public void setStatus(Character status) {
		this.status = status==null?'I':status;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Double getCoSharePer() {
		return coSharePer;
	}

	public void setCoSharePer(Double coSharePer) {
		this.coSharePer = coSharePer;
	}

	public Integer getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String getTpa() {
		return tpa;
	}

	public void setTpa(String tpa) {
		this.tpa = tpa;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getCorporateEmpNote() {
		return corporateEmpNote;
	}

	public void setCorporateEmpNote(String corporateEmpNote) {
		this.corporateEmpNote = corporateEmpNote;
	}

	public Character getIsContractChanged() {
		return isContractChanged;
	}

	public void setIsContractChanged(Character isContractChanged) {
		this.isContractChanged = (isContractChanged == null) ? 'N' : isContractChanged;
	}

	public Character getIsPayeeUpdated() {
		return isPayeeUpdated;
	}

	public void setIsPayeeUpdated(Character isPayeeUpdated) {
		this.isPayeeUpdated = (isPayeeUpdated == null) ? 'N' : isPayeeUpdated;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getMedicalCardNo() {
		return medicalCardNo;
	}

	public void setMedicalCardNo(String medicalCardNo) {
		this.medicalCardNo = medicalCardNo;
	}

	public String getBedName() {
		return bedName;
	}

	public void setBedName(String bedName) {
		this.bedName = bedName;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getClaimNumber() {
		return claimNumber;
	}

	public void setClaimNumber(String claimNumber) {
		this.claimNumber = claimNumber;
	}
	
}
