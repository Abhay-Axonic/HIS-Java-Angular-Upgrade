package com.param.bloodBank.dto;

public class PatientRequisitionDetailsDto {

	private Integer bloodComponentOrderId;
	private String	bloodComponentDesc;
	private Integer	bloodComponentId;
	private	 Integer doctorId;
	private String	doctorName;
	private	 Integer organizationId;
	private String	patientName;
	private String	requisitionDate;
	private	 String requisitionNumber;
	private String 	requisitionOnDate;
	private Integer bloodUnit;
	private Integer bloodGroupId;
	private String bloodGroupDesc;
	private Character isRequired;
	private Integer unitId;
	private Integer patientRequisitionId;
	private Integer requisitionTypeId;
	private Integer hospitalId;
	private Integer requiredBloodGroupId;
	private String diagnosis;
	private Integer patientBloodGroupId;
	private Integer componentId;
	private String remark;
	private boolean exportedToCrossMatch;
	private boolean exportedToReservation;
	private Integer patientId;
	private Integer admissionId;
	private Integer createdBy;
	private Integer updatedBy;
	private String updatedDate;
	private String createdDate;
	private Character status;
	private String fromDate;
	private String toDate;
	private String uhidNumber;
	private String bloodGroupName;
	private String visitTypeName;
	private String dob;
	private String requiredBloodGroup;
	private Boolean isRequisitionClosed;
	private String issueDate;
	private String requisitionType;
	private Character byCrossChecking;

	public Integer getBloodComponentOrderId() {
		return bloodComponentOrderId;
	}
	public void setBloodComponentOrderId(Integer bloodComponentOrderId) {
		this.bloodComponentOrderId = bloodComponentOrderId;
	}
	public String getBloodComponentDesc() {
		return bloodComponentDesc;
	}
	public void setBloodComponentDesc(String bloodComponentDesc) {
		this.bloodComponentDesc = bloodComponentDesc;
	}
	public Integer getBloodComponentId() {
		return bloodComponentId;
	}
	public void setBloodComponentId(Integer bloodComponentId) {
		this.bloodComponentId = bloodComponentId;
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
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getRequisitionDate() {
		return requisitionDate;
	}
	public void setRequisitionDate(String requisitionDate) {
		this.requisitionDate = requisitionDate;
	}
	public String getRequisitionOnDate() {
		return requisitionOnDate;
	}
	public void setRequisitionOnDate(String requisitionOnDate) {
		this.requisitionOnDate = requisitionOnDate;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Integer getBloodUnit() {
		return bloodUnit;
	}
	public void setBloodUnit(Integer bloodUnit) {
		this.bloodUnit = bloodUnit;
	}
	public Integer getBloodGroupId() {
		return bloodGroupId;
	}
	public void setBloodGroupId(Integer bloodGroupId) {
		this.bloodGroupId = bloodGroupId;
	}
	public String getBloodGroupDesc() {
		return bloodGroupDesc;
	}
	public void setBloodGroupDesc(String bloodGroupDesc) {
		this.bloodGroupDesc = bloodGroupDesc;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Character getIsRequired() {
		return isRequired;
	}
	public void setIsRequired(Character isRequired) {
		this.isRequired = isRequired;
	}
	public Integer getPatientRequisitionId() {
		return patientRequisitionId;
	}
	public void setPatientRequisitionId(Integer patientRequisitionId) {
		this.patientRequisitionId = patientRequisitionId;
	}
	public Integer getRequisitionTypeId() {
		return requisitionTypeId;
	}
	public void setRequisitionTypeId(Integer requisitionTypeId) {
		this.requisitionTypeId = requisitionTypeId;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public Integer getRequiredBloodGroupId() {
		return requiredBloodGroupId;
	}
	public void setRequiredBloodGroupId(Integer requiredBloodGroupId) {
		this.requiredBloodGroupId = requiredBloodGroupId;
	}
	public String getDiagnosis() {
		return diagnosis;
	}
	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}
	public Integer getPatientBloodGroupId() {
		return patientBloodGroupId;
	}
	public void setPatientBloodGroupId(Integer patientBloodGroupId) {
		this.patientBloodGroupId = patientBloodGroupId;
	}
	public Integer getComponentId() {
		return componentId;
	}
	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public boolean getExportedToCrossMatch() {
		return exportedToCrossMatch;
	}
	public void setExportedToCrossMatch(boolean exportedToCrossMatch) {
		this.exportedToCrossMatch = exportedToCrossMatch;
	}
	public boolean getExportedToReservation() {
		return exportedToReservation;
	}
	public void setExportedToReservation(boolean exportedToReservation) {
		this.exportedToReservation = exportedToReservation;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
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
	public String getUhidNumber() {
		return uhidNumber;
	}
	public void setUhidNumber(String uhidNumber) {
		this.uhidNumber = uhidNumber;
	}
	public String getBloodGroupName() {
		return bloodGroupName;
	}
	public void setBloodGroupName(String bloodGroupName) {
		this.bloodGroupName = bloodGroupName;
	}
	public String getVisitTypeName() {
		return visitTypeName;
	}
	public void setVisitTypeName(String visitTypeName) {
		this.visitTypeName = visitTypeName;
	}
	public String getDob() {
		return dob;
	}
	public void setDob(String dob) {
		this.dob = dob;
	}
		public Boolean getIsRequisitionClosed() {
		return isRequisitionClosed;
	}
	public void setIsRequisitionClosed(Boolean isRequisitionClosed) {
		this.isRequisitionClosed = isRequisitionClosed;
	}
	public String getRequiredBloodGroup() {
		return requiredBloodGroup;
	}
	public void setRequiredBloodGroup(String requiredBloodGroup) {
		this.requiredBloodGroup = requiredBloodGroup;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getRequisitionType() {
		return requisitionType;
	}
	public void setRequisitionType(String requisitionType) {
		this.requisitionType = requisitionType;
	}
	public Character getByCrossChecking() {
		return byCrossChecking;
	}
	public void setByCrossChecking(Character byCrossChecking) {
		this.byCrossChecking = byCrossChecking;
	}
	public String getRequisitionNumber() {
		return requisitionNumber;
	}
	public void setRequisitionNumber(String requisitionNumber) {
		this.requisitionNumber = requisitionNumber;
	}
	
}
