package com.param.privilege.model;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "privilege", schema = "privilege")
public class Privilege implements Serializable {

	@Serial
	private static final long serialVersionUID = 6807808198317246730L;

	@Id
	@Column(name = "privilege_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Integer privilegeId;
	
	@Column(name = "role_id")
	Integer roleId;

	@Column(name = "modules")
	String modules;
	
	@Column(name = "organisation_id")
	Integer organisationId;
	
	@Column(name = "module_type")
	Integer moduleType;
	
	@Column(name = "unit_id")
	Integer unitId;

	public Integer getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(Integer privilegeId) {
		this.privilegeId = privilegeId;
	}
	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getModules() {
		return modules;
	}

	public void setModules(String modules) {
		this.modules = modules;
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

	public Integer getModuleType() {
		return moduleType;
	}

	public void setModuleType(Integer moduleType) {
		this.moduleType = moduleType;
	}
	
	
	

}