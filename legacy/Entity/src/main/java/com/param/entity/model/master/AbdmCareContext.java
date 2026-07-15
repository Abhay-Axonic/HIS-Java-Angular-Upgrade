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
@Table(name = "m_abdm_care_context", schema = "abdm")
@SequenceGenerator(name = "m_abdm_care_context_seq", sequenceName = "abdm.m_abdm_care_context_seq", allocationSize = 1)

public class AbdmCareContext {
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_abdm_care_context_seq")
	
	@Column(name = "id")
	private Integer id;

	@Column(name = "encounter_id")
	private Integer encounterId;

	@Column(name = "admission_id")
	private Integer admissionId;
	
	@Column(name = "hitype")
	private String hitype;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "linked_by")
	private String linkedBy;

	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "health_id")
	private String healthId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public String getHitype() {
		return hitype;
	}

	public void setHitype(String hitype) {
		this.hitype = hitype;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLinkedBy() {
		return linkedBy;
	}

	public void setLinkedBy(String linkedBy) {
		this.linkedBy = linkedBy;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public String getHealthId() {
		return healthId;
	}

	public void setHealthId(String healthId) {
		this.healthId = healthId;
	}
	
	

}
