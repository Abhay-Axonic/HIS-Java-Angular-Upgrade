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
@Table(name = "t_lab_report_document_details", schema = "lab")
@SequenceGenerator(name = "t_lab_report_document_details_seq", sequenceName = "lab.t_lab_report_document_details_seq", allocationSize = 1)
public class LabReportDocumentDetails {
	@Id
	@Column(name = "lab_report_doc_dtls_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_lab_report_document_details_seq")
	private Integer labReportDocDtlsId;

	@Column(name = "lab_sample_dtls_id")
	private Integer labSampleDtlsId;

	@Column(name = "test_id")
	private Integer testId;

	@Column(name = "file_path")
	private String filePath;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "image")
	private byte[] image;

	public Integer getLabReportDocDtlsId() {
		return labReportDocDtlsId;
	}

	public void setLabReportDocDtlsId(Integer labReportDocDtlsId) {
		this.labReportDocDtlsId = labReportDocDtlsId;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Integer getTestId() {
		return testId;
	}

	public void setTestId(Integer testId) {
		this.testId = testId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
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

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

}
