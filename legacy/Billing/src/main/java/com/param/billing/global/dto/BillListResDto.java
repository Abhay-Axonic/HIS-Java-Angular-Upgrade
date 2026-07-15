package com.param.billing.global.dto;

import java.math.BigInteger;
import java.util.Date;

public class BillListResDto {
	private Integer billingMasterId;
	private String billDate;
	private Date billDateNew;
	private String billNumber;
	private String uhidNumber;
	private String patientName;
	private Integer visitTypeId;
	private Character isOtcReg;
	private String visitNo;
	private String visitDate;
	private Date visitDateNew;
	private Double totalBillAmount;
	private Double netAmount;
	private Double amountPaid;
	private Double outstanding;
	private Integer billStatusId;
	private Integer orgId;
	private Integer unitId;
	private Integer patientId;
	private Integer visitId;
	private Integer appointmentId;
	private Character isDrNote;
	private String againstbillNumber;
	private String userName;
	private Integer isInterimOrFinalBill;
	private Integer doctorId;
	private Double totalSelfPayable;
	private Double totalCreditPayable;
	private Integer referalDocId;
	private Double intermediateSelfDue;
	private Character isNursingAssessmentRequired;
	private Double disallowedAmount;
	private Integer disallowedAgainstInvoiceDtlsId;
	private Double totalCredit;
	private Double amountReceived;
	private Integer doctorCrId;
	private Integer billPayeeDetailsId;
	private String fromDate;
	private String toDate;
	private String createdDate;
	private Double patientPayable;
	private String birthDate;
	private String ageString;
	private String imagePath;
	private Integer genderId;
	private String age;
	private String uhIdNumber;
	private String genderName;
	private String mobileNo;
	private String identificationName;
	private String identificationNo;
	private String reasonDesc;
	private String remark;
	private Character isPaidAppointment;
	private Double selfDueAmount;
	private Character isPrint; 
	private BigInteger pendingCount;
	private Character isFinancialClearance;
	private String associateCompanyDesc;
	private String paymentEntitlementDesc;
	private String genderCode;
	private String doctorName;
	private String specialityName;
	private String email;
	private Integer paymentEntitlementId;
	private String countryCallingCode;
	private String firstName;
	private Character billReopened;
	private Integer encounterId;
	private Integer admissionId;
	private Integer bedId;
	private Integer wardId;
	private String pdd;
	private Character isDischarge;
	private Integer dischargeStatusId;
	
	
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public Character getIsFinancialClearance() {
		return isFinancialClearance;
	}
	public void setIsFinancialClearance(Character isFinancialClearance) {
		this.isFinancialClearance = isFinancialClearance;
	}
	public BigInteger getPendingCount() {
		return pendingCount;
	}
	public void setPendingCount(BigInteger pendingCount) {
		this.pendingCount = pendingCount;
	}
	public Character getIsPrint() {
		return isPrint;
	}
	public void setIsPrint(Character isPrint) {
		this.isPrint = isPrint;
	}
	public Double getSelfDueAmount() {
		return selfDueAmount;
	}
	public void setSelfDueAmount(Double selfDueAmount) {
		this.selfDueAmount = selfDueAmount;
	}
	public Character getIsPaidAppointment() {
		return isPaidAppointment;
	}
	public void setIsPaidAppointment(Character isPaidAppointment) {
		this.isPaidAppointment = isPaidAppointment;
	}
	public String getReasonDesc() {
		return reasonDesc;
	}
	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getUhIdNumber() {
		return uhIdNumber;
	}
	public void setUhIdNumber(String uhIdNumber) {
		this.uhIdNumber = uhIdNumber;
	}
	public String getGenderName() {
		return genderName;
	}
	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getIdentificationName() {
		return identificationName;
	}
	public void setIdentificationName(String identificationName) {
		this.identificationName = identificationName;
	}
	public String getIdentificationNo() {
		return identificationNo;
	}
	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}
	public Integer getGenderId() {
		return genderId;
	}
	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public String getAgeString() {
		return ageString;
	}
	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}
	public String getImagePath() {
		return imagePath;
	}
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	public Double getPatientPayable() {
		return patientPayable;
	}
	public void setPatientPayable(Double patientPayable) {
		this.patientPayable = patientPayable;
	}
	public Integer getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}
	public void setBillPayeeDetailsId(Integer billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
	}
	public Integer getDoctorCrId() {
		return doctorCrId;
	}
	public void setDoctorCrId(Integer doctorCrId) {
		this.doctorCrId = doctorCrId;
	}
	public Double getTotalCredit() {
		return totalCredit;
	}
	public void setTotalCredit(Double totalCredit) {
		this.totalCredit = totalCredit;
	}
	public Double getAmountReceived() {
		return amountReceived;
	}
	public void setAmountReceived(Double amountReceived) {
		this.amountReceived = amountReceived;
	}
	public Double getDisallowedAmount() {
		return disallowedAmount;
	}
	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}
	public Integer getDisallowedAgainstInvoiceDtlsId() {
		return disallowedAgainstInvoiceDtlsId;
	}
	public void setDisallowedAgainstInvoiceDtlsId(Integer disallowedAgainstInvoiceDtlsId) {
		this.disallowedAgainstInvoiceDtlsId = disallowedAgainstInvoiceDtlsId;
	}
	public Character getIsNursingAssessmentRequired() {
		return isNursingAssessmentRequired;
	}
	public void setIsNursingAssessmentRequired(Character isNursingAssessmentRequired) {
		this.isNursingAssessmentRequired = isNursingAssessmentRequired;
	}
	public Double getIntermediateSelfDue() {
		return intermediateSelfDue;
	}
	public void setIntermediateSelfDue(Double intermediateSelfDue) {
		this.intermediateSelfDue = intermediateSelfDue;
	}
	public Integer getReferalDocId() {
		return referalDocId;
	}
	public void setReferalDocId(Integer referalDocId) {
		this.referalDocId = referalDocId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Double getTotalSelfPayable() {
		return totalSelfPayable;
	}
	public void setTotalSelfPayable(Double totalSelfPayable) {
		this.totalSelfPayable = totalSelfPayable;
	}
	public Double getTotalCreditPayable() {
		return totalCreditPayable;
	}
	public void setTotalCreditPayable(Double totalCreditPayable) {
		this.totalCreditPayable = totalCreditPayable;
	}
	public Integer getIsInterimOrFinalBill() {
		return isInterimOrFinalBill;
	}
	public void setIsInterimOrFinalBill(Integer isInterimOrFinalBill) {
		this.isInterimOrFinalBill = isInterimOrFinalBill;
	}
	public Integer getVisitId() {
		return visitId;
	}
	public void setVisitId(Integer visitId) {
		this.visitId = visitId;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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
	public Integer getBillingMasterId() {
		return billingMasterId;
	}
	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}
	public String getBillDate() {
		return billDate;
	}
	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}
	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	public String getUhidNumber() {
		return uhidNumber;
	}
	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public Character getIsOtcReg() {
		return isOtcReg;
	}
	public void setIsOtcReg(Character isOtcReg) {
		this.isOtcReg = (isOtcReg != '\u0000' ? isOtcReg : 'I');
	}
	public String getVisitNo() {
		return visitNo;
	}
	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	public String getVisitDate() {
		return visitDate;
	}
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	public Double getTotalBillAmount() {
		return totalBillAmount;
	}
	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}
	public Double getNetAmount() {
		return netAmount;
	}
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	public Double getAmountPaid() {
		return amountPaid;
	}
	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}
	public Double getOutstanding() {
		return outstanding;
	}
	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}
	public Integer getBillStatusId() {
		return billStatusId;
	}
	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}
	public Character getIsDrNote() {
		return isDrNote;
	}
	public void setIsDrNote(Character isDrNote) {
		this.isDrNote = isDrNote;
	}
	public String getAgainstbillNumber() {
		return againstbillNumber;
	}
	public void setAgainstbillNumber(String againstbillNumber) {
		this.againstbillNumber = againstbillNumber;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
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
	public String getCreatedDate() {
		return createdDate;
	}
	public String getAssociateCompanyDesc() {
		return associateCompanyDesc;
	}
	public void setAssociateCompanyDesc(String associateCompanyDesc) {
		this.associateCompanyDesc = associateCompanyDesc;
	}
	public String getPaymentEntitlementDesc() {
		return paymentEntitlementDesc;
	}
	public void setPaymentEntitlementDesc(String paymentEntitlementDesc) {
		this.paymentEntitlementDesc = paymentEntitlementDesc;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Date getBillDateNew() {
		return billDateNew;
	}
	public void setBillDateNew(Date billDateNew) {
		this.billDateNew = billDateNew;
	}
	public Date getVisitDateNew() {
		return visitDateNew;
	}
	public void setVisitDateNew(Date visitDateNew) {
		this.visitDateNew = visitDateNew;
	}
	public String getCountryCallingCode() {
		return countryCallingCode;
	}
	public void setCountryCallingCode(String countryCallingCode) {
		this.countryCallingCode = countryCallingCode;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public Character getBillReopened() {
		return billReopened;
	}
	public void setBillReopened(Character billReopened) {
		this.billReopened = billReopened;
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
	public Integer getBedId() {
		return bedId;
	}
	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}
	public Integer getWardId() {
		return wardId;
	}
	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}
	public String getPdd() {
		return pdd;
	}
	public void setPdd(String pdd) {
		this.pdd = pdd;
	}
	public Character getIsDischarge() {
		return isDischarge;
	}
	public void setIsDischarge(Character isDischarge) {
		this.isDischarge = isDischarge;
	}
	public Integer getDischargeStatusId() {
		return dischargeStatusId;
	}
	public void setDischargeStatusId(Integer dischargeStatusId) {
		this.dischargeStatusId = dischargeStatusId;
	}
	
	
}
