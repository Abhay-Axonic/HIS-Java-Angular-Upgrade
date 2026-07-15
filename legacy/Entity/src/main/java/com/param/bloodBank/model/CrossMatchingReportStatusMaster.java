package com.param.bloodBank.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="m_cross_matching_report_status",schema="blood_bank")
@SequenceGenerator(name="m_cross_matching_report_status_seq",sequenceName="blood_bank.m_cross_matching_report_status_seq")
public class CrossMatchingReportStatusMaster {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "seq_m_blood_bank_master")
	@Column(name = "cross_matching_report_id")
	private Integer crossMatchingReportId;
	
	@Column(name = "cross_matching_report_name")
	private Integer crossMatchingReportName;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	public Integer getCrossMatchingReportId() {
		return crossMatchingReportId;
	}

	public void setCrossMatchingReportId(Integer crossMatchingReportId) {
		this.crossMatchingReportId = crossMatchingReportId;
	}

	public Integer getCrossMatchingReportName() {
		return crossMatchingReportName;
	}

	public void setCrossMatchingReportName(Integer crossMatchingReportName) {
		this.crossMatchingReportName = crossMatchingReportName;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	
}
