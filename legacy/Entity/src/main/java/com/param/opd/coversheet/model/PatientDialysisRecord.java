package com.param.opd.coversheet.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;
import com.param.global.common.InstantConverter;

@NamedNativeQueries({
	@NamedNativeQuery(name = "GET_PATIENT_DIALYSIS_RECORD_DETAILS", 
	query ="SELECT to_char(PatDiaRec.dialysis_start_date,'DD/MM/YYYY') as \"dialysisStartDate\",PatDiaRec.dialysis_event_id as \"dialysisEventId\","
		+ " to_char(PatDiaRec.dialysis_start_time,'HH24:MI') as \"dialysisStartTime\",to_char(PatDiaRec.dialysis_end_time,'HH24:MI') as \"dialysisEndTime\","
		+ " PatDiaRec.dialysis_duration as \"dialysisDuration\",PatDiaRec.dia_net_ultrafication as \"netUltrafication\","
		+ " PatDiaRec.dia_vascular_access_type as \"vascularAccessType\",PatDiaRec.dia_ktv_result as \"ktvResult\","
		+ " PatDiaRec.dialysis_note as \"dialysisNote\",PatDiaRec.document_path as \"documentPath\","
		+ " tempTran.template_object as \"htmlTemplateObject\",tempTran.title as \"templateTitle\" "
		+ " from patient.t_patient_dialysis_record PatDiaRec "
		+ " left join public.t_template_transaction tempTran on tempTran.template_transaction_id=PatDiaRec.template_transaction_id "
		+ " where PatDiaRec.appointment_id=:appointmentId"
		)
})

@Entity
@Table(name="t_patient_dialysis_record",schema="patient")
@SequenceGenerator(name="patient_dialysis_record_seq" , sequenceName="patient.patient_dialysis_record_seq", allocationSize=1)
public class PatientDialysisRecord {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "patient_dialysis_record_seq")
	@Column(name="patient_dialysis_rec_id")
	private Integer patientDialysisRecId;
	
	@Column(name="appointment_id")
	private Integer appointmentId;
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name="dialysis_event_id")
	private Integer dialysisEventId;
	
	@Column(name="dialysis_start_date")
	private Date dialysisStartDate;
	
	@Column(name="dialysis_start_time")
	@Convert(converter = InstantConverter.class)
	private Instant dialysisStartTime;
	
	@Column(name="dialysis_end_time")
	@Convert(converter = InstantConverter.class)
	private Instant dialysisEndTime;
	
	@Column(name = "dialysis_duration")
	private String dialysisDuration;
	
	@Column(name = "dia_vascular_access_type")
	private String dialysisVascularAccessType;
	
	@Column(name = "dia_ktv_result")
	private String dialysisKtvResult;
	
	@Column(name = "dia_net_ultrafication")
	private Integer dialysisNetUltrafication;
	
	@Column(name = "dialysis_note")
	private String dialysisNote;
	
	@Column(name = "document_path")
	private String documentPath;
	
	@Column(name = "template_transaction_id")
	private Integer templateTransactionId;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "status")
	private Character status;

	public Integer getPatientDialysisRecId() {
		return patientDialysisRecId;
	}

	public void setPatientDialysisRecId(Integer patientDialysisRecId) {
		this.patientDialysisRecId = patientDialysisRecId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Date getDialysisStartDate() {
		return dialysisStartDate;
	}

	public void setDialysisStartDate(Date dialysisStartDate) {
		this.dialysisStartDate = dialysisStartDate;
	}


	public String getDialysisDuration() {
		return dialysisDuration;
	}

	public void setDialysisDuration(String dialysisDuration) {
		this.dialysisDuration = dialysisDuration;
	}

	public String getDialysisVascularAccessType() {
		return dialysisVascularAccessType;
	}

	public void setDialysisVascularAccessType(String dialysisVascularAccessType) {
		this.dialysisVascularAccessType = dialysisVascularAccessType;
	}

	public String getDialysisKtvResult() {
		return dialysisKtvResult;
	}

	public void setDialysisKtvResult(String dialysisKtvResult) {
		this.dialysisKtvResult = dialysisKtvResult;
	}

	public Integer getDialysisNetUltrafication() {
		return dialysisNetUltrafication;
	}

	public void setDialysisNetUltrafication(Integer dialysisNetUltrafication) {
		this.dialysisNetUltrafication = dialysisNetUltrafication;
	}

	public String getDialysisNote() {
		return dialysisNote;
	}

	public void setDialysisNote(String dialysisNote) {
		this.dialysisNote = dialysisNote;
	}

	public String getDocumentPath() {
		return documentPath;
	}

	public void setDocumentPath(String documentPath) {
		this.documentPath = documentPath;
	}

	public Integer getTemplateTransactionId() {
		return templateTransactionId;
	}

	public void setTemplateTransactionId(Integer templateTransactionId) {
		this.templateTransactionId = templateTransactionId;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getDialysisEventId() {
		return dialysisEventId;
	}

	public void setDialysisEventId(Integer dialysisEventId) {
		this.dialysisEventId = dialysisEventId;
	}

	public Instant getDialysisStartTime() {
		return dialysisStartTime;
	}

	public void setDialysisStartTime(Instant dialysisStartTime) {
		this.dialysisStartTime = dialysisStartTime;
	}

	public Instant getDialysisEndTime() {
		return dialysisEndTime;
	}

	public void setDialysisEndTime(Instant dialysisEndTime) {
		this.dialysisEndTime = dialysisEndTime;
	}

	
}
