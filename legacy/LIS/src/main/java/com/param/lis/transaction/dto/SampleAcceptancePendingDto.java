package com.param.lis.transaction.dto;

import java.util.Date;

public class SampleAcceptancePendingDto {
  private Integer labSampleDtlsId;
  private String labSampleNo;
  private String sampleNo;
  private String visitType;
  private String uhid;
  private String patientDetails;
  private String doctorDetails;
  private String testDesc;
  private Character isAlliquoteReq;
  private String wardCode;
  private String bedNumber;
  private String priorityName;
  private String colorCode;
  private String deptName;
  private Character isAcceptOrReject;
  private Character sendToDept;
  private String sampleBarcode;
  private Integer sampleRejectReasonId;
  private String sampleRejectReason;
  private Integer sampWrkStatusId;
  private String panelCode;
  private Integer panelId;
  private Date orderDateTime;
  private Date sampleCollectionDatetime;
  private String testCode;
  private String sampleType;
  private Integer sampleId;
  private Integer genderId;
  private Character sampleRecollectFlag;
  private Integer sampleRecollectAgainstId;
  private Integer visitAdmId;
  private Integer visitTypeId;
  private Integer testId;
  private Integer deptId;
  private Integer orgId;
  private Integer orgUnitId;
  private Integer subDeptId;
  private Integer patientId;
  private Integer orderDetailsId;
  private Integer patientAgeDays;
  private Integer testType;
  private Integer tIncubationId;
  private Integer tMacroExaId;
  private Integer tbiochemicalTestId;
  private Integer sensitivityResultId;
  private Integer microTemplateStatusId;
  private String orderNo;
  private Integer serviceId;
  private String dob;
  private String doa;
  private String pdd;
  private String genderCode;
  private String ageString;
  private Integer admissionId;
  private Integer encounterId;
  private Character isProvisionalRelease;
  private Character isFinalRelease;
  private String patientName;
  private String imagePath;
  private String orderDateTimeString;
  private String sampleCollectionDatetimeString;
  private String billNumber;

  private String orderDate;
  
  private String prefixCode;
  
  private String templateObject;
  
  private String referralName;
  
  private String email;
  
  private Character isVip;
  
  private String visitNo;
  
  private Boolean billSettled;

  private Integer doctorId;
  
  private String entitlementDesc;
  
  private String statusName;
  
  private Integer paymentEntitlementId;
  
  private String companyDesc;
  
  private String comAddress;
  
  private String registerDate;
  private Integer priorityId;

  private String orderDtTime;
  
  private Integer microbiologyTemplateId;
  
  private String reportEntryDate;
	
  private String reportValidationDate;
	
  private String reportReleaseDate;
  
  private Boolean isReportRecall;
  private Boolean isHtmlPrint;
  
  private Integer labTestResId;
  
  private Boolean isDummyService;
  
  private Boolean cultureTest;
  private Integer reportEntryBy;
  private Integer validatedBy;
  private Integer tariffId;

  public Integer getLabSampleDtlsId() {
    return labSampleDtlsId;
  }

  public void setLabSampleDtlsId(Integer labSampleDtlsId) {
    this.labSampleDtlsId = labSampleDtlsId;
  }

  public String getLabSampleNo() {
    return labSampleNo;
  }

  public void setLabSampleNo(String labSampleNo) {
    this.labSampleNo = labSampleNo;
  }

  public String getVisitType() {
    return visitType;
  }

  public void setVisitType(String visitType) {
    this.visitType = visitType;
  }

  public String getUhid() {
    return uhid;
  }

  public void setUhid(String uhid) {
    this.uhid = uhid;
  }

  public String getPatientDetails() {
    return patientDetails;
  }

  public void setPatientDetails(String patientDetails) {
    this.patientDetails = patientDetails;
  }

  public String getDoctorDetails() {
    return doctorDetails;
  }

  public void setDoctorDetails(String doctorDetails) {
    this.doctorDetails = doctorDetails;
  }

  public String getTestDesc() {
    return testDesc;
  }

  public void setTestDesc(String testDesc) {
    this.testDesc = testDesc;
  }

  public Character getIsAlliquoteReq() {
    return isAlliquoteReq;
  }

  public void setIsAlliquoteReq(Character isAlliquoteReq) {
    this.isAlliquoteReq = isAlliquoteReq;
  }

  public String getWardCode() {
    return wardCode;
  }

  public void setWardCode(String wardCode) {
    this.wardCode = wardCode;
  }

  public String getBedNumber() {
    return bedNumber;
  }

  public void setBedNumber(String bedNumber) {
    this.bedNumber = bedNumber;
  }

  public String getPriorityName() {
    return priorityName;
  }

  public void setPriorityName(String priorityName) {
    this.priorityName = priorityName;
  }

  public String getColorCode() {
    return colorCode;
  }

  public void setColorCode(String colorCode) {
    this.colorCode = colorCode;
  }

  public Character getIsAcceptOrReject() {
    return isAcceptOrReject;
  }

  public void setIsAcceptOrReject(Character isAcceptOrReject) {
    this.isAcceptOrReject = isAcceptOrReject;
  }

  public Character getSendToDept() {
    return sendToDept;
  }

  public void setSendToDept(Character sendToDept) {
    this.sendToDept = sendToDept;
  }

  public String getSampleBarcode() {
    return sampleBarcode;
  }

  public void setSampleBarcode(String sampleBarcode) {
    this.sampleBarcode = sampleBarcode;
  }

  public Integer getSampleRejectReasonId() {
    return sampleRejectReasonId;
  }

  public void setSampleRejectReasonId(Integer sampleRejectReasonId) {
    this.sampleRejectReasonId = sampleRejectReasonId;
  }

  public String getSampleRejectReason() {
    return sampleRejectReason;
  }

  public void setSampleRejectReason(String sampleRejectReason) {
    this.sampleRejectReason = sampleRejectReason;
  }

  public String getSampWrkStatusId() {
    return "1"; // dummy code
  }

  public void setSampWrkStatusId(Integer sampWrkStatusId) {
    this.sampWrkStatusId = sampWrkStatusId;
  }

  public String getPanelCode() {
    return panelCode;
  }

  public void setPanelCode(String panelCode) {
    this.panelCode = panelCode;
  }

  public Date getOrderDateTime() {
    return orderDateTime;
  }

  public void setOrderDateTime(Date orderDateTime) {
    this.orderDateTime = orderDateTime;
  }

  public Date getSampleCollectionDatetime() {
    return sampleCollectionDatetime;
  }

  public void setSampleCollectionDatetime(Date sampleCollectionDatetime) {
    this.sampleCollectionDatetime = sampleCollectionDatetime;
  }

  public String getTestCode() {
    return testCode;
  }

  public void setTestCode(String testCode) {
    this.testCode = testCode;
  }

  public String getSampleType() {
    return sampleType;
  }

  public void setSampleType(String sampleType) {
    this.sampleType = sampleType;
  }

  public Integer getSampleId() {
    return sampleId;
  }

  public void setSampleId(Integer sampleId) {
    this.sampleId = sampleId;
  }

  public Integer getPanelId() {
    return panelId;
  }

  public void setPanelId(Integer panelId) {
    this.panelId = panelId;
  }

  public Integer getGenderId() {
    return genderId;
  }

  public void setGenderId(Integer genderId) {
    this.genderId = genderId;
  }

  public Character getSampleRecollectFlag() {
    return sampleRecollectFlag;
  }

  public void setSampleRecollectFlag(Character sampleRecollectFlag) {
    this.sampleRecollectFlag = sampleRecollectFlag;
  }

  public Integer getSampleRecollectAgainstId() {
    return sampleRecollectAgainstId;
  }

  public void setSampleRecollectAgainstId(Integer sampleRecollectAgainstId) {
    this.sampleRecollectAgainstId = sampleRecollectAgainstId;
  }

  public Integer getVisitAdmId() {
    return visitAdmId;
  }

  public void setVisitAdmId(Integer visitAdmId) {
    this.visitAdmId = visitAdmId;
  }

  public Integer getVisitTypeId() {
    return visitTypeId;
  }

  public void setVisitTypeId(Integer visitTypeId) {
    this.visitTypeId = visitTypeId;
  }

  public Integer getTestId() {
    return testId;
  }

  public void setTestId(Integer testId) {
    this.testId = testId;
  }

  public Integer getDeptId() {
    return deptId;
  }

  public void setDeptId(Integer deptId) {
    this.deptId = deptId;
  }

  public Integer getSubDeptId() {
    return subDeptId;
  }

  public void setSubDeptId(Integer subDeptId) {
    this.subDeptId = subDeptId;
  }

  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  public Integer getOrderDetailsId() {
    return orderDetailsId;
  }

  public void setOrderDetailsId(Integer orderDetailsId) {
    this.orderDetailsId = orderDetailsId;
  }

  public Integer getPatientAgeDays() {
    return patientAgeDays;
  }

  public void setPatientAgeDays(Integer patientAgeDays) {
    this.patientAgeDays = patientAgeDays;
  }

  public Integer getTestType() {
    return testType;
  }

  public void setTestType(Integer testType) {
    this.testType = testType;
  }

  public Integer gettIncubationId() {
    return tIncubationId;
  }

  public void settIncubationId(Integer tIncubationId) {
    this.tIncubationId = tIncubationId;
  }

  public Integer gettMacroExaId() {
    return tMacroExaId;
  }

  public void settMacroExaId(Integer tMacroExaId) {
    this.tMacroExaId = tMacroExaId;
  }

  public Integer getTbiochemicalTestId() {
    return tbiochemicalTestId;
  }

  public void setTbiochemicalTestId(Integer tbiochemicalTestId) {
    this.tbiochemicalTestId = tbiochemicalTestId;
  }

  public Integer getSensitivityResultId() {
    return sensitivityResultId;
  }

  public void setSensitivityResultId(Integer sensitivityResultId) {
    this.sensitivityResultId = sensitivityResultId;
  }

  public Integer getOrgId() {
    return orgId;
  }

  public void setOrgId(Integer orgId) {
    this.orgId = orgId;
  }

  public Integer getOrgUnitId() {
    return orgUnitId;
  }

  public void setOrgUnitId(Integer orgUnitId) {
    this.orgUnitId = orgUnitId;
  }

  public String getSampleNo() {
    return sampleNo;
  }

  public void setSampleNo(String sampleNo) {
    this.sampleNo = sampleNo;
  }

public String getDeptName() {
	return deptName;
}

public void setDeptName(String deptName) {
	this.deptName = deptName;
}

public Integer getMicroTemplateStatusId() {
	return microTemplateStatusId;
}

public void setMicroTemplateStatusId(Integer microTemplateStatusId) {
	this.microTemplateStatusId = microTemplateStatusId;
}

public String getOrderNo() {
	return orderNo;
}

public void setOrderNo(String orderNo) {
	this.orderNo = orderNo;
}

public Integer getServiceId() {
	return serviceId;
}

public void setServiceId(Integer serviceId) {
	this.serviceId = serviceId;
}

public String getDob() {
	return dob;
}

public void setDob(String dob) {
	this.dob = dob;
}

public String getDoa() {
	return doa;
}

public void setDoa(String doa) {
	this.doa = doa;
}

public String getPdd() {
	return pdd;
}

public void setPdd(String pdd) {
	this.pdd = pdd;
}

public String getGenderCode() {
	return genderCode;
}

public void setGenderCode(String genderCode) {
	this.genderCode = genderCode;
}

public String getAgeString() {
	return ageString;
}

public void setAgeString(String ageString) {
	this.ageString = ageString;
}

public Integer getAdmissionId() {
	return admissionId;
}

public void setAdmissionId(Integer admissionId) {
	this.admissionId = admissionId;
}

public Integer getEncounterId() {
	return encounterId;
}

public void setEncounterId(Integer encounterId) {
	this.encounterId = encounterId;
}

public Character getIsProvisionalRelease() {
	return isProvisionalRelease;
}

public void setIsProvisionalRelease(Character isProvisionalRelease) {
	this.isProvisionalRelease = isProvisionalRelease;
}

public Character getIsFinalRelease() {
	return isFinalRelease;
}

public void setIsFinalRelease(Character isFinalRelease) {
	this.isFinalRelease = isFinalRelease;
}

public String getPatientName() {
	return patientName;
}

public void setPatientName(String patientName) {
	this.patientName = patientName;
}

public String getOrderDate() {
	return orderDate;
}

public void setOrderDate(String orderDate) {
	this.orderDate = orderDate;
}

public String getPrefixCode() {
	return prefixCode;
}

public void setPrefixCode(String prefixCode) {
	this.prefixCode = prefixCode;
}

public String getTemplateObject() {
	return templateObject;
}

public void setTemplateObject(String templateObject) {
	this.templateObject = templateObject;
}

public String getReferralName() {
	return referralName;
}

public void setReferralName(String referralName) {
	this.referralName = referralName;
}

public String getEmail() {
	return email;
}

public void setEmail(String email) {
	this.email = email;
}

public Character getIsVip() {
	return isVip;
}

public void setIsVip(Character isVip) {
	this.isVip = isVip;
}

public String getVisitNo() {
	return visitNo;
}

public void setVisitNo(String visitNo) {
	this.visitNo = visitNo;
}

public String getImagePath() {
	return imagePath;
}

public void setImagePath(String imagePath) {
	this.imagePath = imagePath;
}

public Boolean getBillSettled() {
	return billSettled;
}

public void setBillSettled(Boolean billSettled) {
	this.billSettled = billSettled;
}

public Integer getDoctorId() {
	return doctorId;
}

public void setDoctorId(Integer doctorId) {
	this.doctorId = doctorId;
}

public String getEntitlementDesc() {
	return entitlementDesc;
}

public void setEntitlementDesc(String entitlementDesc) {
	this.entitlementDesc = entitlementDesc;
}

public String getStatusName() {
	return statusName;
}

public void setStatusName(String statusName) {
	this.statusName = statusName;
}

public Integer getPaymentEntitlementId() {
	return paymentEntitlementId;
}

public void setPaymentEntitlementId(Integer paymentEntitlementId) {
	this.paymentEntitlementId = paymentEntitlementId;
}

public String getCompanyDesc() {
	return companyDesc;
}

public void setCompanyDesc(String companyDesc) {
	this.companyDesc = companyDesc;
}

public String getComAddress() {
	return comAddress;
}

public void setComAddress(String comAddress) {
	this.comAddress = comAddress;
}

public String getRegisterDate() {
	return registerDate;
}

public void setRegisterDate(String registerDate) {
	this.registerDate = registerDate;
}

public String getOrderDtTime() {
	return orderDtTime;
}

public void setOrderDtTime(String orderDtTime) {
	this.orderDtTime = orderDtTime;
}

public Integer getPriorityId() {
	return priorityId;
}

public void setPriorityId(Integer priorityId) {
	this.priorityId = priorityId;
}

public Integer getMicrobiologyTemplateId() {
	return microbiologyTemplateId;
}

public void setMicrobiologyTemplateId(Integer microbiologyTemplateId) {
	this.microbiologyTemplateId = microbiologyTemplateId;
}

public String getReportEntryDate() {
	return reportEntryDate;
}

public void setReportEntryDate(String reportEntryDate) {
	this.reportEntryDate = reportEntryDate;
}

public String getReportValidationDate() {
	return reportValidationDate;
}

public void setReportValidationDate(String reportValidationDate) {
	this.reportValidationDate = reportValidationDate;
}

public String getReportReleaseDate() {
	return reportReleaseDate;
}

public void setReportReleaseDate(String reportReleaseDate) {
	this.reportReleaseDate = reportReleaseDate;
}

public Boolean getIsReportRecall() {
	return isReportRecall;
}

public void setIsReportRecall(Boolean isReportRecall) {
	this.isReportRecall = isReportRecall;
}

public Boolean getIsHtmlPrint() {
	return isHtmlPrint;
}

public void setIsHtmlPrint(Boolean isHtmlPrint) {
	this.isHtmlPrint = isHtmlPrint;
}

public String getOrderDateTimeString() {
	return orderDateTimeString;
}

public void setOrderDateTimeString(String orderDateTimeString) {
	this.orderDateTimeString = orderDateTimeString;
}

public String getSampleCollectionDatetimeString() {
	return sampleCollectionDatetimeString;
}

public void setSampleCollectionDatetimeString(String sampleCollectionDatetimeString) {
	this.sampleCollectionDatetimeString = sampleCollectionDatetimeString;
}

public String getBillNumber() {
	return billNumber;
}

public void setBillNumber(String billNumber) {
	this.billNumber = billNumber;
}

public Integer getLabTestResId() {
	return labTestResId;
}

public void setLabTestResId(Integer labTestResId) {
	this.labTestResId = labTestResId;
}

public Boolean getIsDummyService() {
	return isDummyService;
}

public void setIsDummyService(Boolean isDummyService) {
	this.isDummyService = isDummyService;
}

public Boolean getCultureTest() {
	return cultureTest;
}

public void setCultureTest(Boolean cultureTest) {
	this.cultureTest = cultureTest;
}

  public Integer getReportEntryBy() {
    return reportEntryBy;
  }

  public void setReportEntryBy(Integer reportEntryBy) {
    this.reportEntryBy = reportEntryBy;
  }

  public Integer getValidatedBy() {
    return validatedBy;
  }

  public void setValidatedBy(Integer validatedBy) {
    this.validatedBy = validatedBy;
  }

  public Integer getTariffId() {
    return tariffId;
  }

  public void setTariffId(Integer tariffId) {
    this.tariffId = tariffId;
  }
}
