package com.param.opd.appointment.dto;

public class AppointmentMasterDto {
	
	private int appointmentId;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private Integer slotId;
	
	private Integer patientId;
	
	private String remark;
	
	private Character status;
	
	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;
	
	private Integer priorityId;
	
	private Integer counterId;
	
	
	private Integer patientUserId;

	private Integer doctorUserId;
	
	private Integer applicationId;
	
	private String uuid;
	
	private Integer globalUhid;
	
	private String hisApiUrl;
	
	private Integer hisUnitId;

	public Integer getPriorityId() {
		return priorityId;
	}

	public void setPriorityId(Integer priorityId) {
		this.priorityId = priorityId;
	}

	public int getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
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

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Integer getPatientUserId() {
		return patientUserId;
	}

	public void setPatientUserId(Integer patientUserId) {
		this.patientUserId = patientUserId;
	}

	public Integer getDoctorUserId() {
		return doctorUserId;
	}

	public void setDoctorUserId(Integer doctorUserId) {
		this.doctorUserId = doctorUserId;
	}

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getGlobalUhid() {
		return globalUhid;
	}

	public void setGlobalUhid(Integer globalUhid) {
		this.globalUhid = globalUhid;
	}

	public String getHisApiUrl() {
		return hisApiUrl;
	}

	public void setHisApiUrl(String hisApiUrl) {
		this.hisApiUrl = hisApiUrl;
	}

	public Integer getHisUnitId() {
		return hisUnitId;
	}

	public void setHisUnitId(Integer hisUnitId) {
		this.hisUnitId = hisUnitId;
	}

	
}
