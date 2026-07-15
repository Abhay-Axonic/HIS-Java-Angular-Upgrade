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
	
	@NamedNativeQuery(name="GET_OT_MEAL_DATA",
			query="select tom.ot_meal_id  as \"otMealId\" , " + 
					"tom.meal_id  as \"mealId\" , "+
					"tom.status as \"status\", " + 
					"tom.post_operative_care_id  as \"postOperativeCareId\", " + 
					"mmm.name as \"mealName\" " + 
					"from otims.t_ot_meal tom   " + 
					"inner join kitchen.m_meal_master mmm on tom.meal_id = mmm.id " + 
					"where tom.post_operative_care_id  = :postOperativeCareId and tom.status = 'A' "
				)
	
})

@Entity
@Table(name = "t_ot_meal" , schema = "otims")
@SequenceGenerator(name = "ot_meal_seq" , sequenceName = "otims.ot_meal_seq" , allocationSize = 1)
public class OtMeal {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "ot_meal_seq")
	@Column(name = "ot_meal_id")
	private Integer otMealId;
	
	@Column(name = "post_operative_care_id")
	private Integer postOperativeCareId;
	
	@Column(name = "meal_id")
	private Integer mealId;
	
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

	public Integer getOtMealId() {
		return otMealId;
	}

	public void setOtMealId(Integer otMealId) {
		this.otMealId = otMealId;
	}

	public Integer getPostOperativeCareId() {
		return postOperativeCareId;
	}

	public void setPostOperativeCareId(Integer postOperativeCareId) {
		this.postOperativeCareId = postOperativeCareId;
	}

	public Integer getMealId() {
		return mealId;
	}

	public void setMealId(Integer mealId) {
		this.mealId = mealId;
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
	
	
}
