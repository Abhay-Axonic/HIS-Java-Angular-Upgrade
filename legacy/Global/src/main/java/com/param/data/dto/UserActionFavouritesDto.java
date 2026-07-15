package com.param.data.dto;

public class UserActionFavouritesDto {

	private Integer id;
	
	private Integer actionId;
	
	private String actionName;
	
	private String actionLink;
	
	private Integer userId;
	
	private Integer organizationId;
	
	private Integer unitId;
	
	private Character status;
	
	public String getActionLink() {
		return actionLink;
	}

	public void setActionLink(String actionLink) {
		this.actionLink = actionLink;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActionId() {
		return actionId;
	}

	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}
	
	
}
