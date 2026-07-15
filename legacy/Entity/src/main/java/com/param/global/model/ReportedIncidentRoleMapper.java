package com.param.global.model;

import java.util.Date;
import javax.persistence.NamedQueries;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQueries({
@NamedQuery(name = "UPDATE_REPORTED_INCIDENT_ROLE_MAPPER",
query = " UPDATE ReportedIncidentRoleMapper reportedIncidentRoleMapper "
		+ " SET reportedIncidentRoleMapper.status = :status"
		+ " WHERE reportedIncidentRoleMapper.organizationId= :organizationId "
		+ " AND reportedIncidentRoleMapper.unitId = :unitId"
		+ " AND reportedIncidentRoleMapper.reportedIncMpprId = :reportedIncMpprId"
	    ),
	@NamedQuery(name = "UPDATE_REPORTED_INCIDENT_ROLE_MAPPER_NABH",
	query = " UPDATE ReportedIncidentRoleMapper reportedIncidentRoleMapper "
	        + " SET reportedIncidentRoleMapper.status = :status"
	        + " WHERE reportedIncidentRoleMapper.organizationId= :organizationId "
	        + " AND reportedIncidentRoleMapper.unitId = :unitId"
	        + " AND reportedIncidentRoleMapper.reportedNabhMpprId = :repIncRoleMpprId"
	    )
	})

@Entity
@Table(name = "t_reported_incident_role_mapper", schema = "public")
@SequenceGenerator(name = "t_reported_incident_role_mapper_seq", sequenceName = "public.t_reported_incident_role_mapper_seq", allocationSize = 1)

public class ReportedIncidentRoleMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_reported_incident_role_mapper_seq")
	@Column(name = "rep_inc_role_mppr_id")
	private Integer repIncRoleMpprId;
	
	@Column(name = "reported_inc_mppr_id")
	private Integer reportedIncMpprId;
	
	@Column(name = "reported_nabh_mppr_id")
	private Integer reportedNabhMpprId;
	
	public Integer getReportedNabhMpprId() {
		return reportedNabhMpprId;
	}

	public void setReportedNabhMpprId(Integer reportedNabhMpprId) {
		this.reportedNabhMpprId = reportedNabhMpprId;
	}

	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "parent_rep_inc_user_id")
	private Integer parentRepIncUserId;
	
	@Column(name = "reported_template_id")
	private Integer reportingTemplateId;

	@Column(name = "reported_template_obj")
	private String reportedTemplateObj;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "status")
	private Character status;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;

	public Integer getRepIncRoleMpprId() {
		return repIncRoleMpprId;
	}

	public void setRepIncRoleMpprId(Integer repIncRoleMpprId) {
		this.repIncRoleMpprId = repIncRoleMpprId;
	}

	public Integer getReportedIncMpprId() {
		return reportedIncMpprId;
	}

	public void setReportedIncMpprId(Integer reportedIncMpprId) {
		this.reportedIncMpprId = reportedIncMpprId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getReportedTemplateId() {
		return reportingTemplateId;
	}

	public void setReportedTemplateId(Integer reportingTemplateId) {
		this.reportingTemplateId = reportingTemplateId;
	}

	public String getReportedTemplateObj() {
		return reportedTemplateObj;
	}

	public void setReportedTemplateObj(String reportedTemplateObj) {
		this.reportedTemplateObj = reportedTemplateObj;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getParentRepIncUserId() {
		return parentRepIncUserId;
	}

	public void setParentRepIncUserId(Integer parentRepIncUserId) {
		this.parentRepIncUserId = parentRepIncUserId;
	}

	public Integer getReportingTemplateId() {
		return reportingTemplateId;
	}

	public void setReportingTemplateId(Integer reportingTemplateId) {
		this.reportingTemplateId = reportingTemplateId;
	}


}





