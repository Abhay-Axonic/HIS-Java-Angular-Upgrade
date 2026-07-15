package com.param.billing.global.transaction.dto;

import java.math.BigInteger;
import java.util.List;

public class TDoctorReferralShareDto {
	
	private List<Integer> perShareList;
	private Integer referralShareId;
	private Integer billId;
	private Integer admissionId;
	private Double totalAmount;
	private Double netAmount;
	private Integer taxId;
	private Integer taxType;
	private Integer referralDocId;
	private Double referralSharePer;
	private Double processedShareAmt;
	private Double dueShareAmt;
	private Integer isBillCrDr;
	private Integer isActive;
	private Double discount;
	private Double concession;
	private Double taxAmt;
	private Integer orgUnitId;
	private Integer orgId;
	private Character status;
	private Long createdDate;
	private Integer createdBy;
	private Long updatedDate;
	private Integer updatedBy;
	private String  patientName;
	private String doctorName;
	private String  visitTypeCode;
	private Double billDateTime;
	private Double encounterDate;
	private String encounterNumber;
	private String billNumber;
	private String uhidNumber;
	private BigInteger docReferralShare;
	private Integer  visitTypeId ;
	private Integer  patientId;
	private Integer  billStatusId;
	private Double outstanding;
	private Integer  referalDetailsId;
	private Integer  encounterId;
	private Integer  referralTypeId;
	private Double  share;
	public Integer getReferralShareId() {
		return referralShareId;
	}

	public void setReferralShareId(Integer referralShareId) {
		this.referralShareId = referralShareId;
	}

	public Integer getBillId() {
		return billId;
	}

	public void setBillId(Integer billId) {
		this.billId = billId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Integer getTaxId() {
		return taxId;
	}

	public void setTaxId(Integer taxId) {
		this.taxId = taxId;
	}

	public Integer getTaxType() {
		return taxType;
	}

	public void setTaxType(Integer taxType) {
		this.taxType = taxType;
	}

	public Integer getReferralDocId() {
		return referralDocId;
	}

	public void setReferralDocId(Integer referralDocId) {
		this.referralDocId = referralDocId;
	}

	public Double getReferralSharePer() {
		return referralSharePer;
	}

	public void setReferralSharePer(Double referralSharePer) {
		this.referralSharePer = referralSharePer;
	}

	public Double getProcessedShareAmt() {
		return processedShareAmt;
	}

	public void setProcessedShareAmt(Double processedShareAmt) {
		this.processedShareAmt = processedShareAmt;
	}

	public Double getDueShareAmt() {
		return dueShareAmt;
	}

	public void setDueShareAmt(Double dueShareAmt) {
		this.dueShareAmt = dueShareAmt;
	}

	public Integer getIsBillCrDr() {
		return isBillCrDr;
	}

	public void setIsBillCrDr(Integer isBillCrDr) {
		this.isBillCrDr = isBillCrDr;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Double getConcession() {
		return concession;
	}

	public void setConcession(Double concession) {
		this.concession = concession;
	}

	public Double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(Double taxAmt) {
		this.taxAmt = taxAmt;
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

	public String getPatientName() {
		return patientName;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getVisitTypeCode() {
		return visitTypeCode;
	}

	public void setVisitTypeCode(String visitTypeCode) {
		this.visitTypeCode = visitTypeCode;
	}

	public Double getBillDateTime() {
		return billDateTime;
	}

	public void setBillDateTime(Double billDateTime) {
		this.billDateTime = billDateTime;
	}

	public Double getEncounterDate() {
		return encounterDate;
	}

	public void setEncounterDate(Double encounterDate) {
		this.encounterDate = encounterDate;
	}

	public String getEncounterNumber() {
		return encounterNumber;
	}

	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
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

	public BigInteger getDocReferralShare() {
		return docReferralShare;
	}

	public void setDocReferralShare(BigInteger docReferralShare) {
		this.docReferralShare = docReferralShare;
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

	public List<Integer> getPerShareList() {
		return perShareList;
	}

	public void setPerShareList(List<Integer> perShareList) {
		this.perShareList = perShareList;
	}

	public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}

	public Double getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}

	public Integer getReferalDetailsId() {
		return referalDetailsId;
	}

	public void setReferalDetailsId(Integer referalDetailsId) {
		this.referalDetailsId = referalDetailsId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getReferralTypeId() {
		return referralTypeId;
	}

	public void setReferralTypeId(Integer referralTypeId) {
		this.referralTypeId = referralTypeId;
	}

	public Double getShare() {
		return share;
	}

	public void setShare(Double share) {
		this.share = share;
	}
	
}
