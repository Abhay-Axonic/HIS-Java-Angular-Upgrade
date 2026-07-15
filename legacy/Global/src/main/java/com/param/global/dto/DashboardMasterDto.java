package com.param.global.dto;

import java.util.List;

public class DashboardMasterDto {
	
	private Integer dashboardId;
	
	private String dashboardDesc;
	
	private String dashboard_link;
	
	private Integer organizationId;
	
	private Character status;

	private String createdDate;

	private Integer createdBy;

	private Integer updatedBy;

	private String updatedDate;
	private Integer roleId;
	private Integer unitId;
	private List<Integer> roleIdList;
	public Integer getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(Integer dashboardId) {
		this.dashboardId = dashboardId;
	}

	public String getDashboardDesc() {
		return dashboardDesc;
	}

	public void setDashboardDesc(String dashboardDesc) {
		this.dashboardDesc = dashboardDesc;
	}

	public String getDashboard_link() {
		return dashboard_link;
	}

	public void setDashboard_link(String dashboard_link) {
		this.dashboard_link = dashboard_link;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public List<Integer> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Integer> roleIdList) {
		this.roleIdList = roleIdList;
	}
	
	
}
