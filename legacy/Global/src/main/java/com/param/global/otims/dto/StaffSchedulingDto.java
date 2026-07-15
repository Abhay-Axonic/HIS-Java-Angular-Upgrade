package com.param.global.otims.dto;

import java.util.List;

public class StaffSchedulingDto {

	private Integer staffSchedulingId;

	private Integer userId;

	private Integer roleId;

	private String staffSchedulingDate;

	private Integer switchOverTime;

	private String fromTime;

	private String toTime;

	private Integer dayId;

	private Character status;

	private Integer orgId;

	private Integer unitId;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;
	
	private String color;
	
	private List<Integer> userList;
	
	private List<OtSchedulingTimeDetailsDto> timeDetails;
	
	private Integer offset;

	private Integer noOfRecordsPerPage;
	
	private String userName;
	
	private String roleName;
	
	private String emailId;
	
	private Integer proReqId;
	
	private List<StaffSchedulingDto> staffList; 
	
	private Integer procedureStaffScheduleId;

	public List<StaffSchedulingDto> getStaffList() {
		return staffList;
	}

	public void setStaffList(List<StaffSchedulingDto> staffList) {
		this.staffList = staffList;
	}

	public Integer getStaffSchedulingId() {
		return staffSchedulingId;
	}

	public void setStaffSchedulingId(Integer staffSchedulingId) {
		this.staffSchedulingId = staffSchedulingId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getStaffSchedulingDate() {
		return staffSchedulingDate;
	}

	public void setStaffSchedulingDate(String staffSchedulingDate) {
		this.staffSchedulingDate = staffSchedulingDate;
	}

	public Integer getSwitchOverTime() {
		return switchOverTime;
	}

	public void setSwitchOverTime(Integer switchOverTime) {
		this.switchOverTime = switchOverTime;
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

	public List<Integer> getUserList() {
		return userList;
	}

	public void setUserList(List<Integer> userList) {
		this.userList = userList;
	}

	public List<OtSchedulingTimeDetailsDto> getTimeDetails() {
		return timeDetails;
	}

	public void setTimeDetails(List<OtSchedulingTimeDetailsDto> timeDetails) {
		this.timeDetails = timeDetails;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getProcedureStaffScheduleId() {
		return procedureStaffScheduleId;
	}

	public void setProcedureStaffScheduleId(Integer procedureStaffScheduleId) {
		this.procedureStaffScheduleId = procedureStaffScheduleId;
	}

}
