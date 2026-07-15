package com.param.global.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderDetailsByEncounterIdDto {
	private Integer orderDetailsId;
	private Integer orgId;
	private Integer orgUnitId;
	private Integer orderId;
	private Integer quantity;
	private Integer serviceMasterId;
	private String serviceStandardName;
	private String serviceStandardCode;
	private Integer priorityId;
	private Character isOutsourced;
	private String orderDateString;
	private String orderSchDateString;
	private Long orderDate;
	private Long orderSchDate;
	private BigDecimal basePrice;
	private BigDecimal concession;
	private BigDecimal discount;
	private BigDecimal netPay;
	private BigDecimal oldNetPay;
	private Integer specialityId;
	private Integer taxId;
	private String taxName;
	private BigDecimal taxPercentage;
	private BigDecimal taxAmt;
	private BigDecimal selfPayable;
	private BigDecimal creditPayable;
	private Integer payeeId;
	private BigDecimal packageConsumptionAmt;
	private Integer orderSetId;
	private Integer contractId;
	private Integer bedCategoryId;
	private BigDecimal coPayPer;
	private BigDecimal docSharePer;
	private BigDecimal totalAmt;
	private Integer ordCancelBy;
	private String ordCancelRemark;
	private Integer tariffId;
	private Integer billingClassId;
	private Integer bedId;
	private Integer roomId;
	private Integer wardId;
	private Character orderEmergencyFlag;
	private Integer packageId;
	private Integer doctorId;
	private Integer drugId;
	private Integer batchId;
	private Integer oldOrdDtlId;
	private Integer serviceRendered;
	private Integer serviceChargeable;
	private Integer serviceRenderingDeptId;
	private Long ordRenderDatetime;
	private Integer serviceIsBilled;
	private Integer serviceBillId;
	private String ordRemarks;
	private Character ordCancelled;
	private Integer ordCancelReasonId;
	private Long ordCancelDatetime;
	private Character isDrug;
	private Character status;
	private Integer updatedBy;
	private Long updatedDate;
	private Long createdDate;
	private Integer createdBy;
	private Character isRateEditable;
	private BigDecimal minRateEditable;
	private BigDecimal maxRateEditable;
	private Character isTaxApplicable;
	private Character isRefDoctorShare;
	private Character isDoctorRequired;
	private Character isInvestigationOrder;
	private Character isDiscountApplicable;
	private BigDecimal discountValue;
	private Integer gstTypeId;
	private Integer otcGstTypeId;
	private Character isQuantityEditable;
	private String doctorName;
	private String specialityName;
	private String orderNo;
	private Integer serviceSpecialityId;
	private Integer serviceSubSpecialityId;
	private List<OrderDetailsDiscountDto> discountDetailsList;
	private List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList;
	private List<DoctorMasterDto> doctorList;
	private List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList;
	private List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList;
	private Character isPackageService;
	private boolean isPackage;
	private Character isCapService;
	private String subSpecialityName;
	private Integer packageTypeId;
	private Integer packageEitherOrGroupId;
	private Integer saleDetailId;
	private Integer encounterId;
	private Integer appointmentId;
	private BigDecimal taxAmtOld;
	private BigDecimal selfPayableOldC;
	private BigDecimal creditPayableOldC;
	private Integer patientClassId;
	private BigDecimal dummyServiceAmt;
    private BigDecimal patientClassPercentage;
	private Boolean markupStatus;
	private String ordCancelDatetimeString;
	private String userName;
	private String reasonDesc;
	private Character isAutoRender;
	private Integer periodicityServiceOrderId;
	private Character isFromAddservice;
	private Integer saleId;
	private Character isTariffRateApplicable;
	private BigDecimal oldBasePrice;
	private BigDecimal refDocSharePer;
	private Character isConsultationService;
	private Integer doctorSpecialityId;
	private Integer procedureOrderMstId;
	private Character isProcedureService;
	private Integer packageConsumptionMasterId;
	private Character isPostedWhileDischarge;
	private Integer forceAddRemoved;	  
	private Boolean isConsumed;
	private Character nursingServiceOrderFlag;
	private Integer admissionId;
	private Integer discountCategoryId;
	private Character isOrderVisibleDepartment;
	private Integer discountOwnership;
	private BigDecimal hospitalShare;
	private boolean homeCollection;
	private String orderDateTime;
	private String packageApplicableDate;
	private String packageApplicableDateDisplay;
	private Integer discountTypeId;
	private Character isChargedHalfDay;
	private String orderDateTimeString;
	private Integer patientCouponTemplateMapperId;
	private Integer medallServiceId;
	private boolean isDummyService;
	private boolean isHiddenService;
	private Date packageApplicableDateDisplayNew;
	private Date ordCancelDatetimeStringNew;
	private Date orderDateStringNew;
	private Integer pharmacyDiscountOverride;
	private Integer serviceUpdateforPackage;

	public Integer getServiceUpdateforPackage() {
		return serviceUpdateforPackage;
	}

	public void setServiceUpdateforPackage(Integer serviceUpdateforPackage) {
		this.serviceUpdateforPackage = serviceUpdateforPackage;
	}

	public boolean getIsDummyService() {
		return isDummyService;
	}
	public void setIsDummyService(boolean isDummyService) {
		this.isDummyService = isDummyService;
	}
	public boolean getIsHiddenService() {
		return isHiddenService;
	}
	public void setIsHiddenService(boolean isHiddenService) {
		this.isHiddenService = isHiddenService;
	}
	public Character getIsChargedHalfDay() {
		return isChargedHalfDay;
	}

	public void setIsChargedHalfDay(Character isChargedHalfDay) {
		this.isChargedHalfDay = isChargedHalfDay!=null ? isChargedHalfDay : 'N';
	}
	
	public String getPackageApplicableDateDisplay() {
		return packageApplicableDateDisplay;
	}

	public void setPackageApplicableDateDisplay(String packageApplicableDateDisplay) {
		this.packageApplicableDateDisplay = packageApplicableDateDisplay;
	}

	public String getPackageApplicableDate() {
		return packageApplicableDate;
	}

	public void setPackageApplicableDate(String packageApplicableDate) {
		this.packageApplicableDate = packageApplicableDate;
	}

	public boolean isHomeCollection() {
		return homeCollection;
	}

	public void setHomeCollection(boolean homeCollection) {
		this.homeCollection = homeCollection;
	}

	public BigDecimal getHospitalShare() {
		return hospitalShare;
	}

	public void setHospitalShare(BigDecimal hospitalShare) {
		this.hospitalShare = hospitalShare;
	}

	public Integer getDiscountOwnership() {
		return discountOwnership;
	}

	public void setDiscountOwnership(Integer discountOwnership) {
		this.discountOwnership = discountOwnership;
	}

	public Character getIsOrderVisibleDepartment() {
		return isOrderVisibleDepartment;
	}

	public void setIsOrderVisibleDepartment(Character isOrderVisibleDepartment) {
		this.isOrderVisibleDepartment = isOrderVisibleDepartment;
	}

	public Integer getDiscountCategoryId() {
		return discountCategoryId;
	}

	public void setDiscountCategoryId(Integer discountCategoryId) {
		this.discountCategoryId = discountCategoryId;
	}

	public Character getNursingServiceOrderFlag() {
		return nursingServiceOrderFlag;
	}

	public void setNursingServiceOrderFlag(Character nursingServiceOrderFlag) {
		this.nursingServiceOrderFlag = (nursingServiceOrderFlag!=null ? nursingServiceOrderFlag : 'N');
	}

	public Character getIsPostedWhileDischarge() {
		return isPostedWhileDischarge;
	}

	public void setIsPostedWhileDischarge(Character isPostedWhileDischarge) {
		this.isPostedWhileDischarge = isPostedWhileDischarge;
	}

	public Character getIsProcedureService() {
		return isProcedureService;
	}

	public void setIsProcedureService(Character isProcedureService) {
		this.isProcedureService = isProcedureService;
	}

	public Integer getProcedureOrderMstId() {
		return procedureOrderMstId;
	}

	public void setProcedureOrderMstId(Integer procedureOrderMstId) {
		this.procedureOrderMstId = procedureOrderMstId;
	}
	
	public Character getIsConsultationService() {
		return isConsultationService;
	}

	public void setIsConsultationService(Character isConsultationService) {
		this.isConsultationService = (isConsultationService !=null ? isConsultationService : 'N');
	}
	
	public BigDecimal getRefDocSharePer() {
		return refDocSharePer;
	}

	public void setRefDocSharePer(BigDecimal refDocSharePer) {
		this.refDocSharePer = refDocSharePer;
	}

	public Character getIsDoctorRequired() {
		return isDoctorRequired;
	}

	public void setIsDoctorRequired(Character isDoctorRequired) {
		this.isDoctorRequired = isDoctorRequired;
	}

	public BigDecimal getOldBasePrice() {
		return oldBasePrice;
	}

	public void setOldBasePrice(BigDecimal oldBasePrice) {
		this.oldBasePrice = oldBasePrice;
	}

	public BigDecimal getDummyServiceAmt() {
		return dummyServiceAmt;
	}

	public void setDummyServiceAmt(BigDecimal dummyServiceAmt) {
		this.dummyServiceAmt = dummyServiceAmt;
	}

	public Character getIsTariffRateApplicable() {
		return isTariffRateApplicable;
	}

	public void setIsTariffRateApplicable(Character isTariffRateApplicable) {
		this.isTariffRateApplicable = isTariffRateApplicable;
	}
	
	
	public Character getIsFromAddservice() {
		return isFromAddservice;
	}
	public void setIsFromAddservice(Character isFromAddservice) {
		this.isFromAddservice = isFromAddservice;
	}
	public Integer getPeriodicityServiceOrderId() {
		return periodicityServiceOrderId;
	}
	public void setPeriodicityServiceOrderId(Integer periodicityServiceOrderId) {
		this.periodicityServiceOrderId = periodicityServiceOrderId;
	}
	public List<OrderDetailsPayeeMapperDto> getOrderDetailsPayeeMapperDtosList() {
		return orderDetailsPayeeMapperDtosList;
	}
	public void setOrderDetailsPayeeMapperDtosList(List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList) {
		this.orderDetailsPayeeMapperDtosList = orderDetailsPayeeMapperDtosList;
	}
	public BigDecimal getPatientClassPercentage() {
		/*
		 * try { if (basePrice!=null && basePrice.doubleValue()>0 && concession!=null
		 * &&concession.doubleValue()>0) return
		 * BigDecimal.valueOf(GlobalCommonDateUtils.getAmount((concession.doubleValue()/
		 * basePrice.doubleValue() * 100))); else return BigDecimal.ZERO; }
		 * catch(Exception e) {  throw e; }
		 */
		return patientClassPercentage==null?BigDecimal.ZERO:patientClassPercentage;
	}
	public void setPatientClassPercentage(BigDecimal patientClassPercentage) {
		this.patientClassPercentage = patientClassPercentage;
	}
	public Boolean getMarkupStatus() {
		return markupStatus;
	}
	public void setMarkupStatus(Boolean markupStatus) {
		this.markupStatus = markupStatus;
	}
	public Integer getPatientClassId() {
		return patientClassId;
	}
	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}
	public BigDecimal getTaxAmtOld() {
		return taxAmtOld;
	}
	public void setTaxAmtOld(BigDecimal taxAmtOld) {
		this.taxAmtOld = taxAmtOld;
	}
	public BigDecimal getSelfPayableOldC() {
		return selfPayableOldC;
	}
	public void setSelfPayableOldC(BigDecimal selfPayableOldC) {
		this.selfPayableOldC = selfPayableOldC;
	}
	public BigDecimal getCreditPayableOldC() {
		return creditPayableOldC;
	}
	public void setCreditPayableOldC(BigDecimal creditPayableOldC) {
		this.creditPayableOldC = creditPayableOldC;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Integer getSaleDetailId() {
		return saleDetailId;
	}
	public void setSaleDetailId(Integer saleDetailId) {
		this.saleDetailId = saleDetailId;
	}
	public Integer getPackageEitherOrGroupId() {
		return packageEitherOrGroupId;
	}
	public void setPackageEitherOrGroupId(Integer packageEitherOrGroupId) {
		this.packageEitherOrGroupId = packageEitherOrGroupId;
	}
	public Integer getPackageTypeId() {
		return packageTypeId;
	}
	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}
	public Character getIsCapService() {
		return isCapService;
	}
	public void setIsCapService(Character isCapService) {
		this.isCapService = isCapService;
	}
	public boolean getIsPackage() {
		return isPackage;
	}
	public void setIsPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}
	public Integer getServiceSpecialityId() {
		return serviceSpecialityId;
	}
	public void setServiceSpecialityId(Integer serviceSpecialityId) {
		this.serviceSpecialityId = serviceSpecialityId;
	}
	public Integer getServiceSubSpecialityId() {
		return serviceSubSpecialityId;
	}
	public void setServiceSubSpecialityId(Integer serviceSubSpecialityId) {
		this.serviceSubSpecialityId = serviceSubSpecialityId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Long getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Long orderDate) {
		this.orderDate = orderDate;
	}
	public Long getOrderSchDate() {
		return orderSchDate;
	}
	public void setOrderSchDate(Long orderSchDate) {
		this.orderSchDate = orderSchDate;
	}
	public BigDecimal getOldNetPay() {
		return oldNetPay;
	}
	public void setOldNetPay(BigDecimal oldNetPay) {
		this.oldNetPay = oldNetPay;
	}
	public List<OrderDetailsByEncounterIdDto> getOrderDetailsByEncounterIdDtosList() {
		return orderDetailsByEncounterIdDtosList;
	}
	public void setOrderDetailsByEncounterIdDtosList(List<OrderDetailsByEncounterIdDto> orderDetailsByEncounterIdDtosList) {
		this.orderDetailsByEncounterIdDtosList = orderDetailsByEncounterIdDtosList;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
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
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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
	public Integer getPriorityId() {
		return priorityId;
	}
	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}
	public Character getIsOutsourced() {
		return isOutsourced;
	}
	public void setIsOutsourced(Character isOutsourced) {
		this.isOutsourced = isOutsourced;
	}
	public String getOrderDateString() {
		return orderDateString;
	}
	public void setOrderDateString(String orderDateString) {
		this.orderDateString = orderDateString;
	}
	public String getOrderSchDateString() {
		return orderSchDateString;
	}
	public void setOrderSchDateString(String orderSchDateString) {
		this.orderSchDateString = orderSchDateString;
	}
	public BigDecimal getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(BigDecimal basePrice) {
		this.basePrice = basePrice;
	}
	public BigDecimal getConcession() {
		return concession==null?BigDecimal.ZERO:concession;
	}
	
	public void setConcession(BigDecimal concession) {
		this.concession = concession==null?BigDecimal.ZERO:concession;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	
	public BigDecimal getNetPay() {
		return netPay;
	}
	public void setNetPay(BigDecimal netPay) {
		this.netPay = netPay;
	}
	public Integer getTaxId() {
		return taxId;
	}
	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
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
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public BigDecimal getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}
	public void setPackageConsumptionAmt(BigDecimal packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}
	public Integer getOrderSetId() {
		return orderSetId;
	}
	public void setOrderSetId(Integer orderSetId) {
		this.orderSetId = orderSetId;
	}
	public Integer getContractId() {
		return contractId;
	}
	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}
	public Integer getBedCategoryId() {
		return bedCategoryId;
	}
	public void setBedCategoryId(Integer bedCategoryId) {
		this.bedCategoryId = bedCategoryId;
	}
	public BigDecimal getCoPayPer() {
		return coPayPer==null?BigDecimal.ZERO:coPayPer;
	}
	public void setCoPayPer(BigDecimal coPayPer) {
		this.coPayPer = coPayPer==null?BigDecimal.ZERO:coPayPer;
	}
	public BigDecimal getDocSharePer() {
		return docSharePer;
	}
	public void setDocSharePer(BigDecimal docSharePer) {
		this.docSharePer = docSharePer;
	}
	public void setPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}
	public BigDecimal getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(BigDecimal totalAmt) {
		this.totalAmt = totalAmt;
	}
	public Integer getOrdCancelBy() {
		return ordCancelBy;
	}
	public void setOrdCancelBy(Integer ordCancelBy) {
		this.ordCancelBy = ordCancelBy;
	}
	public String getOrdCancelRemark() {
		return ordCancelRemark;
	}
	public void setOrdCancelRemark(String ordCancelRemark) {
		this.ordCancelRemark = ordCancelRemark;
	}
	public Integer getTariffId() {
		return tariffId;
	}
	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}
	public Integer getBillingClassId() {
		return billingClassId;
	}
	public void setBillingClassId(Integer billingClassId) {
		this.billingClassId = billingClassId;
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
	public Integer getWardId() {
		return wardId;
	}
	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}
	public Character getOrderEmergencyFlag() {
		return orderEmergencyFlag;
	}
	public void setOrderEmergencyFlag(Character orderEmergencyFlag) {
		this.orderEmergencyFlag = orderEmergencyFlag;
	}
	public Integer getPackageId() {
		return packageId;
	}
	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}
	public Integer getDrugId() {
		return drugId;
	}
	public void setDrugId(Integer drugId) {
		this.drugId = drugId;
	}
	public Integer getBatchId() {
		return batchId;
	}
	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}
	public Integer getOldOrdDtlId() {
		return oldOrdDtlId;
	}
	public void setOldOrdDtlId(Integer oldOrdDtlId) {
		this.oldOrdDtlId = oldOrdDtlId;
	}
	public Integer getServiceRendered() {
		return serviceRendered;
	}
	public void setServiceRendered(Integer serviceRendered) {
		this.serviceRendered = serviceRendered;
	}
	public Integer getServiceChargeable() {
		return serviceChargeable;
	}
	public void setServiceChargeable(Integer serviceChargeable) {
		this.serviceChargeable = serviceChargeable;
	}
	public Integer getServiceRenderingDeptId() {
		return serviceRenderingDeptId;
	}
	public void setServiceRenderingDeptId(Integer serviceRenderingDeptId) {
		this.serviceRenderingDeptId = serviceRenderingDeptId;
	}
	public Long getOrdRenderDatetime() {
		return ordRenderDatetime;
	}
	public void setOrdRenderDatetime(Long ordRenderDatetime) {
		this.ordRenderDatetime = ordRenderDatetime;
	}
	public Integer getServiceIsBilled() {
		return serviceIsBilled;
	}
	public void setServiceIsBilled(Integer serviceIsBilled) {
		this.serviceIsBilled = serviceIsBilled;
	}
	public Integer getServiceBillId() {
		return serviceBillId;
	}
	public void setServiceBillId(Integer serviceBillId) {
		this.serviceBillId = serviceBillId;
	}
	public String getOrdRemarks() {
		return ordRemarks;
	}
	public void setOrdRemarks(String ordRemarks) {
		this.ordRemarks = ordRemarks;
	}
	public Character getOrdCancelled() {
		return ordCancelled;
	}
	public void setOrdCancelled(Character ordCancelled) {
		this.ordCancelled = ordCancelled;
	}
	public Integer getOrdCancelReasonId() {
		return ordCancelReasonId;
	}
	public void setOrdCancelReasonId(Integer ordCancelReasonId) {
		this.ordCancelReasonId = ordCancelReasonId;
	}
	public Long getOrdCancelDatetime() {
		return ordCancelDatetime;
	}
	public void setOrdCancelDatetime(Long ordCancelDatetime) {
		this.ordCancelDatetime = ordCancelDatetime;
	}
	public Character getIsDrug() {
		return isDrug;
	}
	public void setIsDrug(Character isDrug) {
		this.isDrug = isDrug;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
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
	public Long getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Character getIsRateEditable() {
		return isRateEditable;
	}
	public void setIsRateEditable(Character isRateEditable) {
		this.isRateEditable = isRateEditable;
	}
	public BigDecimal getMinRateEditable() {
		return minRateEditable;
	}
	public void setMinRateEditable(BigDecimal minRateEditable) {
		this.minRateEditable = minRateEditable;
	}
	public BigDecimal getMaxRateEditable() {
		return maxRateEditable;
	}
	public void setMaxRateEditable(BigDecimal maxRateEditable) {
		this.maxRateEditable = maxRateEditable;
	}
	public Character getIsTaxApplicable() {
		return isTaxApplicable;
	}
	public void setIsTaxApplicable(Character isTaxApplicable) {
		this.isTaxApplicable = isTaxApplicable;
	}
	public Character getIsRefDoctorShare() {
		return isRefDoctorShare;
	}
	public void setIsRefDoctorShare(Character isRefDoctorShare) {
		this.isRefDoctorShare = isRefDoctorShare;
	}
	public Character getIsDiscountApplicable() {
		return isDiscountApplicable;
	}
	public void setIsDiscountApplicable(Character isDiscountApplicable) {
		this.isDiscountApplicable = isDiscountApplicable;
	}
	public BigDecimal getDiscountValue() {
		return discountValue;
	}
	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue;
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
	public Character getIsQuantityEditable() {
		return isQuantityEditable;
	}
	public void setIsQuantityEditable(Character isQuantityEditable) {
		this.isQuantityEditable = isQuantityEditable;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getTaxName() {
		return taxName;
	}
	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}
	public BigDecimal getTaxPercentage() {
		return taxPercentage;
	}
	public void setTaxPercentage(BigDecimal taxPercentage) {
		this.taxPercentage = taxPercentage;
	}
	public BigDecimal getTaxAmt() {
		return taxAmt;
	}
	public void setTaxAmt(BigDecimal taxAmt) {
		this.taxAmt = taxAmt;
	}
	public List<OrderDetailsDiscountDto> getDiscountDetailsList() {
		return discountDetailsList;
	}
	public void setDiscountDetailsList(List<OrderDetailsDiscountDto> discountDetailsList) {
		this.discountDetailsList = discountDetailsList;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Character getIsPackageService() {
		return isPackageService;
	}
	public void setIsPackageService(Character isPackageService) {
		this.isPackageService = isPackageService;
	}
	public String getSubSpecialityName() {
		return subSpecialityName;
	}
	public void setSubSpecialityName(String subSpecialityName) {
		this.subSpecialityName = subSpecialityName;
	}
	public String getOrdCancelDatetimeString() {
		return ordCancelDatetimeString;
	}
	public void setOrdCancelDatetimeString(String ordCancelDatetimeString) {
		this.ordCancelDatetimeString = ordCancelDatetimeString;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getReasonDesc() {
		return reasonDesc;
	}
	public void setReasonDesc(String reasonDesc) {
		this.reasonDesc = reasonDesc;
	}
	public Character getIsAutoRender() {
		return isAutoRender;
	}
	public void setIsAutoRender(Character isAutoRender) {
		this.isAutoRender = isAutoRender;
	}
	public Character getIsInvestigationOrder() {
		return isInvestigationOrder;
	}
	public void setIsInvestigationOrder(Character isInvestigationOrder) {
		this.isInvestigationOrder = isInvestigationOrder;
	}
	public Integer getSaleId() {
		return saleId;
	}
	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

	public List<OrderDetailsPackageMapperDto> getOrderDetailsPackageMapperDtosList() {
		return orderDetailsPackageMapperDtosList;
	}

	public void setOrderDetailsPackageMapperDtosList(List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList) {
		this.orderDetailsPackageMapperDtosList = orderDetailsPackageMapperDtosList;
	}

	public Integer getDoctorSpecialityId() {
		return doctorSpecialityId;
	}

	public void setDoctorSpecialityId(Integer doctorSpecialityId) {
		this.doctorSpecialityId = doctorSpecialityId;
	}

	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}

	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}

	public Integer getForceAddRemoved() {
		return forceAddRemoved!=null?forceAddRemoved:0;
	}

	public void setForceAddRemoved(Integer forceAddRemoved) {
		this.forceAddRemoved = forceAddRemoved;
	}

	public Boolean getIsConsumed() {
		return isConsumed!=null?isConsumed:false;
	}

	public void setIsConsumed(Boolean isConsumed) {
		this.isConsumed = isConsumed!=null?isConsumed:false;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public Integer getDiscountTypeId() {
		return discountTypeId;
	}

	public void setDiscountTypeId(Integer discountTypeId) {
		this.discountTypeId = discountTypeId;
	}

	public List<DoctorMasterDto> getDoctorList() {
		return doctorList;
	}

	public void setDoctorList(List<DoctorMasterDto> doctorList) {
		this.doctorList = doctorList;
	}

	public String getOrderDateTimeString() {
		return orderDateTimeString;
	}

	public void setOrderDateTimeString(String orderDateTimeString) {
		this.orderDateTimeString = orderDateTimeString;
	}

	public Integer getPatientCouponTemplateMapperId() {
		return patientCouponTemplateMapperId;
	}

	public void setPatientCouponTemplateMapperId(Integer patientCouponTemplateMapperId) {
		this.patientCouponTemplateMapperId = patientCouponTemplateMapperId;
	}

	public Integer getMedallServiceId() {
		return medallServiceId;
	}

	public void setMedallServiceId(Integer medallServiceId) {
		this.medallServiceId = medallServiceId;
	}
	public Date getPackageApplicableDateDisplayNew() {
		return packageApplicableDateDisplayNew;
	}
	public void setPackageApplicableDateDisplayNew(Date packageApplicableDateDisplayNew) {
		this.packageApplicableDateDisplayNew = packageApplicableDateDisplayNew;
	}
	public Date getOrdCancelDatetimeStringNew() {
		return ordCancelDatetimeStringNew;
	}
	public void setOrdCancelDatetimeStringNew(Date ordCancelDatetimeStringNew) {
		this.ordCancelDatetimeStringNew = ordCancelDatetimeStringNew;
	}
	public Date getOrderDateStringNew() {
		return orderDateStringNew;
	}
	public void setOrderDateStringNew(Date orderDateStringNew) {
		this.orderDateStringNew = orderDateStringNew;
	}
	public Integer getPharmacyDiscountOverride() {
		return pharmacyDiscountOverride;
	}
	public void setPharmacyDiscountOverride(Integer pharmacyDiscountOverride) {
		this.pharmacyDiscountOverride = pharmacyDiscountOverride;
	}
	

}
