package com.param.billing.global.transaction.config.dto;

import java.math.BigDecimal;

public class ChargeConversionMasterDto {
	private Integer conversionMasterId;
	private Integer patinetId;
	private Integer visitTypeId;
	private Integer encounterId;
	private Integer admissionId;
	private Integer fromPayeeId;
	private Integer fromContractId;
	private Integer toPayeeId;
	private Integer toContractId;
	private String remarks;
	private Integer orgUnitId;
	private Integer orgId;
	private Character status;
	private Long createdDate;
	private Integer createdBy;
	private Integer updatedBy;
	private Long updatedDate;
	private BigDecimal convertAmt;


	
	public Integer getConversionMasterId() {
		return conversionMasterId;
	}

	public void setConversionMasterId(Integer conversionMasterId) {
		this.conversionMasterId = conversionMasterId;
	}

	public Integer getPatinetId() {
		return patinetId;
	}

	public void setPatinetId(Integer patinetId) {
		this.patinetId = patinetId;
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

	public Integer getFromPayeeId() {
		return fromPayeeId;
	}

	public void setFromPayeeId(Integer fromPayeeId) {
		this.fromPayeeId = fromPayeeId;
	}

	public Integer getFromContractId() {
		return fromContractId;
	}

	public void setFromContractId(Integer fromContractId) {
		this.fromContractId = fromContractId;
	}

	public Integer getToPayeeId() {
		return toPayeeId;
	}

	public void setToPayeeId(Integer toPayeeId) {
		this.toPayeeId = toPayeeId;
	}

	public Integer getToContractId() {
		return toContractId;
	}

	public void setToContractId(Integer toContractId) {
		this.toContractId = toContractId;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public BigDecimal getConvertAmt() {
		return convertAmt;
	}

	public void setConvertAmt(BigDecimal convertAmt) {
		this.convertAmt = convertAmt;
	}
	
}
