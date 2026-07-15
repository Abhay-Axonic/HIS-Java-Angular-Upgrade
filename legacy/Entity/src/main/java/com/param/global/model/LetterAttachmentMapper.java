package com.param.global.model;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.base.CommonEntity;
@Entity
@Table(name = "letter_attachment_mapper" , schema = "patient")
@SequenceGenerator(name = "letter_attachment_mapper_seq" , sequenceName = "patient.letter_attachment_mapper_seq" , allocationSize = 1)
public class LetterAttachmentMapper{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "letter_attachment_mapper_seq")
	@Column(name = "letter_attachment_mapper_id")
	private int letterAttachmentMapperId;
	
	@Column(name = "appointment_id")
	private Integer appointmentId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
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

	@Column(name = "letter_id")
	private Integer letterId;
	
	@Column(name="attachment_file_path")
	private String attachmentFilePath;
	
	@Column(name="status")
	private Character status;
	
	@Column(name="ai_letter")
	private String aiLetter;

	@Column(name = "tab_id")
	private Integer tabId;
	
	public Integer getTabId() {
		return tabId;
	}

	public void setTabId(Integer tabId) {
		this.tabId = tabId;
	}

	public int getLetterAttachmentMapperId() {
		return letterAttachmentMapperId;
	}

	public void setLetterAttachmentMapperId(int letterAttachmentMapperId) {
		this.letterAttachmentMapperId = letterAttachmentMapperId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Integer getLetterId() {
		return letterId;
	}

	public void setLetterId(Integer letterId) {
		this.letterId = letterId;
	}

	public String getAttachmentFilePath() {
		return attachmentFilePath;
	}

	public void setAttachmentFilePath(String attachmentFilePath) {
		this.attachmentFilePath = attachmentFilePath;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getAiLetter() {
		return aiLetter;
	}

	public void setAiLetter(String aiLetter) {
		this.aiLetter = aiLetter;
	}

	
	

	
}

