package com.param.lis.transaction.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public class LabResultMasterDto {
	private Integer labTestResId;

	private Integer orgId;

	private Integer orgUnitId;

	private Integer testId;

	private Integer deptId;

	private Integer patientId;

	private Integer visitTypeId;

	private Integer visitAdmId;

	private Integer orderDetailsId;

	private String sampleNo;

	private Integer labSampleDtlsId;

	private Date resultEnterDatetime;

	private Integer resultEnterBy;

	private Date resultValidatedDatetime;

	private Integer resultValidatedBy;

	private Date resultAuthorisedDatetime;

	private Integer resultAuthorisedBy;

	private Character resultAuthorisedFlag;

	private Long resultHandoverDatetime;

	private Long resultHandoverBy;

	private String suggetionNotes;

	private String footsNotes;

	private Integer sampleStatusId;

	private Integer createdBy;

	private Long createdDate;

	private Integer updatedBy;

	private Long updatedDate;

	private Integer resultLevel;

	// Dummy Fields

	private Integer reportType;

	private String deptName;

	private String sampleBarcode;

	private String labSampleNo;

	private Integer subDeptId;

	private String visitType;

	private String panelCode;

	private String uhid;

	private String patientDetails;

	private String doctorDetails;

	private String testDesc;

	private String priorityName;

	private String colorCode;

	private Integer testType;

	private Integer patientAgeDays;

	private Integer offset;

	private Integer recordPerPage;

	private BigDecimal patientAgeInYears;

	private String patientGender;

	private String printReport;

	private Character sampleRecollectFlag;

	private Integer sampleRecollectAgainstId;

	private Date orderDateTime;

	private Date sampleCollectionDatetime;

	private Integer panelId;

	private Integer genderId;

	private String wardCode;

	private String bedNumber;

	private String sampleType;

	private String testCode;

	private BigInteger retestCount;

	private String patientMail;
	
	private String doctorMail;
	
	private Integer assingnedFrom;
	
	private String mobileNo;
	
	private Character isReviewApplicable;
	
	private Character proRelease; 
	
	private String imagePath;
	
	private boolean print;
	
	private boolean rePrint;
	
	private Integer performTestId;
	
	private Integer assignUnit;
	
	private boolean statusFlag;
	
	private boolean billSettled;
	
	private MultipartFile file;
	
	private String fileName;
	
	private String pImage;

	private Integer doctorId;
	
	private String subSpecialityName;
	
	private String remark;
	
	private List<Integer> labSampleDtlsIdsList; 
	
	private Boolean triggerReleaseMail;
	
	private String entitlementDesc;
	
	private Integer paymentEntitlementId;
	
	private Date releaseDate;
	
	private String rptResponse;
	
	private Integer payeeId;
	
	private Integer insuranceId;
	
	private Integer companyId;
	
	private String orderNo;
	
	private Integer machineId;
	
	private Character status;
	
	private boolean isMailSend;
	private String internalNotes;
	private String srfId;
	private String icmrId;
	private Boolean sendMailOnOrderCompletion;
    private boolean isOnHoldParameter;
	private Integer orderId;
	private Integer priorityId;
	private boolean isReportRecall;
	
	private Character blockReportPrint;
	
	private Integer sampleRejectReasonId;
	
	private String sampleRejectReason;
	private Boolean selectOnlyOneVisitType;

	private Integer serviceId;
	private String containerName;
	private String userName;
	private Date resultEntryDate;
	private Date resultValidatedDate;
	private Date reportUploadedDate;
    private String sampleReception;
    private String sampleProcessing;
    private String sampleAnalysisAndDetection;
    private String orderDateTimeString;
	private String sampleCollectionDatetimeString;
	private String releaseDateString;
	private String resultEntryDateString;
	private String resultValidatedDateString;
	private String reportUploadedDateString;
	private boolean sampleRecollection;

	private Integer serologyId;
	private Integer labSpecialityId;

	private String billNumber;
	
	private String sampleId;
	
	private Boolean provisional;

	private Integer bucketFlag;
	
	private String amendReason;
	
	private Boolean amend;
	
	private Boolean isDummyService;
	
	private String interfaceResult;
	
	private String interfaceName;
	private String newWhatsAppNo;

	private Character isClinicalInterventionRequired;
	private String interventionEndDate;
	private String interventionRemarks;
	private Integer clinicalInterventionProcedureId;
	private Integer interventionCompletedDoctorId;

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

	private List<LabResultDetailsViewDto> listLabSampleResultDetailsMaster;

	public Integer getLabTestResId() {
		return labTestResId;
	}

	public void setLabTestResId(Integer labTestResId) {
		this.labTestResId = labTestResId;
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

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getVisitAdmId() {
		return visitAdmId;
	}

	public void setVisitAdmId(Integer visitAdmId) {
		this.visitAdmId = visitAdmId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public String getSampleNo() {
		return sampleNo;
	}

	public void setSampleNo(String sampleNo) {
		this.sampleNo = sampleNo;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Date getResultEnterDatetime() {
		return resultEnterDatetime;
	}

	public void setResultEnterDatetime(Date resultEnterDatetime) {
		this.resultEnterDatetime = resultEnterDatetime;
	}

	public Integer getResultEnterBy() {
		return resultEnterBy;
	}

	public void setResultEnterBy(Integer resultEnterBy) {
		this.resultEnterBy = resultEnterBy;
	}

	public Date getResultValidatedDatetime() {
		return resultValidatedDatetime;
	}

	public void setResultValidatedDatetime(Date resultValidatedDatetime) {
		this.resultValidatedDatetime = resultValidatedDatetime;
	}

	public Integer getResultValidatedBy() {
		return resultValidatedBy;
	}

	public void setResultValidatedBy(Integer resultValidatedBy) {
		this.resultValidatedBy = resultValidatedBy;
	}

	public Date getResultAuthorisedDatetime() {
		return resultAuthorisedDatetime;
	}

	public void setResultAuthorisedDatetime(Date resultAuthorisedDatetime) {
		this.resultAuthorisedDatetime = resultAuthorisedDatetime;
	}

	public Integer getResultAuthorisedBy() {
		return resultAuthorisedBy;
	}

	public void setResultAuthorisedBy(Integer resultAuthorisedBy) {
		this.resultAuthorisedBy = resultAuthorisedBy;
	}

	public Character getResultAuthorisedFlag() {
		return resultAuthorisedFlag;
	}

	public void setResultAuthorisedFlag(Character resultAuthorisedFlag) {
		this.resultAuthorisedFlag = (resultAuthorisedFlag == null) ? 'N' : resultAuthorisedFlag;
	}

	public Long getResultHandoverDatetime() {
		return resultHandoverDatetime;
	}

	public void setResultHandoverDatetime(Long resultHandoverDatetime) {
		this.resultHandoverDatetime = resultHandoverDatetime;
	}

	public Long getResultHandoverBy() {
		return resultHandoverBy;
	}

	public void setResultHandoverBy(Long resultHandoverBy) {
		this.resultHandoverBy = resultHandoverBy;
	}

	public String getSuggetionNotes() {
		return suggetionNotes;
	}

	public void setSuggetionNotes(String suggetionNotes) {
		this.suggetionNotes = suggetionNotes;
	}

	public String getFootsNotes() {
		return footsNotes;
	}

	public void setFootsNotes(String footsNotes) {
		this.footsNotes = footsNotes;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
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

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public String getSampleBarcode() {
		return sampleBarcode;
	}

	public void setSampleBarcode(String sampleBarcode) {
		this.sampleBarcode = sampleBarcode;
	}

	public Integer getTestType() {
		return testType;
	}

	public void setTestType(Integer testType) {
		this.testType = testType;
	}

	public Integer getPatientAgeDays() {
		return patientAgeDays;
	}

	public void setPatientAgeDays(Integer patientAgeDays) {
		this.patientAgeDays = patientAgeDays;
	}

	public List<LabResultDetailsViewDto> getListLabSampleResultDetailsMaster() {
		return listLabSampleResultDetailsMaster;
	}

	public void setListLabSampleResultDetailsMaster(List<LabResultDetailsViewDto> listLabSampleResultDetailsMaster) {
		this.listLabSampleResultDetailsMaster = listLabSampleResultDetailsMaster;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getReportType() {
		return reportType;
	}

	public void setReportType(Integer reportType) {
		this.reportType = reportType;
	}

	public BigDecimal getPatientAgeInYears() {
		return patientAgeInYears;
	}

	public void setPatientAgeInYears(BigDecimal patientAgeInYears) {
		this.patientAgeInYears = patientAgeInYears;
	}

	public String getPatientGender() {
		return patientGender;
	}

	public void setPatientGender(String patientGender) {
		this.patientGender = patientGender;
	}

	public String getPrintReport() {
		return printReport;
	}

	public void setPrintReport(String printReport) {
		this.printReport = printReport;
	}

	public Character getSampleRecollectFlag() {
		return sampleRecollectFlag;
	}

	public void setSampleRecollectFlag(Character sampleRecollectFlag) {
		this.sampleRecollectFlag = (sampleRecollectFlag == null) ? 'N' : sampleRecollectFlag;
	}

	public Integer getSampleRecollectAgainstId() {
		return sampleRecollectAgainstId;
	}

	public void setSampleRecollectAgainstId(Integer sampleRecollectAgainstId) {
		this.sampleRecollectAgainstId = sampleRecollectAgainstId;
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

	public Integer getResultLevel() {
		return resultLevel;
	}

	public void setResultLevel(Integer resultLevel) {
		this.resultLevel = resultLevel;
	}

	public Integer getSampleStatusId() {
		return sampleStatusId;
	}

	public void setSampleStatusId(Integer sampleStatusId) {
		this.sampleStatusId = sampleStatusId;
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

	public BigInteger getRetestCount() {
		return retestCount;
	}

	public void setRetestCount(BigInteger retestCount) {
		this.retestCount = retestCount;
	}

	public String getPatientMail() {
		return patientMail;
	}

	public void setPatientMail(String patientMail) {
		this.patientMail = patientMail;
	}

	public String getDoctorMail() {
		return doctorMail;
	}

	public void setDoctorMail(String doctorMail) {
		this.doctorMail = doctorMail;
	}

	public Integer getAssingnedFrom() {
		return assingnedFrom;
	}

	public void setAssingnedFrom(Integer assingnedFrom) {
		this.assingnedFrom = assingnedFrom;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}


	public Character getIsReviewApplicable() {
		return isReviewApplicable;
	}

	public void setIsReviewApplicable(Character isReviewApplicable) {
		this.isReviewApplicable = isReviewApplicable;
	}

	public Character getProRelease() {
		return proRelease;
	}

	public void setProRelease(Character proRelease) {
		this.proRelease = proRelease;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public boolean getPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}

	public boolean getRePrint() {
		return rePrint;
	}

	public void setRePrint(boolean rePrint) {
		this.rePrint = rePrint;
	}

	public Integer getPerformTestId() {
		return performTestId;
	}

	public void setPerformTestId(Integer performTestId) {
		this.performTestId = performTestId;
	}

	public Integer getAssignUnit() {
		return assignUnit;
	}

	public void setAssignUnit(Integer assignUnit) {
		this.assignUnit = assignUnit;
	}

	public boolean getStatusFlag() {
		return statusFlag;
	}

	public void setStatusFlag(boolean statusFlag) {
		this.statusFlag = statusFlag;
	}

	public boolean isBillSettled() {
		return billSettled;
	}

	public void setBillSettled(boolean billSettled) {
		this.billSettled = billSettled;
	}

	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
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

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<Integer> getLabSampleDtlsIdsList() {
		return labSampleDtlsIdsList;
	}

	public void setLabSampleDtlsIdsList(List<Integer> labSampleDtlsIdsList) {
		this.labSampleDtlsIdsList = labSampleDtlsIdsList;
	}

	public Boolean getTriggerReleaseMail() {
		return triggerReleaseMail;
	}

	public void setTriggerReleaseMail(Boolean triggerReleaseMail) {
		this.triggerReleaseMail = triggerReleaseMail;
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

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getRptResponse() {
		return rptResponse;
	}

	public void setRptResponse(String rptResponse) {
		this.rptResponse = rptResponse;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public boolean isMailSend() {
		return isMailSend;
	}

	public void setMailSend(boolean isMailSend) {
		this.isMailSend = isMailSend;
	}

	public Boolean getSendMailOnOrderCompletion() {
		return sendMailOnOrderCompletion;
	}

	public void setSendMailOnOrderCompletion(Boolean sendMailOnOrderCompletion) {
		this.sendMailOnOrderCompletion = sendMailOnOrderCompletion;
	}

	public String getInternalNotes() {
		return internalNotes;
	}

	public void setInternalNotes(String internalNotes) {
		this.internalNotes = internalNotes;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public boolean getIsReportRecall() {
		return isReportRecall;
	}

	public void setIsReportRecall(boolean isReportRecall) {
		this.isReportRecall = isReportRecall;
	}


	public Character getBlockReportPrint() {
		return blockReportPrint;
	}

	public void setBlockReportPrint(Character blockReportPrint) {
		this.blockReportPrint = blockReportPrint;
	}

    public boolean getIsOnHoldParameter() {
        return isOnHoldParameter;
    }

    public void setIsOnHoldParameter(boolean isOnHoldParameter) {
        this.isOnHoldParameter = isOnHoldParameter;
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

	public Boolean getSelectOnlyOneVisitType() {
		return selectOnlyOneVisitType;
	}

	public void setSelectOnlyOneVisitType(Boolean selectOnlyOneVisitType) {
		this.selectOnlyOneVisitType = selectOnlyOneVisitType;
	}
	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getContainerName() {
		return containerName;
	}

	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}

	public Date getResultEntryDate() {
		return resultEntryDate;
	}

	public void setResultEntryDate(Date resultEntryDate) {
		this.resultEntryDate = resultEntryDate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getResultValidatedDate() {
		return resultValidatedDate;
	}

	public void setResultValidatedDate(Date resultValidatedDate) {
		this.resultValidatedDate = resultValidatedDate;
	}

	public Date getReportUploadedDate() {
		return reportUploadedDate;
	}

	public void setReportUploadedDate(Date reportUploadedDate) {
		this.reportUploadedDate = reportUploadedDate;
	}

	public String getSampleReception() {
		return sampleReception;
	}

	public void setSampleReception(String sampleReception) {
		this.sampleReception = sampleReception;
	}

	public String getSampleProcessing() {
		return sampleProcessing;
	}

	public void setSampleProcessing(String sampleProcessing) {
		this.sampleProcessing = sampleProcessing;
	}

	public String getSampleAnalysisAndDetection() {
		return sampleAnalysisAndDetection;
	}

	public void setSampleAnalysisAndDetection(String sampleAnalysisAndDetection) {
		this.sampleAnalysisAndDetection = sampleAnalysisAndDetection;
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

	public String getReleaseDateString() {
		return releaseDateString;
	}

	public void setReleaseDateString(String releaseDateString) {
		this.releaseDateString = releaseDateString;
	}

	public String getResultEntryDateString() {
		return resultEntryDateString;
	}

	public void setResultEntryDateString(String resultEntryDateString) {
		this.resultEntryDateString = resultEntryDateString;
	}

	public String getResultValidatedDateString() {
		return resultValidatedDateString;
	}

	public void setResultValidatedDateString(String resultValidatedDateString) {
		this.resultValidatedDateString = resultValidatedDateString;
	}

	public String getReportUploadedDateString() {
		return reportUploadedDateString;
	}

	public void setReportUploadedDateString(String reportUploadedDateString) {
		this.reportUploadedDateString = reportUploadedDateString;
	}

	public boolean getSampleRecollection() {
		return sampleRecollection;
	}

	public void setSampleRecollection(boolean sampleRecollection) {
		this.sampleRecollection = sampleRecollection;
	}


	public Integer getSerologyId() {
		return serologyId;
	}

	public void setSerologyId(Integer serologyId) {
		this.serologyId = serologyId;
	}

	public Integer getLabSpecialityId() {
		return labSpecialityId;
	}

	public void setLabSpecialityId(Integer labSpecialityId) {
		this.labSpecialityId = labSpecialityId;
	}


	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getSampleId() {
		return sampleId;
	}

	public void setSampleId(String sampleId) {
		this.sampleId = sampleId;
	}


	public Boolean getProvisional() {
		return provisional;
	}

	public void setProvisional(Boolean provisional) {
		this.provisional = provisional;
	}
	public Integer getBucketFlag() {
		return bucketFlag;
	}

	public void setBucketFlag(Integer bucketFlag) {
		this.bucketFlag = bucketFlag;

	}

	public String getAmendReason() {
		return amendReason;
	}

	public void setAmendReason(String amendReason) {
		this.amendReason = amendReason;
	}

	public Boolean getAmend() {
		return amend;
	}

	public void setAmend(Boolean amend) {
		this.amend = amend;
	}

	public Boolean getIsDummyService() {
		return isDummyService;
	}

	public void setIsDummyService(Boolean isDummyService) {
		this.isDummyService = isDummyService;
	}


	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getInterfaceResult() {
		return interfaceResult;
	}

	public void setInterfaceResult(String interfaceResult) {
		this.interfaceResult = interfaceResult;
	}

	public String getNewWhatsAppNo() {
		return newWhatsAppNo;
	}

	public void setNewWhatsAppNo(String newWhatsAppNo) {
		this.newWhatsAppNo = newWhatsAppNo;
	}

	public Character getIsClinicalInterventionRequired() {
		return isClinicalInterventionRequired;
	}

	public void setIsClinicalInterventionRequired(Character isClinicalInterventionRequired) {
		this.isClinicalInterventionRequired = isClinicalInterventionRequired;
	}

	public String getInterventionEndDate() {
		return interventionEndDate;
	}

	public void setInterventionEndDate(String interventionEndDate) {
		this.interventionEndDate = interventionEndDate;
	}

	public String getInterventionRemarks() {
		return interventionRemarks;
	}

	public void setInterventionRemarks(String interventionRemarks) {
		this.interventionRemarks = interventionRemarks;
	}

	public Integer getClinicalInterventionProcedureId() {
		return clinicalInterventionProcedureId;
	}

	public void setClinicalInterventionProcedureId(Integer clinicalInterventionProcedureId) {
		this.clinicalInterventionProcedureId = clinicalInterventionProcedureId;
	}

	public Integer getInterventionCompletedDoctorId() {
		return interventionCompletedDoctorId;
	}

	public void setInterventionCompletedDoctorId(Integer interventionCompletedDoctorId) {
		this.interventionCompletedDoctorId = interventionCompletedDoctorId;
	}
}
