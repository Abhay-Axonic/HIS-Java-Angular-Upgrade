package com.param.entity.model.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_abdm_patient_reg_request", schema = "abdm")
@SequenceGenerator(name = "m_abdm_patient_reg_request_seq", sequenceName = "abdm.m_abdm_patient_reg_request_seq", allocationSize = 1)

public class AbdmPatientRegRequest {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_abdm_patient_reg_request_seq")
	
	@Column(name = "abdm_patient_reg_request_id")
	private Integer abdmPatientRegRequestId;

	@Column(name = "hip_id")
	private String hipId;

	@Column(name = "health_id")
	private String healthId;
	
	@Column(name = "health_number")
	private String healthNumber;
	
	@Column(name = "discover_json")
	private String discoverJson;
	
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "mobile_number")
	private String mobileNumber;
	
	@Column(name = "dob")
	private String dob;
	
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "token_number")
	private Integer tokenNumber;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "expired_date")
	private Date expiredDate;

	

	public Integer getAbdmPatientRegRequestId() {
		return abdmPatientRegRequestId;
	}

	public void setAbdmPatientRegRequestId(Integer abdmPatientRegRequestId) {
		this.abdmPatientRegRequestId = abdmPatientRegRequestId;
	}

	public String getHealthId() {
		return healthId;
	}

	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}

	public String getHealthNumber() {
		return healthNumber;
	}

	public void setHealthNumber(String healthNumber) {
		this.healthNumber = healthNumber;
	}

	public String getDiscoverJson() {
		return discoverJson;
	}

	public void setDiscoverJson(String discoverJson) {
		this.discoverJson = discoverJson;
	}

	public String getHipId() {
		return hipId;
	}

	public void setHipId(String hipId) {
		this.hipId = hipId;
	}


	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getTokenNumber() {
		return tokenNumber;
	}

	public void setTokenNumber(Integer tokenNumber) {
		this.tokenNumber = tokenNumber;
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

	public Date getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(Date expiredDate) {
		this.expiredDate = expiredDate;
	}
	
	

}
