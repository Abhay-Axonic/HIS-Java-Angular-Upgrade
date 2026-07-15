package com.param.kitchen.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.master.model.MealMaster;

import java.util.List;

@JsonInclude(Include.NON_NULL)
public class DietIssueDto {
	
	private Integer orderId;

	private Integer patientId;

	private Integer floorId;

	private Integer wardId;

	private Integer bedId;

	private Integer mealId;
	
	private String dishName;
	
	private Integer quantity;

	private Character status;
	
	private Boolean isIssued;

	private MealMaster mealMaster;

	private List<Integer> dietList;
	
	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getFloorId() {
		return floorId;
	}

	public void setFloorId(Integer floorId) {
		this.floorId = floorId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public Integer getBedId() {
		return bedId;
	}

	public void setBedId(Integer bedId) {
		this.bedId = bedId;
	}

	public Integer getMealId() {
		return mealId;
	}

	public void setMealId(Integer mealId) {
		this.mealId = mealId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	
	public Boolean getIsIssued() {
		return isIssued;
	}

	public void setIsIssued(Boolean isIssued) {
		this.isIssued = isIssued;
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

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public MealMaster getMealMaster() {
		return mealMaster;
	}

	public void setMealMaster(MealMaster mealMaster) {
		this.mealMaster = mealMaster;
	}

	public List<Integer> getDietList() {
		return dietList;
	}

	public void setDietList(List<Integer> dietList) {
		this.dietList = dietList;
	}
}
