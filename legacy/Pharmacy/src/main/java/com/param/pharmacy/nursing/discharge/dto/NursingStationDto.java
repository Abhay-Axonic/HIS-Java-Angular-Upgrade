package com.param.pharmacy.nursing.discharge.dto;

public class NursingStationDto {

	private Integer id;
	
	private String floor;
	
	public NursingStationDto() {
		super();
	}
	
	public NursingStationDto(Integer id, String floor) {
		super();
		this.id = id;
		this.floor = floor;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}
	
}
