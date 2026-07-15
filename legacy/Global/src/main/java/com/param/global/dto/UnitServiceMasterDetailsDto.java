package com.param.global.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.param.global.billing.dto.TContractServiceDetailsDto;


public class UnitServiceMasterDetailsDto {
	private int unitServiceMasterDetailsId;

	private Integer serviceMasterId;

	private char isOutsource;
	
	private char isOutsourceJob;

	private char isPackage;

	private char isRateEditable;

	private BigDecimal rateEditbleMinValue;

	private BigDecimal rateEditableMaxValue;

	private char isTaxApplicable;

	private char isRefDoctorShare;

	private char isDiscount;

	private BigDecimal discountValue;

	private Integer gstTypeId;

	private Integer otcGstTypeId;

	private Integer organizationId;

	private Integer unitId;

	private Integer createdBy;

	private Long createdDate;

	private char status;

	private Integer updatedBy;

	private Long updatedDate;

	private Integer offset;

	private Integer recordPerPage;

	private String serviceStandardName;

	private String specialityName;

	private String subSpecialityName;

	private Integer specialityId;

	private Integer subSpecialityId;

	private Character isPanel;

	private String serviceStandardCode;
	
	private Character isProcedure;

	private Character isDocReq;

	private Character isBedSide;

	private Character isPoc;

	private Character isPeriodicity;
	
	private Character isQuantityEditable;
	
	private Character isAutoRender;
	
	private Integer id;
	
	private String name;

	private Character isAppointable; 
	
	private Integer gender;
	
	private BigDecimal refShareAmt;
	
	private Integer procedureTypeId;
	
	private String procedureTypeName;
	
	private Character isPacsReq;
	
	private Character isReviewApplicable;
	
	private int reviewTime;
	
	List<Integer> procedureTypeIdList;

	private Double refSharePer;

	private Integer testId;
	
	private Character billOfMaterial;
	
	private Double docRefPer;

	private Character isCyclicFlag;
	
	private Integer patientId;
	
	private Integer packageConsumptionMasterId;
	
	private Integer packageId;
	
	private Integer orderId;
	
	private Integer orderDetailsId;
	
	private Integer balancePackageQuantity;
	
	private String billNumber;
	
	private String billDate;
	
	//private Integer availableQty;
	private BigInteger availableQty;
	
	private Double serviceAmount;
	
    private Integer taxId;
	
	private Double taxPercentage;
	
	private Integer packageConsumptionServiceDetailId;
	
	private Integer packageEoDetailId;
	
	private Integer encounterId;
	
	private List<Integer> orderList;
	
	private String packageValidityToDate;
	
	private Integer packageDuration;
	
	private Integer doctorId;
	
	private Character deptWiseAppointment;
	
	private Integer admissionId;
	
	private Integer visitTypeId;
	
	private double costOfService;
	
	private double basePrice;
	
	private Integer serviceBasePriceId;
	
	private Integer surgeryGradeId;

	private String surgeryGrade;

	private List<TContractServiceDetailsDto> tContractServiceDetailsDtoList;
	
	private Character sampleCollection;
	
	private Integer contractGradeMasterId;
	
	private Character isDocSharePerWise;
	
	private Character isRefSharePerWise;
	
	private String fromDate;
	
	private String toDate;
	
	private String patientDetails;
	
	private String uhid;
	
	private Character labAutoMailExclusion;
	
	private Character preRequisite;
	
	private String preRequisiteDetails;
	
	private String searchKeyword;
	
	private String specialityNameList;

	private String golbalSpeciality;

	private String golbalSubSpeciality;
	
	private String subSpecialityNameList;
	
	private List<UnitServiceMasterDetailsDto> globalServiceMapperList;
	
	private Integer billingBedCategoryId;
	
	private String billingBedDesc;
	
	private Double rate;
	
	private Integer tariffId;
	
	private Integer paymentEntitlementId;
	
	private Integer patientTypeId;
	
	private Character ventilatorRequiered;

	private Character isApplicableForContract;

	private Character isFormFApplicable;

	private Character isAdminService;

	public Character getVentilatorRequiered() {
		return ventilatorRequiered;
	}

	public void setVentilatorRequiered(Character ventilatorRequiered) {
		this.ventilatorRequiered = ventilatorRequiered;
	}

	public Character getIsRefSharePerWise() {
		return isRefSharePerWise;
	}

	public void setIsRefSharePerWise(Character isRefSharePerWise) {
		this.isRefSharePerWise = isRefSharePerWise;
	}

	public Character getIsDocSharePerWise() {
		return isDocSharePerWise;
	}

	public void setIsDocSharePerWise(Character isDocSharePerWise) {
		this.isDocSharePerWise = isDocSharePerWise;
	}
	
	public List<TContractServiceDetailsDto> gettContractServiceDetailsDtoList() {
		return tContractServiceDetailsDtoList;
	}

	public void settContractServiceDetailsDtoList(List<TContractServiceDetailsDto> tContractServiceDetailsDtoList) {
		this.tContractServiceDetailsDtoList = tContractServiceDetailsDtoList;
	}

	public Integer getServiceBasePriceId() {
		return serviceBasePriceId;
	}

	public void setServiceBasePriceId(Integer serviceBasePriceId) {
		this.serviceBasePriceId = serviceBasePriceId;
	}

	public double getCostOfService() {
		return costOfService;
	}

	public void setCostOfService(double costOfService) {
		this.costOfService = costOfService;
	}

	public double getBasePrice() {
		return basePrice;
	}
	

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Character getDeptWiseAppointment() {
		return deptWiseAppointment;
	}

	public void setDeptWiseAppointment(Character deptWiseAppointment) {
		this.deptWiseAppointment = deptWiseAppointment;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Double getRefSharePer() {
		return refSharePer;
	}

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public void setRefSharePer(Double refSharePer) {
		this.refSharePer = refSharePer;
	}

	public BigDecimal getRefShareAmt() {
		return refShareAmt;
	}

	public void setRefShareAmt(BigDecimal refShareAmt) {
		this.refShareAmt = refShareAmt;
	}

	public Character getIsAppointable() {
		return isAppointable;
	}

	public void setIsAppointable(Character isAppointable) {
		this.isAppointable = isAppointable;
	}

	public int getUnitServiceMasterDetailsId() {
		return unitServiceMasterDetailsId;
	}

	public void setUnitServiceMasterDetailsId(int unitServiceMasterDetailsId) {
		this.unitServiceMasterDetailsId = unitServiceMasterDetailsId;
	}

	public Integer getServiceMasterId() {
		return serviceMasterId;
	}

	public void setServiceMasterId(Integer serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}

	public char getIsOutsource() {
		return isOutsource;
	}

	public void setIsOutsource(char isOutsource) {
		this.isOutsource = isOutsource;
	}
	
	public char getIsOutsourceJob() {
		return isOutsourceJob;
	}

	public void setIsOutsourceJob(char isOutsourceJob) {
		this.isOutsourceJob = isOutsourceJob;
	}

	public char getIsPackage() {
		return isPackage;
	}

	public void setIsPackage(char isPackage) {
		this.isPackage = (isPackage == '\u0000') ? 'I' : isPackage;
	}

	public char getIsRateEditable() {
		return isRateEditable;
	}

	public void setIsRateEditable(char isRateEditable) {
		this.isRateEditable = isRateEditable;
	}

	public BigDecimal getRateEditbleMinValue() {
		return rateEditbleMinValue;
	}

	public void setRateEditbleMinValue(BigDecimal rateEditbleMinValue) {
		this.rateEditbleMinValue = rateEditbleMinValue;
	}

	public BigDecimal getRateEditableMaxValue() {
		return rateEditableMaxValue;
	}

	public void setRateEditableMaxValue(BigDecimal rateEditableMaxValue) {
		this.rateEditableMaxValue = rateEditableMaxValue;
	}

	public char getIsTaxApplicable() {
		return isTaxApplicable;
	}

	public void setIsTaxApplicable(char isTaxApplicable) {
		this.isTaxApplicable = isTaxApplicable;
	}

	public char getIsRefDoctorShare() {
		return isRefDoctorShare;
	}

	public void setIsRefDoctorShare(char isRefDoctorShare) {
		this.isRefDoctorShare = isRefDoctorShare;
	}

	public char getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(char isDiscount) {
		this.isDiscount = isDiscount;
	}

	public Integer getGstTypeId() {
		return gstTypeId;
	}

	public void setGstTypeId(Integer gstTypeId) {
		this.gstTypeId = gstTypeId;
	}

	public Integer getOtcGstTypeId() {
		return otcGstTypeId;
	}

	public void setOtcGstTypeId(Integer otcGstTypeId) {
		this.otcGstTypeId = otcGstTypeId;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public String getServiceStandardName() {
		return serviceStandardName;
	}

	public void setServiceStandardName(String serviceStandardName) {
		this.serviceStandardName = serviceStandardName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getSubSpecialityName() {
		return subSpecialityName;
	}

	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}

	public BigDecimal getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
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

	public Character getIsPanel() {
		return isPanel;
	}

	public void setIsPanel(Character isPanel) {
		this.isPanel = isPanel;
	}

	public String getServiceStandardCode() {
		return serviceStandardCode;
	}

	public void setServiceStandardCode(String serviceStandardCode) {
		this.serviceStandardCode = serviceStandardCode;
	}

	public Character getIsProcedure() {
		return isProcedure;
	}

	public void setIsProcedure(Character isProcedure) {
		this.isProcedure = isProcedure;
	}

	public Character getIsDocReq() {
		return isDocReq;
	}

	public void setIsDocReq(Character isDocReq) {
		this.isDocReq = isDocReq;
	}

	public Character getIsBedSide() {
		return isBedSide;
	}

	public void setIsBedSide(Character isBedSide) {
		this.isBedSide = isBedSide;
	}

	public Character getIsPoc() {
		return isPoc;
	}

	public void setIsPoc(Character isPoc) {
		this.isPoc = isPoc;
	}

	public Character getIsPeriodicity() {
		return isPeriodicity;
	}

	public void setIsPeriodicity(Character isPeriodicity) {
		this.isPeriodicity = isPeriodicity;
	}

	public Character getIsQuantityEditable() {
		return isQuantityEditable;
	}

	public void setIsQuantityEditable(Character isQuantityEditable) {
		this.isQuantityEditable = isQuantityEditable;
	}

	public Character getIsAutoRender() {
		return isAutoRender;
	}

	public void setIsAutoRender(Character isAutoRender) {
		this.isAutoRender = isAutoRender;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}

	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}

	public Character getIsPacsReq() {
		return isPacsReq;
	}

	public void setIsPacsReq(Character isPacsReq) {
		this.isPacsReq = isPacsReq;
	}

	public String getProcedureTypeName() {
		return procedureTypeName;
	}

	public void setProcedureTypeName(String procedureTypeName) {
		this.procedureTypeName = procedureTypeName;
	}

	public List<Integer> getProcedureTypeIdList() {
		return procedureTypeIdList;
	}

	public void setProcedureTypeIdList(List<Integer> procedureTypeIdList) {
		this.procedureTypeIdList = procedureTypeIdList;
	}

	public Character getIsReviewApplicable() {
		return isReviewApplicable;
	}

	public void setIsReviewApplicable(Character isReviewApplicable) {
		this.isReviewApplicable = (isReviewApplicable == null) ? 'N' : isReviewApplicable;
	}

	public int getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(int reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Character getBillOfMaterial() {
		return billOfMaterial;
	}

	public void setBillOfMaterial(Character billOfMaterial) {
		this.billOfMaterial = billOfMaterial;
	}

	public Double getDocRefPer() {
		return docRefPer;
	}

	public void setDocRefPer(Double docRefPer) {
		this.docRefPer = docRefPer;
	}

	public Character getIsCyclicFlag() {
		return isCyclicFlag;
	}

	public void setIsCyclicFlag(Character isCyclicFlag) {
		this.isCyclicFlag = (isCyclicFlag == null) ? 'N' : isCyclicFlag;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}

	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getBalancePackageQuantity() {
		return balancePackageQuantity;
	}

	public void setBalancePackageQuantity(Integer balancePackageQuantity) {
		this.balancePackageQuantity = balancePackageQuantity;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}


	public Double getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(Double serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Integer getPackageConsumptionServiceDetailId() {
		return packageConsumptionServiceDetailId;
	}

	public void setPackageConsumptionServiceDetailId(Integer packageConsumptionServiceDetailId) {
		this.packageConsumptionServiceDetailId = packageConsumptionServiceDetailId;
	}

	public Integer getPackageEoDetailId() {
		return packageEoDetailId;
	}

	public void setPackageEoDetailId(Integer packageEoDetailId) {
		this.packageEoDetailId = packageEoDetailId;
	}

	public BigInteger getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(BigInteger availableQty) {
		this.availableQty = availableQty;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public List<Integer> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<Integer> orderList) {
		this.orderList = orderList;
	}

	public String getPackageValidityToDate() {
		return packageValidityToDate;
	}

	public void setPackageValidityToDate(String packageValidityToDate) {
		this.packageValidityToDate = packageValidityToDate;
	}

	public Integer getPackageDuration() {
		return packageDuration;
	}

	public void setPackageDuration(Integer packageDuration) {
		this.packageDuration = packageDuration;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Character getSampleCollection() {
		return sampleCollection;
	}

	public void setSampleCollection(Character sampleCollection) {
		this.sampleCollection = sampleCollection;
	}

	public Integer getContractGradeMasterId() {
		return contractGradeMasterId;
	}

	public void setContractGradeMasterId(Integer contractGradeMasterId) {
		this.contractGradeMasterId = contractGradeMasterId;
	}

	public Integer getSurgeryGradeId() {
		return surgeryGradeId;
	}

	public void setSurgeryGradeId(Integer surgeryGradeId) {
		this.surgeryGradeId = surgeryGradeId;
	}

	public String getSurgeryGrade() {
		return surgeryGrade;
	}

	public void setSurgeryGrade(String surgeryGrade) {
		this.surgeryGrade = surgeryGrade;
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

	public String getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(String patientDetails) {
		this.patientDetails = patientDetails;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public Character getLabAutoMailExclusion() {
		return labAutoMailExclusion;
	}

	public void setLabAutoMailExclusion(Character labAutoMailExclusion) {
		this.labAutoMailExclusion = labAutoMailExclusion;
	}

	public Character getPreRequisite() {
		return preRequisite;
	}

	public void setPreRequisite(Character preRequisite) {
		this.preRequisite = preRequisite;
	}

	public String getPreRequisiteDetails() {
		return preRequisiteDetails;
	}

	public void setPreRequisiteDetails(String preRequisiteDetails) {
		this.preRequisiteDetails = preRequisiteDetails;
	}

	public String getSearchKeyword() {
		return searchKeyword;
	}

	public void setSearchKeyword(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}

	public Object[] getSpecialityNameList() {
		return new String[]{specialityNameList};
	}

	public void setSpecialityNameList(String specialityNameList) {
		this.specialityNameList = specialityNameList;
	}

	public Object[] getSubSpecialityNameList() {
		return new String[]{subSpecialityNameList};
	}

	public void setSubSpecialityNameList(String subSpecialityNameList) {
		this.subSpecialityNameList = subSpecialityNameList;
	}

	public List<UnitServiceMasterDetailsDto> getGlobalServiceMapperList() {
		return globalServiceMapperList;
	}

	public void setGlobalServiceMapperList(List<UnitServiceMasterDetailsDto> globalServiceMapperList) {
		this.globalServiceMapperList = globalServiceMapperList;
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


	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public String getGolbalSpeciality() {
		return golbalSpeciality;
	}

	public void setGolbalSpeciality(String golbalSpeciality) {
		this.golbalSpeciality = golbalSpeciality;
	}

	public String getGolbalSubSpeciality() {
		return golbalSubSpeciality;
	}

	public void setGolbalSubSpeciality(String golbalSubSpeciality) {
		this.golbalSubSpeciality = golbalSubSpeciality;
	}

	public Character getIsApplicableForContract() {
		return isApplicableForContract;
	}

	public void setIsApplicableForContract(Character isApplicableForContract) {
		this.isApplicableForContract = isApplicableForContract;
	}

	public Character getIsFormFApplicable() {
		return isFormFApplicable;
	}

	public void setIsFormFApplicable(Character isFormFApplicable) {
		this.isFormFApplicable = isFormFApplicable;
	}

    public Character getIsAdminService() {
        return isAdminService;
    }

    public void setIsAdminService(Character isAdminService) {
        this.isAdminService = isAdminService;
    }
}
