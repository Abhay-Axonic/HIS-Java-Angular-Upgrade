package com.param.global.otims.dto;

public class SurgicalCalendarReqDto {

	 private Integer recordPerPage;
	 private Integer offset;
	 private String procedureDate;
	 private Integer orgId;
	 private Integer unitId;
	 private Integer dayId;
	 private Integer doctorId;
	 private Integer specialityId;
	 private String fromDate;
	 private String toDate;
	 private char status;
	 

	public Integer getRecordPerPage() {
		return recordPerPage;
	}
	public void setRecordPerPage(Integer recordPerPage) {
		this.recordPerPage = recordPerPage;
	}
	public Integer getOffset() {
		return offset;
	}
	public void setOffset(Integer offset) {
		this.offset = offset;
	}
	public String getProcedureDate() {
		return procedureDate;
	}
	public void setProcedureDate(String procedureDate) {
		this.procedureDate = procedureDate;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
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
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	public Integer getDayId() {
		return dayId;
	}
	public void setDayId(Integer dayId) {
		this.dayId = dayId;
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
	 
}
