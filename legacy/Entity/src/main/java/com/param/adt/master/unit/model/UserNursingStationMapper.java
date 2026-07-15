package com.param.adt.master.unit.model;

import java.util.Date;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.global.model.EmployeeMaster;

@NamedQueries({
	
	@NamedQuery(name = "CHECK_USER_NURSING_STATION_MAPPER_AVAILABLE",
	query = "SELECT userNursingStationMapper.userNursingStationMpprId as userNursingStationMpprId,"
		+ " userNursingStationMapper.userId as userId "
		+ " FROM UserNursingStationMapper userNursingStationMapper"
		+ " WHERE userNursingStationMapper.organisationId= :organisationId "
		+ " AND userNursingStationMapper.unitId= :unitId "
		+ " AND userNursingStationMapper.userId= :userId"),

	
	@NamedQuery(name = "GET_ACTIVE_USER_NURSING_STATION_MAPPER_LIST",
	query = "SELECT userNursingStationMapper.userNursingStationMpprId as userNursingStationMpprId,"
	    + " userNursingStationMapper.nursingStationId as nursingStationId,"
		+ " userNursingStationMapper.userId as userId,"
		+ " userNursingStationMapper.status as status,"
		+ " userNursingStationMapper.isDeleted as isDeleted,"
		+ " userNursingStationMapper.organisationId as organisationId,"
		+ " userNursingStationMapper.unitId as unitId "
		+ " FROM UserNursingStationMapper userNursingStationMapper"
		+ " WHERE userNursingStationMapper.status= 'A' "
		+ " AND userNursingStationMapper.organisationId= :organisationId "
		+ " AND userNursingStationMapper.unitId= :unitId "
		+ " AND userNursingStationMapper.isDeleted!= 'Y' "
		),
	
	@NamedQuery(name = "GET_DETAILED_NURSING_STATION_LIST",
	query = "SELECT userNursingStationMapper.userNursingStationMpprId as userNursingStationMpprId,"
	    + " userNursingStationMapper.nursingStationId as nursingStationId,"
		+ " userNursingStationMapper.userId as userId,"
		+ " nursingStationMaster.nursingStationDesc AS nursingStationDesc"
		+ " FROM UserNursingStationMapper userNursingStationMapper"
		+ " INNER JOIN userNursingStationMapper.nursingStationMaster nursingStationMaster"
		+ " WHERE userNursingStationMapper.userId= :userId "
		+ " AND userNursingStationMapper.organisationId= :organisationId "
		+ " AND userNursingStationMapper.unitId= :unitId "
		+ " AND userNursingStationMapper.status = 'A' "
		+ " AND nursingStationMaster.status='A' "
		+ " AND userNursingStationMapper.isDeleted!= 'Y' "
		),
	
	
	@NamedQuery(name = "UPDATE_USER_NURSING_STATION_MAPPER_STATUS",
	query = " UPDATE UserNursingStationMapper userNursingStationMapper "
			+ " SET userNursingStationMapper.status = :status"
			+ " WHERE userNursingStationMapper.organisationId= :organisationId "
			+ " AND userNursingStationMapper.unitId= :unitId "
			+ " AND userNursingStationMapper.userId = :userId"
		),
	
	@NamedQuery(name = "UPDATE_USER_NURSING_STATION_MAPPER",
	query = " UPDATE UserNursingStationMapper userNursingStationMapper "
			+ " SET userNursingStationMapper.isDeleted = :isDeleted"
			+ " WHERE userNursingStationMapper.organisationId= :organisationId "
			+ " AND userNursingStationMapper.unitId = :unitId"
			+ " AND userNursingStationMapper.userId = :userId"
		),
})

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_USER_NURSING_STATION_MAPPER_COUNT", 
			query=" select count(distinct(mppr.user_id)) as \"userId\" "
				+ " from adt.t_user_nursing_station_mapper mppr "
				+ " inner join public.m_employee_master em " 
				+ " on mppr.user_id=em.user_id "
				+ " inner join public.m_user_master um " 
				+ " on mppr.user_id=um.user_id "
				+ " where mppr.organisation_id= :organisationId"
				+ " and mppr.unit_id= :unitId"
			),
	
	@NamedNativeQuery(name="GET_USER_NURSING_STATION_MAPPER_LIST", 
	query=" select distinct(emp.user_id) as \"userId\", "
			+ " concat(emp.first_name,' ',emp.middle_name,' ',emp.last_name) as \"employeeName\", mppr.status as \"status\", emp.emp_code as \"empCode\"  "
			+ " from adt.t_user_nursing_station_mapper mppr "
			+ " inner join public.m_employee_master emp " 
			+ " on mppr.user_id=emp.user_id "
			+ " where mppr.organisation_id= :organisationId"
			+ " and mppr.unit_id= :unitId"
	),
	
	@NamedNativeQuery(name="GET_USER_NURSING_STATION_MAPPER_BY_ID", 
	query=" select mppr.user_nursing_station_mppr_id as \"userNursingStationMpprId\", mppr.nursing_station_id as \"nursingStationId\", "
			+ " mppr.user_id as \"userId\", mppr.organisation_id as \"organisationId\", mppr.unit_id as \"unitId\", emp.emp_code as \"empCode\" ,"
			+ " (coalesce(emp.first_name, '')||' '||coalesce(emp.middle_name, '')||' '||coalesce(emp.last_name, '')) as \"employeeName\", nsm.nursing_station_desc as\"nursingStationDesc\", mppr.status as \"status\" "
			+ " from adt.t_user_nursing_station_mapper mppr "
			+ " inner join public.m_employee_master emp " 
			+ " on mppr.user_id=emp.user_id "
			+ " inner join adt.m_nursing_station_master nsm " 
			+ " on mppr.nursing_station_id=nsm.nursing_station_id "
			+ " where mppr.organisation_id= :organisationId"
			+ " and mppr.unit_id= :unitId"
			+ " and mppr.user_id= :userId"
			+ " and mppr.is_deleted= 'N'"
			+ " ORDER BY nsm.nursing_station_desc ASC"
	),
	
	@NamedNativeQuery(name = "GET_LIST_OF_USER",
	query = "select " 
			 + " 	employeema0_.user_id as \"userId\", " 
			 + " 	(coalesce(employeema0_.first_name, '')|| ' ' || coalesce(employeema0_.middle_name, '')|| ' ' || coalesce(employeema0_.last_name, '')) as \"employeeName\", " 
			 + " 	employeema0_.emp_code as \"empCode\" " 
			 + " from " 
			 + " 	public.m_employee_master employeema0_ " 
			 + " inner join public.t_user_unit_mapper usermaster1_ on " 
			 + " 	employeema0_.user_id = usermaster1_.user_id " 
			 + " where " 
			 + " 	employeema0_.organization_id =:organisationId " 
			 + " 	and usermaster1_.unit_id =:unitId " 
			 + " 	and employeema0_.status='A' "
			 + " 	and usermaster1_.status='A' "
			+ "  ORDER BY employeema0_.first_name ASC "
		),
	
	@NamedNativeQuery(name = "GET_LIST_OF_USER_FOR_STORE",
	query = "select " 
			 + " 	employeema0_.user_id as \"userId\", " 
			 + " 	(coalesce(employeema0_.first_name, '')|| ' ' || coalesce(employeema0_.middle_name, '')|| ' ' || coalesce(employeema0_.last_name, '')) as \"employeeName\", " 
			 + " 	employeema0_.emp_code as \"empCode\" " 
			 + " from " 
			 + " 	public.m_employee_master employeema0_ " 
			 + " inner join public.t_user_unit_mapper usermaster1_ on " 
			 + " 	employeema0_.user_id = usermaster1_.user_id " 
			 + " where " 
			 + " 	employeema0_.organization_id =:organisationId " 
			 + " 	and usermaster1_.unit_id =:unitId " 
			 + " 	and employeema0_.status='A' "
			 + " 	and usermaster1_.status='A' "
			+ " union "
			+ " select "
			+ "doctorMaster.user_id as \"userId\", " 
			+ "CONCAT  (doctorMaster.first_name, ' ', doctorMaster.middle_name, ' ',doctorMaster.last_name) as  \"employeeName\", "
			+ "coalesce(doctorMaster.doc_code,'') as \"empCode\" "
			+ "from "
			+ "doctor.m_doctor_master doctorMaster "
		    + "inner join public.t_user_unit_mapper usermaster1_ on  "
		 	+ "doctorMaster.user_id = usermaster1_.user_id "
		    + "where "
		 	+ "doctorMaster.organization_id =:organisationId " 
		 	+ "and usermaster1_.unit_id =:unitId " 
		 	+ "and doctorMaster.status='A' "
		 	+ "and usermaster1_.status='A' "
		),
	
	  

})

@Entity
@Table(name="t_user_nursing_station_mapper",schema="adt")
@SequenceGenerator(name="t_user_nursing_station_mapper_seq",sequenceName="adt.t_user_nursing_station_mapper_seq",allocationSize=1)
public class UserNursingStationMapper {
	@Id
	@Column(name="user_nursing_station_mppr_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="t_user_nursing_station_mapper_seq")
	private Integer userNursingStationMpprId;
	
	@Column(name="nursing_station_id")
	private Integer nursingStationId;
	
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="status") 
	private Character status;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="created_by")
	private Integer createdBy;

	@Column(name="updated_by")
	private Integer updatedBy;

	@Column(name="updated_date")
	private Date updatedDate;

	@Column(name="is_deleted")
	private Character isDeleted;

	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="nursing_station_id", insertable=false, updatable=false)
	private NursingStationMaster nursingStationMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", insertable=false, updatable=false)
	private UserMaster userMaster;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="user_id", insertable=false, updatable=false)
	private EmployeeMaster employeeMaster;


	public Integer getUserNursingStationMpprId() {
		return userNursingStationMpprId;
	}


	public void setUserNursingStationMpprId(Integer userNursingStationMpprId) {
		this.userNursingStationMpprId = userNursingStationMpprId;
	}


	public Integer getNursingStationId() {
		return nursingStationId;
	}


	public void setNursingStationId(Integer nursingStationId) {
		this.nursingStationId = nursingStationId;
	}


	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	public Integer getOrganisationId() {
		return organisationId;
	}


	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}


	public Integer getUnitId() {
		return unitId;
	}


	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}


	public Character getStatus() {
		return status;
	}


	public void setStatus(Character status) {
		this.status = status;
	}


	public Date getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}


	public Integer getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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


	public Character getIsDeleted() {
		return isDeleted;
	}


	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}


	public NursingStationMaster getNursingStationMaster() {
		return nursingStationMaster;
	}


	public void setNursingStationMaster(NursingStationMaster nursingStationMaster) {
		this.nursingStationMaster = nursingStationMaster;
	}

}
