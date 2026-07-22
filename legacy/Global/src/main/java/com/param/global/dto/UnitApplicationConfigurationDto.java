package com.param.global.dto;

import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;

public class UnitApplicationConfigurationDto extends MailDto {

	private Integer unitApplicationBillingConfigurationId;

	private Integer organizationId;

	private Integer unitId;

	private Integer defaultSelfTariffId;

	private Character isPreOrPostBilling;

	private Character isAppointmentRequired;

	private Character isCounterApplicable;

	private Integer docRoleId;

	private Integer nurseRoleId;

	private Integer staffRoleId;

	private String pdfGenerationUrl;

	private Character isServiceWiseSplitBill;

	private Integer patientRegSerId;

	private Integer opdBillingCatId;

	private Integer selfPayeeId;

	private Integer consultationSpecialityId;

	private Integer consultationSubSpecialityId;

	private Integer billingBedCategoryId;

	private Integer adminRoleId;

	private String createdDate;

	private Integer createdBy;

	private Integer patientCategoryId;

	private String updatedDate;

	private Integer updatedBy;

	private Character status;

	private Integer radiologySpecialityId;

	private Integer offset;

	private Integer noOfRecordsPerPage;

	private String specialityName;

	private String tariffCode;

	private String tariffDesc;

	private String label;

	private Boolean taxInclusive;

	// private Boolean atCopRequired;

	private Boolean issueMaterialPickerRequired;

	private Boolean assignPickerRequired;

	protected Boolean isMarkup;

	protected Boolean markupUp;

	private String billingBedDesc;

	private String companyDesc;

	private String senderId;

	private Integer casualtySpecialityId;

	private Integer paediatricsSpecialityId;

	private Integer pharmacyServiceId;

	private Integer companyPatientCategory;

	private Integer insurancePatientCategory;

	private Integer pharmacyReturnSaleServiceId;

	private Character isKioskRequired;

	private Integer otRentServiceId;

	private Integer surgeonChargesServiceId;

	private Integer surchargeServiceId;

	private List<Integer> selectunitApplicationConfigurationList;

	private Character isNursingAssessment;

	private Integer erSpecialityId;

	private Character isRenderedServiceRateEditable;

	private Integer admissionChargesServiceId;

	private Character isSmartCardRequired;

	private Character isDigitalSignatureRequired;

	private Character isPinPadRequired;

	private Character isShowPatientByBed;

	private String groupPracticeNo;

	private String groupPracticeName;

	private String practiceNo;

	private Character isAdviceTabVisible;

	private String startDateFormat;

	private Character isCorporateEmpVerificationReq;

	private Character isPACSRequired;

	private Integer obgynSpecialityId;

	private Integer hemodialysisSpecialityId;

	private Character isConsolidatedBill;

	private Integer assetMaintenanceSpecialityId;

	private Integer defaultPatientCategoryId;

	private Integer physiotherapySpecialityId;

	private Character isProcessShareAgainstSettledBill;

	private String morningFromTime;

	private String morningToTime;

	private String eveningFromTime;

	private String eveningToTime;

	private Character isRefDoctorShare;

	private Integer serviceSpecilaityId;

	private String refreshTime;

	private Integer additionalOtrentService;

	private Integer badDebtsDays;

	private Character isSchemeDiscount;

	private Character isBackDatedAdmission;

	private Integer pddDays;

	private Character skipReadyForBilling;

	private Character skipAdmissionAcceptance;

	private Character skipHousekeeping;

	private Character isFreeVisitReq;

	private Character isHospitalVisitReq;

	private Character isReviewAppReq;

	private Character isLabReq;

	private Character isEHCReq;

	private Character isRadiologyReq;

	private Character isDocConsultReq;

	private Character isGstVisible;

	private Character optimizeTransferFlow;

	private Character allowOtherDoctorToConsult;

	private Character diagnosisRequire;

	private Character skipRISReportEntry;

	private Integer defaultDiscountOwnership;

	private char isRateVisibleOnCoversheet;

	private String pharmacyServiceName;

	private String otRentServiceName;

	private String surgeonChargesServiceName;

	private String surchargeServiceName;

	private String admissionChargesServiceName;

	private String additionalOtrentServiceName;

	private String pharmacyReturnSaleName;

	protected Boolean isPharmacologicalValidationSkipIp;

	private char isSendMailOnMailPrescription;
	private char isVitalQualifierVisible;
	private char isAdvisedDateVisible;
	private String kioskLandingScreen;
	private Integer doctorSlipTDS;
	private Integer cashAmountValidation;

	private Integer biomedicalsChargesServiceId;

	private Integer hospitalChargesServiceId;

	private Integer mrdChargesServiceId;

	private String biomedicalsChargesServiceName;
	private String hospitalChargesServiceName;
	private String mrdChargesServiceName;

	private Character skipTransferAcceptance;

	private String emailId;
	private String password;
	private Character isPaymentGatewayReqForWebApp;
	private Character isRisPatientDetailsOnFirstPage;
	private String razorPayKeyId;
	private String razorPaySecretKey;
	private Character isReportAddIntoDischargeSummary;
	private Integer admissionCancellationTime;
	private boolean opStatNotRequired;
	private boolean ipStatNotRequired;
	private Character isPackageApplicableDateEditable;
	private Integer webAppUserId;
	private String razorPayCallBackUrl;
	private Integer mailProviderId;
	private String mailCredential;
	private Character isKioskAnnouncementRequired;
	private Integer cashAmountLimit;
	private boolean vaccinationChartHide;
	private String bccEmailId;
	private Character blockReportPrint;
	private Integer payGatewayProviderId;
	private Character emailValidationRequired;
	private Boolean depositTypeWiseRequired;
	private Integer whatsAppOption;
	private String dateFormat;
	private boolean selfPaidConsultAlert;
	private Character neuronPlus;
	private boolean serviceWiseDoctorListReq;

	private String registeredPinPadDevice;
	private Character cimsOption;
	private boolean gradeWiseSeviceApplicable;

	private String pdfGenerationUrlMis;

	private Integer backDateAdmissionDays;

	private Character appointmentUniqueIdSearch;

	private Boolean couponSystemApplicable;

	private Character tokenNumber;

	private Character mobileValidationRequired;

	private Character mandatoryScan;

	private Boolean exceededBillAmountSettlement;

	private Boolean runTimeRateChangeUsingTarrif;

	private Character qmsPrnPrint;

	private String qmsPrnPrintFormat;

	private Integer dischargeSummaryDataViewOptionId;

	private Integer ambulanceChargesServiceId;

	private boolean isEmrRequired;

	private String ambulanceChargesService;

	private String tableHeaderColor;

	private Boolean gstForBillingBedCategory;

	private String singlePageRefresh;

	private Character roa;
	private String localStorageJson;

	private Integer bucketFlag;

	private Integer smsType;
	
	private String smsTypejson;
	
	private Character dosage;

	private Character isBillManipulationAllowed;
	
	private Character invoiceCreationInsurance;
	
	private Character invoiceCreationCorporate;
	private Character isAiButtonConfigc;
	private Character ayushmanBharatIntegration;
	private Boolean duplicateTab;

	private Character ispdfPasswordRequire;
	private String tallyIp;
	private String tallyPortNo;
	private Character isTallyLocalHost;

	private Integer mobileDigit;
	private Integer adminServiceMasterId;

	private Character otherOrgLogoRequired;

	private Character drugTodrugCheck;
	private BigDecimal maxInputAmount;

	private Character isAiDoctorEnabled;

	public Character getIsBillManipulationAllowed() {
		return isBillManipulationAllowed;
	}

	public void setIsBillManipulationAllowed(Character isBillManipulationAllowed) {
		this.isBillManipulationAllowed = isBillManipulationAllowed;
	}

	public String getSinglePageRefresh() {
		return singlePageRefresh;
	}

	public void setSinglePageRefresh(String singlePageRefresh) {
		this.singlePageRefresh = singlePageRefresh;
	}

	public String getBccEmailId() {
		return bccEmailId;
	}

	public void setBccEmailId(String bccEmailId) {
		this.bccEmailId = bccEmailId;
	}

	public Character getIsKioskAnnouncementRequired() {
		return isKioskAnnouncementRequired;
	}

	public void setIsKioskAnnouncementRequired(Character isKioskAnnouncementRequired) {
		this.isKioskAnnouncementRequired = isKioskAnnouncementRequired;
	}

	public Character getIsPackageApplicableDateEditable() {
		return isPackageApplicableDateEditable;
	}

	public void setIsPackageApplicableDateEditable(Character isPackageApplicableDateEditable) {
		this.isPackageApplicableDateEditable = isPackageApplicableDateEditable;
	}

	public String getKioskLandingScreen() {
		return kioskLandingScreen;
	}

	public void setKioskLandingScreen(String kioskLandingScreen) {
		this.kioskLandingScreen = kioskLandingScreen;
	}

	public Integer getDefaultDiscountOwnership() {
		return defaultDiscountOwnership;
	}

	public void setDefaultDiscountOwnership(Integer defaultDiscountOwnership) {
		this.defaultDiscountOwnership = defaultDiscountOwnership;
	}

	public Character getOptimizeTransferFlow() {
		return optimizeTransferFlow;
	}

	public void setOptimizeTransferFlow(Character optimizeTransferFlow) {
		this.optimizeTransferFlow = optimizeTransferFlow;
	}

	public Character getIsGstVisible() {
		return isGstVisible;
	}

	public void setIsGstVisible(Character isGstVisible) {
		this.isGstVisible = isGstVisible;
	}

	public Character getIsFreeVisitReq() {
		return isFreeVisitReq;
	}

	public void setIsFreeVisitReq(Character isFreeVisitReq) {
		this.isFreeVisitReq = isFreeVisitReq;
	}

	public Character getIsHospitalVisitReq() {
		return isHospitalVisitReq;
	}

	public void setIsHospitalVisitReq(Character isHospitalVisitReq) {
		this.isHospitalVisitReq = isHospitalVisitReq;
	}

	public Character getIsReviewAppReq() {
		return isReviewAppReq;
	}

	public void setIsReviewAppReq(Character isReviewAppReq) {
		this.isReviewAppReq = isReviewAppReq;
	}

	public Character getIsLabReq() {
		return isLabReq;
	}

	public void setIsLabReq(Character isLabReq) {
		this.isLabReq = isLabReq;
	}

	public Character getIsEHCReq() {
		return isEHCReq;
	}

	public void setIsEHCReq(Character isEHCReq) {
		this.isEHCReq = isEHCReq;
	}

	public Character getIsRadiologyReq() {
		return isRadiologyReq;
	}

	public void setIsRadiologyReq(Character isRadiologyReq) {
		this.isRadiologyReq = isRadiologyReq;
	}

	public Character getIsDocConsultReq() {
		return isDocConsultReq;
	}

	public void setIsDocConsultReq(Character isDocConsultReq) {
		this.isDocConsultReq = isDocConsultReq;
	}

	public Character getSkipAdmissionAcceptance() {
		return skipAdmissionAcceptance;
	}

	public void setSkipAdmissionAcceptance(Character skipAdmissionAcceptance) {
		this.skipAdmissionAcceptance = skipAdmissionAcceptance;
	}

	public Character getSkipHousekeeping() {
		return skipHousekeeping;
	}

	public void setSkipHousekeeping(Character skipHousekeeping) {
		this.skipHousekeeping = skipHousekeeping;
	}

	public Character getSkipReadyForBilling() {
		return skipReadyForBilling;
	}

	public void setSkipReadyForBilling(Character skipReadyForBilling) {
		this.skipReadyForBilling = skipReadyForBilling;
	}

	public Integer getPddDays() {
		return pddDays;
	}

	public void setPddDays(Integer pddDays) {
		this.pddDays = pddDays;
	}

	public Character getIsBackDatedAdmission() {
		return isBackDatedAdmission;
	}

	public void setIsBackDatedAdmission(Character isBackDatedAdmission) {
		this.isBackDatedAdmission = isBackDatedAdmission;
	}

	public Character getIsSchemeDiscount() {
		return isSchemeDiscount;
	}

	public void setIsSchemeDiscount(Character isSchemeDiscount) {
		this.isSchemeDiscount = isSchemeDiscount;
	}

	public Integer getBadDebtsDays() {
		return badDebtsDays;
	}

	public void setBadDebtsDays(Integer badDebtsDays) {
		this.badDebtsDays = badDebtsDays;
	}

	public String getRefreshTime() {
		return refreshTime;
	}

	public void setRefreshTime(String refreshTime) {
		this.refreshTime = refreshTime;
	}

	public Integer getServiceSpecilaityId() {
		return serviceSpecilaityId;
	}

	public void setServiceSpecilaityId(Integer serviceSpecilaityId) {
		this.serviceSpecilaityId = serviceSpecilaityId;
	}

	public Character getIsRefDoctorShare() {
		return isRefDoctorShare;
	}

	public void setIsRefDoctorShare(Character isRefDoctorShare) {
		this.isRefDoctorShare = isRefDoctorShare;
	}

	public Character getIsProcessShareAgainstSettledBill() {
		return isProcessShareAgainstSettledBill;
	}

	public void setIsProcessShareAgainstSettledBill(Character isProcessShareAgainstSettledBill) {
		this.isProcessShareAgainstSettledBill = isProcessShareAgainstSettledBill;
	}

	public Integer getDefaultPatientCategoryId() {
		return defaultPatientCategoryId;
	}

	public void setDefaultPatientCategoryId(Integer defaultPatientCategoryId) {
		this.defaultPatientCategoryId = defaultPatientCategoryId;
	}

	public Integer getSurchargeServiceId() {
		return surchargeServiceId;
	}

	public void setSurchargeServiceId(Integer surchargeServiceId) {
		this.surchargeServiceId = surchargeServiceId;
	}

	public Character getIsKioskRequired() {
		return isKioskRequired;
	}

	public void setIsKioskRequired(Character isKioskRequired) {
		this.isKioskRequired = isKioskRequired;
	}

	public Integer getCompanyPatientCategory() {
		return companyPatientCategory;
	}

	public void setCompanyPatientCategory(Integer companyPatientCategory) {
		this.companyPatientCategory = companyPatientCategory;
	}

	public Integer getInsurancePatientCategory() {
		return insurancePatientCategory;
	}

	public void setInsurancePatientCategory(Integer insurancePatientCategory) {
		this.insurancePatientCategory = insurancePatientCategory;
	}

	public Integer getConsultationSubSpecialityId() {
		return consultationSubSpecialityId;
	}

	public void setConsultationSubSpecialityId(Integer consultationSubSpecialityId) {
		this.consultationSubSpecialityId = consultationSubSpecialityId;
	}

	public Integer getPatientRegSerId() {
		return patientRegSerId;
	}

	public void setPatientRegSerId(Integer patientRegSerId) {
		this.patientRegSerId = patientRegSerId;
	}

	public Integer getOpdBillingCatId() {
		return opdBillingCatId;
	}

	public void setOpdBillingCatId(Integer opdBillingCatId) {
		this.opdBillingCatId = opdBillingCatId;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Integer getConsultationSpecialityId() {
		return consultationSpecialityId;
	}

	public void setConsultationSpecialityId(Integer consultationSpecialityId) {
		this.consultationSpecialityId = consultationSpecialityId;
	}

	public Character getIsServiceWiseSplitBill() {
		return isServiceWiseSplitBill;
	}

	public void setIsServiceWiseSplitBill(Character isServiceWiseSplitBill) {
		this.isServiceWiseSplitBill = isServiceWiseSplitBill;
	}

	public String getPdfGenerationUrl() {
		return pdfGenerationUrl;
	}

	public void setPdfGenerationUrl(String pdfGenerationUrl) {
		this.pdfGenerationUrl = pdfGenerationUrl;
	}

	public Integer getDocRoleId() {
		return docRoleId;
	}

	public void setDocRoleId(Integer docRoleId) {
		this.docRoleId = docRoleId;
	}

	public Integer getNurseRoleId() {
		return nurseRoleId;
	}

	public void setNurseRoleId(Integer nurseRoleId) {
		this.nurseRoleId = nurseRoleId;
	}

	public Integer getStaffRoleId() {
		return staffRoleId;
	}

	public void setStaffRoleId(Integer staffRoleId) {
		this.staffRoleId = staffRoleId;
	}

	public Integer getAdminRoleId() {
		return adminRoleId;
	}

	public void setAdminRoleId(Integer adminRoleId) {
		this.adminRoleId = adminRoleId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	/*
	 * public Integer getId() { return id; }
	 * 
	 * public void setId(Integer id) { this.id = id; }
	 */

	public Character getIsAppointmentRequired() {
		return isAppointmentRequired;
	}

	public void setIsAppointmentRequired(Character isAppointmentRequired) {
		this.isAppointmentRequired = isAppointmentRequired;
	}

	public Integer getUnitApplicationBillingConfigurationId() {
		return unitApplicationBillingConfigurationId;
	}

	public void setUnitApplicationBillingConfigurationId(Integer unitApplicationBillingConfigurationId) {
		this.unitApplicationBillingConfigurationId = unitApplicationBillingConfigurationId;
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

	public Integer getDefaultSelfTariffId() {
		return defaultSelfTariffId;
	}

	public void setDefaultSelfTariffId(Integer defaultSelfTariffId) {
		this.defaultSelfTariffId = defaultSelfTariffId;
	}

	public Character getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}

	public void setIsPreOrPostBilling(Character isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getRadiologySpecialityId() {
		return radiologySpecialityId;
	}

	public void setRadiologySpecialityId(Integer radiologySpecialityId) {
		this.radiologySpecialityId = radiologySpecialityId;
	}

	public Character getIsCounterApplicable() {
		return isCounterApplicable;
	}

	public void setIsCounterApplicable(Character isCounterApplicable) {
		this.isCounterApplicable = isCounterApplicable;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getTariffCode() {
		return tariffCode;
	}

	public void setTariffCode(String tariffCode) {
		this.tariffCode = tariffCode;
	}

	public String getTariffDesc() {
		return tariffDesc;
	}

	public void setTariffDesc(String tariffDesc) {
		this.tariffDesc = tariffDesc;
	}

	public Boolean getTaxInclusive() {
		return taxInclusive;
	}

	public void setTaxInclusive(Boolean taxInclusive) {
		this.taxInclusive = taxInclusive;
	}

	public Boolean getIssueMaterialPickerRequired() {
		return issueMaterialPickerRequired;
	}

	public void setIssueMaterialPickerRequired(Boolean issueMaterialPickerRequired) {
		this.issueMaterialPickerRequired = issueMaterialPickerRequired;
	}

	public Boolean getAssignPickerRequired() {
		return assignPickerRequired;
	}

	public void setAssignPickerRequired(Boolean assignPickerRequired) {
		this.assignPickerRequired = assignPickerRequired;
	}

	public Boolean getIsMarkup() {
		return isMarkup;
	}

	public void setIsMarkup(Boolean isMarkup) {
		this.isMarkup = isMarkup;
	}

	public Boolean getMarkupUp() {
		return markupUp;
	}

	public void setMarkupUp(Boolean markupUp) {
		this.markupUp = markupUp;
	}

	public List<Integer> getSelectunitApplicationConfigurationList() {
		return selectunitApplicationConfigurationList;
	}

	public void setSelectunitApplicationConfigurationList(List<Integer> selectunitApplicationConfigurationList) {
		this.selectunitApplicationConfigurationList = selectunitApplicationConfigurationList;
	}

	public Integer getPatientCategoryId() {
		return patientCategoryId;
	}

	public void setPatientCategoryId(Integer patientCategoryId) {
		this.patientCategoryId = patientCategoryId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public String getBillingBedDesc() {
		return billingBedDesc;
	}

	public void setBillingBedDesc(String billingBedDesc) {
		this.billingBedDesc = billingBedDesc;
	}

	public String getCompanyDesc() {
		return companyDesc;
	}

	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public Integer getCasualtySpecialityId() {
		return casualtySpecialityId;
	}

	public void setCasualtySpecialityId(Integer casualtySpecialityId) {
		this.casualtySpecialityId = casualtySpecialityId;
	}

	public Integer getPaediatricsSpecialityId() {
		return paediatricsSpecialityId;
	}

	public void setPaediatricsSpecialityId(Integer paediatricsSpecialityId) {
		this.paediatricsSpecialityId = paediatricsSpecialityId;
	}

	public Integer getPharmacyServiceId() {
		return pharmacyServiceId;
	}

	public void setPharmacyServiceId(Integer pharmacyServiceId) {
		this.pharmacyServiceId = pharmacyServiceId;
	}

	public Integer getPharmacyReturnSaleServiceId() {
		return pharmacyReturnSaleServiceId;
	}

	public void setPharmacyReturnSaleServiceId(Integer pharmacyReturnSaleServiceId) {
		this.pharmacyReturnSaleServiceId = pharmacyReturnSaleServiceId;
	}

	public Integer getOtRentServiceId() {
		return otRentServiceId;
	}

	public void setOtRentServiceId(Integer otRentServiceId) {
		this.otRentServiceId = otRentServiceId;
	}

	public Integer getSurgeonChargesServiceId() {
		return surgeonChargesServiceId;
	}

	public void setSurgeonChargesServiceId(Integer surgeonChargesServiceId) {
		this.surgeonChargesServiceId = surgeonChargesServiceId;
	}

	public Character getIsNursingAssessment() {
		return isNursingAssessment;
	}

	public void setIsNursingAssessment(Character isNursingAssessment) {
		this.isNursingAssessment = isNursingAssessment;
	}

	public Integer getErSpecialityId() {
		return erSpecialityId;
	}

	public void setErSpecialityId(Integer erSpecialityId) {
		this.erSpecialityId = erSpecialityId;
	}

	public Character getIsRenderedServiceRateEditable() {
		return isRenderedServiceRateEditable;
	}

	public void setIsRenderedServiceRateEditable(Character isRenderedServiceRateEditable) {
		this.isRenderedServiceRateEditable = isRenderedServiceRateEditable;
	}

	public Integer getAdmissionChargesServiceId() {
		return admissionChargesServiceId;
	}

	public void setAdmissionChargesServiceId(Integer admissionChargesServiceId) {
		this.admissionChargesServiceId = admissionChargesServiceId;
	}

	public Character getIsSmartCardRequired() {
		return isSmartCardRequired;
	}

	public void setIsSmartCardRequired(Character isSmartCardRequired) {
		this.isSmartCardRequired = isSmartCardRequired;
	}

	public Character getIsDigitalSignatureRequired() {
		return isDigitalSignatureRequired;
	}

	public void setIsDigitalSignatureRequired(Character isDigitalSignatureRequired) {
		this.isDigitalSignatureRequired = isDigitalSignatureRequired;
	}

	public Character getIsPinPadRequired() {
		return isPinPadRequired;
	}

	public void setIsPinPadRequired(Character isPinPadRequired) {
		this.isPinPadRequired = isPinPadRequired;
	}

	public Character getIsShowPatientByBed() {
		return isShowPatientByBed;
	}

	public void setIsShowPatientByBed(Character isShowPatientByBed) {
		this.isShowPatientByBed = isShowPatientByBed;
	}

	public String getGroupPracticeNo() {
		return groupPracticeNo;
	}

	public void setGroupPracticeNo(String groupPracticeNo) {
		this.groupPracticeNo = groupPracticeNo;
	}

	public String getGroupPracticeName() {
		return groupPracticeName;
	}

	public void setGroupPracticeName(String groupPracticeName) {
		this.groupPracticeName = groupPracticeName;
	}

	public String getPracticeNo() {
		return practiceNo;
	}

	public void setPracticeNo(String practiceNo) {
		this.practiceNo = practiceNo;
	}

	public String getStartDateFormat() {
		return startDateFormat;
	}

	public void setStartDateFormat(String startDateFormat) {
		this.startDateFormat = startDateFormat;
	}

	public Character getIsCorporateEmpVerificationReq() {
		return isCorporateEmpVerificationReq;
	}

	public void setIsCorporateEmpVerificationReq(Character isCorporateEmpVerificationReq) {
		this.isCorporateEmpVerificationReq = isCorporateEmpVerificationReq;
	}

	public Character getIsAdviceTabVisible() {
		return isAdviceTabVisible;
	}

	public void setIsAdviceTabVisible(Character isAdviceTabVisible) {
		this.isAdviceTabVisible = isAdviceTabVisible;
	}

	public Character getIsPACSRequired() {
		return isPACSRequired;
	}

	public void setIsPACSRequired(Character isPACSRequired) {
		this.isPACSRequired = isPACSRequired;
	}

	public Integer getObgynSpecialityId() {
		return obgynSpecialityId;
	}

	public void setObgynSpecialityId(Integer obgynSpecialityId) {
		this.obgynSpecialityId = obgynSpecialityId;
	}

	public Integer getHemodialysisSpecialityId() {
		return hemodialysisSpecialityId;
	}

	public void setHemodialysisSpecialityId(Integer hemodialysisSpecialityId) {
		this.hemodialysisSpecialityId = hemodialysisSpecialityId;
	}

	public Character getIsConsolidatedBill() {
		return isConsolidatedBill;
	}

	public void setIsConsolidatedBill(Character isConsolidatedBill) {
		this.isConsolidatedBill = isConsolidatedBill;
	}

	public Integer getAssetMaintenanceSpecialityId() {
		return assetMaintenanceSpecialityId;
	}

	public void setAssetMaintenanceSpecialityId(Integer assetMaintenanceSpecialityId) {
		this.assetMaintenanceSpecialityId = assetMaintenanceSpecialityId;
	}

	public Integer getPhysiotherapySpecialityId() {
		return physiotherapySpecialityId;
	}

	public void setPhysiotherapySpecialityId(Integer physiotherapySpecialityId) {
		this.physiotherapySpecialityId = physiotherapySpecialityId;
	}

	public String getMorningFromTime() {
		return morningFromTime;
	}

	public void setMorningFromTime(String morningFromTime) {
		this.morningFromTime = morningFromTime;
	}

	public String getMorningToTime() {
		return morningToTime;
	}

	public void setMorningToTime(String morningToTime) {
		this.morningToTime = morningToTime;
	}

	public String getEveningFromTime() {
		return eveningFromTime;
	}

	public void setEveningFromTime(String eveningFromTime) {
		this.eveningFromTime = eveningFromTime;
	}

	public String getEveningToTime() {
		return eveningToTime;
	}

	public void setEveningToTime(String eveningToTime) {
		this.eveningToTime = eveningToTime;
	}

	public Integer getAdditionalOtrentService() {
		return additionalOtrentService;
	}

	public void setAdditionalOtrentService(Integer additionalOtrentService) {
		this.additionalOtrentService = additionalOtrentService;
	}

	public Character getAllowOtherDoctorToConsult() {
		return allowOtherDoctorToConsult;
	}

	public void setAllowOtherDoctorToConsult(Character allowOtherDoctorToConsult) {
		this.allowOtherDoctorToConsult = allowOtherDoctorToConsult;
	}

	public Character getDiagnosisRequire() {
		return diagnosisRequire;
	}

	public void setDiagnosisRequire(Character diagnosisRequire) {
		this.diagnosisRequire = diagnosisRequire;
	}

	public Character getSkipRISReportEntry() {
		return skipRISReportEntry;
	}

	public void setSkipRISReportEntry(Character skipRISReportEntry) {
		this.skipRISReportEntry = skipRISReportEntry;
	}

	public Boolean getIsPharmacologicalValidationSkipIp() {
		return isPharmacologicalValidationSkipIp;
	}

	public void setIsPharmacologicalValidationSkipIp(Boolean isPharmacologicalValidationSkipIp) {
		this.isPharmacologicalValidationSkipIp = isPharmacologicalValidationSkipIp;
	}

	public char getIsRateVisibleOnCoversheet() {
		return isRateVisibleOnCoversheet;
	}

	public void setIsRateVisibleOnCoversheet(char isRateVisibleOnCoversheet) {
		this.isRateVisibleOnCoversheet = isRateVisibleOnCoversheet;
	}

	public String getPharmacyServiceName() {
		return pharmacyServiceName;
	}

	public void setPharmacyServiceName(String pharmacyServiceName) {
		this.pharmacyServiceName = pharmacyServiceName;
	}

	public String getOtRentServiceName() {
		return otRentServiceName;
	}

	public void setOtRentServiceName(String otRentServiceName) {
		this.otRentServiceName = otRentServiceName;
	}

	public String getSurgeonChargesServiceName() {
		return surgeonChargesServiceName;
	}

	public void setSurgeonChargesServiceName(String surgeonChargesServiceName) {
		this.surgeonChargesServiceName = surgeonChargesServiceName;
	}

	public String getSurchargeServiceName() {
		return surchargeServiceName;
	}

	public void setSurchargeServiceName(String surchargeServiceName) {
		this.surchargeServiceName = surchargeServiceName;
	}

	public String getAdmissionChargesServiceName() {
		return admissionChargesServiceName;
	}

	public void setAdmissionChargesServiceName(String admissionChargesServiceName) {
		this.admissionChargesServiceName = admissionChargesServiceName;
	}

	public String getAdditionalOtrentServiceName() {
		return additionalOtrentServiceName;
	}

	public void setAdditionalOtrentServiceName(String additionalOtrentServiceName) {
		this.additionalOtrentServiceName = additionalOtrentServiceName;
	}

	public String getPharmacyReturnSaleName() {
		return pharmacyReturnSaleName;
	}

	public void setPharmacyReturnSaleName(String pharmacyReturnSaleName) {
		this.pharmacyReturnSaleName = pharmacyReturnSaleName;
	}

	public char getIsSendMailOnMailPrescription() {
		return isSendMailOnMailPrescription;
	}

	public void setIsSendMailOnMailPrescription(char isSendMailOnMailPrescription) {
		this.isSendMailOnMailPrescription = isSendMailOnMailPrescription;
	}

	public char getIsVitalQualifierVisible() {
		return isVitalQualifierVisible;
	}

	public void setIsVitalQualifierVisible(char isVitalQualifierVisible) {
		this.isVitalQualifierVisible = isVitalQualifierVisible;
	}

	public char getIsAdvisedDateVisible() {
		return isAdvisedDateVisible;
	}

	public void setIsAdvisedDateVisible(char isAdvisedDateVisible) {
		this.isAdvisedDateVisible = isAdvisedDateVisible;
	}

	public Integer getDoctorSlipTDS() {
		return doctorSlipTDS;
	}

	public void setDoctorSlipTDS(Integer doctorSlipTDS) {
		this.doctorSlipTDS = doctorSlipTDS;
	}

	public Integer getBiomedicalsChargesServiceId() {
		return biomedicalsChargesServiceId;
	}

	public void setBiomedicalsChargesServiceId(Integer biomedicalsChargesServiceId) {
		this.biomedicalsChargesServiceId = biomedicalsChargesServiceId;
	}

	public Integer getHospitalChargesServiceId() {
		return hospitalChargesServiceId;
	}

	public void setHospitalChargesServiceId(Integer hospitalChargesServiceId) {
		this.hospitalChargesServiceId = hospitalChargesServiceId;
	}

	public Integer getMrdChargesServiceId() {
		return mrdChargesServiceId;
	}

	public void setMrdChargesServiceId(Integer mrdChargesServiceId) {
		this.mrdChargesServiceId = mrdChargesServiceId;
	}

	public String getBiomedicalsChargesServiceName() {
		return biomedicalsChargesServiceName;
	}

	public void setBiomedicalsChargesServiceName(String biomedicalsChargesServiceName) {
		this.biomedicalsChargesServiceName = biomedicalsChargesServiceName;
	}

	public String getHospitalChargesServiceName() {
		return hospitalChargesServiceName;
	}

	public void setHospitalChargesServiceName(String hospitalChargesServiceName) {
		this.hospitalChargesServiceName = hospitalChargesServiceName;
	}

	public String getMrdChargesServiceName() {
		return mrdChargesServiceName;
	}

	public void setMrdChargesServiceName(String mrdChargesServiceName) {
		this.mrdChargesServiceName = mrdChargesServiceName;
	}

	public Character getSkipTransferAcceptance() {
		return skipTransferAcceptance;
	}

	public void setSkipTransferAcceptance(Character skipTransferAcceptance) {
		this.skipTransferAcceptance = skipTransferAcceptance;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Character getIsPaymentGatewayReqForWebApp() {
		return isPaymentGatewayReqForWebApp;
	}

	public void setIsPaymentGatewayReqForWebApp(Character isPaymentGatewayReqForWebApp) {
		this.isPaymentGatewayReqForWebApp = isPaymentGatewayReqForWebApp;
	}

	public Character getIsRisPatientDetailsOnFirstPage() {
		return isRisPatientDetailsOnFirstPage;
	}

	public void setIsRisPatientDetailsOnFirstPage(Character isRisPatientDetailsOnFirstPage) {
		this.isRisPatientDetailsOnFirstPage = isRisPatientDetailsOnFirstPage;
	}

	public String getRazorPayKeyId() {
		return razorPayKeyId;
	}

	public void setRazorPayKeyId(String razorPayKeyId) {
		this.razorPayKeyId = razorPayKeyId;
	}

	public String getRazorPaySecretKey() {
		return razorPaySecretKey;
	}

	public void setRazorPaySecretKey(String razorPaySecretKey) {
		this.razorPaySecretKey = razorPaySecretKey;
	}

	/**
	 * Nulls out fields that must never leave the server in a client-facing
	 * response, regardless of caller. See docs/DECISION_NEEDED_UnitApplicationConfigCredentialDisclosure.md
	 * and docs/DECISION_NEEDED_CookieAppConfigCredentialEcho.md. Call this at
	 * every point this DTO is serialized directly to an HTTP response or a
	 * client-bound cookie -- never inside internal business logic (e.g.
	 * RazorPaymentController's real payment-order creation still needs the
	 * unstripped values, fetched directly, not through a client response).
	 */
	public void stripClientFacingSecrets() {
		this.password = null;
		this.razorPayKeyId = null;
		this.razorPaySecretKey = null;
	}

	public Character getIsReportAddIntoDischargeSummary() {
		return isReportAddIntoDischargeSummary;
	}

	public void setIsReportAddIntoDischargeSummary(Character isReportAddIntoDischargeSummary) {
		this.isReportAddIntoDischargeSummary = isReportAddIntoDischargeSummary;
	}

	public boolean getOpStatNotRequired() {
		return opStatNotRequired;
	}

	public void setOpStatNotRequired(boolean opStatNotRequired) {
		this.opStatNotRequired = opStatNotRequired;
	}

	public boolean getIpStatNotRequired() {
		return ipStatNotRequired;
	}

	public void setIpStatNotRequired(boolean ipStatNotRequired) {
		this.ipStatNotRequired = ipStatNotRequired;
	}

	public Integer getAdmissionCancellationTime() {
		return admissionCancellationTime;
	}

	public void setAdmissionCancellationTime(Integer admissionCancellationTime) {
		this.admissionCancellationTime = admissionCancellationTime;
	}

	public Integer getWebAppUserId() {
		return webAppUserId;
	}

	public void setWebAppUserId(Integer webAppUserId) {
		this.webAppUserId = webAppUserId;
	}

	public Integer getCashAmountValidation() {
		return cashAmountValidation;
	}

	public void setCashAmountValidation(Integer cashAmountValidation) {
		this.cashAmountValidation = cashAmountValidation;
	}

	public String getRazorPayCallBackUrl() {
		return razorPayCallBackUrl;
	}

	public void setRazorPayCallBackUrl(String razorPayCallBackUrl) {
		this.razorPayCallBackUrl = razorPayCallBackUrl;
	}

	public Integer getMailProviderId() {
		return mailProviderId;
	}

	public void setMailProviderId(Integer mailProviderId) {
		this.mailProviderId = mailProviderId;
	}

	public String getMailCredential() {
		return mailCredential;
	}

	public void setMailCredential(String mailCredential) {
		this.mailCredential = mailCredential;
	}

	public Integer getCashAmountLimit() {
		return cashAmountLimit;
	}

	public void setCashAmountLimit(Integer cashAmountLimit) {
		this.cashAmountLimit = cashAmountLimit;
	}

	public boolean getVaccinationChartHide() {
		return vaccinationChartHide;
	}

	public void setVaccinationChartHide(boolean vaccinationChartHide) {
		this.vaccinationChartHide = vaccinationChartHide;
	}

	public Character getBlockReportPrint() {
		return blockReportPrint;
	}

	public void setBlockReportPrint(Character blockReportPrint) {
		this.blockReportPrint = blockReportPrint;
	}

	public Integer getPayGatewayProviderId() {
		return payGatewayProviderId;
	}

	public void setPayGatewayProviderId(Integer payGatewayProviderId) {
		this.payGatewayProviderId = payGatewayProviderId;
	}

	public Integer getWhatsAppOption() {
		return whatsAppOption;
	}

	public void setWhatsAppOption(Integer whatsAppOption) {
		this.whatsAppOption = whatsAppOption;
	}

	public Character getEmailValidationRequired() {
		return emailValidationRequired;
	}

	public boolean isSelfPaidConsultAlert() {
		return selfPaidConsultAlert;
	}

	public void setSelfPaidConsultAlert(boolean selfPaidConsultAlert) {
		this.selfPaidConsultAlert = selfPaidConsultAlert;
	}

	public void setEmailValidationRequired(Character emailValidationRequired) {
		this.emailValidationRequired = emailValidationRequired;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public Boolean getDepositTypeWiseRequired() {
		return depositTypeWiseRequired;
	}

	public void setDepositTypeWiseRequired(Boolean depositTypeWiseRequired) {
		this.depositTypeWiseRequired = depositTypeWiseRequired;
	}

	public Character getNeuronPlus() {
		return neuronPlus;
	}

	public void setNeuronPlus(Character neuronPlus) {
		this.neuronPlus = neuronPlus;
	}

	public String getRegisteredPinPadDevice() {
		return registeredPinPadDevice;
	}

	public void setRegisteredPinPadDevice(String registeredPinPadDevice) {
		this.registeredPinPadDevice = registeredPinPadDevice;
	}

	public Character getCimsOption() {
		return cimsOption;
	}

	public void setCimsOption(Character cimsOption) {
		this.cimsOption = cimsOption;
	}

	// public Boolean getatCopRequired() {
	// return atCopRequired;
	// }
	//
	// public void setatCopRequired(Boolean atCopRequired) {
	// this.atCopRequired = atCopRequired;
	// }
	public String getPdfGenerationUrlMis() {
		return pdfGenerationUrlMis;
	}

	public void setPdfGenerationUrlMis(String pdfGenerationUrlMis) {
		this.pdfGenerationUrlMis = pdfGenerationUrlMis;
	}

	public boolean getServiceWiseDoctorListReq() {
		return serviceWiseDoctorListReq;
	}

	public void setServiceWiseDoctorListReq(boolean serviceWiseDoctorListReq) {
		this.serviceWiseDoctorListReq = serviceWiseDoctorListReq;
	}

	public Integer getBackDateAdmissionDays() {
		return backDateAdmissionDays;
	}

	public void setBackDateAdmissionDays(Integer backDateAdmissionDays) {
		this.backDateAdmissionDays = backDateAdmissionDays;
	}

	public Character getAppointmentUniqueIdSearch() {
		return appointmentUniqueIdSearch;
	}

	public void setAppointmentUniqueIdSearch(Character appointmentUniqueIdSearch) {
		this.appointmentUniqueIdSearch = appointmentUniqueIdSearch;

	}

	public boolean getGradeWiseSeviceApplicable() {
		return gradeWiseSeviceApplicable;
	}

	public void setGradeWiseSeviceApplicable(boolean gradeWiseSeviceApplicable) {
		this.gradeWiseSeviceApplicable = gradeWiseSeviceApplicable;
	}

	public Boolean getCouponSystemApplicable() {
		return couponSystemApplicable;
	}

	public void setCouponSystemApplicable(Boolean couponSystemApplicable) {
		this.couponSystemApplicable = couponSystemApplicable;
	}

	public Character getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(Character tokenNumber) {
		this.tokenNumber = tokenNumber;
	}

	public Character getMobileValidationRequired() {
		return mobileValidationRequired;
	}

	public void setMobileValidationRequired(Character mobileValidationRequired) {
		this.mobileValidationRequired = mobileValidationRequired;
	}

	public Character getMandatoryScan() {
		return mandatoryScan;
	}

	public void setMandatoryScan(Character mandatoryScan) {
		this.mandatoryScan = mandatoryScan;
	}

	public Boolean getExceededBillAmountSettlement() {
		return exceededBillAmountSettlement;
	}

	public void setExceededBillAmountSettlement(Boolean exceededBillAmountSettlement) {
		this.exceededBillAmountSettlement = exceededBillAmountSettlement;
	}

	public Boolean getRunTimeRateChangeUsingTarrif() {
		return runTimeRateChangeUsingTarrif;
	}

	public void setRunTimeRateChangeUsingTarrif(Boolean runTimeRateChangeUsingTarrif) {
		this.runTimeRateChangeUsingTarrif = runTimeRateChangeUsingTarrif;
	}

	public Character getQmsPrnPrint() {
		return qmsPrnPrint;
	}

	public void setQmsPrnPrint(Character qmsPrnPrint) {
		this.qmsPrnPrint = qmsPrnPrint;
	}

	public String getQmsPrnPrintFormat() {
		return qmsPrnPrintFormat;
	}

	public void setQmsPrnPrintFormat(String qmsPrnPrintFormat) {
		this.qmsPrnPrintFormat = qmsPrnPrintFormat;
	}

	public Integer getDischargeSummaryDataViewOptionId() {
		return dischargeSummaryDataViewOptionId;
	}

	public void setDischargeSummaryDataViewOptionId(Integer dischargeSummaryDataViewOptionId) {
		this.dischargeSummaryDataViewOptionId = dischargeSummaryDataViewOptionId;
	}

	public Integer getAmbulanceChargesServiceId() {
		return ambulanceChargesServiceId;
	}

	public void setAmbulanceChargesServiceId(Integer ambulanceChargesServiceId) {
		this.ambulanceChargesServiceId = ambulanceChargesServiceId;
	}

	public boolean getIsEmrRequired() {
		return isEmrRequired;
	}

	public void setEmrRequired(boolean isEmrRequired) {
		this.isEmrRequired = isEmrRequired;
	}

	public String getAmbulanceChargesService() {
		return ambulanceChargesService;
	}

	public void setAmbulanceChargesService(String ambulanceChargesService) {
		this.ambulanceChargesService = ambulanceChargesService;
	}

	public String getTableHeaderColor() {
		return tableHeaderColor;
	}

	public void setTableHeaderColor(String tableHeaderColor) {
		this.tableHeaderColor = tableHeaderColor;
	}

	public Boolean getGstForBillingBedCategory() {
		return gstForBillingBedCategory;
	}

	public void setGstForBillingBedCategory(Boolean gstForBillingBedCategory) {
		this.gstForBillingBedCategory = gstForBillingBedCategory;
	}

	public Character getRoa() {
		return roa;
	}

	public void setRoa(Character roa) {
		this.roa = roa;
	}

	public Character getDosage() {
		return dosage;
	}

	public void setDosage(Character dosage) {
		this.dosage = dosage;
	}

	public String getLocalStorageJson() {
		return localStorageJson;
	}

	public void setLocalStorageJson(String localStorageJson) {
		this.localStorageJson = localStorageJson;
	}

	public Integer getBucketFlag() {
		return bucketFlag;
	}

	public void setBucketFlag(Integer bucketFlag) {
		this.bucketFlag = bucketFlag;
	}

	public Integer getSmsType() {
		return smsType;
	}

	public void setSmsType(Integer smsType) {
		this.smsType = smsType;
	}

	public String getSmsTypejson() {
		return smsTypejson;
	}

	public void setSmsTypejson(String smsTypejson) {
		this.smsTypejson = smsTypejson;
	}

	public Character getInvoiceCreationInsurance() {
		return invoiceCreationInsurance;
	}

	public void setInvoiceCreationInsurance(Character invoiceCreationInsurance) {
		this.invoiceCreationInsurance = invoiceCreationInsurance;
	}

	public Character getInvoiceCreationCorporate() {
		return invoiceCreationCorporate;
	}

	public void setInvoiceCreationCorporate(Character invoiceCreationCorporate) {
		this.invoiceCreationCorporate = invoiceCreationCorporate;
	}

	public Character getIsAiButtonConfigc() {
		return isAiButtonConfigc;
	}

	public void setIsAiButtonConfigc(Character isAiButtonConfigc) {
		this.isAiButtonConfigc = isAiButtonConfigc;
	}

	public Boolean getDuplicateTab() {
		return duplicateTab;
	}

	public void setDuplicateTab(Boolean duplicateTab) {
		this.duplicateTab = duplicateTab;
	}

	public Character getAyushmanBharatIntegration() {
		return ayushmanBharatIntegration;
	}

	public void setAyushmanBharatIntegration(Character ayushmanBharatIntegration) {
		this.ayushmanBharatIntegration = ayushmanBharatIntegration;
	}

	public Character getIspdfPasswordRequire() {
		return ispdfPasswordRequire;
	}

	public void setIspdfPasswordRequire(Character ispdfPasswordRequire) {
		this.ispdfPasswordRequire = ispdfPasswordRequire;
	}

	public String getTallyIp() {
		return tallyIp;
	}

	public void setTallyIp(String tallyIp) {
		this.tallyIp = tallyIp;
	}

	public String getTallyPortNo() {
		return tallyPortNo;
	}

	public void setTallyPortNo(String tallyPortNo) {
		this.tallyPortNo = tallyPortNo;
	}

	public Character getIsTallyLocalHost() {
		return isTallyLocalHost;
	}

	public void setIsTallyLocalHost(Character isTallyLocalHost) {
		this.isTallyLocalHost = isTallyLocalHost;
	}

	public Integer getMobileDigit() {
		return mobileDigit;
	}

	public void setMobileDigit(Integer mobileDigit) {
		this.mobileDigit = mobileDigit;
	}

    public Integer getAdminServiceMasterId() {
        return adminServiceMasterId;
    }

    public void setAdminServiceMasterId(Integer adminServiceMasterId) {
        this.adminServiceMasterId = adminServiceMasterId;
    }

	public Character getOtherOrgLogoRequired() {
		return otherOrgLogoRequired;
	}

	public void setOtherOrgLogoRequired(Character otherOrgLogoRequired) {
		this.otherOrgLogoRequired = otherOrgLogoRequired;
	}

	public Character getDrugTodrugCheck() {
		return drugTodrugCheck;
	}

	public void setDrugTodrugCheck(Character drugTodrugCheck) {
		this.drugTodrugCheck = drugTodrugCheck;
	}

	public BigDecimal getMaxInputAmount() {
		return maxInputAmount;
	}

	public void setMaxInputAmount(BigDecimal maxInputAmount) {
		this.maxInputAmount = maxInputAmount;
	}

	public Character getIsAiDoctorEnabled() {
		return isAiDoctorEnabled;
	}

	public void setIsAiDoctorEnabled(Character isAiDoctorEnabled) {
		this.isAiDoctorEnabled = isAiDoctorEnabled;
	}
}
