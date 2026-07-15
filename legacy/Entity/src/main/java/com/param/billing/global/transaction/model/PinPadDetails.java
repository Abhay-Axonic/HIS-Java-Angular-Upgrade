package com.param.billing.global.transaction.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_pinpad_transection_details", schema="billing")
@SequenceGenerator(name="pinpad_transection_details_seq", sequenceName="billing.pinpad_transection_details_seq", allocationSize=1)
public class PinPadDetails {

	@Id
	@Column(name = "pinpad_transection_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pinpad_transection_details_seq")
	private Integer pinpadTransectionId;
	
	
	@Column(name="patient_id")
	private Integer patientId;
	
	@Column(name = "orgnization_id")
	private Integer orgnizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	  
	@Column(name = "till_id")
	private Integer tillId;
	
	@Column(name = "response")
	private String response;
	
	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	public Integer getPinpadTransectionId() {
		return pinpadTransectionId;
	}

	public void setPinpadTransectionId(Integer pinpadTransectionId) {
		this.pinpadTransectionId = pinpadTransectionId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getOrgnizationId() {
		return orgnizationId;
	}

	public void setOrgnizationId(Integer orgnizationId) {
		this.orgnizationId = orgnizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getTillId() {
		return tillId;
	}

	public void setTillId(Integer tillId) {
		this.tillId = tillId;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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
	
	
	
	
}
