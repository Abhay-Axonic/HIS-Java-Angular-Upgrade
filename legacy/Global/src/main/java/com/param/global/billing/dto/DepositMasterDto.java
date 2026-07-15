package com.param.global.billing.dto;

import java.util.List;

public class DepositMasterDto {
	private Integer depositMasterId;
	private Integer orgId;
	private Integer unitId;
	private String depositNo;
	private String depositDate;
	private Integer payeeId;
	private Integer patientId;
	private Integer visitTypeId;
	private Integer encounterId;
	private Integer admissionId;
	private Integer admNoteId;
	private Integer depositTypeId;
	private double depositAmount;
	private double depositPending;
	private double availableDeposit;
	private double consumedDeposit;
	private double refundAmount;
	private String receivedFrom;
	private String identificationNo;
	private char status;
	private String createdDate;
	private Integer createdBy;
	private String updatedDate;
	private Integer updatedBy;
	private String remarks;
	private Integer counterId;
	private Character isAddendum;
	private Integer billId;
	private Integer depositId;
	private int patientLoyaltyCardMpprId;
	private String signetureImage;
	private byte[] signature;
	private boolean bedReservation;
	private Integer otReservationId;
	private Integer againstPaymentRecieptId;
	private List<PaymentReceiptDetailsDto> paymentDetailsList;
	private String requestedUniqueId;

	private String cancelReasonNote;

	private Integer cancelReasonId;

	public Integer getAgainstPaymentRecieptId() {
		return againstPaymentRecieptId;
	}

	public void setAgainstPaymentRecieptId(Integer againstPaymentRecieptId) {
		this.againstPaymentRecieptId = againstPaymentRecieptId;
	}

	public String getSignetureImage() {
		return signetureImage;
	}

	public void setSignetureImage(String signetureImage) {
		this.signetureImage = signetureImage;
	}

	public List<PaymentReceiptDetailsDto> getPaymentDetailsList() {
		return paymentDetailsList;
	}

	public void setPaymentDetailsList(List<PaymentReceiptDetailsDto> paymentDetailsList) {
		this.paymentDetailsList = paymentDetailsList;
	}

	public Character getIsAddendum() {
		return isAddendum;
	}

	public void setIsAddendum(Character isAddendum) {
		this.isAddendum = isAddendum;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Integer getDepositMasterId() {
		return depositMasterId;
	}

	public void setDepositMasterId(Integer depositMasterId) {
		this.depositMasterId = depositMasterId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getDepositNo() {
		return depositNo;
	}

	public void setDepositNo(String depositNo) {
		this.depositNo = depositNo;
	}

	public String getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(String depositDate) {
		this.depositDate = depositDate;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
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

	public Integer getAdmNoteId() {
		return admNoteId;
	}

	public void setAdmNoteId(Integer admNoteId) {
		this.admNoteId = admNoteId;
	}

	public Integer getDepositTypeId() {
		return depositTypeId;
	}

	public void setDepositTypeId(Integer depositTypeId) {
		this.depositTypeId = depositTypeId;
	}

	public double getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(double depositAmount) {
		this.depositAmount = depositAmount;
	}

	public double getAvailableDeposit() {
		return availableDeposit;
	}

	public void setAvailableDeposit(double availableDeposit) {
		this.availableDeposit = availableDeposit;
	}

	public double getConsumedDeposit() {
		return consumedDeposit;
	}

	public void setConsumedDeposit(double consumedDeposit) {
		this.consumedDeposit = consumedDeposit;
	}

	public String getReceivedFrom() {
		return receivedFrom;
	}

	public void setReceivedFrom(String receivedFrom) {
		this.receivedFrom = receivedFrom;
	}

	public String getIdentificationNo() {
		return identificationNo;
	}

	public void setIdentificationNo(String identificationNo) {
		this.identificationNo = identificationNo;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'A');
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

	public double getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(double refundAmount) {
		this.refundAmount = refundAmount;
	}

	public double getDepositPending() {
		return depositPending;
	}

	public void setDepositPending(double depositPending) {
		this.depositPending = depositPending;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Integer getDepositId() {
		return depositId;
	}

	public void setDepositId(Integer depositId) {
		this.depositId = depositId;
	}

	public int getPatientLoyaltyCardMpprId() {
		return patientLoyaltyCardMpprId;
	}

	public void setPatientLoyaltyCardMpprId(int patientLoyaltyCardMpprId) {
		this.patientLoyaltyCardMpprId = patientLoyaltyCardMpprId;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public boolean isBedReservation() {
		return bedReservation;
	}

	public void setBedReservation(boolean bedReservation) {
		this.bedReservation = bedReservation;
	}

	public Integer getOtReservationId() {
		return otReservationId;
	}

	public void setOtReservationId(Integer otReservationId) {
		this.otReservationId = otReservationId;
	}

	public String getRequestedUniqueId() {
		return requestedUniqueId;
	}

	public void setRequestedUniqueId(String requestedUniqueId) {
		this.requestedUniqueId = requestedUniqueId;
	}

	public String getCancelReasonNote() {
		return cancelReasonNote;
	}

	public void setCancelReasonNote(String cancelReasonNote) {
		this.cancelReasonNote = cancelReasonNote;
	}

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}
}
