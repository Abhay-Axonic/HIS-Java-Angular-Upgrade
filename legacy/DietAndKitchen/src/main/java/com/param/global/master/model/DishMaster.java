package com.param.global.master.model;

import java.util.Date;
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

		@NamedQuery(name = "GET_DISH_MASTER_LIST_BY_NAME", query = "SELECT dishMaster.dishId as dishId "
				+ "FROM DishMaster dishMaster "
				+ "WHERE LOWER(dishMaster.dishCode)=:dishName OR dishMaster.dishCode=:dishName "),
		@NamedQuery(name = "GET_DISH_MASTER_LIST", query = "SELECT dishMaster.dishId as dishId,"
				+ "dishMaster.dishCode as dishCode, dishMaster.dishDescription as dishDescription,"
				+ "dishMaster.dishValue as dishValue, dishMaster.uomId as uomId,unitMaster.uomDescription as uomName,"
				+ "dishMaster.status as status "
				+ "FROM DishMaster dishMaster "
				+ "INNER JOIN dishMaster.unitOfMeasurmentMaster as unitMaster WHERE dishMaster.organizationId=:organizationId AND dishMaster.unitId=:unitId "
				+ "ORDER BY dishMaster.dishId desc "),
		
		@NamedQuery(name = "GET_DISH_MASTER_LIST_WITHOUT_ORG_ID", query = "SELECT dishMaster.dishId as dishId,"
				+ "dishMaster.dishCode as dishCode, dishMaster.dishDescription as dishDescription,"
				+ "dishMaster.dishValue as dishValue, dishMaster.uomId as uomId,unitMaster.uomName as uomName,"
				+ "dishMaster.status as status "
				+ "FROM DishMaster dishMaster "
				+ "INNER JOIN dishMaster.unitOfMeasurmentMaster as unitMaster "),
		
		@NamedQuery(name = "GET_DISH_ACTIVE_MASTER_LIST", query = "SELECT dishMaster.dishId as dishId,"
				+ "dishMaster.dishCode as dishCode, dishMaster.dishDescription as dishDescription,"
				+ "dishMaster.dishValue as dishValue, dishMaster.uomId as uomId, unitMaster.uomName as uomName,"
				+ "dishMaster.status as status "
				+ "FROM DishMaster dishMaster  "
				+ "INNER JOIN dishMaster.unitOfMeasurmentMaster as unitMaster where dishMaster.status = 'A'"),

		@NamedQuery(name="GET_DISH_ACTIVE_MASTER_DROP_DOWN", query="SELECT dishMaster.dishId as dishId,"
				+ "dishMaster.dishDescription as dishDescription "
				+ "FROM DishMaster dishMaster where dishMaster.status = 'A' and dishMaster.organizationId=:organizationId and dishMaster.unitId=:unitId ")

})

@Entity

@Table(name = "m_dish_master", schema = "kitchen")
@SequenceGenerator(name = "dish_master_seq", sequenceName = "kitchen.dish_master_seq", allocationSize = 1)
public class DishMaster {

	@Id
	@Column(name = "dish_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_master_seq")
	Integer dishId;

	@Column(name = "dish_code")
	String dishCode;

	@Column(name = "dish_description")
	String dishDescription;

	@Column(name = "dish_value")
	Double dishValue;

	@Column(name = "uom_id")
	Integer uomId;

	@Column(name = "diet_preference_id")
	Integer dietPreferenceId;

	@Column(name = "diet_preference")
	String dietPreference;

	@Column(name = "created_by")
	Integer createdBy;

	@Column(name = "updated_by")
	Integer updatedBy;

	@Column(name = "created_date")
	Date createdDate;

	@Column(name = "updated_date")
	Date updatedDate;

	@Column(name = "status")
	Character status;
	
	@Column(name = "organization_id")
	Integer organizationId;
	
	@Column(name = "unit_id")
	Integer unitId;

	@Column(name = "meal_Type_id")
	Integer mealtypeId;
	
	@Column(name = "meal_preference")
	String mealPreference;
	
	@Column(name = "diet_type_id")
	Integer dietTypeId;
	
	@Column(name = "diet_type_description")
	String dietTypeDescription;

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
	@JoinColumn(name = "uom_id", insertable = false, updatable = false)
	UnitOfMeasurmentMaster unitOfMeasurmentMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diet_preference_id", insertable = false, updatable = false)
	MealPreferenceMaster mealPreferenceMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "meal_Type_id", insertable = false, updatable = false)
	MealMaster mealMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "diet_type_id", insertable = false, updatable = false)
	DietTypeMaster dietTypeMaster;
	
	public Integer getDishId() {
		return dishId;
	}

	public void setDishId(Integer dishId) {
		this.dishId = dishId;
	}

	public String getDishCode() {
		return dishCode;
	}

	public void setDishCode(String dishCode) {
		this.dishCode = dishCode;
	}

	public String getDishDescription() {
		return dishDescription;
	}

	public void setDishDescription(String dishDescription) {
		this.dishDescription = dishDescription;
	}

	public Double getDishValue() {
		return dishValue;
	}

	public void setDishValue(Double dishValue) {
		this.dishValue = dishValue;
	}

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getDietPreference() {
		return dietPreference;
	}

	public void setDietPreference(String dietPreference) {
		this.dietPreference = dietPreference;
	}

	public UnitOfMeasurmentMaster getUnitOfMeasurmentMaster() {
		return unitOfMeasurmentMaster;
	}

	public void setUnitOfMeasurmentMaster(UnitOfMeasurmentMaster unitOfMeasurmentMaster) {
		this.unitOfMeasurmentMaster = unitOfMeasurmentMaster;
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

	public Integer getMealtypeId() {
		return mealtypeId;
	}

	public void setMealtypeId(Integer mealtypeId) {
		this.mealtypeId = mealtypeId;
	}

	public String getMealPreference() {
		return mealPreference;
	}

	public void setMealPreference(String mealPreference) {
		this.mealPreference = mealPreference;
	}

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
	}

	public String getDietTypeDescription() {
		return dietTypeDescription;
	}

	public void setDietTypeDescription(String dietTypeDescription) {
		this.dietTypeDescription = dietTypeDescription;
	}	
}
