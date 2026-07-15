package com.param.opd.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "t_quick_notes_mapper", schema = "opd")
@SequenceGenerator(name = "t_quick_notes_mapper_seq", sequenceName = "opd.t_quick_notes_mapper_seq", allocationSize = 1)
public class QuickNotesMapper {

		@Id
		@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_quick_notes_mapper_seq")
		
		@Column(name = "quick_notes_mapper_id")
		private Integer quickNotesMapperId;
		
		@Column(name = "organization_id")
		private Integer organizationId;
		
		@Column(name = "unit_id")
		private Integer unitId;
		
		@Column(name = "encounter_id")
		private Integer encounterId;
		
		@Column(name = "doctor_id")
		private Integer doctorId;
		
		@Column(name = "patient_id")
		private Integer patientId;
		
		@Column(name = "updated_by")
		private Integer updatedBy;
		
		@Column(name = "updated_date")
		private Date updatedDate;
		
		@Column(name = "created_by")
		private Integer createdBy;
		
		@Column(name = "created_date")
		private Date createdDate;
		
		@Column(name = "admission_id")
		private Integer admissionId;
		
		@Column(name = "status")
		private Character status;
		
		@Column(name = "quick_notes_desc")
		private String quickNotesDesc;
		
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

		public Integer getEncounterId() {
			return encounterId;
		}

		public void setEncounterId(Integer encounterId) {
			this.encounterId = encounterId;
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

		public Integer getUpdatedBy() {
			return updatedBy;
		}

		public void setUpdatedBy(Integer updatedBy) {
			this.updatedBy = updatedBy;
		}

		public Integer getCreatedBy() {
			return createdBy;
		}

		public void setCreatedBy(Integer createdBy) {
			this.createdBy = createdBy;
		}

		public Integer getAdmissionId() {
			return admissionId;
		}

		public void setAdmissionId(Integer admissionId) {
			this.admissionId = admissionId;
		}

		public Character getStatus() {
			return status;
		}

		public void setStatus(Character status) {
			this.status = status;
		}

		public Date getUpdatedDate() {
			return updatedDate;
		}

		public void setUpdatedDate(Date updatedDate) {
			this.updatedDate = updatedDate;
		}

		public Date getCreatedDate() {
			return createdDate;
		}

		public void setCreatedDate(Date createdDate) {
			this.createdDate = createdDate;
		}

		public Integer getQuickNotesMapperId() {
			return quickNotesMapperId;
		}

		public void setQuickNotesMapperId(Integer quickNotesMapperId) {
			this.quickNotesMapperId = quickNotesMapperId;
		}

		public String getQuickNotesDesc() {
			return quickNotesDesc;
		}

		public void setQuickNotesDesc(String quickNotesDesc) {
			this.quickNotesDesc = quickNotesDesc;
		}

}
