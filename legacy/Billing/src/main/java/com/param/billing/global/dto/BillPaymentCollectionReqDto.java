package com.param.billing.global.dto;

import java.util.List;

import com.param.global.billing.dto.PaymentDepositDto;
import com.param.global.billing.dto.PaymentReceiptDetailsDto;

public class BillPaymentCollectionReqDto {
	
	private Double consumedAmount;
	
	private Double totalCollection;
	
	private Integer appointmentId;
	
	private Character isPreOrPostBilling;
	
	private Integer encounterId;
	
	private Integer admissionId;
	
	private Integer paymentRecieptId;
	
	private Integer orgnizationId;
	
	private Integer unitId;
	
	private Integer billingMasterId;
	  
	private Double amount;
	
	private String recieptNumber;
	
	private String dateOfReciept;
	
	private Integer receiptType;
	 
	private Integer counterId;
	
	private Integer settlementId;
	
	private Integer payeeId;
	
	private String receivedFrom;
	
	private String identificationNumber;
	
	private String remark;
	
	private Character isPrinted;
	
	private Character isFinancialClearance;
	
	private Character status;
	
	private String createdDate;
	
	private Integer createdBy;
	
	private String updatedDate;
	
	private Integer updatedBy;
	
	private Integer billPayeeDetailsId;
	
	private String billNumber;
	
	private String receiptNumber;
	
	private Integer receiptId;

	private Integer depositConsumptionId;

	private String depositeConsumptionNumber;
	
	private Double totalDepositConsumption;
	
	private Double payeeDueAmount;
	
	private Double totalBillAmount;
	
	private Integer depositConsumptionAmount;
	
	private Character isTransferToActiveDeposit;
	
	private Integer patientId;
	
	private Integer VisitTypeId;
	
	private Integer isInterimOrFinalBill;
	
	private Character isNursingAssessmentRequired;

	private Integer doctorId;
	
	private Integer referalDocId;
	
	private Double totalSelfPayable;
	
	private Double totalCreditPayable;
	
	private String signetureImage;
	
	private byte[] signature;
	
	private List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList;
	
	private List<PaymentDepositDto> paymentDepositDetailsList;
	
	private List<BillingPaymentDetailsReqDto> listBillingPaymentDetailsReqDto;
	
	private String binaryValueDataForSing;
	
	private Double disallowedAmount;
	
	private Integer disallowedAgainstInvoiceDtlsId;
	
	private Character isPaidAppointment;
	
	private Double outstanding;
	
	private Character isPrint;
	
	private boolean isTeleConsultApp;
	
	private Integer saleId;
	
	private Boolean delete;
	
	private Double depositAmount;
	
	private boolean isPharmacyReturn;
	
	private Integer patientCouponId;
	
	public Character getIsPrint() {
		return isPrint;
	}
	public void setIsPrint(Character isPrint) {
		this.isPrint = isPrint;
	}
	public Double getOutstanding() {
		return outstanding;
	}
	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}
	public Character getIsPaidAppointment() {
		return isPaidAppointment;
	}
	public void setIsPaidAppointment(Character isPaidAppointment) {
		this.isPaidAppointment = isPaidAppointment;
	}
	public Double getDisallowedAmount() {
		return disallowedAmount;
	}
	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}
	public Integer getDisallowedAgainstInvoiceDtlsId() {
		return disallowedAgainstInvoiceDtlsId;
	}
	public void setDisallowedAgainstInvoiceDtlsId(Integer disallowedAgainstInvoiceDtlsId) {
		this.disallowedAgainstInvoiceDtlsId = disallowedAgainstInvoiceDtlsId;
	}
	public Character getIsNursingAssessmentRequired() {
		return isNursingAssessmentRequired;
	}
	public void setIsNursingAssessmentRequired(Character isNursingAssessmentRequired) {
		this.isNursingAssessmentRequired = isNursingAssessmentRequired;
	}
	public Integer getReferalDocId() {
		return referalDocId;
	}
	public void setReferalDocId(Integer referalDocId) {
		this.referalDocId = referalDocId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Double getTotalSelfPayable() {
		return totalSelfPayable;
	}
	public void setTotalSelfPayable(Double totalSelfPayable) {
		this.totalSelfPayable = totalSelfPayable;
	}
	public Double getTotalCreditPayable() {
		return totalCreditPayable;
	}
	public void setTotalCreditPayable(Double totalCreditPayable) {
		this.totalCreditPayable = totalCreditPayable;
	}
	public Integer getIsInterimOrFinalBill() {
		return isInterimOrFinalBill;
	}
	public void setIsInterimOrFinalBill(Integer isInterimOrFinalBill) {
		this.isInterimOrFinalBill = isInterimOrFinalBill;
	}
	public Integer getVisitTypeId() {
		return VisitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		VisitTypeId = visitTypeId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Character getIsTransferToActiveDeposit() {
		return isTransferToActiveDeposit;
	}
	public void setIsTransferToActiveDeposit(Character isTransferToActiveDeposit) {
		this.isTransferToActiveDeposit = isTransferToActiveDeposit;
	}
	public Double getPayeeDueAmount() {
		return payeeDueAmount;
	}
	public void setPayeeDueAmount(Double payeeDueAmount) {
		this.payeeDueAmount = payeeDueAmount;
	}
	public Double getTotalDepositConsumption() {
		return totalDepositConsumption;
	}
	public void setTotalDepositConsumption(Double totalDepositConsumption) {
		this.totalDepositConsumption = totalDepositConsumption;
	}
	public String getDepositeConsumptionNumber() {
		return depositeConsumptionNumber;
	}
	public void setDepositeConsumptionNumber(String depositeConsumptionNumber) {
		this.depositeConsumptionNumber = depositeConsumptionNumber;
	}
	public Integer getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}
	public void setBillPayeeDetailsId(Integer billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
	}
	public Character getIsFinancialClearance() {
		return isFinancialClearance;
	}
	public Double getConsumedAmount() {
		return consumedAmount;
	}
	public void setConsumedAmount(Double consumedAmount) {
		this.consumedAmount = consumedAmount;
	}
	public Double getTotalCollection() {
		return totalCollection;
	}
	public void setTotalCollection(Double totalCollection) {
		this.totalCollection = totalCollection;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Character getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}
	public void setIsPreOrPostBilling(Character isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public Integer getPaymentRecieptId() {
		return paymentRecieptId;
	}
	public void setPaymentRecieptId(Integer paymentRecieptId) {
		this.paymentRecieptId = paymentRecieptId;
	}
	public Integer getOrgnizationId() {
		return orgnizationId;
	}
	public void setOrgnizationId(Integer orgnizationId) {
		this.orgnizationId = orgnizationId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getBillingMasterId() {
		return billingMasterId;
	}
	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getRecieptNumber() {
		return recieptNumber;
	}
	public void setRecieptNumber(String recieptNumber) {
		this.recieptNumber = recieptNumber;
	}
	public String getDateOfReciept() {
		return dateOfReciept;
	}
	public void setDateOfReciept(String dateOfReciept) {
		this.dateOfReciept = dateOfReciept;
	}
	public Integer getReceiptType() {
		return receiptType;
	}
	public void setReceiptType(Integer receiptType) {
		this.receiptType = receiptType;
	}
	public Integer getCounterId() {
		return counterId;
	}
	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}
	public Integer getSettlementId() {
		return settlementId;
	}
	public void setSettlementId(Integer settlementId) {
		this.settlementId = settlementId;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public String getReceivedFrom() {
		return receivedFrom;
	}
	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}
	public String getIdentificationNumber() {
		return identificationNumber;
	}
	public void setIdentificationNumber(String identificationNumber) {
		this.identificationNumber = identificationNumber;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Character getIsPrinted() {
		return isPrinted;
	}
	public void setIsPrinted(Character isPrinted) {
		this.isPrinted = isPrinted;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
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
	public List<PaymentReceiptDetailsDto> getPaymentReceiptDetailsDtosList() {
		return paymentReceiptDetailsDtosList;
	}
	public void setPaymentReceiptDetailsDtosList(List<PaymentReceiptDetailsDto> paymentReceiptDetailsDtosList) {
		this.paymentReceiptDetailsDtosList = paymentReceiptDetailsDtosList;
	}
	public List<BillingPaymentDetailsReqDto> getListBillingPaymentDetailsReqDto() {
		return listBillingPaymentDetailsReqDto;
	}
	public void setListBillingPaymentDetailsReqDto(List<BillingPaymentDetailsReqDto> listBillingPaymentDetailsReqDto) {
		this.listBillingPaymentDetailsReqDto = listBillingPaymentDetailsReqDto;
	}
	public void setIsFinancialClearance(Character isFinancialClearance) {
		this.isFinancialClearance = (isFinancialClearance != '\u0000' ? isFinancialClearance : 'N');
	}
	public List<PaymentDepositDto> getPaymentDepositDetailsList() {
		return paymentDepositDetailsList;
	}
	public void setPaymentDepositDetailsList(List<PaymentDepositDto> paymentDepositDetailsList) {
		this.paymentDepositDetailsList = paymentDepositDetailsList;
	}

	public String getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}
	
	public Integer getDepositConsumptionId() {
		return depositConsumptionId;
	}
	public void setDepositConsumptionId(Integer depositConsumptionId) {
		this.depositConsumptionId = depositConsumptionId;
	}
	public Double getTotalBillAmount() {
		return totalBillAmount;
	}
	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}
	public Integer getDepositConsumptionAmount() {
		return depositConsumptionAmount;
	}
	public void setDepositConsumptionAmount(Integer depositConsumptionAmount) {
		this.depositConsumptionAmount = depositConsumptionAmount;
	}
	public String getReceiptNumber() {
		return receiptNumber;
	}
	public void setReceiptNumber(String receiptNumber) {
		this.receiptNumber = receiptNumber;
	}
	public Integer getReceiptId() {
		return receiptId;
	}
	public void setReceiptId(Integer receiptId) {
		this.receiptId = receiptId;
	}
	public String getBinaryValueDataForSing() {
		return binaryValueDataForSing;
	}
	public void setBinaryValueDataForSing(String binaryValueDataForSing) {
		this.binaryValueDataForSing = binaryValueDataForSing;
	}
	public String getSignetureImage() {
		return signetureImage;
	}
	public void setSignetureImage(String signetureImage) {
		this.signetureImage = signetureImage;
	}
	public byte[] getSignature() {
		return signature;
	}
	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	public boolean isTeleConsultApp() {
		return isTeleConsultApp;
	}
	public void setTeleConsultApp(boolean isTeleConsultApp) {
		this.isTeleConsultApp = isTeleConsultApp;
	}
	public Integer getSaleId() {
		return saleId;
	}
	public void setSaleId(Integer saleId) {
		this.saleId = saleId;
	}
	public Boolean getDelete() {
		return delete;
	}
	public void setDelete(Boolean delete) {
		this.delete = delete;
	}
	public Double getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(Double depositAmount) {
		this.depositAmount = depositAmount;
	}
	public boolean getIsPharmacyReturn() {
		return isPharmacyReturn;
	}
	public void setIsPharmacyReturn(boolean isPharmacyReturn) {
		this.isPharmacyReturn = isPharmacyReturn;
	}
	public Integer getPatientCouponId() {
		return patientCouponId;
	}
	public void setPatientCouponId(Integer patientCouponId) {
		this.patientCouponId = patientCouponId;
	}
	
}
