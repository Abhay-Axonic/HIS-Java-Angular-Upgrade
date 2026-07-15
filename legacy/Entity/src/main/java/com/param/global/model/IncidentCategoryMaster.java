package com.param.global.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@NamedQueries({
	
	@NamedQuery(name="GET_INCIDENT_CATEGORY_MASTER_LIST",
			query="SELECT incidentCategoryMaster.incidentCategoryId AS incidentCategoryId, incidentCategoryMaster.incidentCategoryCode AS incidentCategoryCode,"
				        + " incidentCategoryMaster.incidentCategoryDesc AS incidentCategoryDesc,"
						+ " incidentCategoryMaster.organizationId AS organizationId, incidentCategoryMaster.unitId AS unitId, incidentCategoryMaster.status AS status"
						+ " FROM IncidentCategoryMaster incidentCategoryMaster"
						+ " WHERE  incidentCategoryMaster.organizationId= :organizationId "
						+ " AND incidentCategoryMaster.unitId= :unitId"
						+ " ORDER BY incidentCategoryMaster.incidentCategoryId asc"
						),
	
	
	@NamedQuery(name="GET_ACTIVE_INCIDENT_CATEGORY_MASTER",
			query="SELECT incidentCategoryMaster.incidentCategoryId AS incidentCategoryId, incidentCategoryMaster.incidentCategoryCode AS incidentCategoryCode,"
			    + " incidentCategoryMaster.incidentCategoryDesc AS incidentCategoryDesc,"
				+ " incidentCategoryMaster.organizationId AS organizationId, incidentCategoryMaster.unitId AS unitId, incidentCategoryMaster.status AS status"
				+ " FROM IncidentCategoryMaster incidentCategoryMaster"
				+ " WHERE incidentCategoryMaster.status= 'A'"
				+ " AND incidentCategoryMaster.organizationId= :organizationId "
				+ " AND incidentCategoryMaster.unitId= :unitId "
				+ " ORDER BY incidentCategoryMaster.incidentCategoryDesc asc "
				),
	
	@NamedQuery(name="GET_INCIDENT_CATEGORY_MASTER_BY_ID",
			query="SELECT incidentCategoryMaster.incidentCategoryId AS incidentCategoryId, incidentCategoryMaster.incidentCategoryCode AS incidentCategoryCode,"
					    + " incidentCategoryMaster.incidentCategoryDesc AS incidentCategoryDesc,"
						+ " incidentCategoryMaster.organizationId AS organizationId, incidentCategoryMaster.unitId AS unitId, incidentCategoryMaster.status AS status"
						+ " FROM IncidentCategoryMaster incidentCategoryMaster"
						+ " WHERE incidentCategoryMaster.incidentCategoryId= :incidentCategoryId"
						+ " AND incidentCategoryMaster.organizationId= :organizationId"
						+ " AND incidentCategoryMaster.unitId= :unitId"
						),

	@NamedQuery(name="GET_INCIDENT_CATEGORY_MASTER_BY_NAME",
		query="SELECT incidentCategoryMaster.incidentCategoryId AS incidentCategoryId "
					+ "	FROM IncidentCategoryMaster incidentCategoryMaster"
					+ " WHERE (LOWER(incidentCategoryMaster.incidentCategoryDesc)=:incidentCategoryDesc OR incidentCategoryMaster.incidentCategoryDesc=:incidentCategoryDesc) "
					+ " AND incidentCategoryMaster.unitId= :unitId"
					),
	
	@NamedQuery(name="GET_INCIDENT_CATEGORY_BY_NAME_NOT_ID",
	query="SELECT incidentCategoryMaster.incidentCategoryId AS incidentCategoryId "
				+ " FROM IncidentCategoryMaster incidentCategoryMaster"
				+ " WHERE (LOWER(incidentCategoryMaster.incidentCategoryDesc)=:incidentCategoryDesc OR incidentCategoryMaster.incidentCategoryDesc=:incidentCategoryDesc) "
				+ " AND incidentCategoryMaster.incidentCategoryId != :incidentCategoryId"
				+ " AND incidentCategoryMaster.unitId = :unitId"
				),
	
	
	})

@Entity
@Table(name = "m_incident_category_master", schema = "public")
@SequenceGenerator(name = "m_incident_category_master_seq", sequenceName = "public.m_incident_category_master_seq", allocationSize = 1)
public class IncidentCategoryMaster {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_incident_category_master_seq")
	
	@Column(name = "incident_category_id")
	private Integer incidentCategoryId;
	
	@Column(name = "incidentCategoryCode")
	private String incidentCategoryCode;
	
	@Column(name = "incidentCategoryDesc")
	private String incidentCategoryDesc;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "incidentCategoryMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<IncidentReportingMapper> incidentCategoryList;
	
	public Integer getIncidentCategoryId() {
		return incidentCategoryId;
	}

	public void setIncidentCategoryId(Integer incidentCategoryId) {
		this.incidentCategoryId = incidentCategoryId;
	}

	public String getIncidentCategoryCode() {
		return incidentCategoryCode;
	}

	public void setIncidentCategoryCode(String incidentCategoryCode) {
		this.incidentCategoryCode = incidentCategoryCode;
	}

	public String getIncidentCategoryDesc() {
		return incidentCategoryDesc;
	}

	public void setIncidentCategoryDesc(String incidentCategoryDesc) {
		this.incidentCategoryDesc = incidentCategoryDesc;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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
