package com.param.opd.encounter.dto;

public class EncounterAssignmenMapperDto {

	private Integer encounterDetailsMapperId ;
	private Character isAssignmentCompStatus;
	private String assignmentStartTime;
	private Integer assignmentStartBy;
	private Integer assignmentStopBy;
	private Integer assignmentStartRoleId;
	private Integer assignmentStopRoleId;
	private String assignmentStopTime;
	private Character isConsultationStatus;
	private String consultationStartTime;
	private Integer consultationStartBy;
	private Integer consultationStopBy;
	private Integer consultationStartRoleId;
	private Integer consultationStopRoleId;
	private String consultationStopTime;
	private Integer unitId;
	private Integer organizationId;
	private Integer encounterId;
	private Integer patientId;
	private Integer appointmentId;
	private Integer updatedBy;
	private Character isFromCoverSheet;
	private Integer visitTypeId;
	private String visitType;
	private String doctorName;
	private String specialityName;
	private String encounterDate;
	private Integer admissionId;
	private Integer doctorId;
	private EncounterMasterDto encounterMasterDto;
	private Character isPriority;
	private Character isNursingAssessmentSkipped;
	private Integer childEncounterId;
	private Integer orderDetailsId;
	private Integer consultDoctorId;
	private Character allowOtherDoctorsToConsult;
	private Integer otherDoctorId;
	private Character isSendMailOnMailPrescription;
	
	private Character isEHC;
	public Integer getOtherDoctorId() {
		return otherDoctorId;
	}
	public void setOtherDoctorId(Integer otherDoctorId) {
		this.otherDoctorId = otherDoctorId;
	}
	public Character getAllowOtherDoctorsToConsult() {
		return allowOtherDoctorsToConsult;
	}
	public void setAllowOtherDoctorsToConsult(Character allowOtherDoctorsToConsult) {
		this.allowOtherDoctorsToConsult = allowOtherDoctorsToConsult;
	}
	public Integer getConsultDoctorId() {
		return consultDoctorId;
	}
	public void setConsultDoctorId(Integer consultDoctorId) {
		this.consultDoctorId = consultDoctorId;
	}
	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}
	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}
	public Character getIsNursingAssessmentSkipped() {
		return isNursingAssessmentSkipped;
	}
	public void setIsNursingAssessmentSkipped(Character isNursingAssessmentSkipped) {
		this.isNursingAssessmentSkipped = isNursingAssessmentSkipped;
	}
	public Character getIsPriority() {
		return isPriority;
	}
	public void setIsPriority(Character isPriority) {
		this.isPriority = isPriority;
	}
	public Integer getEncounterDetailsMapperId() {
		return encounterDetailsMapperId;
	}
	public void setEncounterDetailsMapperId(Integer encounterDetailsMapperId) {
		this.encounterDetailsMapperId = encounterDetailsMapperId;
	}
	public Character getIsAssignmentCompStatus() {
		return isAssignmentCompStatus;
	}
	public void setIsAssignmentCompStatus(Character isAssignmentCompStatus) {
		this.isAssignmentCompStatus = isAssignmentCompStatus;
	}
	public String getAssignmentStartTime() {
		return assignmentStartTime;
	}
	public void setAssignmentStartTime(String assignmentStartTime) {
		this.assignmentStartTime = assignmentStartTime;
	}
	public Integer getAssignmentStartBy() {
		return assignmentStartBy;
	}
	public void setAssignmentStartBy(Integer assignmentStartBy) {
		this.assignmentStartBy = assignmentStartBy;
	}
	public Integer getAssignmentStopBy() {
		return assignmentStopBy;
	}
	public void setAssignmentStopBy(Integer assignmentStopBy) {
		this.assignmentStopBy = assignmentStopBy;
	}
	public Integer getAssignmentStartRoleId() {
		return assignmentStartRoleId;
	}
	public void setAssignmentStartRoleId(Integer assignmentStartRoleId) {
		this.assignmentStartRoleId = assignmentStartRoleId;
	}
	public Integer getAssignmentStopRoleId() {
		return assignmentStopRoleId;
	}
	public void setAssignmentStopRoleId(Integer assignmentStopRoleId) {
		this.assignmentStopRoleId = assignmentStopRoleId;
	}
	public String getAssignmentStopTime() {
		return assignmentStopTime;
	}
	public void setAssignmentStopTime(String assignmentStopTime) {
		this.assignmentStopTime = assignmentStopTime;
	}
	public Character getIsConsultationStatus() {
		return isConsultationStatus;
	}
	public void setIsConsultationStatus(Character isConsultationStatus) {
		this.isConsultationStatus = isConsultationStatus;
	}
	public String getConsultationStartTime() {
		return consultationStartTime;
	}
	public void setConsultationStartTime(String consultationStartTime) {
		this.consultationStartTime = consultationStartTime;
	}
	public Integer getConsultationStartBy() {
		return consultationStartBy;
	}
	public void setConsultationStartBy(Integer consultationStartBy) {
		this.consultationStartBy = consultationStartBy;
	}
	public Integer getConsultationStopBy() {
		return consultationStopBy;
	}
	public void setConsultationStopBy(Integer consultationStopBy) {
		this.consultationStopBy = consultationStopBy;
	}
	public Integer getConsultationStartRoleId() {
		return consultationStartRoleId;
	}
	public void setConsultationStartRoleId(Integer consultationStartRoleId) {
		this.consultationStartRoleId = consultationStartRoleId;
	}
	public Integer getConsultationStopRoleId() {
		return consultationStopRoleId;
	}
	public void setConsultationStopRoleId(Integer consultationStopRoleId) {
		this.consultationStopRoleId = consultationStopRoleId;
	}
	public String getConsultationStopTime() {
		return consultationStopTime;
	}
	public void setConsultationStopTime(String consultationStopTime) {
		this.consultationStopTime = consultationStopTime;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}
	public Integer getEncounterId() {
		return encounterId;
	}
	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Character getIsFromCoverSheet() {
		return isFromCoverSheet;
	}
	public void setIsFromCoverSheet(Character isFromCoverSheet) {
		this.isFromCoverSheet = isFromCoverSheet;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public String getEncounterDate() {
		return encounterDate;
	}
	public void setEncounterDate(String encounterDate) {
		this.encounterDate = encounterDate;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public EncounterMasterDto getEncounterMasterDto() {
		return encounterMasterDto;
	}
	public void setEncounterMasterDto(EncounterMasterDto encounterMasterDto) {
		this.encounterMasterDto = encounterMasterDto;
	}
	public Integer getChildEncounterId() {
		return childEncounterId;
	}
	public void setChildEncounterId(Integer childEncounterId) {
		this.childEncounterId = childEncounterId;
	}
	public Character getIsSendMailOnMailPrescription() {
		return isSendMailOnMailPrescription;
	}
	public void setIsSendMailOnMailPrescription(Character isSendMailOnMailPrescription) {
		this.isSendMailOnMailPrescription = isSendMailOnMailPrescription;
	}
	public Character getIsEHC() {
		return isEHC;
	}
	public void setIsEHC(Character isEHC) {
		this.isEHC = isEHC;
	}

	
}
