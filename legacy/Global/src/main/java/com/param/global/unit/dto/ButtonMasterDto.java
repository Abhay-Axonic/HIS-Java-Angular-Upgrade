package com.param.global.unit.dto;

import java.util.List;

public class ButtonMasterDto {
	private String systemPrompt;
	private String userPrompt;
	private String label;
	private Integer tabId;
	private Integer buttonId;
	private boolean status;
	private Integer hospitalId;
	private Integer doctorId;
	private Integer specialityId;
	private String jsonObject;
	private Integer configId;
	private String doctorName;
	private String specialityName;
	private Integer unitId;
	private String  jsonConfiguration;
	private List<Integer> tabIds;
	public String getSystemPrompt() {
		return systemPrompt;
	}
	public void setSystemPrompt(String systemPrompt) {
		this.systemPrompt = systemPrompt;
	}
	public String getUserPrompt() {
		return userPrompt;
	}
	public void setUserPrompt(String userPrompt) {
		this.userPrompt = userPrompt;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getTabId() {
		return tabId;
	}
	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}
	public Integer getButtonId() {
		return buttonId;
	}
	public void setButtonId(Integer buttonId) {
		this.buttonId = buttonId;
	}
	public boolean getIsStatus() {
		return status;
	}
	public void setIsStatus(boolean status) {
		this.status = status;
	}
	public Integer getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(Integer hospitalId) {
		this.hospitalId = hospitalId;
	}
	public String getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public Integer getSpecialityId() {
		return specialityId;
	}
	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}
	public Integer getConfigId() {
		return configId;
	}
	public void setConfigId(Integer configId) {
		this.configId = configId;
	}
	public String getDoctorName() {
		return doctorName;
	}
	public void setDoctorName(String doctorName) {
		this.doctorName = doctorName;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getJsonConfiguration() {
		return jsonConfiguration;
	}
	public void setJsonConfiguration(String jsonConfiguration) {
		this.jsonConfiguration = jsonConfiguration;
	}

	public List<Integer> getTabIds() {
		return tabIds;
	}

	public void setTabIds(List<Integer> tabIds) {
		this.tabIds = tabIds;
	}
}
