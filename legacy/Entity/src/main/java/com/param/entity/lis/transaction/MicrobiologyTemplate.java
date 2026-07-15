package com.param.entity.lis.transaction;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_microbiology_template", schema = "lab")
@SequenceGenerator(name = "t_microbiology_template_seq", sequenceName = "lab.t_microbiology_template_seq", allocationSize = 1)
public class MicrobiologyTemplate {

	@Id
	@Column(name = "microbiology_template_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_microbiology_template_seq")
	private Integer microbiologyTemplateId;
	
	@Column(name = "lab_sample_dtls_id")
	private Integer labSampleDtlsId;
	
	@Column(name = "template_id")
	private Integer templateId;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "template_object")
	private String templateObject;
	
	@Column(name="is_provisional_release")
	private Character isProvisionalRelease;
	
	@Column(name="is_final_release")
	private Character isFinalRelease;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private char status;
	
	@Column(name="report_entry_date")
	private Date reportEntryDate;
	
	@Column(name="report_validation_date")
	private Date reportValidationDate;
	
	@Column(name="report_release_date")
	private Date reportReleaseDate;
	
	@Column(name="is_report_recall")
	private boolean isReportRecall;

	@Column(name="validated_by")
	private Integer validatedBy;

	public Integer getMicrobiologyTemplateId() {
		return microbiologyTemplateId;
	}

	public void setMicrobiologyTemplateId(Integer microbiologyTemplateId) {
		this.microbiologyTemplateId = microbiologyTemplateId;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTemplateObject() {
		return templateObject;
	}

	public void setTemplateObject(String templateObject) {
		this.templateObject = templateObject;
	}

	public Character getIsProvisionalRelease() {
		return isProvisionalRelease;
	}

	public void setIsProvisionalRelease(Character isProvisionalRelease) {
		this.isProvisionalRelease = isProvisionalRelease;
	}

	public Character getIsFinalRelease() {
		return isFinalRelease;
	}

	public void setIsFinalRelease(Character isFinalRelease) {
		this.isFinalRelease = isFinalRelease;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public Date getReportEntryDate() {
		return reportEntryDate;
	}

	public void setReportEntryDate(Date reportEntryDate) {
		this.reportEntryDate = reportEntryDate;
	}

	public Date getReportValidationDate() {
		return reportValidationDate;
	}

	public void setReportValidationDate(Date reportValidationDate) {
		this.reportValidationDate = reportValidationDate;
	}

	public Date getReportReleaseDate() {
		return reportReleaseDate;
	}

	public void setReportReleaseDate(Date reportReleaseDate) {
		this.reportReleaseDate = reportReleaseDate;
	}

	public boolean getIsReportRecall() {
		return isReportRecall;
	}

	public void setIsReportRecall(boolean isReportRecall) {
		this.isReportRecall = isReportRecall;
	}

	public Integer getValidatedBy() {
		return validatedBy;
	}

	public void setValidatedBy(Integer validatedBy) {
		this.validatedBy = validatedBy;
	}
}
