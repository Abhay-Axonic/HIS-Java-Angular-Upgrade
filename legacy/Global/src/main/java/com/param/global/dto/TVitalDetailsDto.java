package com.param.global.dto;

public class TVitalDetailsDto {
	
private Integer tVitalDetailsId;
	
	private Integer tVitalId;

	private Integer vitalId;
	
	private String observation1;
	
	private Integer typeId;
	
	private String observation;
	
     private String vitalName;
	
	private String vitalUnit;
	
	private String vitalTime;
	
	private String createdDate;
	

	public Integer gettVitalDetailsId() {
		return tVitalDetailsId;
	}

	public void settVitalDetailsId(Integer tVitalDetailsId) {
		this.tVitalDetailsId = tVitalDetailsId;
	}

	public Integer gettVitalId() {
		return tVitalId;
	}

	public void settVitalId(Integer tVitalId) {
		this.tVitalId = tVitalId;
	}

	public Integer getVitalId() {
		return vitalId;
	}

	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public String getObservation1() {
		return observation1;
	}

	public void setObservation1(String observation1) {
		this.observation1 = observation1;
	}

	public String getVitalName() {
		return vitalName;
	}

	public void setVitalName(String vitalName) {
		this.vitalName = vitalName;
	}

	public String getVitalUnit() {
		return vitalUnit;
	}

	public void setVitalUnit(String vitalUnit) {
		this.vitalUnit = vitalUnit;
	}

	public String getVitalTime() {
		return vitalTime;
	}

	public void setVitalTime(String vitalTime) {
		this.vitalTime = vitalTime;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	
	
}
