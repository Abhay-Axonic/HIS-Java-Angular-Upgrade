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
	
	@NamedNativeQuery(name="GET_OT_DISH_DATA",
			query="select tod.ot_dish_id  as \"otDishId\"," + 
					"tod.ot_meal_id  as \"otMealId\"," + 
					"tod.dish_id  as \"dishId\"," + 
					"mdm.dish_description  as \"dishDescription\" " + 
					"from otims.t_ot_dish tod " + 
					"inner join kitchen.m_dish_master mdm on  mdm.dish_id = tod.dish_id " + 
					"where  tod.ot_meal_id = :otMealId "
				)
	
})

@Entity
@Table(name = "t_ot_dish" , schema = "otims")
@SequenceGenerator(name = "ot_dish_seq" , sequenceName = "otims.ot_dish_seq" , allocationSize = 1)
public class OtDish {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "ot_dish_seq")
	@Column(name = "ot_dish_id")
	private Integer otDishId;
	
	@Column(name = "ot_meal_id")
	private Integer otMealId;
	
	@Column(name = "dish_id")
	private Integer dishId;
	
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

	public Integer getOtDishId() {
		return otDishId;
	}

	public void setOtDishId(Integer otDishId) {
		this.otDishId = otDishId;
	}

	public Integer getOtMealId() {
		return otMealId;
	}

	public void setOtMealId(Integer otMealId) {
		this.otMealId = otMealId;
	}

	public Integer getDishId() {
		return dishId;
	}

	public void setDishId(Integer dishId) {
		this.dishId = dishId;
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
