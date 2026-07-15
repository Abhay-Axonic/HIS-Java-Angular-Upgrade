package com.param.global.otims.dto;

import java.util.Date;
import java.util.List;

public class SurgicalCalendarMasterDto {
	
	private Integer specialityId;

	private Integer doctorId;
	
	private String fromDate;
	
	private String toDate;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer organisationId;
	
	private Integer unitId;
	
	private String doctorName;
	
	private String specialityName;
	
	private String dayName;
	
	private String procedureDate;
	
	private Date procedureInDate;
	
	private List<SurgicalCalendarDetailsDto> surgicalCalendarDetailsDtoList;
	

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


	public String getDayName() {
		return dayName;
	}

	public void setDayName(String dayName) {
		this.dayName = dayName;
	}


	public List<SurgicalCalendarDetailsDto> getSurgicalCalendarDetailsDtoList() {
		return surgicalCalendarDetailsDtoList;
	}

	public void setSurgicalCalendarDetailsDtoList(List<SurgicalCalendarDetailsDto> surgicalCalendarDetailsDtoList) {
		this.surgicalCalendarDetailsDtoList = surgicalCalendarDetailsDtoList;
	}

	public String getProcedureDate() {
		return procedureDate;
	}

	public void setProcedureDate(String procedureDate) {
		this.procedureDate = procedureDate;
	}

	public Date getProcedureInDate() {
		return procedureInDate;
	}

	public void setProcedureInDate(Date procedureInDate) {
		this.procedureInDate = procedureInDate;
	}
	
	
	
	
}
