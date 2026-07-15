package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_otims_user_role_mapper",schema="public")
@SequenceGenerator(name="t_otims_user_role_mapper_seq",sequenceName="public.t_otims_user_role_mapper_seq",allocationSize=1)
public class OTIMSUserRoleMapper {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_otims_user_role_mapper_seq")
	@Column(name = "user_role_mppr_id")
	private Integer userRoleMpprId;
	
	@Column(name = "user_id")
	private Integer userId;
	
	
	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "status ")
	private Character status ;
	
	@Column(name="organisation_id ")
	private Integer organisationId;
	
	@Column(name="unit_id ")
	private Integer unitId;

	@Column(name = "created_by ")
	private int createdBy;

	@Column(name = "created_date ")
	private Date createdDate;

	@Column(name = "updated_by ")
	private int updatedBy;

	@Column(name = "updated_date ")
	private Date updatedDate;

	public Integer getUserRoleMpprId() {
		return userRoleMpprId;
	}

	public void setUserRoleMpprId(Integer userRoleMpprId) {
		this.userRoleMpprId = userRoleMpprId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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
	
	
	
	}