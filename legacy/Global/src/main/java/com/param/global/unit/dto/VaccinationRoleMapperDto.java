package com.param.global.unit.dto;

import java.util.Date;
import java.util.List;

import com.param.global.model.UserRoleMaster;

public class VaccinationRoleMapperDto {

	private Integer vaccinationRoleMapperId;

	private Integer roleId;

	private Integer id;

	private Integer vaccinationId;

	private Character status;

	private String vaccinationName;

	private Integer organizationId;

	private Integer unitId;

	private int createdBy;

	private Date createdDate;

	private int updatedBy;

	private Date updatedDate;

	private Integer userId;

	private UserRoleMaster userRoleMaster;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getVaccinationRoleMapperId() {
		return vaccinationRoleMapperId;
	}

	public void setVaccinationRoleMapperId(Integer vaccinationRoleMapperId) {
		this.vaccinationRoleMapperId = vaccinationRoleMapperId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getVaccinationId() {
		return vaccinationId;
	}

	public void setVaccinationId(Integer vaccinationId) {
		this.vaccinationId = vaccinationId;
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

	public UserRoleMaster getUserRoleMaster() {
		return userRoleMaster;
	}

	public void setUserRoleMaster(UserRoleMaster userRoleMaster) {
		this.userRoleMaster = userRoleMaster;
	}

	public String getVaccinationName() {
		return vaccinationName;
	}

	public void setVaccinationName(String vaccinationName) {
		this.vaccinationName = vaccinationName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
