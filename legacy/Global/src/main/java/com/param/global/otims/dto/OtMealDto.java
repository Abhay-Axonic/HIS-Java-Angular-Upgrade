package com.param.global.otims.dto;

import java.util.List;

public class OtMealDto {

	private Integer otMealId;
	
	private Integer postOperativeCareId;
	
	private Integer mealId;
	
	private String mealName;
	
	private char status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;
	
	private List<OtDishDto> otDishList;
	
	private String selectedDishes;

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

	public List<OtDishDto> getOtDishList() {
		return otDishList;
	}

	public void setOtDishList(List<OtDishDto> otDishList) {
		this.otDishList = otDishList;
	}

	public String getMealName() {
		return mealName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	public String getSelectedDishes() {
		return selectedDishes;
	}

	public void setSelectedDishes(String selectedDishes) {
		this.selectedDishes = selectedDishes;
	}
	
	
}
