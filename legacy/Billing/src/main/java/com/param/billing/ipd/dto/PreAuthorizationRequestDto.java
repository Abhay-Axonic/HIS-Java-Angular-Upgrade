package com.param.billing.ipd.dto;

import java.math.BigInteger;
import java.util.Date;

public class PreAuthorizationRequestDto {
	
	private Integer admissionId;
	private Integer visitTypeId;
	private String visitTypeCode;
	private String paymentEntitlementDesc;
	private Integer paymentEntitlementId;
	private String visitDate;
	private String admissionNo;
	private String patientName;
	private Integer payeeId;
	private Integer contractId;
	private String companyDesc;	
	private Integer companyTypeId;
	private Integer companyId;
	private String contractName;
	private Integer patientId;
	private Integer patientSponsorDetailsId;
	private String dob;
	private String uhidNumber;
	private String  genderCode;
	private String  genderDec;
	private Character isAuthDeleted;
	private Integer authStatus;
	private Integer authRequestSendThroughId;
	private Date authorizationDate;
	private Character isFinalSubmit;
	private String requestRemark;
	private String  authRemark;
	private String receivedByName;
	private String requestSendByName;
	private String  insuranceNumber;
	private String validFrom;
	private String validTo;
	private Date validFromNew;
	private Date validToNew;
	private String insuranceDesc;
	private Integer unitId;
	private String dischargeDate;
	private String age;
	private String bedNo;
	private String doctorName;
	private String imagePath;
	private Integer genderId;
    private BigInteger count;
	private Integer offset;
	private Integer recordPerPage;
	
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getInsuranceDesc() {
		return insuranceDesc;
	}
	public void setInsuranceDesc(String insuranceDesc) {
		this.insuranceDesc = insuranceDesc;
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
	public String getVisitTypeCode() {
		return visitTypeCode;
	}
	public void setVisitTypeCode(String visitTypeCode) {
		this.visitTypeCode = visitTypeCode;
	}
	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}
	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public String getAdmissionNo() {
		return admissionNo;
	}
	public void setAdmissionNo(String admissionNo) {
		this.admissionNo = admissionNo;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public String getCompanyDesc() {
		return companyDesc;
	}
	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}
	public Integer getCompanyTypeId() {
		return companyTypeId;
	}
	public void setCompanyTypeId(Integer companyTypeId) {
		this.companyTypeId = companyTypeId;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getPatientSponsorDetailsId() {
		return patientSponsorDetailsId;
	}
	public void setPatientSponsorDetailsId(Integer patientSponsorDetailsId) {
		this.patientSponsorDetailsId = patientSponsorDetailsId;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
	public String getUhidNumber() {
		return uhidNumber;
	}
	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public String getGenderDec() {
		return genderDec;
	}
	public void setGenderDec(String genderDec) {
		this.genderDec = genderDec;
	}
	public Character getIsAuthDeleted() {
		return isAuthDeleted;
	}
	public void setIsAuthDeleted(Character isAuthDeleted) {
		this.isAuthDeleted = isAuthDeleted;
	}
	public Integer getAuthStatus() {
		return authStatus;
	}
	public void setAuthStatus(Integer authStatus) {
		this.authStatus = authStatus;
	}
	public Integer getAuthRequestSendThroughId() {
		return authRequestSendThroughId;
	}
	public void setAuthRequestSendThroughId(Integer authRequestSendThroughId) {
		this.authRequestSendThroughId = authRequestSendThroughId;
	}
	public Date getAuthorizationDate() {
		return authorizationDate;
	}
	public void setAuthorizationDate(Date authorizationDate) {
		this.authorizationDate = authorizationDate;
	}
	public Character getIsFinalSubmit() {
		return isFinalSubmit;
	}
	public void setIsFinalSubmit(Character isFinalSubmit) {
		this.isFinalSubmit = isFinalSubmit;
	}
	public String getRequestRemark() {
		return requestRemark;
	}
	public void setRequestRemark(String requestRemark) {
		this.requestRemark = requestRemark;
	}
	public String getAuthRemark() {
		return authRemark;
	}
	public void setAuthRemark(String authRemark) {
		this.authRemark = authRemark;
	}
	public String getReceivedByName() {
		return receivedByName;
	}
	public void setReceivedByName(String receivedByName) {
		this.receivedByName = receivedByName;
	}
	public String getRequestSendByName() {
		return requestSendByName;
	}
	public void setRequestSendByName(String requestSendByName) {
		this.requestSendByName = requestSendByName;
	}
	public String getInsuranceNumber() {
		return insuranceNumber;
	}
	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}
	public String getValidFrom() {
		return validFrom;
	}
	public void setValidFrom(String validFrom) {
		this.validFrom = validFrom;
	}
	public String getValidTo() {
		return validTo;
	}
	public void setValidTo(String validTo) {
		this.validTo = validTo;
	}
	public String getDischargeDate() {
		return dischargeDate;
	}
	public void setDischargeDate(String dischargeDate) {
		this.dischargeDate = dischargeDate;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getBedNo() {
		return bedNo;
	}
	public void setBedNo(String bedNo) {
		this.bedNo = bedNo;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public Integer getGenderId() {
		return genderId;
	}
	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}
	public Date getValidToNew() {
		return validToNew;
	}
	public void setValidToNew(Date validToNew) {
		this.validToNew = validToNew;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getRecordPerPage() {
		return recordPerPage;
	}
	public void setRecordPerPage(Integer recordPerPage) {
		this.recordPerPage = recordPerPage;
	}
	public BigInteger getCount() {
		return count;
	}
	public void setCount(BigInteger count) {
		this.count = count;
	}
	
	
	
}
