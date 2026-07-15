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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_reports_master", schema = "public")
@SequenceGenerator(name="m_reports_master_seq", sequenceName="public.m_reports_master_seq", allocationSize=1)
public class ReportMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_reports_master_seq")
	@Column(name = "report_id")
	private Integer reportId;
	
	@Column(name = "report_name")
	private String reportName;
	
	@Column(name = "action")
	private String action;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;

	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="reportgroupheader_id")
	private Integer reportGroupHeaderId;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "reportMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ReportMasterDetails> listReportMasterDetails;


	public Integer getReportId() {
		return reportId;
	}

	public void setReportId(Integer reportId) {
		this.reportId = reportId;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
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

	public Integer getReportGroupHeaderId() {
		return reportGroupHeaderId;
	}

	public void setReportGroupHeaderId(Integer reportGroupHeaderId) {
		this.reportGroupHeaderId = reportGroupHeaderId;
	}

	public List<ReportMasterDetails> getListReportMasterDetails() {
		return listReportMasterDetails;
	}

	public void setListReportMasterDetails(List<ReportMasterDetails> listReportMasterDetails) {
		this.listReportMasterDetails = listReportMasterDetails;
	}
	
}
