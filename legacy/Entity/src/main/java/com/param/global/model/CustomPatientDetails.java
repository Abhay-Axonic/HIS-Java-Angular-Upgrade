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
@Table(name = "t_custom_patient_details_external", schema = "public")
@SequenceGenerator(name = "t_custom_patient_details_external_seq", sequenceName = "public.t_custom_patient_details_external_seq", allocationSize = 1)
public class CustomPatientDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_custom_patient_details_external_seq")
	@Column(name = "id")
	private Integer id;

	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "encounter_id")
	private Integer encounterId;
	
	@Column(name = "appointment_id")
	private Integer appointmentId;
	 
	@Column(name = "custom_data")
	private String  customData;
	
	@Column(name = "request_payload")
	private String requestPayload;
	
	@Column(name = "posted_at")
	private Date postedAt;
	
	@Column(name = "response_status")
	private String responseStatus;
	
	@Column(name = "response_msg")
	private String responseMsg;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;

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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAppointmentId() {
		return appointmentId;
	}

	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}

	public Date getPostedAt() {
		return postedAt;
	}

	public void setPostedAt(Date postedAt) {
		this.postedAt = postedAt;
	}

	public String getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(String responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getResponseMsg() {
		return responseMsg;
	}

	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
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

	public String getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}

	public String getCustomData() {
		return customData;
	}

	public void setCustomData(String customData) {
		this.customData = customData;
	}
	
}
