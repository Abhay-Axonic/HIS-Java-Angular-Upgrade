package com.param.otims.model;

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

import com.param.adt.master.global.model.UserMaster;
import com.param.global.common.LocalTimeConverter;
import com.param.global.model.UserRoleMaster;
import com.param.global.common.InstantConverter;


@NamedQueries({
	
	@NamedQuery(name="TO_CHECK_USED_STAFF_SLOT",
	query="SELECT mstr.procedureStaffScheduleId as procedureStaffScheduleId "
				+ " FROM ProcedureStaffSchedule mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND to_char(mstr.prodeureDate,'yyyy-MM-dd')= :staffScheDate "
				+ " AND mstr.userId= :userId "
				+ " AND mstr.roleId= :roleId "
				+ " AND mstr.status ='A' "
				+ " AND ((to_char(mstr.fromTime,'hh24:mi:ss') <= :fromTime  AND to_char(mstr.toTime,'hh24:mi:ss')  >= :fromTime ) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss') <= :toTime AND to_char(mstr.toTime,'hh24:mi:ss') >= :toTime) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss')  >= :fromTime   AND to_char(mstr.toTime,'hh24:mi:ss') <= :toTime))"),
	
	@NamedQuery(name="TO_CHECK_USED_STAFF_SLOT_WITH_PRO_REQ_ID",
	query="SELECT mstr.procedureStaffScheduleId as procedureStaffScheduleId "
				+ " FROM ProcedureStaffSchedule mstr "
				+ " WHERE mstr.organizationId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND to_char(mstr.prodeureDate,'yyyy-MM-dd')= :staffScheDate "
				+ " AND mstr.userId = :userId "
				+ " AND mstr.roleId = :roleId "
				+ " AND mstr.proReqId <> :proReqId "
				+ " AND mstr.status ='A' "
				+ " AND ((to_char(mstr.fromTime,'hh24:mi:ss') <= :fromTime  AND to_char(mstr.toTime,'hh24:mi:ss')  >= :fromTime ) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss') <= :toTime AND to_char(mstr.toTime,'hh24:mi:ss') >= :toTime) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss')  >= :fromTime   AND to_char(mstr.toTime,'hh24:mi:ss') <= :toTime))"),
	
	@NamedQuery(name="GET_PROCEDURE_STAFF_SCHEDULE_BY_PRO_SCHEDULE_ID",
	query="SELECT distinct mstr.procedureStaffScheduleId as procedureStaffScheduleId , " 
			+ " mstr.proReqId as proReqId , "
			+ " mstr.roleId as roleId, "
			+ " mstr.userId as userId, "
			+ " mstr.color as color , "
			+ " to_char(mstr.prodeureDate,'YYYY-MM-DD') as prodeureDate, "
			+ " to_char(mstr.fromTime,'hh24:mi') as fromTime, "
			+ " to_char(mstr.toTime,'hh24:mi') as toTime, "
			+ " otimsRoleMaster.roleName as roleName, "
			+ " mstr.procedureScheduleId as procedureScheduleId, "
			+" (case when ( empList.userId is not null )then "
			+ " (concat(coalesce(prefix.prefixCode),'. ',coalesce(empList.firstName), ' ', coalesce(empList.middleName), ' ', coalesce(empList.lastName))) "
			+" else "
			+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) as staffName, "
			//+ " concat(coalesce(prefix.prefixCode),'.',' ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as staffName, "
			+ " mstr.organizationId as organizationId, "
			+ " mstr.unitId as unitId, "
			+ " mstr.createdBy as createdBy, "
			+ " mstr.updatedBy as updatedBy, "
			+ " to_char(mstr.createdDate, 'dd-MM-yyyy HH:ss:mm')as createdDate, "
			+ " to_char(mstr.updatedDate, 'dd-MM-yyyy HH:ss:mm')as updatedDate, "
			+ " mstr.status as status "
			+ " FROM ProcedureStaffSchedule mstr "
			+ " INNER JOIN  mstr.otimsRoleMaster otimsRoleMaster  "
			+ " INNER JOIN  mstr.userMaster userMaster  "
			+ " LEFT JOIN userMaster.doctorMastersList doc "
			  +"LEFT JOIN userMaster.employeeMastersList empList  "
			+ " LEFT JOIN empList.prefixMaster prefix "
			+ " WHERE  mstr.organizationId= :organizationId "
			+ " AND mstr.unitId= :unitId "
			+ " AND mstr.procedureScheduleId= :procedureScheduleId "
			+ " AND mstr.status = 'A' "),
	
	@NamedQuery(name="GET_PROCEDURE_SCHEDULE_SURGEON_LIST",
	query="SELECT distinct mstr.procedureStaffScheduleId as procedureStaffScheduleId , " 
			+ " mstr.userId as userId, "
			+ " roleMaster.roleName as roleName,"
			+" (case when ( empList.userId is not null )then "
			+ " (concat(coalesce(prefix.prefixCode),'. ',coalesce(empList.firstName), ' ', coalesce(empList.middleName), ' ', coalesce(empList.lastName))) "
			+" else "
			+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) as staffName "
			//+ " concat(coalesce(prefix.prefixCode),'.',' ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as staffName "
			+ " FROM ProcedureStaffSchedule mstr "
			+ " INNER JOIN  mstr.userMaster userMaster  "
			+ " INNER JOIN userMaster.userMasterList  userRoleMapp "
			+ " INNER JOIN  mstr.otimsRoleMaster roleMaster  "
			+ " LEFT JOIN userMaster.doctorMastersList doc "
			+ " LEFT JOIN userMaster.employeeMastersList empList  "
			+ " LEFT JOIN empList.prefixMaster prefix "
			+ " WHERE   mstr.procedureScheduleId= :procedureScheduleId "
			+ " AND roleMaster.roleTypeId = 8"
			+ " AND mstr.status = 'A' "),
	
	@NamedQuery(name="GET_PROCEDURE_SCHEDULE_SURGEON_LIST_FOR_CALENDER",
	query="SELECT distinct mstr.procedureStaffScheduleId as procedureStaffScheduleId , " 
			+ " mstr.userId as userId, "
			+ " roleMaster.roleName as roleName,"
			+" (case when ( empList.userId is not null )then "
			+ " (concat(coalesce(prefix.prefixCode),'. ',coalesce(empList.firstName), ' ', coalesce(empList.middleName), ' ', coalesce(empList.lastName))) "
			+" else "
			+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) as staffName "
			+ " FROM ProcedureStaffSchedule mstr "
			+ " INNER JOIN  mstr.userMaster userMaster  "
			+ " INNER JOIN userMaster.userMasterList  userRoleMapp "
			+ " INNER JOIN  mstr.otimsRoleMaster roleMaster  "
			+ " LEFT JOIN userMaster.doctorMastersList doc "
			+ " LEFT JOIN userMaster.employeeMastersList empList  "
			+ " LEFT JOIN empList.prefixMaster prefix "
			+ " WHERE   mstr.proReqId= :proReqId "
			+ " AND roleMaster.roleTypeId = 8"
			+ " AND mstr.status = 'A' "),
})

@Entity	
@Table(name = "t_procedure_staff_schedule", schema = "otims")
@SequenceGenerator(name = "procedure_staff_schedule_seq", sequenceName = "otims.procedure_staff_schedule_seq", allocationSize = 1)
public class ProcedureStaffSchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "procedure_staff_schedule_seq")
	@Column(name = "procedure_staff_schedule_id ")
	private Integer procedureStaffScheduleId ;
	
	@Column(name = "pro_req_id")
	private Integer proReqId;
	
	@Column(name = "procedure_id")
	private Integer procedureId;
	
	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "user_id ")
	private Integer userId ;
	
	@Column(name = "prodeure_date")
	private Date prodeureDate;
	
	@Column(name = "from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	@Column(name = "to_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;
	
	@Column(name = "status")
	private char status;
			
	@Column(name = "organization_id")
	private Integer organizationId;
	
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
	
	@Column(name = "procedure_schedule_id")
	private Integer procedureScheduleId;
	
	@Column(name = "color")
	private String color;
	
	@Column(name = "remark")
	private String remark;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	UserRoleMaster otimsRoleMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	UserMaster userMaster;
	
	public Integer getProcedureStaffScheduleId() {
		return procedureStaffScheduleId;
	}

	public void setProcedureStaffScheduleId(Integer procedureStaffScheduleId) {
		this.procedureStaffScheduleId = procedureStaffScheduleId;
	}

	public Integer getProReqId() {
		return proReqId;
	}

	public void setProReqId(Integer proReqId) {
		this.proReqId = proReqId;
	}

	public Integer getProcedureId() {
		return procedureId;
	}

	public void setProcedureId(Integer procedureId) {
		this.procedureId = procedureId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Date getProdeureDate() {
		return prodeureDate;
	}

	public void setProdeureDate(Date prodeureDate) {
		this.prodeureDate = prodeureDate;
	}

	public Instant getFromTime() {
		return fromTime;
	}

	public void setFromTime(Instant fromTime) {
		this.fromTime = fromTime;
	}

	public Instant getToTime() {
		return toTime;
	}

	public void setToTime(Instant toTime) {
		this.toTime = toTime;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getProcedureScheduleId() {
		return procedureScheduleId;
	}

	public void setProcedureScheduleId(Integer procedureScheduleId) {
		this.procedureScheduleId = procedureScheduleId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
