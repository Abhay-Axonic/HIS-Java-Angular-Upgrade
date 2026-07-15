package com.param.global.dto;

import java.util.List;

public class VitalNameDto {
	
	private Integer vitalId;
	private String vitalName;
	private List<VitalDateTimeDto> vitalDateTimeDtoList;
	
	public Integer getVitalId() {
		return vitalId;
	}
	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}
	public String getVitalName() {
		return vitalName;
	}
	public void setVitalName(String vitalName) {
		this.vitalName = vitalName;
	}
	public List<VitalDateTimeDto> getVitalDateTimeDtoList() {
		return vitalDateTimeDtoList;
	}
	public void setVitalDateTimeDtoList(List<VitalDateTimeDto> vitalDateTimeDtoList) {
		this.vitalDateTimeDtoList = vitalDateTimeDtoList;
	}
	
	

}
