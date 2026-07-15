package com.param.pharmacy.master.global.dto.equipment;

import java.util.Date;

public class ScheduleMasterDto {

	private Integer scheduleId;
	private String scheduleCode;
	private String scheduleDescription;
	private Character status;
	private Integer createdBy;
	private Date createdDate;
	private Integer updatedBy;
	private Date updatedDate;
	private Integer orgId;
	private Integer unitId;
	private Integer id;
	private String code;
	private String desc;
	private String colorCode;
	
	public Integer getScheduleId() {
		return scheduleId;
	}
	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getScheduleCode() {
		return scheduleCode;
	}
	public void setScheduleCode(String scheduleCode) {
		this.scheduleCode = scheduleCode;
	}
	public String getScheduleDescription() {
		return scheduleDescription;
	}
	public void setScheduleDescription(String scheduleDescription) {
		this.scheduleDescription = scheduleDescription;
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
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return "ScheduleMasterDto [scheduleId=" + scheduleId + ", scheduleCode=" + scheduleCode
				+ ", scheduleDescription=" + scheduleDescription + ", status=" + status + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate
				+ ", orgId=" + orgId + ", unitId=" + unitId + ", id=" + id + ", code=" + code + ", desc=" + desc + "]";
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

}
