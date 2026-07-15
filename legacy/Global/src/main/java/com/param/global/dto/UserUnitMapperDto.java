package com.param.global.dto;

import java.util.Date;

public class UserUnitMapperDto {
	
	private Integer userUnitMpprId;
	private Integer userId;
	private Character status;
	private Integer organisationId;
	private Integer unitId;
	private int createdBy;
	private Date createdDate;
	private int updatedBy;
	private Date updatedDate;
	private String isUnitSelected;
	private String misRights;
	private boolean isRenderedServiceCancelled;
	private boolean isUpdatePatientReg;
	private String coversheetRights;
	private String tabAccessIds;
	private boolean isUpdateIpdChargesRemark;

	public String getCoversheetRights() {
		return coversheetRights;
	}
	public void setCoversheetRights(String coversheetRights) {
		this.coversheetRights = coversheetRights;
	}
	public String getMisRights() {
		return misRights;
	}
	public void setMisRights(String misRights) {
		this.misRights = misRights;
	}
	public Integer getUserUnitMpprId() {
		return userUnitMpprId;
	}
	public void setUserUnitMpprId(Integer userUnitMpprId) {
		this.userUnitMpprId = userUnitMpprId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Character getStatus() {
		return status;
	}
	public void setStatus(Character status) {
		this.status = status;
	}
	public Integer getOrganisationId() {
		return organisationId;
	}
	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getIsUnitSelected() {
		return isUnitSelected;
	}
	public void setIsUnitSelected(String isUnitSelected) {
		this.isUnitSelected = isUnitSelected;
	}
	public boolean getIsRenderedServiceCancelled() {
		return isRenderedServiceCancelled;
	}
	public void setIsRenderedServiceCancelled(boolean isRenderedServiceCancelled) {
		this.isRenderedServiceCancelled = isRenderedServiceCancelled;
	}
	public boolean getIsUpdatePatientReg() {
		return isUpdatePatientReg;
	}
	public void setIsUpdatePatientReg(boolean isUpdatePatientReg) {
		this.isUpdatePatientReg = isUpdatePatientReg;
	}
	public String getTabAccessIds() {
		return tabAccessIds;
	}
	public void setTabAccessIds(String tabAccessIds) {
		this.tabAccessIds = tabAccessIds;
	}
	public boolean getIsUpdateIpdChargesRemark() {
		return isUpdateIpdChargesRemark;
	}
	public void setIsUpdateIpdChargesRemark(boolean isUpdateIpdChargesRemark) {
		this.isUpdateIpdChargesRemark = isUpdateIpdChargesRemark;
	}
	
	
	
}
