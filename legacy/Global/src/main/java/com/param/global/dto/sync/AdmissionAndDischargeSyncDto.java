package com.param.global.dto.sync;

import java.util.List;

public class AdmissionAndDischargeSyncDto {
	private Integer hospitalId;
	private Integer admissionId;
	private Integer unitId;
	private Integer globalUhid;
	private String admissionNumber;
	private String visitType;
	private Integer doctorId;
	private String doctorName;
	private String speciality;
	private String ward;
	private String room;
	private String remark;
	private String bedCategory;
	private String dateOfAdmission;
	private String dischargeDate;
	private String organisationName;
	private Integer pkAdmissionDetailsId;
	private String localUhid;
	private Integer applicationId;
	private Integer dischargeSummaryId;
	private String pdfGenerationUrl;
	
	private Integer dischargeId;
	private String dischargeType;
	private String reason;
	private String dischargeRemark;
	private String dischargeTime;
	private String filePath;
	private Integer pkDischargeDetailsId;
	private List<MedicationAppointmentMapperSyncDto> medicationList;
	private List<ServiceappoServiceMpprSyncDto> serviceAppoMapperList;
	private List<AppointmentVitalMapperSyncDto> vitalMapperList;
	private List<DiagnosisAppoMapperSyncDto> diagnosisAppoMapperList;
	private String uuid;
	private Integer globalOrgId;
	private Integer globalUnitId;
	private Integer channelId;
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public Integer getAdmissionId() {
		return admissionId;
	}
	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
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
	public String getAdmissionNumber() {
		return admissionNumber;
	}
	public void setAdmissionNumber(String admissionNumber) {
		this.admissionNumber = admissionNumber;
	}
	public String getVisitType() {
		return visitType;
	}
	public void setVisitType(String visitType) {
		this.visitType = visitType;
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
	public String getSpeciality() {
		return speciality;
	}
	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}
	public String getWard() {
		return ward;
	}
	public void setWard(String ward) {
		this.ward = ward;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getBedCategory() {
		return bedCategory;
	}
	public void setBedCategory(String bedCategory) {
		this.bedCategory = bedCategory;
	}
	public String getDateOfAdmission() {
		return dateOfAdmission;
	}
	public void setDateOfAdmission(String dateOfAdmission) {
		this.dateOfAdmission = dateOfAdmission;
	}
	public String getDischargeDate() {
		return dischargeDate;
	}
	public void setDischargeDate(String dischargeDate) {
		this.dischargeDate = dischargeDate;
	}
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	public Integer getPkAdmissionDetailsId() {
		return pkAdmissionDetailsId;
	}
	public void setPkAdmissionDetailsId(Integer pkAdmissionDetailsId) {
		this.pkAdmissionDetailsId = pkAdmissionDetailsId;
	}
	public String getLocalUhid() {
		return localUhid;
	}
	public void setLocalUhid(String localUhid) {
		this.localUhid = localUhid;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public Integer getDischargeId() {
		return dischargeId;
	}
	public void setDischargeId(Integer dischargeId) {
		this.dischargeId = dischargeId;
	}
	public String getDischargeType() {
		return dischargeType;
	}
	public void setDischargeType(String dischargeType) {
		this.dischargeType = dischargeType;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getDischargeRemark() {
		return dischargeRemark;
	}
	public void setDischargeRemark(String dischargeRemark) {
		this.dischargeRemark = dischargeRemark;
	}
	public String getDischargeTime() {
		return dischargeTime;
	}
	public void setDischargeTime(String dischargeTime) {
		this.dischargeTime = dischargeTime;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Integer getPkDischargeDetailsId() {
		return pkDischargeDetailsId;
	}
	public void setPkDischargeDetailsId(Integer pkDischargeDetailsId) {
		this.pkDischargeDetailsId = pkDischargeDetailsId;
	}
	public String getPdfGenerationUrl() {
		return pdfGenerationUrl;
	}
	public void setPdfGenerationUrl(String pdfGenerationUrl) {
		this.pdfGenerationUrl = pdfGenerationUrl;
	}
	public Integer getDischargeSummaryId() {
		return dischargeSummaryId;
	}
	public void setDischargeSummaryId(Integer dischargeSummaryId) {
		this.dischargeSummaryId = dischargeSummaryId;
	}
	public List<MedicationAppointmentMapperSyncDto> getMedicationList() {
		return medicationList;
	}
	public void setMedicationList(List<MedicationAppointmentMapperSyncDto> medicationList) {
		this.medicationList = medicationList;
	}
	public List<ServiceappoServiceMpprSyncDto> getServiceAppoMapperList() {
		return serviceAppoMapperList;
	}
	public void setServiceAppoMapperList(List<ServiceappoServiceMpprSyncDto> serviceAppoMapperList) {
		this.serviceAppoMapperList = serviceAppoMapperList;
	}
	public List<AppointmentVitalMapperSyncDto> getVitalMapperList() {
		return vitalMapperList;
	}
	public void setVitalMapperList(List<AppointmentVitalMapperSyncDto> vitalMapperList) {
		this.vitalMapperList = vitalMapperList;
	}
	public List<DiagnosisAppoMapperSyncDto> getDiagnosisAppoMapperList() {
		return diagnosisAppoMapperList;
	}
	public void setDiagnosisAppoMapperList(List<DiagnosisAppoMapperSyncDto> diagnosisAppoMapperList) {
		this.diagnosisAppoMapperList = diagnosisAppoMapperList;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Integer getGlobalOrgId() {
		return globalOrgId;
	}
	public void setGlobalOrgId(Integer globalOrgId) {
		this.globalOrgId = globalOrgId;
	}
	public Integer getGlobalUnitId() {
		return globalUnitId;
	}
	public void setGlobalUnitId(Integer globalUnitId) {
		this.globalUnitId = globalUnitId;
	}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
}
