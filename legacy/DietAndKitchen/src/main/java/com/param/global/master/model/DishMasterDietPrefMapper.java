package com.param.global.master.model;

import java.util.List;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.unit.model.MealPreferenceMaster;
import com.param.global.master.dto.DishMasterDietPrefMapperDto;
import com.param.opd.coversheet.model.DietTypeMaster;

@NamedQueries({
@NamedQuery(name = "GET_DISH_LIST_BY_DIET_PREF_ID", query = "SELECT distinct mapper.dishId as dishId,"
		+ "dishMaster.dishDescription as dishDescription "
		+ "FROM DishMasterDietPrefMapper mapper INNER JOIN mapper.dishMaster as dishMaster where dishMaster.status = 'A' "
		+ "AND mapper.dietPreferenceId IN (:dietPrefIds) AND mapper.MealTypeId IN (:mealPrefIds) AND mapper.dietTypeId IN (:dietTypeIds) "
		+ "AND dishMaster.organizationId=:organizationId AND dishMaster.unitId=:unitId "),

@NamedQuery(name = "GET_DIET_PREF_DETAILS_BY_DISH_ID", query = "SELECT distinct mapper.dietPreferenceId as dietPreferenceId,"
		+ "mealPreferenceMaster.mealPreferenceCode as dietPreferenceCode,mealPreferenceMaster.mealPreferenceDesc as dietPreferenceDesc  "
		+ "FROM DishMasterDietPrefMapper mapper INNER JOIN mapper.mealPreferenceMaster as mealPreferenceMaster where mapper.dishId =:dishId "),

@NamedQuery(name = "GET_MEAL_PREF_DETAILS_BY_DISH_ID",  query = "SELECT DISTINCT dmdpm.MealTypeId AS MealTypeId, mmm.name AS mealPreferenceDesc "
	            + "FROM com.param.global.master.model.DishMasterDietPrefMapper dmdpm "
	            + "INNER JOIN dmdpm.mealMaster AS mmm "
	            + "WHERE dmdpm.dishId = :dishId"),

@NamedQuery(name =	"GET_DIET_TYPE_DETAILS_BY_DISH_ID", query = "SELECT DISTINCT dmdpm.dietTypeId as dietTypeId, mdtm.dietTypeDescription as dietTypeDescription "
				+ "FROM com.param.global.master.model.DishMasterDietPrefMapper dmdpm "
				+ "inner join dmdpm.dietTypeMaster AS mdtm "
				+ "WHERE dmdpm.dishId = :dishId")


})
@Entity

@Table(name = "dish_master_diet_pref_mapper", schema = "kitchen")
@SequenceGenerator(name = "dish_master_diet_pref_mapper_id_seq", sequenceName = "kitchen.dish_master_diet_pref_mapper_id_seq", allocationSize = 1)
public class DishMasterDietPrefMapper {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_master_diet_pref_mapper_id_seq")
	Integer id;

	@Column(name = "dish_id")
	Integer dishId;

	@Column(name = "diet_preference_id")
	Integer dietPreferenceId;
	
	@Column(name = "meal_type_id")
	Integer MealTypeId;
	
	@Column(name = "diet_type_id")
	Integer dietTypeId;
	
	
	public Integer getDietPreferenceId() {
		return dietPreferenceId;
	}

	public void setDietPreferenceId(Integer dietPreferenceId) {
		this.dietPreferenceId = dietPreferenceId;
	}

	public MealPreferenceMaster getMealPreferenceMaster() {
		return mealPreferenceMaster;
	}

	public void setMealPreferenceMaster(MealPreferenceMaster mealPreferenceMaster) {
		this.mealPreferenceMaster = mealPreferenceMaster;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dish_id", insertable = false, updatable = false)
	DishMaster dishMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diet_preference_id", insertable = false, updatable = false)
	MealPreferenceMaster mealPreferenceMaster;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_type_id", insertable = false, updatable = false)
    MealMaster mealMaster;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_type_id", insertable = false, updatable = false)
    DietTypeMaster dietTypeMaster;


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

	public Integer getMealTypeId() {
		return MealTypeId;
	}

	public void setMealTypeId(Integer MealTypeId) {
		this.MealTypeId = MealTypeId;
	}

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
	}


}
