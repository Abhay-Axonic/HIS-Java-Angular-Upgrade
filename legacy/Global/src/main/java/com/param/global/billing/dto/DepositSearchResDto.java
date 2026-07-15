package com.param.global.billing.dto;

import java.math.BigDecimal;
import java.util.Date;

public class DepositSearchResDto {
	private String depositNo;
	private String depositDate;
	private Date depositDateNew;
	private String uhidNumber;
	private String patientName;
	private Integer depositTypeId;
	private Integer payeeId;
	private Integer visitTypeId;
	private BigDecimal depositAmount;
	private Double refundAmount;
	private BigDecimal consumedDeposit;
	private BigDecimal availableDeposit;
	private String visitNo;
	private Integer depositMasterId;
	private Integer admissionId;
	private Integer encounterId;
	private String companyDesc;
	private String userName;
	private String encounterNumber;
	private String admissionNumber;
	private String visitTypeName;
	private String depositType;
	private String createdBy;

	private char status;
	
	public BigDecimal getDepositAmount() {
		return depositAmount;
	}
	public void setDepositAmount(BigDecimal depositAmount) {
		this.depositAmount = depositAmount;
	}
	public BigDecimal getAvailableDeposit() {
		return availableDeposit;
	}
	public void setAvailableDeposit(BigDecimal availableDeposit) {
		this.availableDeposit = availableDeposit;
	}
	public BigDecimal getConsumedDeposit() {
		return consumedDeposit;
	}
	public void setConsumedDeposit(BigDecimal consumedDeposit) {
		this.consumedDeposit = consumedDeposit;
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
	public Integer getDepositTypeId() {
		return depositTypeId;
	}
	public void setDepositTypeId(Integer depositTypeId) {
		this.depositTypeId = depositTypeId;
	}
	public Integer getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public Double getRefundAmount() {
		return refundAmount;
	}
	public void setRefundAmount(Double refundAmount) {
		this.refundAmount = refundAmount;
	}
	public String getVisitNo() {
		return visitNo;
	}
	public void setVisitNo(String visitNo) {
		this.visitNo = visitNo;
	}
	public Integer getDepositMasterId() {
		return depositMasterId;
	}
	public void setDepositMasterId(Integer depositMasterId) {
		this.depositMasterId = depositMasterId;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public String getCompanyDesc() {
		return companyDesc;
	}
	public void setCompanyDesc(String companyDesc) {
		this.companyDesc = companyDesc;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEncounterNumber() {
		return encounterNumber;
	}
	public void setEncounterNumber(String encounterNumber) {
		this.encounterNumber = encounterNumber;
	}
	public String getAdmissionNumber() {
		return admissionNumber;
	}
	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}
	public String getVisitTypeName() {
		return visitTypeName;
	}
	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}
	public String getDepositType() {
		return depositType;
	}
	public void setDepositType(String depositType) {
		this.depositType = depositType;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getDepositDateNew() {
		return depositDateNew;
	}
	public void setDepositDateNew(Date depositDateNew) {
		this.depositDateNew = depositDateNew;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}
}
