package com.param.global.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.param.global.otims.dto.ProcedureOrderComponentDetailsDto;

public class OrderDetailsMasterDto {
	private Integer orderDetailsId;
	private Integer orgId;
	private Integer orgUnitId;
	private Integer orderId;
	private Integer orderQty;
	private Integer serviceId;
	private Integer priorityId;
	private Character isOutsourced;
	private Long orderDate;
	private Long orderSchDate;
	private String orderDateString;
	private String orderSchDateString;
	private BigDecimal serviceAmt;
	private BigDecimal convertAmt;
	private BigDecimal ordConcession;
	private BigDecimal ordConcessionPer;
	private BigDecimal ordDiscount;
	private BigDecimal ordDiscountPer;
	private BigDecimal netAmount;
	private BigDecimal oldNetPay;
	private Integer tariffId;
	private Integer billingClassId;
	private Integer bedId;
	private Integer roomId;
	private Integer wardId;
	private Character orderEmergencyFlag;
	private Integer packageId;
	private Integer ordDocId;
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
	private Long createdDate;
	private Integer createdBy;
	private Character ordCancelled;
	private Integer ordCancelReasonId;
	private Long ordCancelDatetime;
	private Character status;
	private Integer updatedBy;
	private Long updatedDate;
	private Integer ordDocSplId;
	private Integer taxId;
	private BigDecimal taxPer;
	private BigDecimal taxAmount;
	private BigDecimal selfPayable;
	private BigDecimal creditPayable;
	private BigDecimal oldCreditPayable;
	private BigDecimal oldSelfPayable;
	private Integer payeeId;
	private BigDecimal packageConsumptionAmt;
	private Integer orderSetId;
	private Integer contractId;
	private Integer bedCategoryId;//used as billingBed Category
	private BigDecimal coPayPer;
	private BigDecimal docSharePer;
	private BigDecimal ordTotalAmt;
	private Integer ordCancelBy;
	private String ordCancelRemark;
	private Character isDrug;
	private Integer encounterId;
	private Character isRateEditable;
	private BigDecimal rateEditbleMinValue;
	private BigDecimal rateEditableMaxValue;
	private Character isTaxApplicable;
	private Character isRefDoctorShare;
	private Character isDocReq;
	private Character isDiscount;
	private BigDecimal discountValue;
	private Integer gstTypeId;
	private Integer otcGstTypeId;
	private Character isQuantityEditable;
	private List<OrderDetailsDiscountDto> discountDetailsList;
	private boolean isPackage;// yogesh
	private Integer packageTypeId;// yogesh
	private char isPackageService;// yogesh
	private BigDecimal apportionedPrice;// yogesh
	private char isCapService;// Yogesh
	private Integer departmentId;// Santosh
	private Integer subDepartmentId;// Santosh
	private Character isInvestigationOrder;// kaustubh
	private Integer admissionId;// Yogesh
	private char isProcedure;// kaustubh
	private char isProcedureService;// kaustubh
	private Integer procedureOrderMstId;// kaustubh
	private Integer specialityId;// Yogesh
	private Integer procedureTypeId;// Yogesh
	private Integer patientTypeId;// Yogesh
	private Integer paymentEntitlementId;// Yogesh
	private double orderComponentAmount;// yogesh
	private Integer visitTypeId;
	private Integer saleDetailId;
	private BigDecimal reqDisAmt;
	private Map<Integer, List<ProcedureOrderComponentDetailsDto>> procedureOrderComponents;// yogesh
	private Character isAppointableRadiology; // kaustubh
	private Integer packageEitherOrGroupId;
	private Integer packageCapDetailsId;
	private OrderDetailsMasterDto cloneDto;
	private String serviceName;
	private String specialityName;
	private BigDecimal patientClassPercentage;
	private Boolean markupStatus;
	private boolean debitNoteService;
	private Integer intrimBillId;
	private Integer intrimBillDetailsId;
	private Integer patientId;
	private Character isPostedWhileDischarge;
	private String accessionNumber;
	private Double rate;
	private Integer productCategoryId;
	private Character isFromAddservice;
	private BigDecimal dummyServiceAmt;
	private Character isDiscountApplicable;
	private Integer referralId;
	private List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList;
	private List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList;
	private BigDecimal refDocSharePer;
	private Integer packageConsumptionMasterId;
	private int index;// ganesh
	private Character isConsultationService;
	private Integer packagePayable;
	private Character isPeriodicityService;
	private Integer saleId;
	private Character isOrderVisibleDepartment;
	private Character nursingServiceOrderFlag;
	private Integer billDetailsId;
	private Double concessionPer;
	private Double discountPer;
	private Integer discountCategoryId;
	private Integer forceAddRemoved;
	private Boolean isConsumed;
	private Character isLabService;
	private Character isFromServiceSerach;
	private Integer discountOwnership;
	private BigDecimal hospitalShare;
	private String pImage;
	private String pImageName;
	private String serviceCode;
	private Integer servicePriorityId;
	private String packageApplicableDate;
	private Double crNetAmount;
	private String testCode;
	private String testRecordId;
	private String testName;
	private Character isChargedHalfDay;
	private Integer patientCouponTemplateMapperId;
	private Integer covTemplateId;
	private Integer freeTextOrderedServicesId;
	private String batchNo;
	private String orderNo;
	private String expiryDate;
	private String itemName;
	private String itemCode;
	private String uomName;
	private Integer fromMedicationCoBilling;
	private Integer issueId;
	private Integer batchMapperId;
	private Integer issueDetailId;
	private Integer returnQuantity;
	private boolean isFromPackage;
	private Boolean creditNoteService;
	private boolean isDummyService;
	private boolean isHiddenService;
	private Date expiryDateNew;
	private Date orderDateStringNew;

	private Boolean isFutureService = false;
	private Integer pharmacyDiscountOverride;
	
	private Integer oldPackageId;

	public Boolean getFutureService() {
		return isFutureService;
	}

	public void setFutureService(Boolean futureService) {
		isFutureService = futureService;
	}

	public boolean getIsDummyService() {
		return isDummyService;
	}

	public void setDummyService(boolean isDummyService) {
		this.isDummyService = isDummyService;
	}

	public boolean getIsHiddenService() {
		return isHiddenService;
	}

	public void setHiddenService(boolean isHiddenService) {
		this.isHiddenService = isHiddenService;
	}

	public String getUomName() {
		return uomName;
	}

	public void setUomName(String uomName) {
		this.uomName = uomName;
	}

	public Character getIsChargedHalfDay() {
		return isChargedHalfDay;
	}

	public void setIsChargedHalfDay(Character isChargedHalfDay) {
		this.isChargedHalfDay = isChargedHalfDay!=null ? isChargedHalfDay : 'N';
	}

	public Double getCrNetAmount() {
		return crNetAmount!=null ? crNetAmount : 0.00;
	}

	public void setCrNetAmount(Double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}

	public String getPackageApplicableDate() {
		return packageApplicableDate;
	}

	public void setPackageApplicableDate(String packageApplicableDate) {
		this.packageApplicableDate = packageApplicableDate;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
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

	public Character getIsFromServiceSerach() {
		return isFromServiceSerach;
	}

	public void setIsFromServiceSerach(Character isFromServiceSerach) {
		this.isFromServiceSerach = isFromServiceSerach;
	}

	public Character getIsLabService() {
		return isLabService;
	}

	public void setIsLabService(Character isLabService) {
		this.isLabService = isLabService;
	}

	public Integer getDiscountCategoryId() {
		return discountCategoryId;
	}

	public void setDiscountCategoryId(Integer discountCategoryId) {
		this.discountCategoryId = discountCategoryId;
	}

	public Double getConcessionPer() {
		return concessionPer;
	}

	public void setConcessionPer(Double concessionPer) {
		this.concessionPer = concessionPer;
	}

	public Double getDiscountPer() {
		return discountPer;
	}

	public void setDiscountPer(Double discountPer) {
		this.discountPer = discountPer;
	}

	public Integer getBillDetailsId() {
		return billDetailsId;
	}

	public void setBillDetailsId(Integer billDetailsId) {
		this.billDetailsId = billDetailsId;
	}

	public BigDecimal getOrdDiscountPer() {
		return ordDiscountPer!=null ? ordDiscountPer : BigDecimal.ZERO;
	}

	public void setOrdDiscountPer(BigDecimal ordDiscountPer) {
		this.ordDiscountPer = ordDiscountPer!=null ? ordDiscountPer : BigDecimal.ZERO;
	}

	public Character getIsPeriodicityService() {
		return isPeriodicityService;
	}

	public void setIsPeriodicityService(Character isPeriodicityService) {
		this.isPeriodicityService = isPeriodicityService;
	}

	public Character getIsConsultationService() {
		return isConsultationService;
	}

	public void setIsConsultationService(Character isConsultationService) {
		this.isConsultationService = (isConsultationService == null ? 'N' : isConsultationService);
	}

	public BigDecimal getRefDocSharePer() {
		return refDocSharePer;
	}

	public void setRefDocSharePer(BigDecimal refDocSharePer) {
		this.refDocSharePer = refDocSharePer;
	}

	public Character getIsDocReq() {
		return isDocReq;
	}

	public void setIsDocReq(Character isDocReq) {
		this.isDocReq = isDocReq;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public BigDecimal getDummyServiceAmt() {
		return dummyServiceAmt;
	}

	public void setDummyServiceAmt(BigDecimal dummyServiceAmt) {
		this.dummyServiceAmt = dummyServiceAmt;
	}

	public Character getIsDiscountApplicable() {
		return isDiscountApplicable;
	}

	public void setIsDiscountApplicable(Character isDiscountApplicable) {
		this.isDiscountApplicable = isDiscountApplicable;
	}

	public Character getIsFromAddservice() {
		return isFromAddservice;
	}

	public void setIsFromAddservice(Character isFromAddservice) {
		this.isFromAddservice = isFromAddservice;
	}

	public Character getIsPostedWhileDischarge() {
		return isPostedWhileDischarge = (isPostedWhileDischarge != null ? isPostedWhileDischarge : 'N');
	}

	public void setIsPostedWhileDischarge(Character isPostedWhileDischarge) {
		this.isPostedWhileDischarge = isPostedWhileDischarge;
	}

	public String getAccessionNumber() {
		return accessionNumber;
	}

	public void setAccessionNumber(String accessionNumber) {
		this.accessionNumber = accessionNumber;
	}

	public List<OrderDetailsPayeeMapperDto> getOrderDetailsPayeeMapperDtosList() {
		return orderDetailsPayeeMapperDtosList;
	}

	public void setOrderDetailsPayeeMapperDtosList(List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList) {
		this.orderDetailsPayeeMapperDtosList = orderDetailsPayeeMapperDtosList;
	}

	public Integer getIntrimBillId() {
		return intrimBillId;
	}

	public void setIntrimBillId(Integer intrimBillId) {
		this.intrimBillId = intrimBillId;
	}

	public Integer getIntrimBillDetailsId() {
		return intrimBillDetailsId;
	}

	public void setIntrimBillDetailsId(Integer intrimBillDetailsId) {
		this.intrimBillDetailsId = intrimBillDetailsId;
	}

	public BigDecimal getPatientClassPercentage() {
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

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public Integer getPackageEitherOrGroupId() {
		return packageEitherOrGroupId;
	}

	public void setPackageEitherOrGroupId(Integer packageEitherOrGroupId) {
		this.packageEitherOrGroupId = packageEitherOrGroupId;
	}

	public BigDecimal getReqDisAmt() {
		return reqDisAmt;
	}

	public void setReqDisAmt(BigDecimal reqDisAmt) {
		this.reqDisAmt = reqDisAmt;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Character getIsAppointableRadiology() {
		return isAppointableRadiology;
	}

	public void setIsAppointableRadiology(Character isAppointableRadiology) {
		this.isAppointableRadiology = isAppointableRadiology;
	}

	public Map<Integer, List<ProcedureOrderComponentDetailsDto>> getProcedureOrderComponents() {
		return procedureOrderComponents;
	}

	public void setProcedureOrderComponents(
			Map<Integer, List<ProcedureOrderComponentDetailsDto>> procedureOrderComponents) {
		this.procedureOrderComponents = procedureOrderComponents;
	}

	public double getOrderComponentAmount() {
		return orderComponentAmount;
	}

	public void setOrderComponentAmount(double orderComponentAmount) {
		this.orderComponentAmount = orderComponentAmount;
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

	public Integer getProcedureTypeId() {
		return procedureTypeId;
	}

	public void setProcedureTypeId(Integer procedureTypeId) {
		this.procedureTypeId = procedureTypeId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public char getIsProcedureService() {
		return isProcedureService = (isProcedureService != '\u0000' ? isProcedureService : 'N');
	}

	public char getIsProcedure() {
		return isProcedure = (isProcedure != '\u0000' ? isProcedure : 'N');
	}

	public void setIsProcedure(char isProcedure) {
		this.isProcedure = (isProcedure != '\u0000' ? isProcedure : 'N');
	}

	public void setIsProcedureService(char isProcedureService) {
		this.isProcedureService = (isProcedureService != '\u0000' ? isProcedureService : 'N');
	}

	public Integer getProcedureOrderMstId() {
		return procedureOrderMstId;
	}

	public void setProcedureOrderMstId(Integer procedureOrderMstId) {
		this.procedureOrderMstId = procedureOrderMstId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Character getIsInvestigationOrder() {
		return isInvestigationOrder;
	}

	public void setIsInvestigationOrder(Character isInvestigationOrder) {
		this.isInvestigationOrder = isInvestigationOrder;
	}

	public Integer getPackageTypeId() {
		return packageTypeId;
	}

	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}

	public BigDecimal getOldNetPay() {
		return oldNetPay;
	}

	public void setOldNetPay(BigDecimal oldNetPay) {
		this.oldNetPay = oldNetPay;
	}

	public List<OrderDetailsDiscountDto> getDiscountDetailsList() {
		return discountDetailsList;
	}

	public void setDiscountDetailsList(List<OrderDetailsDiscountDto> discountDetailsList) {
		this.discountDetailsList = discountDetailsList;
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

	public Integer getOrderQty() {
		return orderQty;
	}

	public void setOrderQty(Integer orderQty) {
		this.orderQty = orderQty;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
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

	public BigDecimal getServiceAmt() {
		return serviceAmt;
	}

	public void setServiceAmt(BigDecimal serviceAmt) {
		this.serviceAmt = serviceAmt;
	}

	public BigDecimal getOrdConcession() {
		return ordConcession!=null?ordConcession:BigDecimal.ZERO;
	}

	public void setOrdConcession(BigDecimal ordConcession) {
		this.ordConcession = ordConcession!=null?ordConcession:BigDecimal.ZERO;
	}

	public BigDecimal getOrdDiscount() {
		return ordDiscount!=null ? ordDiscount : BigDecimal.ZERO;
	}

	public void setOrdDiscount(BigDecimal ordDiscount) {
		this.ordDiscount = ordDiscount!=null ? ordDiscount : BigDecimal.ZERO;
	}

	public BigDecimal getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(BigDecimal netAmount) {
		this.netAmount = netAmount;
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

	public Integer getOrdDocId() {
		return ordDocId;
	}

	public void setOrdDocId(Integer ordDocId) {
		this.ordDocId = ordDocId;
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

	public Integer getOrdDocSplId() {
		return ordDocSplId;
	}

	public void setOrdDocSplId(Integer ordDocSplId) {
		this.ordDocSplId = ordDocSplId;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public BigDecimal getTaxPer() {
		return taxPer == null ? BigDecimal.ZERO : taxPer;
	}

	public void setTaxPer(BigDecimal taxPer) {
		this.taxPer = taxPer;
	}

	public BigDecimal getTaxAmount() {
		return taxAmount == null ? BigDecimal.ZERO : taxAmount;
	}

	public void setTaxAmount(BigDecimal taxAmount) {
		this.taxAmount = taxAmount;
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
		return (packageConsumptionAmt == null ? BigDecimal.ZERO : packageConsumptionAmt);
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
		this.coPayPer = coPayPer;
	}

	public BigDecimal getDocSharePer() {
		return docSharePer;
	}

	public void setDocSharePer(BigDecimal docSharePer) {
		this.docSharePer = docSharePer;
	}

	public BigDecimal getOrdTotalAmt() {
		return ordTotalAmt!=null?ordTotalAmt:BigDecimal.ZERO;
	}

	public void setOrdTotalAmt(BigDecimal ordTotalAmt) {
		this.ordTotalAmt = ordTotalAmt;
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

	public Character getIsDrug() {
		return isDrug=(isDrug != null ? isDrug : 'N') ;
	}

	public void setIsDrug(Character isDrug) {
		this.isDrug = isDrug;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Character getIsRateEditable() {
		return isRateEditable;
	}

	public void setIsRateEditable(Character isRateEditable) {
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

	public Character getIsDiscount() {
		return isDiscount;
	}

	public void setIsDiscount(Character isDiscount) {
		this.isDiscount = isDiscount;
	}

	public BigDecimal getDiscountValue() {
		return discountValue!=null?discountValue:BigDecimal.ZERO;
	}

	public void setDiscountValue(BigDecimal discountValue) {
		this.discountValue = discountValue!=null?discountValue:BigDecimal.ZERO;
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

	public void setPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}

	public void setIsPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}

	public boolean isPackage() {
		return isPackage;
	}

	public char getIsPackageService() {
		return isPackageService = (isPackageService != '\u0000' ? isPackageService : 'N');
	}

	public void setIsPackageService(char isPackageService) {
		this.isPackageService = (isPackageService != '\u0000' ? isPackageService : 'N');
	}

	public BigDecimal getApportionedPrice() {
		return apportionedPrice;
	}

	public void setApportionedPrice(BigDecimal apportionedPrice) {
		this.apportionedPrice = apportionedPrice;
	}

	public char getIsCapService() {
		return isCapService = (isCapService != '\u0000' ? isCapService : 'N');
	}

	public void setIsCapService(char isCapService) {
		this.isCapService = (isCapService != '\u0000' ? isCapService : 'N');
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public Integer getSubDepartmentId() {
		return subDepartmentId;
	}

	public void setSubDepartmentId(Integer subDepartmentId) {
		this.subDepartmentId = subDepartmentId;
	}

	public BigDecimal getConvertAmt() {
		return convertAmt;
	}

	public void setConvertAmt(BigDecimal convertAmt) {
		this.convertAmt = convertAmt;
	}

	public Integer getSaleDetailId() {
		return saleDetailId;
	}

	public void setSaleDetailId(Integer saleDetailId) {
		this.saleDetailId = saleDetailId;
	}

	public Integer getPackageCapDetailsId() {
		return packageCapDetailsId;
	}

	public void setPackageCapDetailsId(Integer packageCapDetailsId) {
		this.packageCapDetailsId = packageCapDetailsId;
	}

	public OrderDetailsMasterDto getCloneDto() {
		return cloneDto;
	}

	public void setCloneDto(OrderDetailsMasterDto cloneDto) {
		this.cloneDto = cloneDto;
	}

	public boolean getDebitNoteService() {
		return debitNoteService;
	}

	public void setDebitNoteService(boolean debitNoteService) {
		this.debitNoteService = debitNoteService;
	}

	public BigDecimal getOldCreditPayable() {
		return oldCreditPayable;
	}

	public void setOldCreditPayable(BigDecimal oldCreditPayable) {
		this.oldCreditPayable = oldCreditPayable;
	}

	public BigDecimal getOldSelfPayable() {
		return oldSelfPayable;
	}

	public void setOldSelfPayable(BigDecimal oldSelfPayable) {
		this.oldSelfPayable = oldSelfPayable;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Integer getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(Integer productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public List<OrderDetailsPackageMapperDto> getOrderDetailsPackageMapperDtosList() {
		return orderDetailsPackageMapperDtosList;
	}

	public void setOrderDetailsPackageMapperDtosList(
			List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtosList) {
		this.orderDetailsPackageMapperDtosList = orderDetailsPackageMapperDtosList;
	}

	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}

	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public BigDecimal getOrdConcessionPer() {
		return ordConcessionPer;
	}

	public void setOrdConcessionPer(BigDecimal ordConcessionPer) {
		this.ordConcessionPer = ordConcessionPer;
	}

	public Integer getPackagePayable() {
		return packagePayable;
	}

	public void setPackagePayable(Integer packagePayable) {
		this.packagePayable = packagePayable;
	}

	public Integer getSaleId() {
		return saleId;
	}

	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}

	public Character getIsOrderVisibleDepartment() {
		return isOrderVisibleDepartment!=null ? isOrderVisibleDepartment:'A' ;
	}

	public void setIsOrderVisibleDepartment(Character isOrderVisibleDepartment) {
		this.isOrderVisibleDepartment = isOrderVisibleDepartment;
	}

	public Character getNursingServiceOrderFlag() {
		return nursingServiceOrderFlag !=null ?  nursingServiceOrderFlag : 'N';
	}

	public void setNursingServiceOrderFlag(Character nursingServiceOrderFlag) {
		this.nursingServiceOrderFlag = nursingServiceOrderFlag!=null ? nursingServiceOrderFlag : 'N';
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

	public String getpImage() {
		return pImage;
	}

	public void setpImage(String pImage) {
		this.pImage = pImage;
	}

	public String getpImageName() {
		return pImageName;
	}

	public void setpImageName(String pImageName) {
		this.pImageName = pImageName;
	}

	public Integer getServicePriorityId() {
		return servicePriorityId;
	}

	public void setServicePriorityId(Integer servicePriorityId) {
		this.servicePriorityId = servicePriorityId;
	}

	public String getTestCode() {
		return testCode;
	}

	public void setTestCode(String testCode) {
		this.testCode = testCode;
	}

	public String getTestRecordId() {
		return testRecordId;
	}

	public void setTestRecordId(String testRecordId) {
		this.testRecordId = testRecordId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public Integer getPatientCouponTemplateMapperId() {
		return patientCouponTemplateMapperId;
	}

	public void setPatientCouponTemplateMapperId(Integer patientCouponTemplateMapperId) {
		this.patientCouponTemplateMapperId = patientCouponTemplateMapperId;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}

	public Integer getFreeTextOrderedServicesId() {
		return freeTextOrderedServicesId;
	}

	public void setFreeTextOrderedServicesId(Integer freeTextOrderedServicesId) {
		this.freeTextOrderedServicesId = freeTextOrderedServicesId;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public Integer getFromMedicationCoBilling() {
		return fromMedicationCoBilling;
	}

	public void setFromMedicationCoBilling(Integer fromMedicationCoBilling) {
		this.fromMedicationCoBilling = fromMedicationCoBilling;
	}

	public Integer getIssueId() {
		return issueId;
	}

	public void setIssueId(Integer issueId) {
		this.issueId = issueId;
	}

	public Integer getBatchMapperId() {
		return batchMapperId;
	}

	public void setBatchMapperId(Integer batchMapperId) {
		this.batchMapperId = batchMapperId;
	}

	public Integer getIssueDetailId() {
		return issueDetailId;
	}

	public void setIssueDetailId(Integer issueDetailId) {
		this.issueDetailId = issueDetailId;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public boolean getIsFromPackage() {
		return isFromPackage;
	}

	public void setIsFromPackage(boolean isFromPackage) {
		this.isFromPackage = isFromPackage;
	}

	public Boolean getCreditNoteService() {
		return creditNoteService;
	}

	public void setCreditNoteService(Boolean creditNoteService) {
		this.creditNoteService = creditNoteService;
	}

	public Date getExpiryDateNew() {
		return expiryDateNew;
	}

	public void setExpiryDateNew(Date expiryDateNew) {
		this.expiryDateNew = expiryDateNew;
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

	public Integer getOldPackageId() {
		return oldPackageId;
	}

	public void setOldPackageId(Integer oldPackageId) {
		this.oldPackageId = oldPackageId;
	}

	
	

}
