package com.param.global.master.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "meal_details", schema = "kitchen")
@SequenceGenerator(name = "meal_details_id_seq", sequenceName = "kitchen.meal_details_id_seq", allocationSize = 1)
public class MealDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meal_details_id_seq")
	@Column(name = "meal_details_id")
	private Integer mealDetailsId;

	@Column(name = "day_id")
	private Integer dayId;

	@Column(name = "diet_template_id")
	private Integer dietTemplateId;

	@Column(name = "meal_id")
	private Integer mealId;

	@Column(name = "dish_id")
	private Integer dishId;

	@Column(name = "quantity")
	private Integer quantity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meal_id", insertable = false, updatable = false)
	private MealMaster mealMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dish_id", insertable = false, updatable = false)
	private DishMaster dishMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diet_template_id", insertable = false, updatable = false)
	private DietTemplate dietTemplate;

	public Integer getMealDetailsId() {
		return mealDetailsId;
	}

	public void setMealDetailsId(Integer mealDetailsId) {
		this.mealDetailsId = mealDetailsId;
	}

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public Integer getDietTemplateId() {
		return dietTemplateId;
	}

	public void setDietTemplateId(Integer dietTemplateId) {
		this.dietTemplateId = dietTemplateId;
	}

	public Integer getMealId() {
		return mealId;
	}

	public void setMealId(Integer mealId) {
		this.mealId = mealId;
	}

	public Integer getDishId() {
		return dishId;
	}

	public void setDishId(Integer dishId) {
		this.dishId = dishId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

}
