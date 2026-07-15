package com.param.otims.model;

import java.time.Instant;
import java.time.LocalTime;
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

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedQueries({

	/*@NamedQuery(name="GET_STAFF_SCHEDULING",
	query="SELECT mstr.staffSchedulingId as staffSchedulingId, "
				+ " mstr.userId as userId, "
				+ " mstr.roleId as roleId, "
				+ " to_char(mstr.fromTime,'hh24:mi:ss') as fromTime, "
				+ " to_char(mstr.toTime,'hh24:mi:ss') as toTime, "
				+ " mstr.dayId as dayId, "
				+ " mstr.status as status, "
				+ " to_char(mstr.staffSchedulingDate,'DD/MM/YYYY') as staffSchedulingDate, "
				+ " mstr.switchOverTime as switchOverTime, "
				+ " FROM OtScheduling mstr "
				+ " INNER JOIN mstr.operationTable opTable "
				+ " INNER JOIN mstr.operationTheatre opTheatre"
				+ " WHERE mstr.orgId= :organizationId "
				+ " AND mstr.unitId= :unitId "),*/
	
	@NamedQuery(name="TO_CHECK_ALREADY_EXITS_STAFF_SCHEDULING",
	query="SELECT mstr.staffSchedulingId as staffSchedulingId "
				+ " FROM StaffScheduling mstr "
				+ " WHERE mstr.orgId= :organizationId "
				+ " AND mstr.unitId= :unitId "
				+ " AND mstr.userId= :userId "
				+ " AND to_char(mstr.staffSchedulingDate,'yyyy-MM-dd')= :staffScheDate "
				+ " AND mstr.roleId= :roleId "
				+ " AND ((to_char(mstr.fromTime,'hh24:mi:ss') <= :fromTime  AND to_char(mstr.toTime,'hh24:mi:ss')  >= :fromTime ) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss') <= :toTime AND to_char(mstr.toTime,'hh24:mi:ss') >= :toTime) " 
				+" or (to_char(mstr.fromTime,'hh24:mi:ss')  >= :fromTime   AND to_char(mstr.toTime,'hh24:mi:ss') <= :toTime))"),


@NamedQuery(name="TO_CHECK_AVAIBILITY_SCHEDULING",
query="SELECT mstr.staffSchedulingId as staffSchedulingId ,"
			+ " mstr.switchOverTime as switchOverTime "
			+ " FROM StaffScheduling mstr "
			+ " WHERE mstr.orgId= :organizationId "
			+ " AND mstr.unitId= :unitId "
			+ " AND mstr.userId= :userId "
			+ " AND to_char(mstr.staffSchedulingDate,'yyyy-MM-dd')= :staffScheDate "
			+ " AND mstr.roleId= :roleId "
			+ " AND (to_char(mstr.fromTime,'hh24:mi:ss') <= :fromTime  AND to_char(mstr.toTime,'hh24:mi:ss')  >= :toTime ) ")
})

@NamedNativeQueries({
	
	@NamedNativeQuery(name="GET_STAFF_SCHEDULING",
			query=" select  distinct concat(coalesce(em.first_name,'') ,' ', coalesce(em.middle_name,'') ,' ', coalesce(em.last_name,'')) as \"userName\"  , " + 
					" ss.staff_scheduling_id as \"staffSchedulingId\" , " + 
					" to_char(ss.staff_scheduling_date,'DD/MM/YYYY') as \"staffSchedulingDate\" , " + 
					" ss.switch_over_time as \"switchOverTime\" , " + 
					" to_char(ss.from_time,'hh24:mm:ss') as \"fromTime\" , " + 
					" to_char(ss.to_time,'hh24:mm:ss') as \"toTime\" , " + 
					" ss.day_id as \"dayId\" ,  " + 
					" ss.status as \"status\" , " + 
					" otm.role_name as \"roleName\" " + 
					" from  otims.t_staff_scheduling ss " + 
					" inner join public.m_user_master um on ss.user_id = um.user_id " + 
					" inner join public.m_employee_master em on em.user_id = um.user_id " + 
					" inner join public.t_user_role_mapper orm  on orm.user_id = um.user_id " + 
					" inner join  public.m_role_master otm on otm.role_id = orm.role_id  "
					+ " where ss.organization_id = :organizationId " 
					+ " and ss.unit_id = :unitId "
					+ " and otm.role_type_id in(8,9,10,11,12) "
					+ " union all " + 
					"  select distinct concat('Dr.',' ',coalesce(dm.first_name,'') ,' ', coalesce(dm.middle_name,'') ,' ', coalesce(dm.last_name,'')) as \"userName\" ," + 
					" ss.staff_scheduling_id as \"staffSchedulingId\" , " + 
					" to_char(ss.staff_scheduling_date,'DD/MM/YYYY') as \"staffSchedulingDate\" , " + 
					" ss.switch_over_time as \"switchOverTime\" , " + 
					" to_char(ss.from_time,'hh24:mm:ss') as \"fromTime\" , " + 
					" to_char(ss.to_time,'hh24:mm:ss') as \"toTime\" , " + 
					" ss.day_id as \"dayId\" ,  " + 
					" ss.status as \"status\" , " + 
					" otm.role_name as \"roleName\" " + 
					" from  otims.t_staff_scheduling ss " + 
					" inner join public.m_user_master um on ss.user_id = um.user_id " + 
					" inner join doctor.m_doctor_master dm  on dm.user_id = um.user_id " + 
					" inner join public.t_user_role_mapper orm  on orm.user_id = um.user_id " + 
					" inner join  public.m_role_master otm on otm.role_id = orm.role_id "
					+ " where ss.organization_id = :organizationId " + 
					" and ss.unit_id = :unitId " + 
					" and otm.role_type_id in(8,9,10,11,12) order by 2  desc ")
})

@Entity
@Table(name = "t_staff_scheduling", schema = "otims")
@SequenceGenerator(name = "staff_scheduling_seq", sequenceName = "otims.staff_scheduling_seq", allocationSize = 1)
public class StaffScheduling {
	
	@Id
	@Column(name = "staff_scheduling_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_scheduling_seq")
	private Integer staffSchedulingId;
	
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="role_id")
	private Integer roleId;
	
	@Column(name = "staff_scheduling_date")
	private Date staffSchedulingDate;
	
	@Column(name="switch_over_time")
	private Integer switchOverTime;
	
	@Column(name="from_time")
	@Convert(converter = InstantConverter.class)
	private Instant fromTime;
	
	@Column(name="to_time")
	@Convert(converter = InstantConverter.class)
	private Instant toTime;

	@Column(name="day_id")
	private Integer dayId;
	
	@Column(name="status")
	private Character status;
	
	@Column(name = "organization_id")
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

	public Date getStaffSchedulingDate() {
		return staffSchedulingDate;
	}

	public void setStaffSchedulingDate(Date staffSchedulingDate) {
		this.staffSchedulingDate = staffSchedulingDate;
	}

	public Integer getSwitchOverTime() {
		return switchOverTime;
	}

	public void setSwitchOverTime(Integer switchOverTime) {
		this.switchOverTime = switchOverTime;
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
