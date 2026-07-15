package com.param.adt.master.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_patient_consent_mapper" , schema = "public")
@SequenceGenerator(name = "seq_patient_consent_mapper" , sequenceName = "public.seq_patient_consent_mapper" , allocationSize = 1)
public class PatientConsentMapper {
	
	@Id
	@Column(name = "consent_transaction_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "seq_patient_consent_mapper")
	private int consentTransactionId;
	
	@Column(name="consent_master_id")
	private Integer consentMasterId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="appointment_id")
	private Integer appointmentId;
	
	@Column(name="template_desc")
	private String templateDesc;
	
	@Column(name = "status")
	private char status;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="document_name")
	private String documentName;
	
	@Column(name="document_path")
	private String documentPath;
	
	@Column(name="video_path")
	private String videoPath;
	
	@Column(name="is_from_consent_print")
	private Boolean isFromConsentPrint;
	
	@Column(name="template_form_url")
	private String templateFormUrl;
	
	@Column(name="conversation_id")
	private String conversationId;
	
	@Column(name="form_template_id")
	private String formTemplateId;

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public int getConsentTransactionId() {
		return consentTransactionId;
	}

	public void setConsentTransactionId(int consentTransactionId) {
		this.consentTransactionId = consentTransactionId;
	}

	public Integer getConsentMasterId() {
		return consentMasterId;
	}

	public void setConsentMasterId(Integer consentMasterId) {
		this.consentMasterId = consentMasterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public String getTemplateDesc() {
		return templateDesc;
	}

	public void setTemplateDesc(String templateDesc) {
		this.templateDesc = templateDesc;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
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

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public String getVideoPath() {
		return videoPath;
	}

	public void setVideoPath(String videoPath) {
		this.videoPath = videoPath;
	}

	public Boolean getIsFromConsentPrint() {
		return isFromConsentPrint;
	}

	public void setIsFromConsentPrint(Boolean isFromConsentPrint) {
		this.isFromConsentPrint = isFromConsentPrint;
	}

	public String getTemplateFormUrl() {
		return templateFormUrl;
	}

	public void setTemplateFormUrl(String templateFormUrl) {
		this.templateFormUrl = templateFormUrl;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public String getFormTemplateId() {
		return formTemplateId;
	}

	public void setFormTemplateId(String formTemplateId) {
		this.formTemplateId = formTemplateId;
	}
	
	
	

}
