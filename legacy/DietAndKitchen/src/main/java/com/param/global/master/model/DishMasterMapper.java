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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({

		@NamedQuery(name = "GET_INGREDIENTS_BY_DISH_ID", query = "SELECT mapper.dishMasterKeyIngredientsMapperId as dishMasterKeyIngredientsMapperId, keyIngMas.keyIngredientId as keyIngredientId ,"
				+ "keyIngMas.ingredientCode as ingredientCode," + "mapper.value as value,"
				+ "keyIngMas.uomId as uomId, " + "unitMaster.uomDescription as uomName, "
				+ "keyIngMas.ingredientDescription as ingredientDescription "
				+ "FROM DishMasterMapper mapper INNER JOIN mapper.keyIngredientsMaster as keyIngMas "
				+ "INNER JOIN keyIngMas.unitOfMeasurmentMaster as unitMaster "
				+ "INNER JOIN mapper.dishMaster as dishMaster " + "WHERE mapper.dishId=:dishId ")

})

@Entity
@Table(name = "dish_master_key_ingredients_mapper", schema = "kitchen")
@SequenceGenerator(name = "dish_master_key_ingredients_mapper_seq", sequenceName = "kitchen.dish_master_key_ingredients_mapper_seq", allocationSize = 1)

public class DishMasterMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dish_master_key_ingredients_mapper_seq")
	@Column(name = "dish_master_key_ingredients_mapper_id")
	private Integer dishMasterKeyIngredientsMapperId;

	@Column(name = "key_ingredients_id")
	private Integer keyIngredientsId;

	@Column(name = "uom_id")
	private Integer uomId;

	@Column(name = "value")
	private Integer value;

	@Column(name = "dish_id")
	private Integer dishId;

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
	@JoinColumn(name = "uom_id", insertable = false, updatable = false)
	private UnitOfMeasurmentMaster unitOfMeasurmentMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "key_ingredients_id", insertable = false, updatable = false)
	private KeyIngredientsMaster keyIngredientsMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dish_id", insertable = false, updatable = false)
	private DishMaster dishMaster;

	public Integer getDishMasterKeyIngredientsMapperId() {
		return dishMasterKeyIngredientsMapperId;
	}

	public void setDishMasterKeyIngredientsMapperId(Integer dishMasterKeyIngredientsMapperId) {
		this.dishMasterKeyIngredientsMapperId = dishMasterKeyIngredientsMapperId;
	}

	public Integer getKeyIngredientsId() {
		return keyIngredientsId;
	}

	public void setKeyIngredientsId(Integer keyIngredientsId) {
		this.keyIngredientsId = keyIngredientsId;
	}

	public Integer getUomId() {
		return uomId;
	}

	public void setUomId(Integer uomId) {
		this.uomId = uomId;
	}

	public Integer getDishId() {
		return dishId;
	}

	public void setDishId(Integer dishId) {
		this.dishId = dishId;
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

	public UnitOfMeasurmentMaster getUnitOfMeasurmentMaster() {
		return unitOfMeasurmentMaster;
	}

	public void setUnitOfMeasurmentMaster(UnitOfMeasurmentMaster unitOfMeasurmentMaster) {
		this.unitOfMeasurmentMaster = unitOfMeasurmentMaster;
	}

	public KeyIngredientsMaster getKeyIngredientsMaster() {
		return keyIngredientsMaster;
	}

	public void setKeyIngredientsMaster(KeyIngredientsMaster keyIngredientsMaster) {
		this.keyIngredientsMaster = keyIngredientsMaster;
	}

	public DishMaster getDishMaster() {
		return dishMaster;
	}

	public void setDishMaster(DishMaster dishMaster) {
		this.dishMaster = dishMaster;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
