package com.param.global.org.common.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.model.IntakeOutputChartDetailsMaster;
import com.param.global.model.IntakeOutputChartMaster;
import com.param.global.model.OutputChartDetailsMaster;
import com.param.global.common.InstantConverter;

@NamedQueries({
	@NamedQuery(name="GET_SHIFT_MASTER_LIST_BY_ID",
			query="SELECT shiftMaster.shiftId AS shiftId,"
					+ " shiftMaster.shiftName AS shiftName,"
					+ " to_char(shiftMaster.fromTime,'HH24:MI') as fromTime,"
					+ " to_char(shiftMaster.toTime,'HH24:MI') as toTime,"
					+ " shiftMaster.status AS status,shiftMaster.isWeeklyOff AS isWeeklyOff "
					+ " FROM ShiftMaster shiftMaster "
					+ " WHERE shiftMaster.organizationId=:orgId"
					+ " AND shiftMaster.unitId=:unitId "
					+ " AND shiftMaster.shiftId=:shiftId"),
	
	@NamedQuery(name="GET_SHIFT_MASTER_LIST",
	query="SELECT shiftMaster.shiftId AS shiftId,"
			+ " shiftMaster.shiftName AS shiftName,"
			+ " to_char(shiftMaster.fromTime,'HH24:MI') as fromTime,"
			+ " to_char(shiftMaster.toTime,'HH24:MI') as toTime,"
			+ " shiftMaster.status AS status,shiftMaster.isWeeklyOff AS isWeeklyOff "
			+ " FROM ShiftMaster shiftMaster "
			+ " WHERE shiftMaster.organizationId=:orgId"
			+ " AND shiftMaster.unitId=:unitId "),
	
	@NamedQuery(name="GET_ACTIVE_SHIFT_MASTER_LIST",
	query="SELECT shiftMaster.shiftId AS shiftId,"
			+ " shiftMaster.shiftName AS shiftName,"
			+ " to_char(shiftMaster.fromTime,'HH24:MI') as fromTime,"
			+ " to_char(shiftMaster.toTime,'HH24:MI') as toTime,"
			+ " shiftMaster.status AS status,shiftMaster.isWeeklyOff AS isWeeklyOff "
			+ " FROM ShiftMaster shiftMaster "
			+ " WHERE shiftMaster.organizationId=:orgId"
			+ " AND shiftMaster.unitId=:unitId "
			+ " AND shiftMaster.status='A' "
			+ " ORDER BY shiftMaster.shiftName ASC "),
	
	
	@NamedQuery(name="GET_SHIFT_MASTER_LIST_BY_NAME",
	query="SELECT shiftMaster.shiftId AS shiftId,"
			+ " shiftMaster.shiftName AS shiftName"	
			+ " FROM ShiftMaster shiftMaster "
			+ " WHERE LOWER(shiftMaster.shiftName)=:shiftName "
			+ " AND shiftMaster.organizationId=:orgId"
			+ " AND shiftMaster.unitId=:unitId"),
	
	@NamedQuery(name="GET_SHIFT_MASTER_LIST_BY_NAME_NOT_BY_ID",
	query="SELECT shiftMaster.shiftId AS shiftId,"
			+ " shiftMaster.shiftName AS shiftName"	
			+ " FROM ShiftMaster shiftMaster "
			+ " WHERE LOWER(shiftMaster.shiftName)=:shiftName"
			+ " AND shiftMaster.shiftId!=:shiftId "
			+ " AND shiftMaster.organizationId=:orgId "
			+ " AND shiftMaster.unitId=:unitId")
})
@Entity
@Table(name = "m_shift_master", schema = "public")
@SequenceGenerator(name = "m_shift_master_seq", sequenceName = "public.m_shift_master_seq", allocationSize = 1)
public class ShiftMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_shift_master_seq")
	@Column(name = "shift_id")
	private Integer shiftId;
	
	@Column(name = "shift_name")
	private String shiftName;
	
	@Column(name="from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	@Column(name="to_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "is_weekly_off")
	private Character isWeeklyOff;

	
	public Integer getShiftId() {
		return shiftId;
	}


	public Instant getFromTime() {
		return fromTime;
	}

	public Instant getToTime() {
		return toTime;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Character getStatus() {
		return status;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public Date getUpdatedDate() {
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


	public void setFromTime(Instant fromTime) {
		this.fromTime = fromTime;
	}

	public void setToTime(Instant toTime) {
		this.toTime = toTime;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public void setUpdatedDate(Date updatedDate) {
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


	public Character getIsWeeklyOff() {
		return isWeeklyOff;
	}


	public void setIsWeeklyOff(Character isWeeklyOff) {
		this.isWeeklyOff = isWeeklyOff;
	}
	
	

}
