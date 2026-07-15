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
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@NamedQuery(name="GET_DASHBOARD_MASTER_LIST",
		query="SELECT dashboardMaster.dashboardId AS dashboardId, dashboardMaster.dashboardDesc AS dashboardDesc "
				+ " FROM DashboardMaster dashboardMaster "
				+ " WHERE dashboardMaster.status = 'A' "
				+ " ORDER BY dashboardMaster.dashboardId asc"

)
@Entity
@Table(name = "m_dashboard_master", schema = "public")
@SequenceGenerator(name = "m_dashboard_master_seq", sequenceName = "public.m_dashboard_master_seq", allocationSize = 1)
public class DashboardMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_dashboard_master_seq")
	@Column(name = "dashboard_id")
	private Integer dashboardId;
	
	@Column(name = "dashboard_desc")
	private String dashboardDesc;
	
	@Column(name = "dashboard_link")
	private String dashboard_link;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "dashboardMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRoleMaster> listUserRoleMaster;
	
	public Integer getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(Integer dashboardId) {
		this.dashboardId = dashboardId;
	}

	public String getDashboardDesc() {
		return dashboardDesc;
	}

	public void setDashboardDesc(String dashboardDesc) {
		this.dashboardDesc = dashboardDesc;
	}

	public String getDashboard_link() {
		return dashboard_link;
	}

	public void setDashboard_link(String dashboard_link) {
		this.dashboard_link = dashboard_link;
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
	
	
	

}
