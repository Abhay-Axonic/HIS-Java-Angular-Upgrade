package com.param.adt.master.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
@NamedNativeQueries({

@NamedNativeQuery(name="GET_CONSENT_DOCUMENT_LIST_BY_MAPPER_ID",
query="select dtls.document_details_id as \"documentDetailsId\"," + 
		"dtls.document_name as \"documentName\", " + 
		"dtls.document_path  as \"documentPath\", " + 
		"to_char(dtls.created_date,'DD-MM-YYYY HH:mm:ss') as \"createdDate\" , " + 
		"docMstr.document_type_name as \"documnetTypeName\" " + 
		"from public.t_patient_consent_document_details dtls " + 
		"inner join public.m_document_type_master docMstr on dtls.document_type_id = docMstr.document_type_id " + 
		"where dtls.consent_transaction_id = :consentTransactionId " + 
		"and dtls.organization_id = :orgId " + 
		"and dtls.unit_id = :unitId "
		+ "and  dtls.status = 'A' "),
})

@Entity
@Table(name = "t_patient_consent_document_details" , schema = "public")
@SequenceGenerator(name = "t_patient_consent_document_details_seq" , sequenceName = "public.t_patient_consent_document_details_seq" , allocationSize = 1)
public class PatientConsentDocumentDetails {
	
	@Id
	@Column(name = "document_details_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "t_patient_consent_document_details_seq")
	private Integer documentDetailsId;
	
	@Column(name="consent_transaction_id")
	private Integer consentTransactionId;
	
	@Column(name="document_type_id")
	private Integer documentTypeId;
	
	@Column(name="document_path")
	private String documentPath;
	
	@Column(name="document_name")
	private String documentName;
	
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
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;

	public Integer getDocumentDetailsId() {
		return documentDetailsId;
	}

	public void setDocumentDetailsId(Integer documentDetailsId) {
		this.documentDetailsId = documentDetailsId;
	}

	public Integer getConsentTransactionId() {
		return consentTransactionId;
	}

	public void setConsentTransactionId(Integer consentTransactionId) {
		this.consentTransactionId = consentTransactionId;
	}

	public Integer getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(Integer documentTypeId) {
		this.documentTypeId = documentTypeId;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
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
