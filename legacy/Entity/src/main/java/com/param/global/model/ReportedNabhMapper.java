package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_reported_nabh_mapper", schema = "public")
@SequenceGenerator(name = "t_reported_nabh_mapper_seq", sequenceName = "public.t_reported_nabh_mapper_seq", allocationSize = 1)
public class ReportedNabhMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_reported_nabh_mapper_seq")
	@Column(name = "reported_nabh_mppr_id")
	private Integer reportedNabhMpprId;
	
	@Column(name = "resolution_details_type_id")
	private Integer resolutionDetailsTypeId;
	
	@Column(name = "is_submit")
	private Character isSubmit;
	
	@Column(name = "reported_template_id")
	private Integer reportedTemplateId;
	
	@Column(name = "parent_user_id")
	private Integer parentUserId;
	
	@Column(name = "nabh_reporting_mapper_id")
	private Integer nabhReportingMapperId;
	
	@Column(name = "nabh_process_id")
	private Integer nabhProcessId;

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

	public Integer getReportedNabhMpprId() {
		return reportedNabhMpprId;
	}

	public void setReportedNabhMpprId(Integer reportedNabhMpprId) {
		this.reportedNabhMpprId = reportedNabhMpprId;
	}

	public Integer getResolutionDetailsTypeId() {
		return resolutionDetailsTypeId;
	}

	public void setResolutionDetailsTypeId(Integer resolutionDetailsTypeId) {
		this.resolutionDetailsTypeId = resolutionDetailsTypeId;
	}

	public Character getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(Character isSubmit) {
		this.isSubmit = isSubmit;
	}

	public Integer getReportedTemplateId() {
		return reportedTemplateId;
	}

	public void setReportedTemplateId(Integer reportedTemplateId) {
		this.reportedTemplateId = reportedTemplateId;
	}

	public Integer getParentUserId() {
		return parentUserId;
	}

	public void setParentUserId(Integer parentUserId) {
		this.parentUserId = parentUserId;
	}

	public Integer getNabhReportingMapperId() {
		return nabhReportingMapperId;
	}

	public void setNabhReportingMapperId(Integer nabhReportingMapperId) {
		this.nabhReportingMapperId = nabhReportingMapperId;
	}

	public Integer getNabhProcessId() {
		return nabhProcessId;
	}

	public void setNabhProcessId(Integer nabhProcessId) {
		this.nabhProcessId = nabhProcessId;
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


}
