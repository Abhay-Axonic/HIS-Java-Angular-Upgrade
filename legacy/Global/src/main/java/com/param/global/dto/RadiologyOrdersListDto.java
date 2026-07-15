package com.param.global.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.param.global.dto.sync.DoctorListDummy;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class RadiologyOrdersListDto {
	private Integer patientId;
	private String uhidNumber;
	private Integer prefixId;
	private String prefixCode;
	private String patientName;
	private Integer genderId;
	private String genderCode;
	private String dob;
	private String ageString;
	private Integer encounterId;
	private Integer modalityId;
	private String modalityDesc;
	private Integer doctorId;
	private String doctorName;
	private Integer specialityId;
	private String specialityName;
	private String serviceName;
	private String orderNo;
	private String orderDate;
	private Integer organizationId;
	private Integer unitId;
	private String fromDate;
	private String toDate;
	private List<Integer> visitList;
	private List<Integer> modalityList;
	private List<Integer> deptList;
	private List<Integer> serviceList;
	private Integer serviceId;
	private Integer priorityId;
	private String serviceCode;
	private String priorityName;
	private Integer orderDetailsId;
	private Integer orderId;
	private String currentDate;
	private Integer updatedBy;
	private Character isAppointableRadiology;
	private Integer radiologySpecialityId;
	private Integer hemodialysisSpecialityId;
	private Integer admissionId;
	private Integer subSpecialityId;
	private String templateObject;
	private Integer templateTransactionId;
	private Integer templateFormatId;
	private Integer admissionIdFrAppointment;
	private Integer serviceRendered;
	private Character status;
	private Character orderStatus;
	private String accessionNumber;
    private String  title;
    private Character isProvisionalRelease;
	private Character isFinalRelease;
	private String bedNumber;
	private String deptName;
	private String mobileNumber;
	private String patientMail;
	private Integer visitTypeId;
	private Character isPACSRequired;
	private String identificationNumber;
	private String visitName;
	private String patientCategory;
	private String visitNumber;
	private String countryCode;
	private Integer fromBedId;
	private Integer toBedId;
	private String toBedNumber;
	private String appointmentTime;
	private Integer fromWardId;
	private Integer fromRoomId;
	private Integer toWardId;
	private Integer toRoomId;
	private Integer fromBedCategoryId;
	private Integer fromBillingBedCategoryId;
	private String updatedDate;
	private Integer slotId;
	private String appointmentDate;
	private Character isReviewApplicable;
	private String admissionNumber;
	private String doa;
	private String pdd;
	private Integer encounterTypeId;
	private Character isCritical;
	private Character isVip;
	private Integer  orderDetailsIdForAppoitment;
	private Integer  userId;
	private Integer  reasonId;
	private Character isPreOrPostBilling;
    private Character isCancel;
    private Integer templateTypeId;
    private String location;
    private Integer ordDoctorId;
    private Integer noOfRecordsPerPage;
  	private Integer  offset;
	private Integer payeeId;
  	private String wardName;
	private Integer  admissonId;
	private String  prefixDesc;
	private String  imagePath;
	private List<Integer> dialysisChairList;
	private Integer labSampleDetailsId;
	private String templateContent;
	private String referralName;
	private String header;
	private String footer;
	private String signtureImg;
	private String latterHeadImg;
	private String email;
 	private Integer isForMail;
 	private String  labSampleNo;
 	private String visitNo;
	private Integer appointmentId;
	private Character isTempFinalRelease;
	private Character isResultEntry;
	private String templateDesc;
	private Boolean printReport;
    private Integer templateDoctorId;
    private Integer printWithHeader;
	private Boolean billSettled;
	private Integer insuranceId;

	private Integer redoCount;
	private String newWhatsAppNo;

//	private String redoReason;

	@JsonProperty("redoReason")
	private String redoReason;

	private List<DoctorListDummy> docList;

	private Integer companyId;
	private String fileName;
	
	private String pImage;
	
	private Boolean finalRelease;
	
	private String mailFooter;
	
	private String entitlementDesc;
	private String ordRemarks;
	
	private Integer paymentEntitlementId;
	
	private List<String> sampleIds;
	
	private String billNo;
	
	private String companyDesc;
	  
	private String comAddress;
	  
	private String registerDate;
	
	private String unitName; 
	private String orderDateTime;
	private String footerImg;
	private String orderDtTime;
    private String headerWithoutImg;
    private String labHeaderWithoutImg;
    private String reportEntryByName;
	private String reportvalidateByName;
	private String reportEntryDate;
	private String reportValidationDate;
	private String reportReleaseDate;
	private String sampleType;
	private boolean isReportRecall;
	private String templateDoctorName;
	private BigDecimal serviceAmt;  
	private Character isRecall;
	private Boolean isHtmlPrint;
	private String orderSchDate;
	private String serviceStandardName;
	private String statusName;
	private String createdDate;
	private Integer batchId;
	private Integer createdBy;
	private List<Integer> templateTransactionIds;
	private String radiologyReportUrl;
	private String prnCount;
	private String wristBandPrnSize;
	private String orderSchDateString;
	private String orderDateString;
	private String orderDateTimeString;
	private String testType;
	private Integer billingMasterId;
	private String testDesc;
	private Integer referralId;
	private BigDecimal discount;
	private BigDecimal concession;
	private BigDecimal ordTotalAmount;
	private Integer tariffId;
	private Integer discountOwnership;
	private Integer patientTypeId;
	private Character isRefDoctorShare;
	private Character isDoctorRequired;
	private Integer billingBedCategoryId;
	private Timestamp orderDateLong;
	private Integer doctorShareId;
	private Integer creditNoteId;
	private String appointmentTime1;
	private boolean isDummyService;
	private Integer medallServiceId;
	private Boolean provisional;
	private Boolean bonusBatchFlag;
	private String grnId;
	private Boolean barcodeScreen;

	private Character isSkipRISResultValidation;

	private Character skipRISReportEntry;

	private Character isAmend;
	
	private Character isMriModality;
	
	private Character isCTModality;
	
	private String htmlContent;
	private String headerPdf;
	private String letterHeadImagePdf;

	private Integer reportEntryBy;
	private Integer validatedBy;

	private Character blockReportPrint;
	private Integer templateId;

	private Character isFormFApplicable;

	private String letterHeadImageTariff;
	private Boolean fromLisTemplate;


	public Boolean getBonusBatchFlag() {
		return bonusBatchFlag;
	}
	public void setBonusBatchFlag(Boolean bonusBatchFlag) {
		this.bonusBatchFlag = bonusBatchFlag;
	}
	public String getGrnId() {
		return grnId;
	}
	public void setGrnId(String grnId) {
		this.grnId = grnId;
	}
	
	public Integer getTemplateDoctorId() {
		return templateDoctorId;
	}
	public void setTemplateDoctorId(Integer templateDoctorId) {
		this.templateDoctorId = templateDoctorId;
	}
	
	public Boolean getPrintReport() {
		return printReport;
	}

	public void setPrintReport(Boolean printReport) {
		this.printReport = printReport;
	}

	public String getTemplateDesc() {
		return templateDesc;
	}

	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}

	public Character getIsResultEntry() {
		return isResultEntry;
	}

	public void setIsResultEntry(Character isResultEntry) {
		this.isResultEntry = isResultEntry;
	}

	public Character getIsTempFinalRelease() {
		return isTempFinalRelease;
	}

	public void setIsTempFinalRelease(Character isTempFinalRelease) {
		this.isTempFinalRelease = isTempFinalRelease;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getEncounterTypeId() {
		return encounterTypeId;
	}

	public void setEncounterTypeId(Integer encounterTypeId) {
		this.encounterTypeId = encounterTypeId;
	}

	public Character getIsCritical() {
		return isCritical;
	}

	public void setIsCritical(Character isCritical) {
		this.isCritical = isCritical;
	}

	public Character getIsVip() {
		return isVip;
	}

	public void setIsVip(Character isVip) {
		this.isVip = isVip;
	}

	public Integer getOrderDetailsIdForAppoitment() {
		return orderDetailsIdForAppoitment;
	}

	public void setOrderDetailsIdForAppoitment(Integer orderDetailsIdForAppoitment) {
		this.orderDetailsIdForAppoitment = orderDetailsIdForAppoitment;
	}

	public Integer getSubSpecialityId() {
		return subSpecialityId;
	}

	public void setSubSpecialityId(Integer subSpecialityId) {
		this.subSpecialityId = subSpecialityId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getRadiologySpecialityId() {
		return radiologySpecialityId;
	}

	public void setRadiologySpecialityId(Integer radiologySpecialityId) {
		this.radiologySpecialityId = radiologySpecialityId;
	}

	public Character getIsAppointableRadiology() {
		return isAppointableRadiology;
	}

	public void setIsAppointableRadiology(Character isAppointableRadiology) {
		this.isAppointableRadiology = isAppointableRadiology;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public String getPriorityName() {
		return priorityName;
	}

	public void setPriorityName(String priorityName) {
		this.priorityName = priorityName;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
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

	public List<Integer> getVisitList() {
		return visitList;
	}

	public void setVisitList(List<Integer> visitList) {
		this.visitList = visitList;
	}

	public List<Integer> getModalityList() {
		return modalityList;
	}

	public void setModalityList(List<Integer> modalityList) {
		this.modalityList = modalityList;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public Integer getPrefixId() {
		return prefixId;
	}

	public void setPrefixId(Integer prefixId) {
		this.prefixId = prefixId;
	}

	public String getPrefixCode() {
		return prefixCode;
	}

	public void setPrefixCode(String prefixCode) {
		this.prefixCode = prefixCode;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Integer getGenderId() {
		return genderId;
	}

	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}

	public String getGenderCode() {
		return genderCode;
	}

	public void setGenderCode(String genderCode) {
		this.genderCode = genderCode;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAgeString() {
		return ageString;
	}

	public void setAgeString(String ageString) {
		this.ageString = ageString;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getModalityId() {
		return modalityId;
	}

	public void setModalityId(Integer modalityId) {
		this.modalityId = modalityId;
	}

	public String getModalityDesc() {
		return modalityDesc;
	}

	public void setModalityDesc(String modalityDesc) {
		this.modalityDesc = modalityDesc;
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

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Integer getTemplateTransactionId() {
		return templateTransactionId;
	}

	public void setTemplateTransactionId(Integer templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}

	public String getTemplateObject() {
		return templateObject;
	}

	public void setTemplateObject(String templateObject) {
		this.templateObject = templateObject;
	}

	public Integer getTemplateFormatId() {
		return templateFormatId;
	}

	public void setTemplateFormatId(Integer templateFormatId) {
		this.templateFormatId = templateFormatId;
	}

	public Integer getAdmissionIdFrAppointment() {
		return admissionIdFrAppointment;
	}

	public void setAdmissionIdFrAppointment(Integer admissionIdFrAppointment) {
		this.admissionIdFrAppointment = admissionIdFrAppointment;
	}

	public Integer getServiceRendered() {
		return serviceRendered;
	}

	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<Integer> getDeptList() {
		return deptList;
	}

	public void setDeptList(List<Integer> deptList) {
		this.deptList = deptList;
	}

	public List<Integer> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Integer> serviceList) {
		this.serviceList = serviceList;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getPatientMail() {
		return patientMail;
	}

	public void setPatientMail(String patientMail) {
		this.patientMail = patientMail;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Character getIsPACSRequired() {
		return isPACSRequired;
	}

	public void setIsPACSRequired(Character isPACSRequired) {
		this.isPACSRequired = isPACSRequired;
	}

	public String getIdentificationNumber() {
		return identificationNumber;
	}

	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}

	public String getVisitName() {
		return visitName;
	}

	public void setVisitName(String visitName) {
		this.visitName = visitName;
	}

	public String getPatientCategory() {
		return patientCategory;
	}

	public void setPatientCategory(String patientCategory) {
		this.patientCategory = patientCategory;
	}

	public String getVisitNumber() {
		return visitNumber;
	}

	public void setVisitNumber(String visitNumber) {
		this.visitNumber = visitNumber;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public Integer getHemodialysisSpecialityId() {
		return hemodialysisSpecialityId;
	}

	public void setHemodialysisSpecialityId(Integer hemodialysisSpecialityId) {
		this.hemodialysisSpecialityId = hemodialysisSpecialityId;
	}

	public Integer getFromBedId() {
		return fromBedId;
	}

	public void setFromBedId(Integer fromBedId) {
		this.fromBedId = fromBedId;
	}

	public Integer getToBedId() {
		return toBedId;
	}

	public void setToBedId(Integer toBedId) {
		this.toBedId = toBedId;
	}

	public String getToBedNumber() {
		return toBedNumber;
	}

	public void setToBedNumber(String toBedNumber) {
		this.toBedNumber = toBedNumber;
	}

	public String getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public Integer getFromWardId() {
		return fromWardId;
	}

	public void setFromWardId(Integer fromWardId) {
		this.fromWardId = fromWardId;
	}

	public Integer getFromRoomId() {
		return fromRoomId;
	}

	public void setFromRoomId(Integer fromRoomId) {
		this.fromRoomId = fromRoomId;
	}

	public Integer getToWardId() {
		return toWardId;
	}

	public void setToWardId(Integer toWardId) {
		this.toWardId = toWardId;
	}

	public Integer getToRoomId() {
		return toRoomId;
	}

	public void setToRoomId(Integer toRoomId) {
		this.toRoomId = toRoomId;
	}

	public Integer getFromBedCategoryId() {
		return fromBedCategoryId;
	}

	public void setFromBedCategoryId(Integer fromBedCategoryId) {
		this.fromBedCategoryId = fromBedCategoryId;
	}

	public Integer getFromBillingBedCategoryId() {
		return fromBillingBedCategoryId;
	}

	public void setFromBillingBedCategoryId(Integer fromBillingBedCategoryId) {
		this.fromBillingBedCategoryId = fromBillingBedCategoryId;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public Character getIsReviewApplicable() {
		return isReviewApplicable;
	}

	public void setIsReviewApplicable(Character isReviewApplicable) {
		this.isReviewApplicable = isReviewApplicable;
	}

	public String getAdmissionNumber() {
		return admissionNumber;
	}

	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
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



	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Character getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}

	public void setIsPreOrPostBilling(Character isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
	}

	public Character getIsCancel() {
		return isCancel;
	}

	public void setIsCancel(Character isCancel) {
		this.isCancel = isCancel;
	}

	public Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	public Integer getOrdDoctorId() {
		return ordDoctorId;
	}

	public void setOrdDoctorId(Integer ordDoctorId) {
		this.ordDoctorId = ordDoctorId;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public Integer getAdmissonId() {
		return admissonId;
	}

	public void setAdmissonId(Integer admissonId) {
		this.admissonId = admissonId;
	}

	public String getPrefixDesc() {
		return prefixDesc;
	}

	public void setPrefixDesc(String prefixDesc) {
		this.prefixDesc = prefixDesc;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public List<Integer> getDialysisChairList() {
		return dialysisChairList;
	}

	public void setDialysisChairList(List<Integer> dialysisChairList) {
		this.dialysisChairList = dialysisChairList;
	}

	public Integer getLabSampleDetailsId() {
		return labSampleDetailsId;
	}

	public void setLabSampleDetailsId(Integer labSampleDetailsId) {
		this.labSampleDetailsId = labSampleDetailsId;
	}
	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public String getReferralName() {
		return referralName;
	}

	public void setReferralName(String referralName) {
		this.referralName = referralName;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getFooter() {
		return footer;
	}

	public void setFooter(String footer) {
		this.footer = footer;
	}

	public String getSigntureImg() {
		return signtureImg;
	}

	public void setSigntureImg(String signtureImg) {
		this.signtureImg = signtureImg;
	}

	public String getLatterHeadImg() {
		return latterHeadImg;
	}

	public void setLatterHeadImg(String latterHeadImg) {
		this.latterHeadImg = latterHeadImg;
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

	public String getLabSampleNo() {
		return labSampleNo;
	}

	public void setLabSampleNo(String labSampleNo) {
		this.labSampleNo = labSampleNo;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	public Integer getPrintWithHeader() {
		return printWithHeader;
	}
	public void setPrintWithHeader(Integer printWithHeader) {
		this.printWithHeader = printWithHeader;
	}
	public Boolean getBillSettled() {
		return billSettled;
	}
	public void setBillSettled(Boolean billSettled) {
		this.billSettled = billSettled;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getpImage() {
		return pImage;
	}
	public void setpImage(String pImage) {
		this.pImage = pImage;
	}
	public Boolean getFinalRelease() {
		return finalRelease;
	}
	public void setFinalRelease(Boolean finalRelease) {
		this.finalRelease = finalRelease;
	}
	public String getMailFooter() {
		return mailFooter;
	}
	public void setMailFooter(String mailFooter) {
		this.mailFooter = mailFooter;
	}
	public String getEntitlementDesc() {
		return entitlementDesc;
	}
	public void setEntitlementDesc(String entitlementDesc) {
		this.entitlementDesc = entitlementDesc;
	}
	public String getOrdRemarks() {
		return ordRemarks;
	}
	public void setOrdRemarks(String ordRemarks) {
		this.ordRemarks = ordRemarks;
	}
	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}
	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}
	public List<String> getSampleIds() {
		return sampleIds;
	}
	public void setSampleIds(List<String> sampleIds) {
		this.sampleIds = sampleIds;
	}
	public String getBillNo() {
		return billNo;
	}
	public void setBillNo(String billNo) {
		this.billNo = billNo;
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
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getOrderDateTime() {
		return orderDateTime;
	}
	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	public String getFooterImg() {
		return footerImg;
	}
	public void setFooterImg(String footerImg) {
		this.footerImg = footerImg;
	}
	public String getOrderDtTime() {
		return orderDtTime;
	}
	public void setOrderDtTime(String orderDtTime) {
		this.orderDtTime = orderDtTime;
	}
	public String getHeaderWithoutImg() {
		return headerWithoutImg;
	}
	public void setHeaderWithoutImg(String headerWithoutImg) {
		this.headerWithoutImg = headerWithoutImg;
	}
	public String getLabHeaderWithoutImg() {
		return labHeaderWithoutImg;
	}
	public void setLabHeaderWithoutImg(String labHeaderWithoutImg) {
		this.labHeaderWithoutImg = labHeaderWithoutImg;
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
	public String getSampleType() {
		return sampleType;
	}
	public void setSampleType(String sampleType) {
		this.sampleType = sampleType;
	}
	public boolean getIsReportRecall() {
		return isReportRecall;
	}

	public void setIsReportRecall(boolean isReportRecall) {
		this.isReportRecall = isReportRecall;
	}
	public String getTemplateDoctorName() {
		return templateDoctorName;
	}
	public void setTemplateDoctorName(String templateDoctorName) {
		this.templateDoctorName = templateDoctorName;
	}
	public BigDecimal getServiceAmt() {
		return serviceAmt;
	}
	public void setServiceAmt(BigDecimal serviceAmt) {
		this.serviceAmt = serviceAmt;
	}
	public Character getIsRecall() {
		return isRecall;
	}
	public void setIsRecall(Character isRecall) {
		this.isRecall = isRecall;
	}
	public Boolean getIsHtmlPrint() {
		return isHtmlPrint;
	}
	public void setIsHtmlPrint(Boolean isHtmlPrint) {
		this.isHtmlPrint = isHtmlPrint;
	}
	public String getOrderSchDate() {
		return orderSchDate;
	}
	public void setOrderSchDate(String orderSchDate) {
		this.orderSchDate = orderSchDate;
	}
	public String getServiceStandardName() {
		return serviceStandardName;
	}
	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Character getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Character orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public List<Integer> getTemplateTransactionIds() {
		return templateTransactionIds;
	}
	public void setTemplateTransactionIds(List<Integer> templateTransactionIds) {
		this.templateTransactionIds = templateTransactionIds;
	}
	public Integer getInsuranceId() {
		return insuranceId;
	}
	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}

	public Integer getRedoCount() {
		return redoCount;
	}

	public void setRedoCount(Integer redoCount) {
		this.redoCount = redoCount;
	}

	public String getRedoReason() {
		return redoReason;
	}

	public void setRedoReason(String redoReason) {
		this.redoReason = redoReason;
	}

	public List<DoctorListDummy> getDocList() {
		return docList;
	}

	public void setDocList(List<DoctorListDummy> docList) {
		this.docList = docList;
	}

	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public String getRadiologyReportUrl() {
		return radiologyReportUrl;
	}
	public void setRadiologyReportUrl(String radiologyReportUrl) {
		this.radiologyReportUrl = radiologyReportUrl;
	}
	public String getPrnCount() {
		return prnCount;
	}
	public void setPrnCount(String prnCount) {
		this.prnCount = prnCount;
	}
	public String getWristBandPrnSize() {
		return wristBandPrnSize;
	}
	public void setWristBandPrnSize(String wristBandPrnSize) {
		this.wristBandPrnSize = wristBandPrnSize;
	}
	public String getOrderSchDateString() {
		return orderSchDateString;
	}
	public void setOrderSchDateString(String orderSchDateString) {
		this.orderSchDateString = orderSchDateString;
	}
	public String getOrderDateString() {
		return orderDateString;
	}
	public void setOrderDateString(String orderDateString) {
		this.orderDateString = orderDateString;
	}
	public String getOrderDateTimeString() {
		return orderDateTimeString;
	}
	public void setOrderDateTimeString(String orderDateTimeString) {
		this.orderDateTimeString = orderDateTimeString;
	}
	public String getTestType() {
		return testType;
	}
	public void setTestType(String testType) {
		this.testType = testType;
	}
	public Integer getBillingMasterId() {
		return billingMasterId;
	}
	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}
	public String getTestDesc() {
		return testDesc;
	}
	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}
	public Integer getReferralId() {
		return referralId;
	}
	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public BigDecimal getConcession() {
		return concession;
	}
	public void setConcession(BigDecimal concession) {
		this.concession = concession;
	}
	public BigDecimal getOrdTotalAmount() {
		return ordTotalAmount;
	}
	public void setOrdTotalAmount(BigDecimal ordTotalAmount) {
		this.ordTotalAmount = ordTotalAmount;
	}
	public Integer getTariffId() {
		return tariffId;
	}
	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}
	public Integer getDiscountOwnership() {
		return discountOwnership;
	}
	public void setDiscountOwnership(Integer discountOwnership) {
		this.discountOwnership = discountOwnership;
	}
	public Integer getPatientTypeId() {
		return patientTypeId;
	}
	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}
	public Character getIsRefDoctorShare() {
		return isRefDoctorShare;
	}
	public void setIsRefDoctorShare(Character isRefDoctorShare) {
		this.isRefDoctorShare = isRefDoctorShare;
	}
	public Character getIsDoctorRequired() {
		return isDoctorRequired;
	}
	public void setIsDoctorRequired(Character isDoctorRequired) {
		this.isDoctorRequired = isDoctorRequired;
	}
	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}
	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}
	public Timestamp getOrderDateLong() {
		return orderDateLong;
	}
	public void setOrderDateLong(Timestamp orderDateLong) {
		this.orderDateLong = orderDateLong;
	}
	public Integer getDoctorShareId() {
		return doctorShareId;
	}
	public void setDoctorShareId(Integer doctorShareId) {
		this.doctorShareId = doctorShareId;
	}
	public Integer getCreditNoteId() {
		return creditNoteId;
	}
	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}
	public String getAppointmentTime1() {
		return appointmentTime1;
	}
	public void setAppointmentTime1(String appointmentTime1) {
		this.appointmentTime1 = appointmentTime1;
	}
	public boolean getIsDummyService() {
		return isDummyService;
	}
	public void setDummyService(boolean isDummyService) {
		this.isDummyService = isDummyService;
	}
	public Integer getMedallServiceId() {
		return medallServiceId;
	}
	public void setMedallServiceId(Integer medallServiceId) {
		this.medallServiceId = medallServiceId;
	}
	public Boolean getProvisional() {
		return provisional;
	}
	public void setProvisional(Boolean provisional) {
		this.provisional = provisional;
	}
	public void setReportRecall(boolean isReportRecall) {
		this.isReportRecall = isReportRecall;
	}
	public Boolean getBarcodeScreen() {
		return barcodeScreen;
	}
	public void setBarcodeScreen(Boolean barcodeScreen) {
		this.barcodeScreen = barcodeScreen;
	}

	public Character getIsSkipRISResultValidation() {
		return isSkipRISResultValidation;
	}

	public void setIsSkipRISResultValidation(Character isSkipRISResultValidation) {
		this.isSkipRISResultValidation = isSkipRISResultValidation;
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
	public Character getIsMriModality() {
		return isMriModality;
	}
	public void setIsMriModality(Character isMriModality) {
		this.isMriModality = isMriModality;
	}
	public Character getIsCTModality() {
		return isCTModality;
	}
	public void setIsCTModality(Character isCTModality) {
		this.isCTModality = isCTModality;
	}
	public String getHtmlContent() {
		return htmlContent;
	}
	public void setHtmlContent(String htmlContent) {
		this.htmlContent = htmlContent;
	}

	public String getHeaderPdf() {
		return headerPdf;
	}

	public void setHeaderPdf(String headerPdf) {
		this.headerPdf = headerPdf;
	}

	public String getLetterHeadImagePdf() {
		return letterHeadImagePdf;
	}

	public void setLetterHeadImagePdf(String letterHeadImagePdf) {
		this.letterHeadImagePdf = letterHeadImagePdf;
	}

	public String getNewWhatsAppNo() {
		return newWhatsAppNo;
	}

	public void setNewWhatsAppNo(String newWhatsAppNo) {
		this.newWhatsAppNo = newWhatsAppNo;
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

	public Character getBlockReportPrint() {
		return blockReportPrint;
	}

	public void setBlockReportPrint(Character blockReportPrint) {
		this.blockReportPrint = blockReportPrint;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Character getIsFormFApplicable() {
		return isFormFApplicable;
	}

	public void setIsFormFApplicable(Character isFormFApplicable) {
		this.isFormFApplicable = isFormFApplicable;
	}

	public String getLetterHeadImageTariff() {
		return letterHeadImageTariff;
	}

	public void setLetterHeadImageTariff(String letterHeadImageTariff) {
		this.letterHeadImageTariff = letterHeadImageTariff;
	}

	public Boolean getFromLisTemplate() {
		return fromLisTemplate;
	}

	public void setFromLisTemplate(Boolean fromLisTemplate) {
		this.fromLisTemplate = fromLisTemplate;
	}
}
