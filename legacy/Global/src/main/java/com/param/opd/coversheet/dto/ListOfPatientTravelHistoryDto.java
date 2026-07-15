package com.param.opd.coversheet.dto;

public class ListOfPatientTravelHistoryDto {

	private String description;
	private String whenTravel;
	private char isReviewedFlag;
	private char isEnterInErrorStatus;
	private Integer travelHistoryDetailsId;
	private Character isNoSignificantTravel;
	private boolean isDevHistory;

	public char getIsReviewedFlag() {
		return isReviewedFlag;
	}
	public void setIsReviewedFlag(char isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}
	public char getIsEnterInErrorStatus() {
		return isEnterInErrorStatus;
	}
	public void setIsEnterInErrorStatus(char isEnterInErrorStatus) {
		this.isEnterInErrorStatus = isEnterInErrorStatus;
	}
	public Integer getTravelHistoryDetailsId() {
		return travelHistoryDetailsId;
	}
	public void setTravelHistoryDetailsId(Integer travelHistoryDetailsId) {
		this.travelHistoryDetailsId = travelHistoryDetailsId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getWhenTravel() {
		return whenTravel;
	}
	public void setWhenTravel(String whenTravel) {
		this.whenTravel = whenTravel;
	}
	public Character getIsNoSignificantTravel() {
		return isNoSignificantTravel;
	}
	public void setIsNoSignificantTravel(Character isNoSignificantTravel) {
		this.isNoSignificantTravel = isNoSignificantTravel;
	}
	public boolean getIsDevHistory() {
		return isDevHistory;
	}
	public void setIsDevHistory(boolean isDevHistory) {
		this.isDevHistory = isDevHistory;
	}
	
	
}
