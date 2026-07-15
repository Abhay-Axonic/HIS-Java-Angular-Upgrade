package com.param.global.dto.sync;

public class GlobalUnitSyncDto {
	private Integer unitId;
	private String unitName;
	private char status;
	private String createdDate;
	private Integer localId;
	private Integer applicationId;
	private Integer globalUnitId;
	private Integer countryId;
	private String currency;
	private String unitAddress;
	private String unitAddressLink;
	private String unitEmail;
	private String unitContactNumber;
	private Integer channelId;
	private Character isPreOrPostBilling;

	public Integer getGlobalUnitId() {
		return globalUnitId;
	}
	public void setGlobalUnitId(Integer globalUnitId) {
		this.globalUnitId = globalUnitId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = (status != '\u0000' ? status : 'I');
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public Integer getLocalId() {
		return localId;
	}
	public void setLocalId(Integer localId) {
		this.localId = localId;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getUnitAddress() {
		return unitAddress;
	}
	public void setUnitAddress(String unitAddress) {
		this.unitAddress = unitAddress;
	}
	public String getUnitAddressLink() {
		return unitAddressLink;
	}
	public void setUnitAddressLink(String unitAddressLink) {
		this.unitAddressLink = unitAddressLink;
	}
	public String getUnitEmail() {
		return unitEmail;
	}
	public void setUnitEmail(String unitEmail) {
		this.unitEmail = unitEmail;
	}
	public String getUnitContactNumber() {
		return unitContactNumber;
	}
	public void setUnitContactNumber(String unitContactNumber) {
}
	public Integer getChannelId() {
		return channelId;
	}
	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	public Character getIsPreOrPostBilling() {
		return isPreOrPostBilling;
	}
	public void setIsPreOrPostBilling(Character isPreOrPostBilling) {
		this.isPreOrPostBilling = isPreOrPostBilling;
	}
	
}
