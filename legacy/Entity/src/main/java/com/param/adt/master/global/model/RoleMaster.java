package com.param.adt.master.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@Entity
@SequenceGenerator(name = "m_role_master_seq", sequenceName = "public.m_role_master_seq", allocationSize = 1)
@Table(name = "m_role_master", schema = "public")
public class RoleMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "m_role_master_seq")
	@Column(name = "role_id")
	private int roleId;
	
	@Column(name = "role_name")
	private String roleDesc;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "created_by")
	private int createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name="updated_by")
	private int updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	
	@ManyToOne
	@JoinColumn(name = "unit_id", insertable = false, updatable = false)
	private UnitMaster unitMaster;
	
	@Column(name = "organisation_id")
	private Integer organizationId;
	
	/*@OneToMany(fetch = FetchType.LAZY,mappedBy = "roleMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRoleMapper> listUserRoleMppr;

	
	@OneToMany(fetch = FetchType.LAZY,mappedBy = "roleMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserMaster> listUserMaster;
	*/
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "organisation_id" , insertable = false , updatable = false , nullable = false)
	private OrganizationMaster organizationMaster;

	
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

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

/*	public List<UserRoleMapper> getListUserRoleMppr() {
		return listUserRoleMppr;
	}

	public void setListUserRoleMppr(List<UserRoleMapper> listUserRoleMppr) {
		this.listUserRoleMppr = listUserRoleMppr;
	}

	public List<UserMaster> getListUserMaster() {
		return listUserMaster;
	}

	public void setListUserMaster(List<UserMaster> listUserMaster) {
		this.listUserMaster = listUserMaster;
	}*/

	public OrganizationMaster getOrganizationMaster() {
		return organizationMaster;
	}

	public void setOrganizationMaster(OrganizationMaster organizationMaster) {
		this.organizationMaster = organizationMaster;
	}

	public UnitMaster getUnitMaster() {
		return unitMaster;
	}

	public void setUnitMaster(UnitMaster unitMaster) {
		this.unitMaster = unitMaster;
	}

	
	
	/*@OneToMany(fetch = FetchType.LAZY,mappedBy = "roleMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<RoleActionMapper> listRoleActionMppr;*/
}
