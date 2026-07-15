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
@Table(name = "t_patient_registration_doc_details", schema = "patient")
@SequenceGenerator(name = "t_patient_registration_doc_details_seq", sequenceName = "patient.t_patient_registration_doc_details_seq", allocationSize = 1)
public class PatientRegistrationDocDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_patient_registration_doc_details_seq")
    @Column(name = "patient_doc_id")
    private Integer patientDocId;

    @Column(name = "patient_id")
    private Integer patientId;

    @Column(name = "document_path")
    private String documentPath;
    
    @Column(name="status")
    private char status;
    
    @Column(name="created_date")
    private Date createdDate;
    
    @Column(name="updated_date")
    private Date updatedDate;
    
    @Column(name="unit_id")
    private Integer unitId;
    
    @Column(name="organization_id")
    private Integer organizationId;
    
    @Column(name="file_name")
    private String fileName;

	public Integer getPatientDocId() {
		return patientDocId;
	}

	public void setPatientDocId(Integer patientDocId) {
		this.patientDocId = patientDocId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
    
}
