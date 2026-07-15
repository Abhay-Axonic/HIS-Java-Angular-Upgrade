package com.param.global.dto.sync;

public class AppointmentSyncSchedulingDto {
	private Integer doctorId;
	private Integer appointmentId;
	private Integer hospitalId;
	private Integer unitId;
	private Integer appointmentStatusId;
	private String appointmentDate;
	private String appointmentTime;
	private Integer applicationId;
	private Integer patientId;
	private Integer createdBy;
	private String createdDate;
	private Integer doctorLocalId;
	private Integer departmentId;
	private String departmentName;
	private String doctorName;
	private Character directEncounter;
	private boolean applicationFlag;
	private String appointmentEndTime;
	private Integer visitTypeId;
	private Integer isVideo;
	private Integer globalOrgId;
	private Integer globalUnitId;
	private String uuid;
	private Integer channelId;
	private Character isAppointmentFromMylife;
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public boolean isApplicationFlag() {
		return applicationFlag;
	}
	public void setApplicationFlag(boolean applicationFlag) {
		this.applicationFlag = applicationFlag;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public Integer getDoctorLocalId() {
		return doctorLocalId;
	}
	public void setDoctorLocalId(Integer doctorLocalId) {
		this.doctorLocalId = doctorLocalId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
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
	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}
	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}
	public String getAppointmentDate() {
		return appointmentDate;
	}
	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public String getAppointmentTime() {
		return appointmentTime;
	}
	public void setAppointmentTime(String appointmentTime) {
		this.appointmentTime = appointmentTime;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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
	public Character getDirectEncounter() {
		return directEncounter;
	}
	public void setDirectEncounter(Character directEncounter) {
		this.directEncounter = directEncounter;
	}
	public String getAppointmentEndTime() {
		return appointmentEndTime;
	}
	public void setAppointmentEndTime(String appointmentEndTime) {
		this.appointmentEndTime = appointmentEndTime;
	}
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public Integer getIsVideo() {
		return isVideo;
	}
	public void setIsVideo(Integer isVideo) {
		this.isVideo = isVideo;
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
	public Character getIsAppointmentFromMylife() {
		return isAppointmentFromMylife;
	}
	public void setIsAppointmentFromMylife(Character isAppointmentFromMylife) {
		this.isAppointmentFromMylife = isAppointmentFromMylife;
	}
	
}
