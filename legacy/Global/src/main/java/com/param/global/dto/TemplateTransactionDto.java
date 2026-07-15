package com.param.global.dto;

import java.util.List;

import com.param.global.service.TemplateQuetionaryPrintDto;

public class TemplateTransactionDto {
	private int templateTransactionId;
	private Integer templateId;
	private String title;
	private Integer specialityId;
	private Integer subSpecialityId;
	private String templateObject;
	private Integer admissionId;
	private Integer encounterId;
	private Integer doctorId;
	private Integer patientId;
	private Integer templateTypeId;
	private int diagnosisId;
	private char status;
	private Integer signedBy;
	private String signedDate;
	private String jsonObject;
	private Integer orgId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private int orderDetailsId;
	private String updatedDate;
	private int mIcpcSystemId;
	private int mIcpcId;
	private Character isProvisionalRelease ;
	private Character isFinalRelease ;
	private Character isReportRelease;
	private String mobileNumber;
	private String patientName;
	private String patientMail;
	private String uhidNumber;
	private Integer visitTypeId;
	private String doctorName;
	private Character isQuestionary;
	private String orderDate;
	private String ageString;
	private String genderCode;
	private String referralName;
	private String email;
	private Integer isForMail;
	private Integer templateDoctorId;
	private String userName;
	private String conversationId;
	private String postbackURL;
	
	private Integer tSubSpecimanId;
	private String reportNo;
	private String reviewTime;
	private Integer stainingId;
	private Integer labSampleDtlsId;
	private Integer templateResId;
	private Integer sampleStatusId;
	private Integer templateFormatId;

	private String appointmentDate;
	private String timeSlot;
	private Integer appointmentId;
	private Integer admNoteId;
	private String specialityName;
	private boolean feedbackFormSubmitted;
	private String pdfByte;
	private Integer reportEntryBy;
	private Integer reportvalidateBy;
	private String reportEntryByName;
	private String reportvalidateByName;
	private String templateHtml;
	private String headerPart;
	private String footerPart;
	private String prefixCode;
	private Integer genderId;
	private String latterHeadImg;
	private String signtureImg;
	private String gender;
	private boolean isHtmlPrint;
	private Integer tabularTemplateId;
	private Integer formTemplateId;
	private String formData;
	private String templateUrl;

	private Character skipRISReportEntry;

	private Character isAmend;
	private List<TemplateQuetionaryPrintDto> listPrintJson;

	private List <TemplateTransactionDto> certificateListForCaseSheet;

	private String addedDate;

	private List <String> dates;

	private List<TemplateTransactionDto> lettersListForCaseSheet;
	private String dob;
	private String doa;
	private String pdd;
	private String admissionNumber;

	public List<TemplateTransactionDto> getCertificateListForCaseSheet() {
		return certificateListForCaseSheet;
	}

	public void setCertificateListForCaseSheet(List<TemplateTransactionDto> certificateListForCaseSheet) {
		this.certificateListForCaseSheet = certificateListForCaseSheet;
	}

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public List<TemplateTransactionDto> getLettersListForCaseSheet() {
		return lettersListForCaseSheet;
	}

	public void setLettersListForCaseSheet(List<TemplateTransactionDto> lettersListForCaseSheet) {
		this.lettersListForCaseSheet = lettersListForCaseSheet;
	}

	private String createdDate1;

	public String getCreatedDate1() {
		return createdDate1;
	}

	public void setCreatedDate1(String createdDate1) {
		this.createdDate1 = createdDate1;
	}

	public Integer getTemplateDoctorId() {
		return templateDoctorId;
	}
	public void setTemplateDoctorId(Integer templateDoctorId) {
		this.templateDoctorId = templateDoctorId;
	}
	public int getTemplateTransactionId() {
		return templateTransactionId;
	}
	public void setTemplateTransactionId(int templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}
	public Integer getTemplateId() {
		return templateId;
	}
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}
	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}
	public String getTemplateObject() {
		return templateObject;
	}
	public void setTemplateObject(String templateObject) {
		this.templateObject = templateObject;
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
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public int getDiagnosisId() {
		return diagnosisId;
	}
	public void setDiagnosisId(int diagnosisId) {
		this.diagnosisId = diagnosisId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public Integer getSignedBy() {
		return signedBy;
	}
	public void setSignedBy(Integer signedBy) {
		this.signedBy = signedBy;
	}
	public String getSignedDate() {
		return signedDate;
	}
	public void setSignedDate(String signedDate) {
		this.signedDate = signedDate;
	}
	public String getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
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
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public int getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(int orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	public int getmIcpcSystemId() {
		return mIcpcSystemId;
	}
	public void setmIcpcSystemId(int mIcpcSystemId) {
		this.mIcpcSystemId = mIcpcSystemId;
	}
	public int getmIcpcId() {
		return mIcpcId;
	}
	public void setmIcpcId(int mIcpcId) {
		this.mIcpcId = mIcpcId;
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
	public Character getIsReportRelease() {
		return isReportRelease;
	}
	public void setIsReportRelease(Character isReportRelease) {
		this.isReportRelease = isReportRelease;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getPatientMail() {
		return patientMail;
	}
	public void setPatientMail(String patientMail) {
		this.patientMail = patientMail;
	}
	public String getUhidNumber() {
		return uhidNumber;
	}
	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public Character getIsQuestionary() {
		return isQuestionary;
	}
	public void setIsQuestionary(Character isQuestionary) {
		this.isQuestionary = isQuestionary;
	}
	public String getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}
	public String getAgeString() {
		return ageString;
	}
	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}
	public String getGenderCode() {
		return genderCode;
	}
	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
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
	public Integer getIsForMail() {
		return isForMail;
	}
	public void setIsForMail(Integer isForMail) {
		this.isForMail = isForMail;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getConversationId() {
		return conversationId;
	}
	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	public String getPostbackURL() {
		return postbackURL;
	}
	public void setPostbackURL(String postbackURL) {
		this.postbackURL = postbackURL;
	}
	public Integer getTemplateTypeId() {
		return templateTypeId;
	}
	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}
	public Integer gettSubSpecimanId() {
		return tSubSpecimanId;
	}
	public void settSubSpecimanId(Integer tSubSpecimanId) {
		this.tSubSpecimanId = tSubSpecimanId;
	}
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getReviewTime() {
		return reviewTime;
	}
	public void setReviewTime(String reviewTime) {
		this.reviewTime = reviewTime;
	}
	public Integer getStainingId() {
		return stainingId;
	}
	public void setStainingId(Integer stainingId) {
		this.stainingId = stainingId;
	}
	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}
	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}
	public Integer getTemplateResId() {
		return templateResId;
	}
	public void setTemplateResId(Integer templateResId) {
		this.templateResId = templateResId;
	}
	public Integer getSampleStatusId() {
		return sampleStatusId;
	}
	public void setSampleStatusId(Integer sampleStatusId) {
		this.sampleStatusId = sampleStatusId;
	}
	public String getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Integer getAdmNoteId() {
		return admNoteId;
	}
	public void setAdmNoteId(Integer admNoteId) {
		this.admNoteId = admNoteId;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public boolean isFeedbackFormSubmitted() {
		return feedbackFormSubmitted;
	}
	public void setFeedbackFormSubmitted(boolean feedbackFormSubmitted) {
		this.feedbackFormSubmitted = feedbackFormSubmitted;
	}
	public String getPdfByte() {
		return pdfByte;
	}
	public void setPdfByte(String pdfByte) {
		this.pdfByte = pdfByte;
	}
	public Integer getReportEntryBy() {
		return reportEntryBy;
	}
	public void setReportEntryBy(Integer reportEntryBy) {
		this.reportEntryBy = reportEntryBy;
	}
	public Integer getReportvalidateBy() {
		return reportvalidateBy;
	}
	public void setReportvalidateBy(Integer reportvalidateBy) {
		this.reportvalidateBy = reportvalidateBy;
	}
	public String getReportEntryByName() {
		return reportEntryByName;
	}
	public void setReportEntryByName(String reportEntryByName) {
		this.reportEntryByName = reportEntryByName;
	}
	public String getReportvalidateByName() {
		return reportvalidateByName;
	}
	public void setReportvalidateByName(String reportvalidateByName) {
		this.reportvalidateByName = reportvalidateByName;
	}
	public String getTemplateHtml() {
		return templateHtml;
	}
	public void setTemplateHtml(String templateHtml) {
		this.templateHtml = templateHtml;
	}
	public Integer getTemplateFormatId() {
		return templateFormatId;
	}
	public void setTemplateFormatId(Integer templateFormatId) {
		this.templateFormatId = templateFormatId;
	}
	public String getHeaderPart() {
		return headerPart;
	}
	public void setHeaderPart(String headerPart) {
		this.headerPart = headerPart;
	}
	public String getFooterPart() {
		return footerPart;
	}
	public void setFooterPart(String footerPart) {
		this.footerPart = footerPart;
	}
	public String getPrefixCode() {
		return prefixCode;
	}
	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}
	
	public String getLatterHeadImg() {
		return latterHeadImg;
	}
	public void setLatterHeadImg(String latterHeadImg) {
		this.latterHeadImg = latterHeadImg;
	}
	public Integer getGenderId() {
		return genderId;
	}
	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}
	public String getSigntureImg() {
		return signtureImg;
	}
	public void setSigntureImg(String signtureImg) {
		this.signtureImg = signtureImg;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public boolean getIsHtmlPrint() {
		return isHtmlPrint;
	}
	public void setIsHtmlPrint(boolean isHtmlPrint) {
		this.isHtmlPrint = isHtmlPrint;
	}
	public Integer getTabularTemplateId() {
		return tabularTemplateId;
	}
	public void setTabularTemplateId(Integer tabularTemplateId) {
		this.tabularTemplateId = tabularTemplateId;
	}
	public Integer getFormTemplateId() {
		return formTemplateId;
	}
	public void setFormTemplateId(Integer formTemplateId) {
		this.formTemplateId = formTemplateId;
	}
	public String getFormData() {
		return formData;
	}
	public void setFormData(String formData) {
		this.formData = formData;
	}
	public String getTemplateUrl() {
		return templateUrl;
	}
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}
	public List<TemplateQuetionaryPrintDto> getListPrintJson() {
		return listPrintJson;
	}
	public void setListPrintJson(List<TemplateQuetionaryPrintDto> listPrintJson) {
		this.listPrintJson = listPrintJson;
	}

	public Character getSkipRISReportEntry() {
		return skipRISReportEntry;
	}

	public void setSkipRISReportEntry(Character skipRISReportEntry) {
		this.skipRISReportEntry = skipRISReportEntry;
	}

	public Character getIsAmend() {
		return isAmend;
	}

	public void setIsAmend(Character isAmend) {
		this.isAmend = isAmend;
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

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}
}

