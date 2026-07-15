package com.param.global.dto;

public class ScheduleMasterDto {

	private Integer doctorId;
	private Integer specialityId;
	private String scheduleDate;
	/*private List<TimeSlotsDto> listTimeSlotDto;*/
	private String fromTime;
	private String toTime;
	private Integer organisationId;
	private Integer unitId;
	private Integer createdBy;
	private String createdDate;
	private Integer updatedBy;
	private String updatedDate;
	private Integer timePerSlot;
	private String fromDate;
	private String toDate;
	private Character deleteAllSlots;
	private Integer addRemove;
	
	public Integer getAddRemove() {
		return addRemove;
	}
	public void setAddRemove(Integer addRemove) {
		this.addRemove = addRemove;
	}
	public Character getDeleteAllSlots() {
		return deleteAllSlots;
	}
	public void setDeleteAllSlots(Character deleteAllSlots) {
		this.deleteAllSlots = deleteAllSlots;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
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
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public String getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	/*public List<TimeSlotsDto> getListTimeSlotDto() {
		return listTimeSlotDto;
	}
	public void setListTimeSlotDto(List<TimeSlotsDto> listTimeSlotDto) {
		this.listTimeSlotDto = listTimeSlotDto;
	}*/
	public Integer getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	public Integer getTimePerSlot() {
		return timePerSlot;
	}
	public void setTimePerSlot(Integer timePerSlot) {
		this.timePerSlot = timePerSlot;
	}
	
}
