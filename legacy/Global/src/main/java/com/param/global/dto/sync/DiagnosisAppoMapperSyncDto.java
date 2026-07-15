package com.param.global.dto.sync;

public class DiagnosisAppoMapperSyncDto {

	private Integer appointmentId;
	
	private Integer hospitalId;
	
	private Integer unitId;
	
	private Integer globalUhid;
	
	private String diagnosis;
	
	private char status;
	
	private String diagnosisDate;
	
	private String createdDate;
	
	private String comment;
	
	private String diagnosisType;
	
	private String priority;
	
	private String remark;
	
	private Integer diagnosisId;
	private String localUhid;
	private String organisationName;
	private Integer pkDiagnosisAppoMapperId;
	private Integer applicationId;
	private Integer admissionId;
	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public Integer getPkDiagnosisAppoMapperId() {
		return pkDiagnosisAppoMapperId;
	}

	public void setPkDiagnosisAppoMapperId(Integer pkDiagnosisAppoMapperId) {
		this.pkDiagnosisAppoMapperId = pkDiagnosisAppoMapperId;
	}

	public String getLocalUhid() {
		return localUhid;
	}

	public void setLocalUhid(String localUhid) {
		this.localUhid = localUhid;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getHospitalId() {
		return hospitalId;
	}

	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getGlobalUhid() {
		return globalUhid;
	}

	public void setGlobalUhid(Integer globalUhid) {
		this.globalUhid = globalUhid;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public String getDiagnosisDate() {
		return diagnosisDate;
	}

	public void setDiagnosisDate(String diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getDiagnosisType() {
		return diagnosisType;
	}

	public void setDiagnosisType(String diagnosisType) {
		this.diagnosisType = diagnosisType;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public String getOrganisationName() {
		return organisationName;
	}

	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	
	
	
}
