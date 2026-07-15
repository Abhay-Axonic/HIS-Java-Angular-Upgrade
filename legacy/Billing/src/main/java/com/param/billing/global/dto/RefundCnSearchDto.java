package com.param.billing.global.dto;

public class RefundCnSearchDto {
	private String fromDate;
	private String toDate;
	private String patientName;
	private String uhidNumber;
	private Integer visitTypeId;
	private Integer depositTypeId;
	private Integer orgId;
	private Integer unitId;
	private String refundCnNo;
	private String refundCrNo;
	private String paymentRefundVoucherNo;
	private Integer noOfRecordsPerPage;
	private Integer  offset;
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getUhidNumber() {
		return uhidNumber;
	}
	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public Integer getDepositTypeId() {
		return depositTypeId;
	}
	public void setDepositTypeId(Integer depositTypeId) {
		this.depositTypeId = depositTypeId;
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
	public String getRefundCnNo() {
		return refundCnNo;
	}
	public void setRefundCnNo(String refundCnNo) {
		this.refundCnNo = refundCnNo;
	}
	public String getPaymentRefundVoucherNo() {
		return paymentRefundVoucherNo;
	}
	public void setPaymentRefundVoucherNo(String paymentRefundVoucherNo) {
		this.paymentRefundVoucherNo = paymentRefundVoucherNo;
	}
	public String getRefundCrNo() {
		return refundCrNo;
	}
	public void setRefundCrNo(String refundCrNo) {
		this.refundCrNo = refundCrNo;
	}
	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}
	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	
	
}
