package com.param.global.model;

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
	
	/*@NamedQuery(name="GET_INCIDENT_REPORT_MASTER_LIST",
		query="select" + 
				"	incRept.incidentReportId as incidentReportId," + 
				"	incRept.incidentReportCode as incidentReportCode," + 
				"	incRept.incidentReportDesc as incidentReportDesc," + 
				"	incRept.status as status " + 
				" from" + 
				"	IncidentReportMaster incRept" + 
				" where" + 
				"	incRept.organizationId =:organizationId" + 
				"	and incRept.unitId =:unitId "+
				" order by incidentReportId asc "
					),*/
	
	
	@NamedQuery(name="GET_INCIDENT_REPORT_MASTER_LIST",
			query="select " +
					"	incRept.incidentReportId as incidentReportId," + 
					"	incRept.incidentReportCode as incidentReportCode," + 
					"	incRept.incidentReportDesc as incidentReportDesc," + 
					"	incRept.status as status " + 
					" from" + 
					"	IncidentReportMaster incRept" + 
					" where " +
					" incRept.organizationId =:organizationId" +
					"	and incRept.unitId =:unitId "
						),
	
	
	@NamedQuery(name="GET_ACTIVE_INCIDENT_REPORT_MASTER_LIST",
		query="select" +
				" incRept.incidentReportId as incidentReportId, " +
				" incRept.incidentReportCode as incidentReportCode, " +
				" incRept.incidentReportDesc as incidentReportDesc, " +
				" incRept.status as status " +
				" from" +
				" IncidentReportMaster incRept " +
				" where " +
				"   (( incRept.incidentReportId < 100) " + 
				"	OR (incident_report_id >= 100 " +
				" and incRept.organizationId =:organizationId " + 
				"	and incRept.unitId =:unitId )) " +
				"   and incRept.status ='A' "
					),
	@NamedQuery(name="GET_INCIDENT_REPORT_MASTER_BY_ID",
		query="select" +
				" incRept.incidentReportId as incidentReportId," +
				" incRept.incidentReportCode as incidentReportCode," +
				" incRept.incidentReportDesc as incidentReportDesc," +
				" incRept.status as status " +
				" from" +
				" IncidentReportMaster incRept" +
				" where " +
				"   (( incRept.incidentReportId < 100)"+
				" and incRept.incidentReportId =:incidentReportId " + 
				"	OR (incident_report_id >= 100 " +
				" and incRept.organizationId =:organizationId " + 
				" and incRept.unitId =:unitId )) "+
				" and incRept.incidentReportId =:incidentReportId" 
				),
	@NamedQuery(name="CHECK_INCIDENT_REPORT_MASTER_AVAILABLE_BY_NAME",
	query="select" +
			" incRept.incidentReportId as incidentReportId, " +
			" incRept.incidentReportCode as incidentReportCode, " +
			" incRept.incidentReportDesc as incidentReportDesc " +
			" from" +
			" IncidentReportMaster incRept " +
			" where " +
			" incRept.organizationId =:organizationId " +
			" and incRept.unitId =:unitId "+
			" and (LOWER(incRept.incidentReportDesc)=:incidentReportDesc OR incRept.incidentReportDesc=:incidentReportDesc) "
			),
})
@Entity
@Table(name = "m_incident_report_master", schema = "public")
@SequenceGenerator(name = "m_incident_report_master_seq", sequenceName = "public.m_incident_report_master_seq", allocationSize = 1)

public class IncidentReportMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_incident_report_master_seq")
	@Column(name="incident_report_id")
	private Integer incidentReportId;
	
	@Column(name="incident_report_code")
	private String incidentReportCode;
	
	@Column(name="incident_report_desc")
	private String incidentReportDesc;
	
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

	public Integer getIncidentReportId() {
		return incidentReportId;
	}

	public void setIncidentReportId(Integer incidentReportId) {
		this.incidentReportId = incidentReportId;
	}

	public String getIncidentReportCode() {
		return incidentReportCode;
	}

	public void setIncidentReportCode(String incidentReportCode) {
		this.incidentReportCode = incidentReportCode;
	}

	public String getIncidentReportDesc() {
		return incidentReportDesc;
	}

	public void setIncidentReportDesc(String incidentReportDesc) {
		this.incidentReportDesc = incidentReportDesc;
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
	
	
	
}



