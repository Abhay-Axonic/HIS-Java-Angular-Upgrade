package com.param.global.dto;

import java.time.*;

import com.param.global.common.GlobalCommonDateUtils;

public class WeekendMasterDto {
	
	private Integer weekendId;
	
	private Integer unitId;
	
	private Integer organizationId;
	
	private String weekendDate;
	
	private Integer dayId;
	
	private char isHalfDay;
	
	private Instant fromTime;
	
	private Instant toTime;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private char status;
	
	private String year;
	
	private String day;
	
	private String fromTimeString;
	
	private String toTimeString;
	
	private Integer offset;
	
	private Integer noOfRecordsPerPage;
	
	public String getFromTimeString() {
		return fromTimeString;
	}

	public void setFromTimeString(String fromTimeString) {
		this.fromTimeString = fromTimeString;
	}

	public String getToTimeString() {
		return toTimeString;
	}

	public void setToTimeString(String toTimeString) {
		this.toTimeString = toTimeString;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Integer getWeekendId() {
		return weekendId;
	}

	public void setWeekendId(Integer weekendId) {
		this.weekendId = weekendId;
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

	public String getWeekendDate() {
		return weekendDate;
	}

	public void setWeekendDate(String weekendDate) {
		this.weekendDate = weekendDate;
	}

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public char getIsHalfDay() {
		return isHalfDay;
	}

	public void setIsHalfDay(char isHalfDay) {
		this.isHalfDay = isHalfDay;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Instant getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		if (fromTime!=null && !fromTime.equals(""))
		{
			LocalTime time = LocalTime.parse(fromTime);
			LocalDate today = LocalDate.now();
			this.fromTime = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.fromTime = midnight.toInstant(ZoneOffset.UTC);

		}
	}

	public Instant getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		if (toTime!=null && !toTime.equals(""))
		{
			LocalTime time = LocalTime.parse(toTime);
			LocalDate today = LocalDate.now();
			this.toTime = time.atDate(today).atZone(ZoneId.of("UTC")).toInstant();
//			System.out.println("timeslot=" + this.encounterTime);
		} else {
			LocalDateTime midnight= LocalDate.now().atTime(0, 0);
			this.toTime = midnight.toInstant(ZoneOffset.UTC);

		}
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}

	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}

	


}
