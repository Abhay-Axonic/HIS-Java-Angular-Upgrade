package com.param.opd.coversheet.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ListAppointmentVitalMapperDto {

	private Integer vitalId;
	private String value;
	private Integer appointmentVitalMapperId;
	private char isReviewedFlag;
	private Integer vitalQualifireId;
	private List<AppointmentVitalMapperDto> vitalMethodList;
	private List<AppointmentVitalMapperDto> vitalPositionList;
	private List<AppointmentVitalMapperDto> vitalSiteList;
	private List<AppointmentVitalMapperDto> vitalLocationList;
	private List<AppointmentVitalMapperDto> vitalQualityList;
	private List<AppointmentVitalMapperDto> vitalCuffList;

	public Integer getAppointmentVitalMapperId() {
		return appointmentVitalMapperId;
	}
	public void setAppointmentVitalMapperId(Integer appointmentVitalMapperId) {
		this.appointmentVitalMapperId = appointmentVitalMapperId;
	}
	public char getIsReviewedFlag() {
		return isReviewedFlag;
	}
	public void setIsReviewedFlag(char isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}
	public Integer getVitalId() {
		return vitalId;
	}
	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
		public Integer getVitalQualifireId() {
		return vitalQualifireId;
	}
	public void setVitalQualifireId(Integer vitalQualifireId) {
		this.vitalQualifireId = vitalQualifireId;
	}
	public List<AppointmentVitalMapperDto> getVitalMethodList() {
		return vitalMethodList;
	}
	public void setVitalMethodList(List<AppointmentVitalMapperDto> vitalMethodList) {
		this.vitalMethodList = vitalMethodList;
	}
	public List<AppointmentVitalMapperDto> getVitalPositionList() {
		return vitalPositionList;
	}
	public void setVitalPositionList(List<AppointmentVitalMapperDto> vitalPositionList) {
		this.vitalPositionList = vitalPositionList;
	}
	public List<AppointmentVitalMapperDto> getVitalSiteList() {
		return vitalSiteList;
	}
	public void setVitalSiteList(List<AppointmentVitalMapperDto> vitalSiteList) {
		this.vitalSiteList = vitalSiteList;
	}
	
	public List<AppointmentVitalMapperDto> getVitalQualityList() {
		return vitalQualityList;
	}
	public void setVitalQualityList(List<AppointmentVitalMapperDto> vitalQualityList) {
		this.vitalQualityList = vitalQualityList;
	}
	public List<AppointmentVitalMapperDto> getVitalCuffList() {
		return vitalCuffList;
	}
	public void setVitalCuffList(List<AppointmentVitalMapperDto> vitalCuffList) {
		this.vitalCuffList = vitalCuffList;
	}
	public List<AppointmentVitalMapperDto> getVitalLocationList() {
		return vitalLocationList;
	}
	public void setVitalLocationList(List<AppointmentVitalMapperDto> vitalLocationList) {
		this.vitalLocationList = vitalLocationList;
	}
	
}
