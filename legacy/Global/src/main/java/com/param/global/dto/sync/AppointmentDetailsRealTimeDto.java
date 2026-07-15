package com.param.global.dto.sync;

import java.util.List;

public class AppointmentDetailsRealTimeDto {
	private String localUhid;
	private Integer hospitalId;
	private Integer applicationId;
	private Integer unitId;
	private String pdfGenerationUrl;
	private AppointmentDoctorCoversheetMapperDto coversheetMapper;
	private List<MedicationAppointmentMapperSyncDto> medicationList;
	private List<ServiceappoServiceMpprSyncDto> serviceAppoMapperList;
	private List<AppointmentVitalMapperSyncDto> vitalMapperList;
	private List<DiagnosisAppoMapperSyncDto> diagnosisAppoMapperList;
	private String uuid;
	private Integer globalOrgId;
	private Integer globalUnitId;
	private Integer channelId;
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getLocalUhid() {
		return localUhid;
	}
	public void setLocalUhid(String localUhid) {
		this.localUhid = localUhid;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
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
	public AppointmentDoctorCoversheetMapperDto getCoversheetMapper() {
		return coversheetMapper;
	}
	public void setCoversheetMapper(AppointmentDoctorCoversheetMapperDto coversheetMapper) {
		this.coversheetMapper = coversheetMapper;
	}
	public String getPdfGenerationUrl() {
		return pdfGenerationUrl;
	}
	public void setPdfGenerationUrl(String pdfGenerationUrl) {
		this.pdfGenerationUrl = pdfGenerationUrl;
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
