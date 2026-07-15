package com.param.kitchen.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "diet_issue", schema = "kitchen")
@SequenceGenerator(name = "diet_issue_id_seq", sequenceName = "kitchen.diet_issue_id_seq", allocationSize = 1)
public class DietIssue {

	@Id
	@Column(name = "diet_issue_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "diet_issue_id_seq")
	private Integer diet_issue_id;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "floor_id")
	private Integer floorId;

	@Column(name = "ward_id")
	private Integer wardId;

	@Column(name = "bed_id")
	private Integer bedId;

	@Column(name = "diet_type_id")
	private Integer dietTypeId;

	@Column(name = "food_type_id")
	private Integer foodTypeId;

	@Column(name = "meal_id")
	private Integer mealId;

	@Column(name = "status")
	private Character status;
	
	@Column(name = "is_issued")
	private Boolean isIssued;

	public Integer getDiet_issue_id() {
		return diet_issue_id;
	}

	public void setDiet_issue_id(Integer diet_issue_id) {
		this.diet_issue_id = diet_issue_id;
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

	public Integer getDietTypeId() {
		return dietTypeId;
	}

	public void setDietTypeId(Integer dietTypeId) {
		this.dietTypeId = dietTypeId;
	}

	public Integer getFoodTypeId() {
		return foodTypeId;
	}

	public void setFoodTypeId(Integer foodTypeId) {
		this.foodTypeId = foodTypeId;
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


	
}
