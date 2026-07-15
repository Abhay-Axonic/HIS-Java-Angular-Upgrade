package com.param.global.org.common.dto;



public class ShiftMasterDto {

	
	private Integer shiftId;
	
	private String shiftName;
	
	private String fromTime;
	
	private String toTime;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Character status;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private Integer unitId;

	private Integer organizationId;
	
	private Integer noOfRecordsPerPage;
	
	private Integer  offset;
	
	private String startTime;
	
	private String endTime;
	
	private Character isWeeklyOff;

	public Integer getShiftId() {
		return shiftId;
	}


	public String getFromTime() {
		return fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setShiftId(Integer shiftId) {
		this.shiftId = shiftId;
	}


	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}


	public String getShiftName() {
		return shiftName;
	}


	public void setShiftName(String shiftName) {
		this.shiftName = shiftName;
	}


	public Integer getNoOfRecordsPerPage() {
		return noOfRecordsPerPage;
	}


	public Integer getOffset() {
		return offset;
	}


	public void setNoOfRecordsPerPage(Integer noOfRecordsPerPage) {
		this.noOfRecordsPerPage = noOfRecordsPerPage;
	}


	public void setOffset(Integer offset) {
		this.offset = offset;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndTime() {
		return endTime;
	}


	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}


	public Character getIsWeeklyOff() {
		return isWeeklyOff;
	}


	public void setIsWeeklyOff(Character isWeeklyOff) {
		this.isWeeklyOff = isWeeklyOff;
	}
	
	
}
