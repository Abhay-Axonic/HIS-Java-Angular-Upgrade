package com.param.CSSD.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class SterilizedBatchDto {
	
	
	private Integer id;

	private Integer batchNumber;
	
	private Integer sterilizationTemperature;
	
	private Integer pressure;
	
	private String expiryDate;
	
	private String color;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(Integer batchNumber) {
		this.batchNumber = batchNumber;
	}

	public Integer getSterilizationTemperature() {
		return sterilizationTemperature;
	}

	public void setSterilizationTemperature(Integer sterilizationTemperature) {
		this.sterilizationTemperature = sterilizationTemperature;
	}

	public Integer getPressure() {
		return pressure;
	}

	public void setPressure(Integer pressure) {
		this.pressure = pressure;
	}

	public String getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(String expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}


}
