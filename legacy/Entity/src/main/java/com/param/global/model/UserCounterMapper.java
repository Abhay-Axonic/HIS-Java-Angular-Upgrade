package com.param.global.model;

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
	
	@NamedNativeQuery(name="TOTAL_USER_MAPPER_COUNT",
			query= "select "
					+ "  count(distinct mpper.user_id) "
					+ "from "
					+ "  public.t_user_counter_mapper mpper "
					+ "where "
					+ "  mpper.organisation_id = :orgId "
					+ "  and mpper.unit_id = :unitId "
					+ "  and mpper.is_deleted = 'N'"
			),
	
	@NamedNativeQuery(name="USER_COUNTER_MAPPER_LIST",
	query= "select "
			+ "  distinct(mpper.user_id) as \"userId\", "
			+ "  mpper.status as \"status\", "
			+ "  emp_mst.emp_code as \"employeeCode\", "
			+ "  emp_mst.first_name || ' ' || emp_mst.last_name as \"employeeName\" "
			+ "from "
			+ "  public.t_user_counter_mapper mpper "
			+ "  inner join public.m_employee_master emp_mst on mpper.user_id = emp_mst.user_id "
			+ "where "
			+ "  mpper.organisation_id = :orgId "
			+ "  and mpper.unit_id = :unitId "
			+ "  and mpper.is_deleted = 'N'"
	),
	
	@NamedNativeQuery(name="GET_COUNTER_BY_USER",
	query= "select "
			+ "  counter_mst.counter_id as \"counterId\", "
			+ "  counter_mst.counter_code as \"counterCode\", "
			+ "  counter_mst.counter_desc as \"counterDesc\" "
			+ " from "
			+ "  public.t_user_counter_mapper mpper "
			+ "  inner join public.m_counter_master counter_mst on mpper.counter_id = counter_mst.counter_id "
			+ "where "
			+ "  mpper.is_deleted = 'N' "
			+ "  and mpper.organisation_id = :orgId "
			+ "  and mpper.status = 'A' "
			+ "  and counter_mst.status = 'A' "
			+ "  and mpper.unit_id = :unitId "
			+ "  and mpper.user_id = :userId"
	),
	
	@NamedNativeQuery(name="DELETE_USER_COUNTER_MAPPER",
	query= "update "
			+ "  public.t_user_counter_mapper "
			+ "set "
			+ "  is_deleted = 'Y' "
			+ "where "
			+ "  user_id = :userId"
	),
	@NamedNativeQuery(name="UPDATE_USER_COUNTER_MAPPER_STATUS",
	query= "update "
			+ "  public.t_user_counter_mapper "
			+ "set "
			+ "  status = :status "
			+ "where "
			+ "  user_id = :userId"
	),
	
	@NamedNativeQuery(name="CHECK_USER_EXIST_OR_NOT",
	query= " select "
			+ "  count(*) "
			+ "from "
			+ "  public.t_user_counter_mapper mpper "
			+ "where "
			+ "  mpper.is_deleted = 'N' "
			+ "  and mpper.organisation_id = :organisationId "
			+ "  and mpper.unit_id = :unitId "
			+ "  and mpper.user_id = :userId"
	),
	
	@NamedNativeQuery(name="GET_COUNTER_BY_USERID",
	query= "select "
			+ "  mpper.user_counter_mppr_id as \"userCounterMpprId\", "
			+ "  mpper.user_id as \"userId\", "
			+ "  mpper.counter_id as \"counterId\", "
			+ "  mpper.status as \"status\", "
			+ "  mpper.organisation_id as \"organisationId\", "
			+ "  mpper.unit_id as \"unitId\", "
			+ "  mpper.is_deleted as \"isDeleted\", "
			+ "  coun_mst.counter_code as \"counterCode\", "
			+ "  coun_mst.counter_desc as \"name\", "
			+ "  to_char(mpper.created_date, 'DD-MM-YYYY HH:MI:SS') as \"createdDate\", "
			+ "  emp_mst.first_name || ' ' || emp_mst.last_name || '-' || emp_mst.emp_code as \"employeeName\" "
			+ "from "
			+ "  public.t_user_counter_mapper mpper "
			+ "  inner join public.m_employee_master emp_mst on mpper.user_id = emp_mst.user_id "
			+ "  inner join public.m_counter_master coun_mst on mpper.counter_id = coun_mst.counter_id "
			+ "where "
			+ "  mpper.organisation_id = :orgId "
			+ "  and mpper.unit_id = :unitId "
			+ "  and mpper.user_id = :userId "
			+ "  and mpper.is_deleted = 'N'"
			+ "  and coun_mst.status = 'A'"
	)
	

})
@Entity
@Table(name="t_user_counter_mapper",schema="public")
@SequenceGenerator(name="seq_user_counter_mapper",sequenceName="public.seq_user_counter_mapper",allocationSize=1)
public class UserCounterMapper {
	
	@Id
	@Column(name="user_counter_mppr_id")
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="seq_user_counter_mapper")
	private int userCounterMpprId;
	
	@Column(name="counter_id")
	private Integer counterId;
	
	@Column(name="user_id")
	private Integer userId;
	
	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="status")
	private char status;

	@Column(name="created_date")
	private Date createdDate;

	@Column(name="created_by")
	private int createdBy;

	@Column(name="updated_by")
	private int updatedBy;

	@Column(name="updated_date")
	private Date updatedDate;

	@Column(name="is_deleted")
	private char isDeleted;

	public int getUserCounterMpprId() {
		return userCounterMpprId;
	}

	public void setUserCounterMpprId(int userCounterMpprId) {
		this.userCounterMpprId = userCounterMpprId;
	}

	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = (status == '\u0000') ? 'A' : status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public char getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(char isDeleted) {
		this.isDeleted = (isDeleted == '\u0000') ? 'N' : isDeleted;
	}
	
	
}
