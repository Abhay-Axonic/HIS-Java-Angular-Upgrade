package com.param.kitchen.model;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.param.global.master.model.DishMaster;



@NamedQueries( { @NamedQuery( name="DISH_TYPE.GET_BY_DIET_PLANNING_ID", query="SELECT dishType.id as id,"
			+ "dishType.dishId as dishId,dishType.dishName as dishName,"
			+ "dishType.quantity as quantity,"
			+ "dishType.mealId as mealId,"
			+ "dishType.dietPlanningId as dietPlanningId "
			+ "from DishType dishType where dietPlanningId=:dietPlanningId ")
}) 




@Entity
@Table(name = "dish_type", schema = "kitchen")
@SequenceGenerator(name = "dish_type_id_seq", sequenceName = "kitchen.dish_type_id_seq", allocationSize = 1)
public class DishType {

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_type_id_seq")
	private Integer id;

	@Column(name="dish_id")
	private Integer dishId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dish_id", insertable = false, nullable = false, updatable = false)
	private DishMaster dishMaster;

	@Column(name = "dish_name")
	private String dishName;

	@Column(name = "quantity")
	private Integer quantity;
	
	@Column(name = "meal_id")
	private Integer mealId;
	
	//Added 09-07-2018
	
	@Column(name="diet_planning_id")
	private Integer dietPlanningId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "diet_planning_id", insertable = false, nullable = false, updatable = false)
	private DietPlanning dietPlanning;

	//
	
	public Integer getDietPlanningId() {
		return dietPlanningId;
	}

	public void setDietPlanningId(Integer dietPlanningId) {
		this.dietPlanningId = dietPlanningId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDishId() {
		return dishId;
	}

	public void setDishId(Integer dishId) {
		this.dishId = dishId;
	}
	
	public DishMaster getDishMaster() {
		return dishMaster;
	}

	public void setDishMaster(DishMaster dishMaster) {
		this.dishMaster = dishMaster;
	}

	public String getDishName() {
		return dishName;
	}

	public void setDishName(String dishName) {
		this.dishName = dishName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	public Integer getMealId() {
		return mealId;
	}

	public void setMealId(Integer mealId) {
		this.mealId = mealId;
	}

	public DietPlanning getDietPlanning() {
		return dietPlanning;
	}

	public void setDietPlanning(DietPlanning dietPlanning) {
		this.dietPlanning = dietPlanning;
	}
	
	

}
