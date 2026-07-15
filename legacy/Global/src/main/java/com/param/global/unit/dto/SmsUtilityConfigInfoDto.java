package com.param.global.unit.dto;

import java.math.BigDecimal;
import java.util.List;

public class SmsUtilityConfigInfoDto {

	private String patientName;
	
	private String hospitalName;

	private String patientMobileNo;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private String smsTemplateMessage;
	
	private String dateTime;
	
	private String patientUhidNo;
	
	private String mobileNo;
	
	private String referringDoctorMobileNo;
	
	private String treatingDoctorMobileNo;
	
	private String admitingDoctorMobileNo;
	
	private String referringDoctorName;
	
	private Integer referringDoctorId;
	
	private String consultingDoctorMobileNo;
	
	private String consultingDoctorName;
	
	private String treatingDoctorName;
	
	private String admitingDoctorName;
	
	private String bedDetails;
	
	private Integer admitingDoctorId;
	
	private Integer treatingDoctorId;
	
	private Integer consultingDoctorId;
	
	private Integer patientId;
	
	private String senderId;
	
	private BigDecimal billAmount;
	
	private BigDecimal selfAmount;
	
	private BigDecimal creditAmount;
	
	private BigDecimal outstandingAmount;
	
	private BigDecimal paidAmount;
	
	private Integer billId;
	
	private Integer depositId;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private Integer triggerId;
	
	private String templateDescription;
	
	private Integer orgId;
	
	private String triggerName;

	private String categoryName;
	
	private Integer categoryId;
	
	private Integer smsUtilityId ;
	
	private String depositNo;
	
	private String encounterNo;
	
	private String billNo;
	
	private String recieptNo;
	
	private String admissionNo;
	
	private String creditNoteNumber;
	
	private String debitNoteNumber;
	
	private String refundNumber;
	
	private String companyAdmissionPatientCount;
	
	private String selfAdmissionPatientCount;
	
	private String selfDischargePatientCount;
	
	private String companyDischargePatientCount;
	
	private String opdNewCount;
	
	private String opdFollowUpCount;
	
	private String collectionAmount;
	
	private String refundAmount;
	
	private String depositeAmount;
	
	private String deathCount;
	
	private String age;
	
	private String treatingEmail;
	
	private String consultingEmail;
	
	private String admitingEmail;
	
	private String referringEmail;
	
	private String patientEmail;
	
	private String toEmailId;
	
	private String mailSubject;
	
	private String rptReport;
	
	private Integer depositeConsumptionId;
	
	private Integer recieptId;
	
	private Integer appointmentId;
	
	private Integer encounterTypeId;
	
	private Integer labSampleDtlsId;
	
	private String rptFileName;
	
	private String testDesc;
	
	private String risHeader;

	private String risBody;

	private String risFooter;
	
	private Integer templateTransactionId;
	
	private List<Integer> labSampleDtlsIdsList; 
	
	private String password; 
	
	private String userName; 
	
	private String applicationLink ;
	
	private Integer employeeId;
	
	private String employeeName; 
	
	private String employeeEmail; 
	
	private Integer insuranceId;
	
	private String insuranceEmail;
	
	private String insuranceName;
	
	private Integer companyId;
	
	private String companyEmail;
	
	private String companyName;
	
	private String smtpHost;
	
	private String smtpPort;
	
	private String mailId;
	
	private String mailPassword;
	
	private String pdfGenerationUrl;
	private Integer paymentEntitlementId;
	private Integer payeeId;
	private List<BulkEmailDto> patientList;
	
	private String patientWhatsAppNo;
	
	private String companyMobileNo;
	
	private String insuranceMobileNo;
	
	private String employeeMobileNo;
	
	private String admitingDrMobileNo;
	
	private String consultingDrMobileNo;
	
	private String treatingDrMobileNo;
	
	private String toMobileNumber;
	
	private Character isWhatsappSend;
	
	private Character isSMSSend;
	
	 private String unitFolder;
	 
	 private String uniqueId;
	 
	 private Integer userId;
	 
	 private String patientIdList;
	 
		private String reportString;
		
		private String cyclicOrderDate;
		
	private String labReportUrl;	
	
	private String srfId;
	
	private Integer consentTransactionId;
	
	private String consentMasterName;
	
	private String consentTypeMasterName;
	
	private String formTemplateId;
	
	private String documentPath;
	
	private String documentName;
	
	private String visitNumber;

	private String visitType;

	private Integer visitTypeId;

	private Integer templateType;
	
	private String gender;
	
	private String proceduceName;
	
	private String htmlTemplateData;
	
	private String userDefinedMail;
	
	private String vaccineName;
	
	private String siteName;
	
	private String triggerType;
	
	private String seriesName;
	
	private Integer whatsAppOption;
	
	private String videoCallLink ;
	private String templateDesc;
	private Integer appointmentUniqueId;
	private String reportUrl;
	private String reportMsgUrl;
	private List<Integer> templateTransactionIds;
	
	private Integer skipSaveMessageQueue;

	private String message;
	private String whatsaAppmessagehel;
	private Character criticalReport;
	
	private Double totalDepositAmount;
	
	private Double totalPendingDuaAmount;
	
	private Integer costEstimateId;


	private String criticalUrl;

	//subscription model
	private String subscriptionEndDate;
	private Integer subscriptionId;
	
	private Integer rptOrHtml;

	private Integer whatsAppTemplateMasterId;
	private String whatsAppTemplateName;

	
	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSeriesName() {
		return seriesName;
	}

	public void setSeriesName(String seriesName) {
		this.seriesName = seriesName;
	}

	public String getVaccineName() {
		return vaccineName;
	}

	public void setVaccineName(String vaccineName) {
		this.vaccineName = vaccineName;
	}

	public String getUserDefinedMail() {
		return userDefinedMail;
	}

	public void setUserDefinedMail(String userDefinedMail) {
		this.userDefinedMail = userDefinedMail;
	}

	public String getHtmlTemplateData() {
		return htmlTemplateData;
	}

	public void setHtmlTemplateData(String htmlTemplateData) {
		this.htmlTemplateData = htmlTemplateData;
	}

	public String getPdfGenerationUrl() {
		return pdfGenerationUrl;
	}

	public void setPdfGenerationUrl(String pdfGenerationUrl) {
		this.pdfGenerationUrl = pdfGenerationUrl;
	}

	public String getSmtpHost() {
		return smtpHost;
	}

	public void setSmtpHost(String smtpHost) {
		this.smtpHost = smtpHost;
	}

	public String getSmtpPort() {
		return smtpPort;
	}

	public void setSmtpPort(String smtpPort) {
		this.smtpPort = smtpPort;
	}

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public String getMailPassword() {
		return mailPassword;
	}

	public void setMailPassword(String mailPassword) {
		this.mailPassword = mailPassword;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Integer getReferringDoctorId() {
		return referringDoctorId;
	}

	public void setReferringDoctorId(Integer referringDoctorId) {
		this.referringDoctorId = referringDoctorId;
	}

	public String getEncounterNo() {
		return encounterNo;
	}

	public void setEncounterNo(String encounterNo) {
		this.encounterNo = encounterNo;
	}

	public String getBillNo() {
		return billNo;
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
	}

	public String getRecieptNo() {
		return recieptNo;
	}

	public void setRecieptNo(String recieptNo) {
		this.recieptNo = recieptNo;
	}

	public String getAdmissionNo() {
		return admissionNo;
	}

	public void setAdmissionNo(String admissionNo) {
		this.admissionNo = admissionNo;
	}

	public String getCreditNoteNumber() {
		return creditNoteNumber;
	}

	public void setCreditNoteNumber(String creditNoteNumber) {
		this.creditNoteNumber = creditNoteNumber;
	}

	public String getDebitNoteNumber() {
		return debitNoteNumber;
	}

	public void setDebitNoteNumber(String debitNoteNumber) {
		this.debitNoteNumber = debitNoteNumber;
	}

	public String getRefundNumber() {
		return refundNumber;
	}

	public void setRefundNumber(String refundNumber) {
		this.refundNumber = refundNumber;
	}

	public String getDepositNo() {
		return depositNo;
	}

	public void setDepositNo(String depositNo) {
		this.depositNo = depositNo;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
	}

	public BigDecimal getOutstandingAmount() {
		return outstandingAmount;
	}

	public void setOutstandingAmount(BigDecimal outstandingAmount) {
		this.outstandingAmount = outstandingAmount;
	}

	public String getBedDetails() {
		return bedDetails;
	}

	public void setBedDetails(String bedDetails) {
		this.bedDetails = bedDetails;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public Integer getSmsUtilityId() {
		return smsUtilityId;
	}

	public void setSmsUtilityId(Integer smsUtilityId) {
		this.smsUtilityId = smsUtilityId;
	}

	public Integer getAdmitingDoctorId() {
		return admitingDoctorId;
	}

	public void setAdmitingDoctorId(Integer admitingDoctorId) {
		this.admitingDoctorId = admitingDoctorId;
	}

	public Integer getTreatingDoctorId() {
		return treatingDoctorId;
	}

	public void setTreatingDoctorId(Integer treatingDoctorId) {
		this.treatingDoctorId = treatingDoctorId;
	}

	public Integer getConsultingDoctorId() {
		return consultingDoctorId;
	}

	public void setConsultingDoctorId(Integer consultingDoctorId) {
		this.consultingDoctorId = consultingDoctorId;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getTriggerId() {
		return triggerId;
	}

	public void setTriggerId(Integer triggerId) {
		this.triggerId = triggerId;
	}

	public String getTemplateDescription() {
		return templateDescription;
	}

	public void setTemplateDescription(String templateDescription) {
		this.templateDescription = templateDescription;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public String getTriggerName() {
		return triggerName;
	}

	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getConsultingDoctorMobileNo() {
		return consultingDoctorMobileNo;
	}

	public void setConsultingDoctorMobileNo(String consultingDoctorMobileNo) {
		this.consultingDoctorMobileNo = consultingDoctorMobileNo;
	}

	public String getConsultingDoctorName() {
		return consultingDoctorName;
	}

	public void setConsultingDoctorName(String consultingDoctorName) {
		this.consultingDoctorName = consultingDoctorName;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public BigDecimal getSelfAmount() {
		return selfAmount;
	}

	public void setSelfAmount(BigDecimal selfAmount) {
		this.selfAmount = selfAmount;
	}

	public BigDecimal getCreditAmount() {
		return creditAmount;
	}

	public void setCreditAmount(BigDecimal creditAmount) {
		this.creditAmount = creditAmount;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getDepositId() {
		return depositId;
	}

	public void setDepositId(Integer depositId) {
		this.depositId = depositId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public BigDecimal getBillAmount() {
		return billAmount;
	}

	public void setBillAmount(BigDecimal billAmount) {
		this.billAmount = billAmount;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
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

	public String getPatientMobileNo() {
		return patientMobileNo;
	}

	public void setPatientMobileNo(String patientMobileNo) {
		this.patientMobileNo = patientMobileNo;
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

	public String getSmsTemplateMessage() {
		return smsTemplateMessage;
	}

	public void setSmsTemplateMessage(String smsTemplateMessage) {
		this.smsTemplateMessage = smsTemplateMessage;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getPatientUhidNo() {
		return patientUhidNo;
	}

	public void setPatientUhidNo(String patientUhidNo) {
		this.patientUhidNo = patientUhidNo;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getReferringDoctorMobileNo() {
		return referringDoctorMobileNo;
	}

	public void setReferringDoctorMobileNo(String referringDoctorMobileNo) {
		this.referringDoctorMobileNo = referringDoctorMobileNo;
	}

	public String getTreatingDoctorMobileNo() {
		return treatingDoctorMobileNo;
	}

	public void setTreatingDoctorMobileNo(String treatingDoctorMobileNo) {
		this.treatingDoctorMobileNo = treatingDoctorMobileNo;
	}

	public String getAdmitingDoctorMobileNo() {
		return admitingDoctorMobileNo;
	}

	public void setAdmitingDoctorMobileNo(String admitingDoctorMobileNo) {
		this.admitingDoctorMobileNo = admitingDoctorMobileNo;
	}

	public String getReferringDoctorName() {
		return referringDoctorName;
	}

	public void setReferringDoctorName(String referringDoctorName) {
		this.referringDoctorName = referringDoctorName;
	}

	public String getTreatingDoctorName() {
		return treatingDoctorName;
	}

	public void setTreatingDoctorName(String treatingDoctorName) {
		this.treatingDoctorName = treatingDoctorName;
	}

	public String getAdmitingDoctorName() {
		return admitingDoctorName;
	}

	public void setAdmitingDoctorName(String admitingDoctorName) {
		this.admitingDoctorName = admitingDoctorName;
	}

	public String getCompanyAdmissionPatientCount() {
		return companyAdmissionPatientCount;
	}

	public void setCompanyAdmissionPatientCount(String companyAdmissionPatientCount) {
		this.companyAdmissionPatientCount = companyAdmissionPatientCount;
	}

	public String getSelfAdmissionPatientCount() {
		return selfAdmissionPatientCount;
	}

	public void setSelfAdmissionPatientCount(String selfAdmissionPatientCount) {
		this.selfAdmissionPatientCount = selfAdmissionPatientCount;
	}

	public String getSelfDischargePatientCount() {
		return selfDischargePatientCount;
	}

	public void setSelfDischargePatientCount(String selfDischargePatientCount) {
		this.selfDischargePatientCount = selfDischargePatientCount;
	}

	public String getCompanyDischargePatientCount() {
		return companyDischargePatientCount;
	}

	public void setCompanyDischargePatientCount(String companyDischargePatientCount) {
		this.companyDischargePatientCount = companyDischargePatientCount;
	}

	public String getOpdNewCount() {
		return opdNewCount;
	}

	public void setOpdNewCount(String opdNewCount) {
		this.opdNewCount = opdNewCount;
	}

	public String getOpdFollowUpCount() {
		return opdFollowUpCount;
	}

	public void setOpdFollowUpCount(String opdFollowUpCount) {
		this.opdFollowUpCount = opdFollowUpCount;
	}

	public String getCollectionAmount() {
		return collectionAmount;
	}

	public void setCollectionAmount(String collectionAmount) {
		this.collectionAmount = collectionAmount;
	}

	public String getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getDepositeAmount() {
		return depositeAmount;
	}

	public void setDepositeAmount(String depositeAmount) {
		this.depositeAmount = depositeAmount;
	}

	public String getDeathCount() {
		return deathCount;
	}

	public void setDeathCount(String deathCount) {
		this.deathCount = deathCount;
	}

	public String getTreatingEmail() {
		return treatingEmail;
	}

	public void setTreatingEmail(String treatingEmail) {
		this.treatingEmail = treatingEmail;
	}

	public String getConsultingEmail() {
		return consultingEmail;
	}

	public void setConsultingEmail(String consultingEmail) {
		this.consultingEmail = consultingEmail;
	}

	public String getAdmitingEmail() {
		return admitingEmail;
	}

	public void setAdmitingEmail(String admitingEmail) {
		this.admitingEmail = admitingEmail;
	}

	public String getReferringEmail() {
		return referringEmail;
	}

	public void setReferringEmail(String referringEmail) {
		this.referringEmail = referringEmail;
	}

	public String getPatientEmail() {
		return patientEmail;
	}

	public void setPatientEmail(String patientEmail) {
		this.patientEmail = patientEmail;
	}

	public String getToEmailId() {
		return toEmailId;
	}

	public void setToEmailId(String toEmailId) {
		this.toEmailId = toEmailId;
	}

	public String getMailSubject() {
		return mailSubject;
	}

	public void setMailSubject(String mailSubject) {
		this.mailSubject = mailSubject;
	}

	public String getRptReport() {
		return rptReport;
	}

	public void setRptReport(String rptReport) {
		this.rptReport = rptReport;
	}

	public Integer getDepositeConsumptionId() {
		return depositeConsumptionId;
	}

	public void setDepositeConsumptionId(Integer depositeConsumptionId) {
		this.depositeConsumptionId = depositeConsumptionId;
	}

	public Integer getRecieptId() {
		return recieptId;
	}

	public void setRecieptId(Integer recieptId) {
		this.recieptId = recieptId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getEncounterTypeId() {
		return encounterTypeId;
	}

	public void setEncounterTypeId(Integer encounterTypeId) {
		this.encounterTypeId = encounterTypeId;
	}

	public String getRptFileName() {
		return rptFileName;
	}

	public void setRptFileName(String rptFileName) {
		this.rptFileName = rptFileName;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

	public String getRisHeader() {
		return risHeader;
	}

	public void setRisHeader(String risHeader) {
		this.risHeader = risHeader;
	}

	public String getRisBody() {
		return risBody;
	}

	public void setRisBody(String risBody) {
		this.risBody = risBody;
	}

	public String getRisFooter() {
		return risFooter;
	}

	public void setRisFooter(String risFooter) {
		this.risFooter = risFooter;
	}

	public Integer getTemplateTransactionId() {
		return templateTransactionId;
	}

	public void setTemplateTransactionId(Integer templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}

	public List<Integer> getLabSampleDtlsIdsList() {
		return labSampleDtlsIdsList;
	}

	public void setLabSampleDtlsIdsList(List<Integer> labSampleDtlsIdsList) {
		this.labSampleDtlsIdsList = labSampleDtlsIdsList;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getApplicationLink() {
		return applicationLink;
	}

	public void setApplicationLink(String applicationLink) {
		this.applicationLink = applicationLink;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeEmail() {
		return employeeEmail;
	}

	public void setEmployeeEmail(String employeeEmail) {
		this.employeeEmail = employeeEmail;
	}

	public Integer getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String getInsuranceEmail() {
		return insuranceEmail;
	}

	public void setInsuranceEmail(String insuranceEmail) {
		this.insuranceEmail = insuranceEmail;
	}

	public String getInsuranceName() {
		return insuranceName;
	}

	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyEmail() {
		return companyEmail;
	}

	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public List<BulkEmailDto> getPatientList() {
		return patientList;
	}

	public void setPatientList(List<BulkEmailDto> patientList) {
		this.patientList = patientList;
	}

	public String getPatientWhatsAppNo() {
		return patientWhatsAppNo;
	}

	public void setPatientWhatsAppNo(String patientWhatsAppNo) {
		this.patientWhatsAppNo = patientWhatsAppNo;
	}

	public String getCompanyMobileNo() {
		return companyMobileNo;
	}

	public void setCompanyMobileNo(String companyMobileNo) {
		this.companyMobileNo = companyMobileNo;
	}

	public String getInsuranceMobileNo() {
		return insuranceMobileNo;
	}

	public void setInsuranceMobileNo(String insuranceMobileNo) {
		this.insuranceMobileNo = insuranceMobileNo;
	}

	public String getEmployeeMobileNo() {
		return employeeMobileNo;
	}

	public void setEmployeeMobileNo(String employeeMobileNo) {
		this.employeeMobileNo = employeeMobileNo;
	}

	public String getAdmitingDrMobileNo() {
		return admitingDrMobileNo;
	}

	public void setAdmitingDrMobileNo(String admitingDrMobileNo) {
		this.admitingDrMobileNo = admitingDrMobileNo;
	}

	public String getConsultingDrMobileNo() {
		return consultingDrMobileNo;
	}

	public void setConsultingDrMobileNo(String consultingDrMobileNo) {
		this.consultingDrMobileNo = consultingDrMobileNo;
	}

	public String getTreatingDrMobileNo() {
		return treatingDrMobileNo;
	}

	public void setTreatingDrMobileNo(String treatingDrMobileNo) {
		this.treatingDrMobileNo = treatingDrMobileNo;
	}

	public Character getIsWhatsappSend() {
		return isWhatsappSend;
	}

	public void setIsWhatsappSend(Character isWhatsappSend) {
		this.isWhatsappSend = isWhatsappSend;
	}

	public String getToMobileNumber() {
		return toMobileNumber;
	}

	public void setToMobileNumber(String toMobileNumber) {
		this.toMobileNumber = toMobileNumber;
	}

	public String getUnitFolder() {
		return unitFolder;
	}

	public void setUnitFolder(String unitFolder) {
		this.unitFolder = unitFolder;
	}

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getPatientIdList() {
		return patientIdList;
	}

	public void setPatientIdList(String patientIdList) {
		this.patientIdList = patientIdList;
	}

	public String getReportString() {
		return reportString;
	}

	public void setReportString(String reportString) {
		this.reportString = reportString;
	}

	public String getCyclicOrderDate() {
		return cyclicOrderDate;
	}

	public void setCyclicOrderDate(String cyclicOrderDate) {
		this.cyclicOrderDate = cyclicOrderDate;
	}

	public String getLabReportUrl() {
		return labReportUrl;
	}

	public void setLabReportUrl(String labReportUrl) {
		this.labReportUrl = labReportUrl;
	}

	public String getSrfId() {
		return srfId;
	}

	public void setSrfId(String srfId) {
		this.srfId = srfId;
	}

	public Integer getConsentTransactionId() {
		return consentTransactionId;
	}

	public void setConsentTransactionId(Integer consentTransactionId) {
		this.consentTransactionId = consentTransactionId;
	}

	public String getConsentMasterName() {
		return consentMasterName;
	}

	public void setConsentMasterName(String consentMasterName) {
		this.consentMasterName = consentMasterName;
	}

	public String getConsentTypeMasterName() {
		return consentTypeMasterName;
	}

	public void setConsentTypeMasterName(String consentTypeMasterName) {
		this.consentTypeMasterName = consentTypeMasterName;
	}

	public String getFormTemplateId() {
		return formTemplateId;
	}

	public void setFormTemplateId(String formTemplateId) {
		this.formTemplateId = formTemplateId;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getVisitNumber() {
		return visitNumber;
	}

	public void setVisitNumber(String visitNumber) {
		this.visitNumber = visitNumber;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getProceduceName() {
		return proceduceName;
	}

	public void setProceduceName(String proceduceName) {
		this.proceduceName = proceduceName;
	}

	public Character getIsSMSSend() {
		return isSMSSend;
	}

	public void setIsSMSSend(Character isSMSSend) {
		this.isSMSSend = isSMSSend;
	}

	public String getTriggerType() {
		return triggerType;
	}

	public void setTriggerType(String triggerType) {
		this.triggerType = triggerType;
	}

	public Integer getWhatsAppOption() {
		return whatsAppOption;
	}

	public void setWhatsAppOption(Integer whatsAppOption) {
		this.whatsAppOption = whatsAppOption;
	}

	public String getVideoCallLink() {
		return videoCallLink;
	}

	public void setVideoCallLink(String videoCallLink) {
		this.videoCallLink = videoCallLink;
	}

	public Integer getAppointmentUniqueId() {
		return appointmentUniqueId;
	}

	public void setAppointmentUniqueId(Integer appointmentUniqueId) {
		this.appointmentUniqueId = appointmentUniqueId;
	}

	public String getReportUrl() {
		return reportUrl;
	}

	public void setReportUrl(String reportUrl) {
		this.reportUrl = reportUrl;
	}

	public String getReportMsgUrl() {
		return reportMsgUrl;
	}

	public void setReportMsgUrl(String reportMsgUrl) {
		this.reportMsgUrl = reportMsgUrl;
	}

	public List<Integer> getTemplateTransactionIds() {
		return templateTransactionIds;
	}

	public void setTemplateTransactionIds(List<Integer> templateTransactionIds) {
		this.templateTransactionIds = templateTransactionIds;
	}

	public String getTemplateDesc() {
		return templateDesc;
	}

	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}

	public Integer getSkipSaveMessageQueue() {
		return skipSaveMessageQueue;
	}

	public void setSkipSaveMessageQueue(Integer skipSaveMessageQueue) {
		this.skipSaveMessageQueue = skipSaveMessageQueue;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getWhatsaAppmessagehel() {
		return whatsaAppmessagehel;
	}

	public void setWhatsaAppmessagehel(String whatsaAppmessagehel) {
		this.whatsaAppmessagehel = whatsaAppmessagehel;
	}

	public Character getCriticalReport() {
		return criticalReport;
	}

	public void setCriticalReport(Character criticalReport) {
		this.criticalReport = criticalReport;
	}

	public Double getTotalDepositAmount() {
		return totalDepositAmount;
	}

	public void setTotalDepositAmount(Double totalDepositAmount) {
		this.totalDepositAmount = totalDepositAmount;
	}

	public Double getTotalPendingDuaAmount() {
		return totalPendingDuaAmount;
	}

	public void setTotalPendingDuaAmount(Double totalPendingDuaAmount) {
		this.totalPendingDuaAmount = totalPendingDuaAmount;
	}
	
	private String passNumber;
	
	public String getPassNumber(){
		return passNumber;
	}
	
	public void setPassNumber(String passNumber){
		this.passNumber = passNumber;
	}

	public Integer getCostEstimateId() {
		return costEstimateId;
	}

	public void setCostEstimateId(Integer costEstimateId) {
		this.costEstimateId = costEstimateId;
	}


	public String getCriticalUrl() {
		return criticalUrl;
	}

	public void setCriticalUrl(String criticalUrl) {
		this.criticalUrl = criticalUrl;
	}
	public String getSubscriptionEndDate() {
		return subscriptionEndDate;
	}

	public void setSubscriptionEndDate(String subscriptionEndDate) {
		this.subscriptionEndDate = subscriptionEndDate;
	}


	public Integer getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(Integer subscriptionId) {
		this.subscriptionId = subscriptionId;

	}

	public Integer getRptOrHtml() {
		return rptOrHtml;
	}

	public void setRptOrHtml(Integer rptOrHtml) {
		this.rptOrHtml = rptOrHtml;
	}

	public Integer getWhatsAppTemplateMasterId() {
		return whatsAppTemplateMasterId;
	}

	public void setWhatsAppTemplateMasterId(Integer whatsAppTemplateMasterId) {
		this.whatsAppTemplateMasterId = whatsAppTemplateMasterId;
	}

	public String getWhatsAppTemplateName() {
		return whatsAppTemplateName;
	}

	public void setWhatsAppTemplateName(String whatsAppTemplateName) {
		this.whatsAppTemplateName = whatsAppTemplateName;
	}
}
