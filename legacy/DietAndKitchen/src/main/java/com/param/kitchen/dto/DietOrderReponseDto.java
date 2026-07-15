package com.param.kitchen.dto;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DietOrderReponseDto {
	
	private Integer mealId;
	
	public Map<Integer, PatientDetailsDto> getPatientMap() {
		return patientMap;
	}

	public void setPatientMap(Map<Integer, PatientDetailsDto> patientMap) {
		this.patientMap = patientMap;
	}

	private String dishName;
	
	private String mealName;
	
	private List<PatientDetailsDto> patientList;
	private String mealNotes;

	private Map<Integer,PatientDetailsDto> patientMap;
	public String getMealName() {
		return mealName;
	}

	public void setMealName(String mealName) {
		this.mealName = mealName;
	}

	private Integer quantity;

	public Integer getMealId() {
		return mealId;
	}

	public void setMealId(Integer mealId) {
		this.mealId = mealId;
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

	public List<PatientDetailsDto> getPatientList() {
		return patientList;
	}

	public void setPatientList(List<PatientDetailsDto> patientList) {
		this.patientList = patientList;
	}
	
	public String getMealNotes() {
		return mealNotes;
	}

	public void setMealNotes(String mealNotes) {
		this.mealNotes = mealNotes;
	}

}
