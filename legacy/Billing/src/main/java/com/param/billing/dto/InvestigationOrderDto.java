package com.param.billing.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.param.global.dto.OrderDetailsPackageMapperDto;

public class InvestigationOrderDto {

	private Integer orderDetailsId;

	private Integer orderId;

	private Integer serviceMasterId;
	
	private Integer sampleStatusId;

	private String serviceStandardName;

	private String serviceStandardCode;

	private String specialityName;

	private String createdDate;

	private Integer admissionId;

	private Integer encounterId;

	private Integer labSampleDtlsId;

	private Integer labTestResId;

	private Integer panelId;

	private Integer subDeptId;
	
	private Integer specialityId;
	
	private Integer subSpecialityId;

	private Character isPanel;

	private String statusName;

	private Integer patientId;
	
	private Integer serviceRendered;
	
	private Integer templateTransactionId;
	
	private Integer serviceIsBilled;

	private Character orderStatus;
	
	private Integer appointmentId;
	private Integer medallServiceId;

	private Integer slotId;
	private Integer organizationId;
	private String createdDateBookView;
	private Date createdDateNew;
	private Date sampleGenDatetimeNew;
	private Date orderSchDateNew;
	private Character isPackageService;
	private Character isAutoRender;
	
	private Integer orderSetId;


	private Integer proReqId;
	
	private Integer procedureReqId;
	private Integer packageId;
	private Integer priorityId;
	
	private String orderSchDate;
	
	private String userName;
	
	private String ordRemarks;
	
	private List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList;

	private String templateObject;
	
	private Integer microTemplateStatusId;
	private String sampleGenDatetime;
	private String collectedUserName;
	
	private Character isProcedureService;
	/////
	private String uhidNumber;
	private Integer prefixId;
	private String prefixCode;
	private String patientName;
	private Integer genderId;
	private String genderCode;
	private String dob;
	private String ageString;
	private String doctorName;
	private String serviceName;
	private String orderNo;
	private String orderDate;
	private Integer unitId;
	private Integer serviceId;
	private String serviceCode;
	private Integer templateFormatId;
	private Character status;
    private String  title;
    private Character isProvisionalRelease;
	private Character isFinalRelease;
	private String bedNumber;
	private String deptName;
	private String mobileNumber;
	private String patientMail;
	private Integer visitTypeId;
	private String identificationNumber;
	private String visitName;
	private String patientCategory;
	private String visitNumber;
	private String countryCode;
	private String doa;
	private String pdd;
	private Character isCritical;
	private Character isVip;
    private Integer templateTypeId;
	private String templateContent;
	private String email;
 	private String visitNo;
	private Boolean printReport;
    private Integer printWithHeader;
    private Boolean billSettled;
	private Integer paymentEntitlementId;
	private String billNo;
	private String companyDesc;
	private String registerDate;
	private String unitName; 
    private String reportEntryByName;
	private String reportvalidateByName;
	private Boolean isHtmlPrint;
	private String orderDateTime;
	private String labSampleNo;
	private Character isProcedure;

	private Integer procedureTypeId;
	private String reportEntryDate;
	private String reportValidationDate;
	private String reportReleaseDate;
	private String orderSchDateString;
	private String orderDateString;
	private String orderDateTimeString;
	 private BigDecimal serviceAmt; 
		private Integer testType;
		
	private Boolean provisional;
	private Character isPoc;
	private Integer patientWorkbenchId;
	private Integer resultId;
	private Date workbenchAddedDate;
	private String workbenchRemark;
	private List<InvestigationOrderDto> currentEncounterOrderLists;

	private List<String> dates;

	private String addedDate;

	private String createdDate2;

	private String createdDateForFilter;

	private Date ordRenderDatetime;

	public String getCreatedDateForFilter() {
		return createdDateForFilter;
	}

	public void setCreatedDateForFilter(String createdDateForFilter) {
		this.createdDateForFilter = createdDateForFilter;
	}

	public String getCreatedDate2() {
		return createdDate2;
	}

	public void setCreatedDate2(String createdDate2) {
		this.createdDate2 = createdDate2;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
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

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Integer getPanelId() {
		return panelId;
	}

	public void setPanelId(Integer panelId) {
		this.panelId = panelId;
	}

	public Character getIsPanel() {
		return isPanel;
	}

	public void setIsPanel(Character isPanel) {
		this.isPanel = isPanel;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Integer getLabTestResId() {
		return labTestResId;
	}

	public void setLabTestResId(Integer labTestResId) {
		this.labTestResId = labTestResId;
	}

	public Integer getSubDeptId() {
		return subDeptId;
	}

	public void setSubDeptId(Integer subDeptId) {
		this.subDeptId = subDeptId;
	}

	public Integer getSampleStatusId() {
		return sampleStatusId;
	}

	public void setSampleStatusId(Integer sampleStatusId) {
		this.sampleStatusId = sampleStatusId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getServiceRendered() {
		return serviceRendered;
	}

	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}

	public Integer getTemplateTransactionId() {
		return templateTransactionId;
	}

	public void setTemplateTransactionId(Integer templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}

	public Integer getServiceIsBilled() {
		return serviceIsBilled;
	}

	public void setServiceIsBilled(Integer serviceIsBilled) {
		this.serviceIsBilled = serviceIsBilled;
	}

	public Character getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Character orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getCreatedDateBookView() {
		return createdDateBookView;
	}

	public void setCreatedDateBookView(String createdDateBookView) {
		this.createdDateBookView = createdDateBookView;
	}

	public Character getIsPackageService() {
		return isPackageService;
	}

	public void setIsPackageService(Character isPackageService) {
		this.isPackageService = isPackageService;
	}

	public List<OrderDetailsPackageMapperDto> getOrderDetailsPackageMapperDtosList() {
		return orderDetailsPackageMapperDtosList;
	}

	public void setOrderDetailsPackageMapperDtosList(List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList) {
		this.orderDetailsPackageMapperDtosList = orderDetailsPackageMapperDtosList;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Character getIsAutoRender() {
		return isAutoRender;
	}

	public void setIsAutoRender(Character isAutoRender) {
		this.isAutoRender = isAutoRender;
	}

	public Integer getOrderSetId() {
		return orderSetId;
	}

	public void setOrderSetId(Integer orderSetId) {
		this.orderSetId = orderSetId;
	}

	public String getOrderSchDate() {
		return orderSchDate;
	}

	public void setOrderSchDate(String orderSchDate) {
		this.orderSchDate = orderSchDate;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getProcedureReqId() {
		return procedureReqId;
	}

	public void setProcedureReqId(Integer procedureReqId) {
		this.procedureReqId = procedureReqId;
	}

	

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOrdRemarks() {
		return ordRemarks;
	}

	public void setOrdRemarks(String ordRemarks) {
		this.ordRemarks = ordRemarks;
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

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public String getTemplateObject() {
		return templateObject;
	}

	public void setTemplateObject(String templateObject) {
		this.templateObject = templateObject;
	}

	public Integer getMicroTemplateStatusId() {
		return microTemplateStatusId;
	}

	public void setMicroTemplateStatusId(Integer microTemplateStatusId) {
		this.microTemplateStatusId = microTemplateStatusId;
	}

	public String getSampleGenDatetime() {
		return sampleGenDatetime;
	}

	public void setSampleGenDatetime(String sampleGenDatetime) {
		this.sampleGenDatetime = sampleGenDatetime;
	}

	public String getCollectedUserName() {
		return collectedUserName;
	}

	public void setCollectedUserName(String collectedUserName) {
		this.collectedUserName = collectedUserName;
	}
	

    public Character getIsProcedureService() {
    return isProcedureService;
	}

	public void setIsProcedureService(Character isProcedureService) {
	    this.isProcedureService = isProcedureService;
	}

	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}

	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}

	public Date getSampleGenDatetimeNew() {
		return sampleGenDatetimeNew;
	}

	public void setSampleGenDatetimeNew(Date sampleGenDatetimeNew) {
		this.sampleGenDatetimeNew = sampleGenDatetimeNew;
	}

	public Date getOrderSchDateNew() {
		return orderSchDateNew;
	}

	public void setOrderSchDateNew(Date orderSchDateNew) {
		this.orderSchDateNew = orderSchDateNew;
	}

	public Date getCreatedDateNew() {
		return createdDateNew;
	}

	public void setCreatedDateNew(Date createdDateNew) {
		this.createdDateNew = createdDateNew;
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

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Integer getTemplateFormatId() {
		return templateFormatId;
	}

	public void setTemplateFormatId(Integer templateFormatId) {
		this.templateFormatId = templateFormatId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}


	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVisitNo() {
		return visitNo;
	}

	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}

	public Boolean getPrintReport() {
		return printReport;
	}

	public void setPrintReport(Boolean printReport) {
		this.printReport = printReport;
	}

	public Integer getPrintWithHeader() {
		return printWithHeader;
	}

	public void setPrintWithHeader(Integer printWithHeader) {
		this.printWithHeader = printWithHeader;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
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

	public Boolean getIsHtmlPrint() {
		return isHtmlPrint;
	}

	public void setIsHtmlPrint(Boolean isHtmlPrint) {
		this.isHtmlPrint = isHtmlPrint;
	}

	public Boolean getBillSettled() {
		return billSettled;
	}

	public void setBillSettled(Boolean billSettled) {
		this.billSettled = billSettled;
	}

	public String getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public String getLabSampleNo() {
		return labSampleNo;
	}

	public void setLabSampleNo(String labSampleNo) {
		this.labSampleNo = labSampleNo;
	}

	public Character getIsProcedure() {
		return isProcedure;
	}

	public void setIsProcedure(Character isProcedure) {
		this.isProcedure = isProcedure;
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

	public BigDecimal getServiceAmt() {
		return serviceAmt;
	}

	public void setServiceAmt(BigDecimal serviceAmt) {
		this.serviceAmt = serviceAmt;
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

	public Integer getTestType() {
		return testType;
	}

	public void setTestType(Integer testType) {
		this.testType = testType;
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


	public List<InvestigationOrderDto> getCurrentEncounterOrderLists() {
		return currentEncounterOrderLists;
	}

	public void setCurrentEncounterOrderLists(List<InvestigationOrderDto> currentEncounterOrderLists) {
		this.currentEncounterOrderLists = currentEncounterOrderLists;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public Character getIsPoc() {
		return isPoc;
	}

	public void setIsPoc(Character isPoc) {
		this.isPoc = isPoc;
	}

	public Integer getPatientWorkbenchId() {
		return patientWorkbenchId;
	}

	public void setPatientWorkbenchId(Integer patientWorkbenchId) {
		this.patientWorkbenchId = patientWorkbenchId;
	}

	public Integer getResultId() {
		return resultId;
	}

	public void setResultId(Integer resultId) {
		this.resultId = resultId;
	}

	public Date getWorkbenchAddedDate() {
		return workbenchAddedDate;
	}

	public void setWorkbenchAddedDate(Date workbenchAddedDate) {
		this.workbenchAddedDate = workbenchAddedDate;
	}

	public String getWorkbenchRemark() {
		return workbenchRemark;
	}

	public void setWorkbenchRemark(String workbenchRemark) {
		this.workbenchRemark = workbenchRemark;
	}

	public Date getOrdRenderDatetime() {
		return ordRenderDatetime;
	}

	public void setOrdRenderDatetime(Date ordRenderDatetime) {
		this.ordRenderDatetime = ordRenderDatetime;
	}
}
