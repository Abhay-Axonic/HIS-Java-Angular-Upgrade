package com.param.global.master.model;

import java.util.Date;

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
@Table(name = "key_ingredients_nutrition_mapper", schema = "kitchen")
@SequenceGenerator(name = "ingredient_nutrition_master_seq", sequenceName = "kitchen.ingredient_nutrition_master_seq", allocationSize = 1)
public class KeyIngredientsNutritionMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredient_nutrition_master_seq")
	@Column(name = "ingredient_nutrition_id")
	private Integer ingredientNutritionId;

	@Column(name = "key_ingredients_master_id")
	private Integer keyIngredientsMasterId;

	@Column(name = "nutrition_master_id")
	private Integer nutritionMasterId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "nutrition_value")
	private double nutritionValue;

	@Column(name = "uom_id")
	private Integer uomId;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "nutrition_master_id", insertable = false, updatable = false)
	private NutritionalMaster nutritionalMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_ingredients_master_id", insertable = false, updatable = false)
	private KeyIngredientsMaster keyIngredientsMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "uom_id", insertable = false, updatable = false)
	private UnitOfMeasurmentMaster unitOfMeasurmentMaster;

	public Integer getIngredientNutritionId() {
		return ingredientNutritionId;
	}

	public void setIngredientNutritionId(Integer ingredientNutritionId) {
		this.ingredientNutritionId = ingredientNutritionId;
	}

	public Integer getKeyIngredientsMasterId() {
		return keyIngredientsMasterId;
	}

	public void setKeyIngredientsMasterId(Integer keyIngredientsMasterId) {
		this.keyIngredientsMasterId = keyIngredientsMasterId;
	}

	public Integer getNutritionMasterId() {
		return nutritionMasterId;
	}

	public void setNutritionMasterId(Integer nutritionMasterId) {
		this.nutritionMasterId = nutritionMasterId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public double getNutritionValue() {
		return nutritionValue;
	}

	public void setNutritionValue(double nutritionValue) {
		this.nutritionValue = nutritionValue;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public NutritionalMaster getNutritionalMaster() {
		return nutritionalMaster;
	}

	public void setNutritionalMaster(NutritionalMaster nutritionalMaster) {
		this.nutritionalMaster = nutritionalMaster;
	}

	public KeyIngredientsMaster getKeyIngredientsMaster() {
		return keyIngredientsMaster;
	}

	public void setKeyIngredientsMaster(KeyIngredientsMaster keyIngredientsMaster) {
		this.keyIngredientsMaster = keyIngredientsMaster;
	}

	public UnitOfMeasurmentMaster getUnitOfMeasurmentMaster() {
		return unitOfMeasurmentMaster;
	}

	public void setUnitOfMeasurmentMaster(UnitOfMeasurmentMaster unitOfMeasurmentMaster) {
		this.unitOfMeasurmentMaster = unitOfMeasurmentMaster;
	}

}
