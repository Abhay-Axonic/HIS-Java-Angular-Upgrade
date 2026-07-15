package com.param.global.otims.dto;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

public class SurgicalCalendarDetailsDto {
	
	private Integer surgicalCalenderId;
	
	private Integer specialityId;
	
	private String speciality;

	private Integer doctorId;
	
	private String doctorName;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer dayId;
	
	private String day;

	private String fromTime;
	
	private String toTime;
	
	private Character status;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer organisationId;
	
	private Integer unitId;
	
	private Character isDeleted;
	
	private Integer calenderStatusId;
	
	private String procedureDate;
	
	private Date procedureInDate;
	
	private Instant fromTimeToSave;

	private Instant toTimeToSave;
	
	private String  uhid;
	
	private String  patientDetails;
	
	private String  procedureName;

	
	public Integer getSurgicalCalenderId() {
		return surgicalCalenderId;
	}

	public void setSurgicalCalenderId(Integer surgicalCalenderId) {
		this.surgicalCalenderId = surgicalCalenderId;
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

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

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

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
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

	public Integer getCalenderStatusId() {
		return calenderStatusId;
	}

	public void setCalenderStatusId(Integer calenderStatusId) {
		this.calenderStatusId = calenderStatusId;
	}

	public String getProcedureDate() {
		return procedureDate;
	}

	public void setProcedureDate(String procedureDate) {
		this.procedureDate = procedureDate;
	}

	public Instant getFromTimeToSave() {
		return fromTimeToSave;
	}

	public void setFromTimeToSave(Instant fromTimeToSave) {
		this.fromTimeToSave = fromTimeToSave;
	}

	public Instant getToTimeToSave() {
		return toTimeToSave;
	}

	public void setToTimeToSave(Instant toTimeToSave) {
		this.toTimeToSave = toTimeToSave;
	}

	public String getSpeciality() {
		return speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	public String getDoctorName() {
		return doctorName;
	}

	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getUhid() {
		return uhid;
	}

	public void setUhid(String uhid) {
		this.uhid = uhid;
	}

	public String getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(String patientDetails) {
		this.patientDetails = patientDetails;
	}

	public Date getProcedureInDate() {
		return procedureInDate;
	}

	public void setProcedureInDate(Date procedureInDate) {
		this.procedureInDate = procedureInDate;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}


}
