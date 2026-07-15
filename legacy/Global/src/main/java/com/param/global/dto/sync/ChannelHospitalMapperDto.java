package com.param.global.dto.sync;

public class ChannelHospitalMapperDto {

	//private ChannelHospitalMapperId channelHospitalMapperId
	private Character status;
	private Integer createdBy;
	private String createdDate;
	private Integer channelId;
	private int hospitalId;
	private int applicationId;
	private String channelIdArray;
	public int getHospitalId() {
		return hospitalId;
	}
	public void setHospitalId(int hospitalId) {
		this.hospitalId = hospitalId;
	}
	 
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public int getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public String getChannelIdArray() {
		return channelIdArray;
	}
	public void setChannelIdArray(String channelIdArray) {
		this.channelIdArray = channelIdArray;
	}

}
