package com.param.entity.model.pharmacy;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.lis.common.LocalTimeConverter;

@NamedQueries({
		@NamedQuery(name = "GET_SCHEDULE_BY_SCHEDULE_ID", 
		query = "SELECT scheduleMaster.scheduleId as id,"
				+ " scheduleMaster.scheduleCode as code,"
				+ " scheduleMaster.scheduleDescription as desc,"
				+ " scheduleMaster.status as status,"
				+ " scheduleMaster.createdBy as createdBy,"
				+ " scheduleMaster.createdDate as createdDate,"
				+ " scheduleMaster.updatedBy as updatedBy,"
				+ " scheduleMaster.orgId as orgId," 
				+ " scheduleMaster.unitId as unitId,"
				+ " scheduleMaster.updatedDate as updatedDate," 
				+ " scheduleMaster.colorCode as colorCode" 
				+ " FROM ScheduleMaster scheduleMaster"
				+ " WHERE scheduleMaster.orgId=:orgId " 
//				+ " AND scheduleMaster.unitId=:unitId "
				+ " AND scheduleMaster.scheduleId=:scheduleId "),

		@NamedQuery(name = "GET_SCHEDULE_BY_CODE", 
		query = "SELECT scheduleMaster.scheduleId as id,"
				+ " scheduleMaster.scheduleCode as code,"
				+ " scheduleMaster.scheduleDescription as desc,"
				+ " scheduleMaster.status as status,"
				+ " scheduleMaster.createdBy as createdBy,"
				+ " scheduleMaster.createdDate as createdDate,"
				+ " scheduleMaster.updatedBy as updatedBy,"
				+ " scheduleMaster.orgId as orgId," 
				+ " scheduleMaster.unitId as unitId,"
				+ " scheduleMaster.updatedDate as updatedDate" 
				+ " FROM ScheduleMaster scheduleMaster"
				+ " WHERE scheduleMaster.orgId=:orgId " 
				+ " AND scheduleMaster.unitId=:unitId "
				+ " AND lower(scheduleMaster.scheduleCode) = lower(:code)"),

		@NamedQuery(name = "UPDATE_GET_SCHEDULE_BY_CODE", 
				query ="SELECT scheduleMaster.scheduleId as id,"
				+ " scheduleMaster.scheduleCode as code,"
				+ " scheduleMaster.scheduleDescription as desc,"
				+ " scheduleMaster.status as status,"
				+ " scheduleMaster.createdBy as createdBy,"
				+ " scheduleMaster.createdDate as createdDate,"
				+ " scheduleMaster.updatedBy as updatedBy,"
				+ " scheduleMaster.orgId as orgId," 
				+ " scheduleMaster.unitId as unitId,"
				+ " scheduleMaster.updatedDate as updatedDate" 
				+ " FROM ScheduleMaster scheduleMaster"
				+ " WHERE scheduleMaster.orgId=:orgId " 
//				+ " AND scheduleMaster.unitId=:unitId "
				+ " AND lower(scheduleMaster.scheduleCode) = lower(:code)"
				+ " AND scheduleMaster.scheduleId <> :scheduleId "),

		@NamedQuery(name = "GET_SCHEDULE_LIST", 
		query = "SELECT scheduleMaster.scheduleId as scheduleId,"
				+ " scheduleMaster.scheduleCode as scheduleCode,"
				+ " scheduleMaster.scheduleDescription as scheduleDescription,"
				+ " scheduleMaster.status as status,"
				+ " scheduleMaster.createdBy as createdBy,"
				+ " scheduleMaster.createdDate as createdDate,"
				+ " scheduleMaster.updatedBy as updatedBy,"
				+ " scheduleMaster.orgId as orgId," 
				+ " scheduleMaster.unitId as unitId,"
				+ " scheduleMaster.updatedDate as updatedDate" 
				+ " FROM ScheduleMaster scheduleMaster"
				+ " WHERE scheduleMaster.orgId=:orgId " 
//				+ " AND scheduleMaster.unitId=:unitId "
				),
				
		@NamedQuery(name = "GET_PAGINATED_SCHEDULE_MASTER_LIST", 
		query = "SELECT scheduleMaster.scheduleId as id,"
				+ " scheduleMaster.scheduleCode as code,"
				+ " scheduleMaster.scheduleDescription as desc,"
				+ " scheduleMaster.status as status,"
				+ " scheduleMaster.createdBy as createdBy,"
				+ " scheduleMaster.createdDate as createdDate,"
				+ " scheduleMaster.updatedBy as updatedBy,"
				+ " scheduleMaster.orgId as orgId," 
				+ " scheduleMaster.unitId as unitId,"
				+ " scheduleMaster.updatedDate as updatedDate" 
				+ " FROM ScheduleMaster scheduleMaster"
				+ " WHERE scheduleMaster.orgId=:orgId " 
//				+ " AND scheduleMaster.unitId=:unitId "
				+ " ORDER BY id DESC")

})
@NamedNativeQueries({
		@NamedNativeQuery(name = "GET_TOTAL_SCHEDULE_RECORD", query = "select count(*) from pharmacy.m_schedule_master where "
				+ "org_id = :orgId ") })

@Entity
@Table(name = "m_schedule_master", schema = "pharmacy")
@SequenceGenerator(name = "m_schedule_master_schedule_id_seq", sequenceName = "pharmacy.m_schedule_master_schedule_id_seq", allocationSize = 1)
public class ScheduleMaster {
	@Id
	@Column(name = "schedule_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_schedule_master_schedule_id_seq")
	private Integer scheduleId;
	@Column(name = "schedule_code")
	private String scheduleCode;

	@Column(name = "schedule_description")
	private String scheduleDescription;

	@Column(name = "schedule_status")
	private Character status;

	@Column(name = "crated_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	
	private Date updatedDate;

	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "color_code")
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

	public String getColorCode() {
		return colorCode;
	}

	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}


}
