package com.param.billing.global.transaction.dto;

public class DoctorShareCrNoteDetailsDto {

	private Integer doctorCrDetailsId;
	
	private Integer doctorCrId;
	
	private Integer billId;
	
	private Integer doctorId;
	
	private Double doctorCredit;
	
	private String note;
	
	private Integer orgUnitId;

	private Integer orgId;

	private Character status;
	
	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;
	
	private String billDate;
	
	private String billNumber;
	
	private String uhidNumber;
	
	private String patientName;
	
	private Double totalBillAmount;
	
	private Double netAmount;
	
	private Double outstanding;
	
	private Double totalCredit;
	
	private Double amountPaid;

	private String doctorName; 
	
	private Integer selfPayeeId;
	
	private Character isPreBilling;
	
	private Integer requestStatus;

	private Double oldDocCreditAmt;
	
    private Integer rejectReasonId;
	
	private String rejectionNote;
	
	private Integer docShareVoucheId;
	
	private Double balanceCredit;

	private Double  consumedCredit;
	
	private Double availableCredit;
	
	private Double doctorAdvance;
	
	private Integer doctorAdvanceId;
	
	private Double consumedCreditOld;
	
	private Double amountPaidOld;
	
	private Integer referralId;
	
	private String referralName;
	
	public String getReferralName() {
		return referralName;
	}

	public void setReferralName(String referralName) {
		this.referralName = referralName;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public Double getConsumedCreditOld() {
		return consumedCreditOld;
	}

	public void setConsumedCreditOld(Double consumedCreditOld) {
		this.consumedCreditOld = consumedCreditOld;
	}

	public Double getAmountPaidOld() {
		return amountPaidOld;
	}

	public void setAmountPaidOld(Double amountPaidOld) {
		this.amountPaidOld = amountPaidOld;
	}

	public Integer getDoctorAdvanceId() {
		return doctorAdvanceId;
	}

	public void setDoctorAdvanceId(Integer doctorAdvanceId) {
		this.doctorAdvanceId = doctorAdvanceId;
	}

	public Double getDoctorAdvance() {
		return doctorAdvance;
	}

	public void setDoctorAdvance(Double doctorAdvance) {
		this.doctorAdvance = doctorAdvance;
	}

	public Double getAvailableCredit() {
		return availableCredit;
	}

	public void setAvailableCredit(Double availableCredit) {
		this.availableCredit = availableCredit;
	}

	public Double getBalanceCredit() {
		return balanceCredit;
	}

	public void setBalanceCredit(Double balanceCredit) {
		this.balanceCredit = balanceCredit;
	}

	public Double getConsumedCredit() {
		return consumedCredit;
	}

	public void setConsumedCredit(Double consumedCredit) {
		this.consumedCredit = consumedCredit;
	}

	public Integer getDocShareVoucheId() {
		return docShareVoucheId;
	}

	public void setDocShareVoucheId(Integer docShareVoucheId) {
		this.docShareVoucheId = docShareVoucheId;
	}

	public Integer getRejectReasonId() {
		return rejectReasonId;
	}

	public void setRejectReasonId(Integer rejectReasonId) {
		this.rejectReasonId = rejectReasonId;
	}

	public String getRejectionNote() {
		return rejectionNote;
	}

	public void setRejectionNote(String rejectionNote) {
		this.rejectionNote = rejectionNote;
	}

	public Double getOldDocCreditAmt() {
		return oldDocCreditAmt;
	}

	public void setOldDocCreditAmt(Double oldDocCreditAmt) {
		this.oldDocCreditAmt = oldDocCreditAmt;
	}
	
	public Integer getRequestStatus() {
		return requestStatus;
	}

	public void setRequestStatus(Integer requestStatus) {
		this.requestStatus = requestStatus;
	}

	public Character getIsPreBilling() {
		return isPreBilling;
	}

	public void setIsPreBilling(Character isPreBilling) {
		this.isPreBilling = isPreBilling;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
	}

	public Double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getBillDate() {
		return billDate;
	}

	public void setBillDate(String billDate) {
		this.billDate = billDate;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public String getUhidNumber() {
		return uhidNumber;
	}

	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public Double getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}

	public Double getTotalCredit() {
		return totalCredit;
	}

	public void setTotalCredit(Double totalCredit) {
		this.totalCredit = totalCredit;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}


	public Integer getDoctorCrDetailsId() {
		return doctorCrDetailsId;
	}

	public void setDoctorCrDetailsId(Integer doctorCrDetailsId) {
		this.doctorCrDetailsId = doctorCrDetailsId;
	}

	public Integer getDoctorCrId() {
		return doctorCrId;
	}

	public void setDoctorCrId(Integer doctorCrId) {
		this.doctorCrId = doctorCrId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Double getDoctorCredit() {
		return doctorCredit;
	}

	public void setDoctorCredit(Double doctorCredit) {
		this.doctorCredit = doctorCredit;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public String getCreatedDate() {
		return createdDate;
	}


}
