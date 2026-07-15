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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.RoleMaster;


@NamedQueries({
	
	@NamedQuery(name="GET_INCIDENT_REPORT_MASTER_LIST_FOR_ROLE",
		query="select" + 
				"	incRol.incidentReportId as incidentReportId," + 
				"	incRol.incidentReportRoleId as incidentReportRoleId," + 
				"	incRol.roleId as roleId," + 
				"	incRol.status as status," + 
				"	roleMaster.roleDesc as roleName" + 
				" from" + 
				"	IncidentReportRoleMapper incRol" + 
				" inner join incRol.roleMaster roleMaster with roleMaster.status='A' " + 
				" where" + 
				"	incRol.organizationId =:organizationId" + 
				"	and incRol.unitId =:unitId" + 
				"	and incRol.incidentReportId =:incidentReportId "+
				"	and incRol.status ='A' "
				+ " ORDER BY roleMaster.roleDesc ASC "
					),
	@NamedQuery(name="CHECK_INCIDENT_REPORT_ROLE_AVAILABLE_BY_ROLE_ID",
	query="select" +
	" incRol.incidentReportId as incidentReportId," +
	" incRol.roleId as roleId " +
	" from" +
	" IncidentReportRoleMapper incRol" +
	" where" +
	" incRol.organizationId =:organizationId" +
	" and incRol.unitId =:unitId" +
	" and incRol.incidentReportId =:incidentReportId "+
	" and incRol.roleId =:roleId "
	),
	
	@NamedQuery(name="GET_NABH_PROCESS_MASTER_LIST_FOR_ROLE",
	query="select" + 
			"	incRol.nabhProcessId as nabhProcessId," + 
			"	incRol.incidentReportRoleId as incidentReportRoleId," + 
			"	incRol.roleId as roleId," + 
			"	incRol.status as status," + 
			"	roleMaster.roleDesc as roleName" + 
			" from" + 
			"	IncidentReportRoleMapper incRol" + 
			" inner join incRol.roleMaster roleMaster with roleMaster.status='A' " + 
			" where" + 
			"	incRol.organizationId =:organizationId" + 
			"	and incRol.unitId =:unitId" + 
			"	and incRol.nabhProcessId =:nabhProcessId "+
			"	and incRol.status ='A' "
			+ " ORDER BY roleMaster.roleDesc ASC "
				),
})
@Entity
@Table(name = "t_incident_report_role_mapper", schema = "public")
@SequenceGenerator(name = "t_incident_report_role_mapper_seq", sequenceName = "public.t_incident_report_role_mapper_seq", allocationSize = 1)

public class IncidentReportRoleMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_incident_report_role_mapper_seq")
	@Column(name="incident_report_role_id")
	private Integer incidentReportRoleId;
	
	@Column(name="incident_report_id")
	private Integer incidentReportId;
	
	@Column(name="role_id")
	private Integer roleId																		;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="nabh_process_id")
	private Integer nabhProcessId;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="role_id", insertable=false, updatable=false)
	private RoleMaster roleMaster;

	public Integer getIncidentReportRoleId() {
		return incidentReportRoleId;
	}

	public void setIncidentReportRoleId(Integer incidentReportRoleId) {
		this.incidentReportRoleId = incidentReportRoleId;
	}

	public Integer getIncidentReportId() {
		return incidentReportId;
	}

	public void setIncidentReportId(Integer incidentReportId) {
		this.incidentReportId = incidentReportId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
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

	public Integer getNabhProcessId() {
		return nabhProcessId;
	}

	public void setNabhProcessId(Integer nabhProcessId) {
		this.nabhProcessId = nabhProcessId;
	}
	
	
}

