package com.param.kitchen.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class PatientInfoDto {
	
	private Integer patientId;

	private Integer floorId;
	
	private Integer wardId;
	
	private Integer bedId;
	private String mealNotes;

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

	public String getMealNotes() {
		return mealNotes;
	}

	public void setMealNotes(String mealNotes) {
		this.mealNotes = mealNotes;
	}
	
	
}
