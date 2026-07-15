package com.param.opd.coversheet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_encounter_doc_details", schema="opd")
@SequenceGenerator(name = "seq_t_encounter_doc_details", sequenceName = "opd.seq_t_encounter_doc_details", allocationSize = 1)
public class EncounterDocDetails {

	@Id
	@Column(name="encounter_doc_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_t_encounter_doc_details")
	private int encounterDocId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="patient_id")
	private Integer patientId;

	@Column(name = "document_type_id")
	private int documentTypeId;
	
	@Column(name = "document_path")
	private String documentPath;
	
	@Column(name="status")
	private char status;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="preauth_request_id")
	private Integer preauthRequestId;
	
	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="lab_sample_dtls_id")
	private Integer labSampleDtlsId;
	
	@Column(name="upload_doccument_date")
	private Date uploadDoccumentDate;
	
	@Column(name="expense_id")
	private Integer expenseId;
	
	@Column(name="file_name")
	private String fileName;
	
	@Column(name="grn_id")
	private Integer grnId;
	
	@Column(name="company_expense_id ")
	private Integer companyExpenseId ;
	
	@Column(name="incident_reporting_mapper_id")
	private Integer incidentReportingMapperId;
	
    @Column(name="remark")
    private String remark;	
	
    @Column(name="po_id")
	private Integer poId;
    
    @Column(name="death_patient_id")
  	private Integer deathPatientId;
      
    @Column(name="opb_id")
	private Integer opbId;
    
    @Column(name="nabh_reporting_mapper_id")
	private Integer nabhReportingMapperId;

	@Column(name="document_analyzed_path")
	private String documentAnalyzedPath;

	@Column(name = "document_analyzed_date")
	private Date documentAnalyzedDate;
    
	public Integer getNabhReportingMapperId() {
		return nabhReportingMapperId;
	}

	public void setNabhReportingMapperId(Integer nabhReportingMapperId) {
		this.nabhReportingMapperId = nabhReportingMapperId;
	}

	public Integer getCompanyExpenseId() {
		return companyExpenseId;
	}

	public void setCompanyExpenseId(Integer companyExpenseId) {
		this.companyExpenseId = companyExpenseId;
	}

	public Integer getExpenseId() {
		return expenseId;
	}

	public void setExpenseId(Integer expenseId) {
		this.expenseId = expenseId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getEncounterDocId() {
		return encounterDocId;
	}

	public void setEncounterDocId(int encounterDocId) {
		this.encounterDocId = encounterDocId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public int getDocumentTypeId() {
		return documentTypeId;
	}

	public void setDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
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

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getPreauthRequestId() {
		return preauthRequestId;
	}

	public void setPreauthRequestId(Integer preauthRequestId) {
		this.preauthRequestId = preauthRequestId;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Date getUploadDoccumentDate() {
		return uploadDoccumentDate;
	}

	public void setUploadDoccumentDate(Date uploadDoccumentDate) {
		this.uploadDoccumentDate = uploadDoccumentDate;
	}

	public Integer getGrnId() {
		return grnId;
	}

	public void setGrnId(Integer grnId) {
		this.grnId = grnId;
	}

	public Integer getIncidentReportingMapperId() {
		return incidentReportingMapperId;
	}

	public void setIncidentReportingMapperId(Integer incidentReportingMapperId) {
		this.incidentReportingMapperId = incidentReportingMapperId;
	}	
	
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

	public Integer getPoId() {
		return poId;
	}

	public void setPoId(Integer poId) {
		this.poId = poId;
	}

	public Integer getDeathPatientId() {
		return deathPatientId;
	}

	public void setDeathPatientId(Integer deathPatientId) {
		this.deathPatientId = deathPatientId;
	}    	
	
	public Integer getOpbId() {
		return opbId;
	}

	public void setOpbId(Integer opbId) {
		this.opbId = opbId;
	}
/*-----------------------------------------------------vendor_id added for upload documents on vendor master---------------------------------*/	
	@Column(name="vendor_id")
	private Integer vendorId;
	
	public Integer getVendorId(){
		return vendorId;
	}
	
	public void setVendorId(Integer vendorId){
		this.vendorId = vendorId;
	}

	/*-----------------------------------------------------documentAnalyzedPath added for analyzing documents with AI---------------------------------*/
	public String getDocumentAnalyzedPath() {
		return documentAnalyzedPath;
	}

	public void setDocumentAnalyzedPath(String documentAnalyzedPath) {
		this.documentAnalyzedPath = documentAnalyzedPath;
	}

	public Date getDocumentAnalyzedDate() {
		return documentAnalyzedDate;
	}

	public void setDocumentAnalyzedDate(Date documentAnalyzedDate) {
		this.documentAnalyzedDate = documentAnalyzedDate;
	}
}
