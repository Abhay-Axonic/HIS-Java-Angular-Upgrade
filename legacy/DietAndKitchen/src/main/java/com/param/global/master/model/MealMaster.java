package com.param.global.master.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



/*@NamedQueries({
	
	@NamedQuery(name = "MEAL_MASTER.GET_MEAL_MASTER_LIST_BY_NAME", query = "SELECT mealMaster.id as mealId "
			+ "FROM MealMaster mealMaster " + "WHERE LOWER(mealMaster.code)=:code order by mealMaster.servingTime"),
	
	@NamedQuery(name = "MEAL_MASTER.GET_MEAL_MASTER_LIST", query = "SELECT mealMaster.id as mealId,"
			+ "mealMaster.code as code,mealMaster.name as name,mealMaster.servingTime as servingTime,"
			+ "mealMaster.status as status FROM MealMaster mealMaster " + "WHERE mealMaster.status='A' order by mealMaster.servingTime"),
	
	@NamedQuery(name = "MEAL_MASTER.GET_ACTIVE_MEAL_MASTER_LIST", query = "SELECT mealMaster.id as mealId,"
			+ "mealMaster.code as code,mealMaster.name as name,mealMaster.servingTime as servingTime,"
			+ "to_char(mealMaster.updatedDate,'dd-MM-yyyy') as updatedDate,"
			+ "to_char(mealMaster.createdDate,'dd-MM-yyyy') as createdDate," + "mealMaster.updatedBy as updatedBy,"
			+ "mealMaster.createdBy as createdBy," + "mealMaster.status as status FROM MealMaster mealMaster "
			+ "WHERE mealMaster.status='A' order by mealMaster.servingTime"),
	
	@NamedQuery(name = "MEAL_MASTER.GET_MEAL_MASTER_DROP_DOWN", query = "SELECT mealMaster.id as mealId,"
			+ "mealMaster.name as mealName " + "FROM MealMaster mealMaster " + "WHERE mealMaster.status='A' order by mealMaster.servingTime"),
	
	@NamedQuery(name = "MEAL_MASTER.GET_MEAL_MASTER_BY_SERVING_TIME", query = "SELECT mealMaster.id as mealId "
			+ "FROM MealMaster mealMaster " + "WHERE mealMaster.status='A' AND mealMaster.servingTime=:servingTime "),
	
	@NamedQuery(name = "MEAL_MASTER.GET_ALL_MEAL_MASTER_LIST", query = "SELECT mealMaster.id as mealId,"
			+ "mealMaster.code as code,mealMaster.name as name,mealMaster.servingTime as servingTime,"
			+ "to_char(mealMaster.updatedDate,'dd-MM-yyyy') as updatedDate,"
			+ "to_char(mealMaster.createdDate,'dd-MM-yyyy') as createdDate," + "mealMaster.updatedBy as updatedBy,"
			+ "mealMaster.createdBy as createdBy," + "mealMaster.status as status FROM MealMaster mealMaster  order by mealMaster.servingTime") })
*/




@NamedQueries({
	
		@NamedQuery(name = "MEAL_MASTER.GET_MEAL_MASTER_LIST_BY_NAME", query = "SELECT mealMaster.id as mealId "
				+ "FROM MealMaster mealMaster " + "WHERE LOWER(mealMaster.code)=:code "
						+ "AND mealMaster.organizationId=:organizationId "//added by minesh for save"
						+ "order by mealMaster.servingTime"				
				),
		
		//added by minesh for update
		@NamedQuery(name = "MEAL_MASTER.GET_MEAL_MASTER_LIST_BY_NAME_Not_BY_ID", query = "SELECT mealMaster.id as mealId "
				+ "FROM MealMaster mealMaster " + "WHERE LOWER(mealMaster.code)=:code "
						+ "AND mealMaster.organizationId=:organizationId "
						+ "AND mealMaster.id != :mealId "
						+ "order by mealMaster.servingTime"				
				),
		
		@NamedQuery(name = "MEAL_MASTER.GET_MEAL_MASTER_LIST", query = "SELECT mealMaster.id as mealId,"
				+ "mealMaster.code as code,mealMaster.name as name,to_char(mealMaster.servingTime,'HH24:MI') as servingTime,"
				+ "mealMaster.status as status FROM MealMaster mealMaster " + "WHERE mealMaster.status='A' order by mealMaster.servingTime"),
		
		@NamedQuery(name = "MEAL_MASTER.GET_ACTIVE_MEAL_MASTER_LIST", query = "SELECT mealMaster.id as mealId, "
				+ "mealMaster.code as code,mealMaster.name as name,to_char(mealMaster.servingTime,'HH24:MI') as servingTime, "
				+ "to_char(mealMaster.updatedDate,'dd-MM-yyyy') as updatedDate, "
				+ "to_char(mealMaster.createdDate,'dd-MM-yyyy') as createdDate," + "mealMaster.updatedBy as updatedBy, "
				+ "mealMaster.createdBy as createdBy," + "mealMaster.status as status FROM MealMaster mealMaster "
				+ "WHERE mealMaster.status='A' and mealMaster.organizationId=:organizationId "
				//+ "and mealMaster.unitId=:unitId" //coment by minesh bcoz its only on org level.
				+ " order by mealMaster.name ASC"),//mealMaster/getActive
		
		@NamedQuery(name = "MEAL_MASTER.GET_MEAL_MASTER_DROP_DOWN", query = "SELECT mealMaster.id as mealId,"
				+ "mealMaster.name as mealName " + "FROM MealMaster mealMaster " 
				+ "WHERE mealMaster.status='A' and mealMaster.organizationId=:organizationId "
				//+ "and mealMaster.unitId=:unitId "  //coment by minesh bcoz its only on org level.
				+ "order by mealMaster.servingTime"),
		
		@NamedQuery(name = "MEAL_MASTER.GET_MEAL_MASTER_BY_SERVING_TIME", query = "SELECT mealMaster.id as mealId "
				+ "FROM MealMaster mealMaster " + "WHERE mealMaster.status='A' AND to_char(mealMaster.servingTime,'HH24:MI')=:servingTime "
				+ "AND mealMaster.organizationId=:organizationId"//added by minesh for save
				),
		//added by minesh for update
		@NamedQuery(name = "MEAL_MASTER.GET_MEAL_MASTER_BY_SERVING_TIME_NOT_BY_ID", query = "SELECT mealMaster.id as mealId "
				+ "FROM MealMaster mealMaster " + "WHERE mealMaster.status='A' AND to_char(mealMaster.servingTime,'HH24:MI')=:servingTime "
				+ "AND mealMaster.organizationId=:organizationId "
				+ "AND mealMaster.id != :mealId "
				),
		
		@NamedQuery(name = "MEAL_MASTER.GET_ALL_MEAL_MASTER_LIST", query = "SELECT mealMaster.id as mealId,"
				+ "mealMaster.code as code,mealMaster.name as name,to_char(mealMaster.servingTime,'HH24:MI') as servingTime,"
				+ "to_char(mealMaster.updatedDate,'dd-MM-yyyy') as updatedDate,"
				+ "to_char(mealMaster.createdDate,'dd-MM-yyyy') as createdDate," + "mealMaster.updatedBy as updatedBy,"
				+ "mealMaster.createdBy as createdBy," + "mealMaster.status as status FROM MealMaster mealMaster "
				+ "WHERE mealMaster.organizationId=:organizationId "
				//+ "and mealMaster.unitId=:unitId " //coment by minesh bcoz its only on org level.
				+ "order by mealMaster.servingTime") })

@Entity
@Table(name = "m_meal_master", schema = "kitchen")
@SequenceGenerator(name = "m_meal_master_id_seq", sequenceName = "kitchen.m_meal_master_id_seq", allocationSize = 1)
public class MealMaster {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_meal_master_id_seq")
	private Integer id;

	@Column(name = "code")
	private String code;

	@Column(name = "name")
	private String name;

	@Column(name = "serving_time")
	private Date servingTime;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_date")
	private Date updatedDate;

	public MealMaster() {

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getServingTime() {
		return servingTime;
	}

	public void setServingTime(Date servingTime) {
		this.servingTime = servingTime;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	
	
	
}
