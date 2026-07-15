package com.param.global.dto.sync;

public class DoctorScheduleDeleteReqDto {
	
	private String regNo;
	
	private String scheduleDate;
	
	private String fromTime;
	
	private String toTime;
	
	private Integer hospitalId;
	
	private Integer unitId;
	
	private Integer applicationId;
	
	private Integer globalOrgId;
	
	private Integer globalUnitId;
	
	private Integer globalDocId;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer departmentId;
	
	private Boolean deleteAllSlots;
	private Integer channelId;
	public Boolean getDeleteAllSlots() {
		return deleteAllSlots;
	}

	public void setDeleteAllSlots(Boolean deleteAllSlots) {
		this.deleteAllSlots = deleteAllSlots;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
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

	public Integer getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
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

	public Integer getGlobalDocId() {
		return globalDocId;
	}

	public void setGlobalDocId(Integer globalDocId) {
		this.globalDocId = globalDocId;
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
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
}
