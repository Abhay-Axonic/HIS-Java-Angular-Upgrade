package com.param.billing.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@Table(name = "m_trust_visit_type", schema = "public")
@SequenceGenerator(name = "m_trust_visit_type_seq", sequenceName = "public.m_trust_visit_type_seq", allocationSize = 1)

public class TrustVisitTypeMaster {
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_trust_visit_type_seq")
	@Column(name = "trust_visit_id")
	private Integer trustVisitId;
	
	@Column(name="visit_type_id")
	private Integer visitTypeId;
	
	@Column(name="trust_id")
	private Integer trustId;

	@Column(name="visit_type")
	private String visitType;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private char status;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="org_id")
	private Integer orgId;
	
	/*@ManyToOne(fetch= FetchType.LAZY)
	@JoinColumn(name="trust_id", insertable=false, updatable=false)
	private TrustMaster trustMaster;*/

	public Integer getTrustVisitId() {
		return trustVisitId;
	}

	public void setTrustVisitId(Integer trustVisitId) {
		this.trustVisitId = trustVisitId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getTrustId() {
		return trustId;
	}

	public void setTrustId(Integer trustId) {
		this.trustId = trustId;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}
}
