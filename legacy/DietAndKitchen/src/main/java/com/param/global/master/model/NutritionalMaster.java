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

@NamedQueries({

		@NamedQuery(name = "GET_NUTRITION_MASTER_LIST_BY_NAME", query = "SELECT nutritionMaster.nutritionId as nutritionId "
				+ "FROM NutritionalMaster nutritionMaster "
				+ "WHERE (LOWER(nutritionMaster.nutritionName)=:nutritionName OR nutritionMaster.nutritionName=:nutritionName) "
				+ " AND (LOWER(nutritionMaster.nutritionCode)=:nutritionCode OR nutritionMaster.nutritionCode=:nutritionCode) "
				+ " AND nutritionMaster.organizationId=:orgId "),

		@NamedQuery(name = "GET_NUTRITION_MASTER_LIST", query = " SELECT nutrition.nutritionId as nutritionId, nutrition.organizationId as organizationId, nutrition.nutritionCode as nutritionCode, nutrition.nutritionName as nutritionName, nutrition.status as status"
				+ " FROM NutritionalMaster nutrition" + " WHERE nutrition.organizationId=:orgId"),

		@NamedQuery(name = "GET_ALL_NUTRITION_MASTER_LIST", query = " SELECT nutrition.nutritionId as nutritionId, nutrition.organizationId as organizationId,nutrition.unitId as unitId, nutrition.nutritionCode as nutritionCode, nutrition.nutritionName as nutritionName, nutrition.status as status"
				+ " FROM NutritionalMaster nutrition "
				+ " WHERE nutrition.organizationId=:organizationId "), // and nutrition.unitId=:unitId "), //dilip

		@NamedQuery(name = "GET_ACTIVE_NUTRITION_MASTER_LIST", query = " SELECT nutrition.nutritionId as nutritionId, nutrition.organizationId as organizationId, nutrition.nutritionCode as nutritionCode, nutrition.nutritionName as nutritionName, nutrition.status as status"
				+ " FROM NutritionalMaster nutrition" 
				+ " WHERE nutrition.status='A' AND nutrition.organizationId=:organizationId "
				+ " ORDER BY nutrition.nutritionName ASC "), // and nutrition.unitId=:unitId"), //Dilip

		@NamedQuery(name = "GET_NUTRITION_MASTER_LIST_BY_ID", query = " SELECT nutrition.nutritionId as nutritionId, nutrition.organizationId as organizationId, nutrition.nutritionCode as nutritionCode, nutrition.nutritionName as nutritionName, nutrition.status as status"
				+ " FROM NutritionalMaster nutrition"
				+ " WHERE nutrition.status='A' AND nutrition.organizationId=:orgId AND nutrition.nutritionId=:nutritionId"),
		
		@NamedQuery(name = "CHECK_NUTRITION_BY_ID_NOT_BY_NAME", query = "SELECT nutritionMaster.nutritionId as nutritionId "
				+ "FROM NutritionalMaster nutritionMaster "
				+ "WHERE (LOWER(nutritionMaster.nutritionName)=:nutritionName OR nutritionMaster.nutritionName=:nutritionName) "
				+ " AND (LOWER(nutritionMaster.nutritionCode)=:nutritionCode OR nutritionMaster.nutritionCode=:nutritionCode) "
				+ " AND nutritionMaster.organizationId=:orgId"
				+ " AND  nutritionMaster.nutritionId!=:nutritionId")
		})

@Entity
@Table(name = "m_nutrition_master", schema = "kitchen")
@SequenceGenerator(name = "nutrition_master_seq", sequenceName = "kitchen.nutrition_master_seq", allocationSize = 1)
public class NutritionalMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nutrition_master_seq")
	@Column(name = "nutrition_id")
	private Integer nutritionId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "nutrition_code")
	private String nutritionCode;

	@Column(name = "nutrition_name")
	private String nutritionName;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "status")
	private Character status;

	@Column(name = "is_calories")
	private String isCalories;

	public Integer getNutritionId() {
		return nutritionId;
	}

	public void setNutritionId(Integer nutritionId) {
		this.nutritionId = nutritionId;
	}

	public String getNutritionCode() {
		return nutritionCode;
	}

	public void setNutritionCode(String nutritionCode) {
		this.nutritionCode = nutritionCode;
	}

	public String getNutritionName() {
		return nutritionName;
	}

	public void setNutritionName(String nutritionName) {
		this.nutritionName = nutritionName;
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

	public void setUpdatedDate(Date date) {
		this.updatedDate = date;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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

	public String getIsCalories() {
		return isCalories;
	}

	public void setIsCalories(String isCalories) {
		this.isCalories = isCalories;
	}
}
