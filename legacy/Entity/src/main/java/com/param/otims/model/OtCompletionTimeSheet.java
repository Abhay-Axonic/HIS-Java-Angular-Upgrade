package com.param.otims.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_OT_TIME_SHEETS_BY_PRO_REQ_ID",
					 query = "select tm.ot_completion_time_sheet_id as \"otCompletionTimeSheetId\", to_char(tm.wheel_in_time,'DD-MM-YYYY HH24:MI:SS') as \"wheelInTime\", "
							 + " to_char(tm.table_on_time,'DD-MM-YYYY HH24:MI:SS') as \"tableOnTime\",to_char(tm.anesthesia_time,'DD-MM-YYYY HH24:MI:SS') as \"anesthesiaTime\", "
							 + " to_char(tm.procedure_start_time,'DD-MM-YYYY HH24:MI:SS') as \"procedureStartTime\",to_char(tm.procedure_end_time,'DD-MM-YYYY HH24:MI:SS') as \"procedureEndTime\", "
							 + " to_char(tm.table_out_time,'DD-MM-YYYY HH24:MI:SS') as \"tableOutTime\",to_char(tm.wheel_out_time,'DD-MM-YYYY HH24:MI:SS') as \"wheelOutTime\", "
							 + " to_char(tm.schedule_discharge,'DD-MM-YYYY HH24:MI:SS') as \"scheduleDischarge\",to_char(tm.actual_discharge,'DD-MM-YYYY HH24:MI:SS') as \"actualDischarge\", "
							 + " to_char(tm.created_date,'DD-MM-YYYY HH24:MI:SS') as \"createdDate\",tm.status as \"status\", tm.org_id as \"orgId\", tm.unit_id as \"unitId\",  "
							 + " tm.created_by as \"createdBy\", tm.pro_req_id as \"proReqId\" "
							 + " from otims.t_ot_completion_time_sheet tm "
							 + " where tm.pro_req_id = :id and tm.status = 'A' ")
})
@Entity
@Table(name = "t_ot_completion_time_sheet" , schema = "otims")
@SequenceGenerator(name = "t_ot_completion_time_sheet_seq" , sequenceName = "otims.t_ot_completion_time_sheet_seq" , allocationSize = 1)
public class OtCompletionTimeSheet {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_ot_completion_time_sheet_seq")
	@Column(name = "ot_completion_time_sheet_id")
	private int otCompletionTimeSheetId;
	
	@Column(name = "wheel_in_time")
	private Date wheelInTime;
	
	@Column(name = "table_on_time")
	private Date tableOnTime;
	
	@Column(name = "anesthesia_time")
	private Date anesthesiaTime;
	
	@Column(name = "procedure_start_time")
	private Date procedureStartTime;
	
	@Column(name = "procedure_end_time")
	private Date procedureEndTime;
	
	@Column(name = "table_out_time")
	private Date tableOutTime;
	
	@Column(name = "wheel_out_time")
	private Date wheelOutTime;
	
	@Column(name = "schedule_discharge")
	private Date scheduleDischarge;
	
	@Column(name = "actual_discharge")
	private Date actualDischarge;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public int getOtCompletionTimeSheetId() {
		return otCompletionTimeSheetId;
	}

	public void setOtCompletionTimeSheetId(int otCompletionTimeSheetId) {
		this.otCompletionTimeSheetId = otCompletionTimeSheetId;
	}

	public Date getWheelInTime() {
		return wheelInTime;
	}

	public void setWheelInTime(Date wheelInTime) {
		this.wheelInTime = wheelInTime;
	}

	public Date getTableOnTime() {
		return tableOnTime;
	}

	public void setTableOnTime(Date tableOnTime) {
		this.tableOnTime = tableOnTime;
	}

	public Date getAnesthesiaTime() {
		return anesthesiaTime;
	}

	public void setAnesthesiaTime(Date anesthesiaTime) {
		this.anesthesiaTime = anesthesiaTime;
	}

	public Date getProcedureStartTime() {
		return procedureStartTime;
	}

	public void setProcedureStartTime(Date procedureStartTime) {
		this.procedureStartTime = procedureStartTime;
	}

	public Date getProcedureEndTime() {
		return procedureEndTime;
	}

	public void setProcedureEndTime(Date procedureEndTime) {
		this.procedureEndTime = procedureEndTime;
	}

	public Date getTableOutTime() {
		return tableOutTime;
	}

	public void setTableOutTime(Date tableOutTime) {
		this.tableOutTime = tableOutTime;
	}

	public Date getWheelOutTime() {
		return wheelOutTime;
	}

	public void setWheelOutTime(Date wheelOutTime) {
		this.wheelOutTime = wheelOutTime;
	}

	public Date getScheduleDischarge() {
		return scheduleDischarge;
	}

	public void setScheduleDischarge(Date scheduleDischarge) {
		this.scheduleDischarge = scheduleDischarge;
	}

	public Date getActualDischarge() {
		return actualDischarge;
	}

	public void setActualDischarge(Date actualDischarge) {
		this.actualDischarge = actualDischarge;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
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
	
	
}
