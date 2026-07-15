package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;




@Entity
@Table(name = "forms_engine_transaction_details_master" , schema = "public")
@SequenceGenerator(name = "seq_forms_engine_transaction_details_master" , sequenceName = "public.seq_forms_engine_transaction_details_master" , allocationSize = 1)
public class FormsEngineTransactionMaster{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "seq_forms_engine_transaction_details_master")
	@Column(name = "forms_engine_transaction_id")
	private Integer formsEngineTransactionId;
	
	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "template_id")
	private Integer templateId;
	
	@Column(name = "form_template_id")
	private String formTemplateId;
	
	@Column(name = "conversation_id")
	private String conversationId;
	
	@Column(name = "template_type_id")
	private Integer templateTypeId;
	
	@Column(name = "form_url")
	private String  formUrl;
	
	@Column(name = "org_id")
	private Integer orgId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "diagnosis_id")
	private Integer diagnosisId;
	
	@Column(name = "order_details_id")
	private Integer orderDetailsId;
	
	@Column(name = "m_icpc_id")
	private Integer mIcpcId;
	
	@Column(name = "m_icpc_system_id")
	private Integer mIcpcSystemId;
	
	@Column(name = "is_provisional_release")
	private Character isProvisionalRelease;
	
	@Column(name = "is_final_release")
	private Character isFinalRelease;
	
	@Column(name = "is_report_release")
	private Character isReportRelease;
	
	@Column(name = "is_questionary")
	private Character isQuestionary;
	
	@Column(name = "t_sub_speciman_id")
	private Integer tSubSpecimanId;
	
	@Column(name = "report_no")
	private String reportNo;
	
	@Column(name = "staining_id")
	private Integer stainingId;
	
	@Column(name = "lab_sample_dtls_id")
	private Integer labSampleDtlsId;
	
	@Column(name = "template_res_id")
	private Integer templateResId;
	
	@Column(name = "template_transaction_id")
	private Integer templateTransactionId;
	
	@Column(name = "created_by" ,updatable = false)
	private Integer createdBy;
	
	@Column(name = "review_time")
	private Date reviewTime;
	
	@Column(name = "created_date" ,updatable = false)
	@CreationTimestamp
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	@UpdateTimestamp
	private Date updatedDate;
	
	@Column(name = "sample_status_id")
	private Integer sampleStatusId;

	@Column(name = "appointment_id")
	private Integer appointmentId;
	
	@Column(name = "adm_note_id")
	private Integer admNoteId;
	
	
	public Integer getFormsEngineTransactionId() {
		return formsEngineTransactionId;
	}

	public void setFormsEngineTransactionId(Integer formsEngineTransactionId) {
		this.formsEngineTransactionId = formsEngineTransactionId;
	}



	public Integer getTemplateId() {
		return templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	public String getFormTemplateId() {
		return formTemplateId;
	}

	public void setFormTemplateId(String formTemplateId) {
		this.formTemplateId = formTemplateId;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}

	public Integer getTemplateTypeId() {
		return templateTypeId;
	}

	public void setTemplateTypeId(Integer templateTypeId) {
		this.templateTypeId = templateTypeId;
	}

	public String getFormUrl() {
		return formUrl;
	}

	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
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

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status != null ? status :'A';
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getDiagnosisId() {
		return diagnosisId;
	}

	public void setDiagnosisId(Integer diagnosisId) {
		this.diagnosisId = diagnosisId;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getmIcpcId() {
		return mIcpcId;
	}

	public void setmIcpcId(Integer mIcpcId) {
		this.mIcpcId = mIcpcId;
	}

	public Integer getmIcpcSystemId() {
		return mIcpcSystemId;
	}

	public void setmIcpcSystemId(Integer mIcpcSystemId) {
		this.mIcpcSystemId = mIcpcSystemId;
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

	public Character getIsReportRelease() {
		return isReportRelease;
	}

	public void setIsReportRelease(Character isReportRelease) {
		this.isReportRelease = isReportRelease;
	}

	public Character getIsQuestionary() {
		return isQuestionary;
	}

	public void setIsQuestionary(Character isQuestionary) {
		this.isQuestionary = isQuestionary;
	}

	public Integer gettSubSpecimanId() {
		return tSubSpecimanId;
	}

	public void settSubSpecimanId(Integer tSubSpecimanId) {
		this.tSubSpecimanId = tSubSpecimanId;
	}

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

	public Integer getStainingId() {
		return stainingId;
	}

	public void setStainingId(Integer stainingId) {
		this.stainingId = stainingId;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public Integer getTemplateResId() {
		return templateResId;
	}

	public void setTemplateResId(Integer templateResId) {
		this.templateResId = templateResId;
	}

	public Integer getTemplateTransactionId() {
		return templateTransactionId;
	}

	public void setTemplateTransactionId(Integer templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getSampleStatusId() {
		return sampleStatusId;
	}

	public void setSampleStatusId(Integer sampleStatusId) {
		this.sampleStatusId = sampleStatusId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getAdmNoteId() {
		return admNoteId;
	}

	public void setAdmNoteId(Integer admNoteId) {
		this.admNoteId = admNoteId;
	}
	
	
	
}
