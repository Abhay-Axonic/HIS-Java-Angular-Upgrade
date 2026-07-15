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

@NamedQueries({
	
	@NamedQuery(name="GET_INCIDENT_REPORT_MASTER_LIST_FOR_TEMPLATE",
		query="select" + 
				"	distinct(incTempl.incidentReportId) as incidentReportId," + 
				"	incTempl.incidentReportTemplateId as incidentReportTemplateId," + 
				"	incTempl.roleId as roleId," + 
				"	incTempl.status as status," + 
				"	templateMaster.title as title, "
				+ " incTempl.templateId as templateId, " + 
			    " templateMaster.templateObject as templateObject, " + 
				 " templateMaster.templateFormatId as templateFormatId, " +
				 " templateMaster.templateTypeId as templateTypeId, " +
				 " templateMaster.formTemplateId as formTemplateId " +
				" from" + 
				"	IncidentReportTemplateMapper incTempl" + 
				" inner join incTempl.templateMaster templateMaster with templateMaster.status='A' " + 
				" where" + 
				"	incTempl.organizationId =:organizationId " + 
				"	and incTempl.unitId =:unitId " + 
				"	and incTempl.incidentReportId =:incidentReportId "+
				"	and incTempl.roleId =:roleId "+
				"	and incTempl.status='A' "
				+ " ORDER BY templateMaster.title ASC "
					),
	@NamedQuery(name="CHECK_INCIDENT_REPORT_TEMPLATE_AVAILABLE_BY_TEMPLATE_ID",
	query="select" +
	" incTempl.incidentReportId as incidentReportId," +
	" incTempl.roleId as roleId," +
	" incTempl.roleId as templateId " +
	" from" +
	" IncidentReportTemplateMapper incTempl" +
	" where" +
	" incTempl.organizationId =:organizationId " +
	" and incTempl.unitId =:unitId " +
	" and incTempl.incidentReportId =:incidentReportId "+
	" and incTempl.templateId =:templateId "
	),
	
	@NamedQuery(name = "UPDATE_INCIDENT_REPORT_TEMPLATE",
	query = " UPDATE IncidentReportTemplateMapper incidentReportTemplateMapper "
			+ " SET incidentReportTemplateMapper.status = :status"
			+ " WHERE incidentReportTemplateMapper.organizationId= :organizationId "
			+ " AND incidentReportTemplateMapper.unitId = :unitId"
			+ " AND incidentReportTemplateMapper.incidentReportId = :incidentReportId"
		),
	
	@NamedQuery(name="GET_NABH_PROCESS_MASTER_LIST_FOR_TEMPLATE",
	query="select" + 
			"	distinct(incTempl.nabhProcessId) as nabhProcessId," + 
			"	incTempl.incidentReportTemplateId as incidentReportTemplateId," + 
			"	incTempl.roleId as roleId," + 
			"	incTempl.status as status," + 
			"	templateMaster.title as title, "
			+ " incTempl.templateId as templateId, " + 
		    " templateMaster.templateObject as templateObject, " + 
			 " templateMaster.templateFormatId as templateFormatId, " +
			 " templateMaster.templateTypeId as templateTypeId, " +
			 " templateMaster.formTemplateId as formTemplateId " +
			" from" + 
			"	IncidentReportTemplateMapper incTempl" + 
			" inner join incTempl.templateMaster templateMaster with templateMaster.status='A' " + 
			" where" + 
			"	incTempl.organizationId =:organizationId " + 
			"	and incTempl.unitId =:unitId " + 
			"	and incTempl.nabhProcessId =:nabhProcessId "+
			"	and incTempl.roleId =:roleId "+
			"	and incTempl.status='A' "
			+ " ORDER BY templateMaster.title ASC "
				),	
})
@Entity
@Table(name = "t_incident_report_template_mapper", schema = "public")
@SequenceGenerator(name = "t_incident_report_template_mapper_seq", sequenceName = "public.t_incident_report_template_mapper_seq", allocationSize = 1)

public class IncidentReportTemplateMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_incident_report_template_mapper_seq")
	@Column(name="incident_report_template_id")
	private int incidentReportTemplateId;
	
	@Column(name="template_id")
	private Integer templateId;
	
	@Column(name="incident_report_id")
	private Integer incidentReportId;
	
	@Column(name="role_id")
	private Integer roleId;
	
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
	@JoinColumn(name="template_id", insertable=false, updatable=false)
	private TemplateMaster templateMaster;

	public int getIncidentReportTemplateId() {
		return incidentReportTemplateId;
	}

	public void setIncidentReportTemplateId(int incidentReportTemplateId) {
		this.incidentReportTemplateId = incidentReportTemplateId;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
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
