package com.param.global.master.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DishMasterMapperDto {

	private Integer dishMasterKeyIngredientsMapperId;

	private Integer keyIngredientsId;

	private Integer uomId;

	private Integer value;

	private Integer dishId;

	private Integer createdBy;

	private String createdDate;

	private Integer updatedBy;

	private String updatedDate;

	private Character status;

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

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

}
