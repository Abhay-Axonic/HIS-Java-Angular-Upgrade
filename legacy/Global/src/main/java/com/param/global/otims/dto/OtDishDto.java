package com.param.global.otims.dto;

public class OtDishDto {

	private Integer otDishId;
	
	private Integer otMealId;
	
	private Integer dishId;
	
	private String dishDescription;
	
	private char status;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Integer createdBy;
	
	private String createdDate;
	
	private Integer updatedBy;
	
	private String updatedDate;

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

	public String getDishDescription() {
		return dishDescription;
	}

	public void setDishDescription(String dishDescription) {
		this.dishDescription = dishDescription;
	}
}
