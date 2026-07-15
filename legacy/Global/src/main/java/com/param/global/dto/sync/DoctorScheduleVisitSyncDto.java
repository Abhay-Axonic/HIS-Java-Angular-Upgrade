package com.param.global.dto.sync;

public class DoctorScheduleVisitSyncDto {

	private Integer visitTypeId;
	private Character visitTypeStatus;
	private String visitDuration;
	private String visitColor;
    private Integer doctorScheduleVisitDetailId;
	private Integer scheduleId;
	private Integer createdBy;
	private String createdDate;
	private Integer applicationId;
	
	public Integer getVisitTypeId() {
		return visitTypeId;
	}
	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}
	public Character getVisitTypeStatus() {
		return visitTypeStatus;
	}
	public void setVisitTypeStatus(Character visitTypeStatus) {
		this.visitTypeStatus = visitTypeStatus;
	}
	public String getVisitDuration() {
		return visitDuration;
	}
	public void setVisitDuration(String visitDuration) {
		this.visitDuration = visitDuration;
	}
	public String getVisitColor() {
		return visitColor;
	}
	public void setVisitColor(String visitColor) {
		this.visitColor = visitColor;
	}
	public Integer getDoctorScheduleVisitDetailId() {
		return doctorScheduleVisitDetailId;
	}
	public void setDoctorScheduleVisitDetailId(Integer doctorScheduleVisitDetailId) {
		this.doctorScheduleVisitDetailId = doctorScheduleVisitDetailId;
	}
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
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
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	
	
	
}
