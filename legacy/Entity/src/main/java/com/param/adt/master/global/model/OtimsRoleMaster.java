package com.param.adt.master.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@NamedQueries({
	
	@NamedQuery(name="CHECK_FOR_UNIQUE_OTIMS_ROLE",
		query="SELECT mstr.roleId as roleId "
					+ " FROM OtimsRoleMaster mstr"
					+ " WHERE mstr.roleName= :roleName"
					+ " AND mstr.orgId= :orgId"
					+ " AND mstr.unitId= :unitId"),
	
	@NamedQuery(name="GET_ALL_OTIMS_ROLE_MASTER_LIST",query="SELECT "
			+ "roleId as roleId , "
			+ "roleName as roleName, "
			+ "orgId as orgId , "
			+ "unitId as unitId, "
			+ "status as status "  
			+ "FROM OtimsRoleMaster otimsRollMaster "
			+ "WHERE otimsRollMaster.orgId= :organisationId "
			+ "AND otimsRollMaster.unitId= :unitId "
			+ "ORDER BY otimsRollMaster.roleId asc "
			)

})
@Entity
@Table(name="m_otims_role_master",schema="public")
@SequenceGenerator(name="m_otims_role_master_role_id_seq",sequenceName="public.m_otims_role_master_role_id_seq",allocationSize=1)
public class OtimsRoleMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_otims_role_master_role_id_seq")
	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "role_name")
	private String roleName;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name="org_id")
	private Integer orgId;
	
	@Column(name="unit_id")
	private Integer unitId;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	
}
