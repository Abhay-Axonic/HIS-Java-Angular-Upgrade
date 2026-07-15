package com.param.global.dto.sync;

import java.util.List;

public class DoctorScheduleSyncDto {
	private Integer scheduleId;
	private Integer doctorId;
	private Integer departmentId;
	private String scheduleDate;
	private String fromTime;
	private String toTime;
	private Character availableStatus;
	private Integer createdBy;
	private String createdDate;
	private Integer unitId;
	private Integer hospitalId;
	private Integer applicationId;
	private String fromDate;
	private String toDate;
	private Integer doctorLocalId;
	private Integer globalOrgId;
	private Integer globalUnitId;
	private Integer type;
	private Boolean deleteAllSlots;
	private List<DoctorScheduleVisitSyncDto> listDoctorScheduleVisitSyncDto;
	private List<Integer> dayIds;
	private Integer channelId;
	public Boolean getDeleteAllSlots() {
		return deleteAllSlots;
	}
	public void setDeleteAllSlots(Boolean deleteAllSlots) {
		this.deleteAllSlots = deleteAllSlots;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public List<Integer> getDayIds() {
		return dayIds;
	}
	public void setDayIds(List<Integer> dayIds) {
		this.dayIds = dayIds;
	}
	public List<DoctorScheduleVisitSyncDto> getListDoctorScheduleVisitSyncDto() {
		return listDoctorScheduleVisitSyncDto;
	}
	public void setListDoctorScheduleVisitSyncDto(List<DoctorScheduleVisitSyncDto> listDoctorScheduleVisitSyncDto) {
		this.listDoctorScheduleVisitSyncDto = listDoctorScheduleVisitSyncDto;
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
	public Integer getDoctorLocalId() {
		return doctorLocalId;
	}
	public void setDoctorLocalId(Integer doctorLocalId) {
		this.doctorLocalId = doctorLocalId;
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
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Integer getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}
	public String getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public String getFromTime() {
		return fromTime;
	}
	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}
	public String getToTime() {
		return toTime;
	}
	public void setToTime(String toTime) {
		this.toTime = toTime;
	}
	public Character getAvailableStatus() {
		return availableStatus;
	}
	public void setAvailableStatus(Character availableStatus) {
		this.availableStatus = availableStatus;
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
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	
}
