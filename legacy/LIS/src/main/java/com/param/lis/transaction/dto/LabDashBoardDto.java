package com.param.lis.transaction.dto;

import java.util.Date;

public class LabDashBoardDto {

	private String sampleNo;
	private String uhid;
	private String visitType;
	private String genderName;
	private Date dob;
	private String doctorDetails;
	private String patientDetails;
	private String deptName;
	private String testDesc;
	private String specimenType;
	private String outSourceLab;
	private String labSampleStatus;
	private Integer labSampleDtlsId;
	private Integer labSampleId;
	private String sampleType;
	private String testCode;
	private String panelCode;
	private String priority;
	private String subSpecialityName;
	private String specimanName;
	private String status;
	private Date orderDateTime;
	private Date sampleCollectionDatetime;
	private String assignUnitName;
    private String entitlementDesc;
    private Integer paymentEntitlementId;
    private Integer billingMasterId;
    private String visitNo;
    private Integer visitTypeId;
    private Integer patientId;
    private Integer genderId;
    private String mobileNo;
	private String birthDate;
	private String ageString;
	private String imagePath;
	private Integer doctorId;
	private Double intermediateSelfDue;
	private Integer referalDocId;
	private String visitDate;
	private Integer visitId;
	private Integer appointmentId;
	private Character isPaidAppointment;
	private Double totalBillAmount;
	private Double netAmount;
	private Double amountPaid;
	private Double outstanding;
	private Integer billStatusId;
	private Integer isInterimOrFinalBill;
	private Double totalSelfPayable;
	private Double totalCreditPayable;
	private Double selfDueAmount;
	private Character isPrint; 
	private Integer sampleStatusId;
	private String internalNotes;
	private String srfId;
	private String icmrId;
	private String packageName;
	private String panelName;
	private String referralName;
	private String companyOrInsuranceName;
    private String orderDateTimeString;
    private String billNumber;

    private String orderDateTimeStringNew;

    private Boolean isDummyService;
    
	public String getOrderDateTimeString() {
		return orderDateTimeString;
	}

	public void setOrderDateTimeString(String orderDateTimeString) {
		this.orderDateTimeString = orderDateTimeString;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
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

	public Character getIsPaidAppointment() {
		return isPaidAppointment;
	}

	public void setIsPaidAppointment(Character isPaidAppointment) {
		this.isPaidAppointment = isPaidAppointment;
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

	public Integer getIsInterimOrFinalBill() {
		return isInterimOrFinalBill;
	}

	public void setIsInterimOrFinalBill(Integer isInterimOrFinalBill) {
		this.isInterimOrFinalBill = isInterimOrFinalBill;
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

	public Double getSelfDueAmount() {
		return selfDueAmount;
	}

	public void setSelfDueAmount(Double selfDueAmount) {
		this.selfDueAmount = selfDueAmount;
	}

	public Character getIsPrint() {
		return isPrint;
	}

	public void setIsPrint(Character isPrint) {
		this.isPrint = isPrint;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public String getGenderName() {
		return genderName;
	}

	public void setGenderName(String genderName) {
		this.genderName = genderName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getDoctorDetails() {
		return doctorDetails;
	}

	public void setDoctorDetails(String doctorDetails) {
		this.doctorDetails = doctorDetails;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public String getOutSourceLab() {
		return outSourceLab;
	}

	public void setOutSourceLab(String outSourceLab) {
		this.outSourceLab = outSourceLab;
	}

	public String getLabSampleStatus() {
		return labSampleStatus;
	}

	public void setLabSampleStatus(String labSampleStatus) {
		this.labSampleStatus = labSampleStatus;
	}

	public String getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(String patientDetails) {
		this.patientDetails = patientDetails;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getSampleType() {
		return sampleType;
	}

	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}

	public String getTestCode() {
		return testCode;
	}

	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}

	public String getPanelCode() {
		return panelCode;
	}

	public void setPanelCode(String panelCode) {
		this.panelCode = panelCode;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public String getSpecimanName() {
		return specimanName;
	}

	public void setSpecimanName(String specimanName) {
		this.specimanName = specimanName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(Date orderDateTime) {
        this.orderDateTimeString = orderDateTime.toString();
		this.orderDateTime = orderDateTime;
	}

	public Date getSampleCollectionDatetime() {
		return sampleCollectionDatetime;
	}

	public void setSampleCollectionDatetime(Date sampleCollectionDatetime) {
		this.sampleCollectionDatetime = sampleCollectionDatetime;
	}

	public Integer getLabSampleId() {
		return labSampleId;
	}

	public void setLabSampleId(Integer labSampleId) {
		this.labSampleId = labSampleId;
	}

	public String getAssignUnitName() {
		return assignUnitName;
	}

	public void setAssignUnitName(String assignUnitName) {
		this.assignUnitName = assignUnitName;
	}

	public String getEntitlementDesc() {
		return entitlementDesc;
	}

	public void setEntitlementDesc(String entitlementDesc) {
		this.entitlementDesc = entitlementDesc;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getSampleStatusId() {
		return sampleStatusId;
	}

	public void setSampleStatusId(Integer sampleStatusId) {
		this.sampleStatusId = sampleStatusId;
	}

	public String getInternalNotes() {
		return internalNotes;
	}

	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}

	public String getSrfId() {
		return srfId;
	}

	public void setSrfId(String srfId) {
		this.srfId = srfId;
	}

	public String getIcmrId() {
		return icmrId;
	}

	public void setIcmrId(String icmrId) {
		this.icmrId = icmrId;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPanelName() {
		return panelName;
	}

	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}

	public String getReferralName() {
		return referralName;
	}

	public void setReferralName(String referralName) {
		this.referralName = referralName;
	}

	public String getCompanyOrInsuranceName() {
		return companyOrInsuranceName;
	}

	public void setCompanyOrInsuranceName(String companyOrInsuranceName) {
		this.companyOrInsuranceName = companyOrInsuranceName;
	}


	public String getOrderDateTimeStringNew() {
		return orderDateTimeStringNew;
	}

	public void setOrderDateTimeStringNew(String orderDateTimeStringNew) {
		this.orderDateTimeStringNew = orderDateTimeStringNew;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public Boolean getIsDummyService() {
		return isDummyService;
	}

	public void setIsDummyService(Boolean isDummyService) {
		this.isDummyService = isDummyService;
	}

	
	
}
