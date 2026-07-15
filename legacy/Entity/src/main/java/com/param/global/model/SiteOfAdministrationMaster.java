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
	
	@NamedQuery(name="GET_SITE_OF_ADMININSTRATION_MASTER_LIST",
		query="SELECT siteOfAdministrationMaster.siteId AS siteId, siteOfAdministrationMaster.siteCode AS siteCode, siteOfAdministrationMaster.siteName AS siteName,"
					+ " siteOfAdministrationMaster.organizationId AS organizationId,siteOfAdministrationMaster.status AS status"
					+ " FROM SiteOfAdministrationMaster siteOfAdministrationMaster"
					+ " WHERE siteOfAdministrationMaster.organizationId= :organizationId"
					+ " ORDER BY siteOfAdministrationMaster.siteName asc "
					),
	
	@NamedQuery(name="GET_SITE_OF_ADMININSTRATION_MASTER_BY_ID",
		query="SELECT siteOfAdministrationMaster.siteId AS siteId, siteOfAdministrationMaster.siteCode AS siteCode, siteOfAdministrationMaster.siteName AS siteName,"
				    + " siteOfAdministrationMaster.organizationId AS organizationId,siteOfAdministrationMaster.status AS status"
				    + " FROM SiteOfAdministrationMaster siteOfAdministrationMaster"
					+ " WHERE siteOfAdministrationMaster.siteId= :siteId"
					+ " AND siteOfAdministrationMaster.organizationId= :organizationId"
			
					),
	
	@NamedQuery(name="GET_ACTIVE_SITE_OF_ADMININSTRATION_MASTER",
		query="SELECT siteOfAdministrationMaster.siteId AS siteId, siteOfAdministrationMaster.siteCode AS siteCode, siteOfAdministrationMaster.siteName AS siteName,"
			        + " siteOfAdministrationMaster.organizationId AS organizationId,siteOfAdministrationMaster.status AS status"
			        + " FROM SiteOfAdministrationMaster siteOfAdministrationMaster"
					+ " WHERE siteOfAdministrationMaster.status= 'A'"
					+ " AND siteOfAdministrationMaster.organizationId= :organizationId"
				    + " ORDER BY siteOfAdministrationMaster.siteId asc"
					),
	
	@NamedQuery(name="GET_SITE_OF_ADMININSTRATION_MASTER_BY_NAME",
		query="SELECT siteOfAdministrationMaster.siteId AS siteId "
					+ "	FROM SiteOfAdministrationMaster siteOfAdministrationMaster"
					+ " WHERE LOWER(siteOfAdministrationMaster.siteName)= :siteName"
					+ " AND siteOfAdministrationMaster.organizationId= :organizationId"
					),
	
	@NamedQuery(name="GET_SITE_OF_ADMININSTRATION_MASTER_BY_NAME_NOT_ID",
		query="SELECT siteOfAdministrationMaster.siteId AS siteId "
				+ "	FROM SiteOfAdministrationMaster siteOfAdministrationMaster"
					+ " WHERE LOWER(siteOfAdministrationMaster.siteName)= :siteName"
					+ " AND siteOfAdministrationMaster.siteId != :siteId"
					+ " AND siteOfAdministrationMaster.organizationId= :organizationId"
					),
	
	@NamedQuery(name="GET_SITE_OF_ADMININSTRATION_MASTER_LIST_SEARCH",
	query="SELECT siteOfAdministrationMaster.siteId AS siteId, siteOfAdministrationMaster.siteCode AS siteCode, siteOfAdministrationMaster.siteName AS siteName,"
				+ " siteOfAdministrationMaster.organizationId AS organizationId,siteOfAdministrationMaster.status AS status"
				+ " FROM SiteOfAdministrationMaster siteOfAdministrationMaster"
				+ " WHERE siteOfAdministrationMaster.organizationId= :organizationId "
				+ " AND  LOWER(siteOfAdministrationMaster.siteCode) like :siteCode "
				+ " OR LOWER(siteOfAdministrationMaster.siteName) like :siteCode "
				+ " ORDER BY siteOfAdministrationMaster.siteId asc"
				),
	})


@Entity
@Table(name = "m_site_of_admininstration", schema = "public")
@SequenceGenerator(name = "seq_m_site_of_administration", sequenceName = "public.m_site_of_admininstration_id_seq", allocationSize = 1)
public class SiteOfAdministrationMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_m_site_of_administration")
	
	@Column(name = "site_id")
	private Integer siteId;
	
	@Column(name = "site_code")
	private String siteCode;
	
	@Column(name = "site_name")
	private String siteName;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "siteOfAdministrationMaster", cascade = CascadeType.ALL, orphanRemoval = true) 
	private List<VaccinationMaster> vaccinationMasterlist;

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
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
