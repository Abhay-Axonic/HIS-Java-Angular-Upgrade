package com.param.global.dto;

import java.util.List;

public class VitalDateTimeDto {
	
 private String vitalTime;
 private String vitalDate;
 private List<VitalObservation> vitalObservationList;
 
public String getVitalTime() {
	return vitalTime;
}
public void setVitalTime(String vitalTime) {
	this.vitalTime = vitalTime;
}
public String getVitalDate() {
	return vitalDate;
}
public void setVitalDate(String vitalDate) {
	this.vitalDate = vitalDate;
}
public List<VitalObservation> getVitalObservationList() {
	return vitalObservationList;
}
public void setVitalObservationList(List<VitalObservation> vitalObservationList) {
	this.vitalObservationList = vitalObservationList;
}
 
 
}
