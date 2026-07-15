package com.param.billing.global.transaction.dto;

import java.util.List;

public class TDoctorShareLoggerReqDto {
	
	private Integer orgId;
	
	private Integer unitId;
	
	private Integer doctorAdvanceId;
	
	private Double consumedDocAdvance;
	
	private Double consumedDocCredit;
	
	private Double retainerShipAmt;
	
	private Double processedAmt;
	
	private Double paidAmt;
	
	private Integer doctorId;
	
	private Integer createdBy;
	
	private Integer referralId;
	
	private List<TDocSharePaymentVoucherLoggerDto> listTDocSharePaymentVoucherLoggerDto;
	
	private List<DoctorShareCrNoteDetailsDto> doctorShareCrNoteDetailsDtosList;
	
	private Double tdsAmt;
	
	
	
	public Double getTdsAmt() {
		return tdsAmt;
	}

	public void setTdsAmt(Double tdsAmt) {
		this.tdsAmt = tdsAmt;
	}

	public Integer getReferralId() {
		return referralId;
	}

	public void setReferralId(Integer referralId) {
		this.referralId = referralId;
	}

	public Double getProcessedAmt() {
		return processedAmt;
	}

	public void setProcessedAmt(Double processedAmt) {
		this.processedAmt = processedAmt;
	}

	public Double getPaidAmt() {
		return paidAmt;
	}

	public void setPaidAmt(Double paidAmt) {
		this.paidAmt = paidAmt;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getDoctorAdvanceId() {
		return doctorAdvanceId;
	}

	public void setDoctorAdvanceId(Integer doctorAdvanceId) {
		this.doctorAdvanceId = doctorAdvanceId;
	}

	public Double getRetainerShipAmt() {
		return retainerShipAmt;
	}

	public void setRetainerShipAmt(Double retainerShipAmt) {
		this.retainerShipAmt = retainerShipAmt;
	}

	public Double getConsumedDocAdvance() {
		return consumedDocAdvance;
	}

	public void setConsumedDocAdvance(Double consumedDocAdvance) {
		this.consumedDocAdvance = consumedDocAdvance;
	}

	public Double getConsumedDocCredit() {
		return consumedDocCredit;
	}

	public void setConsumedDocCredit(Double consumedDocCredit) {
		this.consumedDocCredit = consumedDocCredit;
	}

	public List<DoctorShareCrNoteDetailsDto> getDoctorShareCrNoteDetailsDtosList() {
		return doctorShareCrNoteDetailsDtosList;
	}

	public void setDoctorShareCrNoteDetailsDtosList(List<DoctorShareCrNoteDetailsDto> doctorShareCrNoteDetailsDtosList) {
		this.doctorShareCrNoteDetailsDtosList = doctorShareCrNoteDetailsDtosList;
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
	public List<TDocSharePaymentVoucherLoggerDto> getListTDocSharePaymentVoucherLoggerDto() {
		return listTDocSharePaymentVoucherLoggerDto;
	}
	public void setListTDocSharePaymentVoucherLoggerDto(
			List<TDocSharePaymentVoucherLoggerDto> listTDocSharePaymentVoucherLoggerDto) {
		this.listTDocSharePaymentVoucherLoggerDto = listTDocSharePaymentVoucherLoggerDto;
	}

	
}
