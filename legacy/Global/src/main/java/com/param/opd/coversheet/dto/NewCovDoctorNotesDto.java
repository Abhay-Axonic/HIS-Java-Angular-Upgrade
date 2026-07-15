package com.param.opd.coversheet.dto;

public class NewCovDoctorNotesDto {
private Integer newCovDoctorNotesId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer encounterId;
	
	private Integer doctorId;
	
	private Integer patientId;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer admissionId;
	
	private Character status;
	
	private Integer noOfRecordsPerPage;

	private Integer offset;

	private String doctorNotes;
	private String createdTime;
	private String employeeName;
	private String doctorName;
	
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
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
	
	public Integer getNewCovDoctorNotesId() {
		return newCovDoctorNotesId;
	}
	public void setNewCovDoctorNotesId(Integer newCovDoctorNotesId) {
		this.newCovDoctorNotesId = newCovDoctorNotesId;
	}
	public String getDoctorNotes() {
		return doctorNotes;
	}
	public void setDoctorNotes(String doctorNotes) {
		this.doctorNotes = doctorNotes;
	}
	public String getCreatedTime() {
		return createdTime;
	}
	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	
}

