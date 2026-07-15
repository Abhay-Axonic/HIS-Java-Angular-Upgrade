package com.param.global.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.param.entity.model.master.OrderDetail;

public class InvestigationOrderMasterNewDto {

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
	
	private Integer slotId;
	private Integer organizationId;
	private String createdDateBookView;
	private Date createdDateNew;
	private Date sampleGenDatetimeNew;
	private Date orderSchDateNew;
	private Character isPackageService;
	private Character isAutoRender;
	private Integer billingBedCategoryId;

	private Integer orderSetId;
	private Integer freeTextOrderedServicesId;
	private boolean isConsumed;
	private String searchKeyword;
	private String serviceText;


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
	private String reportEntryDate;
	private String reportValidationDate;
	private String reportReleaseDate;
	 private BigDecimal serviceAmt;
		private BigDecimal apportionedPrice;// yogesh
	    private Integer departmentId;
		private char isCapService;
		private Integer covTemplateId;
		private String orderSchDateString;
		private String orderDateString;
		
		private Integer testType;
		private Integer billingMasterId;
		private Integer medallServiceId;
		private Boolean provisional;
		private Integer orgId;
		private Integer orgUnitId;
		private Integer ordDoctorId;
		private String patientType;
		private Integer wardId;
		private Integer createdBy;
		private Integer updatedBy;
		private Long updatedDate;
		private Integer orderStatusId;
		private Integer paymentEntitlementTypeId;
		private Integer defaultSelfTariffId;
		private double discountPercentage;
		private List<OrderDetailsMasterDto> listOrderDetailsMasterDto;
		private Integer doctorId;
		private String currentDate;
		private Integer patientTypeId;
		private Integer patientClassId;
		private Integer contractId;
		private Integer payeeId;
		private BigDecimal reqDisAmt;
		private BigDecimal netAmount;
		private Integer discType;
		private Integer requestSendTo;
		private Character isPharmacy;
		private Integer intrimBillId;
		private String intrimBillNo;
		private Integer saleId;
		private Integer referralId;
		private Character isPreOrPostBilling;
		private Integer pharmacyServiceId;
		private List<OrderDetailsMasterDto> lineLevelDiscountOrderList;
		private List<SlotMasterDto> slotMasterDtoList;
		private Integer tariffId;
		private Integer patientCategoryId;
		private Integer ordDocSplId ;
		private Integer systemDefiedVisitType;
	    private OrderDetailsMasterDto orderDetailsMasterDto;
	    private Integer defaultDiscountOwnership;
	    private Integer selfPayeeId;
	    private boolean homeCollection;
	    private boolean addToBill;
	    private Character blockReportPrint;
	    private Integer insuranceId;
	    private String branchName;
		private String orderTs;
		private String ipoptype;
		private String uHID;
		private String iPOPNo;
		private String salutation;
		private String gender;
		private String birthDate;
		private String ward;
		private String patMobileNo;
		private String patEmailId;
		private String patAddress;
		private String patCity;
		private String refDrCode;
		private String refDrName;
		private Boolean feesPaid;
		private String neuronPlusUrl;
		private BigDecimal pendingSelfPayable;
		private Double depositAmount;
		private List<OrderDetailsMasterDto> orderTests;
		private String returnDate;
		private Integer ordDocId;
		private Integer packageTypeId;
		private Integer serviceRenderingDeptId;
		private Integer servicePriorityId;
		private Character isRefDoctorShare;
		private Integer subDepartmentId;
		private Character nursingServiceOrderFlag;
		private Integer procedureOrderMstId;
		private Integer bedId;
		private Integer roomId;
		private BigDecimal ordConcession;
		private BigDecimal ordDiscount;
		private BigDecimal selfPayable;
		private BigDecimal creditPayable;
		private BigDecimal coPayPer;
		private List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList;
		private boolean isPackage;
		private Character isInvestigationOrder;
		
		public Integer getTestType() {
			return testType;
		}

		public void setTestType(Integer testType) {
			this.testType = testType;
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

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public List<OrderDetailsMasterDto> getListOrderDetailsMasterDto() {
		return listOrderDetailsMasterDto;
	}

	public void setListOrderDetailsMasterDto(List<OrderDetailsMasterDto> listOrderDetailsMasterDto) {
		this.listOrderDetailsMasterDto = listOrderDetailsMasterDto;
	}

	public BigDecimal getApportionedPrice() {
		return apportionedPrice;
	}

	public void setApportionedPrice(BigDecimal apportionedPrice) {
		this.apportionedPrice = apportionedPrice;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public char getIsCapService() {
		return isCapService;
	}

	public void setIsCapService(char isCapService) {
		this.isCapService = isCapService;
	}

	public Integer getOrdDocSplId() {
		return ordDocSplId;
	}

	public void setOrdDocSplId(Integer ordDocSplId) {
		this.ordDocSplId = ordDocSplId;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
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

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public Integer getPaymentEntitlementTypeId() {
		return paymentEntitlementTypeId;
	}

	public void setPaymentEntitlementTypeId(Integer paymentEntitlementTypeId) {
		this.paymentEntitlementTypeId = paymentEntitlementTypeId;
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

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public String getOrderSchDateString() {
		return orderSchDateString;
	}

	public void setOrderSchDateString(String orderSchDateString) {
		this.orderSchDateString = orderSchDateString;
	}

	public Integer getFreeTextOrderedServicesId() {
		return freeTextOrderedServicesId;
	}

	public void setFreeTextOrderedServicesId(Integer freeTextOrderedServicesId) {
		this.freeTextOrderedServicesId = freeTextOrderedServicesId;
	}

	public boolean getIsConsumed() {
		return isConsumed;
	}

	public void setIsConsumed(boolean isConsumed) {
		this.isConsumed = isConsumed;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public String getServiceText() {
		return serviceText;
	}

	public void setServiceText(String serviceText) {
		this.serviceText = serviceText;
	}

	public String getOrderDateString() {
		return orderDateString;
	}

	public void setOrderDateString(String orderDateString) {
		this.orderDateString = orderDateString;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
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

	public Integer getOrdDoctorId() {
		return ordDoctorId;
	}

	public void setOrdDoctorId(Integer ordDoctorId) {
		this.ordDoctorId = ordDoctorId;
	}

	public String getPatientType() {
		return patientType;
	}

	public void setPatientType(String patientType) {
		this.patientType = patientType;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public double getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public BigDecimal getReqDisAmt() {
		return reqDisAmt;
	}

	public void setReqDisAmt(BigDecimal reqDisAmt) {
		this.reqDisAmt = reqDisAmt;
	}

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getDiscType() {
		return discType;
	}

	public void setDiscType(Integer discType) {
		this.discType = discType;
	}

	public Integer getRequestSendTo() {
		return requestSendTo;
	}

	public void setRequestSendTo(Integer requestSendTo) {
		this.requestSendTo = requestSendTo;
	}

	public Character getIsPharmacy() {
		return isPharmacy;
	}

	public void setIsPharmacy(Character isPharmacy) {
		this.isPharmacy = isPharmacy;
	}

	public Integer getIntrimBillId() {
		return intrimBillId;
	}

	public void setIntrimBillId(Integer intrimBillId) {
		this.intrimBillId = intrimBillId;
	}

	public String getIntrimBillNo() {
		return intrimBillNo;
	}

	public void setIntrimBillNo(String intrimBillNo) {
		this.intrimBillNo = intrimBillNo;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

	public Character getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}

	public void setIsPreOrPostBilling(Character isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
	}

	public Integer getPharmacyServiceId() {
		return pharmacyServiceId;
	}

	public void setPharmacyServiceId(Integer pharmacyServiceId) {
		this.pharmacyServiceId = pharmacyServiceId;
	}

	public List<OrderDetailsMasterDto> getLineLevelDiscountOrderList() {
		return lineLevelDiscountOrderList;
	}

	public void setLineLevelDiscountOrderList(List<OrderDetailsMasterDto> lineLevelDiscountOrderList) {
		this.lineLevelDiscountOrderList = lineLevelDiscountOrderList;
	}

	public List<SlotMasterDto> getSlotMasterDtoList() {
		return slotMasterDtoList;
	}

	public void setSlotMasterDtoList(List<SlotMasterDto> slotMasterDtoList) {
		this.slotMasterDtoList = slotMasterDtoList;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getSystemDefiedVisitType() {
		return systemDefiedVisitType;
	}

	public void setSystemDefiedVisitType(Integer systemDefiedVisitType) {
		this.systemDefiedVisitType = systemDefiedVisitType;
	}

	public OrderDetailsMasterDto getOrderDetailsMasterDto() {
		return orderDetailsMasterDto;
	}

	public void setOrderDetailsMasterDto(OrderDetailsMasterDto orderDetailsMasterDto) {
		this.orderDetailsMasterDto = orderDetailsMasterDto;
	}

	public Integer getDefaultDiscountOwnership() {
		return defaultDiscountOwnership;
	}

	public void setDefaultDiscountOwnership(Integer defaultDiscountOwnership) {
		this.defaultDiscountOwnership = defaultDiscountOwnership;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public boolean isHomeCollection() {
		return homeCollection;
	}

	public void setHomeCollection(boolean homeCollection) {
		this.homeCollection = homeCollection;
	}

	public boolean isAddToBill() {
		return addToBill;
	}

	public void setAddToBill(boolean addToBill) {
		this.addToBill = addToBill;
	}

	public Character getBlockReportPrint() {
		return blockReportPrint;
	}

	public void setBlockReportPrint(Character blockReportPrint) {
		this.blockReportPrint = blockReportPrint;
	}

	public Integer getInsuranceId() {
		return insuranceId;
	}

	public void setInsuranceId(Integer insuranceId) {
		this.insuranceId = insuranceId;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getOrderTs() {
		return orderTs;
	}

	public void setOrderTs(String orderTs) {
		this.orderTs = orderTs;
	}

	public String getIpoptype() {
		return ipoptype;
	}

	public void setIpoptype(String ipoptype) {
		this.ipoptype = ipoptype;
	}

	public String getuHID() {
		return uHID;
	}

	public void setuHID(String uHID) {
		this.uHID = uHID;
	}

	public String getiPOPNo() {
		return iPOPNo;
	}

	public void setiPOPNo(String iPOPNo) {
		this.iPOPNo = iPOPNo;
	}

	public String getSalutation() {
		return salutation;
	}

	public void setSalutation(String salutation) {
		this.salutation = salutation;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getWard() {
		return ward;
	}

	public void setWard(String ward) {
		this.ward = ward;
	}

	public String getPatMobileNo() {
		return patMobileNo;
	}

	public void setPatMobileNo(String patMobileNo) {
		this.patMobileNo = patMobileNo;
	}

	public String getPatEmailId() {
		return patEmailId;
	}

	public void setPatEmailId(String patEmailId) {
		this.patEmailId = patEmailId;
	}

	public String getPatAddress() {
		return patAddress;
	}

	public void setPatAddress(String patAddress) {
		this.patAddress = patAddress;
	}

	public String getPatCity() {
		return patCity;
	}

	public void setPatCity(String patCity) {
		this.patCity = patCity;
	}

	public String getRefDrCode() {
		return refDrCode;
	}

	public void setRefDrCode(String refDrCode) {
		this.refDrCode = refDrCode;
	}

	public String getRefDrName() {
		return refDrName;
	}

	public void setRefDrName(String refDrName) {
		this.refDrName = refDrName;
	}

	public Boolean getFeesPaid() {
		return feesPaid;
	}

	public void setFeesPaid(Boolean feesPaid) {
		this.feesPaid = feesPaid;
	}

	public String getNeuronPlusUrl() {
		return neuronPlusUrl;
	}

	public void setNeuronPlusUrl(String neuronPlusUrl) {
		this.neuronPlusUrl = neuronPlusUrl;
	}

	public BigDecimal getPendingSelfPayable() {
		return pendingSelfPayable;
	}

	public void setPendingSelfPayable(BigDecimal pendingSelfPayable) {
		this.pendingSelfPayable = pendingSelfPayable;
	}

	public Double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public List<OrderDetailsMasterDto> getOrderTests() {
		return orderTests;
	}

	public void setOrderTests(List<OrderDetailsMasterDto> orderTests) {
		this.orderTests = orderTests;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public void setConsumed(boolean isConsumed) {
		this.isConsumed = isConsumed;
	}

	public Integer getOrdDocId() {
		return ordDocId;
	}

	public void setOrdDocId(Integer ordDocId) {
		this.ordDocId = ordDocId;
	}

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public Integer getServiceRenderingDeptId() {
		return serviceRenderingDeptId;
	}

	public void setServiceRenderingDeptId(Integer serviceRenderingDeptId) {
		this.serviceRenderingDeptId = serviceRenderingDeptId;
	}

	public Integer getServicePriorityId() {
		return servicePriorityId;
	}

	public void setServicePriorityId(Integer servicePriorityId) {
		this.servicePriorityId = servicePriorityId;
	}

	public Character getIsRefDoctorShare() {
		return isRefDoctorShare;
	}

	public void setIsRefDoctorShare(Character isRefDoctorShare) {
		this.isRefDoctorShare = isRefDoctorShare;
	}

	public Integer getSubDepartmentId() {
		return subDepartmentId;
	}

	public void setSubDepartmentId(Integer subDepartmentId) {
		this.subDepartmentId = subDepartmentId;
	}

	public Character getNursingServiceOrderFlag() {
		return nursingServiceOrderFlag;
	}

	public void setNursingServiceOrderFlag(Character nursingServiceOrderFlag) {
		this.nursingServiceOrderFlag = nursingServiceOrderFlag;
	}

	public Integer getProcedureOrderMstId() {
		return procedureOrderMstId;
	}

	public void setProcedureOrderMstId(Integer procedureOrderMstId) {
		this.procedureOrderMstId = procedureOrderMstId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getRoomId() {
		return roomId;
	}

	public void setRoomId(Integer roomId) {
		this.roomId = roomId;
	}

	public BigDecimal getOrdConcession() {
		return ordConcession!=null?ordConcession:BigDecimal.ZERO;
	}

	public void setOrdConcession(BigDecimal ordConcession) {
		this.ordConcession = ordConcession!=null?ordConcession:BigDecimal.ZERO;;
	}

	public BigDecimal getOrdDiscount() {
		return ordDiscount!=null ? ordDiscount : BigDecimal.ZERO;
	}

	public void setOrdDiscount(BigDecimal ordDiscount) {
		this.ordDiscount = ordDiscount!=null ? ordDiscount : BigDecimal.ZERO;
	}
	
	public BigDecimal getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(BigDecimal selfPayable) {
		this.selfPayable = selfPayable;
	}

	public BigDecimal getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(BigDecimal creditPayable) {
		this.creditPayable = creditPayable;
	}
	
	public BigDecimal getCoPayPer() {
		return coPayPer==null?BigDecimal.ZERO:coPayPer;
	}

	public void setCoPayPer(BigDecimal coPayPer) {
		this.coPayPer = coPayPer;
	}
	
	public List<OrderDetailsPayeeMapperDto> getOrderDetailsPayeeMapperDtosList() {
		return orderDetailsPayeeMapperDtosList;
	}

	public void setOrderDetailsPayeeMapperDtosList(List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList) {
		this.orderDetailsPayeeMapperDtosList = orderDetailsPayeeMapperDtosList;
	}
	
	public void setPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}

	public void setIsPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}

	public boolean isPackage() {
		return isPackage;
	}
	
	public Character getIsInvestigationOrder() {
		return isInvestigationOrder;
	}

	public void setIsInvestigationOrder(Character isInvestigationOrder) {
		this.isInvestigationOrder = isInvestigationOrder;
	}
}
