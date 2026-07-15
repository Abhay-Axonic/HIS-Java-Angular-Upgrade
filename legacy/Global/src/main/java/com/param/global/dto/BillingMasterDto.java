package com.param.global.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import com.param.global.billing.dto.CouponTemplateConsumeDetailsDto;
import com.param.global.billing.dto.PaymentDepositDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;

public class BillingMasterDto {

	private Character billCancelled;

	private String billRemark;

	private Integer billingMasterId;

	private Integer encounterId;

	private Integer visitTypeId;

	private Integer patientId;

	private String billNumber;

	private Long billDateTime;

	private Double netAmount;

	private Double discountAmount;

	private Double grossAmount;

	private Double concessionAmount;

	private Double gst;

	private Double totalBillAmount;

	private Double outstanding;

	private Double roundOff;

	private Character isCreditBill;

	private Integer unitId;

	private Integer organisationId;

	private Character status;

	private Long createdDate;

	private Integer createdBy;

	private Long updatedDate;

	private Integer updatedBy;

	private Double paidAmount;

	private Integer billStatusId;

	private Double basePrice;

	private Integer bedTypeId;

	private Integer patientTypeId;

	private Integer paymentTypeId;

	private char isDiscountInPercentage;

	private char isDiscountApplicable;

	private String orderDate;

	private Integer tariffId;

	private Character isFinalBill;

	private Double selfPayable;

	private Double creditPayable;

	private Integer orderId;

	private Integer admissionId;

	private Integer ordDoctorId;

	private Integer priorityId;

	private String patientType;

	private Integer wardId;

	private Integer drCrId;

	private String ordRemarks;

	private Character orderStatus;

	private Integer orderStatusId;

	private Integer reqDiscId;

	private Integer billCancelReasonId;

	private String billCancelRemark;

	private Long billCancelDatetime;

	private Integer billCancelBy;

	private Double difference;

	private Integer newBillId;

	private Character isFinancialClearance;

	private Character isIntermediateBill;

	private Integer financialRequestSendTo;

	private Integer encounterSpecialityId;

	private Long settlementDate;

	private Character isInvoiceCreation;

	private Character isDoctorReferralPosted;

	private char isDrNote;

	private Integer drNoteAgainstBillId;

	private List<BillPayeeDetailsDto> billPayeeDetailsDtosList;

	private List<OrderDetailsMasterDto> orderDetailsMasterDtosList;

	private List<OrderDetailsMasterDto> lineLevelDiscountOrderList;

	private List<OrderDetailsMasterDto> cancelOrderDetailsList;

	private List<OrderDetailsDiscountDto> cancelledDiscountsList;

	private Integer paymentEntitlementId;
	private Integer orgId;
	
	private Integer payeeId;

	private Integer billingBedCategoryId;

	private Character isInterimBill;

	private Integer isInterimOrFinalBill;

	private Integer counterId;

	private BigDecimal reqDisAmt;

	private Integer discType;

	private Integer requestSentTo;

	private Character isAddendumBill;

	private Character isDiscountApplied;

	private Integer oldBillId;

	private Integer appointmentId;

	private Integer referralId;

	private Integer contractId;

	private Integer selfPayeeId;

	private Integer patientClassId;

	private Character isPreBilling;

	private Integer pharmacyServiceId;
	
	private Integer surchargeServiceId;
	
	private Integer tSaleId;
	
	private Integer paymentRecieptId;
	
	private Integer depositConsumptionId;
	
	private Integer againstBillId;
	
	private Character isBillReopened;
	
	private Character cashSettlement;
	
	private Character isTransferToActiveDeposit;
	
	private List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList;
	
	private List<PaymentDepositDto> paymentDepositDetailsList;
	
	private Double totalDepositConsumption;
	
	private boolean homeCollection;
	
	private boolean needToUpdatePharmacyService;
	
	private boolean addPreAuth;
	
	private boolean applyAuth;
	
	private boolean saveFromSplitBill;
	
	private boolean updatePayee;
	
	private boolean capExist;
	
	private boolean packageExist;
	
	private boolean recalculation;
	
	private BigInteger saleid;
	
	private Integer advisedById;
	
	private String advisedByName;
	
	private Integer screenId;
	private Integer serviceId;
	
	private String remark;
	
	private Integer packageId;
	
	private String receivedFrom;
	
	private String returnDate;
	
	private boolean isPackage;
	
	private Integer orderDetailsId;
	
	private long ordRenderDatetime;
	
	private long packageApplicableDate;
	
	private List<CouponTemplateConsumeDetailsDto> patientCouponConsumptionList;
	
	private Boolean consolidated;
	
	private boolean isBillManipulationAllowed;
	
	private Double dueAmount;

	private List<SlotMasterDto> slotMasterDtoList;
	
	private Integer ordDocSplId ;
	
	private Integer pharmacyReturnServiceRecal;
	
	private Boolean forceInOut;

	private Integer prescriptionId;

	public Integer getPrescriptionId() {
		return prescriptionId;
	}

	public void setPrescriptionId(Integer prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public Integer getPharmacyReturnServiceRecal() {
		return pharmacyReturnServiceRecal;
	}

	public void setPharmacyReturnServiceRecal(Integer pharmacyReturnServiceRecal) {
		this.pharmacyReturnServiceRecal = pharmacyReturnServiceRecal;
	}

	public List<SlotMasterDto> getSlotMasterDtoList() {
		return slotMasterDtoList;
	}

	public void setSlotMasterDtoList(List<SlotMasterDto> slotMasterDtoList) {
		this.slotMasterDtoList = slotMasterDtoList;
	}

	public boolean getIsBillManipulationAllowed() {
		return isBillManipulationAllowed;
	}

	public void setIsBillManipulationAllowed(boolean isBillManipulationAllowed) {
		this.isBillManipulationAllowed = isBillManipulationAllowed;
	}

	public boolean isRecalculation() {
		return recalculation;
	}

	public void setRecalculation(boolean recalculation) {
		this.recalculation = recalculation;
	}
	private Double totalAmountFromApp;
	
	private String transactionNo;
	
	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public Double getTotalAmountFromApp() {
		return totalAmountFromApp;
	}

	public void setTotalAmountFromApp(Double totalAmountFromApp) {
		this.totalAmountFromApp = totalAmountFromApp;
	}

	public boolean isPackageExist() {
		return packageExist;
	}

	public void setPackageExist(boolean packageExist) {
		this.packageExist = packageExist;
	}

	public boolean isCapExist() {
		return capExist;
	}

	public void setCapExist(boolean capExist) {
		this.capExist = capExist;
	}

	public boolean isUpdatePayee() {
		return updatePayee;
	}

	public void setUpdatePayee(boolean updatePayee) {
		this.updatePayee = updatePayee;
	}

	public boolean isSaveFromSplitBill() {
		return saveFromSplitBill;
	}

	public void setSaveFromSplitBill(boolean saveFromSplitBill) {
		this.saveFromSplitBill = saveFromSplitBill;
	}

	public boolean isApplyAuth() {
		return applyAuth;
	}

	public void setApplyAuth(boolean applyAuth) {
		this.applyAuth = applyAuth;
	}

	public boolean isAddPreAuth() {
		return addPreAuth;
	}

	public void setAddPreAuth(boolean addPreAuth) {
		this.addPreAuth = addPreAuth;
	}

	public boolean isNeedToUpdatePharmacyService() {
		return needToUpdatePharmacyService;
	}

	public void setNeedToUpdatePharmacyService(boolean needToUpdatePharmacyService) {
		this.needToUpdatePharmacyService = needToUpdatePharmacyService;
	}

	public boolean isHomeCollection() {
		return homeCollection;
	}

	public void setHomeCollection(boolean homeCollection) {
		this.homeCollection = homeCollection;
	}

	public Character getIsTransferToActiveDeposit() {
		return isTransferToActiveDeposit;
	}

	public void setIsTransferToActiveDeposit(Character isTransferToActiveDeposit) {
		this.isTransferToActiveDeposit = isTransferToActiveDeposit;
	}

	public List<PaymentReceiptDetailsDto> getPaymentReceiptDetailsDtosList() {
		return paymentReceiptDetailsDtosList;
	}

	public void setPaymentReceiptDetailsDtosList(List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList) {
		this.paymentReceiptDetailsDtosList = paymentReceiptDetailsDtosList;
	}

	public List<PaymentDepositDto> getPaymentDepositDetailsList() {
		return paymentDepositDetailsList;
	}

	public void setPaymentDepositDetailsList(List<PaymentDepositDto> paymentDepositDetailsList) {
		this.paymentDepositDetailsList = paymentDepositDetailsList;
	}

	public Double getTotalDepositConsumption() {
		return totalDepositConsumption;
	}

	public void setTotalDepositConsumption(Double totalDepositConsumption) {
		this.totalDepositConsumption = totalDepositConsumption;
	}

	public Character getCashSettlement() {
		return cashSettlement;
	}

	public void setCashSettlement(Character cashSettlement) {
		this.cashSettlement = cashSettlement;
	}

	public Integer getAgainstBillId() {
		return againstBillId;
	}

	public void setAgainstBillId(Integer againstBillId) {
		this.againstBillId = againstBillId;
	}

	public Character getIsBillReopened() {
		return isBillReopened;
	}

	public void setIsBillReopened(Character isBillReopened) {
		this.isBillReopened = isBillReopened;
	}

	public Integer getDepositConsumptionId() {
		return depositConsumptionId;
	}

	public void setDepositConsumptionId(Integer depositConsumptionId) {
		this.depositConsumptionId = depositConsumptionId;
	}

	public Integer getPaymentRecieptId() {
		return paymentRecieptId;
	}

	public void setPaymentRecieptId(Integer paymentRecieptId) {
		this.paymentRecieptId = paymentRecieptId;
	}

	public Integer getSurchargeServiceId() {
		return surchargeServiceId;
	}

	public void setSurchargeServiceId(Integer surchargeServiceId) {
		this.surchargeServiceId = surchargeServiceId;
	}

	public Character getIsPreBilling() {
		return isPreBilling;
	}

	public void setIsPreBilling(Character isPreBilling) {
		this.isPreBilling = isPreBilling;
	}

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public List<OrderDetailsMasterDto> getLineLevelDiscountOrderList() {
		return lineLevelDiscountOrderList;
	}

	public void setLineLevelDiscountOrderList(List<OrderDetailsMasterDto> lineLevelDiscountOrderList) {
		this.lineLevelDiscountOrderList = lineLevelDiscountOrderList;
	}

	public Character getIsIntermediateBill() {
		return isIntermediateBill;
	}

	public void setIsIntermediateBill(Character isIntermediateBill) {
		this.isIntermediateBill = isIntermediateBill;
	}

	public Integer getFinancialRequestSendTo() {
		return financialRequestSendTo;
	}

	public void setFinancialRequestSendTo(Integer financialRequestSendTo) {
		this.financialRequestSendTo = financialRequestSendTo;
	}

	public Character getIsFinancialClearance() {
		return isFinancialClearance;
	}

	public void setIsFinancialClearance(Character isFinancialClearance) {
		this.isFinancialClearance = isFinancialClearance;
	}

	public Integer getNewBillId() {
		return newBillId;
	}

	public void setNewBillId(Integer newBillId) {
		this.newBillId = newBillId;
	}

	public Double getDifference() {
		return difference;
	}

	public void setDifference(Double difference) {
		this.difference = difference;
	}

	public Integer getReqDiscId() {
		return reqDiscId;
	}

	public void setReqDiscId(Integer reqDiscId) {
		this.reqDiscId = reqDiscId;
	}

	public Character getIsDiscountApplied() {
		return isDiscountApplied;
	}

	public void setIsDiscountApplied(Character isDiscountApplied) {
		this.isDiscountApplied = isDiscountApplied;
	}

	public BigDecimal getReqDisAmt() {
		return reqDisAmt!=null?reqDisAmt:BigDecimal.ZERO;
	}

	public void setReqDisAmt(BigDecimal reqDisAmt) {
		this.reqDisAmt = reqDisAmt;
	}

	public Integer getDiscType() {
		return discType;
	}

	public void setDiscType(Integer discType) {
		this.discType = discType;
	}

	public Integer getRequestSentTo() {
		return requestSentTo;
	}

	public void setRequestSentTo(Integer requestSentTo) {
		this.requestSentTo = requestSentTo;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public List<BillPayeeDetailsDto> getBillPayeeDetailsDtosList() {
		return billPayeeDetailsDtosList;
	}

	public void setBillPayeeDetailsDtosList(List<BillPayeeDetailsDto> billPayeeDetailsDtosList) {
		this.billPayeeDetailsDtosList = billPayeeDetailsDtosList;
	}

	public Integer getIsInterimOrFinalBill() {
		return isInterimOrFinalBill;
	}

	public void setIsInterimOrFinalBill(Integer isInterimOrFinalBill) {
		this.isInterimOrFinalBill = isInterimOrFinalBill;
	}

	public Long getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Long settlementDate) {
		this.settlementDate = settlementDate;
	}

	public List<OrderDetailsMasterDto> getCancelOrderDetailsList() {
		return cancelOrderDetailsList;
	}

	public void setCancelOrderDetailsList(List<OrderDetailsMasterDto> cancelOrderDetailsList) {
		this.cancelOrderDetailsList = cancelOrderDetailsList;
	}

	public List<OrderDetailsDiscountDto> getCancelledDiscountsList() {
		return cancelledDiscountsList;
	}

	public void setCancelledDiscountsList(List<OrderDetailsDiscountDto> cancelledDiscountsList) {
		this.cancelledDiscountsList = cancelledDiscountsList;
	}

	public Character getBillCancelled() {
		return billCancelled;
	}

	public void setBillCancelled(Character billCancelled) {
		this.billCancelled = billCancelled;
	}

	public String getBillRemark() {
		return billRemark;
	}

	public void setBillRemark(String billRemark) {
		this.billRemark = billRemark;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Long getBillDateTime() {
		return billDateTime;
	}

	public void setBillDateTime(Long billDateTime) {
		this.billDateTime = billDateTime;
	}

	public Double getNetAmount() {
		return netAmount!=null?netAmount:0;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getGrossAmount() {
		return grossAmount;
	}

	public void setGrossAmount(Double grossAmount) {
		this.grossAmount = grossAmount;
	}

	public Double getConcessionAmount() {
		return concessionAmount;
	}

	public void setConcessionAmount(Double concessionAmount) {
		this.concessionAmount = concessionAmount;
	}

	public Double getGst() {
		return gst;
	}

	public void setGst(Double gst) {
		this.gst = gst;
	}

	public Double getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public Double getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}

	public Double getRoundOff() {
		return roundOff;
	}

	public void setRoundOff(Double roundOff) {
		this.roundOff = roundOff;
	}

	public Character getIsCreditBill() {
		return isCreditBill;
	}

	public void setIsCreditBill(Character isCreditBill) {
		this.isCreditBill = isCreditBill;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}

	public Double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(Double basePrice) {
		this.basePrice = basePrice;
	}

	public Integer getBedTypeId() {
		return bedTypeId;
	}

	public void setBedTypeId(Integer bedTypeId) {
		this.bedTypeId = bedTypeId;
	}

	public Integer getPatientTypeId() {
		return patientTypeId;
	}

	public void setPatientTypeId(Integer patientTypeId) {
		this.patientTypeId = patientTypeId;
	}

	public Integer getPaymentTypeId() {
		return paymentTypeId;
	}

	public void setPaymentTypeId(Integer paymentTypeId) {
		this.paymentTypeId = paymentTypeId;
	}

	public char getIsDiscountInPercentage() {
		return isDiscountInPercentage;
	}

	public void setIsDiscountInPercentage(char isDiscountInPercentage) {
		this.isDiscountInPercentage = isDiscountInPercentage;
	}

	public char getIsDiscountApplicable() {
		return isDiscountApplicable;
	}

	public void setIsDiscountApplicable(char isDiscountApplicable) {
		this.isDiscountApplicable = isDiscountApplicable;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public Integer getTariffId() {
		return tariffId;
	}

	public void setTariffId(Integer tariffId) {
		this.tariffId = tariffId;
	}

	public Character getIsFinalBill() {
		return isFinalBill;
	}

	public void setIsFinalBill(Character isFinalBill) {
		this.isFinalBill = isFinalBill;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}

	public List<OrderDetailsMasterDto> getOrderDetailsMasterDtosList() {
		return orderDetailsMasterDtosList;
	}

	public void setOrderDetailsMasterDtosList(List<OrderDetailsMasterDto> orderDetailsMasterDtosList) {
		this.orderDetailsMasterDtosList = orderDetailsMasterDtosList;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getOrdDoctorId() {
		return ordDoctorId;
	}

	public void setOrdDoctorId(Integer ordDoctorId) {
		this.ordDoctorId = ordDoctorId;
	}

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
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

	public Integer getDrCrId() {
		return drCrId;
	}

	public void setDrCrId(Integer drCrId) {
		this.drCrId = drCrId;
	}

	public String getOrdRemarks() {
		return ordRemarks;
	}

	public void setOrdRemarks(String ordRemarks) {
		this.ordRemarks = ordRemarks;
	}

	public Character getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Character orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getOrderStatusId() {
		return orderStatusId;
	}

	public void setOrderStatusId(Integer orderStatusId) {
		this.orderStatusId = orderStatusId;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public Character getIsInterimBill() {
		return isInterimBill;
	}

	public void setIsInterimBill(Character isInterimBill) {
		this.isInterimBill = (isInterimBill != null && isInterimBill != '\u0000' ? isInterimBill : 'N');
	}

	public Character getIsInvoiceCreation() {
		return isInvoiceCreation;
	}

	public void setIsInvoiceCreation(Character isInvoiceCreation) {
		this.isInvoiceCreation = (isInvoiceCreation != null && isInvoiceCreation != '\u0000') ? isInvoiceCreation : 'N';
	}

	public char getIsDrNote() {
		return isDrNote;
	}

	public void setIsDrNote(char isDrNote) {
		this.isDrNote = (isDrNote != '\u0000' ? isDrNote : 'N');
	}

	public Integer getDrNoteAgainstBillId() {
		return drNoteAgainstBillId;
	}

	public void setDrNoteAgainstBillId(Integer drNoteAgainstBillId) {
		this.drNoteAgainstBillId = drNoteAgainstBillId;
	}

	public Character getIsDoctorReferralPosted() {
		return isDoctorReferralPosted;
	}

	public void setIsDoctorReferralPosted(Character isDoctorReferralPosted) {
		this.isDoctorReferralPosted = (isDoctorReferralPosted == null || isDoctorReferralPosted == '\u0000') ? 'N'
				: isDoctorReferralPosted;
	}

	public Integer getBillCancelReasonId() {
		return billCancelReasonId;
	}

	public void setBillCancelReasonId(Integer billCancelReasonId) {
		this.billCancelReasonId = billCancelReasonId;
	}

	public String getBillCancelRemark() {
		return billCancelRemark;
	}

	public void setBillCancelRemark(String billCancelRemark) {
		this.billCancelRemark = billCancelRemark;
	}

	public Long getBillCancelDatetime() {
		return billCancelDatetime;
	}

	public void setBillCancelDatetime(Long billCancelDatetime) {
		this.billCancelDatetime = billCancelDatetime;
	}

	public Integer getBillCancelBy() {
		return billCancelBy;
	}

	public void setBillCancelBy(Integer billCancelBy) {
		this.billCancelBy = billCancelBy;
	}

	public Character getIsAddendumBill() {
		return isAddendumBill;
	}

	public void setIsAddendumBill(Character isAddendumBill) {
		this.isAddendumBill = isAddendumBill;
	}

	public Integer getOldBillId() {
		return oldBillId;
	}

	public void setOldBillId(Integer oldBillId) {
		this.oldBillId = oldBillId;
	}

	public Integer getEncounterSpecialityId() {
		return encounterSpecialityId;
	}

	public void setEncounterSpecialityId(Integer encounterSpecialityId) {
		this.encounterSpecialityId = encounterSpecialityId;
	}

	public Integer getPatientClassId() {
		return patientClassId;
	}

	public void setPatientClassId(Integer patientClassId) {
		this.patientClassId = patientClassId;
	}

	public Integer getPharmacyServiceId() {
		return pharmacyServiceId;
	}

	public void setPharmacyServiceId(Integer pharmacyServiceId) {
		this.pharmacyServiceId = pharmacyServiceId;
	}

	public Integer gettSaleId() {
		return tSaleId;
	}

	public void settSaleId(Integer tSaleId) {
		this.tSaleId = tSaleId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public BigInteger getSaleid() {
		return saleid;
	}

	public void setSaleid(BigInteger saleid) {
		this.saleid = saleid;
	}

	public Integer getAdvisedById() {
		return advisedById;
	}

	public void setAdvisedById(Integer advisedById) {
		this.advisedById = advisedById;
	}

	public String getAdvisedByName() {
		return advisedByName;
	}

	public void setAdvisedByName(String advisedByName) {
		this.advisedByName = advisedByName;
	}

	public Integer getScreenId() {
		return screenId;
	}

	public void setScreenId(Integer screenId) {
		this.screenId = screenId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public List<CouponTemplateConsumeDetailsDto> getPatientCouponConsumptionList() {
		return patientCouponConsumptionList;
	}

	public void setPatientCouponConsumptionList(List<CouponTemplateConsumeDetailsDto> patientCouponConsumptionList) {
		this.patientCouponConsumptionList = patientCouponConsumptionList;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	public boolean getIsPackage() {
		return isPackage;
	}

	public void setIsPackage(boolean isPackage) {
		this.isPackage = isPackage;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public long getOrdRenderDatetime() {
		return ordRenderDatetime;
	}

	public void setOrdRenderDatetime(long ordRenderDatetime) {
		this.ordRenderDatetime = ordRenderDatetime;
	}

	public long getPackageApplicableDate() {
		return packageApplicableDate;
	}

	public void setPackageApplicableDate(long packageApplicableDate) {
		this.packageApplicableDate = packageApplicableDate;
	}

	public Boolean getConsolidated() {
		return consolidated;
	}

	public void setConsolidated(Boolean consolidated) {
		this.consolidated = consolidated;
	}

	public Double getDueAmount() {
		return dueAmount;
	}

	public void setDueAmount(Double dueAmount) {
		this.dueAmount = dueAmount;
	}

	public Integer getOrdDocSplId() {
		return ordDocSplId;
	}

	public void setOrdDocSplId(Integer ordDocSplId) {
		this.ordDocSplId = ordDocSplId;
	}

	public Boolean getForceInOut() {
		return forceInOut;
	}

	public void setForceInOut(Boolean forceInOut) {
		this.forceInOut = forceInOut;
	}



}
