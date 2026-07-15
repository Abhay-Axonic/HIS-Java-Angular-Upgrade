package com.param.opd.coversheet.dto;

import java.util.List;

import com.param.global.dto.SlotPatientDetails;

public class WeeklySlotsDto {

	private Integer slotId;
	
	private Integer specialityId;
	
	private Integer doctorId;
	
	private String appointmentDate;
	
	private String appointmentDate2;
	
	private String fromTimeString;
	
	private String fromTimeString2;
	
	private String toTimeString;
	
	private String toTimeString2;
	
	private String doctorName;
	
	private String specialityName;
	
	private Integer slotStatusId;
	
	private Integer isBlockedUnblocked;
	
	private Integer slotPatientDetailsListsSize;
	
	private String originalFromTime;
	
	private Integer slotTypeId;
	
	private Integer dayId;

	private String day;
	
	private Character isVip;
	
	private boolean result;
	
	private Integer allowedOverBookingSlots;
	
	private String registrationNumber;
	
	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public Character getIsVip() {
		return isVip;
	}

	public void setIsVip(Character isVip) {
		this.isVip = isVip;
	}

	public Integer getAllowedOverBookingSlots() {
		return allowedOverBookingSlots;
	}

	public void setAllowedOverBookingSlots(Integer allowedOverBookingSlots) {
		this.allowedOverBookingSlots = allowedOverBookingSlots;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public Integer getSlotTypeId() {
		return slotTypeId;
	}

	public void setSlotTypeId(Integer slotTypeId) {
		this.slotTypeId = slotTypeId;
	}

	private List<SlotPatientDetails> slotPatientDetailsList;
	
	public Integer getSlotPatientDetailsListsSize() {
		return slotPatientDetailsListsSize;
	}

	public void setSlotPatientDetailsListsSize(Integer slotPatientDetailsListsSize) {
		this.slotPatientDetailsListsSize = slotPatientDetailsListsSize;
	}

	public Integer getSlotId() {
		return slotId;
	}

	public void setSlotId(Integer slotId) {
		this.slotId = slotId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public String getAppointmentDate() {
		return appointmentDate;
	}

	public void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}

	public String getAppointmentDate2() {
		return appointmentDate2;
	}

	public void setAppointmentDate2(String appointmentDate2) {
		this.appointmentDate2 = appointmentDate2;
	}

	public String getFromTimeString() {
		return fromTimeString;
	}

	public void setFromTimeString(String fromTimeString) {
		this.fromTimeString = fromTimeString;
	}

	public String getFromTimeString2() {
		return fromTimeString2;
	}

	public void setFromTimeString2(String fromTimeString2) {
		this.fromTimeString2 = fromTimeString2;
	}

	public String getToTimeString() {
		return toTimeString;
	}

	public void setToTimeString(String toTimeString) {
		this.toTimeString = toTimeString;
	}

	public String getToTimeString2() {
		return toTimeString2;
	}

	public void setToTimeString2(String toTimeString2) {
		this.toTimeString2 = toTimeString2;
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

	public Integer getSlotStatusId() {
		return slotStatusId;
	}

	public void setSlotStatusId(Integer slotStatusId) {
		this.slotStatusId = slotStatusId;
	}

	public Integer getIsBlockedUnblocked() {
		return isBlockedUnblocked;
	}

	public void setIsBlockedUnblocked(Integer isBlockedUnblocked) {
		this.isBlockedUnblocked = isBlockedUnblocked;
	}

	public List<SlotPatientDetails> getSlotPatientDetailsList() {
		return slotPatientDetailsList;
	}

	public void setSlotPatientDetailsList(List<SlotPatientDetails> slotPatientDetailsList) {
		this.slotPatientDetailsList = slotPatientDetailsList;
	}

	public String getOriginalFromTime() {
		return originalFromTime;
	}

	public void setOriginalFromTime(String originalFromTime) {
		this.originalFromTime = originalFromTime;
	}

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	
	
	
}
