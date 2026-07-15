package com.param.billing.global.transaction.dto;

import java.math.BigDecimal;
import java.util.List;

public class CreditNoteMasterDto {

	private Integer creditNoteId;

	private String creditNoteDate;

	private Integer billId;
	
	private Integer patientId;

	private Integer billPayeeId;

	private Integer crNoteAgainst;

	private String creditNoteNo;

	private double crTotalAmount;

	private double crTaxAmount;

	private double crNetAmount;

	private double crOutAdjustmentAmount;

	private double crDeuAmount;

	private Integer crReasonId;

	private String ceRemark;

	private Integer unitId;

	private Integer orgnisationId;

	private char status;

	private String createdDate;

	private Integer createdBy;

	private String updatedDate;

	private Integer updatedBy;

	private Integer cancelOrDiscount;

	private Integer counterId;
	
    private Boolean isCreditNoteApproval;
    
    private Integer requestSentTo;
    
    private Character isApproved;
    
    private double discountRoundoff;
    
    private Integer reqDiscId;
    
    private BigDecimal selfPayable;
	
	private BigDecimal creditPayable;
	
	private BigDecimal packageConsumptionAmt;
	
	private String creditNoteRemark;

	private Integer signRoundOff;

	private List<CreditNoteDetailsDto> listCreditNoteDetails;
	
	private Integer selfPayeeId;
	
	private Integer billPayeeDetailsId;

	private Integer associateCompanyId;
	
private Integer visitTypeId;
	
	private Integer encounterId;
	
	private Integer admissionId;

	public Integer getBillPayeeDetailsId() {
		return billPayeeDetailsId;
	}

	public void setBillPayeeDetailsId(Integer billPayeeDetailsId) {
		this.billPayeeDetailsId = billPayeeDetailsId;
	}

	public Integer getSelfPayeeId() {
		return selfPayeeId;
	}

	public void setSelfPayeeId(Integer selfPayeeId) {
		this.selfPayeeId = selfPayeeId;
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

	public BigDecimal getPackageConsumptionAmt() {
		return packageConsumptionAmt;
	}

	public void setPackageConsumptionAmt(BigDecimal packageConsumptionAmt) {
		this.packageConsumptionAmt = packageConsumptionAmt;
	}

	public Integer getCreditNoteId() {
		return creditNoteId;
	}

	public void setCreditNoteId(Integer creditNoteId) {
		this.creditNoteId = creditNoteId;
	}

	public String getCreditNoteDate() {
		return creditNoteDate;
	}

	public void setCreditNoteDate(String creditNoteDate) {
		this.creditNoteDate = creditNoteDate;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getBillPayeeId() {
		return billPayeeId;
	}

	public void setBillPayeeId(Integer billPayeeId) {
		this.billPayeeId = billPayeeId;
	}

	public Integer getCrNoteAgainst() {
		return crNoteAgainst;
	}

	public void setCrNoteAgainst(Integer crNoteAgainst) {
		this.crNoteAgainst = crNoteAgainst;
	}

	public String getCreditNoteNo() {
		return creditNoteNo;
	}

	public void setCreditNoteNo(String creditNoteNo) {
		this.creditNoteNo = creditNoteNo;
	}

	public double getCrTotalAmount() {
		return crTotalAmount;
	}

	public void setCrTotalAmount(double crTotalAmount) {
		this.crTotalAmount = crTotalAmount;
	}

	public double getCrTaxAmount() {
		return crTaxAmount;
	}

	public void setCrTaxAmount(double crTaxAmount) {
		this.crTaxAmount = crTaxAmount;
	}

	public double getCrNetAmount() {
		return crNetAmount;
	}

	public void setCrNetAmount(double crNetAmount) {
		this.crNetAmount = crNetAmount;
	}

	public double getCrOutAdjustmentAmount() {
		return crOutAdjustmentAmount;
	}

	public void setCrOutAdjustmentAmount(double crOutAdjustmentAmount) {
		this.crOutAdjustmentAmount = crOutAdjustmentAmount;
	}

	public double getCrDeuAmount() {
		return crDeuAmount;
	}

	public void setCrDeuAmount(double crDeuAmount) {
		this.crDeuAmount = crDeuAmount;
	}

	public Integer getCrReasonId() {
		return crReasonId;
	}

	public void setCrReasonId(Integer crReasonId) {
		this.crReasonId = crReasonId;
	}

	public String getCeRemark() {
		return ceRemark;
	}

	public void setCeRemark(String ceRemark) {
		this.ceRemark = ceRemark;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrgnisationId() {
		return orgnisationId;
	}

	public void setOrgnisationId(Integer orgnisationId) {
		this.orgnisationId = orgnisationId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
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

	public List<CreditNoteDetailsDto> getListCreditNoteDetails() {
		return listCreditNoteDetails;
	}

	public void setListCreditNoteDetails(List<CreditNoteDetailsDto> listCreditNoteDetails) {
		this.listCreditNoteDetails = listCreditNoteDetails;
	}

	public Integer getCancelOrDiscount() {
		return cancelOrDiscount;
	}

	public void setCancelOrDiscount(Integer cancelOrDiscount) {
		this.cancelOrDiscount = cancelOrDiscount;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Boolean getIsCreditNoteApproval() {
		return isCreditNoteApproval;
	}

	public void setIsCreditNoteApproval(Boolean isCreditNoteApproval) {
		this.isCreditNoteApproval = isCreditNoteApproval;
	}

	public Integer getRequestSentTo() {
		return requestSentTo;
	}

	public void setRequestSentTo(Integer requestSentTo) {
		this.requestSentTo = requestSentTo;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Character getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(Character isApproved) {
		this.isApproved = (isApproved == null || isApproved == '\u0000') ? 'N' : isApproved;
	}

	public double getDiscountRoundoff() {
		return discountRoundoff;
	}

	public void setDiscountRoundoff(double discountRoundoff) {
		this.discountRoundoff = discountRoundoff;
	}

	public Integer getReqDiscId() {
		return reqDiscId;
	}

	public void setReqDiscId(Integer reqDiscId) {
		this.reqDiscId = reqDiscId;
	}

	public String getCreditNoteRemark() {
		return creditNoteRemark;
	}

	public void setCreditNoteRemark(String creditNoteRemark) {
		this.creditNoteRemark = creditNoteRemark;
	}

	public Integer getSignRoundOff() {
		return signRoundOff != null ?  signRoundOff : 0;
	}

	public void setSignRoundOff(Integer signRoundOff) {
		this.signRoundOff = signRoundOff;
	}

	public Integer getAssociateCompanyId() {
		return associateCompanyId;
	}

	public void setAssociateCompanyId(Integer associateCompanyId) {
		this.associateCompanyId = associateCompanyId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
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
	
	
}
