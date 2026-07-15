package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.Item;

@NamedQueries({ @NamedQuery(name = "GET_VACCINATION_MASTER_LIST_BY_ROLEID", query = "SELECT "
		+ "vaccinationRoleMapper.vaccinationRoleMapperId AS vaccinationRoleMapperId, "
		+ "vaccinationRoleMapper.status AS status, " + "vaccinationRoleMapper.vaccinationId AS vaccinationId, "
		+ "vaccinationRoleMapper.organizationId AS organizationId, " + "vaccinationRoleMapper.unitId AS unitId, "
		+ "userRoleMaster.roleId AS roleId " + "FROM VaccinationRoleMapper vaccinationRoleMapper "
		+ "LEFT JOIN vaccinationRoleMapper.userRoleMaster userRoleMaster "
		+ "WHERE vaccinationRoleMapper.organizationId = :organizationId "
		+ "AND vaccinationRoleMapper.unitId = :unitId " + "AND userRoleMaster.roleId = :roleId "
		+ "AND vaccinationRoleMapper.status = 'A'"), })

@NamedNativeQueries({ @NamedNativeQuery(name = "GET_VACCINATION_MAPPER_LIST_BY_ROLE_ID", query = "SELECT "
		+ "  mvrm.vaccination_id AS \"vaccinationId\", " + "  mi.name AS \"vaccinationName\" " + "FROM "
		+ "  m_vaccination_role_mapper mvrm" + "  INNER JOIN m_item mi  ON mi.id  = mvrm.vaccination_id"
		+ " WHERE  mvrm.role_id = :roleId AND " + "  mvrm.unit_id = :unitId AND "
		+ "  mvrm.organization_id = :organizationId AND " + " mvrm.status = 'A' ")

})

@Entity
@Table(name = "m_vaccination_role_mapper", schema = "public")
@SequenceGenerator(name = "m_vaccination_role_mapper_seq", sequenceName = "public.m_vaccination_role_mapper_seq", allocationSize = 1)
public class VaccinationRoleMapper {

	@Id
	@Column(name = "vaccination_role_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_vaccination_role_mapper_seq")
	private Integer vaccinationRoleMapperId;

	@Column(name = "vaccination_id")
	private Integer vaccinationId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "status")
	private Character status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", nullable = false)
	private UserRoleMaster userRoleMaster;

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

	public Integer getVaccinationId() {
		return vaccinationId;
	}

	public void setVaccinationId(Integer vaccinationId) {
		this.vaccinationId = vaccinationId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organisationId) {
		this.organizationId = organisationId;
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

}
