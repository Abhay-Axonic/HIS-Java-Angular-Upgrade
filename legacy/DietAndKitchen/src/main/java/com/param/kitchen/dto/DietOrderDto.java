package com.param.kitchen.dto;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.param.global.master.dto.DishDto;

@JsonInclude(Include.NON_NULL)
public class DietOrderDto {

	

	private Integer orderId;
	
	private Integer patientId;

	private Integer floorId;
	
	private String floorName;

	private Integer wardId;
	
	private String wardName;
	
	private Integer bedId;
	
	private String bedNumber;
	
	private Integer mealId;
	
	private String dietDate;

	private String dishName;
	
	private String mealName;
	
	private Integer quantity;
	
	private Character status;
	
	private Boolean isIssued;
	
	private Boolean isRejected;
	
	private String remarks;
	
	private Integer dietPlanningId;
	
	private Integer dishId;
	
	private Integer dietPreferenceId;
	
	private String dietPreferenceDesc;
	
	private Integer dietTypeId;
	
	private String dietTypeDesc;
	
	private Boolean accepted;
	
	private List<Integer> orderIds;
	
	private List<DishDto> orderList;
	
	private PatientDetailsDto patientDetails;
	private String mealNotes;

	
	
	private Set<PatientDetailsDto> patientList;
	
	private Integer dayId;
	
	

	public Integer getDayId() {
		return dayId;
	}

	public void setDayId(Integer dayId) {
		this.dayId = dayId;
	}

	public Boolean getAccepted() {
		return accepted;
	}

	public void setAccepted(Boolean accepted) {
		this.accepted = accepted;
	}

	public List<DishDto> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<DishDto> orderList) {
		this.orderList = orderList;
	}

	
	public Integer getDishId() {
		return dishId;
	}

	public void setDishId(Integer dishId) {
		this.dishId = dishId;
	}

	public Integer getDietPlanningId() {
		return dietPlanningId;
	}

	public void setDietPlanningId(Integer dietPlanningId) {
		this.dietPlanningId = dietPlanningId;
	}

	

	public Set<PatientDetailsDto> getPatientList() {
		return patientList;
	}

	public void setPatientList(Set<PatientDetailsDto> patientList) {
		this.patientList = patientList;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

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

	public String getDietDate() {
		return dietDate;
	}

	public void setDietDate(String dietDate) {
		this.dietDate = dietDate;
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

	public Boolean getIsRejected() {
		return isRejected;
	}

	public void setIsRejected(Boolean isRejected) {
		this.isRejected = isRejected;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
	}

	public String getMealName() {
		return mealName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getWardName() {
		return wardName;
	}

	public void setWardName(String wardName) {
		this.wardName = wardName;
	}

	public String getBedNumber() {
		return bedNumber;
	}

	public void setBedNumber(String bedNumber) {
		this.bedNumber = bedNumber;
	}

	public Integer getDietPreferenceId() {
		return dietPreferenceId;
	}

	public void setDietPreferenceId(Integer dietPreferenceId) {
		this.dietPreferenceId = dietPreferenceId;
	}

	public String getDietPreferenceDesc() {
		return dietPreferenceDesc;
	}

	public void setDietPreferenceDesc(String dietPreferenceDesc) {
		this.dietPreferenceDesc = dietPreferenceDesc;
	}

	public String getDietTypeDesc() {
		return dietTypeDesc;
	}

	public void setDietTypeDesc(String dietTypeDesc) {
		this.dietTypeDesc = dietTypeDesc;
	}

	public PatientDetailsDto getPatientDetails() {
		return patientDetails;
	}

	public void setPatientDetails(PatientDetailsDto patientDetails) {
		this.patientDetails = patientDetails;
	}

	public List<Integer> getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(List<Integer> orderIds) {
		this.orderIds = orderIds;
	}

	public String getMealNotes() {
		return mealNotes;
	}

	public void setMealNotes(String mealNotes) {
		this.mealNotes = mealNotes;
	}
	
	
}
