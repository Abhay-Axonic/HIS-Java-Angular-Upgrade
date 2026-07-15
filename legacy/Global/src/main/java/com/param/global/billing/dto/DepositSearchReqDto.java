package com.param.global.billing.dto;

public class DepositSearchReqDto {
	private String fromDate;
	private String toDate;
	private String patientName;
	private String uhidNumber;
	private Integer visitTypeId;
	private Integer depositTypeId;
	private Integer orgId;
	private Integer unitId;
	private Integer admissionNoteId;
	private Integer encounterId;
	private Integer admissionId;
	private Integer offset;
	private Integer noOfRecordsPerPage;

	private Boolean fromDeposit;
 	
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
	public Integer getAdmissionNoteId() {
		return admissionNoteId;
	}
	public void setAdmissionNoteId(Integer admissionNoteId) {
		this.admissionNoteId = admissionNoteId;
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
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}
	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	public Boolean getFromDeposit() {
		return fromDeposit;
	}

	public void setFromDeposit(Boolean fromDeposit) {
		this.fromDeposit = fromDeposit;
	}
}
