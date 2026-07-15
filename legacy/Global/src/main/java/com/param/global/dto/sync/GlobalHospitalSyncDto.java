package com.param.global.dto.sync;

public class GlobalHospitalSyncDto {
	private Integer organisationId;
	private String organisationName;
	private char status;
	private String createdDate;
	private Integer localId;
	private Integer applicationId;
	private Integer globalOrgId;
	private String visibility;
	private Integer channelId;
	public Integer getGlobalOrgId() {
		return globalOrgId;
	}
	public void setGlobalOrgId(Integer globalOrgId) {
		this.globalOrgId = globalOrgId;
	}
	public Integer getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
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
	public String getVisibility() {
		return visibility;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}
	public Integer getChannelId() {
		return channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}
	

}
